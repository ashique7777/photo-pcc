package com.walgreens.oms.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 * 
 */

public class LicenceContentPosBean {

	private long sysOlLcId;
	private long orderLineId;
	private String vendorLcId;
	private String vendorLcDesc;
	private int vendorLcQty;
	private double finalLcPrice;
	private Timestamp downloadedDttm;
	private String downloadedInd;
	private double lcAmountPaid;
	private double originalLcPrice;
	private Timestamp originalPlaceDttm;
	private String createUserId;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updatedDttm;
	private double vndrLcDiscountAmt;

	/**
	 * @return the sysOlLcId
	 */
	public long getSysOlLcId() {
		return sysOlLcId;
	}

	/**
	 * @param sysOlLcId
	 *            the sysOlLcId to set
	 */
	public void setSysOlLcId(long sysOlLcId) {
		this.sysOlLcId = sysOlLcId;
	}

	/**
	 * @return the orderLineId
	 */
	public long getOrderLineId() {
		return orderLineId;
	}

	/**
	 * @param orderLineId
	 *            the orderLineId to set
	 */
	public void setOrderLineId(long orderLineId) {
		this.orderLineId = orderLineId;
	}

	/**
	 * @return the vendorLcId
	 */
	public String getVendorLcId() {
		return vendorLcId;
	}

	/**
	 * @param vendorLcId
	 *            the vendorLcId to set
	 */
	public void setVendorLcId(String vendorLcId) {
		this.vendorLcId = vendorLcId;
	}

	/**
	 * @return the vendorLcDesc
	 */
	public String getVendorLcDesc() {
		return vendorLcDesc;
	}

	/**
	 * @param vendorLcDesc
	 *            the vendorLcDesc to set
	 */
	public void setVendorLcDesc(String vendorLcDesc) {
		this.vendorLcDesc = vendorLcDesc;
	}

	/**
	 * @return the vendorLcQty
	 */
	public int getVendorLcQty() {
		return vendorLcQty;
	}

	/**
	 * @param vendorLcQty
	 *            the vendorLcQty to set
	 */
	public void setVendorLcQty(int vendorLcQty) {
		this.vendorLcQty = vendorLcQty;
	}

	/**
	 * @return the finalLcPrice
	 */
	public double getFinalLcPrice() {
		return finalLcPrice;
	}

	/**
	 * @param finalLcPrice
	 *            the finalLcPrice to set
	 */
	public void setFinalLcPrice(double finalLcPrice) {
		this.finalLcPrice = finalLcPrice;
	}

	/**
	 * @return the downloadedDttm
	 */
	public Timestamp getDownloadedDttm() {
		return downloadedDttm;
	}

	/**
	 * @param downloadedDttm
	 *            the downloadedDttm to set
	 */
	public void setDownloadedDttm(Timestamp downloadedDttm) {
		this.downloadedDttm = downloadedDttm;
	}

	/**
	 * @return the downloadedInd
	 */
	public String getDownloadedInd() {
		return downloadedInd;
	}

	/**
	 * @param downloadedInd
	 *            the downloadedInd to set
	 */
	public void setDownloadedInd(String downloadedInd) {
		this.downloadedInd = downloadedInd;
	}

	/**
	 * @return the lcAmountPaid
	 */
	public double getLcAmountPaid() {
		return lcAmountPaid;
	}

	/**
	 * @param lcAmountPaid
	 *            the lcAmountPaid to set
	 */
	public void setLcAmountPaid(double lcAmountPaid) {
		this.lcAmountPaid = lcAmountPaid;
	}

	/**
	 * @return the originalLcPrice
	 */
	public double getOriginalLcPrice() {
		return originalLcPrice;
	}

	/**
	 * @param originalLcPrice
	 *            the originalLcPrice to set
	 */
	public void setOriginalLcPrice(double originalLcPrice) {
		this.originalLcPrice = originalLcPrice;
	}

	/**
	 * @return the originalPlaceDttm
	 */
	public Timestamp getOriginalPlaceDttm() {
		return originalPlaceDttm;
	}

	/**
	 * @param originalPlaceDttm
	 *            the originalPlaceDttm to set
	 */
	public void setOriginalPlaceDttm(Timestamp originalPlaceDttm) {
		this.originalPlaceDttm = originalPlaceDttm;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the createDttm
	 */
	public Timestamp getCreateDttm() {
		return createDttm;
	}

	/**
	 * @param createDttm
	 *            the createDttm to set
	 */
	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}

	/**
	 * @return the updateUserId
	 */
	public String getUpdateUserId() {
		return updateUserId;
	}

	/**
	 * @param updateUserId
	 *            the updateUserId to set
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * @return the updatedDttm
	 */
	public Timestamp getUpdatedDttm() {
		return updatedDttm;
	}

	/**
	 * @param updatedDttm
	 *            the updatedDttm to set
	 */
	public void setUpdatedDttm(Timestamp updatedDttm) {
		this.updatedDttm = updatedDttm;
	}

	/**
	 * @return the vndrLcDiscountAmt
	 */
	public double getVndrLcDiscountAmt() {
		return vndrLcDiscountAmt;
	}

	/**
	 * @param vndrLcDiscountAmt
	 *            the vndrLcDiscountAmt to set
	 */
	public void setVndrLcDiscountAmt(double vndrLcDiscountAmt) {
		this.vndrLcDiscountAmt = vndrLcDiscountAmt;
	}

	@Override
	public String toString() {
		return "LicenceContentPosBean [sysOlLcId=" + sysOlLcId
				+ ", orderLineId=" + orderLineId + ", vendorLcId=" + vendorLcId
				+ ", vendorLcDesc=" + vendorLcDesc + ", vendorLcQty="
				+ vendorLcQty + ", finalLcPrice=" + finalLcPrice
				+ ", downloadedDttm=" + downloadedDttm + ", downloadedInd="
				+ downloadedInd + ", lcAmountPaid=" + lcAmountPaid
				+ ", originalLcPrice=" + originalLcPrice
				+ ", originalPlaceDttm=" + originalPlaceDttm
				+ ", createUserId=" + createUserId + ", createDttm="
				+ createDttm + ", updateUserId=" + updateUserId
				+ ", updatedDttm=" + updatedDttm + ", VendorLcDiscountAmt="
				+ vndrLcDiscountAmt + "]";
	}

}
