package com.walgreens.omni.utility.dashboard;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.walgreens.omni.factory.DashboardBOFactory;

/**
 * 
 * @author CTS
 * 
 */
public abstract class AbstractDashboardViewProvider extends JdbcDaoSupport
		implements DashboardViewProvider {

	public void setDashboardBOFactory(DashboardBOFactory dashboardBOFactory) {
		this.dashboardBOFactory = dashboardBOFactory;
	}

	private String filterTemplatePath;
	private String storeTemplatePath;
	protected DashboardBOFactory dashboardBOFactory;

	public AbstractDashboardViewProvider() {

	}

	/**
	 * 
	 * @param filterTemplatePath
	 *            the filterTemplatePath to set
	 */
	public void setFilterTemplatePath(String filterTemplatePath) {
		this.filterTemplatePath = filterTemplatePath;
	}

	/**
	 * 
	 * @param storeTemplatePath
	 *            the storeTemplatePath to set
	 */
	public void setStoreTemplatePath(String storeTemplatePath) {
		this.storeTemplatePath = storeTemplatePath;
	}

	/**
	 * @return filterTemplatePath
	 */
	@Override
	public String getFilterTemplate(DashboardViewContext context) {
		return filterTemplatePath;
	}

	/**
	 * @return storeTemplatePath
	 */
	@Override
	public String getStoreTemplate(DashboardViewContext context) {
		return storeTemplatePath;
	}
}
