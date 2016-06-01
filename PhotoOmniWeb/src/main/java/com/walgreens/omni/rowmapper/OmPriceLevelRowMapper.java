package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.OmPriceLevelBean;

public class OmPriceLevelRowMapper implements RowMapper<OmPriceLevelBean>{

	@Override
	public OmPriceLevelBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OmPriceLevelBean omPriceLevelBean = new OmPriceLevelBean();
		omPriceLevelBean.setPriceLevel(rs.getString("PRICE_LEVEL"));
		omPriceLevelBean.setDescription(rs.getString("DESCRIPTION"));
		omPriceLevelBean.setSysPriceLevelId(rs.getLong("SYS_PRICE_LEVEL_ID"));
		return omPriceLevelBean;
	}

}
