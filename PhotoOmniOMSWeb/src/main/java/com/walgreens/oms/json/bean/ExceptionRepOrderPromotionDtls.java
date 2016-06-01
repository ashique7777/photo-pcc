package com.walgreens.oms.json.bean;

public class ExceptionRepOrderPromotionDtls {
	private String orderPromotionDesc;
	private double orderPromotionAmt;

	/**
	 * @return the orderPromotionDesc
	 */
	public String getOrderPromotionDesc() {
		return orderPromotionDesc;
	}

	/**
	 * @param orderPromotionDesc
	 *            the orderPromotionDesc to set
	 */
	public void setOrderPromotionDesc(String orderPromotionDesc) {
		this.orderPromotionDesc = orderPromotionDesc;
	}

	/**
	 * @return the orderPromotionAmt
	 */
	public double getOrderPromotionAmt() {
		return orderPromotionAmt;
	}

	/**
	 * @param orderPromotionAmt
	 *            the orderPromotionAmt to set
	 */
	public void setOrderPromotionAmt(double orderPromotionAmt) {
		this.orderPromotionAmt = orderPromotionAmt;
	}

}
