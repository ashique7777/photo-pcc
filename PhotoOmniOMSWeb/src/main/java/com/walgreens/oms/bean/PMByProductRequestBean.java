package com.walgreens.oms.bean;

import com.walgreens.common.utility.MessageHeader;

public class PMByProductRequestBean {

	private MessageHeader messageHeader;
	private PMByProductFilter filter;
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
	/**
	 * @return the filter
	 */
	public PMByProductFilter getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(PMByProductFilter filter) {
		this.filter = filter;
	}
	
	
}
