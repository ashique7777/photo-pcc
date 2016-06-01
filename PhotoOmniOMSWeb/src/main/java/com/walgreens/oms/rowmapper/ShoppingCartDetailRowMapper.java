package com.walgreens.oms.rowmapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.walgreens.oms.bean.ShopCartPluDetail;

public class ShoppingCartDetailRowMapper implements RowMapper<ShopCartPluDetail> {

	@Override
	public ShopCartPluDetail mapRow(ResultSet rs, int rowNum) throws SQLException { 
		ShopCartPluDetail shopCartBean = new ShopCartPluDetail(); 
		shopCartBean.setOrderPlacedDttm(rs.getString("ORDERPLACEDDTTM"));
		shopCartBean.setPluDiscAmount(rs.getString("PLU_DISCOUNT_AMOUNT"));
		shopCartBean.setShoppingCartId(rs.getLong("SYS_SHOPPING_CART_ID"));
		shopCartBean.setSysPluId(Long.parseLong(rs.getString("SYS_PLU_ID")));
		shopCartBean.setSysScPluId(rs.getString("SYS_SC_PLU_ID"));
		shopCartBean.setActiveCd(rs.getLong("ACTIVE_CD"));
		shopCartBean.setPluNumber(rs.getString("PLU_NBR"));
		
		return shopCartBean;
	}

}

