/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 *
 */
public class LCDailyDataRowmapper implements RowMapper<LCDailyReportPrefDataBean>{

	public LCDailyReportPrefDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LCDailyReportPrefDataBean lCDailyReportPrefDataBean = new LCDailyReportPrefDataBean();
		lCDailyReportPrefDataBean.setReportPrefId(CommonUtil.bigDecimalToLong(rs.getString("SYS_DEFAULT_REPORT_PREF_ID")));
		lCDailyReportPrefDataBean.setReportId(CommonUtil.bigDecimalToLong(rs.getString("SYS_REPORT_ID")));
		lCDailyReportPrefDataBean.setFilterState(rs.getString("FILTER_STATE"));
		return lCDailyReportPrefDataBean;
		}


}
