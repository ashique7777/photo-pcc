package com.walgreens.batch.central.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author CTS
 *
 */
public class POSOrder {

	private long sysOrderId;
	private String orderNo;
	private int calenderId;
	private String orderType;
	private String orderDescription;
	private long owningLocId;
	private String orderOriginType;
	private int srcVendorId;
	private int srcKioskId;
	private String srcVendorOrderId;
	private int subSrcVendorId;
	private String srcKioskOrderId;
	private int fullfilmentVendorId;
	private String fullfilmentVendorOrderId;
	private int fullfillmentLocId;
	private int destinationLocId;
	private String status;
	private int customerId;
	private int destinationCustomerId;
	private String orderPlacedDttm;
	private Date requestDeliveryDttm;
	private Date promiseShipDttm;
	private Date promiseDeliveryDttm;
	private Date actualShipDttm;
	private Date actualDeliveryDttm;
	private String orderCompletedDttm;
	private Date orderSoldDttm;
	private String currencyCodeType;
	private String currencyCodeId;
	private double originalOrderPrice;
	private double finalPrice;
	private int totalOrderDiscount;
	private int loyaltyPrice;
	private int loyaltyDiscountAmount;
	private int orderTax;
	private double soldAmount;
	private String loyaltyInd;
	private String couponInd;
	private int discountCardUsedCd;
	private String replenishmentInd;
	private String costCalculationInd;
	private String pmStatus;
	private String notes;
	private int printsReturned;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updatedDttm;
	

	public long getSysOrderId() {
		return sysOrderId;
	}

	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getCalenderId() {
		return calenderId;
	}

	public void setCalenderId(int calenderId) {
		this.calenderId = calenderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public long getOwningLocId() {
		return owningLocId;
	}

	public void setOwningLocId(long owningLocId) {
		this.owningLocId = owningLocId;
	}

	public String getOrderOriginType() {
		return orderOriginType;
	}

	public void setOrderOriginType(String orderOriginType) {
		this.orderOriginType = orderOriginType;
	}

	public int getSrcVendorId() {
		return srcVendorId;
	}

	public void setSrcVendorId(int srcVendorId) {
		this.srcVendorId = srcVendorId;
	}

	public int getSrcKioskId() {
		return srcKioskId;
	}

	public void setSrcKioskId(int srcKioskId) {
		this.srcKioskId = srcKioskId;
	}

	public String getSrcVendorOrderId() {
		return srcVendorOrderId;
	}

	public void setSrcVendorOrderId(String srcVendorOrderId) {
		this.srcVendorOrderId = srcVendorOrderId;
	}

	public int getSubSrcVendorId() {
		return subSrcVendorId;
	}

	public void setSubSrcVendorId(int subSrcVendorId) {
		this.subSrcVendorId = subSrcVendorId;
	}

	public String getSrcKioskOrderId() {
		return srcKioskOrderId;
	}

	public void setSrcKioskOrderId(String srcKioskOrderId) {
		this.srcKioskOrderId = srcKioskOrderId;
	}

	public int getFullfilmentVendorId() {
		return fullfilmentVendorId;
	}

	public void setFullfilmentVendorId(int fullfilmentVendorId) {
		this.fullfilmentVendorId = fullfilmentVendorId;
	}

	public String getFullfilmentVendorOrderId() {
		return fullfilmentVendorOrderId;
	}

	public void setFullfilmentVendorOrderId(String fullfilmentVendorOrderId) {
		this.fullfilmentVendorOrderId = fullfilmentVendorOrderId;
	}

	public int getFullfillmentLocId() {
		return fullfillmentLocId;
	}

	public void setFullfillmentLocId(int fullfillmentLocId) {
		this.fullfillmentLocId = fullfillmentLocId;
	}

	public int getDestinationLocId() {
		return destinationLocId;
	}

	public void setDestinationLocId(int destinationLocId) {
		this.destinationLocId = destinationLocId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getDestinationCustomerId() {
		return destinationCustomerId;
	}

	public void setDestinationCustomerId(int destinationCustomerId) {
		this.destinationCustomerId = destinationCustomerId;
	}

	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public Date getPromiseShipDttm() {
		return promiseShipDttm;
	}

	public void setPromiseShipDttm(Date promiseShipDttm) {
		this.promiseShipDttm = promiseShipDttm;
	}

	public Date getPromiseDeliveryDttm() {
		return promiseDeliveryDttm;
	}

	public void setPromiseDeliveryDttm(Date promiseDeliveryDttm) {
		this.promiseDeliveryDttm = promiseDeliveryDttm;
	}

	public Date getActualShipDttm() {
		return actualShipDttm;
	}

	public void setActualShipDttm(Date actualShipDttm) {
		this.actualShipDttm = actualShipDttm;
	}

	public Date getActualDeliveryDttm() {
		return actualDeliveryDttm;
	}

	public void setActualDeliveryDttm(Date actualDeliveryDttm) {
		this.actualDeliveryDttm = actualDeliveryDttm;
	}

	public String getOrderCompletedDttm() {
		return orderCompletedDttm;
	}

	public void setOrderCompletedDttm(String orderCompletedDttm) {
		this.orderCompletedDttm = orderCompletedDttm;
	}

	public Date getOrderSoldDttm() {
		return orderSoldDttm;
	}

	public void setOrderSoldDttm(Date orderSoldDttm) {
		this.orderSoldDttm = orderSoldDttm;
	}

	public String getCurrencyCodeType() {
		return currencyCodeType;
	}

	public void setCurrencyCodeType(String currencyCodeType) {
		this.currencyCodeType = currencyCodeType;
	}

	public String getCurrencyCodeId() {
		return currencyCodeId;
	}

	public void setCurrencyCodeId(String currencyCodeId) {
		this.currencyCodeId = currencyCodeId;
	}

	public double getOriginalOrderPrice() {
		return originalOrderPrice;
	}

	public void setOriginalOrderPrice(double originalOrderPrice) {
		this.originalOrderPrice = originalOrderPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public int getTotalOrderDiscount() {
		return totalOrderDiscount;
	}

	public void setTotalOrderDiscount(int totalOrderDiscount) {
		this.totalOrderDiscount = totalOrderDiscount;
	}

	public int getLoyaltyPrice() {
		return loyaltyPrice;
	}

	public void setLoyaltyPrice(int loyaltyPrice) {
		this.loyaltyPrice = loyaltyPrice;
	}

	public int getOrderTax() {
		return orderTax;
	}

	public void setOrderTax(int orderTax) {
		this.orderTax = orderTax;
	}

	public double getSoldAmount() {
		return soldAmount;
	}

	public void setSoldAmount(double soldAmount) {
		this.soldAmount = soldAmount;
	}

	public String getLoyaltyInd() {
		return loyaltyInd;
	}

	public void setLoyaltyInd(String loyaltyInd) {
		this.loyaltyInd = loyaltyInd;
	}

	public String getCouponInd() {
		return couponInd;
	}

	public void setCouponInd(String couponInd) {
		this.couponInd = couponInd;
	}

	public int getDiscountCardUsedCd() {
		return discountCardUsedCd;
	}

	public void setDiscountCardUsedCd(int discountCardUsedCd) {
		this.discountCardUsedCd = discountCardUsedCd;
	}

	public String getReplenishmentInd() {
		return replenishmentInd;
	}

	public void setReplenishmentInd(String replenishmentInd) {
		this.replenishmentInd = replenishmentInd;
	}

	public String getCostCalculationInd() {
		return costCalculationInd;
	}

	public void setCostCalculationInd(String costCalculationInd) {
		this.costCalculationInd = costCalculationInd;
	}

	public String getPmStatus() {
		return pmStatus;
	}

	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}

	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}

	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
	}

