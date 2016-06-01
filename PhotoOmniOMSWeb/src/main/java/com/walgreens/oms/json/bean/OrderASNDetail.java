package com.walgreens.oms.json.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"loyaltyPrice",	
"sysSrcVendorId",
"loyaltyDiscountAmount",	
"couponInd"	,
"locationType",
"locationNumber",
"pcpOrderId",
"envelopeNumber",
"referenceId",
"shipmentStatus",
"shipmentCompany",
"trackingNo",
"expArrivalDate",
"shippedDate",
"shipmentURL",
"shipmentCompanyPhone",
"retailPrice",
"costPrice",
"credit",
"discountedPrice",
"couponAmount",
"orderLineASNDetails"
})

public class OrderASNDetail {
@JsonProperty("loyaltyPrice")
private Double loyaltyPrice;
@JsonProperty("sysSrcVendorId")
private int sysSrcVendorId;
@JsonProperty("loyaltyDiscountAmount")
private Double loyaltyDiscountAmount;
@JsonProperty("couponInd")
private String couponInd;
@JsonProperty("locationType")
private String locationType;
@JsonProperty("locationNumber")
private Integer locationNumber;
@JsonProperty("pcpOrderId")
private long pcpOrderId;
@JsonProperty("envelopeNumber")
private String envelopeNumber;
@JsonProperty("referenceId")
private String referenceId;
@JsonProperty("shipmentStatus")
private String shipmentStatus;
@JsonProperty("shipmentCompany")
private String shipmentCompany;
@JsonProperty("trackingNo")
private String trackingNo;
@JsonProperty("expArrivalDate")
private String expArrivalDate;
@JsonProperty("shippedDate")
private String shippedDate;
@JsonProperty("shipmentURL")
private String shipmentURL;
@JsonProperty("shipmentCompanyPhone")
private String shipmentCompanyPhone;
@JsonProperty("retailPrice")
private Double retailPrice;
@JsonProperty("costPrice")
private Double costPrice;
@JsonProperty("credit")
private Double credit;
@JsonProperty("discountedPrice")
private Double discountedPrice;
@JsonProperty("couponAmount")
private Double couponAmount;
@JsonProperty("orderNumber")
private String orderNumber;

public String getOrderNumber() {
	return orderNumber;
}

public void setOrderNumber(String orderNumber) {
	this.orderNumber = orderNumber;
}

@JsonProperty("orderLineASNDetails")
private List<OrderLineASNDetail> orderLineASNDetails = new ArrayList<OrderLineASNDetail>();
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();


/**
 * @return the loyaltyPrice
 */
@JsonProperty("loyaltyPrice")
public Double getLoyaltyPrice() {
	return loyaltyPrice;
}

/**
 * @param loyaltyPrice the loyaltyPrice to set
 */
@JsonProperty("loyaltyPrice")
public void setLoyaltyPrice(Double loyaltyPrice) {
	this.loyaltyPrice = loyaltyPrice;
}


/**
 * @return the sysSrcVendorId
 */
public int getSysSrcVendorId() {
	return sysSrcVendorId;
}

/**
 * @param sysSrcVendorId the sysSrcVendorId to set
 */
public void setSysSrcVendorId(int sysSrcVendorId) {
	this.sysSrcVendorId = sysSrcVendorId;
}

/**
 * @return the loyaltyDiscountAmount
 */
@JsonProperty("loyaltyDiscountAmount")
public Double getLoyaltyDiscountAmount() {
	return loyaltyDiscountAmount;
}

/**
 * @param loyaltyDiscountAmount the loyaltyDiscountAmount to set
 */
@JsonProperty("loyaltyDiscountAmount")
public void setLoyaltyDiscountAmount(Double loyaltyDiscountAmount) {
	this.loyaltyDiscountAmount = loyaltyDiscountAmount;
}

/**
 * @return the couponInd
 */
@JsonProperty("couponInd")
public String getCouponInd() {
	return couponInd;
}

/**
 * @param couponInd the couponInd to set
 */
@JsonProperty("couponInd")
public void setCouponInd(String couponInd) {
	this.couponInd = couponInd;
}

/**
* 
* @return
* The locationType
*/
@JsonProperty("locationType")
public String getLocationType() {
return locationType;
 }

/**
* 
* @param locationType
* The locationType
*/
@JsonProperty("locationType")
public void setLocationType(String locationType) {
this.locationType = locationType;
 }

/**
* 
* @return
* The locationNumber
*/
@JsonProperty("locationNumber")
public Integer getLocationNumber() {
return locationNumber;
 }

/**
* 
* @param locationNumber
* The locationNumber
*/
@JsonProperty("locationNumber")
public void setLocationNumber(Integer locationNumber) {
this.locationNumber = locationNumber;
 }

/**
* 
* @return
* The pcpOrderId
*/
@JsonProperty("pcpOrderId")
public long getPcpOrderId() {
return pcpOrderId;
 }

/**
* 
* @param pcpOrderId
* The pcpOrderId
*/
@JsonProperty("pcpOrderId")
public void setPcpOrderId(long pcpOrderId) {
this.pcpOrderId = pcpOrderId;
 }

/**
* 
* @return
* The envelopeNumber
*/
@JsonProperty("envelopeNumber")
public String getEnvelopeNumber() {
return envelopeNumber;
 }

/**
* 
* @param envelopeNumber
* The envelopeNumber
*/
@JsonProperty("envelopeNumber")
public void setEnvelopeNumber(String envelopeNumber) {
this.envelopeNumber = envelopeNumber;
 }

/**
* 
* @return
* The referenceId
*/
@JsonProperty("referenceId")
public String getReferenceId() {
return referenceId;
 }

/**
* 
* @param referenceId
* The referenceId
*/
@JsonProperty("referenceId")
public void setReferenceId(String referenceId) {
this.referenceId = referenceId;
 }

/**
* 
* @return
* The shipmentStatus
*/
@JsonProperty("shipmentStatus")
public String getShipmentStatus() {
return shipmentStatus;
 }

/**
* 
* @param shipmentStatus
* The shipmentStatus
*/
@JsonProperty("shipmentStatus")
public void setShipmentStatus(String shipmentStatus) {
this.shipmentStatus = shipmentStatus;
 }

/**
* 
* @return
* The shipmentCompany
*/
@JsonProperty("shipmentCompany")
public String getShipmentCompany() {
return shipmentCompany;
 }

/**
* 
* @param shipmentCompany
* The shipmentCompany
*/
@JsonProperty("shipmentCompany")
public void setShipmentCompany(String shipmentCompany) {
this.shipmentCompany = shipmentCompany;
 }

/**
* 
* @return
* The trackingNo
*/
@JsonProperty("trackingNo")
public String getTrackingNo() {
return trackingNo;
 }

/**
* 
* @param trackingNo
* The trackingNo
*/
@JsonProperty("trackingNo")
public void setTrackingNo(String trackingNo) {
this.trackingNo = trackingNo;
 }

/**
* 
* @return
* The expArrivalDate
*/
@JsonProperty("expArrivalDate")
public String getExpArrivalDate() {
return expArrivalDate;
 }

/**
* 
* @param expArrivalDate
* The expArrivalDate
*/
@JsonProperty("expArrivalDate")
public void setExpArrivalDate(String expArrivalDate) {
this.expArrivalDate = expArrivalDate;
 }

/**
* 
* @return
* The shippedDate
*/
@JsonProperty("shippedDate")
public String getShippedDate() {
return shippedDate;
 }

/**
* 
* @param shippedDate
* The shippedDate
*/
@JsonProperty("shippedDate")
public void setShippedDate(String shippedDate) {
this.shippedDate = shippedDate;
 }

/**
* 
* @return
* The shipmentURL
*/
@JsonProperty("shipmentURL")
public String getShipmentURL() {
return shipmentURL;
 }

/**
* 
* @param shipmentURL
* The shipmentURL
*/
@JsonProperty("shipmentURL")
public void setShipmentURL(String shipmentURL) {
this.shipmentURL = shipmentURL;
 }

/**
* 
* @return
* The shipmentCompanyPhone
*/
@JsonProperty("shipmentCompanyPhone")
public String getShipmentCompanyPhone() {
return shipmentCompanyPhone;
 }

/**
* 
* @param shipmentCompanyPhone
* The shipmentCompanyPhone
*/
@JsonProperty("shipmentCompanyPhone")
public void setShipmentCompanyPhone(String shipmentCompanyPhone) {
this.shipmentCompanyPhone = shipmentCompanyPhone;
 }

/**
* 
* @return
* The retailPrice
*/
@JsonProperty("retailPrice")
public Double getRetailPrice() {
return retailPrice;
 }

/**
* 
* @param retailPrice
* The retailPrice
*/
@JsonProperty("retailPrice")
public void setRetailPrice(Double retailPrice) {
this.retailPrice = retailPrice;
 }

/**
* 
* @return
* The costPrice
*/
@JsonProperty("costPrice")
public Double getCostPrice() {
return costPrice;
 }

/**
* 
* @param costPrice
* The costPrice
*/
@JsonProperty("costPrice")
public void setCostPrice(Double costPrice) {
this.costPrice = costPrice;
 }

/**
* 
* @return
* The credit
*/
@JsonProperty("credit")
public Double getCredit() {
return credit;
 }

/**
* 
* @param credit
* The credit
*/
@JsonProperty("credit")
public void setCredit(Double credit) {
this.credit = credit;
 }

/**
* 
* @return
* The discountedPrice
*/
@JsonProperty("discountedPrice")
public Double getDiscountedPrice() {
return discountedPrice;
 }

/**
* 
* @param discountedPrice
* The discountedPrice
*/
@JsonProperty("discountedPrice")
public void setDiscountedPrice(Double discountedPrice) {
this.discountedPrice = discountedPrice;
 }

/**
* 
* @return
* The couponAmount
*/
@JsonProperty("couponAmount")
public Double getCouponAmount() {
return couponAmount;
 }

/**
* 
* @param couponAmount
* The couponAmount
*/
@JsonProperty("couponAmount")
public void setCouponAmount(Double couponAmount) {
this.couponAmount = couponAmount;
 }

/**
* 
* @return
* The orderLineASNDetails
*/
@JsonProperty("orderLineASNDetails")
public List<OrderLineASNDetail> getOrderLineASNDetails() {
return orderLineASNDetails;
 }

/**
* 
* @param orderLineASNDetails
* The orderLineASNDetails
*/
@JsonProperty("orderLineASNDetails")
public void setOrderLineASNDetails(List<OrderLineASNDetail> orderLineASNDetails) {
this.orderLineASNDetails = orderLineASNDetails;
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
