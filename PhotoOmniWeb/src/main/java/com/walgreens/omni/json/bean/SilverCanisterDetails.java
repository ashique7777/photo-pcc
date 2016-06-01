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
"Store",
"LastCanisterChangeDate",
"RollsCount",
"PrintsCount",
"SilverContentRolls",
"SilverContentPrints",
"PaperSquereInch",
"TotalSilver"
})
public class SilverCanisterDetails {

@JsonProperty("Store")
private String Store;
@JsonProperty("LastCanisterChangeDate")
private String LastCanisterChangeDate;
@JsonProperty("RollsCount")
private String RollsCount;
@JsonProperty("PrintsCount")
private String PrintsCount;
@JsonProperty("SilverContentRolls")
private String SilverContentRolls;
@JsonProperty("SilverContentPrints")
private String SilverContentPrints;
@JsonProperty("TotalSilver")
private String TotalSilver;
@JsonProperty("PaperSquereInch")
private String PaperSquereInch ;

@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The Store
*/
@JsonProperty("Store")
public String getStore() {
return Store;
 }

/**
* 
* @param Store
* The Store
*/
@JsonProperty("Store")
public void setStore(String Store) {
this.Store = Store;
 }

/**
* 
* @return
* The LastCanisterChangeDate
*/
@JsonProperty("LastCanisterChangeDate")
public String getLastCanisterChangeDate() {
return LastCanisterChangeDate;
 }

/**
* 
* @param LastCanisterChangeDate
* The LastCanisterChangeDate
*/
@JsonProperty("LastCanisterChangeDate")
public void setLastCanisterChangeDate(String LastCanisterChangeDate) {
this.LastCanisterChangeDate = LastCanisterChangeDate;
 }

/**
* 
* @return
* The RollsCount
*/
@JsonProperty("RollsCount")
public String getRollsCount() {
return RollsCount;
 }

/**
* 
* @param RollsCount
* The RollsCount
*/
@JsonProperty("RollsCount")
public void setRollsCount(String RollsCount) {
this.RollsCount = RollsCount;
 }

/**
* 
* @return
* The PrintsCount
*/
@JsonProperty("PrintsCount")
public String getPrintsCount() {
return PrintsCount;
 }

/**
* 
* @param PrintsCount
* The PrintsCount
*/
@JsonProperty("PrintsCount")
public void setPrintsCount(String PrintsCount) {
this.PrintsCount = PrintsCount;
 }

/**
* 
* @return
* The SilverContentRolls
*/
@JsonProperty("SilverContentRolls")
public String getSilverContentRolls() {
return SilverContentRolls;
 }

/**
* 
* @param SilverContentRolls
* The SilverContentRolls
*/
@JsonProperty("SilverContentRolls")
public void setSilverContentRolls(String SilverContentRolls) {
this.SilverContentRolls = SilverContentRolls;
 }

/**
* 
* @return
* The SilverContentPrints
*/
@JsonProperty("SilverContentPrints")
public String getSilverContentPrints() {
return SilverContentPrints;
 }

/**
* 
* @param SilverContentPrints
* The SilverContentPrints
*/
@JsonProperty("SilverContentPrints")
public void setSilverContentPrints(String SilverContentPrints) {
this.SilverContentPrints = SilverContentPrints;
 }

/**
* 
* @return
* The TotalSilver
*/
@JsonProperty("TotalSilver")
public String getTotalSilver() {
return TotalSilver;
 }

/**
* 
* @param TotalSilver
* The TotalSilver
*/
@JsonProperty("TotalSilver")
public void setTotalSilver(String TotalSilver) {
this.TotalSilver = TotalSilver;
 }


/**
* 
* @return
* The PaperSquereInch
*/
@JsonProperty("PaperSquereInch")
public String getPaperSquereInch() {
return PaperSquereInch;
 }

/**
* 
* @param paperSquereInch
* The paperSquereInch
*/
@JsonProperty("PaperSquereInch")
public void setPaperSquereInch(String PaperSquereInch) {
this.PaperSquereInch = PaperSquereInch;
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

