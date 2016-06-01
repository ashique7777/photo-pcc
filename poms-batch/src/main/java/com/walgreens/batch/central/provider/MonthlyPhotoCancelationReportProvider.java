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

@Component("EmailMonthlyPhotoCancelationReport")
public class MonthlyPhotoCancelationReportProvider extends
		AbstractEmailReportDataProvider {

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate omniJdbcTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public EmailReportBean populateData(EmailReportBean emailReportBean) {
		Map<String, Object> data = populateData();
		emailReportBean.setData(data);
		Map<String, Object> lastMonth = (Map<String, Object>) data
				.get("lastMonth");
		emailReportBean.setSubject("Monthly Photo Cancellation Report for "
				+ convertDateToString((String) lastMonth.get("DATEWITHTIME")));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> lastMonth = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastMonth());
		map.put("lastMonth", lastMonth);
		Map<String, Object> lastYearSmMonth = omniJdbcTemplate.queryForList(
				EmailReportQuery.getLastYearSameMonth()).get(0);
		map.put("lastYearSmMonth", lastYearSmMonth);
		map.put("totalQuantity", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport10TotalQuantity(),
				new Object[] { lastMonth.get("DATEWITHOUTTIME"),
						lastMonth.get("DATEWITHTIME"),
						lastMonth.get("DATEWITHOUTTIME"),
						lastMonth.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.CANCEL,
						lastYearSmMonth.get("DATEWITHOUTTIME"),
						lastYearSmMonth.get("DATEWITHTIME"),
						lastYearSmMonth.get("DATEWITHOUTTIME"),
						lastYearSmMonth.get("DATEWITHTIME") ,
						PhotoOmniBatchConstants.CANCEL}));
		map.put("totalLost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport10TotalLost(),
						new Object[] { lastMonth.get("DATEWITHOUTTIME"),
								lastMonth.get("DATEWITHTIME"),
								lastMonth.get("DATEWITHOUTTIME"),
								lastMonth.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL,
								lastYearSmMonth.get("DATEWITHOUTTIME"),
								lastYearSmMonth.get("DATEWITHTIME"),
								lastYearSmMonth.get("DATEWITHOUTTIME"),
								lastYearSmMonth.get("DATEWITHTIME") ,
								PhotoOmniBatchConstants.CANCEL}));
		map.put("totalCost",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport10TotalCost(),
						new Object[] { lastMonth.get("DATEWITHOUTTIME"),
								lastMonth.get("DATEWITHTIME"),
								lastMonth.get("DATEWITHOUTTIME"),
								lastMonth.get("DATEWITHTIME"),
								PhotoOmniBatchConstants.CANCEL,
								lastYearSmMonth.get("DATEWITHOUTTIME"),
								lastYearSmMonth.get("DATEWITHTIME"),
								lastYearSmMonth.get("DATEWITHOUTTIME"),
								lastYearSmMonth.get("DATEWITHTIME"),
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
				+ "<td align='left' "+style+">"+ (map.get("vendor_name") == null ? "" : map.get("vendor_name"))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LastMonth")==null?0:map.get("LastMonth"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmMonth")==null?0:map.get("LstYrSmMonth"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameMonthYOY%")==null?"0":map.get("SameMonthYOY%"))))+"</td>");
		buffer.append("</tr>");
	}
	
private void renderTDCount(StringBuffer buffer, Map<String, Object> map, int count) {
		
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
				+ "<td align='right' "+style+">"+(map.get("LastMonth")==null?0:map.get("LastMonth"))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmMonth")==null?0:map.get("LstYrSmMonth"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameMonthYOY%")==null?"0":map.get("SameMonthYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	private void renderHeader(StringBuffer buffer) {
		
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>ORDER_ORIGIN_TYPE</th>"
				+ "<th align='center' style='border-bottom:1px solid black;'>VENDOR_NAME</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>LastMonth</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>LstYrSmMonth</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SameMonthYOY%</th>");
		buffer.append("</tr>");
	}

}

