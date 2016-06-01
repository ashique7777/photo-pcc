package com.walgreens.oms.bean;

import java.util.List;

public class LateEnvelopeProdDetailBean {
	private Long orderLineId;
	private String price;
	private String description;
	private double quantity;
	private double perUnitPrice;
	private List<LateEnvOrderLinePrombean> LateEnvOrderLinePrombean;
	
	
	
	
	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the perUnitPrice
	 */
	public double getPerUnitPrice() {
		return perUnitPrice;
	}
	/**
	 * @param perUnitPrice the perUnitPrice to set
	 */
	public void setPerUnitPrice(double perUnitPrice) {
		this.perUnitPrice = perUnitPrice;
	}
	/**
	 * @return the orderLineId
	 */
	public Long getOrderLineId() {
		return orderLineId;
	}
	/**
	 * @return the lateEnvOrderLinePrombean
	 */
	public List<LateEnvOrderLinePrombean> getLateEnvOrderLinePrombean() {
		return LateEnvOrderLinePrombean;
	}
	/**
	 * @param lateEnvOrderLinePrombean the lateEnvOrderLinePrombean to set
	 */
	public void setLateEnvOrderLinePrombean(
			List<LateEnvOrderLinePrombean> lateEnvOrderLinePrombean) {
		LateEnvOrderLinePrombean = lateEnvOrderLinePrombean;
	}
	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
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
