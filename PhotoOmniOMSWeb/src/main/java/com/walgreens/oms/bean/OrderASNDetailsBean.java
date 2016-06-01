package com.walgreens.oms.bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class OrderASNDetailsBean {	
	
	private String locationType;
	private Integer locationNumber;
	private String pcpOrderId;
	private String envelopeNumber;
	private String referenceId;
	private String shipmentStatus;
	private String shipmentCompany;
	private String trackingNo;	
	private String expArrivalDate;
	private String actArrivalDate;
	private String sysShipmentId;
	private String estShipmentDate;
	private String rollsPickUpDttm;
	private String imemQuantity;
	private long sysOrderId;
	/*private String locatioNbr;*/
	
	
	/**
	 * @return the rollsPickUpDttm
	 */
	public String getRollsPickUpDttm() {
		return rollsPickUpDttm;
	}
	/**
	 * @param rollsPickUpDttm the rollsPickUpDttm to set
	 */
	public void setRollsPickUpDttm(String rollsPickUpDttm) {
		this.rollsPickUpDttm = rollsPickUpDttm;
	}
	private String shippedDate;
	private String shipmentURL;
	private String shipmentCompanyPhone;
	private Double retailPrice;
	private Double costPrice;
	private Double credit;
	private Double discountedPrice;
	private Double couponAmount;
	private String orderNumber;
	
	
	private OrderHistoryBean orderHistoryBean;	
	private List<OrderLineASNDetails> orderLineASNDetails = new ArrayList<OrderLineASNDetails>();
	
	//order details started
	private int sysSrcVendorId;
	private Double loyaltyPrice;
	private Double loyaltyDiscountAmount;
	private String couponInd;
	private Double originalOrderPrice;
	private Double totalOrderDiscount;
	private Timestamp orderPlacedDttm;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updatedDttm;
	private String status;
	private int vendorId;
	
	private String actionCD;
	private String actionNotes;
	double finalPrice;
	
	Timestamp shippedDTTM;
	Timestamp arrivalDTTM;
	//vendor details started
	/**
	 * @return the sysShipmentId
	 */
	public String getSysShipmentId() {
		return sysShipmentId;
	}
	/**
	 * @param sysShipmentId the sysShipmentId to set
	 */
	public void setSysShipmentId(String sysShipmentId) {
		this.sysShipmentId = sysShipmentId;
	}
	private String vendorCostCalcStageInd;
	
	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public Integer getLocationNumber() {
		return locationNumber;
	}
	public void setLocationNumber(Integer locationNumber) {
		this.locationNumber = locationNumber;
	}
	public String getPcpOrderId() {
		return pcpOrderId;
	}
	public void setPcpOrderId(String pcpOrderId) {
		this.pcpOrderId = pcpOrderId;
	}
	public String getEnvelopeNumber() {
		return envelopeNumber;
	}
	public void setEnvelopeNumber(String envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getShipmentStatus() {
		return shipmentStatus;
	}
	public void setShipmentStatus(String shipmentStatus) {
		this.shipmentStatus = shipmentStatus;
	}
	public String getShipmentCompany() {
		return shipmentCompany;
	}
	public void setShipmentCompany(String shipmentCompany) {
		this.shipmentCompany = shipmentCompany;
	}
	public String getTrackingNo() {
		return trackingNo;
	}
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
	public String getExpArrivalDate() {
		return expArrivalDate;
	}
	public void setExpArrivalDate(String expArrivalDate) {
		this.expArrivalDate = expArrivalDate;
	}
	public String getActArrivalDate() {
		return actArrivalDate;
	}
	/**
	 * @param actArrivalDate the actArrivalDate to set
	 */
	public void setActArrivalDate(String actArrivalDate) {
		this.actArrivalDate = actArrivalDate;
	}
	/**
	 * @return the estShipmentDate
	 */
	public String getEstShipmentDate() {
		return estShipmentDate;
	}
	/**
	 * @param estShipmentDate the estShipmentDate to set
	 */
	public void setEstShipmentDate(String estShipmentDate) {
		this.estShipmentDate = estShipmentDate;
	}
	/**
	 * @return the actArrivalDate
	 */
	public String getShippedDate() {
		return shippedDate;
	}
	public void setShippedDate(String shippedDate) {
		this.shippedDate = shippedDate;
	}
	public String getShipmentURL() {
		return shipmentURL;
	}
	public void setShipmentURL(String shipmentURL) {
		this.shipmentURL = shipmentURL;
	}
	public String getShipmentCompanyPhone() {
		return shipmentCompanyPhone;
	}
	public void setShipmentCompanyPhone(String shipmentCompanyPhone) {
		this.shipmentCompanyPhone = shipmentCompanyPhone;
	}
	public Double getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	public Double getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public Double getDiscountedPrice() {
		return discountedPrice;
	}
	public void setDiscountedPrice(Double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}
	public Double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}
	public List<OrderLineASNDetails> getOrderLineASNDetails() {
		return orderLineASNDetails;
	}
	public void setOrderLineASNDetails(List<OrderLineASNDetails> orderLineASNDetails) {
		this.orderLineASNDetails = orderLineASNDetails;
	}
	
	public OrderHistoryBean getOrderHistoryBean() {
		return orderHistoryBean;
	}
	public void setOrderHistoryBean(OrderHistoryBean orderHistoryBean) {
		this.orderHistoryBean = orderHistoryBean;
	}
	public Double getLoyaltyPrice() {
		return loyaltyPrice;
	}
	public void setLoyaltyPrice(Double loyaltyPrice) {
		this.loyaltyPrice = loyaltyPrice;
	}
	public Double getLoyaltyDiscountAmount() {
		return loyaltyDiscountAmount;
	}
	public void setLoyaltyDiscountAmount(Double loyaltyDiscountAmount) {
		this.loyaltyDiscountAmount = loyaltyDiscountAmount;
	}
	public String getCouponInd() {
		return couponInd;
	}
	public void setCouponInd(String couponInd) {
		this.couponInd = couponInd;
	}
	public Double getOriginalOrderPrice() {
		return originalOrderPrice;
	}
	public void setOriginalOrderPrice(Double originalOrderPrice) {
		this.originalOrderPrice = originalOrderPrice;
	}
	public Double getTotalOrderDiscount() {
		return totalOrderDiscount;
	}
	public void setTotalOrderDiscount(Double totalOrderDiscount) {
		this.totalOrderDiscount = totalOrderDiscount;
	}
	public int getSysSrcVendorId() {
		return sysSrcVendorId;
	}
	public void setSysSrcVendorId(int sysSrcVendorId) {
		this.sysSrcVendorId = sysSrcVendorId;
	}
	public String getVendorCostCalcStageInd() {
		return vendorCostCalcStageInd;
	}
	public void setVendorCostCalcStageInd(String vendorCostCalcStageInd) {
		this.vendorCostCalcStageInd = vendorCostCalcStageInd;
	}
	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public Timestamp getCreateDttm() {
		return createDttm;
	}
	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}
	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**
	 * @return the locatioNbr
	 */
	/**
	 * @return the imemQuantity
	 */
	public String getImemQuantity() {
		return imemQuantity;
	}
	/**
	 * @param imemQuantity the imemQuantity to set
	 */
	public void setImemQuantity(String imemQuantity) {
		this.imemQuantity = imemQuantity;
	}
	public long getSysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getActionCD() {
		return actionCD;
	}
	public void setActionCD(String actionCD) {
		this.actionCD = actionCD;
	}
	public String getActionNotes() {
		return actionNotes;
	}
	public void setActionNotes(String actionNotes) {
		this.actionNotes = actionNotes;
	}
	public double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * @return the shippedDTTM
	 */
	public Timestamp getShippedDTTM() {
		return shippedDTTM;
	}
	/**
	 * @param shippedDTTM the shippedDTTM to set
	 */
	public void setShippedDTTM(Timestamp shippedDTTM) {
		this.shippedDTTM = shippedDTTM;
	}
	/**
	 * @return the arrivalDTTM
	 */
	public Timestamp getArrivalDTTM() {
		return arrivalDTTM;
	}
	/**
	 * @param arrivalDTTM the arrivalDTTM to set
	 */
	public void setArrivalDTTM(Timestamp arrivalDTTM) {
		this.arrivalDTTM = arrivalDTTM;
	}

	
}
