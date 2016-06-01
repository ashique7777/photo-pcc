package com.walgreens.oms.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;




public class EventDataFilter {
	
	private List<EventData> event;
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
	 * @return the event
	 */
	public List<EventData> getEvent() {
		return event;
	}

	/**
	 * @param event the event to set
	 */
	public void setEvent(List<EventData> event) {
		this.event = event;
	}

}
