package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.ExceptionByEmployeeBean;

public class ExceptionReportEmployeeRowMapper implements RowMapper<ExceptionByEmployeeBean> {
	
	public ExceptionByEmployeeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionByEmployeeBean employeeRespBean = new ExceptionByEmployeeBean();
		
		employeeRespBean.setEmpName(rs.getString("EMPLOYEE_NAME"));
		employeeRespBean.setEmployeeId(rs.getString("EMPLOYEE_ID"));
		employeeRespBean.setExceptionCount(rs.getLong("EXCEPTION_CNT"));
		employeeRespBean.setExceptionType(!CommonUtil.isNull(rs.getString("EXCEPTION_TYPE")) ? rs.getString("EXCEPTION_TYPE").trim() : "");
		employeeRespBean.setWasteCost(rs.getString("WASTE_COST"));
		employeeRespBean.setTotalRows(rs.getLong("TOTALROWS"));
		//employeeRespBean.setTotalExceptionCount(rs.getLong("EXCEPTION_TOTAL"));
		//employeeRespBean.setTotalWasteCost(rs.getString("SUM_TOTAL"));
		return employeeRespBean;
	}

}
