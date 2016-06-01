/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.*;

/**
 * @author CTS
 *
 */
public class ItemDataBean implements Serializable{
	
	
	private long orderId;
	private long productId;
	private int quantity; // sellable qty
	private int originalQty;  // origiunal order qty
	private double originalLinePrice;
	private double finalPrice;
	private double cost;
	private int loyaltyDiscountAmt;
	private String productActiveInd;
	private int setsQty;
	private OrderItemPromotionDataBean odrItmPmBean = new OrderItemPromotionDataBean();// not needed
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
	 * @return the odrItmPmBean
	 */
	public OrderItemPromotionDataBean getOdrItmPmBean() {
		return odrItmPmBean;
	}
	/**
	 * @param odrItmPmBean the odrItmPmBean to set
	 */
	public void setOdrItmPmBean(OrderItemPromotionDataBean odrItmPmBean) {
		this.odrItmPmBean = odrItmPmBean;
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
	 * @return the originalQty
	 */
	public int getOriginalQty() {
		return originalQty;
	}
	/**
	 * @param originalQty the originalQty to set
	 */
	public void setOriginalQty(int originalQty) {
		this.originalQty = originalQty;
	}
	/**
	 * @return the originalLinePrice
	 */
	public double getOriginalLinePrice() {
		return originalLinePrice;
	}
	/**
	 * @param originalLinePrice the originalLinePrice to set
	 */
	public void setOriginalLinePrice(double originalLinePrice) {
		this.originalLinePrice = originalLinePrice;
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
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
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
	 * @return the productActiveInd
	 */
	public String getProductActiveInd() {
		return productActiveInd;
	}
	/**
	 * @param productActiveInd the productActiveInd to set
	 */
	public void setProductActiveInd(String productActiveInd) {
		this.productActiveInd = productActiveInd;
	}
	
	
	

}
