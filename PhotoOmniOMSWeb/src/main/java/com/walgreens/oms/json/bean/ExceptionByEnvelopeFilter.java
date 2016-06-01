package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "startDate", "endDate", "storeNumber", "customerLastName",
		"employeeLastName", "exceptionStatus", "reasonType", "envelopeEnteredDate", "envNumber", "sortColumnName", "sortOrder", "currentPageNo" })
public class ExceptionByEnvelopeFilter {

	
	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("storeNumber")
	private String storeNumber;
	@JsonProperty("customerLastName")
	private String customerLastName;
	@JsonProperty("employeeLastName")
	private String employeeLastName;
	@JsonProperty("exceptionStatus")
	private String exceptionStatus;
	@JsonProperty("reasonType")
	private String reasonType;
	@JsonProperty("envelopeEnteredDate")
	private String envelopeEnteredDate;
	@JsonProperty("envNumber")
	private String envNumber;
	@JsonProperty("sortColumnName")
	private String sortColumnName;
	@JsonProperty("sortOrder")
	private String sortOrder;
	@JsonProperty("currentPageNo")
	private String currentPageNo;
	@JsonProperty("isPrint")
	private boolean isPrint;
	
	
	
	/**
	 * @return the isPrint
	 */
	public boolean isPrint() {
		return isPrint;
	}
	/**
	 * @param isPrint the isPrint to set
	 */
	public void setPrint(boolean isPrint) {
		this.isPrint = isPrint;
	}
	/**
	 * @return the currentPageNo
	 */
	public String getCurrentPageNo() {
		return currentPageNo;
	}
	/**
	 * @param currentPageNo the currentPageNo to set
	 */
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
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
	 * @return the employeeLastName
	 */
	public String getEmployeeLastName() {
		return employeeLastName;
	}
	/**
	 * @param employeeLastName the employeeLastName to set
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}
	/**
	 * @return the exceptionStatus
	 */
	public String getExceptionStatus() {
		return exceptionStatus;
	}
	/**
	 * @param exceptionStatus the exceptionStatus to set
	 */
	public void setExceptionStatus(String exceptionStatus) {
		this.exceptionStatus = exceptionStatus;
	}
	/**
	 * @return the reasonType
	 */
	public String getReasonType() {
		return reasonType;
	}
	/**
	 * @param reasonType the reasonType to set
	 */
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	/**
	 * @return the envelopeEnteredDate
	 */
	public String getEnvelopeEnteredDate() {
		return envelopeEnteredDate;
	}
	/**
	 * @param envelopeEnteredDate the envelopeEnteredDate to set
	 */
	public void setEnvelopeEnteredDate(String envelopeEnteredDate) {
		this.envelopeEnteredDate = envelopeEnteredDate;
	}
	/**
	 * @return the envNumber
	 */
	public String getEnvNumber() {
		return envNumber;
	}
	/**
	 * @param envNumber the envNumber to set
	 */
	public void setEnvNumber(String envNumber) {
		this.envNumber = envNumber;
	}
	
	
	
}
