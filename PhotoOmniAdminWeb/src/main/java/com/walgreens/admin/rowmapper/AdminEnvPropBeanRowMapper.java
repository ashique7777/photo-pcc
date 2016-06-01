package com.walgreens.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.cache.EvePropBean;

public class AdminEnvPropBeanRowMapper implements RowMapper<EvePropBean> {

	@Override
	public EvePropBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		EvePropBean evePropBean = new EvePropBean();
		evePropBean.setSysInvPropertiesId(rs.getInt("SYS_ENV_PROPERTIES_ID"));
		evePropBean.setEnvName(rs.getString("ENV_NAME"));
		evePropBean.setPropertyName(rs.getString("PROPERTY_NAME"));
		evePropBean.setPropertyType(rs.getString("PROPERTY_TYPE"));
		evePropBean.setPropertyValue(rs.getString("PROPERTY_VALUE"));
		return evePropBean;
	}

}
