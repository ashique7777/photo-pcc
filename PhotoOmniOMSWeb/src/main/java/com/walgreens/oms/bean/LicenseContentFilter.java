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
@JsonPropertyOrder({ "startDate", "endDate", "emailIds" })
public class LicenseContentFilter {
	@JsonProperty("startDate")
	private String startDate;
	@JsonProperty("endDate")
	private String endDate;
	@JsonProperty("emailIds")
	private String emailIds;
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
