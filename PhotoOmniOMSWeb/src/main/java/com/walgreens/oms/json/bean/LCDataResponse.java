package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.LCResponseBean;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "messageHeader",
    "LicenseContentDownloadDetailList"
})
public class LCDataResponse {

		@JsonProperty("messageHeader")
		private MessageHeader messageHeader;
		@JsonProperty("LicenseContentDownloadDetailList")
	    private List<LCResponseBean> licenseContentDownloadDetailList = new ArrayList<LCResponseBean>();
		
		public List<LCResponseBean> getLicenseContentDownloadDetailList() {
			return licenseContentDownloadDetailList;
		}
		public void setLicenseContentDownloadDetailList(
				List<LCResponseBean> licenseContentDownloadDetailList) {
			this.licenseContentDownloadDetailList = licenseContentDownloadDetailList;
		}
		@JsonIgnore
		private Map<String, Object> additionalProperties = new HashMap<String, Object>();
		
		
		/**
		 * @return the messageHeader
		 */
		public MessageHeader getMessageHeader() {
			return messageHeader;
		}
		/**
		 * @param message the messageHeader to set
		 */
		public void setMessageHeader(MessageHeader message) {
			this.messageHeader = message;
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
