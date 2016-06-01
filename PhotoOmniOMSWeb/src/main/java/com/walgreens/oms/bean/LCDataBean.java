package com.walgreens.oms.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "originOrderId", "pcpProductId", "licenseContentId",
		"downloadDttm", "downloadedInd", })
public class LCDataBean {

	@JsonProperty("originOrderId")
	private String originOrderId;
	@JsonProperty("pcpProductId")
	private String pcpProductId;
	@JsonProperty("licenseContentId")
	private String licenseContentId;
	@JsonProperty("downloadDttm")
	private String downloadDttm;
	@JsonProperty("downloadedInd")
	private String downloadedInd;

	/**
	 * @return
	 */
	@JsonProperty("originOrderId")
	public String getOriginOrderId() {
		return originOrderId;
	}

	/**
	 * @param originOrderId
	 */
	@JsonProperty("originOrderId")
	public void setOriginOrderId(String originOrderId) {
		this.originOrderId = originOrderId;
	}

	/**
	 * @return
	 */
	@JsonProperty("pcpProductId")
	public String getPcpProductId() {
		return pcpProductId;
	}

	/**
	 * @param pcpProductId
	 */
	@JsonProperty("pcpProductId")
	public void setPcpProductId(String pcpProductId) {
		this.pcpProductId = pcpProductId;
	}

	/**
	 * @return
	 */
	@JsonProperty("licenseContentId")
	public String getLicenseContentId() {
		return licenseContentId;
	}

	/**
	 * @param licenseContentId
	 */
	@JsonProperty("licenseContentId")
	public void setLicenseContentId(String licenseContentId) {
		this.licenseContentId = licenseContentId;
	}

	/**
	 * @return
	 */
	@JsonProperty("downloadDttm")
	public String getDownloadDttm() {
		return downloadDttm;
	}

	/**
	 * @param downloadDttm
	 */
	@JsonProperty("downloadDttm")
	public void setDownloadDttm(String downloadDttm) {
		this.downloadDttm = downloadDttm;
	}

	/**
	 * @return
	 */
	@JsonProperty("downloadedInd")
	public String getDownloadedInd() {
		return downloadedInd;
	}

	/**
	 * @param downloadedInd
	 */
	@JsonProperty("downloadedInd")
	public void setDownloadedInd(String downloadedInd) {
		this.downloadedInd = downloadedInd;
	}

	@Override
	public String toString() {
		return "LCDataBean [originOrderId=" + originOrderId + ", pcpProductId="
				+ pcpProductId + ", licenseContentId=" + licenseContentId
				+ ", downloadDttm=" + downloadDttm + ", downloadedInd="
				+ downloadedInd + "]";
	}
	

}
