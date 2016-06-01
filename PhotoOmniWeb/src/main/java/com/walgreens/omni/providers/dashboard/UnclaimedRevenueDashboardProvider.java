package com.walgreens.omni.providers.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Component("unclaimedOrder")
public class UnclaimedRevenueDashboardProvider extends
		AbstractDashboardViewProvider {

	@Autowired
	private ExportToExcel excel;

	private static final String[] orderType = { "Kiosk", "Internet", "Mobile" };

	private static final String[] STRINGS = { "OM_1_TO_30_DAYS",
			"OM_31_TO_60_DAYS", "OM_61_TO_90_DAYS" };

	@Autowired
	public UnclaimedRevenueDashboardProvider(DashboardBOFactory boFactory,
			@Qualifier("reportDS") DataSource dataSource) {
		setDataSource(dataSource);
		setDashboardBOFactory(boFactory);
		setStoreTemplatePath("res/template/dashboard/unclaimedorder/store.template.html");
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
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		map.put("data", getFormattedUnclamimedRevenueData());
		map.put("asOfDate",
				new SimpleDateFormat("MM/dd/yyyy").format(calendar.getTime())
						.toString());
		map.put("reportName", "Unclaimed Orders");
		return map;
	}

	/**
	 * it is responsible to provide data for print
	 */
	@Override
	public Map<String, Object> getPrintDetails(
			DashboardViewContext dashboardViewContext) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", getFormattedUnclamimedRevenueData());
		map.put("reportName", "Unclaimed Orders");
		return map;
	}

	/**
	 * it is responsible to provide data for excel file
	 */
	@Override
	public void exportToExcel(HttpServletResponse response,
			DashboardViewContext dashboardViewContext) {

		Map<String, String> headerTitles = new HashMap<String, String>();
		headerTitles.put("ORDERTYPE", "Channel/Days Past Due");
		headerTitles.put("OM_1_TO_30_DAYS", "1 TO 30_DAYS");
		headerTitles.put("OM_31_TO_60_DAYS", "31 TO 60 DAYS");
		headerTitles.put("OM_61_TO_90_DAYS", "61 TO 90 DAYS");
		/*
		 * excel.export(response, DashboardReportQuery
		 * .getUnclaimedDashboardQuery().toString(), null, headerTitles,
		 * "Unclaimed Orders", "UnclaimedOrders");
		 */

	}

	private List<Map<String, Object>> getFormattedUnclamimedRevenueData() {
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> formattedUnclamimedRevenueDataList = getJdbcTemplate()
				.queryForList(
						DashboardReportQuery.getUnclaimedDashboardQuery()
								.toString());
		for (String order : orderType) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ORDERTYPE", order);
			for (Map<String, Object> unclamimedMap : formattedUnclamimedRevenueDataList) {
				if (order.equals(unclamimedMap.get("ORDER_ORIGIN_TYPE"))) {
					map.put((String) unclamimedMap.get("CATEGORY"),
							unclamimedMap.get("COUNT"));
				}
			}
			dataList.add(generateUnclamimedRevenueData(map));
		}
		return dataList;
	}

	private Map<String, Object> generateUnclamimedRevenueData(
			Map<String, Object> map) {
		for (String str : STRINGS) {
			if (!map.containsKey(str)) {
				map.put(str, 0);
			}
		}
		return map;
	}
}