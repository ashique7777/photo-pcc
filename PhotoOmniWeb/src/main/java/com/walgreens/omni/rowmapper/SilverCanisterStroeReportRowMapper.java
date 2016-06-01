package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.json.bean.SilverCanisterStoreDetails;

@SuppressWarnings("rawtypes")
public class SilverCanisterStroeReportRowMapper implements RowMapper{

	@Override
	public SilverCanisterStoreDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SilverCanisterStoreDetails silverCanisterStoreDetails = new SilverCanisterStoreDetails();
		
		silverCanisterStoreDetails.setLastCanisterChangeDate(rs.getDate("CANISTER_END_DTTM"));
		silverCanisterStoreDetails.setServiceDescription(rs.getString("SERVICE_DESCRIPTION"));
		silverCanisterStoreDetails.setSilverCompany(rs.getString("SILVER_COMPANY"));
		silverCanisterStoreDetails.setTotalRowCount(rs.getString("TOTAL_ROWS"));
		silverCanisterStoreDetails.setStoreAddress(rs.getString("LOCATION_ADDRESS"));
		
		return silverCanisterStoreDetails;
	}

}
