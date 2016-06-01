/**
 * 
 */
package com.walgreens.oms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CTS
 * 
 */
public class PMOrderDetailBean implements Serializable {

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
	private long locationId;
	private String orderPlacedDTTM;
	private String tookEmpId;
	private long empId;
	private boolean updateInd;
	private String orderType;
	private String employeeId;
	private int pmEligibleCd;
	private boolean expenseInd;
	private List<PMOrderLineBean> itemList = new ArrayList<PMOrderLineBean>();
	private List<OrderPMDataBean> orderPmList = new ArrayList<OrderPMDataBean>();

	/**
	 * @return the orderPmList
	 */
	public List<OrderPMDataBean> getOrderPmList() {
		return orderPmList;
	}

	/**
	 * @param orderPmList
	 *            the orderPmList to set
	 */
	public void setOrderPmList(List<OrderPMDataBean> orderPmList) {
		this.orderPmList = orderPmList;
	}

	/**
	 * @return the pmEligibleCd
	 */
	public int getPmEligibleCd() {
		return pmEligibleCd;
	}

	/**
	 * @param pmEligibleCd
	 *            the pmEligibleCd to set
	 */
	public void setPmEligibleCd(int pmEligibleCd) {
		this.pmEligibleCd = pmEligibleCd;
	}

	/**
	 * @return the empId
	 */
	public long getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 *            the empId to set
	 */
	public void setEmpId(long empId) {
		this.empId = empId;
	}

	/**
	 * @return the itemList
	 */
	public List<PMOrderLineBean> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList
	 *            the itemList to set
	 */
	public void setItemList(List<PMOrderLineBean> itemList) {
		this.itemList = itemList;
	}

	/**
	 * @return the updateInd
	 */
	public boolean isUpdateInd() {
		return updateInd;
	}

	/**
	 * @param updateInd
	 *            the updateInd to set
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
	 * @param finalPrice
	 *            the finalPrice to set
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
	 * @param soldAmount
	 *            the soldAmount to set
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
	 * @param couponInd
	 *            the couponInd to set
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
	 * @param discntCardInd
	 *            the discntCardInd to set
	 */
	public void setDiscntCardInd(int discntCardInd) {
		this.discntCardInd = discntCardInd;
	}

	/**
	 * @return the orderPlacedDTTM
	 */
	public String getOrderPlacedDTTM() {
		return orderPlacedDTTM;
	}

	/**
	 * @param orderPlacedDTTM
	 *            the orderPlacedDTTM to set
	 */
	public void setOrderPlacedDTTM(String orderPlacedDTTM) {
		this.orderPlacedDTTM = orderPlacedDTTM;
	}

	/**
	 * @return the orderId
	 */
	public long getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
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
	 * @param tookEmpId
	 *            the tookEmpId to set
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
	 * @param orderType
	 *            the orderType to set
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
	 * @param orderStatus
	 *            the orderStatus to set
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
	 * @param pmStatus
	 *            the pmStatus to set
	 */
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
	}

	/**
	 * @return the employeeID
	 */
	public String getEmployeeID() {
		return employeeId;
	}

	/**
	 * @param employeeID
	 *            the employeeID to set
	 */
	public void setEmployeeID(String employeeID) {
		this.employeeId = employeeID;
	}

	/**
	 * @return the locationId
	 */
	public long getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId
	 *            the locationId to set
	 */
	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the expenseInd
	 */
	public boolean isExpenseInd() {
		return expenseInd;
	}

	/**
	 * @param expenseInd
	 *            the expenseInd to set
	 */
	public void setExpenseInd(boolean expenseInd) {
		this.expenseInd = expenseInd;
	}

}
