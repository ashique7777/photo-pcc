package com.walgreens.omni.bean;

import java.util.Date;
/**
 * @author rathorde
 * 
 */
public class DashboardReportBean {

	private long reportId;
	private long userId;

	private String reportName;
	private String preferenceType;

	private String filterState;
	private String sortState;

	private String hiddenColumn;
	private boolean autoRefresh;
	private boolean autoExecute;
	private long refreshInterval;

	private boolean active;
	private String crateUserId;
	private String updateUserId;

	private Date createdDate;
	private Date updatedDate;
	private long pageSize;
	private boolean filterEnabled;

	private boolean defaultItem;

	/**
	 * @return reportId
	 */
	public long getReportId() {
		return reportId;
	}

	/**
	 * @param reportId
	 *            the reportId to set
	 */
	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	/**
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * 
	 * @param reportName
	 *            the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * 
	 * @return preferenceType
	 */
	public String getPreferenceType() {
		return preferenceType;
	}

	/**
	 * 
	 * @param preferenceType
	 *            the preferenceType to set
	 */
	public void setPreferenceType(String preferenceType) {
		this.preferenceType = preferenceType;
	}

	/**
	 * 
	 * @return filterState
	 */
	public String getFilterState() {
		return filterState;
	}

	/**
	 * 
	 * @param filterState
	 *            the filterState to set
	 */
	public void setFilterState(String filterState) {
		this.filterState = filterState;
	}

	/**
	 * 
	 * @return sortState
	 */
	public String getSortState() {
		return sortState;
	}

	/**
	 * 
	 * @param sortState
	 *            the sortState to set
	 */
	public void setSortState(String sortState) {
		this.sortState = sortState;
	}

	/**
	 * 
	 * @return hiddenColumn
	 */
	public String getHiddenColumn() {
		return hiddenColumn;
	}

	/**
	 * 
	 * @param hiddenColumn
	 *            the hiddenColumn to set
	 */
	public void setHiddenColumn(String hiddenColumn) {
		this.hiddenColumn = hiddenColumn;
	}

	/**
	 * 
	 * @return autoRefresh
	 */
	public boolean isAutoRefresh() {
		return autoRefresh;
	}

	/**
	 * 
	 * @param autoRefresh
	 *            the autoRefresh to set
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	/**
	 * 
	 * @return autoExecute
	 */
	public boolean isAutoExecute() {
		return autoExecute;
	}

	/**
	 * 
	 * @param autoExecute
	 *            the autoExecute to set
	 */
	public void setAutoExecute(boolean autoExecute) {
		this.autoExecute = autoExecute;
	}

	/**
	 * 
	 * @return refreshInterval
	 */
	public long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * 
	 * @param refreshInterval
	 *            the refreshInterval to set
	 */
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	/**
	 * 
	 * @return active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * 
	 * @return crateUserId
	 */
	public String getCrateUserId() {
		return crateUserId;
	}

	/**
	 * 
	 * @param crateUserId
	 *            the crateUserId to set
	 */
	public void setCrateUserId(String crateUserId) {
		this.crateUserId = crateUserId;
	}

	/**
	 * 
	 * @return updateUserId
	 */
	public String getUpdateUserId() {
		return updateUserId;
	}

	/**
	 * 
	 * @param updateUserId
	 *            the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * 
	 * @return createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * 
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * 
	 * @return updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * 
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * 
	 * @return pageSize
	 */
	public long getPageSize() {
		return pageSize;
	}

	/**
	 * 
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 
	 * @return defaultItem
	 */
	public boolean isDefaultItem() {
		return defaultItem;
	}

	/**
	 * 
	 * @param defaultItem
	 *            the defaultItem to set
	 */
	public void setDefaultItem(boolean defaultItem) {
		this.defaultItem = defaultItem;
	}

	/**
	 * 
	 * @return filterEnabled
	 */
	public boolean isFilterEnabled() {
		return filterEnabled;
	}

	/**
	 * 
	 * @param filterEnabled
	 *            the filterEnabled to set
	 */
	public void setFilterEnabled(boolean filterEnabled) {
		this.filterEnabled = filterEnabled;
	}

}
