package com.walgreens.oms.bean;

import java.util.List;

public class EnvelopeProductDtlsBean {

	private long orderLineId;
	private double orderLinePrice;
	private String productDesc;
	private long quantity;
	private double perUnitPrice;
	private List<EnvelopeOrderLinePromotionBean> envOrderLinePrombean;
	/**
	 * @return the orderLineId
	 */
	public long getOrderLineId() {
		return orderLineId;
	}
	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(long orderLineId) {
		this.orderLineId = orderLineId;
	}
	
	
	/**
	 * @return the orderLinePrice
	 */
	public double getOrderLinePrice() {
		return orderLinePrice;
	}
	/**
	 * @param orderLinePrice the orderLinePrice to set
	 */
	public void setOrderLinePrice(double orderLinePrice) {
		this.orderLinePrice = orderLinePrice;
	}
	/**
	 * @return the productDesc
	 */
	public String getProductDesc() {
		return productDesc;
	}
	/**
	 * @param productDesc the productDesc to set
	 */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	/**
	 * @return the quantity
	 */
	public long getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(long quantity) {
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
	 * @return the envOrderLinePrombean
	 */
	public List<EnvelopeOrderLinePromotionBean> getEnvOrderLinePrombean() {
		return envOrderLinePrombean;
	}
	/**
	 * @param envOrderLinePrombean the envOrderLinePrombean to set
	 */
	public void setEnvOrderLinePrombean(
			List<EnvelopeOrderLinePromotionBean> envOrderLinePrombean) {
		this.envOrderLinePrombean = envOrderLinePrombean;
	}
	
	
}
