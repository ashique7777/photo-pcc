package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "startDate", "endDate", "storeNumber", "vendorId","ediUpc","sortColumnName","sortOrder","currentPageNo","storePrint"})
public class PayOnFulfillmentReq {
	@JsonProperty("storeNumber")
	private String storeNumber;
	
	@JsonProperty("startDate")
	 private String startDate;
	
	@JsonProperty("endDate")
	 private String endDate;
	
	@JsonProperty("ediupcValue")
	 private String ediupcValue;
	
	@JsonProperty("vendor") // vendorId
	 private String vendorId;
	
	@JsonProperty("description") // vendor description
	 private String description;
	
	 @JsonProperty("sortColumnName")
	private String sortColumnName;
	 
	@JsonProperty("sortOrder")
	private String sortOrder;
	
	@JsonProperty("currrentPage")
	private String currrentPage;
	
	@JsonProperty("filtertypePay")
	private String filtertypePay;
	
	@JsonProperty("totalRecord")
	private String totalRecord;
	
	@JsonProperty("storePrint")
	private boolean storePrint;
	
	
	
	/**
	 * @return the storePrint
	 */
	public boolean isStorePrint() {
		return storePrint;
	}
	/**
	 * @param storePrint the storePrint to set
	 */
	public void setStorePrint(boolean storePrint) {
		this.storePrint = storePrint;
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
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	 * @return the sortColumnName
	 */
	public String getSortColumnName() {
		return sortColumnName;
	}
	/**
	 * @param sortColumnName the sortColumnName to set
	 */
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}
	/**
	 * @return the sortOrder
	 */
	public String getSortOrder() {
		return sortOrder;
	}
	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * @return the ediupcValue
	 */
	public String getEdiupcValue() {
		return ediupcValue;
	}
	/**
	 * @param ediupcValue the ediupcValue to set
	 */
	public void setEdiupcValue(String ediupcValue) {
		this.ediupcValue = ediupcValue;
	}
	/**
	 * @return the currrentPage
	 */
	public String getCurrrentPage() {
		return currrentPage;
	}
	/**
	 * @param currrentPage the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}
	/**
	 * @return the filtertypePay
	 */
	public String getFiltertypePay() {
		return filtertypePay;
	}
	/**
	 * @param filtertypePay the filtertypePay to set
	 */
	public void setFiltertypePay(String filtertypePay) {
		this.filtertypePay = filtertypePay;
	}
	/**
	 * @return the totalRecord
	 */
	public String getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
