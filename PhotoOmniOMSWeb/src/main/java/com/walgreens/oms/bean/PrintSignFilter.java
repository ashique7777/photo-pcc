/**
 * 
 */
package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "eventName" })
public class PrintSignFilter {
	@JsonProperty("eventName")
	private String eventName;

	/**
	 * @return the eventName
	 */
	@JsonProperty("eventName")
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	@JsonProperty("eventName")
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	

}
