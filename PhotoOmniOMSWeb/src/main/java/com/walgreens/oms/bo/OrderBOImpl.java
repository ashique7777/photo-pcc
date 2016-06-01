/**
 * RealTimeOrderBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		12 Jan 2015
 *  
 **/

package com.walgreens.oms.bo;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.DateUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.CostCalculationBean;
import com.walgreens.oms.bean.DailyPLUReqBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.EnvelopeOrderDtlsBean;
import com.walgreens.oms.bean.EnvelopeOrderLinePromotionBean;
import com.walgreens.oms.bean.EnvelopeOrderPromotionBean;
import com.walgreens.oms.bean.EnvelopePopupHeaderBean;
import com.walgreens.oms.bean.EnvelopeProductDtlsBean;
import com.walgreens.oms.bean.LateEnvelopeBean;
import com.walgreens.oms.bean.OmOrderAttributeBean;
import com.walgreens.oms.bean.OrderASNDetailsBean;
import com.walgreens.oms.bean.OrderItemLineBean;
import com.walgreens.oms.bean.OrderStatusBean;
import com.walgreens.oms.bean.PLUJsonFilters;
import com.walgreens.oms.bean.PMBYWICDataBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeReqBean;
import com.walgreens.oms.bean.PMByEmployeeResBean;
import com.walgreens.oms.bean.PMByProductFilter;
import com.walgreens.oms.bean.PMReportResponseBean;
import com.walgreens.oms.bean.PMReportResponseBeanList;
import com.walgreens.oms.bean.POFVendorCostReportDetail;
import com.walgreens.oms.bean.POFVendorCostReportMessage;
import com.walgreens.oms.bean.POFVendorCostValidationFilter;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;
import com.walgreens.oms.bean.RoyaltyDataBean;
import com.walgreens.oms.bean.RoyaltyReportResponseBean;
import com.walgreens.oms.bean.SalesBYProductDataBean;
import com.walgreens.oms.bean.SalesByProductResponseBean;
import com.walgreens.oms.bean.VendorData;
import com.walgreens.oms.bean.VendorType;
import com.walgreens.oms.constants.ReportsConstant;
import com.walgreens.oms.dao.OrderDAO;
import com.walgreens.oms.dao.OrdersDAO;
import com.walgreens.oms.factory.OmsDAOFactory;
import com.walgreens.oms.json.bean.ApproveVCDataRequest;
import com.walgreens.oms.json.bean.EnvHistoryBean;
import com.walgreens.oms.json.bean.EnvelopeDtlsDataRespBean;
import com.walgreens.oms.json.bean.EnvelopeNumberFilter;
import com.walgreens.oms.json.bean.ExceptionByEmployeeBean;
import com.walgreens.oms.json.bean.ExceptionByEnvResponseBean;
import com.walgreens.oms.json.bean.ExceptionByEnvelopeBean;
import com.walgreens.oms.json.bean.ExceptionByEnvelopeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeRespBean;
import com.walgreens.oms.json.bean.ExceptionEmployeeResponseList;
import com.walgreens.oms.json.bean.ExceptionReason;
import com.walgreens.oms.json.bean.ExceptionReasonBean;
import com.walgreens.oms.json.bean.ExceptionRepEnv;
import com.walgreens.oms.json.bean.ExceptionTypeBean;
import com.walgreens.oms.json.bean.LCDataRequest;
import com.walgreens.oms.json.bean.LCDataResponse;
import com.walgreens.oms.json.bean.LabelDataRequest;
import com.walgreens.oms.json.bean.LabelDataResponse;
import com.walgreens.oms.json.bean.LabelPrintDetails;
import com.walgreens.oms.json.bean.LateEnvelopeFilter;
import com.walgreens.oms.json.bean.LateEnvelopeReportReqBean;
import com.walgreens.oms.json.bean.LateEnvelopeReportRespBean;
import com.walgreens.oms.json.bean.OrderDataRequest;
import com.walgreens.oms.json.bean.OrderDataResponse;
import com.walgreens.oms.json.bean.OrderDetails;
import com.walgreens.oms.json.bean.OrderDetailsList;
import com.walgreens.oms.json.bean.OrderItem;
import com.walgreens.oms.json.bean.OrderList;
import com.walgreens.oms.json.bean.PMBYWICRequestBean;
import com.walgreens.oms.json.bean.PayOnFulfillmentReqBean;
import com.walgreens.oms.json.bean.PayOnFulfillmentResponse;
import com.walgreens.oms.json.bean.PayonFulfillmentData;
import com.walgreens.oms.json.bean.ProductAttribute;
import com.walgreens.oms.json.bean.ProductDetails;
import com.walgreens.oms.json.bean.RoyaltyRequestBean;
import com.walgreens.oms.json.bean.SalesByProductRequestBean;
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;
import com.walgreens.oms.utility.ServiceUtil;

/**
 * This class is used to implement Business validation as per action
 * 
 * @author CTS
 * @version 1.1 January 12, 2015
 * 
 */

@Component("OrderBO")
public class OrderBOImpl implements OrderBO, PhotoOmniConstants {

	@Autowired
	private OmsDAOFactory omsDAOFactory; // Use to call the DAO factory class

	@Autowired
	private PromotionalMoneyBO promoBo; // Use to call the PM API
	
	@Autowired
	private MBPromotionalMoneyBO mbpmBo; // Use to call the MBPM API

