package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.CostCalculationBean;

public class CostCalculationBeanRowMapper implements RowMapper<CostCalculationBean>{ 

	@Override
	
	public CostCalculationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		CostCalculationBean calculationBean=new CostCalculationBean();
		calculationBean.setAdditionalCost(rs.getDouble("ADDITIONAL_COST"));
		calculationBean.setDevelopmentCost(rs.getDouble("DEVELOPMENT_COST"));
		calculationBean.setPrintCost(rs.getDouble("PRINT_COST"));		
		calculationBean.setBindingCostInstore(rs.getDouble("BINDING_COST_INSTORE"));
		calculationBean.setSysProductId(rs.getInt("SYS_PRODUCT_ID"));
		
		
		return calculationBean;
	}
}
