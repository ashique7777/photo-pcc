package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.oms.json.bean.ExceptionByEnvelopeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeFilter;

public class ExceptionReportQuery {

	/*
	 * Query for Exception Report by Employees
	 */
	public static StringBuilder getExceptionReportByEmployeeQuery(
			ExceptionEmployeeFilter requestBean) {

		StringBuilder query = new StringBuilder();
		String sortColumn = requestBean.getSortColumnName();
		String sortOrder = requestBean.getSortOrder();
		query.append("SELECT * ");
		query.append(" FROM ");
		query.append("  (SELECT EMPLOYEE_NAME, ");
		query.append("    EMPLOYEE_ID, ");
		query.append("    EXCEPTION_TYPE, ");
		query.append("    EXCEPTION_CNT, ");
		query.append("    TO_CHAR( WASTE_COST , '9999999999990.90') AS WASTE_COST, ");
		query.append("    TOTALROWS, ");
		query.append(" ROW_NUMBER () OVER ( ");
		query.append(" ORDER BY ");
		query.append(sortColumn).append(" ").append(sortOrder);
		query.append(" ) AS RNK ");
		query.append("   FROM ");
		query.append("     (SELECT (OM_USER_ATTRIBUTES.LAST_NAME ");
		query.append("       || ',' ");
		query.append("       || OM_USER_ATTRIBUTES.FIRST_NAME)       AS EMPLOYEE_NAME, ");
		query.append("       OM_USER_ATTRIBUTES.EMPLOYEE_ID          AS EMPLOYEE_ID, ");
		query.append("       OM_EXCEPTION_TYPE.EXCEPTION_TYPE        AS EXCEPTION_TYPE,");
		query.append("       COUNT(OM_EXCEPTION_TYPE.EXCEPTION_TYPE) AS EXCEPTION_CNT,");
		query.append("       SUM(OM_ORDER_EXCEPTION.WASTE_COST)      AS WASTE_COST,");
		query.append(" 	      COUNT(*) OVER()                         AS TOTALROWS");
		query.append(" 	    FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" Order_Tab,");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(
				" OM_ORDER_ATTRIBUTE ,");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ,");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(
				" OM_ORDER_EXCEPTION, ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE).append(
				" OM_EXCEPTION_TYPE, ");
		query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(
				" OM_USER_ATTRIBUTES ");
		query.append(" 	    WHERE Order_Tab.Sys_Order_Id                 = OM_ORDER_ATTRIBUTE.Sys_Order_Id ");
		query.append(" 	    AND Order_Tab.Order_Placed_Dttm              = OM_ORDER_ATTRIBUTE.Order_Placed_Dttm ");
		query.append("     AND Om_Location.Sys_Location_Id              = Om_Order_Exception.Sys_Location_Id ");
		query.append("     AND Order_Tab.Sys_Order_Id                   = Om_Order_Exception.Sys_Order_Id");
		query.append("     AND Order_Tab.Order_Placed_Dttm              = Om_Order_Exception.Order_Placed_Dttm");
		query.append("     AND Om_Order_Exception.Sys_Exception_Type_Id = Om_Exception_Type.Sys_Exception_Type_Id");
		query.append("     AND Om_User_Attributes.Employee_Id           = Om_Order_Exception.Create_User_Id");
		query.append("     AND Om_Location.Location_Nbr                 = ? ");
		query.append("     AND Om_Order_Exception.Create_Dttm BETWEEN To_Date(? || '00:00:00' ,'DD-MM-YYYY HH24:MI:SS') AND To_Date(? || '23:59:59' ,'DD-MM-YYYY HH24:MI:SS')");
		query.append("     AND Order_Tab.Order_Placed_Dttm > To_Date(? || '00:00:00' ,'DD-MM-YYYY HH24:MI:SS') - 195");
		query.append("     GROUP BY (OM_USER_ATTRIBUTES.LAST_NAME, OM_USER_ATTRIBUTES.FIRST_NAME, EXCEPTION_TYPE, EMPLOYEE_ID)");
		query.append(" 	    )");
		query.append(" 	  )");

