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

package com.walgreens.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.walgreens.admin.bo.EmployeeBO;
import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.admin.json.bean.EmployeeDataRequest;
import com.walgreens.admin.json.bean.EmployeeDataResponse;

/**
 * {@link EmployeeRestServiceImpl} is a Service implementation class for {@link EmployeeRestService}
 * This class is used to process employee related client calls to BO layer.
 * This class using for the client calls as well service layer.
 * @since v1.0
 */

@RestController
@RequestMapping(value ="/employees",method=RequestMethod.POST)
public class EmployeeRestServiceImpl implements EmployeeRestService{

	@Autowired
	private AdminBOFactory adminBOFactory;//Use to call the BO factory class

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeRestServiceImpl.class);

	/**
	 * Method to update the Employee informations like EmployeeId, FirstName, LastName etc...
	 * @param objEmpJsonRequest contains MessageHeader and Employee informations like EmployeeId, FirstName, LastName etc... in JSON  
	 * @return EmployeeDataResponse MessageHeader,ErrorDetails, Status true if Employee Details updated else false.
	 * @since v1.0
	 */
	@RequestMapping(value = "/update", produces={"application/json"}, method = RequestMethod.POST)
	@Override
	@ResponseBody
	public  EmployeeDataResponse updateEmployeeDetails(@RequestBody EmployeeDataRequest objEmpJsonRequest) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into EmployeeRestServiceImpl.updateEmployeeDetails()");
			LOGGER.debug("EmployeeDataRequest :" + objEmpJsonRequest.toString());
		}

		EmployeeDataResponse objEmployeeDataResponse = null;
		try{
			EmployeeBO employeeBO = adminBOFactory.getEmployeeBO();
			objEmployeeDataResponse = employeeBO.updateEmployeeDetails(objEmpJsonRequest);
		}catch(Exception e){
			LOGGER.error("Exception while updtaing employee details in EmployeeRestServiceImpl >>----> " + e);
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from EmployeeRestServiceImpl.updateEmployeeDetails()");
				LOGGER.debug("EmployeeDataResponse : " + objEmployeeDataResponse);
			}
		}
		return objEmployeeDataResponse;
	}
}