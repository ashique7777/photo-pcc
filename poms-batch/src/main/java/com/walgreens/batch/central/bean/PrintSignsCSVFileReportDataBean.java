package com.walgreens.batch.central.bean;

public class PrintSignsCSVFileReportDataBean {
	
	private String storeNo;
	private String eventName;
	private Long quantity;
	/**
	 * @return the storeNo
	 */
	public String getStoreNo() {
		return storeNo;
	}
	/**
	 * @param storeNo the storeNo to set
	 */
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	} 
	
	
	
}
