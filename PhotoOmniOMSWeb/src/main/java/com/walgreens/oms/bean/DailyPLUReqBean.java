package com.walgreens.oms.bean;

import com.walgreens.common.utility.MessageHeader;

public class DailyPLUReqBean {

	private String pluKey;
	private int currentPageReq;
	private PLUJsonFilters pluJsonFilters;
	private MessageHeader messageHeader;
	
	public String getPluKey() {
		return pluKey;
	}
	public void setPluKey(String pluKey) {
		this.pluKey = pluKey;
	}
	public int getCurrentPageReq() {
		return currentPageReq;
	}
	public void setCurrentPageReq(int currentPageReq) {
		this.currentPageReq = currentPageReq;
	}
	
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	public PLUJsonFilters getPluJsonFilters() {
		return pluJsonFilters;
	}
	public void setPluJsonFilters(PLUJsonFilters pluJsonFilters) {
		this.pluJsonFilters = pluJsonFilters;
	}
	@Override
	public String toString() {
		return "DailyPLUReqBean [pluKey=" + pluKey + ", currentPageReq="
				+ currentPageReq + ", pluJsonFilters=" + pluJsonFilters
				+ ", messageHeader=" + messageHeader + "]";
	}
}
