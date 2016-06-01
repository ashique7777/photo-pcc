package com.walgreens.oms.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 * 
 */


public class OrderHistoryBean {

	private long orderId;
	private String action;
	private Timestamp actionDttm;
	private String orderStatus;
	private String actionNotes;
	private long exceptionId;
	private Timestamp orderPlacedDttm;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updateDttm;

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Timestamp getActionDttm() {
		return actionDttm;
	}

	public void setActionDttm(Timestamp actionDttm) {
		this.actionDttm = actionDttm;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getActionNotes() {
		return actionNotes;
	}

	public void setActionNotes(String actionNotes) {
		this.actionNotes = actionNotes;
	}

	public long getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(long sysExceptionId) {
		this.exceptionId = sysExceptionId;
	}

	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Timestamp getUpdateDttm() {
		return updateDttm;
	}

	public void setUpdateDttm(Timestamp updateDttm) {
		this.updateDttm = updateDttm;
	}	

}
