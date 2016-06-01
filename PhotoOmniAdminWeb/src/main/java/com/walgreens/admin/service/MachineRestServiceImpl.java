/**
 * MachineRestServiceImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *----------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *----------------------------------------------------------------------
 *  <1.1>     		 12 Jan 2015
 *  
 **/
package com.walgreens.admin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.walgreens.admin.bo.MachineBO;
import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.admin.json.bean.MachineDataResponse;
import com.walgreens.admin.json.bean.MachineDowntimeRequest;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is used to call for Machine Downtime Records insertion or updation
 * 
 * @author CTS
 * 
 */
@RestController
@RequestMapping(value = "/machines", method = RequestMethod.POST)
@Scope("request")
public class MachineRestServiceImpl implements MachineRestService {

	
	@Autowired
	/*
	 * @adminBOFactory
	 */
	private AdminBOFactory adminBOFactory; 

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MachineRestServiceImpl.class);

	/**
	 * This method is use for machine downtime insertion or updation at Central
	 * 
	 * @param strValueOfJson
	 */
	@RequestMapping(value = "/down_time/update",produces={"application/json"}, method = RequestMethod.POST)
	@Override
	public @ResponseBody
	MachineDataResponse updateMachineDwntmDetails(
			@RequestBody final MachineDowntimeRequest machineRequest) throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering machineDwntmDetails method of MachineRestServiceImpl ");
		}
		MachineDataResponse mdResponse = null;
		try {
			LOGGER.debug("MachineDowntime Service machine dowmtime details process start..");
			 final MachineBO machineBO = adminBOFactory.getMachineBO();
			mdResponse = machineBO.updateMachineDwntmDetails(machineRequest);
			

		} catch (PhotoOmniException e) {
			if(LOGGER.isErrorEnabled()){
			LOGGER.error("PhotoOmniException occoured at updateMachineDowntimeDetails method of MachineRestServiceImpl -"
					, e);
			}
		} catch (Exception e) {
			if(LOGGER.isErrorEnabled()){
			LOGGER.error(" Exception occoured at updateMachineDowntimeDetails method of MachineRestServiceImpl - "
					, e);
			}

		} finally {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting updateMachineDowntimeDetails method of MachineRestServiceImpl");
			}
		}
		return mdResponse;
	}
}
