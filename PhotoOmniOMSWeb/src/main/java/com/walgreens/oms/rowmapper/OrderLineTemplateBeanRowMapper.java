package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderLineTemplateBean;

public class OrderLineTemplateBeanRowMapper implements RowMapper<OrderLineTemplateBean>{

	@Override
	public OrderLineTemplateBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		OrderLineTemplateBean orderLineTemplateBean = new OrderLineTemplateBean();
		
		orderLineTemplateBean.setSysOlTemplateId(rs.getLong("SYS_OL_TEMPLATE_ID"));
		orderLineTemplateBean.setOrderLineId(rs.getLong("SYS_ORDER_LINE_ID"));
		orderLineTemplateBean.setTemplateId(rs.getLong("SYS_TEMPLATE_ID"));
		orderLineTemplateBean.setTemplateQty(rs.getInt("TEMPLATE_QTY"));
		orderLineTemplateBean.setTemplateSoldAmt(rs.getDouble("TEMPLATE_SOLD_AMT"));
		
		return orderLineTemplateBean;
	}

}
