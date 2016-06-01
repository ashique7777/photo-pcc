/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;

/**
 * @author CTS
 *
 */
public class OrderItemPromotionDataBean implements Serializable{
	
	private int orderItemPluId;
	private int orderId;
	private int productId;
	private int pluId;
	private int pluDiscountAmt;
	/**
	 * @return the orderItemPluId
	 */
	public int getOrderItemPluId() {
		return orderItemPluId;
	}
	/**
	 * @param orderItemPluId the orderItemPluId to set
	 */
	public void setOrderItemPluId(int orderItemPluId) {
		this.orderItemPluId = orderItemPluId;
	}
	/**
	 * @return the orderId
	 */
	public int getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * @return the pluId
	 */
	public int getPluId() {
		return pluId;
	}
	/**
	 * @param pluId the pluId to set
	 */
	public void setPluId(int pluId) {
		this.pluId = pluId;
	}
	/**
	 * @return the pluDiscountAmt
	 */
	public int getPluDiscountAmt() {
		return pluDiscountAmt;
	}
	/**
	 * @param pluDiscountAmt the pluDiscountAmt to set
	 */
	public void setPluDiscountAmt(int pluDiscountAmt) {
		this.pluDiscountAmt = pluDiscountAmt;
	}
	
	


}
