/**
 * MachineBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		12 Jan 2015
*  
**/
package com.walgreens.admin.bo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bean.MachineDowntimeBean;
import com.walgreens.admin.dao.MachineDAO;
import com.walgreens.admin.factory.AdminDAOFactory;
import com.walgreens.admin.json.bean.MachineDataResponse;
import com.walgreens.admin.json.bean.MachineDowntimeRequest;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;


/**
 * This class is used to implement Business validation as per action 
 * @author CTS
 *
 */

@Component("MachineDowntimeBO")
public class MachineBOImpl implements MachineBO {

	/*
	 * adminDAOFactory to call DAO object
	 */
	@Autowired
	private AdminDAOFactory adminDAOFactory;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MachineBOImpl.class);
	/**
	 * This method is used for create request bean as 
	 * per json message for machine downtime details
	 * @param strJson
	 */
	@Override
	public MachineDataResponse updateMachineDwntmDetails(final MachineDowntimeRequest machineRequest) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering updateMachineDwntmDetails method of MachineBOImpl ");
		}
		MachineDAO machineDAO = adminDAOFactory.getMachineDwntmDao();
		MachineDataResponse machineResponse = new MachineDataResponse();
		boolean status = false;
		boolean validationStatus = false;
		String validationMsg = "";
		ErrorDetails errorDetails = new ErrorDetails();
		try {
			final List<MachineDowntimeBean> machineReqList = machineRequest.getMachineDowntimeList();
			final MessageHeader message = machineRequest.getMessageHeader();
			machineResponse.setMessageHeader(message);
				if (!machineReqList.isEmpty()) {
					status = machineDAO.updateMachineDowntimeDtls(machineReqList);
				} else {
					validationStatus = true;
					validationMsg = "MachineDowntimeList is Empty ";
				}
		} catch (PhotoOmniException e) {
			status = false;
			errorDetails = CommonUtil.createFailureMessageForSystemException(e);
			if(LOGGER.isErrorEnabled()){
			LOGGER.error(" PhotoOmniException occoured at updateMachineDwntmDetails method of MachineBOImpl - ", e);
			}
		} catch (Exception e) {
			if(LOGGER.isErrorEnabled()){
			LOGGER.error(" Exception occoured at updateMachineDwntmDetails method of MachineBOImpl - ", e);
			}
			status = false;
			if (validationStatus){
				errorDetails = CommonUtil.createFailureMessageForValidationException(validationMsg);
			} else {
			errorDetails = CommonUtil.createFailureMessageForDBException(e);
			}
		}  finally {
			if (status) {
				machineResponse.setStatus(status);
				machineResponse.setErrorDetails(null);
			} else {
				machineResponse.setStatus(status);
				machineResponse.setErrorDetails(errorDetails);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting updateMachineDwntmDetails method of MachineBOImpl ");
			}
		}
		return machineResponse;
	}
}
