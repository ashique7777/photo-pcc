package com.walgreens.admin.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

/**
 * @author CTS
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "storeNbr", "messageHeader" })
public class MachineTypeReqBean {
	@JsonProperty("storeNbr")
	private String storeNbr;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;

	/**
	 * @return the messageHeader
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @param messageHeader
	 *            the messageHeader to set
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * @return the storeNbr
	 */
	@JsonProperty("storeNbr")
	public String getStoreNbr() {
		return storeNbr;
	}

	/**
	 * @param storeNbr the storeNbr to set
	 */
	@JsonProperty("storeNbr")
	public void setStoreNbr(String storeNbr) {
		this.storeNbr = storeNbr;
	}

}
