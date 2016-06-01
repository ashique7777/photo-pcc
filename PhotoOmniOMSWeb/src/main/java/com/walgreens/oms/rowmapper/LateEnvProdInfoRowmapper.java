package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.OrderInfo;

public class LateEnvProdInfoRowmapper implements RowMapper<OrderInfo> {

	@Override
	public OrderInfo mapRow(ResultSet rs, int rowNum) throws SQLException { 
		OrderInfo envelopeBean = new OrderInfo(); 
		
		envelopeBean.setEnvelopeNbr(Long.parseLong(rs.getString("ENVELOPE_NUMBER")));
		envelopeBean.setDescription(rs.getString("DESCRIPTION")); 
		envelopeBean.setPrice(rs.getString("FINAL_PRICE"));
		envelopeBean.setKioskId(Long.parseLong(rs.getString("SRC_KIOSK_ORDER_NBR")));
		if(!CommonUtil.isNull(rs.getString("SRC_VENDOR_ORDER_NBR"))) {
		   envelopeBean.setWebId(Long.parseLong(rs.getString("SRC_VENDOR_ORDER_NBR")));
		}
		return envelopeBean;
	}

}
