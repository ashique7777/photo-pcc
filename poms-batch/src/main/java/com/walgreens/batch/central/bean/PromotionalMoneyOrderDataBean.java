/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import com.walgreens.batch.central.bean.User; 

/**
 * @author CTS
 *
 */
public class PromotionalMoneyOrderDataBean implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long orderId;
	private String orderStatus;
	private String pmStatus;
	private double finalPrice;
	private double soldAmount;
	private int couponInd;
	private int discntCardInd;
	private long locNumber;
	private Timestamp orderPlacedDTTM;
	private String modifiedOderPlcdDttm;
	private String orderOriginType;
	private int ExceptionInd;
	private int expenseInd ;
	
	private String tookEmpId;
	private long empId;
	private List<ItemDataBean> itemList = new ArrayList<ItemDataBean> ();
	private User user = new User();
	private boolean updateInd;
	
	private String orderType;
	private boolean isPMEligible = false;
	
	
		
	/**
	 * @return the isPMEligible
	 */
	public boolean isPMEligible() {
		return isPMEligible;
	}
	/**
	 * @param isPMEligible the isPMEligible to set
	 */
	public void setPMEligible(boolean isPMEligible) {
		this.isPMEligible = isPMEligible;
	}
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the modifiedOderPlcdDttm
	 */
	public String getModifiedOderPlcdDttm() {
		return modifiedOderPlcdDttm;
	}
	/**
	 * @param modifiedOderPlcdDttm the modifiedOderPlcdDttm to set
	 */
	public void setModifiedOderPlcdDttm(String modifiedOderPlcdDttm) {
		this.modifiedOderPlcdDttm = modifiedOderPlcdDttm;
	}
	/**
	 * @return the locNumber
	 */
	public long getLocNumber() {
		return locNumber;
	}
	/**
	 * @param locNumber the locNumber to set
	 */
	public void setLocNumber(long locNumber) {
		this.locNumber = locNumber;
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
	 * @return the orderOriginType
	 */
	public String getOrderOriginType() {
		return orderOriginType;
	}
	/**
	 * @param orderOriginType the orderOriginType to set
	 */
	public void setOrderOriginType(String orderOriginType) {
		this.orderOriginType = orderOriginType;
	}
	
	/**
	 * @return the itemList
	 */
	public List<ItemDataBean> getItemList() {
		return itemList;
	}
	/**
	 * @param itemList the itemList to set
	 */
	public void setItemList(List<ItemDataBean> itemList) {
		this.itemList = itemList;
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
	 * @return the finalPrice
	 */
	public double getFinalPrice() {
		return finalPrice;
	}
	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}
	/**
	 * @return the soldAmount
	 */
	public double getSoldAmount() {
		return soldAmount;
	}
	/**
	 * @param soldAmount the soldAmount to set
	 */
	public void setSoldAmount(double soldAmount) {
		this.soldAmount = soldAmount;
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
	 * @return the discntCardInd
	 */
	public int getDiscntCardInd() {
		return discntCardInd;
	}
	/**
	 * @param discntCardInd the discntCardInd to set
	 */
	public void setDiscntCardInd(int discntCardInd) {
		this.discntCardInd = discntCardInd;
	}
	/**
	 * @return the exceptionInd
	 */
	public int getExceptionInd() {
		return ExceptionInd;
	}
	/**
	 * @param exceptionInd the exceptionInd to set
	 */
	public void setExceptionInd(int exceptionInd) {
		ExceptionInd = exceptionInd;
	}
	/**
	 * @return the expenseInd
	 */
	public int getExpenseInd() {
		return expenseInd;
	}
	/**
	 * @param expenseInd the expenseInd to set
	 */
	public void setExpenseInd(int expenseInd) {
		this.expenseInd = expenseInd;
	}
	
		
	/**
	 * @return the orderPlacedDTTM
	 */
	public Timestamp getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}
	/**
	 * @param orderPlacedDTTM the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(Timestamp orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
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
	 * @return the tookEmpId
	 */
	public String getTookEmpId() {
		return tookEmpId;
	}
	/**
	 * @param tookEmpId the tookEmpId to set
	 */
	public void setTookEmpId(String tookEmpId) {
		this.tookEmpId = tookEmpId;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
	
	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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
	
	

}
