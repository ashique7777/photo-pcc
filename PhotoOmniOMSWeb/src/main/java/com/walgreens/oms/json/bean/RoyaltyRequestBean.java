package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.RoyaltyDataBean;


public class RoyaltyRequestBean {

	@JsonProperty("filter")
	private RoyaltyDataBean royaltyDataBena;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	public RoyaltyDataBean getRoyaltyDataBena() {
		return royaltyDataBena;
	}
	public void setRoyaltyDataBena(RoyaltyDataBean royaltyDataBena) {
		this.royaltyDataBena = royaltyDataBena;
	}
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}


	@Override
	public String toString()
	{
		return "RoyaltyRequestBean [messageHeader= " + messageHeader.toString()
				+ ", RoyaltyDataBena= " + royaltyDataBena.toString() + "]";
	}
}
