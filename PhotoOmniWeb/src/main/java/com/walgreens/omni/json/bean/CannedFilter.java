
package com.walgreens.omni.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author cognizant
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "startDate", "endDate", "orderTypeName", "orderTypeId",
		"inputChannelName", "inputChannelId","activeCd ","currrentPage", "totalRecord","pageSize","sortOrder","sortColumnName" })
public class CannedFilter {
	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("orderTypeName")
	private String orderTypeName;
	@JsonProperty("orderTypeId")
	private String orderTypeId;
	@JsonProperty("inputChannelName")
	private String inputChannelName;
	@JsonProperty("inputChannelId")
	private String inputChannelId;
	@JsonProperty("currrentPage")
	private String currrentPage;
	@JsonProperty("activeCd")
	private String activeCd;
	@JsonProperty("totalRecord")
	private String totalRecord;
	@JsonProperty("pageSize")
	private int pageSize;
	@JsonProperty("sortOrder")
	private String sortOrder;
	@JsonProperty("sortColumnName")
	private String sortColumnName;

	
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
	 * @return the orderTypeName
	 */
	@JsonProperty("orderTypeName")
	public String getOrderTypeName() {
		return orderTypeName;
	}
	/**
	 * @param orderTypeName the orderTypeName to set
	 */
	@JsonProperty("orderTypeName")
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}
	/**
	 * @return the orderTypeId
	 */
	@JsonProperty("orderTypeId")
	public String getOrderTypeId() {
		return orderTypeId;
	}
	/**
	 * @param orderTypeId the orderTypeId to set
	 */
	@JsonProperty("orderTypeId")
	public void setOrderTypeId(String orderTypeId) {
		this.orderTypeId = orderTypeId;
	}
	/**
	 * @return the inputChannelName
	 */
	@JsonProperty("inputChannelName")
	public String getInputChannelName() {
		return inputChannelName;
	}
	/**
	 * @param inputChannelName the inputChannelName to set
	 */
	@JsonProperty("inputChannelName")
	public void setInputChannelName(String inputChannelName) {
		this.inputChannelName = inputChannelName;
	}
	/**
	 * @return the inputChannelId
	 */
	@JsonProperty("inputChannelId")
	public String getInputChannelId() {
		return inputChannelId;
	}
	/**
	 * @param inputChannelId the inputChannelId to set
	 */
	@JsonProperty("inputChannelId")
	public void setInputChannelId(String inputChannelId) {
		this.inputChannelId = inputChannelId;
	}
	/**
	 * @return the currrentPage
	 */
	@JsonProperty("currrentPage")
	public String getCurrrentPage() {
		return currrentPage;
	}
	/**
	 * @param currrentPage the currrentPage to set
	 */
	@JsonProperty("currrentPage")
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}
	
	/**
	 * @return the activeCd
	 */
	@JsonProperty("activeCd")
	public String getActiveCd() {
		return activeCd;
	}
	/**
	 * @param activeCd the activeCd to set
	 */
	@JsonProperty("activeCd")
	public void setActiveCd(String activeCd) {
		this.activeCd = activeCd;
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
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @return the sortColumnName
	 */
	@JsonProperty("sortColumnName")
	public String getSortColumnName() {
		return sortColumnName;
	}
	/**
	 * @param sortColumnName the sortColumnName to set
	 */
	@JsonProperty("sortColumnName")
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}
	@JsonProperty("sortOrder")
	public String getSortOrder() {
		return sortOrder;
	}
	@JsonProperty("sortOrder")
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	

}
