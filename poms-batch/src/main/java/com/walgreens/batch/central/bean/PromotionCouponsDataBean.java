package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.Date;

public class PromotionCouponsDataBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String advEventType; 
	private String advEventSeqNbr;
	private int  advPrintVerNbr;
	private int	advItemCouponNbr;
	private int advEvVerStartDate;
	private int advEvVerEndDate;
	private String advEvVerStatus;
	private int itemNumber;
	
	private Date recvDTTM;

	public String getAdvEventType() {
		return advEventType;
	}
	public void setAdvEventType(String advEventType) {
		this.advEventType = advEventType;
	}

	public String getAdvEventSeqNbr() {
		return advEventSeqNbr;
	}
	public void setAdvEventSeqNbr(String advEventSeqNbr) {
		this.advEventSeqNbr = advEventSeqNbr;
	}

	public int getAdvPrintVerNbr() {
		return advPrintVerNbr;
	}
	public void setAdvPrintVerNbr(int advPrintVerNbr) {
		this.advPrintVerNbr = advPrintVerNbr;
	}

	public int getAdvItemCouponNbr() {
		return advItemCouponNbr;
	}
	public void setAdvItemCouponNbr(int advItemCouponNbr) {
		this.advItemCouponNbr = advItemCouponNbr;
	}

	public int getAdvEvVerStartDate() {
		return advEvVerStartDate;
	}
	public void setAdvEvVerStartDate(int advEvVerStartDate) {
		this.advEvVerStartDate = advEvVerStartDate;
	}

	public int getAdvEvVerEndDate() {
		return advEvVerEndDate;
	}
	public void setAdvEvVerEndDate(int advEvVerEndDate) {
		this.advEvVerEndDate = advEvVerEndDate;
	}

	public String getAdvEvVerStatus() {
		return advEvVerStatus;
	}
	public void setAdvEvVerStatus(String advEvVerStatus) {
		this.advEvVerStatus = advEvVerStatus;
	}
	public Date getRecvDTTM() {
		return recvDTTM;
	}
	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}

	public int getItemNumber() {
		return itemNumber;
	}
	public void setItemNumber(int itemNumber) {
		this.itemNumber = itemNumber;
	}



	@Override
	public String toString() {
		return "PromotionCouponsDataBean [advEventType=" + advEventType
				+ ", advEventSeqNbr=" + advEventSeqNbr + ", advPrintVerNbr="
				+ advPrintVerNbr + ", advItemCouponNbr=" + advItemCouponNbr
				+ ", advEvVerStartDate=" + advEvVerStartDate
				+ ", advEvVerEndDate=" + advEvVerEndDate + ", advEvVerStatus="
				+ advEvVerStatus + ", rcvdDTTM=" + recvDTTM + "]";
	}
}
