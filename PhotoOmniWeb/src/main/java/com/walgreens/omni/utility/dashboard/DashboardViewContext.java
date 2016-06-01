package com.walgreens.omni.utility.dashboard;

import javax.servlet.http.HttpServletRequest;

import com.walgreens.omni.bean.DashboardReportBean;

/**
 * 
 * @author CTS 
 * 
 */
/**
 * 
 * This class will be used as a method parameter in real time dashboard related
 * class so that instead of passing multiple parameter only one parameter need
 * to pass and it will contain all required information.
 * 
 */
public class DashboardViewContext {

	private long userId;
	private DashboardReportBean reportBean;
	private HttpServletRequest request;

	public DashboardViewContext(long userId, DashboardReportBean reportBean,
			HttpServletRequest request) {
		super();
		this.userId = userId;
		this.reportBean = reportBean;
		this.request = request;
	}

	/**
	 * 
	 * @return userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * 
	 * @return reportBean
	 */
	public DashboardReportBean getReportBean() {
		return reportBean;
	}

	/**
	 * 
	 * @return request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

}
