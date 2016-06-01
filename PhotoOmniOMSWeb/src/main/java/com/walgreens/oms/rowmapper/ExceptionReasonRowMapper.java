package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.json.bean.ExceptionReason;

public class ExceptionReasonRowMapper implements RowMapper<ExceptionReason> {

	@Override
	public ExceptionReason mapRow(ResultSet rs, int rowNum) throws SQLException {
		ExceptionReason reason = new ExceptionReason();
		reason.setReason(rs.getString("EXCEPTION_REASON"));
		return reason;
	}

}
