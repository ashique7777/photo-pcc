/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

/**
 * @author CTS
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messageHeader",
    "orderDetailList"
})
public class OrderDataResponse {
	
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("orderDetailList")
    private List<OrderDetailsList> orderDtlList = new ArrayList<OrderDetailsList>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	/**
	 * @return the additionalProperties
	 */
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	@JsonAnyGetter
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
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
	/**
	 * @return the orderDtlList
	 */
	 @JsonProperty("orderDetailList")
	public List<OrderDetailsList> getOrderDtlList() {
		return orderDtlList;
	}
	/**
	 * @param orderDtlList the orderDtlList to set
	 */
	 @JsonProperty("orderDetailList")
	public void setOrderDtlList(List<OrderDetailsList> orderDtlList) {
		this.orderDtlList = orderDtlList;
	}
	@Override
	public String toString() {
		return "OrderDataResponse [messageHeader=" + messageHeader
				+ ", orderDtlList=" + orderDtlList + ", additionalProperties="
				+ additionalProperties + "]";
	}
	
	
	

}
