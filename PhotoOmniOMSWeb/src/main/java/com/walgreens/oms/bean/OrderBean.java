/**
 * 
 */
package com.walgreens.oms.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 *
 */
public class OrderBean {
	Long sysOrderID;
	String orderNBR;
	Long sysCalID;
	String orderType;
	String orderDesc;
	Long ownLocId;
	Long fulfillLocID;
	String orderOriginType;
	Long sysSRCVendorID;
	String srcVendorOrderNBR;
	String srcKioskOrderNBR;
	Long sysFullFillVendorID;
	String Status;
	Long sysCustomerId;
	Timestamp promiseDeliveryDTTM;
	Timestamp promiseShipTTM;
	Timestamp orderPlacedDTTM;
	Timestamp orderCompletedDTTM;
	String currencyCodeID;
	Double originalOrderPrice;
	Double finalPrice;
	Double totalOrderDiscount;
	Double loyaltyPrice;
	Double loyalyDisAmount;
	Integer couponCD;
	Integer discountCardUserCd;
	Integer ownLocTZOFFSET;
	Integer vendorLocTZOFFSET;
	String customerLastName;
	String customerFirstName;
	String customerUpperLastName;
	String customerUpperFirstName;
	String emailAddr;
	String areacode;
	String phoneNBR;
	String fullPhoneNBR;
	String createUserID;
	String createDTTM;
	String updateUserID;
	String updateDTTM;
	Long sysStorePosId;
	/**
	 * @return the sysOrderID
	 */
	public Long getSysOrderID() {
		return sysOrderID;
	}
	/**
	 * @param sysOrderID the sysOrderID to set
	 */
	public void setSysOrderID(Long sysOrderID) {
		this.sysOrderID = sysOrderID;
	}
	/**
	 * @return the orderNBR
	 */
	public String getOrderNBR() {
		return orderNBR;
	}
	/**
	 * @param orderNBR the orderNBR to set
	 */
	public void setOrderNBR(String orderNBR) {
		this.orderNBR = orderNBR;
	}
	/**
	 * @return the sysCalID
	 */
	public Long getSysCalID() {
		return sysCalID;
	}
	/**
	 * @param sysCalID the sysCalID to set
	 */
	public void setSysCalID(Long sysCalID) {
		this.sysCalID = sysCalID;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
		this.orderDesc = orderDesc;
	}
	/**
	 * @return the ownLocId
	 */
	public Long getOwnLocId() {
		return ownLocId;
	}
	/**
	 * @param ownLocId the ownLocId to set
	 */
	public void setOwnLocId(Long ownLocId) {
		this.ownLocId = ownLocId;
	}
	/**
	 * @return the fulfillLocID
	 */
	public Long getFulfillLocID() {
		return fulfillLocID;
	}
	/**
	 * @param fulfillLocID the fulfillLocID to set
	 */
	public void setFulfillLocID(Long fulfillLocID) {
		this.fulfillLocID = fulfillLocID;
	}
	/**
	 * @return the orderOriginType
	 */
	public String getOrderOriginType() {
		return orderOriginType;
	}
	/**
	 * @param orderOriginType the orderOriginType to set
	 */
	public void setOrderOriginType(String orderOriginType) {
		this.orderOriginType = orderOriginType;
	}
	/**
	 * @return the sysSRCVendorID
	 */
	public Long getSysSRCVendorID() {
		return sysSRCVendorID;
	}
	/**
	 * @param sysSRCVendorID the sysSRCVendorID to set
	 */
	public void setSysSRCVendorID(Long sysSRCVendorID) {
		this.sysSRCVendorID = sysSRCVendorID;
	}
	/**
	 * @return the srcVendorOrderNBR
	 */
	public String getSrcVendorOrderNBR() {
		return srcVendorOrderNBR;
	}
	/**
	 * @param srcVendorOrderNBR the srcVendorOrderNBR to set
	 */
	public void setSrcVendorOrderNBR(String srcVendorOrderNBR) {
		this.srcVendorOrderNBR = srcVendorOrderNBR;
	}
	/**
	 * @return the srcKioskOrderNBR
	 */
	public String getSrcKioskOrderNBR() {
		return srcKioskOrderNBR;
	}
	/**
	 * @param srcKioskOrderNBR the srcKioskOrderNBR to set
	 */
	public void setSrcKioskOrderNBR(String srcKioskOrderNBR) {
		this.srcKioskOrderNBR = srcKioskOrderNBR;
	}
	/**
	 * @return the sysFullFillVendorID
	 */
	public Long getSysFullFillVendorID() {
		return sysFullFillVendorID;
	}
	/**
	 * @param sysFullFillVendorID the sysFullFillVendorID to set
	 */
	public void setSysFullFillVendorID(Long sysFullFillVendorID) {
		this.sysFullFillVendorID = sysFullFillVendorID;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the sysCustomerId
	 */
	public Long getSysCustomerId() {
		return sysCustomerId;
	}
	/**
	 * @param sysCustomerId the sysCustomerId to set
	 */
	public void setSysCustomerId(Long sysCustomerId) {
		this.sysCustomerId = sysCustomerId;
	}
	/**
	 * @return the promiseDeliveryDTTM
	 */
	public Timestamp getPromiseDeliveryDTTM() {
		return promiseDeliveryDTTM;
	}
	/**
	 * @param promiseDeliveryDTTM the promiseDeliveryDTTM to set
	 */
	public void setPromiseDeliveryDTTM(Timestamp promiseDeliveryDTTM) {
		this.promiseDeliveryDTTM = promiseDeliveryDTTM;
	}
	/**
	 * @return the promiseShipTTM
	 */
	public Timestamp getPromiseShipTTM() {
		return promiseShipTTM;
	}
	/**
	 * @param promiseShipTTM the promiseShipTTM to set
	 */
	public void setPromiseShipTTM(Timestamp promiseShipTTM) {
		this.promiseShipTTM = promiseShipTTM;
	}
	/**
	 * @return the orderPlacedDTTM
	 */
	public Timestamp getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}
	/**
	 * @param orderPlacedDTTM the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(Timestamp orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
	}
	/**
	 * @return the orderCompletedDTTM
	 */
	public Timestamp getOrderCompletedDTTM() {
		return orderCompletedDTTM;
	}
	/**
	 * @param orderCompletedDTTM the orderCompletedDTTM to set
	 */
	public void setOrderCompletedDTTM(Timestamp orderCompletedDTTM) {
		this.orderCompletedDTTM = orderCompletedDTTM;
	}
	/**
	 * @return the currencyCodeID
	 */
	public String getCurrencyCodeID() {
		return currencyCodeID;
	}
	/**
	 * @param currencyCodeID the currencyCodeID to set
	 */
	public void setCurrencyCodeID(String currencyCodeID) {
		this.currencyCodeID = currencyCodeID;
	}
	/**
	 * @return the originalOrderPrice
	 */
	public Double getOriginalOrderPrice() {
		return originalOrderPrice;
	}
	/**
	 * @param originalOrderPrice the originalOrderPrice to set
	 */
	public void setOriginalOrderPrice(Double originalOrderPrice) {
		this.originalOrderPrice = originalOrderPrice;
	}
	/**
	 * @return the finalPrice
	 */
	public Double getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * @return the totalOrderDiscount
	 */
	public Double getTotalOrderDiscount() {
		return totalOrderDiscount;
	}
	/**
	 * @param totalOrderDiscount the totalOrderDiscount to set
	 */
	public void setTotalOrderDiscount(Double totalOrderDiscount) {
		this.totalOrderDiscount = totalOrderDiscount;
	}
	/**
	 * @return the loyaltyPrice
	 */
	public Double getLoyaltyPrice() {
		return loyaltyPrice;
	}
	/**
	 * @param loyaltyPrice the loyaltyPrice to set
	 */
	public void setLoyaltyPrice(Double loyaltyPrice) {
		this.loyaltyPrice = loyaltyPrice;
	}
	/**
	 * @return the loyalyDisAmount
	 */
	public Double getLoyalyDisAmount() {
		return loyalyDisAmount;
	}
	/**
	 * @param loyalyDisAmount the loyalyDisAmount to set
	 */
	public void setLoyalyDisAmount(Double loyalyDisAmount) {
		this.loyalyDisAmount = loyalyDisAmount;
	}
	/**
	 * @return the couponCD
	 */
	public Integer getCouponCD() {
		return couponCD;
	}
	/**
	 * @param couponCD the couponCD to set
	 */
	public void setCouponCD(Integer couponCD) {
		this.couponCD = couponCD;
	}
	/**
	 * @return the discountCardUserCd
	 */
	public Integer getDiscountCardUserCd() {
		return discountCardUserCd;
	}
	/**
	 * @param discountCardUserCd the discountCardUserCd to set
	 */
	public void setDiscountCardUserCd(Integer discountCardUserCd) {
		this.discountCardUserCd = discountCardUserCd;
	}
	/**
	 * @return the ownLocTZOFFSET
	 */
	public Integer getOwnLocTZOFFSET() {
		return ownLocTZOFFSET;
	}
	/**
	 * @param ownLocTZOFFSET the ownLocTZOFFSET to set
	 */
	public void setOwnLocTZOFFSET(Integer ownLocTZOFFSET) {
		this.ownLocTZOFFSET = ownLocTZOFFSET;
	}
	/**
	 * @return the vendorLocTZOFFSET
	 */
	public Integer getVendorLocTZOFFSET() {
		return vendorLocTZOFFSET;
	}
	/**
	 * @param vendorLocTZOFFSET the vendorLocTZOFFSET to set
	 */
	public void setVendorLocTZOFFSET(Integer vendorLocTZOFFSET) {
		this.vendorLocTZOFFSET = vendorLocTZOFFSET;
	}
	/**
	 * @return the customerLastName
	 */
	public String getCustomerLastName() {
		return customerLastName;
	}
	/**
	 * @param customerLastName the customerLastName to set
	 */
	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}
	/**
	 * @return the customerFirstName
	 */
	public String getCustomerFirstName() {
		return customerFirstName;
	}
	/**
	 * @param customerFirstName the customerFirstName to set
	 */
	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}
	/**
	 * @return the customerUpperLastName
	 */
	public String getCustomerUpperLastName() {
		return customerUpperLastName;
	}
	/**
	 * @param customerUpperLastName the customerUpperLastName to set
	 */
	public void setCustomerUpperLastName(String customerUpperLastName) {
		this.customerUpperLastName = customerUpperLastName;
	}
	/**
	 * @return the customerUpperFirstName
	 */
	public String getCustomerUpperFirstName() {
		return customerUpperFirstName;
	}
	/**
	 * @param customerUpperFirstName the customerUpperFirstName to set
	 */
	public void setCustomerUpperFirstName(String customerUpperFirstName) {
		this.customerUpperFirstName = customerUpperFirstName;
	}
	/**
	 * @return the emailAddr
	 */
	public String getEmailAddr() {
		return emailAddr;
	}
	/**
	 * @param emailAddr the emailAddr to set
	 */
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	/**
	 * @return the areacode
	 */
	public String getAreacode() {
		return areacode;
	}
	/**
	 * @param areacode the areacode to set
	 */
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	/**
	 * @return the phoneNBR
	 */
	public String getPhoneNBR() {
		return phoneNBR;
	}
	/**
	 * @param phoneNBR the phoneNBR to set
	 */
	public void setPhoneNBR(String phoneNBR) {
		this.phoneNBR = phoneNBR;
	}
	/**
	 * @return the fullPhoneNBR
	 */
	public String getFullPhoneNBR() {
		return fullPhoneNBR;
	}
	/**
	 * @param fullPhoneNBR the fullPhoneNBR to set
	 */
	public void setFullPhoneNBR(String fullPhoneNBR) {
		this.fullPhoneNBR = fullPhoneNBR;
	}
	/**
	 * @return the createUserID
	 */
	public String getCreateUserID() {
		return createUserID;
	}
	/**
	 * @param createUserID the createUserID to set
	 */
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}
	/**
	 * @return the createDTTM
	 */
	public String getCreateDTTM() {
		return createDTTM;
	}
	/**
	 * @param createDTTM the createDTTM to set
	 */
	public void setCreateDTTM(String createDTTM) {
		this.createDTTM = createDTTM;
	}
	/**
	 * @return the updateUserID
	 */
	public String getUpdateUserID() {
		return updateUserID;
	}
	/**
	 * @param updateUserID the updateUserID to set
	 */
	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}
	/**
	 * @return the updateDTTM
	 */
	public String getUpdateDTTM() {
		return updateDTTM;
	}
	/**
	 * @param updateDTTM the updateDTTM to set
	 */
	public void setUpdateDTTM(String updateDTTM) {
		this.updateDTTM = updateDTTM;
	}
	public Long getSysStorePosId() {
		return sysStorePosId;
	}
	public void setSysStorePosId(Long sysStorePosId) {
		this.sysStorePosId = sysStorePosId;
	}
	

}
