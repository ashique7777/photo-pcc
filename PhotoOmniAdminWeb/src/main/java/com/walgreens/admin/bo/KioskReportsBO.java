package com.walgreens.admin.bo;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.walgreens.admin.bean.KioskDetailBean;
import com.walgreens.admin.bean.KioskFilter;
import com.walgreens.common.exception.PhotoOmniException;

public interface KioskReportsBO {

	/**
	 * Method will return list of record for KIOSK report UI screen for given
	 * filters
	 * 
	 * @param filters
	 * @param page
	 * @param rows
	 * @return list of record based on filter and contain server side filtered
	 *         data based on page and rows
	 * 
	 */
	List<KioskDetailBean> getKioskReportData(List<KioskFilter> filters,
			int page, int rows) throws PhotoOmniException;

	/**
	 * Method will return list of record which will be shown in the criteria
	 * drop down in KIOSK report
	 * 
	 * @return list of data for drop down in UI
	 * 
	 */
	List<Map<String, Object>> getKioskFilterOptions() throws PhotoOmniException;

	/**
	 * This method will generate a excel sheet for KOISK report with detailed
	 * data with all column in DB table based on filters
	 * 
	 * @param filters
	 *            is list of criteria selected in UI for which data need to
	 *            fetched
	 * 
	 * @return file which contains data for given filters
	 * 
	 */
	void exportToExcel(HttpServletResponse response, List<KioskFilter> filters)
			throws PhotoOmniException;

	/**
	 * Method return a readable string for the given criteria
	 * 
	 * @param filters
	 *            is list of criteria selected in UI for which data need to
	 *            fetched
	 * 
	 * @return String which is used to show in UI screen and in an excel sheet
	 *         as header
	 * @throws PhotoOmniException
	 * 
	 */
	String getFilterString(List<KioskFilter> filters) throws PhotoOmniException;

	/**
	 * Method will return the count for total record based on given
	 * filters.Count will used in pagination logic in KIOSK report
	 * 
	 * @param filters
	 * @return int count is total no of record for given filters
	 * @throws PhotoOmniException
	 * 
	 */
	int getReportDataCount(List<KioskFilter> filters) throws PhotoOmniException;

	/**
	 * Method will look for the page count which in configured in database for
	 * given report id
	 * 
	 * @param reportId
	 *            is unique name in database table for which page count need to
	 *            fetched
	 * 
	 * @return perPageDataCount count which will used in pagination
	 * @throws PhotoOmniException
	 * 
	 */
	int getKioskPageCount(String reportId) throws PhotoOmniException;

}