package com.walgreens.oms.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 * 
 */

public class OrderLineTemplateBean {

	private long sysOlTemplateId;
	private long orderLineId;
	private long templateId;
	private int templateQty;
	private double templateSoldAmt;
	private Timestamp orderPlacedDttm;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updatedDttm;

	public long getSysOlTemplateId() {
		return sysOlTemplateId;
	}

	public void setSysOlTemplateId(long sysOlTemplateId) {
		this.sysOlTemplateId = sysOlTemplateId;
	}

	public long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(long orderLineId) {
		this.orderLineId = orderLineId;
	}

	public long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public int getTemplateQty() {
		return templateQty;
	}

	public void setTemplateQty(int templateQty) {
		this.templateQty = templateQty;
	}

	public double getTemplateSoldAmt() {
		return templateSoldAmt;
	}

	public void setTemplateSoldAmt(double templateSoldAmt) {
		this.templateSoldAmt = templateSoldAmt;
	}

	public Timestamp getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	public void setOrderPlacedDttm(Timestamp orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public Timestamp getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}

	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}

	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
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

	@Override
	public String toString() {
		return "OrderLineTemplateBean [sysOlTemplateId=" + sysOlTemplateId
				+ ", orderLineId=" + orderLineId + ", templateId=" + templateId
				+ ", templateQty=" + templateQty + ", templateSoldAmt="
				+ templateSoldAmt + ", orderPlacedDttm=" + orderPlacedDttm
				+ ", createUserId=" + createUserId + ", createDttm="
				+ createDttm + ", updateUserId=" + updateUserId
				+ ", updatedDttm=" + updatedDttm + "]";
	}

}
