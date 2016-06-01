package com.walgreens.batch.central.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.walgreens.common.constant.PhotoOmniConstants;

public class ReportsQuery {

	/**
	 * Method to get select Query from user PRef table.
	 * @return StringBuffer SQL Select Query.
	 * @since v1.0
	 */
	public static StringBuffer getUserPrefQuery(long lSysUserReportID){
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("SELECT SYS_USER_ID as USER_ID, SYS_REPORT_ID as REPORT_ID,  FILTER_STATE FROM OM_USER_REPORT_PREFS WHERE " );
				strQuery.append( "SYS_USER_REPORT_PREF_ID = "+lSysUserReportID+"") ;
		return strQuery;
	}

	/**
	 * Method to get update Query to user PRef table.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuffer updateQueryUserPRef() {
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("UPDATE OM_USER_REPORT_PREFS SET ACTIVE_CD = 0,");
		strQuery.append(" UPDATE_DTTM = SYSDATE");
		strQuery.append(" WHERE SYS_USER_REPORT_PREF_ID = ?");
		return strQuery;
	}

	/**
	 * Method to get StartDate and End date for Monthly report from Dual.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuffer getStartDateAndEndDate() {
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("SELECT TO_CHAR(ADD_MONTHS(SYSDATE,-1),'dd-mm-yyyy') AS STARTDATE, TO_CHAR(SYSDATE-1, 'dd-mm-yyyy') AS ENDDATE FROM DUAL");
		return strQuery;
	}
	
	/**
	 * Method to get CSV Data Query to user PRef table.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuffer getPMBYWICCSVDataQuery(){
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("SELECT * FROM (");
		strQuery.append(" SELECT WIC, UPC, ").append("\"Product Description\"").append(",");
		strQuery.append("\"# of orders\"").append(",");
		strQuery.append("\"Calculated Retail\"").append(",");
		strQuery.append("\"Amount of PM paid\"").append(",");
		strQuery.append("\"Sales (Amount Paid)\"").append(",");
		strQuery.append("\"Item Cost\"").append(",");
		strQuery.append("\"Total Quantity\"").append(",");
		strQuery.append("\"Gross profit\"").append(",");
		strQuery.append("ROWNUM as RNK FROM (");
		strQuery.append(" SELECT D.WIC WIC, D.UPC UPC, D.DESCRIPTION ").append("\"Product Description\"").append(",");
		strQuery.append(" Count(1) ").append("\"# of orders\"").append(",");
		strQuery.append(" Sum(C.Final_Price) ").append("\"Calculated Retail\"").append(",");
		strQuery.append(" Sum(A.Earned_Amount) ").append("\"Amount of PM paid\"").append(",");
		strQuery.append(" Sum(C.Line_Sold_Amount) ").append("\"Sales (Amount Paid)\"").append(",");
		strQuery.append(" Sum(C.Cost) ").append("\"Item Cost\"").append(",");
		strQuery.append(" Sum(C.Quantity)  ").append("\"Total Quantity\"").append(",");
		strQuery.append(" Sum(C.Line_Sold_Amount) - Sum(C.Cost) ").append("\"Gross profit\"");
		strQuery.append(" FROM OM_ORDER B,  OM_ORDER_PM A,  OM_ORDER_LINE C,  OM_PRODUCT D");
		strQuery.append(" WHERE B.SYS_ORDER_ID    = A.SYS_ORDER_ID");
		strQuery.append(" AND B.ORDER_PLACED_DTTM = A.ORDER_PLACED_DTTM");
		strQuery.append(" AND A.SYS_ORDER_ID      = C.SYS_ORDER_ID");
		strQuery.append(" AND A.ORDER_PLACED_DTTM = C.ORDER_PLACED_DTTM");
		strQuery.append(" AND A.SYS_PRODUCT_ID = C.SYS_PRODUCT_ID");
		strQuery.append(" AND C.SYS_PRODUCT_ID    = D.SYS_PRODUCT_ID");
		strQuery.append(" AND A.SYS_PRODUCT_ID  <> 0");
		strQuery.append(" AND B.ORDER_SOLD_DTTM BETWEEN To_Date( ? , 'dd-mm-yyyy HH24:MI:SS') AND To_DATE( ?, 'dd-mm-yyyy HH24:MI:SS')");
		strQuery.append(" AND A.Earned_Amount > 0");
		strQuery.append(" AND A.ORDER_PLACED_DTTM > To_DATE( ? , 'dd-mm-yyyy HH24:MI:SS') - 195");
		strQuery.append(" Group By D.Wic,  D.UPC,  D.DESCRIPTION");
		strQuery.append(" UNION ALL");
		strQuery.append(" SELECT 'NA' Wic, 'NA' UPC, D.PM_RULE_DESC ").append("\"Product Description\"").append(",");
		strQuery.append(" Count(1) ").append("\"# of orders\"").append(",");
		strQuery.append(" SUM(B.Final_Price) ").append("\"Calculated Retail\"").append(",");
		strQuery.append(" Sum(A.Earned_Amount) ").append("\"Amount of PM paid\"").append(",");
		strQuery.append(" SUM(B.Sold_Amount) ").append("\"Sales (Amount Paid)\"").append(",");
		strQuery.append(" Sum(C.Cost) ").append("\"Item Cost\"").append(",");
		strQuery.append(" 0  ").append("\"Total Quantity\"").append(",");
		strQuery.append(" SUM(B.Sold_Amount) - SUM(C.Cost) ").append("\"Gross profit\"");
		strQuery.append(" FROM OM_ORDER B,  OM_ORDER_ATTRIBUTE C,  OM_ORDER_PM A,  OM_PROMOTIONAL_MONEY D");
		strQuery.append(" WHERE B.SYS_ORDER_ID    = C.SYS_ORDER_ID");
		strQuery.append(" AND B.ORDER_PLACED_DTTM = C.ORDER_PLACED_DTTM");
		strQuery.append(" AND C.SYS_ORDER_ID      = A.SYS_ORDER_ID");
		strQuery.append(" AND C.ORDER_PLACED_DTTM = A.ORDER_PLACED_DTTM");
		strQuery.append(" AND A.SYS_PM_ID         = D.SYS_PM_ID");
		strQuery.append(" AND A.SYS_PRODUCT_ID    = 0");
		strQuery.append(" AND B.ORDER_SOLD_DTTM BETWEEN To_DATE( ? , 'dd-mm-yyyy HH24:MI:SS') AND To_DATE( ? , 'dd-mm-yyyy HH24:MI:SS')");
		strQuery.append(" AND A.Earned_Amount > 0");
		strQuery.append(" AND A.ORDER_PLACED_DTTM > To_DATE( ? , 'dd-mm-yyyy HH24:MI:SS') - 195");
		strQuery.append(" GROUP BY D.PM_RULE_DESC");
		strQuery.append(" )) WHERE  RNK BETWEEN ? AND ? ");
		return strQuery;
	}
	
	/**
	 * Method to get CSV Data Query to user PRef table.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuffer getsalesDataQuery(List<String> productTypeList, List<String> printSizeList){
		StringBuffer strQuery = new StringBuffer();
		String commaDelimiter = "";
		Iterator<String> productIterator = productTypeList.iterator();
		Iterator<String> printSizeIterator = printSizeList.iterator();
		strQuery.append(" SELECT * FROM ( ");
		strQuery.append(" SELECT TEMPLATE_ID, CATEGORY, DESCRIPTION, OUTPUT_SIZE, VENDOR, \"COUNT\", QUANTITY, AMOUNT_PAID, ");
		strQuery.append(" CALCULATED_RETAIL, ORIGINAL_RETAIL, ORDER_COST, TOTAL_DISCOUNT_AMT, ROWNUM AS RNK FROM ( ");
		strQuery.append(" SELECT OM_TEMPLATE.TEMPLATE_NBR AS TEMPLATE_ID, OM_TEMPLATE_CATEGORY_LVL1.DESCRIPTION AS CATEGORY, ");
		strQuery.append(" OM_TEMPLATE_CATEGORY_LVL2.DESCRIPTION AS DESCRIPTION, OM_TEMPLATE.OUTPUT_SIZE AS OUTPUT_SIZE, ");
		strQuery.append(" OM_VENDOR.DESCRIPTION AS VENDOR, COUNT(DISTINCT OM_ORDER.ORDER_NBR) AS \"COUNT\", ");
		strQuery.append(" sum(OM_ORDER_LINE_TEMPLATE.TEMPLATE_QTY) AS QUANTITY, sum(OM_ORDER_LINE_TEMPLATE.TEMPLATE_SOLD_AMT) AS AMOUNT_PAID, ");
		strQuery.append(" sum(OM_ORDER_LINE.FINAL_PRICE) AS CALCULATED_RETAIL, SUM(OM_ORDER_LINE.ORIGINAL_LINE_PRICE) AS ORIGINAL_RETAIL, ");
		strQuery.append(" sum(OM_ORDER_LINE.COST) AS ORDER_COST, sum(OM_ORDER_LINE.DISCOUNT_AMT) AS TOTAL_DISCOUNT_AMT FROM ");
		strQuery.append(" OM_ORDER, OM_ORDER_LINE, OM_ORDER_LINE_TEMPLATE, OM_TEMPLATE, ");
		strQuery.append(" OM_TEMPLATE_TEMPL_CAT_ASSO , OM_TEMPLATE_CATEGORY_LVL2 , OM_TEMPLATE_CATEGORY_LVL1 , OM_TEMPLATE_CATEGORY_TYPE, OM_VENDOR ");
		strQuery.append(" WHERE OM_ORDER.SYS_ORDER_ID =OM_ORDER_LINE.SYS_ORDER_ID ");
		strQuery.append(" AND OM_ORDER.ORDER_PLACED_DTTM  = OM_ORDER_LINE.ORDER_PLACED_DTTM ");
		strQuery.append(" AND OM_ORDER_LINE.SYS_ORDER_LINE_ID = OM_ORDER_LINE_TEMPLATE.SYS_ORDER_LINE_ID ");
		strQuery.append(" AND OM_ORDER_LINE.ORDER_PLACED_DTTM = OM_ORDER_LINE_TEMPLATE.ORDER_PLACED_DTTM ");
		strQuery.append(" AND OM_ORDER_LINE_TEMPLATE.SYS_TEMPLATE_ID = OM_TEMPLATE.SYS_TEMPLATE_ID ");
		strQuery.append(" AND OM_TEMPLATE.SYS_TEMPLATE_ID = OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_ID ");
		strQuery.append(" AND  OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_CATEGORY_LVL1_ID =OM_TEMPLATE_CATEGORY_LVL1.SYS_TEMPLATE_CATEGORY_LVL1_ID ");
		strQuery.append(" AND OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_CATEGORY_LVL2_ID = OM_TEMPLATE_CATEGORY_LVL2.SYS_TEMPLATE_CATEGORY_LVL2_ID  ");
		strQuery.append(" AND  OM_TEMPLATE_CATEGORY_LVL1.SYS_TEMPLATE_CATEGORY_LVL1_ID     =OM_TEMPLATE_CATEGORY_LVL2.TEMPLATE_CATEGORY_LVL1_ID ");
		strQuery.append(" AND OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_CATEGORY_TYPE_ID = OM_TEMPLATE_CATEGORY_TYPE.SYS_TEMPLATE_CATEGORY_TYPE_ID ");
		strQuery.append(" AND OM_TEMPLATE.SYS_ROYALTY_VENDOR_ID  =OM_VENDOR.SYS_VENDOR_ID ");
		strQuery.append(" AND OM_TEMPLATE_CATEGORY_LVL1.CATEGORY_NAME IN  ( ");
		while(productIterator.hasNext()){
			productIterator.next();
			strQuery.append(commaDelimiter+ " ? ");
			commaDelimiter = PhotoOmniConstants.COMMA_DELIMITER;
		}
		commaDelimiter = "";
		strQuery.append("  ) AND  OM_TEMPLATE.OUTPUT_SIZE IN ( "); 
		while(printSizeIterator.hasNext()){
			printSizeIterator.next();
			strQuery.append(commaDelimiter+ " ? ");
			commaDelimiter = PhotoOmniConstants.COMMA_DELIMITER;
		}
		strQuery.append(" ) AND OM_TEMPLATE_CATEGORY_TYPE.TEMPLATE_CATEGORY_TYPE = ?  ");
		strQuery.append(" AND OM_ORDER.ORDER_SOLD_DTTM BETWEEN TO_DATE(? || ' 00:00:00', 'dd-mm-yyyy HH24:MI:SS')  AND TO_DATE(? || ' 23:59:59', 'dd-mm-yyyy HH24:MI:SS') ");
		strQuery.append("  AND OM_ORDER.ORDER_PLACED_DTTM > TO_DATE( ? , 'dd-mm-yyyy') - 195 ");
		strQuery.append(" GROUP BY OM_TEMPLATE.TEMPLATE_NBR, OM_TEMPLATE_CATEGORY_LVL1.DESCRIPTION, ");
		strQuery.append(" OM_TEMPLATE_CATEGORY_LVL2.DESCRIPTION, OM_TEMPLATE.OUTPUT_SIZE , OM_VENDOR.DESCRIPTION ");
		strQuery.append(" order by TEMPLATE_ID ");
		strQuery.append(" )) WHERE  RNK BETWEEN ? AND ? ");
		return strQuery;
	}

	/**
	 * Method to get select Query from Default Report PRef table for monthly report.
	 * @return StringBuffer SQL Select Query.
	 * @since v1.0
	 */
	public static StringBuffer getDefaultUserPrefQuery(long lSysUserReportID){
		StringBuffer strQuery = new StringBuffer();
		strQuery.append("SELECT 1 AS USER_ID, SYS_REPORT_ID AS REPORT_ID, FILTER_STATE AS FILTER_STATE FROM OM_DEFAULT_REPORT_PREFS WHERE " );
		strQuery.append( "SYS_DEFAULT_REPORT_PREF_ID = "+lSysUserReportID+"") ;
		return strQuery;
	}
	
