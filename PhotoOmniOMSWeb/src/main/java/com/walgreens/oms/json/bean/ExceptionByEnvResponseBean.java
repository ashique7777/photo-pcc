package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class ExceptionByEnvResponseBean {

	private List<ExceptionByEnvelopeBean> repByEnvList = new ArrayList<ExceptionByEnvelopeBean>();
	private MessageHeader messageHeader;
	private String currentPageNo;
	private long totalRows;
	private String storeWithAddress;
	
	/**
	 * @return the storeWithAddress
	 */
	public String getStoreWithAddress() {
		return storeWithAddress;
	}
	/**
	 * @param storeWithAddress the storeWithAddress to set
	 */
	public void setStoreWithAddress(String storeWithAddress) {
		this.storeWithAddress = storeWithAddress;
	}
	/**
	 * @return the totalRows
	 */
	public long getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}
	/**
	 * @return the repByEnvList
	 */
	public List<ExceptionByEnvelopeBean> getRepByEnvList() {
		return repByEnvList;
	}

	/**
	 * @param repByEnvList the repByEnvList to set
	 */
	public void setRepByEnvList(List<ExceptionByEnvelopeBean> repByEnvList) {
		this.repByEnvList = repByEnvList;
	}

	public String getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
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
