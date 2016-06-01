package com.walgreens.oms.json.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"pcpProductId",
"orderedQty",
"iMemQty",
"calculatedPrice",
"originalRetail",
"discountAmount",
"iMemAddedItem"
})
public class OrderLineASNDetail {

@JsonProperty("pcpProductId")
private String pcpProductId;
@JsonProperty("orderedQty")
private Integer orderedQty;
@JsonProperty("originalRetail")
private Double originalRetail;
@JsonProperty("iMemQty")
private Integer iMemQty;
@JsonProperty("calculatedPrice")
private Double calculatedPrice;
@JsonProperty("discountAmount")
private Double discountAmount;
@JsonProperty("iMemAddedItem")
private String iMemAddedItem;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The pcpProductId
*/
@JsonProperty("pcpProductId")
public String getPcpProductId() {
return pcpProductId;
 }

/**
* 
* @param pcpProductId
* The pcpProductId
*/
@JsonProperty("pcpProductId")
public void setPcpProductId(String pcpProductId) {
this.pcpProductId = pcpProductId;
 }

/**
* 
* @return
* The orderedQty
*/
@JsonProperty("orderedQty")
public Integer getOrderedQty() {
return orderedQty;
 }

/**
* 
* @param orderedQty
* The orderedQty
*/
@JsonProperty("orderedQty")
public void setOrderedQty(Integer orderedQty) {
this.orderedQty = orderedQty;
 }

/**
* 
* @return
* The iMemQty
*/
@JsonProperty("iMemQty")
public Integer getIMemQty() {
return iMemQty;
 }

/**
* 
* @param iMemQty
* The iMemQty
*/
@JsonProperty("iMemQty")
public void setIMemQty(Integer iMemQty) {
this.iMemQty = iMemQty;
 }

/**
* 
* @return
* The calculatedPrice
*/
@JsonProperty("calculatedPrice")
public Double getCalculatedPrice() {
return calculatedPrice;
 }

/**
* 
* @param calculatedPrice
* The calculatedPrice
*/
@JsonProperty("calculatedPrice")
public void setCalculatedPrice(Double calculatedPrice) {
this.calculatedPrice = calculatedPrice;
 }

/**
* 
* @return
* The discountAmount
*/
@JsonProperty("discountAmount")
public Double getDiscountAmount() {
return discountAmount;
 }

/**
* 
* @param discountAmount
* The discountAmount
*/
@JsonProperty("discountAmount")
public void setDiscountAmount(Double discountAmount) {
this.discountAmount = discountAmount;
 }

/**
* 
* @return
* The iMemAddedItem
*/
@JsonProperty("iMemAddedItem")
public String getIMemAddedItem() {
return iMemAddedItem;
 }

/**
* 
* @param iMemAddedItem
* The iMemAddedItem
*/
@JsonProperty("iMemAddedItem")
public void setIMemAddedItem(String iMemAddedItem) {
this.iMemAddedItem = iMemAddedItem;
 }

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
 }

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
 }

/**
 * @return the originalRetail
 */
@JsonAnyGetter
public Double getOriginalRetail() {
	return originalRetail;
}

/**
 * @param originalRetail the originalRetail to set
 */
public void setOriginalRetail(Double originalRetail) {
	this.originalRetail = originalRetail;
}

/**
 * @return the iMemQty
 */
public Integer getiMemQty() {
	return iMemQty;
}

/**
 * @param iMemQty the iMemQty to set
 */
public void setiMemQty(Integer iMemQty) {
	this.iMemQty = iMemQty;
}

/**
 * @return the iMemAddedItem
 */
public String getiMemAddedItem() {
	return iMemAddedItem;
}

/**
 * @param iMemAddedItem the iMemAddedItem to set
 */
public void setiMemAddedItem(String iMemAddedItem) {
	this.iMemAddedItem = iMemAddedItem;
}

/**
 * @param additionalProperties the additionalProperties to set
 */
public void setAdditionalProperties(Map<String, Object> additionalProperties) {
	this.additionalProperties = additionalProperties;
}



}
