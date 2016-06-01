/**
 * 
 */
package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "vendorName", "storeNumber"})
public class POFVendorCostValidationFilter {
	
	@JsonProperty("currrentPage")
	private String currrentPage;
	
	@JsonProperty("totalRecord")
	private String totalRecord;
	
	@JsonProperty("vendorName")
	private String vendorName;
	
	@JsonProperty("storeNumber")
	private String storeNumber;
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the currrentPage
	 */
	public String getCurrrentPage() {
		return currrentPage;
	}
	/**
	 * @param currrentPage the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}
	/**
	 * @return the totalRecord
	 */
	public String getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	

}
