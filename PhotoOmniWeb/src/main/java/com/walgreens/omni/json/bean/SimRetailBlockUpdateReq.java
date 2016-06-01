package com.walgreens.omni.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "storeNumber", "retailBlock" })
public class SimRetailBlockUpdateReq {
	@JsonProperty("storeNumber")
	private List<String> storeNumber = new ArrayList<String>();
	@JsonProperty("retailBlock")
	private String retailBlock;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The storeNumber
	 */
	@JsonProperty("storeNumber")
	public List<String> getStoreNumber() {
		return storeNumber;
	}

	/**
	 * 
	 * @param storeNumber
	 *            The storeNumber
	 */
	@JsonProperty("storeNumber")
	public void setStoreNumber(List<String> storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**
	 * 
	 * @return The retailBlock
	 */
	@JsonProperty("retailBlock")
	public String getRetailBlock() {
		return retailBlock;
	}

	/**
	 * 
	 * @param retailBlock
	 *            The retailBlock
	 */
	@JsonProperty("retailBlock")
	public void setRetailBlock(String retailBlock) {
		this.retailBlock = retailBlock;
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
