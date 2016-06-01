/**
 * 
 */
package com.walgreens.batch.central.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * @author CTS
 * 
 */
/**
 * Query to select data from OM_LOCATION table and OM_MACHINE_DOWNTIME table
 * which are splitted in Batch process
 * 
 * @return query
 */
public class MachineDowntimeQuery {
	
	public static String getCheckQueryMachine() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM ");
		query.append( PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY ).append(" OM_MACHINE_DWNT_HISTORY "); 
		query.append(" WHERE OM_MACHINE_DWNT_HISTORY.SYS_MACHINE_DWNT_ID =? AND OM_MACHINE_DWNT_HISTORY.DAILY_DWNT_START =? ");
		return query.toString();
	}
	
	public static String getCheckQueryEquipment() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM ");
		query.append( PhotoOmniDBConstants.OM_EQUIPMENT_DWNT_HISTORY ).append(" OM_EQUIPMENT_DWNT_HISTORY "); 
		query.append(" WHERE OM_EQUIPMENT_DWNT_HISTORY.SYS_EQUIP_DWNT_ID =? AND OM_EQUIPMENT_DWNT_HISTORY.DAILY_DWNT_START =? ");
		return query.toString();
	}
	
	public static String getCheckQueryComponent() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME_HISTORY).append(" OM_COMPONENT_DOWNTIME_HISTORY ");
		query.append(" WHERE OM_COMPONENT_DOWNTIME_HISTORY.SYS_COMPONENT_DWNT_ID =? AND OM_COMPONENT_DOWNTIME_HISTORY.DAILY_DWNT_START =? ");
		return query.toString();
	}

	public static String getMachineDowntimeDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM (SELECT LOCTABLE.LOCATION_NBR, LOCTABLE.BUSINESS_TIME_OPEN_SUN, LOCTABLE.BUSINESS_TIME_OPEN_MON, LOCTABLE.BUSINESS_TIME_OPEN_TUE,  ");
		query.append(" LOCTABLE.BUSINESS_TIME_OPEN_WED, LOCTABLE.BUSINESS_TIME_OPEN_THU, LOCTABLE.BUSINESS_TIME_OPEN_FRI, LOCTABLE.BUSINESS_TIME_OPEN_SAT, LOCTABLE.BUSINESS_TIME_CLOSED_SUN,");
		query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_MON, LOCTABLE.BUSINESS_TIME_CLOSED_TUE, LOCTABLE.BUSINESS_TIME_CLOSED_WED, LOCTABLE.BUSINESS_TIME_CLOSED_THUS,");
		query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_FRI, LOCTABLE.BUSINESS_TIME_CLOSED_SAT, MACHINE_DOWNTIME.SYS_MACHINE_DWNT_ID, MACHINE_DOWNTIME.SYS_MACHINE_INSTANCE_ID,");
		query.append(" MACHINE_DOWNTIME.SYS_DOWNTIME_REASON_ID, MACHINE_DOWNTIME.DWNT_EVENT_NAME, MACHINE_DOWNTIME.DWNT_START_DTTM, MACHINE_DOWNTIME.ESTIMATED_DWNT_END_DTTM,");
		query.append(" MACHINE_DOWNTIME.ACTUAL_DWNT_END_DTTM, MACHINE_DOWNTIME.BEGIN_EMPLOYEE_ID, MACHINE_DOWNTIME.END_EMPLOYEE_ID, MACHINE_DOWNTIME.ACTIVE_CD, MACHINE_DOWNTIME.NOTES,");
		query.append(" MACHINE_DOWNTIME.CREATE_USER_ID, MACHINE_DOWNTIME.CREATE_DTTM, MACHINE_DOWNTIME.UPDATE_USER_ID, MACHINE_DOWNTIME.UPDATE_DTTM, ROWNUM ROW_NO ");
		query.append(" FROM ");
		query.append( PhotoOmniDBConstants.OM_LOCATION ).append(" LOCTABLE "); 
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_INSTANCE).append(" MACHINE_INSTANCE ");
		query.append(" ON LOCTABLE.SYS_LOCATION_ID = MACHINE_INSTANCE.SYS_LOCATION_ID ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME).append(" MACHINE_DOWNTIME");
		query.append(" ON MACHINE_DOWNTIME.SYS_MACHINE_INSTANCE_ID = MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID ");
		query.append(" WHERE MACHINE_DOWNTIME.PROCESSED_CD = 1 AND LOCTABLE.ACTIVE_CD = 1 ");
		query.append(" ORDER BY MACHINE_DOWNTIME.SYS_MACHINE_DWNT_ID) T1 WHERE T1.ROW_NO >=? AND T1.ROW_NO <=?");
		return query.toString();
	}

	/**
	 * Query to insert split data in OM_MACHINE_DWNT_HISTORY table
	 * 
	 * @return query
	 */
	public static String insertMachineDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
				query.append( PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY );
				query.append(" (SYS_MAC_DWNT_HIST_ID, SYS_MACHINE_DWNT_ID,");
				query.append(" SYS_MACHINE_INSTANCE_ID, ");
				query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, ");
				query.append(" DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM,ACTUAL_DWNT_END_DTTM, ");
				query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,DAILY_DWNT_START, DAILY_DWNT_END, DAILY_DURATION, CREATE_USER_ID,");
				query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_MACHINE_DWNT_HISTORY_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query.toString();
	}
	
	/**
	 * Query to update split data in OM_MACHINE_DWNT_HISTORY table
	 * 
	 * @return query
	 */
	public static String updateMachineDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY).append(" OM_MACHINE_DWNT_HISTORY ");
		query.append(" SET SYS_MACHINE_INSTANCE_ID = ?, SYS_DOWNTIME_REASON_ID = ?, DWNT_EVENT_NAME = ?,   ");
		query.append(" ESTIMATED_DWNT_END_DTTM = ?, ACTUAL_DWNT_END_DTTM = ?, BEGIN_EMPLOYEE_ID= ?, END_EMPLOYEE_ID = ?, ACTIVE_CD= ?, NOTES=?,  ");
		query.append(" DAILY_DWNT_START = ?, DAILY_DWNT_END= ?, DAILY_DURATION = ?, UPDATE_USER_ID = ? , UPDATE_DTTM= ? ");
		query.append(" WHERE OM_MACHINE_DWNT_HISTORY.SYS_MACHINE_DWNT_ID =? AND OM_MACHINE_DWNT_HISTORY.DAILY_DWNT_START =? ");
		
		return query.toString();
	}
	
	
	
	
	/**
	 * Query to select data from OM_LOCATION table and OM_EQUIPMENT_DOWNTIME
	 * table which are splitted in Batch process
	 * 
	 * @return query
	 */
	public static String getEquipmentDowntimeDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM (SELECT LOCTABLE.LOCATION_NBR, LOCTABLE.BUSINESS_TIME_OPEN_SUN, LOCTABLE.BUSINESS_TIME_OPEN_MON,");
				query.append(" LOCTABLE.BUSINESS_TIME_OPEN_TUE, LOCTABLE.BUSINESS_TIME_OPEN_WED, LOCTABLE.BUSINESS_TIME_OPEN_THU, ");
				query.append(" LOCTABLE.BUSINESS_TIME_OPEN_FRI, LOCTABLE.BUSINESS_TIME_OPEN_SAT, LOCTABLE.BUSINESS_TIME_CLOSED_SUN,");
				query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_MON, LOCTABLE.BUSINESS_TIME_CLOSED_TUE, LOCTABLE.BUSINESS_TIME_CLOSED_WED,");
				query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_THUS, LOCTABLE.BUSINESS_TIME_CLOSED_FRI, LOCTABLE.BUSINESS_TIME_CLOSED_SAT,");
				query.append(" EQUIPMENT_DOWNTIME.SYS_EQUIP_DWNT_ID, EQUIPMENT_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID,");
				query.append(" EQUIPMENT_DOWNTIME.SYS_DOWNTIME_REASON_ID,EQUIPMENT_DOWNTIME.DWNT_EVENT_NAME,EQUIPMENT_DOWNTIME.DWNT_START_DTTM,");
				query.append(" EQUIPMENT_DOWNTIME.ESTIMATED_DWNT_END_DTTM, EQUIPMENT_DOWNTIME.ACTUAL_DWNT_END_DTTM,");
				query.append(" EQUIPMENT_DOWNTIME.BEGIN_EMPLOYEE_ID,EQUIPMENT_DOWNTIME.END_EMPLOYEE_ID,");
				query.append(" EQUIPMENT_DOWNTIME.ACTIVE_CD,EQUIPMENT_DOWNTIME.NOTES,EQUIPMENT_DOWNTIME.CREATE_USER_ID,");
				query.append(" EQUIPMENT_DOWNTIME.CREATE_DTTM,EQUIPMENT_DOWNTIME.UPDATE_USER_ID,EQUIPMENT_DOWNTIME.UPDATE_DTTM, ROWNUM ROW_NO");
				query.append(" FROM ");
			    query.append( PhotoOmniDBConstants.OM_LOCATION ).append(" LOCTABLE "); 
			    query.append(" JOIN ");
				query.append( PhotoOmniDBConstants.OM_MACHINE_INSTANCE ).append(" MACHINE_INSTANCE "); 
				query.append(" ON LOCTABLE.SYS_LOCATION_ID = MACHINE_INSTANCE.SYS_LOCATION_ID JOIN ");
				query.append( PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE ).append(" EQUIPMENT_INSTANCE "); 
				query.append(" ON MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID = EQUIPMENT_INSTANCE.SYS_MACHINE_INSTANCE_ID JOIN OM_EQUIPMENT_DOWNTIME EQUIPMENT_DOWNTIME");
				query.append(" ON EQUIPMENT_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID = EQUIPMENT_INSTANCE.SYS_EQUIPMENT_INSTANCE_ID ");
				query.append(" WHERE EQUIPMENT_DOWNTIME.PROCESSED_CD = 1 AND LOCTABLE.ACTIVE_CD = 1 ORDER BY EQUIPMENT_DOWNTIME.SYS_EQUIP_DWNT_ID) T1 WHERE T1.ROW_NO >=? AND T1.ROW_NO  <=? ");
		return query.toString();
	}

	/**
	 * Query to select data from OM_LOCATION table and OM_COMPONENT_DOWNTIME
	 * table which are splitted in Batch process
	 * 
	 * @return query
	 */
	public static String getComponentDowntimeDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM (SELECT COM_DOWNTIME.SYS_COMPONENT_DWNT_ID, COM_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID, COM_DOWNTIME.SYS_COMPONENT_ID,");
		query.append(" COM_DOWNTIME.SYS_DOWNTIME_REASON_ID, COM_DOWNTIME.DWNT_EVENT_NAME, COM_DOWNTIME.DWNT_START_DTTM, COM_DOWNTIME.ESTIMATED_DWNT_END_DTTM,");
		query.append(" COM_DOWNTIME.ACTUAL_DWNT_END_DTTM, COM_DOWNTIME.BEGIN_EMPLOYEE_ID, COM_DOWNTIME.END_EMPLOYEE_ID, COM_DOWNTIME.ACTIVE_CD,");
		query.append(" COM_DOWNTIME.NOTES, COM_DOWNTIME.CREATE_USER_ID, COM_DOWNTIME.CREATE_DTTM, COM_DOWNTIME.UPDATE_USER_ID, COM_DOWNTIME.UPDATE_DTTM, ROWNUM ROW_NO,");
		query.append(" LOCTABLE.LOCATION_NBR, LOCTABLE.BUSINESS_TIME_OPEN_SUN, LOCTABLE.BUSINESS_TIME_OPEN_MON, LOCTABLE.BUSINESS_TIME_OPEN_TUE,");
		query.append("  LOCTABLE.BUSINESS_TIME_OPEN_WED, LOCTABLE.BUSINESS_TIME_OPEN_THU, LOCTABLE.BUSINESS_TIME_OPEN_FRI, LOCTABLE.BUSINESS_TIME_OPEN_SAT,");
		query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_SUN, LOCTABLE.BUSINESS_TIME_CLOSED_MON, LOCTABLE.BUSINESS_TIME_CLOSED_TUE, LOCTABLE.BUSINESS_TIME_CLOSED_WED,");
		query.append(" LOCTABLE.BUSINESS_TIME_CLOSED_THUS, LOCTABLE.BUSINESS_TIME_CLOSED_FRI,  LOCTABLE.BUSINESS_TIME_CLOSED_SAT FROM ");
		query.append( PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME ).append(" COM_DOWNTIME "); 
		query.append(" INNER JOIN ");
		query.append( PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE ).append(" EQUIP_INS "); 
		query.append(" ON COM_DOWNTIME.SYS_EQUIPIMENT_INSTANCE_ID = EQUIP_INS.SYS_EQUIPMENT_INSTANCE_ID ");
		query.append(" INNER JOIN ");
		query.append( PhotoOmniDBConstants.OM_MACHINE_INSTANCE ).append(" MACHINE_INSTANCE "); 
		query.append(" ON EQUIP_INS.SYS_MACHINE_INSTANCE_ID = MACHINE_INSTANCE.SYS_MACHINE_INSTANCE_ID INNER JOIN ");
		query.append( PhotoOmniDBConstants.OM_LOCATION ).append(" LOCTABLE "); 
		query.append( " ON LOCTABLE.SYS_LOCATION_ID = MACHINE_INSTANCE.SYS_LOCATION_ID WHERE COM_DOWNTIME.PROCESSED_CD = 1 AND LOCTABLE.ACTIVE_CD = 1 ORDER BY COM_DOWNTIME.SYS_COMPONENT_DWNT_ID) ");
		query.append("  T1 WHERE T1.ROW_NO >=? AND T1.ROW_NO  <=? ");
		return query.toString();
	}

	/**
	 * Query to insert split data in OM_EQUIPMENT_DWNT_HISTORY table
	 * 
	 * @return query
	 */
	public static String insertEquipmentDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append( PhotoOmniDBConstants.OM_EQUIPMENT_DWNT_HISTORY ); 
		query.append(" (SYS_EQUIP_DWNT_HIST_ID, SYS_EQUIP_DWNT_ID,SYS_EQUIPIMENT_INSTANCE_ID,");
		query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, ");
		query.append(" DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM, ACTUAL_DWNT_END_DTTM, ");
		query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,DAILY_DWNT_START, DAILY_DWNT_END, DAILY_DURATION, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_EQUIPMENT_DWNT_HISTORY_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query.toString();
	}

	/**
	 * Query to insert split data in OM_COMPONENT_DOWNTIME_HISTORY table
	 * 
	 * @return query
	 */
	public static String insertComponentDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME_HISTORY);
		query.append(" (SYS_COMPONENT_DWNT_HIST_ID, SYS_COMPONENT_DWNT_ID,SYS_EQUIPIMENT_INSTANCE_ID,");
		query.append(" SYS_COMPONENT_ID, ");
		query.append(" SYS_DOWNTIME_REASON_ID, DWNT_EVENT_NAME, ");
		query.append(" DWNT_START_DTTM, ESTIMATED_DWNT_END_DTTM,ACTUAL_DWNT_END_DTTM,");
		query.append(" BEGIN_EMPLOYEE_ID, END_EMPLOYEE_ID, ACTIVE_CD, NOTES,DAILY_DWNT_START, DAILY_DWNT_END,DAILY_DURATION, CREATE_USER_ID,");
		query.append(" CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM) VALUES (OM_COMPONENT_DWNT_HISTORY_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query.toString();
	}

	public static String queryToCheckMachineHistryData() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY).append(" OM_MACHINE_DWNT_HISTORY");
		query.append(" WHERE OM_MACHINE_DWNT_HISTORY.MACHINE_DWNT_ID = ? AND OM_MACHINE_DWNT_HISTORY.DWNT_START_DTTM = ?");
		return query.toString();
	}

	/**
	 * Query to update PROCESSED_CD in OM_MACHINE_DOWNTIME to 'N' at the end of
	 * the Batch
	 * 
	 * @return query
	 */
	public static String setProcessIndQueryMachine() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DOWNTIME).append(" OM_MACHINE_DOWNTIME");
		query.append(" SET OM_MACHINE_DOWNTIME.PROCESSED_CD = 0 WHERE OM_MACHINE_DOWNTIME.SYS_MACHINE_DWNT_ID = ? AND OM_MACHINE_DOWNTIME.ACTIVE_CD = 1");
		return query.toString();
	}

	/**
	 * Query to update PROCESSED_CD in OM_EQUIPMENT_DOWNTIME to 'N' at the end
	 * of the Batch
	 * 
	 * @return query
	 */
	public static String setProcessIndQueryEquipment() {

		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DOWNTIME).append(" OM_EQUIPMENT_DOWNTIME");
		query.append(" SET OM_EQUIPMENT_DOWNTIME.PROCESSED_CD = 0 WHERE OM_EQUIPMENT_DOWNTIME.SYS_EQUIP_DWNT_ID = ? and OM_EQUIPMENT_DOWNTIME.ACTIVE_CD = 1");
		return query.toString();
	}

	/**
	 * Query to update PROCESSED_CD in OM_COMPONENT_DOWNTIME to 'N' at the end
	 * of the Batch
	 * 
	 * @return query
	 */
	public static String setProcessIndQueryComponent() {
		
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME).append(" OM_COMPONENT_DOWNTIME");
		query.append( " SET OM_COMPONENT_DOWNTIME.PROCESSED_CD = 0 WHERE OM_COMPONENT_DOWNTIME.SYS_COMPONENT_DWNT_ID = ? and OM_COMPONENT_DOWNTIME.ACTIVE_CD = 1");
		return query.toString();
	}
	/**
	 * Query to delete duplicate records from OM_MACHINE_DWNT_HISTORY and insert updated records
	 * 
	 * @return query
	 */
	public static String deleteMachineDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		
		query.append(" DELETE FROM ");
		query.append(PhotoOmniDBConstants.OM_MACHINE_DWNT_HISTORY).append(" OM_MACHINE_DWNT_HISTORY ");
		query.append(" WHERE OM_MACHINE_DWNT_HISTORY.SYS_MACHINE_DWNT_ID =? ");
		
		return query.toString();
	}
	/**
	 * Query to delete duplicate records from OM_EQUIPMENT_DWNT_HISTORY and insert updated records
	 * 
	 * @return query
	 */
	public static String deleteEquipmentDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" DELETE FROM ");
		query.append(PhotoOmniDBConstants.OM_EQUIPMENT_DWNT_HISTORY).append(" OM_EQUIPMENT_DWNT_HISTORY ");
		query.append(" WHERE OM_EQUIPMENT_DWNT_HISTORY.SYS_EQUIP_DWNT_ID =? ");
		return query.toString();
	}
	/**
	 * Query to delete duplicate records from OM_COMPONENT_DOWNTIME_HISTORY and insert updated records
	 * 
	 * @return query
	 */
	public static String deleteComponentDowntimeHistryDtlsQuery() {
		StringBuilder query = new StringBuilder();
		query.append(" DELETE FROM ");
		query.append(PhotoOmniDBConstants.OM_COMPONENT_DOWNTIME_HISTORY).append(" OM_COMPONENT_DOWNTIME_HISTORY ");
		query.append(" WHERE OM_COMPONENT_DOWNTIME_HISTORY.SYS_COMPONENT_DWNT_ID =? ");
		return query.toString();
	}

	

}
