package com.walgreens.oms.dao;

import com.walgreens.common.exception.PhotoOmniException;
/**
 * @author CTS
 * @version 1.1 mar 17, 2015
 *
 */

public interface LicenseReportFilterDAO {

	public int submitLicenseReportFilterRequest(final String strReportData, final int sysReportId) throws PhotoOmniException;
	
	public Integer getLastEnteredDataForLicenseCont(final int reportId) throws PhotoOmniException;
	
	public int getSysUserId() throws PhotoOmniException;
	
	public Integer getReportIdForAdhocAndException() throws PhotoOmniException;

}
