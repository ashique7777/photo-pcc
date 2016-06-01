package com.walgreens.batch.central.bean;


public class OmOrderAttributeBean {
	
	private int shoppingCartId;
	private int orderId;
	private String tookEmployeeId;
	private String orderPlacedDttm;
	private double cost;
	private double fulfillmentVendorCost;
	private String expenseInd;
	private String processingTypeCd;
	private String costCalculatiomStatusCd;
	private int payOnFulfillmentInd;
	private double originalLinePrice;
	
	public double getOriginalLinePrice() {
		return originalLinePrice;
	}
	public void setOriginalLinePrice(double originalLinePrice) {
		this.originalLinePrice = originalLinePrice;
	}
	public double getFulfillmentVendorCost() {
		return fulfillmentVendorCost;
	}
	public void setFulfillmentVendorCost(double fulfillmentVendorCost) {
		this.fulfillmentVendorCost = fulfillmentVendorCost;
	}
	public int getShoppingCartId() {
		return shoppingCartId;
	}
	public void setShoppingCartId(int shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getTookEmployeeId() {
		return tookEmployeeId;
	}
	public void setTookEmployeeId(String tookEmployeeId) {
		this.tookEmployeeId = tookEmployeeId;
	}
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getExpenseInd() {
		return expenseInd;
	}
	public void setExpenseInd(String expenseInd) {
		this.expenseInd = expenseInd;
	}
	public String getProcessingTypeCd() {
		return processingTypeCd;
	}
	public void setProcessingTypeCd(String processingTypeCd) {
		this.processingTypeCd = processingTypeCd;
	}
	public String getCostCalculatiomStatusCd() {
		return costCalculatiomStatusCd;
	}
	public void setCostCalculatiomStatusCd(String costCalculatiomStatusCd) {
		this.costCalculatiomStatusCd = costCalculatiomStatusCd;
	}
	public int getPayOnFulfillmentInd() {
		return payOnFulfillmentInd;
	}
	public void setPayOnFulfillmentInd(int payOnFulfillmentInd) {
		this.payOnFulfillmentInd = payOnFulfillmentInd;
	}

}
