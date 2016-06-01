package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoyaltyDataBean {

	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("emailIds")
	private String emailIds;
	@JsonProperty("vendorName")
	private String vendorName;
	
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
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
	public String getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}
	
	@Override
	public String toString(){
		return "{\"startDate\":"+ "\"" +startDate +"\"" + ",\"endDate\":"+ "\""
				+endDate +"\"" + ",\"emailIds\":"+ "\"" + emailIds +"\"" + ",\"vendorName\":"+ "\""+ vendorName +"\"" + "}";
	}
}
