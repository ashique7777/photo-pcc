/**
 * DailyMSSRowMapper.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		17 August 2015
*  
**/
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.walgreens.batch.central.bean.DailyMSSDatabean;


/**
 * @author CTS
 *
 */

public class DailyMSSRowMapper implements RowMapper<DailyMSSDatabean> {

	@Override
	public DailyMSSDatabean mapRow(ResultSet rs, int rowNum) throws SQLException {
		DailyMSSDatabean  mssFeedDatabean = new DailyMSSDatabean();
		
		mssFeedDatabean.setMssFeed(rs.getString("MSS_FEED"));
		
		return mssFeedDatabean;
	}
}
