package com.walgreens.oms.bean;

public class SelOrderPluDataBean {
	
	private String orderPlaceddttm;
	private long sysOrderid;
	private long sysOrderPluId;
	private long sysPluId;
	private String discountAmount;
	private long activeCd;
	private String pluNumber;
	private boolean inserInd;
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
	private boolean updateInd;
	public String getOrderPlaceddttm() {
		return orderPlaceddttm;
	}
	public void setOrderPlaceddttm(String orderPlaceddttm) {
		this.orderPlaceddttm = orderPlaceddttm;
	}
	public long getSysOrderid() {
		return sysOrderid;
	}
	public void setSysOrderid(long sysOrderid) {
		this.sysOrderid = sysOrderid;
	}
	public long getSysOrderPluId() {
		return sysOrderPluId;
	}
	public void setSysOrderPluId(long sysOrderPluId) {
		this.sysOrderPluId = sysOrderPluId;
	}
	public long getSysPluId() {
		return sysPluId;
	}
	public void setSysPluId(long sysPluId) {
		this.sysPluId = sysPluId;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public long getActiveCd() {
		return activeCd;
	}
	public void setActiveCd(long activeCd) {
		this.activeCd = activeCd;
	}
	public String getPluNumber() {
		return pluNumber;
	}
	public void setPluNumber(String pluNumber) {
		this.pluNumber = pluNumber;
	}
	
	

}
