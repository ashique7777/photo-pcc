package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;

public class PLUReportPrefDataBeanRowMapper implements RowMapper<PLUReportPrefDataBean> {

	@Override
	public PLUReportPrefDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		PLUReportPrefDataBean objPLUReportPrefDataBean = new PLUReportPrefDataBean();
		/*objPLUReportPrefDataBean.setSysUserReportPrefId(rs.getLong("SYS_USER_REPORT_PREF_ID"));*/
		objPLUReportPrefDataBean.setUserId(rs.getLong("USER_ID"));
		objPLUReportPrefDataBean.setReportId(rs.getLong("REPORT_ID"));
		objPLUReportPrefDataBean.setFilterState(rs.getString("FILTER_STATE"));
		return objPLUReportPrefDataBean;
	}

}
