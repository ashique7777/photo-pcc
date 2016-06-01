/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;

/**
 * @author CTS
 *
 */
public class ItemPMDetailDataBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	int pmDtlId;
	int pmId;
	int minTier;
	int maxTier;
	boolean isPmQtyEligible= false;
	
	
	/**
	 * @return the isPmQtyEligible
	 */
	public boolean isPmQtyEligible() {
		return isPmQtyEligible;
	}
	/**
	 * @param isPmQtyEligible the isPmQtyEligible to set
	 */
	public void setPmQtyEligible(boolean isPmQtyEligible) {
		this.isPmQtyEligible = isPmQtyEligible;
	}
	/**
	 * @return the pmDtlId
	 */
	public int getPmDtlId() {
		return pmDtlId;
	}
	/**
	 * @param pmDtlId the pmDtlId to set
	 */
	public void setPmDtlId(int pmDtlId) {
		this.pmDtlId = pmDtlId;
	}
	/**
	 * @return the pmId
	 */
	public int getPmId() {
		return pmId;
	}
	/**
	 * @param pmId the pmId to set
	 */
	public void setPmId(int pmId) {
		this.pmId = pmId;
	}
	/**
	 * @return the minTier
	 */
	public int getMinTier() {
		return minTier;
	}
	/**
	 * @param minTier the minTier to set
	 */
	public void setMinTier(int minTier) {
		this.minTier = minTier;
	}
	/**
	 * @return the maxTier
	 */
	public int getMaxTier() {
		return maxTier;
	}
	/**
	 * @param maxTier the maxTier to set
	 */
	public void setMaxTier(int maxTier) {
		this.maxTier = maxTier;
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
	double pmAmount;
	
	

}
