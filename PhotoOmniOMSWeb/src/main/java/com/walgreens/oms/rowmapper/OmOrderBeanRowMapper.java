package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmOrderAttributeBean;

public class OmOrderBeanRowMapper implements RowMapper<OmOrderAttributeBean>{

	@Override
	public OmOrderAttributeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OmOrderAttributeBean omOrderAttributeBean = new OmOrderAttributeBean();
		
		
		//omOrderBean.setStatus(rs.getString("STATUS"));
	
		omOrderAttributeBean.setProcessingTypeCd(rs.getString("PROCESSING_TYPE_CD"));
		omOrderAttributeBean.setCostCalculatiomStatusCd(rs.getString("COST_CALCULATION_STATUS_CD"));
		omOrderAttributeBean.setPayOnFulfillmentInd(rs.getInt("PAY_ON_FULFIILLMENT_CD"));
		omOrderAttributeBean.setSysFulfilmentVendorId(rs.getInt("SYS_FULFILLMENT_VENDOR_ID"));
		omOrderAttributeBean.setSysorderId(rs.getLong("SYS_ORDER_ID"));
		omOrderAttributeBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		
		
		return omOrderAttributeBean;
	}

}
