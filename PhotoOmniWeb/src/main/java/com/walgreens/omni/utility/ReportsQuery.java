package com.walgreens.omni.utility;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.exception.PhotoOmniException;

public class ReportsQuery {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportsQuery.class);
	/**
	 * This method create the SQL select query for Machine type 
	 * @return query
	 */
	public static StringBuffer getVendorListQueryForRoyaltyReport() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_VENDOR_ID, VENDOR_NAME FROM OM_VENDOR WHERE VENDOR_TYPE = 'ROYALTY' ");
		return query;
	}

	public static StringBuffer getCustomreportInsertQuery() {

		StringBuffer query = new StringBuffer();
		query.append("insert into OM_USER_REPORT_PREFS (SYS_USER_REPORT_PREF_ID, SYS_USER_ID, SYS_REPORT_ID, PREFERENCE_TYPE, FILTER_STATE, AUTO_REFRESH_IND,");
		query.append("AUTO_EXECUTE_IND, AUTO_REFRESH_INTERVAL, ACTIVE_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM)");
		query.append("values(OM_USER_REPORT_PREFS_SEQ.NEXTVAL, ?, ?, ?, ? , ?, ?, ?, ? , ? , SYSDATE , ?, SYSDATE )");
		return query;

	}

	public static StringBuffer getPMReportIdQuery() {

		StringBuffer query = new StringBuffer();
		query.append("select SYS_REPORT_ID from OM_REPORT where REPORT_NAME = ?");
		return query;

	}
	
	public static StringBuffer getUserIdQuery() {

		StringBuffer query = new StringBuffer();
		query.append("select SYS_USER_ID from OM_USER where SYS_USER_ID = ? ");
		return query;

	}
	/*
	 * This method create SQL select query for KIOSK Report which is displayed on the UI
	 * @Return Select query for the KIOSK Report on the UI
	 */	
	public static StringBuffer getKioskData(int page, int rows, String filter) {

		StringBuffer query = new StringBuffer();
		query.append(" SELECT * FROM ( SELECT a.*, rownum r__ FROM ( ");
		query.append("SELECT CAST(OM_DEVICE_DETAIL.LOCATION_ID as VARCHAR(30)) LocationId, OM_DEVICE_DETAIL.DEVICE_IP as KioskIp,");
		query.append("OM_DEVICE_DETAIL.SOFTWARE_VERSION as SoftwareVer, OM_DEVICE_DETAIL.TEMPLATE_VERSION as TemplateVer,");
		query.append("OM_DEVICE_DETAIL.IMEMORIES_VERSION as iMemoriesVer, OM_DEVICE_DETAIL.TOMO_VERSION as TOMOVer, OM_DEVICE_DETAIL.LAST_REBOOT as LastReboot,"); 
		query.append("OM_DEVICE_DETAIL.C_DRIVE_SPACE_MB as CSpace, CAST(OM_DEVICE_DETAIL.D_DRIVE_SPACE_MB as VARCHAR(30)) DSpace FROM OM_DEVICE_DETAIL ");
		query.append(filter);
		query.append(" ORDER BY OM_DEVICE_DETAIL.LOCATION_ID ");
		query.append(" ) a WHERE rownum < ((" + page + "*" + rows + ") + 1 )) WHERE r__ >= (((" + page + "-1) * " + rows + ") + 1)");	
		return query;
	}
		
	/*
	 * This method create SQL select query for KIOSK filter search criteria on the UI
	 * @Return Select query for the KIOSK filter criteria
	 */
	public static StringBuffer getKioskFilter() {

		StringBuffer query = new StringBuffer();
		query.append(" SELECT CODE.CODE_ID, CODE.DECODE FROM OM_CODE_DECODE CODE WHERE CODE.CODE_TYPE = 'KIOSK Details' ");
		query.append(" AND CODE.CODE_ID NOT IN ('SYS_DEVICE_INSTANCE_ID', 'DEVICE_TYPE', 'CREATE_USER_ID', 'UPDATE_USER_ID') ORDER BY CODE.DECODE ");
		return query;
	}
	
	/*
	 * This method create SQL select query for KOISK export excel data
	 * @Return Select query for the KIOSK excel export
	 */
	public static StringBuffer getKioskReportExcelData() {
		
		StringBuffer query = new StringBuffer();
		query.append(" Select cast(LOCATION_ID as VARCHAR2(30)) LOCATION_ID, DEVICE_IP, ");
		query.append(" SOFTWARE_VERSION, TEMPLATE_VERSION, HARDWARE_MODEL, SCANNER_MODEL, IMEMORIES_VERSION, FIRMWARE_VERSION, REGION_VERSION, ");
		query.append(" FB_ACTIVE, WAG_ACTIVE, MPL_STORE, IMEMORIES_ACTIVE, TOMO_VERSION, ");
		query.append(" AUTO_UPDATE_CONFIG_VERSION, WG_UPDATE_VERSION, CONTENT_VERSION, LAUNCHPAD_VERSION, ");
		query.append(" OS_VERSION, INSTANT_PRINTER_ATT, WIN7_ACTIVATED_IND, LAST_REBOOT as LastReboot, ");
		query.append(" MINILAB_MODEL, PHASER_MODEL, POSTER_MODEL, MASTER_VERSION, cast(C_DRIVE_SIZE_MB as ");
		query.append(" varchar(30)) C_DRIVE_SIZE_MB, C_DRIVE_SPACE_MB, ");
		query.append(" cast(D_DRIVE_SIZE_MB as varchar(30)) D_DRIVE_SIZE_MB, cast(D_DRIVE_SPACE_MB as varchar(30)) ");
		query.append(" D_DRIVE_SPACE_MB, RECEIPT_PRINTER_MODEL, CREATE_DTTM, ");
		query.append(" UPDATE_DTTM, LC_VERSION  from OM_DEVICE_DETAIL ");
		return query;
	}
	
	public static StringBuffer getKioskReportDataCount() {
		
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM OM_DEVICE_DETAIL");
		return query;
	}

	public static StringBuffer getCodeDecode(String criteria) {
		
		StringBuffer query = new StringBuffer();
		query.append("select DECODE from OM_CODE_DECODE where CODE_ID = ");
		query.append("'");
		query.append(criteria);
		query.append("'");
		return query;
	}
	
	public static StringBuilder getOrderTypeQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DECODE,CODE_ID FROM OM_CODE_DECODE where CODE_ID in('POP','SOP') and CODE_TYPE='Order_Type'");
		return query;

	}

	public static StringBuilder getInputChannelQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DECODE,CODE_ID FROM OM_CODE_DECODE where CODE_ID in('Kiosk','Internet','Mobile','A') and CODE_TYPE='Order_Origin_Type'");
		return query;

	}

	
	
	public static StringBuilder getGeneratedCannedReportDataPlaced(String sortColumn,String sortOrder,String startDate,
			String endDate, String originType,  boolean isALL, boolean isMobile, boolean isInternet, boolean isKiosk) {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  TOTAL_RECORD,PRODUCT_NAME,SYS_PRODUCT_ID,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,TOTAL_ORDER_REVENUE,TOTAL_DISCOUNT,TOTAL_REVENUE,TOTAL_REVENUE_DISCOUNT,Rn ");
			
		query.append( " FROM (SELECT TOTAL_RECORD,PRODUCT_NAME,SYS_PRODUCT_ID,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,TOTAL_ORDER_REVENUE,TOTAL_DISCOUNT,TOTAL_REVENUE,  TOTAL_REVENUE_DISCOUNT,    CASE ");
		query.append("  WHEN TOTAL_REVENUE =0 OR TOTAL_PRODUCT_QUANTITY =0 THEN 0  ELSE  (ROUND(TOTAL_REVENUE/TOTAL_PRODUCT_QUANTITY,2)) END  AS UNIT_PRICE");		
		query.append(" ,rownum as Rn");
		query.append("   FROM (SELECT DISTINCT COUNT(*) OVER()  AS TOTAL_RECORD,OM_PRODUCT.DESCRIPTION AS PRODUCT_NAME,");
		query.append("   OM_PRODUCT.SYS_PRODUCT_ID as SYS_PRODUCT_ID,");
		query.append("   COUNT(OM_PRODUCT.SYS_PRODUCT_ID) AS TOTAL_ORDERS,");
		query.append("   SUM(OM_ORDER_LINE.ORIGINAL_QTY)  AS TOTAL_PRODUCT_QUANTITY,");
		query.append("   SUM(OM_ORDER.Final_price)        AS TOTAL_ORDER_REVENUE,");
		query.append("   SUM(OM_ORDER.TOTAL_ORDER_DISCOUNT)  AS TOTAL_DISCOUNT,");
		query.append("   SUM(OM_ORDER_Line.ORIGINAL_LINE_PRICE)   AS TOTAL_REVENUE,   SUM(OM_ORDER_Line.FINAL_PRICE)  AS TOTAL_REVENUE_DISCOUNT ");
		  
		if(isKiosk)
		{
			query.append("   FROM OM_ORDER, ");
			query.append("   OM_ORDER_LINE, ");
			query.append("   OM_PRODUCT");
			query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
			query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
			query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");		
			query.append("   AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
			query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
				
			
			
		}
		else if(isInternet){
		
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
						    query.append("   OM_PRODUCT");
						    query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
						    query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
						    query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
						    query.append("   AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
						    query.append("   AND UPPER(OM_ORDER.ORDER_ORIGIN_TYPE)   =UPPER('"+ originType + "')");
						   
						   
						    
		}			
		
		else if (isALL) {
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");
							query.append("   AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')");
							
							
							
		} 
		else if (isMobile){
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
							query.append("    AND OM_PRODUCT.ACTIVE_CD=1 ");						
							query.append("    AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND UPPER(OM_ORDER.ORDER_ORIGIN_TYPE)   =UPPER('"+ originType + "')");
						
							
		}
		
		query.append("   GROUP BY OM_PRODUCT.SYS_PRODUCT_ID,");
		query.append("   OM_PRODUCT.DESCRIPTION");
	    query.append(" ORDER BY ");
		query.append(sortColumn).append(" ").append(sortOrder);
		query.append("))");
	    query.append("   WHERE Rn >= ? AND Rn <= ?");

		return query;

	}
	
	
	
	public static StringBuilder getGeneratedCannedReportDataSold(String sortColumn,String sortOrder,String startDate,
			String endDate, String originType, boolean isALL, boolean isMobile,boolean isInternet, boolean isKiosk) {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  TOTAL_RECORD,PRODUCT_NAME,SYS_PRODUCT_ID,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,TOTAL_ORDER_REVENUE,TOTAL_DISCOUNT,AMOUNT_PAID,TOTAL_REVENUE_DISCOUNT,UNIT_PRICE,Rn ");
		query.append(" ,PROFIT");		
		query.append( " FROM (SELECT TOTAL_RECORD,PRODUCT_NAME,SYS_PRODUCT_ID,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,TOTAL_ORDER_REVENUE,TOTAL_DISCOUNT,AMOUNT_PAID,  TOTAL_REVENUE_DISCOUNT,    CASE ");
		query.append("  WHEN  AMOUNT_PAID =0 OR TOTAL_PRODUCT_QUANTITY =0 THEN 0  ELSE  (ROUND( AMOUNT_PAID/TOTAL_PRODUCT_QUANTITY,2)) END  AS UNIT_PRICE");
		query.append(" ,PROFIT");		
		query.append(" ,rownum as Rn");
		query.append("   FROM (SELECT DISTINCT COUNT(*) OVER()  AS TOTAL_RECORD,OM_PRODUCT.DESCRIPTION AS PRODUCT_NAME,");
		query.append("   OM_PRODUCT.SYS_PRODUCT_ID as SYS_PRODUCT_ID,");
		query.append("   COUNT(OM_PRODUCT.SYS_PRODUCT_ID) AS TOTAL_ORDERS,");
		query.append("   SUM(OM_ORDER_LINE.QUANTITY)   AS TOTAL_PRODUCT_QUANTITY,");
		query.append("   SUM(OM_ORDER.SOLD_AMOUNT)     AS TOTAL_ORDER_REVENUE,");
		query.append("   SUM(OM_ORDER.TOTAL_ORDER_DISCOUNT)        AS TOTAL_DISCOUNT,");
		query.append("   SUM(OM_ORDER_LINE.LINE_SOLD_AMOUNT) AS AMOUNT_PAID,  SUM(OM_ORDER_Line.FINAL_PRICE)  AS TOTAL_REVENUE_DISCOUNT ");
		query.append(" ,SUM(OM_ORDER_LINE.COST) AS COST, SUM(OM_ORDER_LINE.LINE_SOLD_AMOUNT)-SUM(OM_ORDER_LINE.COST) AS PROFIT");
		
		if(isKiosk)
		{
		
			query.append("   FROM OM_ORDER, ");
			query.append("   OM_ORDER_LINE, ");
			query.append("   OM_PRODUCT");
			query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
			query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
			query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");		
			query.append("	 AND OM_ORDER.STATUS='SOLD'");
			query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
			query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
			query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
			
		}
		else if(isInternet){
		
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
						    query.append("   OM_PRODUCT");
						    query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
						    query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
						    query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
						    query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
							
		}			
		
		else if (isALL) {
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");
							query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')");
							
		} 
		else if (isMobile){
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
							query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195"); 
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
							
		}
		
		query.append("   GROUP BY OM_PRODUCT.SYS_PRODUCT_ID,");
		query.append("   OM_PRODUCT.DESCRIPTION");
	    query.append(" ORDER BY ");
		query.append(sortColumn).append(" ").append(sortOrder);
		query.append("))");
	    query.append("   WHERE Rn >= ? AND Rn <= ?");

		return query;

	}



	
	
	
	public static StringBuilder getGeneratedCannedReportDataCsvPlaced(
			String startDate, String endDate, String originType, 
			 boolean isALL, boolean isMobile ,boolean isInternet, boolean isKiosk ) {

		StringBuilder query = new StringBuilder();
		query.append("  SELECT PRODUCT_NAME,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,TOTAL_REVENUE,TOTAL_REVENUE_DISCOUNT ,'$'|| UNIT_PRICE AS UNIT_PRICE ");
		query.append("  FROM   (SELECT  PRODUCT_NAME,  TOTAL_ORDERS, TOTAL_PRODUCT_QUANTITY,  '$'|| TOTAL_REVENUE AS TOTAL_REVENUE, '$'|| TOTAL_REVENUE_DISCOUNT AS TOTAL_REVENUE_DISCOUNT, ");
		query.append("	 CASE WHEN TOTAL_REVENUE =0 OR TOTAL_PRODUCT_QUANTITY =0 THEN 0  ELSE (ROUND(TOTAL_REVENUE/TOTAL_PRODUCT_QUANTITY,2)) END AS UNIT_PRICE");
		query.append("   FROM (SELECT OM_PRODUCT.DESCRIPTION AS PRODUCT_NAME,");
		// query.append("   OM_PRODUCT.SYS_PRODUCT_ID as SYS_PRODUCT_ID,");
		query.append("   COUNT(OM_PRODUCT.SYS_PRODUCT_ID) AS TOTAL_ORDERS,");
		query.append("   SUM(om_order_line.original_qty)  AS TOTAL_PRODUCT_QUANTITY,");
		query.append("   SUM(OM_ORDER.Final_price)        AS TOTAL_ORDER_REVENUE,");
		// query.append("   SUM(TOTAL_ORDER_DISCOUNT)        AS TOTAL_DISCOUNT,");
		query.append("    SUM(OM_ORDER_Line.ORIGINAL_LINE_PRICE)        AS TOTAL_REVENUE,    SUM(OM_ORDER_Line.Final_price)  AS TOTAL_REVENUE_DISCOUNT");
		
		if(isKiosk)
		{
		
			query.append("   FROM OM_ORDER, ");
			query.append("   OM_ORDER_LINE, ");
			query.append("   OM_PRODUCT");
			query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
			query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
			query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");		
			query.append(" AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
			query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
			
			
		}
		else if(isInternet){
		
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
						    query.append("   OM_PRODUCT");
						    query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
						    query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
						    query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
						    query.append("   AND OM_PRODUCT.ACTIVE_CD=1 ");						   
						    query.append("   AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
						    query.append("   AND UPPER(OM_ORDER.ORDER_ORIGIN_TYPE)   =UPPER('"+ originType + "')");
						    
						   
		}			
		
		else if (isALL) {
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");
							query.append("   AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')");
							
						
		} 
		else if (isMobile){
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
							query.append("    AND OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND UPPER(OM_ORDER.ORDER_ORIGIN_TYPE)   =UPPER('"+ originType + "')");
							
					
		}
		
		query.append("   GROUP BY OM_PRODUCT.SYS_PRODUCT_ID,");
		query.append("   OM_PRODUCT.DESCRIPTION");
		query.append("))");

		return query;

	}
	
	
	public static StringBuilder getGeneratedCannedReportDataCsvSold(
			String startDate, String endDate, String originType, 
			 boolean isALL, boolean isMobile ,boolean isInternet, boolean isKiosk ) {

		StringBuilder query = new StringBuilder();
		query.append("  SELECT PRODUCT_NAME,TOTAL_ORDERS,TOTAL_PRODUCT_QUANTITY,AMOUNT_PAID,TOTAL_REVENUE_DISCOUNT ,'$'|| UNIT_PRICE AS UNIT_PRICE ");
	    query.append(",PROFIT");
		
		query.append("  FROM   (SELECT  PRODUCT_NAME,  TOTAL_ORDERS, TOTAL_PRODUCT_QUANTITY,  '$'|| AMOUNT_PAID AS AMOUNT_PAID, '$'|| TOTAL_REVENUE_DISCOUNT AS TOTAL_REVENUE_DISCOUNT, ");
		query.append("	 CASE WHEN AMOUNT_PAID =0 OR TOTAL_PRODUCT_QUANTITY =0 THEN 0  ELSE (ROUND(AMOUNT_PAID/TOTAL_PRODUCT_QUANTITY,2)) END AS UNIT_PRICE");
		query.append(",PROFIT");
		query.append("   FROM (SELECT OM_PRODUCT.DESCRIPTION AS PRODUCT_NAME,");
		// query.append("   OM_PRODUCT.SYS_PRODUCT_ID as SYS_PRODUCT_ID,");
		query.append("   COUNT(OM_PRODUCT.SYS_PRODUCT_ID) AS TOTAL_ORDERS,");
		query.append("   SUM(OM_ORDER_LINE.QUANTITY)  AS TOTAL_PRODUCT_QUANTITY,");
		query.append("   SUM(OM_ORDER.SOLD_AMOUNT)       AS TOTAL_ORDER_REVENUE,");
		// query.append("   SUM(TOTAL_ORDER_DISCOUNT)        AS TOTAL_DISCOUNT,");
		query.append("   SUM(OM_ORDER_LINE.LINE_SOLD_AMOUNT) AS AMOUNT_PAID,    SUM(OM_ORDER_Line.Final_price)  AS TOTAL_REVENUE_DISCOUNT");
		query.append(" ,SUM(OM_ORDER_LINE.COST) AS COST, SUM(OM_ORDER_LINE.LINE_SOLD_AMOUNT)-SUM(OM_ORDER_LINE.COST) AS PROFIT");
		
		
		if(isKiosk)
		{
		
			query.append("   FROM OM_ORDER, ");
			query.append("   OM_ORDER_LINE, ");
			query.append("   OM_PRODUCT");
			query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
			query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
			query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");		
			query.append("	 AND OM_ORDER.STATUS='SOLD'");
			query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
			query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
			query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
				
			
		}
		else if(isInternet){
		
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
						    query.append("   OM_PRODUCT");
						    query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
						    query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
						    query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
						    query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
							
		}			
		
		else if (isALL) {
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID");
							query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')");
							
		} 
		else if (isMobile){
							query.append("   FROM OM_ORDER, ");
							query.append("   OM_ORDER_LINE, ");
							query.append("   OM_PRODUCT");
							query.append("   WHERE OM_ORDER.SYS_ORDER_ID=OM_ORDER_LINE.SYS_ORDER_ID AND ");	
							query.append("   OM_ORDER.ORDER_PLACED_DTTM= OM_ORDER_LINE.ORDER_PLACED_DTTM AND ");
							query.append("   OM_ORDER_LINE.SYS_PRODUCT_ID=OM_PRODUCT.SYS_PRODUCT_ID ");			
							query.append("	 AND OM_ORDER.STATUS='SOLD'");
							query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
							query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
							query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')");
								
		}
		
		query.append("   GROUP BY OM_PRODUCT.SYS_PRODUCT_ID,");
		query.append("   OM_PRODUCT.DESCRIPTION");
		query.append("))");

		return query;


	}
	
	
	
	
	
	
	
	
	/**
	 * 
	 */
	public static StringBuilder getGenericFields(
			String startDate, String endDate, String originType,boolean isPlaced, boolean isSold,
			 boolean isALL, boolean isMobile,boolean isInternet, boolean isKiosk) {

		StringBuilder query = new StringBuilder();
		
		query.append("  SELECT ");
		if(isPlaced){		
		 query.append("SUM(FINAL_PRICE) AS TOTAL_ORDER_REVENUE,");
		}
		if(isSold){		
			query.append("SUM(SOLD_AMOUNT) AS TOTAL_ORDER_REVENUE,");
		}
				
		query.append("  SUM(TOTAL_ORDER_DISCOUNT) as TOTAL_DISCOUNT,  COUNT(SYS_ORDER_ID)       AS TOTAL_ORDERS  FROM OM_ORDER"); 
	 
	    if(isKiosk)
	    {
	    	if(isPlaced)
	    	{
	    		query.append("  WHERE OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
	    		query.append(" AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')  ");
	    	}
	        if(isSold)
	        {        	
			    	query.append("	 WHERE OM_ORDER.STATUS='SOLD'");
					query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
					query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
					query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "') ");				
	        }
	    }
	    else if (isInternet)
	    {
	    	if(isPlaced)
	    	{
	    		query.append("  WHERE OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
	    		query.append(" AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')  ");
	    	}
	    	if(isSold)
	        {        	
			    	query.append("	 WHERE OM_ORDER.STATUS='SOLD'");
					query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
					query.append("   AND OM_ORDER. ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
					query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "') ");				
	        }
	    	
	    }
	    else if (isMobile)
	    {
	    	if(isPlaced)
	    	{
	    		query.append("  WHERE OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
	    		query.append(" AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "')  ");
	    	}
	    	if(isSold)
	        {        	
			    	query.append("	 WHERE OM_ORDER.STATUS='SOLD'");
					query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
					query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
					query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE)   =('"+ originType + "') ");				
	        }
	    }
	    else if (isALL)
	    {
	    	if(isPlaced)
	    	{
	    		query.append("  WHERE OM_ORDER. ORDER_PLACED_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");
	    		query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')  ");
	    	}
	    	if (isSold)
	    	{
	    		query.append("	 WHERE OM_ORDER.STATUS='SOLD'");
				query.append("   AND OM_ORDER. ORDER_SOLD_DTTM between TO_DATE('"+ startDate + " 00:00:00' ,'YYYY-MM-DD HH24:MI:SS') And TO_DATE('"+ endDate + "  23:59:59','YYYY-MM-DD HH24:MI:SS') ");	
				query.append("   AND OM_ORDER.ORDER_PLACED_DTTM >= TO_DATE('"+ startDate + "  00:00:00' ,'YYYY-MM-DD HH24:MI:SS') -195");
				query.append("   AND (OM_ORDER.ORDER_ORIGIN_TYPE) IN ('Kiosk','Internet', 'Mobile')  ");
	    	}
	    	
	    }
	    return query;
	    
	}
	    


		
	
	

	/**
	 * @return String getSimRetailBlockOnloadRespQuery
	 */
	public static String getSimRetailBlockOnloadRespQuery() {

		StringBuilder query = new StringBuilder();
		query.append("SELECT PRICE_LEVEL,DESCRIPTION FROM OM_PRICE_LEVEL");
		return query.toString();
	}
	
	/**
	 * @param locationType
	 * @param number
	 * @return
	 */
	public static String getLocationNoList(String locationType,List<String> number) {
		
		StringBuilder query = new StringBuilder();
		String LOCATION_TYPE = "";
		
		if(locationType.equalsIgnoreCase("Store")){
			 LOCATION_TYPE = "OMLOC.LOCATION_NBR";
		}else if(locationType.equalsIgnoreCase("District")){
			 LOCATION_TYPE = "OMLOC.DISTRICT_NBR";
		}else if(locationType.equalsIgnoreCase("Region")){
			 LOCATION_TYPE = "OMLOC.REGION_NBR";
		}else if(locationType.equalsIgnoreCase("Chain")){
			 LOCATION_TYPE = "CHAIN";
		}else if(locationType.equalsIgnoreCase("")){
			 LOCATION_TYPE = "OMLOC.LOCATION_NBR";
		}
		
		if(LOCATION_TYPE.equalsIgnoreCase("CHAIN")){
			query.append("SELECT OMLOC.LOCATION_NBR FROM OM_LOCATION OMLOC");
		}else{		
			query.append("SELECT OMLOC.LOCATION_NBR FROM OM_LOCATION OMLOC WHERE "+LOCATION_TYPE+" IN(");	    
			  for(int i=0;i<number.size();i++){			
			    if(i != (number.size()-1) ){
			      query.append(number.get(i));
			      query.append(",");
		        }else{
		    	  query.append(number.get(i));
		          }		
		       }query.append(")");
		 }
		return query.toString();
	}
	/**
	 * @param number
	 * @param locationType
	 * @param sortOrder 
	 * @param sortColoumnOne 
	 * @param retailBlock
	 * @throws PhotoOmniException 
	 * @return getGenarateSimRetailBlockReportQuery
	 */
	public static String getGenarateSimRetailBlockReportQuery(String locationType, List<String> number, 
			  String sortColoumnOne, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(number);
		query.append("SELECT * FROM (SELECT T.*,ROWNUM AS RNK FROM(SELECT COUNT(*) OVER () AS TOTAL_ROWS,");
		query.append("OMLOC.LOCATION_NBR  AS LOCATION_NBR,OMLOC.SYS_PRICE_LEVEL_ID AS SYS_PRICE_LEVEL_ID,");
		query.append("OMPRLVL.PRICE_LEVEL AS PRICE_LEVEL,OMPRLVL.DESCRIPTION AS DESCRIPTION");
		query.append(" FROM OM_LOCATION OMLOC,OM_PRICE_LEVEL OMPRLVL ");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		query.append(" AND OMPRLVL.PRICE_LEVEL = ? AND OMLOC.SYS_PRICE_LEVEL_ID = ? ORDER BY ");
		query.append(sortColoumnOne +" "+sortOrder);
		query.append(")T )OMSILVERRECHEAD WHERE RNK BETWEEN ? AND ? ");
		return query.toString();
	}
	/**
	 * This method creates multiple in block for SqlQuery if storeDataList size
	 * is more than 1000.
	 * 
	 * @param storeDataList
	 *            contains store.
	 * @return inQuery.
	 * @throws PhotoOmniException
	 *             custom exception.
	 */

	private static String getInQueryforSimRetailBlockOne(List<String> storeDataList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery ");
		}
		
		StringBuilder inQuery = new StringBuilder();
		int listSize = storeDataList.size();
		int IN_QUERY_VALUE_SIZE = 1000;
		try {
			if (listSize > IN_QUERY_VALUE_SIZE) {
				int loop = (listSize) / IN_QUERY_VALUE_SIZE;
				int remainder = (listSize) % IN_QUERY_VALUE_SIZE;
				if (remainder > 0) {
					loop++;
				}
				int start = 0;
				int end = IN_QUERY_VALUE_SIZE;
				for (int i = 0; i < loop; i++) {
					inQuery.append(" OMLOC.LOCATION_NBR IN(");
					for (int j = start; j < end; j++) {
						if (j == (listSize - 1)) {
							inQuery.append(storeDataList.get(listSize - 1)
									+ ",");
							break;
						} else {
							inQuery.append(storeDataList.get(j) + ",");
						}
					}
					int lastIndex = inQuery.lastIndexOf(",");
					inQuery.deleteCharAt(lastIndex);
					inQuery.append(" ) ");
					if (loop != (i + 1)) {
						inQuery.append(" OR ");
					}
					start = end;
					end = end + IN_QUERY_VALUE_SIZE;
				}

			} else {
				inQuery.append(" OMLOC.LOCATION_NBR IN(");
				for (int i = 0; i < listSize; i++) {
					inQuery.append(storeDataList.get(i) + ",");
				}
				int lastIndex = inQuery.lastIndexOf(",");
				inQuery.deleteCharAt(lastIndex);
				inQuery.append(" ) ");

			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getInQueryforimRetailBlockOne method of SimRetailBlockReportsQuery ");
			}
		}
		return inQuery.toString();
	}
	/**
	 * @param storeNoList
	 * @param tempRetailBlockDescription
	 * @return
	 * @throws PhotoOmniException 
	 */
	public static String getUpdateSimRetailBlockQuery(List<String> storeNoList,String tempRetailBlockDescription) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(storeNoList);
		query.append("UPDATE OM_LOCATION OMLOC SET OMLOC.SYS_PRICE_LEVEL_ID = ");
		query.append("(SELECT OMPRLVL.PRICE_LEVEL FROM OM_PRICE_LEVEL OMPRLVL WHERE OMPRLVL.DESCRIPTION =");
		query.append("'"+tempRetailBlockDescription+"'");
		query.append(")");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		return query.toString();
	}
	/**
	 * @param locationType
	 * @param locNumList
	 * @param sortColoumnOne
	 * @param sortOrder
	 * @return String
	 * @throws PhotoOmniException 
	 */
	public static String getGenarateSimRetailBlockReportCSVQuery(String locationType, List<String> number, 
			  String sortColoumnOne, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		String inQuery = getInQueryforSimRetailBlockOne(number);
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,");
		query.append("OMLOC.LOCATION_NBR  AS LOCATION_NBR,OMLOC.SYS_PRICE_LEVEL_ID AS SYS_PRICE_LEVEL_ID,");
		query.append("OMPRLVL.PRICE_LEVEL AS PRICE_LEVEL,OMPRLVL.DESCRIPTION AS DESCRIPTION");
		query.append(" FROM OM_LOCATION OMLOC,OM_PRICE_LEVEL OMPRLVL ");
		query.append("WHERE (");
		query.append(inQuery);
		query.append(")");
		query.append(" AND OMPRLVL.PRICE_LEVEL = ? AND OMLOC.SYS_PRICE_LEVEL_ID = ? ORDER BY ");
		query.append(sortColoumnOne +" "+sortOrder);
		return query.toString();
	}
	
	
}