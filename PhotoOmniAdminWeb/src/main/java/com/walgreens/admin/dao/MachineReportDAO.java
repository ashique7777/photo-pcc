package com.walgreens.admin.dao;

import java.util.List;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.bean.MachineReportResBean;
import com.walgreens.admin.json.bean.MachineType;
import com.walgreens.common.exception.PhotoOmniException;

public interface MachineReportDAO {
	
	public List<MachineType> getReportMachineTypDetails(String storeNumber) throws PhotoOmniException;

	public List<MachineReportResBean> submitReportRequest(MachineFilter reqBean) throws PhotoOmniException;
	
	public String getLocationAddress(String locId) throws PhotoOmniException;

}
