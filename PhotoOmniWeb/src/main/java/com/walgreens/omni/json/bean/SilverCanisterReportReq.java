package com.walgreens.omni.json.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"startDate",
"endDate",
"location",
"status",
"number",
"pageNo",
"sortColumnName",
"sortOrder"
})
public class SilverCanisterReportReq {
	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("location")
	private String location;
	@JsonProperty("status")
	private String status;
	@JsonProperty("number")
	private String number;
	@JsonProperty("pageNo")
	private String pageNo;
	@JsonProperty("sortColumnName")
	private String sortColumnName;
	@JsonProperty("sortOrder")
	private String sortOrder;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The startDate
	*/
	@JsonProperty("startDate")
	public String getStartDate() {
	return startDate;
	 }

	/**
	* 
	* @param startDate
	* The startDate
	*/
	@JsonProperty("startDate")
	public void setStartDate(String startDate) {
	this.startDate = startDate;
	 }

	/**
	* 
	* @return
	* The endDate
	*/
	@JsonProperty("endDate")
	public String getEndDate() {
	return endDate;
	 }

	/**
	* 
	* @param endDate
	* The endDate
	*/
	@JsonProperty("endDate")
	public void setEndDate(String endDate) {
	this.endDate = endDate;
	 }

	/**
	* 
	* @return
	* The location
	*/
	@JsonProperty("location")
	public String getLocation() {
	return location;
	 }

	/**
	* 
	* @param location
	* The location
	*/
	@JsonProperty("location")
	public void setLocation(String location) {
	this.location = location;
	 }

	/**
	* 
	* @return
	* The status
	*/
	@JsonProperty("status")
	public String getStatus() {
	return status;
	 }

	/**
	* 
	* @param status
	* The status
	*/
	@JsonProperty("status")
	public void setStatus(String status) {
	this.status = status;
	 }

	/**
	* 
	* @return
	* The number
	*/
	@JsonProperty("number")
	public String getNumber() {
	return number;
	 }

	/**
	* 
	* @param number
	* The number
	*/
	@JsonProperty("number")
	public void setNumber(String number) {
	this.number = number;
	 }

	
	/**
	* 
	* @return
	* The pageNo
	*/
	@JsonProperty("pageNo")
	public String getPageNo() {
	return pageNo;
	 }

	/**
	* 
	* @param pageNo
	* The pageNo
	*/
	@JsonProperty("pageNo")
	public void setPageNo(String pageNo) {
	this.pageNo = pageNo;
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

	/**
	 * @return the sortOrder
	 */
	@JsonProperty("sortOrder")
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	@JsonProperty("sortOrder")
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	 }

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	 }


}
