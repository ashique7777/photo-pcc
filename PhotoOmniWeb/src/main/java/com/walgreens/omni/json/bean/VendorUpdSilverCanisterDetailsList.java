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
"VendorUpdSilverCanisterDetails"
})

public class VendorUpdSilverCanisterDetailsList {@JsonProperty("VendorUpdSilverCanisterDetails")
private VendorUpdSilverCanisterDetails VendorUpdSilverCanisterDetails;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The VendorUpdSilverCanisterDetails
*/
@JsonProperty("VendorUpdSilverCanisterDetails")
public VendorUpdSilverCanisterDetails getVendorUpdSilverCanisterDetails() {
return VendorUpdSilverCanisterDetails;
 }

/**
* 
* @param VendorUpdSilverCanisterDetails
* The VendorUpdSilverCanisterDetails
*/
@JsonProperty("VendorUpdSilverCanisterDetails")
public void setVendorUpdSilverCanisterDetails(VendorUpdSilverCanisterDetails VendorUpdSilverCanisterDetails) {
this.VendorUpdSilverCanisterDetails = VendorUpdSilverCanisterDetails;
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
