package com.walgreens.oms.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.ErrorDetails;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({   
    "status",
    "errorDetails"
})
public class LCResponseBean {

	@JsonProperty("status")
    private Boolean status;
    @JsonProperty("errorDetails")
    private ErrorDetails errorDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
   
	/**
	 * @return the status
	 */
    @JsonProperty("status")
	public Boolean getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
    @JsonProperty("status")
	public void setStatus(Boolean status) {
		this.status = status;
	}
	/**
	 * @return the errorDetails
	 */
    @JsonProperty("errorDetails")
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	/**
	 * @param errorDetails the errorDetails to set
	 */
    @JsonProperty("errorDetails")
	public void setErrorDetails(ErrorDetails errorDetails) {
		this.errorDetails = errorDetails;
	}
	/**
	 * @return the additionalProperties
	 */
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
}
