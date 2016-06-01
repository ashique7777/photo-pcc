package com.walgreens.omni.dao;

import java.util.Date;
import java.util.List;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.omni.bean.CanisterChangeBean;
import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.bean.SilverRecoveryHeaderDetails;
import com.walgreens.omni.exception.ReportException;
import com.walgreens.omni.json.bean.CannedFilter;
import com.walgreens.omni.json.bean.CannedReportResBean;
import com.walgreens.omni.json.bean.CannedReportResGenericBean;
import com.walgreens.omni.json.bean.InputChannel;
import com.walgreens.omni.json.bean.OrderType;
import com.walgreens.omni.json.bean.SilverCanisterReportRepMsg;
import com.walgreens.omni.json.bean.SilverCanisterReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStoreReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepMsg;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;

public interface ReportsDAO {

	
	/**
	 * 
	 * Method used to Generate silver canister report.
	 * 
	 * @param SilverCanisterReportReqMsg
	 * @return SilverCanisterReportRepMsg
	 * @throws ReportException
	 * @throws PhotoOmniException
	 */
	public SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			SilverCanisterReportReqMsg reqParam, String reportReq)
			throws ReportException, PhotoOmniException;

	/**
	 * Method used to get Silver Recovery Header Details
	 * 
	 * @param storeNo
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public List<SilverRecoveryHeaderDetails> getSilverRecoveryHeaderDetails(
			String storeNo) throws ReportException, PhotoOmniException;

	/**
	 * Method used to update Silver Recovery Header table
	 * 
	 * @param silverRecoveryHeaderDetailsTempOne
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public boolean updateSilverRecHead(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempOne) throws ReportException, PhotoOmniException;

	/**
	 * Method used to insert Silver Recovery Header table
	 * 
	 * @param silverRecoveryHeaderDetailsTempTwo
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public boolean insertSilverRecHead(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempTwo) throws ReportException, PhotoOmniException;

	/**
	 * Method used to insert Canister Change Details in table
	 * 
	 * @param canisterChangeBean
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public boolean insertCanisterChange(CanisterChangeBean canisterChangeBean) throws ReportException, PhotoOmniException;

	/**
	 * Method used to get Silver Received Rolls From Canister Detail
	 * 
	 * @param string
	 * @param sysLocationId
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public double getSilverRecvRollsFromCanisterDetail(String startDate,String endDate,
			int sysLocationId) throws ReportException, PhotoOmniException;

	/**
	 * Method used to get Silver Received Prints From Canister Detail
	 * 
	 * @param string
	 * @param sysLocationId
	 * @return
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public double getSilverRecvPrintsFromCanisterDetail(String startDate,String endDate,
			int sysLocationId) throws ReportException, PhotoOmniException;

	/**
	 * Method used to update Silver Recovery Cd
	 * 
	 * @param string
	 * @throws PhotoOmniException 
	 * @throws ReportException 
	 */
	public void updateSilverRecoveryCd(String string,String statusInd) throws ReportException, PhotoOmniException;

	/**
	 * Method used to update submit Silver Canister Store Report Request
	 * 
	 * @param reqParam
	 * @return
	 * @throws ReportException
	 * @throws PhotoOmniException
	 */
	public SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			SilverCanisterStoreReportReqMsg reqParam) throws ReportException,
			PhotoOmniException;

	/**
	 * @param silverRecoveryHeaderDetails
	 * @param dateSelectedFrom 
	 * @param vendorName 
	 * @param serDescArr 
	 * @param dateNextDate 
	 * @return
	 */
	public boolean processCanisterUploadRequest(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetails, Date dateSelectedFrom, String vendorName, String[] serDescArr, Date dateNextDate) throws ReportException,
			PhotoOmniException;
	
	/**
	 * Method to submit SimRetailBlockOnload Request 
	 * @return SimRetailBlockOnloadResp
	 * @throws PhotoOmniException
	 */
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest() throws PhotoOmniException;

	/**
	 * Method to generate SimRetailBlock Report
	 * @param sortOrder 
	 * @param sortColumnName 
	 * @param pageNo 
	 * @param downLoadCsv 
	 * @return
	 * @throws PhotoOmniException
	 */
	public SimRetailBlockReportRespMsg genarateSimRetailBlockReport(String retailBlock,String locationType,List<String> number, String pageNo, String sortColumnName, String sortOrder, String downLoadCsv) throws PhotoOmniException;

	/**
	 * Method to update RetailBlock Request
	 * @param simRetailBlockUpdateReqMsg
	 * @return SimRetailBlockUpdateRespMsg
	 * @throws PhotoOmniException
	 * 
	 */
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest (SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg) throws  PhotoOmniException;
	
	/**
	 * Phase 2.0 Canned Report Start Canned Report: Method to get the drop down
	 * list populated for order type. The following method "getOrderTypeQuery"
	 * fetches data from the database. Table Name-OM-CODE_DECODE Phase 2.0
	 * Canned Report End
	 */
	public List<OrderType> getCannedReportOrderType() throws PhotoOmniException;

	/**
	 * Canned Report: Method to get the drop down list populated for input type.
	 * The following method "getCannedInputType" fetches data from the database.
	 * Table Name-OM-CODE_DECODE
	 */
	public List<InputChannel> getCannedInputType() throws PhotoOmniException;

	/**
	 * Canned Report:Method to generate report for orderType -
	 * placedOrderByProduct & soldOrderByProduct where input channel type can be
	 * Kiosk,Internet,Mobile or ALL. This method triggers a query which will
	 * fetch data from the database. method used to fetch the
	 * query:getGeneratedCannedReportData.
	 * 
	 * @param reqBean	- contains reqBean data.
	 * @exception custom- exception.
	 */
	public List<CannedReportResBean> generateReportRequest(CannedFilter reqBean)
			throws PhotoOmniException;

	
	/**
	 * Canned Report: This method executes export to csv functionality. Method
	 * getGenerated CannedReportDataCsv Fetches data from the database.
	 */
	public List<CannedReportDataCSVBean> generateCannedReportRequest(
			CannedFilter reqBean, String req) throws PhotoOmniException;

	/**
	 * 
	 * @param reqBean
	 * @return
	 * @throws PhotoOmniException
	 * Fetching data for generic fields 
	 */
	public List<CannedReportResGenericBean> getGenereicFields(CannedFilter reqBean)
			throws PhotoOmniException;

}
