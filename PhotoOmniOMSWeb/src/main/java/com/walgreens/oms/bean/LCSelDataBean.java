package com.walgreens.oms.bean;

import java.sql.Timestamp;

public class LCSelDataBean {
	
	private long orderlineId;
	private Timestamp orderPlacedDttm;
	public long getOrderlineId() {
		return orderlineId;
	}
	public void setOrderlineId(long orderlineId) {
		this.orderlineId = orderlineId;
	}
	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}
	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}
	
	
	

}
