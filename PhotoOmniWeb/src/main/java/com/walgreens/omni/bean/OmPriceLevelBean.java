package com.walgreens.omni.bean;

public class OmPriceLevelBean {
	
	private String priceLevel;
	private String description;
	private long sysPriceLevelId;
	
	public long getSysPriceLevelId() {
		return sysPriceLevelId;
	}
	public void setSysPriceLevelId(long sysPriceLevelId) {
		this.sysPriceLevelId = sysPriceLevelId;
	}
	public String getPriceLevel() {
		return priceLevel;
	}
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
