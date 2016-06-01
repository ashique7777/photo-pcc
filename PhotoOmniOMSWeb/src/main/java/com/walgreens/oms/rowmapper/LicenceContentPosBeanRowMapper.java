package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.LicenceContentPosBean;

public class LicenceContentPosBeanRowMapper implements RowMapper<LicenceContentPosBean>{

	@Override
	public LicenceContentPosBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		LicenceContentPosBean licenceContentPosBean = new LicenceContentPosBean();
		
		licenceContentPosBean.setSysOlLcId(rs.getLong("SYS_OL_LC_ID"));
		licenceContentPosBean.setFinalLcPrice(rs.getDouble("FINAL_LC_PRICE"));
		
		return licenceContentPosBean;
	}

}
