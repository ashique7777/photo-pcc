package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "envelopeNbr", "startDate", "orderType", "orderId" })
public class EnvelopeNumberFilter {

	@JsonProperty("envelopeNbr")
	private String envelopeNbr;
	
	@JsonProperty("startDate")
	private String startDate;
	
	/*
	 * Env popup for different types of orders
	 */
	@JsonProperty("orderType")
	private String orderType;
	
	@JsonProperty("orderId")
	private String orderId;
	
	@JsonProperty("processingTypeCD")
	private String processingTypeCD;
	
	
	
	
	
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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	
	/**
	 * @return the envelopeNbr
	 */
	public String getEnvelopeNbr() {
		return envelopeNbr;
	}

	/**
	 * @param envelopeNbr the envelopeNbr to set
	 */
	public void setEnvelopeNbr(String envelopeNbr) {
		this.envelopeNbr = envelopeNbr;
	}
	
	
}
