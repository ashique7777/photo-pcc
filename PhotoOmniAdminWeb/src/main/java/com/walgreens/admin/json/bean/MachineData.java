/**
 * 
 */
package com.walgreens.admin.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;

/**
 * @author CTS
 *
 */
public class MachineData {
	
	private List<MachineType> machine;
	private MessageHeader messageHeader;

	/**
	 * @return the machine
	 */
	public List<MachineType> getMachine() {
		return machine;
	}

	/**
	 * @param machine the machine to set
	 */
	public void setMachine(List<MachineType> machine) {
		this.machine = machine;
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
