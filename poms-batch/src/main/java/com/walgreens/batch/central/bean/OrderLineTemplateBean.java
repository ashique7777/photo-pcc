package com.walgreens.batch.central.bean;

import java.sql.Date;

/**
 * @author CTS
 * 
 */
public class OrderLineTemplateBean {

	private long sysOlTemplateId;
	private long sysOrderLineId;
	private long templateId;
	private int templateQty;
	private double templateSoldAmt;
	private Date orderPlacedDttm;
	private String createUserId;
	private Date createDttm;
	private String updateUserId;
	private Date updatedDttm;

	public long getSysOlTemplateId() {
		return sysOlTemplateId;
	}

	public void setSysOlTemplateId(long sysOlTemplateId) {
		this.sysOlTemplateId = sysOlTemplateId;
	}

	public long getSysOrderLineId() {
		return sysOrderLineId;
	}

	public void setSysOrderLineId(long sysOrderLineId) {
		this.sysOrderLineId = sysOrderLineId;
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

	public Date getOrderPlacedDttm() {
		return orderPlacedDttm;
	}

	public void setOrderPlacedDttm(Date orderPlacedDttm) {
		this.orderPlacedDttm = orderPlacedDttm;
	}

	public Date getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}

	public Date getUpdatedDttm() {
		return updatedDttm;
	}

	public void setUpdatedDttm(Date updatedDttm) {
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

}