	/**
	 * Method to get Select Query For Royalty Report
	 */
	
	public static StringBuffer getRoyaltyReportQuery(){
		StringBuffer query = new StringBuffer();
		query.append(" SELECT * FROM ( ");
		query.append(" SELECT STORENUMBER, TEMPLATEID, OUTPUT_SIZE, VENDOR, PRODUCTNAME, NOOFPRINTS, NOOFORDERS, SOLDAMOUNT,  " );
		query.append(" TEMPLATENAME, PRODUCT,  ROWNUM as RNK FROM ( ");
		query.append(" SELECT OM_LOCATION.LOCATION_NBR AS STORENUMBER ,OM_TEMPLATE.TEMPLATE_NBR AS TEMPLATEID, OM_TEMPLATE.OUTPUT_SIZE,");
		query.append(" OM_VENDOR.DESCRIPTION AS VENDOR, (OM_TEMPLATE.OUTPUT_SIZE ||OM_TEMPLATE_CATEGORY_LVL1.DESCRIPTION) AS PRODUCTNAME, ");
		query.append(" SUM(OM_ORDER_LINE_TEMPLATE.TEMPLATE_QTY) as NOOFPRINTS, COUNT(DISTINCT OM_ORDER.ORDER_NBR) AS NOOFORDERS,");
		query.append(" SUM(OM_ORDER_LINE_TEMPLATE.TEMPLATE_SOLD_AMT) AS SOLDAMOUNT, " );
		query.append(" OM_TEMPLATE.DESCRIPTION AS TEMPLATENAME , OM_TEMPLATE_CATEGORY_LVL1.DESCRIPTION AS PRODUCT FROM  ");
		query.append(" OM_ORDER,  OM_ORDER_LINE,  OM_ORDER_LINE_TEMPLATE,  OM_LOCATION, OM_TEMPLATE_TEMPL_CAT_ASSO, OM_TEMPLATE, ");
		query.append(" OM_VENDOR,  OM_TEMPLATE_CATEGORY_LVL1,  OM_TEMPLATE_CATEGORY_TYPE WHERE ");
		query.append(" OM_ORDER.SYS_ORDER_ID = OM_ORDER_LINE.SYS_ORDER_ID AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_LINE.ORDER_PLACED_DTTM ");
		query.append(" AND OM_ORDER_LINE.SYS_ORDER_LINE_ID = OM_ORDER_LINE_TEMPLATE.SYS_ORDER_LINE_ID ");
		query.append(" AND OM_ORDER_LINE.ORDER_PLACED_DTTM = OM_ORDER_LINE_TEMPLATE.ORDER_PLACED_DTTM ");
		query.append(" AND OM_ORDER.SYS_OWNING_LOC_ID = OM_LOCATION.SYS_LOCATION_ID AND OM_ORDER_LINE_TEMPLATE.SYS_TEMPLATE_ID = OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_ID");
		query.append(" AND OM_ORDER_LINE_TEMPLATE.SYS_TEMPLATE_ID = OM_TEMPLATE.SYS_TEMPLATE_ID AND OM_TEMPLATE.SYS_ROYALTY_VENDOR_ID = OM_VENDOR.SYS_VENDOR_ID");
		query.append(" AND OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_TEMPLATE_CATEGORY_LVL1_ID = OM_TEMPLATE_CATEGORY_LVL1.SYS_TEMPLATE_CATEGORY_LVL1_ID ");
		query.append(" AND OM_TEMPLATE_TEMPL_CAT_ASSO.SYS_CATEGORY_TYPE_ID = OM_TEMPLATE_CATEGORY_TYPE.SYS_TEMPLATE_CATEGORY_TYPE_ID ");
		query.append(" AND OM_VENDOR.DESCRIPTION =  ?  AND OM_TEMPLATE_CATEGORY_TYPE.TEMPLATE_CATEGORY_TYPE = ? ");
		query.append(" AND OM_ORDER.ORDER_SOLD_DTTM BETWEEN TO_DATE(? || ' 00:00:00', 'dd-mm-yyyy HH24:MI:SS')  AND TO_DATE(? || ' 23:59:59', 'dd-mm-yyyy HH24:MI:SS') "); 
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM > TO_DATE( ? , 'dd-mm-yyyy') - 195 ");
		query.append(" GROUP BY OM_LOCATION.LOCATION_NBR ,  OM_TEMPLATE.TEMPLATE_NBR ,  OM_TEMPLATE.OUTPUT_SIZE,  OM_VENDOR.DESCRIPTION , ");
		query.append(" OM_TEMPLATE_CATEGORY_LVL1.DESCRIPTION , ");
		query.append(" OM_TEMPLATE.DESCRIPTION  ORDER BY  TEMPLATEID, STORENUMBER) ");
		query.append(" ) WHERE  RNK BETWEEN ? AND ? ");
		return query;
	}

