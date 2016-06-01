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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bean.EmployeeData;
import com.walgreens.admin.json.bean.EmployeeDataRequest;
import com.walgreens.admin.json.bean.EmployeeDataResponse;
import com.walgreens.admin.utility.EmployeeDataQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;

/**
 * {@link EmployeeDAOImpl} is a DAO implementation class for {@link EmployeeDAO}
 * This class is used to process employee related database transactions like saving or updating Employee  details. 
 * This class uses  jdbcTemplate object to interact with database.
 * @since v1.0
 */

@Component("EmployeeDAO")
public class EmployeeDAOImpl implements EmployeeDAO {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDAOImpl.class);
	ErrorDetails objErrorDetails = null;

	/**
	 * Method to update the Employee informations like EmployeeId, FirstName, LastName etc...
	 * @param objEmployeeRequestBean contains MessageHeader and List of Employee informations like EmployeeId, FirstName, LastName etc...  
	 * @return EmployeeDataResponse MessageHeader,ErrorDetails, Status true if Employee Details updated else false.
	 * @throws PhotoOmniException 
	 * @since v1.0
	 */
	@Override
	public EmployeeDataResponse updateEmployeeDetails(EmployeeDataRequest objEmployeeRequestBean){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into EmployeeDAOImpl.updateEmployeeDetails().");
		}
		objErrorDetails = null;
		List<EmployeeData> lstEmpDetails ;
		EmployeeDataResponse objEmpDataReasponse;
		boolean bStatus = false;
		int iCount;
		long lSysUserID; 
		final String strUserTblInQuery = EmployeeDataQuery.insertQueryUserRef().toString();
		String strUserAttrTblInQuery = EmployeeDataQuery.insertQueryUserAttributeRef().toString();
		String strUserTblUpdtQuery =  EmployeeDataQuery.updateQueryUserRef().toString();
		String strUserAttrTblUpdtQuery = EmployeeDataQuery.updateQueryUserAttributeRef().toString();

		lstEmpDetails = objEmployeeRequestBean.getEmployeeDetails();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("checking EmployeeDataRequest  List size >>>>->."+lstEmpDetails.size());
			LOGGER.debug("InsertQuery for Om_USer table >>>>->."+ strUserTblInQuery);
			LOGGER.debug("Insert Query for Om_USer_Attribute table >>>>->."+ strUserAttrTblInQuery);
			LOGGER.debug("Updte Query for Om_USer_Attribute table >>>>->."+ strUserAttrTblUpdtQuery);
		}

		objEmpDataReasponse = new EmployeeDataResponse();
		MessageHeader objMessageHeader = objEmployeeRequestBean.getMessageHeader();
		try {
			for (EmployeeData objEmployee : lstEmpDetails) {

				if(objEmployee.getEmployeeId() == null){
					break;
				}
				String strUserExistsQuery = EmployeeDataQuery.checkUserExists(objEmployee.getEmployeeId()).toString().toUpperCase();
				iCount = jdbcTemplate.queryForObject(strUserExistsQuery, Integer.class);
				if(iCount == 0){

					String strPKQuery = EmployeeDataQuery.getOMUSerPrimaryKey();
					lSysUserID = jdbcTemplate.queryForObject(strPKQuery, Long.class);

					Object[] objUserParms = new Object[] {lSysUserID, objEmployee.getAuthenticatorId(),
							PhotoOmniConstants.DEFUALT_USER_ID, 
							PhotoOmniConstants.DEFUALT_USER_ID};
					jdbcTemplate.update(strUserTblInQuery, objUserParms);

					Object[] objUserAttrParms = new Object[] {lSysUserID, 
							(objEmployee.getPhotoPmEligibleInd() != null) ? ("Y").equals(objEmployee.getPhotoPmEligibleInd()) ? 1 : 0  : objEmployee.getPhotoPmEligibleInd(), 
							(objEmployee.getStoreEmployeeInd() != null) ? ("Y").equals(objEmployee.getStoreEmployeeInd()) ? 1: 0 : objEmployee.getStoreEmployeeInd(), 
							objEmployee.getEmployeeId().toUpperCase(), objEmployee.getFirstName(), objEmployee.getLastName(), "US", 
							PhotoOmniConstants.DEFUALT_USER_ID,  
							PhotoOmniConstants.DEFUALT_USER_ID};
					jdbcTemplate.update(strUserAttrTblInQuery, objUserAttrParms);

					bStatus = true;

				}else{

					Object[] objUserParms = new Object[] {objEmployee.getAuthenticatorId(), objEmployee.getEmployeeId().toUpperCase()};
					jdbcTemplate.update(strUserTblUpdtQuery, objUserParms);

					Object[] objUserAttrParms = new Object[] {
							(objEmployee.getPhotoPmEligibleInd() != null) ? ("Y").equals(objEmployee.getPhotoPmEligibleInd()) ? 1 : 0  : objEmployee.getPhotoPmEligibleInd(), 
							(objEmployee.getStoreEmployeeInd() != null) ? ("Y").equals(objEmployee.getStoreEmployeeInd()) ? 1: 0 : objEmployee.getStoreEmployeeInd(), 
							objEmployee.getFirstName(), objEmployee.getLastName(), objEmployee.getEmployeeId().toUpperCase()};
					jdbcTemplate.update(strUserAttrTblUpdtQuery, objUserAttrParms);

					bStatus = true;
				}
			}
		}catch(DataAccessException e){
			LOGGER.error("Exception while updtaing employee details in EmployeeDAOImpl1 >>----> " + e);
			bStatus = false; 
			objErrorDetails = CommonUtil.createFailureMessageForDBException(e);
		}catch(NullPointerException e){
			LOGGER.error("Exception while updtaing employee details in EmployeeDAOImpl2 >>----> " + e);
			bStatus = false; 
			objErrorDetails = CommonUtil.createFailureMessageForDBException(e);
		}catch (Exception e) {
			LOGGER.error("Exception while updtaing employee details in EmployeeDAOImpl3 >>----> " + e);
			bStatus = false; 
			objErrorDetails = CommonUtil.createFailureMessageForSystemException(e);
		}
		objEmpDataReasponse.setStatus(bStatus);
		objEmpDataReasponse.setMessageHeader(objMessageHeader);
		if(objErrorDetails != null) {
			objEmpDataReasponse.setErrorDetails(objErrorDetails);
			objEmpDataReasponse.setStatus(false);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from EmployeeDAOImpl.updateEmployeeDetails().");
		}
		return objEmpDataReasponse;
	}
}