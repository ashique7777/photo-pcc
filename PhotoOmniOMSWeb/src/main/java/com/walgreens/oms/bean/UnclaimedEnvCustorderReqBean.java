package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "customerId", "messageHeader"})
public class UnclaimedEnvCustorderReqBean {
	@JsonProperty("customerId")
	private String customerId;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;

	/**
	 * @return the customerId
	 */
	@JsonProperty("customerId")
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	@JsonProperty("customerId")
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the messageHeader
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @param messageHeader the messageHeader to set
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

}
