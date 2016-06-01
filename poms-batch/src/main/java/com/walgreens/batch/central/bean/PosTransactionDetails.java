package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 *
 */
public class PosTransactionDetails {

	
	private String businessDate;
	private long sysStorePosId;
	private long sysOrderId;
	private long sysLocationId;
	private String transactionDttm;
	private String transactionTypeCd;
	private double soldAmt;	
	private String posSequenceNo;
	private String registerNo;
	private int envelopNo;
	private String processingInd;
	private int returnedQty;
	private int posLedgerNo;
	private String employeeId;
	private String discountUsedInd;
	private int locationNbr;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updateDttm;
	
	
	public long getSysStorePosId() {
		return sysStorePosId;
	}
	public void setSysStorePosId(long sysStorePosId) {
		this.sysStorePosId = sysStorePosId;
	}
	public long getSysLocationId() {
		return sysLocationId;
	}
	public void setSysLocationId(long sysLocationId) {
		this.sysLocationId = sysLocationId;
	}

	public double getSoldAmt() {
		return soldAmt;
	}
	public void setSoldAmt(double soldAmt) {
		this.soldAmt = soldAmt;
	}
	public String getBusinessDate() {
		return businessDate;
	}
	public void setBusinessDate(String businessDate) {
		this.businessDate = businessDate;
	}
	public String getPosSequenceNo() {
		return posSequenceNo;
	}
	public void setPosSequenceNo(String posSequenceNo) {
		this.posSequenceNo = posSequenceNo;
	}
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	public int getEnvelopNo() {
		return envelopNo;
	}
	public void setEnvelopNo(int envelopNo) {
		this.envelopNo = envelopNo;
	}
	public String getProcessingInd() {
		return processingInd;
	}
	public void setProcessingInd(String processingInd) {
		this.processingInd = processingInd;
	}
	public int getReturnedQty() {
		return returnedQty;
	}
	public void setReturnedQty(int returnedQty) {
		this.returnedQty = returnedQty;
	}
	public int getPosLedgerNo() {
		return posLedgerNo;
	}
	public void setPosLedgerNo(int posLedgerNo) {
		this.posLedgerNo = posLedgerNo;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public Timestamp getCreateDttm() {
		return createDttm;
	}
	public void setCreateDttm(Timestamp timestamp) {
		this.createDttm = timestamp;
	}
	
	public Timestamp getUpdateDttm() {
		return updateDttm;
	}
	public void setUpdateDttm(Timestamp updateDttm) {
		this.updateDttm = updateDttm;
	}
	public long getSysOrderId() {
		return sysOrderId;
	}
	public void setSysOrderId(long sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	public String getTransactionDttm() {
		return transactionDttm;
	}
	public void setTransactionDttm(String transactionDttm) {
		this.transactionDttm = transactionDttm;
	}
	public String getTransactionTypeCd() {
		return transactionTypeCd;
	}
	public void setTransactionTypeCd(String transactionTypeCd) {
		this.transactionTypeCd = transactionTypeCd;
	}
	public String getDiscountUsedInd() {
		return discountUsedInd;
	}
	public void setDiscountUsedInd(String discountUsedInd) {
		this.discountUsedInd = discountUsedInd;
	}
	
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public int getLocationNbr() {
		return locationNbr;
	}
	public void setLocationNbr(int locationNbr) {
		this.locationNbr = locationNbr;
	}
		
}
