/**
 * 
 */
package com.walgreens.oms.json.bean;

/**
 * @author CTS
 *
 */
public class UnclaimedBean {
	private String customerId;
	private String customerName;
	private String phoneNumber;
	private String totalEnvelope;
	private String totalValue;
	private String orderRangePlaceddate;
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
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
}
