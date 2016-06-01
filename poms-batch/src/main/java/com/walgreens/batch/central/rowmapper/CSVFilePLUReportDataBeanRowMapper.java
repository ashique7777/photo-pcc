package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.CSVFilePLUReportDataBean;

public class CSVFilePLUReportDataBeanRowMapper implements
		RowMapper<CSVFilePLUReportDataBean> {

	@Override
	public CSVFilePLUReportDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {

		CSVFilePLUReportDataBean objCSVFilePLUReportDataBean = new CSVFilePLUReportDataBean();
		objCSVFilePLUReportDataBean.setOrderDate(rs.getString("Order Date"));
		objCSVFilePLUReportDataBean.setPluNumber(rs.getString("PLU #"));
		objCSVFilePLUReportDataBean.setCouponCode(rs.getString("Coupon Code"));
		objCSVFilePLUReportDataBean.setPromotionDescription(rs
				.getString("Promotion Description"));
		objCSVFilePLUReportDataBean.setChannel(rs.getString("Channel"));
		objCSVFilePLUReportDataBean.setRetailPrice(rs.getDouble("Retail $"));
		objCSVFilePLUReportDataBean
				.setDiscountPrice(rs.getDouble("Discount $"));
		objCSVFilePLUReportDataBean.setNoOfOrders(rs.getLong("# of Orders"));
		objCSVFilePLUReportDataBean.setNoOfUnits(rs.getLong("# of Units"));
		return objCSVFilePLUReportDataBean;
	}

}
