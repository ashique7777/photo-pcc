package com.walgreens.oms.bean;

/**
 * @author CTS
 *
 */
public class PMByEmployeeDetailBean {
	
	private String employeeId;
	private String employeeName;
	private double pmDollarEarned;
	private double pmEarnedQty;
	private long totalRows;
	
	/**
	 * 
	 * @return employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * 
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * 
	 * @return pmDollarEarned
	 */
	public double getPmDollarEarned() {
		return pmDollarEarned;
	}
	/**
	 * 
	 * @param pmDollarEarned
	 *            the pmDollarEarned to set
	 */
	public void setPmDollarEarned(double pmDollarEarned) {
		this.pmDollarEarned = pmDollarEarned;
	}
	/**
	 * 
	 * @return pmEarnedQty
	 */
	public double getPmEarnedQty() {
		return pmEarnedQty;
	}
	/**
	 * 
	 * @param pmEarnedQty
	 *            the pmEarnedQty to set
	 */
	public void setPmEarnedQty(double pmEarnedQty) {
		this.pmEarnedQty = pmEarnedQty;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public long getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}
	
	@Override
	public String toString() {
		return "PMByEmployeeDetailBean [employeeId=" + employeeId
				+ ", employeeName=" + employeeName + ", pmDollarEarned="
				+ pmDollarEarned + ", pmEarnedQty=" + pmEarnedQty
				+ ", totalRows=" + totalRows + "]";
	}	
}
