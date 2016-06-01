/**
 * 
 */
package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.CannedReportDataCSVBean;

/**
 * @author CTS
 *RowMapper to get the values for generating report
 */
public class CannedReportRowMapper  implements RowMapper<com.walgreens.omni.bean.CannedReportDataCSVBean> {
	private boolean isSold;
	private boolean isPlaced;
	
	public  CannedReportRowMapper(boolean isPlaced,boolean isSold)
	{
		
		this.isSold = isSold;
		this.isPlaced=isPlaced;
		
	}
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public CannedReportDataCSVBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		CannedReportDataCSVBean cannedReportDataCsvBean=new CannedReportDataCSVBean();
		
		cannedReportDataCsvBean.setProduct_Name(rs.getString("PRODUCT_NAME"));
		//cannedReportResBean.setSysProductId(rs.getInt("SYS_PRODUCT_ID"));
		cannedReportDataCsvBean.setTotal_Order(rs.getInt("TOTAL_ORDERS"));
		cannedReportDataCsvBean.setTotal_Product_Quantity(rs.getInt("TOTAL_PRODUCT_QUANTITY"));
		//cannedReportResBean.setTotalOrderRevenue(rs.getDouble("TOTAL_ORDER_REVENUE"));
		//cannedReportDataCsvBean.setTotalDiscount(rs.getDouble("TOTAL_DISCOUNT"));		
		cannedReportDataCsvBean.setTotal_Revenue_Discount(rs.getString("TOTAL_REVENUE_DISCOUNT"));
		cannedReportDataCsvBean.setUnit_Revenue(rs.getString("UNIT_PRICE"));
		//cannedReportResBean.setSerialNumber(rs.getInt("RN"));
		
		if(isPlaced)
		{
			cannedReportDataCsvBean.setTotal_Revenue(rs.getString("TOTAL_REVENUE"));
		}
		if(isSold)
		{
			cannedReportDataCsvBean.setProfit(rs.getString("PROFIT"));
			cannedReportDataCsvBean.setAmount_Paid(rs.getString("AMOUNT_PAID"));
		}
		//cannedReportResBean.setTotalRevenueDiscount(rs.getDouble("TOTAL_ORDER_DISCOUNT"));
	
		
	
		return cannedReportDataCsvBean;
	}

}
