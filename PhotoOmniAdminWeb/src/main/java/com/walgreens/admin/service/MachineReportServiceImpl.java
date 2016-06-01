/**
 * MachineReportServiceImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 22 Jan 2015
 *  
 **/

package com.walgreens.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.bean.MachineReportReqBean;
import com.walgreens.admin.bean.MachineTypeReqBean;
import com.walgreens.admin.bo.MachineReportBO;
import com.walgreens.admin.excel.MachineReportExcel;
import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.admin.json.bean.MachineData;
import com.walgreens.admin.json.bean.MachineReportBean;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to call for machine down time report.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 Jan 22, 2015
 * 
 */

@RestController
@RequestMapping(value = "/machines", method = RequestMethod.POST)
@Scope("request")
public class MachineReportServiceImpl implements MachineReportService {
	 
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineReportServiceImpl.class);
	
	/**
	 * adminBOFactory
	 */
	@Autowired
	private AdminBOFactory adminBOFactory;
		
	/**
	 * This method is used to get machine type list.
	 * @param model.
	 * @return machineDataList.
	 */
	@RequestMapping(value = "/machine_type", method = RequestMethod.POST)
	@Override
	public @ResponseBody MachineData getReportMachineTypDetails(@RequestBody final MachineTypeReqBean reqParams) {
		 if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportMachineTypDetails method of MachineReportServiceImpl ");
		}
		MachineData machineDataList = null;
		String storeNumber = null;
		try {
			if (!CommonUtil.isNull(reqParams.getStoreNbr()) && !"".equals(reqParams.getStoreNbr())) {
				storeNumber = reqParams.getStoreNbr();
			}
			final MachineReportBO machineReportBO = adminBOFactory.getMachineReportBO();
			machineDataList = machineReportBO.getReportMachineTypDetails(storeNumber);
			machineDataList.setMessageHeader(reqParams.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportServiceImpl - ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportMachineTypDetails method of MachineReportServiceImpl ");
			}
		}
		return machineDataList;
	}
	
	
	
	/**
	 * This method is used to get machine down time report data.
	 *  @param reqParams contains front end request parameters..
	 *  @return machineReportBean.
	 */
	@RequestMapping(value = "/down_time", method = RequestMethod.POST)
	@Override
	public @ResponseBody MachineReportBean submitReportRequest(@RequestBody final MachineReportReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering submitReportRequest method of MachineReportServiceImpl ");
		}
		MachineReportBean machineReportBean = null;
		try {
			final MachineFilter filter = reqParams.getFilter();
			final MachineReportBO machineReportBO = adminBOFactory.getMachineReportBO();
			machineReportBean = machineReportBO.submitReportRequest(filter);
			machineReportBean.setMessageHeader(reqParams.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitReportRequest method of MachineReportServiceImpl - ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of MachineReportServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of MachineReportServiceImpl ");
			}
		}
		return machineReportBean;
	}
	
	
	/**
	 * This method use to export Excel for machine down time report. 
	 * @param machineFilter front end parameters.
	 * @param model Spring MVC parameter.
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/machine-export", method = RequestMethod.GET)
	@Override
	public void submitExportRequest(@RequestParam("machineFilter") final String machineFilter, 
			final ModelMap model, final HttpServletRequest request, final HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
		    LOGGER.debug(" Entering submitExportRequest method of AdhocAndExceptionReportServiceImpl ");
		}
		MachineReportBean machineReportBean = null;
		try {
			final JSONParser parser = new JSONParser();
			final Object obj = parser.parse(machineFilter);
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject = (JSONObject) jsonObject.get("filter");
			
			/*Putting the request values to a request bean*/
			final MachineFilter machineFilterBean = new MachineFilter();
			if (!CommonUtil.isNull(jsonObject.get("startDate")) ) {
				machineFilterBean.setStartDate(jsonObject.get("startDate").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("endDate")) ) {			
				machineFilterBean.setEndDate(jsonObject.get("endDate").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("machineId"))){
				machineFilterBean.setMachineId(jsonObject.get("machineId").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("machine"))){
				machineFilterBean.setMachineName(jsonObject.get("machine").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("storeNumber"))){
				machineFilterBean.setStoreId(jsonObject.get("storeNumber").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("sortColumnName"))){
				machineFilterBean.setSortColumnName(jsonObject.get("sortColumnName").toString());
			}
			if (!CommonUtil.isNull(jsonObject.get("sortOrder"))){
				machineFilterBean.setSortOrder(jsonObject.get("sortOrder").toString());
			}
			final MachineReportBO machineReportBO = adminBOFactory.getMachineReportBO();
			machineReportBean = machineReportBO.submitExportRequest(machineFilterBean);
			final MachineReportExcel machineReportExcel = new MachineReportExcel();
			machineReportExcel.buildExcelDocument(request, response, machineReportBean, machineFilterBean);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitExportRequest method of AdhocAndExceptionReportServiceImpl - ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitExportRequest method of AdhocAndExceptionReportServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(" Exiting submitExportRequest method of AdhocAndExceptionReportServiceImpl ");
			}
		}
		//return new ModelAndView("machineReportExcelView");
	}
	

	/**
	 * This method use to create print data for the machine down time report store. 
	 * @param reqParams front end parameter.
	 * @return machineReportBean
	 */
	@RequestMapping(value = "/machine-store-print", method = RequestMethod.POST)
	public @ResponseBody MachineReportBean submitPrintRequestFromStore(@RequestBody final MachineReportReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering submitPrintRequestFromStore method of MachineReportServiceImpl ");
		}
		MachineReportBean machineReportBean = null;
		try {
			final MachineFilter filter = reqParams.getFilter();
			final MachineReportBO machineReportBO = adminBOFactory.getMachineReportBO();
			machineReportBean = machineReportBO.submitExportRequest(filter);
			machineReportBean.setMessageHeader(reqParams.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.info(" Error occoured at submitPrintRequestFromStore method of MachineReportServiceImpl - ");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitPrintRequestFromStore method of MachineReportServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitPrintRequestFromStore method of MachineReportServiceImpl ");
			}
		}
		return machineReportBean;
	}
	

}
