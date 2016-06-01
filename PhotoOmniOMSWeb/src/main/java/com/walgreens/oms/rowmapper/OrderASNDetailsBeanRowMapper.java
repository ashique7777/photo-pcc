package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderASNDetailsBean;

public class OrderASNDetailsBeanRowMapper implements RowMapper<OrderASNDetailsBean> {
	
	@Override
	public OrderASNDetailsBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		OrderASNDetailsBean orderASNDetailsBean = new OrderASNDetailsBean();
		orderASNDetailsBean.setOrderPlacedDttm(rs.getTimestamp("ORDER_PLACED_DTTM"));
		orderASNDetailsBean.setSysOrderId(rs.getLong("SYS_ORDER_ID"));		
		orderASNDetailsBean.setVendorId(rs.getInt("VENDOR_ID"));
		orderASNDetailsBean.setStatus(rs.getString("STATUS"));
		orderASNDetailsBean.setLocationNumber(rs.getInt("LOCATION_NBR")); 
		orderASNDetailsBean.setLocationType(rs.getString("LOCATION_TYPE"));
		orderASNDetailsBean.setSysSrcVendorId(rs.getInt("SYS_FULFILLMENT_VENDOR_ID"));
		return orderASNDetailsBean;
	}

}
