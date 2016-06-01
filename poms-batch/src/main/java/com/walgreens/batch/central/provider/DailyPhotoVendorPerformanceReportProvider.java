package com.walgreens.batch.central.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailDailyVendorPerformanceReport")
public class DailyPhotoVendorPerformanceReportProvider extends
		AbstractEmailReportDataProvider {

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate omniJdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public EmailReportBean populateData(EmailReportBean emailReportBean) {
		Map<String, Object> data = populateData();
		emailReportBean.setData(data);
		Map<String, Object> yesterdayMap = (Map<String, Object>) data
				.get("yesterdayMap");
		emailReportBean
				.setSubject("Daily Photo Gift Vendor Performance Report for "
						+ convertDateToString((String) yesterdayMap
								.get("DATEWITHOUTTIME")));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> yesterdayMap = omniJdbcTemplate
				.queryForMap(EmailReportQuery.getYesterdayDate());
		Map<String, Object> lastYearSameDay = omniJdbcTemplate
				.queryForMap(EmailReportQuery.getLastYearSameDay());
		Map<String, Object> lastYearSameDate = omniJdbcTemplate
				.queryForMap(EmailReportQuery.getLastYearSameDate());
		map.put("yesterdayMap", yesterdayMap);
		map.put("lastYearSameDay", lastYearSameDay);
		map.put("lastYearSameDate", lastYearSameDate);
		map.put("orderPlaced",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport5OrderPlaced(), new Object[] {
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.GIFTS,
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.GIFTS,
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.GIFTS }));
		map.put("orderReceived", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport5OrderReceived(),
				new Object[] {
				/* Binding variables for YESTERDAY_TMP query */
				PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						/* Binding variables for LAST_YR_SAME_DAY_TMP query */
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						/* Binding variables for LAST_YR_SAME_DATE_TMP query */
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME")
						}));
		map.put("orderCancelled", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport5OrderCalcelled(),
				new Object[] { PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						PhotoOmniBatchConstants.CANCEL,
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						PhotoOmniBatchConstants.CANCEL,
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.GIFTS,
						PhotoOmniBatchConstants.CANCEL }));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		Map<String, Object> data = emailReportBean.getData();
		Map<String, Object> yesterdayMap = (Map<String, Object>) data
				.get("yesterdayMap");
		Map<String, Object> lastYearSameDay = (Map<String, Object>) data
				.get("lastYearSameDay");
		Map<String, Object> lastYearSameDate = (Map<String, Object>) data
				.get("lastYearSameDate");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total Gift orders Placed broken down by vendors- </div>");
		List<Map<String, Object>> orderPlaced = (List<Map<String, Object>>) data
				.get("orderPlaced");
		if (orderPlaced.size() > 1) {
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate,
					buffer);
			int count = 0;
			for (Map<String, Object> map : orderPlaced) {
				renderTD(buffer, map, count);
				count++;
			}
			buffer.append("</table>");
		} else {
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}

		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total Gift orders Received broken down by vendors-</div> ");
		List<Map<String, Object>> orderReceived = (List<Map<String, Object>>) data
				.get("orderReceived");
		if (orderReceived.size() > 1) {
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate,
					buffer);
			int count = 0;
			for (Map<String, Object> map : orderReceived) {
				renderTD(buffer, map, count);
				count++;
			}
			buffer.append("</table>");
		} else {
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}

		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total Gift orders Cancelled broken down by vendors-</div> ");
		List<Map<String, Object>> orderCancelled = (List<Map<String, Object>>) data
				.get("orderCancelled");
		if (orderCancelled.size() > 1) {
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate,
					buffer);
			int count = 0;
			for (Map<String, Object> map : orderCancelled) {
				renderTD(buffer, map, count);
				count++;
			}
			buffer.append("</table>");
		} else {
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		return buffer.toString();
	}

	private void renderTD(StringBuffer buffer, Map<String, Object> map,
			int count) {

		buffer.append("<tr>");
		String style = "";
		String val = (String) map.get("vendor_name");
		if ((val != null && (val.equalsIgnoreCase("-----TOTAL-----")
				|| val.equalsIgnoreCase("-----TOTAL AFFILIATE-----") || val
					.equalsIgnoreCase("-----TOTAL WALGREENS-----")))
				|| count == 0) {
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' style='font-weight: bold;'>"
				+ map.get("ORDER_ORIGIN_TYPE")
				+ "</td>"
				+ "<td align='left' "
				+ style
				+ ">"
				+ (map.get("vendor_name") == null ? "" : map.get("vendor_name"))
				+ "</td>"
				+ "<td align='right' "
				+ style
				+ ">"
				+ (map.get("Yesterday") == null ? 0 : map.get("Yesterday"))
				+ "</td>"
				+ "<td align='right' "
				+ style
				+ ">"
				+ (map.get("LstYrSmDay") == null ? 0 : map.get("LstYrSmDay"))
				+ "</td>"
				+ "<td align='right' "
				+ style
				+ ">"
				+ (convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%") == null ? "0" : map.get("SameDayYOY%"))))
				+ "</td>"
				+ "<td align='right' "
				+ style
				+ ">"
				+ (map.get("LstYrSmDate") == null ? 0 : map.get("LstYrSmDate"))
				+ "</td>"
				+ "<td align='right' "
				+ style
				+ ">"
				+ (convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%") == null ? "0" : map.get("SameDateYOY%")))) + "</td>");
		buffer.append("</tr>");
	}

	private void renderHeader(Map<String, Object> yesterdayMap,
			Map<String, Object> lastYearSameDay,
			Map<String, Object> lastYearSameDate, StringBuffer buffer) {
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>ORDER_ORIGIN_TYPE</th>"
				+ "<th align='center' style='border-bottom:1px solid black;'>VENDOR_NAME</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"
				+ (convertDateToString((String) yesterdayMap
						.get("DATEWITHOUTTIME")))
				+ "</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"
				+ (convertDateToString((String) lastYearSameDay
						.get("DATEWITHOUTTIME")))
				+ "</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDayYOY%</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"
				+ (convertDateToString((String) lastYearSameDate
						.get("DATEWITHOUTTIME")))
				+ "</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDateYOY%</th>");
		buffer.append("</tr>");
	}
}
