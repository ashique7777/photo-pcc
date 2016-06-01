package com.walgreens.omni.service;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/storereport")
public class StoreReportsServiceImpl  implements StoreReportsService{

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreReportsServiceImpl.class);
	
	@RequestMapping(value = "/pmbyemp", method = RequestMethod.GET)
	public String showPMBYEmpReport(ModelMap model, HttpServletRequest request) {

			LOGGER.info("Entering into ReportsRestServiceImpl.showPMBYEmpReport()");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
						request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
			}
			model.addAttribute("storeNumber", request.getParameter("storeNumber"));
			model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
			model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));

			return "res/template/report/pmbyemp/pmbyempreport";
	}
	
	/**
	 * This method is used to get store report for silver Canister
	 * @param model.
	 * @param request.
	 */
	@Override
	@RequestMapping(value = "/silverCanisterStoreReportRequest", method = RequestMethod.GET)
	public String submitSilverCanisterStoreReportRequest(
			ModelMap model, HttpServletRequest request) {

		LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));
				
				return "res/template/report/silverCanRprt/silverCanisterReport";
	}
	
	/**
	 * This method is used to get store report for machine down time
	 * @param model.
	 * @param request.
	 */
	@Override
	@RequestMapping(value = "/machineDowntimeStoreReportRequest", method = RequestMethod.GET)
	public String submitMachineDownTimeStoreReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));
				
				return "res/template/report/machine/machinedowntimereport";
	}
	
	/**
	 * This method is used to get store report for Unclaimed orders. 
	 * @param model.
	 * @param request.
	 */
	@Override
	@RequestMapping(value = "/unclaimedOrdersReportRequest", method = RequestMethod.GET)
	public String submitUnclaimedOrdersReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));
				
				return "res/template/report/unclaimedOrderReport/unclaimedOrdersreport";
	}
	
	@Override
	@RequestMapping(value = "/lateEnvlpRprtReq", method = RequestMethod.GET)
	public String submitLateEnvReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				
				return "res/template/report/lateEnvelopeRprt/lateEnvelopeReport";

	}
	@Override
	@RequestMapping(value = "/getPOFVendorNameList", method = RequestMethod.GET)
	public String submitPayOnFulfillmentStoreReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store for payOnFulfillment Store Report :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));
				
				return "res/template/report/payonfulfilment/payonfulfilmentStoreReport";
	}
	@Override
	@RequestMapping(value = "/getPOFApproveVendorNameList", method = RequestMethod.GET)
	public String submitPayOnFulfillmentApproveVendorReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store for payOnFulfillment Store Report :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));
				
				return "res/template/report/payonfulfilment/payonfulfilmentStoreReport";
	}
	
	/**
	 * This method is used to get store report for Exception Report
	 * @param model.
	 * @param request.
	 */
	@Override
	@RequestMapping(value = "/exceptionStoreReportRequest", method = RequestMethod.GET)
	public String submitExceptionStoreReportRequest(ModelMap model, HttpServletRequest request) {
		LOGGER.info("Parameters From Store :  storeNumber :" + request.getParameter("storeNumber") +" selectedEmpId : "+  
				request.getParameter("selectedEmpId") +" storeLevelReport : "+ request.getParameter("storeLevelReport"));
				model.addAttribute("storeNumber", request.getParameter("storeNumber"));
				/*model.addAttribute("selectedEmpId", request.getParameter("selectedEmpId"));
				model.addAttribute("storeLevelReport", request.getParameter("storeLevelReport"));*/
				
				return "res/template/report/exceptionReport/exceptReport";
	}
}
