package com.walgreens.batch.central.bean;

import java.util.Date;

/**
 * This class is used as bean to store OmPromotionalMoneyDetail info
 * @author Cognizant
 *
 */
public class OmPromotionalMoneyDetailBean {
	
	private int sysPmDtlId;
	private int pmId;
	private int minimumTier;
	private int maximumTier;
	private double pmAmount;
	private String createUserId;
	private Date createDttm;
	private String updateUserID;
	private Date updateDttm;
	/**
	 * This method fetches sysPmDtlId
	 * @return sysPmDtlId
	 */
	public int getSysPmDtlId() {
		return sysPmDtlId;
	}
	/**
	 * This method sets sysPmDtlId
	 * @param sysPmDtlId
	 */
	public void setSysPmDtlId(int sysPmDtlId) {
		this.sysPmDtlId = sysPmDtlId;
	}
	/**
	 * This method fetches pmId
	 * @return pmId
	 */
	public int getPmId() {
		return pmId;
	}
	/**
	 * This method sets pmId
	 * @param pmId
	 */
	public void setPmId(int pmId) {
		this.pmId = pmId;
	}
	/**
	 * This method fetches minimumTier
	 * @return minimumTier
	 */
	public int getMinimumTier() {
		return minimumTier;
	}
	/**
	 * This method sets minimumTier
	 * @param minimumTier
	 */
	public void setMinimumTier(int minimumTier) {
		this.minimumTier = minimumTier;
	}
	/**
	 * This method fetches maximumTier
	 * @return maximumTier
	 */
	public int getMaximumTier() {
		return maximumTier;
	}
	/**
	 * This method sets maximumTier
	 * @param maximumTier
	 */
	public void setMaximumTier(int maximumTier) {
		this.maximumTier = maximumTier;
	}
	/**
	 * This method fetches pmAmount
	 * @return pmAmount
	 */
	public double getPmAmount() {
		return pmAmount;
	}
	/**
	 * This method sets pmAmount
	 * @param pmAmount
	 */
	public void setPmAmount(double pmAmount) {
		this.pmAmount = pmAmount;
	}
	/**
	 * This method fetches createUserId
	 * @return createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}
	/**
	 * This method sets
	 * @param createUserId
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * This method fetches createDttm
	 * @return createDttm
	 */
	public Date getCreateDttm() {
		return createDttm;
	}
	/**
	 * This method sets createDttm
	 * @param createDttm
	 */
	public void setCreateDttm(Date createDttm) {
		this.createDttm = createDttm;
	}
	/**
	 * This method fetches updateUserID
	 * @return updateUserID
	 */
	public String getUpdateUserID() {
		return updateUserID;
	}
	/**
	 * This method sets updateUserID
	 * @param updateUserID
	 */
	public void setUpdateUserID(String updateUserID) {
		this.updateUserID = updateUserID;
	}
	/**
	 * This method fetches updateDttm
	 * @return updateDttm
	 */
	public Date getUpdateDttm() {
		return updateDttm;
	}
	/**
	 * This method sets updateDttm
	 * @param updateDttm
	 */
	public void setUpdateDttm(Date updateDttm) {
		this.updateDttm = updateDttm;
	}

}
