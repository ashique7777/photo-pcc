/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 *
 */
public class AdhocAndDailyCSVFileReportDataBean {
	private Timestamp orderDate;
	private Long quantity;
	private String provider;
	private String locationType;
	private Long locationNumb;
	private Double calRetailPrice;
	private Double discountApplied;
	private String employeeDiscount;
	private Double netSale;
	private String productDescription;
	private String uPC;
	private String wIC;
	private String licenseContOrTempId;
	private String templateID;
	private Long envelopeNumber;
	private Double originalRetailPrice;
	private String orderStatus;
	
	/**
	 * @return the orderDate
	 */
	public Timestamp getOrderDate() {
		return orderDate;
	}
	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
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
	 * @return the locationNumb
	 */
	public Long getLocationNumb() {
		return locationNumb;
	}
	/**
	 * @param locationNumb the locationNumb to set
	 */
	public void setLocationNumb(Long locationNumb) {
		this.locationNumb = locationNumb;
	}
	/**
	 * @return the calRetailPrice
	 */
	public Double getCalRetailPrice() {
		return calRetailPrice;
	}
	/**
	 * @param calRetailPrice the calRetailPrice to set
	 */
	public void setCalRetailPrice(Double calRetailPrice) {
		this.calRetailPrice = calRetailPrice;
	}
	/**
	 * @return the discountApplied
	 */
	public Double getDiscountApplied() {
		return discountApplied;
	}
	/**
	 * @param discountApplied the discountApplied to set
	 */
	public void setDiscountApplied(Double discountApplied) {
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
	public Double getNetSale() {
		return netSale;
	}
	/**
	 * @param netSale the netSale to set
	 */
	public void setNetSale(Double netSale) {
		this.netSale = netSale;
	}
	/**
	 * @return the uPC
	 */
	public String getuPC() {
		return uPC;
	}
	/**
	 * @param uPC the uPC to set
	 */
	public void setuPC(String uPC) {
		this.uPC = uPC;
	}
	/**
	 * @return the wIC
	 */
	public String getwIC() {
		return wIC;
	}
	/**
	 * @param wIC the wIC to set
	 */
	public void setwIC(String wIC) {
		this.wIC = wIC;
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
	public Long getEnvelopeNumber() {
		return envelopeNumber;
	}
	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	public void setEnvelopeNumber(Long envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	/**
	 * @return the originalRetailPrice
	 */
	public Double getOriginalRetailPrice() {
		return originalRetailPrice;
	}
	/**
	 * @param originalRetailPrice the originalRetailPrice to set
	 */
	public void setOriginalRetailPrice(Double originalRetailPrice) {
		this.originalRetailPrice = originalRetailPrice;
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
	 * @return the licenseContOrTempId
	 */
	public String getLicenseContOrTempId() {
		return licenseContOrTempId;
	}
	/**
	 * @param licenseContOrTempId the licenseContOrTempId to set
	 */
	public void setLicenseContOrTempId(String licenseContOrTempId) {
		this.licenseContOrTempId = licenseContOrTempId;
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


}
