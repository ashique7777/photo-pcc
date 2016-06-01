/**
 * 
 */
package com.walgreens.oms.bean;

/**
 * @author CTS
 *
 */
public class PayOnFulfillmentCSVRespData {
	
	//{ "SerialNumber", "StoreNumber", "ReportingDate", "EnvelopeNumber", "EDIupc", "Quantity", "Cost","DoneDate" };
	private int totalRecord;
	private int serialNumber;
	private String storeNumber;
	private String reportingDate;
	private String envelopeNumber;
	private String eDIupc;
	private int quantity;
	private String doneDate;

	private double cost;
	
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	/**
	 * @return the eDIupc
	 */
	public String geteDIupc() {
		return eDIupc;
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
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the envelopeNumber
	 */
	public String getEnvelopeNumber() {
		return envelopeNumber;
	}
	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	public void setEnvelopeNumber(String envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	/**
	 * @return the eDIupc
	 */
	public String getEDIupc() {
		return eDIupc;
	}
	/**
	 * @param eDIupc the eDIupc to set
	 */
	public void seteDIupc(String eDIupc) {
		this.eDIupc = eDIupc;
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
	 * @return the doneDate
	 */
	public String getDoneDate() {
		return doneDate;
	}
	/**
	 * @param doneDate the doneDate to set
	 */
	public void setDoneDate(String doneDate) {
		this.doneDate = doneDate;
	}

}
