package com.walgreens.oms.json.bean;

public class ExceptionRepOrderLinePromotionDtls {

	private long orderLineId;
	private String orderLinePromotionDesc;
	private double orderLinePromotionAmt;

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
	 * @return the orderLinePromotionDesc
	 */
	public String getOrderLinePromotionDesc() {
		return orderLinePromotionDesc;
	}

	/**
	 * @param orderLinePromotionDesc
	 *            the orderLinePromotionDesc to set
	 */
	public void setOrderLinePromotionDesc(String orderLinePromotionDesc) {
		this.orderLinePromotionDesc = orderLinePromotionDesc;
	}

	/**
	 * @return the orderLinePromotionAmt
	 */
	public double getOrderLinePromotionAmt() {
		return orderLinePromotionAmt;
	}

	/**
	 * @param orderLinePromotionAmt
	 *            the orderLinePromotionAmt to set
	 */
	public void setOrderLinePromotionAmt(double orderLinePromotionAmt) {
		this.orderLinePromotionAmt = orderLinePromotionAmt;
	}

}
