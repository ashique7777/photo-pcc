/**
 * 
 */
package com.walgreens.oms.bean;

/**
 * @author CTS
 *
 */
public class PMRuleDetail implements Comparable<PMRuleDetail>  {
	double pmAmount;
	double minTier;
	double maxTier;
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
	public double getMinTier() {
		return minTier;
	}
	/**
	 * @param minTier the minTier to set
	 */
	public void setMinTier(double minTier) {
		this.minTier = minTier;
	}
	/**
	 * @return the maxTier
	 */
	public double getMaxTier() {
		return maxTier;
	}
	/**
	 * @param maxTier the maxTier to set
	 */
	public void setMaxTier(double maxTier) {
		this.maxTier = maxTier;
	}
	@Override
	public int compareTo(PMRuleDetail pmReuleDetail) {
		// TODO Auto-generated method stub
		return (int)(pmReuleDetail.getMaxTier()-this.getMaxTier());
	}

}
