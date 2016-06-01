/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

/**
 * @author dasra
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messageHeader",
    "orderList"
})
public class OrderDataRequest {
	
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("orderList")
	private List<OrderList> orderList;
	
	
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
	/**
	 * @return the orderList
	 */
	 @JsonProperty("orderList")
	public List<OrderList> getOrderList() {
		return orderList;
	}
	/**
	 * @param orderList the orderList to set
	 */
	 @JsonProperty("orderList")
	public void setOrderList(List<OrderList> orderList) {
		this.orderList = orderList;
	}
	
	
	

}
