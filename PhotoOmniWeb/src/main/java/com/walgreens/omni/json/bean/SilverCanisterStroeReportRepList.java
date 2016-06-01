package com.walgreens.omni.json.bean;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"SilverCanisterStoreDetails"
})

public class SilverCanisterStroeReportRepList {
	@JsonProperty("SilverCanisterStoreDetails")
	private SilverCanisterStoreDetails SilverCanisterStoreDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The SilverCanisterStoreDetails
	*/
	@JsonProperty("SilverCanisterStoreDetails")
	public SilverCanisterStoreDetails getSilverCanisterStoreDetails() {
	return SilverCanisterStoreDetails;
	 }

	/**
	* 
	* @param SilverCanisterStoreDetails
	* The SilverCanisterStoreDetails
	*/
	@JsonProperty("SilverCanisterStoreDetails")
	public void setSilverCanisterStoreDetails(SilverCanisterStoreDetails SilverCanisterStoreDetails) {
	this.SilverCanisterStoreDetails = SilverCanisterStoreDetails;
	 }

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	 }

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}
