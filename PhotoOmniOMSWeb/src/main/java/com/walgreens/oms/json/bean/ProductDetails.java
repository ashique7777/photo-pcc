package com.walgreens.oms.json.bean;

public class ProductDetails {
	
	private int serialNumber;
	private String productDescription;
	private String wic;
	private String upc;
	private String quantity;
	private double vendorItemCost;
	private int totalRecord;
	
	
	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * @return the wic
	 */
	public String getWic() {
		return wic;
	}
	/**
	 * @param wic the wic to set
	 */
	public void setWic(String wic) {
		this.wic = wic;
	}
	/**
	 * @return the upc
	 */
	public String getUpc() {
		return upc;
	}
	/**
	 * @param upc the upc to set
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the vendorItemCost
	 */
	public double getVendorItemCost() {
		return vendorItemCost;
	}
	/**
	 * @param vendorItemCost the vendorItemCost to set
	 */
	public void setVendorItemCost(double vendorItemCost) {
		this.vendorItemCost = vendorItemCost;
	}
	
	

}
