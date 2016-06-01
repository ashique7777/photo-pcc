/**
 * MachineReportRowMapper.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		04 March 2015
*  
**/
package com.walgreens.admin.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.admin.bean.StoreResBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to save result set data.
 * @author CTS
 * @version 1.1 March 04, 2015
 *
 */
public class StoreRowMapper implements RowMapper<StoreResBean>{

	@Override
	public StoreResBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		StoreResBean storeResBean = new StoreResBean();
		storeResBean.setStoreNumber(CommonUtil.bigDecimalToLong(rs.getString("STORE_NUMBER")));
		return storeResBean;
	}

}
