package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

public class MachineDowntimeDataBean {
	private long downtimeId;
	private long instanceId;
	private String componentId;
	private int workqueueStatus;
	private Timestamp estimatedEndDttm;
	private Timestamp actualEndDttm;
	private Timestamp startDttm;
	private String downtimeReason;
	private long downtimeReasonId;
	private String idCreated;
	private Timestamp dateTimeCreated;
	private String idModified;
	private Timestamp dateTimeModified;
	private String beginEmployeeId;
	private String endEmployeeId;
	private String downtimeEventName;
	private String businessTimeOpenSun;
	private String businessTimeOpenMon;
	private String businessTimeOpenTue;
	private String businessTimeOpenWed;
	private String businessTimeOpenThs;
	private String businessTimeOpenFri;
	private String businessTimeOpenSat;
	private String businessTimeCloseSun;
	private String businessTimeCloseMon;
	private String businessTimeCloseTue;
	private String businessTimeCloseWed;
	private String businessTimeCloseThs;
	private String businessTimeCloseFri;
	private String businessTimeCloseSat;
	private String StoreNumber;

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
	 * @return the businessTimeOpenSun
	 */
	public String getBusinessTimeOpenSun() {
		return businessTimeOpenSun;
	}

	/**
	 * @param businessTimeOpenSun
	 *            the businessTimeOpenSun to set
	 */
	public void setBusinessTimeOpenSun(String businessTimeOpenSun) {
		this.businessTimeOpenSun = businessTimeOpenSun;
	}

	/**
	 * @return the businessTimeOpenMon
	 */
	public String getBusinessTimeOpenMon() {
		return businessTimeOpenMon;
	}

	/**
	 * @param businessTimeOpenMon
	 *            the businessTimeOpenMon to set
	 */
	public void setBusinessTimeOpenMon(String businessTimeOpenMon) {
		this.businessTimeOpenMon = businessTimeOpenMon;
	}

	/**
	 * @return the businessTimeOpenTue
	 */
	public String getBusinessTimeOpenTue() {
		return businessTimeOpenTue;
	}

	/**
	 * @param businessTimeOpenTue
	 *            the businessTimeOpenTue to set
	 */
	public void setBusinessTimeOpenTue(String businessTimeOpenTue) {
		this.businessTimeOpenTue = businessTimeOpenTue;
	}

	/**
	 * @return the businessTimeOpenWed
	 */
	public String getBusinessTimeOpenWed() {
		return businessTimeOpenWed;
	}

	/**
	 * @param businessTimeOpenWed
	 *            the businessTimeOpenWed to set
	 */
	public void setBusinessTimeOpenWed(String businessTimeOpenWed) {
		this.businessTimeOpenWed = businessTimeOpenWed;
	}

	/**
	 * @return the businessTimeOpenThs
	 */
	public String getBusinessTimeOpenThs() {
		return businessTimeOpenThs;
	}

	/**
	 * @param businessTimeOpenThs
	 *            the businessTimeOpenThs to set
	 */
	public void setBusinessTimeOpenThs(String businessTimeOpenThs) {
		this.businessTimeOpenThs = businessTimeOpenThs;
	}

	/**
	 * @return the businessTimeOpenFri
	 */
	public String getBusinessTimeOpenFri() {
		return businessTimeOpenFri;
	}

	/**
	 * @param businessTimeOpenFri
	 *            the businessTimeOpenFri to set
	 */
	public void setBusinessTimeOpenFri(String businessTimeOpenFri) {
		this.businessTimeOpenFri = businessTimeOpenFri;
	}

	/**
	 * @return the businessTimeOpenSat
	 */
	public String getBusinessTimeOpenSat() {
		return businessTimeOpenSat;
	}

	/**
	 * @param businessTimeOpenSat
	 *            the businessTimeOpenSat to set
	 */
	public void setBusinessTimeOpenSat(String businessTimeOpenSat) {
		this.businessTimeOpenSat = businessTimeOpenSat;
	}

