package com.walgreens.oms.json.bean;

public class PayonFulfillmentData {
	private String vendorName;
	private long EnvelopeNumber;
	private String dateDone;
	private String transmittedDate;
	private double vendorCost;
	private double calculateRetailPrice;
	private String reportingDate;
	
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the envelopeNumber
	 */
	public long getEnvelopeNumber() {
		return EnvelopeNumber;
	}
	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	public void setEnvelopeNumber(long envelopeNumber) {
		EnvelopeNumber = envelopeNumber;
	}
	/**
	 * @return the dateDone
	 */
	public String getDateDone() {
		return dateDone;
	}
	/**
	 * @param dateDone the dateDone to set
	 */
	public void setDateDone(String dateDone) {
		this.dateDone = dateDone;
	}
	/**
	 * @return the transmittedDate
	 */
	public String getTransmittedDate() {
		return transmittedDate;
	}
	/**
	 * @param transmittedDate the transmittedDate to set
	 */
	public void setTransmittedDate(String transmittedDate) {
		this.transmittedDate = transmittedDate;
	}
	/**
	 * @return the vendorCost
	 */
	public double getVendorCost() {
		return vendorCost;
	}
	/**
	 * @param vendorCost the vendorCost to set
	 */
	public void setVendorCost(double vendorCost) {
		this.vendorCost = vendorCost;
	}
	/**
	 * @return the calculateRetailPrice
	 */
	public double getCalculateRetailPrice() {
		return calculateRetailPrice;
	}
	/**
	 * @param calculateRetailPrice the calculateRetailPrice to set
	 */
	public void setCalculateRetailPrice(double calculateRetailPrice) {
		this.calculateRetailPrice = calculateRetailPrice;
	}
	/**
	 * @return the reportingDate
	 */
	public String getReportingDate() {
		return reportingDate;
	}
	/**
	 * @param reportingDate the reportingDate to set
	 */
	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}

}
