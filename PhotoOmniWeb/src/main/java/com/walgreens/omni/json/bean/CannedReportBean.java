/**
 * 
 */
package com.walgreens.omni.json.bean;

import java.util.List;

import com.walgreens.common.utility.MessageHeader;

/**
 * @author cognizant
 *
 */
public class CannedReportBean {
	
	private String currentPage;
	private int totalRecord;
	private double totalRevenue;
	private double totalDiscount;
	private double sumTotalRevenue;
	private double sumTotalDiscount;
	private double sumTotalOrders;
	private double averagePlacedvalue;
	private int pageSize;
	private List<CannedReportDataBean> cannedReportDataBeanList;
	/**
	 * @return the currentPage
	 */
	public String getCurrentPage() {
		return currentPage;
	}
	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * @return the totalRecord
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * @return the totalRevenue
	 */
	public double getTotalRevenue() {
		return totalRevenue;
	}
	/**
	 * @param totalRevenue the totalRevenue to set
	 */
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	/**
	 * @return the totalDiscount
	 */
	public double getTotalDiscount() {
		return totalDiscount;
	}
	/**
	 * @param totalDiscount the totalDiscount to set
	 */
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	/**
	 * @return the sumTotalRevenue
	 */
	public double getSumTotalRevenue() {
		return sumTotalRevenue;
	}
	/**
	 * @param sumTotalRevenue the sumTotalRevenue to set
	 */
	public void setSumTotalRevenue(double sumTotalRevenue) {
		this.sumTotalRevenue = sumTotalRevenue;
	}
	/**
	 * @return the sumTotalDiscount
	 */
	public double getSumTotalDiscount() {
		return sumTotalDiscount;
	}
	/**
	 * @param sumTotalDiscount the sumTotalDiscount to set
	 */
	public void setSumTotalDiscount(double sumTotalDiscount) {
		this.sumTotalDiscount = sumTotalDiscount;
	}
	/**
	 * @return the sumTotalOrders
	 */
	public double getSumTotalOrders() {
		return sumTotalOrders;
	}
	/**
	 * @param sumTotalOrders the sumTotalOrders to set
	 */
	public void setSumTotalOrders(double sumTotalOrders) {
		this.sumTotalOrders = sumTotalOrders;
	}
	/**
	 * @return the averagePlacedvalue
	 */
	public double getAveragePlacedvalue() {
		return averagePlacedvalue;
	}
	/**
	 * @param averagePlacedvalue the averagePlacedvalue to set
	 */
	public void setAveragePlacedvalue(double averagePlacedvalue) {
		this.averagePlacedvalue = averagePlacedvalue;
	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the cannedReportDataBeanList
	 */
	public List<CannedReportDataBean> getCannedReportDataBeanList() {
		return cannedReportDataBeanList;
	}
	/**
	 * @param cannedReportDataBeanList the cannedReportDataBeanList to set
	 */
	public void setCannedReportDataBeanList(
			List<CannedReportDataBean> cannedReportDataBeanList) {
		this.cannedReportDataBeanList = cannedReportDataBeanList;
	}

}
