package com.walgreens.oms.rowmapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.SelOrderPluDataBean;

public class OrderPluDatarowMapper implements RowMapper<SelOrderPluDataBean> {

	@Override
	public SelOrderPluDataBean mapRow(ResultSet rs, int rowNum) throws SQLException { 
		SelOrderPluDataBean orderPluBean = new SelOrderPluDataBean(); 
		orderPluBean.setOrderPlaceddttm(rs.getString("ORDERPLACEDDTTM"));
		orderPluBean.setDiscountAmount(rs.getString("PLU_DISCOUNT_AMOUNT"));
		orderPluBean.setSysPluId(Long.parseLong(rs.getString("SYS_PLU_ID")));
		orderPluBean.setActiveCd(rs.getLong("ACTIVE_CD"));
		orderPluBean.setSysOrderid(rs.getLong("SYS_ORDER_ID"));
		orderPluBean.setSysOrderPluId(rs.getLong("SYS_ORDER_PLU_ID"));
		orderPluBean.setPluNumber(rs.getString("PLU_NBR"));
		
		return orderPluBean;
	}

}


