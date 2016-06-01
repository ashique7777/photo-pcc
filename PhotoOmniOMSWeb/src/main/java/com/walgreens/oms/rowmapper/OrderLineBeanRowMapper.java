package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderLineBean;


/**
 * The following user defined RowMapper is used to get order line details based on the query used
 * @author Cognizant
 *
 */
public class OrderLineBeanRowMapper implements RowMapper<OrderLineBean>{
	
	@Override
	public OrderLineBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OrderLineBean orderLineBean = new OrderLineBean();

			orderLineBean.setSysOrderLineId(rs.getLong("SYS_ORDER_LINE_ID"));
			orderLineBean.setFinalPrice(rs.getDouble("FINAL_PRICE"));
			orderLineBean.setQuantity(rs.getInt("QUANTITY"));
			orderLineBean.setSysProductId(rs.getLong("SYS_PRODUCT_ID"));
			orderLineBean.setCost(rs.getDouble("COST"));
		
		return orderLineBean;
	}

}
