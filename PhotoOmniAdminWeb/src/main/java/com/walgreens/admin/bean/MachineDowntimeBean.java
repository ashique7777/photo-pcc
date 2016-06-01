package com.walgreens.admin.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "locationNbr", "machineTypeId", "equipmentTypeId",
		"workqueueStatus", "completedDttm", "startDttm", "downtimeReason",
		"downtimeReasonId", "employeeId", "componentId" })
public class MachineDowntimeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1336540261501132120L;

	@JsonProperty("locationNbr")
	private String locationNbr;

	@JsonProperty("machineTypeId")
	private String machineId;

	@JsonProperty("equipmentTypeId")
	private String equipmentId;

	@JsonProperty("workqueueStatus")
	private String workqueueStatus;

	@JsonProperty("completedDttm")
	private String completedDttm;

	@JsonProperty("startDttm")
	private String startDttm;

	@JsonProperty("downtimeReason")
	private String downtimeReason;

	@JsonProperty("downtimeReasonId")
	private String downtimeReasonId;

	@JsonProperty("idCreated")
	private String idCreated;

	@JsonProperty("dateTimeCreated")
	private String dateTimeCreated;

	@JsonProperty("idModified")
	private String idModified;

	@JsonProperty("dateTimeModified")
	private String dateTimeModified;

	@JsonProperty("employeeId")
	private String employeeId;

	@JsonProperty("componentId")
	private String mediaId;
	
	private String machineInstanceId;
	private String equipmentInstanceId;
	private String componentDowntimeId;
	
	

	/**
	 * @return the componentDowntimeId
	 */
	public String getComponentDowntimeId() {
		return componentDowntimeId;
	}

	/**
	 * @param componentDowntimeId the componentDowntimeId to set
	 */
	public void setComponentDowntimeId(String componentDowntimeId) {
		this.componentDowntimeId = componentDowntimeId;
	}

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	/**
	 * @return the equipmentInstanceId
	 */
	public String getEquipmentInstanceId() {
		return equipmentInstanceId;
	}

	/**
	 * @param equipmentInstanceId the equipmentInstanceId to set
	 */
	public void setEquipmentInstanceId(String equipmentInstanceId) {
		this.equipmentInstanceId = equipmentInstanceId;
	}

	/**
	 * @return the machineInstanceId
	 */
	public String getMachineInstanceId() {
		return machineInstanceId;
	}

	/**
	 * @param machineInstanceId the machineInstanceId to set
	 */
	public void setMachineInstanceId(String machineInstanceId) {
		this.machineInstanceId = machineInstanceId;
	}

	/**
	 * @return the locationNbr
	 */
	@JsonProperty("locationNbr")
	public String getLocationNbr() {
		return locationNbr;
	}

	/**
	 * @param locationNbr
	 *            the locationNbr to set
	 */
	@JsonProperty("locationNbr")
	public void setLocationNbr(String locationNbr) {
		this.locationNbr = locationNbr;
	}

	/**
	 * 
	 * @return The machineId
	 */
	@JsonProperty("machineTypeId")
	public String getMachineId() {
		return machineId;
	}

	/**
	 * 
	 * @param machineId
	 *            The MachineId
	 */
	@JsonProperty("machineTypeId")
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	/**
	 * 
	 * @return The equipmentId
	 */
	@JsonProperty("equipmentTypeId")
	public String getEquipmentId() {
		return equipmentId;
	}

	/**
	 * 
	 * @param EquipmentId
	 *            The EquipmentId
	 */
	@JsonProperty("equipmentTypeId")
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	/**
	 * 
	 * @return The WorkqueueStatus
	 */
	@JsonProperty("workqueueStatus")
	public String getWorkqueueStatus() {
		return workqueueStatus;
	}

	/**
	 * 
	 * @param WorkqueueStatus
	 *            The WorkqueueStatus
	 */
	@JsonProperty("workqueueStatus")
	public void setWorkqueueStatus(String workqueueStatus) {
		this.workqueueStatus = workqueueStatus;
	}

	/**
	 * 
	 * @return The CompletedDttm
	 */
	@JsonProperty("completedDttm")
	public String getCompletedDttm() {
		return completedDttm;
	}

	/**
	 * 
	 * @param CompletedDttm
	 *            The CompletedDttm
	 */
	@JsonProperty("completedDttm")
	public void setCompletedDttm(String completedDttm) {
		this.completedDttm = completedDttm;
	}

	/**
	 * 
	 * @return The StartDttm
	 */
	@JsonProperty("startDttm")
	public String getStartDttm() {
		return startDttm;
	}

	/**
	 * 
	 * @param StartDttm
	 *            The StartDttm
	 */
	@JsonProperty("startDttm")
	public void setStartDttm(String startDttm) {
		this.startDttm = startDttm;
	}

	/**
	 * 
	 * @return The DowntimeReason
	 */
	@JsonProperty("downtimeReason")
	public String getDowntimeReason() {
		return downtimeReason;
	}

	/**
	 * 
	 * @param DowntimeReason
	 *            The DowntimeReason
	 */
	@JsonProperty("downtimeReason")
	public void setDowntimeReason(String downtimeReason) {
		this.downtimeReason = downtimeReason;
	}

	/**
	 * 
	 * @return The DowntimeReasonId
	 */
	@JsonProperty("downtimeReasonId")
	public String getDowntimeReasonId() {
		return downtimeReasonId;
	}

	/**
	 * 
	 * @param DowntimeReasonId
	 *            The DowntimeReasonId
	 */
	@JsonProperty("downtimeReasonId")
	public void setDowntimeReasonId(String downtimeReasonId) {
		this.downtimeReasonId = downtimeReasonId;
	}

	/**
	 * 
	 * @return The IdCreated
	 */
	public String getIdCreated() {
		return idCreated;
	}

	/**
	 * 
	 * @param IdCreated
	 *            The IdCreated
	 */
	public void setIdCreated(String idCreated) {
		this.idCreated = idCreated;
	}

	/**
	 * 
	 * @return The DateTimeCreated
	 */
	public String getDateTimeCreated() {
		return dateTimeCreated;
	}

	/**
	 * 
	 * @param DateTimeCreated
	 *            The DateTimeCreated
	 */
	public void setDateTimeCreated(String dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	/**
	 * 
	 * @return The IdModified
	 */
	public String getIdModified() {
		return idModified;
	}

	/**
	 * 
	 * @param IdModified
	 *            The IdModified
	 */
	public void setIdModified(String idModified) {
		this.idModified = idModified;
	}

	/**
	 * 
	 * @return The DateTimeModified
	 */
	public String getDateTimeModified() {
		return dateTimeModified;
	}

	/**
	 * 
	 * @param DateTimeModified
	 *            The DateTimeModified
	 */
	public void setDateTimeModified(String dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}

	/**
	 * @return the employeeId
	 */
	@JsonProperty("employeeId")
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId
	 *            the employeeId to set
	 */
	@JsonProperty("employeeId")
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the mediaId
	 */
	@JsonProperty("componentId")
	public String getMediaId() {
		return mediaId;
	}

	/**
	 * @param mediaId
	 *            the mediaId to set
	 */
	@JsonProperty("componentId")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return "MachineDowntimeDetail [MachineId=" + machineId
				+ ", EquipmentId=" + equipmentId + ", WorkqueueStatus="
				+ workqueueStatus + ", CompletedDttm=" + completedDttm
				+ ", StartDttm=" + startDttm + ", DowntimeReason="
				+ downtimeReason + ", DowntimeReasonId=" + downtimeReasonId
				+ ", IdCreated=" + idCreated + ", DateTimeCreated="
				+ dateTimeCreated + ", IdModified=" + idModified
				+ ", DateTimeModified=" + dateTimeModified + "]";
	}
}
