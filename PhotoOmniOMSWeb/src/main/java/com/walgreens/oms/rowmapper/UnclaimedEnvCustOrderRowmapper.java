/**
 * UnclaimedEnvCustOrderRowmapper.java 
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
import com.walgreens.oms.bean.UnclaimedEnvCustOrderRespBean;
/**
 * This class is used as a row mapper for unclaimed envelope.
 * @author CTS
 * @version 1.1 April 27, 2015
 */
	public class  UnclaimedEnvCustOrderRowmapper implements RowMapper<UnclaimedEnvCustOrderRespBean> {
		
		public UnclaimedEnvCustOrderRespBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			UnclaimedEnvCustOrderRespBean orderResponse = new UnclaimedEnvCustOrderRespBean();
			orderResponse.setEnvelopeNo(rs.getString("ENVELOPE_NUMBER"));
			orderResponse.setType(rs.getString("PROCESSING_TYPE_CD"));
			orderResponse.setEnvelopeDesc(rs.getString("ORDER_DESCRIPTION"));
			orderResponse.setStatus(rs.getString("STATUS"));
			orderResponse.setPromiseTime(rs.getString("PROMISE_DELIVERY_DTTM"));
			orderResponse.setException(CommonUtil.bigDecimalToLong(rs.getString("EXCEPTION_CD")) != null 
					? CommonUtil.bigDecimalToLong(rs.getString("EXCEPTION_CD")).toString() : "");
			orderResponse.setOrderDate(rs.getString("ORDER_PLACED_DTTM"));
			orderResponse.setFinishDate(rs.getString("ORDER_COMPLETED_DTTM"));
			orderResponse.setSoldDate(rs.getString("ORDER_SOLD_DTTM"));
			orderResponse.setOrderId(CommonUtil.bigDecimalToLong(rs.getString("SYS_ORDER_ID")) != null 
					? CommonUtil.bigDecimalToLong(rs.getString("SYS_ORDER_ID")).toString() : "");
			orderResponse.setTotalEnvelope(rs.getString("TOTALENVELOPES"));
			orderResponse.setCustomerName(rs.getString("CUSTOMER_NAME"));
			orderResponse.setPhoneNo(rs.getString("PHONE_NBR"));
			orderResponse.setStoreNumber(rs.getString("LOCATION_NBR"));
			
			return orderResponse;
		}
}
