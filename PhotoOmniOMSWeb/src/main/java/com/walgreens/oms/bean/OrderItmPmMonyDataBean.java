/**
 * 
 */
package com.walgreens.oms.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author CTS
 *
 */
public class OrderItmPmMonyDataBean  implements  Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2923657143091657714L;
	private long pmId;
	private String pmType;
	private String pmRuleType;
	private String pmPaymentType;
	private String tierType;
	private int discntAplicableInd;
	private Date UPDATE_DTTM;
	private boolean pmRule=false;
	private int minimumTier;
	private int maximumTier;
	private double pmAmount;
	
	
	/**
	 * @return the pmRule
	 */
	public boolean isPmRule() {
		return pmRule;
	}
	/**
	 * @param pmRule the pmRule to set
	 */
	public void setPmRule(boolean pmRule) {
		this.pmRule = pmRule;
	}
	/**
	 * @return the pmRuleType
	 */
	public String getPmRuleType() {
		return pmRuleType;
	}
	/**
	 * @param pmRuleType the pmRuleType to set
	 */
	public void setPmRuleType(String pmRuleType) {
		this.pmRuleType = pmRuleType;
	}
	/**
	 * @return the uPDATE_DTTM
	 */
	public Date getUPDATE_DTTM() {
		return UPDATE_DTTM;
	}
	/**
	 * @return the pmId
	 */
	public long getPmId() {
		return pmId;
	}
	/**
	 * @param pmId the pmId to set
	 */
	public void setPmId(long pmId) {
		this.pmId = pmId;
	}
	/**
	 * @param uPDATE_DTTM the uPDATE_DTTM to set
	 */
	public void setUPDATE_DTTM(Date uPDATE_DTTM) {
		UPDATE_DTTM = uPDATE_DTTM;
	}
	/**
	 * @return the pmType
	 */
	public String getPmType() {
		return pmType;
	}
	
	/**
	 * @param pmType the pmType to set
	 */
	public void setPmType(String pmType) {
		this.pmType = pmType;
	}
	
	
	
	/**
	 * @return the pmPaymentType
	 */
	public String getPmPaymentType() {
		return pmPaymentType;
	}
	/**
	 * @param pmPaymentType the pmPaymentType to set
	 */
	public void setPmPaymentType(String pmPaymentType) {
		this.pmPaymentType = pmPaymentType;
	}
	/**
	 * @return the tierType
	 */
	public String getTierType() {
		return tierType;
	}
	/**
	 * @param tierType the tierType to set
	 */
	public void setTierType(String tierType) {
		this.tierType = tierType;
	}
	
	/**
	 * @return the discntAplicableInd
	 */
	public int getDiscntAplicableInd() {
		return discntAplicableInd;
	}
	/**
	 * @param discntAplicableInd the discntAplicableInd to set
	 */
	public void setDiscntAplicableInd(int discntAplicableInd) {
		this.discntAplicableInd = discntAplicableInd;
	}
	/**
	 * @return the minimumTier
	 */
	public int getMinimumTier() {
		return minimumTier;
	}
	/**
	 * @param minimumTier the minimumTier to set
	 */
	public void setMinimumTier(int minimumTier) {
		this.minimumTier = minimumTier;
	}
	/**
	 * @return the maximumTier
	 */
	public int getMaximumTier() {
		return maximumTier;
	}
	/**
	 * @param maximumTier the maximumTier to set
	 */
	public void setMaximumTier(int maximumTier) {
		this.maximumTier = maximumTier;
	}
	/**
	 * @return the pmAmount
	 */
	public double getPmAmount() {
		return pmAmount;
	}
	/**
	 * @param pmAmount the pmAmount to set
	 */
	public void setPmAmount(double pmAmount) {
		this.pmAmount = pmAmount;
	}
	

}
