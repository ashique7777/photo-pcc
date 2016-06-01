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

@Component("EmailWeeklyPhotoCancelationReport")
public class WeeklyPhotoCancelationReportProvider extends
		AbstractEmailReportDataProvider {

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate omniJdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public EmailReportBean populateData(EmailReportBean emailReportBean) {
		Map<String, Object> data = populateData();
		emailReportBean.setData(data);
		Map<String, Object> lastWeek = (Map<String, Object>) data
				.get("lastWeek");
		emailReportBean
				.setSubject("Weekly Photo Cancellation Report for week ending on "
						+ convertDateToString((String) lastWeek
								.get("DATEWITHTIME")));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> lastWeek = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastWeek());
		map.put("lastWeek", lastWeek);
		Map<String, Object> lastYearSmWeek = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearSameWeek());
		map.put("lastYearSmWeek", lastYearSmWeek);
		map.put("totalQuantity", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport9TotalQuantity(),
				new Object[] { lastWeek.get("DATEWITHOUTTIME"),
						lastWeek.get("DATEWITHTIME"),
						lastWeek.get("DATEWITHOUTTIME"),
						lastWeek.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.CANCEL,
						lastYearSmWeek.get("DATEWITHOUTTIME"),
						lastYearSmWeek.get("DATEWITHTIME"),
						lastYearSmWeek.get("DATEWITHOUTTIME"),
						lastYearSmWeek.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.CANCEL}));
		map.put("totalLost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport9TotalLost(),
						new Object[] { lastWeek.get("DATEWITHOUTTIME"),
								lastWeek.get("DATEWITHTIME"),
								lastWeek.get("DATEWITHOUTTIME"),
								lastWeek.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL,
								lastYearSmWeek.get("DATEWITHOUTTIME"),
								lastYearSmWeek.get("DATEWITHTIME"),
								lastYearSmWeek.get("DATEWITHOUTTIME"),
								lastYearSmWeek.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL}));
		map.put("totalCost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport9TotalCost(),
						new Object[] { lastWeek.get("DATEWITHOUTTIME"),
								lastWeek.get("DATEWITHTIME"),
								lastWeek.get("DATEWITHOUTTIME"),
								lastWeek.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL,
								lastYearSmWeek.get("DATEWITHOUTTIME"),
								lastYearSmWeek.get("DATEWITHTIME"),
								lastYearSmWeek.get("DATEWITHOUTTIME"),
								lastYearSmWeek.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL}));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		Map<String, Object> data = emailReportBean.getData();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total quantity of printed than cancelled orders broken down by channel-</div> ");
		List<Map<String, Object>>  totalQuantity = (List<Map<String, Object>>) data.get("totalQuantity");
		if(totalQuantity.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(buffer);
			int count = 0;
			for (Map<String, Object> map : totalQuantity) {
				renderTDCount(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total lost revenue of printed than cancelled orders broken down by channel-</div> ");
		List<Map<String, Object>>  totalLost = (List<Map<String, Object>>) data.get("totalLost");
		if(totalLost.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(buffer);
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
		if(totalCost.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(buffer);
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


	private void renderTD(StringBuffer buffer, Map<String, Object> map,int count) {
		
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
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LastWeek")==null?0:map.get("LastWeek"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmWeek")==null?0:map.get("LstYrSmWeek"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameWeekYOY%")==null?"0":map.get("SameWeekYOY%"))))+"</td>");
		buffer.append("</tr>");
	}
	
private void renderTDCount(StringBuffer buffer, Map<String, Object> map,int count) {
		
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
				+ "<td align='right' "+style+">"+(map.get("LastWeek")==null?0:map.get("LastWeek"))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmWeek")==null?0:map.get("LstYrSmWeek"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameWeekYOY%")==null?"0":map.get("SameWeekYOY%"))))+"</td>");
		buffer.append("</tr>");
	}


	private void renderHeader(StringBuffer buffer) {
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>ORDER_ORIGIN_TYPE</th>"
				+ "<th align='center' style='border-bottom:1px solid black;'>VENDOR_NAME</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>Lastweek</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>LstYrSmWeek</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SameWeekYOY%</th>");
		buffer.append("</tr>");
	}
}

