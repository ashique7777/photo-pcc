package com.walgreens.omni.bean;

public class CannedReportCSVBean {
	
	private String ProductName;
	private int TotalProductQuantity;
	private int TotalOrder;
	private Double TotalRevenue;
	private Double TotalRevenueDiscount;
	private Double UnitPrice;
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return ProductName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		ProductName = productName;
	}
	/**
	 * @return the totalProductQuantity
	 */
	public int getTotalProductQuantity() {
		return TotalProductQuantity;
	}
	/**
	 * @param totalProductQuantity the totalProductQuantity to set
	 */
	public void setTotalProductQuantity(int totalProductQuantity) {
		TotalProductQuantity = totalProductQuantity;
	}
	/**
	 * @return the totalOrder
	 */
	public int getTotalOrder() {
		return TotalOrder;
	}
	/**
	 * @param totalOrder the totalOrder to set
	 */
	public void setTotalOrder(int totalOrder) {
		TotalOrder = totalOrder;
	}
	/**
	 * @return the totalRevenue
	 */
	public Double getTotalRevenue() {
		return TotalRevenue;
	}
	/**
	 * @param totalRevenue the totalRevenue to set
	 */
	public void setTotalRevenue(Double totalRevenue) {
		TotalRevenue = totalRevenue;
	}
	/**
	 * @return the totalRevenueDiscount
	 */
	public Double getTotalRevenueDiscount() {
		return TotalRevenueDiscount;
	}
	/**
	 * @param totalRevenueDiscount the totalRevenueDiscount to set
	 */
	public void setTotalRevenueDiscount(Double totalRevenueDiscount) {
		TotalRevenueDiscount = totalRevenueDiscount;
	}
	/**
	 * @return the unitPrice
	 */
	public Double getUnitPrice() {
		return UnitPrice;
	}
	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(Double unitPrice) {
		UnitPrice = unitPrice;
	}
	
	

}
