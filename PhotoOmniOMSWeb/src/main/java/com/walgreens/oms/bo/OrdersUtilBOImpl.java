package com.walgreens.oms.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.DateUtil;
import com.walgreens.oms.bean.OrderASNDetailsBean;
import com.walgreens.oms.bean.OrderHistoryBean;
import com.walgreens.oms.bean.UnclaimedEnvCustOrderRespBean;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.dao.OrdersDAO;
import com.walgreens.oms.factory.OmsBOFactory;
import com.walgreens.oms.factory.OmsDAOFactory;
import com.walgreens.oms.json.bean.AsnOrderRequest;
import com.walgreens.oms.json.bean.AsnOrderResponse;
import com.walgreens.oms.json.bean.OrderASNDetail;
import com.walgreens.oms.json.bean.OrderASNResponseDetail;
import com.walgreens.oms.json.bean.OrderLineASNDetail;
import com.walgreens.oms.json.bean.UnclaimedBean;
import com.walgreens.oms.json.bean.UnclaimedEnvCustomer;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;
import com.walgreens.oms.json.bean.UnclaimedEnvRespList;
import com.walgreens.oms.json.bean.UnclaimedResponse;
import com.walgreens.oms.utility.ServiceUtil;



@Component("OrdersUtilBO")
@Service
public class OrdersUtilBOImpl implements OrdersUtilBO,PhotoOmniConstants {

	@Autowired
	private OmsDAOFactory omsDAOFactory;
	
	@Autowired
	private OmsBOFactory omsBOFactory;
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrdersUtilBOImpl.class);

	@Override
