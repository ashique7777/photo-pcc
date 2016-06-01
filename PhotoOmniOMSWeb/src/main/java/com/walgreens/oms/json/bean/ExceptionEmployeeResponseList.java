package com.walgreens.oms.json.bean;


public class ExceptionEmployeeResponseList {

	
	private String empName;
	private String employeeId;
	private ExceptionTypeBean refused;
	private ExceptionTypeBean remake;
	private ExceptionTypeBean soldForFree;
	private ExceptionTypeBean cancel;
	private ExceptionTypeBean unSellable;
	private ExceptionTypeBean priceModify;
	private ExceptionTypeBean missing;
	private ExceptionTypeBean totalExceptions;
	private long totalCount = 0;
	private double totalDollars =0;
	
	
	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the totalDollars
	 */
	public double getTotalDollars() {
		return totalDollars;
	}
	/**
	 * @param totalDollars the totalDollars to set
	 */
	public void setTotalDollars(double totalDollars) {
		this.totalDollars = totalDollars;
	}
	/**
	 * @return the unSellable
	 */
	public ExceptionTypeBean getUnSellable() {
		return unSellable;
	}
	/**
	 * @param unSellable the unSellable to set
	 */
	public void setUnSellable(ExceptionTypeBean unSellable) {
		this.unSellable = unSellable;
	}
	/**
	 * @return the priceModify
	 */
	public ExceptionTypeBean getPriceModify() {
		return priceModify;
	}
	/**
	 * @param priceModify the priceModify to set
	 */
	public void setPriceModify(ExceptionTypeBean priceModify) {
		this.priceModify = priceModify;
	}
	/**
	 * @return the missing
	 */
	public ExceptionTypeBean getMissing() {
		return missing;
	}
	/**
	 * @param missing the missing to set
	 */
	public void setMissing(ExceptionTypeBean missing) {
		this.missing = missing;
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
	 * @return the refused
	 */
	public ExceptionTypeBean getRefused() {
		return refused;
	}
	/**
	 * @param refused the refused to set
	 */
	public void setRefused(ExceptionTypeBean refused) {
		this.refused = refused;
	}
	/**
	 * @return the remake
	 */
	public ExceptionTypeBean getRemake() {
		return remake;
	}
	/**
	 * @param remake the remake to set
	 */
	public void setRemake(ExceptionTypeBean remake) {
		this.remake = remake;
	}
	/**
	 * @return the soldForFree
	 */
	public ExceptionTypeBean getSoldForFree() {
		return soldForFree;
	}
	/**
	 * @param soldForFree the soldForFree to set
	 */
	public void setSoldForFree(ExceptionTypeBean soldForFree) {
		this.soldForFree = soldForFree;
	}
	/**
	 * @return the cancel
	 */
	public ExceptionTypeBean getCancel() {
		return cancel;
	}
	/**
	 * @param cancel the cancel to set
	 */
	public void setCancel(ExceptionTypeBean cancel) {
		this.cancel = cancel;
	}
	/**
	 * @return the totalExceptions
	 */
	public ExceptionTypeBean getTotalExceptions() {
		return totalExceptions;
	}
	/**
	 * @param totalExceptions the totalExceptions to set
	 */
	public void setTotalExceptions(ExceptionTypeBean totalExceptions) {
		this.totalExceptions = totalExceptions;
	}
	
	
	
	
}
