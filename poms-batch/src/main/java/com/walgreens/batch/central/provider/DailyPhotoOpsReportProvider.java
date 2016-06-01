package com.walgreens.batch.central.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailDailyPhotoOpsReport")
public class DailyPhotoOpsReportProvider extends
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
		emailReportBean.setSubject("Daily Photo Ops Report for "
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
		map.put("atleastOneOrderProcessed", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3AtleastOneOrderProcessed(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("countOfOrder", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3CountOfOrder(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME")
						}));
		map.put("placedOrderRevenue", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3PlacedOrderRevenue(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("anticipatedDollarValue", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3AnticipatedDollarValue(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("placedOrderAvgRevenuePerOrder", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3AvgRevenuePerOrder(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("percentOrderNotReady", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport3PercentOrderNotReady(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME")}));
		map.put("avgNoPrint",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport3AvgNoPrint(), new Object[] {
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME")}));
		map.put("totalNoPrint", omniJdbcTemplate.queryForList(EmailReportQuery
				.getReport3TotalNoPrint(), new Object[] {
					yesterdayMap.get("DATEWITHOUTTIME"),
					yesterdayMap.get("DATEWITHTIME"),
					lastYearSameDay.get("DATEWITHOUTTIME"),
					lastYearSameDay.get("DATEWITHTIME"),
					lastYearSameDate.get("DATEWITHOUTTIME"),
					lastYearSameDate.get("DATEWITHTIME") }));
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
		List<Map<String, Object>> orderProcessed = (List<Map<String, Object>>) data.get("atleastOneOrderProcessed");
		if(orderProcessed.size() > 0){
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Number of stores that processed at least one order- </div>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			buffer.append("<tr>");
			buffer.append("<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME")))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME")))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>SmDayYOY%</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>"+(convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME")))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>SmDateYOY%</th>");
			buffer.append("</tr>");
			buffer.append("<tr>");
			buffer.append("<td align='right'>"+orderProcessed.get(0).get("YESTERDAY")+"</td>"
							+ "<td align='right'>"+orderProcessed.get(0).get("LSTYRSMDAY")+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(orderProcessed.get(0).get("SameDayYOY%")==null?0:orderProcessed.get(0).get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>"+orderProcessed.get(0).get("LSTYRSMDATE")+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(orderProcessed.get(0).get("SameDateYOY%")==null?0:orderProcessed.get(0).get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
			buffer.append("</table>");
		}
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Count of orders received by stores broken down by channels- </div>");
		List<Map<String, Object>>  countOfOrder = (List<Map<String, Object>>) data.get("countOfOrder");
		if(countOfOrder.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : countOfOrder) {
				renderTD1(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Placed order revenue from orders received by stores broken down by channels-</div>");
		List<Map<String, Object>>  placedOrderRevenue = (List<Map<String, Object>>) data.get("placedOrderRevenue");
		if(placedOrderRevenue.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : placedOrderRevenue) {
				renderTD3(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Anticipated dollar value of discounts given to customers from orders received by stores broken down by channel-</div> ");
		List<Map<String, Object>>  anticipatedDollarValue = (List<Map<String, Object>>) data.get("anticipatedDollarValue");
		if(anticipatedDollarValue.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : anticipatedDollarValue) {
				renderTD2(buffer, map,count,true);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Placed order average revenue per order from orders received by stores broken down by channel-</div>");
		List<Map<String, Object>>  placedOrderAvgRevenuePerOrder = (List<Map<String, Object>>) data.get("placedOrderAvgRevenuePerOrder");
		if(placedOrderAvgRevenuePerOrder.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : placedOrderAvgRevenuePerOrder) {
				renderTD2(buffer, map,count,true);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Percentage of orders not ready by the promised time from orders received by stores broken down by channel-</div>");
		List<Map<String, Object>>  percentOrderNotReady = (List<Map<String, Object>>) data.get("percentOrderNotReady");
		if(percentOrderNotReady.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : percentOrderNotReady) {
				renderTD2(buffer, map,count,false);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Average number of prints per order from orders received by stores broken down by channel-</div>");
		List<Map<String, Object>>  avgNoPrint = (List<Map<String, Object>>) data.get("avgNoPrint");
		if(avgNoPrint.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : avgNoPrint) {
				renderTD4(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		
		buffer.append("<br>");
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total numbers of prints from order received by stores broken down by channel-</div>");
		List<Map<String, Object>>  totalNoPrint = (List<Map<String, Object>>) data.get("totalNoPrint");
		if(totalNoPrint.size() > 1){
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			int count = 0;
			for (Map<String, Object> map : totalNoPrint) {
				renderTD1(buffer, map,count);
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		return buffer.toString();
	}

	private void renderTD3(StringBuffer buffer, Map<String, Object> map, int count) {
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
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Yesterday($)")==null?0:map.get("Yesterday($)"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDay($)")==null?0:map.get("LstYrSmDay($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%")==null?0:map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDate($)")==null?0:map.get("LstYrSmDate($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%")==null?0:map.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	private void renderTD2(StringBuffer buffer, Map<String, Object> map, int count, boolean showDollar) {
		buffer.append("<tr>");
		String style = "";
		String val = (String) map.get("vendor_name");
		if ((val != null && (val.equalsIgnoreCase("-----TOTAL-----")
				|| val.equalsIgnoreCase("-----TOTAL AFFILIATE-----") || val
					.equalsIgnoreCase("-----TOTAL WALGREENS-----")))
				|| count == 0) {
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' style='font-weight: bold;'>"+map.get("ORDER_ORIGIN_TYPE")+"</td>"
				+ "<td align='left' "+style+">"+(map.get("vendor_name") == null ? "" : map.get("vendor_name"))+"</td>"
				+ "<td align='right' "+style+">"+(showDollar ? "$":"")+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Yesterday($)")==null?"0":map.get("Yesterday($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(showDollar ? "$":"")+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDay($)")==null?"0":map.get("LstYrSmDay($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%")==null?"0":map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">"+(showDollar ? "$":"")+( convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDate($)")==null?"0":map.get("LstYrSmDate($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%")==null?"0":map.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	private void renderTD1(StringBuffer buffer, Map<String, Object> map, int count) {
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
				+ "<td align='right' "+style+">"+(map.get("Yesterday") == null?0:map.get("Yesterday"))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmDay")==null?0:map.get("LstYrSmDay"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%") == null?"0":map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">"+(map.get("LstYrSmDate")==null?0:map.get("LstYrSmDate"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDateYOY%")==null?"0":map.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	
	private void renderTD4(StringBuffer buffer, Map<String, Object> map, int count) {
		buffer.append("<tr>");
		String style = "";
		String val = (String) map.get("vendor_name");
		if ((val != null && (val.equalsIgnoreCase("-----TOTAL-----")
				|| val.equalsIgnoreCase("-----TOTAL AFFILIATE-----") || val
					.equalsIgnoreCase("-----TOTAL WALGREENS-----")))
				|| count == 0) {
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' style='font-weight: bold;'>"+map.get("ORDER_ORIGIN_TYPE")+"</td>"
				+ "<td align='left' "+style+">"+(map.get("vendor_name") == null ? "" : map.get("vendor_name"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("Yesterday($)") == null?0:map.get("Yesterday($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDay($)")==null?0:map.get("LstYrSmDay($)"))))+"</td>"
				+ "<td align='right' "+style+">"+( convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("SameDayYOY%") == null?"0":map.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(map.get("LstYrSmDate($)")==null?0:map.get("LstYrSmDate($)"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble (map.get("SameDateYOY%")==null?"0":map.get("SameDateYOY%"))))+"</td>");
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
