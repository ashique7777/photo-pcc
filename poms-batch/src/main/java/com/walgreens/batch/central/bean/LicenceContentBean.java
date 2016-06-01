/**
 * 
 */
package com.walgreens.batch.central.bean;

/**
 * @author CTS
 *
 */
public class LicenceContentBean {
	
	
	/**
	 * Store Order date
	 */
	private String orderDate;
	
	/**
	 * Stores no of quantity of product in an order  
	 */
	private String quantity;
	
	/**
	 * Stores provider name
	 */
	private String provider;
	/**
	 * Stores type of location e.g store, Chain
	 */
	private String locationType;
	/**
	 * Stores unique number of a location
	 */
	private String locationNumber;
	/**
	 * Stores original Retail Amount
	 */
	private String retail;
	/**
	 * Stores Discount amount applied on retail
	 */
	private String discountApplied;
	/**
	 * Stores Employee Discount amount applied on retail
	 */
	private String employeeDiscount;
	/**
	 * Stores net sale
	 */
	private String netSale;
	/**
	 * Stores unique product id
	 */
	private String productID;
	/**
	 * Stores unique UPC no
	 */
	private String UPC;
	/**
	 * Stores unique WIC no
	 */
	private String WIC;
	/**
	 * Stores  product description
	 */
	private String productDescription;
	/**
	 * Stores unique template id
	 */
	private String templateID;
	/**
	 * Stores unique envelope number of particular order
	 */
	private String envelopeNumber;
	/**
	 * @return the orderDate
	 */
	public String getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
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
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}
	/**
	 * @return the locationType
	 */
	public String getLocationType() {
		return locationType;
	}
	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	/**
	 * @return the locationNumber
	 */
	public String getLocationNumber() {
		return locationNumber;
	}
	/**
	 * @param locationNumber the locationNumber to set
	 */
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber;
	}
	/**
	 * @return the retail
	 */
	public String getRetail() {
		return retail;
	}
	/**
	 * @param retail the retail to set
	 */
	public void setRetail(String retail) {
		this.retail = retail;
	}
	/**
	 * @return the discountApplied
	 */
	public String getDiscountApplied() {
		return discountApplied;
	}
	/**
	 * @param discountApplied the discountApplied to set
	 */
	public void setDiscountApplied(String discountApplied) {
		this.discountApplied = discountApplied;
	}
	/**
	 * @return the employeeDiscount
	 */
	public String getEmployeeDiscount() {
		return employeeDiscount;
	}
	/**
	 * @param employeeDiscount the employeeDiscount to set
	 */
	public void setEmployeeDiscount(String employeeDiscount) {
		this.employeeDiscount = employeeDiscount;
	}
	/**
	 * @return the netSale
	 */
	public String getNetSale() {
		return netSale;
	}
	/**
	 * @param netSale the netSale to set
	 */
	public void setNetSale(String netSale) {
		this.netSale = netSale;
	}
	/**
	 * @return the productID
	 */
	public String getProductID() {
		return productID;
	}
	/**
	 * @param productID the productID to set
	 */
	public void setProductID(String productID) {
		this.productID = productID;
	}
	/**
	 * @return the uPC
	 */
	public String getUPC() {
		return UPC;
	}
	/**
	 * @param uPC the uPC to set
	 */
	public void setUPC(String uPC) {
		UPC = uPC;
	}
	/**
	 * @return the wIC
	 */
	public String getWIC() {
		return WIC;
	}
	/**
	 * @param wIC the wIC to set
	 */
	public void setWIC(String wIC) {
		WIC = wIC;
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
	 * @return the templateID
	 */
	public String getTemplateID() {
		return templateID;
	}
	/**
	 * @param templateID the templateID to set
	 */
	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}
	/**
	 * @return the envelopeNumber
	 */
	public String getEnvelopeNumber() {
		return envelopeNumber;
	}
	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	public void setEnvelopeNumber(String envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LicenceContentBean [orderDate=" + orderDate + ", quantity="
				+ quantity + ", provider=" + provider + ", locationType="
				+ locationType + ", locationNumber=" + locationNumber
				+ ", retail=" + retail + ", discountApplied=" + discountApplied
				+ ", employeeDiscount=" + employeeDiscount + ", netSale="
				+ netSale + ", productID=" + productID + ", UPC=" + UPC
				+ ", WIC=" + WIC + ", productDescription=" + productDescription
				+ ", templateID=" + templateID + ", envelopeNumber="
				+ envelopeNumber + "]";
	}

}
