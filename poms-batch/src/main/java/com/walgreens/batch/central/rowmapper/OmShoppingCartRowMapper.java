package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.OmShoppingCart;

/**
 * The following user defined RowMapper is used to get OM shopping cart details based on the query used
 * @author Cognizant
 *
 */
public class OmShoppingCartRowMapper implements RowMapper<OmShoppingCart>{

	
	private static final Logger log = LoggerFactory.getLogger(OmShoppingCartRowMapper.class);	
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public OmShoppingCart mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		log.debug("Entering OmShoppingCartRowMapper: mapRow()");
		
		OmShoppingCart omShoppingCart = new OmShoppingCart();
		omShoppingCart.setSysShoppingCartId(rs.getInt("SYS_SHOPPING_CART_ID"));
		omShoppingCart.setCartType(rs.getString("CART_TYPE"));
		omShoppingCart.setPmStatus(rs.getString("PM_STATUS"));
		omShoppingCart.setOrderPlacedDttm(rs.getTimestamp("ORDER_PLACED_DTTM"));
		omShoppingCart.setCreateUserId(rs.getString("CREATE_USER_ID"));
		omShoppingCart.setCreateDttm(rs.getDate("CREATE_DTTM"));
		omShoppingCart.setUpdatedUserId(rs.getString("UPDATE_USER_ID"));
		omShoppingCart.setUpdateDttm(rs.getDate("UPDATE_DTTM"));
		omShoppingCart.setOwningLocationId(rs.getInt("OWNING_LOC_ID"));
		omShoppingCart.setShopCartNo(rs.getLong("SHOPPING_CART_NO"));
		log.debug("Exiting OmShoppingCartRowMapper: mapRow()");
		
		return omShoppingCart;
	}

}