		return query;
	}

	public static StringBuilder getExceptionByEnvMain(
			ExceptionByEnvelopeFilter requestBean) {
		StringBuilder query = new StringBuilder();
		String sortColumn = requestBean.getSortColumnName();
		String sortOrder = requestBean.getSortOrder();
		query.append(" SELECT  * FROM (SELECT ORDER_ATTRIBUTE.ENVELOPE_NUMBER AS ENV_NUMBER, EXCEPTION_TYPE.EXCEPTION_TYPE AS REASON");
		/*
		 * Customer Name Removed from the Exception report 
		 */
		//query.append(" ,ORDER_TAB.CUSTOMER_FIRST_NAME AS CUSTOMER_FIRST_NAME, ORDER_TAB.CUSTOMER_LAST_NAME AS CUSTOMER_LAST_NAME ");
		query.append(" ,USER_ATTRIBUTE.LAST_NAME AS EMPLOYEE_LAST_NAME, USER_ATTRIBUTE.FIRST_NAME AS EMPLOYEE_FIRST_NAME,");
		query.append("  ORDER_TAB.ORDER_DESCRIPTION AS ENV_DESCRIPTION,");
		query.append(" ORDER_TAB.STATUS AS ENV_STATUS, TO_CHAR(ORDER_TAB.ORDER_PLACED_DTTM, 'MM-dd-yyyy hh:mi:ss AM' ) AS ENV_ENTERED_DTTM, TO_CHAR(ORDER_EXCEPTION.CREATE_DTTM, 'MM-dd-yyyy hh:mi:ss AM' ) AS EXCEPTION_ENTERED_DTTM,");
		query.append("  '$' ||  TO_CHAR( ORDER_TAB.FINAL_PRICE , '9999999999990.90') AS ORDER_PRICE, '$' ||  TO_CHAR(  ORDER_TAB.SOLD_AMOUNT , '9999999999990.90') AS SOLD_PRICE, ORDER_EXCEPTION.WASTE_QTY   AS REFUSED_PRINTS,");
		query.append("  NVL(ORDER_EXCEPTION.STATUS, 'No Exception') AS EXCEPTION_STATUS, ");
		/*
		 * Env popup for different types of orders
		 */
		query.append(" ORDER_ATTRIBUTE.PROCESSING_TYPE_CD AS PROCESSING_TYPE_CD, ORDER_TAB.ORDER_ORIGIN_TYPE AS ORDER_ORIGIN_TYPE , ORDER_TAB.SYS_ORDER_ID AS ORDER_ID, ");
		
		query.append(" count(*) OVER() TOTALROWS ");
		if (!requestBean.isPrint()) {
			query.append("  , ROW_NUMBER () OVER ( ");
			query.append(" ORDER BY ");
			query.append(sortColumn).append(" ").append(sortOrder);
			query.append(" ) AS RNK");
		}
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" ORDER_TAB ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(
				" ORDER_ATTRIBUTE ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append("	 AND ORDER_TAB.ORDER_PLACED_DTTM=ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(
				" ORDER_EXCEPTION ");
		query.append(" ON ORDER_EXCEPTION.SYS_ORDER_ID = ORDER_TAB.SYS_ORDER_ID ");
		query.append(" AND ORDER_EXCEPTION.ORDER_PLACED_DTTM=ORDER_TAB.ORDER_PLACED_DTTM ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE).append(
				" EXCEPTION_TYPE ");
		query.append("ON ORDER_EXCEPTION.SYS_EXCEPTION_TYPE_ID = EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ");
		query.append("ON OM_LOCATION.SYS_LOCATION_ID = ORDER_EXCEPTION.SYS_LOCATION_ID ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(
				" USER_ATTRIBUTE ");
		query.append(" ON USER_ATTRIBUTE.EMPLOYEE_ID     = ORDER_EXCEPTION.CREATE_USER_ID ");
		query.append(" WHERE ");
		query.append(" OM_LOCATION.ACTIVE_CD = 1 AND "); // MANDATORY CONDITION
															// FOR ACTIVE_CD
		query.append(" OM_LOCATION.LOCATION_NBR = ? AND ");
		query.append(" ORDER_EXCEPTION.CREATE_DTTM BETWEEN TO_DATE(? || '00:00:00','DD-MM-YYYY HH24:MI:SS') AND TO_DATE(? || '23:59:59','DD-MM-YYYY HH24:MI:SS') ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM           > TO_DATE(? || '00:00:00' ,'DD-MM-YYYY HH24:MI:SS') - 195 ");

		return query;

	}

	public static StringBuilder getorderByQuery(
			ExceptionByEnvelopeFilter requestBean) {
		StringBuilder query = new StringBuilder();
		String sortColumn = requestBean.getSortColumnName();
		String sortOrder = requestBean.getSortOrder();
		query.append(" ORDER BY ");
		query.append(sortColumn).append(" ").append(sortOrder);
		query.append(" )");
		return query;
	}

	public static StringBuilder getCustomerNameCheck() {
		StringBuilder query = new StringBuilder();
		query.append(" AND ORDER_TAB.CUSTOMER_LAST_NAME = ? ");
		return query;
	}

	public static StringBuilder getEmployeeNameCheck() {
		StringBuilder query = new StringBuilder();
		query.append("AND USER_ATTRIBUTE.LAST_NAME = ? ");
		return query;
	}

	public static StringBuilder getEnvNumberCheck() {
		StringBuilder query = new StringBuilder();
		query.append(" AND ORDER_ATTRIBUTE.ENVELOPE_NUMBER = ? ");
		return query;
	}

	public static StringBuilder getEnvEnteredDttmCheck() {
		StringBuilder query = new StringBuilder();
		query.append(" AND TRUNC(ORDER_TAB.ORDER_PLACED_DTTM) = TO_DATE(? ,'DD-MM-YYYY') ");
		return query;
	}

	public static StringBuilder getExceptionStatusCheck() {
		StringBuilder query = new StringBuilder();
		query.append(" AND ORDER_EXCEPTION.STATUS = ? ");
		return query;
	}

	public static StringBuilder getReasonTypeCheck() {
		StringBuilder query = new StringBuilder();
		query.append(" AND TRIM(UPPER(EXCEPTION_TYPE.EXCEPTION_TYPE)) = TRIM(UPPER(?))");
		return query;
	}

	public static StringBuilder getPaginationQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" ) WHERE RNK  BETWEEN ? AND ?");
		return query;
	}

	public static StringBuilder getEnvelopeHistryDtls() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT * FROM (SELECT TO_CHAR(ORDER_HISTORY.ORDER_ACTION_DTTM, '");
		query.append(PhotoOmniConstants.DATE_FORMAT_FORTH).append("'");
		query.append(" ) AS ORDER_ACTION_DTTM, ORDER_HISTORY.ORDER_ACTION_CD AS ACTION, ");
		query.append(" USER_ATTRIBUTE.FIRST_NAME AS FIRST_NAME, ");
		query.append(" USER_ATTRIBUTE.LAST_NAME AS LAST_NAME, ");
		query.append(" EXCEPTION_TYPE.REASON AS REASON, ");
		query.append(" ORDER_HISTORY.ORDER_ACTION_NOTES AS COMMENTS ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_HISTORY).append(
				" ORDER_HISTORY ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(
				" ORDER_EXCEPTION ");
		query.append(" ON ORDER_HISTORY.SYS_ORDER_ID = ORDER_EXCEPTION.SYS_ORDER_ID ");
		/*
		  * changes made in query start
		  */
		query.append(" AND ORDER_HISTORY.ORDER_PLACED_DTTM = ORDER_EXCEPTION.ORDER_PLACED_DTTM ");
		/*
		  * changes made in query END
		  */
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE).append(
				" EXCEPTION_TYPE ");
		query.append(" ON ORDER_EXCEPTION.SYS_EXCEPTION_TYPE_ID = EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(
				" ORDER_ATTRIBUTE ");
		query.append(" ON ORDER_EXCEPTION.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		/*
		  * changes made in query start
		  */
		query.append(" AND ORDER_EXCEPTION.ORDER_PLACED_DTTM = ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(
				" ORDER_TAB ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM = ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		/*
		  * changes made in query END
		  */
		/*
		 * QUERY CHANGES FROM ONSITE START
		 */
		query.append(" LEFT JOIN ");
		 query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" USER_ATTRIBUTE ");
		 query.append("  ON ORDER_EXCEPTION.CREATE_USER_ID       = USER_ATTRIBUTE.employee_id ");
		/*query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_USER).append(" USER_TAB ");
		query.append(" ON ORDER_ATTRIBUTE.LABELED_EMPLOYEE_ID = USER_TAB.SYS_USER_ID ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(
				" USER_ATTRIBUTE ");
		query.append(" ON USER_TAB. SYS_USER_ID = USER_ATTRIBUTE. SYS_USER_ID ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(
				" ORDER_TAB ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");*/
		  /*
		 * QUERY CHANGES FROM ONSITE START
		 */
		query.append(" WHERE ORDER_ATTRIBUTE.ENVELOPE_NUMBER = ? ");
		query.append(" AND TRUNC(ORDER_TAB.ORDER_PLACED_DTTM) = TO_DATE(? ,'DD-MM-YYYY') ");
		query.append("	AND ORDER_TAB.ORDER_PLACED_DTTM            > TO_DATE(? ,'DD-MM-YYYY') - 195 ");
		query.append(" ORDER BY ORDER_HISTORY.ORDER_ACTION_DTTM )");
		return query;
	}

	public static StringBuilder getProductDescription() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT * ");
		query.append(" FROM ");
		query.append(" (SELECT ");
		query.append("   NVL ( ORDER_TAB.ORDER_DESCRIPTION, '--' )      AS ORDER_DESC,");
		query.append("    (ORDER_TAB.FINAL_PRICE - ORDER_TAB.TOTAL_ORDER_DISCOUNT ) AS FINAL_PRICE, ");
		query.append("    ORDER_TAB.SYS_ORDER_ID AS ORDER_ID, ");
		query.append("    PRODUCT_TAB.DESCRIPTION                                                             AS PRODUCT_DESC, ");
		query.append("    ORDER_LINE_TAB.QUANTITY                                                             AS QUANTITY, ");
		/*
		  * changes made in query start
		  */
		query.append(" ORDER_LINE_TAB.FINAL_PRICE as ORDER_LINE_PRICE, ");
		
		//query.append("    TO_CHAR((ORDER_LINE_TAB.FINAL_PRICE / ORDER_LINE_TAB.QUANTITY), '9999999999990.90') AS PER_UNIT_PRICE, ");
		/*
		  * changes made in query END
		  */
		query.append("    ORDER_LINE_PLU_TAB.PLU_DISCOUNT_AMOUNT                                              AS DISCOUNT_LINE_THREE_AMT, ");
		query.append("     NVL( PROMOTION_TAB.PROMOTION_DESC, '--')   AS DESC_LINE_THREE ");
		 query.append(" FROM  ");
		 query.append(PhotoOmniDBConstants.OM_ORDER).append(" ORDER_TAB ");
		 query.append(" INNER JOIN ");
		 query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" ORDER_ATTRIBUTE ");
		query.append("  ON ORDER_TAB.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM=ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append("  INNER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE).append(" ORDER_LINE_TAB ");
		 query.append(" ON ORDER_LINE_TAB.SYS_ORDER_ID = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		 /*
		  * changes made in query start
		  */
		 query.append(" AND ORDER_LINE_TAB.ORDER_PLACED_DTTM = ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		 /*
		  * changes made in query end
		  */
		 query.append("INNER JOIN ");
		 query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(" ORDER_EXCEPTION_TAB ");
		query.append(" ON ORDER_EXCEPTION_TAB.SYS_ORDER_ID = ORDER_TAB.SYS_ORDER_ID ");
		query.append("  AND ORDER_EXCEPTION_TAB.ORDER_PLACED_DTTM = ORDER_TAB.ORDER_PLACED_DTTM ");
		query.append("  INNER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT).append(" PRODUCT_TAB ");
		 query.append(" ON PRODUCT_TAB.SYS_PRODUCT_ID = ORDER_LINE_TAB.SYS_PRODUCT_ID ");
		 query.append(" LEFT OUTER JOIN ");
		  query.append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU).append(" ORDER_LINE_PLU_TAB ");
		 query.append(" ON ORDER_LINE_PLU_TAB.SYS_ORDER_LINE_ID = ORDER_LINE_TAB.SYS_ORDER_LINE_ID ");
		 /*
		  * changes made in query start
		  */
		 query.append(" AND ORDER_LINE_PLU_TAB.ORDER_PLACED_DTTM = ORDER_LINE_TAB.ORDER_PLACED_DTTM");
		 /*
		  * changes made in query end
		  */
		query.append("  LEFT OUTER JOIN ");
		query.append(PhotoOmniDBConstants.OM_PROMOTION).append("  PROMOTION_TAB "); 
		query.append("  ON PROMOTION_TAB.SYS_PLU_ID            = ORDER_LINE_PLU_TAB.SYS_PLU_ID ");
		query.append("  WHERE ORDER_ATTRIBUTE.ENVELOPE_NUMBER  = ? ");
		query.append("  AND TRUNC(ORDER_EXCEPTION_TAB.CREATE_DTTM) = TO_DATE(? ,'DD-MM-YYYY') ");
		/*
		  * changes made in query start
		  */
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM         > TO_DATE(? ,'DD-MM-YYYY') - 195 ");
		/*
		  * changes made in query end
		  */
		query.append("   ) "); 

		/*query.append(" SELECT DISTINCT * FROM ( SELECT ");
		query.append(" CASE ");
		query.append(" WHEN ORDER_TAB.ORDER_DESCRIPTION IS NULL ");
		query.append(" THEN '-' ");
		query.append("  ELSE ORDER_TAB.ORDER_DESCRIPTION ");
		query.append(" END AS ORDER_DESC, ");
		query.append(" (ORDER_TAB.FINAL_PRICE  - ORDER_TAB.TOTAL_ORDER_DISCOUNT )         AS FINAL_PRICE, ");
		query.append(" '$' ");
		query.append("  || TO_CHAR((ORDER_TAB.FINAL_PRICE), '9999999999990.90') AS FINAL_PRICE, ");
		query.append(" ORDER_ATTRIBUTE.SYS_ORDER_ID                                 AS ORDER_ID, ");
		query.append("  PRODUCT_TAB.DESCRIPTION                                      AS PRODUCT_DESC, ");
		query.append("  ORDER_LINE_TAB.QUANTITY                                      AS QUANTITY, ");
		//query.append(" '$' ");
		query.append("   TO_CHAR((ORDER_LINE_TAB.FINAL_PRICE /  ORDER_LINE_TAB.QUANTITY), '9999999999990.90') AS PER_UNIT_PRICE, ");
		//query.append("  (ORDER_LINE_TAB.FINAL_PRICE /  ORDER_LINE_TAB.QUANTITY)                                AS PER_UNIT_PRICE, ");
		query.append("  ORDER_LINE_TAB.SYS_ORDER_LINE_ID                             AS ORDER_LINE_ID, ");
		query.append("  PROMOTION_DESC, ");
		query.append("  CASE ");
		query.append("  WHEN (SELECT COUNT(SYS_PLU_ID) ");
		query.append("  FROM OM_SHOPPING_CART_PLU OSCP ");
		query.append("  WHERE OSCP.SYS_SHOPPING_CART_ID = ORDER_ATTRIBUTE.SYS_SHOPPING_CART_ID ");
		query.append("  AND OSCP.ORDER_PLACED_DTTM      =ORDER_ATTRIBUTE.ORDER_PLACED_DTTM) > 0 ");
		query.append("  THEN 0 ");
		query.append("  ELSE PLU_DISCOUNT_AMOUNT ");
		query.append("  END AS DISCOUNT_AMOUNT ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE).append(" ORDER_LINE_TAB ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT).append(" PRODUCT_TAB ");
		query.append(" ON ORDER_LINE_TAB.SYS_PRODUCT_ID = PRODUCT_TAB.SYS_PRODUCT_ID");
		query.append(" LEFT OUTER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU).append(" orl ");
		query.append(" ON ORDER_LINE_TAB.SYS_ORDER_LINE_ID =orl.SYS_ORDER_LINE_ID");
		query.append(" AND ORDER_LINE_TAB.ORDER_PLACED_DTTM=orl.ORDER_PLACED_DTTM ");
		query.append(" LEFT OUTER JOIN ");
		query.append(PhotoOmniDBConstants.OM_PROMOTION).append("  PROMOTION ");
		query.append("  ON orl.SYS_PLU_ID = PROMOTION.SYS_PLU_ID ");
		query.append(" LEFT OUTER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" ORDER_ATTRIBUTE ");
		query.append(" ON ORDER_LINE_TAB.SYS_ORDER_ID        = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND ORDER_LINE_TAB.ORDER_PLACED_DTTM  =ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" LEFT OUTER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" ORDER_TAB ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID         = ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM   =ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" WHERE ORDER_ATTRIBUTE.ENVELOPE_NUMBER = ?  AND TRUNC(ORDER_TAB.ORDER_PLACED_DTTM) = TO_DATE(? ,'DD-MM-YYYY') )");
*/
		return query;
	}

	public static StringBuilder getPromotionLineFour() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT * ");
		query.append(" FROM ");
		query.append(" ( SELECT ORDER_PLU_TAB.PLU_DISCOUNT_AMOUNT AS LINE_FOUR_DISCOUNT_AMT, ");
		query.append("  PROMOTION_TAB.PROMOTION_DESC           AS LINE_FOUR_DESC ");
		query.append("FROM  ");
		query.append(PhotoOmniDBConstants.OM_ORDER_PLU).append(" ORDER_PLU_TAB ");
		query.append("JOIN "); 
		query.append(PhotoOmniDBConstants.OM_PROMOTION).append(" PROMOTION_TAB ");
		query.append(" ON ORDER_PLU_TAB.SYS_PLU_ID = PROMOTION_TAB.SYS_PLU_ID ");
		query.append(" WHERE SYS_ORDER_ID          = ?) ");
		return query;
	}

	public static StringBuilder getExceptionReason() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT (OM_EXCEPTION_TYPE.EXCEPTION_TYPE) AS EXCEPTION_REASON FROM ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE).append(
				" OM_EXCEPTION_TYPE ");
		query.append(" WHERE OM_EXCEPTION_TYPE.EXCEPTION_TYPE IS NOT NULL ORDER BY EXCEPTION_REASON");
		return query;
	}

	public static StringBuilder getPopupHeaderQueryForSendOutOrders() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT * FROM (SELECT VENDOR_TAB. DESCRIPTION AS VENDOR_NAME, ");
		query.append(" ORDER_TAB.SYS_SRC_VENDOR_ID, ");
		query.append(" VENDOR_TAB.SYS_VENDOR_ID, ");
		query.append(" VENDOR_TAB. AREA_CODE         AS AREA_CODE, ");
		query.append("	 '(' || VENDOR_TAB.AREA_CODE || ')'  ||  VENDOR_TAB.PHONE_NBR AS VENDOR_PHONE, ");
		query.append("  SHIPMENT_TAB. SHIPMENT_STATUS AS SHIPMENT_STATUS, ");
		query.append("	  SHIPMENT_TAB. SHIPMENT_URL    AS VENDOR_TRACKING_SITE,");
		//query.append("	  ORDER_TAB.SYS_FULFILLMENT_VENDOR_ID ");
		query.append(" ORDER_TAB.SRC_VENDOR_ORDER_NBR as WEB_ID, ");
		query.append("  ORDER_TAB. SRC_KIOSK_ORDER_NBR AS KIOSK_ID ");
		query.append("	FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" ORDER_ATTRIBUTE_TAB ");
		query.append(" JOIN  ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" ORDER_TAB ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID      = ORDER_ATTRIBUTE_TAB.SYS_ORDER_ID ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM = ORDER_ATTRIBUTE_TAB.ORDER_PLACED_DTTM ");
		query.append(" LEFT OUTER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_VENDOR).append(" VENDOR_TAB ");
		query.append(" ON ( ORDER_TAB.SYS_SRC_VENDOR_ID = VENDOR_TAB.SYS_VENDOR_ID ");
		query.append(" OR ORDER_TAB.SYS_FULFILLMENT_VENDOR_ID = VENDOR_TAB.SYS_VENDOR_ID ) ");
		query.append(" JOIN OM_ORDER_EXCEPTION ORDER_EXCEPTION_TAB ");
		query.append(" ON ORDER_EXCEPTION_TAB.SYS_ORDER_ID = ORDER_TAB.SYS_ORDER_ID ");
		query.append(" LEFT OUTER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_SHIPMENT).append(" SHIPMENT_TAB ");
		query.append(" ON SHIPMENT_TAB.SYS_ORDER_ID              = ORDER_TAB.SYS_ORDER_ID ");
		query.append(" WHERE ORDER_ATTRIBUTE_TAB.ENVELOPE_NUMBER = ? ");
		query.append(" AND TRUNC(ORDER_TAB.ORDER_PLACED_DTTM)    = TO_DATE(? ,'DD-MM-YYYY')");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM         > TO_DATE(? ,'DD-MM-YYYY') - 195) ");
		return query;
	}
	/*SELECT DISTINCT * FROM (SELECT VENDOR_TAB. DESCRIPTION AS VENDOR_NAME,
			  ORDER_TAB.SYS_SRC_VENDOR_ID,
			  VENDOR_TAB.SYS_VENDOR_ID,
			  VENDOR_TAB. AREA_CODE          AS AREA_CODE,
			  VENDOR_TAB. PHONE_NBR          AS VENDOR_PHONE,
			  SHIPMENT_TAB. SHIPMENT_STATUS  AS SHIPMENT_STATUS,
			  SHIPMENT_TAB. SHIPMENT_URL     AS VENDOR_TRACKING_SITE,
			  ORDER_TAB.SRC_VENDOR_ORDER_NBR AS WEB_ID,
			  ORDER_TAB. SRC_KIOSK_ORDER_NBR AS KIOSK_ID
			FROM OM_ORDER_ATTRIBUTE ORDER_ATTRIBUTE_TAB
			JOIN OM_ORDER ORDER_TAB
			ON ORDER_TAB.SYS_ORDER_ID      = ORDER_ATTRIBUTE_TAB.SYS_ORDER_ID
			LEFT OUTER JOIN OM_VENDOR VENDOR_TAB
			ON ORDER_TAB.SYS_SRC_VENDOR_ID = VENDOR_TAB.SYS_VENDOR_ID
			JOIN OM_ORDER_EXCEPTION ORDER_EXCEPTION_TAB
			ON ORDER_EXCEPTION_TAB.SYS_ORDER_ID = ORDER_TAB.SYS_ORDER_ID
			LEFT OUTER JOIN OM_SHIPMENT SHIPMENT_TAB
			ON SHIPMENT_TAB.SYS_ORDER_ID              = ORDER_TAB.SYS_ORDER_ID
			WHERE ORDER_ATTRIBUTE_TAB.ENVELOPE_NUMBER = 388902
			AND TRUNC(ORDER_EXCEPTION_TAB.CREATE_DTTM)    = TO_DATE('06-07-2015' ,'DD-MM-YYYY'))*/

	public static StringBuilder getOrderDescriptionQry() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT * FROM ( SELECT ORDER_TAB.Order_Origin_Type ");
		query.append("  ||'(' ");
		query.append("	||ORDER_TAB.Order_Description");
		query.append("  ||')' AS ORDER_DESC, ");
		query.append(" ORDER_TAB.Final_Price  as FINAL_PRICE ");
		query.append("	FROM Om_Order ORDER_TAB ");
		query.append(" WHERE  ");
		query.append("	ORDER_TAB.Sys_Order_Id = ? ");
		query.append(" AND TRUNC(ORDER_TAB.order_placed_dttm) = TO_DATE(? ,'DD-MM-YYYY')) ");
		return query;
	}

	public static StringBuilder getOrderLineDescQry() {
		StringBuilder query = new StringBuilder();

		query.append(" SELECT DISTINCT * FROM (	SELECT ORDER_LINE_TAB.sys_order_line_id as ORDER_LINE_ID, ");
		query.append("	ORDER_TAB.Order_Origin_Type ");
		query.append("	||'(' ");
		query.append("	||PRODUCT_TAB.DESCRIPTION ");
		query.append("	||')' AS PRODUCT_DESC ,  ORDER_LINE_TAB.QUANTITY  AS ORDER_LINE_QUANTITY, ");
		query.append("	 ORDER_LINE_TAB.FINAL_PRICE AS ORDER_LINE_PRICE ");
		query.append("	FROM Om_Order ORDER_TAB, ");
		query.append("	 Om_Order_Line ORDER_LINE_TAB, ");
		query.append("	  Om_Product PRODUCT_TAB ");
		query.append("	WHERE ORDER_TAB.Sys_Order_Id    = ORDER_LINE_TAB.Sys_Order_Id ");
		query.append("	AND ORDER_TAB.Order_Placed_Dttm = ORDER_LINE_TAB.Order_Placed_Dttm ");
		query.append("	AND ORDER_LINE_TAB.Sys_Product_Id    = PRODUCT_TAB.Sys_Product_Id ");
		query.append("	AND ORDER_TAB.Sys_Order_Id      = ? ");
		query.append("	AND TRUNC(ORDER_TAB.order_placed_dttm)  = TO_DATE(?,'DD-MM-YYYY') ");
		query.append("	ORDER BY ORDER_LINE_TAB.sys_order_line_id) ");
		return query;
	}

	public static StringBuilder getOrderDiscountQry() {
		StringBuilder query = new StringBuilder();
		query.append("	SELECT DISTINCT * FROM ( ");
		query.append("	SELECT PROMOTION_TAB.Promotion_Desc as ORDER_PROMOTION_DESC, ");
		query.append(" ORDER_PLU_TAB.Plu_Discount_Amount AS ORDER_PROMOTION_AMT ");
		query.append("	FROM Om_Order ORDER_TAB, ");
		query.append(" Om_Order_Plu ORDER_PLU_TAB, ");
		query.append("	 Om_Promotion PROMOTION_TAB ");
		query.append("	WHERE ORDER_TAB.Sys_Order_Id    = ORDER_PLU_TAB.Sys_Order_Id ");
		query.append("	AND ORDER_TAB.Order_Placed_Dttm = ORDER_PLU_TAB.Order_Placed_Dttm ");
		query.append("	AND ORDER_PLU_TAB.sys_plu_id    = PROMOTION_TAB.sys_plu_id ");
		query.append("	AND ORDER_TAB.Sys_Order_Id      =? ");
		query.append("	AND TRUNC(ORDER_TAB.order_placed_dttm) = TO_DATE(? ,'DD-MM-YYYY')) ");
		return query;
	}

	public static StringBuilder getOrderLineDiscountQry() {
		StringBuilder query = new StringBuilder();
		query.append("	SELECT DISTINCT * ");
		query.append("	FROM ");
		query.append("	  (SELECT ORDER_LINE_TAB.Sys_Order_Line_Id AS ORDER_LINE_ID, ");
		query.append("	  PROMOTION_TAB.Promotion_Desc AS ORDER_LINE_PROMOTION_DESC, ");
		query.append("	   ORDER_LINE_PLU_TAB.Plu_Discount_Amount AS ORDER_LINE_PROMOTION_AMT ");
		query.append("	 FROM Om_Order_Line ORDER_LINE_TAB ");
		query.append("	 INNER JOIN Om_Order_Line_Plu ORDER_LINE_PLU_TAB ");
		query.append("	 ON ORDER_LINE_TAB.Sys_Order_line_Id  = ORDER_LINE_PLU_TAB.Sys_Order_line_Id ");
		query.append("	 AND ORDER_LINE_TAB.Order_Placed_Dttm = ORDER_LINE_PLU_TAB.Order_Placed_Dttm ");
		query.append("	  INNER JOIN Om_Promotion PROMOTION_TAB ");
		query.append("	 ON ORDER_LINE_PLU_TAB.sys_plu_id            = PROMOTION_TAB.sys_plu_id ");
		query.append("	WHERE ORDER_LINE_TAB.Sys_Order_Id           = ? ");
		query.append("	 AND TRUNC(ORDER_LINE_TAB.order_placed_dttm) = TO_DATE(? ,'DD-MM-YYYY') ");
		query.append("	  ) ");
		query.append(" ORDER BY ORDER_LINE_ID ");
		return query;
	}
}
