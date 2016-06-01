package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.OmSimRetailBlockReportBean;

public class OmSimRetailBlockReportBeanRowMapper implements RowMapper<OmSimRetailBlockReportBean> {

	@Override
	public OmSimRetailBlockReportBean mapRow(ResultSet rs, int rowNum)throws SQLException {
		
		OmSimRetailBlockReportBean omSimRetailBlockReportBean = new OmSimRetailBlockReportBean();
		omSimRetailBlockReportBean.setLocationNbr(rs.getInt("LOCATION_NBR"));
		omSimRetailBlockReportBean.setRetailBlock(Integer.valueOf(rs.getString("PRICE_LEVEL")));
		omSimRetailBlockReportBean.setDescription(rs.getString("DESCRIPTION"));
		omSimRetailBlockReportBean.setTotalRows(rs.getInt("TOTAL_ROWS"));
		
		return omSimRetailBlockReportBean;
	}

}
