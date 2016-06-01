package com.walgreens.oms.bean;

public class ShopCartPluDetail {
	    
	private String orderPlacedDttm;
	private String sysScPluId;
	private long shoppingCartId;
	private long sysPluId;
	private String pluDiscAmount;
	private long activeCd;
	private String pluNumber;
	private boolean inserInd;
	private boolean updateInd;
	
	
	
	public long getActiveCd() {
		return activeCd;
	}
	public void setActiveCd(long activeCd) {
		this.activeCd = activeCd;
	}
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	
	public long getShoppingCartId() {
		return shoppingCartId;
	}
	public void setShoppingCartId(long shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	
	public String getPluDiscAmount() {
		return pluDiscAmount;
	}
	public void setPluDiscAmount(String pluDiscAmount) {
		this.pluDiscAmount = pluDiscAmount;
	}
	public String getSysScPluId() {
		return sysScPluId;
	}
	public void setSysScPluId(String sysScPluId) {
		this.sysScPluId = sysScPluId;
	}
	
	public boolean isInserInd() {
		return inserInd;
	}
	public void setInserInd(boolean inserInd) {
		this.inserInd = inserInd;
	}
	public boolean isUpdateInd() {
		return updateInd;
	}
	public void setUpdateInd(boolean updateInd) {
		this.updateInd = updateInd;
	}
	public String getPluNumber() {
		return pluNumber;
	}
	public void setPluNumber(String pluNumber) {
		this.pluNumber = pluNumber;
	}
	public long getSysPluId() {
		return sysPluId;
	}
	public void setSysPluId(long sysPluId) {
		this.sysPluId = sysPluId;
	}
	
	
}
