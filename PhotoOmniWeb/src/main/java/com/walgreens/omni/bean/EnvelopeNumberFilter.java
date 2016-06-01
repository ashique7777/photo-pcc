package com.walgreens.omni.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "envelopeNbr" })
public class EnvelopeNumberFilter {

	@JsonProperty("envelopeNbr")
	private String envelopeNbr;

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
