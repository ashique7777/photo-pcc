package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;



public class MachineDwntmRowmapper implements RowMapper<MachineDowntimeDataBean> {

	@Override
	public MachineDowntimeDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		MachineDowntimeDataBean machineDowntimeDataBean = new MachineDowntimeDataBean();
		machineDowntimeDataBean.setDowntimeId(rs.getLong("SYS_MACHINE_DWNT_ID"));
		machineDowntimeDataBean.setInstanceId(rs.getLong("SYS_MACHINE_INSTANCE_ID"));
		machineDowntimeDataBean.setWorkqueueStatus(rs.getInt("ACTIVE_CD"));
		machineDowntimeDataBean.setEstimatedEndDttm(rs.getTimestamp("ESTIMATED_DWNT_END_DTTM"));
		machineDowntimeDataBean.setActualEndDttm(rs.getTimestamp("ACTUAL_DWNT_END_DTTM"));
		machineDowntimeDataBean.setStartDttm(rs.getTimestamp("DWNT_START_DTTM"));
		machineDowntimeDataBean.setDowntimeReason(rs.getString("NOTES"));
		machineDowntimeDataBean.setDowntimeReasonId(rs.getLong("SYS_DOWNTIME_REASON_ID"));
		machineDowntimeDataBean.setIdCreated(rs.getString("CREATE_USER_ID"));
		machineDowntimeDataBean.setDateTimeCreated(rs.getTimestamp("CREATE_DTTM"));
		machineDowntimeDataBean.setIdModified(rs.getString("UPDATE_USER_ID"));
		machineDowntimeDataBean.setDateTimeModified(rs.getTimestamp("UPDATE_DTTM"));
		machineDowntimeDataBean.setBeginEmployeeId(rs.getString("BEGIN_EMPLOYEE_ID"));
		machineDowntimeDataBean.setEndEmployeeId(rs.getString("END_EMPLOYEE_ID"));
		machineDowntimeDataBean.setDowntimeEventName(rs.getString("DWNT_EVENT_NAME"));
		machineDowntimeDataBean.setBusinessTimeOpenSun(rs.getString("BUSINESS_TIME_OPEN_SUN"));
		machineDowntimeDataBean.setBusinessTimeOpenMon(rs.getString("BUSINESS_TIME_OPEN_MON"));
		machineDowntimeDataBean.setBusinessTimeOpenTue(rs.getString("BUSINESS_TIME_OPEN_TUE"));
		machineDowntimeDataBean.setBusinessTimeOpenWed(rs.getString("BUSINESS_TIME_OPEN_WED"));
		machineDowntimeDataBean.setBusinessTimeOpenThs(rs.getString("BUSINESS_TIME_OPEN_THU"));
		machineDowntimeDataBean.setBusinessTimeOpenFri(rs.getString("BUSINESS_TIME_OPEN_FRI"));
		machineDowntimeDataBean.setBusinessTimeOpenSat(rs.getString("BUSINESS_TIME_OPEN_SAT"));
		machineDowntimeDataBean.setBusinessTimeCloseSun(rs.getString("BUSINESS_TIME_CLOSED_SUN"));
		machineDowntimeDataBean.setBusinessTimeCloseMon(rs.getString("BUSINESS_TIME_CLOSED_MON"));
		machineDowntimeDataBean.setBusinessTimeCloseTue(rs.getString("BUSINESS_TIME_CLOSED_TUE"));
		machineDowntimeDataBean.setBusinessTimeCloseWed(rs.getString("BUSINESS_TIME_CLOSED_WED"));
		machineDowntimeDataBean.setBusinessTimeCloseThs(rs.getString("BUSINESS_TIME_CLOSED_THUS"));
		machineDowntimeDataBean.setBusinessTimeCloseFri(rs.getString("BUSINESS_TIME_CLOSED_FRI"));
		machineDowntimeDataBean.setBusinessTimeCloseSat(rs.getString("BUSINESS_TIME_CLOSED_SAT"));
		machineDowntimeDataBean.setStoreNumber(rs.getString("LOCATION_NBR"));
		
		return machineDowntimeDataBean;
	}

	

}
