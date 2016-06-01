package com.walgreens.admin.bean;

import java.util.List;

import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

public class KioskReportResponseBean {

	private int totalPage;
	private int totalRecord;
	private int currentPage;
	private List<KioskDetailBean> data;
	private MessageHeader messageHeader;
	private String readableString;
	private ErrorDetails errorDetails;

	/**
	 * @return the totalPage
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage
	 *            the totalPage to set
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord
	 *            the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the data
	 */
	public List<KioskDetailBean> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<KioskDetailBean> data) {
		this.data = data;
	}

	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @param messageHeader
	 *            the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * @return the readableString
	 */
	public String getReadableString() {
		return readableString;
	}

	/**
	 * @param readableString
	 *            the readableString to set
	 */
	public void setReadableString(String readableString) {
		this.readableString = readableString;
	}

	/**
	 * @return the errorDetails
	 */
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	/**
	 * @param errorDetails
	 *            the errorDetails to set
	 */
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KioskReportResponseBean [totalPage=" + totalPage
				+ ", totalRecord=" + totalRecord + ", currentPage="
				+ currentPage + ", data=" + data + ", messageHeader="
				+ messageHeader + ", readableString=" + readableString
				+ ", errorDetails=" + errorDetails + "]";
	}

}