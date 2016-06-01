package com.walgreens.oms.bean;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * This class is used as a bean to store OmPromotionalMpney info
 * @author Cognizant
 *
 */
public class OmPromotionalMoneyBean {
	
	private int sysPmId;
	private String type;
	//private String pmDistributionType;
	//private String pmRuleDesc;
	private int productId;
	//private Timestamp startDttm;
	//private Timestamp endDttm;
	private String pmPaymentType;
	private String tierType;
	//private int activeInd;
	private Date deactivationDttm;
	private int discountApplicableInd;
	private String createUserID;
	private Timestamp createDttm;
	private String updateUserId;
	private Timestamp updateDttm;
	private String pmRuleType;
	private String pmNbr;
	
	private List<OmPromotionalMoneyDetailBean> omPromotionalMoneyDetailBeanList;
	private boolean mBPmRule = false;
	private boolean isDiscountApplicableInd = false;
	
	private double minimumTier;
	private double maximumTier;
	private float pmAmount;
	/**
	 * This method fetches sysPmId
	 * @return sysPmId
	 */
	public int getSysPmId() {
		return sysPmId;
	}
	/**
	 * This method sets sysPmId
	 * @param sysPmId
	 */
	public void setSysPmId(int sysPmId) {
		this.sysPmId = sysPmId;
	}
	/**
	 * This method fetches type
	 * @return type
	 */
	public String getType() {
		return type;
	}
	/**
	 * This method sets type
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * This method fetches pmDistributionType
	 * @return pmDistributionType
	 *//*
	public String getPmDistributionType() {
		return pmDistributionType;
	}
	*//**
	 * This method sets pmDistributionType
	 * @param pmDistributionType
	 *//*
	public void setPmDistributionType(String pmDistributionType) {
		this.pmDistributionType = pmDistributionType;
	}
	*//**
	 * This method fetches pmRuleDesc
	 * @return pmRuleDesc
	 *//*
	public String getPmRuleDesc() {
		return pmRuleDesc;
	}
	*//**
	 * This method sets pmRuleDesc
	 * @param pmRuleDesc
	 *//*
	public void setPmRuleDesc(String pmRuleDesc) {
		this.pmRuleDesc = pmRuleDesc;
	}*/
	/**
	 * This method fetches productId
	 * @return productId
	 */
	public int getProductId() {
		return productId;
	}
	/**
	 * This method sets productId
	 * @param productId
	 */
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	/**
	 * This method fetches pmPaymentType
	 * @return pmPaymentType
	 */
	public String getPmPaymentType() {
		return pmPaymentType;
	}
	/**
	 * This method sets pmPaymentType
	 * @param pmPaymentType
	 */
	public void setPmPaymentType(String pmPaymentType) {
		this.pmPaymentType = pmPaymentType;
	}
	/**
	 * This method fetches tierType
	 * @return tierType
	 */
	public String getTierType() {
		return tierType;
	}
	/**
	 * This method sets tierType
	 * @param tierType
	 */
	public void setTierType(String tierType) {
		this.tierType = tierType;
	}
	/**
	 * This method fetches activeInd
	 * @return activeInd
	 *//*
	public int getActiveInd() {
		return activeInd;
	}
	*//**
	 * This method sets activeInd
	 * @param activeInd
	 *//*
	public void setActiveInd(int activeInd) {
		this.activeInd = activeInd;
	}*/
	/**
	 * This method fetches deactivationDttm
	 * @return deactivationDttm
	 */
	public Date getDeactivationDttm() {
		return deactivationDttm;
	}
	/**
	 * This method sets deactivationDttm
	 * @param deactivationDttm
	 */
	public void setDeactivationDttm(Date deactivationDttm) {
		this.deactivationDttm = deactivationDttm;
	}
	/**
	 * This method fetches discountApplicableInd
	 * @return discountApplicableInd
	 */
	public int getDiscountApplicableInd() {
		return discountApplicableInd;
	}
	/**
	 * This method sets discountApplicableInd
	 * @param discountApplicableInd
	 */
	public void setDiscountApplicableInd(int discountApplicableInd) {
		this.discountApplicableInd = discountApplicableInd;
	}
	/**
	 * This method fetches createUserID
	 * @return createUserID
	 */
	public String getCreateUserID() {
		return createUserID;
	}
	/**
	 * This method sets createUserID
	 * @param createUserID
	 */
	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}
	
	
	/**
	 * This method fetches updateUserId
	 * @return updateUserId
	 */
	public String getUpdateUserId() {
		return updateUserId;
	}
	/**
	 * This method sets updateUserId
	 * @param updateUserId
	 */
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	/**
	 * This method fetches pmRuleType
	 * @return pmRuleType
	 */
	public String getPmRuleType() {
		return pmRuleType;
	}
	/**
	 * This method sets pmRuleType
	 * @param pmRuleType
	 */
	public void setPmRuleType(String pmRuleType) {
		this.pmRuleType = pmRuleType;
	}
	/**
	 * This method fetches omPromotionalMoneyDetailBeanList
	 * @return omPromotionalMoneyDetailBeanList
	 */
	public List<OmPromotionalMoneyDetailBean> getOmPromotionalMoneyDetailBeanList() {
		return omPromotionalMoneyDetailBeanList;
	}
	/**
	 * This method sets omPromotionalMoneyDetailBeanList
	 * @param omPromotionalMoneyDetailBeanList
	 */
	public void setOmPromotionalMoneyDetailBeanList(
			List<OmPromotionalMoneyDetailBean> omPromotionalMoneyDetailBeanList) {
		this.omPromotionalMoneyDetailBeanList = omPromotionalMoneyDetailBeanList;
	}
	/**
	 * This method fetches mBPmRule
	 * @return mBPmRule
	 */
	public boolean ismBPmRule() {
		return mBPmRule;
	}
	/**
	 * This method sets mBPmRule
	 * @param mBPmRule
	 */
	public void setmBPmRule(boolean mBPmRule) {
		this.mBPmRule = mBPmRule;
	}
	
	/**
	 * This method fetches isDiscountApplicableInd
	 * @return isDiscountApplicableInd
	 */
	public boolean isDiscountApplicableInd() {
		return isDiscountApplicableInd;
	}
	/**
	 * This method sets isDiscountApplicableInd
	 * @param isDiscountApplicableInd
	 */
	public void setDiscountApplicableInd(boolean isDiscountApplicableInd) {
		this.isDiscountApplicableInd = isDiscountApplicableInd;
	}
	/**
	 * This method fetches pmNbr
	 * @return pmNbr
	 */
	public String getPmNbr() {
		return pmNbr;
	}
	/**
	 * This method sets pmNbr
	 * @param pmNbr
	 */
	public void setPmNbr(String pmNbr) {
		this.pmNbr = pmNbr;
	}
	/*public Timestamp getStartDttm() {
		return startDttm;
	}
	public void setStartDttm(Timestamp startDttm) {
		this.startDttm = startDttm;
	}
	public Timestamp getEndDttm() {
		return endDttm;
	}
	public void setEndDttm(Timestamp endDttm) {
		this.endDttm = endDttm;
	}*/
	public Timestamp getCreateDttm() {
		return createDttm;
	}
	public void setCreateDttm(Timestamp createDttm) {
		this.createDttm = createDttm;
	}
	public Timestamp getUpdateDttm() {
		return updateDttm;
	}
	public void setUpdateDttm(Timestamp updateDttm) {
		this.updateDttm = updateDttm;
	}
	public double getMinimumTier() {
		return minimumTier;
	}
	public void setMinimumTier(double minimumTier) {
		this.minimumTier = minimumTier;
	}
	public double getMaximumTier() {
		return maximumTier;
	}
	public void setMaximumTier(double maximumTier) {
		this.maximumTier = maximumTier;
	}
	public float getPmAmount() {
		return pmAmount;
	}
	public void setPmAmount(float pmAmount) {
		this.pmAmount = pmAmount;
	}	

}
