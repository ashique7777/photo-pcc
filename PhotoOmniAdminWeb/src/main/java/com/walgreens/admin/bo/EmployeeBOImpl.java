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

package com.walgreens.admin.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.admin.dao.EmployeeDAO;
import com.walgreens.admin.factory.AdminDAOFactory;
import com.walgreens.admin.json.bean.EmployeeDataRequest;
import com.walgreens.admin.json.bean.EmployeeDataResponse;

/**
 * {@link EmployeeBOImpl} is a business implementation class for {@link EmployeeBO}
 * This class is used to process employee related information to DAO layer.
 * This class using for the business logic.
 * @since v1.0
 */

@Component("EmployeeBO")
public class EmployeeBOImpl  implements EmployeeBO{

	@Autowired
	private AdminDAOFactory adminDAOFactory; //Use to call the DAO factory class

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeBOImpl.class);

	/**
	 * Method to update the Employee informations like EmployeeId, FirstName, LastName etc...
	 * @param objEmpJsonRequest contains MessageHeader and List of Employee informations like EmployeeId, FirstName, LastName etc... in JSON  
	 * @return EmployeeDataResponse MessageHeader,ErrorDetails, Status true if Employee Details updated else false.
	 * @since v1.0
	 */
	@Override
	public EmployeeDataResponse updateEmployeeDetails(EmployeeDataRequest objEmpJsonRequest) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into EmployeeBOImpl.updateEmployeeDetails().");
		}
		EmployeeDAO employeeDAO = null;
		EmployeeDataResponse objEmployeeDataResponse = null;
		try{
			employeeDAO = adminDAOFactory.getEmployeeDAO();
			objEmployeeDataResponse = employeeDAO.updateEmployeeDetails(objEmpJsonRequest);
		}catch (Exception e) {
			LOGGER.error("Exception while updtaing employee details in EmployeeBOImpl >>----> " + e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from  EmployeeBOImpl.updateEmployeeDetails().");
			}
		}
		return objEmployeeDataResponse;
	}
}