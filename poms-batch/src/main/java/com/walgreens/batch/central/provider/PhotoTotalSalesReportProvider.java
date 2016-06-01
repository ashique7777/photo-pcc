package com.walgreens.batch.central.provider;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailPhotoTotalSalesReport")
public class PhotoTotalSalesReportProvider extends
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
				.setSubject("Daily Photo Total Sales Report (Total Activities) "
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
		
		map.put("aggregateOrderPlaced", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport1AggregateOrderPlaced(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));

		map.put("aggregateOrderPlacedCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport1AggregateOrderPlacedCount(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("aggregateOrderSold", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport1AggregateOrderSold(),
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
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("aggregateOrderSoldCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport1AggregateOrderSoldCount(),
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
						lastYearSameDate.get("DATEWITHTIME") }));
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		Map<String, Object> data = emailReportBean.getData();
		List<Map<String, Object>> aggregateOrderPlaced = (List<Map<String, Object>>) data
				.get("aggregateOrderPlaced");
		List<Map<String, Object>> aggregateOrderPlacedCount = (List<Map<String, Object>>) data
				.get("aggregateOrderPlacedCount");
		List<Map<String, Object>> aggregateOrderSold = (List<Map<String, Object>>) data
				.get("aggregateOrderSold");
		List<Map<String, Object>> aggregateOrderSoldCount = (List<Map<String, Object>>) data
				.get("aggregateOrderSoldCount");
		Map<String, Object> yesterdayMap = (Map<String, Object>) data.get("yesterdayMap");
		Map<String, Object> lastYearSameDay = (Map<String, Object>) data.get("lastYearSameDay");
		Map<String, Object> lastYearSameDate = (Map<String, Object>) data.get("lastYearSameDate");
		
		Map<String, Object> uniqueKeys = getUniqueOriginType(
				aggregateOrderPlaced, aggregateOrderPlacedCount,
				aggregateOrderSold, aggregateOrderSoldCount);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		
		if(uniqueKeys.size() > 1){
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals across channels:</div>");
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			buffer.append("<tr>");
			buffer.append("<th align='left' style='border-bottom:1px solid black;'>Type</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>|SmDayYOY%</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th align='right' style='border-bottom:1px solid black;'>|SmDateYOY%</th>");
			buffer.append("</tr>");
			
			int count = 0;
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				if(count == 1){
					buffer.append("<br>");
					buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals broken down by channels:</div><br>");
					buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
					buffer.append("<tr>");
					buffer.append("<th align='left' style='border-bottom:1px solid black;'>Type</th>"
							+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME"))+"</th>"
							+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME"))+"</th>"
							+ "<th align='right' style='border-bottom:1px solid black;'>|SmDayYOY%</th>"
							+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME"))+"</th>"
							+ "<th align='right' style='border-bottom:1px solid black;'>|SmDateYOY%</th>");
					buffer.append("</tr>");
				}
				if(count != 0){
					buffer.append("<tr>");
					buffer.append("<td  align='left' colspan='5' style='font-weight: bold;'>------"+entry.getKey()+" Total------</td>");
					buffer.append("</tr>");
				}
				Map<String, Object> outputData = (Map<String, Object>) entry.getValue();
				if(outputData.containsKey("aggregateOrderPlaced")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlaced");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>"+upperToCamelCase(entry.getKey())+" Orders Placed Revenue</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday") == null ? "0" : output.get("Yesterday")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay") == null ? "0" : output.get("LstYrSmDay")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%") == null ? "0":output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")==null?"0":output.get("LstYrSmDate")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(outputData.containsKey("aggregateOrderSold")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSold");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>"+upperToCamelCase(entry.getKey())+" Orders Sold Revenue</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday") == null ? "0" : output.get("Yesterday")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay") == null ? "0" : output.get("LstYrSmDay")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%") == null ? "0":output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")==null?"0":output.get("LstYrSmDate")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(outputData.containsKey("aggregateOrderPlacedCount")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlacedCount");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>"+upperToCamelCase(entry.getKey())+"  Orders Placed Count</td>"
							+ "<td align='right'>"+(output.get("Yesterday") == null ? "0" : output.get("Yesterday"))+"</td>"
							+ "<td align='right'>"+(output.get("LstYrSmDay") == null ? "0" : output.get("LstYrSmDay"))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%") == null ? "0":output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>"+(output.get("LstYrSmDate")==null?"0":output.get("LstYrSmDate"))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(outputData.containsKey("aggregateOrderSoldCount")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSoldCount");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>"+upperToCamelCase(entry.getKey())+"  Orders Sold Count</td>"
							+ "<td align='right'>"+(output.get("Yesterday") == null ? "0" : output.get("Yesterday"))+"</td>"
							+ "<td align='right'>"+(output.get("LstYrSmDay") == null ? "0" : output.get("LstYrSmDay"))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%") == null ? "0":output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>"+(output.get("LstYrSmDate")==null?"0":output.get("LstYrSmDate"))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(count == 0){
					buffer.append("</table>");
				}
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals across channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
			
			buffer.append("<br><div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals broken down by channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		return buffer.toString();
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getUniqueOriginType(
			List<Map<String, Object>> aggregateOrderPlaced,
			List<Map<String, Object>> aggregateOrderPlacedCount,
			List<Map<String, Object>> aggregateOrderSold,
			List<Map<String, Object>> aggregateOrderSoldCount) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Map<String, Object> map : aggregateOrderPlaced) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if(!data.containsKey(key)){
				Map<String, Object> aggregateOrderPlacedMap = new HashMap<String, Object>();
				aggregateOrderPlacedMap.put("aggregateOrderPlaced", map);
				data.put(key, aggregateOrderPlacedMap);
			}else{
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderPlaced", map);
				data.put(key, x);
			}
		}
		for (Map<String, Object> map : aggregateOrderPlacedCount) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if(!data.containsKey(key)){
				Map<String, Object> aggregateOrderPlacedCountMap = new HashMap<String, Object>();
				aggregateOrderPlacedCountMap.put("aggregateOrderPlacedCount", map);
				data.put(key, aggregateOrderPlacedCountMap);
			}else{
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderPlacedCount", map);
				data.put(key, x);
			}
		}
		for (Map<String, Object> map : aggregateOrderSold) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if(!data.containsKey(key)){
				Map<String, Object> aggregateOrderSoldMap = new HashMap<String, Object>();
				aggregateOrderSoldMap.put("aggregateOrderSold", map);
				data.put(key, aggregateOrderSoldMap);
			}else{
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderSold", map);
				data.put(key, x);
			}
		}
		for (Map<String, Object> map : aggregateOrderSoldCount) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if(!data.containsKey(key)){
				Map<String, Object> aggregateOrderSoldCountMap = new HashMap<String, Object>();
				aggregateOrderSoldCountMap.put("aggregateOrderSoldCount", map);
				data.put(key, aggregateOrderSoldCountMap);
			}else{
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderSoldCount", map);
				data.put(key, x);
			}
		}
		

		return sortByValues(data);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HashMap sortByValues(Map map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map<String, Object> obj1 = (Map<String, Object>) ((Map.Entry) (o1))
						.getValue();
				Map<String, Object> obj11 = (Map<String, Object>) obj1
						.get("aggregateOrderPlaced");
				if (obj11 == null) {
					obj11 = (Map<String, Object>) obj1
							.get("aggregateOrderSoldCount");
					if (obj11 == null) {
						obj11 = (Map<String, Object>) obj1
								.get("aggregateOrderSold");
						if (obj11 == null) {
							obj11 = (Map<String, Object>) obj1
									.get("aggregateOrderPlacedCount");
						}
					}
				}
				Long sortOrder1 = CommonUtil.bigDecimalToLong(obj11
						.get("SORTORDER2"));
				Map<String, Object> obj2 = (Map<String, Object>) ((Map.Entry) (o2))
						.getValue();
				Map<String, Object> obj22 = (Map<String, Object>) obj2
						.get("aggregateOrderPlaced");
				if (obj22 == null) {
					obj22 = (Map<String, Object>) obj1
							.get("aggregateOrderSoldCount");
					if (obj22 == null) {
						obj22 = (Map<String, Object>) obj1
								.get("aggregateOrderSold");
						if (obj22 == null) {
							obj22 = (Map<String, Object>) obj1
									.get("aggregateOrderPlacedCount");
						}
					}
				}
				Long sortOrder2 = CommonUtil.bigDecimalToLong(obj22
						.get("SORTORDER2"));
				return sortOrder1.compareTo(sortOrder2);
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	
}
