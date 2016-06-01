package com.walgreens.oms.json.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.oms.bean.LCDataBean;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "licenseContentDownloadDetail" })
public class LicenseContentDownloadReqList {

	@JsonProperty("licenseContentDownloadDetail")
	private LCDataBean licenseContentDownloadDetail;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	/**
	 * @return the licenseContentDownloadDetail
	 */
	@JsonProperty("licenseContentDownloadDetail")
	public LCDataBean getLicenseContentDownloadDetail() {
		return licenseContentDownloadDetail;
	}
	/**
	 * @param licenseContentDownloadDetail the licenseContentDownloadDetail to set
	 */
	@JsonProperty("licenseContentDownloadDetail")
	public void setLicenseContentDownloadDetail(
			LCDataBean licenseContentDownloadDetail) {
		this.licenseContentDownloadDetail = licenseContentDownloadDetail;
	}
	/**
	 * @return the additionalProperties
	 */
	@JsonIgnore
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}
	/**
	 * @param additionalProperties the additionalProperties to set
	 */
	@JsonIgnore
	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}
	
	
}
