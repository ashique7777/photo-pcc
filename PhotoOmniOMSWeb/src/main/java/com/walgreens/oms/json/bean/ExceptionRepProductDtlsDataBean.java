package com.walgreens.oms.json.bean;

import java.util.List;

public class ExceptionRepProductDtlsDataBean {

	
    private String productDesc;
    private long quantity;
    private double orderLinePrice;
    private long orderLineId;
    private double perUnitPrice;
    
    private List<ExceptionRepOrderLinePromotionDtls> orderLinePromotionList;
  
    
    
	/**
	 * @return the orderLinePromotionList
	 */
	public List<ExceptionRepOrderLinePromotionDtls> getOrderLinePromotionList() {
		return orderLinePromotionList;
	}
	/**
	 * @param orderLinePromotionList the orderLinePromotionList to set
	 */
	public void setOrderLinePromotionList(
			List<ExceptionRepOrderLinePromotionDtls> orderLinePromotionList) {
		this.orderLinePromotionList = orderLinePromotionList;
	}
	/**
	 * @return the orderLinePrice
	 */
	public double getOrderLinePrice() {
		return orderLinePrice;
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
	 * @param orderLinePrice the orderLinePrice to set
	 */
	public void setOrderLinePrice(double orderLinePrice) {
		this.orderLinePrice = orderLinePrice;
	}
	
	
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
	
    
    
	
}
