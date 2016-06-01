/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.List;

import com.walgreens.common.utility.MessageHeader;

/**
 * @author CTS
 *
 */
public class UnclaimedResponse {
	private String storeNumber;
	private String  currentDate;
	private String  totalRecord;
	private String  currentPage;
	private MessageHeader messageHeader;
	private List<UnclaimedBean> unclaimedBeanList = new ArrayList<UnclaimedBean>();
	/**
	 * @return the storeNumber
	 */
	public String getStoreNumber() {
		return storeNumber;
	}
	/**
	 * @param storeNumber the storeNumber to set
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	/**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	/**
	 * @return the totalRecord
	 */
	public String getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
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
	 * @return the unclaimedBeanList
	 */
	public List<UnclaimedBean> getUnclaimedBeanList() {
		return unclaimedBeanList;
	}
	/**
	 * @param unclaimedBeanList the unclaimedBeanList to set
	 */
	public void setUnclaimedBeanList(List<UnclaimedBean> unclaimedBeanList) {
		this.unclaimedBeanList = unclaimedBeanList;
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
