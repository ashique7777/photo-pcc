package com.walgreens.oms.bean;


/**
 * This class is used as a bean to store PmMarketBasketTransfer information
 * @author Cognizant
 *
 */
public class PmMarketBasketTransferBean {
	
	private int sysOrderPmId;
	private  int orderId;
	private int pmId;
	private String employeeId;
	private int potentialQty;
	private int pendingQty;
	private double earnedAmount;
	private double potentialAmount;
	private double pendingAmount;
	//private Date orderPlacedDttm;
	private String pmStatus;
	private int activeInd;
	//private String createUserId;
	//private Date createDttm;
	//private String updateUserId;
	//private Date updatedDttm;
	//private long productId;
	private int earnedQty;
	private int shoppingCartId;
	private boolean insrtOrderPmInd;
	private boolean deleteInd;
	private boolean updateOrderPMInd;
	private boolean updateShoppingCartInd;
	
	/**
	 * This method fetches sysOrderPmId
	 * @return sysOrderPmId
	 */
	public int getSysOrderPmId() {
		return sysOrderPmId;
	}
	
	/**
	 * This method sets sysOrderPmId
	 * @param sysOrderPmId
	 */
	public void setSysOrderPmId(int sysOrderPmId) {
		this.sysOrderPmId = sysOrderPmId;
	}
	
	/**
	 * This method fetches orderId
	 * @return orderId
	 */
	public int getOrderId() {
		return orderId;
	}
	
	/**This method sets orderId
	 * @param orderId
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * This method fetches pmId
	 * @return pmId
	 */
	public int getPmId() {
		return pmId;
	}
	
	/**
	 * This method sets pmId
	 * @param pmId
	 */
	public void setPmId(int pmId) {
		this.pmId = pmId;
	}
	
	/**
	 * This method fetches employeeId
	 * @return employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	
	/**
	 * This method sets employeeId
	 * @param employeeId
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	/**
	 * This method fetches potentialQty
	 * @return potentialQty
	 */
	public int getPotentialQty() {
		return potentialQty;
	}
	
	/**
	 * This method sets potentialQty
	 * @param potentialQty
	 */
	public void setPotentialQty(int potentialQty) {
		this.potentialQty = potentialQty;
	}
	
	/**
	 * This method fetches pendingQty
	 * @return pendingQty
	 */
	public int getPendingQty() {
		return pendingQty;
	}
	
	/**
	 * This method sets pendingQty
	 * @param pendingQty
	 */
	public void setPendingQty(int pendingQty) {
		this.pendingQty = pendingQty;
	}
	
	/**
	 * This method fetches potentialAmount
	 * @return potentialAmount
	 */
	public double getPotentialAmount() {
		return potentialAmount;
	}
	
	/**
	 * This method sets potentialAmount
	 * @param potentialAmount
	 */
	public void setPotentialAmount(double potentialAmount) {
		this.potentialAmount = potentialAmount;
	}
	
	/**
	 * This method fetches pendingAmount
	 * @return pendingAmount
	 */
	public double getPendingAmount() {
		return pendingAmount;
	}
	
	/**
	 * This method sets potentialAmt
	 * @param potentialAmt
	 */
	public void setPendingAmount(double potentialAmt) {
		this.pendingAmount = potentialAmt;
	} 
	
	/**
	 * This method fetches orderPlacedDttm
	 * @return orderPlacedDttm
	 */
	/*public Date getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	
	*//**
	 * This method sets orderPlacedDttm
	 * @param orderPlacedDttm
	 *//*
	public void setOrderPlacedDttm(Date orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}*/
	
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
	 * This method fetches earnedAmount
	 * @return earnedAmount
	 */
	public double getEarnedAmount() {
		return earnedAmount;
	}
	
	/**
	 * This method sets earnedAmount
	 * @param earnedAmount
	 */
	public void setEarnedAmount(double earnedAmount) {
		this.earnedAmount = earnedAmount;
	}
	
	/**
	 * This method fetches activeInd
	 * @return activeInd
	 */
	public int getActiveInd() {
		return activeInd;
	}
	
