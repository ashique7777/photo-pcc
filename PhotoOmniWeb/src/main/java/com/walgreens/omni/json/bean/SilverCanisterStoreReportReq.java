package com.walgreens.omni.json.bean;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"startDate",
"endDate",
"location",
"sortOrder",
"sortColumnName",
"currentPage"
})

public class SilverCanisterStoreReportReq {

	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("location")
	private String location;
	@JsonProperty("sortOrder")
	private String sortOrder;
	@JsonProperty("sortColumnName")
	private String sortColumnName;
	@JsonProperty("currentPage")
	private String currentPage;
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
	* The sortOrder
	*/
	@JsonProperty("sortOrder")
	public String getSortOrder() {
	return sortOrder;
	 }

	/**
	* 
	* @param sortOrder
	* The sortOrder
	*/
	@JsonProperty("sortOrder")
	public void setSortOrder(String sortOrder) {
	this.sortOrder = sortOrder;
	 }

	/**
	* 
	* @return
	* The sortColumnName
	*/
	@JsonProperty("sortColumnName")
	public String getSortColumnName() {
	return sortColumnName;
	 }

	/**
	* 
	* @param sortColumnName
	* The sortColumnName
	*/
	@JsonProperty("sortColumnName")
	public void setSortColumnName(String sortColumnName) {
	this.sortColumnName = sortColumnName;
	 }

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	 }

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	 }

	@JsonProperty("currentPage")
	public String getCurrentPage() {
		return currentPage;
	}

	@JsonProperty("currentPage")
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

}
