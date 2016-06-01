package com.walgreens.omni.utility.dashboard;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface DashboardViewProvider {

	String getFilterTemplate(DashboardViewContext context);

	String getStoreTemplate(DashboardViewContext context);

	Map<String, Object> getFilterTemplateData(DashboardViewContext context);

	Map<String, Object> getStoreTemplateData(DashboardViewContext context);

	Map<String, Object> getPrintDetails(
			DashboardViewContext dashboardViewContext);

	void exportToExcel(HttpServletResponse response,
			DashboardViewContext dashboardViewContext);

}
