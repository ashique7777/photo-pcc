package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.TADataBean;

public class PropertyValueRowMapper implements RowMapper<TADataBean> {

	@Override
	public TADataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		TADataBean taDataBean = new TADataBean();
		
		taDataBean.setPropValue(rs.getString("PROPERTY_VALUE"));
		
		return taDataBean;
	}

}
