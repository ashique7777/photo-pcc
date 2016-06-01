package com.walgreens.oms.json.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ 
	    "locationNumber", 
	    "posTrnTypeDTTM",
	    "posTrnType",
		"posSoldAmount", 
		"posBusinessDate", 
		"posSequenceNbr", 
		"posRegisterNbr",
		"envelopeNbr", 
		"printsReturned", 
		"posLedgerNbr", 
		"employeeId",
		"posDiscountUsedInd",
		"discountProcessInd" 
		})
public class PosDetails {

	@JsonProperty("locationNumber")
	private String locationNumber;
	@JsonProperty("posTrnTypeDTTM")
	private String posTrnTypeDTTM;
	@JsonProperty("posTrnType")
	private String posTrnType;
	@JsonProperty("posSoldAmount")
	private String posSoldAmount;
	@JsonProperty("posBusinessDate")
	private String posBusinessDate;
	@JsonProperty("posSequenceNbr")
	private String posSequenceNbr;
	@JsonProperty("posRegisterNbr")
	private String posRegisterNbr;
	@JsonProperty("envelopeNbr")
	private String envelopeNbr;
	@JsonProperty("printsReturned")
	private String printsReturned;
	@JsonProperty("posLedgerNbr")
	private String posLedgerNbr;
	@JsonProperty("employeeId")
	private String employeeId;
	@JsonProperty("posDiscountUsedInd")
	private String posDiscountUsedInd;
	@JsonProperty("discountProcessInd")
	private String discountProcessInd;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The LocationNumber
	 */
	@JsonProperty("locationNumber")
	public String getLocationNumber() {
		return locationNumber;
	}

	/**
	 * 
	 * @param LocationNumber
	 *            The LocationNumber
	 */
	@JsonProperty("locationNumber")
	public void setLocationNumber(String locationNumber) {
		this.locationNumber = locationNumber;
	}

	/**
	 * 
	 * @return The PosTrnTypeDTTM
	 */
	@JsonProperty("posTrnTypeDTTM")
	public String getPosTrnTypeDTTM() {
		return posTrnTypeDTTM;
	}

	/**
	 * 
	 * @param PosTrnTypeDTTM
	 *            The PosTrnTypeDTTM
	 */
	@JsonProperty("posTrnTypeDTTM")
	public void setPosTrnTypeDTTM(String posTrnTypeDTTM) {
		this.posTrnTypeDTTM = posTrnTypeDTTM;
	}

	/**
	 * 
	 * @return The PosTrnType
	 */
	@JsonProperty("posTrnType")
	public String getPosTrnType() {
		return posTrnType;
	}

	/**
	 * 
	 * @param PosTrnType
	 *            The PosTrnType
	 */
	@JsonProperty("posTrnType")
	public void setPosTrnType(String posTrnType) {
		this.posTrnType = posTrnType;
	}

	/**
	 * 
	 * @return The PosSoldAmount
	 */
	@JsonProperty("posSoldAmount")
	public String getPosSoldAmount() {
		return posSoldAmount;
	}

	/**
	 * 
	 * @param PosSoldAmount
	 *            The PosSoldAmount
	 */
	@JsonProperty("posSoldAmount")
	public void setPosSoldAmount(String posSoldAmount) {
		this.posSoldAmount = posSoldAmount;
	}

	/**
	 * 
	 * @return The PosBusinessDate
	 */
	@JsonProperty("posBusinessDate")
	public String getPosBusinessDate() {
		return posBusinessDate;
	}

	/**
	 * 
	 * @param PosBusinessDate
	 *            The PosBusinessDate
	 */
	@JsonProperty("posBusinessDate")
	public void setPosBusinessDate(String posBusinessDate) {
		this.posBusinessDate = posBusinessDate;
	}

	/**
	 * 
	 * @return The PosSequenceNbr
	 */
	@JsonProperty("posSequenceNbr")
	public String getPosSequenceNbr() {
		return posSequenceNbr;
	}

	/**
	 * 
	 * @param PosSequenceNbr
	 *            The PosSequenceNbr
	 */
	@JsonProperty("posSequenceNbr")
	public void setPosSequenceNbr(String posSequenceNbr) {
		this.posSequenceNbr = posSequenceNbr;
	}

	/**
	 * 
	 * @return The PosRegisterNbr
	 */
	@JsonProperty("posRegisterNbr")
	public String getPosRegisterNbr() {
		return posRegisterNbr;
	}

	/**
	 * 
	 * @param PosRegisterNbr
	 *            The PosRegisterNbr
	 */
	@JsonProperty("posRegisterNbr")
	public void setPosRegisterNbr(String posRegisterNbr) {
		this.posRegisterNbr = posRegisterNbr;
	}

	/**
	 * 
	 * @return The EnvelopeNbr
	 */
	@JsonProperty("envelopeNbr")
	public String getEnvelopeNbr() {
		return envelopeNbr;
	}

	/**
	 * 
	 * @param EnvelopeNbr
	 *            The EnvelopeNbr
	 */
	@JsonProperty("envelopeNbr")
	public void setEnvelopeNbr(String envelopeNbr) {
		this.envelopeNbr = envelopeNbr;
	}

	/**
	 * 
	 * @return The PrintsReturned
	 */
	@JsonProperty("printsReturned")
	public String getPrintsReturned() {
		return printsReturned;
	}

	/**
	 * 
	 * @param PrintsReturned
	 *            The PrintsReturned
	 */
	@JsonProperty("printsReturned")
	public void setPrintsReturned(String printsReturned) {
		this.printsReturned = printsReturned;
	}

	/**
	 * 
	 * @return The PosLedgerNbr
	 */
	@JsonProperty("posLedgerNbr")
	public String getPosLedgerNbr() {
		return posLedgerNbr;
	}

	/**
	 * 
	 * @param PosLedgerNbr
	 *            The PosLedgerNbr
	 */
	@JsonProperty("posLedgerNbr")
	public void setPosLedgerNbr(String posLedgerNbr) {
		this.posLedgerNbr = posLedgerNbr;
	}

	/**
	 * 
	 * @return The EmployeeId
	 */
	@JsonProperty("employeeId")
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * 
	 * @param EmployeeId
	 *            The EmployeeId
	 */
	@JsonProperty("employeeId")
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * 
	 * @return The PosDiscountUsedInd
	 */
	@JsonProperty("posDiscountUsedInd")
	public String getPosDiscountUsedInd() {
		return posDiscountUsedInd;
	}

	/**
	 * 
	 * @param PosDiscountUsedInd
	 *            The PosDiscountUsedInd
	 */
	@JsonProperty("posDiscountUsedInd")
	public void setPosDiscountUsedInd(String posDiscountUsedInd) {
		this.posDiscountUsedInd = posDiscountUsedInd;
	}

	/**
	 * 
	 * @return The DiscountProcessInd
	 */
	@JsonProperty("discountProcessInd")
	public String getDiscountProcessInd() {
		return discountProcessInd;
	}

	/**
	 * 
	 * @param DiscountProcessInd
	 *            The DiscountProcessInd
	 */
	@JsonProperty("discountProcessInd")
	public void setDiscountProcessInd(String discountProcessInd) {
		this.discountProcessInd = discountProcessInd;
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
