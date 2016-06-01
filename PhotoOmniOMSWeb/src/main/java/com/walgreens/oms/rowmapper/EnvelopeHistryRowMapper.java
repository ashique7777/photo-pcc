package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.json.bean.EnvHistoryBean;

public class EnvelopeHistryRowMapper implements RowMapper<EnvHistoryBean> {

	@Override
	public EnvHistoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		EnvHistoryBean envHistryBean = new EnvHistoryBean();
		envHistryBean.setAction(rs.getString("ACTION"));
		envHistryBean.setComments(rs.getString("COMMENTS"));
		envHistryBean.setDateTime(rs.getString("ORDER_ACTION_DTTM"));
		envHistryBean.setReasonForTransfer(rs.getString("REASON"));
		envHistryBean.setUser(rs.getString("LAST_NAME") +"," +rs.getString("FIRST_NAME"));
		return envHistryBean;
	}

}
