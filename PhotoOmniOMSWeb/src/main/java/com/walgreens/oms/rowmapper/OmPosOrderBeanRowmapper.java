package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.OmOrderBean;

public class OmPosOrderBeanRowmapper implements RowMapper<OmOrderBean> {

	@Override
	public OmOrderBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmOrderBean omOrderBean = new OmOrderBean();
		omOrderBean.setSysOrderId(rs.getInt("SYS_ORDER_ID"));
		omOrderBean.setOrderPlacedDttm(rs.getTimestamp("ORDER_PLACED_DTTM").toString());
		omOrderBean.setStatus(rs.getString("STATUS"));
		return omOrderBean;
	}

}
