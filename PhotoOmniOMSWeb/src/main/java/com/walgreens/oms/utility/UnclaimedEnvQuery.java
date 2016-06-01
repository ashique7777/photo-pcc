/**
 * UnclaimedEnvQuery.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		27 April 2015
 *  
 **/
package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;

/**
 * This class is used to create Query for Unclaimed envelope Reports.
 * @author CTS
 * @version 1.1 April 27, 2015
 */

public class UnclaimedEnvQuery {
	
	
	/**
     * This method create the select query for OM_ORDER_ATTRIBUTE,OM_ORDER,OM_LOCATION  table
     * @return query
     */
	public static StringBuilder selectUnclaimedEnvdataQry(UnclaimedEnvFilter reqBean) {
		StringBuilder query = new StringBuilder();
		String sortColumn = reqBean.getSortColumnName();
		String sortOrder = reqBean.getSortOrder();
		String currentPage = reqBean.getCurrentPageNo();
		if (!CommonUtil.isNull(currentPage) && !"".equals(currentPage)) {
			query.append(" SELECT * FROM (SELECT T.* , ROWNUM AS RNK FROM ( ");
		}
		query.append(" SELECT SYS_CUSTOMER_ID   AS CUSTOMERID,(INITCAP(CUSTOMER_LAST_NAME) ||' ' || INITCAP(CUSTOMER_FIRST_NAME)) AS CUSTOMERNAME, ");
		query.append(" ('('|| OM_ORDER.AREA_CODE ||') ' || SUBSTR(PHONE_NBR, 0, 3 ) || '-' || SUBSTR(PHONE_NBR, 4, length(PHONE_NBR))) AS PHONE,");
		query.append(" COUNT(OM_ORDER.SYS_ORDER_ID)  AS TOTALENVELOPES, '$' ||  TO_CHAR(SUM(OM_ORDER.FINAL_PRICE), '9999999999990.90') AS TOTALVALUE ,");
		query.append(" TO_CHAR(MAX(OM_ORDER.ORDER_PLACED_DTTM), 'YYYY-MM-DD') || ' - ' || TO_CHAR(MIN(OM_ORDER.ORDER_PLACED_DTTM), 'YYYY-MM-DD')  AS ORDERDATERANGE ,");
		query.append(" COUNT(*) OVER ()  AS TOTALROWS FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER ).append(" OM_ORDER ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE ).append(" OM_ORDER_ATTRIBUTE ");
		query.append(" ON OM_ORDER.SYS_ORDER_ID = OM_ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION ).append(" OM_LOCATION ");
		query.append(" ON OM_LOCATION.SYS_LOCATION_ID = OM_ORDER.SYS_OWNING_LOC_ID");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1 AND STATUS ='DONE' AND ");
		query.append(" OM_ORDER.ORDER_PLACED_DTTM BETWEEN (SYSDATE-195) AND SYSDATE AND LOCATION_NBR = ? ");
		query.append(" GROUP BY SYS_CUSTOMER_ID, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, OM_ORDER.AREA_CODE, PHONE_NBR ");
		query.append(" ORDER BY  ");
		query.append(sortColumn).append(" ").append(sortOrder);
		if (!CommonUtil.isNull(currentPage) && !"".equals(currentPage)) {
			query.append(" ) T ) WHERE RNK BETWEEN ? AND ? ");
		}
		return query;
	}
	
	/**
	 * This method create the sql query for last six month(195 Days) orders for selected customer.
	 * @return query
	 */
	public static StringBuilder selectUnclaimedEnvcustOrderQry() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT ENVELOPE_NUMBER AS ENVELOPE_NUMBER,PROCESSING_TYPE_CD AS PROCESSING_TYPE_CD,ORDER_DESCRIPTION AS ORDER_DESCRIPTION, STATUS AS STATUS,");
		query.append(" TO_CHAR(PROMISE_DELIVERY_DTTM ,'mm/dd/yy hh:mi am') AS PROMISE_DELIVERY_DTTM, EXCEPTION_CD AS EXCEPTION_CD, ");
		query.append(" TO_CHAR(OM_ORDER.ORDER_PLACED_DTTM ,'mm/dd/yy hh:mi am') AS ORDER_PLACED_DTTM,");
		query.append(" TO_CHAR(ORDER_COMPLETED_DTTM,'mm/dd/yy hh:mi am') AS ORDER_COMPLETED_DTTM, ");
		query.append(" TO_CHAR(ORDER_SOLD_DTTM,'mm/dd/yy hh:mi am') AS ORDER_SOLD_DTTM, OM_ORDER.SYS_ORDER_ID AS SYS_ORDER_ID, COUNT(OM_ORDER.SYS_ORDER_ID) AS TOTALENVELOPES,");
		query.append(" (INITCAP(CUSTOMER_LAST_NAME) || ' ' || INITCAP(CUSTOMER_FIRST_NAME)) AS CUSTOMER_NAME , ");
		query.append(" ('('|| OM_ORDER.AREA_CODE ||') ' || SUBSTR(PHONE_NBR, 0, 3 ) || '-' || SUBSTR(PHONE_NBR, 4, length(PHONE_NBR))) AS PHONE_NBR ");
		query.append( " ,LOCATION_NBR AS LOCATION_NBR FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER ).append(" OM_ORDER ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE ).append(" OM_ORDER_ATTRIBUTE ");
		query.append(" ON OM_ORDER.SYS_ORDER_ID = OM_ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_ATTRIBUTE.ORDER_PLACED_DTTM ");
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION ).append(" OM_LOCATION ");
		query.append(" ON OM_LOCATION.SYS_LOCATION_ID = OM_ORDER.SYS_OWNING_LOC_ID");
		query.append(" WHERE OM_ORDER.STATUS = 'DONE' AND OM_ORDER.ORDER_PLACED_DTTM BETWEEN (SYSDATE-195) AND SYSDATE AND SYS_CUSTOMER_ID =  ? "); 
		query.append(" GROUP BY ENVELOPE_NUMBER, PROCESSING_TYPE_CD, ORDER_DESCRIPTION, STATUS, PROMISE_DELIVERY_DTTM, ");
		query.append(" EXCEPTION_CD, OM_ORDER.ORDER_PLACED_DTTM, ORDER_COMPLETED_DTTM, ");
		query.append(" ORDER_SOLD_DTTM, OM_ORDER.SYS_ORDER_ID, CUSTOMER_LAST_NAME, CUSTOMER_FIRST_NAME, OM_ORDER.AREA_CODE, PHONE_NBR, LOCATION_NBR ");
		return query;
	}
	
}
