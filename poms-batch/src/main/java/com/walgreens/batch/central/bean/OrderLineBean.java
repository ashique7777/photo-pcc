package com.walgreens.batch.central.bean;

import java.sql.Date;
import java.util.List;


/**
 * @author CTS
 *
 */
public class OrderLineBean {

	private long sysOrderLineId;
	private long orderId;
	private long productId;
	private long machineId;
	private double unitPrice;
	private int quantity;      // sellable qty
	private int originalQnty;  // origiunal order qty
	private double originalLinePrice;
	private double finalPrice;
	private double discountAmt;
	private double loyaltyPrice;
	private double loyaltyDiscountAmt;
	private double cost;
	private double fullfillmentVendorCost;
	private double lineSoldAmt;
	private String orderPlacedDttm;
	private String createUserId;
	private Date createdDttm;
	private String updateUserId;
	private Date updatedDttm;
	private long EquipmentTypeId;
	private long sysOrderId;
	
	private List<OmPromotionalMoneyBean> omPromotionalMoneyBeanList;
	private boolean isMBPMEligibleForDate = false;

	public long getEquipmentTypeId() {
		return EquipmentTypeId;
	}

	public void setEquipmentTypeId(long equipmentTypeId) {
		EquipmentTypeId = equipmentTypeId;
	}

	public long getSysOrderLineId() {
		return sysOrderLineId;
	}

	public void setSysOrderLineId(long sysOrderLineId) {
		this.sysOrderLineId = sysOrderLineId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getMachineId() {
		return machineId;
	}

	public double getOriginalLinePrice() {
		return originalLinePrice;
	}

	public void setOriginalLinePrice(double originalLinePrice) {
		this.originalLinePrice = originalLinePrice;
	}

	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOriginalQnty() {
		return originalQnty;
	}

	public void setOriginalQnty(int originalQnty) {
		this.originalQnty = originalQnty;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public double getDiscountAmt() {
		return discountAmt;
	}

	public void setDiscountAmt(double discountAmt) {
		this.discountAmt = discountAmt;
	}

	public double getLoyaltyPrice() {
		return loyaltyPrice;
	}

	public void setLoyaltyPrice(double loyaltyPrice) {
		this.loyaltyPrice = loyaltyPrice;
	}

	public double getLoyaltyDiscountAmt() {
		return loyaltyDiscountAmt;
	}

	public void setLoyaltyDiscountAmt(double loyaltyDiscountAmt) {
		this.loyaltyDiscountAmt = loyaltyDiscountAmt;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getFullfillmentVendorCost() {
		return fullfillmentVendorCost;
	}

	public void setFullfillmentVendorCost(double fullfillmentVendorCost) {
		this.fullfillmentVendorCost = fullfillmentVendorCost;
	}

	public double getLineSoldAmt() {
		return lineSoldAmt;
	}

	public void setLineSoldAmt(double lineSoldAmt) {
		this.lineSoldAmt = lineSoldAmt;
	}

	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public Date getCreatedDttm() {
		return createdDttm;
	}

	public void setCreatedDttm(Date createdDttm) {
		this.createdDttm = createdDttm;
	}

	public Date getUpdatedDttm() {
		return updatedDttm;
	}

	public void setUpdatedDttm(Date updatedDttm) {
		this.updatedDttm = updatedDttm;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}	

	public long getSysOrderId() {
		return sysOrderId;
	}

	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	public List<OmPromotionalMoneyBean> getOmPromotionalMoneyBeanList() {
		return omPromotionalMoneyBeanList;
	}

	public void setOmPromotionalMoneyBeanList(
			List<OmPromotionalMoneyBean> omPromotionalMoneyBeanList) {
		this.omPromotionalMoneyBeanList = omPromotionalMoneyBeanList;
	}

	public boolean isMBPMEligibleForDate() {
		return isMBPMEligibleForDate;
	}

	public void setMBPMEligibleForDate(boolean isMBPMEligibleForDate) {
		this.isMBPMEligibleForDate = isMBPMEligibleForDate;
	}

	

	
}
