package com.walgreens.batch.central.bean;

/**This is a bean class holding the Time and Attendance data
 * 
 * @author Cognizant
 *
 */
public class TADataBean {

	private String effectiveDATE;
	private String employeeID;
	private String dollarPMAmount;
	private String storeNumber;
	private String commentText;
	private String storeFeedCode;
	private String payCodeName;
	private int totalRecors ;
	private int orderId;
	private String propValue;
	String undefined;
	
	/**This method returns the value of the orderId attribute
	 * @return integer
	 */
	public int getOrderId() {
		return orderId;
	}

	/**This method sets the value of the orderId attribute
	 * @param orderId
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**This method returns the value of the effectiveDATE attribute
	 * @return Date-String
	 */
	public String getEffectiveDATE() { 
		return effectiveDATE;
	}

	/**
	 * @param effectiveDATE
	 */
	public void setEffectiveDATE(String effectiveDATE) {
		this.effectiveDATE = effectiveDATE;
	}

	/**This method returns the value of the employeeID attribute
	 * @return String
	 */
	public String getEmployeeID() {
		return employeeID;
	}

	/**This method sets the value of the employeeID attribute
	 * @param employeeID
	 */
	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	/**This method returns the value of the dollarPMAmount attribute
	 * @return integer
	 */
	public String getDollarPMAmount() {
		return dollarPMAmount;
	}

	/**This method sets the value of the dollarPMAmount attribute
	 * @param dollarPMAmount
	 */
	public void setDollarPMAmount(String dollarPMAmount) {
		this.dollarPMAmount = dollarPMAmount;
	}

	/**This method returns the value of the storeNumber attribute
	 * @return String
	 */
	public String getStoreNumber() {
		return storeNumber;
	}

	/**This method sets the value of the storeNumber attribute
	 * @param storeNumber
	 */
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}

	/**This method returns the value of the commentText attribute
	 * @return String
	 */
	public String getCommentText() {
		return commentText;
	}

	/**This method sets the value of the commentText attribute
	 * @param commentText
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**This method returns the value of the storeFeedCode attribute
	 * @return String
	 */
	public String getStoreFeedCode() {
		return storeFeedCode;
	}

	/**This method sets the value of the storeFeedCode attribute
	 * @param storeFeedCode
	 */
	public void setStoreFeedCode(String storeFeedCode) {
		this.storeFeedCode = storeFeedCode; 
	}

	/**This method returns the value of the payCodeName attribute
	 * @return String
	 */
	public String getPayCodeName() {
		return payCodeName;
	}

	/**This method sets the value of the payCodeName attribute
	 * @param payCodeName
	 */
	public void setPayCodeName(String payCodeName) {
		this.payCodeName = payCodeName;
	}

	/**This method returns the value of the totalRecors attribute
	 * @return integer
	 */
	public int getTotalRecors() {
		return totalRecors;
	}

	/**This method sets the value of the totalRecors attribute
	 * @param totalRecors
	 */
	public void setTotalRecors(int totalRecors) {
		this.totalRecors = totalRecors;
	}

	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public String getUndefined() {
		return undefined;
	}

	public void setUndefined(String undefined) {
		this.undefined = undefined;
	}
	
	
	
}