public AsnOrderResponse updateASNDetails(AsnOrderRequest asnOrderJsonRequest) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateASNDetails method of OrdersUtilBOImpl ");
		}

		boolean updateStatus = false; 
		boolean updateOrdHistStatus = false;
		boolean vendorCostCalcStageIndFlag = false;
		boolean removeItemFromOmOrderLineFlag = false;
		boolean updateItemIntoOmOrderLineFlag = false;
		boolean insertItemIntoOmOrderLineFlag = false;
		boolean updateOmOrderDetailsFlag = false;
		Timestamp sysTimeStamp=new Timestamp(new Date().getTime());
		
		OrdersDAO ordersDAO = omsDAOFactory.getOrdersDAO();
		
		//need to add response details to asnOrderResponse
		AsnOrderResponse asnOrderResponse = new AsnOrderResponse();
		List<OrderASNResponseDetail> orderASNResponseDetailsList = new ArrayList<OrderASNResponseDetail>();
		asnOrderResponse.setMessageHeader(asnOrderJsonRequest.getMessageHeader());

		
		
		if (null!=asnOrderJsonRequest) {

			for (OrderASNDetail orderASNDetail : asnOrderJsonRequest.getOrderASNDetails()) {
				
				try{

				// get order detils from OM_ORDER and OM_ORDER_ATTRIBUTE with
				// ORDER_NBR
					OrderASNDetailsBean orderASNDetailsBean = ordersDAO.getOrderDetails(orderASNDetail.getPcpOrderId(), orderASNDetail.getLocationNumber());
				
				OrderHistoryBean orderHistoryBean = new OrderHistoryBean();
				orderASNDetailsBean.setOrderHistoryBean(orderHistoryBean);

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
				
				String couponInd = orderASNDetailsBean.getCouponInd();
				/*Double loyaltyPrice = orderASNDetailsBean.getLoyaltyPrice();
				Double loyaltyDiscountAmount = orderASNDetailsBean.getLoyaltyDiscountAmount();
*/
				//Double originalOrderPrice = orderASNDetailsBean.getOriginalOrderPrice();				
				
				orderASNDetailsBean.setTrackingNo(orderASNDetail.getTrackingNo());
				orderASNDetailsBean.setShipmentCompany(orderASNDetail.getShipmentCompany());
				
				 
				Timestamp actualShippedDTTM=sysTimeStamp;
				if(null!= orderASNDetail.getShippedDate()){
					actualShippedDTTM=DateUtil.getISO861(orderASNDetail.getShippedDate());					
				}
				
				Timestamp estimatedShippedDTTM = sysTimeStamp;
				if(null!= orderASNDetail.getShippedDate()){
					estimatedShippedDTTM=DateUtil.getISO861(orderASNDetail.getShippedDate());					
				}	
			    Timestamp expectedArrivalDTTM = sysTimeStamp;
				if(null!= orderASNDetail.getExpArrivalDate()){
					expectedArrivalDTTM=DateUtil.getISO861(orderASNDetail.getExpArrivalDate());					
				}
				
				Timestamp actualArrivalDTTM = sysTimeStamp;
				if(null!= orderASNDetail.getExpArrivalDate()){
					actualArrivalDTTM=DateUtil.getISO861(orderASNDetail.getExpArrivalDate());					
				}
				 
				orderASNDetailsBean.setShippedDTTM(actualShippedDTTM);			
				orderASNDetailsBean.setShippedDTTM(estimatedShippedDTTM);
				orderASNDetailsBean.setArrivalDTTM(expectedArrivalDTTM);
				orderASNDetailsBean.setArrivalDTTM(actualArrivalDTTM);
				orderASNDetailsBean.setShipmentStatus(orderASNDetail.getShipmentStatus());
				orderASNDetailsBean.setShipmentURL(orderASNDetail.getShipmentURL());
				orderASNDetailsBean.setShipmentCompanyPhone(orderASNDetail.getShipmentCompanyPhone());
				orderASNDetailsBean.setCostPrice(orderASNDetail.getCostPrice());
				orderASNDetailsBean.setCredit(orderASNDetail.getCredit());
				orderASNDetailsBean.setDiscountedPrice(orderASNDetail.getDiscountedPrice());
				

				updateStatus = ordersDAO.getSysOrderId( sysOrderId  );
				
				if(!updateStatus){
					LOGGER.debug("Inserting into OM_SHIPMENT");				
					updateStatus = ordersDAO.updateOmShipment(orderASNDetailsBean);
				}
				else{
					updateStatus = ordersDAO.checkUpdateOmShipment(orderASNDetailsBean);
					LOGGER.debug("Updating  OM_SHIPMENT");	
					
				}
					
				
				
				LOGGER.debug("Shipment detail insertion: sysOrderId :"+sysOrderId+" : "+updateStatus);	
				
				orderASNDetailsBean.setActionCD(ACTION_CD);
				orderASNDetailsBean.setActionNotes(ACTION_NOTES);			
				
				LOGGER.debug("Inserting into OM_ORDER_HISTORY");
				updateOrdHistStatus = ordersDAO.updateOmOrderHistory(orderASNDetailsBean);
				LOGGER.debug("Order history detail insertion: sysOrderId :"+sysOrderId+" : "+updateOrdHistStatus);	
				
				if (null!= orderASNDetail  && orderASNDetail.getOrderLineASNDetails().size() > 0) {

					// need to Update
					// OM_ORDER_ATTRIBUTE.COST_CALCULATION_STATUS_CD = “P”
					// if OM_VENDOR_ATTRIBUTE.VENDOR_COST_CALC_STAGE_IND = ‘A’
					// for the vendor associated with the order.
					// assumed that if one imemories item exists in the order
					// the the order is an imemoriesOrder

					int vendorCostCalcStageCd = ordersDAO.getvendorCostCalcStageInd(vendorId);
					LOGGER.debug("vendorCostCalcStageCd   "+vendorCostCalcStageCd);
					LOGGER.debug("cost calculation stage ind for PCPOrderId:" + orderASNDetail.getPcpOrderId()+ " and Vendor id: " + orderASNDetail.getSysSrcVendorId() + "Is:" +  vendorCostCalcStageCd);
					
					if (ACTIVE_IND_Y == vendorCostCalcStageCd){
						vendorCostCalcStageIndFlag = ordersDAO.updateOrdAttrCostCalStatus(sysOrderId);
						LOGGER.debug("Order cost cal ind updated for sysOrderId:" + sysOrderId + " Is " + vendorCostCalcStageIndFlag);
					}
					double calculatedRetail = 0.00;
					double totaldiscountAmount = 0.00;
					OrderLineASNDetail orderLineASNDetails = null;
					for(OrderLineASNDetail orderLineASNDetail : orderASNDetail.getOrderLineASNDetails()){
						
						orderLineASNDetails = new OrderLineASNDetail();
						
						String pcpProductId = orderLineASNDetail.getPcpProductId();
						LOGGER.debug("pcpProductId    "+pcpProductId);	
						
						orderLineASNDetails.setCalculatedPrice(orderLineASNDetail.getCalculatedPrice());
						
						orderLineASNDetails.setOriginalRetail(orderASNDetail.getRetailPrice());
						
						orderLineASNDetails.setDiscountAmount(orderLineASNDetail.getDiscountAmount());
						
						orderLineASNDetails.setiMemAddedItem(orderLineASNDetail.getIMemAddedItem());
						
						orderLineASNDetails.setiMemQty(orderLineASNDetail.getIMemQty());
						
						orderLineASNDetails.setOrderedQty(orderLineASNDetail.getOrderedQty());
						
						orderLineASNDetails.setPcpProductId(pcpProductId);
						
						//orderASNDetailsBean.setOrderLineASNDetails(orderLineASNDetails);
						calculatedRetail += orderLineASNDetails.getCalculatedPrice();
						LOGGER.debug("calculatedRetail    "+calculatedRetail);
						totaldiscountAmount += orderLineASNDetails.getDiscountAmount();
						LOGGER.debug("totaldiscountAmounttotaldiscountAmount    "+calculatedRetail);						
						
						if(ZERO!=orderLineASNDetails.getiMemAddedItem().trim().length()){
							
							/*
							 *  If the item exists in PC+ system and iMemories
							 *  item quantity is ZERO, remove the item from OM_ORDER_LINE
							 */
							if (ZERO==orderLineASNDetails.getiMemQty()) {
								removeItemFromOmOrderLineFlag = ordersDAO.removeItemFromOmOrderLine(sysOrderId,pcpProductId);
								LOGGER.debug("iMem Item removed pcporderid:"+ sysOrderId + "and pcpProductId : " + pcpProductId + " Is " + removeItemFromOmOrderLineFlag);
							}
							// If the item exists in PC+ system and iMemories
							// item quantity is not ZERO, update the following
							// details in Order Line table
							//
							else{
								if(YES_IND.equalsIgnoreCase(orderLineASNDetails.getiMemAddedItem())){									
									insertItemIntoOmOrderLineFlag = ordersDAO.insertItemIntoOmOrderLine(
											orderPlacedDttm,
											sysOrderId,
											pcpProductId,		
											orderLineASNDetails.getiMemQty(),
											orderLineASNDetails.getOrderedQty(),
											orderLineASNDetails.getCalculatedPrice(),
											orderLineASNDetails.getDiscountAmount()
										);
									LOGGER.debug("insertItemIntoOmOrderLineFlag    "+insertItemIntoOmOrderLineFlag);
								}
								else{
									LOGGER.debug("About to update OM_ORDER_LINE");
									updateItemIntoOmOrderLineFlag = ordersDAO.updateItemIntoOmOrderLine(
											orderLineASNDetails.getiMemQty(),
											orderLineASNDetails.getOriginalRetail(),
											orderLineASNDetails.getCalculatedPrice(),
											orderLineASNDetails.getDiscountAmount(),
											sysOrderId,
											pcpProductId
										);
									LOGGER.debug("updateItemIntoOmOrderLineFlag    "+updateItemIntoOmOrderLineFlag);
								}
								LOGGER.debug("iMem Item updated pcporderid:"+ sysOrderId + "and pcpProductId : " + pcpProductId + " Is" + updateItemIntoOmOrderLineFlag);

							}
							
						}
						else
						{
							updateItemIntoOmOrderLineFlag = ordersDAO.updateItemIntoOmOrderLine(
									orderLineASNDetails.getOrderedQty(),
									orderLineASNDetails.getOriginalRetail(),
									orderLineASNDetails.getCalculatedPrice(),
									orderLineASNDetails.getDiscountAmount(),
									sysOrderId,
									pcpProductId);
							LOGGER.debug("Item updated pcporderid:"+ sysOrderId + "and pcpProductId : " + pcpProductId + " Is" + updateItemIntoOmOrderLineFlag);
						}

					// Update the following details in Order table for imemories
					// Orders
					orderASNDetailsBean.setOriginalOrderPrice(calculatedRetail);					
					orderASNDetailsBean.setCouponInd(couponInd);
					/*orderASNDetailsBean.setLoyaltyPrice(loyaltyPrice);
					orderASNDetailsBean.setLoyaltyDiscountAmount(loyaltyDiscountAmount);
				*/	
					orderASNDetailsBean.setTotalOrderDiscount(totaldiscountAmount);		
					/*
					 * Below code will update OM_ORDER
					 */
					updateOmOrderDetailsFlag = ordersDAO.updateOmOrderDetails(
							sysOrderId,
							calculatedRetail,
							totaldiscountAmount,
							ACTIVE_IND_Y,
							0.0,
							0.0
							
							);
					
					LOGGER.debug("Order details updated for PCPOrderId:" + sysOrderId + " Is " + updateOmOrderDetailsFlag);
				}

				/* creates exception for ASN Return message if insert fails */
					OrderASNResponseDetail orderASNResponseDetail = new OrderASNResponseDetail();
					/*if (!updateStatus || !updateOrdHistStatus || !updateItemIntoOmOrderLineFlag ||
						!removeItemFromOmOrderLineFlag || !insertItemIntoOmOrderLineFlag 
						|| !updateOmOrderDetailsFlag && asnOrderResponse != null) {	
					*/
					orderASNResponseDetail.setEnvelopeNumber(orderASNDetail.getEnvelopeNumber());
					
					orderASNResponseDetail.setLocationNumber(orderASNDetail.getLocationNumber());
				    
					orderASNResponseDetail.setLocationType(orderASNDetail.getLocationType());
					
					orderASNResponseDetail.setPcpOrderId(orderASNDetailsBean.getSysOrderId());
					
					orderASNResponseDetail.setOrderPlacedDttm(orderASNDetailsBean.getOrderPlacedDttm());
					
					orderASNResponseDetail.setReferenceId(orderASNDetail.getReferenceId());
					
					orderASNResponseDetail.setStatus(true);
					
					orderASNResponseDetail.setErrorDetails(ServiceUtil.createSuccessMessage());
					
					if(null!=orderASNResponseDetailsList){
						orderASNResponseDetailsList.add(orderASNResponseDetail);
					}
					
					//asnOrderResponse.setOrderASNResponseDetails(orderASNResponseDetailsList);
					LOGGER.debug("ASN update procedure success ");
					
							
				    String  orderPlacedDttm1 = orderASNResponseDetail.getOrderPlacedDttm().toString();
				    orderPlacedDttm1=orderPlacedDttm1.substring(0, 19);
				    OrderBO ordersBO = omsBOFactory.getOrdersBO();
					ordersBO.calculateOrderCost(orderASNResponseDetail.getPcpOrderId(),orderPlacedDttm1);
					
				//}
			/*	else {
					OrderASNResponseDetail orderASNResponseDetail = new OrderASNResponseDetail();
					orderASNResponseDetail.setEnvelopeNumber(asnOrderJsonRequest
							.getOrderASNDetails().get(i).getEnvelopeNumber());
					orderASNResponseDetail.setLocationNumber(asnOrderJsonRequest
							.getOrderASNDetails().get(i).getLocationNumber());
					orderASNResponseDetail.setLocationType(asnOrderJsonRequest
							.getOrderASNDetails().get(i).getLocationType());
					orderASNResponseDetail.setPcpOrderId(asnOrderJsonRequest
							.getOrderASNDetails().get(i).getPcpOrderId());
					orderASNResponseDetail.setReferenceId(asnOrderJsonRequest
							.getOrderASNDetails().get(i).getReferenceId());
					orderASNResponseDetail.setStatus(true);
					orderASNResponseDetail.setErrorDetails(ServiceUtil.createFailureMessage());
					if(orderASNResponseDetailsList != null){
						orderASNResponseDetailsList.add(orderASNResponseDetail);
					}
					LOGGER.debug("ASN update procedure success!");
				}*/					
				}
			}
			catch(Exception poex){
				LOGGER.error("PhotoOmniException occurred while calculating ASN    ",poex);
				OrderASNResponseDetail orderASNResponseDetail = new OrderASNResponseDetail();
				orderASNResponseDetail.setEnvelopeNumber(orderASNDetail.getEnvelopeNumber());
				orderASNResponseDetail.setLocationNumber(orderASNDetail.getLocationNumber());
				orderASNResponseDetail.setLocationType(orderASNDetail.getLocationType());
				orderASNResponseDetail.setPcpOrderId(orderASNDetail.getPcpOrderId());
				orderASNResponseDetail.setReferenceId(orderASNDetail.getReferenceId());
				orderASNResponseDetail.setStatus(false);
				if(RESPONSE_DB_ERROR_STRING.equalsIgnoreCase(poex.getMessage())){
					orderASNResponseDetail.setErrorDetails(CommonUtil.createFailureMessageForDBException(poex));
				}				
				else{
					orderASNResponseDetail.setErrorDetails(CommonUtil.createFailureMessageForSystemException(poex));
				}
				if(null!=orderASNResponseDetailsList){
					orderASNResponseDetailsList.add(orderASNResponseDetail);
				}
				LOGGER.debug("ASN update procedure success!");
		
			}
			asnOrderResponse.setOrderASNResponseDetails(orderASNResponseDetailsList);
		}

		LOGGER.debug(" Exiting updateASNDetails method of OrdersUtilBOImpl ");
		}	
		return asnOrderResponse;
	}
	
	/**
	 * This method gets all the unclaimed orders.
	 * @param reqBean contains request params.
	 * @return unclaimedDataList
	 * @throws PhotoOmniException custom exception.
	 */
	public UnclaimedResponse submitUnclaimedEnvRequest(final UnclaimedEnvFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitUnclaimedEnvRequest method of OrdersUtilBOImpl ");
		}
		List<UnclaimedEnvRespList> responseList = null;
		UnclaimedResponse unclaimedResponse = null;
		try {
			OrdersDAO orderDAO = omsDAOFactory.getOrdersDAO();
			String sortColumn = this.getSortColumnName(reqBean.getSortColumnName());
			reqBean.setSortColumnName(sortColumn);
			responseList = orderDAO.submitUnclaimedEnvRequest(reqBean);
			unclaimedResponse = this.getJsonDataForUnclaimedResp(reqBean, responseList);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrdersUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrdersUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitUnclaimedEnvRequest method of OrdersUtilBOImpl ");
			}
		}
		
		return unclaimedResponse;
	}	
	
	
	
	/**
	 * This method find the last six month orders for a customer.
	 * @param contains front end parameters.
	 * @return unclaimedDataList.
	 * @throws PhotoOmniException. 
	 */
	public UnclaimedEnvCustomer unclaimedEnvCustOrderRequest(final UnclaimedEnvCustorderReqBean reqBean) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering unclaimedEnvCustOrderRequest method of OrderBOImpl ");
		}
		List<UnclaimedEnvCustOrderRespBean> unclaimedDataList = null;
		UnclaimedEnvCustomer unclaimedEnvCustomer = null;
		try {
			OrdersDAO orderDAO = omsDAOFactory.getOrdersDAO();
			unclaimedDataList = orderDAO.unclaimedEnvCustOrderRequest(reqBean);
			unclaimedEnvCustomer = this.getJsonDataForCustomerOrder(unclaimedDataList);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at unclaimedEnvCustOrderRequest method of OrderBOImpl - ", e);
		    throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at unclaimedEnvCustOrderRequest method of OrderBOImpl - ", e);
		    throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting unclaimedEnvCustOrderRequest method of OrderBOImpl ");
			}
		}
		
		return unclaimedEnvCustomer;
	}	
	
	

	/**
	 * This method use to find the database shot column name.
	 * @param columnName contains front end parameter.
	 * @return shortColumn.
	 * @throws PhotoOmniException.
	 */
	private String getSortColumnName(final String columnName) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of OrderBOImpl ");
		}
		String shortColumn = "CUSTOMER_LAST_NAME";
		try {
			Map <String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("customerName", "CUSTOMERNAME");
			dbColumnName.put("totalEnvelope", "TOTALENVELOPES");
			dbColumnName.put("totalValue", "TOTALVALUE");
			dbColumnName.put("orderRangePlaceddate", "MAX(OM_ORDER.ORDER_PLACED_DTTM)");
			dbColumnName.put("ID", "CUSTOMERID");
			shortColumn = dbColumnName.get(columnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnName method of OrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of OrderBOImpl ");
			}
		}
		return shortColumn;
	}
	
	
	/**
	 * this method creates Json structure for Unclaimed Orders.
	 * @param reqBean contains request parameters.
	 * @param responseList contains response data from database.
	 * @return unclaimedResponse
	 * @throws PhotoOmniException 
	 * @throws JsonProcessingException
	 */
	private UnclaimedResponse getJsonDataForUnclaimedResp(final UnclaimedEnvFilter reqBean, 
			final List<UnclaimedEnvRespList> responseList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getJsonDataForUnclaimedResp method of OrderBOImpl ");
		}
		UnclaimedResponse unclaimedResponse = new UnclaimedResponse();
		try {
			OrdersDAO objOrdersDao = omsDAOFactory.getOrdersDAO();
			Timestamp currentTimestamp = new Timestamp(new Date().getTime());
			String locationAddress = objOrdersDao.getLocationAddress(reqBean.getStoreNumber());
			unclaimedResponse.setStoreNumber(locationAddress);
			String today = CommonUtil.convertTimestampToString(currentTimestamp, PhotoOmniConstants.DATE_FORMAT_EIGHT);
			unclaimedResponse.setCurrentDate(today);
			unclaimedResponse.setCurrentPage(!CommonUtil.isNull(reqBean.getCurrentPageNo()) ? reqBean.getCurrentPageNo() : "");
			if (!CommonUtil.isNull(responseList) && responseList.size() > 0) {
				List<UnclaimedBean> UnclaimedBean = unclaimedResponse.getUnclaimedBeanList();
				for (UnclaimedEnvRespList unclaimedEnvResp: responseList) {
					UnclaimedBean unclaimedBean = new UnclaimedBean();
					unclaimedBean.setCustomerId(unclaimedEnvResp.getCustomerId() != null 
							? Long.toString(unclaimedEnvResp.getCustomerId()) : "");
					unclaimedBean.setCustomerName(unclaimedEnvResp.getCustomerName());
					unclaimedBean.setPhoneNumber(unclaimedEnvResp.getPhoneNumber());
					unclaimedBean.setTotalEnvelope(unclaimedEnvResp.getTotalEnvelope());
					unclaimedBean.setTotalValue(unclaimedEnvResp.getTotalValue());
					unclaimedBean.setOrderRangePlaceddate(unclaimedEnvResp.getOrderRangePlaceddate());
					unclaimedResponse.setTotalRecord(Integer.toString(unclaimedEnvResp.getTotalRecord()));
					UnclaimedBean.add(unclaimedBean);
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getJsonDataForUnclaimedResp method of OrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getJsonDataForUnclaimedResp method of OrderBOImpl ");
			}
		}
		
		return unclaimedResponse;
	}
	
	/**
	 * This method create the json structure for last six month searched customer order data.
	 * @param unclaimedDataList contains database data.
	 * @return UnclaimedEnvCustomer.
	 * @throws PhotoOmniException custom exception
	 */
	private UnclaimedEnvCustomer getJsonDataForCustomerOrder(
			final List<UnclaimedEnvCustOrderRespBean> unclaimedDataList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getJsonDataForCustomerOrder method of OrderBOImpl ");
		}
		UnclaimedEnvCustomer UnclaimedEnvCustomer = new UnclaimedEnvCustomer();
		try {
			if (!CommonUtil.isNull(unclaimedDataList)) {
				UnclaimedEnvCustomer.setUnclaimedDataList(unclaimedDataList);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getJsonDataForCustomerOrder method of OrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getJsonDataForCustomerOrder method of OrderBOImpl ");
			}
		}
		return UnclaimedEnvCustomer;
	}
	
}
