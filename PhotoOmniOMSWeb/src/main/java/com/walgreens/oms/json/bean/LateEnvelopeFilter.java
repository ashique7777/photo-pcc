package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "startDate", "endDate", "currrentPage","sortColumnName","sortOrder" })
public class LateEnvelopeFilter {
	@JsonProperty("startDate")
	private String startDate;
	
	@JsonProperty("endDate")
	private String endDate;
	
	@JsonProperty("sortColumnName")
	private String sortColumnName;
	@JsonProperty("sortOrder")
	private String sortOrder;
	@JsonProperty("flag")
	private boolean flag;
	@JsonProperty("reportName")
	private String reportName;
	
	private int pageSizeResponse;
	
	
	
	
	
	
	
	/**
	 * @return the pageSizeResponse
	 */
	public int getPageSizeResponse() {
		return pageSizeResponse;
	}
	/**
	 * @param pageSizeResponse the pageSizeResponse to set
	 */
	public void setPageSizeResponse(int pageSizeResponse) {
		this.pageSizeResponse = pageSizeResponse;
	}
	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	@JsonProperty("flag")
	public void setFlag(boolean flag) {
		this.flag = flag;
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
	@JsonProperty("storeId")
	private String storeNumber;
	
	@JsonProperty("currrentPage")
	private String currentPageNo;
	
	@JsonProperty("currentPageNo")
	public String getCurrentPageNo() {
		return currentPageNo;
	}
	@JsonProperty("currentPageNo")
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
	}
	/**
	 * @return the startDate
	 */
	@JsonProperty("startDate")
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	@JsonProperty("startDate")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	@JsonProperty("endDate")
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	@JsonProperty("endDate")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LateEnvelopeFilter [startDate=" + startDate + ", endDate="
				+ endDate + ", sortColumnName=" + sortColumnName
				+ ", sortOrder=" + sortOrder + ", currentPageNo="
				+ currentPageNo + "]";
	}
	
}
