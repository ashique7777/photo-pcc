/**
 * 
 */
package com.walgreens.oms.bean;

/**
 * @author CTS
 *
 */
public class OrderStatusBean {
	private long sysOrderId;
	private boolean status;
	private String orderPlacedDTTM; 
	private long sysShoppingCartId;
	
	/**
	 * @return the sysShoppingCartId
	 */
	public long getSysShoppingCartId() {
		return sysShoppingCartId;
	}
	/**
	 * @param sysShoppingCartId the sysShoppingCartId to set
	 */
	public void setSysShoppingCartId(long sysShoppingCartId) {
		this.sysShoppingCartId = sysShoppingCartId;
	}
	/**
	 * @return the orderPlacedDTTM
	 */
	public String getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}
	/**
	 * @param orderPlacedDTTM the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(String orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
	}
	/**
	 * @return the sysOrderId
	 */
	public long getSysOrderId() {
		return sysOrderId;
	}
	/**
	 * @param sysOrderId the sysOrderId to set
	 */
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

}
