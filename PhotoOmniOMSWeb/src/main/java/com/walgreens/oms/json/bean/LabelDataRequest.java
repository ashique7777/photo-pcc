package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"messageHeader",
"labelPrintDetails"})
public class LabelDataRequest {
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("labelPrintDetails")
	private List<LabelPrintDetails> labelPrintDetailsList = new ArrayList<LabelPrintDetails>();
	@JsonProperty("labelPrintDetails")
	public List<LabelPrintDetails> getLabelPrintDetailsList() {
		return labelPrintDetailsList;
	}
	@JsonProperty("labelPrintDetails")
	public void setLabelPrintDetailsList(
			List<LabelPrintDetails> labelPrintDetailsList) {
		this.labelPrintDetailsList = labelPrintDetailsList;
	}
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}


}