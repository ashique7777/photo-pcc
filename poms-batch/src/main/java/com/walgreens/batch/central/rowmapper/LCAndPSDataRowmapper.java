/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 *
 */
public class LCAndPSDataRowmapper implements RowMapper<LCAndPSReportPrefDataBean>{

	public LCAndPSReportPrefDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean = new LCAndPSReportPrefDataBean();
		lCAndPSReportPrefDataBean.setReportPrefId(CommonUtil.bigDecimalToLong(rs.getString("SYS_USER_REPORT_PREF_ID")));
		lCAndPSReportPrefDataBean.setReportId(CommonUtil.bigDecimalToLong(rs.getString("SYS_REPORT_ID")));
		lCAndPSReportPrefDataBean.setFilterState(rs.getString("FILTER_STATE"));
		return lCAndPSReportPrefDataBean;
		}


}
