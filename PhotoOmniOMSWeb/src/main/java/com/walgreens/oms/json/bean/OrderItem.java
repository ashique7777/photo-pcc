package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;











import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walgreens.oms.bean.LicenseContent;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"totalNumberOfProductImages",
"pcpProductId",
"quantity",
"couponCode",
"numberOfSheets",
"pcpAttributes",
"equipmentId",
"originalRetail",
"discountAmt",
"calculatedRetail",
"wasteQty",
"wastePrintCost",
"loyaltyPrice",
"loyaltyDiscountAmt",
"productAttribute",
"template",
"licenseContent",
"pluDetails",
"printableSigns"
})

public class OrderItem {
	
	@JsonProperty("totalNumberOfProductImages")private String  totalNumberOfProductImages;
	@JsonProperty("pcpProductId")private String  pCPProductId;
	@JsonProperty("quantity")private String  quantity;
	@JsonProperty("couponCode")private String  couponCode;
	@JsonProperty("numberOfSheets")private String  numberOfSheets;
	@JsonProperty("pcpAttributes")private String  pCPAttributes;
	@JsonProperty("equipmentId")private String  equipmentId;
	@JsonProperty("originalRetail")private String  originalRetail;
	@JsonProperty("discountAmt")private String  discountAmt;
	@JsonProperty("calculatedRetail")private String  calculatedRetail;
	
	@JsonProperty("wasteQty")private String  wasteQty;
	@JsonProperty("wastePrintCost")private String  wastePrintCost;				
	@JsonProperty("loyaltyPrice")private String  loyaltyPrice;
	@JsonProperty("loyaltyDiscountAmt")private String  loyaltyDiscountAmt;
	
	@JsonProperty("productAttribute")private ProductAttribute productAttribute;
	
	@JsonProperty("template")
	private List<Template> template = new ArrayList<Template>();
	@JsonProperty("licenseContent")
	private List<LicenseContent> licenseContent = new ArrayList<LicenseContent>();
	
	@JsonProperty("pluDetails")
	private List<PLUDetail> pluDetails = new ArrayList<PLUDetail>();
	@JsonProperty("printableSigns")
	private List<PrintableSign> printableSigns = new ArrayList<PrintableSign>();
	
		
	/**
	 * @return the pluDetails
	 */
	@JsonProperty("pluDetails")
	public List<PLUDetail> getPluDetails() {
		return pluDetails;
	}
	/**
	 * @param pluDetails the pluDetails to set
	 */
	@JsonProperty("pluDetails")
	public void setPluDetails(List<PLUDetail> pluDetails) {
		this.pluDetails = pluDetails;
	}
	/**
	 * @return the printableSigns
	 */
	@JsonProperty("printableSigns")
	public List<PrintableSign> getPrintableSigns() {
		return printableSigns;
	}
	/**
	 * @param printableSigns the printableSigns to set
	 */
	@JsonProperty("printableSigns")
	public void setPrintableSigns(List<PrintableSign> printableSigns) {
		this.printableSigns = printableSigns;
	}
	
