package com.walgreens.batch.central.writer;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.bean.PosTransactionDetails;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.OrderBean;
import com.walgreens.oms.bo.MBPromotionalMoneyBO;
import com.walgreens.oms.bo.OrderBO;
import com.walgreens.oms.bo.PromotionalMoneyBO;
import com.walgreens.oms.dao.PosOrdersDAO;
import com.walgreens.oms.json.bean.PosDetails;
import com.walgreens.oms.json.bean.PosList;
import com.walgreens.oms.utility.PosOrderQuery;

/** 
 * @author CTS
 * This class writes reconciliation records to DB after getting records from reader.
 * 
 */
@Component("posReconciliationWriter")
public class PosReconciliationWriter implements
		ItemWriter<PosTransactionDetails> {

	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(PosReconciliationWriter.class);
	
	@Autowired
	private PromotionalMoneyBO promoBo; /**Use to call the PM API*/
	
	@Autowired
	private MBPromotionalMoneyBO mbpmBo; /**Use to call the MBPM API */
	
	@Autowired
	private OrderBO orderBO; /**Use to call cost calculation */
	
	@Autowired
	private PosOrdersDAO posOrdersDAO; /**Use to call PhotoOmniOMSWeb PosOrdersDAO Class*/
	
	private PosOrderQuery posOrderQuery = new PosOrderQuery();
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	boolean reconFlag = true;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void write(List<? extends PosTransactionDetails> items)throws Exception,PhotoOmniException {
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering PosReconciliationWriter write()");	
		}
			
		for (PosTransactionDetails posTransactionDetails : items) {	
			
			PosList posList = new PosList();
			PosDetails posDetails = new PosDetails();
			
			posDetails.setEmployeeId(posTransactionDetails.getEmployeeId());
			posDetails.setEnvelopeNbr(String.valueOf(posTransactionDetails.getEnvelopNo()));
			posDetails.setLocationNumber(String.valueOf(posTransactionDetails.getLocationNbr()));
			posDetails.setPosBusinessDate(posTransactionDetails.getBusinessDate());
			posDetails.setPosDiscountUsedInd(posTransactionDetails.getDiscountUsedInd());
			posDetails.setPosLedgerNbr(String.valueOf(posTransactionDetails.getPosLedgerNo()));
			posDetails.setPosRegisterNbr(posTransactionDetails.getRegisterNo());
			posDetails.setPosSequenceNbr(posTransactionDetails.getPosSequenceNo());
			posDetails.setPosSoldAmount(String.valueOf(posTransactionDetails.getSoldAmt()));
			posDetails.setPosTrnType(posTransactionDetails.getTransactionTypeCd());
			posDetails.setPosTrnTypeDTTM(posTransactionDetails.getTransactionDttm());
			posDetails.setPrintsReturned(String.valueOf(posTransactionDetails.getReturnedQty()));
			
			posList.setPosDetails(posDetails);
			
			/** get sysOrderId from OM_ORDER_ATTRIBUTE,OM_ORDER with EnvelopeNbr */	
			OrderBean orderBean = new OrderBean();
			try{
				 orderBean = posOrdersDAO.getOrderId(posList.getPosDetails().getEnvelopeNbr(),
						 posList.getPosDetails().getPosTrnTypeDTTM(),posList.getPosDetails().getLocationNumber());								     				   
			}catch(Exception e){
				  if (LOGGER.isErrorEnabled()) {
					  LOGGER.error("Order Details not found For the Pos transaction details with envelop no: "
				                   +String.valueOf(posTransactionDetails.getEnvelopNo()));}
			     }
			
			
			
			/**invoke cost calculation method From PhotoOmniOMSWeb*/
			/**Check condition for cost calculation method invocation*/
			if(!CommonUtil.isNull(orderBean) && 
					!CommonUtil.isNull(orderBean.getSysOrderID()) &&
					!CommonUtil.isNull(orderBean.getOrderPlacedDTTM()) ){
			String costCalcStatusCdQuery = posOrderQuery.getCostCalStatusCdQuery();
			String costCalcStatusCd = jdbcTemplate.queryForObject(costCalcStatusCdQuery,new Object[]{orderBean.getSysOrderID(),
					formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString()))},String.class);
			if(!CommonUtil.isNull(costCalcStatusCd)){
			if( !costCalcStatusCd.equalsIgnoreCase(PhotoOmniConstants.COST_CALCULATION_STATUS_CD)) {			      
				if (posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)||
					posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)||
					posList.getPosDetails().getPosTrnType().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
				        try {
					           orderBO.calculateOrderCost(orderBean.getSysOrderID(), 
							   formatter.format(formatter.parse(orderBean.getOrderPlacedDTTM().toString())));
				                 } catch (Exception e) {
					                     if(LOGGER.isErrorEnabled()){LOGGER.error("Error occoured in orderBOImpl.calculateOrderCost()");}
				                        }			       
			                }
			           }
			      }
			 }
			
			/** process POS generic logics From PhotoOmniOMSWeb*/
			posOrdersDAO.processPosRequest(posList,reconFlag,orderBean);
			
			/**call PM & MBPM methods From PhotoOmniOMSWeb*/
			if(!CommonUtil.isNull(orderBean) && 
					!CommonUtil.isNull(orderBean.getSysOrderID()) &&
					!CommonUtil.isNull(orderBean.getOrderPlacedDTTM()) ){
			SimpleDateFormat formatter = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
			String orderDate = formatter.format(orderBean.getOrderPlacedDTTM());
			long shoppingCartId = posOrdersDAO.getShoppingCartId(orderBean.getSysOrderID(), orderDate);
			try {
				promoBo.calculatePromotionalMoney(orderBean.getSysOrderID(), orderDate);
				mbpmBo.calculateMBPromotionalMoney(shoppingCartId, orderDate);
			} catch (Exception e) {
				if(LOGGER.isErrorEnabled()){LOGGER.error("Error occoured in PM and MBPM calculation");
			          }
			      }
			 }		
		 }
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Exiting PosReconciliationWriter write()");
		}
	}
	
}
