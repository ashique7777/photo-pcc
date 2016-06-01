package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * This bean is populated by calling the following 
 * SilverCanisterQuery:populateSilvCanDBQuery()
 * @author Cognizant
 *
 */
public class SilverCanisterDataBean {
	
	String orderNbr;
	String serviceCatCode;
	String sysOrderId;
	String sysProdId;
	String sysProdDimId;
	String length;
	String width;
	int sysMacInstId;
	int sysMacTypeId;
	String totalPrintsFactor;
	int wastedQty;
	float rollFactor;
	int rollCount;
	int printCount;
	int printInSqInchs;
	int silverRecvRolls;
	int silverRecvPrints;
	String canisterStatus;
	int locationNo;
	String locationType;
	Timestamp canisterCalcDate;
	int transferCD;
	
	/**
	 * @return
	 */
	public String getOrderNbr() {
		return orderNbr;
	}
	/**
	 * @param orderNbr
	 */
	public void setOrderNbr(String orderNbr) {
		this.orderNbr = orderNbr;
	}
	/**
	 * @return
	 */
	public String getServiceCatCode() {
		return serviceCatCode;
	}
	/**
	 * @param serviceCatCode
	 */
	public void setServiceCatCode(String serviceCatCode) {
		this.serviceCatCode = serviceCatCode;
	}
	/**
	 * @return
	 */
	public String getSysOrderId() {
		return sysOrderId;
	}
	/**
	 * @param sysOrderId
	 */
	public void setSysOrderId(String sysOrderId) {
		this.sysOrderId = sysOrderId;
	}
	/**
	 * @return
	 */
	public String getSysProdId() {
		return sysProdId;
	}
	/**
	 * @param sysProdId
	 */
	public void setSysProdId(String sysProdId) {
		this.sysProdId = sysProdId;
	}
	/**
	 * @return
	 */
	public String getSysProdDimId() {
		return sysProdDimId;
	}
	/**
	 * @param sysProdDimId
	 */
	public void setSysProdDimId(String sysProdDimId) {
		this.sysProdDimId = sysProdDimId;
	}
	/**
	 * @return
	 */
	public String getLength() {
		return length;
	}
	/**
	 * @param length
	 */
	public void setLength(String length) {
		this.length = length;
	}
	/**
	 * @return
	 */
	public String getWidth() {
		return width;
	}
	/**
	 * @param width
	 */
	public void setWidth(String width) {
		this.width = width;
	}
	/**
	 * @return
	 */
	public int getSysMacInstId() {
		return sysMacInstId;
	}
	/**
	 * @param sysMacInstId
	 */
	public void setSysMacInstId(int sysMacInstId) {
		this.sysMacInstId = sysMacInstId;
	}
	/**
	 * @return
	 */
	public int getSysMacTypeId() {
		return sysMacTypeId;
	}
	/**
	 * @param sysMacTypeId
	 */
	public void setSysMacTypeId(int sysMacTypeId) {
		this.sysMacTypeId = sysMacTypeId;
	}
	/**
	 * @return
	 */
	public String getTotalPrintsFactor() {
		return totalPrintsFactor;
	}
	/**
	 * @param totalPrintsFactor
	 */
	public void setTotalPrintsFactor(String totalPrintsFactor) {
		this.totalPrintsFactor = totalPrintsFactor;
	}
	/**
	 * @return
	 */
	public int getWastedQty() {
		return wastedQty;
	}
	/**
	 * @param wastedQty
	 */
	public void setWastedQty(int wastedQty) {
		this.wastedQty = wastedQty;
	}
	/**
	 * @return
	 */
	public float getRollFactor() {
		return rollFactor;
	}
	/**
	 * @param rollFactor
	 */
	public void setRollFactor(float rollFactor) {
		this.rollFactor = rollFactor;
	}
	/**
	 * @return
	 */
	public int getRollCount() {
		return rollCount;
	}
	/**
	 * @param rollCount
	 */
	public void setRollCount(int rollCount) {
		this.rollCount = rollCount;
	}
	/**
	 * @return
	 */
	public int getPrintCount() {
		return printCount;
	}
	/**
	 * @param printCount
	 */
	public void setPrintCount(int printCount) {
		this.printCount = printCount;
	}
	/**
	 * @return
	 */
	public int getPrintInSqInchs() {
		return printInSqInchs;
	}
	/**
	 * @param printInSqInchs
	 */
	public void setPrintInSqInchs(int printInSqInchs) {
		this.printInSqInchs = printInSqInchs;
	}
	/**
	 * @return
	 */
	public int getSilverRecvRolls() {
		return silverRecvRolls;
	}
	/**
	 * @param silverRecvRolls
	 */
	public void setSilverRecvRolls(int silverRecvRolls) {
		this.silverRecvRolls = silverRecvRolls;
	}
	/**
	 * @return
	 */
	public int getSilverRecvPrints() {
		return silverRecvPrints;
	}
	/**
	 * @param silverRecvPrints
	 */
	public void setSilverRecvPrints(int silverRecvPrints) {
		this.silverRecvPrints = silverRecvPrints;
	}
	/**
	 * @return
	 */
	public String getCanisterStatus() {
		return canisterStatus;
	}
	/**
	 * @param canisterStatus
	 */
	public void setCanisterStatus(String canisterStatus) {
		this.canisterStatus = canisterStatus;
	}
	/**
	 * @return
	 */
	public int getLocationNo() {
		return locationNo;
	}
	/**
	 * @param locationNo
	 */
	public void setLocationNo(int locationNo) {
		this.locationNo = locationNo;
	}
	/**
	 * @return
	 */
	public String getLocationType() {
		return locationType;
	}
	/**
	 * @param locationType
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	/**
	 * @return
	 */
	public Timestamp getCanisterCalcDate() {
		return canisterCalcDate;
	}
	/**
	 * @param canisterCalcDate
	 */
	public void setCanisterCalcDate(Timestamp canisterCalcDate) {
		this.canisterCalcDate = canisterCalcDate;
	}
	/**
	 * @return
	 */
	public int getTransferCD() {
		return transferCD;
	}
	/**
	 * @param transferCD
	 */
	public void setTransferCD(int transferCD) {
		this.transferCD = transferCD;
	}
	

}
