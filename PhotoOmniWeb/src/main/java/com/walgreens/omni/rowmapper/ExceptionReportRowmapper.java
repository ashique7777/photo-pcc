package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.ExceptionEmployeeResponseList;

public class ExceptionReportRowmapper implements RowMapper<ExceptionEmployeeResponseList> {
	
	public ExceptionEmployeeResponseList mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionEmployeeResponseList employeeRespBean = new ExceptionEmployeeResponseList();
		employeeRespBean.setFirstName(rs.getString("FIRST_NAME"));
		employeeRespBean.setLastName(rs.getString("LAST_NAME"));
		employeeRespBean.setExceptionType(rs.getString("EXCEPTION_TYPE"));
		employeeRespBean.setWasteCost(rs.getString("WASTE_COST"));
		
		return employeeRespBean;
	}

}
