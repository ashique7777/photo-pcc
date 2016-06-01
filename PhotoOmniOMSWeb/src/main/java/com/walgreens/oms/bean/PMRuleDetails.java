/**
 * 
 */
package com.walgreens.oms.bean;

/**
 * @author khansr
 *
 */
public class PMRuleDetails {
	double pmAmount;
	int minTier;
	int maxTier;
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

}
