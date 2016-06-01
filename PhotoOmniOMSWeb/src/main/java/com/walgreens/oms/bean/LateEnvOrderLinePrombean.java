package com.walgreens.oms.bean;

public class LateEnvOrderLinePrombean {
	
	private Long OrderLineId;
	private String promotionDesc;
	private double discountAmt;
	/**
	 * @return the orderLineId
	 */
	public Long getOrderLineId() {
		return OrderLineId;
	}
	/**
	 * @param orderLineId the orderLineId to set
	 */
	public void setOrderLineId(Long orderLineId) {
		OrderLineId = orderLineId;
	}
	/**
	 * @return the promotionDesc
	 */
	public String getPromotionDesc() {
		return promotionDesc;
	}
	/**
	 * @param promotionDesc the promotionDesc to set
	 */
	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}
	/**
	 * @return the discountAmt
	 */
	public double getDiscountAmt() {
		return discountAmt;
	}
	/**
	 * @param discountAmt the discountAmt to set
	 */
	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}
	
	
	

}
