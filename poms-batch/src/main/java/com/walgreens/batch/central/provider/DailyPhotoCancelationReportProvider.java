package com.walgreens.batch.central.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.batch.query.EmailReportQuery;

@Component("EmailDailyPhotoCancelationReport")
public class DailyPhotoCancelationReportProvider extends
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
		emailReportBean.setSubject("Daily Photo Cancellation Report for "
				+ convertDateToString((String) yesterdayMap
						.get("DATEWITHOUTTIME")));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> yesterdayMap = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getYesterdayDate());
		Map<String, Object> lastYearSameDay = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearSameDay());
		Map<String, Object> lastYearSameDate = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearSameDate());
		map.put("yesterdayMap", yesterdayMap);
		map.put("lastYearSameDay", lastYearSameDay);
		map.put("lastYearSameDate", lastYearSameDate);
		map.put("totalQuantity", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport4TotalQuantity(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						"CANCEL",
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						"CANCEL",
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"), 
						"CANCEL"}));
		map.put("totalLost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport4TotalLost(),
						new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								"CANCEL",
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"),
								"CANCEL",
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"),
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"),
								"CANCEL"}));
		map.put("totalCost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport4TotalCost(),
						new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								"CANCEL",
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								"CANCEL",
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"),
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME"), 
								"CANCEL"}));
		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		Map<String, Object> data = emailReportBean.getData();
		Map<String, Object> yesterdayMap = (Map<String, Object>) data.get("yesterdayMap");
		Map<String, Object> lastYearSameDay = (Map<String, Object>) data.get("lastYearSameDay");
		Map<String, Object> lastYearSameDate = (Map<String, Object>) data.get("lastYearSameDate");
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total quantity of printed than cancelled orders broken down by channel- </div>");
		List<Map<String, Object>>  totalQuantity = (List<Map<String, Object>>) data.get("totalQuantity");
		if(totalQuantity.size()>1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : totalQuantity) {
				renderTDForCount(buffer, map, count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total lost revenue of printed than cancelled orders broken down by channel-</div> ");
		List<Map<String, Object>>  totalLost = (List<Map<String, Object>>) data.get("totalLost");
		if(totalLost.size()>1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : totalLost) {
				renderTD(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total cost of printed than cancelled orders broken down by channel-</div> ");
		List<Map<String, Object>>  totalCost = (List<Map<String, Object>>) data.get("totalCost");
		if(totalCost.size()>1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : totalCost) {
				renderTD(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		return buffer.toString();
	}

	private void renderTD(StringBuffer buffer, Map<String, Object> map, int count) {
		
		buffer.append("<tr>");
		String style = "";
		String val = (String) map.get("vendor_name");
		if ((val != null && (val.equalsIgnoreCase("-----TOTAL-----")
				|| val.equalsIgnoreCase("-----TOTAL AFFILIATE-----") || val
					.equalsIgnoreCase("-----TOTAL WALGREENS-----")))
				|| count == 0) {
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' style='font-weight: bold;'>"+ map.get("ORDER_ORIGIN_TYPE")+"</td>"
				+ "<td align='left' "+style+">"+(map.get("vendor_name") == null ? "" : map.get("vendor_name"))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Yesterday") == null?0:map.get("Yesterday"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDay")==null?0:map.get("LstYrSmDay"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%") == null?"0":map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDate")==null?0:map.get("LstYrSmDate"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%")==null?"0":map.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}
	
	private void renderTDForCount(StringBuffer buffer, Map<String, Object> map, int count) {

		buffer.append("<tr>");
		String style = "";
		String val = (String) map.get("vendor_name");
		if ((val != null && (val.equalsIgnoreCase("-----TOTAL-----")
				|| val.equalsIgnoreCase("-----TOTAL AFFILIATE-----") || val
					.equalsIgnoreCase("-----TOTAL WALGREENS-----")))
				|| count == 0) {
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' style='font-weight: bold;'>"+ map.get("ORDER_ORIGIN_TYPE")+"</td>"
				+ "<td align='left' "+style+">"+(map.get("vendor_name") == null ? "" : map.get("vendor_name"))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("Yesterday") == null?0:map.get("Yesterday"))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmDay")==null?0:map.get("LstYrSmDay"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%") == null?"0":map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmDate")==null?0:map.get("LstYrSmDate"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%")==null?"0":map.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	private void renderHeader(Map<String, Object> yesterdayMap,
			Map<String, Object> lastYearSameDay,
			Map<String, Object> lastYearSameDate, StringBuffer buffer) {
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>ORDER_ORIGIN_TYPE</th>"
				+ "<th align='center' style='border-bottom:1px solid black;'>VENDOR_NAME</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME")))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME")))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDayYOY%</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME")))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDateYOY%</th>");
		buffer.append("</tr>");
	}

}
