package com.walgreens.admin.dao;

import java.util.List;
import java.util.Map;

import com.walgreens.admin.bean.KioskFilter;
import com.walgreens.common.exception.PhotoOmniException;

public interface KioskReportsDAO {

	/**
	 * Method will return list of record for KIOSK report UI screen for given
	 * filters
	 * 
	 * @param filters
	 * @param page
	 * @param rows
	 * @return list of record based on filter and contain server side filtered
	 *         data based on page and rows
	 * @throws PhotoOmniException
	 * 
	 */
	List<Map<String, Object>> getKioskReportData(List<KioskFilter> filters,
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
	 * Method will return list of record which will be used for input to the
	 * excel file
	 * 
	 * @param filters
	 *            is list of criteria selected in UI for which data need to
	 *            fetched
	 * 
	 * @return list of data for provided filters
	 * 
	 */
	List<Map<String, Object>> getExportToExcelData(List<KioskFilter> filters)
			throws Exception;

	/**
	 * Method will look for the page count which in configured in database for
	 * given report id
	 * 
	 * @param reportId
	 *            is unique name in database table for which page count need to
	 *            be fetched
	 * 
	 * @return page count which will used in pagination
	 * @throws PhotoOmniException
	 * 
	 */
	int getReportRecordPageCount(String reportId) throws PhotoOmniException;

	/**
	 * Method return a readable string for the given criteria
	 * 
	 * @param criteria
	 *            is code_id column in database table
	 * 
	 * @return decode value from database for given code_id
	 * @throws PhotoOmniException
	 * 
	 */
	String getDecodedValue(String criteria) throws PhotoOmniException;

	/**
	 * Method will return the count for total record based on given
	 * filters.Count will used in pagination logic in KIOSK report
	 * 
	 * @param filters
	 * @return int count is total no of record for given filters
	 * @throws PhotoOmniException
	 * 
	 */
	int getDataCount(List<KioskFilter> filters) throws PhotoOmniException;

	/**
	 * Method will prepare the condition for sql query which will be used in
	 * WHERE clause in query
	 * 
	 * @param filters
	 * @return string which contain the conditional string used in where clause
	 *         in sql query
	 * @throws PhotoOmniException
	 * 
	 */
	String getKioskReportConditons(List<KioskFilter> filters)
			throws PhotoOmniException;

	/**
	 * 
	 * @param filters
	 * @param page
	 * @param rows
	 * @param b
	 * @return
	 */
	String getFilterParams(List<KioskFilter> filters, int page, int rows,
			boolean b);
}
