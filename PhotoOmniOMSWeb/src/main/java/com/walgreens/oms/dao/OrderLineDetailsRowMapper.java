package com.walgreens.oms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OrderItemLineBean;

public class OrderLineDetailsRowMapper implements RowMapper<OrderItemLineBean> {

	@Override
	public OrderItemLineBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		OrderItemLineBean itemLineBean = new OrderItemLineBean();
		
		itemLineBean.setOrderLineId(rs.getLong("ORDER_LINE_ID"));
		itemLineBean.setOriginalLinePrice(rs.getDouble("ORIGINAL_LINE_PRICE"));
		itemLineBean.setPrintedQty(rs.getInt("PRINTED_QTY"));
		itemLineBean.setQuantity(rs.getInt("QUANTITY"));
		itemLineBean.setSysEquipmentInstanceId(rs.getInt("SYS_EQUIPMENT_INSTANCE_ID"));
		itemLineBean.setSysMachineInstanceId(rs.getInt("SYS_MACHINE_INSTANCE_ID"));
		itemLineBean.setSysProductId(rs.getInt("SYS_PRODUCT_ID"));
		itemLineBean.setWastedQty(rs.getInt("WASTED_QTY"));
		itemLineBean.setSysOrderId(rs.getInt("SYS_ORDER_ID"));
		itemLineBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		itemLineBean.setPanaromicPrints(rs.getString("PANOROMIC_PRINT_QTY"));
		itemLineBean.setNoOfInputs(rs.getString("INPUT_IMAGE_QTY"));
		return itemLineBean;
		
	}

}
