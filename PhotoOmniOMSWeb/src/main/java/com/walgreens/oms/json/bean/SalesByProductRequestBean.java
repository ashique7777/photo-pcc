package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.SalesBYProductDataBean;


public class SalesByProductRequestBean {

	@JsonProperty("filter")
	private SalesBYProductDataBean salesBYProductDataBean;
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	
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
				+ ", RoyaltyDataBena= " + salesBYProductDataBean.toString() + "]";
	}
	public SalesBYProductDataBean getSalesBYProductDataBean() {
		return salesBYProductDataBean;
	}
	public void setSalesBYProductDataBean(
			SalesBYProductDataBean salesBYProductDataBean) {
		this.salesBYProductDataBean = salesBYProductDataBean;
	}
}
