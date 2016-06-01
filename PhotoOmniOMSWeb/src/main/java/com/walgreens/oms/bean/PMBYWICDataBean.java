package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PMBYWICDataBean {

	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("emailIds")
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
	public String getEmailIds() {
		return emailIds;
	}
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}
	
	@Override
	public String toString(){
		return "{\"startDate\":"+ "\"" +startDate +"\"" + ",\"endDate\":"+ "\""
				+endDate +"\"" + ",\"emailIds\":"+ "\"" + emailIds +"\"" + "}";
	}
}
