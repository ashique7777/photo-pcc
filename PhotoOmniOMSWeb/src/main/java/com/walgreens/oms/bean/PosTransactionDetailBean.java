package com.walgreens.oms.bean;

public class PosTransactionDetailBean {
	
	private long sysStorePosId;
	private String posTransactionType;
	
	
	public long getSysStorePosId() {
		return sysStorePosId;
	}
	public void setSysStorePosId(long sysStorePosId) {
		this.sysStorePosId = sysStorePosId;
	}
	public String getPosTransactionType() {
		return posTransactionType;
	}
	public void setPosTransactionType(String posTransactionType) {
		this.posTransactionType = posTransactionType;
	}
	

}
