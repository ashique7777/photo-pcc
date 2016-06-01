package com.walgreens.oms.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.OrderASNDetailsBean;
import com.walgreens.oms.bean.UnclaimedEnvCustOrderRespBean;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;
import com.walgreens.oms.json.bean.UnclaimedEnvRespList;


public interface OrdersDAO {

	/**
	 * @param orderASNDetailsBean
	 * @return
	 */
	boolean updateASNDetails(OrderASNDetailsBean orderASNDetailsBean);
 
	/**
	 * @param orderASNDetailsBean
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean updateOmShipment(OrderASNDetailsBean orderASNDetailsBean) throws PhotoOmniException;

	/**
	 * @param orderHistoryBean
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean updateOmOrderHistory(OrderASNDetailsBean orderASNDetailsBean) throws PhotoOmniException;

	/**
	 * @param pcpOrderId
	 * @param pcpProductId
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean checkItemExist(String pcpOrderId, String pcpProductId) throws PhotoOmniException;

	/**
	 * @param pcpOrderId
	 * @param pcpProductId
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean removeItemFromOmOrderLine(long sysOrderId, String pcpProductId) throws PhotoOmniException;

	/**
	 * @param orderedQty
	 * @param originalRetail
	 * @param calculatedPrice
	 * @param discountAmount
	 * @param pcpOrderId
	 * @param pcpProductId
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean updateItemIntoOmOrderLine(int orderedQty, Double originalRetail,
			Double calculatedPrice, Double discountAmount, long sysOrderId,
			String pcpProductId) throws PhotoOmniException;

	/**
	 * @param pcpOrderId
	 * @param pcpProductId
	 * @param calculatedPrice
	 * @param discountAmount
	 * @param iMemQty
	 * @param OrderedQty
	 * @return
	 */
	boolean insertItemIntoOmOrderLine(Timestamp orderPlacedDttm,long sysOrderId,String pcpProductId,
			 int iMemQty, int OrderedQty,Double calculatedPrice, Double discountAmount) throws PhotoOmniException;

	/**
	 * @param pcpOrderId
	 * @return
	 */
	OrderASNDetailsBean getOrderDetails(Long pcpOrderId, int locatioNumber)  throws PhotoOmniException;

	/**
	 * @param pcpOrderId
	 * @param originalOrderPrice
	 * @param totalOrderDiscount
	 * @param couponInd
	 * @param loyaltyPrice
	 * @param loyaltyDiscountAmount
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean updateOmOrderDetails(long sysOrderId, Double originalOrderPrice,
			Double totalOrderDiscount, int couponInd, Double loyaltyPrice,
			Double loyaltyDiscountAmount) throws PhotoOmniException;

	/**
	 * @param sysSrcVendorId
	 * @return
	 * @throws PhotoOmniException 
	 */
	int getvendorCostCalcStageInd(int sysSrcVendorId);

	/**
	 * @param sysOrderId
	 * @return
	 * @throws PhotoOmniException 
	 */
	boolean updateOrdAttrCostCalStatus(long sysOrderId) throws PhotoOmniException;
	
	/**
	 * @param orderNo
	 * @return
	 */
	int getExceptionId(int orderNo);
	
	/**
	 * 
	 * @param reqBean
	 * @return
	 * @throws PhotoOmniException 
	 */
	public  List<UnclaimedEnvRespList> submitUnclaimedEnvRequest( UnclaimedEnvFilter reqBean) throws PhotoOmniException ;
	
	/**
	 * 
	 * @param reqBean
	 * @return
	 * @throws PhotoOmniException 
	 */
	public @ResponseBody List<UnclaimedEnvCustOrderRespBean> unclaimedEnvCustOrderRequest(@RequestBody UnclaimedEnvCustorderReqBean reqBean) throws PhotoOmniException;

	
	public Map<String , Object> getAdditonalOrderDetails(String sysOrderId, int locationNumber) throws PhotoOmniException;

	boolean checkUpdateOmShipment(OrderASNDetailsBean orderASNDetailsBean) throws PhotoOmniException;
	/**
	 * @param orderHistoryBean
	 * @return
	 * @throws PhotoOmniException 
	 */

	boolean getSysOrderId(long sysOrderId ) throws PhotoOmniException;
	
	/**
	 * @param locId
	 * @return
	 * @throws PhotoOmniException
	 */
	public  String getLocationAddress(String locId ) throws PhotoOmniException;

	OrderASNDetailsBean getOrderDetails(String orderNumber, int locationNumber)
			throws PhotoOmniException;
	
	// Added for Insert into OM_SHIPMENT - Starts
	/**
	 * @param OrderASNDetailsBean
	 * @return boolean
	 * @throws PhotoOmniException
	 */
	public boolean updateOmShipmentStatus(OrderASNDetailsBean orderASNDetailsBean)
			throws PhotoOmniException ;
	/**
	 * @param sysOrderId , orderDTM
	 * @return Processing type - String
	 * @throws PhotoOmniException
	 */
	public String getProcessingType(long sysOrderId , Timestamp orderDTM ) throws PhotoOmniException; 
	
	/**
	 * @param sysOrderId, orderDTM
	 * @return boolean
	 * @throws PhotoOmniException
	 */
	public boolean checkRecordPresent(long sysOrderId , Timestamp orderDTM ) throws PhotoOmniException;
	
	/**
	 * This method returns the Processing type value from OM_SHIPMENT table
	 * @param sysOrderId ,orderDTM
	 * @return Processing type.
	 * @throws PhotoOmniException 
	 */
	public String getEstDTM(long sysOrderId , int locationNumber ) throws PhotoOmniException;
	// Added for Insert into OM_SHIPMENT - Ends

}
