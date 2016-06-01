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
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailHourlyPhotoTotalSalesReport")
public class HourlyPhotoTotalSalesReportProvider extends
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
		Map<String, Object> yesterdayRangeMap = (Map<String, Object>) data
				.get("yesterdayRangeMap");
		emailReportBean.setSubject("Hourly Photo Total Sales Report for "
				+ convertDateToString((String) yesterdayMap
						.get("DATEWITHOUTTIME")) + " as of "
				+ yesterdayRangeMap.get("time"));
		return emailReportBean;
	}

	private Map<String, Object> populateData() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> yesterdayMap = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getTodayDate());
		Map<String, Object> yesterdayRangeMap = getDatesForHourlyReport((String) yesterdayMap
				.get("DATEWITHOUTTIME"));
		Map<String, Object> lastYearSameDay = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearTodaySameDay());
		Map<String, Object> lastYearSameDayRangeMap = getDatesForHourlyReportsameDay((String) lastYearSameDay
				.get("DATEWITHOUTTIME"));
		Map<String, Object> lastYearSameDate = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearTodaySameDate());
		Map<String, Object> lastYearSameDateRangeMap = getDatesForHourlyReportsameDate((String) lastYearSameDate
				.get("DATEWITHOUTTIME"));
		map.put("yesterdayMap", yesterdayMap);
		map.put("lastYearSameDay", lastYearSameDay);
		map.put("lastYearSameDate", lastYearSameDate);
		map.put("yesterdayRangeMap", yesterdayRangeMap);

		map.put("aggregateOrderPlaced", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderPlaced(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus")}));

		map.put("aggregateOrderPlacedCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderPlacedCount(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus")}));
		
		map.put("aggregateOrderSold", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderSold(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus")}));
		
		map.put("aggregateOrderSoldCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderSoldCount(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus")}));

		map.put("aggregateOrderPlacedWithGift", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderPlacedGifts(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus") }));
		
		map.put("aggregateOrderPlacedCountWithGift", omniJdbcTemplate
				.queryForList(
						EmailReportQuery
								.getReport7AggregateOrderPlacedCountGifts(),
						new Object[] { yesterdayRangeMap.get("preDate"),
								yesterdayRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayRangeMap.get("oneHourMinus"),
								lastYearSameDayRangeMap.get("preDate"),
								lastYearSameDayRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDayRangeMap.get("oneHourMinus"),
								lastYearSameDateRangeMap.get("preDate"),
								lastYearSameDateRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDateRangeMap.get("oneHourMinus") }));
		
		map.put("aggregateOrderSoldWithGift", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport7AggregateOrderSoldGifts(),
				new Object[] { yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						yesterdayRangeMap.get("preDate"),
						yesterdayRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayRangeMap.get("oneHourMinus"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						lastYearSameDayRangeMap.get("preDate"),
						lastYearSameDayRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDayRangeMap.get("oneHourMinus"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						lastYearSameDateRangeMap.get("preDate"),
						lastYearSameDateRangeMap.get("postDate"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						PhotoOmniBatchConstants.GIFTS,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDateRangeMap.get("oneHourMinus")}));
		
		map.put("aggregateOrderSoldCountWithGift", omniJdbcTemplate
				.queryForList(
						EmailReportQuery
								.getReport7AggregateOrderSoldCountGifts(),
						new Object[] { yesterdayRangeMap.get("preDate"),
								yesterdayRangeMap.get("postDate"),
								yesterdayRangeMap.get("preDate"),
								yesterdayRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayRangeMap.get("oneHourMinus"),
								lastYearSameDayRangeMap.get("preDate"),
								lastYearSameDayRangeMap.get("postDate"),
								lastYearSameDayRangeMap.get("preDate"),
								lastYearSameDayRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDayRangeMap.get("oneHourMinus"),
								lastYearSameDateRangeMap.get("preDate"),
								lastYearSameDateRangeMap.get("postDate"),
								lastYearSameDateRangeMap.get("preDate"),
								lastYearSameDateRangeMap.get("postDate"),
								PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
								PhotoOmniBatchConstants.GIFTS,
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDateRangeMap.get("oneHourMinus")}));
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
		int count = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		if(uniqueKeys.size() > 1){
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals across channels:</div>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				if(count == 1){
					buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'> <br>Aggregated totals broken down by channels:</div>");
					buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
					renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate,
							buffer);
				}
				if(count != 0){
					buffer.append("<tr>");
					buffer.append("<td  align='left' colspan='5' style='font-weight: bold;'>------"+upperToCamelCase(entry.getKey())+" Total------</td>");
					buffer.append("</tr>");
				}
				Map<String, Object> outputData = (Map<String, Object>) entry.getValue();
				if(outputData.containsKey("aggregateOrderPlaced")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlaced");
					String text =upperToCamelCase(entry.getKey())+" Orders Placed Revenue";
					renderTD(buffer, output, text);
				}
				if(outputData.containsKey("aggregateOrderSold")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSold");
					String text = upperToCamelCase(entry.getKey())+" Orders Sold Revenue";
					renderTD(buffer, output, text);
				}
				if(outputData.containsKey("aggregateOrderPlacedCount")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlacedCount");
					String text = upperToCamelCase(entry.getKey())+" Orders Placed Count";
					renderTDCount(buffer, output, text);
				}
				if(outputData.containsKey("aggregateOrderSoldCount")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSoldCount");
					String text = upperToCamelCase(entry.getKey())+" Orders Sold Count"; 
					renderTDCount(buffer, output, text);
				}
				if(count == 0){
					buffer.append("</table>");
				}
				count++;
			}
			if(count > 0 && count == (uniqueKeys.size())){
				buffer.append("</table>");
			}
			
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals across channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals broken down by channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
			
		}

		
		List<Map<String, Object>> aggregateOrderPlacedWithGift = (List<Map<String, Object>>) data
				.get("aggregateOrderPlacedWithGift");
		List<Map<String, Object>> aggregateOrderPlacedCountWithGift = (List<Map<String, Object>>) data
				.get("aggregateOrderPlacedCountWithGift");
		List<Map<String, Object>> aggregateOrderSoldWithGift = (List<Map<String, Object>>) data
				.get("aggregateOrderSoldWithGift");
		List<Map<String, Object>> aggregateOrderSoldCountWithGift = (List<Map<String, Object>>) data
				.get("aggregateOrderSoldCountWithGift");
		
		uniqueKeys = getUniqueOriginType(
				aggregateOrderPlacedWithGift, aggregateOrderPlacedCountWithGift,
				aggregateOrderSoldWithGift, aggregateOrderSoldCountWithGift);
		
		buffer.append("<br>");
		count = 0;
		if(uniqueKeys.size() > 1){
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				if(count == 0){
					buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals for GIFT orders broken down by channels:</div>");
					buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
					renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate,
							buffer);
				}else{
					buffer.append("<tr>");
					buffer.append("<td  align='left' colspan='5' style='font-weight: bold;'>------"+upperToCamelCase(entry.getKey())+" Total------</td>");
					buffer.append("</tr>");
					Map<String, Object> outputData = (Map<String, Object>) entry.getValue();
					if(outputData.containsKey("aggregateOrderPlaced")){
						Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlaced");
						String text = entry.getKey()+" Orders Placed Revenue";
						renderTD(buffer, output, text);
					}
					if(outputData.containsKey("aggregateOrderSold")){
						Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSold");
						String text = entry.getKey()+" Orders Sold Revenue";
						renderTD(buffer, output, text);
					}
					if(outputData.containsKey("aggregateOrderPlacedCount")){
						Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlacedCount");
						String text = entry.getKey()+" Orders Placed Count";
						renderTDCount(buffer, output, text);
					}
					if(outputData.containsKey("aggregateOrderSoldCount")){
						Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSoldCount");
						String text = entry.getKey()+" Orders Sold Count";
						renderTDCount(buffer, output, text);
					}	
				}
				count++;
			}
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals for GIFT orders broken down by channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("</table>");
		return buffer.toString();
	
	}

	private void renderTD(StringBuffer buffer, Map<String, Object> output,
			String text) {
		buffer.append("<tr>");
		buffer.append("<td align='left'  style='font-weight: bold;'>"+text+"</td>"
				+ "<td align='right'>$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday")==null?0:output.get("Yesterday"))))+"</td>"
				+ "<td align='right'>$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay")==null?0:output.get("LstYrSmDay"))))+"</td>"
				+ "<td align='right'>"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")==null?"0":output.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right'>$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")==null?0:output.get("LstYrSmDate"))))+"</td>"
				+ "<td align='right'>"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}
	
	private void renderTDCount(StringBuffer buffer, Map<String, Object> output,
			String text) {
		buffer.append("<tr>");
		buffer.append("<td align='left'  style='font-weight: bold;'>"+text+"</td>"
				+ "<td align='right'>"+(output.get("Yesterday")==null?0:output.get("Yesterday"))+"</td>"
				+ "<td align='right'>"+(output.get("LstYrSmDay")==null?0:output.get("LstYrSmDay"))+"</td>"
				+ "<td align='right'>"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")==null?"0":output.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right'>"+(output.get("LstYrSmDate")==null?0:output.get("LstYrSmDate"))+"</td>"
				+ "<td align='right'>"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	private void renderHeader(Map<String, Object> yesterdayMap,
			Map<String, Object> lastYearSameDay,
			Map<String, Object> lastYearSameDate, StringBuffer buffer) {
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>Type</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>|SmDayYOY%</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>|"+convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>|SmDateYOY%</th>");
		buffer.append("</tr>");
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