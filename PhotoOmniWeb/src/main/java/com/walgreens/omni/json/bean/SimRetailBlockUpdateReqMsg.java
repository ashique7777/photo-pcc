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
@JsonPropertyOrder({ "simRetailBlockUpdateReq" })
public class SimRetailBlockUpdateReqMsg {
	@JsonProperty("simRetailBlockUpdateReq")
	private SimRetailBlockUpdateReq simRetailBlockUpdateReq;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The simRetailBlockUpdateReq
	 */
	@JsonProperty("simRetailBlockUpdateReq")
	public SimRetailBlockUpdateReq getSimRetailBlockUpdateReq() {
		return simRetailBlockUpdateReq;
	}

	/**
	 * 
	 * @param simRetailBlockUpdateReq
	 *            The simRetailBlockUpdateReq
	 */
	@JsonProperty("simRetailBlockUpdateReq")
	public void setSimRetailBlockUpdateReq(SimRetailBlockUpdateReq simRetailBlockUpdateReq) {
		this.simRetailBlockUpdateReq = simRetailBlockUpdateReq;
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
