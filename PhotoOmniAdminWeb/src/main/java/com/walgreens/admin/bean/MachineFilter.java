package com.walgreens.admin.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "startDate", "endDate", "machineId", "machineName",
		"location", "locationId", "currrentPage", "storeId", "isPagination" })
public class MachineFilter {

	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("machineId")
	private String machineId;
	@JsonProperty("machineName")
	private String machineName;
	@JsonProperty("location")
	private String location;
	@JsonProperty("locationId")
	private String locationId;
	@JsonProperty("currrentPage")
	private String currentPageNo;
	@JsonProperty("storeId")
	private String storeId;
	@JsonProperty("isPagination")
	private boolean pagination;
	@JsonProperty("sortColumnName")
	private String sortColumnName;
	@JsonProperty("sortOrder")
	private String sortOrder;

	/**
	 * @return the startDate
	 */
	@JsonProperty("startDate")
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
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
	 * @param endDate
	 *            the endDate to set
	 */
	@JsonProperty("endDate")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the machineId
	 */
	@JsonProperty("machineId")
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId
	 *            the machineId to set
	 */
	@JsonProperty("machineId")
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * @return the machineName
	 */
	@JsonProperty("machineName")
	public String getMachineName() {
		return machineName;
	}

	/**
	 * @param machineName
	 *            the machineName to set
	 */
	@JsonProperty("machineName")
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	/**
	 * @return the location
	 */
	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the locationId
	 */
	@JsonProperty("locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	@JsonProperty("locationId")
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the currentPageNo
	 */
	@JsonProperty("currrentPage")
	public String getCurrentPageNo() {
		return currentPageNo;
	}

	/**
	 * @param currentPageNo
	 *            the currentPageNo to set
	 */
	@JsonProperty("currrentPage")
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	/**
	 * @return the storeId
	 */
	@JsonProperty("storeId")
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId
	 *            the storeId to set
	 */
	@JsonProperty("storeId")
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the pagination
	 */
	@JsonProperty("isPagination")
	public boolean isPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            the pagination to set
	 */
	@JsonProperty("isPagination")
	public void setPagination(boolean pagination) {
		this.pagination = pagination;
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

	

}
