package com.walgreens.omni.json.bean;

import java.util.ArrayList;
import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class ExceptionResponseBean {

	private List<ExceptionRepEnv> repByEnvList = new ArrayList<ExceptionRepEnv>();
	private MessageHeader messageHeader;
	/**
	 * @return the repByEnvList
	 */
	public List<ExceptionRepEnv> getRepByEnvList() {
		return repByEnvList;
	}

	/**
	 * @param repByEnvList the repByEnvList to set
	 */
	public void setRepByEnvList(List<ExceptionRepEnv> repByEnvList) {
		this.repByEnvList = repByEnvList;
	}

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
	
	
	
	
}
