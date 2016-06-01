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
@JsonPropertyOrder({
"StoreNumber",
"CanisterChangeDate",
"ServiceDescription"
})

public class VendorUpdSilverCanisterDetails {
	
	@JsonProperty("StoreNumber")
	private String StoreNumber;
	@JsonProperty("CanisterChangeDate")
	private String CanisterChangeDate;
	@JsonProperty("ServiceDescription")
	private String ServiceDescription;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	* 
	* @return
	* The StoreNumber
	*/
	@JsonProperty("StoreNumber")
	public String getStoreNumber() {
	return StoreNumber;
	 }

	/**
	* 
	* @param StoreNumber
	* The StoreNumber
	*/
	@JsonProperty("StoreNumber")
	public void setStoreNumber(String StoreNumber) {
	this.StoreNumber = StoreNumber;
	 }

	/**
	* 
	* @return
	* The CanisterChangeDate
	*/
	@JsonProperty("CanisterChangeDate")
	public String getCanisterChangeDate() {
	return CanisterChangeDate;
	 }

	/**
	* 
	* @param CanisterChangeDate
	* The CanisterChangeDate
	*/
	@JsonProperty("CanisterChangeDate")
	public void setCanisterChangeDate(String CanisterChangeDate) {
	this.CanisterChangeDate = CanisterChangeDate;
	 }

	/**
	* 
	* @return
	* The ServiceDescription
	*/
	@JsonProperty("ServiceDescription")
	public String getServiceDescription() {
	return ServiceDescription;
	 }

	/**
	* 
	* @param ServiceDescription
	* The ServiceDescription
	*/
	@JsonProperty("ServiceDescription")
	public void setServiceDescription(String ServiceDescription) {
	this.ServiceDescription = ServiceDescription;
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
