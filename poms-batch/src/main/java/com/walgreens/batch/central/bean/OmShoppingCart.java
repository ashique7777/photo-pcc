package com.walgreens.batch.central.bean;

import java.sql.Timestamp;
import java.util.Date;

/**
 * This class us used as a bean to store OmShoppingCart info
 * @author Cognizant
 *
 */
public class OmShoppingCart {
	
	private int shoppingCartId;
	private String cartType;
	private String pmStatus;
	private Timestamp orderPlacedDttm;
	private String createUserId;
	private Date createDttm;
	private String updatedUserId;
	private Date updateDttm;
	private int owningLocationId;
	private long shopCartNo;
	
	/**
	 * This method fetches shoppingCartId
	 * @return shoppingCartId
	 */
	public int getSysShoppingCartId() {
		return shoppingCartId;
	}
	/**
	 * This method sets shoppingCartId
	 * @param shoppingCartId
	 */
	public void setSysShoppingCartId(int shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	/**
	 * This method fetches cartType
	 * @return cartType
	 */
	public String getCartType() {
		return cartType;
	}
	/**
	 * This method sets cartType
	 * @param cartType
	 */
	public void setCartType(String cartType) {
		this.cartType = cartType;
	}
	/**
	 * This method fetches pmStatus
	 * @return pmStatus
	 */
	public String getPmStatus() {
		return pmStatus;
	}
	/**
	 * This method sets pmStatus
	 * @param pmStatus
	 */
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}
	/**
	 * This method fetches orderPlacedDttm
	 * @return orderPlacedDttm
	 */
	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	/**
	 * This method sets orderPlacedDttm
	 * @param orderPlacedDttm
	 */
	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	/**
	 * This method fetches createUserId
	 * @return createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * This method sets createUserId
	 * @param createUserId
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * This method fetches createDttm
	 * @return createDttm
	 */
	public Date getCreateDttm() {
		return createDttm;
	}
	/**
	 * This method sets createDttm
	 * @param createDttm
	 */
	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}
	/**
	 * This method fetches updatedUserId
	 * @return updatedUserId
	 */
	public String getUpdatedUserId() {
		return updatedUserId;
	}
	/**
	 * This method sets updatedUserId
	 * @param updatedUserId
	 */
	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	/**
	 * This method fetches updateDttm
	 * @return updateDttm
	 */
	public Date getUpdateDttm() {
		return updateDttm;
	}
	/**
	 * This method sets updateDttm
	 * @param updateDttm
	 */
	public void setUpdateDttm(Date updateDttm) {
		this.updateDttm = updateDttm;
	}
	/**
	 * This method fetches owningLocationId
	 * @return owningLocationId
	 */
	public int getOwningLocationId() {
		return owningLocationId;
	}
	/**
	 * This method sets owningLocationId
	 * @param owningLocationId
	 */
	public void setOwningLocationId(int owningLocationId) {
		this.owningLocationId = owningLocationId;
	}
	/**
	 * This method fetches shopCartNo
	 * @return shopCartNo
	 */
	public long getShopCartNo() {
		return shopCartNo;
	}
	/**
	 * This method sets shopCartNo
	 * @param shopCartNo
	 */
	public void setShopCartNo(long shopCartNo) {
		this.shopCartNo = shopCartNo;
	}
	
	

}
