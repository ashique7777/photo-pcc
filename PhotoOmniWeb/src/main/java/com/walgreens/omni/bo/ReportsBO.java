package com.walgreens.omni.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.json.bean.CannedDropDownData;
import com.walgreens.omni.json.bean.CannedFilter;
import com.walgreens.omni.json.bean.CannedReportBean;
import com.walgreens.omni.json.bean.CannedReportGenericBean;
import com.walgreens.omni.json.bean.SilverCanisterReportRepMsg;
import com.walgreens.omni.json.bean.SilverCanisterReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStoreReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepMsg;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;

public interface ReportsBO {

	
	/**
	 * Method used to Generate silver canister report.
	 * 
	 * @param SilverCanisterReportReqMsg
	 * @return SilverCanisterReportRepMsg
	 * @throws PhotoOmniException
	 */
	public SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			SilverCanisterReportReqMsg reqParam, String reqType)
			throws PhotoOmniException;

	/**
	 * Method used to update Silver Canister Details
	 * @param dataMap
	 * @param vendorName
	 * @return
	 */
	public Map<Integer, ArrayList<String>> updateSilverCanisterDetails(
			Map<Integer, ArrayList<String>> dataMap, String vendorName);

	/**
	 * Method used to submit Silver Canister Store Report Request
	 * @param reqParam
	 * @return
	 * @throws PhotoOmniException
	 */
	public SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			SilverCanisterStoreReportReqMsg reqParam) throws PhotoOmniException;
	/**
	 * Method to submit SimRetailBlockOnload Request
	 * @return SimRetailBlockOnloadResp
	 * @throws PhotoOmniException 
	 */
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest() throws PhotoOmniException;

	/**
	 * Method to generate SimRetailBlockReport
	 * @param sortOrder 
	 * @param sortColumnName 
	 * @param pageNo 
	 * @param downLoadCsv 
	 * @return
	 * @throws PhotoOmniException
	 */
	public SimRetailBlockReportRespMsg genarateSimRetailBlockReport(String retailBlock,String locationType,List<String> number, String pageNo, String sortColumnName, String sortOrder, String downLoadCsv) throws PhotoOmniException;

	/**
	 * Method to update RetailBlockRequest
	 * @param simRetailBlockUpdateReqMsg
	 * @return SimRetailBlockUpdateRespMsg
	 */
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest(SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg) throws PhotoOmniException;
	

	/**
	 * Phase 2.0 Canned Report Start :Method to get the drop down list populated
	 * for order type and input channel in UI Phase 2.0 Canned Report End
     * @param cannedOrderTypeList
	 * @param InputChannel	 	
	 * @return cannedDropDownData
         */
	public CannedDropDownData getCannedReportOrderTypeInputChannelRequest()  throws PhotoOmniException;

/**
 * CannedReport: Method to generate report for orderType -
 * placedOrderByProduct & soldOrderByProduct where input channel type can be
 * Kiosk,Internet,Mobile or ALL
 * @param reqBean
 *  Phase 2.0 Canned Report Start: Method to generate report for orderType - placedOrderByProduct & soldOrderByProduct where input channel type can be Kiosk,Internet,Mobile or ALL
 *  Phase 2.0 Canned Report End
 *  @return cannedReportBean
 */
	public CannedReportBean generateReportRequest(CannedFilter filter) throws PhotoOmniException;
	/**
	 * @param reqBean
	 * @param req
	 * Canned Report: This method executes export to csv functionality. 
	 * @return cannedReportDataBeanList
	 */

	java.util.List<CannedReportDataCSVBean> generateCannedReportRequest(
			CannedFilter reqParam, String req) throws PhotoOmniException;

	/**
	 * 
	 * @param filter
	 * @return
	 * @throws PhotoOmniException
	 * This method is used to calculate the generic fields 
	 */
	public CannedReportGenericBean getGenericFields(CannedFilter filter) throws PhotoOmniException;

}



