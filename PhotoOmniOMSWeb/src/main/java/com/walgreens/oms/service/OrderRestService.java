package com.walgreens.oms.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.DailyPLUReqBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.LicenseContentFilterRespBean;
import com.walgreens.oms.bean.LicenseReportReqBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeReqBean;
import com.walgreens.oms.bean.PMByEmployeeResBean;
import com.walgreens.oms.bean.PMByProductRequestBean;
import com.walgreens.oms.bean.PMReportResponseBeanList;
import com.walgreens.oms.bean.PrintSignEventReqBean;
import com.walgreens.oms.bean.PrintSignFilterRespBean;
import com.walgreens.oms.bean.PrintSignReqBean;
import com.walgreens.oms.bean.RoyaltyReportResponseBean;
import com.walgreens.oms.bean.SalesByProductResponseBean;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.bean.UnclaimedEnvReqBean;
import com.walgreens.oms.bean.VendorData;
import com.walgreens.oms.json.bean.ApproveVCDataRequest;
import com.walgreens.oms.json.bean.ApproveVCDataResponse;
import com.walgreens.oms.json.bean.AsnOrderRequest;
import com.walgreens.oms.json.bean.AsnOrderResponse;
import com.walgreens.oms.json.bean.EnvelopeDtlsDataRespBean;
import com.walgreens.oms.json.bean.EnvelopeDtlsRequestBean;
import com.walgreens.oms.json.bean.EventDataFilter;
import com.walgreens.oms.json.bean.ExceptionByEnvResponseBean;
import com.walgreens.oms.json.bean.ExceptionEmployeeReqBean;
import com.walgreens.oms.json.bean.ExceptionEmployeeRespBean;
import com.walgreens.oms.json.bean.ExceptionReasonBean;
import com.walgreens.oms.json.bean.ExceptionRequestBean;
import com.walgreens.oms.json.bean.LCDataRequest;
import com.walgreens.oms.json.bean.LCDataResponse;
import com.walgreens.oms.json.bean.LabelDataRequest;
import com.walgreens.oms.json.bean.LabelDataResponse;
import com.walgreens.oms.json.bean.LateEnvelopeReportReqBean;
import com.walgreens.oms.json.bean.LateEnvelopeReportRespBean;
import com.walgreens.oms.json.bean.OrderDataRequest;
import com.walgreens.oms.json.bean.OrderDataResponse;
import com.walgreens.oms.json.bean.PMBYWICRequestBean;
import com.walgreens.oms.json.bean.PayOnFulfillmentReqBean;
import com.walgreens.oms.json.bean.PayOnFulfillmentResponse;
import com.walgreens.oms.json.bean.PayonFulfillmentData;
import com.walgreens.oms.json.bean.ProductDetails;
import com.walgreens.oms.json.bean.RoyaltyRequestBean;
import com.walgreens.oms.json.bean.SalesByProductRequestBean;
import com.walgreens.oms.json.bean.UnclaimedEnvCustomer;
import com.walgreens.oms.json.bean.UnclaimedResponse;
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;

/**
 * @author CTS Interface is Used to process orders
 * 
 */

public interface OrderRestService {

	/**
	 * Method used to process Order Submission JSON messages
	 * 
	 * @param jsonRequest
	 * @return 
	 */
	public @ResponseBody
	OrderDataResponse submitOrderDetails( 
			@RequestBody OrderDataRequest jsonRequest);

	/**
	 * Method used to process Order Completion JSON messages
	 * 
	 * @param orderJsonMsg
	 * @return
	 */
	public @ResponseBody
	OrderDataResponse completeOrderDetails(
			@RequestBody OrderDataRequest orderJsonMsg);

	/**
	 * Method used to process Order Exception JSON messages
	 * 
	 * @param updateOrderExceptions
	 * @return
	 */

	public @ResponseBody
	OrderDataResponse updateOrderExceptions(
			@RequestBody OrderDataRequest posJsonRequestMsg);

	/**
	 * Method used to process License content data JSON messages
	 * 
	 * @param orderJsonMsg
	 * @return
	 */
	public @ResponseBody
	LCDataResponse updateLicenseContent(@RequestBody LCDataRequest orderJsonMsg);

	public @ResponseBody
	LabelDataResponse updateLabelPrntInfo(
			@RequestBody LabelDataRequest strValueOfJson);

	/**
	 * Method to get details for PM_MBPM by Product Report
	 * 
	 * @param PMByProductRequestBean
	 * @return PMReportResponseBeanList
	 * */
	public @ResponseBody PMReportResponseBeanList submitPMByProductReportRequest( @RequestBody PMByProductRequestBean reqParam);
	
