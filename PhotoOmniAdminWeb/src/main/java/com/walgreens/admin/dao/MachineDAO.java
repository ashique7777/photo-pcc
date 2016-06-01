package com.walgreens.admin.dao;

import java.sql.SQLException;
import java.util.List;

import com.walgreens.admin.bean.MachineDowntimeBean;
import com.walgreens.common.exception.PhotoOmniException;

public interface MachineDAO {

	public boolean updateMachineDowntimeDtls(
			List<MachineDowntimeBean> machineReqList)
			throws PhotoOmniException, SQLException;

}
