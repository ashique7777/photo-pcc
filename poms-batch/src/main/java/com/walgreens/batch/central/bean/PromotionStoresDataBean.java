package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.util.Date;

public class PromotionStoresDataBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String advEventType; 
	private String advEventSeqNbr;
	private String locationType; 
	private int advLocationNbr;
	private int advPrintVerNbr ;
	
	public int getAdvPrintVerNbr() {
		return advPrintVerNbr;
	}
	public void setAdvPrintVerNbr(int advPrintVerNbr) {
		this.advPrintVerNbr = advPrintVerNbr;
	}

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

	public String getLocationType() {
		return locationType;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public int getAdvLocationNbr() {
		return advLocationNbr;
	}
	public void setAdvLocationNbr(int advLocationNbr) {
		this.advLocationNbr = advLocationNbr;
	}

	public Date getRecvDTTM() {
		return recvDTTM;
	}
	public void setRecvDTTM(Date recvDTTM) {
		this.recvDTTM = recvDTTM;
	} 
	
	@Override
	public String toString() {
		return "PromotionStoresDataBean [advEventType=" + advEventType
				+ ", advEventSeqNbr=" + advEventSeqNbr + ", locationType="
				+ locationType + ", advLocationNumber=" + advLocationNbr
				+ ", rcvdDTTM=" + recvDTTM + "]";
	}
}
