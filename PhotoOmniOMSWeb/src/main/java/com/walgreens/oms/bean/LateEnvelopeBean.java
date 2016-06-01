/**
 * 
 */
package com.walgreens.oms.bean;


/**
 * @author kumarxso
 *
 */
public class LateEnvelopeBean {
	private String orderId;
	private long envelopeNumber;
	private String orderOriginType;
	private String processingTypeCD;
	private String status;
	private String timeSubmitted;
	private String timePromised;
	private String timeDone;
	private String employeeTookOrder;
	private String reportPageSize;
	
	
	
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the reportPageSize
	 */
	public String getReportPageSize() {
		return reportPageSize;
	}
	/**
	 * @param reportPageSize the reportPageSize to set
	 */
	public void setReportPageSize(String reportPageSize) {
		this.reportPageSize = reportPageSize;
	}
	/**
	 * @return the totalRows
	 */
	public int getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	private int totalRows;
	/**
	 * @return the envelopeNumber
	 */
	public long getEnvelopeNumber() {
		return envelopeNumber;
	}
	/**
	 * @param envelopeNumber the envelopeNumber to set
	 */
	public void setEnvelopeNumber(long envelopeNumber) {
		this.envelopeNumber = envelopeNumber;
	}
	/**
	 * @return the orderOriginType
	 */
	public String getOrderOriginType() {
		return orderOriginType;
	}
	/**
	 * @param orderOriginType the orderOriginType to set
	 */
	public void setOrderOriginType(String orderOriginType) {
		this.orderOriginType = orderOriginType;
	}
	/**
	 * @return the processingTypeCD
	 */
	public String getProcessingTypeCD() {
		return processingTypeCD;
	}
	/**
	 * @param processingTypeCD the processingTypeCD to set
	 */
	public void setProcessingTypeCD(String processingTypeCD) {
		this.processingTypeCD = processingTypeCD;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the timeSubmitted
	 */
	public String getTimeSubmitted() {
		return timeSubmitted;
	}
	/**
	 * @param timeSubmitted the timeSubmitted to set
	 */
	public void setTimeSubmitted(String timeSubmitted) {
		this.timeSubmitted = timeSubmitted;
	}
	/**
	 * @return the timePromised
	 */
	public String getTimePromised() {
		return timePromised;
	}
	/**
	 * @param timePromised the timePromised to set
	 */
	public void setTimePromised(String timePromised) {
		this.timePromised = timePromised;
	}
	/**
	 * @return the timeDone
	 */
	public String getTimeDone() {
		return timeDone;
	}
	/**
	 * @param timeDone the timeDone to set
	 */
	public void setTimeDone(String timeDone) {
		this.timeDone = timeDone;
	}
	/**
	 * @return the employeeTookOrder
	 */
	public String getEmployeeTookOrder() {
		return employeeTookOrder;
	}
	/**
	 * @param employeeTookOrder the employeeTookOrder to set
	 */
	public void setEmployeeTookOrder(String employeeTookOrder) {
		this.employeeTookOrder = employeeTookOrder;
	}
}
