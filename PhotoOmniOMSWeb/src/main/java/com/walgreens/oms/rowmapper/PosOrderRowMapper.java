package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.POSOrder;

public class PosOrderRowMapper implements RowMapper<POSOrder>{

	@Override
	public POSOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		POSOrder posOrder = new POSOrder();
		
		posOrder.setSysOrderId(rs.getLong("SYS_ORDER_ID"));
		posOrder.setSoldAmount(rs.getDouble("SOLD_AMOUNT"));
		posOrder.setFinalPrice(rs.getDouble("FINAL_PRICE"));
		posOrder.setStatus(rs.getString("STATUS"));
		posOrder.setOrderCompletedDttm(rs.getTimestamp("ORDER_COMPLETED_DTTM"));
		posOrder.setOrderPlacedDttm(rs.getTimestamp("ORDER_PLACED_DTTM"));
		posOrder.setOwningLocId(rs.getInt("SYS_OWNING_LOC_ID"));
		posOrder.setCostCalculationCd(rs.getString("COST_CALCULATION_STATUS_CD"));
		posOrder.setProcessingTypeCd(rs.getString("PROCESSING_TYPE_CD"));
	
		return posOrder;
	}

}