	/**
	 * Method to get group by criteria for royalty report 
	 */
	
	public static StringBuffer getRoyaltyGroupByCriteria(){
		StringBuffer query = new StringBuffer();
		query.append("SELECT ROYALTY_SALES_REPORT_CATEGORY AS VENDORNAME FROM OM_VENDOR_ATTRIBUTE OMVA INNER JOIN OM_VENDOR OMV ");
				query.append( "ON OMVA.SYS_VENDOR_ID = OMV.SYS_VENDOR_ID WHERE OMV.DESCRIPTION = ?");
		
		return query;
	}
	
	/**
	 * This method create the SQL query to find out 'SYS_USER_REPORT_PREF_ID'. 
	 * @return Stringbuffer
	 */
		
	public static StringBuffer getActiveUserPrefId() {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT SYS_USER_REPORT_PREF_ID FROM OM_USER_REPORT_PREFS INNER JOIN OM_REPORT ");
		query.append(" ON OM_REPORT.SYS_REPORT_ID = OM_USER_REPORT_PREFS.SYS_REPORT_ID ");
		query.append(" WHERE OM_REPORT.REPORT_NAME = ? AND OM_REPORT.ACTIVE_CD = 1 ");
		query.append(" AND OM_USER_REPORT_PREFS.ACTIVE_CD = 1 ");
		
		return query;
	}
	
