package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"locationNumber",
"pcpOrderId",
"originOrderId",
"envelopeNumber",
"customerLastName",
"customerFirstName",
"customerPhoneNumber",
"customerEmail",
"customerId",
"orderSource",
"vendorId",
"totalDiscountAmount",
"calculatedRetail",
"originalRetail",
"referenceId",
"orderStatus",
"emplTookOrder",
"processingType",
"numberOfProducts",
"inStorePromisedTime",
"submittedTime",
"completionTime",
"calculatedPromisedTime",
"couponInd",
"loyaltyPrice",
"loyaltyDiscountAmt",
"employeeDiscountInd",
"serviceCategoryCode",
"nextDayServiceFlag",
"marketBasketId",
"mbPLUDetails",
"orderPLUDetails",
"orderCategory",
"machineId",
"submittedEmployeeId",
"completedEmployeeId",
"phoneAreaCode",
"orderDesc"
})

public class Order {
	
	private String locationType;
	
	
	
	
	private long orderId;
	private String orderSourceId;
	
	private String employeeId;
	private Date labelPrintDTTM;
	
		@JsonProperty("pcpOrderId")
		private String pcpOrderId;
		@JsonProperty("originOrderId")
		private String originOrderId;
		@JsonProperty("locationNumber")
		private String locationNumber;
		@JsonProperty("envelopeNumber")
		private String envelopeNumber;
		@JsonProperty("customerLastName")
		private String customerLastName;
		@JsonProperty("customerFirstName")
		private String customerFirstName;
		@JsonProperty("customerPhoneNumber")
		private String customerPhoneNumber;
		@JsonProperty("customerEmail")
		private String customerEmail;
		@JsonProperty("customerId")
		private String customerId;
	@JsonProperty("orderSource")
	private String orderSource;
	@JsonProperty("vendorId")
	private String vendorId;
	@JsonProperty("totalDiscountAmount")
	private String totalDiscountAmount;
	@JsonProperty("calculatedRetail")
	private String calculatedRetail;
	@JsonProperty("originalRetail")
	private String originalRetail;
	@JsonProperty("referenceId")
	private String referenceId;
	@JsonProperty("orderStatus")
	private String orderStatus;
	@JsonProperty("emplTookOrder")
	private String emplTookOrder;
	@JsonProperty("processingType")
	private String processingType;
	@JsonProperty("numberOfProducts")
	private String numberOfProducts;
	@JsonProperty("inStorePromisedTime")
	private String inStorePromisedTime;
	@JsonProperty("submittedTime")
	private String submittedTime;
	@JsonProperty("completionTime")
	private String completionTime;
	@JsonProperty("calculatedPromisedTime")
	private String calculatedPromisedTime;
	@JsonProperty("couponInd")
	
	private String couponInd;
	@JsonProperty("loyaltyPrice")
	private String loyaltyPrice;
	@JsonProperty("loyaltyDiscountAmt")
	private String loyaltyDiscountAmt;
	@JsonProperty("employeeDiscountInd")
	private String employeeDiscountInd;
	@JsonProperty("serviceCategoryCode")
	private String serviceCategoryCode;
	@JsonProperty("nextDayServiceFlag")
	private String nextDayServiceFlag;
	@JsonProperty("marketBasketId")
	private String marketBasketId;
	
	@JsonProperty("mbPLUDetails")
	private List<PLUDetail> mbPLUDetails = new ArrayList<PLUDetail>();
	@JsonProperty("orderPLUDetails")
	private List<PLUDetail> orderPLUDetails = new ArrayList<PLUDetail>();
	@JsonProperty("orderCategory")
	private String orderCategory;
	@JsonProperty("machineId")
	private String machineId;
	@JsonProperty("submittedEmployeeId")
	private String submittedEmployeeId;
	@JsonProperty("completedEmployeeId")
	private String completedEmployeeId;
	@JsonProperty("phoneAreaCode")
	private String phoneAreaCode;
	

	@JsonProperty("orderDesc")
	private String  orderDesc;
	@JsonProperty("machineId")
	public String getMachineId() {
		return machineId;
	}
	
