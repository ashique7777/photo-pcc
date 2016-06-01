/**
 * UnclaimedEnvRowmapper.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		27 April 2015
 *  
 **/
package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.UnclaimedEnvRespList;
/**
 * This class is used to create Query for Unclaimed envelope Reports.
 * @author CTS
 * @version 1.1 April 27, 2015
 */
public class  UnclaimedEnvRowmapper implements RowMapper<UnclaimedEnvRespList> {
	
	public UnclaimedEnvRespList mapRow(ResultSet rs, int rowNum) throws SQLException {
		UnclaimedEnvRespList unclaimedEnvResponse = new UnclaimedEnvRespList();
		unclaimedEnvResponse.setCustomerId(CommonUtil.bigDecimalToLong(rs.getString("CUSTOMERID")));
		unclaimedEnvResponse.setCustomerName(rs.getString("CUSTOMERNAME"));
		unclaimedEnvResponse.setPhoneNumber(rs.getString("PHONE"));
		unclaimedEnvResponse.setTotalEnvelope(rs.getString("TOTALENVELOPES"));
		unclaimedEnvResponse.setTotalValue(rs.getString("TOTALVALUE"));
		unclaimedEnvResponse.setOrderRangePlaceddate(rs.getString("ORDERDATERANGE"));
		unclaimedEnvResponse.setTotalRecord(rs.getInt("TOTALROWS"));
		
		return unclaimedEnvResponse;
	}

}
