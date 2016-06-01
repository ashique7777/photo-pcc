/**
 * MachineReportQuery.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		27 Jan 2015
*  
**/
package com.walgreens.admin.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;


/**
 * This class is used to create Query for MachineDownTime Report.
 * @author CTS
 * @version 1.1 January 27, 2015
 */


public class MachineReportQuery {
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineReportQuery.class);
	
	/**
	 * This method create the SQL select query for Machine type 
	 * @return query
	 */
	public static StringBuilder getActiveMachineType() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_MACHINE_TYPE_ID AS SYS_MACHINE_TYPE_ID , MACHINE_TYPE AS MACHINE_TYPE FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_TYPE);
		query.append(" WHERE ACTIVE_CD=1 AND MACHINE_CAT='MINI'");
		return query;
	}
	
	/**
	 * This method create the SQL select query for Machine type for a specific store.
	 * @return query
	 */
	public static StringBuilder getActiveMachineTypeForSpecStore() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID AS SYS_MACHINE_TYPE_ID , OM_MACHINE_TYPE.MACHINE_TYPE AS MACHINE_TYPE FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_TYPE);
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_INSTANCE);
		query.append(" ON OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID = OM_MACHINE_INSTANCE.SYS_MACHINE_TYPE_ID INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" ON OM_MACHINE_INSTANCE.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID WHERE LOCATION_NBR = ? ");
		query.append(" AND OM_LOCATION.ACTIVE_CD = 1 ");
		return query;
	}
	
	/**
	 * This method create the SQL select query to get store address.
	 * @return query.
	 */
	public static StringBuilder getLocationAddresQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT (LOCATION_NBR ||',' ||NVL(ADDRESS_LINE_1,' ')) AS LOCATIONADDRESS FROM  ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" WHERE LOCATION_NBR = ? ");
		return query;
	}
  
	
	/**
	 * This method creates machine down time report data Query.
	 * @param currentPage contains current page no.
	 * @param storeDataList contains store data.
	 * @return query.
	 * @throws PhotoOmniException
	 */
	public static String getReportData(MachineFilter reqBean) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
    	StringBuilder locationQuery = locationDecider(reqBean);
    	String currentPage = reqBean.getCurrentPageNo();
    	String sortColumn = reqBean.getSortColumnName();
		String sortOrder = reqBean.getSortOrder();
		String machineType = reqBean.getMachineId();
		query.append(" SELECT * FROM (SELECT ID, MACHINE_NAME, EQUIPMENT_NAME, MEDIA_NAME,START_TIME, END_TIME, DURATION, DOWNTIME_REASON_ID, OM_DOWNTIME_REASON.DOWNTIME_REASON,REGION_NBR, "); 
		query.append(" CASE WHEN FIRST_NAME IS NULL AND LAST_NAME IS NULL THEN BEGIN_EMPLOYEE_ID WHEN FIRST_NAME IS NULL AND LAST_NAME  IS NOT NULL ");
		query.append(" THEN LAST_NAME || ',' WHEN LAST_NAME  IS NULL AND FIRST_NAME IS NOT NULL THEN ',' || FIRST_NAME ELSE LAST_NAME ||',' || FIRST_NAME END ENTERED_BY, ");
		query.append(" DIST_NBR, STORE_NUMBER, MACHINE_TYP_ID, ROW_NUMBER () OVER (PARTITION BY STORE_NUMBER ORDER BY REGION_NBR, DIST_NBR, STORE_NUMBER, ");
		query.append(sortColumn).append(" ").append(sortOrder);
		query.append(" ) RN, COUNT(*) OVER (PARTITION BY STORE_NUMBER) "); 
		query.append(" TOTAL_CNT FROM (SELECT SYS_MAC_DWNT_HIST_ID AS ID, MACHINE_DESC AS MACHINE_NAME, '' AS EQUIPMENT_NAME, '' AS MEDIA_NAME, OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID, BEGIN_EMPLOYEE_ID "); 
		query.append(" AS BEGIN_EMPLOYEE_ID, DAILY_DWNT_START AS START_TIME, DAILY_DWNT_END AS END_TIME, DAILY_DURATION AS DURATION, SYS_DOWNTIME_REASON_ID AS DOWNTIME_REASON_ID, NOTES AS DOWNTIME_REASON, "); 
		query.append(" REGION_NBR AS REGION_NBR, DISTRICT_NBR AS DIST_NBR, LOCATION_NBR AS STORE_NUMBER, OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID AS MACHINE_TYP_ID "); 
		query.append(" FROM ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY ).append(" OM_MACHINE_DWNT_HISTORY "); 
		query.append(" INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_INSTANCE ).append(" OM_MACHINE_INSTANCE "); 
		query.append(" ON OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID = OM_MACHINE_DWNT_HISTORY.SYS_MACHINE_INSTANCE_ID INNER JOIN ");  
		query.append(  PhotoOmniDBConstants.OM_MACHINE_TYPE ).append(" OM_MACHINE_TYPE "); 
		query.append(" ON OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID = OM_MACHINE_INSTANCE.SYS_MACHINE_TYPE_ID INNER JOIN  "); 
		query.append(  PhotoOmniDBConstants.OM_LOCATION ).append(" OM_LOCATION "); 
		query.append(" ON OM_MACHINE_INSTANCE.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID ");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1 AND OM_MACHINE_DWNT_HISTORY.DAILY_DURATION <> 0 "); /* AND OM_MACHINE_DWNT_HISTORY.DAILY_DURATION <> 0 : this line added for JIRA 540*/
		query.append(locationQuery);
		query.append(" UNION SELECT SYS_EQUIP_DWNT_HIST_ID AS ID, MACHINE_DESC AS MACHINE_NAME, EQUIPMENT_TYPE_DESC AS EQUIPMENT_NAME, ");
		query.append(" '' AS MEDIA_NAME, SYS_EQUIPIMENT_INSTANCE_ID, BEGIN_EMPLOYEE_ID AS BEGIN_EMPLOYEE_ID, DAILY_DWNT_START AS START_TIME, DAILY_DWNT_END AS END_TIME, "); 
		query.append(" DAILY_DURATION AS DURATION, SYS_DOWNTIME_REASON_ID AS DOWNTIME_REASON_ID, NOTES AS DOWNTIME_REASON, REGION_NBR AS REGION_NBR, DISTRICT_NBR AS DIST_NBR, "); 
		query.append(" LOCATION_NBR AS STORE_NUMBER, OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID AS MACHINE_TYP_ID FROM ");
		query.append(  PhotoOmniDBConstants.OM_EQUIPMENT_DWNT_HISTORY ).append(" OM_EQUIPMENT_DWNT_HISTORY "); 
		query.append(" INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE ).append(" OM_EQUIPMENT_INSTANCE "); 
		query.append(" ON OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_INSTANCE_ID = OM_EQUIPMENT_DWNT_HISTORY.SYS_EQUIPIMENT_INSTANCE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_EQUIPMENT_TYPE ).append(" OM_EQUIPMENT_TYPE "); 
		query.append(" ON OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID = OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_TYPE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_INSTANCE ).append(" OM_MACHINE_INSTANCE "); 
		query.append(" ON OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID = OM_EQUIPMENT_INSTANCE.SYS_MACHINE_INSTANCE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_TYPE ).append(" OM_MACHINE_TYPE ");  
		query.append(" ON OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID = OM_MACHINE_INSTANCE.SYS_MACHINE_TYPE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_LOCATION ).append(" OM_LOCATION ");  
		query.append(" ON OM_MACHINE_INSTANCE.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID  ");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1 AND OM_EQUIPMENT_DWNT_HISTORY.DAILY_DURATION <> 0 "); /* AND OM_EQUIPMENT_DWNT_HISTORY.DAILY_DURATION <> 0 : this line added for JIRA 540*/
		query.append(locationQuery);
		query.append(" UNION SELECT SYS_COMPONENT_DWNT_HIST_ID AS ID, MACHINE_DESC AS MACHINE_NAME, EQUIPMENT_TYPE_DESC AS EQUIPMENT_NAME, COMPONENT_NAME AS MEDIA_NAME, SYS_COMPONENT_ID, "); 
		query.append(" BEGIN_EMPLOYEE_ID AS BEGIN_EMPLOYEE_ID, DAILY_DWNT_START AS START_TIME, DAILY_DWNT_END AS END_TIME, DAILY_DURATION AS DURATION, "); 
		query.append(" SYS_DOWNTIME_REASON_ID AS DOWNTIME_REASON_ID, NOTES AS DOWNTIME_REASON, REGION_NBR AS REGION_NBR, DISTRICT_NBR AS DIST_NBR, LOCATION_NBR AS STORE_NUMBER, "); 
		query.append(" OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID AS MACHINE_TYP_ID FROM ");
		query.append(  PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME_HISTORY).append(" OM_COMPONENT_DOWNTIME_HISTORY "); 
		query.append(" INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_PRODUCT_COMPONENT).append(" OM_PRODUCT_COMPONENT "); 
		query.append(" ON OM_PRODUCT_COMPONENT.SYS_PRODUCT_COMPONENT_ID = OM_COMPONENT_DOWNTIME_HISTORY.SYS_COMPONENT_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE).append(" OM_EQUIPMENT_INSTANCE ");
	    query.append(" ON OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_INSTANCE_ID = OM_COMPONENT_DOWNTIME_HISTORY.SYS_EQUIPIMENT_INSTANCE_ID INNER JOIN ");
	    query.append(  PhotoOmniDBConstants.OM_EQUIPMENT_TYPE ).append(" OM_EQUIPMENT_TYPE "); 
		query.append(" ON OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID = OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_TYPE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_INSTANCE ).append(" OM_MACHINE_INSTANCE "); 
		query.append(" ON OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID = OM_EQUIPMENT_INSTANCE.SYS_MACHINE_INSTANCE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_MACHINE_TYPE ).append(" OM_MACHINE_TYPE "); 
		query.append(" ON OM_MACHINE_TYPE.SYS_MACHINE_TYPE_ID = OM_MACHINE_INSTANCE.SYS_MACHINE_TYPE_ID INNER JOIN ");
		query.append(  PhotoOmniDBConstants.OM_LOCATION ).append(" OM_LOCATION "); 
		query.append(" ON OM_MACHINE_INSTANCE.SYS_LOCATION_ID = OM_LOCATION.SYS_LOCATION_ID ");
		query.append(" WHERE OM_LOCATION.ACTIVE_CD = 1  ");
		query.append(locationQuery);
		query.append(" AND OM_COMPONENT_DOWNTIME_HISTORY.DAILY_DURATION <> 0 ) "); /* AND OM_COMPONENT_DOWNTIME_HISTORY.DAILY_DURATION <> 0 : this line added for JIRA 540*/
		query.append(" MACHINEDOWNTIMEDATA LEFT JOIN ");
		query.append(  PhotoOmniDBConstants.OM_DOWNTIME_REASON).append(" OM_DOWNTIME_REASON "); 
		query.append(" ON UPPER(TRIM(OM_DOWNTIME_REASON.DOWNTIME_REASON)) = UPPER(TRIM(MACHINEDOWNTIMEDATA.DOWNTIME_REASON)) LEFT JOIN ");
		query.append(  PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" OM_USER_ATTRIBUTES ");
		query.append(" ON MACHINEDOWNTIMEDATA.BEGIN_EMPLOYEE_ID = OM_USER_ATTRIBUTES.EMPLOYEE_ID ");
		query.append(" WHERE OM_DOWNTIME_REASON.ACTIVE_CD = 1 " );
		if (!CommonUtil.isNull(machineType) && !"".equals(machineType)) {
			query.append(" AND MACHINE_TYP_ID = ? "); 
		}
		query.append(" AND START_TIME BETWEEN TO_DATE(? || ' 00:00:00','DD-MM-YYYY HH24:MI:SS') AND TO_DATE(? || ' 23:59:59','DD-MM-YYYY HH24:MI:SS') )  ");
		if (!CommonUtil.isNull(currentPage) && !"".equals(currentPage)) {
			query.append( " WHERE rn between ? and ? ");
		}
			
		return query.toString();
	}
	
	/**
	 * This method create the SQL query for location. 
	 * @param reqBean is request data bean.
	 * @return locationQuery.
	 * @throws PhotoOmniException
	 */
	private static StringBuilder locationDecider(final MachineFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering locationDecider method of MachineReportQuery ");
		}
		StringBuilder locationQuery = new StringBuilder();
		try {
			if (!CommonUtil.isNull(reqBean.getStoreId()) && !reqBean.getStoreId().isEmpty()) {
				if (reqBean.isPagination()) {
					/* When pagination is clicked from a particular store */
					locationQuery.append(" AND LOCATION_NBR = ");
					locationQuery.append(reqBean.getStoreId());
				} else if (CommonUtil.isNull(reqBean.getCurrentPageNo())
						|| reqBean.getCurrentPageNo().isEmpty()) {
					/* For Export to excel */
					locationQuery.append(" AND LOCATION_NBR = ");
					locationQuery.append(reqBean.getStoreId());
				} else {
					/* When pagination is not clicked, getting all store data */
					if (PhotoOmniConstants.LOCATION_TYPE_REGION.equals(reqBean.getLocationId())) {
						locationQuery.append(" AND REGION_NBR = ");
						locationQuery.append(reqBean.getStoreId());
					} else if (PhotoOmniConstants.LOCATION_TYPE_DISTRICT.equals(reqBean.getLocationId())) {
						locationQuery.append(" AND DISTRICT_NBR = ");
						locationQuery.append(reqBean.getStoreId());
					} else if (PhotoOmniConstants.LOCATION_TYPE_STORE.equals(reqBean.getLocationId())) {
						locationQuery.append(" AND LOCATION_NBR = ");
						locationQuery.append(reqBean.getStoreId());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at locationDecider method of MachineReportQuery - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting locationDecider method of MachineReportQuery ");
			}
		}
		return locationQuery;
	}

}
