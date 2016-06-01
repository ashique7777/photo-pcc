/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;

/**
 * @author CTS
 *
 */
public class PayOnFulfillmentItemData  implements Serializable {

	int productId;
	double vendPaymentAmt;
	double calculatedPrice;
	int quantity;
	double itemSoldAmount;
	String orderPlaceDttm;
	
	
	/**
	 * @return the orderPlaceDttm
	 */
	public String getOrderPlaceDttm() {
		return orderPlaceDttm;
	}
	/**
	 * @param orderPlaceDttm the orderPlaceDttm to set
	 */
	public void setOrderPlaceDttm(String orderPlaceDttm) {
		this.orderPlaceDttm = orderPlaceDttm;
	}
	/**
	 * @return the itemSoldAmount
	 */
	public double getItemSoldAmount() {
		return itemSoldAmount;
	}
	/**
	 * @param itemSoldAmount the itemSoldAmount to set
	 */
	public void setItemSoldAmount(double itemSoldAmount) {
		this.itemSoldAmount = itemSoldAmount;
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
	 * @return the vendPaymentAmt
	 */
	public double getVendPaymentAmt() {
		return vendPaymentAmt;
	}
	/**
	 * @param vendPaymentAmt the vendPaymentAmt to set
	 */
	public void setVendPaymentAmt(double vendPaymentAmt) {
		this.vendPaymentAmt = vendPaymentAmt;
	}
	/**
	 * @return the calculatedPrice
	 */
	public double getCalculatedPrice() {
		return calculatedPrice;
	}
	/**
	 * @param calculatedPrice the calculatedPrice to set
	 */
	public void setCalculatedPrice(double calculatedPrice) {
		this.calculatedPrice = calculatedPrice;
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
	
	
}
