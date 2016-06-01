/**
 * KronosDataRowMapper.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 2nd September 2015
 *  
 **/
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.KronosDataBean;
import com.walgreens.common.utility.CommonUtil;

/**
 * This is the RowMapper class for kronos data.
 * @author CTS
 *
 */
public class KronosDataRowMapper implements RowMapper<KronosDataBean> {

	@Override
	public KronosDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		KronosDataBean kronosDataBean = new KronosDataBean();
		kronosDataBean.setUnits(!CommonUtil.isNull(rs.getString("MEASURE")) ? rs.getString("MEASURE") : "");
		kronosDataBean.setStoreNumber(!CommonUtil.isNull(rs.getString("LOCATION_NBR")) ? rs.getString("LOCATION_NBR") : "");
		kronosDataBean.setMatrixCode(!CommonUtil.isNull(rs.getString("CATEGORY_NAME")) ? rs.getString("CATEGORY_NAME") : "");
		kronosDataBean.setDate(!CommonUtil.isNull(rs.getString("COMPLETED_DATE")) ? rs.getString("COMPLETED_DATE") : "");
		kronosDataBean.setSlotAndCount(!CommonUtil.isNull(rs.getString("SLOTANDCOUNT")) ? rs.getString("SLOTANDCOUNT") : "");
		kronosDataBean.setDayName(!CommonUtil.isNull(rs.getString("DAYNAME")) ? rs.getString("DAYNAME") : "");
		return kronosDataBean;
	}
}
