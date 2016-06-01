/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.POFVendorCostReportDetail;

/**
 * @author CTS
 *
 */
public class ApproveVCDataRequest {
	
	private String currentPage;
	private List<POFVendorCostReportDetail> data;
	private MessageHeader messageHeader;
	
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
	 * @return the data
	 */
	public List<POFVendorCostReportDetail> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<POFVendorCostReportDetail> data) {
		this.data = data;
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
