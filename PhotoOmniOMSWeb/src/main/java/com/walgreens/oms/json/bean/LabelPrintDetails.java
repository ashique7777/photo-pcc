package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.ErrorDetails;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"orderId",
"employeeId",
"labelPrintDTTM",
"locationNumber"})

public class LabelPrintDetails {
	
	@JsonProperty("orderId")private String  orderId;
	@JsonProperty("employeeId")private String employeeId;
	@JsonProperty("labelPrintDTTM")private String labelPrintDTTM;
	@JsonProperty("locationNumber")private long locationNumber;
	private long sysOrderId;
	private String orderPlacedDTTM;
	private long sysShoppingCartId;

	/**
	 * @return the sysShoppingCartId
	 */
	public long getSysShoppingCartId() {
		return sysShoppingCartId;
	}
	/**
	 * @param sysShoppingCartId the sysShoppingCartId to set
	 */
	public void setSysShoppingCartId(long sysShoppingCartId) {
		this.sysShoppingCartId = sysShoppingCartId;
	}
	/**
	 * @return the sysOrderId
	 */
	public long getSysOrderId() {
		return sysOrderId;
	}
	/**
	 * @param sysOrderId the sysOrderId to set
	 */
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	/**
	 * @return the orderPlacedDTTM
	 */
	public String getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}
	/**
	 * @param orderPlacedDTTM the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(String orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
	}
	private Boolean status;
	private ErrorDetails errorDetails;

	@JsonProperty("orderId")
	public String getOrderId() {
		return orderId;
	}
	@JsonProperty("orderId")
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	@JsonProperty("employeeId")
	public String getEmployeeId() {
		return employeeId.toUpperCase();
	}
	@JsonProperty("employeeId")
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	@JsonProperty("labelPrintDTTM")
	public String getLabelPrintDTTM() {
		return labelPrintDTTM;
	}
	@JsonProperty("labelPrintDTTM")
	public void setLabelPrintDTTM(String labelPrintDTTM) {
		this.labelPrintDTTM = labelPrintDTTM;
	}
	@JsonProperty("locationNumber")
	public long getLocationNumber() {
		return locationNumber;
	}
	@JsonProperty("locationNumber")
	public void setLocationNumber(long locationNumber) {
		this.locationNumber = locationNumber;
	}
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	@Override
	public String toString() {
		return "LabelPrintDetails [orderId=" + orderId + ", employeeId="
				+ employeeId + ", labelPrintDTTM=" + labelPrintDTTM
				+ ",  locationNumber="
				+ locationNumber + ", status=" + status + ", errorDetails="
				+ errorDetails + "]";
	}
	

}
