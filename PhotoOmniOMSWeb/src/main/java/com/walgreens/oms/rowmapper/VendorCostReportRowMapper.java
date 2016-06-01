/**
 * 
 */
package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.POFVendorCostReportDetail;

/**
 * @author CTS
 *
 */
public class VendorCostReportRowMapper implements RowMapper<POFVendorCostReportDetail>{

	@Override
	public POFVendorCostReportDetail mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		POFVendorCostReportDetail vendorCostReportDtl = new POFVendorCostReportDetail();
		vendorCostReportDtl.setTotalRecord(rs.getInt("TOTAL_RECORD"));
		vendorCostReportDtl.setStoreNumber(rs.getInt("STORE_NUMBER"));
		vendorCostReportDtl.setEnvNumber(rs.getInt("ENVELOPE_NUMBER"));
		vendorCostReportDtl.setProductDesc(rs.getString("PRODUCT_DESCRIPTION"));
		vendorCostReportDtl.setVendorName(rs.getString("VENDOR_NAME"));
		vendorCostReportDtl.setCentralCalculatedCost(rs.getDouble("CENTRAL_CALCULATED_VEND_PMT"));
		vendorCostReportDtl.setStoreCalculatedCost(rs.getDouble("STORE_CALCULATED_VEND_COST"));
		vendorCostReportDtl.setVcId(rs.getInt("ORDER_VC_ID"));
		return vendorCostReportDtl;
	}

}
