package com.walgreens.omni.utility;

/**
 * 
 * @author CTS
 * 
 */
public class DashboardReportQuery {

	/**
	 * 
	 * @return a string query for Unclaimed Revenue Report
	 */
	public static StringBuffer getUnclaimedDashboardQuery() {

		StringBuffer query = new StringBuffer();
		query.append("SELECT 'OM_1_TO_30_DAYS' AS category, \n");
		query.append("  ORDER_ORIGIN_TYPE, \n");
		query.append("  COUNT(1)AS COUNT \n");
		query.append("FROM OM_ORDER \n");
		query.append("WHERE ORDER_PLACED_DTTM BETWEEN (sysdate - 30) AND sysdate \n");
		query.append("AND STATUS             = 'DONE' \n");
		query.append("AND ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') \n");
		query.append("GROUP BY ORDER_ORIGIN_TYPE \n");
		query.append("UNION ALL \n");
		query.append("SELECT 'OM_31_TO_60_DAYS' AS category, \n");
		query.append("  ORDER_ORIGIN_TYPE, \n");
		query.append("  COUNT(1) AS COUNT \n");
		query.append("FROM OM_ORDER \n");
		query.append("WHERE ORDER_PLACED_DTTM BETWEEN (sysdate - 60) AND (sysdate - 31) \n");
		query.append("AND STATUS             = 'DONE' \n");
		query.append("AND ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') \n");
		query.append("GROUP BY ORDER_ORIGIN_TYPE \n");
		query.append("UNION ALL \n");
		query.append("SELECT 'OM_61_TO_90_DAYS' AS category, \n");
		query.append("  ORDER_ORIGIN_TYPE, \n");
		query.append("  COUNT(1) AS COUNT \n");
		query.append("FROM OM_ORDER \n");
		query.append("WHERE ORDER_PLACED_DTTM BETWEEN (sysdate - 90) AND (sysdate - 61) \n");
		query.append("AND STATUS             = 'DONE' \n");
		query.append("AND ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') \n");
		query.append("GROUP BY ORDER_ORIGIN_TYPE");
		return query;
	}

	/**
	 * 
	 * @return a string query for Current Order Revenue report
	 */
	public static StringBuffer getCurrentOderRevenueDashboardQuery() {
		StringBuffer query = new StringBuffer();
		query.append(" WITH QUERY1 AS ");
		query.append(" (SELECT CHANNEL,CASE CHANNEL WHEN 'Kiosk' THEN 5 WHEN 'Internet' THEN 10 WHEN 'Mobile' THEN 15 ELSE 20 END \"SORTORDER2\" , ");
		query.append(" PLACED_ORDER_REVENUE,0 AS SOLD_ORDER_REVENUE FROM ");
		query.append(" (SELECT O.ORDER_ORIGIN_TYPE AS CHANNEL,SUM(O.FINAL_PRICE) AS PLACED_ORDER_REVENUE ");
		query.append(" FROM OM_ORDER O WHERE ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') ");
		query.append(" AND O.ORDER_PLACED_DTTM >= TRUNC(SYSDATE) GROUP BY O.ORDER_ORIGIN_TYPE) ");
		query.append(" UNION ALL ");
		query.append(" SELECT CHANNEL,CASE CHANNEL WHEN 'Kiosk' THEN 5 WHEN 'Internet' THEN 10 WHEN 'Mobile' THEN 15 ELSE 20 END \"SORTORDER2\" , ");
		query.append(" 0 AS PLACED_ORDER_REVENUE,SOLD_ORDER_REVENUE FROM ");
		query.append(" (SELECT O.ORDER_ORIGIN_TYPE AS CHANNEL,SUM(O.SOLD_AMOUNT) AS SOLD_ORDER_REVENUE ");
		query.append(" FROM OM_ORDER O WHERE ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') ");
		query.append(" AND O.ORDER_PLACED_DTTM >= TRUNC(SYSDATE) - 195 AND O.ORDER_SOLD_DTTM >= TRUNC(SYSDATE) ");
		query.append(" GROUP BY O.ORDER_ORIGIN_TYPE)) ");
		query.append(" SELECT CHANNEL ,\"SORTORDER2\",SUM(PLACED_ORDER_REVENUE) AS \"PLACED_ORDER_REVENUE\",SUM(SOLD_ORDER_REVENUE) AS \"SOLD_ORDER_REVENUE\" ");
		query.append(" FROM QUERY1 GROUP BY CHANNEL,\"SORTORDER2\" ORDER BY \"SORTORDER2\",CHANNEL ");
		return query;
	}

	public static StringBuffer getAsOfDateQuery() {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT MAX(O.Order_Placed_Dttm - ((O.Owning_Loc_Tz_Offset + 6)/24)) AS ORDER_PLACED, MAX(O.ORDER_SOLD_DTTM - ((O.OWNING_LOC_TZ_OFFSET + 6)/24)) AS ORDER_SOLD ");
		query.append(" FROM OM_ORDER O WHERE ORDER_ORIGIN_TYPE IN ('Kiosk','Internet','Mobile') AND ");
		query.append(" ((O.Order_Placed_Dttm >= Trunc(Sysdate) And O.Order_Placed_Dttm  <= Trunc(Sysdate+1)) OR ");
		query.append(" (O.ORDER_SOLD_DTTM  >= TRUNC(SYSDATE) AND O.ORDER_SOLD_DTTM  <= TRUNC(SYSDATE+1))) ");
		return query;
	}

}
