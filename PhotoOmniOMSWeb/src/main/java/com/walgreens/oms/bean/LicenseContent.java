/**
 * 
 */
package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"licenseContentId",
"quantity",
"downloadDttm",
"downloadedInd",
"addonLicenseContentDesc",
"calculatedRetail",
"originalRetail"
})

public class LicenseContent {
	
	@JsonProperty("licenseContentId")private String  licenseContentId;
	@JsonProperty("quantity")private String  quantity;
	@JsonProperty("downloadDttm")private String  downloadDttm;
	@JsonProperty("downloadedInd")private String  downloadedInd;
	@JsonProperty("addonLicenseContentDesc")private String  addonLicenseContentDesc;
	
	@JsonProperty("calculatedRetail")private String  calculatedRetail;
	@JsonProperty("originalRetail")private String  originalRetail;
	
	
	
	/**
	 * @return the licenseContentId
	 */
	@JsonProperty("licenseContentId")
	public String getLicenseContentId() {
		return licenseContentId;
	}
	
	
	/**
	 * @param licenseContentId the licenseContentId to set
	 */
	@JsonProperty("licenseContentId")
	public void setLicenseContentId(String licenseContentId) {
		this.licenseContentId = licenseContentId;
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
		this.quantity = quantity;
	}
	
	
	/**
	 * @return the downloadDttm
	 */
	@JsonProperty("downloadDttm")
	public String getDownloadDttm() {
		return downloadDttm;
	}
	
	
	/**
	 * @param downloadDttm the downloadDttm to set
	 */
	@JsonProperty("downloadDttm")
	public void setDownloadDttm(String downloadDttm) {
		this.downloadDttm = downloadDttm;
	}
	
	
	/**
	 * @return the downloadedInd
	 */
	@JsonProperty("downloadedInd")
	public String getDownloadedInd() {
		return downloadedInd;
	}
	
	
	/**
	 * @param downloadedInd the downloadedInd to set
	 */
	@JsonProperty("downloadedInd")
	public void setDownloadedInd(String downloadedInd) {
		this.downloadedInd = downloadedInd;
	}
	
	
	/**
	 * @return the addonLicenseContentDesc
	 */
	@JsonProperty("addonLicenseContentDesc")
	public String getAddonLicenseContentDesc() {
		return addonLicenseContentDesc;
	}
	
	
	/**
	 * @param addonLicenseContentDesc the addonLicenseContentDesc to set
	 */
	@JsonProperty("addonLicenseContentDesc")
	public void setAddonLicenseContentDesc(String addonLicenseContentDesc) {
		this.addonLicenseContentDesc = addonLicenseContentDesc;
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
		this.calculatedRetail = calculatedRetail;
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
		this.originalRetail = originalRetail;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LicenseContent [licenseContentId=" + licenseContentId
				+ ", quantity=" + quantity + ", downloadDttm=" + downloadDttm
				+ ", downloadedInd=" + downloadedInd
				+ ", addonLicenseContentDesc=" + addonLicenseContentDesc
				+ ", calculatedRetail=" + calculatedRetail
				+ ", originalRetail=" + originalRetail + "]";
	}
	

}
