package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.EnvelopeOrderPromotionBean;

public class EnvelopeOrderPromotionRowMapper implements RowMapper<EnvelopeOrderPromotionBean> {

	@Override
	public EnvelopeOrderPromotionBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		EnvelopeOrderPromotionBean bean = new EnvelopeOrderPromotionBean();
		bean.setOrderDiscountamt(rs.getDouble("ORDER_PROMOTION_AMT"));
		bean.setOrderPromotnDesc(rs.getString("ORDER_PROMOTION_DESC"));
		return bean;
	}

}
