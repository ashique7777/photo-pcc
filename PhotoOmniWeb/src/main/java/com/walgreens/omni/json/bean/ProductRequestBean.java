package com.walgreens.omni.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.omni.bean.EnvelopeNumberFilter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "filter", "messageHeader" })
public class ProductRequestBean {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("filter")
	private EnvelopeNumberFilter filter;
	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	/**
	 * @return the filter
	 */
	public EnvelopeNumberFilter getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(EnvelopeNumberFilter filter) {
		this.filter = filter;
	}
	
	
}