	/**
	 * Method to process ASN details and updte them accordingly
	 * 
	 * @param AsnOrderRequest
	 * @return AsnOrderResponse
	 * */
	
	public @ResponseBody 
	AsnOrderResponse updateASNDetails(@RequestBody AsnOrderRequest asnOrderJsonRequest);

	
	public PMBYWICResponseBean submitPMWICReportRequest(@RequestBody PMBYWICRequestBean pmbywicRequestBean) ;

	public RoyaltyReportResponseBean submitRoyaltyReportRequest(@RequestBody RoyaltyRequestBean royaltyRequestBean);

	public @ResponseBody VendorData getRoyaltyVendorNameList(ModelMap model) ;
	
	public @ResponseBody List<PayonFulfillmentData> getStoreDetails(@RequestParam("storeNumber") String storeNumber);
	
	public List<ProductDetails>  getProductDetails();
	
	
	public @ResponseBody  ModelAndView  submitCSVExportRequest(HttpServletResponse response, @RequestParam("pofDownloadCSVFilter") final String pofDownloadCSVFilter);
	
	public @ResponseBody  PayOnFulfillmentResponse payOnFulfillmentReportRequest(@RequestBody PayOnFulfillmentReqBean reqBean) ;
	
	public @ResponseBody VendorData getPOFVendorNameList(ModelMap model) ;
	
	public @ResponseBody VendorCostValidationReportResponse  fetchVendorPaymentAwaitingApproval(@RequestBody VendorCostValidationReportRequest  params);
	
	public @ResponseBody ApproveVCDataResponse  updateVendorpaymentApproval(@RequestBody ApproveVCDataRequest  params);
	
	public DailyPLUResBean submitPLUReportRequest(DailyPLUReqBean dailyPLUReqBean)
			throws Exception;

	public DailyPLUResBean getPLUList(DailyPLUReqBean dailyPLUReqBean);
	
	
	PMByEmployeeResBean getPMByEmployeeReportDetails(@RequestBody PMByEmployeeReqBean objPMByEmployeeReqBean) throws JsonParseException,
			JsonMappingException, IOException;	
	
	public @ResponseBody String getEmployeeNameStoreAdd(String strEmpORLocID, boolean isEmployeeName);
	
	/*Method for LicensereportFilter and PrintsSignReportFilter*/
	
	public @ResponseBody EventDataFilter getEventTypDetails(@RequestBody PrintSignEventReqBean reqParams);
	
	public @ResponseBody PrintSignFilterRespBean submitPSReportFilterRequest(@RequestBody PrintSignReqBean params);
	
	public @ResponseBody LicenseContentFilterRespBean submitLicenseReportFilterRequest(@RequestBody LicenseReportReqBean reqParams);
	
	public @ResponseBody UnclaimedResponse submitUnclaimedEnvRequest(@RequestBody UnclaimedEnvReqBean reqBean);
	
	public @ResponseBody UnclaimedEnvCustomer unclaimedEnvCustOrderRequest(@RequestBody UnclaimedEnvCustorderReqBean reqBean);
	

	
	/*Method for Exception Report*/

	public @ResponseBody ExceptionByEnvResponseBean submitEnvelopeReportRequest(@RequestBody ExceptionRequestBean  reqParam);
	
	public @ResponseBody ExceptionEmployeeRespBean submitEmployeeReportRequest(@RequestBody ExceptionEmployeeReqBean requestBean);
	
	public @ResponseBody ExceptionReasonBean getReportExceptionReason();
	public SalesByProductResponseBean saveSalesReportByProductFilters(@RequestBody SalesByProductRequestBean salesByProductRequestBean );
	
	
	public @ResponseBody LateEnvelopeReportRespBean fetchLateEnvelopeData(@RequestBody LateEnvelopeReportReqBean reqParams);
	//Method for MBPM
	
	/* public @ResponseBody  boolean MBOrderPromotionalMoney(ModelMap model, HttpServletRequest request);*/
	/**
	 * This method generates Bean for Envelope Details ie Product details and Envelope History
	 * @param EnvelopeDtlsRequestBean reqParams
	 * @return EnvelopeDtlsDataRespBean
	 * @throws PhotoOmniException
	 */
	public @ResponseBody EnvelopeDtlsDataRespBean fetchEnvelopeDtlsData(@RequestBody EnvelopeDtlsRequestBean reqParams);
}
