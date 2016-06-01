package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.PMByEmployeeDetailBean;

public class PMByEmployeeDetailBeanRowMapper implements RowMapper<PMByEmployeeDetailBean> {

	@Override
	public PMByEmployeeDetailBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PMByEmployeeDetailBean bean = new PMByEmployeeDetailBean();
		bean.setEmployeeId(rs.getString("Employee Id"));
		bean.setEmployeeName(rs.getString("Employee Name"));
		bean.setPmDollarEarned(rs.getDouble("Earned Amount"));
		bean.setPmEarnedQty(rs.getDouble("Earned Qty"));
		bean.setTotalRows(rs.getLong("TOTALROWS"));
		return bean;
	}

}
