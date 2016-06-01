package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.ProductResponseBean;

public class ProductEnveHistoryRowmapper implements RowMapper<ProductResponseBean> {
	public ProductResponseBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductResponseBean productResponseBean = new ProductResponseBean();
		/*productResponseBean.setOrderDescription(rs.getString(""));
		productResponseBean.setFinalPrice(rs.getDouble(""));
		productResponseBean.setDescription(rs.getString(""));
		productResponseBean.setQuantity(rs.getDouble(""));
		productResponseBean.setOriginalLineprice(rs.getDouble(""));
		productResponseBean.setPromotionDescOne(rs.getString(""));
		productResponseBean.setPluDiscountAmountone(rs.getDouble(""));
		productResponseBean.setPromotionDescTwo(rs.getString(""));
		productResponseBean.setPluDiscountAmountTwo(rs.getDouble(""));
		productResponseBean.setDttm(rs.getDate(""));
		productResponseBean.setAction(rs.getString(""));
		productResponseBean.setUser(rs.getString(""));
		productResponseBean.setReasonfortransfer(rs.getString(""));
		productResponseBean.setComments(rs.getString(""));*/
		
		return productResponseBean;
	}


}