	/**
	 * @return the totalNumberOfProductImages
	 */
	@JsonProperty("totalNumberOfProductImages")
	public String getTotalNumberOfProductImages() {
		return totalNumberOfProductImages;
	}
	/**
	 * @param totalNumberOfProductImages the totalNumberOfProductImages to set
	 */
	@JsonProperty("totalNumberOfProductImages")
	public void setTotalNumberOfProductImages(String totalNumberOfProductImages) {
		this.totalNumberOfProductImages = totalNumberOfProductImages;
	}
	/**
	 * @return the pCPProductId
	 */
	@JsonProperty("pcpProductId")
	public String getpCPProductId() {
		return pCPProductId;
	}
	/**
	 * @param pCPProductId the pCPProductId to set
	 */
	@JsonProperty("pcpProductId")
	public void setpCPProductId(String pCPProductId) {
		this.pCPProductId = pCPProductId.trim();
	}
	/**
	 * @return the quantity
	 */
	@JsonProperty("quantity")
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	@JsonProperty("quantity")
	public void setQuantity(String quantity) {
		this.quantity = quantity.trim();
	}
	/**
	 * @return the couponCode
	 */
	@JsonProperty("couponCode")
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	@JsonProperty("couponCode")
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode.trim();
	}
	
	/**
	 * @return the numberOfSheets
	 */
	@JsonProperty("numberOfSheets")
	public String getNumberOfSheets() {
		return numberOfSheets;
	}
	/**
	 * @param numberOfSheets the numberOfSheets to set
	 */
	@JsonProperty("numberOfSheets")
	public void setNumberOfSheets(String numberOfSheets) {
		this.numberOfSheets = numberOfSheets.trim();
	}
	

	/**
	 * @return the pCPAttributes
	 */
	@JsonProperty("pcpAttributes")
	public String getpCPAttributes() {
		return pCPAttributes;
	}
	/**
	 * @param pCPAttributes the pCPAttributes to set
	 */
	@JsonProperty("pcpAttributes")
	public void setpCPAttributes(String pCPAttributes) {
		this.pCPAttributes = pCPAttributes.trim();
	}
	

	/**
	 * @return the equipmentId
	 */
	@JsonProperty("equipmentId")
	public String getEquipmentId() {
		return equipmentId;
	}
	/**
	 * @param equipmentId the equipmentId to set
	 */
	@JsonProperty("equipmentId")
	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId.trim();
	}
	
	/**
	 * @return the originalRetail
	 */
	@JsonProperty("originalRetail")
	public String getOriginalRetail() {
		return originalRetail;
	}
	/**
	 * @param originalRetail the originalRetail to set
	 */
	@JsonProperty("originalRetail")
	public void setOriginalRetail(String originalRetail) {
		this.originalRetail = originalRetail.trim();
	}
	/**
	 * @return the discountAmt
	 */
	@JsonProperty("discountAmt")
	public String getDiscountAmt() {
		return discountAmt;
	}
	/**
	 * @param discountAmt the discountAmt to set
	 */
	@JsonProperty("discountAmt")
	public void setDiscountAmt(String discountAmt) {
		this.discountAmt = discountAmt.trim();
	}
	/**
	 * @return the calculatedRetail
	 */
	@JsonProperty("calculatedRetail")
	public String getCalculatedRetail() {
		return calculatedRetail;
	}
	/**
	 * @param calculatedRetail the calculatedRetail to set
	 */
	@JsonProperty("calculatedRetail")
	public void setCalculatedRetail(String calculatedRetail) {
		this.calculatedRetail = calculatedRetail.trim();
	}
	
	/**
	 * @return the wasteQty
	 */
	@JsonProperty("wasteQty")
	public String getWasteQty() {
		return wasteQty;
	}
	/**
	 * @param wasteQty the wasteQty to set
	 */
	@JsonProperty("wasteQty")
	public void setWasteQty(String wasteQty) {
		this.wasteQty = wasteQty.trim();
	}
	/**
	 * @return the wastePrintCost
	 */
	@JsonProperty("wastePrintCost")
	public String getWastePrintCost() {
		return wastePrintCost;
	}
	/**
	 * @param wastePrintCost the wastePrintCost to set
	 */
	@JsonProperty("wastePrintCost")
	public void setWastePrintCost(String wastePrintCost) {
		this.wastePrintCost = wastePrintCost.trim();
	}
	/**
	 * @return the loyaltyPrice
	 */
	@JsonProperty("loyaltyPrice")
	public String getLoyaltyPrice() {
		return loyaltyPrice;
	}
	/**
	 * @param loyaltyPrice the loyaltyPrice to set
	 */
	@JsonProperty("loyaltyPrice")
	public void setLoyaltyPrice(String loyaltyPrice) {
		this.loyaltyPrice = loyaltyPrice.trim();
	}
	/**
	 * @return the loyaltyDiscountAmt
	 */
	@JsonProperty("loyaltyDiscountAmt")
	public String getLoyaltyDiscountAmt() {
		return loyaltyDiscountAmt;
	}
	/**
	 * @param loyaltyDiscountAmt the loyaltyDiscountAmt to set
	 */
	@JsonProperty("loyaltyDiscountAmt")
	public void setLoyaltyDiscountAmt(String loyaltyDiscountAmt) {
		this.loyaltyDiscountAmt = loyaltyDiscountAmt.trim();
	}
	/**
	 * @return the template
	 */
	@JsonProperty("template")
	public List<Template> getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	@JsonProperty("template")
	public void setTemplate(List<Template> template) {
		this.template = template;
	}
	/**
	 * @return the licenseContent
	 */
	@JsonProperty("licenseContent")
	public List<LicenseContent> getLicenseContent() {
		return licenseContent;
	}
	/**
	 * @param licenseContent the licenseContent to set
	 */
	@JsonProperty("licenseContent")
	public void setLicenseContent(List<LicenseContent> licenseContent) {
		this.licenseContent = licenseContent;
	}
	/**
	 * @return the productAttribute
	 */
	@JsonProperty("productAttribute")
	public ProductAttribute getProductAttribute() {
		return productAttribute;
	}
	/**
	 * @param productAttribute the productAttribute to set
	 */
	@JsonProperty("productAttribute")
	public void setProductAttribute(ProductAttribute productAttribute) {
		this.productAttribute = productAttribute;
	}
	
	@Override
	public String toString() {
		return "OrderItem [totalNumberOfProductImages="
				+ totalNumberOfProductImages + ", pCPProductId=" + pCPProductId
				+ ", quantity=" + quantity + ", couponCode=" + couponCode
				+ ", numberOfSheets=" + numberOfSheets + ", pCPAttributes="
				+ pCPAttributes + ", equipmentId=" + equipmentId
				+ ", originalRetail=" + originalRetail + ", discountAmt="
				+ discountAmt + ", calculatedRetail=" + calculatedRetail
				+ ", wasteQty=" + wasteQty + ", wastePrintCost="
				+ wastePrintCost + ", loyaltyPrice=" + loyaltyPrice
				+ ", loyaltyDiscountAmt=" + loyaltyDiscountAmt
				+ ", productAttribute=" + productAttribute + ", template="
				+ template + ", licenseContent=" + licenseContent
				+ ", pluDetails=" + pluDetails + ", printableSigns="
				+ printableSigns + "]";
	}

}
