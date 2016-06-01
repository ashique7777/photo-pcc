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
@JsonPropertyOrder({ "storeNumber", "retailBlock", "description","totalrecords" })
public class SimRetailBlockReportResp {

	@JsonProperty("storeNumber")
	private String storeNumber;
	@JsonProperty("retailBlock")
	private String retailBlock;
	@JsonProperty("description")
	private String description;
	@JsonProperty("totalrecords")
	private String totalrecords;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The storeNumber
	 */
	@JsonProperty("storeNumber")
	public String getStoreNumber() {
		return storeNumber;
	}

	/**
	 * 
	 * @param storeNumber
	 *            The storeNumber
	 */
	@JsonProperty("storeNumber")
	public void setStoreNumber(String storeNumber) {
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

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return The totalrecords
	 */
	@JsonProperty("totalrecords")
	public String getTotalrecords() {
		return totalrecords;
	}

	/**
	 * 
	 * @param totalrecords
	 *            The totalrecords
	 */
	@JsonProperty("totalrecords")
	public void setTotalrecords(String totalrecords) {
		this.totalrecords = totalrecords;
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