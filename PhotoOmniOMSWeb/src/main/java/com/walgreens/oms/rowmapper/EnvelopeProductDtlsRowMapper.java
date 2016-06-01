package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.EnvelopeProductDtlsBean;

public class EnvelopeProductDtlsRowMapper implements RowMapper<EnvelopeProductDtlsBean> {

	@Override
	public EnvelopeProductDtlsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		EnvelopeProductDtlsBean dataBean = new EnvelopeProductDtlsBean();
		dataBean.setProductDesc(rs.getString("PRODUCT_DESC"));
		dataBean.setOrderLinePrice(rs.getDouble("ORDER_LINE_PRICE"));
		dataBean.setQuantity(rs.getLong("ORDER_LINE_QUANTITY"));
		if (!(CommonUtil.isNull(dataBean.getQuantity()) || (dataBean.getQuantity() ==0))) {
			dataBean.setPerUnitPrice(dataBean.getOrderLinePrice()/dataBean.getQuantity());
		} else {
			dataBean.setPerUnitPrice(0);
		}
		dataBean.setOrderLineId(rs.getLong("ORDER_LINE_ID"));
		return dataBean;
	}

}
