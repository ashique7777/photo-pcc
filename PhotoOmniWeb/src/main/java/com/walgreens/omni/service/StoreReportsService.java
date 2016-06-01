package com.walgreens.omni.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public interface StoreReportsService {
	
	public String showPMBYEmpReport(ModelMap model, HttpServletRequest request);

	public String submitSilverCanisterStoreReportRequest(ModelMap model,
			HttpServletRequest request);

	public String submitMachineDownTimeStoreReportRequest(ModelMap model, HttpServletRequest request);

	public String submitLateEnvReportRequest(ModelMap model, HttpServletRequest request);
	
	public String submitPayOnFulfillmentStoreReportRequest(ModelMap model, HttpServletRequest request);

	public String submitUnclaimedOrdersReportRequest(ModelMap model, HttpServletRequest request);
	
	public String submitExceptionStoreReportRequest(ModelMap model, HttpServletRequest request);

	public String submitPayOnFulfillmentApproveVendorReportRequest(ModelMap model, HttpServletRequest request);
}
