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
*  <1.1>     		05 March 2015
*  
**/
package com.walgreens.batch.query;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class is used to create Query for License Content Reports.
 * @author CTS
 * @version 1.1 March 05, 2015
 */
public class LicenseContentReportQuery {
	
	/**
	 * This method is used to Create SQL Query for Previous date and current date.
	 * @return String.
	 */
	public static StringBuilder getCurrentAndPreviousDate() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT TO_CHAR(SYSDATE-1, 'dd-mm-yyyy') AS DATERANGE FROM DUAL ");
		return query;
	}
	
	/**
	 * This method is used to Create SQL Query for License content Exception report.
	 * @return String.
	 */
	public static StringBuilder getExceptionReportQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM ( ");
		query.append(" SELECT OM_ORDER.ORDER_NBR AS ORDER_ID, OM_PRODUCT.PRODUCT_NBR  AS PRODUCT_ID, ('\"'|| NVL(OM_PRODUCT.DESCRIPTION, ' ') || '\"') AS PRODUCT_DESCRIPTION,");
		query.append(" ('\"'|| NVL(OM_EXCEPTION_TYPE.EXCEPTION_TYPE,' ') || '\"') AS EXCEPTION_TYPE, ('\"'|| NVL(OM_ORDER_EXCEPTION.NOTES,' ') || '\"') AS REMARKS, ");
		query.append(" ('\"'|| NVL(OM_EXCEPTION_TYPE.REASON, ' ') || '\"') AS EXCEPTION_DESCRIPTION,");
		query.append(" OM_ORDER_EXCEPTION.CREATE_DTTM AS EXCEPTION_DATE, OM_ORDER_EXCEPTION.ORDER_PLACED_DTTM AS DATE_TIME_CREATED, OM_LOCATION.LOCATION_NBR AS STORE, ");
		query.append(" ROWNUM AS RNK FROM ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT).append(" OM_OL_LICENSE_CONTENT ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE).append(" OM_ORDER_LINE ");
		query.append(" ON OM_OL_LICENSE_CONTENT.SYS_ORDER_LINE_ID = OM_ORDER_LINE.SYS_ORDER_LINE_ID ");
		query.append(" AND OM_OL_LICENSE_CONTENT.ORDER_PLACED_DTTM = OM_ORDER_LINE.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" OM_ORDER ");
		query.append(" ON OM_ORDER.SYS_ORDER_ID = OM_ORDER_LINE.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_LINE.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(" OM_ORDER_EXCEPTION ");
		query.append(" ON OM_ORDER_EXCEPTION.SYS_ORDER_ID = OM_ORDER.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER_EXCEPTION.ORDER_PLACED_DTTM = OM_ORDER.ORDER_PLACED_DTTM INNER JOIN ");
		query.append( PhotoOmniDBConstants.OM_EXCEPTION_TYPE ).append(" OM_EXCEPTION_TYPE ");
		query.append(" ON OM_ORDER_EXCEPTION.SYS_EXCEPTION_TYPE_ID = OM_EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT).append(" OM_PRODUCT ");
		query.append(" ON OM_PRODUCT.SYS_PRODUCT_ID = OM_ORDER_LINE.SYS_PRODUCT_ID  INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ");
		query.append(" ON OM_ORDER.SYS_OWNING_LOC_ID = OM_LOCATION.SYS_LOCATION_ID ");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1 AND OM_OL_LICENSE_CONTENT.ORDER_PLACED_DTTM ");
		query.append(" BETWEEN TO_DATE(? || ' 00:00:00','dd-mm-yyyy HH24:MI:SS') AND TO_DATE(? || ' 23:59:59','dd-mm-yyyy HH24:MI:SS') ");
		query.append(" ) WHERE RNK BETWEEN ? AND ? ");
		return query;
	}
	
	/**
	 * This method is use to Create SQL Query for License content Exception report.
	 * @return String.
	 */
	public static StringBuilder getAdhocAndDailyReportQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM ( ");
		query.append(" SELECT OM_OL_LICENSE_CONTENT.ORDER_PLACED_DTTM AS ORDERDATE, NVL(OM_OL_LICENSE_CONTENT.VENDOR_LC_QTY, 0) AS QUANTITY, 'LULU' AS PROVIDER, ");
		query.append(" NVL(OM_OL_LICENSE_CONTENT.FINAL_LC_PRICE, 0) AS CALCULATEDRETAILPRICE, NVL(OM_OL_LICENSE_CONTENT.LC_AMOUNT_PAID, 0) AS NETSALE, ");
		query.append(" NVL(OM_OL_LICENSE_CONTENT.ORIGINAL_LC_PRICE, 0) AS ORIGINALRETAILPRICE, (OM_OL_LICENSE_CONTENT.ORIGINAL_LC_PRICE - OM_OL_LICENSE_CONTENT.FINAL_LC_PRICE) ");
		query.append(" AS DISCOUNTAPPLIED, ('\"'|| NVL(OM_PRODUCT.DESCRIPTION, ' ') || '\"') AS PRODUCTDESCRIPTION, OM_PRODUCT.UPC AS UPC , OM_PRODUCT.WIC AS WIC, OM_ORDER_ATTRIBUTE.ENVELOPE_NUMBER AS ENVELOPENUMBER, ");
		query.append(" OM_OL_LICENSE_CONTENT.VENDOR_LC_ID AS LICECONTORTEMPID, OM_ORDER.STATUS AS ORDERSTATUS, (CASE WHEN NVL(OM_ORDER.DISCOUNT_CARD_USED_CD, 0) = 1 THEN 'Yes' ELSE 'No' END) AS EMPLOYEEDISCOUNT, ");
		query.append(" OM_LOCATION.LOCATION_TYPE AS LOCATIONTYPE, OM_LOCATION.LOCATION_NBR AS LOCATIONNUMBER, ROWNUM AS RNK FROM ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT ).append(" OM_OL_LICENSE_CONTENT");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE ).append(" OM_ORDER_LINE ");
		query.append(" ON OM_OL_LICENSE_CONTENT.SYS_ORDER_LINE_ID = OM_ORDER_LINE.SYS_ORDER_LINE_ID "); 
		query.append(" AND OM_OL_LICENSE_CONTENT.ORDER_PLACED_DTTM = OM_ORDER_LINE.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" OM_ORDER ");
		query.append(" ON OM_ORDER.SYS_ORDER_ID = OM_ORDER_LINE.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_LINE.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT).append(" OM_PRODUCT ");
		query.append(" ON OM_PRODUCT.SYS_PRODUCT_ID = OM_ORDER_LINE.SYS_PRODUCT_ID INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" OM_ORDER_ATTRIBUTE ");
		query.append(" ON OM_ORDER_ATTRIBUTE.SYS_ORDER_ID = OM_ORDER.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER_ATTRIBUTE.ORDER_PLACED_DTTM = OM_ORDER.ORDER_PLACED_DTTM INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ");
		query.append(" ON OM_ORDER.SYS_OWNING_LOC_ID = OM_LOCATION.SYS_LOCATION_ID ");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1 AND OM_OL_LICENSE_CONTENT.ORDER_PLACED_DTTM ");
		query.append(" BETWEEN TO_DATE(? || ' 00:00:00','dd-mm-yyyy HH24:MI:SS') AND TO_DATE(? || ' 23:59:59','dd-mm-yyyy HH24:MI:SS') ");
		query.append(" ) WHERE RNK BETWEEN ? AND ? ");

		return query;
	}
	
	/**
	 * This method create the SQL query to find out 'SYS_USER_REPORT_PREF_ID'. 
	 * @return String.
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
	
	
	/**
	 * This method generate SQL Query to get 'SYS_DEFAULT_REPORT_PREF_ID'.
	 * @return String.
	 */
	public static StringBuilder getActiveUserPrefIdForDaily() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" SELECT SYS_DEFAULT_REPORT_PREF_ID FROM ");
		strQuery.append(PhotoOmniDBConstants.OM_DEFAULT_REPORT_PREFS).append(" OM_DEFAULT_REPORT_PREFS ");
		strQuery.append(" INNER JOIN ");
		strQuery.append(PhotoOmniDBConstants.OM_REPORT).append(" OM_REPORT "); 
		strQuery.append(" ON OM_REPORT.SYS_REPORT_ID = OM_DEFAULT_REPORT_PREFS.SYS_REPORT_ID ");
		strQuery.append(" WHERE UPPER(TRIM(OM_REPORT.REPORT_NAME)) = ? AND OM_REPORT.ACTIVE_CD = 1  ");
		strQuery.append(" AND OM_DEFAULT_REPORT_PREFS.ACTIVE_CD = 1 ") ;
		
		return strQuery;
	}
	
	/**
	 * This method update the table 'OM_USER_REPORT_PREFS' with ACTIVE_IND.
	 * @return String.
	 */
	public static StringBuilder updateQueryUserPRef() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" UPDATE ");
		strQuery.append(PhotoOmniDBConstants.OM_USER_REPORT_PREFS);
		strQuery.append(" SET ACTIVE_CD = 0 WHERE SYS_USER_REPORT_PREF_ID = ? ");
		
		return strQuery;
	}
	
}
