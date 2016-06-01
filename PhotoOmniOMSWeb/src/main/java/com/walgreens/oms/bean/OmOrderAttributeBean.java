package com.walgreens.oms.bean;

import java.util.ArrayList;
import java.util.List;


public class OmOrderAttributeBean { 
	
	
	private int shoppingCartId;
	private long sysorderId;
	private String tookEmployeeId;
	private String orderPlacedDttm;
	private double cost;
	private double fulfillmentVendorCost;
	private String expenseInd;
	private String processingTypeCd;
	private String costCalculatiomStatusCd;
	private int payOnFulfillmentInd;
	private double originalLinePrice;
	private int sysFulfilmentVendorId;
	private String costCalStatusCd;
	List<OrderItemLineBean> orderItemLineBeanList=new ArrayList<OrderItemLineBean>();
	
	
	public List<OrderItemLineBean> getOrderItemLineBeanList() {
		return orderItemLineBeanList;
	}
	public void setOrderItemLineBeanList(
			List<OrderItemLineBean> orderItemLineBeanList) {
		this.orderItemLineBeanList = orderItemLineBeanList;
	}
	public int getSysFulfilmentVendorId() {
		return sysFulfilmentVendorId;
	}
	public void setSysFulfilmentVendorId(int sysFulfilmentVendorId) {
		this.sysFulfilmentVendorId = sysFulfilmentVendorId;
	}
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

	public long getSysorderId() {
		return sysorderId;
	}
	public void setSysorderId(long sysorderId) {
		this.sysorderId = sysorderId;
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
	public String getCostCalStatusCd() {
		return costCalStatusCd;
	}
	public void setCostCalStatusCd(String costCalStatusCd) {
		this.costCalStatusCd = costCalStatusCd;
	}

}
