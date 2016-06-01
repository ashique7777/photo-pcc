package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.OrderLineBean;

/**
 * The following user defined RowMapper is used to get order line details based on the query used
 * @author Cognizant
 *
 */
public class OrderLineBeanRowMapper implements RowMapper<OrderLineBean>{
	
	private static final Logger log = LoggerFactory.getLogger(OrderLineBeanRowMapper.class);
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public OrderLineBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		log.debug("Entering OrderLineBeanRowMapper: mapRow()");
		
		OrderLineBean orderLineBean = new OrderLineBean();
		
		orderLineBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		orderLineBean.setSysOrderLineId(rs.getLong("SYS_ORDER_LINE_ID"));
		orderLineBean.setSysOrderId(rs.getLong("SYS_ORDER_ID"));
		orderLineBean.setProductId(rs.getLong("SYS_PRODUCT_ID"));
		orderLineBean.setMachineId(rs.getLong("SYS_MACHINE_INSTANCE_ID"));
		orderLineBean.setEquipmentTypeId(rs.getLong("SYS_EQUIPMENT_INSTANCE_ID"));
		orderLineBean.setUnitPrice(rs.getDouble("UNIT_PRICE"));
		orderLineBean.setQuantity(rs.getInt("QUANTITY"));
		orderLineBean.setOriginalQnty(rs.getInt("ORIGINAL_QTY"));
		orderLineBean.setOriginalLinePrice(rs.getDouble("ORIGINAL_LINE_PRICE"));
		orderLineBean.setFinalPrice(rs.getDouble("FINAL_PRICE"));
		orderLineBean.setDiscountAmt(rs.getDouble("DISCOUNT_AMT"));
		orderLineBean.setLoyaltyPrice(rs.getDouble("LOYALTY_PRICE"));
		orderLineBean.setLoyaltyDiscountAmt(rs.getDouble("LOYALTY_DISCOUNT_AMT"));
		orderLineBean.setCost(rs.getDouble("COST"));
		orderLineBean.setCreatedDttm(rs.getDate("CREATE_DTTM"));
		orderLineBean.setUpdatedDttm(rs.getDate("UPDATE_DTTM"));
		orderLineBean.setUpdateUserId(rs.getString("UPDATE_USER_ID"));
		
		log.debug("Exiting OrderLineBeanRowMapper: mapRow(");
		
		return orderLineBean;
	}

}
