package com.walgreens.batch.central.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

@Component("EmailDailyPhotoDetailsSalesReport")
public class DailyPhotoDetailsSalesReportProvider extends
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
				.setSubject("Daily Photo Detailed Sales Report (Detailed Activities) for "
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

		map.put("orderPlaced",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport2OrderPlaced(), new Object[] {
								yesterdayMap.get("DATEWITHOUTTIME"),
								yesterdayMap.get("DATEWITHTIME"),
								lastYearSameDay.get("DATEWITHOUTTIME"),
								lastYearSameDay.get("DATEWITHTIME"),
								lastYearSameDate.get("DATEWITHOUTTIME"),
								lastYearSameDate.get("DATEWITHTIME") }));
		map.put("orderPlacedCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport2OrderPlacedCount(),
				new Object[] { yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("orderSold",
				omniJdbcTemplate.queryForList(
						EmailReportQuery.getReport2SoldRevenue(), new Object[] {
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
								lastYearSameDate.get("DATEWITHTIME") }));
		map.put("giftOderPlaced", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport2GiftOrderPlaced(),
				new Object[] { PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
					yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));

		map.put("giftOrderPlacedCount", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport2GiftOrderPlacedCount(),
				new Object[] { 
					PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
					yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME") }));
		map.put("giftOrderSold", omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport2GiftOrderSold(),
				new Object[] { PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
					yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						yesterdayMap.get("DATEWITHOUTTIME"),
						yesterdayMap.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						lastYearSameDay.get("DATEWITHOUTTIME"),
						lastYearSameDay.get("DATEWITHTIME"),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME"),
						lastYearSameDate.get("DATEWITHOUTTIME"),
						lastYearSameDate.get("DATEWITHTIME")}));
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
		List<Map<String, Object>> orderPlaced = (List<Map<String, Object>>) data
				.get("orderPlaced");
		List<Map<String, Object>> orderPlacedCount = (List<Map<String, Object>>) data
				.get("orderPlacedCount");
		List<Map<String, Object>> orderSold = (List<Map<String, Object>>) data
				.get("orderSold");
		Map<String, Object> uniqueKeys = getUniqueOriginType(orderPlaced,
				orderPlacedCount, orderSold);
		int count = 0;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<br>");
		if(uniqueKeys.size() > 1){
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals broken down by input channels:</div>");
			buffer.append("<br>");
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			buffer.append("<tr style='border-bottom:1px solid black;'>");
			buffer.append("<th align='left'>Type</th>"
					+ "<th>|"+convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th>|"+convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th>|SmDayYOY%</th>"
					+ "<th>|"+convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME"))+"</th>"
					+ "<th>|SmDateYOY%</th>");
			buffer.append("</tr>");
			
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				if(count != 0){
					buffer.append("<tr>");
					buffer.append("<td  align='left' colspan='5' style='font-weight: bold;'>---"+entry.getKey()+" Total---</td>");
					buffer.append("</tr>");
				
				Map<String, Object> outputData = (Map<String, Object>) entry.getValue();
				if(outputData.containsKey("aggregateOrderPlaced")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlaced");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>Orders Placed Revenue</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(outputData.containsKey("aggregateOrderSold")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderSold");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>Orders Sold Revenue</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>$"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")))+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				if(outputData.containsKey("aggregateOrderPlacedCount")){
					Map<String, Object> output = (Map<String, Object>) outputData.get("aggregateOrderPlacedCount");
					buffer.append("<tr>");
					buffer.append("<td align='left'  style='font-weight: bold;'>Orders Placed Count</td>"
							+ "<td align='right'>"+output.get("Yesterday")+"</td>"
							+ "<td align='right'>"+output.get("LstYrSmDay")+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")))+"</td>"
							+ "<td align='right'>"+output.get("LstYrSmDate")+"</td>"
							+ "<td align='right'>"+convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")))+"</td>");
					buffer.append("</tr>");
				}
				
				}
				count++;
			}
			buffer.append("</table>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Aggregated totals broken down by input channels:</div>");
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div>");
		}
		
		buffer.append("<br>");
		List<Map<String, Object>> giftOderPlaced = (List<Map<String, Object>>) data
				.get("giftOderPlaced");
		uniqueKeys = getUniqueOriginType1(giftOderPlaced);
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total PLACED order Revenue by categories-</div>");
		if(giftOderPlaced.size() > 1){
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) entry
						.getValue();
				count = 0;
				for (Map<String, Object> output : list) {
					renderBuffer(buffer, count, output);
					count++;
				}
			}
			buffer.append("</table><br>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
		}
		
		List<Map<String, Object>> giftOrderSold = (List<Map<String, Object>>) data
				.get("giftOrderSold");
		uniqueKeys = getUniqueOriginType1(giftOrderSold);
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total SOLD orders Revenue by categories-</div>");
		if(giftOrderSold.size() > 1){
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) entry
						.getValue();
				count = 0;
				for (Map<String, Object> output : list) {
					renderBuffer(buffer, count, output);
					count++;
				}
			}
			buffer.append("</table><br>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
		}
		
		List<Map<String, Object>> giftOrderPlacedCount = (List<Map<String, Object>>) data
				.get("giftOrderPlacedCount");
		/**
		 * to update order placed count by categories
		 */
		String orderOriginType = "";
		String category = "";
		String orderCountOriginType = "";
		if(CollectionUtils.isNotEmpty(giftOrderPlacedCount) && CollectionUtils.isNotEmpty(orderPlacedCount))
		{
			for(Map<String, Object>  giftOrderMap : giftOrderPlacedCount){
				orderOriginType = giftOrderMap.get(PhotoOmniConstants.ORDERORIGINTYPE).toString().
						replaceAll(PhotoOmniBatchConstants.HYPHEN, PhotoOmniConstants. EMPTY_SPACE_CHAR );
				category = giftOrderMap.get(PhotoOmniBatchConstants.CATEGORY).toString().
						replaceAll(PhotoOmniBatchConstants.HYPHEN,PhotoOmniConstants. EMPTY_SPACE_CHAR);
				if(PhotoOmniBatchConstants.TOTAL.equals(orderOriginType) && PhotoOmniConstants.CATEGORYBREAKUP.equals(category)){
					for( Map<String, Object> orderCountMap : orderPlacedCount){
						orderCountOriginType = orderCountMap.get(PhotoOmniConstants.ORDERORIGINTYPE).toString().
								replaceAll(PhotoOmniBatchConstants.HYPHEN, PhotoOmniConstants. EMPTY_SPACE_CHAR);
						if(orderOriginType.equals(orderCountOriginType)){
							giftOrderMap.put(PhotoOmniConstants.YESTERDAY, orderCountMap.get(PhotoOmniConstants.YESTERDAY));
							giftOrderMap.put(PhotoOmniConstants.SAMEDAY, orderCountMap.get(PhotoOmniConstants.SAMEDAY));
							giftOrderMap.put(PhotoOmniConstants.SAMEDAYCHANGE, orderCountMap.get(PhotoOmniConstants.SAMEDAYCHANGE));
							giftOrderMap.put(PhotoOmniConstants.SAMEDATE, orderCountMap.get(PhotoOmniConstants.SAMEDATE));
							giftOrderMap.put(PhotoOmniConstants.SAMEDATECHANGE, orderCountMap.get(PhotoOmniConstants.SAMEDATECHANGE));
						}
					}
				}else if(PhotoOmniBatchConstants.TOTAL.equals(category)){
					for( Map<String, Object> orderCountMap : orderPlacedCount){
						orderCountOriginType = orderCountMap.get(PhotoOmniConstants.ORDERORIGINTYPE).toString().
								replaceAll(PhotoOmniBatchConstants.HYPHEN, PhotoOmniConstants. EMPTY_SPACE_CHAR );
						if(orderOriginType.equals(orderCountOriginType)){
							giftOrderMap.put(PhotoOmniConstants.YESTERDAY, orderCountMap.get(PhotoOmniConstants.YESTERDAY));
							giftOrderMap.put(PhotoOmniConstants.SAMEDAY, orderCountMap.get(PhotoOmniConstants.SAMEDAY));
							giftOrderMap.put(PhotoOmniConstants.SAMEDAYCHANGE, orderCountMap.get(PhotoOmniConstants.SAMEDAYCHANGE));
							giftOrderMap.put(PhotoOmniConstants.SAMEDATE, orderCountMap.get(PhotoOmniConstants.SAMEDATE));
							giftOrderMap.put(PhotoOmniConstants.SAMEDATECHANGE, orderCountMap.get(PhotoOmniConstants.SAMEDATECHANGE));
						}
					}
				}

			}
		}
		uniqueKeys = getUniqueOriginType1(giftOrderPlacedCount);
		buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total PLACED orders COUNT by categories-</div>");
		if(giftOrderPlacedCount.size() > 1){
			buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
			renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
			for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
				List<Map<String, Object>> list = (List<Map<String, Object>>) entry
						.getValue();
				count = 0;
				for (Map<String, Object> output : list) {
					renderBufferCount(buffer, count, output);
					count++;
				}
			}
			buffer.append("</table><br>");
		}else{
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
		}
		
		
		return buffer.toString();
	}
	
	private void renderHeader(Map<String, Object> yesterdayMap,
			Map<String, Object> lastYearSameDay,
			Map<String, Object> lastYearSameDate, StringBuffer buffer) {
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>ORDER_ORIGIN_TYPE</th><th align='center' style='border-bottom:1px solid black;'>CATEGORY_NAME</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) yesterdayMap.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) lastYearSameDay.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDayYOY%</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) lastYearSameDate.get("DATEWITHOUTTIME"))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDateYOY%</th>");
		buffer.append("</tr>");
	}

	private void renderBuffer(StringBuffer buffer, int count,
			Map<String, Object> output) {
		buffer.append("<tr>");
		String style="";
		if(count == 0){
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' "+style+">"+(count==0?(String) output.get("ORDER_ORIGIN_TYPE"):"")+"</td>"
				+ "<td align='left' "+style+">"+(output.get("Category") == null ? "" : output.get("Category"))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("Yesterday")==null?0:output.get("Yesterday"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDay")==null?0:output.get("LstYrSmDay"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")==null?0:output.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("LstYrSmDate")==null?0:output.get("LstYrSmDate"))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?0:output.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}
	
	private void renderBufferCount(StringBuffer buffer, int count,
			Map<String, Object> output) {
		buffer.append("<tr>");
		String style="";
		if(count == 0){
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left' "+style+">"+(count==0?(String) output.get("ORDER_ORIGIN_TYPE"):"")+"</td>"
				+ "<td align='left' "+style+">"+(output.get("Category") == null ? "" : output.get("Category"))+"</td>"
				+ "<td align='right' "+style+">"+(output.get("Yesterday")==null?0:output.get("Yesterday"))+"</td>"
				+ "<td align='right' "+style+">"+(output.get("LstYrSmDay")==null?0:output.get("LstYrSmDay"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDayYOY%")==null?"0":output.get("SameDayYOY%"))))+"</td>"
				+ "<td align='right' "+style+">"+(output.get("LstYrSmDate")==null?0:output.get("LstYrSmDate"))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(output.get("SameDateYOY%")==null?"0":output.get("SameDateYOY%"))))+"</td>");
		buffer.append("</tr>");
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getUniqueOriginType(
			List<Map<String, Object>> orderPlaced,
			List<Map<String, Object>> orderPlacedCount,
			List<Map<String, Object>> orderSold) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Map<String, Object> map : orderPlaced) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if (!data.containsKey(key)) {
				Map<String, Object> aggregateOrderPlacedMap = new HashMap<String, Object>();
				aggregateOrderPlacedMap.put("aggregateOrderPlaced", map);
				data.put(key, aggregateOrderPlacedMap);
			} else {
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderPlaced", map);
				data.put(key, x);
			}
		}
		for (Map<String, Object> map : orderPlacedCount) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if (!data.containsKey(key)) {
				Map<String, Object> aggregateOrderPlacedCountMap = new HashMap<String, Object>();
				aggregateOrderPlacedCountMap.put("aggregateOrderPlacedCount",
						map);
				data.put(key, aggregateOrderPlacedCountMap);
			} else {
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderPlacedCount", map);
				data.put(key, x);
			}
		}
		for (Map<String, Object> map : orderSold) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if (!data.containsKey(key)) {
				Map<String, Object> aggregateOrderSoldMap = new HashMap<String, Object>();
				aggregateOrderSoldMap.put("aggregateOrderSold", map);
				data.put(key, aggregateOrderSoldMap);
			} else {
				Map<String, Object> x = (Map<String, Object>) data.get(key);
				x.put("aggregateOrderSold", map);
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
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> getUniqueOriginType1(
			List<Map<String, Object>> orderPlacedCreative) {
		Map<String, Object> data = new HashMap<String, Object>();
		for (Map<String, Object> map : orderPlacedCreative) {
			String key = (String) map.get("ORDER_ORIGIN_TYPE");
			if (!data.containsKey(key)) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				list.add(map);
				data.put(key, list);
			} else {
				List<Map<String, Object>> x = (List<Map<String, Object>>) data
						.get(key);
				x.add(map);
				data.put(key, x);
			}
		}
		return sortByValues1(data);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HashMap sortByValues1(Map map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				List<Map<String, Object>> obj1 = (List<Map<String, Object>>) ((Map.Entry) (o1))
						.getValue();
				Map<String, Object> obj11 = (Map<String, Object>) obj1.get(0);
				Long sortOrder1 = CommonUtil.bigDecimalToLong(obj11
						.get("SORTORDER2"));
				List<Map<String, Object>> obj2 = (List<Map<String, Object>>) ((Map.Entry) (o2))
						.getValue();
				Map<String, Object> obj22 = (Map<String, Object>) obj2.get(0);
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

