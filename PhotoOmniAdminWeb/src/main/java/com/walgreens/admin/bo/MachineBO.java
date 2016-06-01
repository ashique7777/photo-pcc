package com.walgreens.admin.bo;

import com.walgreens.admin.json.bean.MachineDataResponse;
import com.walgreens.admin.json.bean.MachineDowntimeRequest;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 *
 */
public interface MachineBO {

	public MachineDataResponse updateMachineDwntmDetails(MachineDowntimeRequest machineRequest)throws PhotoOmniException;
}
