package com.walgreens.batch.central.bean;

public class CSVFilePLUReportDataBean {

	private String orderDate;
	private String pluNumber;
	private String couponCode;
	private String promotionDescription;
	private String channel;
	private double retailPrice;
	private double discountPrice;
	private long noOfOrders;
	private long noOfUnits;

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getPluNumber() {
		return pluNumber;
	}

	public void setPluNumber(String pluNumber) {
		this.pluNumber = pluNumber;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public long getNoOfOrders() {
		return noOfOrders;
	}

	public void setNoOfOrders(long noOfOrders) {
		this.noOfOrders = noOfOrders;
	}

	public long getNoOfUnits() {
		return noOfUnits;
	}

	public void setNoOfUnits(long noOfUnits) {
		this.noOfUnits = noOfUnits;
	}
}
