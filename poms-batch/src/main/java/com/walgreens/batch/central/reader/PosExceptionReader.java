package com.walgreens.batch.central.reader;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.bean.OrderHistoryBean;
import com.walgreens.batch.central.bean.POSOrder;
import com.walgreens.batch.central.bean.PosExceptionTransferBean;
import com.walgreens.batch.central.bean.PosOrderExceptionDataBean;
import com.walgreens.batch.central.bean.WasteQtyBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.PosOrderRowMapper;
import com.walgreens.batch.central.utility.PosQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 * This Class is used to create POS exception for different orders 
 * 
 */

@Component("posExceptionReader")
@Scope("pos_excption_step1")
public class PosExceptionReader implements ItemReader<PosExceptionTransferBean> {

	/** FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED */
	private boolean queryFlag = false;

	/** COUNTER USED TO READ & CALCULATE posTransactionDetails & SEND TO PROCESSOR*/
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

	private static final Logger LOGGER = LoggerFactory.getLogger(PosExceptionReader.class);

	@SuppressWarnings({ "unused" })
	@Override
	@Transactional
	public PosExceptionTransferBean read() throws Exception,UnexpectedInputException, ParseException,
			NonTransientResourceException ,PhotoOmniException{

		if (LOGGER.isDebugEnabled()) {
		LOGGER.debug("Entering PosExceptionReader read()");
		}

		boolean updSoldAmtStatus = false;
		boolean orderHistoryStatus = false;

		PosQuery posQuery = new PosQuery();
		String calenderIdWithEarlierDayQuery = posQuery.getCalenderIdWithEarlierDayQuery();
		String orderDetailsWithCalenderIdQuery = posQuery.getOrderDetailsWithCalenderIdQuery();
		String orderLineIdQuery = posQuery.getorderLineIdQuery();
		String orderLineCostQuery = posQuery.getOrderLineCostQuery();
		String printsReturnedQty = posQuery.getPrintsReturnedQty();
		String getWasteQtyQuery = posQuery.getWasteQtyQuery();
		String excepTypeIdQuery = posQuery.getExcepTypeIdQuery();
		String excepTypeIdForRefusedQuery = posQuery.getExcepTypeIdForRefusedQuery();
				
		List<PosExceptionTransferBean> posExceptionTransferBeansList = new ArrayList<PosExceptionTransferBean>();
		List<POSOrder> posOrderList = new ArrayList<POSOrder>();
		PosExceptionTransferBean posExceptionTransferBeanTempOne = null;

		
		try {
			
			
			/** calculate earlier day */
			DateFormat dateFormat = new SimpleDateFormat(PhotoOmniBatchConstants.DATE_FORMAT_POS_ONE);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			String earlierDate = dateFormat.format(cal.getTime());

			/** get calenderId of the earlier day from OM_CALENDER */		
			List<Integer> calenderIDList = jdbcTemplate.queryForList(calenderIdWithEarlierDayQuery,
					new Object[] { earlierDate }, Integer.class);

			if (!queryFlag) {
				
				/**Proceed further if calenderId present for earlier day */
				if(!calenderIDList.isEmpty() && calenderIDList.get(0) != null){				

				/*** Read all the records from OM_ORDER for calendar id of the earlier day*/				
				posOrderList = jdbcTemplate.query(orderDetailsWithCalenderIdQuery, new Object[] {
						calenderIDList.get(0), pageBegin,(pageBegin + paginationCounter - 1) },new PosOrderRowMapper());

				/**Proceed further if orders present in OM_ORDER table for earlier day */
				if (!posOrderList.isEmpty() && posOrderList.size() > 0) {

					/** create instance for beans and lists*/
					PosExceptionTransferBean posExceptionTransferBean = new PosExceptionTransferBean();
					List<PosOrderExceptionDataBean> posOrderExceptionDataBeanList = new ArrayList<PosOrderExceptionDataBean>();
					List<OrderHistoryBean> posOrderHistoryBeanList = new ArrayList<OrderHistoryBean>();
					List<WasteQtyBean> wasteQtyBeanList = new ArrayList<WasteQtyBean>(); 

					for (int i = 0; i < posOrderList.size(); i++) {
						
						POSOrder posOrder = new POSOrder();
						posOrder = posOrderList.get(i);

						/**** If order status = “SOLD”, then create exception */
						if (posOrder.getStatus().equalsIgnoreCase(PhotoOmniBatchConstants.POS_ORDER_STATUS_SOLD)) {

							/** get orderlineId from OM_ORDER_LINE with orderId */							
							List<Long> orderLineIdList = new ArrayList<Long>();							
							orderLineIdList = jdbcTemplate.queryForList(
									orderLineIdQuery,new Object[] { posOrder.getSysOrderId() },Long.class);	
							
							/**Proceed further if order lines are associated with order */
							if(!orderLineIdList.isEmpty() && orderLineIdList.get(0) != null){

							/** select cost from first order line of the order */    
							List<Double> costOrderLineList = jdbcTemplate.queryForList(
							orderLineCostQuery,new Object[] { orderLineIdList.get(0) },Double.class);	
							 if(costOrderLineList.isEmpty() || costOrderLineList.get(0) == null){
								if (LOGGER.isDebugEnabled()) {
								    LOGGER.debug("cost for first order line "+orderLineIdList.get(0)+" is not present");
							      }
							   }

							/** get OM_ORDER_ATTRIBUTE.PRINTS_RETURNED_QTY for the order*/						
							List<Integer> printsReturnedQuentityList = jdbcTemplate.queryForList(printsReturnedQty,
							new Object[] { posOrder.getSysOrderId() },Integer.class);
							 if(printsReturnedQuentityList.isEmpty() || printsReturnedQuentityList.get(0) == null){
								if (LOGGER.isDebugEnabled()) {
								    LOGGER.debug("OM_ORDER_ATTRIBUTE.PRINTS_RETURNED_QTY is Null for order "+posOrder.getSysOrderId());
							      }
							   }
							
							/**for the 1st order line, update the OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY by adding this WASTE_QTY to it*/
							List<Integer> wasteQtyList = jdbcTemplate.queryForList(
							getWasteQtyQuery,new Object[] { orderLineIdList.get(0) },Integer.class);
							 if(wasteQtyList.isEmpty() || wasteQtyList.get(0) == null){
								 if (LOGGER.isDebugEnabled()){
								    LOGGER.debug("OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY is Null for order line "+orderLineIdList.get(0));
							      }
							   }
							
							 /**Logic to add wasteQty and printsReturnedQuentity to tempWastQty*/
							Integer tempWastQty = null;
							if((!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null)
									&& (!wasteQtyList.isEmpty() && wasteQtyList.get(0) != null)){
							    tempWastQty = wasteQtyList.get(0) + printsReturnedQuentityList.get(0);
							}else if(!wasteQtyList.isEmpty() && wasteQtyList.get(0) != null){
								tempWastQty = wasteQtyList.get(0);
							}else if(!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null){
								tempWastQty = printsReturnedQuentityList.get(0);
							}
							

							/**
							 * If Calculated Retail/Final price and Sold Amount,
							 * both are zero, then create an exception “Priced
							 * for Free”, and add a record in the Exception
							 * table (OM_ORDER_EXCEPTION).
							 */
							if (posOrder.getSoldAmount() == 0 && posOrder.getFinalPrice() == 0) {

								/*** get excepTypeId from OM_EXCEPTION_TYPE with ORDER_STATUS */							
								List<Integer> excepTypeIdList = jdbcTemplate.queryForList(excepTypeIdQuery, new Object[] 
								{PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_SOLD_FOR_FREE},Integer.class);
								 if(excepTypeIdList.isEmpty() || excepTypeIdList.get(0) == null){
									if (LOGGER.isDebugEnabled()) {
									    LOGGER.debug("OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID is Null for Exception Type"
									                  +PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_SOLD_FOR_FREE);
								      }
								   }
								

								/** update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY */
								if(tempWastQty != null){
								WasteQtyBean tempWasteQtyBeanOne = new WasteQtyBean();
								tempWasteQtyBeanOne.setOrderlineId(orderLineIdList.get(0));								
								tempWasteQtyBeanOne.setWastQty(tempWastQty);								
								wasteQtyBeanList.add(tempWasteQtyBeanOne);
							    }else{
									  if (LOGGER.isDebugEnabled()) {
										  LOGGER.debug("Temporary  Wast Quantity is Null");
										   }
								        }

								/**create pos order exception bean */
								if((!costOrderLineList.isEmpty() && costOrderLineList.get(0) != null) 
										&& (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) 
										&& (!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null)){								
								posOrderExceptionDataBeanList.add(getPosOrderExceptionDataBeanDetails(										
										posOrder,
										orderLineIdList.get(0),
										excepTypeIdList.get(0),
										printsReturnedQuentityList.get(0),
										costOrderLineList.get(0),
										PhotoOmniBatchConstants.POS_EXCEPTION_NOTES_ONE));
								}

								/**create pos order history bean*/	
								if(!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null){
								posOrderHistoryBeanList.add(getOrderHistoryBean(posOrder,excepTypeIdList.get(0)));
								}

							}

							/**
							 * if Sold Amount is less than Original Calculated
							 * Amount, then create an exception and add a record
							 * in the Exception table (OM_ORDER_EXCEPTION)
							 */
							if (posOrder.getSoldAmount() < (posOrder.getOriginalOrderPrice())) {

								/** get excepTypeId from OM_EXCEPTION_TYPE with ORDER_STATUS */	
								List<Integer> excepTypeIdList = jdbcTemplate.queryForList(excepTypeIdQuery, new Object[] 
								{PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_PRICE_MODIFY },Integer.class);
								if(excepTypeIdList.isEmpty() || excepTypeIdList.get(0) == null){
									if (LOGGER.isDebugEnabled()) {
									    LOGGER.debug("OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID is Null for Exception Type"
									                  +PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_PRICE_MODIFY);
								      }
								   }

								/** update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY */
								if(tempWastQty != null){
								WasteQtyBean tempWasteQtyBeanTwo = new WasteQtyBean();
								tempWasteQtyBeanTwo.setOrderlineId(orderLineIdList.get(0));								
								tempWasteQtyBeanTwo.setWastQty(tempWastQty);								
								wasteQtyBeanList.add(tempWasteQtyBeanTwo);
								}else{
									  if (LOGGER.isDebugEnabled()) {
										  LOGGER.debug("Temporary  Wast Quantity is Null");
										   }
								        }

								/**create pos order exception bean */
								if((!costOrderLineList.isEmpty() && costOrderLineList.get(0) != null) 
										&& (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) 
										&& (!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null)){								
								posOrderExceptionDataBeanList.add(getPosOrderExceptionDataBeanDetails(										 
										posOrder,
										orderLineIdList.get(0),
										excepTypeIdList.get(0),
										printsReturnedQuentityList.get(0),
										costOrderLineList.get(0),
										PhotoOmniBatchConstants.POS_EXCEPTION_NOTES_TWO));
								}

								/**create pos order history bean*/	
								if(!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null){
								posOrderHistoryBeanList.add(getOrderHistoryBean(posOrder,excepTypeIdList.get(0)));
								}

							}

							/**
							 * If EmployeeDiscountFlag is set to true, then
							 * create an exception and add a record in the
							 * Exception table (OM_ORDER_EXCEPTION).
							 */
							if (posOrder.getDiscountCardUsedCd() == 1) {

								/** get excepTypeId from OM_EXCEPTION_TYPE with ORDER_STATUS*/
								List<Integer> excepTypeIdList = jdbcTemplate.queryForList(excepTypeIdQuery, 
								new Object[] {PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_EMPLOYEE_DISCOUNT },Integer.class);
								if(excepTypeIdList.isEmpty() || excepTypeIdList.get(0) == null){
									if (LOGGER.isDebugEnabled()) {
									    LOGGER.debug("OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID is Null for Exception Type"
									                  +PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_EMPLOYEE_DISCOUNT);
								      }
								   }
								

								/** update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY */
								if(tempWastQty != null){
								WasteQtyBean tempWasteQtyBeanThree = new WasteQtyBean();
								tempWasteQtyBeanThree.setOrderlineId(orderLineIdList.get(0));								
								tempWasteQtyBeanThree.setWastQty(tempWastQty);								
								wasteQtyBeanList.add(tempWasteQtyBeanThree);
								}else{
									  if (LOGGER.isDebugEnabled()) {
										  LOGGER.debug("Temporary  Wast Quantity is Null");
										   }
								        }

								/**create pos order exception bean */
								if((!costOrderLineList.isEmpty() && costOrderLineList.get(0) != null) 
										&& (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) 
										&& (!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null)){								
								posOrderExceptionDataBeanList.add(getPosOrderExceptionDataBeanDetails(										 
										posOrder,
										orderLineIdList.get(0), 
										excepTypeIdList.get(0),
										printsReturnedQuentityList.get(0), 
										costOrderLineList.get(0),
										PhotoOmniBatchConstants.POS_EXCEPTION_NOTES_THREE));
								}

								/**create pos order history bean*/
								if(!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null){
								posOrderHistoryBeanList.add(getOrderHistoryBean(posOrder,excepTypeIdList.get(0)));
								}

							}

							/**
							 * If prints returned > 0, then create an exception
							 * and add a record in the Exception table
							 * (OM_ORDER_EXCEPTION)
							 */
							if (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) {
								
							 if(printsReturnedQuentityList.get(0) > 0){	

								/** get excepTypeId from OM_EXCEPTION_TYPE withORDER_STATUS*/
								List<Integer> excepTypeIdList = jdbcTemplate.queryForList(excepTypeIdForRefusedQuery, new Object[] {
							    PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_REFUSED,
								PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_REFUSED_REASON},Integer.class);
								if(excepTypeIdList.isEmpty() || excepTypeIdList.get(0) == null){
									if (LOGGER.isDebugEnabled()) {
									    LOGGER.debug("OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID is Null for Exception Type"
									                  +PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_REFUSED);
								      }
								   }
								

								/** update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY */
								if(tempWastQty != null){
								WasteQtyBean tempWasteQtyBeanFour = new WasteQtyBean();
								tempWasteQtyBeanFour.setOrderlineId(orderLineIdList.get(0));
								tempWasteQtyBeanFour.setWastQty(tempWastQty);								
								wasteQtyBeanList.add(tempWasteQtyBeanFour);
								}else{
									  if (LOGGER.isDebugEnabled()) {
										  LOGGER.debug("Temporary  Wast Quantity is Null");
										   }
								        }

								/**create pos order exception bean */
								if((!costOrderLineList.isEmpty() && costOrderLineList.get(0) != null) 
										&& (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) 
										&& (!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null)){								
								posOrderExceptionDataBeanList.add(getPosOrderExceptionDataBeanDetails(										 
										posOrder,
										orderLineIdList.get(0), 
										excepTypeIdList.get(0),
										printsReturnedQuentityList.get(0), 
										costOrderLineList.get(0),
										PhotoOmniBatchConstants.POS_EXCEPTION_NOTES_FOUR));
								}

								/**create pos order history bean*/	
								if(!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null){
								posOrderHistoryBeanList.add(getOrderHistoryBean(posOrder,excepTypeIdList.get(0)));
								}
								
							  }

							}
							/**
							 * Amount paid by customer is Zero Dollars i.e.
							 * “Sold for Free”, then create an exception and add
							 * a record in the Exception table
							 * (OM_ORDER_EXCEPTION)
							 */
							if (posOrder.getSoldAmount() == 0 && posOrder.getFinalPrice() != 0) {
                               
								/** get excepTypeId from OM_EXCEPTION_TYPE with ORDER_STATUS*/
								List<Integer> excepTypeIdList = jdbcTemplate.queryForList(excepTypeIdQuery, 
							    new Object[] {PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_SOLD_FOR_FREE },Integer.class);
								if(excepTypeIdList.isEmpty() || excepTypeIdList.get(0) == null){
									if (LOGGER.isDebugEnabled()) {
									    LOGGER.debug("OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID is Null for Exception Type"
									                  +PhotoOmniBatchConstants.POS_EXCEPTION_TYPE_SOLD_FOR_FREE);
								      }
								   }

								/** update OM_ORDER_LINE_ATTRIBUTE.WASTED_QTY */
								if(tempWastQty != null){
								WasteQtyBean tempWasteQtyBeanFive = new WasteQtyBean();
								tempWasteQtyBeanFive.setOrderlineId(orderLineIdList.get(0));								
								tempWasteQtyBeanFive.setWastQty(tempWastQty);								
								wasteQtyBeanList.add(tempWasteQtyBeanFive);
								}else{
									  if (LOGGER.isDebugEnabled()) {
										  LOGGER.debug("Temporary  Wast Quantity is Null");
										   }
								        }

								/**create pos order exception bean */
								if((!costOrderLineList.isEmpty() && costOrderLineList.get(0) != null) 
										&& (!printsReturnedQuentityList.isEmpty() && printsReturnedQuentityList.get(0) != null) 
										&& (!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null)){								
								posOrderExceptionDataBeanList.add(getPosOrderExceptionDataBeanDetails(										 
										posOrder,
										orderLineIdList.get(0), 
										excepTypeIdList.get(0),
										printsReturnedQuentityList.get(0), 
										costOrderLineList.get(0),
										PhotoOmniBatchConstants.POS_EXCEPTION_NOTES_FIVE));
								}

								/**create pos order history bean*/	
								if(!excepTypeIdList.isEmpty() && excepTypeIdList.get(0) != null){
								posOrderHistoryBeanList.add(getOrderHistoryBean(posOrder,excepTypeIdList.get(0)));
								}

							}

						  }else{
							  if (LOGGER.isDebugEnabled()) {
								  LOGGER.debug("No order line details found for SysOrderId "+posOrder.getSysOrderId());
								   }
						        }
						
						}

					}
						
					posExceptionTransferBean.setPosOrderExceptionDataBeanList(posOrderExceptionDataBeanList);
					posExceptionTransferBean.setOrderHistoryBeanList(posOrderHistoryBeanList);
					posExceptionTransferBean.setWasteQtyBeanList(wasteQtyBeanList);

					/** set posExceptionTransferBean to posExceptionTransferBeansList */
					posExceptionTransferBeansList.add(posExceptionTransferBean);
					

				}

				if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("DB transaction in Reader ended");
				LOGGER.debug("No more data found in OM_ORDER table for calendar id "+calenderIDList.get(0));
				}
				queryFlag = true;
				counter = 0;

			   }else{
				  if (LOGGER.isDebugEnabled()) {
					  LOGGER.debug("calenderID not found for earlier day "+earlierDate+" in OM_CALENDER table");
					   }
			        }
			
			}
			if (posExceptionTransferBeansList != null
					&& posExceptionTransferBeansList.size() >= 1
					&& counter < posExceptionTransferBeansList.size()) {

				if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("sending to processor");
				}
				if (counter == posExceptionTransferBeansList.size() - 1) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting PosExceptionReader read()");
				}
				posExceptionTransferBeanTempOne = posExceptionTransferBeansList.get(counter++);

			} else {
				if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting PosExceptionReader read()");
				}
				return null;
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
			LOGGER.error("Error at Read method in PosExceptionReader ....", e);
			}
			throw new PhotoOmniException(e.getMessage());
		}
		return posExceptionTransferBeanTempOne;

	}

	/**Method used to populate OrderExceptionDataBean*/
	/**
	 * @param posOrderExceptionDataBean
	 * @param posOrder
	 * @param SysOrderLineId
	 * @param excepTypeId
	 * @param printsReturnedQuentity
	 * @param costOrderLine
	 * @param notes
	 * @return
	 */
	public PosOrderExceptionDataBean getPosOrderExceptionDataBeanDetails(			
		   POSOrder posOrder, long SysOrderLineId, int excepTypeId,
		   int printsReturnedQuentity, double costOrderLine,String notes) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering PosExceptionReader getPosOrderExceptionDataBeanDetails()");
			}

		PosOrderExceptionDataBean posOrderExceptionDataBeanTemp = new PosOrderExceptionDataBean();
		posOrderExceptionDataBeanTemp.setOrderPlacedDttm(posOrder.getOrderPlacedDttm());
		posOrderExceptionDataBeanTemp.setSysOrderId(posOrder.getSysOrderId());
		posOrderExceptionDataBeanTemp.setSysOrderLineId(SysOrderLineId);
		posOrderExceptionDataBeanTemp.setExctTypeId(excepTypeId);
		posOrderExceptionDataBeanTemp.setWasteQty(printsReturnedQuentity);
		posOrderExceptionDataBeanTemp.setWasteCost(costOrderLine * printsReturnedQuentity);
		posOrderExceptionDataBeanTemp.setNotes(notes+ posOrder.getStatus());
		posOrderExceptionDataBeanTemp.setPrevEnvelopeNo(0);
		posOrderExceptionDataBeanTemp.setPrevOrderStatus(PhotoOmniBatchConstants.BLANK);
		posOrderExceptionDataBeanTemp.setWasteCalcCd(1);
		posOrderExceptionDataBeanTemp.setStatus(posOrder.getStatus());
		posOrderExceptionDataBeanTemp.setSysOrderId(posOrder.getSysOrderId());
		posOrderExceptionDataBeanTemp.setIdCreated(posOrder.getCreateUserId());
		posOrderExceptionDataBeanTemp.setDateTimeCreated(posOrder.getCreateDttm());
		posOrderExceptionDataBeanTemp.setIdModified(posOrder.getUpdateUserId());
		posOrderExceptionDataBeanTemp.setDateTimeModified(posOrder.getUpdatedDttm());
		posOrderExceptionDataBeanTemp.setOrderPlacedDttm(posOrder.getOrderPlacedDttm());
		posOrderExceptionDataBeanTemp.setOwningLocId(posOrder.getOwningLocId());
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting PosExceptionReader getPosOrderExceptionDataBeanDetails()");
			}

		return posOrderExceptionDataBeanTemp;
	}

	
	/**Method used to populate OrderHistoryBean*/
	/**
	 * @param orderHistoryBean
	 * @param posOrder
	 * @param excepTypeId
	 * @return
	 */
	public OrderHistoryBean getOrderHistoryBean(POSOrder posOrder,long excepTypeId) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering PosExceptionReader getOrderHistoryBean()");
			}

		PosQuery posQuery = new PosQuery();
		String action = null;
		List<String> actionNotesList = null;
		String posTranDttmQuery = posQuery.getPosTranDttmQuery();
		String posEmployeeIdQuery = posQuery.getPosEmployeeIdQuery();
		String posTranTypeQuery = posQuery.getPosTranTypeQuery();
		String decodeQuery = posQuery.getDecodeQuery(); 
		
		List<String> tempPosTranDttmList = jdbcTemplate.queryForList(posTranDttmQuery, 
		new Object[] { posOrder.getSysOrderId() }, String.class);
		    if(tempPosTranDttmList.isEmpty() || tempPosTranDttmList.get(0) == null){
			if (LOGGER.isDebugEnabled()) {
			    LOGGER.debug("tempPosTranDttmList is Null for order "+posOrder.getSysOrderId());
		      }
		   }
		
		List<String> posEmployeeIdList = jdbcTemplate.queryForList(posEmployeeIdQuery,
		new Object[] { posOrder.getSysOrderId() }, String.class);
		    if(posEmployeeIdList.isEmpty() || posEmployeeIdList.get(0) == null){
			if (LOGGER.isDebugEnabled()) {
			    LOGGER.debug("posEmployeeIdList is Null for order "+posOrder.getSysOrderId());
		      }
		   }
		
		List<String> posTranTypeList = jdbcTemplate.queryForList(posTranTypeQuery,
		new Object[] { posOrder.getSysOrderId() }, String.class);
		if(posTranTypeList.isEmpty() || posTranTypeList.get(0) == null){
			if (LOGGER.isDebugEnabled()) {
			    LOGGER.debug("posTranTypeList is Null for order "+posOrder.getSysOrderId());
		      }
		   }
		
		if(!posTranTypeList.isEmpty() || posTranTypeList.get(0) != null){
         action = posTranTypeList.get(0);
         actionNotesList = jdbcTemplate.queryForList(decodeQuery,
		  new Object[] {posTranTypeList.get(0)},String.class);
              if(actionNotesList.isEmpty() || actionNotesList.get(0) == null){
 			    if (LOGGER.isDebugEnabled()) {
 			    LOGGER.debug("actionNotesList is Null for pos Transaction Type "+posTranTypeList.get(0));
 		           }
 		        }
		  }		
		
		OrderHistoryBean orderHistoryBeanTemp = new OrderHistoryBean();
		if(action != null 
				&& (!actionNotesList.isEmpty() && actionNotesList.get(0) != null) 
				&& (!tempPosTranDttmList.isEmpty() && tempPosTranDttmList.get(0) != null) 
				&& (!posEmployeeIdList.isEmpty() && posEmployeeIdList.get(0) != null)){
		orderHistoryBeanTemp.setAction(action);
		orderHistoryBeanTemp.setActionDttm(tempPosTranDttmList.get(0));
		orderHistoryBeanTemp.setActionNotes(actionNotesList.get(0));
		orderHistoryBeanTemp.setExceptionId(excepTypeId);
		orderHistoryBeanTemp.setOrderId(posOrder.getSysOrderId());
		orderHistoryBeanTemp.setOrderPlacedDttm(posOrder.getOrderPlacedDttm());
		orderHistoryBeanTemp.setOrderStatus(posOrder.getStatus());
		orderHistoryBeanTemp.setCreateUserId(posEmployeeIdList.get(0));
		}
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting PosExceptionReader getOrderHistoryBean()");
			}

		return orderHistoryBeanTemp;
	}

}
