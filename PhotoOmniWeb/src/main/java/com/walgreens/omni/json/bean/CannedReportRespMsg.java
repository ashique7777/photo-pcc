/**
 * 
 */
package com.walgreens.omni.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

/**
 * @author jnaircd
 *
 */
public class CannedReportRespMsg {
	
	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("CannedReportRespList")
	private List<CannedReportRespList> CannedReportRespList = new ArrayList<CannedReportRespList>();
	@JsonProperty("currentPage")
	private String currentPage;
	@JsonProperty("totalRecords")
	private String totalRecords;
	@JsonProperty("errorDetails")
	private ErrorDetails errorDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}
	/**
	 * @param messageHeader the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}
	/**
	 * @return the cannedReportRespList
	 */
	public List<CannedReportRespList> getCannedReportRespList() {
		return CannedReportRespList;
	}
	/**
	 * @param cannedReportRespList the cannedReportRespList to set
	 */
	public void setCannedReportRespList(
			List<CannedReportRespList> cannedReportRespList) {
		CannedReportRespList = cannedReportRespList;
	}
	/**
	 * @return the currentPage
	 */
	public String getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return the totalRecords
	 */
	public String getTotalRecords() {
		return totalRecords;
	}
	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(String totalRecords) {
		this.totalRecords = totalRecords;
	}
	/**
	 * @return the errorDetails
	 */
	public ErrorDetails getErrorDetails() {
		return errorDetails;
	}
	/**
	 * @param errorDetails the errorDetails to set
	 */
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
