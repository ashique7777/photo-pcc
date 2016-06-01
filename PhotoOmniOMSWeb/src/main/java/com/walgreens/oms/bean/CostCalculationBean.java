package com.walgreens.oms.bean;


/**
 * @author 
 *
 */
public class CostCalculationBean { 
	
	
	
	private int productId;
	private int wastedQty;
	private int quantity;
	private double originalLinePrice;
	private double cost;
	private String costType;
	private double shippingCost;
	private String shippingCostType;
	private double additionalCost;
	private double bindingCostVendor;
	private int printedQty;
	private double developmentCost;
	private double bindingCostInstore;
	private double printCost;
	private double additionalCostVendorProduct;
	private double dfltMacCostPercent;
	private long sysOrderId;
	private int sysProductId;
	private String ordPlcedDttm;

	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getWastedQty() {
		return wastedQty;
	}
	public void setWastedQty(int wastedQty) {
		this.wastedQty = wastedQty;
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
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public String getCostType() {
		return costType;
	}
	public void setCostType(String costType) {
		this.costType = costType;
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
	public double getAdditionalCost() {
		return additionalCost;
	}
	public void setAdditionalCost(double additionalCost) {
		this.additionalCost = additionalCost;
	}
	public int getPrintedQty() {
		return printedQty;
	}
	public void setPrintedQty(int printedQty) {
		this.printedQty = printedQty;
	}
	public double getDevelopmentCost() {
		return developmentCost;
	}
	public void setDevelopmentCost(double developmentCost) {
		this.developmentCost = developmentCost;
	}
	public double getPrintCost() {
		return printCost;
	}
	public void setPrintCost(double printCost) {
		this.printCost = printCost;
	}
	public double getAdditionalCostVendorProduct() {
		return additionalCostVendorProduct;
	}
	public void setAdditionalCostVendorProduct(double additionalCostVendorProduct) {
		this.additionalCostVendorProduct = additionalCostVendorProduct;
	}
	public double getDfltMacCostPercent() {
		return dfltMacCostPercent;
	}
	public void setDfltMacCostPercent(double dfltMacCostPercent) {
		this.dfltMacCostPercent = dfltMacCostPercent;
	}
	public long getSysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(int sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public long getSysProductId() {
		return sysProductId;
	}
	public void setSysProductId(int sysProductId) {
		this.sysProductId = sysProductId;
	}
	public double getBindingCostVendor() {
		return bindingCostVendor;
	}
	public void setBindingCostVendor(double bindingCostVendor) {
		this.bindingCostVendor = bindingCostVendor;
	}
	public double getBindingCostInstore() {
		return bindingCostInstore;
	}
	public void setBindingCostInstore(double bindingCostInstore) {
		this.bindingCostInstore = bindingCostInstore;
	}
	public String getOrdPlcedDttm() {
		return ordPlcedDttm;
	}
	public void setOrdPlcedDttm(String ordPlcedDttm) {
		this.ordPlcedDttm = ordPlcedDttm;
	}
	
	

}
