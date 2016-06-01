package com.walgreens.admin.json.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"messageHeader",
	"status",
	"errorDetails"
})
public class EmployeeDataResponse {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;

	@JsonProperty("status")
	private boolean status;

	@JsonProperty("errorDetails")
	private ErrorDetails errorDetails;

	/**
	 * @return the messageHeader object
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * method to set messageHeader object.
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * Get and Set method for status property.
	 */
	@JsonProperty("status")
	public boolean isStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the errorDetails object
	 */
	@JsonProperty("errorDetails")
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	/**
	 * method to set errorDetails object.
	 */
	@JsonProperty("errorDetails")
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	
	@Override
	public String toString() {
		return "EmployeeDataResponse [messageHeader=" + messageHeader
				+ ", status =" + status + ""
						+ ", errorDetails = "+ errorDetails +"]";
	}
	
}