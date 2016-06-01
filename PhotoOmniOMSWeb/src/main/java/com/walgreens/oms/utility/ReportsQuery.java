package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.oms.bean.PMByEmployeeReqBean;

public class ReportsQuery {

	/**
	 * This method create the SQL select query for Machine type 
	 * @return query
	 */
	public static StringBuilder getVendorListQueryForRoyaltyReport() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_VENDOR_ID, DESCRIPTION FROM OM_VENDOR WHERE VENDOR_TYPE = 'ROYALTY' ");
		return query;
	}
	
	/**
	 * method to get query to insert report request in report table
	 * @return String -- > Insert query
	 */

	public static StringBuilder getCustomreportInsertQuery() {

		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_USER_REPORT_PREFS (SYS_USER_REPORT_PREF_ID, SYS_USER_ID, SYS_REPORT_ID, PREFERENCE_TYPE, FILTER_STATE, AUTO_REFRESH_CD,");
		query.append("AUTO_EXECUTE_CD, AUTO_REFRESH_INTERVAL, ACTIVE_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM, FILTER_ENABLED_CD)");
		query.append("VALUES (OM_USER_REPORT_PREFS_SEQ.NEXTVAL, ?, ?, ?, ? , ?, ?, ?, ? , ? , SYSDATE , ?, SYSDATE, ?)");
		return query;

	}

	public static StringBuilder getPMReportIdQuery() {

		StringBuilder query = new StringBuilder();
		query.append("select SYS_REPORT_ID from OM_REPORT where REPORT_NAME = ?");
		return query;

	}
	
	public static StringBuilder getUserIdQuery() {

		StringBuilder query = new StringBuilder();
		query.append("select SYS_USER_ID from OM_USER_ATTRIBUTES where EMPLOYEE_ID = ? ");
		return query;

	}
	
	public static StringBuilder getPluListQuery(String pluKey) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM ( SELECT a.*, rownum r__ FROM (SELECT PLU_NBR FROM OM_PROMOTION ");
		if(!pluKey.equals(" ")) query.append(" WHERE PLU_NBR LIKE '"+pluKey+"%' ");
		query.append(" GROUP BY PLU_NBR ORDER BY PLU_NBR ASC) a WHERE rownum < ((? * ?) + 1 ) ) WHERE r__ >= (((? -1) * ?) + 1) ");
		return query;
	}

	public static StringBuilder getPluListQueryAll() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT * FROM ( SELECT a.*, rownum r__ FROM ( ");
		query.append(" SELECT PLU_NBR FROM OM_PROMOTION GROUP BY PLU_NBR ORDER BY PLU_NBR ASC ");
		query.append(" ) a WHERE rownum < ((? * ?) + 1 ) ) WHERE r__ >= (((? -1) * ?) + 1) ");
		return query;
	}
	
	/**
	 * Method to get pmByEmployeeReportQuery details.
	 * @param empId contains employee ID.
	 * @return String SQL getting pmByEmployeeReportQuery details.
	 * @since v1.0
	 */
	public static StringBuilder pmByEmployeeReportQuery(PMByEmployeeReqBean objPMByEmployeeReqBean, String empId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * FROM ( SELECT a.*, ROWNUM as RNK FROM (  SELECT U.EMPLOYEE_ID as \"Employee Id\", (U.LAST_NAME || ', ' || U.FIRST_NAME) as \"Employee Name\", SUM(P.EARNED_AMOUNT) as \"Earned Amount\", SUM(P.EARNED_QTY)	 as \"Earned Qty\" , COUNT(1) OVER() TOTALROWS ");
		sb.append(" FROM OM_ORDER O,");
		sb.append(" OM_ORDER_PM P,");
		sb.append(" OM_LOCATION L,");
		sb.append(" OM_USER_ATTRIBUTES U");
		sb.append(" WHERE O.ORDER_PLACED_DTTM = P.ORDER_PLACED_DTTM ");
		sb.append(" AND O.SYS_ORDER_ID        = P.SYS_ORDER_ID ");
		sb.append(" AND O.SYS_OWNING_LOC_ID   = L.SYS_LOCATION_ID ");
		sb.append(" AND P.SYS_EMPLOYEE_ID     = U.SYS_USER_ID ");
		sb.append(" AND L.LOCATION_NBR        = ? ");
		sb.append(" AND O.ORDER_SOLD_DTTM BETWEEN To_Date( ? , 'dd-mm-yyyy HH24:MI:SS') AND To_Date( ? , 'dd-mm-yyyy HH24:MI:SS')");
		sb.append(" AND P.ACTIVE_CD           = 1");
		if(!empId.equals("")) sb.append(" AND U.EMPLOYEE_ID = '"+empId+"'"  );
		sb.append(" AND P.ORDER_PLACED_DTTM > To_Date( ? , 'dd-mm-yyyy HH24:MI:SS') - 195 ");
		sb.append(" GROUP BY U.EMPLOYEE_ID, U.FIRST_NAME, U.LAST_NAME");
		sb.append(" ORDER BY \"").append(objPMByEmployeeReqBean.getFilter().getSortColumnName()).append("\"  ").append(objPMByEmployeeReqBean.getFilter().getSortOrder());
		sb.append(" ) a WHERE ROWNUM < ((? * ?) + 1 ) ) WHERE RNK >= (((? -1) * ?) + 1) ");
		return sb;
	}
	
	/**
	 * Method to get pmByEmployeeReportQueryForPrint details.
	 * @param empId contains employee ID.
	 * @return String SQL getting pmByEmployeeReportQuery details.
	 * @since v1.0
	 */	
	public static StringBuilder pmByEmployeeReportQueryForPrint(PMByEmployeeReqBean objPMByEmployeeReqBean, String empId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT U.EMPLOYEE_ID as \"Employee Id\", (U.LAST_NAME || ', ' || U.FIRST_NAME) as \"Employee Name\", SUM(P.EARNED_AMOUNT) as \"Earned Amount\", SUM(P.EARNED_QTY) as \"Earned Qty\" , COUNT(1) OVER() TOTALROWS ");
		sb.append(" FROM OM_ORDER O,");
		sb.append(" OM_ORDER_PM P,");
		sb.append(" OM_LOCATION L,");
		sb.append(" OM_USER_ATTRIBUTES U");
		sb.append(" WHERE O.ORDER_PLACED_DTTM = P.ORDER_PLACED_DTTM ");
		sb.append(" AND O.SYS_ORDER_ID        = P.SYS_ORDER_ID ");
		sb.append(" AND O.SYS_OWNING_LOC_ID   = L.SYS_LOCATION_ID ");
		sb.append(" AND P.SYS_EMPLOYEE_ID     = U.SYS_USER_ID ");
		sb.append(" AND L.LOCATION_NBR        = ? ");
		sb.append(" AND O.ORDER_SOLD_DTTM BETWEEN To_Date( ? , 'dd-mm-yyyy HH24:MI:SS') AND To_Date( ? , 'dd-mm-yyyy HH24:MI:SS')");
		sb.append(" AND P.ACTIVE_CD           = 1");
		if(!empId.equals("")) sb.append(" AND U.EMPLOYEE_ID = '"+empId+"'"  );
		sb.append(" AND P.ORDER_PLACED_DTTM > To_Date( ? , 'dd-mm-yyyy HH24:MI:SS') - 195 ");
		sb.append(" GROUP BY U.EMPLOYEE_ID, U.FIRST_NAME, U.LAST_NAME");
		sb.append(" ORDER BY \"").append(objPMByEmployeeReqBean.getFilter().getSortColumnName()).append("\"  ").append(objPMByEmployeeReqBean.getFilter().getSortOrder());
		return sb;
	}
	
	/**
	 * Method to get Employee LAst NAme and First Name.
	 * @param empId contains employee ID.
	 * @return String SQL getting Employee LAst NAme and First Name..
	 * @since v1.0
	 */
	public static StringBuilder getEmployeeNameQuery(String empId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT (LAST_NAME  || ', ' || FIRST_NAME) AS \"EMPLOYEE NAME\"");
		sb.append(" FROM OM_USER_ATTRIBUTES ");
		sb.append(" WHERE EMPLOYEE_ID = '"+empId+"'"  );
		return sb;
	}
	
	/**
	 * Method to get Employee LAst NAme and First Name.
	 * @param empId contains employee ID.
	 * @return String SQL getting Employee LAst NAme and First Name..
	 * @since v1.0
	 */
	public static StringBuilder getLocationAddresQuery(String locId) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT (LOCATION_NBR || ', ' || ADDRESS_LINE_1) ");
		sb.append("AS \"LOCATION ADDRESS\"");
		sb.append(" FROM OM_LOCATION ");
		sb.append(" WHERE LOCATION_NBR = '"+locId+"'"  );
		return sb;
	}
	
	/**
	 * This Method used to get Store address.
	 * @since v1.0
	 */
	public static StringBuilder getLocationAddresQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT (LOCATION_NBR ||',' ||NVL(ADDRESS_LINE_1,' ')) AS LOCATIONADDRESS FROM  ");
		sb.append(PhotoOmniDBConstants.OM_LOCATION);
		sb.append(" WHERE LOCATION_NBR = ? "  );
		return sb;
	}
	
	/**
	 * Method to get query to get page rows per page.
	 * @return String SQL getting Query to get page rows per page..
	 * @since v1.0
	 */
    public static StringBuilder getPageCount() {
		
    	StringBuilder sb = new StringBuilder();
		sb.append(" SELECT CONFIG.PAGE_SIZE  FROM OM_REPORT_CONFIG CONFIG WHERE CONFIG.SYS_REPORT_ID = ");
		sb.append(" (SELECT REPORT.SYS_REPORT_ID FROM OM_REPORT REPORT WHERE REPORT.REPORT_NAME = ? ) ");
		return sb;
	}
	
	public static StringBuilder getPluListCountQuery(String pluKey) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM OM_PROMOTION WHERE PLU_NBR LIKE '"+pluKey+"%' ");
		return query;
	}

	public static StringBuilder getPluTotalCount() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT Count(*) FROM OM_PROMOTION ");
		return query;
	}
	
	 /**
	* Method to get query to get ProductFilter
	* @return String SQL getting Query to get get ProductFilter
	* @since v1.0
	*/
	public static StringBuilder getProductFilterQuery() {
	StringBuilder query = new StringBuilder();
	query.append("SELECT CODE_ID AS CODE,DECODE AS DESCRIPTION FROM OM_CODE_DECODE WHERE CODE_TYPE= ? ORDER BY DECODE ASC");
	return query;
	}
}
