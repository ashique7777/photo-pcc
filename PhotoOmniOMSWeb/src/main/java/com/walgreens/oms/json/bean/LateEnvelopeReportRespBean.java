package com.walgreens.oms.json.bean;

import java.util.List;
import java.util.Map;

import com.walgreens.common.utility.MessageHeader;

public class LateEnvelopeReportRespBean {

	
	private int totalRows;
	private List<Map<String, Object>> data;
	private MessageHeader messageHeader;
	private int currentPage;
	private String storeAddress;
	
	
	
	/**
	 * @return the storeAddress
	 */
	public String getStoreAddress() {
		return storeAddress;
	}
	/**
	 * @param storeAddress the storeAddress to set
	 */
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
		/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows2 the totalRows to set
	 */
	public void setTotalRows(int totalRows2) {
		this.totalRows = totalRows2;
	}
	/**
	 * @return the data
	 */
	
	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return the data
	 */
	public List<Map<String, Object>> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}
	
	

	



}
