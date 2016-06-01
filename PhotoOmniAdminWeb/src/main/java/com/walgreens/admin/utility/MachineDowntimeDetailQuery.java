package com.walgreens.admin.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;

public class MachineDowntimeDetailQuery {

	/**
	 * Query to return EQUIPMENT_INSTANCE_ID from OM_EQUIPMENT_INSTANCE
	 * 
	 * @return query
	 */
	public static StringBuilder selectEquipmentInstanceId() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_INSTANCE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE).append(" OM_EQUIPMENT_INSTANCE ");
		query.append(" WHERE ");
		query.append(" OM_EQUIPMENT_INSTANCE.SYS_MACHINE_INSTANCE_ID = ?  ");
		/*
		 * ACTIVE_CD check commented
		 * query.append(" AND OM_EQUIPMENT_INSTANCE.ACTIVE_CD = 1 ");
		 */
		query.append(" AND ");
		query.append(" OM_EQUIPMENT_INSTANCE.SYS_EQUIPMENT_TYPE_ID = (SELECT OM_EQUIPMENT_TYPE.SYS_EQUIPMENT_TYPE_ID ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_TYPE).append(" OM_EQUIPMENT_TYPE ");
		query.append(" WHERE ");
		query.append("  OM_EQUIPMENT_TYPE.EQUIPMENT_TYPE_NBR = ?) ");
		/*
		 * ACTIVE_CD check commented
		query.append(" AND OM_EQUIPMENT_TYPE.ACTIVE_CD = 1  ");
		
		 * STATUS_CD check commented
		 * query.append(" AND ((OM_EQUIPMENT_INSTANCE.STATUS_CD = 'A') OR (OM_EQUIPMENT_INSTANCE.STATUS_CD = 'S') ) ");
		 */
		return query;
	}

	/**
	 * Query to return MACHINE_INSTANCE_ID from OM_MACHINE_INSTANCE
	 * 
	 * @return query
	 */
	public static StringBuilder selectMachineInstanceId() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_INSTANCE).append(" OM_MACHINE_INSTANCE ");
		query.append(" WHERE ");
		query.append(" OM_MACHINE_INSTANCE.MACHINE_NAME =  ?  ");
		query.append(" AND ");
		query.append(" OM_MACHINE_INSTANCE.SYS_LOCATION_ID = (SELECT OM_LOCATION.SYS_LOCATION_ID ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_LOCATION).append(" OM_LOCATION ");
		query.append(" WHERE OM_LOCATION.LOCATION_NBR = ? AND OM_LOCATION.ACTIVE_CD      = 1 ) ");
		/*
		 * ACTIVE_CD check commented
		query.append("AND OM_MACHINE_INSTANCE.ACTIVE_CD = 1 ");
		
		 * STATUS_CD check commented
		 * query.append(" AND ((OM_MACHINE_INSTANCE.STATUS_CD = 'A') OR (OM_MACHINE_INSTANCE.STATUS_CD = 'S'))");
		 */
		return query;
	} 

	/*
	 * SELECT OM_MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID
FROM OM_MACHINE_INSTANCE OM_MACHINE_INSTANCE
WHERE OM_MACHINE_INSTANCE.MACHINE_NAME = 314166
AND OM_MACHINE_INSTANCE.SYS_LOCATION_ID =
  (SELECT OM_LOCATION.SYS_LOCATION_ID
  FROM OM_LOCATION OM_LOCATION
  WHERE OM_LOCATION.LOCATION_NBR = 59156
  AND OM_LOCATION.ACTIVE_CD      = 1
  )
AND OM_MACHINE_INSTANCE.ACTIVE_CD = 1 
AND ((OM_MACHINE_INSTANCE.STATUS_CD = 'A') OR (OM_MACHINE_INSTANCE.STATUS_CD = 'S'))
	 */
	/**
	 * Query to insert data in OM_MACHINE_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder insertPROCQueryMachineDwntm() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME);
		query.append(" (SYS_MACHINE_DWNT_ID, SYS_MACHINE_INSTANCE_ID, ");
		query.append(" SYS_DOWNTIME_REASON_ID,DWNT_EVENT_NAME, DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID,  ACTIVE_CD, NOTES,PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_MACHINE_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to insert data in OM_MACHINE_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder insertDONEQueryMachineDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME);
		query.append(" (SYS_MACHINE_DWNT_ID, SYS_MACHINE_INSTANCE_ID, ");
		query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, DWNT_START_DTTM, ACTUAL_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_MACHINE_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to insert data in OM_EQUIPMENT_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder insertPROCQueryEquipmentDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME);
		query.append(" (SYS_EQUIP_DWNT_ID, SYS_EQUIPIMENT_INSTANCE_ID, ");
		query.append(" SYS_DOWNTIME_REASON_ID,DWNT_EVENT_NAME, DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID,  ACTIVE_CD, NOTES, PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_EQUIPMENT_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to insert data in OM_EQUIPMENT_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder insertDONEQueryEquipmentDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME);
		query.append(" (SYS_EQUIP_DWNT_ID, SYS_EQUIPIMENT_INSTANCE_ID, ");
		query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, DWNT_START_DTTM, ACTUAL_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_EQUIPMENT_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to insert data in OM_COMPONENT_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder insertPROCQueryMediaDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME);
		query.append(" (SYS_COMPONENT_DWNT_ID,SYS_EQUIPIMENT_INSTANCE_ID, ");
		query.append(" SYS_COMPONENT_ID, SYS_DOWNTIME_REASON_ID,DWNT_EVENT_NAME, DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID,  ACTIVE_CD, NOTES,PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_COMPONENT_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to insert data in OM_COMPONENT_DOWNTIME table
	 * 
	 * @return query
	 */
	public static StringBuilder inserDONEQueryMediaDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME);
		query.append(" (SYS_COMPONENT_DWNT_ID, SYS_EQUIPIMENT_INSTANCE_ID,SYS_COMPONENT_ID,  ");
		query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, DWNT_START_DTTM, ACTUAL_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,PROCESSED_CD, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_COMPONENT_DOWNTIME_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query;
	}

	/**
	 * Query to check if the data already present or not and the status is PROC
	 * 
	 * @return
	 */
	public static StringBuilder checkQueryMachineDataExists() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME).append(" OM_MACHINE_DOWNTIME ");
		query.append(" WHERE ");
		query.append(" OM_MACHINE_DOWNTIME.SYS_MACHINE_INSTANCE_ID = ? AND OM_MACHINE_DOWNTIME.ACTIVE_CD = 0 ");
		return query;
	}
	/* SELECT COUNT(*) FROM OM_MACHINE_DOWNTIME OM_MACHINE_DOWNTIME WHERE 
OM_MACHINE_DOWNTIME.SYS_MACHINE_INSTANCE_ID = 9 AND OM_MACHINE_DOWNTIME.ACTIVE_CD                 = 1 */

	

	/**
	 * Query to check if the data already present or not and the status is PROC
	 * 
	 * @return
	 */
	public static StringBuilder checkQueryEquipmentDataExists() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) FROM ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME).append(" OM_EQUIPMENT_DOWNTIME ");
		query.append(" WHERE ");
		query.append(" OM_EQUIPMENT_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID = ? AND OM_EQUIPMENT_DOWNTIME.ACTIVE_CD = 0 ");
		return query;
	}

	
	
	/**
	 * Query to check if the data already present or not and the status is PROC
	 * 
	 * @return
	 */
	public static StringBuilder checkQueryMediaDataExists() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME).append(" COMPONENT_DOWNTIME ");
		query.append(" WHERE COMPONENT_DOWNTIME.SYS_COMPONENT_ID = ? and COMPONENT_DOWNTIME.ACTIVE_CD = 0 and COMPONENT_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID = ? ");
		return query;
	}

	/**
	 * Query to update data in OM_COMPONENT_DOWNTIME table when workQueuStatus
	 * is PROC
	 * 
	 * @return query
	 */
	public static StringBuilder editQueryMediaDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ESTIMATED_DWNT_END_DTTM = ?, BEGIN_EMPLOYEE_ID = ? ");
		query.append(" , ACTIVE_CD = ?, NOTES = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = ?, PROCESSED_CD =? WHERE ");
		query.append(" SYS_COMPONENT_ID = ? AND ACTIVE_CD = 0 AND SYS_EQUIPIMENT_INSTANCE_ID = ? ");
		return query;
	}

	/**
	 * Query to update data in OM_COMPONENT_DOWNTIME table when workQueuStatus
	 * is DONE
	 * 
	 * @return query
	 */
	public static StringBuilder completedStatusQueryMediaDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ACTUAL_DWNT_END_DTTM = ? ");
		query.append(", END_EMPLOYEE_ID = ?, ACTIVE_CD = ?, NOTES = ?,UPDATE_USER_ID = ?, UPDATE_DTTM = ?, PROCESSED_CD =? WHERE ");
		query.append(" SYS_COMPONENT_ID = ? AND ACTIVE_CD = 0 AND SYS_EQUIPIMENT_INSTANCE_ID = ? ");
		return query;
	}

	/**
	 * Query to update data in OM_EQUIPMENT_DOWNTIME table when workQueuStatus
	 * is PROC
	 * 
	 * @return query
	 */
	public static StringBuilder editQueryEquipmentDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ESTIMATED_DWNT_END_DTTM = ?, BEGIN_EMPLOYEE_ID = ? ");
		query.append(" , ACTIVE_CD = ?, NOTES = ?,UPDATE_USER_ID = ?, UPDATE_DTTM = ?, PROCESSED_CD =? WHERE ");
		query.append(" SYS_EQUIPIMENT_INSTANCE_ID = ? AND ACTIVE_CD = 0 ");
		return query;
	}

	/**
	 * Query to update data in OM_EQUIPMENT_DOWNTIME table when workQueuStatus
	 * is DONE
	 * 
	 * @return query
	 */
	public static StringBuilder completedStatusQueryEquipmentDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ACTUAL_DWNT_END_DTTM = ? ");
		query.append(" , END_EMPLOYEE_ID = ?, ACTIVE_CD = ?, NOTES = ?,UPDATE_USER_ID = ?, UPDATE_DTTM = ?, PROCESSED_CD =? WHERE ");
		query.append(" SYS_EQUIPIMENT_INSTANCE_ID = ? AND ACTIVE_CD = 0 ");
		return query;
	}

	/**
	 * Query to update data in OM_MACHINE_DOWNTIME table when workQueuStatus is
	 * PROC
	 * 
	 * @return query
	 */
	public static StringBuilder editQueryMachineDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ESTIMATED_DWNT_END_DTTM = ?, BEGIN_EMPLOYEE_ID = ? ");
		query.append(" , ACTIVE_CD = ?, NOTES = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = ?,PROCESSED_CD =? WHERE ");
		query.append(" SYS_MACHINE_INSTANCE_ID = ? AND ACTIVE_CD = 0 ");
		return query;
	}

	/**
	 * Query to update data in OM_MACHINE_DOWNTIME table when workQueuStatus is
	 * DONE
	 * 
	 * @return query
	 */
	public static StringBuilder completedStatusQueryMachineDwntm() {
		StringBuilder query = new StringBuilder();
		query.append(" UPDATE ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME);
		query.append(" SET SYS_DOWNTIME_REASON_ID = ?,DWNT_EVENT_NAME=?, DWNT_START_DTTM = ?, ACTUAL_DWNT_END_DTTM = ? ");
		query.append(" , END_EMPLOYEE_ID = ?, ACTIVE_CD = ?, NOTES = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = ?,PROCESSED_CD =? WHERE ");
		query.append(" SYS_MACHINE_INSTANCE_ID = ? AND ACTIVE_CD = 0 ");
		return query;
	}


	

	public static StringBuilder getProductComponentIdQuery() {
		StringBuilder query = new StringBuilder();

		query.append("SELECT OM_PRODUCT_COMPONENT.SYS_PRODUCT_COMPONENT_ID ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT_COMPONENT).append(" OM_PRODUCT_COMPONENT ");
		query.append(" WHERE OM_PRODUCT_COMPONENT.COMPONENT_NBR = ? "); 
		/*
		 * ACTIVE_CD CHECK COMMENTED FOR OM_PRODUCT_COMPONENT
		 * query.append("	AND OM_PRODUCT_COMPONENT.ACTIVE_CD = 1 ");
		 */
		return query;
	}

	public static StringBuilder checkDowntimeReasonId() {
		StringBuilder query = new StringBuilder();
		
		query.append("SELECT OM_DOWNTIME_REASON.SYS_DOWNTIME_REASON_ID ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_DOWNTIME_REASON).append(" OM_DOWNTIME_REASON ");
		query.append(" WHERE OM_DOWNTIME_REASON.SYS_DOWNTIME_REASON_ID = ? ");
		
		return query;
	}

	public static StringBuilder selectDowntimeReasonId(String downtimeReason) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT OM_DOWNTIME_REASON.SYS_DOWNTIME_REASON_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_DOWNTIME_REASON).append(" OM_DOWNTIME_REASON ");
		query.append(" WHERE UPPER(OM_DOWNTIME_REASON.DOWNTIME_REASON) LIKE ");
		query.append(" '" +downtimeReason.toUpperCase()+ "' ");
		return query;
	}

	

}
