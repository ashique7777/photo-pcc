package com.walgreens.batch.central.bean;

public class CSVFileReportDataBean {
	
	private String WIC;
	private String productDescription;
	private double calculatedRetail;
	private double sales;
	private double amountOfPMPaid;
	private long numberOfOrders;
	private double itemCost;
	private int totalQuantity;
	private double grossProfit;
	
	
	public String getWIC() {
		return WIC;
	}
	public void setWIC(String wIC) {
		WIC = wIC;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public double getCalculatedRetail() {
		return calculatedRetail;
	}
	public void setCalculatedRetail(double calculatedRetail) {
		this.calculatedRetail = calculatedRetail;
	}
	public double getSales() {
		return sales;
	}
	public void setSales(double sales) {
		this.sales = sales;
	}
	public double getAmountOfPMPaid() {
		return amountOfPMPaid;
	}
	public void setAmountOfPMPaid(double amountOfPMPaid) {
		this.amountOfPMPaid = amountOfPMPaid;
	}
	public long getNumberOfOrders() {
		return numberOfOrders;
	}
	public void setNumberOfOrders(long l) {
		this.numberOfOrders = l;
	}
	public double getItemCost() {
		return itemCost;
	}
	public void setItemCost(double itemCost) {
		this.itemCost = itemCost;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(double grossProfit) {
		this.grossProfit = grossProfit;
	}
	
}
