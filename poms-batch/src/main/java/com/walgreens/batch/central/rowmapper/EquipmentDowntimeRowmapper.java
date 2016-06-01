package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;

public class EquipmentDowntimeRowmapper implements RowMapper<MachineDowntimeDataBean> {

	@Override
	public MachineDowntimeDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		MachineDowntimeDataBean equipmentDataBean = new MachineDowntimeDataBean();
		equipmentDataBean.setDowntimeId(rs.getLong("SYS_EQUIP_DWNT_ID"));
		equipmentDataBean.setInstanceId(rs.getLong("SYS_EQUIPIMENT_INSTANCE_ID"));
		equipmentDataBean.setWorkqueueStatus(rs.getInt("ACTIVE_CD"));
		equipmentDataBean.setEstimatedEndDttm(rs.getTimestamp("ESTIMATED_DWNT_END_DTTM"));
		equipmentDataBean.setActualEndDttm(rs.getTimestamp("ACTUAL_DWNT_END_DTTM"));
		equipmentDataBean.setStartDttm(rs.getTimestamp("DWNT_START_DTTM"));
		equipmentDataBean.setDowntimeReason(rs.getString("NOTES"));
		equipmentDataBean.setDowntimeReasonId(rs.getLong("SYS_DOWNTIME_REASON_ID"));
		equipmentDataBean.setIdCreated(rs.getString("CREATE_USER_ID"));
		equipmentDataBean.setDateTimeCreated(rs.getTimestamp("CREATE_DTTM"));
		equipmentDataBean.setIdModified(rs.getString("UPDATE_USER_ID"));
		equipmentDataBean.setDateTimeModified(rs.getTimestamp("UPDATE_DTTM"));
		equipmentDataBean.setBeginEmployeeId(rs.getString("BEGIN_EMPLOYEE_ID"));
		equipmentDataBean.setEndEmployeeId(rs.getString("END_EMPLOYEE_ID"));
		equipmentDataBean.setDowntimeEventName(rs.getString("DWNT_EVENT_NAME"));
		equipmentDataBean.setBusinessTimeOpenSun(rs.getString("BUSINESS_TIME_OPEN_SUN"));
		equipmentDataBean.setBusinessTimeOpenMon(rs.getString("BUSINESS_TIME_OPEN_MON"));
		equipmentDataBean.setBusinessTimeOpenTue(rs.getString("BUSINESS_TIME_OPEN_TUE"));
		equipmentDataBean.setBusinessTimeOpenWed(rs.getString("BUSINESS_TIME_OPEN_WED"));
		equipmentDataBean.setBusinessTimeOpenThs(rs.getString("BUSINESS_TIME_OPEN_THU"));
		equipmentDataBean.setBusinessTimeOpenFri(rs.getString("BUSINESS_TIME_OPEN_FRI"));
		equipmentDataBean.setBusinessTimeOpenSat(rs.getString("BUSINESS_TIME_OPEN_SAT"));
		equipmentDataBean.setBusinessTimeCloseSun(rs.getString("BUSINESS_TIME_CLOSED_SUN"));
		equipmentDataBean.setBusinessTimeCloseMon(rs.getString("BUSINESS_TIME_CLOSED_MON"));
		equipmentDataBean.setBusinessTimeCloseTue(rs.getString("BUSINESS_TIME_CLOSED_TUE"));
		equipmentDataBean.setBusinessTimeCloseWed(rs.getString("BUSINESS_TIME_CLOSED_WED"));
		equipmentDataBean.setBusinessTimeCloseThs(rs.getString("BUSINESS_TIME_CLOSED_THUS"));
		equipmentDataBean.setBusinessTimeCloseFri(rs.getString("BUSINESS_TIME_CLOSED_FRI"));
		equipmentDataBean.setBusinessTimeCloseSat(rs.getString("BUSINESS_TIME_CLOSED_SAT"));
		equipmentDataBean.setStoreNumber(rs.getString("LOCATION_NBR"));
		return equipmentDataBean;
	}

}
