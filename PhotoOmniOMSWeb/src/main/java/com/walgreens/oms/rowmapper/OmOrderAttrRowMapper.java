package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmOrderAttributeBean;

public class OmOrderAttrRowMapper implements RowMapper<OmOrderAttributeBean>{

	@Override
	public OmOrderAttributeBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		OmOrderAttributeBean omOrderAttributeBean = new OmOrderAttributeBean();
		omOrderAttributeBean.setCostCalStatusCd(rs.getString("COST_CALCULATION_STATUS_CD"));
		omOrderAttributeBean.setProcessingTypeCd(rs.getString("PROCESSING_TYPE_CD"));
		return omOrderAttributeBean;
	}

}
