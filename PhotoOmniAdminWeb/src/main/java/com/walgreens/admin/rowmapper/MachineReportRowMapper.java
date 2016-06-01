/**
 * MachineReportRowMapper.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		04 March 2015
*  
**/
package com.walgreens.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.admin.bean.MachineReportResBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to save result set data.
 * @author CTS
 * @version 1.1 March 04, 2015
 *
 */
public class MachineReportRowMapper implements RowMapper<MachineReportResBean>{

	@Override
	public MachineReportResBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		MachineReportResBean machineReportResBean = new MachineReportResBean();
		machineReportResBean.setMachineName(rs.getString("MACHINE_NAME"));
		machineReportResBean.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
		machineReportResBean.setComponentName(rs.getString("MEDIA_NAME"));
		machineReportResBean.setEnteredBy(rs.getString("ENTERED_BY"));
		machineReportResBean.setStartTime(rs.getTimestamp("START_TIME"));
		machineReportResBean.setEndTime(rs.getTimestamp("END_TIME"));
		machineReportResBean.setDuration(CommonUtil.bigDecimalToLong(rs.getString("DURATION")));
		machineReportResBean.setReason(rs.getString("DOWNTIME_REASON"));
		machineReportResBean.setRegionNumber(CommonUtil.bigDecimalToLong(rs.getString("REGION_NBR")));
		machineReportResBean.setDistrictNumber(CommonUtil.bigDecimalToLong(rs.getString("DIST_NBR")));
		machineReportResBean.setStoreNumber(CommonUtil.bigDecimalToLong(rs.getString("STORE_NUMBER")));
		machineReportResBean.setTotalRecord(CommonUtil.bigDecimalToLong(rs.getString("Total_Cnt")));
		
		return machineReportResBean;
	}

}
