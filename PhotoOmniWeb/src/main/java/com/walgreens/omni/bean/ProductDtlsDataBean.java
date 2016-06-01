package com.walgreens.omni.bean;

public class ProductDtlsDataBean {

	private String orderDesc;
    private String finalPrice;
    private String productDesc;
    private String quantity;
    private String perUnitPrice;
    private String orderLineId;
    
    
	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId() {
		return orderLineId;
	}
	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(String orderLineId) {
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
	 * @return the orderDesc
	 */
	public String getOrderDesc() {
		return orderDesc;
	}
	/**
	 * @param orderDesc the orderDesc to set
	 */
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	/**
	 * @return the finalPrice
	 */
	public String getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(String finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the perUnitPrice
	 */
	public String getPerUnitPrice() {
		return perUnitPrice;
	}
	/**
	 * @param perUnitPrice the perUnitPrice to set
	 */
	public void setPerUnitPrice(String perUnitPrice) {
		this.perUnitPrice = perUnitPrice;
	}
    
    
	
}
