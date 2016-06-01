package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;

public class RoyaltySalesReportRowMapper implements RowMapper<RoyaltySalesReportPrefDataBean>{

	
	public RoyaltySalesReportPrefDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean = new RoyaltySalesReportPrefDataBean();
		royaltySalesReportPrefDataBean.setUserId(Integer.parseInt(rs.getString("USER_ID")));
		royaltySalesReportPrefDataBean.setReportId(Integer.parseInt(rs.getString("REPORT_ID")));
		royaltySalesReportPrefDataBean.setFilterState(rs.getString("FILTER_STATE"));
		return royaltySalesReportPrefDataBean;
	}

}
