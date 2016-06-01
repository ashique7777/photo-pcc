/**
 * OrderUtilQuery.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 4 mar 2015
 *  
 **/
package com.walgreens.batch.query;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class is a query creation class for Order Util.
 * @author CTS
 * @version 1.1 March 04, 2015
 */
public class OrderUtilQuery {
	
	
	/**
	 * This method create the sql Query to get Email id from OM_REPORT_EMAIL_CONFIG table.
	 * @return StringBuilder.
	 */
	public static StringBuilder getFromEmailId() {
		StringBuilder strQuery = new StringBuilder();
		strQuery.append(" SELECT SYS_REPORT_EMAIL_CONFIG_ID, SYS_REPORT_ID, EMAIL_ID, CONFIG_NAME FROM ");
		strQuery.append(PhotoOmniDBConstants.OM_REPORT_EMAIL_CONFIG).append(" OM_REPORT_EMAIL_CONFIG "); ;
		strQuery.append(" WHERE UPPER(TRIM(OM_REPORT_EMAIL_CONFIG.CONFIG_NAME))  = ?  AND EMAIL_TYPE = 'FROM' ");
		return strQuery;
	}
	

}
