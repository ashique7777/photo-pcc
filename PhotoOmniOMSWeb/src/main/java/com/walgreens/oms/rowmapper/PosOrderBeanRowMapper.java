package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderBean;

public class PosOrderBeanRowMapper implements RowMapper<OrderBean> {

	@Override
	public OrderBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderBean  orderBean = new OrderBean();
		orderBean.setSysOrderID(rs.getLong("SYS_ORDER_ID"));
		orderBean.setOrderNBR(rs.getString("ORDER_NBR"));
		orderBean.setStatus(rs.getString("STATUS"));
		orderBean.setOrderPlacedDTTM(rs.getTimestamp("ORDER_PLACED_DTTM"));
		return orderBean;
	}

}
