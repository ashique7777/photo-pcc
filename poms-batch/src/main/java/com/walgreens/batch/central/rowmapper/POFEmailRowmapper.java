package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.POFEmailDataBean;

public class POFEmailRowmapper implements RowMapper<POFEmailDataBean>{
	
	@Override
	public POFEmailDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		POFEmailDataBean pofEmailDataBean = new POFEmailDataBean();
		
		pofEmailDataBean.setSysReportEmailConfigId(rs.getInt("EMAIL_CONFIG_ID"));
		pofEmailDataBean.setSysReportId(rs.getInt("REPORT_ID"));
		pofEmailDataBean.setConfigName(rs.getString("CONFIG_NAME"));
		pofEmailDataBean.setEmailId(rs.getString("EMAIL_ID"));
		pofEmailDataBean.setEmailType(rs.getString("EMAIL_TYPE"));
		pofEmailDataBean.setActiveCd(rs.getInt("ACTIVE_CD"));
		
		return pofEmailDataBean;
	}

}
