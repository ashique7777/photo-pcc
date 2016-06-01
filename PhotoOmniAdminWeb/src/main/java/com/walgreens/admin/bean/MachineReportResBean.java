/**
 * 
 */
package com.walgreens.admin.bean;

import java.sql.Timestamp;


/**
 * @author CTS
 *
 */
public class MachineReportResBean {
	
	private String machineName;
	private String equipmentName;
	private String componentName;
	private String enteredBy;
	private Timestamp startTime;
	private Timestamp endTime;
	private Long duration;
	private String reason;
	private Long regionNumber;
	private Long districtNumber;
	private Long storeNumber;
	private Long totalRecord;
	private String currrentPage;
	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}
	/**
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	/**
	 * @return the equipmentName
	 */
	public String getEquipmentName() {
		return equipmentName;
	}
	/**
	 * @param equipmentName the equipmentName to set
	 */
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	/**
	 * @return the enteredBy
	 */
	public String getEnteredBy() {
		return enteredBy;
	}
	/**
	 * @param enteredBy the enteredBy to set
	 */
	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	/**
	 * @return the startTime
	 */
	public Timestamp getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the duration
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the regionNumber
	 */
	public Long getRegionNumber() {
		return regionNumber;
	}
	/**
	 * @param regionNumber the regionNumber to set
	 */
	public void setRegionNumber(Long regionNumber) {
		this.regionNumber = regionNumber;
	}
	/**
	 * @return the districtNumber
	 */
	public Long getDistrictNumber() {
		return districtNumber;
	}
	/**
	 * @param districtNumber the districtNumber to set
	 */
	public void setDistrictNumber(Long districtNumber) {
		this.districtNumber = districtNumber;
	}
	/**
	 * @return the storeNumber
	 */
	public Long getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the totalRecord
	 */
	public Long getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the currrentPage
	 */
	public String getCurrrentPage() {
		return currrentPage;
	}
	/**
	 * @param currrentPage the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}
	/**
	 * @return the componentName
	 */
	public String getComponentName() {
		return componentName;
	}
	/**
	 * @param componentName the componentName to set
	 */
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	

}
