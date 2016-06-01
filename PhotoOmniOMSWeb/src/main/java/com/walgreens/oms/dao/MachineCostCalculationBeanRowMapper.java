package com.walgreens.oms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.CostCalculationBean;

public class MachineCostCalculationBeanRowMapper implements RowMapper<CostCalculationBean> { 

	@Override
	public CostCalculationBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		CostCalculationBean costCalculationBean = new CostCalculationBean();
			
		costCalculationBean.setDfltMacCostPercent(rs.getDouble("DFLT_MAC_COST_PERCNT"));
		
		return costCalculationBean;
	}
   
}
