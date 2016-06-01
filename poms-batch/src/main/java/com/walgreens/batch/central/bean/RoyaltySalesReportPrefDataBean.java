package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class RoyaltySalesReportPrefDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long sysUserReportPrefId;
	private long userId;
	private long reportId;
	private String filterState;
	private String fileLocation;
	private List<String> fileNameList;
	private Map<String, Object> filterMap;
	private String groupBy;
	private String reportType;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public Map<String, Object> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(Map<String, Object> filterMap) {
		this.filterMap = filterMap;
	}
	

	public long getSysUserReportPrefId() {
		return sysUserReportPrefId;
	}

	public void setSysUserReportPrefId(long sysUserReportPrefId) {
		this.sysUserReportPrefId = sysUserReportPrefId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public String getFilterState() {
		return filterState;
	}

	public void setFilterState(String filterState) {
		this.filterState = filterState;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public List<String> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}
}
