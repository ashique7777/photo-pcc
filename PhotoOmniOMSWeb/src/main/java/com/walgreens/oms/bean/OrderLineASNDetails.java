package com.walgreens.oms.bean;


public class OrderLineASNDetails {
		
	private String pcpProductId;	
	private Integer orderedQty=0;	
	private Integer iMemQty=0;
	private Double calculatedPrice=0.0;
	private Double discountAmount=0.0;
	private String iMemAddedItem;
	private Double originalRetail=0.0;
	
	
	public String getPcpProductId() {
		return pcpProductId;
	}
	public void setPcpProductId(String pcpProductId) {
		this.pcpProductId = pcpProductId;
	}
	public Integer getOrderedQty() {
		return orderedQty;
	}
	public void setOrderedQty(Integer orderedQty) {
		this.orderedQty = orderedQty;
	}
	public Integer getiMemQty() {
		return iMemQty;
	}
	public void setiMemQty(Integer iMemQty) {
		this.iMemQty = iMemQty;
	}
	public Double getCalculatedPrice() {
		return calculatedPrice;
	}
	public void setCalculatedPrice(Double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
	}
	public Double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getiMemAddedItem() {
		return iMemAddedItem;
	}
	public void setiMemAddedItem(String iMemAddedItem) {
		this.iMemAddedItem = iMemAddedItem;
	}
	public Double getOriginalRetail() {
		return originalRetail;
	}
	public void setOriginalRetail(Double originalRetail) {
		this.originalRetail = originalRetail;
	}

}
