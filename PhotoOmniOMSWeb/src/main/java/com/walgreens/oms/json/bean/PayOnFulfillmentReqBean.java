/**
 * 
 */
package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.PayOnFulfillmentReq;

/**
 * @author CTS
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = false)
@JsonPropertyOrder({ "filter", "messageHeader" })
public class PayOnFulfillmentReqBean {
	
	
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	
	
	@JsonProperty("filter")
	private PayOnFulfillmentReq filter;
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
	public PayOnFulfillmentReq getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(PayOnFulfillmentReq filter) {
		this.filter = filter;
	}
	
	

}
