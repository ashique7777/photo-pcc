package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.ProductDtlsDataBean;

public class ProductDtlsRowMapper implements RowMapper<ProductDtlsDataBean> {

	@Override
	public ProductDtlsDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductDtlsDataBean dataBean = new ProductDtlsDataBean();
		dataBean.setProductDesc(rs.getString("PRODUCT_DESC"));
		dataBean.setFinalPrice(rs.getString("FINAL_PRICE"));
		dataBean.setOrderDesc(rs.getString("ORDER_DESC"));
		dataBean.setPerUnitPrice(rs.getString("PER_UNIT_PRICE"));
		dataBean.setQuantity(rs.getString("QUANTITY"));
		dataBean.setOrderLineId(rs.getString("ORDER_LINE_ID"));
		return dataBean;
	}

}
