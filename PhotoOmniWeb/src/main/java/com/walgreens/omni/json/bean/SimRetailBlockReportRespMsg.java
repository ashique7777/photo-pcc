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
import com.walgreens.common.utility.ErrorDetails;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "simRetailBlockReportResp", "errorDetails" })
public class SimRetailBlockReportRespMsg {

	@JsonProperty("simRetailBlockReportResp")
	private List<SimRetailBlockReportResp> simRetailBlockReportResp = new ArrayList<SimRetailBlockReportResp>();
	@JsonProperty("errorDetails")
	private ErrorDetails errorDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The simRetailBlockReportResp
	 */
	@JsonProperty("simRetailBlockReportResp")
	public List<SimRetailBlockReportResp> getSimRetailBlockReportResp() {
		return simRetailBlockReportResp;
	}

	/**
	 * 
	 * @param simRetailBlockReportResp
	 *            The simRetailBlockReportResp
	 */
	@JsonProperty("simRetailBlockReportResp")
	public void setSimRetailBlockReportResp(
			List<SimRetailBlockReportResp> simRetailBlockReportResp) {
		this.simRetailBlockReportResp = simRetailBlockReportResp;
	}

	/**
	 * 
	 * @return The errorDetails
	 */
	@JsonProperty("errorDetails")
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}

	/**
	 * 
	 * @param errorDetails
	 *            The errorDetails
	 */
	@JsonProperty("errorDetails")
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
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
