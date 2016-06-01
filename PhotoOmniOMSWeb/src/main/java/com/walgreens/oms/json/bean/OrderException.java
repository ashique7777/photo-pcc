/**
 * 
 */
package com.walgreens.oms.json.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author CTS
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
"exceptionRecordId",
"exceptionTime",
"productId",
"exceptionIdSeq",
"processingType",
"wasteQty",
"wasteRetailAmount",
"orderNotes",
"oldEnvelopeNbr",
"oldOrderStatus",
"exceptionEmployeeId"
})
public class OrderException {
	
	@JsonProperty("exceptionRecordId")private String exceptionRecordId;  
	@JsonProperty("exceptionTime")private String orderCancelDateTime; 
	@JsonProperty("productId")private String productId;  
	@JsonProperty("exceptionIdSeq")private String exceptionIdSeq;  
	@JsonProperty("processingType")private String processingType;  
	@JsonProperty("wasteQty")private String wasteQty;  	
	@JsonProperty("wasteRetailAmount")private String wasteRetailAmount;  	
	@JsonProperty("orderNotes")private String orderNotes;  	
	@JsonProperty("oldEnvelopeNbr")private String oldEnvelopeNbr;  	
	@JsonProperty("oldOrderStatus")private String oldOrderStatus;
	@JsonProperty("exceptionEmployeeId")private String exceptionEmployeeId;
	
	
	
	/**
	 * @return the exceptionRecordId
	 */
	@JsonProperty("exceptionRecordId")
	public String getExceptionRecordId() {
		return exceptionRecordId;
	}
	
	/**
	 * @param exceptionRecordId the exceptionRecordId to set
	 */
	@JsonProperty("exceptionRecordId")
	public void setExceptionRecordId(String exceptionRecordId) {
		this.exceptionRecordId = exceptionRecordId;
	}
	
	/**
	 * @return the productId
	 */
	@JsonProperty("productId")
	public String getProductId() {
		return productId;
	}
	
	/**
	 * @param productId the productId to set
	 */
	@JsonProperty("productId")
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * @return the exceptionIdSeq
	 */
	@JsonProperty("exceptionIdSeq")
	public String getExceptionIdSeq() {
		return exceptionIdSeq;
	}
	
	/**
	 * @param exceptionIdSeq the exceptionIdSeq to set
	 */
	@JsonProperty("exceptionIdSeq")
	public void setExceptionIdSeq(String exceptionIdSeq) {
		this.exceptionIdSeq = exceptionIdSeq;
	}
	/**
	 * @return the processingType
	 */
	@JsonProperty("processingType")
	public String getProcessingType() {
		return processingType;
	}
	/**
	 * @param processingType the processingType to set
	 */
	@JsonProperty("processingType")
	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}
	
	/**
	 * @return the wasteQty
	 */
	@JsonProperty("wasteQty")
	public String getWasteQty() {
		return wasteQty;
	}
	/**
	 * @param wasteQty the wasteQty to set
	 */
	@JsonProperty("wasteQty")
	public void setWasteQty(String wasteQty) {
		this.wasteQty = wasteQty;
	}
	
	/**
	 * @return the wasteRetailAmount
	 */
	@JsonProperty("wasteRetailAmount")
	public String getWasteRetailAmount() {
		return wasteRetailAmount;
	}
	
	/**
	 * @param wasteRetailAmount the wasteRetailAmount to set
	 */
	@JsonProperty("wasteRetailAmount")
	public void setWasteRetailAmount(String wasteRetailAmount) {
		this.wasteRetailAmount = wasteRetailAmount;
	}
	
	
	/**
	 * @return the orderNotes
	 */
	@JsonProperty("orderNotes")
	public String getOrderNotes() {
		return orderNotes;
	}
	
	/**
	 * @param orderNotes the orderNotes to set
	 */
	@JsonProperty("orderNotes")
	public void setOrderNotes(String orderNotes) {
		this.orderNotes = orderNotes;
	}
	
	/**
	 * @return the oldEnvelopeNbr
	 */
	@JsonProperty("oldEnvelopeNbr")
	public String getOldEnvelopeNbr() {
		return oldEnvelopeNbr;
	}
	
	/**
	 * @param oldEnvelopeNbr the oldEnvelopeNbr to set
	 */
	@JsonProperty("oldEnvelopeNbr")
	public void setOldEnvelopeNbr(String oldEnvelopeNbr) {
		this.oldEnvelopeNbr = oldEnvelopeNbr;
	}
	
	/**
	 * @return the oldOrderStatus
	 */
	@JsonProperty("oldOrderStatus")
	public String getOldOrderStatus() {
		return oldOrderStatus;
	}
	
	/**
	 * @param oldOrderStatus the oldOrderStatus to set
	 */
	@JsonProperty("oldOrderStatus")
	public void setOldOrderStatus(String oldOrderStatus) {
		this.oldOrderStatus = oldOrderStatus;
	}
	/**
	 * @return the exceptionEmployeeId
	 */
	public String getExceptionEmployeeId() {
		return exceptionEmployeeId;
	}

	/**
	 * @param exceptionEmployeeId the exceptionEmployeeId to set
	 */
	public void setExceptionEmployeeId(String exceptionEmployeeId) {
		this.exceptionEmployeeId = exceptionEmployeeId;
	}

	
	
	/**
	 * @return the orderCancelDateTime
	 */
	public String getOrderCancelDateTime() {
		return orderCancelDateTime;
	}

	/**
	 * @param orderCancelDateTime the orderCancelDateTime to set
	 */
	public void setOrderCancelDateTime(String orderCancelDateTime) {
		this.orderCancelDateTime = orderCancelDateTime;
	}

	@Override
	public String toString() {
		return "OrderException [exceptionRecordId=" + exceptionRecordId
				+ ", productId=" + productId + ", exceptionIdSeq="
				+ exceptionIdSeq + ", processingType=" + processingType
				+ ", wasteQty=" + wasteQty + ", wasteRetailAmount="
				+ wasteRetailAmount + ", orderNotes=" + orderNotes
				+ ", oldEnvelopeNbr=" + oldEnvelopeNbr + ", oldOrderStatus="
				+ oldOrderStatus + ", exceptionEmployeeId="
				+ exceptionEmployeeId + "]";
	}
	
	
	

}
