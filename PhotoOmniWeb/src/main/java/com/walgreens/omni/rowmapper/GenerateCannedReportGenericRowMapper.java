package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.CannedReportResBean;
import com.walgreens.omni.json.bean.CannedReportResGenericBean;

public class GenerateCannedReportGenericRowMapper  implements RowMapper<CannedReportResGenericBean> {


	@Override
	public CannedReportResGenericBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		CannedReportResGenericBean cannedReportResGenericBean=new CannedReportResGenericBean();
/*		cannedReportResBean.setTotalRecord(rs.getInt("TOTAL_RECORD"));
		cannedReportResBean.setProductName(rs.getString("PRODUCT_NAME"));
		cannedReportResBean.setSysProductId(rs.getInt("SYS_PRODUCT_ID"));
		cannedReportResBean.setTotalOrder(rs.getInt("TOTAL_ORDERS"));
		cannedReportResBean.setTotalProductQuantity(rs.getInt("TOTAL_PRODUCT_QUANTITY"));*/
		cannedReportResGenericBean.setTotalOrderRevenue(rs.getDouble("TOTAL_ORDER_REVENUE"));
		cannedReportResGenericBean.setTotalDiscount(rs.getDouble("TOTAL_DISCOUNT"));
		cannedReportResGenericBean.setTotalOrder(rs.getInt("TOTAL_ORDERS"));
		/*cannedReportResBean.setTotalRevenue(rs.getDouble("TOTAL_REVENUE"));
		cannedReportResBean.setTotalRevenueDiscount(rs.getDouble("TOTAL_REVENUE_DISCOUNT"));
		cannedReportResBean.setUnitPrice(rs.getDouble("UNIT_PRICE"));	*/	

		/*if(isSold)
		{
			cannedReportResBean.setProfit(rs.getDouble("PROFIT"));
		}*/
		//cannedReportResBean.setTotalRevenueDiscount(rs.getDouble("TOTAL_ORDER_DISCOUNT"));
	
		
	
		return cannedReportResGenericBean;
	}
}

