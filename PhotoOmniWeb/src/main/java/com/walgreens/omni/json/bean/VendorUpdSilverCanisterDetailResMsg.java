package com.walgreens.omni.json.bean;
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
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"messageHeader",
"VendorUpdSilverCanisterDetailsList",
"errorDetails"
})

public class VendorUpdSilverCanisterDetailResMsg {

@JsonProperty("messageHeader")
private MessageHeader messageHeader;
@JsonProperty("filter")
private List<VendorUpdSilverCanisterDetailsList> filter = new ArrayList<VendorUpdSilverCanisterDetailsList>();
@JsonProperty("errorDetails")
private ErrorDetails errorDetails;
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
* The filter
*/
@JsonProperty("filter")
public List<VendorUpdSilverCanisterDetailsList> getFilter() {
return filter;
 }

/**
* 
* @param filter
* The filter
*/
@JsonProperty("filter")
public void setFilter(List<VendorUpdSilverCanisterDetailsList> filter) {
this.filter = filter;
 }

/**
* 
* @return
* The errorDetails
*/
@JsonProperty("errorDetails")
public ErrorDetails getErrorDetails() {
return errorDetails;
 }

/**
* 
* @param errorDetails
* The errorDetails
*/
@JsonProperty("errorDetails")
public void setErrorDetails(ErrorDetails errorDetails) {
this.errorDetails = errorDetails;
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

