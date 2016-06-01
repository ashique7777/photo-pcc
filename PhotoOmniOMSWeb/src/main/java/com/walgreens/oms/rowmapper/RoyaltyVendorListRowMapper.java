package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.VendorType;

public class RoyaltyVendorListRowMapper implements RowMapper<VendorType>{

	public VendorType mapRow(ResultSet rs, int rowNum) throws SQLException {
		VendorType objVendorType = new VendorType();
		objVendorType.setSysVendorId(rs.getInt("SYS_VENDOR_ID"));
		objVendorType.setDescription(rs.getString("DESCRIPTION"));
		
		return objVendorType;
	}

}
