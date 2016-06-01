package com.walgreens.batch.central.reader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.LicenceContentPosBean;
import com.walgreens.batch.central.bean.OrderHistoryBean;
import com.walgreens.batch.central.bean.OrderLineBean;
import com.walgreens.batch.central.bean.OrderLineTemplateBean;
import com.walgreens.batch.central.bean.POSOrder;
import com.walgreens.batch.central.bean.PosCostCalCulationBean;
import com.walgreens.batch.central.bean.PosReconciliationTransferBean;
import com.walgreens.batch.central.bean.PosTransactionDetails;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.LicenceContentPosBeanRowMapper;
import com.walgreens.batch.central.rowmapper.OrderLineBeanRowMapper;
import com.walgreens.batch.central.rowmapper.OrderLineTemplateBeanRowMapper;
import com.walgreens.batch.central.rowmapper.PosOrderRowMapper;
import com.walgreens.batch.central.rowmapper.PosTranscationRowMapperAllStore;
import com.walgreens.batch.central.utility.PosQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bo.MBPromotionalMoneyBO;
import com.walgreens.oms.bo.PromotionalMoneyBO;
import com.walgreens.oms.dao.PosOrdersDAO;

/**
 * @author CTS
 * This batch is used to process orders having NULL orderIds from OM_POS_TRANSACTION TABLE
 * with the POS real time processing scenarios.
 */

