package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.SalesReportByProductBean;

/**
 * 
 * @author CTS
 * 
 */
public class SalesReportByProductRowmapper implements
		RowMapper<SalesReportByProductBean> {

	@Override
	public SalesReportByProductBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		SalesReportByProductBean obj = new SalesReportByProductBean();
		obj.setUserId(Integer.parseInt(rs.getString("USER_ID")));
		obj.setReportId(Integer.parseInt(rs.getString("REPORT_ID")));
		obj.setFilterState(rs.getString("FILTER_STATE"));
		return obj;
	}

}
