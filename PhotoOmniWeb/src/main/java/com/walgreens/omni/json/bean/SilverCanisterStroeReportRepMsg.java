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
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "messageHeader", "SilverCanisterStroeReportRepList",
		"currentPage", "totalRecords", "errorDetails" })
public class SilverCanisterStroeReportRepMsg {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("SilverCanisterStroeReportRepList")
	private List<SilverCanisterStroeReportRepList> SilverCanisterStroeReportRepList = new ArrayList<SilverCanisterStroeReportRepList>();
	@JsonProperty("currentPage")
	private String currentPage;
	@JsonProperty("totalRecords")
	private String totalRecords;
	@JsonProperty("errorDetails")
	private ErrorDetails errorDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The messageHeader
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * 
	 * @param messageHeader
	 *            The messageHeader
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	/**
	 * 
	 * @return The SilverCanisterStroeReportRepList
	 */
	@JsonProperty("SilverCanisterStroeReportRepList")
	public List<SilverCanisterStroeReportRepList> getSilverCanisterStroeReportRepList() {
		return SilverCanisterStroeReportRepList;
	}

	/**
	 * 
	 * @param SilverCanisterStroeReportRepList
	 *            The SilverCanisterStroeReportRepList
	 */
	@JsonProperty("SilverCanisterStroeReportRepList")
	public void setSilverCanisterStroeReportRepList(
			List<SilverCanisterStroeReportRepList> SilverCanisterStroeReportRepList) {
		this.SilverCanisterStroeReportRepList = SilverCanisterStroeReportRepList;
	}

	/**
	 * 
	 * @return The currentPage
	 */
	@JsonProperty("currentPage")
	public String getCurrentPage() {
		return currentPage;
	}

	/**
	 * 
	 * @param currentPage
	 *            The currentPage
	 */
	@JsonProperty("currentPage")
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * 
	 * @return The totalRecords
	 */
	@JsonProperty("totalRecords")
	public String getTotalRecords() {
		return totalRecords;
	}

	/**
	 * 
	 * @param totalRecords
	 *            The totalRecords
	 */
	@JsonProperty("totalRecords")
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
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