	public int getPrintsReturned() {
		return printsReturned;
	}

	public void setPrintsReturned(int printsReturned) {
		this.printsReturned = printsReturned;
	}

	public Date getRequestDeliveryDttm() {
		return requestDeliveryDttm;
	}

	public void setRequestDeliveryDttm(Date requestDeliveryDttm) {
		this.requestDeliveryDttm = requestDeliveryDttm;
	}

	public int getLoyaltyDiscountAmount() {
		return loyaltyDiscountAmount;
	}

	public void setLoyaltyDiscountAmount(int loyaltyDiscountAmount) {
		this.loyaltyDiscountAmount = loyaltyDiscountAmount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "POSOrder [sysOrderId=" + sysOrderId + ", orderNo=" + orderNo
				+ ", calenderId=" + calenderId + ", orderType=" + orderType
				+ ", orderDescription=" + orderDescription + ", owningLocId="
				+ owningLocId + ", orderOriginType=" + orderOriginType
				+ ", srcVendorId=" + srcVendorId + ", srcKioskId=" + srcKioskId
				+ ", srcVendorOrderId=" + srcVendorOrderId
				+ ", subSrcVendorId=" + subSrcVendorId + ", srcKioskOrderId="
				+ srcKioskOrderId + ", fullfilmentVendorId="
				+ fullfilmentVendorId + ", fullfilmentVendorOrderId="
				+ fullfilmentVendorOrderId + ", fullfillmentLocId="
				+ fullfillmentLocId + ", destinationLocId=" + destinationLocId
				+ ", status=" + status + ", customerId=" + customerId
				+ ", destinationCustomerId=" + destinationCustomerId
				+ ", orderPlacedDttm=" + orderPlacedDttm
				+ ", requestDeliveryDttm=" + requestDeliveryDttm
				+ ", promiseShipDttm=" + promiseShipDttm
				+ ", promiseDeliveryDttm=" + promiseDeliveryDttm
				+ ", actualShipDttm=" + actualShipDttm
				+ ", actualDeliveryDttm=" + actualDeliveryDttm
				+ ", orderCompletedDttm=" + orderCompletedDttm
				+ ", orderSoldDttm=" + orderSoldDttm + ", currencyCodeType="
				+ currencyCodeType + ", currencyCodeId=" + currencyCodeId
				+ ", originalOrderPrice=" + originalOrderPrice
				+ ", finalPrice=" + finalPrice + ", totalOrderDiscount="
				+ totalOrderDiscount + ", loyaltyPrice=" + loyaltyPrice
				+ ", loyaltyDiscountAmount=" + loyaltyDiscountAmount
				+ ", orderTax=" + orderTax + ", soldAmount=" + soldAmount
				+ ", loyaltyInd=" + loyaltyInd + ", couponInd=" + couponInd
				+ ", discountCardUsedInd=" + discountCardUsedCd
				+ ", replenishmentInd=" + replenishmentInd
				+ ", costCalculationInd=" + costCalculationInd + ", pmStatus="
				+ pmStatus + ", notes=" + notes + ", printsReturned="
				+ printsReturned + ", createUserId=" + createUserId
				+ ", createDttm=" + createDttm + ", updateUserId="
				+ updateUserId + ", updatedDttm=" + updatedDttm + "]";
	}

}
