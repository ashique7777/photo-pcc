/**
 * 
 */
package com.walgreens.batch.central.bean;

import java.sql.Timestamp;

/**
 * @author CTS
 *
 */
public class ExceptionCSVFileReportDataBean {
	private Long store;
	private Long orderID;
	private Long productID;
	private String productDescription;
	private Timestamp exceptionDate;
	private String exceptionType;
	private String exceptionDescription;
	private String remarks;
	private Timestamp datetimeCreated;
	/**
	 * @return the store
	 */
	public Long getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(Long store) {
		this.store = store;
	}
	/**
	 * @return the orderID
	 */
	public Long getOrderID() {
		return orderID;
	}
	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	/**
	 * @return the productID
	 */
	public Long getProductID() {
		return productID;
	}
	/**
	 * @param productID the productID to set
	 */
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}
	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	/**
	 * @return the exceptionDate
	 */
	public Timestamp getExceptionDate() {
		return exceptionDate;
	}
	/**
	 * @param exceptionDate the exceptionDate to set
	 */
	public void setExceptionDate(Timestamp exceptionDate) {
		this.exceptionDate = exceptionDate;
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
	 * @return the exceptionDescription
	 */
	public String getExceptionDescription() {
		return exceptionDescription;
	}
	/**
	 * @param exceptionDescription the exceptionDescription to set
	 */
	public void setExceptionDescription(String exceptionDescription) {
		this.exceptionDescription = exceptionDescription;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the datetimeCreated
	 */
	public Timestamp getDatetimeCreated() {
		return datetimeCreated;
	}
	/**
	 * @param datetimeCreated the datetimeCreated to set
	 */
	public void setDatetimeCreated(Timestamp datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}
	
}
