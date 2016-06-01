package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"messageHeader",
"orderASNResponseDetails"
})
public class AsnOrderResponse {

@JsonProperty("messageHeader")
private MessageHeader messageHeader;
@JsonProperty("orderASNResponseDetails")
private List<OrderASNResponseDetail> orderASNResponseDetails = new ArrayList<OrderASNResponseDetail>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The messageHeader
*/
@JsonProperty("messageHeader")
public MessageHeader getMessageHeader() {
return messageHeader;
 }

/**
* 
* @param messageHeader
* The messageHeader
*/
@JsonProperty("messageHeader")
public void setMessageHeader(MessageHeader messageHeader) {
this.messageHeader = messageHeader;
 }

/**
* 
* @return
* The orderASNResponseDetails
*/
@JsonProperty("orderASNResponseDetails")
public List<OrderASNResponseDetail> getOrderASNResponseDetails() {
return orderASNResponseDetails;
 }

/**
* 
* @param orderASNResponseDetails
* The orderASNResponseDetails
*/
@JsonProperty("orderASNResponseDetails")
public void setOrderASNResponseDetails(List<OrderASNResponseDetail> orderASNResponseDetails) {
this.orderASNResponseDetails = orderASNResponseDetails;
 }

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
 }

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
 }

}
