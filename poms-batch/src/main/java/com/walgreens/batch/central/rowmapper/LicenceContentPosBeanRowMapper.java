package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.LicenceContentPosBean;

public class LicenceContentPosBeanRowMapper implements RowMapper<LicenceContentPosBean>{

	@Override
	public LicenceContentPosBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		LicenceContentPosBean licenceContentPosBean = new LicenceContentPosBean();
		licenceContentPosBean.setSysOlLcId(rs.getLong("SYS_OL_LC_ID"));
		licenceContentPosBean.setSysOrderLineId(rs.getLong("SYS_ORDER_LINE_ID"));
		licenceContentPosBean.setFinalLcPrice(rs.getDouble("FINAL_LC_PRICE"));
		return licenceContentPosBean;
	}

}
