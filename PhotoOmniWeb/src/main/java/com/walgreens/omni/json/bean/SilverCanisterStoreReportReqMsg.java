package com.walgreens.omni.json.bean;

import java.util.HashMap;
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
"SilverCanisterStoreReportReq",
"errorDetails"
})

public class SilverCanisterStoreReportReqMsg {
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("filter")
	private SilverCanisterStoreReportReq filter;
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
	* The SilverCanisterStoreReportReq
	*/
	@JsonProperty("filter")
	public SilverCanisterStoreReportReq getFilter() {
	return filter;
	 }

	/**
	* 
	* @param SilverCanisterStoreReportReq
	* The SilverCanisterStoreReportReq
	*/
	@JsonProperty("filter")
	public void setFilter(SilverCanisterStoreReportReq filter) {
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
