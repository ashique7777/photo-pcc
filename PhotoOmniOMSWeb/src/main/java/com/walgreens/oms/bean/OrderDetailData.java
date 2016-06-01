package com.walgreens.oms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Cognizant
 * 
 */
public class OrderDetailData {
	
	private long shoppingCartId; 
	private int lbldEmployeeId; 
	private String orderPlacedDttm; 
	private float soldAmount; 
	private int expenseInd; 
	private String pmStatus; 
	private  boolean pmEligible; 
	private String employeeId; 
	private int sysOrderId; 
	private int couponInd; 
	private int discountCardUsedInd; 
	private String status; 
	private float finalPrice; 
	private int owningLocationID;
	private boolean updateShoppingCart;
	private List<OmPromotionalMoneyBean> omPromotionalMoneyBeanList;
	private boolean updateOrderPm;
	private boolean insertOrderPm;
	private int orderPMActive_CD;
	private boolean isValidProration = true;
	private boolean isKiosk;
	private long empId;
	private PMRule pmRule;
    private List<OrderPMDataBean> orderPmList = new ArrayList<OrderPMDataBean> ();
    
	
	
	/**
	 * @return the isKiosk
	 */
	public boolean isKiosk() {
		return isKiosk;
	}
	/**
	 * @param isKiosk the isKiosk to set
	 */
	public void setKiosk(boolean isKiosk) {
		this.isKiosk = isKiosk;
	}
	/**
	 * @return the orderPmList
	 */
	public List<OrderPMDataBean> getOrderPmList() {
		return orderPmList;
	}
	/**
	 * @param orderPmList the orderPmList to set
	 */
	public void setOrderPmList(List<OrderPMDataBean> orderPmList) {
		this.orderPmList = orderPmList;
	}
	
	/**
	 * @return the pmRule
	 */
	public PMRule getPmRule() {
		return pmRule;
	}
	/**
	 * @param pmRule the pmRule to set
	 */
	public void setPmRule(PMRule pmRule) {
		this.pmRule = pmRule;
	}
	/**
	 * @return the pmEligible
	 */
	public boolean isPmEligible() {
		return pmEligible;
	}
	/**
	 * @param pmEligible the pmEligible to set
	 */
	public void setPmEligible(boolean pmEligible) {
		this.pmEligible = pmEligible;
	}
	
	public int getExpenseInd() {
		return expenseInd;
	}
	public void setExpenseInd(int expenseInd) {
		this.expenseInd = expenseInd;
	}
	/**
	 * @return the shoppingCartId
	 */
	public long getShoppingCartId() {
		return shoppingCartId;
	}
	/**
	 * @param shoppingCartId the shoppingCartId to set
	 */
	public void setShoppingCartId(long shoppingCartId) {
		this.shoppingCartId = shoppingCartId;
	}
	/**
	 * @return the lbldEmployeeId
	 */
	public int getLbldEmployeeId() {
		return lbldEmployeeId;
	}
	/**
	 * @param lbldEmployeeId the lbldEmployeeId to set
	 */
	public void setLbldEmployeeId(int lbldEmployeeId) {
		this.lbldEmployeeId = lbldEmployeeId;
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
	/**
	 * @return the soldAmount
	 */
	public float getSoldAmount() {
		return soldAmount;
	}
	/**
	 * @param soldAmount the soldAmount to set
	 */
	public void setSoldAmount(float soldAmount) {
		this.soldAmount = soldAmount;
	}
	
	/**
	 * @return the pmStatus
	 */
	public String getPmStatus() {
		return pmStatus;
	}
	/**
	 * @param pmStatus the pmStatus to set
	 */
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}
	
	
	
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the sysOrderId
	 */
	public int getSysOrderId() {
		return sysOrderId;
	}
	/**
	 * @param sysOrderId the sysOrderId to set
	 */
	public void setSysOrderId(int sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	/**
	 * @return the couponInd
	 */
	public int getCouponInd() {
		return couponInd;
	}
	/**
	 * @param couponInd the couponInd to set
	 */
	public void setCouponInd(int couponInd) {
		this.couponInd = couponInd;
	}
	/**
	 * @return the discountCardUsedInd
	 */
	public int getDiscountCardUsedInd() {
		return discountCardUsedInd;
	}
	/**
	 * @param discountCardUsedInd the discountCardUsedInd to set
	 */
	public void setDiscountCardUsedInd(int discountCardUsedInd) {
		this.discountCardUsedInd = discountCardUsedInd;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * @return the finalPrice
	 */
	public float getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * @return the owningLocationID
	 */
	public int getOwningLocationID() {
		return owningLocationID;
	}
	/**
	 * @param owningLocationID the owningLocationID to set
	 */
	public void setOwningLocationID(int owningLocationID) {
		this.owningLocationID = owningLocationID;
	}
	/**
	 * @return the updateShoppingCart
	 */
	public boolean isUpdateShoppingCart() {
		return updateShoppingCart;
	}
	/**
	 * @param updateShoppingCart the updateShoppingCart to set
	 */
	public void setUpdateShoppingCart(boolean updateShoppingCart) {
		this.updateShoppingCart = updateShoppingCart;
	}
	/**
	 * @return the omPromotionalMoneyBeanList
	 */
	public List<OmPromotionalMoneyBean> getOmPromotionalMoneyBeanList() {
		return omPromotionalMoneyBeanList;
	}
	/**
	 * @param omPromotionalMoneyBeanList the omPromotionalMoneyBeanList to set
	 */
	public void setOmPromotionalMoneyBeanList(
			List<OmPromotionalMoneyBean> omPromotionalMoneyBeanList) {
		this.omPromotionalMoneyBeanList = omPromotionalMoneyBeanList;
	}
	
	/**
	 * @return the updateOrderPm
	 */
	public boolean isUpdateOrderPm() {
		return updateOrderPm;
	}
	/**
	 * @param updateOrderPm the updateOrderPm to set
	 */
	public void setUpdateOrderPm(boolean updateOrderPm) {
		this.updateOrderPm = updateOrderPm;
	}
	/**
	 * @return the insertOrderPm
	 */
	public boolean isInsertOrderPm() {
		return insertOrderPm;
	}
	/**
	 * @param insertOrderPm the insertOrderPm to set
	 */
	public void setInsertOrderPm(boolean insertOrderPm) {
		this.insertOrderPm = insertOrderPm;
	}
	/**
	 * @return the orderPMActive_CD
	 */
	public int getOrderPMActive_CD() {
		return orderPMActive_CD;
	}
	/**
	 * @param orderPMActive_CD the orderPMActive_CD to set
	 */
	public void setOrderPMActive_CD(int orderPMActive_CD) {
		this.orderPMActive_CD = orderPMActive_CD;
	}
	/**
	 * @return the isValidProration
	 */
	public boolean isValidProration() {
		return isValidProration;
	}
	/**
	 * @param isValidProration the isValidProration to set
	 */
	public void setValidProration(boolean isValidProration) {
		this.isValidProration = isValidProration;
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
	
	
	

}
