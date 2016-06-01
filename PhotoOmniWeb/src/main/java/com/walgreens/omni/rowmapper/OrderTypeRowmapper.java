/**
 * Class to get the values of Decode and Code_id
 */
package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.OrderType;



/**
 * @author Cognizant
 * Method to use to get the values of Decode and Code_id
 *
 */
public class OrderTypeRowmapper implements RowMapper<OrderType> {

	@Override
	public OrderType mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		OrderType orderTypeBean = new OrderType();
		orderTypeBean.setOrderTypeName(rs.getString("DECODE"));
		orderTypeBean.setOrderId(rs.getString("CODE_ID"));
		return orderTypeBean;
	}

}
