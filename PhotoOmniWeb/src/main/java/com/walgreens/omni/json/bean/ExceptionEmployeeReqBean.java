package com.walgreens.omni.json.bean;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.omni.bean.ExceptionEmployeeFilter;

public class ExceptionEmployeeReqBean {
	
	
	private MessageHeader messageHeader;
	private ExceptionEmployeeFilter filter;
	
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
	public ExceptionEmployeeFilter getFilter() {
		return filter;
	}
	/**
	 * @param filter the filter to set
	 */
	public void setFilter(ExceptionEmployeeFilter filter) {
		this.filter = filter;
	}
	
	

}
