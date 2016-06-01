package com.walgreens.batch.central.bean;

import java.sql.Date;


/**
 * @author CTS
 */
public class LicenceContentPosBean {

	private long sysOlLcId;
	private long sysOrderLineId;
	private String vendorLcId;
	private String vendorLcDesc;
	private int vendorLcQty;
	private double finalLcPrice;
	private Date downloadedDttm;
	private String downloadedInd;
	private double lcAmountPaid;
	private int originalLcPrice;
	private Date originalPlaceDttm;
	private String createUserId;
	private Date createDttm;
	private String updateUserId;
	private Date updatedDttm;
	private int VendorLcDiscountAmt;

	public long getSysOlLcId() {
		return sysOlLcId;
	}

	public void setSysOlLcId(long sysOlLcId) {
		this.sysOlLcId = sysOlLcId;
	}

	public long getSysOrderLineId() {
		return sysOrderLineId;
	}

	public void setSysOrderLineId(long sysOrderLineId) {
		this.sysOrderLineId = sysOrderLineId;
	}

	public String getVendorLcId() {
		return vendorLcId;
	}

	public void setVendorLcId(String vendorLcId) {
		this.vendorLcId = vendorLcId;
	}

	public String getVendorLcDesc() {
		return vendorLcDesc;
	}

	public void setVendorLcDesc(String vendorLcDesc) {
		this.vendorLcDesc = vendorLcDesc;
	}

	public int getVendorLcQty() {
		return vendorLcQty;
	}

	public void setVendorLcQty(int vendorLcQty) {
		this.vendorLcQty = vendorLcQty;
	}

	public double getFinalLcPrice() {
		return finalLcPrice;
	}

	public void setFinalLcPrice(double finalLcPrice) {
		this.finalLcPrice = finalLcPrice;
	}

	public Date getDownloadedDttm() {
		return downloadedDttm;
	}

	public void setDownloadedDttm(Date downloadedDttm) {
		this.downloadedDttm = downloadedDttm;
	}

	public String getDownloadedInd() {
		return downloadedInd;
	}

	public void setDownloadedInd(String downloadedInd) {
		this.downloadedInd = downloadedInd;
	}

	public int getOriginalLcPrice() {
		return originalLcPrice;
	}

	public void setOriginalLcPrice(int originalLcPrice) {
		this.originalLcPrice = originalLcPrice;
	}

	public Date getOriginalPlaceDttm() {
		return originalPlaceDttm;
	}

	public void setOriginalPlaceDttm(Date originalPlaceDttm) {
		this.originalPlaceDttm = originalPlaceDttm;
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

	public double getLcAmountPaid() {
		return lcAmountPaid;
	}

	public void setLcAmountPaid(double lcAmtPaid1) {
		this.lcAmountPaid = lcAmtPaid1;
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

	public int getVendorLcDiscountAmt() {
		return VendorLcDiscountAmt;
	}

	public void setVendorLcDiscountAmt(int vendorLcDiscountAmt) {
		VendorLcDiscountAmt = vendorLcDiscountAmt;
	}

}
