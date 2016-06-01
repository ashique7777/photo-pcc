package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

public class MachineDwntmSplitDataBean {
	private long downtimeId;
	private long instanceId;
	private long equipmentInstanceId;
	private String componentId;
	private int workqueueStatus;
	private String downtimeEventName;
	private Timestamp estimatedEndDttm;
	private Timestamp actualEndDttm;
	private Timestamp startDttm;
	private Timestamp dailyDwntmStart;
	private Timestamp dailyDwntmEnd;
	private String downtimeReason;
	private long duration;
	private long downtimeReasonId;
	private String idCreated;
	private Timestamp dateTimeCreated;
	private String idModified;
	private Timestamp dateTimeModified;
	private String beginEmployeeId;
	private String endEmployeeId;
	private String downtimeEventId;
	
	
	/**
	 * @return the equipmentInstanceId
	 */
	public long getEquipmentInstanceId() {
		return equipmentInstanceId;
	}
	/**
	 * @param equipmentInstanceId the equipmentInstanceId to set
	 */
	public void setEquipmentInstanceId(long equipmentInstanceId) {
		this.equipmentInstanceId = equipmentInstanceId;
	}
	/**
	 * @return the componentId
	 */
	public String getComponentId() {
		return componentId;
	}
	/**
	 * @param componentId the componentId to set
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}
	/**
	 * @return the endEmployeeId
	 */
	public String getEndEmployeeId() {
		return endEmployeeId;
	}
	/**
	 * @param endEmployeeId the endEmployeeId to set
	 */
	public void setEndEmployeeId(String endEmployeeId) {
		this.endEmployeeId = endEmployeeId;
	}
	/**
	 * @return the dailyDwntmStart
	 */
	public Timestamp getDailyDwntmStart() {
		return dailyDwntmStart;
	}
	/**
	 * @param dailyDwntmStart the dailyDwntmStart to set
	 */
	public void setDailyDwntmStart(Timestamp dailyDwntmStart) {
		this.dailyDwntmStart = dailyDwntmStart;
	}
	/**
	 * @return the dailyDwntmEnd
	 */
	public Timestamp getDailyDwntmEnd() {
		return dailyDwntmEnd;
	}
	/**
	 * @param dailyDwntmEnd the dailyDwntmEnd to set
	 */
	public void setDailyDwntmEnd(Timestamp dailyDwntmEnd) {
		this.dailyDwntmEnd = dailyDwntmEnd;
	}
	/**
	 * @return the downtimeEventName
	 */
	public String getDowntimeEventName() {
		return downtimeEventName;
	}
	/**
	 * @param downtimeEventName the downtimeEventName to set
	 */
	public void setDowntimeEventName(String downtimeEventName) {
		this.downtimeEventName = downtimeEventName;
	}
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
	/**
	 * @return the downtimeEventId
	 */
	public String getDowntimeEventId() {
		return downtimeEventId;
	}
	/**
	 * @param downtimeEventId the downtimeEventId to set
	 */
	public void setDowntimeEventId(String downtimeEventId) {
		this.downtimeEventId = downtimeEventId;
	}
	
	/**
	 * @return the workqueueStatus
	 */
	public int getWorkqueueStatus() {
		return workqueueStatus;
	}
	/**
	 * @param workqueueStatus the workqueueStatus to set
	 */
	public void setWorkqueueStatus(int workqueueStatus) {
		this.workqueueStatus = workqueueStatus;
	}
	/**
	 * @return the estimatedEndDttm
	 */
	public Timestamp getEstimatedEndDttm() {
		return estimatedEndDttm;
	}
	/**
	 * @param estimatedEndDttm the estimatedEndDttm to set
	 */
	public void setEstimatedEndDttm(Timestamp estimatedEndDttm) {
		this.estimatedEndDttm = estimatedEndDttm;
	}
	/**
	 * @return the actualEndDttm
	 */
	public Timestamp getActualEndDttm() {
		return actualEndDttm;
	}
	/**
	 * @param actualEndDttm the actualEndDttm to set
	 */
	public void setActualEndDttm(Timestamp actualEndDttm) {
		this.actualEndDttm = actualEndDttm;
	}
	/**
	 * @return the startDttm
	 */
	public Timestamp getStartDttm() {
		return startDttm;
	}
	/**
	 * @param startDttm the startDttm to set
	 */
	public void setStartDttm(Timestamp startDttm) {
		this.startDttm = startDttm;
	}
	/**
	 * @return the downtimeReason
	 */
	public String getDowntimeReason() {
		return downtimeReason;
	}
	/**
	 * @param downtimeReason the downtimeReason to set
	 */
	public void setDowntimeReason(String downtimeReason) {
		this.downtimeReason = downtimeReason;
	}
	
	
	/**
	 * @return the downtimeId
	 */
	public long getDowntimeId() {
		return downtimeId;
	}
	/**
	 * @param downtimeId the downtimeId to set
	 */
	public void setDowntimeId(long downtimeId) {
		this.downtimeId = downtimeId;
	}
	/**
	 * @return the instanceId
	 */
	public long getInstanceId() {
		return instanceId;
	}
	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}
	/**
	 * @return the downtimeReasonId
	 */
	public long getDowntimeReasonId() {
		return downtimeReasonId;
	}
	/**
	 * @param downtimeReasonId the downtimeReasonId to set
	 */
	public void setDowntimeReasonId(long downtimeReasonId) {
		this.downtimeReasonId = downtimeReasonId;
	}
	/**
	 * @return the idCreated
	 */
	public String getIdCreated() {
		return idCreated;
	}
	/**
	 * @param idCreated the idCreated to set
	 */
	public void setIdCreated(String idCreated) {
		this.idCreated = idCreated;
	}
	/**
	 * @return the dateTimeCreated
	 */
	public Timestamp getDateTimeCreated() {
		return dateTimeCreated;
	}
	/**
	 * @param dateTimeCreated the dateTimeCreated to set
	 */
	public void setDateTimeCreated(Timestamp dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}
	/**
	 * @return the idModified
	 */
	public String getIdModified() {
		return idModified;
	}
	/**
	 * @param idModified the idModified to set
	 */
	public void setIdModified(String idModified) {
		this.idModified = idModified;
	}
	/**
	 * @return the dateTimeModified
	 */
	public Timestamp getDateTimeModified() {
		return dateTimeModified;
	}
	/**
	 * @param dateTimeModified the dateTimeModified to set
	 */
	public void setDateTimeModified(Timestamp dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}
	/**
	 * @return the beginEmployeeId
	 */
	public String getBeginEmployeeId() {
		return beginEmployeeId;
	}
	/**
	 * @param beginEmployeeId the beginEmployeeId to set
	 */
	public void setBeginEmployeeId(String beginEmployeeId) {
		this.beginEmployeeId = beginEmployeeId;
	}
	
	

}
