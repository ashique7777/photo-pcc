package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.OldStoreDetailBean;

public class OldStoreCloningDataRowMapper implements RowMapper<OldStoreDetailBean>{

	@Override
	public OldStoreDetailBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OldStoreDetailBean oldStoreDetailBean = new OldStoreDetailBean();
		oldStoreDetailBean.setSysLocationId(rs.getLong("SYS_LOCATION_ID"));
		oldStoreDetailBean.setLocationNbr(rs.getLong("LOCATION_NBR"));
		oldStoreDetailBean.setDistrictNbr(rs.getLong("DISTRICT_NBR"));
		oldStoreDetailBean.setAddressStateCode(rs.getString("ADDRESS_STATE_CODE"));
		oldStoreDetailBean.setSysPriceLevelId(rs.getLong("SYS_PRICE_LEVEL_ID"));
		return oldStoreDetailBean;
	}
	
	

}
