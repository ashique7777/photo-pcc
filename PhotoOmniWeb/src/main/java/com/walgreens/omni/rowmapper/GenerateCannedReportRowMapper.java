/**
 * 
 */
package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.CannedReportResBean;

/**
 * @author CTS
 *
 */
public class GenerateCannedReportRowMapper implements RowMapper<CannedReportResBean> {
	private boolean isSold;
	private boolean isPlaced ;
	private int pageSize;
	public  GenerateCannedReportRowMapper(boolean isPlaced,boolean isSold, int pageSize)
	{
		this.isPlaced=isPlaced;
		this.isSold = isSold;
		this.pageSize = pageSize;
	}

	@Override
	public CannedReportResBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		CannedReportResBean cannedReportResBean=new CannedReportResBean();
		cannedReportResBean.setTotalRecord(rs.getInt("TOTAL_RECORD"));
		cannedReportResBean.setProductName(rs.getString("PRODUCT_NAME"));
		cannedReportResBean.setSysProductId(rs.getInt("SYS_PRODUCT_ID"));
		cannedReportResBean.setTotalOrder(rs.getInt("TOTAL_ORDERS"));
		cannedReportResBean.setTotalProductQuantity(rs.getInt("TOTAL_PRODUCT_QUANTITY"));
		cannedReportResBean.setTotalOrderRevenue(rs.getDouble("TOTAL_ORDER_REVENUE"));
		cannedReportResBean.setTotalDiscount(rs.getDouble("TOTAL_DISCOUNT"));	
		cannedReportResBean.setTotalRevenueDiscount(rs.getDouble("TOTAL_REVENUE_DISCOUNT"));
		cannedReportResBean.setSerialNumber(rs.getInt("RN"));
		cannedReportResBean.setPageSize(pageSize);
		if(isPlaced)
		{
		cannedReportResBean.setTotalRevenue(rs.getDouble("TOTAL_REVENUE"));
		}
		if(isSold)
		{
			cannedReportResBean.setProfit(rs.getDouble("PROFIT"));
			cannedReportResBean.setAmountPaid(rs.getDouble("AMOUNT_PAID"));
		}
		//cannedReportResBean.setTotalRevenueDiscount(rs.getDouble("TOTAL_ORDER_DISCOUNT"));
	
		
	
		return cannedReportResBean;
	}

}
