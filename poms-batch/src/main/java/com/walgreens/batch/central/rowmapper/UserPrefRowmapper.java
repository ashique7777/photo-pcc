package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;

public class UserPrefRowmapper implements RowMapper<PMBYWICReportPrefDataBean>{

	@Override
	public PMBYWICReportPrefDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean = new PMBYWICReportPrefDataBean();
		objPMBYWICReportPrefDataBean.setUserId(Integer.parseInt(rs.getString("USER_ID")));
		objPMBYWICReportPrefDataBean.setReportId(Integer.parseInt(rs.getString("REPORT_ID")));
		objPMBYWICReportPrefDataBean.setFilterState(rs.getString("FILTER_STATE"));
		return objPMBYWICReportPrefDataBean;
	}
}
