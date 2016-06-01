package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.CostCalculationBean;

public class DefaultCostCalculationBeanRowMapper implements RowMapper<CostCalculationBean>{
	
	@Override
	public CostCalculationBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		CostCalculationBean costCalculationBean = new CostCalculationBean();
		costCalculationBean.setSysOrderId(rs.getInt("ORDER_ID"));
		costCalculationBean.setSysProductId(rs.getInt("PRODID"));
		costCalculationBean.setOriginalLinePrice(rs.getDouble("ORIGINAL_LINE_PRICE"));
		return costCalculationBean;
	}

}
