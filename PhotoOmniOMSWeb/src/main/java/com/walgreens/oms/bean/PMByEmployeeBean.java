package com.walgreens.oms.bean;

public class PMByEmployeeBean {

	private String startDate;
	private String endDate;
	private String employeeId;
	private int storeNumber;
	private String storeLevelReport;
	private String sortColumnName;
	private String sortOrder;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public int getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(int storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getStoreLevelReport() {
		return storeLevelReport;
	}
	public void setStoreLevelReport(String storeLevelReport) {
		this.storeLevelReport = storeLevelReport;
	}
	
	@Override
	public String toString() {
		return "PMByEmployeeBean [startDate=" + startDate + ", endDate="
				+ endDate + ", employeeId=" + employeeId + ", storeNumber="
				+ storeNumber + ", storeLevelReport=" + storeLevelReport + "]";
	}
	public String getSortColumnName() {
		return sortColumnName;
	}
	public void setSortColumnName(String sortColumnName) {
		this.sortColumnName = sortColumnName;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
}
