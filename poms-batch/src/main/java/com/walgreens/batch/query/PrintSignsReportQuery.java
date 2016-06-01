/**
 * PrintSignsReportQuery.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 4 mar 2015
 *  
 **/
package com.walgreens.batch.query;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class is a query creation class for Print Sign report.
 * @author CTS
 * @version 1.1 March 04, 2015
 */
public class PrintSignsReportQuery {
	
	/**
	 * This method create the sql Query for Print Sign report.
	 * @return StringBuilder
	 */
	public static StringBuilder getPrintSignsQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM (SELECT T.* , ROWNUM AS RNK FROM ( ");
		query.append(" SELECT OM_LOCATION.LOCATION_NBR AS STORE, ('\"'|| OM_SIGNS_HEADER.EVENT_NAME || '\"') AS EVENT_NAME, ");
		query.append(" SUM(OM_ORDER_LINE_SIGNS_DTL.SIGNS_IMAGE_QTY) AS QUANTITY FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_SIGNS_DTL).append(" OM_ORDER_LINE_SIGNS_DTL "); 
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_SIGNS_IMAGE_MAPPING).append(" OM_SIGNS_IMAGE_MAPPING "); 
		query.append(" ON OM_SIGNS_IMAGE_MAPPING.SYS_IMAGE_ID = OM_ORDER_LINE_SIGNS_DTL.SYS_IMAGE_ID INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_SIGNS_HEADER).append(" OM_SIGNS_HEADER ");  
		query.append(" ON OM_SIGNS_HEADER.SYS_EVENT_ID = OM_SIGNS_IMAGE_MAPPING.SYS_EVENT_ID INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE).append(" OM_ORDER_LINE "); 
		query.append(" ON OM_ORDER_LINE.SYS_ORDER_LINE_ID = OM_ORDER_LINE_SIGNS_DTL.SYS_ORDER_LINE_ID ");
		query.append(" AND OM_ORDER_LINE.ORDER_PLACED_DTTM = OM_ORDER_LINE_SIGNS_DTL.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" OM_ORDER ");
		query.append(" ON OM_ORDER_LINE.SYS_ORDER_ID = OM_ORDER.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER_LINE.ORDER_PLACED_DTTM = OM_ORDER.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ");
		query.append(" ON OM_ORDER.SYS_OWNING_LOC_ID = OM_LOCATION.SYS_LOCATION_ID  ");
		query.append(" WHERE OM_SIGNS_HEADER.ACTIVE_CD = 1 AND OM_SIGNS_IMAGE_MAPPING.ACTIVE_CD = 1 ");
		query.append(" AND OM_LOCATION.ACTIVE_CD = 1 AND OM_SIGNS_HEADER.SYS_EVENT_ID = ? ");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM BETWEEN TO_DATE(? || ' 00:00:00','dd-mm-yyyy HH24:MI:SS') AND TO_DATE(? || ' 23:59:59','dd-mm-yyyy HH24:MI:SS') ");
		query.append(" GROUP BY LOCATION_NBR, EVENT_NAME ) T ");
		query.append(" ) WHERE RNK BETWEEN ? AND ? ");
		
		return query;
	}
	
	
	/**
	 * This method update the table 'OM_USER_REPORT_PREFS' with ACTIVE_CD.
	 * @return StringBuilder.
	 */
	public static StringBuilder updateQueryUserPRef() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE ");
		strQuery.append(PhotoOmniDBConstants.OM_USER_REPORT_PREFS);
		strQuery.append(" SET ACTIVE_CD = 0 WHERE SYS_USER_REPORT_PREF_ID = ?");
		return strQuery;
	}
	
	/**
	 * This method create the SQL query to find out 'SYS_USER_REPORT_PREF_ID'. 
	 * @return StringBuilder.
	 */
	public static StringBuilder getActiveUserPrefId() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" SELECT SYS_USER_REPORT_PREF_ID FROM ");
		strQuery.append(PhotoOmniDBConstants.OM_USER_REPORT_PREFS).append(" OM_USER_REPORT_PREFS "); 
		strQuery.append(" INNER JOIN ");
		strQuery.append(PhotoOmniDBConstants.OM_REPORT).append(" OM_REPORT "); 
		strQuery.append(" ON OM_REPORT.SYS_REPORT_ID = OM_USER_REPORT_PREFS.SYS_REPORT_ID ");
		strQuery.append(" WHERE UPPER(TRIM(OM_REPORT.REPORT_NAME)) = ? AND OM_REPORT.ACTIVE_CD = 1 ");
		strQuery.append(" AND OM_USER_REPORT_PREFS.ACTIVE_CD = 1 ");
		
		return strQuery;
	}

}
