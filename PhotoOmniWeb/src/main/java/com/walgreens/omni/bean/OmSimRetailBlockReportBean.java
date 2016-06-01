package com.walgreens.omni.bean;

public class OmSimRetailBlockReportBean {
	
	private int locationNbr;
	private int retailBlock;
	private String description;
	private int totalRows;
	
	public int getLocationNbr() {
		return locationNbr;
	}
	public void setLocationNbr(int locationNbr) {
		this.locationNbr = locationNbr;
	}
	public int getRetailBlock() {
		return retailBlock;
	}
	public void setRetailBlock(int retailBlock) {
		this.retailBlock = retailBlock;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

}
