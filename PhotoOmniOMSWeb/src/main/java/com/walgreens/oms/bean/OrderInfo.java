package com.walgreens.oms.bean;

public class OrderInfo {
	private long envelopeNbr;
	private String description; 
	private String price;
	private long kioskId;
	private long webId;
	
	
	
	
	/**
	 * @return the envelopeNbr
	 */
	public long getEnvelopeNbr() {
		return envelopeNbr;
	}
	/**
	 * @param envelopeNbr the envelopeNbr to set
	 */
	public void setEnvelopeNbr(long envelopeNbr) {
		this.envelopeNbr = envelopeNbr;
	}
	/**
	 * @return the kioskId
	 */
	public long getKioskId() {
		return kioskId;
	}
	/**
	 * @param kioskId the kioskId to set
	 */
	public void setKioskId(long kioskId) {
		this.kioskId = kioskId;
	}
	/**
	 * @return the webId
	 */
	public long getWebId() {
		return webId;
	}
	/**
	 * @param webId the webId to set
	 */
	public void setWebId(long webId) {
		this.webId = webId;
	}
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
