/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author CTS
 *
 */
public class OrderItmPmMonyDataBean  implements  Serializable{
	
	private long pmId;
	private String pmType;
	private String pmDistributionType;
	private  String pmRuleDesc;
	private String pmRuleType;
	private Timestamp pmStartDttm;
	private Timestamp pmEndDttm;
	private String pmPaymentType;
	private String tierType;
	private String pmActiveInd;
	private int discntAplicableInd;
	private Date deActivationDttm;
	private List<ItemPMDetailDataBean>  pmDetailList = new ArrayList<ItemPMDetailDataBean>();
	private Date UPDATE_DTTM;
	private boolean pmRule=false;
	
	
	/**
	 * @return the pmDetailList
	 */
	public List<ItemPMDetailDataBean> getPmDetailList() {
		return pmDetailList;
	}
	/**
	 * @param pmDetailList the pmDetailList to set
	 */
	public void setPmDetailList(List<ItemPMDetailDataBean> pmDetailList) {
		this.pmDetailList = pmDetailList;
	}
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
	 * @return the pmDistributionType
	 */
	public String getPmDistributionType() {
		return pmDistributionType;
	}
	/**
	 * @param pmDistributionType the pmDistributionType to set
	 */
	public void setPmDistributionType(String pmDistributionType) {
		this.pmDistributionType = pmDistributionType;
	}
	/**
	 * @return the pmRuleDesc
	 */
	public String getPmRuleDesc() {
		return pmRuleDesc;
	}
	/**
	 * @param pmRuleDesc the pmRuleDesc to set
	 */
	public void setPmRuleDesc(String pmRuleDesc) {
		this.pmRuleDesc = pmRuleDesc;
	}
	
	/**
	 * @return the pmStartDttm
	 */
	public Timestamp getPmStartDttm() {
		return pmStartDttm;
	}
	/**
	 * @param pmStartDttm the pmStartDttm to set
	 */
	public void setPmStartDttm(Timestamp pmStartDttm) {
		this.pmStartDttm = pmStartDttm;
	}
	/**
	 * @return the pmEndDttm
	 */
	public Timestamp getPmEndDttm() {
		return pmEndDttm;
	}
	/**
	 * @param pmEndDttm the pmEndDttm to set
	 */
	public void setPmEndDttm(Timestamp pmEndDttm) {
		this.pmEndDttm = pmEndDttm;
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
	 * @return the pmActiveInd
	 */
	public String getPmActiveInd() {
		return pmActiveInd;
	}
	/**
	 * @param pmActiveInd the pmActiveInd to set
	 */
	public void setPmActiveInd(String pmActiveInd) {
		this.pmActiveInd = pmActiveInd;
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
	 * @return the deActivationDttm
	 */
	public Date getDeActivationDttm() {
		return deActivationDttm;
	}
	/**
	 * @param deActivationDttm the deActivationDttm to set
	 */
	public void setDeActivationDttm(Date deActivationDttm) {
		this.deActivationDttm = deActivationDttm;
	}
	

}
