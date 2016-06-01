package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author CTS
 * 
 */
public class SalesReportByProductBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private long sysUserReportPrefId;
	private long userId;
	private long reportId;
	private String filterState;
	private String fileLocation;
	private List<String> fileNameList;
	private String reportType;
	private Map<String, Object> filterMap;

	public Map<String, Object> getFilterMap() {
		return filterMap;
	}

	public void setFilterMap(Map<String, Object> filterMap) {
		this.filterMap = filterMap;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public List<String> getFileNameList() {
		return fileNameList;
	}

	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 * @return sysUserReportPrefId
	 */
	public long getSysUserReportPrefId() {
		return sysUserReportPrefId;
	}

	/**
	 * 
	 * @param sysUserReportPrefId
	 *            the sysUserReportPrefId to set
	 */
	public void setSysUserReportPrefId(long sysUserReportPrefId) {
		this.sysUserReportPrefId = sysUserReportPrefId;
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
	 * @return reportId
	 */
	public long getReportId() {
		return reportId;
	}

	/**
	 * 
	 * @param reportId
	 *            the reportId to set
	 */
	public void setReportId(long reportId) {
		this.reportId = reportId;
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
	 * @return fileLocation
	 */
	public String getFileLocation() {
		return fileLocation;
	}

	/**
	 * 
	 * @param fileLocation
	 *            the fileLocation to set
	 */
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

}
