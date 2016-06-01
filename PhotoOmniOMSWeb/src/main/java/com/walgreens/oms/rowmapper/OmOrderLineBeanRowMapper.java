package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmOrderLineBean;

public class OmOrderLineBeanRowMapper implements RowMapper<OmOrderLineBean> {

	@Override
	public OmOrderLineBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OmOrderLineBean omOrderLineBean = new OmOrderLineBean();
		omOrderLineBean.setCost(rs.getDouble("COST"));
		omOrderLineBean.setQuantity(rs.getInt("QUANTITY"));
		return omOrderLineBean;
	}

	
}
