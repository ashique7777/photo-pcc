/**
 * 
 */
package com.walgreens.oms.json.bean;

/**
 * @author CTS
 *
 */
public class PayOnFulfillmentRespData {
	private int totalRecord;
	private int serialNumber;
	private String storeNumber;
	private String reportingDate;
	private String envelopeNumber;
	private String eDIupc;
	private int quantity;
	private double cost;
	private String asnRecievedDate;
	private String ediTransferDate;
	private double retailPrice;
	private String doneDate;
	private String vendorId;
	private int sysLocationId;
	private int sysVendorId;
	private String vendorNbr;
	
	
	
	/**
	 * @return the vendorNbr
	 */
	public String getVendorNbr() {
		return vendorNbr;
	}
	/**
	 * @param vendorNbr the vendorNbr to set
	 */
	public void setVendorNbr(String vendorNbr) {
		this.vendorNbr = vendorNbr;
	}
	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @return the asnRecievedDate
	 */
	public String getAsnRecievedDate() {
		return asnRecievedDate;
	}
	/**
	 * @param asnRecievedDate the asnRecievedDate to set
	 */
	public void setAsnRecievedDate(String asnRecievedDate) {
		this.asnRecievedDate = asnRecievedDate;
	}
	/**
	 * @return the ediTransferDate
	 */
	public String getEdiTransferDate() {
		return ediTransferDate;
	}
	/**
	 * @param ediTransferDate the ediTransferDate to set
	 */
	public void setEdiTransferDate(String ediTransferDate) {
		this.ediTransferDate = ediTransferDate;
	}
	/**
	 * @return the retailPrice
	 */
	public double getRetailPrice() {
		return retailPrice;
	}
	/**
	 * @param retailPrice the retailPrice to set
	 */
	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
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
	/**
	 * @return the vendorId
	 */
	public String getVendorId() {
		return vendorId;
	}
	/**
	 * @param vendorId the vendorId to set
	 */
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	/**
	 * @return the sysLocationId
	 */
	public int getSysLocationId() {
		return sysLocationId;
	}
	/**
	 * @param sysLocationId the sysLocationId to set
	 */
	public void setSysLocationId(int sysLocationId) {
		this.sysLocationId = sysLocationId;
	}
	/**
	 * @return the sysVendorId
	 */
	public int getSysVendorId() {
		return sysVendorId;
	}
	/**
	 * @param sysVendorId the sysVendorId to set
	 */
	public void setSysVendorId(int sysVendorId) {
		this.sysVendorId = sysVendorId;
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
	public String geteDIupc() {
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
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	/**
	 * @return the totalRec
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRec the totalRec to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

}
