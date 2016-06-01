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
@JsonPropertyOrder({ "vcId", "storeNumber", "envNumber", "productDesc","vendorName","storeCalculatedCost","centralCalculatedCost","approvedCost"})
public class POFVendorCostReportDetail {
	
	@JsonProperty("vcId")
	int vcId;
	
	@JsonProperty("storeNumber")
	int storeNumber;
	
	@JsonProperty("envNumber")
	int envNumber;
	
	@JsonProperty("productDesc")
	String productDesc;
	
	@JsonProperty("vendorName")
	String vendorName;
	
	@JsonProperty("storeCalculatedCost")
	double storeCalculatedCost;
	
	@JsonProperty("centralCalculatedCost")
	double centralCalculatedCost;
	
	@JsonProperty("approvedCost")
	double approvedCost;
	
	private int totalRecord;
	
	
	/**
	 * @return the productDesc
	 */
	public String getProductDesc() {
		return productDesc;
	}
	/**
	 * @param productDesc the productDesc to set
	 */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
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
	 * @return the storeCalculatedCost
	 */
	public double getStoreCalculatedCost() {
		return storeCalculatedCost;
	}
	/**
	 * @param storeCalculatedCost the storeCalculatedCost to set
	 */
	public void setStoreCalculatedCost(double storeCalculatedCost) {
		this.storeCalculatedCost = storeCalculatedCost;
	}
	/**
	 * @return the centralCalculatedCost
	 */
	public double getCentralCalculatedCost() {
		return centralCalculatedCost;
	}
	/**
	 * @param centralCalculatedCost the centralCalculatedCost to set
	 */
	public void setCentralCalculatedCost(double centralCalculatedCost) {
		this.centralCalculatedCost = centralCalculatedCost;
	}
	
	/**
	 * @return the vcId
	 */
	public int getVcId() {
		return vcId;
	}
	/**
	 * @param vcId the vcId to set
	 */
	public void setVcId(int vcId) {
		this.vcId = vcId;
	}
	
	/**
	 * @return the storeNumber
	 */
	public int getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(int storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	/**
	 * @return the envNumber
	 */
	public int getEnvNumber() {
		return envNumber;
	}
	/**
	 * @param envNumber the envNumber to set
	 */
	public void setEnvNumber(int envNumber) {
		this.envNumber = envNumber;
	}
	/**
	 * @return the approvedCost
	 */
	public double getApprovedCost() {
		return approvedCost;
	}
	/**
	 * @param approvedCost the approvedCost to set
	 */
	public void setApprovedCost(double approvedCost) {
		this.approvedCost = approvedCost;
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	

}
