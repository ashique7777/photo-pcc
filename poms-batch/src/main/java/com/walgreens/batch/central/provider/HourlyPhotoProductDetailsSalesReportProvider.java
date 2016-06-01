/* Copyright (c) 2015, Walgreens Co. */
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.query.EmailReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * 	class which implements EmailReportDataProvider.
 *  provider class which will read data for hourly photo product details report 
 *  and render the same into required format
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
@Component("EmailHourlyPhotoProductDetailsSalesReport")
public class HourlyPhotoProductDetailsSalesReportProvider extends
		AbstractEmailReportDataProvider {
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HourlyPhotoProductDetailsSalesReportProvider.class);

	/**
	 * jdbcTemplate to fetch report data from db
	 */
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate omniJdbcTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.batch.central.provider.EmailReportDataProvider#populateData(EmailReportBean)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EmailReportBean populateData(EmailReportBean emailReportBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.populateData(EmailReportBean) method ");
		}
		try {
		Map<String, Object> data = populateData();
		emailReportBean.setData(data);
		Map<String, Object> yesterdayMap = (Map<String, Object>) data
				.get(PhotoOmniConstants.YESTERDAYMAP);
		Map<String, Object> yesterdayRangeMap = (Map<String, Object>) data
				.get(PhotoOmniConstants.YESTERDAYRANGE);
		emailReportBean
				.setSubject("Hourly Photo Product Details Sales Report for "
						+ convertDateToString((String) yesterdayMap
								.get(PhotoOmniConstants.DATEWITHOUTTIME)) + " as of "
						+ yesterdayRangeMap.get(PhotoOmniConstants.TIME));
		} catch (Exception e) {
			LOGGER.error(
					"Error at HourlyPhotoProductDetailsSalesReportProvider.populateData(EmailReportBean) method",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.populateData(EmailReportBean) method");
			}
		}
		return emailReportBean;
	}

	/**
	 * Method will fetch data for hourly photo details report for yesterday, lastyearSameDay and lastYearSameDate 
	 * and calculate the percentage change yesterday with lastyearSameDay and lastYearSameDate
	 * 
	 * @return Map<String, Object> -- E-mail report raw data
	 */
	private Map<String, Object> populateData() {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.populateData() method ");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
		Map<String, Object> yesterdayMap = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getTodayDate());
		Map<String, Object> yesterdayRangeMap = getDatesForHourlyReport((String) yesterdayMap
				.get(PhotoOmniConstants.DATEWITHOUTTIME));
		Map<String, Object> lastYearSameDay = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearTodaySameDay());
		Map<String, Object> lastYearSameDayRangeMap = getDatesForHourlyReportsameDay((String) lastYearSameDay
				.get(PhotoOmniConstants.DATEWITHOUTTIME));
		Map<String, Object> lastYearSameDate = omniJdbcTemplate.queryForMap(
				EmailReportQuery.getLastYearTodaySameDate());
		Map<String, Object> lastYearSameDateRangeMap = getDatesForHourlyReportsameDate((String) lastYearSameDate
				.get(PhotoOmniConstants.DATEWITHOUTTIME));
		map.put(PhotoOmniConstants.YESTERDAYMAP, yesterdayMap);
		map.put(PhotoOmniConstants.LASTYEARSAMEDAY, lastYearSameDay);
		map.put(PhotoOmniConstants.LASTYEARSAMEDATE, lastYearSameDate);
		map.put(PhotoOmniConstants.YESTERDAYRANGE, yesterdayRangeMap);

		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Execution completed for calculating dates used for report generation");
		}
		map.put(PhotoOmniConstants.ORDERPLACEDTRADITIONAL, omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport8OrderPlaced(PhotoOmniConstants.TRADITIONAL),
				new Object[] { yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get(PhotoOmniConstants.DATEWITHOUTTIME),
						yesterdayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.ONEHOUR)}));
		
		map.put(PhotoOmniConstants.ORDERSOLDTRADITIONAL, omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport8OrderSold(PhotoOmniConstants.TRADITIONAL),
				new Object[] { yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get(PhotoOmniConstants.DATEWITHOUTTIME),
						yesterdayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.ONEHOUR)}));
		
		map.put(PhotoOmniConstants.ORDERPLACEDCREATIVE, omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport8OrderPlaced(PhotoOmniConstants.CREATIVE),
				new Object[] { yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get(PhotoOmniConstants.DATEWITHOUTTIME),
						yesterdayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.ONEHOUR) }));
		
		map.put(PhotoOmniConstants.ORDERSOLDCREATIVE, omniJdbcTemplate.queryForList(
				EmailReportQuery.getReport8OrderSold(PhotoOmniConstants.CREATIVE),
				new Object[] { yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.PREDATE),
						yesterdayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						yesterdayMap.get(PhotoOmniConstants.DATEWITHOUTTIME),
						yesterdayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDay.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDayRangeMap.get(PhotoOmniConstants.ONEHOUR),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.PREDATE),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.POSTDATE),
						PhotoOmniBatchConstants.PRODUCT_CATEGORY_TYPE,
						lastYearSameDate.get(PhotoOmniConstants.DATEWITHOUTTIME),
						lastYearSameDateRangeMap.get(PhotoOmniConstants.ONEHOUR)}));
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Query execution for E-mail report executed sucessfully");
			}
		} catch (DataAccessException e) {
			LOGGER.error(
					"Error at While fetching report data from DB at HourlyPhotoProductDetailsSalesReportProvider."
							+ "populateData() method", e);
		} catch (Exception e) {
			LOGGER.error(
					"Error at HourlyPhotoProductDetailsSalesReportProvider.populateData() method",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.populateData() method");
			}
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.batch.central.provider.EmailReportDataProvider#processEmailBody(EmailReportBean)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public String processEmailBody(EmailReportBean emailReportBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.processEmailBody() method ");
		}
		StringBuffer buffer = new StringBuffer();

		try{
			Map<String, Object> data = emailReportBean.getData();
			List<Map<String, Object>> orderPlacedCreative = (List<Map<String, Object>>) data
					.get(PhotoOmniConstants.ORDERPLACEDCREATIVE);
			List<Map<String, Object>> orderSoldCreative = (List<Map<String, Object>>) data
					.get(PhotoOmniConstants.ORDERSOLDCREATIVE);
			List<Map<String, Object>> orderPlacedTraditional = (List<Map<String, Object>>) data
					.get(PhotoOmniConstants.ORDERPLACEDTRADITIONAL);
			List<Map<String, Object>> orderSoldTraditional = (List<Map<String, Object>>) data
					.get(PhotoOmniConstants.ORDERSOLDTRADITIONAL);

			Map<String, Object> yesterdayMap = (Map<String, Object>) data.get(PhotoOmniConstants.YESTERDAYMAP);
			Map<String, Object> lastYearSameDay = (Map<String, Object>) data.get(PhotoOmniConstants.LASTYEARSAMEDAY);
			Map<String, Object> lastYearSameDate = (Map<String, Object>) data.get(PhotoOmniConstants.LASTYEARSAMEDATE);

			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Setting data for order placed for Traditional categories");
			}
			Map<String, Object> uniqueKeys = getUniqueOriginType(orderPlacedTraditional);
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total PLACED Sales for Traditional categories-</div>");
			if(uniqueKeys.size() > 1){
				buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
				renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
				for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					int count = 0;
					for (Map<String, Object> output : list) {
						renderBuffer(buffer, count, output);
						count++;
					}
				}
				buffer.append("</table><br>");
			}else{
				buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
			}

			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Setting data for orders sold for Traditional categories");
			}
			uniqueKeys = getUniqueOriginType(orderSoldTraditional);
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total SOLD Sales for Traditional categories-</div>");
			if(uniqueKeys.size() > 1){
				buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
				renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
				for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					int count = 0;
					for (Map<String, Object> output : list) {
						renderBuffer(buffer, count, output);
						count++;
					}
				}
				buffer.append("</table><br>");
			}else{
				buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Setting data for orders placed for Creative categories");
			}
			uniqueKeys = getUniqueOriginType(orderPlacedCreative);
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total PLACED Sales for Creative categories-</div>");
			if(uniqueKeys.size() > 1){
				buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
				renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
				for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					int count = 0;
					for (Map<String, Object> output : list) {
						renderBuffer(buffer, count, output);
						count++;
					}
				}
				buffer.append("</table><br>");
			}else{
				buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Setting data for orders sold for Creative categories");
			}
			uniqueKeys = getUniqueOriginType(orderSoldCreative);
			buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Total SOLD Sales for Creative categories-</div>");
			if(uniqueKeys.size() > 1){
				buffer.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
				renderHeader(yesterdayMap, lastYearSameDay, lastYearSameDate, buffer);
				for (Map.Entry<String, Object> entry : uniqueKeys.entrySet()) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) entry.getValue();
					int count = 0;
					for (Map<String, Object> output : list) {
						renderBuffer(buffer, count, output);
						count++;
					}
				}
				buffer.append("</table><br>");
			}else{
				buffer.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>No data available.</div><br>");
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Data population completed for horuly report");
			}
		}catch (Exception e) {
			LOGGER.error("Error at HourlyPhotoProductDetailsSalesReportProvider.processEmailBody() method" , e);
		}finally{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.processEmailBody() method ");
			}
		}
		return buffer.toString();
	}

	/**
	 * To create header details for hourly photo product E-Mail report
	 * 
	 * @param yesterdayMap -- Contains yesterday report data
	 * @param lastYearSameDay -- Contains last year same day report data
	 * @param lastYearSameDate -- Contains last yeast same date report data
	 * @param buffer -- Email report data string
	 */
	private void renderHeader(Map<String, Object> yesterdayMap,
			Map<String, Object> lastYearSameDay,
			Map<String, Object> lastYearSameDate, StringBuffer buffer) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.renderHeader() method");
		}
		buffer.append("<tr>");
		buffer.append("<th align='left' style='border-bottom:1px solid black;'>"+ PhotoOmniConstants.ORDERORIGINTYPE 
				+"</th><th align='center' style='border-bottom:1px solid black;'>"+ PhotoOmniConstants.CATEGORYNAME+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) yesterdayMap.get(PhotoOmniConstants.DATEWITHOUTTIME))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) lastYearSameDay.get(PhotoOmniConstants.DATEWITHOUTTIME))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDayYOY%</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>"+convertDateToString((String) lastYearSameDate.get(PhotoOmniConstants.DATEWITHOUTTIME))+"</th>"
				+ "<th align='right' style='border-bottom:1px solid black;'>SmDateYOY%</th>");
		buffer.append("</tr>");
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.renderHeader() method");
		}
	}

	/**
	 * Method to populate report data for ourly photo product E-Mail report body
	 * 
	 * @param buffer -- Email report data string
	 * @param count -- list of unique origin type
	 * @param output -- sorted report data
	 */
	private void renderBuffer(StringBuffer buffer, int count,
			Map<String, Object> output) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.renderBuffer() method");
		}
		buffer.append("<tr>");
		String style="";
		String val = (String) output.get( PhotoOmniConstants.CATEGORYNAME);
		if ((val != null && (val.equalsIgnoreCase("---"+ PhotoOmniConstants.CATEGORYBREAKUP+ "---"))))
		{
			style = "style='font-weight: bold;'";
		}
		buffer.append("<td align='left'  style='font-weight: bold;'>"+(count==0?(String) output.get(PhotoOmniConstants.ORDERORIGINTYPE):"")+"</td>"
				+ "<td align='left' "+style+">"+ (output.get(PhotoOmniConstants.CATEGORYNAME) == null ? "" : output.get(PhotoOmniConstants.CATEGORYNAME))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(
						output.get(PhotoOmniConstants.YESTERDAY)==null?0:output.get(PhotoOmniConstants.YESTERDAY))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(
						output.get(PhotoOmniConstants.SAMEDAY)==null?0:output.get(PhotoOmniConstants.SAMEDAY))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(
						output.get(PhotoOmniConstants.SAMEDAYCHANGE)==null?"0":output.get(PhotoOmniConstants.SAMEDAYCHANGE))))+"</td>"
				+ "<td align='right' "+style+">$"+(convertFloatToString(CommonUtil.bigDecimalToDouble(
						output.get(PhotoOmniConstants.SAMEDATE)==null?0:output.get(PhotoOmniConstants.SAMEDATE))))+"</td>"
				+ "<td align='right' "+style+">"+(convertFloatToString(CommonUtil.bigDecimalToDouble(
						output.get(PhotoOmniConstants.SAMEDATECHANGE)==null?"0":output.get(PhotoOmniConstants.SAMEDATECHANGE))))+"</td>");
		buffer.append("</tr>");
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.renderBuffer() method");
		}
	}

	/** 
	 * Method to get list of unique order origin type from the raw e-mail report data
	 * 
	 * @param orderPlacedCreative -- Email report data
	 * @return Map<String, object> -- E-mail report sorted using sort order
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getUniqueOriginType(
			List<Map<String, Object>> orderPlacedCreative) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.getUniqueOriginType() method");
		}
		Map<String, Object> data = new HashMap<String, Object>();
		for (Map<String, Object> map : orderPlacedCreative) {
			String key = (String) map.get(PhotoOmniConstants.ORDERORIGINTYPE);
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
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.getUniqueOriginType() method");
		}
		return sortByValues(data);
	}

	/**
	 * To sort the E-mail report raw data based on sortOrder 
	 * before rendering it into required E-Mail format
	 * 
	 * @param map -- Contains E-mail report raw data in key value pair
	 * @return HashMap -- Returns Map with sorted data
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HashMap sortByValues(Map map) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into HourlyPhotoProductDetailsSalesReportProvider.sortByValues() method");
		}
		HashMap sortedHashMap = new LinkedHashMap();
		try {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				List<Map<String, Object>> obj1 = (List<Map<String, Object>>) ((Map.Entry) (o1))
						.getValue();
				Map<String, Object> obj11 = (Map<String, Object>) obj1.get(0);
				Long sortOrder1 = CommonUtil.bigDecimalToLong(obj11
						.get(PhotoOmniConstants.SORTORDERTWO));
				List<Map<String, Object>> obj2 = (List<Map<String, Object>>) ((Map.Entry) (o2))
						.getValue();
				Map<String, Object> obj22 = (Map<String, Object>) obj2.get(0);
				Long sortOrder2 = CommonUtil.bigDecimalToLong(obj22
						.get(PhotoOmniConstants.SORTORDERTWO));
				return sortOrder1.compareTo(sortOrder2);
			}
		});
		// Here copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		}catch (Exception e) {
			LOGGER.error("Error at HourlyPhotoProductDetailsSalesReportProvider.sortByValues() method" , e);
		}finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from HourlyPhotoProductDetailsSalesReportProvider.sortByValues() method");
			}
		}
		return sortedHashMap;
	}

}