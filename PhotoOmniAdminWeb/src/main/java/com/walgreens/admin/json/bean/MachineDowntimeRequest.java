package com.walgreens.admin.json.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.admin.bean.MachineDowntimeBean;
import com.walgreens.common.utility.MessageHeader;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "messageHeader", "machineDowntimeDetailList" })
public class MachineDowntimeRequest {

	@JsonProperty("messageHeader")
	private MessageHeader messageHeader;
	@JsonProperty("machineDowntimeDetailList")
	private List<MachineDowntimeBean> machineDowntimeList;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	
	

	

	/**
	 * @return the machineDowntimeList
	 */
	public List<MachineDowntimeBean> getMachineDowntimeList() {
		return machineDowntimeList;
	}

	/**
	 * @param machineDowntimeList the machineDowntimeList to set
	 */
	public void setMachineDowntimeList(List<MachineDowntimeBean> machineDowntimeList) {
		this.machineDowntimeList = machineDowntimeList;
	}

	/**
	 * 
	 * @return The MessageHeader
	 */
	@JsonProperty("messageHeader")
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * 
	 * @param MessageHeader
	 *            The MessageHeader
	 */
	@JsonProperty("messageHeader")
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}


	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return "MachineDowntimeRequest [messageHeader=" + messageHeader
				+ ", machineDowntimeDetailList=" + machineDowntimeList + "]";
	}
}
