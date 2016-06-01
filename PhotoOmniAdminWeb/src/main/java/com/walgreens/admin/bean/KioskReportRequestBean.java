package com.walgreens.admin.bean;

import java.util.List;
import com.walgreens.common.utility.MessageHeader;

public class KioskReportRequestBean {

	private int currentPage;
	private List<KioskFilter> filter;
	private MessageHeader messageHeader;

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @param currentPage
	 *            the currentPage to set
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * @return the filter
	 */
	public List<KioskFilter> getFilter() {
		return filter;
	}

	/**
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(List<KioskFilter> filter) {
		this.filter = filter;
	}

	/**
	 * @return the messageHeader
	 */
	public MessageHeader getMessageHeader() {
		return messageHeader;
	}

	/**
	 * @param messageHeader
	 *            the messageHeader to set
	 */
	public void setMessageHeader(MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	@Override
	public String toString() {
		return "KioskReportRequestBean [currentPage=" + currentPage
				+ ", filter=" + filter + ", messageHeader=" + messageHeader
				+ "]";
	}
}