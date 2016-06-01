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
@JsonPropertyOrder({ "retailBlock" })
public class SimRetailBlocOnloadList {

	@JsonProperty("retailBlock")
	private String retailBlock;
	@JsonProperty("priceLevel")
	private String priceLevel;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
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

	/**
	 * 
	 * @return The priceLevel
	 */
	@JsonProperty("priceLevel")
	public String getPriceLevel() {
		return priceLevel;
	}

	/**
	 * 
	 * @param priceLevel
	 *            The priceLevel
	 */
	@JsonProperty("priceLevel")
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
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
