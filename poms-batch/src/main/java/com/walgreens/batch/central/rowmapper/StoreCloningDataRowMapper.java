package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.StoreCloningBean;



public class StoreCloningDataRowMapper implements RowMapper<StoreCloningBean>{
	
	@Override
	public StoreCloningBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		StoreCloningBean storeCloningBean = new StoreCloningBean();
		storeCloningBean.setSysLocationId(rs.getLong("SYS_LOCATION_ID"));
		storeCloningBean.setLocationNbr(rs.getLong("LOCATION_NBR"));
		storeCloningBean.setDistrictNbr(rs.getLong("DISTRICT_NBR"));
		storeCloningBean.setAddressStateCode(rs.getString("ADDRESS_STATE_CODE"));
		storeCloningBean.setSysPriceLevelId(rs.getLong("SYS_PRICE_LEVEL_ID"));
		return storeCloningBean;
	}

}
