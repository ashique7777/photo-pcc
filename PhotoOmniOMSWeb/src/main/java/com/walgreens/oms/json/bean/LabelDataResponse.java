package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;

import com.walgreens.common.utility.MessageHeader;



public class LabelDataResponse {

	private MessageHeader messageHeader;
	private List<LabelPrintDetails> labelPrintDetails = new ArrayList<LabelPrintDetails>();
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
	public List<LabelPrintDetails> getLabelPrintDetails() {
		return labelPrintDetails;
	}
	public void setLabelPrintDetails(List<LabelPrintDetails> labelPrintDetails) {
		this.labelPrintDetails = labelPrintDetails;
	}
	@Override
	public String toString() {
		return "LabelDataResponse [messageHeader=" + messageHeader
				+ ", labelPrintDetails=" + labelPrintDetails + "]";
	}
	
	

}
