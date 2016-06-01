/**
 * 
 */
package com.walgreens.omni.json.bean;

/**
 * @author CTS
 *
 */
public class CannedReportResGenericBean {
	
	private Double totalOrderRevenue;
	private Double totalDiscount;
	private int totalOrder;
	/**
	 * @return the totalOrder
	 */
	public int getTotalOrder() {
		return totalOrder;
	}
	/**
	 * @param totalOrder the totalOrder to set
	 */
	public void setTotalOrder(int totalOrder) {
		this.totalOrder = totalOrder;
	}
	/**
	 * @return the totalOrderRevenue
	 */
	public Double getTotalOrderRevenue() {
		return totalOrderRevenue;
	}
	/**
	 * @param totalOrderRevenue the totalOrderRevenue to set
	 */
	public void setTotalOrderRevenue(Double totalOrderRevenue) {
		this.totalOrderRevenue = totalOrderRevenue;
	}
	/**
	 * @return the totalDiscount
	 */
	public Double getTotalDiscount() {
		return totalDiscount;
	}
	/**
	 * @param totalDiscount the totalDiscount to set
	 */
	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	
	

}