	/**
	 * This method sets activeInd
	 * @param activeInd
	 */
	public void setActiveInd(int activeInd) {
		this.activeInd = activeInd;
	}
	
	/**
	 * This method fetches createUserId
	 * @return
	 */
	/*public String getCreateUserId() {
		return createUserId;
	}
	
	*//**
	 * This method sets createUserId
	 * @param createUserId
	 *//*
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
	*//**
	 * This method fetches createDttm
	 * @return createDttm
	 *//*
	public Date getCreateDttm() {
		return createDttm;
	}
	
	*//**
	 * This method sets createDttm
	 * @param createDttm
	 *//*
	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}
	
	*//**
	 * This method fetches updateUserId
	 * @return updateUserId
	 *//*
	public String getUpdateUserId() {
		return updateUserId;
	}
	
	*//**
	 * This method sets updateUserId
	 * @param updateUserId
	 *//*
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	*//**
	 * This method fetches getUpdatedDttm
	 * @return getUpdatedDttm
	 *//*
	public Date getUpdatedDttm() {
		return updatedDttm;
	}
	
	*//**
	 * This method sets updateDttm
	 * @param updatedDttm
	 *//*
	public void setUpdatedDttm(Date updatedDttm) {
		this.updatedDttm = updatedDttm;
	}
	
	*//**
	 * This method fetches productId
	 * @return productId
	 *//*
	public long getProductId() {
		return productId;
	}
	
	*//**This method sets productId
	 * @param productId
	 *//*
	public void setProductId(long productId) {
		this.productId = productId;
	}
	*/
	/**
	 * This method fetches earnedQty
	 * @return earnedQty
	 */
	public int getEarnedQty() {
		return earnedQty;
	}
	
	/**
	 * This method sets earnedQty
	 * @param earnedQty
	 */
	public void setEarnedQty(int earnedQty) {
		this.earnedQty = earnedQty;
	}
	
	/**
	 * This method fetches shoppingCartId
	 * @return shoppingCartId
	 */
	public int getShoppingCartId() {
		return shoppingCartId;
	}
	
	/**
	 * This method sets shoppingCartId
	 * @param shoppingCartId
	 */
	public void setShoppingCartId(int shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	
	/**
	 * This method fetches deleteInd
	 * @return deleteInd
	 */
	public boolean isDeleteInd() {
		return deleteInd;
	}
	
	/**
	 * This method sets deleteInd
	 * @param deleteInd
	 */
	public void setDeleteInd(boolean deleteInd) {
		this.deleteInd = deleteInd;
	}
	
	
	/**
	 * This method fetches updateOrderPMInd
	 * @return
	 */
	public boolean isUpdateOrderPMInd() {
		return updateOrderPMInd;
	}

	/**
	 * This method sets deleteInd
	 * @param updateOrderPMInd
	 */
	public void setUpdateOrderPMInd(boolean updateOrderPMInd) {
		this.updateOrderPMInd = updateOrderPMInd;
	}

	/**
	 * This method fetches updateOrderPMInd
	 * @return
	 */
	public boolean updateShoppingCartInd() {
		return updateShoppingCartInd;
	}

	/**
	 * This method sets updateShoppingCartInd
	 * @param updateShoppingCartInd
	 */
	public void setUpdateShoppingCartInd(boolean updateShoppingCartInd) {
		this.updateShoppingCartInd = updateShoppingCartInd;
	}

	/**
	 * This method fetches insrtOrderPmInd
	 * @return
	 */
	public boolean isInsrtOrderPmInd() {
		return insrtOrderPmInd;
	}

	/**
	 * This method sets insrtOrderPmInd
	 * @param insrtOrderPmInd
	 */
	public void setInsrtOrderPmInd(boolean insrtOrderPmInd) {
		this.insrtOrderPmInd = insrtOrderPmInd;
	}

	/**
	 * This method sets updateShoppingCartInd
	 * @return
	 */
	public boolean isUpdateShoppingCartInd() {
		return updateShoppingCartInd;
	}
	
}
