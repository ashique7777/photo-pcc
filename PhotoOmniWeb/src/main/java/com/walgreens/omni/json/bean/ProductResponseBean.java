/**
 * 
 */
package com.walgreens.omni.json.bean;

import com.walgreens.common.utility.MessageHeader;
import com.walgreens.omni.bean.ExceptionRepEnvNbrLink;

/**
 *
 */
public class ProductResponseBean {
	private ExceptionRepEnvNbrLink envelopNbrHyperLink;
	private MessageHeader messageHeader;
	
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
	 * @return the envelopNbrHyperLink
	 */
	public ExceptionRepEnvNbrLink getEnvelopNbrHyperLink() {
		return envelopNbrHyperLink;
	}

	/**
	 * @param envelopNbrHyperLink the envelopNbrHyperLink to set
	 */
	public void setEnvelopNbrHyperLink(ExceptionRepEnvNbrLink envelopNbrHyperLink) {
		this.envelopNbrHyperLink = envelopNbrHyperLink;
	}

	
	
	
	
}