package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.json.bean.ProductDetails;

public class ProductdetailsRowmapper implements RowMapper<ProductDetails>  {
	
	public ProductDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProductDetails projectDetails = new ProductDetails();
		projectDetails.setWic(rs.getString("WIC"));
		projectDetails.setProductDescription(rs.getString("PRODUCT_DESCRIPTION"));
		projectDetails.setQuantity(rs.getString("QUANTITY"));
		projectDetails.setSerialNumber(rs.getInt("Row_no"));
		projectDetails.setUpc(rs.getString("UPC"));
		projectDetails.setVendorItemCost(rs.getDouble("VENDOR_ITEM_COST"));
		projectDetails.setWic(rs.getString("WIC"));
		projectDetails.setTotalRecord(rs.getInt("TOTAL_RECORD"));
		return projectDetails;
	}

}
