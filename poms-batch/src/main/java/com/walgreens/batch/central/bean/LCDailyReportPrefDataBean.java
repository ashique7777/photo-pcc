package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.List;

public class LCDailyReportPrefDataBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long reportPrefId;
	private Long userId;
	private Long reportId;
	private String filterState;
	private String fileLocation;
	private List<String> fileNameList;
	private String startDate;
	private String endDate; 
	/**
	 * @return the reportPrefId
	 */
	public Long getReportPrefId() {
		return reportPrefId;
	}
	/**
	 * @param reportPrefId the reportPrefId to set
	 */
	public void setReportPrefId(Long reportPrefId) {
		this.reportPrefId = reportPrefId;
	}
	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * @return the reportId
	 */
	public Long getReportId() {
		return reportId;
	}
	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	/**
	 * @return the filterState
	 */
	public String getFilterState() {
		return filterState;
	}
	/**
	 * @param filterState the filterState to set
	 */
	public void setFilterState(String filterState) {
		this.filterState = filterState;
	}
	/**
	 * @return the fileLocation
	 */
	public String getFileLocation() {
		return fileLocation;
	}
	/**
	 * @param fileLocation the fileLocation to set
	 */
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the fileNameList
	 */
	public List<String> getFileNameList() {
		return fileNameList;
	}
	/**
	 * @param fileNameList the fileNameList to set
	 */
	public void setFileNameList(List<String> fileNameList) {
		this.fileNameList = fileNameList;
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
	
}
