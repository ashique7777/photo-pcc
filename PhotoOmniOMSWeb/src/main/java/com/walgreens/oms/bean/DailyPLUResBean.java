package com.walgreens.oms.bean;

import java.util.List;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

public class DailyPLUResBean {

	private List<String> pluNumber;
	private int totalRecord;
	private int currentPage;
	private String response;
	private MessageHeader messageHeader;
	private ErrorDetails errorDetails;
	private int totalPages;
	private boolean pluStatus;
	private long reportId;
	
	public long getReportId() {
		return reportId;
	}
	public void setReportId(long reportId) {
		this.reportId = reportId;
	}
	
	public List<String> getPluNumber() {
		return pluNumber;
	}
	public void setPluNumber(List<String> pluNumber) {
		this.pluNumber = pluNumber;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public boolean isPluStatus() {
		return pluStatus;
	}
	public void setPluStatus(boolean pluStatus) {
		this.pluStatus = pluStatus;
	}
	@Override
	public String toString() {
		return "DailyPLUResBean [pluNumber=" + pluNumber + ", totalRecord="
				+ totalRecord + ", currentPage=" + currentPage + ", response="
				+ response + ", messageHeader=" + messageHeader
				+ ", errorDetails=" + errorDetails + ", totalPages="
				+ totalPages + ", pluStatus=" + pluStatus + ", reportId="
				+ reportId + "]";
	}
}
