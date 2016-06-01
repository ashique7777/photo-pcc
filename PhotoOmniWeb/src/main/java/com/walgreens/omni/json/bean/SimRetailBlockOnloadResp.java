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
@JsonPropertyOrder({ "simRetailBlocOnloadList" })
public class SimRetailBlockOnloadResp {

	@JsonProperty("simRetailBlocOnloadList")
	private List<SimRetailBlocOnloadList> simRetailBlocOnloadList = new ArrayList<SimRetailBlocOnloadList>();
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The simRetailBlocOnloadList
	 */
	@JsonProperty("simRetailBlocOnloadList")
	public List<SimRetailBlocOnloadList> getSimRetailBlocOnloadList() {
		return simRetailBlocOnloadList;
	}

	/**
	 * 
	 * @param simRetailBlocOnloadList
	 *            The simRetailBlocOnloadList
	 */
	@JsonProperty("simRetailBlocOnloadList")
	public void setSimRetailBlocOnloadList(
			List<SimRetailBlocOnloadList> simRetailBlocOnloadList) {
		this.simRetailBlocOnloadList = simRetailBlocOnloadList;
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
