package com.walgreens.oms.bean;


/**
 * @author CTS
 * 
 */

public class OrderLineBean {

	private long sysOrderLineId;
	private long SysProductId;
	private long orderId;
	private long productId;
	private long machineId;
	private double unitPrice;
	private int quantity;
	private int originalQnty;
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
	private String createdDttm;
	private String updateUserId;
	private String updatedDttm;
	private long equipmentTypeId;
	
	

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
	 * @return the createdDttm
	 */
	public String getCreatedDttm() {
		return createdDttm;
	}

	/**
	 * @param createdDttm the createdDttm to set
	 */
	public void setCreatedDttm(String createdDttm) {
		this.createdDttm = createdDttm;
	}

	/**
	 * @return the updatedDttm
	 */
	public String getUpdatedDttm() {
		return updatedDttm;
	}

	/**
	 * @param updatedDttm the updatedDttm to set
	 */
	public void setUpdatedDttm(String updatedDttm) {
		this.updatedDttm = updatedDttm;
	}

	public long getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(long equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
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
	
	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}

	public double getOriginalLinePrice() {
		return originalLinePrice;
	}

	public void setOriginalLinePrice(double originalLinePrice) {
		this.originalLinePrice = originalLinePrice;
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
	public long getSysProductId() {
		return SysProductId;
	}

	public void setSysProductId(long sysProductId) {
		SysProductId = sysProductId;
	}

	@Override
	public String toString() {
		return "OrderLineBean [sysOrderLineId=" + sysOrderLineId + ", orderId="
				+ orderId + ", productId=" + productId + ", machineId="
				+ machineId + ", unitPrice=" + unitPrice + ", quantity="
				+ quantity + ", originalQnty=" + originalQnty
				+ ", originalLinePrice=" + originalLinePrice + ", finalPrice="
				+ finalPrice + ", discountAmt=" + discountAmt
				+ ", loyaltyPrice=" + loyaltyPrice + ", loyaltyDiscountAmt="
				+ loyaltyDiscountAmt + ", cost=" + cost
				+ ", fullfillmentVendorCost=" + fullfillmentVendorCost
				+ ", lineSoldAmt=" + lineSoldAmt + ", orderPlacedDttm="
				+ orderPlacedDttm + ", createUserId=" + createUserId
				+ ", createdDttm=" + createdDttm + ", updateUserId="
				+ updateUserId + ", updatedDttm=" + updatedDttm
				+ ", EquipmentTypeId=" + equipmentTypeId + "]";
	}

}
