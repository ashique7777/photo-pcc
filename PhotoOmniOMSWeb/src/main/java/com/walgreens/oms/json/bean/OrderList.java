/**
 * 
 */
package com.walgreens.oms.json.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author dasra
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"order",
"orderItems",
"exception"
})
public class OrderList implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("order")
	private Order order;
	@JsonProperty("orderItems")
	private List<OrderItem> orderItem = new ArrayList<OrderItem>();
	@JsonProperty("exception")
	private List<OrderException> exception = new ArrayList<OrderException>();
	/*@JsonProperty("oneOf")
	private List<OneOf> oneOf = new ArrayList<OneOf>();*/
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	/**
	 * @return the order
	 */
	@JsonProperty("order")
	public Order getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	@JsonProperty("order")
	public void setOrder(Order order) {
		this.order = order;
	}
	/**
	 * @return the orderItem
	 */
	@JsonProperty("orderItems")
	public List<OrderItem> getOrderItem() {
		return orderItem;
	}
	/**
	 * @param orderItem the orderItem to set
	 */
	@JsonProperty("orderItems")
	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}
	
	/**
	*
	* @return
	* The oneOf
	*//*
	@JsonProperty("oneOf")
	public List<OneOf> getOneOf() {
	return oneOf;
	}

	*//**
	*
	* @param oneOf
	* The oneOf
	*//*
	@JsonProperty("oneOf")
	public void setOneOf(List<OneOf> oneOf) {
	this.oneOf = oneOf;
	}*/

	/**
	*
	* @return
	* The Exception
	*/
	@JsonProperty("exception")
	public List<com.walgreens.oms.json.bean.OrderException> getException() {
		return exception;
	}

	/**
	*
	* @param OrderException
	* The Exception
	*/
	@JsonProperty("exception")
	public void setException(List<com.walgreens.oms.json.bean.OrderException> exception) {
		this.exception = exception;
	}
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
	
	

}