	@JsonProperty("machineId")
	public void setMachineId(String machineId) {
		this.machineId = machineId.trim();
	}
	public String getSubmittedEmployeeId() {
		return submittedEmployeeId;
	}
	public void setSubmittedEmployeeId(String submittedEmployeeId) {
		this.submittedEmployeeId = submittedEmployeeId.trim();
	}
	public String getCompletedEmployeeId() {
		return completedEmployeeId.toUpperCase();
	}
	public void setCompletedEmployeeId(String completedEmployeeId) {
		this.completedEmployeeId = completedEmployeeId.trim();
	}
	/**
	 * @return the orderCategory
	 */
	@JsonProperty("orderCategory")
	public String getOrderCategory() {
		return orderCategory;
	}

	/**
	 * @param orderCategory the orderCategory to set
	 */
	@JsonProperty("orderCategory")
	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory.trim();
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	*
	* @return
	* The originOrderId
	*/
	@JsonProperty("originOrderId")
	public String getOriginOrderId() {
	return originOrderId;
	}

	/**
	*
	* @param originOrderId
	* The originOrderId
	*/
	@JsonProperty("originOrderId")
	public void setOriginOrderId(String originOrderId) {
	this.originOrderId = originOrderId.trim();
	}

	/**
	*
	* @return
	* The envelopeNumber
	*/
	@JsonProperty("envelopeNumber")
	public String getEnvelopeNumber() {
	return envelopeNumber;
	}

	/**
	*
	* @param envelopeNumber
	* The envelopeNumber
	*/
	@JsonProperty("envelopeNumber")
	public void setEnvelopeNumber(String envelopeNumber) {
	this.envelopeNumber = envelopeNumber.trim();
	}

	/**
	*
	* @return
	* The customerLastName
	*/
	@JsonProperty("customerLastName")
	public String getCustomerLastName() {
	return customerLastName;
	}

	/**
	*
	* @param customerLastName
	* The customerLastName
	*/
	@JsonProperty("customerLastName")
	public void setCustomerLastName(String customerLastName) {
	this.customerLastName = customerLastName.trim();
	}

	/**
	*
	* @return
	* The CustomerFirstName
	*/
	@JsonProperty("customerFirstName")
	public String getCustomerFirstName() {
	return customerFirstName;
	}

	/**
	*
	* @param CustomerFirstName
	* The CustomerFirstName
	*/
	@JsonProperty("customerFirstName")
	public void setCustomerFirstName(String customerFirstName) {
	this.customerFirstName = customerFirstName.trim();
	}

	/**
	*
	* @return
	* The customerPhoneNumber
	*/
	@JsonProperty("customerPhoneNumber")
	public String getCustomerPhoneNumber() {
	return customerPhoneNumber;
	}

	/**
	*
	* @param customerPhoneNumber
	* The customerPhoneNumber
	*/
	@JsonProperty("customerPhoneNumber")
	public void setCustomerPhoneNumber(String customerPhoneNumber) {
	this.customerPhoneNumber = customerPhoneNumber.trim();
	}

	/**
	*
	* @return
	* The customerEmail
	*/
	@JsonProperty("customerEmail")
	public String getCustomerEmail() {
	return customerEmail;
	}

	/**
	*
	* @param customerEmail
	* The customerEmail
	*/
	@JsonProperty("customerEmail")
	public void setCustomerEmail(String customerEmail) {
	this.customerEmail = customerEmail.trim();
	}

	/**
	*
	* @return
	* The customerId
	*/
	@JsonProperty("customerId")
	public String getCustomerId() {
	return customerId;
	}

	/**
	*
	* @param customerId
	* The customerId
	*/
	@JsonProperty("customerId")
	public void setCustomerId(String customerId) {
	this.customerId = customerId.trim();
	}

	/**
	*
	* @return
	* The orderSource
	*/
	@JsonProperty("orderSource")
	public String getOrderSource() {
	return orderSource;
	}

	/**
	*
	* @param orderSource
	* The orderSource
	*/
	@JsonProperty("orderSource")
	public void setOrderSource(String orderSource) {
	this.orderSource = orderSource.trim().trim();
	}

	/**
	*
	* @return
	* The vendorId
	*/
	@JsonProperty("vendorId")
	public String getVendorId() {
	return vendorId;
	}

	/**
	*
	* @param vendorId
	* The vendorId
	*/
	@JsonProperty("vendorId")
	public void setVendorId(String vendorId) {
	this.vendorId = vendorId.trim();
	}

