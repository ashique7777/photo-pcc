package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.POSOrder;

public class PosOrderRowMapper implements RowMapper<POSOrder>{

	@Override
	public POSOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
		POSOrder posOrder = new POSOrder();
		
		posOrder.setSysOrderId(rs.getLong("SYS_ORDER_ID"));
		posOrder.setSoldAmount(rs.getDouble("SOLD_AMOUNT"));
		posOrder.setFinalPrice(rs.getDouble("FINAL_PRICE"));
		posOrder.setStatus(rs.getString("STATUS"));
		posOrder.setOrderCompletedDttm(rs.getString("ORDER_COMPLETED_DTTM"));
		posOrder.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		posOrder.setCreateUserId(rs.getString("CREATE_USER_ID"));
		posOrder.setCreateDttm(rs.getTimestamp("CREATE_DTTM"));
		posOrder.setUpdateUserId(rs.getString("UPDATE_USER_ID"));
		posOrder.setUpdatedDttm(rs.getTimestamp("UPDATE_DTTM"));		
		posOrder.setOriginalOrderPrice(rs.getDouble("ORIGINAL_ORDER_PRICE"));
		posOrder.setDiscountCardUsedCd(rs.getInt("DISCOUNT_CARD_USED_CD"));
		posOrder.setOwningLocId(rs.getLong("SYS_OWNING_LOC_ID"));		
		
		return posOrder;
	}

}
