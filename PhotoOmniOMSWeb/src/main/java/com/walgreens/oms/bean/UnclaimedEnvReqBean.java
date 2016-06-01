package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "storeNumber", "sortColumnName", "sortOrder" })
public class UnclaimedEnvReqBean {
	
	@JsonProperty("filter")
	private UnclaimedEnvFilter filter;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	
	
	/**
	 * @return the filter
	 */
	public UnclaimedEnvFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(UnclaimedEnvFilter filter) {
		this.filter = filter;
	}

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

	
	
	

}
