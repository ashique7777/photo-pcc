package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderPMDataBean;


/**
 * @author CTS
 *
 */
public class OrderMbpmRowmapper implements RowMapper<OrderPMDataBean> {

	@Override
	public OrderPMDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderPMDataBean orderPMDataBean = new OrderPMDataBean();
		
		orderPMDataBean.setOrderId(rs.getInt("SYS_ORDER_ID"));
		return orderPMDataBean;
	}

}