	/**
	 * This method create the SQL query to find out 'SYS_DEFAULT_REPORT_PREF_ID'. 
	 * @return Stringbuffer
	 */
		
	public static StringBuffer getActiveDefaultPrefId() {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT SYS_DEFAULT_REPORT_PREF_ID FROM OM_DEFAULT_REPORT_PREFS INNER JOIN OM_REPORT ");
		query.append(" ON OM_REPORT.SYS_REPORT_ID = OM_DEFAULT_REPORT_PREFS.SYS_REPORT_ID ");
		query.append(" WHERE OM_REPORT.REPORT_NAME = ? AND OM_REPORT.ACTIVE_CD = 1 ");
		query.append(" AND OM_DEFAULT_REPORT_PREFS.ACTIVE_CD = 1 ");
		return query;
	}
	
	
	/**
	 * This method will return query to fetch report Report type
	 * @return stringBuffer --> Query
	 */
	
	public static StringBuffer getVendorAttributes(){
		StringBuffer query = new StringBuffer();
		
		query.append(" SELECT ROYALTY_SALES_GEN_TYPE, ROYALTY_REPORT_GENERATION_DAY, ROYALTY_REPORT_GEN_QUARTER, ");
		query.append(" ROYALTY_REPORT_GEN_MONTH FROM OM_VENDOR_ATTRIBUTE ,OM_VENDOR WHERE ");
		query.append(" OM_VENDOR_ATTRIBUTE.SYS_VENDOR_ID = OM_VENDOR.SYS_VENDOR_ID AND  DESCRIPTION= ?");
		return query;
	}
	

