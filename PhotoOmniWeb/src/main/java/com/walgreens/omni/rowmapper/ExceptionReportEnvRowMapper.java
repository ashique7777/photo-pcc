package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.ExceptionRepEnv;

public class ExceptionReportEnvRowMapper implements RowMapper<ExceptionRepEnv> {

	@Override
	public ExceptionRepEnv mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionRepEnv responseBean = new ExceptionRepEnv();
		responseBean.setEnv(rs.getString("ENV_NUMBER"));
		responseBean.setCustomerName(rs.getString("CUSTOMER_LAST_NAME") +" , "+rs.getString("CUSTOMER_FIRST_NAME"));
		responseBean.setEnvelopeStatus(rs.getString("ENV_STATUS"));
		responseBean.setEnvelopeDescription(rs.getString("ENV_DESCRIPTION"));
		responseBean.setEnvelopeEntered(rs.getString("ENV_ENTERED_DTTM"));
		responseBean.setExceptionStatus(rs.getString("EXCEPTION_STATUS"));
		responseBean.setReason(rs.getString("REASON"));
		//responseBean.setEmployeeLastName(rs.getString("EMPLOYEE_LAST_NAME"));
		responseBean.setExceptionEntered(rs.getString("EXCEPTION_ENTERED_DTTM"));
		responseBean.setOrderPrice(rs.getString("ORDER_PRICE"));
		responseBean.setSoldPrice(rs.getString("SOLD_PRICE"));
		responseBean.setRefusedPrints(rs.getString("REFUSED_PRINTS"));
		responseBean.setTotalRows(rs.getString("TOTALROWS"));
		return responseBean;
	}

}
