package com.walgreens.oms.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class PMReportResponseBeanList {
	
	private MessageHeader messageHeader;
	private List<PMReportResponseBean> pmReportResponseBeanList;
	/*private String currentPageNo;
	private long totalRows;*/
	private String employeeName;
	
	
	

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the currentPageNo
	 */
	/*public String getCurrentPageNo() {
		return currentPageNo;
	}

	*//**
	 * @param currentPageNo the currentPageNo to set
	 *//*
	public void setCurrentPageNo(String currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	*//**
	 * @return the totalRows
	 *//*
	public long getTotalRows() {
		return totalRows;
	}

	*//**
	 * @param totalRows the totalRows to set
	 *//*
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}*/

	/**
	 * @return the pmReportResponseBeanList
	 */
	public List<PMReportResponseBean> getPmReportResponseBeanList() {
		return pmReportResponseBeanList;
	}

	/**
	 * @param pmReportResponseBeanList the pmReportResponseBeanList to set
	 */
	public void setPmReportResponseBeanList(
			List<PMReportResponseBean> pmReportResponseBeanList) {
		this.pmReportResponseBeanList = pmReportResponseBeanList;
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
