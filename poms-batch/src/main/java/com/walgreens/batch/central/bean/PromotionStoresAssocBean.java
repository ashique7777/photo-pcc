package com.walgreens.batch.central.bean;

import java.io.Serializable;

public class PromotionStoresAssocBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String advEventType; 
	private String advEventSeqNbr;
	private String adEventRelInd;
	private String rcvdDTTM;
    private String storeNBR;
    private int pluNBR;
    private String advEvVerStartDate;
	private String advEvVerEndDate;

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

	public String getRcvdDTTM() {
		return rcvdDTTM;
	}
	public void setRcvdDTTM(String rcvdDTTM) {
		this.rcvdDTTM = rcvdDTTM;
	}
	
	public String getStoreNBR() {
		return storeNBR;
	}
	public void setStoreNBR(String storeNBR) {
		this.storeNBR = storeNBR;
	}
	
	public int getPluNBR() {
		return pluNBR;
	}
	public void setPluNBR(int pluNBR) {
		this.pluNBR = pluNBR;
	}
	
	public String getAdvEvVerStartDate() {
		return advEvVerStartDate;
	}
	public void setAdvEvVerStartDate(String advEvVerStartDate) {
		this.advEvVerStartDate = advEvVerStartDate;
	}
	
	public String getAdvEvVerEndDate() {
		return advEvVerEndDate;
	}
	public void setAdvEvVerEndDate(String advEvVerEndDate) {
		this.advEvVerEndDate = advEvVerEndDate;
	}
	
	@Override
	public String toString() {
		return "PromotionStoresAssocBean [advEventType=" + advEventType
				+ ", advEventSeqNbr=" + advEventSeqNbr + ", adEventRelInd="
				+ adEventRelInd + ", rcvdDTTM=" + rcvdDTTM + ", storeNBR="
				+ storeNBR + ", pluNBR=" + pluNBR + ", advEvVerStartDate="
				+ advEvVerStartDate + ", advEvVerEndDate=" + advEvVerEndDate
				+ "]";
	}

	
}