	/**
	 * @return the businessTimeCloseSun
	 */
	public String getBusinessTimeCloseSun() {
		return businessTimeCloseSun;
	}

	/**
	 * @param businessTimeCloseSun
	 *            the businessTimeCloseSun to set
	 */
	public void setBusinessTimeCloseSun(String businessTimeCloseSun) {
		this.businessTimeCloseSun = businessTimeCloseSun;
	}

	/**
	 * @return the businessTimeCloseMon
	 */
	public String getBusinessTimeCloseMon() {
		return businessTimeCloseMon;
	}

	/**
	 * @param businessTimeCloseMon
	 *            the businessTimeCloseMon to set
	 */
	public void setBusinessTimeCloseMon(String businessTimeCloseMon) {
		this.businessTimeCloseMon = businessTimeCloseMon;
	}

	/**
	 * @return the businessTimeCloseTue
	 */
	public String getBusinessTimeCloseTue() {
		return businessTimeCloseTue;
	}

	/**
	 * @param businessTimeCloseTue
	 *            the businessTimeCloseTue to set
	 */
	public void setBusinessTimeCloseTue(String businessTimeCloseTue) {
		this.businessTimeCloseTue = businessTimeCloseTue;
	}

	/**
	 * @return the businessTimeCloseWed
	 */
	public String getBusinessTimeCloseWed() {
		return businessTimeCloseWed;
	}

	/**
	 * @param businessTimeCloseWed
	 *            the businessTimeCloseWed to set
	 */
	public void setBusinessTimeCloseWed(String businessTimeCloseWed) {
		this.businessTimeCloseWed = businessTimeCloseWed;
	}

	/**
	 * @return the businessTimeCloseThs
	 */
	public String getBusinessTimeCloseThs() {
		return businessTimeCloseThs;
	}

	/**
	 * @param businessTimeCloseThs
	 *            the businessTimeCloseThs to set
	 */
	public void setBusinessTimeCloseThs(String businessTimeCloseThs) {
		this.businessTimeCloseThs = businessTimeCloseThs;
	}

	/**
	 * @return the businessTimeCloseFri
	 */
	public String getBusinessTimeCloseFri() {
		return businessTimeCloseFri;
	}

	/**
	 * @param businessTimeCloseFri
	 *            the businessTimeCloseFri to set
	 */
	public void setBusinessTimeCloseFri(String businessTimeCloseFri) {
		this.businessTimeCloseFri = businessTimeCloseFri;
	}

	/**
	 * @return the businessTimeCloseSat
	 */
	public String getBusinessTimeCloseSat() {
		return businessTimeCloseSat;
	}

	/**
	 * @param businessTimeCloseSat
	 *            the businessTimeCloseSat to set
	 */
	public void setBusinessTimeCloseSat(String businessTimeCloseSat) {
		this.businessTimeCloseSat = businessTimeCloseSat;
	}

	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return StoreNumber;
	}

	/**
	 * @param storeNumber
	 *            the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		StoreNumber = storeNumber;
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
	 * @return the estimatedEndDttm
	 */
	public Timestamp getEstimatedEndDttm() {
		return estimatedEndDttm;
	}

	/**
	 * @param estimatedEndDttm
	 *            the estimatedEndDttm to set
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
	 * @param actualEndDttm
	 *            the actualEndDttm to set
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
	 * @param startDttm
	 *            the startDttm to set
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
	 * @param downtimeReason
	 *            the downtimeReason to set
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

	/**
	 * @return the idCreated
	 */
	public String getIdCreated() {
		return idCreated;
	}

	/**
	 * @param idCreated
	 *            the idCreated to set
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
	 * @param dateTimeCreated
	 *            the dateTimeCreated to set
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
	 * @param idModified
	 *            the idModified to set
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
	 * @param dateTimeModified
	 *            the dateTimeModified to set
	 */
	public void setDateTimeModified(Timestamp dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}

}