	/**
	 * Method to get Primary key from OM_STORE_PROMO_ASSOC table.
	 * @return String SQL getting Primary Key Query.
	 * @since v1.0
	 */
	public static String  getStorePromotionAssocPrimaryKey() {

		return "SELECT OM_STORE_PROMO_ASSOC_SEQ.NEXTVAL FROM DUAL";
	}

	/**
	 * Method to get insert Query to OM_STORE_PROMOTION_ASSOC table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder insertQueryToStorePromotionASSOC() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("INSERT INTO OM_STORE_PROMOTION_ASSOC (SYS_STORE_PROMO_ASSOC_ID, SYS_LOCATION_ID,  SYS_PLU_ID,");
		strQuery.append(" PLU_START_DATE, PLU_END_DATE, ACTIVE_CD, AD_EVENT_TYPE_CD, AD_EVENT_SEQ_ID, CREATE_USER_ID, CREATE_DTTM)");
		strQuery.append("values(?, ?, ?, ?, ?, ?,?,?,?,").append(PhotoOmniConstants.DEFUALT_DTTM).append(")");

		return strQuery;
	}

	/**
	 * Method to get delete Query to OM_STORE_PROMOTION_ASSOC table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder deletQueryToStorePromotionASSOC() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_STORE_PROMOTION_ASSOC");
		strQuery.append(" WHERE  AD_EVENT_TYPE_CD = ? ");
		strQuery.append(" AND AD_EVENT_SEQ_ID = ? ");

		return strQuery;
	}

	/**
	 * Method to get select Query to get store list.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder getStoreList() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("SELECT LOCATION_NBR AS STORE_NBR FROM OM_AD_EVENT_STORES_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM   = TO_DATE( ? ,'DD-MON-YY:HH24:mi:ss')");
		strQuery.append(" AND  AD_EVENT_TYPE_CD = ? ");
		strQuery.append(" AND AD_EVENT_SEQ_ID = ? ");

		return strQuery;
	}

	/**
	 * Method to get update Query to OM_STORE_PROMOTION_ASSOC table when release IND = D.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder updateStoreAssocActiveCD() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE OM_STORE_PROMOTION_ASSOC SET ACTIVE_CD = 0");
		strQuery.append(" WHERE SYS_LOCATION_ID = ? ");
		strQuery.append(" AND AD_EVENT_TYPE_CD = ? ");
		strQuery.append(" AND AD_EVENT_SEQ_ID = ? ");

		return strQuery;
	}

	/**
	 * Method to get delete Query to OM_AD_EVENT_HEADER_STG table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder deletPromoHederTempTableData() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_HEADER_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM = TO_DATE( ? ,'DD-MON-YY:HH24:MI:SS')");
		return strQuery;
	}

	/**
	 * Method to get delete Query to OM_AD_EVENT_COUPONS_STG table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder deletPromoCouponTempTableData() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_COUPONS_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM = TO_DATE( ? ,'DD-MON-YY:HH24:MI:SS')");
		return strQuery;
	}

	/**
	 * Method to get delete Query to OM_AD_EVENT_STORES_STG table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder deletPromoStoreTempTableData() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_STORES_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM = TO_DATE( ? ,'DD-MON-YY:HH24:MI:SS')");

		return strQuery;
	}

	/**
	 * Method to get Update Query to to update PROMO_EMERGENCY_IND if any emergency coupon.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder UpdateEmergencyIndicator() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE OM_LOCATION SET PROMO_EMERGENCY_IND = 'Y'");
		strQuery.append(" WHERE LOCATION_NBR = ?");
		return strQuery;
	}

	/**
	 * Method to get Query to check whether Event type exists or not from OM_AD_EVENT_HEADER_STG.
	 * @return StringBuffer SQL select Query.
	 * @since v1.0
	 */
	public static StringBuilder  checkEventTypeExists() {

		StringBuilder  strQuery = new StringBuilder();
		strQuery.append("SELECT COUNT(1)");
		strQuery.append(" FROM OM_AD_EVENT_HEADER_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM   = TO_DATE( ? ,'DD-MON-YY:HH24:mi:ss')");

		return strQuery;
	}

