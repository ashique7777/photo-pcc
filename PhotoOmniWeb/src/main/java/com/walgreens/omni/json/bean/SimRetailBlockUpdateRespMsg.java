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
@JsonPropertyOrder({ "updateStatusMessage" })
public class SimRetailBlockUpdateRespMsg {
	@JsonProperty("updateStatusMessage")
	private String updateStatusMessage;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The updateStatusMessage
	 */
	@JsonProperty("updateStatusMessage")
	public String getUpdateStatusMessage() {
		return updateStatusMessage;
	}

	/**
	 * 
	 * @param updateStatusMessage
	 *            The updateStatusMessage
	 */
	@JsonProperty("updateStatusMessage")
	public void setUpdateStatusMessage(String updateStatusMessage) {
		this.updateStatusMessage = updateStatusMessage;
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
