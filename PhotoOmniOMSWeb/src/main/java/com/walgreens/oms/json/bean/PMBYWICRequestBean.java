package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.PMBYWICDataBean;


public class PMBYWICRequestBean {

	@JsonProperty("filter")
	private PMBYWICDataBean pmbywicDataBean;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	
	
	public PMBYWICDataBean getPmbywicDataBean() {
		return pmbywicDataBean;
	}
	public void setPmbywicDataBean(PMBYWICDataBean pmbywicDataBean) {
		this.pmbywicDataBean = pmbywicDataBean;
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
				+ ", RoyaltyDataBena= " + pmbywicDataBean.toString() + "]";
	}
}
