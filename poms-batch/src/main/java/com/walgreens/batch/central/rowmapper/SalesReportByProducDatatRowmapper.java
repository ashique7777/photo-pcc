package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.SalesReportByProductDataBean;

/**
 * 
 * @author CTS
 * 
 */
public class SalesReportByProducDatatRowmapper implements
		RowMapper<SalesReportByProductDataBean> {

	@Override
	public SalesReportByProductDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {

		SalesReportByProductDataBean bean = new SalesReportByProductDataBean();
		bean.setTemplateId(rs.getLong("TEMPLATE_ID"));
		bean.setCategory(rs.getString("CATEGORY"));
		bean.setDescription(rs.getString("DESCRIPTION"));
		bean.setOutputSize(rs.getString("OUTPUT_SIZE"));
		bean.setVendor(rs.getString("VENDOR"));
		bean.setCount(rs.getInt("COUNT"));
		bean.setQuantity(rs.getInt("QUANTITY"));
		bean.setAmountPaid(rs.getDouble("AMOUNT_PAID"));
		bean.setCalculatedRetail(rs.getDouble("CALCULATED_RETAIL"));
		bean.setOriginalRetail(rs.getDouble("ORIGINAL_RETAIL"));
		bean.setOrderCost(rs.getDouble("ORDER_COST"));
		bean.setTotalDiscountAmount(rs.getDouble("TOTAL_DISCOUNT_AMT"));
		return bean;
	}

}
