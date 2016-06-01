package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.json.bean.PrintableSignFilter;

/**
 * @author CTS
 * @version 1.1 Mar 17, 2015
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"filter",
"messageHeader"
})
public class PrintSignReqBean {
	@JsonProperty("filter")
	private PrintableSignFilter filter;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;

	/**
	 * @return the startDate
	 */

	/**
	 * @return the messageHeader
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @return the filter
	 */
	@JsonProperty("filter")
	public PrintableSignFilter getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	@JsonProperty("filter")
	public void setFilter(PrintableSignFilter filter) {
		this.filter = filter;
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
	 * @return the eventId
	 */

	/**
	 * @return the emailIds
	 */

}
