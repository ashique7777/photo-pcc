/**
 * 
 */
package com.walgreens.omni.json.bean;

import java.util.List;

/**
 * @author CTS
 *
 */
public class CannedDropDownDataTest {
	
	private List<OrderType> orderType;
	private List<InputChannel> inputChannel;
	
	/**
	 * @return the inputChannel
	 */
	public List<InputChannel> getInputChannel() {
		return inputChannel;
	}
	/**
	 * @param inputChannel the inputChannel to set
	 */
	public void setInputChannel(List<InputChannel> inputChannel) {
		this.inputChannel = inputChannel;
	}
	/**
	 * @return the orderType
	 */
	public List<OrderType> getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(List<OrderType> orderType) {
		this.orderType = orderType;
	}
}
