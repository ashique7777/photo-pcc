package com.walgreens.oms.bean;

/**
 * This class is used as a bean to store OmOrderLine info
 * @author Cognizant
 *
 */
public class OmOrderLineBean{
	
	private int orderId;
	private int productId;
	private double cost;
	private double fulfillmentVendorCost;
	private int quantity;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * This method fetches fulfillmentVendorCost
	 * @return fulfillmentVendorCost
	 */
	public double getFulfillmentVendorCost() {
		return fulfillmentVendorCost;
	}
	/**
	 * This method sets fulfillmentVendorCost
	 * @param fulfillmentVendorCost
	 */
	public void setFulfillmentVendorCost(double fulfillmentVendorCost) {
		this.fulfillmentVendorCost = fulfillmentVendorCost;
	}
	/**
	 * This method fetches orderId
	 * @return orderId
	 */
	public int getOrderId() {
		return orderId;
	}
	/**
	 * This method sets orderId
	 * @param orderId
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	/**
	 * This method fetches productId
	 * @return productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * This method sets productId
	 * @param productId
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	/**
	 * This method fetches cost
	 * @return cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * This method sets cost
	 * @param cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

}