	/**
	 * Method to get the list of queries to delete temporary data
	 * 
	 * @return list<String> -- SQL query list
	 * 
	 */
	public static List<String> getDeleteTempTableQuery(){

		List<String> queryList = new ArrayList<String>();
		StringBuilder strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_HEADER_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM  <  to_char((sysdate - ?),'DD-MON-YYYY') ");
		queryList.add(strQuery.toString());
		strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_COUPONS_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM <   to_char((sysdate - ?), 'DD-MON-YYYY')");
		queryList.add(strQuery.toString());
		strQuery = new StringBuilder();
		strQuery.append("DELETE FROM OM_AD_EVENT_STORES_STG");
		strQuery.append(" WHERE FEEDFILE_RCVD_DTTM <   to_char((sysdate - ?), 'DD-MON-YYYY')");
		queryList.add(strQuery.toString());
		return queryList;
	}
	
	/**
	 * Method to get query to delete data from 
	 * store association table
	 * 
	 * @return String -- SQL query 
	 * 
	 */
	public static String getDeleteStoreAssociationQuery(){
	StringBuilder stringQuery =  new StringBuilder();
	stringQuery.append("DELETE FROM OM_STORE_PROMOTION_ASSOC ");
	stringQuery.append("WHERE PLU_END_DATE < to_char((sysdate - ?), 'DD-MON-YYYY')");
	return stringQuery.toString();
	}
}
