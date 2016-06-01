package com.walgreens.admin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;

import com.walgreens.admin.bean.KioskReportRequestBean;
import com.walgreens.admin.bean.KioskReportResponseBean;

public interface KioskReportsRestService {

	/**
	 * Method will return list of record for KIOSK report UI screen for given
	 * filters
	 * 
	 * @param KioskReportRequestBean
	 *            params which will contains currentPage,filters and
	 *            messageHeader
	 * @return list of record based on filter and contain server side filtered
	 *         data based on page and rows
	 * 
	 */
	public KioskReportResponseBean getKioskReportData(
			@RequestBody KioskReportRequestBean params);

	/**
	 * This method is responsible to provide filters for UI in KIOSK report.
	 * 
	 * @return list of data for drop down in UI
	 * 
	 */
	public Map<String, Object> getKioskFilterOptions(HttpServletRequest request);

	/**
	 * This method is responsible for generate the detailed KOISK report with
	 * all the column present in DB table it will generate a excel file and user
	 * can download the file
	 * 
	 * @param param
	 * @return file which contains data for given filters
	 * 
	 */
	public void exportToExcel(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * Method will return data for print screen in the KIOSK report
	 * 
	 * @param param
	 * @return list of data to show in print screen of KIOSK report
	 * 
	 */
	Object printKioskRpt(HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}