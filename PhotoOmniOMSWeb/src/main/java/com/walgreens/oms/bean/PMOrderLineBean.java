/**
 * 
 */
package com.walgreens.oms.bean;

import java.io.Serializable;
import java.util.*;

/**
 * @author CTS
 *
 */
public class PMOrderLineBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3727982171189896696L;
	private long orderId;
	private long orderLineId;
	private long productId;
	private int quantity; // sellable qty
	private double finalPrice;
	private int loyaltyDiscountAmt;
	private int setsQty;
	private List<OrderItmPmMonyDataBean>  pmMoneyList = new ArrayList<OrderItmPmMonyDataBean>();
	private OrderPMDataBean  orderPMDataBean = new OrderPMDataBean();
	private boolean isPMEligibleForDate = false;
	private boolean isPMEligibleForCoupon  = false ;
	private boolean isPMEligibleForQuantity  = false ;
	
	
	
	/**
	 * @return the isPMEligibleForQuantity
	 */
	public boolean isPMEligibleForQuantity() {
		return isPMEligibleForQuantity;
	}
	/**
	 * @param isPMEligibleForQuantity the isPMEligibleForQuantity to set
	 */
	public void setPMEligibleForQuantity(boolean isPMEligibleForQuantity) {
		this.isPMEligibleForQuantity = isPMEligibleForQuantity;
	}
	/**
	 * @return the isPMEligibleForCoupon
	 */
	public boolean isPMEligibleForCoupon() {
		return isPMEligibleForCoupon;
	}
	/**
	 * @param isPMEligibleForCoupon the isPMEligibleForCoupon to set
	 */
	public void setPMEligibleForCoupon(boolean isPMEligibleForCoupon) {
		this.isPMEligibleForCoupon = isPMEligibleForCoupon;
	}
	/**
	 * @return the isPMEligibleForDate
	 */
	public boolean isPMEligibleForDate() {
		return isPMEligibleForDate;
	}
	/**
	 * @param isPMEligibleForDate the isPMEligibleForDate to set
	 */
	public void setPMEligibleForDate(boolean isPMEligibleForDate) {
		this.isPMEligibleForDate = isPMEligibleForDate;
	}
	/**
	 * @return the pmMoneyList
	 */
	public List<OrderItmPmMonyDataBean> getPmMoneyList() {
		return pmMoneyList;
	}
	/**
	 * @param pmMoneyList the pmMoneyList to set
	 */
	public void setPmMoneyList(List<OrderItmPmMonyDataBean> pmMoneyList) {
		this.pmMoneyList = pmMoneyList;
	}
	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * @return the setsQty
	 */
	public int getSetsQty() {
		return setsQty;
	}
	/**
	 * @param setsQty the setsQty to set
	 */
	public void setSetsQty(int setsQty) {
		this.setsQty = setsQty;
	}
	/**
	 * @return the orderPMDataBean
	 */
	public OrderPMDataBean getOrderPMDataBean() {
		return orderPMDataBean;
	}
	/**
	 * @param orderPMDataBean the orderPMDataBean to set
	 */
	public void setOrderPMDataBean(OrderPMDataBean orderPMDataBean) {
		this.orderPMDataBean = orderPMDataBean;
	}
	
	/**
	 * @return the productId
	 */
	public long getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}
	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	/**
	 * @return the finalPrice
	 */
	public double getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	
	/**
	 * @return the loyaltyDiscountAmt
	 */
	public int getLoyaltyDiscountAmt() {
		return loyaltyDiscountAmt;
	}
	/**
	 * @param loyaltyDiscountAmt the loyaltyDiscountAmt to set
	 */
	public void setLoyaltyDiscountAmt(int loyaltyDiscountAmt) {
		this.loyaltyDiscountAmt = loyaltyDiscountAmt;
	}
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

}
