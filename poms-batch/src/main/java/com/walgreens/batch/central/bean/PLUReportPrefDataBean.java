package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.List;

public class PLUReportPrefDataBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -51281638888405870L;

	private long sysUserReportPrefId;
	private long userId;
	private long reportId;
	private String filterState;
	private String fileLocation;
	private List<String> fileNameList;
	private String reportType;
	
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
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
}
