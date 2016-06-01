package com.walgreens.omni.json.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "lastCanisterChangeDate", "serviceDescription",
		"silverCompany","storeAddress" })
public class SilverCanisterStoreDetails {

	@JsonProperty("lastCanisterChangeDate")
	private java.util.Date lastCanisterChangeDate;
	@JsonProperty("serviceDescription")
	private String serviceDescription;
	@JsonProperty("silverCompany")
	private String silverCompany;
    @JsonProperty("storeAddress")
    private String storeAddress;
	@JsonIgnore
	private String totalRowCount;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * 
	 * @return The lastCanisterChangeDate
	 */
	@JsonProperty("lastCanisterChangeDate")
	public java.util.Date getLastCanisterChangeDate() {
		return lastCanisterChangeDate;
	}

	/**
	 * 
	 * @param date
	 *            The lastCanisterChangeDate
	 */
	@JsonProperty("lastCanisterChangeDate")
	public void setLastCanisterChangeDate(java.util.Date date) {
		this.lastCanisterChangeDate = date;
	}

	/**
	 * 
	 * @return The serviceDescription
	 */
	@JsonProperty("serviceDescription")
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * 
	 * @param serviceDescription
	 *            The serviceDescription
	 */
	@JsonProperty("serviceDescription")
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	/**
	 * 
	 * @return The silverCompany
	 */
	@JsonProperty("silverCompany")
	public String getSilverCompany() {
		return silverCompany;
	}

	/**
	 * 
	 * @param silverCompany
	 *            The silverCompany
	 */
	@JsonProperty("silverCompany")
	public void setSilverCompany(String silverCompany) {
		this.silverCompany = silverCompany;
	}


	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@JsonProperty("totalRowCount")
	public String getTotalRowCount() {
		return totalRowCount;
	}
	@JsonProperty("totalRowCount")
	public void setTotalRowCount(String totalRowCount) {
		this.totalRowCount = totalRowCount;
	}
	
	@JsonProperty("storeAddress")
	public String getStoreAddress() {
		return storeAddress;
	}
	@JsonProperty("storeAddress")
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

}
