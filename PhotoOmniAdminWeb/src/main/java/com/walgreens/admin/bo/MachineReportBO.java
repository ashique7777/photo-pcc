/**
 * 
 */
package com.walgreens.admin.bo;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.json.bean.MachineData;
import com.walgreens.admin.json.bean.MachineReportBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 * 
 */
public interface MachineReportBO {

	public MachineData getReportMachineTypDetails(final String storeNumber) throws PhotoOmniException;

	public MachineReportBean submitReportRequest(final MachineFilter reqBean) throws PhotoOmniException;
	
	public MachineReportBean submitExportRequest(final MachineFilter reqBean) throws PhotoOmniException;

}
