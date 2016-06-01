package com.walgreens.oms.json.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.ErrorDetails;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "posDetails",
    "status",
    "errorDetails"
})
public class PosList {

    @JsonProperty("posDetails")
    private PosDetails posDetails;
    @JsonIgnore
    @JsonProperty("status")
    private Boolean status;
    @JsonIgnore
    @JsonProperty("sysOrderId")
    private Long sysOrderId;
    @JsonIgnore
    @JsonProperty("orderPlacedDttm")
    private Timestamp orderPlacedDttm;
    @JsonProperty("sysShoppingCartId")
    private Long sysShoppingCartId;
    @JsonIgnore
    @JsonProperty("errorDetails")
    private ErrorDetails errorDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The posDetails
     */
    @JsonProperty("posDetails")
    public PosDetails getPosDetails() {
        return posDetails;
    }

    /**
     * 
     * @param posDetails
     *     The posDetails
     */
    @JsonProperty("posDetails")
    public void setPosDetails(PosDetails posDetails) {
        this.posDetails = posDetails;
    }

    /**
     * 
     * @return
     *     The status
     */
    @JsonProperty("status")
    public Boolean getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The errorDetails
     */
    @JsonProperty("errorDetails")
    public ErrorDetails getErrorDetails() {
        return errorDetails;
    }

    /**
     * 
     * @param errorDetails
     *     The errorDetails
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

	/**
	 * @return the sysOrderId
	 */
    @JsonProperty("sysOrderId")
	public Long getSysOrderId() {
		return sysOrderId;
	}

	/**
	 * @param sysOrderId the sysOrderId to set
	 */
    @JsonProperty("sysOrderId")
	public void setSysOrderId(Long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}

	/**
	 * @return the orderPlacedDttm
	 */
    @JsonProperty("orderPlacedDttm")
	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	/**
	 * @param orderPlacedDttm the orderPlacedDttm to set
	 */
    @JsonProperty("orderPlacedDttm")
	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public Long getSysShoppingCartId() {
		return sysShoppingCartId;
	}

	public void setSysShoppingCartId(Long sysShoppingCartId) {
		this.sysShoppingCartId = sysShoppingCartId;
	}

}
