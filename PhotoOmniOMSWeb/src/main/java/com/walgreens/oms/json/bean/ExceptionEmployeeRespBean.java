package com.walgreens.oms.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class ExceptionEmployeeRespBean {
	
	private MessageHeader messageHeader;
	private List<ExceptionEmployeeResponseList> empResponseList;
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
	 * @return the currentPageNo
	 */
	public String getCurrentPageNo() {
		return currentPageNo;
	}
	/**
	 * @param currentPageNo the currentPageNo to set
	 */
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
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
	 * @return the empResponseList
	 */
	public List<ExceptionEmployeeResponseList> getEmpResponseList() {
		return empResponseList;
	}
	/**
	 * @param empResponseList the empResponseList to set
	 */
	public void setEmpResponseList(
			List<ExceptionEmployeeResponseList> empResponseList) {
		this.empResponseList = empResponseList;
	}
	
	

}
