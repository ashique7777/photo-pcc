/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.UserPrefOrDefaultRepDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 * 
 */
public class DefaultReportRowmapper implements RowMapper<UserPrefOrDefaultRepDataBean> {

	public UserPrefOrDefaultRepDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserPrefOrDefaultRepDataBean userPrefdataBean = new UserPrefOrDefaultRepDataBean();
		userPrefdataBean.setReportPrefId(CommonUtil.bigDecimalToLong(rs.getString("SYS_DEFAULT_REPORT_PREF_ID")));
		return userPrefdataBean;
	}
}
