/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author CTS
 *
 */
public class PayOnFulfillmentLWU implements Serializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	long orderId;
	String orderPlacedDttm;
	Timestamp originalOrderPlacedDttm;
	String orderNumber;
	String envelopeNo;
	long vendorId;
	long locationId;
	Date completedDttm;
	Date soldDttm;
	Date asnRecieveDttm;
	boolean vendorPaymentEligible = false;
	int storeNumber;
	String ediUPC;
	
//	private List<PayOnFulfillmentItemData> itemList = new ArrayList<PayOnFulfillmentItemData> ();
	
	int productId;
	double vendPaymentAmt;
	double calculatedPrice;
	int quantity;
	double itemSoldAmount;
	String vendorType;
	
	
	
	
	
	/**
	 * @return the vendorType
	 */
	public String getVendorType() {
		return vendorType;
	}
	/**
	 * @param vendorType the vendorType to set
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	/**
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the vendPaymentAmt
	 */
	public double getVendPaymentAmt() {
		return vendPaymentAmt;
	}
	/**
	 * @param vendPaymentAmt the vendPaymentAmt to set
	 */
	public void setVendPaymentAmt(double vendPaymentAmt) {
		this.vendPaymentAmt = vendPaymentAmt;
	}
	/**
	 * @return the calculatedPrice
	 */
	public double getCalculatedPrice() {
		return calculatedPrice;
	}
	/**
	 * @param calculatedPrice the calculatedPrice to set
	 */
	public void setCalculatedPrice(double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the itemSoldAmount
	 */
	public double getItemSoldAmount() {
		return itemSoldAmount;
	}
	/**
	 * @param itemSoldAmount the itemSoldAmount to set
	 */
	public void setItemSoldAmount(double itemSoldAmount) {
		this.itemSoldAmount = itemSoldAmount;
	}
	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the vendorId
	 */
	public long getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the locationId
	 */
	public long getLocationId() {
		return locationId;
	}
	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}
	/**
	 * @return the originalOrderPlacedDttm
	 */
	public Timestamp getOriginalOrderPlacedDttm() {
		return originalOrderPlacedDttm;
	}
	/**
	 * @param originalOrderPlacedDttm the originalOrderPlacedDttm to set
	 */
	public void setOriginalOrderPlacedDttm(Timestamp originalOrderPlacedDttm) {
		this.originalOrderPlacedDttm = originalOrderPlacedDttm;
	}
	/**
	 * @return the storeNumber
	 */
	public int getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(int storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the ediUPC
	 */
	public String getEdiUPC() {
		return ediUPC;
	}
	/**
	 * @param ediUPC the ediUPC to set
	 */
	public void setEdiUPC(String ediUPC) {
		this.ediUPC = ediUPC;
	}

	
	
	
	/**
	 * @return the asnRecieveDttm
	 */
	public Date getAsnRecieveDttm() {
		return asnRecieveDttm;
	}
	/**
	 * @param asnRecieveDttm the asnRecieveDttm to set
	 */
	public void setAsnRecieveDttm(Date asnRecieveDttm) {
		this.asnRecieveDttm = asnRecieveDttm;
	}
	/**
	 * @return the itemList
	 *//*
	public List<PayOnFulfillmentItemData> getItemList() {
		return itemList;
	}
	*//**
	 * @param itemList the itemList to set
	 *//*
	public void setItemList(List<PayOnFulfillmentItemData> itemList) {
		this.itemList = itemList;
	}*/
	/**
	 * @return the vendorPaymentEligible
	 */
	public boolean isVendorPaymentEligible() {
		return vendorPaymentEligible;
	}
	/**
	 * @param vendorPaymentEligible the vendorPaymentEligible to set
	 */
	public void setVendorPaymentEligible(boolean vendorPaymentEligible) {
		this.vendorPaymentEligible = vendorPaymentEligible;
	}
	
	
	
	/**
	 * @return the orderPlacedDttm
	 */
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	/**
	 * @param orderPlacedDttm the orderPlacedDttm to set
	 */
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
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
	 * @return the envelopeNo
	 */
	public String getEnvelopeNo() {
		return envelopeNo;
	}
	/**
	 * @param envelopeNo the envelopeNo to set
	 */
	public void setEnvelopeNo(String envelopeNo) {
		this.envelopeNo = envelopeNo;
	}
	
	
	/**
	 * @return the completedDttm
	 */
	public Date getCompletedDttm() {
		return completedDttm;
	}
	/**
	 * @param completedDttm the completedDttm to set
	 */
	public void setCompletedDttm(Date completedDttm) {
		this.completedDttm = completedDttm;
	}
	/**
	 * @return the soldDttm
	 */
	public Date getSoldDttm() {
		return soldDttm;
	}
	/**
	 * @param soldDttm the soldDttm to set
	 */
	public void setSoldDttm(Date soldDttm) {
		this.soldDttm = soldDttm;
	}
	
	
	
}