@Component("posReconciliationReader")
@Scope("pos_reconciliation_step1")
public class PosReconciliationReader implements
		ItemReader<PosReconciliationTransferBean> {
	
	@Autowired
	private PromotionalMoneyBO promoBo; // Use to call the PM API
	
	@Autowired
	private MBPromotionalMoneyBO mbpmBo; // Use to call the MBPM API
	
	@Autowired
	private PosOrdersDAO posOrdersDAO; // Use to call updatePos realtime DAO  
	

	/** FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED */
	private boolean queryFlag = false;

	/**
	 * COUNTER USED TO READ & CALCULATE AN PosTransactionDetails & SEND TO PROCESSOR */
	private int counter = 0;

	/** FIELD USED TO INDICATE STARTING ROWNUM FOR PAGINATION */
	private int pageBegin = 1;

	/** FIELD USED TO CONFIGURE PAGINATION */
	private int paginationCounter = 500;

	private JdbcTemplate jdbcTemplate;
		
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	private Long jobSubmitTime;

	public Long getJobSubmitTime() {
		return jobSubmitTime;
	}

	public void setJobSubmitTime(Long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(PosReconciliationReader.class);
	private PosQuery posQuery;


	@SuppressWarnings("unused")
	@Override
	public PosReconciliationTransferBean read() throws Exception,PhotoOmniException,
			UnexpectedInputException, ParseException,NonTransientResourceException {

 		if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Entering PosReconciliationReader read()");
		}

		boolean updSoldAmtStatus = false;
		
		posQuery = new PosQuery();		
		String blankOrderIdPosDetailsQuery = posQuery.getBlankOrderIdPosDetailsQuery();
		String getOrderIdQuery = posQuery.getOrderIdFromOrderAttribute();
		String posOrderDetailsQuery = posQuery.posOrderDetailsQuery();
		String getCalenderIdQuery = posQuery.getCalenderIdQuery();
		String decodeQuery = posQuery.getDecodeQuery();
		String sysExceptionIdQuery = posQuery.getSysExceptionIdQuery();
		
		List<PosTransactionDetails> posTransactionDetailsList = new ArrayList<PosTransactionDetails>();
		List<PosReconciliationTransferBean> posReconciliationTransferBeansList = new ArrayList<PosReconciliationTransferBean>();
		PosReconciliationTransferBean posReconciliationTransferBeanTempOne = new PosReconciliationTransferBean();


		try {			
			 
			
			if (!queryFlag) {

				/** get all the POS transaction details having blank Order id from OM_POS_TRANSACTION*/				
				posTransactionDetailsList = jdbcTemplate.query(blankOrderIdPosDetailsQuery, new Object[] { pageBegin,
								(pageBegin + paginationCounter - 1) },new PosTranscationRowMapperAllStore());

				if (!posTransactionDetailsList.isEmpty() && posTransactionDetailsList.size() > 0) {

					for (int i = 0; i < posTransactionDetailsList.size(); i++) {

						/** create instance for beans */
						PosReconciliationTransferBean posReconciliationTransferBean = new PosReconciliationTransferBean();
						OrderHistoryBean orderHistoryBean = new OrderHistoryBean();

						/**Logic to get (posTranDttm-195) date*/
						long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
						DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");	
						DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");					        
						Date posTranDttm = dateFormat2.parse(posTransactionDetailsList.get(i).getTransactionDttm());
				        //Prod - bug fix starts
						//Date oneNintyFiveDayBefore = new Date(posTranDttm.getTime() - (195*MILLIS_IN_DAY));
				        //String formattedOneNintyFiveDayBefore = dateFormat.format(oneNintyFiveDayBefore);
						Date threeSixtyFiveDayBefore = new Date(posTranDttm.getTime() - (365*MILLIS_IN_DAY));
				        String formattedThreeSixtyFiveDayBefore = dateFormat2.format(threeSixtyFiveDayBefore);
						
				        
				        /** get orderId from OM_ORDER_ATTRIBUTE with ENVELOPE_NUMBER */
						List<Long> sysOrderIdList = jdbcTemplate.queryForList(getOrderIdQuery,new Object[] { 
								posTransactionDetailsList.get(i).getSysLocationId(),
								formattedThreeSixtyFiveDayBefore,
								posTransactionDetailsList.get(i).getEnvelopNo()},
								Long.class);
						
						//Prod - bug fix ends
						
						/**if sysOrderId found then only proceed further*/
						if(!sysOrderIdList.isEmpty() && sysOrderIdList.get(0) != null){
						

						/** set orderId for the particular transaction */
						posTransactionDetailsList.get(i).setSysOrderId(sysOrderIdList.get(0));

						/** add posTransactionDetails to  posReconciliationTransferBean*/
						posReconciliationTransferBean.setPosTransactionDetails(posTransactionDetailsList.get(i));

						/*** Apply and update sold amount with proportionating logic*/
                        PosCostCalCulationBean posCostCalCulationBean = new PosCostCalCulationBean();
						posCostCalCulationBean = calculateSoldAmount(posTransactionDetailsList.get(i).getSoldAmt(),
								sysOrderIdList.get(0), posTransactionDetailsList.get(i).getTransactionTypeCd(),posCostCalCulationBean);
						
						posReconciliationTransferBean.setPosCostCalCulationBean(posCostCalCulationBean);
						
						/**update ORDER_SOLD_DTTM in OM_ORDER*/
						posReconciliationTransferBean.setOrderSoldDttm(posTransactionDetailsList.get(i).getTransactionDttm());

						/** query OM_ORDER with ORDER_ID and gets all details for order */												
						List<POSOrder> posOrderList = jdbcTemplate.query(posOrderDetailsQuery,new Object[] { sysOrderIdList.get(0) },new PosOrderRowMapper());
						
						/**Proced further if data found in OM_ORDER table*/
						if(!posOrderList.isEmpty() && posOrderList.get(0) != null){
						
						/** include sysOrderId,OwingLocId,OrderPlaceDttm in posReconciliationTransferBean*/
						posReconciliationTransferBean.setSysOrderId(sysOrderIdList.get(0));
						posReconciliationTransferBean.setOwingLocId(posOrderList.get(0).getOwningLocId());
						posReconciliationTransferBean.setOrderPlaceDttm(posOrderList.get(0).getOrderPlacedDttm());

						/** get soldAmount and Order Status */
						double updSoldAmt = posOrderList.get(0).getSoldAmount();
						String orderStatus = posOrderList.get(0).getStatus();
						String finalOrderStatus = PhotoOmniConstants.BLANK;
						String posTransactionType = posTransactionDetailsList.get(i).getTransactionTypeCd();
						
						if(updSoldAmt > 0){
							
							finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
							posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
							
						}
						if (updSoldAmt <= 0) {
							
							/** update order status for earlier order status PROC */
							if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
								posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
								
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
								
								if (null != posOrderList.get(0).getOrderCompletedDttm()) {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																	
								} else {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
								    posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																	
								}
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_PROC)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_CANCELLED;
								posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);							
								
							}
							
							/** update order status for earlier order status DONE */
							else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {
								
								if (updSoldAmt == 0) {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
									
								}
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
								
								if (null != posOrderList.get(0).getOrderCompletedDttm()) {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																		
								} else {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																	
								}
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_CANCELLED;							
								posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
															
							}
							
							/** update order status for earlier order status SOLD*/
							else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)) {
								
								if (updSoldAmt == 0) {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_SOLD;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																		
								}
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) {
								
								if (null != posOrderList.get(0).getOrderCompletedDttm()) {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
									posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);									
									
								} else {
									
									finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
								    posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																		
								}
							} else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_CANCELLED;
								posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
																
							}
							
							/** update order status for earlier order status CANCELLED */
							else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_CANCELLED)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_CANCELLED;
								posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);
															
							}
							
							/** update order status for earlier order status COMPLETE */
							else if (orderStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_COMPLETE)
									&& posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MS)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MA)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_SR)
									|| posTransactionType.equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MR)) {
								
								finalOrderStatus = PhotoOmniConstants.ORDER_STATUS_COMPLETE;
						        posReconciliationTransferBean.setFinalOrderStatus(finalOrderStatus);							
							}
														
						}

						/** Update OM_ORDER_ATTRIBUTE table with calendar id of  the day the POS transaction took place */
												
						String tempDate = posTransactionDetailsList.get(i).getTransactionDttm();
						List<Integer> calenderIdList = jdbcTemplate.queryForList(getCalenderIdQuery,new Object[] { tempDate },Integer.class);
						if(!calenderIdList.isEmpty() && calenderIdList.get(0) != null){
						posReconciliationTransferBean.setCalenderId(calenderIdList.get(0));	
						}

						/** update the PRINTS_RETURNED_QTY in OM_ORDER_ATTRIBUTE */
						int printsReturnedQty = 0;
						if (posTransactionDetailsList.get(i).getTransactionTypeCd().equalsIgnoreCase(PhotoOmniConstants.POS_TRANSACTIO_TYPE_MV)) 
						{
							printsReturnedQty = (posOrderList.get(0).getPrintsReturned() - posTransactionDetailsList.get(i).getReturnedQty());

						} else {
							printsReturnedQty = (posOrderList.get(0).getPrintsReturned() + posTransactionDetailsList.get(i).getReturnedQty());
						}
						posReconciliationTransferBean.setPrintsReturnedQty(printsReturnedQty);
						

						/** update order history table */
						String action = posTransactionDetailsList.get(i).getTransactionTypeCd();						
						List<String> actionNotesList = jdbcTemplate.queryForList(decodeQuery, new Object[] {
						                               posTransactionDetailsList.get(i).getTransactionTypeCd().trim()},String.class);
												
						List<Long> sysExceptionIdList = jdbcTemplate.queryForList(sysExceptionIdQuery,new Object[] {sysOrderIdList.get(0)},Long.class);						
						
						orderHistoryBean.setAction(action);
						orderHistoryBean.setActionDttm(posTransactionDetailsList.get(i).getTransactionDttm().toString());
						if(actionNotesList.isEmpty() || actionNotesList.get(0) == null){
							      orderHistoryBean.setActionNotes(PhotoOmniConstants.BLANK);
							}else{
								  orderHistoryBean.setActionNotes(actionNotesList.get(0));
							}
						if(sysExceptionIdList.isEmpty() || sysExceptionIdList.get(0) == null){
							   orderHistoryBean.setExceptionId(0);
						    }else{
						    	  orderHistoryBean.setExceptionId(sysExceptionIdList.get(0));
						    }						
						orderHistoryBean.setOrderId(sysOrderIdList.get(0));
						orderHistoryBean.setOrderPlacedDttm(posOrderList.get(0).getOrderPlacedDttm());
						orderHistoryBean.setOrderStatus(finalOrderStatus);						
						/**insert EmployeeID from POS message received*/
						if(posTransactionDetailsList.get(i).getEmployeeId().equalsIgnoreCase("")
						   || posTransactionDetailsList.get(i).getEmployeeId().equalsIgnoreCase(PhotoOmniConstants.BLANK)
						   || posTransactionDetailsList.get(i).getEmployeeId() == null){
							
							     orderHistoryBean.setCreateUserId(PhotoOmniConstants.CREATE_USER_ID);					
						    } else {
							     orderHistoryBean.setCreateUserId(posTransactionDetailsList.get(i).getEmployeeId());
						    }				
						orderHistoryBean.setUpdateUserId(PhotoOmniConstants.UPDATE_USER_ID);
						
						//call PM & MBPM here
						final String orderDate = posOrderList.get(0).getOrderPlacedDttm();
						Long shoppingCartId = this.getShoppingCartId(posOrderList.get(0).getSysOrderId(), orderDate);
						if(shoppingCartId == null){
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("shoppingCartId is null for sysOrderID "+posOrderList.get(0).getSysOrderId());
								}
						}
						promoBo.calculatePromotionalMoney(posOrderList.get(0).getSysOrderId(), orderDate);
						mbpmBo.calculateMBPromotionalMoney(shoppingCartId, orderDate);

						
						}else{
						    if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Order details not found for sysOrderId "+sysOrderIdList.get(0)+" hence not processed further.");
							}
					     }
						
						
						
						/** add orderHistoryBean to posReconciliationTransferBean*/
						posReconciliationTransferBean.setOrderHistoryBean(orderHistoryBean);

						/** add posReconciliationTransferBean to posReconciliationTransferBeansList*/
						posReconciliationTransferBeansList.add(posReconciliationTransferBean);
																			
						}else{
							    if (LOGGER.isDebugEnabled()) {
								LOGGER.debug("sysOrderId not found for Envelop number "+posTransactionDetailsList.get(i).getEnvelopNo());
								}
						     }									
					  }

				}
				
				
				queryFlag = true;
				counter = 0;
				LOGGER.debug("DB transaction in Reader ended");
			}

			if (posReconciliationTransferBeansList != null
					&& posReconciliationTransferBeansList.size() >= 1
					&& counter < posReconciliationTransferBeansList.size()) {

				if (counter == posReconciliationTransferBeansList.size() - 1) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				LOGGER.debug("send posReconciliationTransferBeans to Writer");
				posReconciliationTransferBeanTempOne = posReconciliationTransferBeansList.get(counter++);

			} else {
				if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting PosReconciliationReader read() and going to writer");}
				return null;
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Error at Read method in PosReconciliationReader ....", e);}
			throw new PhotoOmniException(e.getMessage());
		}
		return posReconciliationTransferBeanTempOne;
	}

	/**
	 * @param posOrderSoldAmt
	 * @param orderId
	 * @param posTranType
	 * @param posCostCalCulationBean 
	 * @return
	 */
	public PosCostCalCulationBean calculateSoldAmount(double posOrderSoldAmt, long orderId,
			String posTranType, PosCostCalCulationBean posCostCalCulationBean) {

		if (LOGGER.isDebugEnabled()) {
		LOGGER.info("Entering PosProportionatingSoldAmt calculateSoldAmount() method ");}

		//POSOrder posOrder = new POSOrder();
		DecimalFormat formatter = new DecimalFormat("0.0000");

		/** Apply proportionating logic based on posTranType */
		if (posTranType.equalsIgnoreCase(PhotoOmniBatchConstants.POS_TRAN_TYPE_MS)
				|| posTranType.equalsIgnoreCase(PhotoOmniBatchConstants.POS_TRAN_TYPE_MA)) {

			/** get order details for OM_ORDER table with SYS_ORDER_ID */
			String omOrderDetailsQuery = posQuery.getOmOrderDetailsQuery();
			List<POSOrder> posOrderList = jdbcTemplate.query(omOrderDetailsQuery,
					new Object[] { orderId }, new PosOrderRowMapper());

			double posOrderOmFinalPrice = posOrderList.get(0).getFinalPrice();
			double soldAmtOmOrder = posOrderList.get(0).getSoldAmount() + posOrderSoldAmt;

			/** UPDATE SOLD_AMOUNT in OM_ORDER table */
			posCostCalCulationBean.setSysOrderId(orderId);
			posCostCalCulationBean.setSoldAmtOmOrder(soldAmtOmOrder);
			

			/** get Order details from OM_ORDER_LINE details table with ORDER_ID */
			List<OrderLineBean> orderLineBeanList = new ArrayList<OrderLineBean>();
			String orderLineDetailsQuery = posQuery.getOrderLineDetailsQuery();
			orderLineBeanList = jdbcTemplate.query(orderLineDetailsQuery,
					new OrderLineBeanRowMapper(), new Object[] { orderId });

			/** Calculate OrderLine Sold Amt with proportion */
			if (orderLineBeanList != null && orderLineBeanList.size()>0) {

				double lineSoldAmt = 0.00;
				for (int p = 0; p < orderLineBeanList.size(); p++) {
					
					double orderLineFinalPrice = orderLineBeanList.get(p).getFinalPrice();
					
					/**Logic to count the fractions in consideration*/
					String tempOrderLineStringOne = "";
					if(posOrderOmFinalPrice != 0){
					     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
					}else{
						 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
					}
					
					lineSoldAmt = lineSoldAmt + Double.parseDouble(CommonUtil
							.formatStringDecimalVal(tempOrderLineStringOne));
				
				}

				double lineSoldAmtDiff = 0.00;
				if (lineSoldAmt < soldAmtOmOrder) {
					lineSoldAmtDiff = soldAmtOmOrder - lineSoldAmt;
				}

				double lineSoldAmt1 = 0.00;
				for (int q = 0; q < orderLineBeanList.size(); q++) {

					long sysOrderLineID = orderLineBeanList.get(q).getSysOrderLineId();
					double orderLineQuentity = orderLineBeanList.get(q).getQuantity();
					double orderLineFinalPrice = orderLineBeanList.get(q).getFinalPrice();

					if (q == (orderLineBeanList.size() - 1)) {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						String tempOrderLineStringThree = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}
						tempOrderLineStringThree = formatter.format(lineSoldAmtDiff);

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringThree)) + 
						Double.parseDouble(CommonUtil
								.formatStringDecimalVal(tempOrderLineStringOne));
						
					} else {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringOne));

					}

					/** update LINE_SOLD_AMOUNT in OM_ORDER_LINE with ORDER_ID */
					orderLineBeanList.get(q).setLineSoldAmt(lineSoldAmt1);
					posCostCalCulationBean.setOrderLineBeanList(orderLineBeanList);
					
			
					/**
					 * get license content details from OM_OL_LICENSE_CONTENT
					 * with SYS_ORDER_LINE_ID
					 */
					List<LicenceContentPosBean> licenceContentBeanList = new ArrayList<LicenceContentPosBean>();
					String licenceContentDetailsQuery = posQuery.getlicenceContentDetailsQuery();
					licenceContentBeanList = jdbcTemplate.query(licenceContentDetailsQuery,
							new LicenceContentPosBeanRowMapper(),new Object[] { sysOrderLineID });

					/**
					 * get order line template details for form
					 * OM_ORDER_LINE_TEMPLATE with SYS_ORDER_LINE_ID
					 */
					List<OrderLineTemplateBean> orderLineTemplateBeanList = new ArrayList<OrderLineTemplateBean>();
					String orderLineTemplateBeansListQuery = posQuery.getorderLineTemplateBeansListQuery();
					orderLineTemplateBeanList = jdbcTemplate.query(orderLineTemplateBeansListQuery,
							new OrderLineTemplateBeanRowMapper(),new Object[] { sysOrderLineID });

					/** Calculate License Amt paid with proportion */
					if (licenceContentBeanList != null && licenceContentBeanList.size()>0) {

						double lcAmtPaid = 0.00;
						for (int j = 0; j < licenceContentBeanList.size(); j++) {

							double finalLcPrice = licenceContentBeanList.get(j).getFinalLcPrice();
							
							/**Logic to count the fractions in consideration*/
							String tempLicenceStringOne = "";
							
							if(orderLineFinalPrice !=0){
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
							}else{
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
							}
							
							
							lcAmtPaid = lcAmtPaid + Double.parseDouble(CommonUtil
						               .formatStringDecimalVal(tempLicenceStringOne));

						}

						double lcAmtPaidDiff = 0.00;
						if (lcAmtPaid < lineSoldAmt1) {
							lcAmtPaidDiff = lineSoldAmt1 - lcAmtPaid;
						}

						double lcAmtPaid1 = 0.00;
						for (int k = 0; k < licenceContentBeanList.size(); k++) {

							if (k == (licenceContentBeanList.size() - 1)) {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								String tempLicenceStringThree = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								tempLicenceStringThree = formatter.format(lcAmtPaidDiff);
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								        .formatStringDecimalVal(tempLicenceStringOne)) + 
								     Double.parseDouble(CommonUtil
										.formatStringDecimalVal(tempLicenceStringThree));
								
							} else {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								           .formatStringDecimalVal(tempLicenceStringOne));
							}

							/**
							 * update LC_AMOUNT_PAID in OM_OL_LICENSE_CONTENT
							 * with SYS_OL_LC_ID
							 */
							licenceContentBeanList.get(k).setLcAmountPaid(lcAmtPaid1);
							posCostCalCulationBean.setLicenceContentBeanList(licenceContentBeanList);
														
						}
					}

					/** Calculate template Sold Amt with proportion */
					if (orderLineTemplateBeanList != null && orderLineTemplateBeanList.size()>0) {

						double templateSoldAmt = 0.00;
						for (int m = 0; m < orderLineTemplateBeanList.size(); m++) {

							double templateQuentity = orderLineTemplateBeanList.get(m).getTemplateQty();
							
							/**Logic to count the fractions in consideration*/
							String tempTemplateStringOne = "";
							
							if(orderLineQuentity != 0){
							    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
							}else{
								tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
							}
							templateSoldAmt = templateSoldAmt + Double.parseDouble(CommonUtil
			                          .formatStringDecimalVal(tempTemplateStringOne));

						}

						double templateSoldAmtDiff = 0.00;
						if (templateSoldAmt < lineSoldAmt1) {
							templateSoldAmtDiff = lineSoldAmt1- templateSoldAmt;
						}

						double templateSoldAmt1 = 0.00;
						for (int n = 0; n < orderLineTemplateBeanList.size(); n++) {

							if (n == (orderLineTemplateBeanList.size() - 1)) {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								String tempTemplateStringThree = "";
								
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								tempTemplateStringThree = formatter.format(templateSoldAmtDiff);
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
					                      .formatStringDecimalVal(tempTemplateStringOne)) + 
					                  Double.parseDouble(CommonUtil
							              .formatStringDecimalVal(tempTemplateStringThree));

							} else {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
						                 .formatStringDecimalVal(tempTemplateStringOne));

							}
							/**
							 * update TEMPLATE_SOLD_AMT in
							 * OM_ORDER_LINE_TEMPLATE with SYS_OL_TEMPLATE_ID
							 */

							orderLineTemplateBeanList.get(n).setTemplateSoldAmt(templateSoldAmt1);
							posCostCalCulationBean.setOrderLineTemplateBeanList(orderLineTemplateBeanList);
							
							
						}
					}

				}
			}

		}

		/** Apply proportionating logic based on posTranType */
		if (posTranType
				.equalsIgnoreCase(PhotoOmniBatchConstants.POS_TRAN_TYPE_MR)
				|| posTranType
						.equalsIgnoreCase(PhotoOmniBatchConstants.POS_TRAN_TYPE_SR)) {

			/** get order details for OM_ORDER table with SYS_ORDER_ID */
			String omOrderDetailsQuery = posQuery.getOmOrderDetailsQuery();
			List<POSOrder> posOrderList = jdbcTemplate.query(omOrderDetailsQuery,
					new Object[] { orderId }, new PosOrderRowMapper());

			double posOrderOmFinalPrice = posOrderList.get(0).getFinalPrice();
			double soldAmtOmOrder = posOrderList.get(0).getSoldAmount() - posOrderSoldAmt;

			/** UPDATE SOLD_AMOUNT in OM_ORDER table */
			posCostCalCulationBean.setSysOrderId(orderId);
			posCostCalCulationBean.setSoldAmtOmOrder(soldAmtOmOrder);

			/** get Order details from OM_ORDER_LINE details tabe with ORDER_ID */
			List<OrderLineBean> orderLineBeanList = new ArrayList<OrderLineBean>();
			String orderLineDetailsQuery = posQuery.getOrderLineDetailsQuery();
			orderLineBeanList = jdbcTemplate.query(orderLineDetailsQuery,
					new OrderLineBeanRowMapper(), new Object[] { orderId });

			/** Calculate OrderLine Sold Amt with proportion */
			if (orderLineBeanList != null && orderLineBeanList.size()>0) {

				double lineSoldAmt = 0.00;
				for (int p = 0; p < orderLineBeanList.size(); p++) {
					
					double orderLineFinalPrice = orderLineBeanList.get(p).getFinalPrice();
					
					/**Logic to count the fractions in consideration*/
					String tempOrderLineStringOne = "";
					if(posOrderOmFinalPrice != 0){
					     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
					}else{
						 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
					}
					
					lineSoldAmt = lineSoldAmt + Double.parseDouble(CommonUtil
							.formatStringDecimalVal(tempOrderLineStringOne));
				
				}

				double lineSoldAmtDiff = 0.00;
				if (lineSoldAmt < soldAmtOmOrder) {
					lineSoldAmtDiff = soldAmtOmOrder - lineSoldAmt;
				}

				double lineSoldAmt1 = 0.00;
				for (int q = 0; q < orderLineBeanList.size(); q++) {

					long sysOrderLineID = orderLineBeanList.get(q).getSysOrderLineId();
					double orderLineQuentity = orderLineBeanList.get(q).getQuantity();
					double orderLineFinalPrice = orderLineBeanList.get(q).getFinalPrice();

					if (q == (orderLineBeanList.size() - 1)) {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						String tempOrderLineStringThree = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}
						tempOrderLineStringThree = formatter.format(lineSoldAmtDiff);

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringThree)) + 
						Double.parseDouble(CommonUtil
								.formatStringDecimalVal(tempOrderLineStringOne));
						
					} else {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringOne));

					}

					/** update LINE_SOLD_AMOUNT in OM_ORDER_LINE with ORDER_ID */
					orderLineBeanList.get(q).setLineSoldAmt(lineSoldAmt1);
					posCostCalCulationBean.setOrderLineBeanList(orderLineBeanList);
					
			
					/**
					 * get license content details from OM_OL_LICENSE_CONTENT
					 * with SYS_ORDER_LINE_ID
					 */
					List<LicenceContentPosBean> licenceContentBeanList = new ArrayList<LicenceContentPosBean>();
					String licenceContentDetailsQuery = posQuery.getlicenceContentDetailsQuery();
					licenceContentBeanList = jdbcTemplate.query(licenceContentDetailsQuery,
							new LicenceContentPosBeanRowMapper(),new Object[] { sysOrderLineID });

					/**
					 * get order line template details for form
					 * OM_ORDER_LINE_TEMPLATE with SYS_ORDER_LINE_ID
					 */
					List<OrderLineTemplateBean> orderLineTemplateBeanList = new ArrayList<OrderLineTemplateBean>();
					String orderLineTemplateBeansListQuery = posQuery.getorderLineTemplateBeansListQuery();
					orderLineTemplateBeanList = jdbcTemplate.query(orderLineTemplateBeansListQuery,
							new OrderLineTemplateBeanRowMapper(),new Object[] { sysOrderLineID });

					/** Calculate License Amt paid with proportion */
					if (licenceContentBeanList != null && licenceContentBeanList.size()>0) {

						double lcAmtPaid = 0.00;
						for (int j = 0; j < licenceContentBeanList.size(); j++) {

							double finalLcPrice = licenceContentBeanList.get(j).getFinalLcPrice();
							
							/**Logic to count the fractions in consideration*/
							String tempLicenceStringOne = "";
							
							if(orderLineFinalPrice !=0){
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
							}else{
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
							}
							
							
							lcAmtPaid = lcAmtPaid + Double.parseDouble(CommonUtil
						               .formatStringDecimalVal(tempLicenceStringOne));

						}

						double lcAmtPaidDiff = 0.00;
						if (lcAmtPaid < lineSoldAmt1) {
							lcAmtPaidDiff = lineSoldAmt1 - lcAmtPaid;
						}

						double lcAmtPaid1 = 0.00;
						for (int k = 0; k < licenceContentBeanList.size(); k++) {

							if (k == (licenceContentBeanList.size() - 1)) {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								String tempLicenceStringThree = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								tempLicenceStringThree = formatter.format(lcAmtPaidDiff);
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								        .formatStringDecimalVal(tempLicenceStringOne)) + 
								     Double.parseDouble(CommonUtil
										.formatStringDecimalVal(tempLicenceStringThree));
								
							} else {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								           .formatStringDecimalVal(tempLicenceStringOne));
							}

							/**
							 * update LC_AMOUNT_PAID in OM_OL_LICENSE_CONTENT
							 * with SYS_OL_LC_ID
							 */
							licenceContentBeanList.get(k).setLcAmountPaid(lcAmtPaid1);
							posCostCalCulationBean.setLicenceContentBeanList(licenceContentBeanList);
														
						}
					}

					/** Calculate template Sold Amt with proportion */
					if (orderLineTemplateBeanList != null && orderLineTemplateBeanList.size()>0) {

						double templateSoldAmt = 0.00;
						for (int m = 0; m < orderLineTemplateBeanList.size(); m++) {

							double templateQuentity = orderLineTemplateBeanList.get(m).getTemplateQty();
							
							/**Logic to count the fractions in consideration*/
							String tempTemplateStringOne = "";
							
							if(orderLineQuentity != 0){
							    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
							}else{
								tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
							}
							templateSoldAmt = templateSoldAmt + Double.parseDouble(CommonUtil
			                          .formatStringDecimalVal(tempTemplateStringOne));

						}

						double templateSoldAmtDiff = 0.00;
						if (templateSoldAmt < lineSoldAmt1) {
							templateSoldAmtDiff = lineSoldAmt1- templateSoldAmt;
						}

						double templateSoldAmt1 = 0.00;
						for (int n = 0; n < orderLineTemplateBeanList.size(); n++) {

							if (n == (orderLineTemplateBeanList.size() - 1)) {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								String tempTemplateStringThree = "";
								
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								tempTemplateStringThree = formatter.format(templateSoldAmtDiff);
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
					                      .formatStringDecimalVal(tempTemplateStringOne)) + 
					                  Double.parseDouble(CommonUtil
							              .formatStringDecimalVal(tempTemplateStringThree));

							} else {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
						                 .formatStringDecimalVal(tempTemplateStringOne));

							}
							/**
							 * update TEMPLATE_SOLD_AMT in
							 * OM_ORDER_LINE_TEMPLATE with SYS_OL_TEMPLATE_ID
							 */

							orderLineTemplateBeanList.get(n).setTemplateSoldAmt(templateSoldAmt1);
							posCostCalCulationBean.setOrderLineTemplateBeanList(orderLineTemplateBeanList);
							
							
						}
					}

				}
			}

		}

		/** Apply proportionating logic based on posTranType */
		if (posTranType
				.equalsIgnoreCase(PhotoOmniBatchConstants.POS_TRAN_TYPE_MV)) {

			/** get order details for OM_ORDER table with SYS_ORDER_ID */
			String omOrderDetailsQuery = posQuery.getOmOrderDetailsQuery();
			List<POSOrder> posOrderList = jdbcTemplate.query(omOrderDetailsQuery,
					new Object[] { orderId }, new PosOrderRowMapper());

			double posOrderOmFinalPrice = posOrderList.get(0).getFinalPrice();
			double soldAmtOmOrder = posOrderList.get(0).getSoldAmount() - posOrderSoldAmt;

			/** UPDATE SOLD_AMOUNT in OM_ORDER table */
			posCostCalCulationBean.setSysOrderId(orderId);
			posCostCalCulationBean.setSoldAmtOmOrder(soldAmtOmOrder);

			/** get Order details from OM_ORDER_LINE details tabe with ORDER_ID */
			List<OrderLineBean> orderLineBeanList = new ArrayList<OrderLineBean>();
			String orderLineDetailsQuery = posQuery.getOrderLineDetailsQuery();
			orderLineBeanList = jdbcTemplate.query(orderLineDetailsQuery,
					new OrderLineBeanRowMapper(), new Object[] { orderId });

			/** Calculate OrderLine Sold Amt with proportion */
			if (orderLineBeanList != null && orderLineBeanList.size()>0) {

				double lineSoldAmt = 0.00;
				for (int p = 0; p < orderLineBeanList.size(); p++) {
					
					double orderLineFinalPrice = orderLineBeanList.get(p).getFinalPrice();
					
					/**Logic to count the fractions in consideration*/
					String tempOrderLineStringOne = "";
					if(posOrderOmFinalPrice != 0){
					     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
					}else{
						 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
					}
					
					lineSoldAmt = lineSoldAmt + Double.parseDouble(CommonUtil
							.formatStringDecimalVal(tempOrderLineStringOne));
				
				}

				double lineSoldAmtDiff = 0.00;
				if (lineSoldAmt < soldAmtOmOrder) {
					lineSoldAmtDiff = soldAmtOmOrder - lineSoldAmt;
				}

				double lineSoldAmt1 = 0.00;
				for (int q = 0; q < orderLineBeanList.size(); q++) {

					long sysOrderLineID = orderLineBeanList.get(q).getSysOrderLineId();
					double orderLineQuentity = orderLineBeanList.get(q).getQuantity();
					double orderLineFinalPrice = orderLineBeanList.get(q).getFinalPrice();

					if (q == (orderLineBeanList.size() - 1)) {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						String tempOrderLineStringThree = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}
						tempOrderLineStringThree = formatter.format(lineSoldAmtDiff);

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringThree)) + 
						Double.parseDouble(CommonUtil
								.formatStringDecimalVal(tempOrderLineStringOne));
						
					} else {

						/**Logic to count the fractions in consideration*/
						String tempOrderLineStringOne = "";
						
						if(posOrderOmFinalPrice != 0){
						     tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder) / (posOrderOmFinalPrice));
						}else{
							 tempOrderLineStringOne = formatter.format((orderLineFinalPrice * soldAmtOmOrder));
						}

						lineSoldAmt1 = Double.parseDouble(CommonUtil
						         .formatStringDecimalVal(tempOrderLineStringOne));

					}

					/** update LINE_SOLD_AMOUNT in OM_ORDER_LINE with ORDER_ID */
					orderLineBeanList.get(q).setLineSoldAmt(lineSoldAmt1);
					posCostCalCulationBean.setOrderLineBeanList(orderLineBeanList);
					
			
					/**
					 * get license content details from OM_OL_LICENSE_CONTENT
					 * with SYS_ORDER_LINE_ID
					 */
					List<LicenceContentPosBean> licenceContentBeanList = new ArrayList<LicenceContentPosBean>();
					String licenceContentDetailsQuery = posQuery.getlicenceContentDetailsQuery();
					licenceContentBeanList = jdbcTemplate.query(licenceContentDetailsQuery,
							new LicenceContentPosBeanRowMapper(),new Object[] { sysOrderLineID });

					/**
					 * get order line template details for form
					 * OM_ORDER_LINE_TEMPLATE with SYS_ORDER_LINE_ID
					 */
					List<OrderLineTemplateBean> orderLineTemplateBeanList = new ArrayList<OrderLineTemplateBean>();
					String orderLineTemplateBeansListQuery = posQuery.getorderLineTemplateBeansListQuery();
					orderLineTemplateBeanList = jdbcTemplate.query(orderLineTemplateBeansListQuery,
							new OrderLineTemplateBeanRowMapper(),new Object[] { sysOrderLineID });

					/** Calculate License Amt paid with proportion */
					if (licenceContentBeanList != null && licenceContentBeanList.size()>0) {

						double lcAmtPaid = 0.00;
						for (int j = 0; j < licenceContentBeanList.size(); j++) {

							double finalLcPrice = licenceContentBeanList.get(j).getFinalLcPrice();
							
							/**Logic to count the fractions in consideration*/
							String tempLicenceStringOne = "";
							
							if(orderLineFinalPrice !=0){
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
							}else{
								tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
							}
							
							
							lcAmtPaid = lcAmtPaid + Double.parseDouble(CommonUtil
						               .formatStringDecimalVal(tempLicenceStringOne));

						}

						double lcAmtPaidDiff = 0.00;
						if (lcAmtPaid < lineSoldAmt1) {
							lcAmtPaidDiff = lineSoldAmt1 - lcAmtPaid;
						}

						double lcAmtPaid1 = 0.00;
						for (int k = 0; k < licenceContentBeanList.size(); k++) {

							if (k == (licenceContentBeanList.size() - 1)) {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								String tempLicenceStringThree = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								tempLicenceStringThree = formatter.format(lcAmtPaidDiff);
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								        .formatStringDecimalVal(tempLicenceStringOne)) + 
								     Double.parseDouble(CommonUtil
										.formatStringDecimalVal(tempLicenceStringThree));
								
							} else {
								double finalLcPrice = licenceContentBeanList.get(k).getFinalLcPrice();
								
								/**Logic to count the fractions in consideration*/
								String tempLicenceStringOne = "";
								
								if(orderLineFinalPrice !=0){
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1) / (orderLineFinalPrice));
								}else{
									tempLicenceStringOne = formatter.format((finalLcPrice * lineSoldAmt1));
								}
								
								
								lcAmtPaid1 = Double.parseDouble(CommonUtil
								           .formatStringDecimalVal(tempLicenceStringOne));
							}

							/**
							 * update LC_AMOUNT_PAID in OM_OL_LICENSE_CONTENT
							 * with SYS_OL_LC_ID
							 */
							licenceContentBeanList.get(k).setLcAmountPaid(lcAmtPaid1);
							posCostCalCulationBean.setLicenceContentBeanList(licenceContentBeanList);
														
						}
					}

					/** Calculate template Sold Amt with proportion */
					if (orderLineTemplateBeanList != null && orderLineTemplateBeanList.size()>0) {

						double templateSoldAmt = 0.00;
						for (int m = 0; m < orderLineTemplateBeanList.size(); m++) {

							double templateQuentity = orderLineTemplateBeanList.get(m).getTemplateQty();
							
							/**Logic to count the fractions in consideration*/
							String tempTemplateStringOne = "";
							
							if(orderLineQuentity != 0){
							    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
							}else{
								tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
							}
							templateSoldAmt = templateSoldAmt + Double.parseDouble(CommonUtil
			                          .formatStringDecimalVal(tempTemplateStringOne));

						}

						double templateSoldAmtDiff = 0.00;
						if (templateSoldAmt < lineSoldAmt1) {
							templateSoldAmtDiff = lineSoldAmt1- templateSoldAmt;
						}

						double templateSoldAmt1 = 0.00;
						for (int n = 0; n < orderLineTemplateBeanList.size(); n++) {

							if (n == (orderLineTemplateBeanList.size() - 1)) {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								String tempTemplateStringThree = "";
								
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								tempTemplateStringThree = formatter.format(templateSoldAmtDiff);
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
					                      .formatStringDecimalVal(tempTemplateStringOne)) + 
					                  Double.parseDouble(CommonUtil
							              .formatStringDecimalVal(tempTemplateStringThree));

							} else {
								double templateQuentity = orderLineTemplateBeanList.get(n).getTemplateQty();
								
								/**Logic to count the fractions in consideration*/
								String tempTemplateStringOne = "";
								if(orderLineQuentity != 0){
								    tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity) / orderLineQuentity);
								}else{
									tempTemplateStringOne = formatter.format((lineSoldAmt1 *templateQuentity));
								}
								
								templateSoldAmt1 = Double.parseDouble(CommonUtil
						                 .formatStringDecimalVal(tempTemplateStringOne));

							}
							/**
							 * update TEMPLATE_SOLD_AMT in
							 * OM_ORDER_LINE_TEMPLATE with SYS_OL_TEMPLATE_ID
							 */

							orderLineTemplateBeanList.get(n).setTemplateSoldAmt(templateSoldAmt1);
							posCostCalCulationBean.setOrderLineTemplateBeanList(orderLineTemplateBeanList);
							
							
						}
					}

				}
			}

		}

		if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Exiting PosProportionatingSoldAmt calculateSoldAmount()");
		}
		return posCostCalCulationBean;
	}
	
	/**
	 * @param orderDate
	 * @param SysOrderId
	 * @return sysShoppingCartId
	 */
	public Long getShoppingCartId(long SysOrderId, String orderDate)
			throws NullPointerException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getShoppingCartId updateOrderSoldDttm()");
		}
		//long sysShoppingCartId = 0;
		String shoppingCartQuery = posQuery.getShoppingCartIDquery();
		//try {
			List<Long> sysShoppingCartIdList = jdbcTemplate.queryForList(shoppingCartQuery,
					new Object[] { SysOrderId, orderDate }, Long.class);
		//} catch (Exception e) {
		//	throw new NullPointerException();
		//}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting getShoppingCartId updateOrderSoldDttm()");
		}
		if(sysShoppingCartIdList.isEmpty() || sysShoppingCartIdList.get(0) == null){
			return null;
		}
		return sysShoppingCartIdList.get(0);
	}
}
