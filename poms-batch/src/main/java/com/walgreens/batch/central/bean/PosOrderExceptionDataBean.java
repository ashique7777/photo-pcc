package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * 
 * @author CTS
 *
 */
public class PosOrderExceptionDataBean {
	
	private long sysOrderExcpId;
	private long sysOrderId;
	private long sysOrderLineId;
	private int exctTypeId;
	private int wasteQty;
	private double wasteCost;
	private String notes;
	private String status;
	private int prevEnvelopeNo;
	private String prevOrderStatus;
	private int wasteCalcCd;
	private Timestamp dateTimeCreated;
	private Timestamp dateTimeModified; 
	private String idCreated;
	private String idModified;
	private String exceptionFlag;
	private String orderPlacedDttm;
	private long owningLocId;
	
	public long getsysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public long getSysOrderLineId() {
		return sysOrderLineId;
	}
	public void setSysOrderLineId(long sysOrderLineId) {
		this.sysOrderLineId = sysOrderLineId;
	}
	public int getWasteQty() {
		return wasteQty;
	}
	public void setWasteQty(int wasteQty) {
		this.wasteQty = wasteQty;
	}
	public double getWasteCost() {
		return wasteCost;
	}
	public void setWasteCost(double wasteCost) {
		this.wasteCost = wasteCost;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPrevEnvelopeNo() {
		return prevEnvelopeNo;
	}
	public void setPrevEnvelopeNo(int prevEnvelopeNo) {
		this.prevEnvelopeNo = prevEnvelopeNo;
	}
	public String getPrevOrderStatus() {
		return prevOrderStatus;
	}
	public void setPrevOrderStatus(String prevOrderStatus) {
		this.prevOrderStatus = prevOrderStatus;
	}
	public int getWasteCalcCd() {
		return wasteCalcCd;
	}
	public void setWasteCalcCd(int wasteCalcCd) {
		this.wasteCalcCd = wasteCalcCd;
	}
	public Timestamp getDateTimeCreated() {
		return dateTimeCreated;
	}
	public void setDateTimeCreated(Timestamp dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}
	public Timestamp getDateTimeModified() {
		return dateTimeModified;
	}
	public void setDateTimeModified(Timestamp dateTimeModified) {
		this.dateTimeModified = dateTimeModified;
	}
	public String getIdCreated() {
		return idCreated;
	}
	public void setIdCreated(String idCreated) {
		this.idCreated = idCreated;
	}
	public String getIdModified() {
		return idModified;
	}
	public void setIdModified(String idModified) {
		this.idModified = idModified;
	}
	public long getSysOrderExcpId() {
		return sysOrderExcpId;
	}
	public void setSysOrderExcpId(long sysOrderExcpId) {
		this.sysOrderExcpId = sysOrderExcpId;
	}
	public int getExctTypeId() {
		return exctTypeId;
	}
	public void setExctTypeId(int exctTypeId) {
		this.exctTypeId = exctTypeId;
	}
	public String getExceptionFlag() {
		return exceptionFlag;
	}
	public void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}
	public String getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	public void setOrderPlacedDttm(String orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	public long getOwningLocId() {
		return owningLocId;
	}
	public void setOwningLocId(long owningLocId) {
		this.owningLocId = owningLocId;
	}
	
	}
