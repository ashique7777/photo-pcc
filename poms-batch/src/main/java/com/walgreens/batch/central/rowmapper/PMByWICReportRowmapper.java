package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.CSVFileReportDataBean;

/**
 * @author CTS
 * 
 */
public class PMByWICReportRowmapper implements RowMapper<CSVFileReportDataBean> {

	public CSVFileReportDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {

		CSVFileReportDataBean objreportDataBean = new CSVFileReportDataBean();

		objreportDataBean.setWIC(rs.getString("WIC"));
		objreportDataBean.setProductDescription(rs.getString("Product Description"));
		objreportDataBean.setCalculatedRetail(rs.getDouble("Calculated Retail"));
		objreportDataBean.setSales(rs.getDouble("Amount of PM paid"));
		objreportDataBean.setAmountOfPMPaid(rs.getDouble("Sales (Amount Paid)"));
		objreportDataBean.setNumberOfOrders(rs.getLong("# of orders"));
		objreportDataBean.setItemCost(rs.getDouble("Item Cost"));
		objreportDataBean.setTotalQuantity(rs.getInt("Total Quantity"));
		objreportDataBean.setGrossProfit(rs.getDouble("Gross profit"));
		
		return objreportDataBean;
	}
}