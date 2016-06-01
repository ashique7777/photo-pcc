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

package com.walgreens.admin.dao;

import com.walgreens.admin.json.bean.EmployeeDataRequest;
import com.walgreens.admin.json.bean.EmployeeDataResponse;

/**
 * This interface is used to process employee related information to EmployeeDAOImpl class.
 * @since v1.0
 */
public interface EmployeeDAO {
	
	/**
	 * Method to update the Employee informations like EmployeeId, FirstName, LastName etc...
	 * @param objEmployeeRequestBean contains MessageHeader and List of Employee informations like EmployeeId, FirstName, LastName etc...  
	 * @return EmployeeDataResponse MessageHeader,ErrorDetails, Status true if Employee Details updated else false.
	 * @since v1.0
	 */
	public EmployeeDataResponse updateEmployeeDetails(EmployeeDataRequest objEmployeeRequestBean);

}
