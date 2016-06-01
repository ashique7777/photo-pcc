/**
 * 
 */
package com.walgreens.admin.json.bean;

/**
 * @author CTS
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.walgreens.common.utility.MessageHeader;

public class MachineReportBean {

	private String startDate;
	private String endDate;
	private String location;
	private String locationNumber;
	private String machine;
	private MessageHeader messageHeader;
	private List<Store> store = new ArrayList<Store>();

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the locationNumber
	 */
	public String getLocationNumber() {
		return locationNumber;
	}

	/**
	 * @param locationNumber
	 *            the locationNumber to set
	 */
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * @return the machine
	 */
	public String getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            the machine to set
	 */
	public void setMachine(String machine) {
		this.machine = machine;
	}

	/**
	 * @return the store
	 */
	public List<Store> getStore() {
		return store;
	}

	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore(List<Store> store) {
		this.store = store;
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
