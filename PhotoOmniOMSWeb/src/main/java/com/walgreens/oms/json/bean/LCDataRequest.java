package com.walgreens.oms.json.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "messageHeader", "licenseContentDownloadDetailList" })
public class LCDataRequest {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("licenseContentDownloadDetailList")
	private List<LicenseContentDownloadReqList> lcDetailList;

	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * @return the lcDetailList
	 */
	public List<LicenseContentDownloadReqList> getLcDetailList() {
		return lcDetailList;
	}

	/**
	 * @param lcDetailList the lcDetailList to set
	 */
	public void setLcDetailList(List<LicenseContentDownloadReqList> lcDetailList) {
		this.lcDetailList = lcDetailList;
	}

	
	@Override
	public String toString() {
		return "LicensedContentOrderRequest [messageHeader=" + messageHeader
				+ ", LicenseContentDownloadDetailList=" + lcDetailList + "]";
	}
}
