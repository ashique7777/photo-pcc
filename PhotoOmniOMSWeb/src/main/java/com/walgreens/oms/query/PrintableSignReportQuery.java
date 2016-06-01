/**
 * LicenseContentReportQuery.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		20 Feb 2015
 *  
 **/
package com.walgreens.oms.query;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class is used to create Query for Printable Sign Reports.
 * 
 * @author CTS
 * @version 1.1 Mar 18, 2015
 */
public class PrintableSignReportQuery {
	
	/**
	 * This method create the SQL query to get SYS_REPORT_ID for Print Sign report.
	 * @return StringBuilder.
	 */
	public static StringBuilder getReportIdForPrintSignReport() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT SYS_REPORT_ID AS SYS_REPORT_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_REPORT);
		query.append(" WHERE TRIM(UPPER(REPORT_NAME)) = TRIM(UPPER(?)) ");
		return query;
	}

     
	/**
	 * This method create the SQL Insert query for OM_USER_REPORT_PREFS
	 * @return query
	 */
	public static StringBuilder getPrintableSignReportInsertQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ").append(PhotoOmniDBConstants.OM_USER_REPORT_PREFS);
		query.append(" (SYS_USER_REPORT_PREF_ID, SYS_USER_ID, SYS_REPORT_ID, PREFERENCE_TYPE, FILTER_STATE, ");
		query.append(" ACTIVE_CD, AUTO_REFRESH_CD, AUTO_EXECUTE_CD, AUTO_REFRESH_INTERVAL, FILTER_ENABLED_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID, ");
		query.append(" UPDATE_DTTM) VALUES(OM_USER_REPORT_PREFS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE) ");
		
		return query;
	}
	
	/**
	 * This method find the SYS_USER_ID from OAM_ID.
	 * @return query
	 */
	public static StringBuilder getSysUserIdByOamId() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT SYS_USER_ID FROM ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES);
		query.append(" WHERE EMPLOYEE_ID = ? ");
		return query;
	}
	

	/**
	 * This method create the SQL select query for Event type.
	 * @param reqEventName contains search request value.
	 * @return query
	 */
	public static StringBuilder getActiveEventType(String reqEventName) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_EVENT_ID AS SYS_EVENT_ID, EVENT_NAME AS EVENT_NAME FROM ");
		query.append(PhotoOmniDBConstants.OM_SIGNS_HEADER);
		query.append(" WHERE ACTIVE_CD = 1 AND UPPER(EVENT_NAME) LIKE ");
		query.append(" '%" +reqEventName+ "%' ");
		return query;
	}
	
	/**
	 * This method create the SQL to get last print sign data row 
	 * from OM_USER_REPORT_PREFS table.
	 * @return query
	 */
	public static StringBuilder getLastEnteredDataForPrintSign() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT MAX(SYS_USER_REPORT_PREF_ID) AS LATEST_ROW FROM ");
		query.append(PhotoOmniDBConstants.OM_USER_REPORT_PREFS);
		query.append(" WHERE SYS_REPORT_ID = ? ");
		return query;
	}

}
