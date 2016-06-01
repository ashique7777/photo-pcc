package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;

public class ComponentDowntimeRowmapper implements RowMapper<MachineDowntimeDataBean> {

	@Override
	public MachineDowntimeDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		MachineDowntimeDataBean bean = new MachineDowntimeDataBean();
		bean.setDowntimeId(rs.getLong("SYS_COMPONENT_DWNT_ID"));
		bean.setInstanceId(rs.getLong("SYS_EQUIPIMENT_INSTANCE_ID"));
		bean.setComponentId(rs.getString("SYS_COMPONENT_ID"));
		bean.setWorkqueueStatus(rs.getInt("ACTIVE_CD"));
		bean.setEstimatedEndDttm(rs.getTimestamp("ESTIMATED_DWNT_END_DTTM"));
		bean.setActualEndDttm(rs.getTimestamp("ACTUAL_DWNT_END_DTTM"));
		bean.setStartDttm(rs.getTimestamp("DWNT_START_DTTM"));
		bean.setDowntimeReason(rs.getString("NOTES"));
		bean.setDowntimeReasonId(rs.getLong("SYS_DOWNTIME_REASON_ID"));
		bean.setIdCreated(rs.getString("CREATE_USER_ID"));
		bean.setDateTimeCreated(rs.getTimestamp("CREATE_DTTM"));
		bean.setIdModified(rs.getString("UPDATE_USER_ID"));
		bean.setDateTimeModified(rs.getTimestamp("UPDATE_DTTM"));
		bean.setBeginEmployeeId(rs.getString("BEGIN_EMPLOYEE_ID"));
		bean.setEndEmployeeId(rs.getString("END_EMPLOYEE_ID"));
		bean.setDowntimeEventName(rs.getString("DWNT_EVENT_NAME"));
		bean.setBusinessTimeOpenSun(rs.getString("BUSINESS_TIME_OPEN_SUN"));
		bean.setBusinessTimeOpenMon(rs.getString("BUSINESS_TIME_OPEN_MON"));
		bean.setBusinessTimeOpenTue(rs.getString("BUSINESS_TIME_OPEN_TUE"));
		bean.setBusinessTimeOpenWed(rs.getString("BUSINESS_TIME_OPEN_WED"));
		bean.setBusinessTimeOpenThs(rs.getString("BUSINESS_TIME_OPEN_THU"));
		bean.setBusinessTimeOpenFri(rs.getString("BUSINESS_TIME_OPEN_FRI"));
		bean.setBusinessTimeOpenSat(rs.getString("BUSINESS_TIME_OPEN_SAT"));
		bean.setBusinessTimeCloseSun(rs.getString("BUSINESS_TIME_CLOSED_SUN"));
		bean.setBusinessTimeCloseMon(rs.getString("BUSINESS_TIME_CLOSED_MON"));
		bean.setBusinessTimeCloseTue(rs.getString("BUSINESS_TIME_CLOSED_TUE"));
		bean.setBusinessTimeCloseWed(rs.getString("BUSINESS_TIME_CLOSED_WED"));
		bean.setBusinessTimeCloseThs(rs.getString("BUSINESS_TIME_CLOSED_THUS"));
		bean.setBusinessTimeCloseFri(rs.getString("BUSINESS_TIME_CLOSED_FRI"));
		bean.setBusinessTimeCloseSat(rs.getString("BUSINESS_TIME_CLOSED_SAT"));
		bean.setStoreNumber(rs.getString("LOCATION_NBR"));
		return bean;
	}

}
