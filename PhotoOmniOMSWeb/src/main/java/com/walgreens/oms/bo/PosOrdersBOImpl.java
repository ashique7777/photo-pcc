package com.walgreens.oms.bo;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.oms.bean.OrderBean;
import com.walgreens.oms.factory.OmsDAOFactory;
import com.walgreens.oms.json.bean.PosList;
import com.walgreens.oms.json.bean.PosOrderRequest;
import com.walgreens.oms.json.bean.PosOrderResponse;
import com.walgreens.oms.utility.PosOrderQuery;
import com.walgreens.oms.utility.ServiceUtil;

/**
 * 
 * @author CTS Class used to process POS realtime order request
 * 
 */

@Component("PosOrdersBO")
@Service
public class PosOrdersBOImpl implements PosOrdersBO {
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private OmsDAOFactory omsDAOFactory;
	
	@Autowired
	private PromotionalMoneyBO promoBo; /** Use to call the PM API */
	
	@Autowired
	private MBPromotionalMoneyBO mbpmBo; /** Use to call the MBPM API */
	
	@Autowired
	private OrderBOImpl orderBOImpl; /** use to call cost calculation */
	
	private PosOrderQuery posOrderQuery = new PosOrderQuery();
	SimpleDateFormat formatter = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PosOrdersBOImpl.class);

	/**
	 * This method is use for pos order submission at Central
	 * 
	 * @param posJsonRequestMsg
	 * @return PosOrderResponse
	 
	 */
	public PosOrderResponse submittPOSOrder(PosOrderRequest posOrderRequest)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submittPOSOrder() method of PosOrdersBOImpl ");
		}	

		PosOrderResponse posOrderResponse = new PosOrderResponse();
		try {
            boolean reconFlag = false; 
			boolean errorFlag = false;
					
			for (int i = 0; i < posOrderRequest.getPosList().size(); i++) {

				PosList tempPosList = new PosList();
				ErrorDetails errorDetails = new ErrorDetails();
				try {
					
					/** Added for Phase 2.0 for making the sold amount negative for MV,MR and SR type of POS transaction*/
					if (posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)||
					    posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)||
						posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)) {

						double posSoldAmt = ((-1) * CommonUtil.doubleValueFromString(posOrderRequest.getPosList().get(i).getPosDetails().getPosSoldAmount()));
						posOrderRequest.getPosList().get(i).getPosDetails().setPosSoldAmount(String.valueOf(posSoldAmt));
					}else{
						double posSoldAmt = (CommonUtil.doubleValueFromString(posOrderRequest.getPosList().get(i).getPosDetails().getPosSoldAmount()));
						posOrderRequest.getPosList().get(i).getPosDetails().setPosSoldAmount(String.valueOf(posSoldAmt));
					}
					
					/** get sysOrderId from OM_ORDER_ATTRIBUTE,OM_ORDER table with EnvelopeNbr for the orders to be considered for POS transactions*/	
					OrderBean orderBean = new OrderBean();
					try{
						 orderBean = omsDAOFactory.getPosOrdersDAO().getOrderId(posOrderRequest.getPosList().get(i).getPosDetails().getEnvelopeNbr(),
							                posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnTypeDTTM(),
							                posOrderRequest.getPosList().get(i).getPosDetails().getLocationNumber());										     	
					   }catch(Exception e){
						     if (LOGGER.isErrorEnabled()) {LOGGER.error("Order not found For the Pos Resquest");}
					    }																				
					
					/**invoke cost calculation method for POS real time*/
					/**Check condition for cost calculation method invocation*/
					if(!CommonUtil.isNull(orderBean) && 
							!CommonUtil.isNull(orderBean.getSysOrderID()) &&
							!CommonUtil.isNull(orderBean.getOrderPlacedDTTM()) ){
					String costCalcStatusCdQuery = posOrderQuery.getCostCalStatusCdQuery();
					String costCalcStatusCd = jdbcTemplate.queryForObject(costCalcStatusCdQuery,new Object[]{orderBean.getSysOrderID(),
							formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString()))},String.class);
					if(!CommonUtil.isNull(costCalcStatusCd)){
					if( !costCalcStatusCd.equalsIgnoreCase(PhotoOmniConstants.COST_CALCULATION_STATUS_CD)) {
							if (posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)||
								posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)||
							    posOrderRequest.getPosList().get(i).getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
						            try {
							              orderBOImpl.calculateOrderCost(orderBean.getSysOrderID(), 
									      formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())));
						                       } catch (Exception e) {
							                           if(LOGGER.isErrorEnabled()){LOGGER.error("Error occoured in orderBOImpl.calculateOrderCost()");}
						                               }							
					                     }
					            }
					       }
					 }
					
					/** process POS generic logics*/					
					tempPosList = omsDAOFactory.getPosOrdersDAO().processPosRequest(posOrderRequest.getPosList().get(i),reconFlag,orderBean);	
					
					/**call PM & MBPM for POS real time*/
					//Prod - bug fix for phase 1.0 starts
					if(!CommonUtil.isNull(orderBean) && !CommonUtil.isNull(orderBean.getSysOrderID())){
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String orderDate = formatter.format(orderBean.getOrderPlacedDTTM());
						long shoppingCartId = omsDAOFactory.getPosOrdersDAO().getShoppingCartId(orderBean.getSysOrderID(), orderDate);
						promoBo.calculatePromotionalMoney(orderBean.getSysOrderID(), orderDate);
						mbpmBo.calculateMBPromotionalMoney(shoppingCartId, orderDate);
					}
					//Prod - bug fix for phase 1.0ends
					
				} catch (Exception e) {
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error(" Exception occoured at PosOrdersBOImpl - ", e);
					}
					errorDetails = com.walgreens.common.utility.CommonUtil.createFailureMessageForDBException(e);
					tempPosList.setErrorDetails(errorDetails);
					if(!CommonUtil.isNull(tempPosList.getErrorDetails().getDescription()) && 
							tempPosList.getErrorDetails().getDescription().
							equalsIgnoreCase("Order details already present for the POS message received")){
						tempPosList.setStatus(true);
						errorFlag = false;
					}else{
					tempPosList.setStatus(false);
					errorFlag = true;
					}

				} finally {
					/**
					 * creates exception for POS Return message if insert fails
					 */
					if (errorFlag) {
						posOrderRequest.getPosList().get(i).setErrorDetails(tempPosList.getErrorDetails());
						posOrderRequest.getPosList().get(i).setPosDetails(posOrderRequest.getPosList().get(i).getPosDetails());
						posOrderRequest.getPosList().get(i).setStatus(tempPosList.getStatus());
					} else {
						tempPosList.setErrorDetails(ServiceUtil.createSuccessMessage());
						tempPosList.setStatus(true);
						posOrderRequest.getPosList().get(i).setErrorDetails(tempPosList.getErrorDetails());
						posOrderRequest.getPosList().get(i).setPosDetails(posOrderRequest.getPosList().get(i).getPosDetails());
						posOrderRequest.getPosList().get(i).setStatus(tempPosList.getStatus());
					}
				}

			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at submittPOSOrder method() of PosOrdersBOImpl - ", e);
			}
			throw new PhotoOmniException("Exception occoured in submittPOSOrder in PosOrdersBOImpl "+ e.getMessage());
		} finally {
			posOrderResponse.setMessageHeader(posOrderRequest.getMessageHeader());
			
			//Prod - bug fix for phase 1.0 starts
            for(PosList posList:posOrderRequest.getPosList()){
                  posList.getPosDetails().setLocationNumber(null);
                  posList.getPosDetails().setPosBusinessDate(null);
                  posList.getPosDetails().setPosRegisterNbr(null);
                  posList.getPosDetails().setPrintsReturned(null);
                  posList.getPosDetails().setPosLedgerNbr(null);
                  posList.getPosDetails().setEmployeeId(null);
                  posList.getPosDetails().setDiscountProcessInd(null);
                  posList.getPosDetails().setPosDiscountUsedInd(null);
            }
            //Prod - bug fix for phase 1.0 ends
            
			posOrderResponse.setPosList(posOrderRequest.getPosList());
		}		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting  submittPOSOrder method of PosOrdersBOImpl ");
		}		
		return posOrderResponse;
	}

}
