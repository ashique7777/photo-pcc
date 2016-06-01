package com.walgreens.admin.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.admin.json.bean.MachineDataResponse;
import com.walgreens.admin.json.bean.MachineDowntimeRequest;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * @author CTS
 * 
 */

public interface MachineRestService {

	public @ResponseBody
	MachineDataResponse updateMachineDwntmDetails(@RequestBody MachineDowntimeRequest machineRequest) throws PhotoOmniException;
}
