/**
 * MachineTypeRowMapper.java 
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

import com.walgreens.admin.json.bean.MachineType;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to save result set data.
 * @author CTS
 * @version 1.1 March 04, 2015
 *
 */
public class MachineTypeRowMapper implements RowMapper<MachineType> {

	@Override
	public MachineType mapRow(ResultSet rs, int rowNum) throws SQLException {
		MachineType machineTypeBean = new MachineType();
		machineTypeBean.setId(CommonUtil.bigDecimalToLong(rs.getString("SYS_MACHINE_TYPE_ID")));
		machineTypeBean.setMachineName(rs.getString("MACHINE_TYPE"));
		return machineTypeBean;
	}

}
