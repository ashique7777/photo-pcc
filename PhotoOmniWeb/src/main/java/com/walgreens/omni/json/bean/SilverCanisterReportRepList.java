package com.walgreens.omni.json.bean;

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
"SilverCanisterDetails"
})
public class SilverCanisterReportRepList {

@JsonProperty("SilverCanisterDetails")
private com.walgreens.omni.json.bean.SilverCanisterDetails SilverCanisterDetails;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The SilverCanisterDetails
*/
@JsonProperty("SilverCanisterDetails")
public com.walgreens.omni.json.bean.SilverCanisterDetails getSilverCanisterDetails() {
return SilverCanisterDetails;
 }

/**
* 
* @param SilverCanisterDetails
* The SilverCanisterDetails
*/
@JsonProperty("SilverCanisterDetails")
public void setSilverCanisterDetails(com.walgreens.omni.json.bean.SilverCanisterDetails SilverCanisterDetails) {
this.SilverCanisterDetails = SilverCanisterDetails;
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
