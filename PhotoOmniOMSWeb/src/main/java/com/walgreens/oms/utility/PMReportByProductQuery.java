package com.walgreens.oms.utility;

import com.walgreens.oms.bean.PMByProductFilter;

public class PMReportByProductQuery {
	/*
	 * Query to select PM details from OM_ORDER_PM table
	 * 
	 * @return query
	 */
			public static String selectPMByProductDetails(PMByProductFilter requestBean) {
				
				StringBuilder query = new StringBuilder();
				String sortColumn = requestBean.getSortColumnName();
				String sortOrder = requestBean.getSortOrder();
				query.append("SELECT ");
				query.append(" K.* ");
				query.append(" FROM (SELECT ORDER_PM.SYS_PRODUCT_ID AS PROD_ID, ");
				query.append("  PRODUCT.DESCRIPTION          AS DESCRIPTION, ");
				query.append("SUM(  ORDER_PM.PENDING_AMOUNT )  AS PENDING_AMOUNT, ");
				query.append("SUM(   ORDER_PM.EARNED_AMOUNT )   AS EARNED_AMOUNT,");
				query.append(" SUM(  ORDER_PM.POTENTIAL_AMOUNT) AS POTENTIAL_AMOUNT, "); 
				query.append(" SUM(  ORDER_PM.POTENTIAL_QTY  )  AS POTENTIAL_QTY, ");
				query.append(" SUM(  ORDER_PM.EARNED_QTY  )     AS EARNED_QTY, ");
				query.append(" SUM( ORDER_PM.PENDING_QTY  )    AS PENDING_QTY, "); 
				query.append(" USER_TAB.LAST_NAME as LAST_NAME, ");
				query.append(" USER_TAB.FIRST_NAME as FIRST_NAME ");
				query.append(" FROM OM_ORDER ORDER_TAB, ");
				query.append(" OM_ORDER_PM ORDER_PM, ");
				query.append(" OM_LOCATION LOCATION_TAB, ");
				query.append(" OM_USER_ATTRIBUTES USER_TAB, ");
				query.append(" OM_PRODUCT PRODUCT ");
				query.append(" WHERE ORDER_TAB.SYS_ORDER_ID = ORDER_PM.SYS_ORDER_ID ");
				query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM  = ORDER_PM.ORDER_PLACED_DTTM ");
				query.append(" AND ORDER_TAB.SYS_OWNING_LOC_ID  = LOCATION_TAB.SYS_LOCATION_ID ");
				query.append(" AND ORDER_PM.SYS_EMPLOYEE_ID = USER_TAB.SYS_USER_ID ");
				query.append(" AND ORDER_PM.SYS_PRODUCT_ID = PRODUCT.SYS_PRODUCT_ID "); 
				query.append(" AND ORDER_PM.SYS_PRODUCT_ID     <> 0 ");
				query.append(" AND LOCATION_TAB.LOCATION_NBR  = ? ");
				query.append(" AND USER_TAB.EMPLOYEE_ID  = ? ");
				query.append(" AND TRUNC(ORDER_TAB.ORDER_SOLD_DTTM) BETWEEN TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') AND TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') ");
				query.append(" AND ORDER_PM.ACTIVE_CD = 1 ");
				query.append(" AND ORDER_PM.ORDER_PLACED_DTTM > TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') - 195 ");
				query.append(" GROUP BY  (ORDER_PM.SYS_PRODUCT_ID,PRODUCT.DESCRIPTION,USER_TAB.LAST_NAME,USER_TAB.FIRST_NAME ) ");
				query.append(" UNION ");
				query.append(" SELECT ORDER_PM.SYS_PRODUCT_ID AS PROD_ID, ");
				query.append("  OM_PM.PM_RULE_DESC           AS DESCRIPTION, ");
				query.append("SUM(  ORDER_PM.PENDING_AMOUNT )  AS PENDING_AMOUNT, ");
				query.append("SUM(   ORDER_PM.EARNED_AMOUNT )   AS EARNED_AMOUNT,");
				query.append(" SUM(  ORDER_PM.POTENTIAL_AMOUNT) AS POTENTIAL_AMOUNT, "); 
				query.append(" SUM(  ORDER_PM.POTENTIAL_QTY  )  AS POTENTIAL_QTY, ");
				query.append(" SUM(  ORDER_PM.EARNED_QTY  )     AS EARNED_QTY, ");
				query.append(" SUM( ORDER_PM.PENDING_QTY  )    AS PENDING_QTY, ");
				query.append(" USER_TAB.LAST_NAME as LAST_NAME, ");
				query.append(" USER_TAB.FIRST_NAME as FIRST_NAME ");
				query.append(" FROM OM_ORDER ORDER_TAB, ");
				query.append(" OM_ORDER_PM ORDER_PM, ");
				query.append(" OM_USER_ATTRIBUTES USER_TAB, ");
				query.append(" OM_LOCATION LOCATION_TAB, ");
				query.append(" OM_PROMOTIONAL_MONEY OM_PM ");
				query.append(" WHERE ORDER_TAB.SYS_ORDER_ID = ORDER_PM.SYS_ORDER_ID ");
				query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM  = ORDER_PM.ORDER_PLACED_DTTM ");
				query.append(" AND ORDER_TAB.SYS_OWNING_LOC_ID  = LOCATION_TAB.SYS_LOCATION_ID ");
				query.append(" AND ORDER_PM.SYS_EMPLOYEE_ID = USER_TAB.SYS_USER_ID ");
				query.append(" AND OM_PM.SYS_PM_ID = ORDER_PM.SYS_PM_ID ");
				query.append(" AND OM_PM.TYPE             = 'MBPM' ");
				query.append(" AND LOCATION_TAB.LOCATION_NBR  = ? ");
				query.append(" AND USER_TAB.EMPLOYEE_ID  = ? ");
				query.append(" AND TRUNC(ORDER_TAB.ORDER_SOLD_DTTM) BETWEEN TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') AND TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') ");
				query.append(" AND ORDER_PM.ACTIVE_CD = 1 ");
				query.append(" AND ORDER_PM.ORDER_PLACED_DTTM > TO_DATE(? ,'DD-MM-YYYY HH24:MI:SS') - 195 ");
				query.append(" GROUP BY  (ORDER_PM.SYS_PRODUCT_ID,OM_PM.PM_RULE_DESC,USER_TAB.LAST_NAME,USER_TAB.FIRST_NAME  ) ");
				query.append(" ) K ");
				query.append(" ORDER BY ");
				query.append(sortColumn).append(" ").append(sortOrder);
				return query.toString();
			}

}
