/********************************************************************************/
/* Copyright (c) 2015, Walgreens Co.											*/
/* All Rights Reserved.															*/
/*																				*/
/* THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT			*/
/* HOLDERS MAKE NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED,			*/
/* INCLUDING BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY OR				*/
/* FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE OF THE SOFTWARE			*/
/* OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY PATENTS,					*/
/* COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.										*/
/*																				*/
/* COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,				*/
/* SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE				*/
/* SOFTWARE OR DOCUMENTATION.													*/
/*																				*/
/* The name and trademarks of copyright holders may NOT be used in				*/
/* advertising or publicity pertaining to the software without					*/
/* specific, written prior permission. Title to copyright in this				*/
/* software and any associated documentation will at all times remain			*/
/* with copyright holders.														*/
/*																				*/
/* /*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Version             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		18 Feb 2015           CTS
 *  
/********************************************************************************/

package com.walgreens.admin.utility;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class is used to write employee related Database Query like inserting updating Employee details.
 * @since v1.0
 */
public class EmployeeDataQuery {
	
	private EmployeeDataQuery(){
		
	}

	/**
	 * Method to get Query to check whether user exists or not.
	 * @return StringBuffer SQL select Query.
	 * @since v1.0
	 */
	public static StringBuilder  checkUserExists(String strEmpID) {

		StringBuilder  strQuery = new StringBuilder();
		strQuery.append("SELECT COUNT(1) FROM ");
		strQuery.append(PhotoOmniDBConstants.OM_USER).append(" OMU");
		strQuery.append(" JOIN ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" OMUA");
		strQuery.append(" ON OMUA.SYS_USER_ID = OMU.SYS_USER_ID");
		strQuery.append(" WHERE EMPLOYEE_ID = ");
		strQuery.append("'").append(strEmpID).append("'");

		return strQuery;
	}

	/**
	 * Method to get Primary key from OM_ USer table.
	 * @return String SQL getting Primary Key Query.
	 * @since v1.0
	 */
	public static String  getOMUSerPrimaryKey() {

		return "SELECT OM_USER_SEQ.NEXTVAL FROM DUAL ";
	}

	/**
	 * Method to get insert Query to user table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder insertQueryUserRef() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_USER);
		strQuery.append(" (SYS_USER_ID, AUTHENTICATOR_ID, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM)");
		strQuery.append("values(?, ?, ?, ").append(PhotoOmniConstants.DEFUALT_DTTM).append(", ?, ").append(PhotoOmniConstants.DEFUALT_DTTM).append(")");

		return strQuery;
	}

	/**
	 * Method to get insert Query to user_Attribute table.
	 * @return StringBuffer SQL insert Query.
	 * @since v1.0
	 */
	public static StringBuilder insertQueryUserAttributeRef() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES);
		strQuery.append(" (SYS_USER_ATTRIBUTE_ID, SYS_USER_ID, PM_ELIGIBLE_CD, STORE_EMPLOYEE_CD, EMPLOYEE_ID, FIRST_NAME,");
		strQuery.append(" LAST_NAME, COUNTRY_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID, UPDATE_DTTM )");
		strQuery.append("values(OM_USER_ATTRIBUTES_SEQ.NEXTVAL, ?, ?, ?, ?, ?,");
		strQuery.append(" ?, ?, ?, ").append(PhotoOmniConstants.DEFUALT_DTTM).append(", ?, ").append(PhotoOmniConstants.DEFUALT_DTTM).append(")");

		return strQuery;
	}

	/**
	 * Method to get update Query to user table if record already exists.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuilder updateQueryUserRef() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE ").append(PhotoOmniDBConstants.OM_USER);
		strQuery.append(" SET AUTHENTICATOR_ID = ? , UPDATE_DTTM = ").append(PhotoOmniConstants.DEFUALT_DTTM);
		strQuery.append(" WHERE SYS_USER_ID IN ");
		strQuery.append(" (SELECT SYS_USER_ID  FROM ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES);
		strQuery.append(" WHERE EMPLOYEE_ID = ? )");
		return strQuery;
	}

	/**
	 * Method to get update Query to user Attribute table if record already exists.
	 * @return StringBuffer SQL Update Query.
	 * @since v1.0
	 */
	public static StringBuilder updateQueryUserAttributeRef() {

		StringBuilder strQuery = new StringBuilder();
		strQuery.append("UPDATE ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES);
		strQuery.append(" SET PM_ELIGIBLE_CD = ? , STORE_EMPLOYEE_CD = ? , FIRST_NAME = ?, LAST_NAME = ?");
		strQuery.append(" WHERE EMPLOYEE_ID = ?");

		return strQuery;
	}
}
