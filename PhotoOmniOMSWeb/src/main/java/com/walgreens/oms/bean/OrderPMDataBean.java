/**
 * 
 */
package com.walgreens.oms.bean;

import java.io.Serializable;

/**
 * @author CTS
 *
 */
public class OrderPMDataBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 824583227028454640L;
	private long orderId;
	private long productId;
	private long pmId;
	private long empId;
	private int earnedQty;
	private int potentialQty;
	private int pendingQty;
	private double earnedAmt;
	private double potentialAmt;
	private double pendingAmt;
	private String orderPlacedDttm;
	private int activeCd;
	private boolean insrtInd;
	private boolean deleteInd;
	private boolean updateInd;
	
	
	/**
	 * @return the activeCd
	 */
	public int getActiveCd() {
		return activeCd;
	}
	/**
	 * @param activeCd the activeCd to set
	 */
	public void setActiveCd(int activeCd) {
		this.activeCd = activeCd;
	}
	/**
	 * @return the insrtInd
	 */
	public boolean isInsrtInd() {
		return insrtInd;
	}
	/**
	 * @param insrtInd the insrtInd to set
	 */
	public void setInsrtInd(boolean insrtInd) {
		this.insrtInd = insrtInd;
	}
	/**
	 * @return the deleteInd
	 */
	public boolean isDeleteInd() {
		return deleteInd;
	}
	/**
	 * @param deleteInd the deleteInd to set
	 */
	public void setDeleteInd(boolean deleteInd) {
		this.deleteInd = deleteInd;
	}
	/**
	 * @return the updateInd
	 */
	public boolean isUpdateInd() {
		return updateInd;
	}
	/**
	 * @param updateInd the updateInd to set
	 */
	public void setUpdateInd(boolean updateInd) {
		this.updateInd = updateInd;
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
	 * @return the pmId
	 */
	public long getPmId() {
		return pmId;
	}
	/**
	 * @param pmId the pmId to set
	 */
	public void setPmId(long pmId) {
		this.pmId = pmId;
	}
	/**
	 * @return the empId
	 */
	public long getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	/**
	 * @return the earnedQty
	 */
	public int getEarnedQty() {
		return earnedQty;
	}
	/**
	 * @param earnedQty the earnedQty to set
	 */
	public void setEarnedQty(int earnedQty) {
		this.earnedQty = earnedQty;
	}
	/**
	 * @return the potentialQty
	 */
	public int getPotentialQty() {
		return potentialQty;
	}
	/**
	 * @param potentialQty the potentialQty to set
	 */
	public void setPotentialQty(int potentialQty) {
		this.potentialQty = potentialQty;
	}
	/**
	 * @return the pendingQty
	 */
	public int getPendingQty() {
		return pendingQty;
	}
	/**
	 * @param pendingQty the pendingQty to set
	 */
	public void setPendingQty(int pendingQty) {
		this.pendingQty = pendingQty;
	}
	/**
	 * @return the earnedAmt
	 */
	public double getEarnedAmt() {
		return earnedAmt;
	}
	/**
	 * @param earnedAmt the earnedAmt to set
	 */
	public void setEarnedAmt(double earnedAmt) {
		this.earnedAmt = earnedAmt;
	}
	/**
	 * @return the potentialAmt
	 */
	public double getPotentialAmt() {
		return potentialAmt;
	}
	/**
	 * @param potentialAmt the potentialAmt to set
	 */
	public void setPotentialAmt(double potentialAmt) {
		this.potentialAmt = potentialAmt;
	}
	/**
	 * @return the pendingAmt
	 */
	public double getPendingAmt() {
		return pendingAmt;
	}
	/**
	 * @param pendingAmt the pendingAmt to set
	 */
	public void setPendingAmt(double pendingAmt) {
		this.pendingAmt = pendingAmt;
	}
	/**
	 * @return the orderPlacedDttm
	 */
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	/**
	 * @param orderPlacedDttm the orderPlacedDttm to set
	 */
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	

}
