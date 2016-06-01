package com.walgreens.oms.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;



public class PMByEmployeeResBean {

	private List<PMByEmployeeDetailBean> pmByEmployeeDetailBeans;
	private MessageHeader message;
	private String errorMessage;
	private int totalRecords;
	private int totalRows;
	private int currentPage;
	private long totalPage;
	private boolean isAuthorised;

	public List<PMByEmployeeDetailBean> getPmByEmployeeDetailBeans() {
		return pmByEmployeeDetailBeans;
	}

	public void setPmByEmployeeDetailBeans(List<PMByEmployeeDetailBean> pmByEmployeeDetailBeans) {
		this.pmByEmployeeDetailBeans = pmByEmployeeDetailBeans;
	}

	public MessageHeader getMessage() {
		return message;
	}

	public void setMessage(MessageHeader message) {
		this.message = message;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	
	public boolean isAuthorised() {
		return isAuthorised;
	}

	public void setAuthorised(boolean isAuthorised) {
		this.isAuthorised = isAuthorised;
	}

	@Override
	public String toString() {
		return "PMByEmployeeResBean [pmByEmployeeDetailBeans="
				+ pmByEmployeeDetailBeans + ", message=" + message
				+ ", errorMessage=" + errorMessage + ", totalRecords="
				+ totalRecords + ", totalRows=" + totalRows + ", currentPage="
				+ currentPage + ", totalPage=" + totalPage + ", isAuthorised="
				+ isAuthorised + "]";
	}

}
