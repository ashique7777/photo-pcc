package com.walgreens.oms.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SalesBYProductDataBean {

	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("emailIds")
	private String emailIds;
	@JsonProperty("productCategory")
	private List<String> productCategory;
	public List<String> getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(List<String> productCategory) {
		this.productCategory = productCategory;
	}
	public List<String> getProductSize() {
		return productSize;
	}
	public void setProductSize(List<String> productSize) {
		this.productSize = productSize;
	}

	@JsonProperty("productSize")
	private List<String> productSize;
	
	
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
		
		String productType = productCategory.toString().replace("[", "").replace("]", "");
		String printSize = productSize.toString().replace("[", "").replace("]", "");
		
		return "{\"startDate\":"+ "\"" +startDate +"\"" + ",\"endDate\":"+ "\""
				+endDate +"\"" + ",\"emailIds\":"+ "\"" + emailIds +"\"" + ",\"productType\":"+ "\""+ productType + "\""
				+ ",\"printSize\":"+ "\""+ printSize +"\"" + "}";
	}
}
