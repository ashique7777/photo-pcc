package com.walgreens.omni.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.omni.bean.DashboardReportBean;
import com.walgreens.omni.bo.DashboardBO;
import com.walgreens.omni.factory.DashboardBOFactory;
import com.walgreens.omni.utility.dashboard.DashboardViewContext;
import com.walgreens.omni.utility.dashboard.DashboardViewProvider;

/**
 * 
 * @author CTS This class will act as a global request handler for all Dashboard
 *         report
 * 
 */
@Controller
@RequestMapping(value = "/dashboard")
public class DashboardServiceImpl implements DashboardService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DashboardServiceImpl.class);

	@Autowired
	private DashboardBOFactory boFactory;

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * it is responsible to provide list of reports for logged in user
	 */
	@Override
	@RequestMapping(value = "/load", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDashboardItems() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered getDashboardItems method of DashboardServiceImpl");
		}
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		Map<String, Object> model = new HashMap<String, Object>();
		long userId = dashboardBO.getUserID();
		model.put("data", dashboardBO.getActiveReportByUser(userId));
		return model;
	}

	/**
	 * it is responsible to provide filter template and store template based on
	 * report key
	 */
	@Override
	@RequestMapping(value = "/load/{key}/{defaultFlag}/templates", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadReportTemplates(@PathVariable String key,
			@PathVariable Boolean defaultFlag, HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered loadReportTemplates method of DashboardServiceImpl");
		}
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		Map<String, Object> model = new HashMap<String, Object>();
		long userId = dashboardBO.getUserID();
		DashboardViewProvider provider = applicationContext.getBean(key,
				DashboardViewProvider.class);
		DashboardReportBean dashboardReportBean = dashboardBO
				.getDashboardReportBean(key, defaultFlag, userId);
		DashboardViewContext dashboardViewContext = new DashboardViewContext(
				userId, dashboardReportBean, request);
		model.put("filterTemplate",
				provider.getFilterTemplate(dashboardViewContext));
		model.put("storeTemplate",
				provider.getStoreTemplate(dashboardViewContext));
		return model;
	}

	/**
	 * it is responsible to provide filter data for report filters based on
	 * report key
	 */
	@Override
	@RequestMapping(value = "/load/{key}/{defaultFlag}/filterData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadFilterData(@PathVariable String key,
			@PathVariable Boolean defaultFlag, HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered loadFilterData method of DashboardServiceImpl");
		}
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		Map<String, Object> model = new HashMap<String, Object>();
		long userId = dashboardBO.getUserID();
		DashboardViewProvider provider = applicationContext.getBean(key,
				DashboardViewProvider.class);
		DashboardReportBean dashboardReportBean = dashboardBO
				.getDashboardReportBean(key, defaultFlag, userId);
		DashboardViewContext dashboardViewContext = new DashboardViewContext(
				userId, dashboardReportBean, request);
		model.put("data", provider.getFilterTemplateData(dashboardViewContext));
		return model;
	}

	/**
	 * it is responsible to provide store data for report filters based on
	 * report key
	 */
	@Override
	@RequestMapping(value = "/load/{key}/{defaultFlag}/storeData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> loadStoreData(@PathVariable String key,
			@PathVariable Boolean defaultFlag, HttpServletRequest request,
			@RequestBody Map<String, Object> params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered loadStoreData method of DashboardServiceImpl");
		}
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		Map<String, Object> model = new HashMap<String, Object>();
		long userId = dashboardBO.getUserID();
		DashboardViewProvider provider = applicationContext.getBean(key,
				DashboardViewProvider.class);
		DashboardReportBean dashboardReportBean = dashboardBO
				.getDashboardReportBean(key, defaultFlag, userId);
		DashboardViewContext dashboardViewContext = new DashboardViewContext(
				userId, dashboardReportBean, request);
		model.put("data", provider.getStoreTemplateData(dashboardViewContext));
		return model;
	}

	/**
	 * it is responsible to provide template for all dashboard reports
	 */
	@Override
	@RequestMapping(value = "/print", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> print(HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered print method of DashboardServiceImpl");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("printTemplate",
				"res/template/dashboard/dashboard-print.html");
		return model;
	}

	/**
	 * it is responsible to provide data to print template for report based on
	 * report key
	 */
	@Override
	@RequestMapping(value = "/{key}/{defaultFlag}/print-data", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> printData(HttpServletRequest request,
			@PathVariable String key, @PathVariable Boolean defaultFlag) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered printData method of DashboardServiceImpl");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		long userId = dashboardBO.getUserID();
		DashboardViewProvider provider = applicationContext.getBean(key,
				DashboardViewProvider.class);
		DashboardReportBean dashboardReportBean = dashboardBO
				.getDashboardReportBean(key, defaultFlag, userId);
		DashboardViewContext dashboardViewContext = new DashboardViewContext(
				userId, dashboardReportBean, request);
		model.put("data", provider.getPrintDetails(dashboardViewContext));
		return model;
	}

	/**
	 * it is responsible to export the report data to excel file
	 */
	@Override
	@RequestMapping(value = "{key}/{defaultFlag}/export")
	public void exportToExcel(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String key,
			@PathVariable Boolean defaultFlag) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered exportToExcel method of DashboardServiceImpl");
		}
		DashboardBO dashboardBO = boFactory.getDashboardBO();
		long userId = dashboardBO.getUserID();
		DashboardViewProvider provider = applicationContext.getBean(key,
				DashboardViewProvider.class);
		DashboardReportBean dashboardReportBean = dashboardBO
				.getDashboardReportBean(key, defaultFlag, userId);
		DashboardViewContext dashboardViewContext = new DashboardViewContext(
				userId, dashboardReportBean, request);
		provider.exportToExcel(response, dashboardViewContext);
	}
}