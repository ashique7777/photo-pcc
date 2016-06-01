package com.walgreens.batch.central.bean;

public class OmUserAttribute {
	
	private int userId;
	private int isEmployee;
	private int pmEligible;
	private String employeeId;
	int storeEmployeeCD;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getIsEmployee() {
		return isEmployee;
	}
	public void setIsEmployee(int isEmployee) {
		this.isEmployee = isEmployee;
	}
	public int getPmEligible() {
		return pmEligible;
	}
	public void setPmEligible(int pmEligible) {
		this.pmEligible = pmEligible;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public int getStoreEmployeeCD() {
		return storeEmployeeCD;
	}
	public void setStoreEmployeeCD(int storeEmployeeCD) {
		this.storeEmployeeCD = storeEmployeeCD;
	}
	
	

}
