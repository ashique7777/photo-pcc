package com.walgreens.oms.bean;

public class OrderItemLineBean { 
	
	private int sysProductId;
	private int printedQty;
	private int quantity;
	private double originalLinePrice;
	private int wastedQty;
	private int sysMachineInstanceId;
	private int sysEquipmentInstanceId;
	private int sysEquipmentTypeId;
	private  long orderLineId;
	private double dfltMacCostPercent;
	private double printCost;
	private double additionalCost;
	private double bindingCostVendor;
	private double developmentCost;
	private double bindingCostInstore;
	private String orderPlacedDttm;
	private String noOfInputs;
	private String  panaromicPrints; 
	
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
		
	}
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	public int getSysEquipmentTypeId() {
		return sysEquipmentTypeId;
	}
	public void setSysEquipmentTypeId(int sysEquipmentTypeId) {
		this.sysEquipmentTypeId = sysEquipmentTypeId;
	}
	private int sysVendorId;
	private String costType;
	private double shippingCost;
	private String shippingCostType;
	private double cost;
	private double fulfillmentVendorCost;
	private long sysOrderId;
	
	
	public long getSysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public double getFulfillmentVendorCost() {
		return fulfillmentVendorCost;
	}
	public void setFulfillmentVendorCost(double fulfillmentVendorCost) {
		this.fulfillmentVendorCost = fulfillmentVendorCost;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(double shippingCost) {
		this.shippingCost = shippingCost;
	}
	public String getShippingCostType() {
		return shippingCostType;
	}
	public void setShippingCostType(String shippingCostType) {
		this.shippingCostType = shippingCostType;
	}
	public int getSysVendorId() {
		return sysVendorId;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
	}
	public void setSysVendorId(int sysVendorId) {
		this.sysVendorId = sysVendorId;
	}
	public double getDfltMacCostPercent() {
		return dfltMacCostPercent;
	}
	public void setDfltMacCostPercent(double dfltMacCostPercent) {
		this.dfltMacCostPercent = dfltMacCostPercent;
	}
	public double getPrintCost() {
		return printCost;
	}
	public void setPrintCost(double printCost) {
		this.printCost = printCost;
	}
	public double getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}
	public double getBindingCostVendor() {
		return bindingCostVendor;
	}
	public void setBindingCostVendor(double bindingCostVendor) {
		this.bindingCostVendor = bindingCostVendor;
	}
	public double getDevelopmentCost() {
		return developmentCost;
	}
	public void setDevelopmentCost(double developmentCost) {
		this.developmentCost = developmentCost;
	}
	public double getBindingCostInstore() {
		return bindingCostInstore;
	}
	public void setBindingCostInstore(double bindingCostInstore) {
		this.bindingCostInstore = bindingCostInstore;
	}
	public int getSysProductId() {
		return sysProductId;
	}
	public void setSysProductId(int sysProductId) {
		this.sysProductId = sysProductId;
	}
	public int getPrintedQty() {
		return printedQty;
	}
	public void setPrintedQty(int printedQty) {
		this.printedQty = printedQty;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getOriginalLinePrice() {
		return originalLinePrice;
	}
	public void setOriginalLinePrice(double originalLinePrice) {
		this.originalLinePrice = originalLinePrice;
	}
	public int getWastedQty() {
		return wastedQty;
	}
	public void setWastedQty(int wastedQty) {
		this.wastedQty = wastedQty;
	}
	public int getSysMachineInstanceId() {
		return sysMachineInstanceId;
	}
	public void setSysMachineInstanceId(int sysMachineInstanceId) {
		this.sysMachineInstanceId = sysMachineInstanceId;
	}
	public int getSysEquipmentInstanceId() {
		return sysEquipmentInstanceId;
	}
	public void setSysEquipmentInstanceId(int sysEquipmentInstanceId) {
		this.sysEquipmentInstanceId = sysEquipmentInstanceId;
	}
	public long getOrderLineId() {
		return orderLineId;
	}
	public void setOrderLineId(long orderLineId) {
		this.orderLineId = orderLineId;
	}
	public String getNoOfInputs() {
		return noOfInputs;
	}
	public void setNoOfInputs(String noOfInputs) {
		this.noOfInputs = noOfInputs;
	}
	public String getPanaromicPrints() {
		return panaromicPrints;
	}
	public void setPanaromicPrints(String panaromicPrints) {
		this.panaromicPrints = panaromicPrints;
	}
	
	

}
