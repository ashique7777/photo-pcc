package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.EnvelopeOrderDtlsBean;

public class EnvelopeOrderInfoRowMapper implements RowMapper<EnvelopeOrderDtlsBean> {

	@Override
	public EnvelopeOrderDtlsBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EnvelopeOrderDtlsBean exceptionOrderDetailBean = new EnvelopeOrderDtlsBean();
		exceptionOrderDetailBean.setFinalPrice(rs.getDouble("FINAL_PRICE"));
		exceptionOrderDetailBean.setDescription(rs.getString("ORDER_DESC"));
		return exceptionOrderDetailBean;
	}

}
