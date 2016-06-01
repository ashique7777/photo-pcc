package com.walgreens.omni.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.omni.bean.ExceptionEmployeeResponseList;

public class ExceptionEmployeeRespBean {
	
	private MessageHeader messageHeader;
	private List<ExceptionEmployeeResponseList> empResponseList;
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
	 * @return the empResponseList
	 */
	public List<ExceptionEmployeeResponseList> getEmpResponseList() {
		return empResponseList;
	}
	/**
	 * @param empResponseList the empResponseList to set
	 */
	public void setEmpResponseList(
			List<ExceptionEmployeeResponseList> empResponseList) {
		this.empResponseList = empResponseList;
	}
	
	

}
