package com.walgreens.omni.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;

public interface SubmitReportRequest {
	
	/**
	 * @return SimRetailBlockOnloadResp
	 */
	public @ResponseBody SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest();

	/**
	 * @param file
	 * @param retailBlock
	 * @param locationType
	 * @param number
	 * @param request
	 * @return
	 */
	public @ResponseBody
	SimRetailBlockReportRespMsg genarateSimRetailBlockReport(
			@RequestParam("file") MultipartFile file,
			@RequestParam("retailBlock") String retailBlock,
			@RequestParam("locationType") String locationType,
			@RequestParam("number") String number,
			@RequestParam("pageNo") String pageNo,
			@RequestParam("sortColumnName") String sortColumnName,
			@RequestParam("sortOrder") String sortOrder,
			HttpServletRequest request); 
	
	/**
	 * @param simRetailBlockUpdateReqMsg
	 * @return SimRetailBlockUpdateRespMsg
	 */
	public @ResponseBody
	SimRetailBlockUpdateRespMsg  updateRetailBlockRequest(
			@RequestBody SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg);
	
	
	/**
	 * @param file
	 * @param retailBlock
	 * @param locationType
	 * @param number
	 * @param request
	 * @return ModelAndView
	 * 
	 */
	public @ResponseBody
	ModelAndView downloadSimRetailBlockCSV(
			@RequestParam("file") MultipartFile file,
			@RequestParam("retailBlock") String retailBlock,
			@RequestParam("locationType") String locationType,
			@RequestParam("number") String number,
			@RequestParam("pageNo") String pageNo,
			@RequestParam("sortColumnName") String sortColumnName,
			@RequestParam("sortOrder") String sortOrder,
			HttpServletRequest request);

}
