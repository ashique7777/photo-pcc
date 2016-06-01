package com.walgreens.oms.bean;

public class EnvelopeOrderPromotionBean {

	private String orderPromotnDesc;
	private double orderDiscountamt;
	/**
	 * @return the orderPromotnDesc
	 */
	public String getOrderPromotnDesc() {
		return orderPromotnDesc;
	}
	/**
	 * @param orderPromotnDesc the orderPromotnDesc to set
	 */
	public void setOrderPromotnDesc(String orderPromotnDesc) {
		this.orderPromotnDesc = orderPromotnDesc;
	}
	/**
	 * @return the orderDiscountamt
	 */
	public double getOrderDiscountamt() {
		return orderDiscountamt;
	}
	/**
	 * @param orderDiscountamt the orderDiscountamt to set
	 */
	public void setOrderDiscountamt(double orderDiscountamt) {
		this.orderDiscountamt = orderDiscountamt;
	}
	
}
