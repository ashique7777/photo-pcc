/**
 * 
 */
package com.walgreens.oms.bean;

import java.sql.Timestamp;

/**
 * @author ghoshsam
 *
 */
public class ShoppingCartBean {
	private long sysShoppingCartId;
	private long sysLocationID;
	private String shopingCartNBR;
	private Timestamp OrderPlacedDTTM;
	private String cartTypeCD;
	private String pmStatusCD;
	private String createUserID;
	private Timestamp createDTTM;
	private String updateUserID;
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
	 * @return the sysLocationID
	 */
	public long getSysLocationID() {
		return sysLocationID;
	}
	/**
	 * @param sysLocationID the sysLocationID to set
	 */
	public void setSysLocationID(long sysLocationID) {
		this.sysLocationID = sysLocationID;
	}
	/**
	 * @return the shopingCartNBR
	 */
	public String getShopingCartNBR() {
		return shopingCartNBR;
	}
	/**
	 * @param shopingCartNBR the shopingCartNBR to set
	 */
	public void setShopingCartNBR(String shopingCartNBR) {
		this.shopingCartNBR = shopingCartNBR;
	}
	/**
	 * @return the orderPlacedDTTM
	 */
	public Timestamp getOrderPlacedDTTM() {
		return OrderPlacedDTTM;
	}
	/**
	 * @param orderPlacedDTTM the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(Timestamp orderPlacedDTTM) {
		OrderPlacedDTTM = orderPlacedDTTM;
	}
	/**
	 * @return the cartTypeCD
	 */
	public String getCartTypeCD() {
		return cartTypeCD;
	}
	/**
	 * @param cartTypeCD the cartTypeCD to set
	 */
	public void setCartTypeCD(String cartTypeCD) {
		this.cartTypeCD = cartTypeCD;
	}
	/**
	 * @return the pmStatusCD
	 */
	public String getPmStatusCD() {
		return pmStatusCD;
	}
	/**
	 * @param pmStatusCD the pmStatusCD to set
	 */
	public void setPmStatusCD(String pmStatusCD) {
		this.pmStatusCD = pmStatusCD;
	}
	/**
	 * @return the createUserID
	 */
	public String getCreateUserID() {
		return createUserID;
	}
	/**
	 * @param createUserID the createUserID to set
	 */
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}
	/**
	 * @return the createDTTM
	 */
	public Timestamp getCreateDTTM() {
		return createDTTM;
	}
	/**
	 * @param createDTTM the createDTTM to set
	 */
	public void setCreateDTTM(Timestamp createDTTM) {
		this.createDTTM = createDTTM;
	}
	/**
	 * @return the updateUserID
	 */
	public String getUpdateUserID() {
		return updateUserID;
	}
	/**
	 * @param updateUserID the updateUserID to set
	 */
	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}
	/**
	 * @return the updateDTTM
	 */
	public Timestamp getUpdateDTTM() {
		return updateDTTM;
	}
	/**
	 * @param updateDTTM the updateDTTM to set
	 */
	public void setUpdateDTTM(Timestamp updateDTTM) {
		this.updateDTTM = updateDTTM;
	}
	private Timestamp updateDTTM;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShoppingCart [sysShoppingCartId=" + sysShoppingCartId
				+ ", sysLocationID=" + sysLocationID + ", shopingCartNBR="
				+ shopingCartNBR + ", OrderPlacedDTTM=" + OrderPlacedDTTM
				+ ", cartTypeCD=" + cartTypeCD + ", pmStatusCD=" + pmStatusCD
				+ ", createUserID=" + createUserID + ", createDTTM="
				+ createDTTM + ", updateUserID=" + updateUserID
				+ ", updateDTTM=" + updateDTTM + "]";
	}
	
	

}
