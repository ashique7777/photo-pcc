/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.provider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * <p>
 * 	Abstract class which implements EmailReportDataProvider.
 *  This class will get dates for hourly report
 *  used for generating reports for  hourly  reports
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public abstract class AbstractEmailReportDataProvider  
		implements EmailReportDataProvider {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEmailReportDataProvider.class);

	/**
	 * Method to convert floating value to string value with precision up to two digits
	 * 
	 * @param value -- Floating value to be converted to string 
	 * @return -- floating value converted to string
	 */
	public String convertFloatToString(Double value) {
		return String.format("%.2f", value);
	}

	/**
	 * Method to convert source date format to target date format
	 * 
	 * @param value -- String date
	 * @return String --- Converted date string
	 */
	public String convertDateToString(String value) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into AbstractEmailReportDataProvider.convertDateToString() method");
		}
		SimpleDateFormat sourceDateFormat = new SimpleDateFormat(PhotoOmniConstants.DATEFORMATONE);
		SimpleDateFormat targetDateFormat = new SimpleDateFormat(PhotoOmniConstants.DATEFORMATTWO);
		String parsedDate = null;
		try {
			Date date = sourceDateFormat.parse(value);
			parsedDate = targetDateFormat.format(date);
		} catch (ParseException e) {
			LOGGER.error(
					"Error occured while parsing date string at AbstractEmailReportDataProvider."
							+ "convertDateToString() method", e);
		}catch (Exception e) {
			LOGGER.error("Error at AbstractEmailReportDataProvider."
					+ "convertDateToString() method", e);
		}
		finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting from AbstractEmailReportDataProvider.convertDateToString() method");
			}
		}
		return parsedDate;
	}

	/**
	 * Method to convert giving string to camelCase string
	 * 
	 * @param value -- String to be converted to camelCase
	 * @return String -- processed String
	 */
	public String upperToCamelCase(String value) {
		if (StringUtils.isNotBlank(value) && StringUtils.isNotEmpty(value)) {
			value = value.toLowerCase();
			return Character.toUpperCase(value.charAt(0)) + value.substring(1);
		} else {
			return StringUtils.EMPTY;
		}

	}

	/**
	 * Method to get dates for hourly report for same day 
	 * 
	 * @param dateString -- Current date String
	 * @return Map<String, Object> -- Map containing key value pair of required dates
	 */
	public Map<String, Object> getDatesForHourlyReport(String dateString) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into AbstractEmailReportDataProvider.getDatesForHourlyReport() method");
		}
		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat timeFormat = new SimpleDateFormat(PhotoOmniConstants.HOURFORMAT);
		SimpleDateFormat timeFormat1 = new SimpleDateFormat(PhotoOmniConstants.LOCALE);
		SimpleDateFormat timeFormat2 = new SimpleDateFormat(PhotoOmniConstants.TIMEFORMAT);
		SimpleDateFormat targetDateFormat = new SimpleDateFormat(
				PhotoOmniConstants.DATEFORMATTHREE);
		SimpleDateFormat otherDateFormat = new SimpleDateFormat(
				PhotoOmniConstants.DATEFORMATONE);

		Calendar calendarStart = Calendar.getInstance();
		calendarStart.add(Calendar.DATE, -1);
		map.put(PhotoOmniConstants.PREDATE, otherDateFormat.format(calendarStart.getTime()));
	
		Calendar calendarendDate = Calendar.getInstance();
		calendarendDate.add(Calendar.HOUR, +23);
		map.put(PhotoOmniConstants.POSTDATE, targetDateFormat.format(calendarendDate.getTime()));
		
		String timeString = timeFormat.format(calendarendDate.getTime());
		try {
			Date date = targetDateFormat.parse(dateString + "  " + timeString+ ":59:59"+ timeFormat1.format(calendarendDate.getTime()));
			Calendar calendarOneHourMinusDate = Calendar.getInstance();
			calendarOneHourMinusDate.setTime(date);
			calendarOneHourMinusDate.add(Calendar.HOUR, 0);
			map.put(PhotoOmniConstants.ONEHOUR,
					targetDateFormat.format(calendarOneHourMinusDate.getTime()));
			map.put(PhotoOmniConstants.TIME, timeFormat2.format(calendarOneHourMinusDate.getTime()));
		} catch (ParseException e) {
			LOGGER.error(
					"Error occured while parsing date string at AbstractEmailReportDataProvider."
							+ "getDatesForHourlyReport() method", e);
		} catch (Exception e) {
			LOGGER.error("Error occured at AbstractEmailReportDataProvider."
					+ "getDatesForHourlyReport() method", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from AbstractEmailReportDataProvider.getDatesForHourlyReport() method");
			}
		}
		return map;

	}
	
	/**
	 * Method to get dates for hourly report for last year same day 
	 * 
	 * @param dateString -- last year same date String
	 * @return Map<String, Object> -- Map containing key value pair of required dates
	 */
	public Map<String, Object> getDatesForHourlyReportsameDay(String dateString) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into AbstractEmailReportDataProvider.getDatesForHourlyReportsameDay() method");
		}
		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat timeFormat = new SimpleDateFormat(PhotoOmniConstants.TIMEFORMATTWO);
		SimpleDateFormat targetDateFormat = new SimpleDateFormat(
				PhotoOmniConstants.DATEFORMATTHREE);
		SimpleDateFormat otherDateFormat = new SimpleDateFormat(PhotoOmniConstants.DATEFORMATONE);

		try {
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.add(Calendar.YEAR, -1);
			map.put(PhotoOmniConstants.PREDATE, otherDateFormat.format(calendarStart.getTime()));

			Calendar calendarendDate = Calendar.getInstance();
			calendarendDate.setTime(targetDateFormat.parse(dateString+ " "+ timeFormat.format(new Date())));
			calendarendDate.add(Calendar.HOUR, +23);
			map.put(PhotoOmniConstants.POSTDATE, targetDateFormat.format(calendarendDate.getTime()));

			String timeString = timeFormat.format(calendarendDate.getTime());
			Date date = targetDateFormat.parse(dateString + "  " + timeString);
			Calendar calendarOneHourMinusDate = Calendar.getInstance();
			calendarOneHourMinusDate.setTime(date);
			calendarOneHourMinusDate.add(Calendar.HOUR, 0);
			SimpleDateFormat locale = new SimpleDateFormat(PhotoOmniConstants.LOCALE);
			SimpleDateFormat hourFormat = new SimpleDateFormat(PhotoOmniConstants.HOURFORMAT);
			map.put(PhotoOmniConstants.ONEHOUR,
					otherDateFormat.format(calendarOneHourMinusDate.getTime())+ " " +
					hourFormat.format(calendarOneHourMinusDate.getTime())+ ":59:59" + locale.format(calendarOneHourMinusDate.getTime()));
		} catch (ParseException e) {
			LOGGER.error(
					"Error occured while parsing date string at AbstractEmailReportDataProvider."
							+ "getDatesForHourlyReportsameDay() method", e);
		} catch (Exception e) {
			LOGGER.error("Error occured at AbstractEmailReportDataProvider."
					+ "getDatesForHourlyReportsameDay() method", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from AbstractEmailReportDataProvider.getDatesForHourlyReportsameDay() method");
			}
		}
		return map;

	}

	/**
	 * Method to get dates for hourly report for last year same date 
	 * 
	 * @param dateString -- last year same date string
	 * @return Map<String, Object> -- Map containing key value pair of required dates
	 */
	public Map<String, Object> getDatesForHourlyReportsameDate(String dateString) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into AbstractEmailReportDataProvider.getDatesForHourlyReportsameDate() method");
		}
		Map<String, Object> map = new HashMap<String, Object>();

		SimpleDateFormat timeFormat = new SimpleDateFormat(PhotoOmniConstants.TIMEFORMATTWO);
		SimpleDateFormat targetDateFormat = new SimpleDateFormat(
				PhotoOmniConstants.DATEFORMATTHREE);
		SimpleDateFormat otherDateFormat = new SimpleDateFormat(PhotoOmniConstants.DATEFORMATONE);

		try {
			Calendar calendarStart = Calendar.getInstance();
			calendarStart.add(Calendar.YEAR, -1);
			Date date1 = new Date();
			date1.setTime(calendarStart.getTime().getTime() - (1000 * 60 * 60 * 24));
			map.put(PhotoOmniConstants.PREDATE, otherDateFormat.format(date1.getTime()));

			Calendar calendarendDate = Calendar.getInstance();
			calendarendDate.setTime(targetDateFormat.parse(dateString+ " "+ timeFormat.format(new Date())));
			calendarendDate.add(Calendar.HOUR, +23);
			map.put(PhotoOmniConstants.POSTDATE, targetDateFormat.format(calendarendDate.getTime()));

			String timeString = timeFormat.format(calendarendDate.getTime());
			Date date = targetDateFormat.parse(dateString + "  " + timeString);
			Calendar calendarOneHourMinusDate = Calendar.getInstance();
			calendarOneHourMinusDate.setTime(date);
			calendarOneHourMinusDate.add(Calendar.HOUR, 0);
			SimpleDateFormat locale = new SimpleDateFormat(PhotoOmniConstants.LOCALE);
			SimpleDateFormat hourFormat = new SimpleDateFormat(PhotoOmniConstants.HOURFORMAT);
			map.put(PhotoOmniConstants.ONEHOUR,
					otherDateFormat.format(calendarOneHourMinusDate.getTime())+ " " +
							hourFormat.format(calendarOneHourMinusDate.getTime())+ ":59:59" + locale.format(calendarOneHourMinusDate.getTime()));
		} catch (ParseException e) {
			LOGGER.error(
					"Error occured while parsing date string at AbstractEmailReportDataProvider."
							+ "getDatesForHourlyReportsameDate() method", e);
		} catch (Exception e) {
			LOGGER.error("Error occured at AbstractEmailReportDataProvider."
					+ "getDatesForHourlyReportsameDate() method", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from AbstractEmailReportDataProvider.getDatesForHourlyReportsameDate() method");
			}
		}
		return map;

	}

}
