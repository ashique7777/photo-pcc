package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.json.bean.ExceptionRepEnv;

public class ExceptionReportEnvelopeRowMapper implements RowMapper<ExceptionRepEnv> {

	@Override
	public ExceptionRepEnv mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionRepEnv responseBean = new ExceptionRepEnv();
		responseBean.setEnv(rs.getLong("ENV_NUMBER"));
		/*
		 * Customer Name Removed from the Exception report
		 */
		//responseBean.setCustomerName(rs.getString("CUSTOMER_LAST_NAME") +" , "+rs.getString("CUSTOMER_FIRST_NAME"));
		responseBean.setEnvelopeStatus(rs.getString("ENV_STATUS"));
		responseBean.setEnvelopeDescription(rs.getString("ENV_DESCRIPTION"));
		responseBean.setEnvelopeEntered(rs.getString("ENV_ENTERED_DTTM"));
		responseBean.setExceptionStatus(rs.getString("EXCEPTION_STATUS"));
		responseBean.setReason(rs.getString("REASON"));
		responseBean.setEmployeeLastName(rs.getString("EMPLOYEE_LAST_NAME"));
		responseBean.setExceptionEntered(rs.getString("EXCEPTION_ENTERED_DTTM"));
		responseBean.setOrderPrice(rs.getString("ORDER_PRICE"));
		responseBean.setSoldPrice(rs.getString("SOLD_PRICE"));
		responseBean.setRefusedPrints(rs.getString("REFUSED_PRINTS"));
		responseBean.setOrderId(rs.getLong("ORDER_ID"));
		responseBean.setTotalRows(rs.getLong("TOTALROWS"));
		/*
		 * Env popup for different types of orders
		 */
		responseBean.setProcessingTypeCD(rs.getString("PROCESSING_TYPE_CD"));
		if (responseBean.getProcessingTypeCD().equalsIgnoreCase("S")) {
			responseBean.setOrderType("SENDOUT");
		} else {
			responseBean.setOrderType(rs.getString("ORDER_ORIGIN_TYPE").toUpperCase());
		}
		return responseBean;
		
	}

}
