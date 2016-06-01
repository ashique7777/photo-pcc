/**
 * 
 */
package com.walgreens.oms.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CTS
 *
 */
public class PMRule {
	private long pmId;
	private String pmRuleType;
	private String pmPaymentType;
	private String tierType;
	private boolean discountApplicableCd;
	private List<PMRuleDetail> ruleDetails = new ArrayList<PMRuleDetail>();
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
	 * @return the discountApplicableCd
	 */
	public boolean isDiscountApplicableCd() {
		return discountApplicableCd;
	}
	/**
	 * @param discountApplicableCd the discountApplicableCd to set
	 */
	public void setDiscountApplicableCd(boolean discountApplicableCd) {
		this.discountApplicableCd = discountApplicableCd;
	}
	/**
	 * @return the ruleDetails
	 */
	public List<PMRuleDetail> getRuleDetails() {
		return ruleDetails;
	}
	/**
	 * @param ruleDetails the ruleDetails to set
	 */
	public void setRuleDetails(List<PMRuleDetail> ruleDetails) {
		this.ruleDetails = ruleDetails;
	}
}
