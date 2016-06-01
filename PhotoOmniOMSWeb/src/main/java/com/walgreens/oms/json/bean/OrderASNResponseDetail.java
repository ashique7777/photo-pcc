package com.walgreens.oms.json.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.walgreens.common.utility.ErrorDetails;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"locationType",
"locationNumber",
"pcpOrderId",
"envelopeNumber",
"referenceId",
"orderPlacedDttm",
"status",
"errorDetails"
})
public class OrderASNResponseDetail {

@JsonProperty("locationType")
private String locationType;
@JsonProperty("locationNumber")
private Integer locationNumber;
@JsonProperty("pcpOrderId")
private long pcpOrderId;
private String envelopeNumber;
@JsonProperty("referenceId")
private String referenceId;
@JsonProperty("orderPlacedDttm")
private Timestamp orderPlacedDttm;
@JsonProperty("status")
private Boolean status;
@JsonProperty("errorDetails")
private ErrorDetails errorDetails;


@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
* @param l
* The pcpOrderId
*/
@JsonProperty("pcpOrderId")
public void setPcpOrderId(long l) {
this.pcpOrderId = l;
 }

/**
* 
* @return
* The envelopeNumber
*/
public String getEnvelopeNumber() {
return envelopeNumber;
 }

/**
* 
* @param envelopeNumber
* The envelopeNumber
*/
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
* The status
*/
@JsonProperty("status")
public Boolean getStatus() {
return status;
 }
@JsonProperty("orderPlacedDttm")
public Timestamp getOrderPlacedDttm() {
	return orderPlacedDttm;
}
@JsonProperty("orderPlacedDttm")
public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
	this.orderPlacedDttm = orderPlacedDttm;
}

/**
* 
* @param status
* The status
*/
@JsonProperty("status")
public void setStatus(Boolean status) {
this.status = status;
 }

/**
* 
* @return
* The errorDetails
*/
@JsonProperty("errorDetails")
public ErrorDetails getErrorDetails() {
return errorDetails;
 }

/**
* 
* @param errorDetails
* The errorDetails
*/
@JsonProperty("errorDetails")
public void setErrorDetails(ErrorDetails errorDetails) {
this.errorDetails = errorDetails;
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
