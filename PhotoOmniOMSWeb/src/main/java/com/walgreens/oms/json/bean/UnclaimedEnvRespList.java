package com.walgreens.oms.json.bean;


public class UnclaimedEnvRespList {
	private Long customerId;
	private String customerName;
	private String phoneNumber;
	private String totalEnvelope;
	private String totalValue;
	private String orderRangePlaceddate;
	private int totalRecord;
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	/**
	 * @return the totalEnvelope
	 */
	public String getTotalEnvelope() {
		return totalEnvelope;
	}
	/**
	 * @param totalEnvelope the totalEnvelope to set
	 */
	public void setTotalEnvelope(String totalEnvelope) {
		this.totalEnvelope = totalEnvelope;
	}
	/**
	 * @return the totalValue
	 */
	public String getTotalValue() {
		return totalValue;
	}
	/**
	 * @param totalValue the totalValue to set
	 */
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	/**
	 * @return the orderRangePlaceddate
	 */
	public String getOrderRangePlaceddate() {
		return orderRangePlaceddate;
	}
	/**
	 * @param orderRangePlaceddate the orderRangePlaceddate to set
	 */
	public void setOrderRangePlaceddate(String orderRangePlaceddate) {
		this.orderRangePlaceddate = orderRangePlaceddate;
	}
	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
