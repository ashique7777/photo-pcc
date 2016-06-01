/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.io.Serializable;

/**
 * @author CTS
 *
 */
public class User implements  Serializable{
	
	private String authId;
	private long userId;
	private int pmUsrEligible;
	private String empId;
	private int isEmp;
	private int StoreEmpInd;
	/**
	 * @return the authId
	 */
	public String getAuthId() {
		return authId;
	}
	/**
	 * @param authId the authId to set
	 */
	public void setAuthId(String authId) {
		this.authId = authId;
	}
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	/**
	 * @return the pmUsrEligible
	 */
	public int getPmUsrEligible() {
		return pmUsrEligible;
	}
	/**
	 * @param pmUsrEligible the pmUsrEligible to set
	 */
	public void setPmUsrEligible(int pmUsrEligible) {
		this.pmUsrEligible = pmUsrEligible;
	}
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * @return the isEmp
	 */
	public int getIsEmp() {
		return isEmp;
	}
	/**
	 * @param isEmp the isEmp to set
	 */
	public void setIsEmp(int isEmp) {
		this.isEmp = isEmp;
	}
	/**
	 * @return the storeEmpInd
	 */
	public int getStoreEmpInd() {
		return StoreEmpInd;
	}
	/**
	 * @param storeEmpInd the storeEmpInd to set
	 */
	public void setStoreEmpInd(int storeEmpInd) {
		StoreEmpInd = storeEmpInd;
	}
	
	

}
