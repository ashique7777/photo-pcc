package com.walgreens.oms.bo;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.LicenseContentFilter;
import com.walgreens.oms.bean.LicenseContentFilterRespBean;

/**
 * @author CTS
 * @version 1.1 mar 17, 2015
 * 
 */
public interface LicenseReportFilterBO {

	public LicenseContentFilterRespBean submitLicenseReportFilterRequest(final LicenseContentFilter reqBean) throws PhotoOmniException;

}
