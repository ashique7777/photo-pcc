/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CTS
 *
 */
public class PromotionHeaderDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String advEventType; 
	private String advEventSeqNbr;
	private String adEventRelInd;
	private int adEventDateStmp;
	private int adEventTimeStmp;
	private String adEventUserStmp;
	private Date recvDTTM;
	private String adEventPrcsInd; 

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

	public String getAdEventRelInd() {
		return adEventRelInd;
	}
	public void setAdEventRelInd(String adEventRelInd) {
		this.adEventRelInd = adEventRelInd;
	}
	
	public Date getRecvDTTM() {
		return recvDTTM;
	}
	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	}
	
	public int getAdEventDateStmp() {
		return adEventDateStmp;
	}
	public void setAdEventDateStmp(int adEventDateStmp) {
		this.adEventDateStmp = adEventDateStmp;
	}
	public int getAdEventTimeStmp() {
		return adEventTimeStmp;
	}
	public void setAdEventTimeStmp(int adEventTimeStmp) {
		this.adEventTimeStmp = adEventTimeStmp;
	}
	public String getAdEventUserStmp() {
		return adEventUserStmp;
	}
	public void setAdEventUserStmp(String adEventUserStmp) {
		this.adEventUserStmp = adEventUserStmp;
	}
	
	public String getAdEventPrcsInd() {
		return adEventPrcsInd;
	}
	public void setAdEventPrcsInd(String adEventPrcsInd) {
		this.adEventPrcsInd = adEventPrcsInd;
	}
	@Override
	public String toString() {
		return "PromotionHeaderDataBean [advEventType=" + advEventType
				+ ", advEventSeqNbr=" + advEventSeqNbr + ", adEventRelInd="
				+ adEventRelInd + ", adEventDateStmp=" + adEventDateStmp
				+ ", adEventTimeStmp=" + adEventTimeStmp + ", adEventUserStmp="
				+ adEventUserStmp + ", adEventPrcsInd=" + adEventUserStmp 
				+ ", recvDTTM=" + recvDTTM + "]";
	}
	
}