	/**
	 * logger to log the details.
	 */

	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderBOImpl.class);

	/**
	 * This method is used for create request bean as per json message for order
	 * submission
	 * 
	 * @param strJson
	 */
	@Override
	public OrderDataResponse submitOrderDetails(
			OrderDataRequest orderRequestBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submitOrderDetails method of OrderBOImpl ");
		}
		OrderDAO orderDAO = null;
		OrderDataResponse orderResponseBean = null;
		// Insert into OM_SHIPMENT - Starts
		OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
		boolean updateStatus = false; 
		// Insert into OM_SHIPMENT - Ends
		orderDAO = omsDAOFactory.getRealTimeOrdersDao();

		orderResponseBean = new OrderDataResponse();
		List<OrderDetailsList> orderDtlsList = new ArrayList<OrderDetailsList>();
		OrderDetailsList orderDtlsBean = null;
		try {

			MessageHeader message = orderRequestBean.getMessageHeader();
			List<OrderList> orderList = orderRequestBean.getOrderList();
			orderResponseBean.setMessageHeader(message);
			// boolean status;

			for (int i = 0; i < orderList.size(); i++) {
				OrderList orders = orderList.get(i);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Inside for loop submitOrderDetails method of OrderBOImpl "
							+ orders.toString());
				}
				ErrorDetails errorDetails = new ErrorDetails();
				boolean validate = false;
				orderDtlsBean = new OrderDetailsList();
				// Set order attr into order bean
				OrderDetails orderBean = new OrderDetails();
				orderBean.setLocationNumber(orders.getOrder()
						.getLocationNumber());
				orderBean.setOrderId(orders.getOrder().getPcpOrderId());

				orderDtlsBean.setOrderDetails(orderBean);
				int orderValidationStatus = this.validateOrder(orders);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("orderStatus submitOrderDetails method of OrderBOImpl "
							+ orderValidationStatus);
				}

				// status = false;
				OrderStatusBean orderStatusBean = new OrderStatusBean();
				try {
					// check whether this order has orderid
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Inside for loop submitOrderDetails method of OrderBOImpl orderStatus"
								+ orderValidationStatus);
					}
					if (orderValidationStatus == 0) {
						orderStatusBean = orderDAO.submitOrderDetails(orders,
								message);
						if (!orderStatusBean.isStatus()) {
							// if duplicate orders found we are updating through
							
							// complete order process.
							orderStatusBean = orderDAO.completeOrderDetails(
									orders, message);
						}
						// Call Pm & MBPM BO API

						promoBo.calculatePromotionalMoney(
								orderStatusBean.getSysOrderId(), ServiceUtil
										.dateformat24(orderStatusBean
												.getOrderPlacedDTTM()));
						
						mbpmBo.calculateMBPromotionalMoney(orderStatusBean.getSysShoppingCartId(), ServiceUtil
										.dateformat24(orderStatusBean
												.getOrderPlacedDTTM()));
						
						// Insert into OM_SHIPMENT - Starts
						
						if(PhotoOmniConstants.POF_STORE_TYPE_S.equalsIgnoreCase( orders.getOrder().getProcessingType())){
							// If the submitted Employee Id is not Empty , not Internet and not Kiosk , then make an entry in OM_SHIPMENT
							if(!CommonUtil.isNull(orders.getOrder().getEmplTookOrder())
								&& !PhotoOmniConstants.EMPTY_SPACE_CHAR.equalsIgnoreCase(orders.getOrder().getEmplTookOrder())
								&& !PhotoOmniConstants.ORDER_INTERNET.equalsIgnoreCase(orders.getOrder().getEmplTookOrder())	
								&& !PhotoOmniConstants.ORDER_KIOSK.equalsIgnoreCase(orders.getOrder().getEmplTookOrder())){
								
									OrderASNDetailsBean orderASNDetailsBean = ordersDAO.getOrderDetails(Long.valueOf(orders.getOrder().getPcpOrderId()), 
										Integer.parseInt(orderBean.getLocationNumber()));
	
								orderASNDetailsBean.setOrderPlacedDttm(DateUtil.getISO861(orders.getOrder().getSubmittedTime()));
								orderASNDetailsBean.setSysOrderId( orderASNDetailsBean.getSysOrderId());
								orderASNDetailsBean.setShippedDTTM(DateUtil.getISO861(orders.getOrder().getInStorePromisedTime())); // Instore Promised Time
								orderASNDetailsBean.setStatus(PhotoOmniConstants.NEW);
								
								// Insert Record
								LOGGER.debug("Inserting into OM_SHIPMENT");	
								updateStatus = ordersDAO.updateOmShipment(orderASNDetailsBean);
								
								LOGGER.debug("Shipment detail insertion: sysOrderId :"+orderASNDetailsBean.getSysOrderId()+" : "+updateStatus);	
							}
						}
						
						/*Order submission flow need to be modified to 
						 * call cost calculation API in case of INSTANT PRINT ORDERS:*/
					   final String odrSource  = orders.getOrder().getOrderSource();
					   if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(" Order Source for the orde is : " + odrSource);
						}
                       if (!CommonUtil.isNull(odrSource) && "INST".equalsIgnoreCase(odrSource.trim())) {
							this.calculateOrderCost(orderStatusBean.getSysOrderId(),
									ServiceUtil.dateformat24(orderStatusBean.getOrderPlacedDTTM()));
					   }
							
						
						// Insert into OM_SHIPMENT - Ends
					} else {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug("Validation fails in for loop submitOrderDetails method of OrderBOImpl orderStatus"
									+ orderValidationStatus);
						}
						validate = true;
					}
				} catch (Exception e) {
					orderStatusBean.setStatus(false);
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error("Exception occured in Realtime Dao Order Submissions"
								+ e);
					}
					errorDetails = CommonUtil
							.createFailureMessageForDBException(e);
				} finally {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Inside inner finally fails in for loop submitOrderDetails method of OrderBOImpl orderStatus"
								+ orderValidationStatus);
					}
					if (orderStatusBean.isStatus()) {
						orderDtlsBean.setStatus(orderStatusBean.isStatus());
						orderDtlsBean.setErrorDetails(null);
					} else {
						if (validate) {
							errorDetails = CommonUtil
									.createFailureMessageForValidationException(ServiceUtil
											.orderValidatemsg(orderValidationStatus));
						}
						orderDtlsBean.setStatus(orderStatusBean.isStatus());
						orderDtlsBean.setErrorDetails(errorDetails);

					}
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Finally in for loop submitOrderDetails method of OrderBOImpl ");
					}

					orderDtlsList.add(orderDtlsBean);
				}
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Exception occured in Realtime Dao Order Submissions"
						+ e);
			}
		} finally {

			orderResponseBean.setOrderDtlList(orderDtlsList);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit submitOrderDetails method of OrderBOImpl ");
		}
		return orderResponseBean;
	}

	/**
	 * 
	 */
	@Override
	public OrderDataResponse completeOrderDetails(
			OrderDataRequest orderRequestBean) throws PhotoOmniException {

		OrderDAO orderDAO = null;
		OrderDataResponse orderResponseBean = new OrderDataResponse();

		try {
			orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			List<OrderDetailsList> orderDtlsList = new ArrayList<OrderDetailsList>();
			OrderDetailsList orderDtlsBean = null;
			MessageHeader message = orderRequestBean.getMessageHeader();
			List<OrderList> orderList = orderRequestBean.getOrderList();
			orderResponseBean.setMessageHeader(message);
			boolean validate;
			// Insert into OM_SHIPMENT - Starts
			OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
			boolean updateStatus = false; 
			// Insert into OM_SHIPMENT - Ends
			for (int i = 0; i < orderList.size(); i++) {

					OrderList orders = orderList.get(i);
					String orderId = orders.getOrder().getOriginOrderId();
					ErrorDetails errorDetails = new ErrorDetails();
					orderDtlsBean = new OrderDetailsList();
					// Set order attr into order bean
					OrderDetails orderBean = new OrderDetails();
					orderBean.setLocationNumber(orders.getOrder()
							.getLocationNumber());
					orderBean.setOrderId(orders.getOrder().getPcpOrderId());
					int orderStatus = this.validateOrder(orders);
					orderDtlsBean.setOrderDetails(orderBean);
					//status = false;
					validate = false;
					OrderStatusBean orderStatusBean = new OrderStatusBean();
					try {
						
						
						if(orderStatus == 0){
							orderStatusBean = orderDAO.completeOrderDetails(orders, message);
							if(!orderStatusBean.isStatus()){
								errorDetails = CommonUtil.createFailureMessageForValidationException("Invalid pcporderid provided");
							}
							//Call PM & MBPM API to calculate respective promotional money
							promoBo.calculatePromotionalMoney(orderStatusBean.getSysOrderId(), ServiceUtil.dateformat24(orderStatusBean.getOrderPlacedDTTM()));
							mbpmBo.calculateMBPromotionalMoney(orderStatusBean.getSysShoppingCartId(), ServiceUtil.dateformat24(orderStatusBean.getOrderPlacedDTTM()));
							calculateOrderCost(orderStatusBean.getSysOrderId(), ServiceUtil.dateFormatter(orderStatusBean.getOrderPlacedDTTM()).toString().substring(0, 19));
							// Insert into OM_SHIPMENT - Starts
							if(PhotoOmniConstants.POF_STORE_TYPE_S.equalsIgnoreCase( orders.getOrder().getProcessingType())){
								OrderASNDetailsBean orderASNDetailsBean = ordersDAO.getOrderDetails(Long.valueOf(orders.getOrder().getPcpOrderId()), 
																	Integer.parseInt(orderBean.getLocationNumber()));
								
								
								long sysOrderId = orderASNDetailsBean.getSysOrderId();
								LOGGER.debug("sysOrderId    "+sysOrderId);
								String status = orderASNDetailsBean.getStatus();
								LOGGER.debug("status    "+status);
								int locationNo = orderASNDetailsBean.getLocationNumber();
								LOGGER.debug("locationNo    "+locationNo);
								String locationType = orderASNDetailsBean.getLocationType();
								LOGGER.debug("locationType    "+locationType);
								int vendorId = orderASNDetailsBean.getVendorId();
								LOGGER.debug("vendorId    "+vendorId);
								Timestamp orderPlacedDttm = orderASNDetailsBean.getOrderPlacedDttm();
								LOGGER.debug("orderPlacedDttm    "+orderPlacedDttm);
								
								orderASNDetailsBean.setStatus(PhotoOmniConstants.DONE);
								orderASNDetailsBean.setSysOrderId(sysOrderId);
								updateStatus = ordersDAO.updateOmShipmentStatus(orderASNDetailsBean);
								
								LOGGER.debug("Shipment detail insertion: sysOrderId :"+orderASNDetailsBean.getSysOrderId()+" : "+updateStatus);
							// Insert into OM_SHIPMENT - Ends
							}
						} else {
							validate = true;
						}
						
				} catch (Exception e) {
					orderStatusBean.setStatus(false);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Exception occured in Realtime Dao Order Submissions --> "
								+ orderId);
						;
					}
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error("Exception occured in Realtime Dao Order Submissions");
					}
					errorDetails = CommonUtil
							.createFailureMessageForDBException(e);
				} finally {
					if (orderStatusBean.isStatus()) {
						orderDtlsBean.setStatus(orderStatusBean.isStatus());
						orderDtlsBean.setErrorDetails(null);
					} else {
						if (validate) {
							errorDetails = CommonUtil
									.createFailureMessageForValidationException(ServiceUtil
											.orderValidatemsg(orderStatus));
						}
						orderDtlsBean.setStatus(orderStatusBean.isStatus());
						orderDtlsBean.setErrorDetails(errorDetails);

					}
					orderDtlsList.add(orderDtlsBean);
				}

			}
			orderResponseBean.setOrderDtlList(orderDtlsList);

		} catch (Exception e) {
			throw new PhotoOmniException(e.getMessage());
			// Write logging message
		} finally {
			// Write logging message
		}
		return orderResponseBean;
	}

	/**
	 * This method handles all type of business logic for exception .
	 * 
	 * @param strJson
	 *            json value will be hold by this variable.
	 * @return RealTimeOrderResponse.
	 */
	public OrderDataResponse updateOrderExceptions(OrderDataRequest orderRequestBean)
			throws PhotoOmniException {
		LOGGER.info(" Entering orderException method of RealTimeOrderBOImpl ");
		OrderDataResponse orderResponseBean = new OrderDataResponse();
		OrderDAO orderDAO = null;
				
				List<OrderDetailsList> orderDtlsList = new ArrayList<OrderDetailsList>();
				OrderDetailsList orderDtlsBean = null;

				try {
					
					MessageHeader message = orderRequestBean.getMessageHeader();
					List<OrderList> orderList = orderRequestBean.getOrderList();
					orderResponseBean.setMessageHeader(message);					
					boolean validate;
					OrderStatusBean orderStatusBean = new OrderStatusBean();
					for (int i = 0; i < orderList.size(); i++) {

						OrderList orders = orderList.get(i);
						ErrorDetails errorDetails = new ErrorDetails();
						orderDtlsBean = new OrderDetailsList();
						// Set order attr into order bean
						OrderDetails orderBean = new OrderDetails();
						orderBean.setLocationNumber(orders.getOrder()
								.getLocationNumber());
						orderBean.setOrderId(orders.getOrder().getPcpOrderId());
						int orderStatus = this.validateOrder(orders);
						orderDtlsBean.setOrderDetails(orderBean);
						validate= false;
						//status = false;
						try{
							orderDAO = omsDAOFactory.getRealTimeOrdersDao();
							if(orderStatus == 0){
								orderStatusBean = orderDAO.updateOrderExceptions(orders, message);
								if(!orderStatusBean.isStatus()){
									errorDetails = CommonUtil.createFailureMessageForValidationException("Invalid pcporderid provided");
								}
								
								//Call PM & MBPM API to calculate respective promotional money
								promoBo.calculatePromotionalMoney(orderStatusBean.getSysOrderId(), ServiceUtil.dateformat24(orderStatusBean.getOrderPlacedDTTM()));
								mbpmBo.calculateMBPromotionalMoney(orderStatusBean.getSysShoppingCartId(), ServiceUtil.dateformat24(orderStatusBean.getOrderPlacedDTTM()));
								Date completedttm = null;
								Date orderPlacedDttm = null;
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								if(!StringUtils.isEmpty(orderRequestBean.getOrderList().get(0).getOrder().getCompletionTime())){
									
									completedttm = sdf.parse(ServiceUtil.dateformat24(orderRequestBean.getOrderList().get(0).getOrder().getCompletionTime()));
								}
								if(!StringUtils.isEmpty(orderRequestBean.getOrderList().get(0).getOrder().getSubmittedTime())){
									orderPlacedDttm = sdf.parse(ServiceUtil.dateformat24(orderRequestBean.getOrderList().get(0).getOrder().getSubmittedTime()));
									
								}
								if(completedttm != null && completedttm.after(orderPlacedDttm)){
									// calculating cost only for completed orders
									calculateOrderCost(orderStatusBean.getSysOrderId(), ServiceUtil.dateFormatter(orderStatusBean.getOrderPlacedDTTM()).toString().substring(0, 19));
								}
							} else {
								validate = true;
							}

						} catch (Exception e) {
						//	status = false;
							orderStatusBean.setStatus(false);
							if (LOGGER.isErrorEnabled()) {
								LOGGER.error(" Error occoured at updateException method of RealTimeOrderBOImpl - "
										+ e.getMessage());
							}
							errorDetails = CommonUtil.createFailureMessageForDBException(e);
						} finally {
							if (orderStatusBean.isStatus()) {
								orderDtlsBean.setStatus(orderStatusBean.isStatus());
								orderDtlsBean.setErrorDetails(null);
							} else {
								if(validate){
									errorDetails = CommonUtil.createFailureMessageForValidationException(ServiceUtil.orderValidatemsg(orderStatus));
								}
								orderDtlsBean.setStatus(orderStatusBean.isStatus());
								orderDtlsBean.setErrorDetails(errorDetails);

							}
							orderDtlsList.add(orderDtlsBean);
						}

					}
					orderResponseBean.setOrderDtlList(orderDtlsList);
				}catch (Exception e) {
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error(" Error occoured at updateException method of RealTimeOrderBOImpl - "
								+ e.getMessage());
					}
					throw new PhotoOmniException(e.getMessage());
				} finally {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.info(" Exiting orderException method of RealTimeOrderBOImpl ");
					}
				}

		return orderResponseBean;
	}

	/**
	 * @param jsonRequest
	 * @return
	 * @throws PhotoOmniException
	 */
	public LCDataResponse updateLicenseContent(LCDataRequest jsonRequestMsg)
			throws PhotoOmniException {

		LOGGER.info("Entering into LicenseCntBOImpl.setLcDetail method");
		OrderDAO orderDAO = null;
		LCDataResponse lcOrderResponse = null;
		try {
			orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			LOGGER.info("json value obtained from store :" + jsonRequestMsg);
			lcOrderResponse = orderDAO.updateLicenseContent(jsonRequestMsg);
		} catch (PhotoOmniException e) {
			LOGGER.error("photo exception occurred in LicenseCntBOImpl.setLcDetail"
					+ e.getMessage());
		} catch (Exception exception) {
			LOGGER.error("photo exception occurred in LicenseCntBOImpl.setLcDetail"
					+ exception.getMessage());
		} finally {
			LOGGER.info("Exiting from LicenseCntBOImpl.setLcDetail method");
		}
		return lcOrderResponse;
	}

	/**
	 * This method is used for create request bean as per json message for label
	 * data print details
	 * 
	 * @param strJson
	 */
	@Override
	public LabelDataResponse updateLabelPrntInfo(
			LabelDataRequest labelDataRequest) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering updateLDPrintDetails method of LabelDataBOImpl ");
		}
		OrderDAO orderDAO = null;
		LabelDataResponse labelDataresponse = null;
		// Insert into OM_SHIPMENT - Starts
		boolean updateStatus = false;
		boolean isRecordPresent = false;
		OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
		String estimatedDTM = PhotoOmniConstants.EMPTY_SPACE_CHAR;
		String estShipDTM = "";
		String strEstDTM ="";
		String processingtype = "";
		// Insert into OM_SHIPMENT - Ends
		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("BO Impl Start....");
			}
			// LabelDataRequest labelDataRequest =
			// ServiceUtil.getLabelRequestJson(strJson);
			if (null != labelDataRequest) {
				orderDAO = omsDAOFactory.getRealTimeOrdersDao();
				labelDataresponse = orderDAO
						.updateLabelPrntInfo(labelDataRequest);
				// Call PM & MBPM API for promotion calculation
				promoBo.calculatePromotionalMoney(labelDataresponse.getLabelPrintDetails().get(0).getSysOrderId(), labelDataresponse.getLabelPrintDetails().get(0).getOrderPlacedDTTM());
				
				mbpmBo.calculateMBPromotionalMoney(labelDataresponse.getLabelPrintDetails().get(0).getSysShoppingCartId(), labelDataresponse.getLabelPrintDetails().get(0).getOrderPlacedDTTM());
				
				
				// Insert into OM_SHIPMENT - Starts
				if(!labelDataresponse.getLabelPrintDetails().isEmpty()){
					for (LabelPrintDetails labelPrintDetail : labelDataresponse.getLabelPrintDetails()) {
						
					OrderASNDetailsBean orderASNDetailsBean = ordersDAO.getOrderDetails(Long.valueOf(labelPrintDetail.getOrderId()), 
							Integer.valueOf(Long.valueOf(labelPrintDetail.getLocationNumber()).toString()));
					
					
					if(null!= labelPrintDetail.getOrderPlacedDTTM()){
						estimatedDTM = CommonUtil.stringDateFormatChange(labelPrintDetail.getOrderPlacedDTTM(),"yyyy-MM-dd HH:mm:ss", 
												PhotoOmniConstants.STORE_DATE_PATTERN);
					}	
					LOGGER.debug("sysOrderId "+orderASNDetailsBean.getSysOrderId());
					// Get the processing type value from OM_ORDER_ATTRIBUTE table
						processingtype = ordersDAO.getProcessingType(orderASNDetailsBean.getSysOrderId(), ServiceUtil.dateFormatter(estimatedDTM));
						if(PhotoOmniConstants.POF_STORE_TYPE_S.equalsIgnoreCase(processingtype)){
							/*
							 *  Check , already record is present in OM_SHIPMENT with
							 *  SYS_ORDER_ID and ORDER_PLACED_DTM
							 */
							strEstDTM = ordersDAO.getEstDTM(Long.valueOf(labelPrintDetail.getOrderId()), 
									Integer.valueOf(Long.valueOf(labelPrintDetail.getLocationNumber()).toString()));
							
							if(null!= strEstDTM){
								estShipDTM = CommonUtil.stringDateFormatChange(strEstDTM,"yyyy-MM-dd HH:mm:ss", 
														PhotoOmniConstants.STORE_DATE_PATTERN);
							}
							isRecordPresent = ordersDAO.checkRecordPresent(orderASNDetailsBean.getSysOrderId(),ServiceUtil.dateFormatter(estimatedDTM));
							if(!isRecordPresent){
								// insert record in OM_SHIPMENT
								orderASNDetailsBean.setSysOrderId( orderASNDetailsBean.getSysOrderId());
								orderASNDetailsBean.setTrackingNo(PhotoOmniConstants.EMPTY_SPACE_CHAR);
								orderASNDetailsBean.setShipmentCompany(PhotoOmniConstants.EMPTY_SPACE_CHAR); 
								orderASNDetailsBean.setShippedDTTM(ServiceUtil.dateFormatter(estShipDTM)); // Order Placed DTTM
								orderASNDetailsBean.setStatus(PhotoOmniConstants.NEW);
								
								LOGGER.debug("Inserting into OM_SHIPMENT");	
								updateStatus = ordersDAO.updateOmShipment(orderASNDetailsBean);
							}
							
							LOGGER.debug("Shipment detail insertion: sysOrderId :"+orderASNDetailsBean.getSysOrderId()+" : "+updateStatus);	
						}
					
					}
				}
				
				// Insert into OM_SHIPMENT - Ends
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("BO Impl Ends....");
			}
		} catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" PhotoOmniException occoured at updateLDPrintDetails method of LabelDataBOImpl - "
						+ e.getMessage());
			}
			throw new PhotoOmniException(
					"Label Data print BO update machine downtime details .. "
							+ e.getMessage());
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at updateLDPrintDetails method of LabelDataBOImpl - "
						+ e.getMessage());
			}
			throw new PhotoOmniException(
					"Label Data print BO update label Data print details .. "
							+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting updateLDPrintDetails method of LabelDataBOImpl ");
			}
		}
		return labelDataresponse;
	}

	/*
	 * @param PMByProductFilter
	 * 
	 * @return PMReportResponseBeanList
	 */
	@Override
	public PMReportResponseBeanList fetchPMEarnedByProduct(
			PMByProductFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering fetchPMEarnedByProduct method of OrderBOImpl ");
		}
		List<PMReportResponseBean> responseBeanList = null;
		PMReportResponseBeanList responseList = new PMReportResponseBeanList();
		OrderDAO orderDAO = null;
		try {
			LOGGER.info("BO Impl Start....");
			orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			String shortColumn = this.getSortColumnForPmByProdRep(requestBean
					.getSortColumnName());
			requestBean.setSortColumnName(shortColumn);
			responseBeanList = orderDAO.fetchPMEarnedByProduct(requestBean);
			/*
			 * CALCULATE TOTAL ROWS FOR PM PRODUCTS
			 */
			if (!(CommonUtil.isNull(responseBeanList) || responseBeanList
					.isEmpty())) {
				PMReportResponseBean totalRowsBean = new PMReportResponseBean();
				double totalPmEarnedAmt = 0.0;
				double totalPmPerProduct = 0.0;
				long totalPmEnteredQty = 0;
				double totalPmEnteredAmt = 0.0;
				long totalPmEarnedQty = 0;
				long totalPmPendingQty = 0;
				double totalPmPendingAmt = 0.0;
				for (PMReportResponseBean pmReportResponseBean : responseBeanList) {
					totalPmEarnedAmt = totalPmEarnedAmt
							+ pmReportResponseBean.getPmEarnedAmt();
					totalPmEarnedQty = totalPmEarnedQty
							+ pmReportResponseBean.getPmEarnedQty();
					totalPmPerProduct = totalPmPerProduct
							+ pmReportResponseBean.getPmPerProduct();
					totalPmEnteredQty = totalPmEnteredQty
							+ pmReportResponseBean.getPmEnteredQty();
					totalPmEnteredAmt = totalPmEnteredAmt
							+ pmReportResponseBean.getPmEnteredAmt();
					totalPmPendingQty = totalPmPendingQty
							+ pmReportResponseBean.getPmPendingQty();
					totalPmPendingAmt = totalPmPendingAmt
							+ pmReportResponseBean.getPmPendingAmt();
					totalRowsBean.setProductDesc("Store Total");
					totalRowsBean.setBoldCheck(true);
					totalRowsBean.setPmEarnedAmt(totalPmEarnedAmt);
					totalRowsBean.setPmEarnedQty(totalPmEarnedQty);
					totalRowsBean.setPmPerProduct(totalPmPerProduct);
					totalRowsBean.setPmEnteredQty(totalPmEnteredQty);
					totalRowsBean.setPmEnteredAmt(totalPmEnteredAmt);
					totalRowsBean.setPmPendingAmt(totalPmPendingAmt);
					totalRowsBean.setPmPendingQty(totalPmPendingQty);

					// to be removed
					// responseBeanList.add(pmReportResponseBean);
				}
				responseBeanList.add(totalRowsBean);
				responseList.setEmployeeName(responseBeanList.get(0)
						.getEmployeeLastName()
						+ ","
						+ responseBeanList.get(0).getEmployeeFirstName());
				// responseList.setTotalRows(responseBeanList.get(0).getTotalRows());
			} else {
				PMReportResponseBean totalRowsBean = new PMReportResponseBean();
				totalRowsBean.setProductDesc("Store Total");
				totalRowsBean.setBoldCheck(true);
				responseBeanList.add(totalRowsBean);
				// responseList.setTotalRows(1);
			}
			responseList.setPmReportResponseBeanList(responseBeanList);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at fetchPMEarnedByProduct method of OrderBOImpl - "
						+ e.getMessage());
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting fetchPMEarnedByProduct method of OrderBOImpl ");
			}
		}
		return responseList;
	}

	/**
	 * Method to get the Sort Column for PM by Product report
	 * 
	 * @param sortColumnName
	 * @return DBColumnName
	 */
	private String getSortColumnForPmByProdRep(String sortColumnName) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnForPmByProdRep method of OrderBOImpl ");
		}
		String shortColumn = "DESCRIPTION";
		try {
			Map<String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("productDesc", "DESCRIPTION");
			dbColumnName.put(null, "DESCRIPTION");
			shortColumn = dbColumnName.get(sortColumnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnForPmByProdRep method of OrderBOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnForPmByProdRep method of OrderBOImpl ");
			}
		}
		return shortColumn;

	}

	/**
	 * BO Method to validate PMBYWIC report request 
	 * and to make a DAO call to save the request
	 * 
	 * @param PMBYWICRequestBean 
	 * @return PMBYWICResponseBean
	 */
	public PMBYWICResponseBean submitPMWICReportRequest(
			PMBYWICRequestBean pmbywicRequestBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering orderBOImpl.submitPMWICReportRequest() method");
		}
		PMBYWICResponseBean pmbywicResponseBean = new PMBYWICResponseBean();
		OrderDAO pmReportDAO = omsDAOFactory.getRealTimeOrdersDao();
		try {
			PMBYWICDataBean pmbywicDataBean = pmbywicRequestBean
					.getPmbywicDataBean();
			// Validate StartDate, EndDate and UserName
			pmbywicDataBean.setStartDate(CommonUtil.stringDateFormatChange(
					pmbywicDataBean.getStartDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			pmbywicDataBean.setEndDate(CommonUtil.stringDateFormatChange(
					pmbywicDataBean.getEndDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			String userNames = CommonUtil
					.validateUserName(getuserNameList(pmbywicDataBean
							.getEmailIds()));
			pmbywicDataBean.setEmailIds(userNames);
			// DAO call to submit royalty report
			pmbywicResponseBean = pmReportDAO
					.submitPMWICReportRequest(pmbywicDataBean.toString());
		} catch (PhotoOmniException e) {
			LOGGER.error("Exception occured at orderBOImpl.submitPMWICReportRequest() method" + e);
			pmbywicResponseBean.setStatus(false);
			pmbywicResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForValidationException(e.getMessage().toString()));
			pmbywicResponseBean.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} catch (Exception e) {
			LOGGER.error("Exception occured at orderBOImpl.submitPMWICReportRequest() method"	+ e);
			pmbywicResponseBean.setStatus(false);
			pmbywicResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			pmbywicResponseBean.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from orderBOImpl.submitPMWICReportRequest() method");
			}
		}
		return pmbywicResponseBean;
	}

	/**
	 * Method to get user name list
	 * 
	 * @param userNames
	 *            - > UserName String
	 * @return List<String> --> List of userNames
	 * @throws PhotoOmniException
	 */
	private List<String> getuserNameList(String userNames)
			throws PhotoOmniException {
		LOGGER.info("Entering getEmailIdlsit method of orderBOImpl.");
		List<String> emailIds = new ArrayList<String>();
		try {
			if (null != userNames && !userNames.isEmpty()) {
				List<String> emailIdList = Arrays.asList(userNames
						.split(ReportsConstant.DELIMITER));
				for (String emailId : emailIdList) {
					emailIds.add(emailId.trim());
				}
			} else {
				throw new PhotoOmniException(
						"Invalid user name or user name is emppty");
			}
		} catch (Exception e) {
			LOGGER.error("Exception in  getuserNameList method of orderBOImpl>>----> "
					+ e.getMessage());
			throw new PhotoOmniException(e);
		} finally {
			LOGGER.debug("Exiting from  getuserNameList method of orderBOImpl.");
		}
		return emailIds;
	}

	/**
	 * Bo method to validate royalty reprot request
	 * 
	 * @param RoyaltyRequestBean
	 * @return RoyaltyReportResponseBean
	 */
	public RoyaltyReportResponseBean submitRoyaltyReportRequest(
			RoyaltyRequestBean royaltyRequestBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderBOImpl.submitRoyaltyReportRequest method");
		}
		RoyaltyReportResponseBean royaltyReportResponseBean = new RoyaltyReportResponseBean();
		OrderDAO royaltyReportDAO = omsDAOFactory.getRealTimeOrdersDao();
		try {
			RoyaltyDataBean royaltyDataBean = royaltyRequestBean
					.getRoyaltyDataBena();
			// Validate StartDate, EndDate and UserName
			royaltyDataBean.setStartDate(CommonUtil.stringDateFormatChange(
					royaltyDataBean.getStartDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			royaltyDataBean.setEndDate(CommonUtil.stringDateFormatChange(
					royaltyDataBean.getEndDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			String userNames = CommonUtil
					.validateUserName(getuserNameList(royaltyDataBean.getEmailIds()));
			royaltyDataBean.setEmailIds(userNames);
			// DAO call to submit royalty report
			royaltyReportResponseBean = royaltyReportDAO
					.submitRoyaltyReportRequest(royaltyDataBean.toString());
		} catch (PhotoOmniException e) {
			LOGGER.error("Exception occured at OrderBOImpl.submitRoyaltyReportRequest method "+ e);
			royaltyReportResponseBean.setStatus(false);
			royaltyReportResponseBean
			.setErrorDetails(CommonUtil
					.createFailureMessageForValidationException(e
							.getMessage()));
			royaltyReportResponseBean.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} catch (Exception e) {
			LOGGER.error("Exception occured at OrderBOImpl.submitRoyaltyReportRequest method "+ e);
			royaltyReportResponseBean.setStatus(false);
			royaltyReportResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			royaltyReportResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderBOImpl.submitRoyaltyReportRequest method");
			}
		}
		return royaltyReportResponseBean;
	}

	/**
	 * Method to get Royalty vendor list details from DAO layer
	 * 
	 * @return vendorData  -- Royalty vendor data ben
	 * @throws PhotoOmniException -- Cusotm Exception
	 */
	public VendorData getRoyaltyVendorNameList() throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderBOImpl.getRoyaltyVendorNameList() method ");
		}
		List<VendorType> lstVendorDetails = null;
		VendorData objVendorData = new VendorData();
		try {
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			lstVendorDetails = objReportsDAO.getRoyaltyVendorNameList();
			if (lstVendorDetails != null && lstVendorDetails.size() > 0) {
				objVendorData.setVendor(lstVendorDetails);
			}

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at OrderBOImpl.getRoyaltyVendorNameList() method - "+ e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderBOImpl.getRoyaltyVendorNameList() method - "+ e);
			throw new PhotoOmniException(e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderBOImpl.getRoyaltyVendorNameList() method ");
			}
		}
		return objVendorData;
	}

	/**
	 * Method to get product Filter details from code decode
	 * 
	 * @param model      .
	 * @return List<Map<String, Object>> -- List of map containing key value pair of filter details
	 */
	@Override
	public List<Map<String, Object>> getCodeDecodeValue(String codeType) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderBOImpl.getCodeDecodeValue() method");
		}
		List<Map<String, Object>> resultList = null;
		try {
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			resultList = orderDAO.getCodeDecodeValue(codeType);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderBOImpl.getCodeDecodeValue() method "+ e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderBOImpl.getCodeDecodeValue() method");
			}
		}
		return resultList;
	}

	/**
	 * Method to submit royalty custom report
	 * 
	 * @param SalesByProductRequestBean
	 * @return SalesByProductResponseBean
	 */
	@Override
	public SalesByProductResponseBean saveSalesReportByProductData(
			SalesByProductRequestBean salesByProductRequestBean) {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entering into OrderBOImpl.saveSalesReportByProductData method");
		}
		SalesByProductResponseBean salesByProductResponseBean = new SalesByProductResponseBean();
		OrderDAO salesReportDAO = omsDAOFactory.getRealTimeOrdersDao();
		try {
			SalesBYProductDataBean salesBYProductDataBean = salesByProductRequestBean
					.getSalesBYProductDataBean();
			// Validate StartDate, EndDate and UserName
			salesBYProductDataBean.setStartDate(CommonUtil
					.stringDateFormatChange(
							salesBYProductDataBean.getStartDate(),
							PhotoOmniConstants.CALENDER_DATE_PATTERN,
							PhotoOmniConstants.CALENDER_DATE_PATTERN));
			salesBYProductDataBean.setEndDate(CommonUtil
					.stringDateFormatChange(
							salesBYProductDataBean.getEndDate(),
							PhotoOmniConstants.CALENDER_DATE_PATTERN,
							PhotoOmniConstants.CALENDER_DATE_PATTERN));
			String userNames = CommonUtil
					.validateUserName(getuserNameList(salesBYProductDataBean
							.getEmailIds()));
			salesBYProductDataBean.setEmailIds(userNames);
			// DAO call to submit royalty report
			salesByProductResponseBean = salesReportDAO
					.saveSalesReportByProductData(salesBYProductDataBean
							.toString());

		} catch (PhotoOmniException e) {
			LOGGER.error("Exception occured in  OrderBOImpl.saveSalesReportByProductData method "
					+ e);
			salesByProductResponseBean.setStatus(false);
			salesByProductResponseBean
					.setErrorDetails(CommonUtil
							.createFailureMessageForValidationException(e
									.getMessage()));
			salesByProductResponseBean
					.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} catch (Exception e) {
			LOGGER.error("Exception occured in  OrderBOImpl.saveSalesReportByProductData method"
					+ e);
			salesByProductResponseBean.setStatus(false);
			salesByProductResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			salesByProductResponseBean
					.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from  OrderBOImpl.saveSalesReportByProductData method");
			}
		}
		return salesByProductResponseBean;
	}

	@Override
	public DailyPLUResBean submitPLUReportRequest(
			DailyPLUReqBean dailyPLUReqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderBOImpl.submitPLUReportRequest()");
		}
		LOGGER.info("Entering into OrderBOImpl.submitPLUReportRequest()");
		OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
		DailyPLUResBean dailyPLUResBean = new DailyPLUResBean();
		PLUJsonFilters pluJsonFilters = new PLUJsonFilters();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			List<String> emailIdList = Arrays.asList(dailyPLUReqBean
					.getPluJsonFilters().getEmailIds()
					.split(ReportsConstant.splitExpression));
			String emailIds = CommonUtil.validateUserName(emailIdList);
			pluJsonFilters.setStartDate(CommonUtil.stringDateFormatChange(
					dailyPLUReqBean.getPluJsonFilters().getStartDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			pluJsonFilters.setEndDate(CommonUtil.stringDateFormatChange(
					dailyPLUReqBean.getPluJsonFilters().getEndDate(),
					PhotoOmniConstants.CALENDER_DATE_PATTERN,
					PhotoOmniConstants.CALENDER_DATE_PATTERN));
			pluJsonFilters.setEmailIds(emailIds);
			pluJsonFilters.setPluNumber(dailyPLUReqBean.getPluJsonFilters()
					.getPluNumber());
			dailyPLUResBean = objReportsDAO.submitPLUReportRequest(objectMapper
					.writeValueAsString(pluJsonFilters));
		} catch (PhotoOmniException e) {
			LOGGER.error("Exception occured in submitPLUReportRequest method of OrderBoImpl "
					+ e.getMessage());
			dailyPLUResBean.setPluStatus(false);
			dailyPLUResBean.setResponse(ReportsConstant.FAILURE_MESSAGE);
			dailyPLUResBean
					.setErrorDetails(CommonUtil
							.createFailureMessageForValidationException(e
									.getMessage()));
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderBOImpl.submitPLUReportRequest() While parsing Request"
					+ e.getMessage());
			dailyPLUResBean.setPluStatus(false);
			dailyPLUResBean.setResponse(ReportsConstant.FAILURE_MESSAGE);
			dailyPLUResBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
		}
		dailyPLUResBean.setMessageHeader(dailyPLUReqBean.getMessageHeader());
		return dailyPLUResBean;
	}

	@Override
	public DailyPLUResBean getPLUList(DailyPLUReqBean dailyPLUReqBean) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into getPLUList method of OrderBOImpl");
		}
		LOGGER.info("Entering into getPLUList method of OrderBOImpl");
		OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
		DailyPLUResBean dailyPLUResBean = new DailyPLUResBean();
		int totalPages = 0;
		try {
			int pluRecPerPage = objReportsDAO.getPluReportRecordPerPageCount();
			if (dailyPLUReqBean.getPluKey() != null
					&& dailyPLUReqBean.getPluKey().length() == 0) {
				List<String> lstPluNumberAll = objReportsDAO.getPLUListAll(
						dailyPLUReqBean.getCurrentPageReq(), pluRecPerPage);
				dailyPLUResBean.setPluNumber(lstPluNumberAll);
				int totalRecord = objReportsDAO.getPLUListAllCount();
				if (lstPluNumberAll.size() > 0) {
					int a = totalRecord
							+ (pluRecPerPage - (totalRecord % pluRecPerPage));
					totalPages = a / pluRecPerPage;
					if (totalRecord % pluRecPerPage == 0) {
						totalPages = totalPages - 1;
					}
				}
				dailyPLUResBean.setTotalRecord(totalRecord);
				dailyPLUResBean.setTotalPages(totalPages);
			} else {
				long pluKey = Long.parseLong(dailyPLUReqBean.getPluKey());
				List<String> lstPluNumber = objReportsDAO.getPLUList(
						String.valueOf(pluKey),
						dailyPLUReqBean.getCurrentPageReq(), pluRecPerPage);
				dailyPLUResBean.setPluNumber(lstPluNumber);
				int totalRecord = objReportsDAO.getPLUListCount(dailyPLUReqBean
						.getPluKey());
				if (lstPluNumber.size() > 0) {
					int a = totalRecord
							+ (pluRecPerPage - (totalRecord % pluRecPerPage));
					totalPages = a / pluRecPerPage;
					if (totalRecord % pluRecPerPage == 0) {
						totalPages = totalPages - 1;
					}
				}
				dailyPLUResBean.setTotalRecord(totalRecord);
				dailyPLUResBean.setTotalPages(totalPages);
			}
			dailyPLUResBean.setCurrentPage(dailyPLUReqBean.getCurrentPageReq());
			dailyPLUResBean
					.setMessageHeader(dailyPLUReqBean.getMessageHeader());
		} catch (Exception e) {
			LOGGER.error(" Error  occoured at getPLUList method of OrderBOImpl - "
					+ e.getMessage());
			dailyPLUResBean.setPluStatus(false);
			dailyPLUResBean
					.setErrorDetails(CommonUtil
							.createFailureMessageForValidationException(e
									.getMessage()));
		} finally {
			LOGGER.info("Exiting from getPLUList method of OrderBOImpl");
		}
		return dailyPLUResBean;
	}

	public List<PayonFulfillmentData> getStoreDetails(String storeNumber) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getStoreDetails method of OrderBOImpl ");
		}
		List<PayonFulfillmentData> dataList = null;
		try {
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			dataList = orderDAO.getStoreDetails(storeNumber);

		} catch (Exception e) {
			LOGGER.error(" Error  occoured at getStoreDetails method of OrderBOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getStoreDetails method of OrderBOImpl ");
			}
		}

		return dataList;
	}

	public List<ProductDetails> getProductDetails() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getProductDetails method of OrderBOImpl ");
		}
		// <ProductDetails> storeList = null;
		// Storedata storeData = new Storedata();
		List<ProductDetails> productList = null;
		try {
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			productList = orderDAO.getProductDetails();

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getProductDetails method of OrderBOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getProductDetails method of OrderBOImpl ");
			}
		}

		return productList;
	}

	public PayOnFulfillmentResponse payOnFulfillmentReportRequest(
			PayOnFulfillmentReqBean reqBean) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getStoreDetails method of OrderBOImpl ");
		}
		PayOnFulfillmentResponse responseBean = null;
		try {
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();

			if (reqBean.getFilter().isStorePrint()) {
				LOGGER.info(" Entering payOnFulfillmentStrorePrintReport method of OrderBOImpl ");
				responseBean = orderDAO					
						.payOnFulfillmentStorePrintReport(reqBean.getFilter());
			} else {
				LOGGER.info(" Entering payOnFulfillmentReportRequest method of OrderBOImpl ");
				responseBean = orderDAO.payOnFulfillmentReportRequest(reqBean
						.getFilter());
			}
			LOGGER.info(" Initialize Response Bean in  payOnFulfillmentReportRequest");
			String storeWithAddress = ordersDAO.getLocationAddress(reqBean
					.getFilter().getStoreNumber());
			responseBean.setStoreWithAddress(storeWithAddress);
			responseBean.setMessageHeader(reqBean.getMessageHeader());
			responseBean.setCurrrentPage(reqBean.getFilter().getCurrrentPage());
			responseBean.setFiltertypePay(reqBean.getFilter()
					.getFiltertypePay());
			responseBean.setStorePrint(reqBean.getFilter().isStorePrint());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at payOnFulfillmentReportRequest method of OrderBOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentReportRequest method of OrderBOImpl ");
			}
		}

		return responseBean;

	}

	/**
	 * Description : This method used for accessing DAO for CSV
	 */
	public List<PayOnFulfillmentCSVRespData> payOnFulfillmentCSVReportRequest(
			PayOnFulfillmentReqBean reqBean) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering payOnFulfillmentCSVReportRequest method of OrderBOImpl ");
		}
		List<PayOnFulfillmentCSVRespData> responseList = null;

		try {
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			responseList = orderDAO.payOnFulfillmentCSVReportRequest(reqBean
					.getFilter());

		} catch (Exception e) {

			LOGGER.error(" Error occoured at payOnFulfillmentCSVReportRequest method of OrderBOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentCSVReportRequest method of OrderBOImpl ");
			}
		}

		return responseList;

	}

	/**
	 * Method to get the PM Employee reports informations like EmployeeName ,
	 * DollersEarned, EarnedQty etc... for store level or employee level
	 * 
	 * @param objPMByEmployeeReqBean
	 *            contains MessageHeader and Employee informations like
	 *            EmployeeId, StoreNumber, filters selected etc... in JSON
	 * @return EmployeeDataResponse MessageHeader, ErrorDetails, List of PM
	 *         Employee reports object informations like EmployeeName ,
	 *         DollersEarned, EarnedQty etc....
	 * @since v1.0
	 */
	public PMByEmployeeResBean getPmByEmployeeData(
			PMByEmployeeReqBean objPMByEmployeeReqBean, boolean isFromPrintPage)
					throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderBOImpl.getPMByEmployeeReportDetails()");
		}
		OrderDAO orderDAO = null;
		PMByEmployeeResBean objPMByEmployeeResBean = null;
		try {
			orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			objPMByEmployeeResBean = orderDAO.getPmByEmployeeReportData(
					objPMByEmployeeReqBean, isFromPrintPage);
		} catch (Exception e) {
			LOGGER.error("Error occoured at OrderBOImpl.getPMByEmployeeReportDetails()  >>----> "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from OrderBOImpl.getPMByEmployeeReportDetails().");
			}
		}
		return objPMByEmployeeResBean;
	}

	/**
	 * Common Method to get the Employee Name or Store Location Address.
	 * 
	 * @param strEmpORLocID
	 *            contains EmployeeID if Employee NAme is required else Store
	 *            Number to get Store Address.
	 * @param isEmployeeName
	 *            contains true if employee name required else false for Store
	 *            address.
	 * @return strEmpNameOrStoreAdd contains Employee Name orStore Location
	 *         Address.
	 * @since v1.0
	 */
	public String getEmployeeNameStoreAdd(String strEmpORLocID,
			boolean isEmployeeName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderBOImpl.getEmployeeNameStoreAdd()");
		}
		OrderDAO orderDAO = null;
		String strEmpNameORStrAdd = "";
		try {
			orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			strEmpNameORStrAdd = orderDAO.getEmployeeNameStoreAdd(
					strEmpORLocID, isEmployeeName);
		} catch (Exception e) {
			LOGGER.error("Error occoured at OrderBOImpl.getEmployeeNameStoreAdd()  >>----> "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from OrderBOImpl.getEmployeeNameStoreAdd().");
			}
		}
		return strEmpNameORStrAdd;
	}

	/**
	 * Method to get the vendor data
	 * 
	 * @return vendorData
	 * @throws PhotoOmniException
	 */
	public VendorData getPOFVendorNameList() throws PhotoOmniException {

		List<VendorType> lstVendorDetails = null;
		VendorData objVendorData = new VendorData();
		try {
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			lstVendorDetails = objReportsDAO.getPOFVendorNameList();
			if (lstVendorDetails != null && lstVendorDetails.size() > 0) {
				objVendorData.setVendor(lstVendorDetails);
			}

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getPOFVendorNameList method of OrderBOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFVendorNameList method of OrderBOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}
		return objVendorData;
	}

	/**
	 * Method to get the vendor data
	 * 
	 * @return objVendorData
	 * @throws PhotoOmniException
	 */
	public VendorData getPOFApproveVendorNameList() throws PhotoOmniException {

		List<VendorType> lstVendorDetails = null;
		VendorData objVendorData = new VendorData();
		try {
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			lstVendorDetails = objReportsDAO.getPOFApproveVendorNameList();
			if (lstVendorDetails != null && lstVendorDetails.size() > 0) {
				objVendorData.setVendor(lstVendorDetails);
			}

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getPOFApproveVendorNameList method of OrderBOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFApproveVendorNameList method of OrderBOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}
		return objVendorData;
	}

	public VendorCostValidationReportResponse fetchVendorPaymentAwaitingApproval(
			VendorCostValidationReportRequest reqBean)
			throws PhotoOmniException {
		LOGGER.debug("Entering into OrderBOImpl:fetchVendorPaymentAwaitingApproval()");
		VendorCostValidationReportResponse responseBean = null;
		try {
			LOGGER.debug("Instanttiating OrderDAO");
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			LOGGER.debug("Calling objReportsDAO.fetchVendorPaymentAwaitingApproval()");
			responseBean = objReportsDAO
					.fetchVendorPaymentAwaitingApproval(reqBean);
			responseBean.setMessageHeader(reqBean.getMessageHeader());

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at fetchVendorPaymentAwaitingApproval method of OrderBOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		}
		LOGGER.debug("Exiting from OrderRestBOImpl:fetchVendorPaymentAwaitingApproval()");
		return responseBean;

	}

	public POFVendorCostReportMessage updateVendorpaymentApproval(
			ApproveVCDataRequest reqBean) throws PhotoOmniException {
		LOGGER.debug("Entering into OrderBOImpl:updateVendorpaymentApproval()");
		POFVendorCostReportMessage reportyMessage = new POFVendorCostReportMessage();
		boolean status = false;
		try {
			LOGGER.debug("Instantiating OrderDAO:updateVendorpaymentApproval()");
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			List<POFVendorCostReportDetail> vendorCostDtlLst = reqBean
					.getData();

			for (int i = 0; i < vendorCostDtlLst.size(); i++) {
				POFVendorCostReportDetail vendorCostDtlBean = vendorCostDtlLst
						.get(i);
				LOGGER.debug("Calling OrderDAO:updateVendorpaymentApproval()");
				status = objReportsDAO
						.updateVendorpaymentApproval(vendorCostDtlBean);
				LOGGER.debug("status    " + status);
			}

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at fetchVendorPaymentAwaitingApproval method of OrderBOImpl - ",
					e);
			status = false;
		} finally {
			reportyMessage.setStatus(status);
			if (status == true) {
				reportyMessage.setMessage("Updation Success");
			} else {
				reportyMessage.setMessage("Updation Fails");
			}
		}
		LOGGER.debug("Exiting from OrderBOImpl:updateVendorpaymentApproval()");
		return reportyMessage;
	}

	public int getReportDataCount(
			POFVendorCostValidationFilter pofVendorCostFilter) {
		int count = 0;
		try {
			OrderDAO objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
			count = objReportsDAO.getReportDataCount(pofVendorCostFilter);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchVendorPaymentAwaitingApproval method of OrderBOImpl - "
					+ e.getMessage());
		}

		return count;
	}

	public LateEnvelopeReportRespBean fetchLateEnvelopeData(
			LateEnvelopeReportReqBean requestBean) throws PhotoOmniException,
			IOException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchLateEnvelopeDataRequest method of LateEnvelopeReportBOImpl ");
		}
		OrderDAO objReportsDAO = null;
		LateEnvelopeReportRespBean updateStatus = new LateEnvelopeReportRespBean();
		List<LateEnvelopeBean> envelopeList = null;

		try {
			if (!CommonUtil.isNull(requestBean)) {

				objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
				OrdersDAO objOrdersDao = omsDAOFactory.getOrdersDAO();

				LateEnvelopeFilter reqFilter = requestBean.getFilter();
				String locId = reqFilter.getStoreNumber();
				String locationAddress = objOrdersDao.getLocationAddress(locId);
				String sortColumn = this.getSortColumnName(reqFilter
						.getSortColumnName());
				reqFilter.setSortColumnName(sortColumn);
				envelopeList = objReportsDAO
						.fetchLateEnvelopeDataRequest(requestBean);
				ArrayList<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
				int totalRows = 0;
				if (envelopeList != null) {
					for (LateEnvelopeBean lateEnvelopeBean : envelopeList) {
						HashMap<String, Object> dataMap = new HashMap<String, Object>();
						dataMap.put("orderid", lateEnvelopeBean.getOrderId());
						dataMap.put("envelopeNumber",
								lateEnvelopeBean.getEnvelopeNumber());
						dataMap.put("orderOriginType",
								lateEnvelopeBean.getOrderOriginType());
						dataMap.put("processingTypeCD",
								lateEnvelopeBean.getProcessingTypeCD());
						dataMap.put("status", lateEnvelopeBean.getStatus());
						dataMap.put("timeSubmitted",
								lateEnvelopeBean.getTimeSubmitted());
						dataMap.put("timePromised",
								lateEnvelopeBean.getTimePromised());
						if (CommonUtil.isNull(lateEnvelopeBean.getTimeDone())) {
							dataMap.put("timeDone", "Not done");
						} else {
							dataMap.put("timeDone",
									lateEnvelopeBean.getTimeDone());
						}
						dataMap.put("employeeTookOrder",
								lateEnvelopeBean.getEmployeeTookOrder());
						dataList.add(dataMap);
						totalRows = lateEnvelopeBean.getTotalRows();

					}

				}
				updateStatus.setData(dataList);
				updateStatus.setMessageHeader(requestBean.getMessageHeader());
				updateStatus.setCurrentPage(requestBean.getFilter()
						.getPageSizeResponse());
				updateStatus.setStoreAddress(locationAddress);
				updateStatus.setTotalRows(totalRows);

			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchLateEnvelopeDataRequest method of LateEnvelopeReportBOImpl - "
					 ,e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchLateEnvelopeDataRequest method of LateEnvelopeReportBOImpl ");
			}
		}
		return updateStatus;

	}


	/**
	 * @param orders
	 * @return
	 */
	private int validateOrder(OrderList orders) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter validateOrder method of LateEnvelopeReportBOImpl ");
		}
		int status = 0;
		if (orders.getOrder().getPcpOrderId().isEmpty()
				|| orders.getOrder().getPcpOrderId() == null) {
			status = 1;
		} else {
			List<OrderItem> orderItemList = orders.getOrderItem();
			if (orderItemList.size() != 0) {
				{
					for (OrderItem orderItem : orderItemList) {
						if (orderItem.getpCPProductId().isEmpty()
								|| orderItem.getpCPProductId() == null) {
							status = 2;
							break;
						}

					}
				}
			} else {
				status = 3;
			}

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exit validateOrder method of LateEnvelopeReportBOImpl status"
					+ status);
		}
		return status;
	}

	/**
	 * This method use to find the database sort column name.
	 * 
	 * @param columnName
	 *            contains front end parameter.
	 * @return shortColumn
	 * @throws PhotoOmniException
	 */
	private String getSortColumnName(String columnName)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of LateEnvelopeReportBOImpl ");
		}
		String shortColumn = "ID";
		try {
			Map<String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("envelopeNumber", "ENVELOPE_NUMBER");
			dbColumnName.put("orderOriginType", "ORDER_ORIGIN_TYPE");
			dbColumnName.put("processingTypeCD", "PROCESSING_TYPE_CD");
			dbColumnName.put("ID", "SYS_ORDER_ATTRIBUTE_ID");
			dbColumnName.put("employeeTookOrder", "LAST_NAME");

			shortColumn = dbColumnName.get(columnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnName method of LateEnvelopeReportBOImpl - "
					, e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of LateEnvelopeReportBOImpl ");
			}
		}
		return shortColumn;
	}

	/**
	 * This method generates Bean for Envelope Details ie Product details and
	 * Envelope History
	 * 
	 * @param EnvelopeDtlsRequestBean
	 *            reqParams
	 * @return EnvelopeDtlsDataRespBean
	 * @throws PhotoOmniException
	 */
	public EnvelopeDtlsDataRespBean fetchEnvelopeDtlsData(
			EnvelopeNumberFilter reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeDtlsData method of OrderBOImpl ");
		}

		OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
		EnvelopeDtlsDataRespBean responseBean = new EnvelopeDtlsDataRespBean();
		List<EnvHistoryBean> envHistoryList = new ArrayList<EnvHistoryBean>();
		EnvelopePopupHeaderBean envelopePopupHeaderBean = new EnvelopePopupHeaderBean();
		EnvelopeOrderDtlsBean orderInfo = new EnvelopeOrderDtlsBean();
		List<EnvelopeOrderPromotionBean> orderPromotionList = new ArrayList<EnvelopeOrderPromotionBean>();
		List<EnvelopeProductDtlsBean> orderLineDtlsList = new ArrayList<EnvelopeProductDtlsBean>();
		List<EnvelopeProductDtlsBean> envProductDtlsList = new ArrayList<EnvelopeProductDtlsBean>();
		List<EnvelopeOrderLinePromotionBean> envOrderLinePrombean = new ArrayList<EnvelopeOrderLinePromotionBean>();

		try {
			envHistoryList = orderDAO.fetchEnvelopeHistoryDetails(reqParams);
			envelopePopupHeaderBean = orderDAO
					.fetchEnvelopePopupHeaderDtls(reqParams);
			orderInfo = orderDAO.fetchEnvelopeOrderInfo(reqParams);
			orderPromotionList = orderDAO
					.fetchEnvelopeOrderPromotion(reqParams);
			envProductDtlsList = orderDAO.fetchEnvelopeOrderLineInfo(reqParams);
			envOrderLinePrombean = orderDAO
					.fetchEnvelopeOrderLinePromotion(reqParams);

			orderLineDtlsList = setJSONForProductDtlsPopup(envProductDtlsList,
					envOrderLinePrombean);

			responseBean.setOrderInfo(orderInfo);
			responseBean.setOrderPromotion(orderPromotionList);
			responseBean.setEnvHistoryList(envHistoryList);
			responseBean.setEnvelopePopupHeaderBean(envelopePopupHeaderBean);
			responseBean.setOrderLineInfo(orderLineDtlsList);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchEnvelopeDtlsData method of OrderBOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeDtlsData method of OrderBOImpl ");
			}
		}
		return responseBean;
	}

	/**
	 * This method generates response bean for Envelope Order Line Details
	 * 
	 * @param EnvelopeProductDtlsBeanList
	 *            , EnvelopeOrderLinePromotionBeanList
	 * @return EnvelopeProductDtlsBean List
	 */
	private List<EnvelopeProductDtlsBean> setJSONForProductDtlsPopup(
			List<EnvelopeProductDtlsBean> envProductDtlsList,
			List<EnvelopeOrderLinePromotionBean> envOrderLinePrombean) {

		List<EnvelopeProductDtlsBean> responseBeanList = new ArrayList<EnvelopeProductDtlsBean>();
		for (int count = 0; count < envProductDtlsList.size(); count++) {
			EnvelopeProductDtlsBean orderLineDtls = envProductDtlsList
					.get(count);
			long orderLineId = orderLineDtls.getOrderLineId();
			List<EnvelopeOrderLinePromotionBean> orderLinePromotionList = new ArrayList<EnvelopeOrderLinePromotionBean>();
			for (int innerCount = 0; innerCount < envOrderLinePrombean.size(); innerCount++) {
				if (orderLineId == envOrderLinePrombean.get(innerCount)
						.getOrderLineId()) {
					EnvelopeOrderLinePromotionBean orderLinePromotion = envOrderLinePrombean
							.get(innerCount);
					orderLinePromotionList.add(orderLinePromotion);
				}
			}
			orderLineDtls.setEnvOrderLinePrombean(orderLinePromotionList);
			responseBeanList.add(orderLineDtls);
		}
		return responseBeanList;
	}

	/*--------------------------------FOR Exception Report Start----------------------------------*/
	/*
	 * Exception Report by Envelopes
	 */
	public ExceptionByEnvResponseBean submitEnvReportRequest(
			ExceptionByEnvelopeFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitEnvReportRequest method of ExceptionReportBOImpl ");
		}
		ExceptionByEnvResponseBean responseBean = new ExceptionByEnvResponseBean();
		List<ExceptionRepEnv> repByEnvList = new ArrayList<ExceptionRepEnv>();
		List<ExceptionByEnvelopeBean> responseBeanList = new ArrayList<ExceptionByEnvelopeBean>();
		try {
			OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			String shortColumn = this
					.getSortColumnNameForExceptionEnv(requestBean
							.getSortColumnName());
			requestBean.setSortColumnName(shortColumn);
			repByEnvList = orderDAO.submitEnvReportRequest(requestBean);
			String storeWithAddress = ordersDAO.getLocationAddress(requestBean
					.getStoreNumber());
			responseBean.setStoreWithAddress(storeWithAddress);
			if (repByEnvList != null && !repByEnvList.isEmpty()) {
				responseBean.setTotalRows(repByEnvList.get(0).getTotalRows());
				responseBeanList = setJSONForExceptionEnvReport(repByEnvList);
			} else {
				responseBean.setTotalRows(1);
			}
			responseBean.setRepByEnvList(responseBeanList);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitEnvReportRequest method of ExceptionReportBOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitEnvReportRequest method of ExceptionReportBOImpl ");
			}
		}
		return responseBean;

	}

	/**
	 * This method use to find the database shot column name.
	 * 
	 * @param columnName
	 *            contains front end parameter.
	 * @return shortColumn
	 * @throws PhotoOmniException
	 */
	private String getSortColumnNameForExceptionEnv(String columnName)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of ExceptionReportBOImpl ");
		}
		String shortColumn = "ORDER_ATTRIBUTE.ENVELOPE_NUMBER";
		try {
			Map<String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("env", "ORDER_ATTRIBUTE.ENVELOPE_NUMBER");
			dbColumnName.put("customerName", "ORDER_TAB.CUSTOMER_LAST_NAME");
			dbColumnName.put("envelopeStatus", "ORDER_TAB.STATUS");
			dbColumnName.put("envelopeDescription",
					"ORDER_TAB.ORDER_DESCRIPTION");
			dbColumnName.put("envelopeEntered", "ORDER_TAB.ORDER_PLACED_DTTM");
			dbColumnName.put("exceptionStatus", "ORDER_EXCEPTION.STATUS");
			dbColumnName.put("reason", "EXCEPTION_TYPE.REASON");
			dbColumnName.put("exceptionEntered", "ORDER_EXCEPTION.CREATE_DTTM");
			dbColumnName.put("orderPrice", "ORDER_TAB.FINAL_PRICE");
			dbColumnName.put("soldPrice", "ORDER_TAB.SOLD_AMOUNT");
			dbColumnName.put("refusedPrints", "ORDER_EXCEPTION.WASTE_QTY");
			dbColumnName.put("employeeLastName", "USER_ATTRIBUTE.LAST_NAME");
			dbColumnName.put(null, "ORDER_ATTRIBUTE.ENVELOPE_NUMBER");

			shortColumn = dbColumnName.get(columnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnName method of ExceptionReportBOImpl - "
					, e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of ExceptionReportBOImpl ");
			}
		}
		return shortColumn;
	}

	/**
	 * This method generates response bean for Exception By- Envelopes
	 * 
	 * @param repByEnvList
	 * @return responseBeanList
	 */
	private List<ExceptionByEnvelopeBean> setJSONForExceptionEnvReport(
			List<ExceptionRepEnv> repByEnvList) {

		List<ExceptionByEnvelopeBean> responseBeanList = new ArrayList<ExceptionByEnvelopeBean>();

		for (ExceptionRepEnv exceptionRepEnv : repByEnvList) {
			ExceptionByEnvelopeBean responseBean = new ExceptionByEnvelopeBean();
			responseBean.setEnv(exceptionRepEnv.getEnv());
			responseBean.setCustomerName(exceptionRepEnv.getCustomerName());
			responseBean.setEmployeeLastName(exceptionRepEnv
					.getEmployeeLastName());
			responseBean.setEnvelopeDescription(exceptionRepEnv
					.getEnvelopeDescription());
			responseBean.setEnvelopeEntered(exceptionRepEnv.getEnvelopeEntered());
			responseBean.setEnvelopeStatus(exceptionRepEnv.getEnvelopeStatus());
			responseBean.setExceptionEntered(exceptionRepEnv.getExceptionEntered());

			if (exceptionRepEnv.getExceptionStatus().equalsIgnoreCase("R")) {
				responseBean.setExceptionStatus("Resolved");
			} else if (exceptionRepEnv.getExceptionStatus().equalsIgnoreCase(
					"O")) {
				responseBean.setExceptionStatus("Open");
			}
			if (exceptionRepEnv.getOrderPrice().equalsIgnoreCase("$")) {
				responseBean.setOrderPrice("-");
			} else {
				responseBean.setOrderPrice(exceptionRepEnv.getOrderPrice());
			}
			responseBean.setReason(exceptionRepEnv.getReason());
			responseBean.setRefusedPrints(exceptionRepEnv.getRefusedPrints());
			if (exceptionRepEnv.getSoldPrice().equalsIgnoreCase("$")) {
				responseBean.setSoldPrice("-");
			} else {
				responseBean.setSoldPrice(exceptionRepEnv.getSoldPrice());
			}
			responseBean.setTotalRows(exceptionRepEnv.getTotalRows());
			/*
			 * Env popup for different types of orders
			 */
			responseBean.setOrderType(exceptionRepEnv.getOrderType());
			responseBean.setOrderId(exceptionRepEnv.getOrderId());
			responseBeanList.add(responseBean);
		}

		return responseBeanList;
	}

	/*
	 * Exception Report by Employees
	 */
	public ExceptionEmployeeRespBean submitEmployeeReportRequest(
			ExceptionEmployeeFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitEmployeeReportRequest method of ExceptionReportBOImpl ");
		}
		ExceptionEmployeeRespBean responseBean = new ExceptionEmployeeRespBean();
		List<ExceptionEmployeeResponseList> empResponseList = new ArrayList<ExceptionEmployeeResponseList>();
		List<ExceptionByEmployeeBean> exceptionByEmployeeBeanList = new ArrayList<ExceptionByEmployeeBean>();
		try {
			OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			String shortColumn = this
					.getSortColumnNameForExceptionEmp(requestBean
							.getSortColumnName());
			requestBean.setSortColumnName(shortColumn);
			exceptionByEmployeeBeanList = orderDAO
					.submitEmployeeReportRequest(requestBean);
			String storeWithAddress = ordersDAO.getLocationAddress(requestBean
					.getStoreNumber());
			responseBean.setStoreWithAddress(storeWithAddress);
			if (null != exceptionByEmployeeBeanList
					&& !exceptionByEmployeeBeanList.isEmpty()) {
				empResponseList = getRespJsonDataStructure(
						exceptionByEmployeeBeanList, requestBean);
				responseBean.setTotalRows(requestBean.getTotalRecords());
			} else {
				responseBean.setTotalRows(1);
			}
			responseBean.setEmpResponseList(empResponseList);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitEmployeeReportRequest method of ExceptionReportBOImpl - "
					, e);

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitEmployeeReportRequest method of ExceptionReportBOImpl ");
			}
		}
		return responseBean;

	}

	/**
	 * This method use to find the database shot column name.
	 * 
	 * @param columnName
	 *            contains front end parameter.
	 * @return shortColumn
	 * @throws PhotoOmniException
	 */
	private String getSortColumnNameForExceptionEmp(String sortColumnName)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of ExceptionReportBOImpl ");
		}
		String shortColumn = "EMPLOYEE_NAME";
		try {
			Map<String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("empName", "EMPLOYEE_NAME");
			dbColumnName.put(null, "EMPLOYEE_NAME");
			shortColumn = dbColumnName.get(sortColumnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnName method of ExceptionReportBOImpl - "
					, e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of ExceptionReportBOImpl ");
			}
		}
		return shortColumn;
	}

	/**
	 * This method generates response bean for Exception By- Envelopes
	 * 
	 * @param exceptionByEmployeeBeanList
	 * @param requestBean
	 * @return dataJSONBeanList
	 */
	private List<ExceptionEmployeeResponseList> getRespJsonDataStructure(
			List<ExceptionByEmployeeBean> exceptionByEmployeeBeanList,
			ExceptionEmployeeFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getRespJsonDataStructure method of ExceptionReportBOImpl ");
		}
		List<ExceptionEmployeeResponseList> dataJSONBeanList = new ArrayList<ExceptionEmployeeResponseList>();
		List<ExceptionEmployeeResponseList> responseList = new ArrayList<ExceptionEmployeeResponseList>();
		ExceptionEmployeeResponseList dataJSONBean = new ExceptionEmployeeResponseList();
		if (!(exceptionByEmployeeBeanList.isEmpty())) {
			dataJSONBean.setEmployeeId(exceptionByEmployeeBeanList.get(0)
					.getEmployeeId());
			long size = exceptionByEmployeeBeanList.size();
			try {
				for (int i = 0; i < size; i++) {
					if (dataJSONBean.getEmployeeId().equalsIgnoreCase(
							exceptionByEmployeeBeanList.get(i).getEmployeeId())) {
						this.setRecordsforEmployee(exceptionByEmployeeBeanList,
								dataJSONBean, i);
						if (i == (size - 1)) {
							dataJSONBeanList.add(dataJSONBean);
						}
					} else {
						if (!(dataJSONBean == null)) {
							dataJSONBeanList.add(dataJSONBean);
						}
						dataJSONBean = new ExceptionEmployeeResponseList();
						this.setRecordsforEmployee(exceptionByEmployeeBeanList,
								dataJSONBean, i);
						if (i == (size - 1)) {
							dataJSONBeanList.add(dataJSONBean);
						}
					}
				}
				List<ExceptionEmployeeResponseList> tempBeanList = new ArrayList<ExceptionEmployeeResponseList>();
				int startLimit;
				int endLimit;
				for (ExceptionEmployeeResponseList items : dataJSONBeanList) {
					if (items.getTotalCount() == 0
							&& items.getTotalDollars() == 0) {
					} else {
						tempBeanList.add(items);
					}
				}
				requestBean.setTotalRecords(tempBeanList.size());
				if (requestBean.isPrint()) {
					responseList = tempBeanList;
				} else {
					Map<String, Object> queryParam = getQueryParam(requestBean
							.getCurrentPageNo());
					startLimit = Integer.parseInt(!CommonUtil.isNull(queryParam
							.get("START_LIMIT")) ? queryParam
							.get("START_LIMIT").toString() : "");
					endLimit = Integer.parseInt(!CommonUtil.isNull(queryParam
							.get("END_LIMIT")) ? queryParam.get("END_LIMIT")
							.toString() : "");
					if (tempBeanList.size() <= endLimit) {
						endLimit = tempBeanList.size();
					}
					for (int index = startLimit - 1; index < endLimit; index++) {
						ExceptionEmployeeResponseList bean = tempBeanList
								.get(index);
						responseList.add(bean);
					}
				}

			} catch (Exception e) {
				LOGGER.error(" Error occoured at getRespJsonDataStructure method of ExceptionReportBOImpl - "
						, e);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting getRespJsonDataStructure method of ExceptionReportBOImpl ");
		}
		return responseList;
	}

	/**
	 * @param reqBean
	 * @return
	 */
	private Map<String, Object> getQueryParam(String currentPageNo) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getQueryParam method of ExceptionReportBOImpl ");
		}
		Map<String, Object> queryParam = null;
		try {
			Map<String, Long> pageLimit = CommonUtil.getPaginationLimit(
					currentPageNo,
					PhotoOmniConstants.EXCEPTION_REPORT_PAGINATION_SIZE);
			if (!CommonUtil.isNull(pageLimit)) {
				queryParam = new HashMap<String, Object>(pageLimit);
			} else {
				queryParam = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getQueryParam method of ExceptionReportBOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getQueryParam method of ExceptionReportBOImpl ");
			}
		}
		return queryParam;
	}

	private void setRecordsforEmployee(
			List<ExceptionByEmployeeBean> exceptionByEmployeeBeanList,
			ExceptionEmployeeResponseList dataJSONBean, int i) {

		dataJSONBean.setEmployeeId(exceptionByEmployeeBeanList.get(i)
				.getEmployeeId());
		dataJSONBean
				.setEmpName(exceptionByEmployeeBeanList.get(i).getEmpName());
		ExceptionTypeBean totalException = new ExceptionTypeBean();

		if (exceptionByEmployeeBeanList.get(i).getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_REFUSED)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());

			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean refusedType = new ExceptionTypeBean();
			refusedType.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			refusedType.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setRefused(refusedType);

		}
		if (exceptionByEmployeeBeanList.get(i).getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_REMAKE)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean remakeType = new ExceptionTypeBean();
			remakeType.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			remakeType.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setRemake(remakeType);

		}
		if (exceptionByEmployeeBeanList.get(i).getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_CANCEL)) {

			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean cancel = new ExceptionTypeBean();
			cancel.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			cancel.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setCancel(cancel);

		}
		if (exceptionByEmployeeBeanList.get(i).getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_UNSELLABLE)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean unSellable = new ExceptionTypeBean();
			unSellable.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			unSellable.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setUnSellable(unSellable);

		}
		if (exceptionByEmployeeBeanList
				.get(i)
				.getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_SOLDFORFREE)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean soldForFree = new ExceptionTypeBean();
			soldForFree.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			soldForFree.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setSoldForFree(soldForFree);

		}
		if (exceptionByEmployeeBeanList
				.get(i)
				.getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_PRICEMODIFY)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean priceModify = new ExceptionTypeBean();
			priceModify.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			priceModify.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setPriceModify(priceModify);

		}
		if (exceptionByEmployeeBeanList.get(i).getExceptionType()
				.equalsIgnoreCase(PhotoOmniConstants.EXCEPTION_TYPE_MISSING)) {
			dataJSONBean.setTotalCount(dataJSONBean.getTotalCount()
					+ exceptionByEmployeeBeanList.get(i).getExceptionCount());
			dataJSONBean.setTotalDollars(dataJSONBean.getTotalDollars()
					+ convertStringToDouble(exceptionByEmployeeBeanList.get(i)
							.getWasteCost()));
			ExceptionTypeBean missing = new ExceptionTypeBean();
			missing.setCount(exceptionByEmployeeBeanList.get(i)
					.getExceptionCount());
			missing.setDollars((exceptionByEmployeeBeanList.get(i)
					.getWasteCost()));
			dataJSONBean.setMissing(missing);

		}
		totalException.setCount(dataJSONBean.getTotalCount());
		double roundOff = Math.round((dataJSONBean.getTotalDollars()) * 100) / 100D;
		totalException.setDollars(Double.toString(roundOff));
		dataJSONBean.setTotalExceptions(totalException);
	}

	/**
	 * This method converts String to Double
	 * 
	 * @param stringObj
	 * @return doubleObj.
	 */
	public static double convertStringToDouble(String stringObj) {
		LOGGER.info(" Entering convertStringToDouble method of ExceptionReportBOImpl ");
		double doubleObj = 0;
		try {
			if (!CommonUtil.isNull(stringObj)) {
				doubleObj = Double.parseDouble(stringObj);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at convertStringToDouble method of ExceptionReportBOImpl - "
					, e);
		} finally {
			LOGGER.debug(" Exiting convertStringToDouble method of ExceptionReportBOImpl ");
		}
		return doubleObj;
	}

	/**
	 * This method return Exception Reason from OM_EXCEPTION_TYPE table
	 */
	public ExceptionReasonBean getReportExceptionReason() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportExceptionReason method of ExceptionReportBOImpl ");
		}
		ExceptionReasonBean responseBean = new ExceptionReasonBean();
		try {
			List<ExceptionReason> reasonList = null;
			OrderDAO orderDAO = omsDAOFactory.getRealTimeOrdersDao();
			reasonList = orderDAO.getReportExceptionReason();
			responseBean.setReasonList(reasonList);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at productDetailsRequest method of ExceptionReportBOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting productDetailsRequest method of ExceptionReportBOImpl ");
			}
		}
		return responseBean;

	}

	/*
	 * Below changes are made for CostCalculationChanges
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.common.bo.OrderBO#calculateOrderCost(long,
	 * java.lang.String)
	 */
	public boolean calculateOrderCost(long sysOrderId, String orderPlacedDttm)
			throws PhotoOmniException {
		boolean calculateOrderCost = false;
		boolean orderUpdSts = false;
		boolean orderItemUpdSts = false;


		LOGGER.debug(" Entering into Cost Calcuctaion method of OrderBOImpl ");
		OrderDAO objReportsDAO = null;
		objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
		OmOrderAttributeBean omOrderAttributeBean = null;
		try {
			omOrderAttributeBean = objReportsDAO.getOrderCost(sysOrderId,
					orderPlacedDttm);
			if (null != omOrderAttributeBean) {
				if ("H".equalsIgnoreCase(omOrderAttributeBean
						.getProcessingTypeCd())) {
					LOGGER.debug("Starting cost calculation for instore order.");
					omOrderAttributeBean = instoreCostCalculation(omOrderAttributeBean);
				}

				/** cost calculation for Send-out orders */
				else if ("S".equalsIgnoreCase(omOrderAttributeBean
						.getProcessingTypeCd())) {
					omOrderAttributeBean = vendorCostCalculation(omOrderAttributeBean);
				}
			}

			/**
			 * Updating
			 */
			if (omOrderAttributeBean != null) {

				orderUpdSts = objReportsDAO
						.updateOrderCostDtl(omOrderAttributeBean);
				orderItemUpdSts = objReportsDAO
						.updateOrderItemCostDtl(omOrderAttributeBean);
			}
			if (orderUpdSts && orderItemUpdSts) {
				calculateOrderCost = true;
				LOGGER.debug("Cost calculation done successfully, cost updated");
			} else {
				calculateOrderCost = false;
				LOGGER.debug("Cost Calculation module was not able to update cost.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return calculateOrderCost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.bo.OrderBO#instoreCostCalculation(com.walgreens.
	 * common.bean.OmOrderBean)
	 */

	public OmOrderAttributeBean instoreCostCalculation(
			OmOrderAttributeBean omOrderAttributeBean) {

		LOGGER.debug("Entering instoreCostCalculation()");

		OrderDAO objReportsDAO = null;
		objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();
		double instoreCost = 0.0;
		double instoreCostComponent = 0.0;
		double vendorCostComponent = 0.0;
		double orderCost = 0.0;
		double orderVendorCost = 0.0;
		double instoreCostCheck=0.0;
		try {
			List<OrderItemLineBean> orderItemLineBeanList = new ArrayList<OrderItemLineBean>();
			int quantity = 0;
			int wastedQuantity = 0;
			int sum = 0;
			boolean status=false;
			for (OrderItemLineBean orderItemLineBean : omOrderAttributeBean
					.getOrderItemLineBeanList()) {
				quantity = orderItemLineBean.getQuantity();
				if(orderItemLineBean.getPrintedQty()==PhotoOmniConstants.ZERO){
					ProductAttribute p = new ProductAttribute();
					OrderItem orderItem=new OrderItem();
					orderItem.setQuantity(String.valueOf(orderItemLineBean.getQuantity()));
					orderItem.setWasteQty(String.valueOf(orderItemLineBean.getWastedQty()));
					p.setNoOfInputs(orderItemLineBean.getNoOfInputs());
					p.setPanaromicPrints(orderItemLineBean.getPanaromicPrints());
					orderItem.setProductAttribute(p);
					//orderItem.setProductAttribute().setNoOfInputs(orderItemLineBean.getNoOfInputs());
					//orderItem.setProductAttribute().setPanaromicPrints(orderItemLineBean.getPanaromicPrints());
					int printQuantity=objReportsDAO.getPrintQty(orderItem);
					status=	objReportsDAO.updateOmOrderLineAttribute(orderItemLineBean.getOrderLineId(),orderItemLineBean.getOrderPlacedDttm(),printQuantity);
					orderItemLineBean.setPrintedQty(printQuantity);
				}
				
				wastedQuantity = orderItemLineBean.getWastedQty();
				sum = quantity + wastedQuantity;
				CostCalculationBean calculationBean = null;
				if (sum > 0) {
					if (orderItemLineBean.getSysEquipmentInstanceId() != 0) {
						calculationBean = objReportsDAO
								.getInstoreCostEquipmentCost(orderItemLineBean);

						if (!CommonUtil.isNull(calculationBean))
						{
							instoreCostComponent = instoreCostComponentCalculation(
									orderItemLineBean, calculationBean);
							instoreCost=instoreCostComponent;
						}	
					} else if (0 != orderItemLineBean.getSysMachineInstanceId()) {
						calculationBean = objReportsDAO
								.getInstoreDefaultMachineCost(orderItemLineBean);

						if ((!CommonUtil.isNull(calculationBean))
								&& calculationBean.getDfltMacCostPercent() > 0)
							instoreCost = (orderItemLineBean
									.getOriginalLinePrice() * calculationBean
									.getDfltMacCostPercent());
					}
					if (instoreCostComponent == 0.0) {
						instoreCost = 0.3 * orderItemLineBean
								.getOriginalLinePrice();

					}
					
					/* taking two digit after decimal */
					//instoreCost = Math.round(instoreCost * 100.0) / 100.0;
					if (omOrderAttributeBean.getSysFulfilmentVendorId() != 0) {
						
						vendorCostComponent = getVendorCostComponentCalculation(
								orderItemLineBean, omOrderAttributeBean);
						vendorCostComponent = Math
								.round(vendorCostComponent * 100.0) / 100.0;
					
						instoreCostCheck = vendorCostComponent
								+ instoreCost;
						instoreCost=instoreCostCheck;
					}
					
					/*
					 *  * Sum up the “Order Item vendor cost” and “In-store order
					 * item cost” and store it in "cost” at order item level
					 */
					instoreCost = Math.round(instoreCost * 100.0) / 100.0;
					orderItemLineBean.setCost(instoreCost);
					orderItemLineBean.setFulfillmentVendorCost(vendorCostComponent);
					orderCost += instoreCost;
					orderVendorCost += vendorCostComponent;
					orderItemLineBeanList.add(orderItemLineBean);

				}

				omOrderAttributeBean
						.setOrderItemLineBeanList(orderItemLineBeanList);
				omOrderAttributeBean.setCost(orderCost);
				omOrderAttributeBean.setFulfillmentVendorCost(orderVendorCost);
				omOrderAttributeBean.setCostCalculatiomStatusCd("C");

			}
		} catch (PhotoOmniException e) {
			omOrderAttributeBean.setCostCalculatiomStatusCd("P");
			LOGGER.error("Exception occur in instoreCostCalculation.", e);
		} catch (Exception e) {

			omOrderAttributeBean.setCostCalculatiomStatusCd("P");
			LOGGER.error("Exception occur in instoreCostCalculation.", e);
		} finally {
			LOGGER.debug("Exiting instoreCostCalculation().");
		}

		return omOrderAttributeBean;
	}

	/**
	 * Method will calculate instore component of order cost
	 * 
	 * @param costCalculationBean
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.bo.OrderBO#instoreCostComponentCalculation(com.walgreens
	 * .common.bean.CostCalculationBean)
	 */
	private double instoreCostComponentCalculation(
			OrderItemLineBean orderItemLineBean,
			CostCalculationBean costCalculationBean) {
		double instoreCostComponent = 0.0;
		LOGGER.debug("entering instoreCostComponentCalculation().");
		instoreCostComponent = costCalculationBean.getDevelopmentCost()
				+ ((orderItemLineBean.getQuantity() + orderItemLineBean
						.getWastedQty())
				* costCalculationBean.getBindingCostInstore())
				+ (orderItemLineBean.getPrintedQty() * costCalculationBean
						.getPrintCost())
				+ costCalculationBean.getAdditionalCost();
		LOGGER.debug("instoreCostComponent: " + instoreCostComponent);
		LOGGER.debug("exiting instoreCostComponentCalculation().");
		return instoreCostComponent;
	}

	/**
	 * Method will calculate vendor component of the order cost
	 * 
	 * @param costCalculationBean
	 * @return
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.bo.OrderBO#vendorCostComponentCalculation(com.walgreens
	 * .common.bean.CostCalculationBean)
	 */
	public double vendorCostComponentCalculation(
			OrderItemLineBean orderItemLineBean,
			CostCalculationBean costCalculationBean) {

		LOGGER.debug("Entering vendorCostComponentCalculation().");

		double vendorCostComponent = 0.0;
		int costQuantity = 1;
		int shippingCostQty = 1;
		int bindingCostQty = orderItemLineBean.getQuantity()
				+ orderItemLineBean.getWastedQty();

		if ("Q".equalsIgnoreCase(costCalculationBean.getCostType())) {
			costQuantity = orderItemLineBean.getQuantity()
					+ orderItemLineBean.getWastedQty();
		}
		if ("Q".equalsIgnoreCase(costCalculationBean.getShippingCostType())) {
			shippingCostQty = orderItemLineBean.getQuantity()
					+ orderItemLineBean.getWastedQty();
		}

		vendorCostComponent = (costQuantity * costCalculationBean.getCost())
				+ (shippingCostQty * costCalculationBean.getShippingCost())
				+ (bindingCostQty * costCalculationBean.getBindingCostVendor())
				+ costCalculationBean.getAdditionalCost();
		LOGGER.debug("vendorCostComponent: " + vendorCostComponent);
		LOGGER.debug("exiting vendorCostComponentCalculation().");
		return vendorCostComponent;
	}

	/**
	 * method will calculate vendor cost component (specifically for electronic
	 * film type order)
	 * 
	 * @param omOrderAttributeBean
	 * 
	 * @param costCalculationBean
	 * @return
	 */

	private double getVendorCostComponentCalculation(
			OrderItemLineBean orderItemLineBean,
			OmOrderAttributeBean omOrderAttributeBean)
			throws PhotoOmniException {
		LOGGER.debug("Entering getVendorCostComponentCalculation().");
		double vendorCost = 0.0;
		OrderDAO objReportsDAO = null;
		CostCalculationBean costCalculationBean = null;
		objReportsDAO = omsDAOFactory.getRealTimeOrdersDao();

		costCalculationBean = objReportsDAO.getVendorCostCalculation(
				orderItemLineBean, omOrderAttributeBean);
		if (!CommonUtil.isNull(costCalculationBean)) {
			vendorCost = vendorCostComponentCalculation(orderItemLineBean,
					costCalculationBean);
		}

		LOGGER.info("SysOrderId    " + costCalculationBean.getSysOrderId()
				+ " Product Id  : " + costCalculationBean.getSysProductId()
				+ " placed DTTM: " + costCalculationBean.getOrdPlcedDttm());

		LOGGER.debug("Exiting getVendorCostComponentCalculation().");

		return vendorCost;
	}

	/**
	 * * Method will calculate vendor component of the order cost
	 * 
	 * @param costCalculationBean
	 * @return
	 */
	public OmOrderAttributeBean vendorCostCalculation(
			OmOrderAttributeBean omOrderAttributeBean) {

		List<OrderItemLineBean> orderItemLineBeanList = new ArrayList<OrderItemLineBean>();
		int quantity = 0;
		int wastedQuantity = 0;

		try {
			double vendorCostComponent = 0.0;
			double orderVendorCost = 0.0;
			/*
			 * OrderDAO objReportsDAO = null; objReportsDAO =
			 * omsDAOFactory.getRealTimeOrdersDao();
			 */
			/**
			 * loop to iterate through the item list
			 */

			for (OrderItemLineBean orderItemLineBean : omOrderAttributeBean
					.getOrderItemLineBeanList()) {

				quantity = orderItemLineBean.getQuantity();
				wastedQuantity = orderItemLineBean.getWastedQty();
				int bindingCostQty = orderItemLineBean.getQuantity()
						+ orderItemLineBean.getWastedQty();
				if (bindingCostQty > 0) {
					vendorCostComponent = getVendorCostComponentCalculation(
							orderItemLineBean, omOrderAttributeBean);
				}

				if (vendorCostComponent == 0) {
					vendorCostComponent = 0.667 * orderItemLineBean
							.getOriginalLinePrice();
				}

				vendorCostComponent = Math.round(vendorCostComponent * 100.0) / 100.0;
				orderItemLineBean.setCost(vendorCostComponent);
				orderItemLineBean.setFulfillmentVendorCost(vendorCostComponent);
				orderVendorCost += vendorCostComponent;
				orderItemLineBeanList.add(orderItemLineBean);
			}
			omOrderAttributeBean
					.setOrderItemLineBeanList(orderItemLineBeanList);
			omOrderAttributeBean.setCost(orderVendorCost);
			omOrderAttributeBean.setFulfillmentVendorCost(orderVendorCost);
			omOrderAttributeBean.setCostCalculatiomStatusCd("C");
		} catch (PhotoOmniException e) {
			LOGGER.error("Exception occur in vendorCostCalculation.", e);
			omOrderAttributeBean.setCostCalculatiomStatusCd("P");
		} finally {
			LOGGER.debug("Exiting vendorCostCalculation().");
		}
		return omOrderAttributeBean;
	}
}