	/**
	*
	* @return
	* The totalDiscountAmount
	*/
	@JsonProperty("totalDiscountAmount")
	public String getTotalDiscountAmount() {
	return totalDiscountAmount;
	}

	/**
	*
	* @param totalDiscountAmount
	* The totalDiscountAmount
	*/
	@JsonProperty("totalDiscountAmount")
	public void setTotalDiscountAmount(String totalDiscountAmount) {
	this.totalDiscountAmount = totalDiscountAmount.trim();
	}

	/**
	*
	* @return
	* The calculatedRetail
	*/
	@JsonProperty("calculatedRetail")
	public String getCalculatedRetail() {
	return calculatedRetail;
	}

	/**
	*
	* @param calculatedRetail
	* The calculatedRetail
	*/
	@JsonProperty("calculatedRetail")
	public void setCalculatedRetail(String calculatedRetail) {
	this.calculatedRetail = calculatedRetail.trim();
	}

	/**
	*
	* @return
	* The originalRetail
	*/
	@JsonProperty("originalRetail")
	public String getOriginalRetail() {
	return originalRetail;
	}

	/**
	*
	* @param originalRetail
	* The originalRetail
	*/
	@JsonProperty("originalRetail")
	public void setOriginalRetail(String originalRetail) {
	this.originalRetail = originalRetail.trim();
	}

	/**
	*
	* @return
	* The referenceId
	*/
	@JsonProperty("referenceId")
	public String getReferenceId() {
	return referenceId;
	}

	/**
	*
	* @param referenceId
	* The referenceId
	*/
	@JsonProperty("referenceId")
	public void setReferenceId(String referenceId) {
	this.referenceId = referenceId.trim();
	}

	/**
	*
	* @return
	* The orderStatus
	*/
	@JsonProperty("orderStatus")
	public String getOrderStatus() {
	return orderStatus;
	}

	/**
	*
	* @param orderStatus
	* The orderStatus
	*/
	@JsonProperty("orderStatus")
	public void setOrderStatus(String orderStatus) {
	this.orderStatus = orderStatus.trim();
	}

	/**
	*
	* @return
	* The emplTookOrder
	*/
	@JsonProperty("emplTookOrder")
	public String getEmplTookOrder() {
	return emplTookOrder.toUpperCase();
	}

	/**
	*
	* @param emplTookOrder
	* The emplTookOrder
	*/
	@JsonProperty("emplTookOrder")
	public void setEmplTookOrder(String emplTookOrder) {
	this.emplTookOrder = emplTookOrder.trim();
	}

	/**
	*
	* @return
	* The processingType
	*/
	@JsonProperty("processingType")
	public String getProcessingType() {
	return processingType;
	}

	/**
	*
	* @param processingType
	* The processingType
	*/
	@JsonProperty("processingType")
	public void setProcessingType(String processingType) {
	this.processingType = processingType.trim();
	}

	/**
	*
	* @return
	* The numberOfProducts
	*/
	@JsonProperty("numberOfProducts")
	public String getNumberOfProducts() {
	return numberOfProducts;
	}

	/**
	*
	* @param numberOfProducts
	* The numberOfProducts
	*/
	@JsonProperty("numberOfProducts")
	public void setNumberOfProducts(String numberOfProducts) {
	this.numberOfProducts = numberOfProducts.trim();
	}

	/**
	*
	* @return
	* The inStorePromisedTime
	*/
	@JsonProperty("inStorePromisedTime")
	public String getInStorePromisedTime() {
	return inStorePromisedTime;
	}

	/**
	*
	* @param inStorePromisedTime
	* The inStorePromisedTime
	*/
	@JsonProperty("inStorePromisedTime")
	public void setInStorePromisedTime(String inStorePromisedTime) {
	this.inStorePromisedTime = inStorePromisedTime.trim();
	}

	/**
	*
	* @return
	* The submittedTime
	*/
	@JsonProperty("submittedTime")
	public String getSubmittedTime() {
	return submittedTime;
	}

	/**
	*
	* @param submittedTime
	* The submittedTime
	*/
	@JsonProperty("submittedTime")
	public void setSubmittedTime(String submittedTime) {
	this.submittedTime = submittedTime.trim();
	}

