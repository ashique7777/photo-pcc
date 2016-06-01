package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "envelopeNumber","processingTypeCD","orderOrigintType","orderid","timeSubmitted"})
public class LateEnvelopeDetailsFilter {
	@JsonProperty("envelopeNumber")
	private String envelopeNumber;
	@JsonProperty("processingTypeCD")
	private String processingTypeCD;
	@JsonProperty("orderOrigintType")
	private String orderOrigintType;
	@JsonProperty("orderid")
	private String orderid;
	@JsonProperty("timeSubmitted")
	private String timeSubmitted;
	
	

	

	/**
	 * @return the timeSubmitted
	 */
	public String getTimeSubmitted() {
		return timeSubmitted;
	}

	/**
	 * @param timeSubmitted the timeSubmitted to set
	 */
	public void setTimeSubmitted(String timeSubmitted) {
		this.timeSubmitted = timeSubmitted;
	}

	/**
	 * @return the orderid
	 */
	public String getOrderid() {
		return orderid;
	}

	/**
	 * @param orderid the orderid to set
	 */
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	/**
	 * @return the orderOrigintType
	 */
	public String getOrderOrigintType() {
		return orderOrigintType;
	}

	/**
	 * @param orderOrigintType the orderOrigintType to set
	 */
	public void setOrderOrigintType(String orderOrigintType) {
		this.orderOrigintType = orderOrigintType;
	}

	/**
	 * @return the processingTypeCD
	 */
	public String getProcessingTypeCD() {
		return processingTypeCD;
	}

	/**
	 * @param processingTypeCD the processingTypeCD to set
	 */
	public void setProcessingTypeCD(String processingTypeCD) {
		this.processingTypeCD = processingTypeCD;
	}

	/**
	 * @return the envelopeNumber
	 */
	@JsonProperty("envelopeNumber")
	public String getEnvelopeNumber() {
		return envelopeNumber;
	}

	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	@JsonProperty("envelopeNumber")
	public void setEnvelopeNumber(String envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	
	
	
	
	
}
