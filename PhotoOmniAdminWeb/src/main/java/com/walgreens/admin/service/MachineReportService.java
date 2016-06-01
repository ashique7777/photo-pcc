package com.walgreens.admin.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.walgreens.admin.bean.MachineReportReqBean;
import com.walgreens.admin.bean.MachineTypeReqBean;
import com.walgreens.admin.json.bean.MachineData;
import com.walgreens.admin.json.bean.MachineReportBean;


public interface MachineReportService { 
	
	public @ResponseBody MachineData getReportMachineTypDetails(@RequestBody final MachineTypeReqBean reqParams); 
	
	public @ResponseBody MachineReportBean submitReportRequest(@RequestBody final MachineReportReqBean reqParams);
	
	public void submitExportRequest(@RequestParam("machineFilter") final String machineFilter, final ModelMap model, final HttpServletRequest request, final HttpServletResponse response);
}