	/**
	 * @return the completionTime
	 */
	@JsonProperty("completionTime")
	public String getCompletionTime() {
		return completionTime;
	}

	/**
	 * @param completionTime the completionTime to set
	 */
	@JsonProperty("completionTime")
	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime.trim();
	}

	/**
	*
	* @return
	* The calculatedPromisedTime
	*/
	@JsonProperty("calculatedPromisedTime")
	public String getCalculatedPromisedTime() {
	return calculatedPromisedTime;
	}

	/**
	*
	* @param calculatedPromisedTime
	* The calculatedPromisedTime
	*/
	@JsonProperty("calculatedPromisedTime")
	public void setCalculatedPromisedTime(String calculatedPromisedTime) {
	this.calculatedPromisedTime = calculatedPromisedTime.trim();
	}

	/**
	*
	* @return
	* The couponInd
	*/
	@JsonProperty("couponInd")
	public String getCouponInd() {
	return couponInd;
	}

	/**
	*
	* @param couponInd
	* The couponInd
	*/
	@JsonProperty("couponInd")
	public void setCouponInd(String couponInd) {
	this.couponInd = couponInd.trim();
	}

	/**
	*
	* @return
	* The loyaltyPrice
	*/
	@JsonProperty("loyaltyPrice")
	public String getLoyaltyPrice() {
	return loyaltyPrice;
	}

	/**
	*
	* @param loyaltyPrice
	* The loyaltyPrice
	*/
	@JsonProperty("loyaltyPrice")
	public void setLoyaltyPrice(String loyaltyPrice) {
	this.loyaltyPrice = loyaltyPrice.trim();
	}

	/**
	*
	* @return
	* The loyaltyDiscountAmt
	*/
	@JsonProperty("loyaltyDiscountAmt")
	public String getLoyaltyDiscountAmt() {
	return loyaltyDiscountAmt;
	}

	/**
	*
	* @param loyaltyDiscountAmt
	* The loyaltyDiscountAmt
	*/
	@JsonProperty("loyaltyDiscountAmt")
	public void setLoyaltyDiscountAmt(String loyaltyDiscountAmt) {
	this.loyaltyDiscountAmt = loyaltyDiscountAmt.trim();
	}

	/**
	*
	* @return
	* The employeeDiscountInd
	*/
	@JsonProperty("employeeDiscountInd")
	public String getEmployeeDiscountInd() {
	return employeeDiscountInd;
	}

	/**
	*
	* @param employeeDiscountInd
	* The employeeDiscountInd
	*/
	@JsonProperty("employeeDiscountInd")
	public void setEmployeeDiscountInd(String employeeDiscountInd) {
	this.employeeDiscountInd = employeeDiscountInd.trim();
	}

	/**
	*
	* @return
	* The serviceCategoryCode
	*/
	@JsonProperty("serviceCategoryCode")
	public String getServiceCategoryCode() {
	return serviceCategoryCode;
	}

	/**
	*
	* @param serviceCategoryCode
	* The serviceCategoryCode
	*/
	@JsonProperty("serviceCategoryCode")
	public void setServiceCategoryCode(String serviceCategoryCode) {
	this.serviceCategoryCode = serviceCategoryCode.trim();
	}

	/**
	*
	* @return
	* The nextDayServiceFlag
	*/
	@JsonProperty("nextDayServiceFlag")
	public String getNextDayServiceFlag() {
	return nextDayServiceFlag;
	}

	/**
	*
	* @param nextDayServiceFlag
	* The nextDayServiceFlag
	*/
	@JsonProperty("nextDayServiceFlag")
	public void setNextDayServiceFlag(String nextDayServiceFlag) {
	this.nextDayServiceFlag = nextDayServiceFlag.trim();
	}

	/**
	*
	* @return
	* The marketBasketId
	*/
	@JsonProperty("marketBasketId")
	public String getMarketBasketId() {
	return marketBasketId;
	}

	/**
	*
	* @param marketBasketId
	* The marketBasketId
	*/
	@JsonProperty("marketBasketId")
	public void setMarketBasketId(String marketBasketId) {
	this.marketBasketId = marketBasketId.trim();
	}

	
	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType.trim();
	}
	

	@JsonProperty("locationNumber")
	public String getLocationNumber() {
		return locationNumber;
	}

	@JsonProperty("locationNumber")
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber.trim();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getLabelPrintDTTM() {
		return labelPrintDTTM;
	}

	public void setLabelPrintDTTM(Date labelPrintDTTM) {
		this.labelPrintDTTM = labelPrintDTTM;
	}

	public String getOrderSourceId() {
		return orderSourceId;
	}

	public void setOrderSourceId(String orderSourceId) {
		this.orderSourceId = orderSourceId;
	}

	

	/**
	 * @return the mbPLUDetails
	 */
	@JsonProperty("mbPLUDetails")
	public List<PLUDetail> getMbPLUDetails() {
		return mbPLUDetails;
	}

	/**
	 * @param mbPLUDetails the mbPLUDetails to set
	 */
	@JsonProperty("mbPLUDetails")
	public void setMbPLUDetails(List<PLUDetail> mbPLUDetails) {
		this.mbPLUDetails = mbPLUDetails;
	}

	/**
	 * @return the orderPLUDetails
	 */
	@JsonProperty("orderPLUDetails")
	public List<PLUDetail> getOrderPLUDetails() {
		return orderPLUDetails;
	}

	/**
	 * @param orderPLUDetails the orderPLUDetails to set
	 */
	@JsonProperty("orderPLUDetails")
	public void setOrderPLUDetails(List<PLUDetail> orderPLUDetails) {
		this.orderPLUDetails = orderPLUDetails;
	}
	/**
	 * @return the pcpOrderId
	 */
	@JsonProperty("pcpOrderId")
	public String getPcpOrderId() {
		return pcpOrderId;
	}

	/**
	 * @param pcpOrderId the pcpOrderId to set
	 */
	@JsonProperty("pcpOrderId")
	public void setPcpOrderId(String pcpOrderId) {
		this.pcpOrderId = pcpOrderId.trim();
	}
	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode.trim();
	}
	
	/**
	 * @return the orderDesc
	 */
	public String getOrderDesc() {
		return orderDesc;
	}

	/**
	 * @param orderDesc the orderDesc to set
	 */
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc.trim();
	}

	@Override
	public String toString() {
		return "Order [locationType=" + locationType + ", orderId=" + orderId
				+ ", orderSourceId=" + orderSourceId + ", employeeId="
				+ employeeId + ", labelPrintDTTM=" + labelPrintDTTM
				+ ", pcpOrderId=" + pcpOrderId + ", originOrderId="
				+ originOrderId + ", locationNumber=" + locationNumber
				+ ", envelopeNumber=" + envelopeNumber + ", customerLastName="
				+ customerLastName + ", customerFirstName=" + customerFirstName
				+ ", customerPhoneNumber=" + customerPhoneNumber
				+ ", customerEmail=" + customerEmail + ", customerId="
				+ customerId + ", orderSource=" + orderSource + ", vendorId="
				+ vendorId + ", totalDiscountAmount=" + totalDiscountAmount
				+ ", calculatedRetail=" + calculatedRetail
				+ ", originalRetail=" + originalRetail + ", referenceId="
				+ referenceId + ", orderStatus=" + orderStatus
				+ ", emplTookOrder=" + emplTookOrder + ", processingType="
				+ processingType + ", numberOfProducts=" + numberOfProducts
				+ ", inStorePromisedTime=" + inStorePromisedTime
				+ ", submittedTime=" + submittedTime + ", completionTime="
				+ completionTime + ", calculatedPromisedTime="
				+ calculatedPromisedTime + ", couponInd=" + couponInd
				+ ", loyaltyPrice=" + loyaltyPrice + ", loyaltyDiscountAmt="
				+ loyaltyDiscountAmt + ", employeeDiscountInd="
				+ employeeDiscountInd + ", serviceCategoryCode="
				+ serviceCategoryCode + ", nextDayServiceFlag="
				+ nextDayServiceFlag + ", marketBasketId=" + marketBasketId
				+ ", mbPLUDetails=" + mbPLUDetails + ", orderPLUDetails="
				+ orderPLUDetails + ", orderCategory=" + orderCategory
				+ ", machineId=" + machineId + ", submittedEmployeeId="
				+ submittedEmployeeId + ", completedEmployeeId="
				+ completedEmployeeId + ", additionalProperties="
				+ additionalProperties + "]";
	}

	
	

}
