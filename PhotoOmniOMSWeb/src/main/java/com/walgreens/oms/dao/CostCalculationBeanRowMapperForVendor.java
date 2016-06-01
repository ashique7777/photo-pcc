package com.walgreens.oms.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.CostCalculationBean;

public class CostCalculationBeanRowMapperForVendor implements  RowMapper<CostCalculationBean>
	 { 


		public CostCalculationBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			CostCalculationBean calculationBean=new CostCalculationBean();
			calculationBean.setAdditionalCost(rs.getDouble("ADDITIONAL_COST"));						
			calculationBean.setSysProductId(rs.getInt("Sys_Product_Id"));
			calculationBean.setCost(rs.getDouble("COST"));
			calculationBean.setCostType(rs.getString("COST_TYPE"));
			calculationBean.setShippingCost(rs.getDouble("SHIPPING_COST"));
			calculationBean.setShippingCostType(rs.getString("SHIPPING_COST_TYPE"));
			calculationBean.setBindingCostVendor(rs.getDouble("BINDING_COST_VENDOR"));
			
			
			
			return calculationBean;
		
	}

}
