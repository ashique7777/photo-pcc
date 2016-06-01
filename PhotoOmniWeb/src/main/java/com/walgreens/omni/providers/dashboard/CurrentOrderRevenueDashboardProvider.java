package com.walgreens.omni.providers.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.omni.factory.DashboardBOFactory;
import com.walgreens.omni.utility.DashboardReportQuery;
import com.walgreens.omni.utility.dashboard.AbstractDashboardViewProvider;
import com.walgreens.omni.utility.dashboard.DashboardViewContext;
import com.walgreens.omni.utility.dashboard.ExportToExcel;

/**
 * 
 * @author CTS
 * 
 */
@Component("currentOrderRevenue")
public class CurrentOrderRevenueDashboardProvider extends
		AbstractDashboardViewProvider {

	@Autowired
	private ExportToExcel excel;

	private static final Logger logger = LoggerFactory
			.getLogger(CurrentOrderRevenueDashboardProvider.class);

	final SimpleDateFormat formatter = new SimpleDateFormat(
			"MM/dd/yyyy h:mm:ss a z");
	
	

	@Autowired
	public CurrentOrderRevenueDashboardProvider(DashboardBOFactory boFactory,
			@Qualifier("reportDS") DataSource dataSource) {
		setDataSource(dataSource);
		setDashboardBOFactory(boFactory);
		setStoreTemplatePath("res/template/dashboard/currentorder/store.template.html");
	}

	/**
	 * it is responsible to provide data for filters in report
	 */
	@Override
	public Map<String, Object> getFilterTemplateData(
			DashboardViewContext context) {
		Map<String, Object> map = new HashMap<String, Object>();
		return map;
	}

	/**
	 * it is responsible to provide data for report
	 */
	@Override
	public Map<String, Object> getStoreTemplateData(DashboardViewContext context) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> list = getJdbcTemplate().queryForList(
					DashboardReportQuery.getCurrentOderRevenueDashboardQuery()
					.toString());
			map.put("data", list);
			if (list.size() > 0) {
				map.put("asOfDate", getAsOfDateForCurrentOrderRevenue());
			} else {
				map.put("asOfDate", CommonUtil.stringDateFormatChange(
						getAsOfDateForCurrentOrderRevenue(),
						"MM/dd/yyyy h:mm:ss a z", "MM/dd/yyyy"));
			}
			map.put("reportName", "Current Order Revenue");
		} catch (Exception e) {
			logger.error("Error occured in getStoreTemplateData method of CurrentOrderRevenueDashboardProvider class "
					+ e);
		}
		return map;
	}

	private String getAsOfDateForCurrentOrderRevenue() throws ParseException {
		/*Map<String, Object> mapObj = getJdbcTemplate().queryForMap(
				DashboardReportQuery.getAsOfDateQuery().toString());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		boolean dateBoolean = false;
		if ((mapObj.get("ORDER_PLACED") == null)
				&& (mapObj.get("ORDER_SOLD") == null)) {
			return formatter.format(new Date());
		} else if ((mapObj.get("ORDER_PLACED") == null)
				&& (mapObj.get("ORDER_SOLD") != null)) {
			return formatter.format(simpleDateFormat.parse(mapObj.get(
					"ORDER_SOLD").toString()));
		} else if ((mapObj.get("ORDER_PLACED") != null)
				&& (mapObj.get("ORDER_SOLD") == null)) {
			return formatter.format(simpleDateFormat.parse(mapObj.get(
					"ORDER_PLACED").toString()));
		} else {
			try {
				dateBoolean = simpleDateFormat.parse(
						mapObj.get("ORDER_PLACED").toString()).before(
						simpleDateFormat.parse(mapObj.get("ORDER_SOLD")
								.toString()));
			} catch (Exception e) {
				logger.error("Error occured in getAsOfDateForCurrentOrderRevenue method of CurrentOrderRevenueDashboardProvider class while comparing order place and order sold date "
						+ e);
			}
			if (dateBoolean == true) {
				return formatter.format(simpleDateFormat.parse(mapObj.get(
						"ORDER_SOLD").toString()));
			} else {
				return formatter.format(simpleDateFormat.parse(mapObj.get(
						"ORDER_PLACED").toString()));
			}
		}*/
		return formatter.format(new Date());
	}

	/**
	 * it is responsible to provide data for print
	 */
	@Override
	public Map<String, Object> getPrintDetails(
			DashboardViewContext dashboardViewContext) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data",
				getJdbcTemplate().queryForList(
						DashboardReportQuery
								.getCurrentOderRevenueDashboardQuery()
								.toString()));
		map.put("reportName", "Current Order Revenue");
		return map;
	}

	/**
	 * it is responsible to provide data for excel file
	 */
	@Override
	public void exportToExcel(HttpServletResponse response,
			DashboardViewContext dashboardViewContext) {
		Map<String, String> headerTitles = new HashMap<String, String>();
		headerTitles.put("CHANNEL", "Channel");
		headerTitles.put("PLACED_ORDER_REVENUE", "Placed Order Revenue");
		headerTitles.put("SOLD_ORDER_REVENUE", "Sold Order Revenue");
		excel.export(response, DashboardReportQuery
				.getCurrentOderRevenueDashboardQuery().toString(), null,
				headerTitles, "Current Order Revenue", "CurrentOrderRevenue");

	}

}
