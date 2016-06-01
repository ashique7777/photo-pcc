package com.walgreens.omni.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.walgreens.omni.json.bean.SilverCanisterReportRepMsg;
import com.walgreens.omni.json.bean.SilverCanisterReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStoreReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepMsg;
import com.walgreens.omni.json.bean.VendorUpdSilverCanisterDetailResMsg;

public interface ReportsRestService {

	

	/**
	 * Method used to Generate silver canister report.
	 * 
	 * @param SilverCanisterReportReqMsg
	 * @return SilverCanisterReportRepMsg
	 */
	public @ResponseBody
	SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			@RequestBody SilverCanisterReportReqMsg reqParam);

	/**
	 * Method used to download silver canister report.
	 * 
	 * @param HttpServletResponse
	 * @param SilverCanisterReportReqMsg
	 */
	public @ResponseBody
	ModelAndView downloadSilverCanisterReportCSV(HttpServletResponse response,
			@RequestParam("silverCanisterFilter") final String silverCanisterFilter);


	/**
	 * Method used to upload silver canister report.
	 * 
	 * @param file
	 * @param vendorName
	 * @param request
	 * @return
	 */
	public @ResponseBody
	VendorUpdSilverCanisterDetailResMsg uploadSilverCanisterCSVFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam("vendorName") String vendorName,
			HttpServletRequest request);

	/**
	 * Method used to download silver canister downloadFailureSilverCanisterCSV
	 * report.
	 * 
	 * @param response
	 * @param reqParam
	 */
	public ModelAndView downloadFailureSilverCanisterCSV(HttpServletResponse response,
			VendorUpdSilverCanisterDetailResMsg reqParam);

	/**
	 * Method used to Generate silver canister Store report
	 * 
	 * @param reqParam
	 * @return
	 */
	public @ResponseBody
	SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			@RequestBody SilverCanisterStoreReportReqMsg reqParam);
	

	/**
	 * @param response
	 * @param filter
	 * @return
	 */
	ModelAndView downloadCannedReportCSV(HttpServletResponse response,
			String filter);
}
