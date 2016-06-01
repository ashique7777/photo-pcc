package com.walgreens.oms.bean;

public class PLUJsonFilters {
	
	private String startDate;
	private String endDate;
	private String pluNumber;
	private String emailIds;
	
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
	public String getPluNumber() {
		return pluNumber;
	}
	public void setPluNumber(String pluNumber) {
		this.pluNumber = pluNumber;
	}
	public String getEmailIds() {
		return emailIds;
	}
	@Override
	public String toString() {
		return "PLUJsonFilters [startDate=" + startDate + ", endDate="
				+ endDate + ", pluNumber=" + pluNumber + ", emailIds="
				+ emailIds + "]";
	}
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}
}
