/**
 * 
 */
package com.walgreens.oms.bean;

/**
 * @author CTS
 *
 */
public class OrderlinePluBean {
	private String orderPlaceddttm;
	private long sysOrderLineid;
	private long sysOrderLinePluId;
	private long sysPluId;
	private long sysProductId;
	public long getSysProductId() {
		return sysProductId;
	}
	public void setSysProductId(long sysProductId) {
		this.sysProductId = sysProductId;
	}
	private String discountAmount;
	private long activeCd;
	private String pluNumber;
	private boolean inserInd;
	public String getOrderPlaceddttm() {
		return orderPlaceddttm;
	}
	public void setOrderPlaceddttm(String orderPlaceddttm) {
		this.orderPlaceddttm = orderPlaceddttm;
	}
	public long getSysOrderLineid() {
		return sysOrderLineid;
	}
	public void setSysOrderLineid(long sysOrderLineid) {
		this.sysOrderLineid = sysOrderLineid;
	}
	public long getSysOrderLinePluId() {
		return sysOrderLinePluId;
	}
	public void setSysOrderLinePluId(long sysOrderLinePluId) {
		this.sysOrderLinePluId = sysOrderLinePluId;
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

}
