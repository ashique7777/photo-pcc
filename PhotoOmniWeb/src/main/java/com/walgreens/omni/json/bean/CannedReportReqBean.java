/**
 * 
 */
package com.walgreens.omni.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

/**
 * @author Cognizant
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "filter", "messageHeader" })
public class CannedReportReqBean {
	
	@JsonProperty("filter")
	private CannedFilter filter;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	/**
	 * @return the filter
	 */
	@JsonProperty("filter")
	public CannedFilter getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	@JsonProperty("filter")
	public void setFilter(CannedFilter filter) {
		this.filter = filter;
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
