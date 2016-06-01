/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.UnclaimedEnvCustOrderRespBean;

/**
 * @author CTS
 *
 */
public class UnclaimedEnvCustomer {
	
	private List<UnclaimedEnvCustOrderRespBean> unclaimedDataList;
	private MessageHeader messageHeader;
	
	/**
	 * @return the unclaimedDataList
	 */
	public List<UnclaimedEnvCustOrderRespBean> getUnclaimedDataList() {
		return unclaimedDataList;
	}
	/**
	 * @param unclaimedDataList the unclaimedDataList to set
	 */
	public void setUnclaimedDataList(
			List<UnclaimedEnvCustOrderRespBean> unclaimedDataList) {
		this.unclaimedDataList = unclaimedDataList;
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
