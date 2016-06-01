package com.walgreens.omni.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class ExceptionReasonBean {

	private List<ExceptionReason> reasonList;
	private MessageHeader messageHeader;
	/**
	 * @return the reasonList
	 */
	public List<ExceptionReason> getReasonList() {
		return reasonList;
	}
	/**
	 * @param reasonList the reasonList to set
	 */
	public void setReasonList(List<ExceptionReason> reasonList) {
		this.reasonList = reasonList;
	}
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
	
}
