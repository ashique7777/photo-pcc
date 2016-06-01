package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.EnvelopeOrderLinePromotionBean;

public class EnvelopeOrderLinePromotionRowMapper implements
		RowMapper<EnvelopeOrderLinePromotionBean> {

	@Override
	public EnvelopeOrderLinePromotionBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EnvelopeOrderLinePromotionBean bean = new EnvelopeOrderLinePromotionBean();
		bean.setOrderLineId(rs.getLong("ORDER_LINE_ID"));
		bean.setDiscountAmt(rs.getDouble("ORDER_LINE_PROMOTION_AMT"));
		bean.setPromotionDesc(rs.getString("ORDER_LINE_PROMOTION_DESC"));
		return bean;
	}

}
