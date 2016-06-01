package com.walgreens.oms.json.bean;

public class ExceptionByEmployeeBean {

	private String empName;
	private String exceptionType;
	private String wasteCost;
	private String employeeId;
	private long exceptionCount;
	private long totalRows;
	private long totalExceptionCount;
	private String totalWasteCost;
	
	
	
	/**
	 * @return the totalExceptionCount
	 */
	public long getTotalExceptionCount() {
		return totalExceptionCount;
	}
	/**
	 * @param totalExceptionCount the totalExceptionCount to set
	 */
	public void setTotalExceptionCount(long totalExceptionCount) {
		this.totalExceptionCount = totalExceptionCount;
	}
	/**
	 * @return the totalWasteCost
	 */
	public String getTotalWasteCost() {
		return totalWasteCost;
	}
	/**
	 * @param totalWasteCost the totalWasteCost to set
	 */
	public void setTotalWasteCost(String totalWasteCost) {
		this.totalWasteCost = totalWasteCost;
	}
	/**
	 * @return the exceptionCount
	 */
	public long getExceptionCount() {
		return exceptionCount;
	}
	/**
	 * @param exceptionCount the exceptionCount to set
	 */
	public void setExceptionCount(long exceptionCount) {
		this.exceptionCount = exceptionCount;
	}
	/**
	 * @return the totalRows
	 */
	public long getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}
	/**
	 * @return the empName
	 */
	public String getEmpName() {
		return empName;
	}
	/**
	 * @param empName the empName to set
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	/**
	 * @return the exceptionType
	 */
	public String getExceptionType() {
		return exceptionType;
	}
	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}
	
	/**
	 * @return the wasteCost
	 */
	public String getWasteCost() {
		return wasteCost;
	}
	/**
	 * @param wasteCost the wasteCost to set
	 */
	public void setWasteCost(String wasteCost) {
		this.wasteCost = wasteCost;
	}
	/**
	 * @return the employeeId
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	
	
}
