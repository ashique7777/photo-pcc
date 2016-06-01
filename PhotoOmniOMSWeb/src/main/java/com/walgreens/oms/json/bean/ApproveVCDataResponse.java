/**
 * 
 */
package com.walgreens.oms.json.bean;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.POFVendorCostReportMessage;

/**
 * @author CTS
 *
 */
public class ApproveVCDataResponse {
	
	private POFVendorCostReportMessage vendorCostReportMsg;
	private MessageHeader messageHeader;
	
	/**
	 * @return the vendorCostReportMsg
	 */
	public POFVendorCostReportMessage getVendorCostReportMsg() {
		return vendorCostReportMsg;
	}
	/**
	 * @param vendorCostReportMsg the vendorCostReportMsg to set
	 */
	public void setVendorCostReportMsg(
			POFVendorCostReportMessage vendorCostReportMsg) {
		this.vendorCostReportMsg = vendorCostReportMsg;
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
