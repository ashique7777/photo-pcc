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
public class VendorCostValidationReportResponse {
	
	
	private int totalRecord;
	private String currrentPage;
	private String filtertypePay;
	private MessageHeader messageHeader;
		
	private List<POFVendorCostReportDetail> data;

	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}

	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * @return the currrentPage
	 */
	public String getCurrrentPage() {
		return currrentPage;
	}

	/**
	 * @param currrentPage the currrentPage to set
	 */
	public void setCurrrentPage(String currrentPage) {
		this.currrentPage = currrentPage;
	}

	/**
	 * @return the filtertypePay
	 */
	public String getFiltertypePay() {
		return filtertypePay;
	}

	/**
	 * @param filtertypePay the filtertypePay to set
	 */
	public void setFiltertypePay(String filtertypePay) {
		this.filtertypePay = filtertypePay;
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
	
	
	

}
