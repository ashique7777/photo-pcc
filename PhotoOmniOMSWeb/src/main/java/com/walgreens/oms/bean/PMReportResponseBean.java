package com.walgreens.oms.bean;

public class PMReportResponseBean {

	private String productDesc;
	private double pmPerProduct;
	private long pmEnteredQty;
	private double pmEnteredAmt;
	private long pmEarnedQty;
	private double pmEarnedAmt;
	private long pmPendingQty;
	private double pmPendingAmt;
	private String employeeFirstName;
	private String employeeLastName;
	private long totalRows;
	private boolean boldCheck;
	


	/**
	 * @return the boldCheck
	 */
	public boolean isBoldCheck() {
		return boldCheck;
	}


	/**
	 * @param boldCheck the boldCheck to set
	 */
	public void setBoldCheck(boolean boldCheck) {
		this.boldCheck = boldCheck;
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
	 * @return the productDesc
	 */
	public String getProductDesc() {
		return productDesc;
	}

	/**
	 * @param productDesc
	 *            the productDesc to set
	 */
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	/**
	 * @return the pmPerProduct
	 */
	public double getPmPerProduct() {
		return pmPerProduct;
	}

	/**
	 * @param pmPerProduct
	 *            the pmPerProduct to set
	 */
	public void setPmPerProduct(double pmPerProduct) {
		this.pmPerProduct = pmPerProduct;
	}

	/**
	 * @return the pmEnteredQty
	 */
	public long getPmEnteredQty() {
		return pmEnteredQty;
	}

	/**
	 * @param pmEnteredQty
	 *            the pmEnteredQty to set
	 */
	public void setPmEnteredQty(long pmEnteredQty) {
		this.pmEnteredQty = pmEnteredQty;
	}

	/**
	 * @return the pmEnteredAmt
	 */
	public double getPmEnteredAmt() {
		return pmEnteredAmt;
	}

	/**
	 * @param pmEnteredAmt
	 *            the pmEnteredAmt to set
	 */
	public void setPmEnteredAmt(double pmEnteredAmt) {
		this.pmEnteredAmt = pmEnteredAmt;
	}

	/**
	 * @return the pmEarnedQty
	 */
	public long getPmEarnedQty() {
		return pmEarnedQty;
	}

	/**
	 * @param pmEarnedQty
	 *            the pmEarnedQty to set
	 */
	public void setPmEarnedQty(long pmEarnedQty) {
		this.pmEarnedQty = pmEarnedQty;
	}

	/**
	 * @return the pmEarnedAmt
	 */
	public double getPmEarnedAmt() {
		return pmEarnedAmt;
	}

	/**
	 * @param pmEarnedAmt
	 *            the pmEarnedAmt to set
	 */
	public void setPmEarnedAmt(double pmEarnedAmt) {
		this.pmEarnedAmt = pmEarnedAmt;
	}

	/**
	 * @return the pmPendingQty
	 */
	public long getPmPendingQty() {
		return pmPendingQty;
	}

	/**
	 * @param pmPendingQty
	 *            the pmPendingQty to set
	 */
	public void setPmPendingQty(long pmPendingQty) {
		this.pmPendingQty = pmPendingQty;
	}

	/**
	 * @return the pmPendingAmt
	 */
	public double getPmPendingAmt() {
		return pmPendingAmt;
	}

	/**
	 * @param pmPendingAmt
	 *            the pmPendingAmt to set
	 */
	public void setPmPendingAmt(double pmPendingAmt) {
		this.pmPendingAmt = pmPendingAmt;
	}

	/**
	 * @return the employeeFirstName
	 */
	public String getEmployeeFirstName() {
		return employeeFirstName;
	}

	/**
	 * @param employeeFirstName
	 *            the employeeFirstName to set
	 */
	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	/**
	 * @return the employeeLastName
	 */
	public String getEmployeeLastName() {
		return employeeLastName;
	}

	/**
	 * @param employeeLastName
	 *            the employeeLastName to set
	 */
	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

}
