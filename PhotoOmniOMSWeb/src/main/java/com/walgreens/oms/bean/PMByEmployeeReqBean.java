package com.walgreens.oms.bean;

import com.walgreens.common.utility.MessageHeader;

public class PMByEmployeeReqBean {
	
	private MessageHeader messageHeader;
	private PMByEmployeeBean filter;
	private int currentPage;
	
	public PMByEmployeeBean getFilter() {
		return filter;
	}

	public void setFilter(PMByEmployeeBean filter) {
		this.filter = filter;
	}
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@Override
	public String toString() {
		return "PMByEmployeeReqBean [messageHeader=" + messageHeader
				+ ", filter=" + filter + ", currentPage=" + currentPage + "]";
	}
}
