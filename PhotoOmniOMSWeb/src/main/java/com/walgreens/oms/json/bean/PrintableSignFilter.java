/**
 * 
 */
package com.walgreens.oms.json.bean;

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
@JsonPropertyOrder({
"endDate",
"emailIds",
"eventId",
"startDate"
})
public class PrintableSignFilter {
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("emailIds")
	private String emailIds;
	@JsonProperty("eventId")
	private String eventId;
	@JsonProperty("startDate")
	private String startDate;
	/**
	 * @return the endDate
	 */
	@JsonProperty("endDate")
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	@JsonProperty("endDate")
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the eventId
	 */
	@JsonProperty("eventId")
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	@JsonProperty("eventId")
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the startDate
	 */
	@JsonProperty("startDate")
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	@JsonProperty("startDate")
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the emailIds
	 */
	@JsonProperty("emailIds")
	public String getEmailIds() {
		return emailIds;
	}
	/**
	 * @param emailIds the emailIds to set
	 */
	@JsonProperty("emailIds")
	public void setEmailIds(String emailIds) {
		this.emailIds = emailIds;
	}

}
