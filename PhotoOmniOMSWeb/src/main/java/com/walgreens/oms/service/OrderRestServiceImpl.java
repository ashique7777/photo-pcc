/**
. * OrderRestServiceImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		12 Jan 2015
 *  
 **/

package com.walgreens.oms.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.DailyPLUReqBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.LicenseContentFilter;
import com.walgreens.oms.bean.LicenseContentFilterRespBean;
import com.walgreens.oms.bean.LicenseReportReqBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeReqBean;
import com.walgreens.oms.bean.PMByEmployeeResBean;
import com.walgreens.oms.bean.PMByProductFilter;
import com.walgreens.oms.bean.PMByProductRequestBean;
import com.walgreens.oms.bean.PMReportResponseBeanList;
import com.walgreens.oms.bean.POFVendorCostReportMessage;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;
import com.walgreens.oms.bean.PayOnFulfillmentReq;
import com.walgreens.oms.bean.PrintSignEventReqBean;
import com.walgreens.oms.bean.PrintSignFilter;
import com.walgreens.oms.bean.PrintSignFilterRespBean;
import com.walgreens.oms.bean.PrintSignReqBean;
import com.walgreens.oms.bean.RoyaltyReportResponseBean;
import com.walgreens.oms.bean.SalesByProductResponseBean;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.bean.UnclaimedEnvReqBean;
import com.walgreens.oms.bean.VendorData;
import com.walgreens.oms.bo.LicenseReportFilterBO;
import com.walgreens.oms.bo.OrderBO;
import com.walgreens.oms.bo.OrdersUtilBO;
import com.walgreens.oms.bo.PrintSignReportFilterBO;
import com.walgreens.oms.bo.PromotionalMoneyBO;
import com.walgreens.oms.constants.ReportsConstant;
import com.walgreens.oms.factory.OmsBOFactory;
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
import com.walgreens.oms.json.bean.PrintableSignFilter;
import com.walgreens.oms.json.bean.ProductDetails;
import com.walgreens.oms.json.bean.RoyaltyRequestBean;
import com.walgreens.oms.json.bean.SalesByProductRequestBean;
import com.walgreens.oms.json.bean.UnclaimedEnvCustomer;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;
import com.walgreens.oms.json.bean.UnclaimedResponse;
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;

/**
 * This class is used to call for Realtime Order status updates and POS Realtime
 * Order process in central system as per action.
 * 
 * @author CTS
 * 
 */

@RestController
@RequestMapping(value = "/orders", method = RequestMethod.POST)
public class OrderRestServiceImpl implements OrderRestService {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderRestServiceImpl.class);

	@Autowired
	private OmsBOFactory omsBOFactory;
	
	@Autowired
	private PromotionalMoneyBO promoBo;

	/**
	 * This method is use for order submission at Central
	 * 
	 * @param jsonString
	 */
	@RequestMapping(value = "/submit", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	OrderDataResponse submitOrderDetails(
			@RequestBody OrderDataRequest jsonRequest) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Order Rest Service submitOrderDetails process start...");
		}

		OrderDataResponse orderResponse = null;

		try {

			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			orderResponse = ordersBO.submitOrderDetails(jsonRequest);

		} catch (NullPointerException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl Order Submit "
						+ e.getMessage());
			}
		} catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl Order Submit"
						+ e.getMessage());
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl Order Submit "
						+ e.getMessage());
			}
		} finally {
			LOGGER.info("RealTime Service submit order process end...");
		}
		return orderResponse;

	}

	/**
	 * This method is use for order submission at Central
	 * 
	 * @param jsonString
	 */
	@RequestMapping(value = "/complete", produces = { "application/json" }, method = RequestMethod.GET)
	public @ResponseBody
	OrderDataResponse completeOrderDetails(
			@RequestBody OrderDataRequest orderJsonMsgStr) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Order Rest Service completeOrderDetails process start...");
		}
		OrderDataResponse orderResponse = null;

		try {

			LOGGER.info("RealTime Service complete order process.");
			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			orderResponse = ordersBO.completeOrderDetails(orderJsonMsgStr);

		} catch (NullPointerException e) {
			// Write logging message
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl completeOrderDetails "
						+ e.getMessage());
			}
		} catch (PhotoOmniException e) {
			// Write logging message
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl completeOrderDetails "
						+ e.getMessage());
			}
		} catch (Exception e) {
			// Write logging message
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("RealTime Service Impl completeOrderDetails "
						+ e.getMessage());
			}
		} finally {
			// Write logging message
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Order Rest Service completeOrderDetails process finished...");
		}
		return orderResponse;

	}

	/**
	 * This method is use for order Exception at Central, it gets action call
	 * from the heart beat process.
	 * 
	 * @param strValueOfJson
	 *            json value will be hold by this variable.
	 * @param model
	 *            spring MVC parameter
	 * @return RealTimeOrderResponse.
	 */
	@RequestMapping(value = "/exception/create", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	OrderDataResponse updateOrderExceptions(
			@RequestBody OrderDataRequest jsonRequest) {
		LOGGER.info(" Entering updateException method of RealTimeOrderServiceImpl ");
		OrderDataResponse orderResponse = null;
		try {
			LOGGER.info("RealTime Service  order exception process.");
			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			orderResponse = ordersBO.updateOrderExceptions(jsonRequest);
		} catch (NullPointerException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Error occoured at updateException method of RealTimeOrderServiceImpl - "
						+ e.getMessage());
			}
		} catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Error occoured at updateException method of RealTimeOrderServiceImpl - "
						+ e.getMessage());
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Error occoured at updateException method of RealTimeOrderServiceImpl - "
						+ e.getMessage());
			}
		} finally {
			LOGGER.info(" Exiting updateException method of RealTimeOrderServiceImpl ");
		}

		return orderResponse;
	}

	/**
	 * This method fetches the json string and calls BO Layer method for data
	 * population
	 * 
	 * @param jsonRequest
	 * @return
	 * @throws PhotoOmniException
	 * @throws IOException
	 * 
	 */

	@RequestMapping(value = "/licensed_content/update", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	LCDataResponse updateLicenseContent(@RequestBody LCDataRequest jsonRequest) {

		LOGGER.info("Entering into LicenseContentServiceImpl.setLcDetails method");

		LCDataResponse response = null;
		try {
			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			response = ordersBO.updateLicenseContent(jsonRequest);
		} catch (PhotoOmniException e) {
			LOGGER.error("Error Occurred at LicenseContentServiceImpl.setLcDetails method - "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.error("Error Occurred at LicenseContentServiceImpl.setLcDetails method - "
					+ e.getMessage());
		} finally {
			LOGGER.info("Exiting from LicenseContentServiceImpl.setLcDetails method");
		}

		return response;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.service.OrderRestService#updateLabelPrntInfo(com
	 * .walgreens.common.json.bean.LabelDataRequest)
	 */
	@RequestMapping(value = "/label/update", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	LabelDataResponse updateLabelPrntInfo(
			@RequestBody LabelDataRequest labelDataRequeststr) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering updateLabelPrntInfo method of OrderRestServiceImpl ");
		}
		LabelDataResponse ldesponse = null;
		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("LabelPrintData Service label print details process start...");
			}
			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			ldesponse = ordersBO.updateLabelPrntInfo(labelDataRequeststr);
			if (ldesponse == null) {
				ldesponse = new LabelDataResponse();
			}

		} catch (NullPointerException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" NullPointerException occoured at updateLabelPrntInfo method of OrderRestServiceImpl - "
						+ e.getMessage());
			}
		} catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("PhotoOmniException occoured at updateLabelPrntInfo method of OrderRestServiceImpl -"
						+ e.getMessage());
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at updateLabelPrntInfo method of OrderRestServiceImpl - "
						+ e.getMessage());
			}

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting updateLabelPrntInfo method of OrderRestServiceImpl");
			}
		}
		return ldesponse;
	}

	/**
	 * Method to get details for PM_MBPM by Product Report
	 * 
	 * @param PMByProductRequestBean
	 * @return PMReportResponseBeanList
	 * */
	@RequestMapping(value = "/pm_mbpm/product", method = RequestMethod.POST)
	public @ResponseBody
	PMReportResponseBeanList submitPMByProductReportRequest(
			@RequestBody PMByProductRequestBean reqParam) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submitReportRequest method of OrderRestServiceImpl ");
		}
		PMReportResponseBeanList responseList = null;
		try {
			PMByProductFilter requestBean = new PMByProductFilter();
			requestBean = reqParam.getFilter();
			LOGGER.debug("OrderRestService submitReportRequest process started for Employee Id"
					+ requestBean.getEmployeeId());
			OrderBO ordersBO = omsBOFactory.getOrdersBO();
			responseList = ordersBO.fetchPMEarnedByProduct(requestBean);
			// responseList.setCurrentPageNo(reqParam.getFilter().getCurrentPageNo());
			responseList.setMessageHeader(reqParam.getMessageHeader());
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at submitReportRequest method of OrderRestServiceImpl - "
						, e);
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting submitReportRequest method of OrderRestServiceImpl ");
			}
		}
		return responseList;
	}

	@RequestMapping(value = "/updateASN", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	AsnOrderResponse updateASNDetails(@RequestBody AsnOrderRequest asnOrderJsonRequest){
		/*AsnOrderRequest asnOrderJsonRequest =new AsnOrderRequest();
		OrderASNDetail orderasnDetail=null;
		long sysOrderId=0;
	//	orderasnDetail.setPcpOrderId(Long.parseLong(request.getParameter("sysOrderId")));
		orderasnDetail.setLocationNumber(Integer.valueOf(request.getParameter("StoreNumber")));*/
		
		
		 
		
		AsnOrderResponse asnOrderResponse = new AsnOrderResponse();
		try {
			LOGGER.info("Inside RealTime Service  updateASNDetails process...");

			OrdersUtilBO ordersUtilBO = omsBOFactory.getOrdersUtilBO();
			if (asnOrderJsonRequest != null) {
				LOGGER.debug("Calling ordersUtilBO.updateASNDetails");
				asnOrderResponse = ordersUtilBO	.updateASNDetails(asnOrderJsonRequest);
				LOGGER.debug("Called ordersUtilBO.updateASNDetails");
			}
			LOGGER.debug("Successfully exiting from updateASNDetails process...");
			
		}catch (PhotoOmniException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" PhotoOmniException occoured at updateASNDetails method of RealTimeOrderServiceImpl - ",e);
			}
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at updateASNDetails method of RealTimeOrderServiceImpl -"	,e);
			}
		} finally {
			LOGGER.debug("Exiting RealTime Service  updateASNDetails process...");
		}
		return asnOrderResponse;
	}

	/**
	 * Method used to submit PMBYWIC adoc report request
	 * 
	 * @param pmWICReportRequestBean 
	 * @return PMBYWICResponseBean 
	 */
	@RequestMapping(value = "/pm_mbpm/submit/pm_wic_rpt_req", method = RequestMethod.POST)
	public @ResponseBody
	PMBYWICResponseBean submitPMWICReportRequest(
			@RequestBody PMBYWICRequestBean pmbywicRequestBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OderRestServiceImpl.submitPMWICReportRequest() method ");
			LOGGER.debug("PMBYWICDataRequest :" + pmbywicRequestBean.toString());
		}
		PMBYWICResponseBean pmbywicResponseBean = new PMBYWICResponseBean();
		try {
			OrderBO pmreportsBO = omsBOFactory.getOrdersBO();
			pmbywicResponseBean = pmreportsBO
					.submitPMWICReportRequest(pmbywicRequestBean);
			pmbywicRequestBean.getMessageHeader().setMsgSentTimestamp(
					CommonUtil.getCurrentTimeStamp());
			pmbywicResponseBean.setMessageHeader(pmbywicRequestBean
					.getMessageHeader());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at OderRestServiceImpl.submitPMWICReportRequest() "
					+ e);
			pmbywicResponseBean.setStatus(false);
			pmbywicResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			pmbywicResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OderRestServiceImpl.submitPMWICReportRequest() method");
			}
		}
		return pmbywicResponseBean;
	}

	/**
	 * Method to save royalty adoc report request
	 * 
	 * @param RoyaltyRequestBean
	 * @return RoyaltyReportResponseBean
	 */
	@RequestMapping(value = "/submit/sales_by_royalty_template_rpt_reqt", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	RoyaltyReportResponseBean submitRoyaltyReportRequest(
			@RequestBody RoyaltyRequestBean royaltyRequestBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OderRestServiceImpl.submitRoyaltyReportRequest() method");
			LOGGER.debug("RoyaltyDataRequest :" + royaltyRequestBean.toString());
		}
		RoyaltyReportResponseBean royaltyReportResponseBean = null;
		try {
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			royaltyReportResponseBean = objReportsBO
					.submitRoyaltyReportRequest(royaltyRequestBean);
			royaltyReportResponseBean.setMessageHeader(royaltyRequestBean
					.getMessageHeader());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OderRestServiceImpl.submitRoyaltyReportRequest() method"
					+ e);
			royaltyReportResponseBean.setStatus(false);
			royaltyReportResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			royaltyReportResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OderRestServiceImpl.submitRoyaltyReportRequest() method");
			}
		}
		return royaltyReportResponseBean;
	}

	/**
	 * method to get list of royalty vendor details
	 * 
	 * @param model
	 * @return VendorData. -- Royalty vendor data details
	 */
	@RequestMapping(value = "/getRoyaltyVendorNameList", method = RequestMethod.GET)
	public @ResponseBody
	VendorData getRoyaltyVendorNameList(ModelMap model) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OderRestServiceImpl.getRoyaltyVendorNameList() method ---->");
		}
		VendorData lstVendorDetails = null;
		try {
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			lstVendorDetails = objReportsBO.getRoyaltyVendorNameList();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OderRestServiceImpl.getRoyaltyVendorNameList() method  ---> "
					+ e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OderRestServiceImpl.getRoyaltyVendorNameList() method ");
			}
		}
		return lstVendorDetails;
	}

	/**
	 * Method to get product Filter details
	 * 
	 * @param model
	 * @return Map<String, Object> -- Map contains product filter details.
	 */
	@RequestMapping(value = "/sales_by_photo_card_rpt_req", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSalesReportByProductFilters() {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OderRestServiceImpl.getSalesReportByProductFilters() method---->");
		}
		Map<String, Object> model = new HashMap<String, Object>();
		try {
			OrderBO orderBO = omsBOFactory.getOrdersBO();
			model.put("productCategory",
					orderBO.getCodeDecodeValue("ProductCategory"));
			model.put("productSizes", orderBO.getCodeDecodeValue("ProductSize"));
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OderRestServiceImpl.getSalesReportByProductFilters() method "+ e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OderRestServiceImpl.getSalesReportByProductFilters() method");
			}
		}
		return model;
	}

	/**
	 * Method to save sales report by product report request
	 * 
	 * @param SalesByProductRequestBean
	 * @return SalesByProductResponseBean
	 */
	@RequestMapping(value = "/submit/sales_by_photo_card_rpt_req", method = RequestMethod.POST)
	@ResponseBody
	public SalesByProductResponseBean saveSalesReportByProductFilters(
			@RequestBody SalesByProductRequestBean salesByProductRequestBean) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderRestServiceImplsaveSalesReportByProductFilters() method");
		}
		SalesByProductResponseBean salesByProductResponseBean = new SalesByProductResponseBean();
		try {
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			salesByProductResponseBean = objReportsBO
					.saveSalesReportByProductData(salesByProductRequestBean);
			salesByProductResponseBean
			.setMessageHeader(salesByProductRequestBean
					.getMessageHeader());
			salesByProductResponseBean.getMessageHeader().setMsgSentTimestamp(
					CommonUtil.getCurrentTimeStamp());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderRestServiceImplsaveSalesReportByProductFilters() method "+ e);
			salesByProductResponseBean.setStatus(false);
			salesByProductResponseBean.setErrorDetails(CommonUtil
					.createFailureMessageForSystemException(e));
			salesByProductResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderRestServiceImplsaveSalesReportByProductFilters() method ");
			}
		}
		return salesByProductResponseBean;
	}

	@Override
	@RequestMapping(value = "/submit/plu_rpt_req", method = RequestMethod.POST)
	@ResponseBody
	public DailyPLUResBean submitPLUReportRequest(
			@RequestBody DailyPLUReqBean dailyPLUReqBean) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering OrderRestServiceImpl.submitPLUReportRequest() ");
		}
		LOGGER.info("Entering into OrderRestServiceImpl.submitPLUReportRequest()");
		OrderBO objReportsBO = omsBOFactory.getOrdersBO();
		return objReportsBO.submitPLUReportRequest(dailyPLUReqBean);
	}

	@Override
	@RequestMapping(value = "/get_plu_list", method = RequestMethod.POST)
	@ResponseBody
	public DailyPLUResBean getPLUList(
			@RequestBody DailyPLUReqBean dailyPLUReqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering Entering into OrderRestServiceImpl.getPLUList() ");
		}
		LOGGER.info("Entering into OrderRestServiceImpl.getPLUList()");
		OrderBO objReportsBO = omsBOFactory.getOrdersBO();
		return objReportsBO.getPLUList(dailyPLUReqBean);
	}

	@RequestMapping(value = " ", method = RequestMethod.GET)
	public List<PayonFulfillmentData> getStoreDetails(
			@RequestParam("storeNumber") String storeNumber) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getStoreDetails method of OrderRestServiceImpl ");
		}
		List<PayonFulfillmentData> dataList = null;
		try {
			OrderBO orderBO = omsBOFactory.getOrdersBO();
			dataList = orderBO.getStoreDetails(storeNumber);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getStoreDetails method of OrderRestServiceImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getStoreDetails method of OrderRestServiceImpl ");
			}
		}
		return dataList;
	}

	public List<ProductDetails> getProductDetails() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getProductDetails method of OrderRestServiceImpl ");
		}
		List<ProductDetails> productList = null;
		try {
			OrderBO orderBO = omsBOFactory.getOrdersBO();
			productList = orderBO.getProductDetails();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getProductDetails method of OrderRestServiceImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getProductDetails method of OrderRestServiceImpl ");
			}
		}
		return productList;
	}

	/**
	 * Pay on Fulfillment : - Vendor Cost validation fetch Vendor cost payment
	 * awaiting approval
	 * 
	 * @param
	 */

	@RequestMapping(value = "/fetchVendorPaymentAwaitingApproval", method = RequestMethod.POST)
	public @ResponseBody
	VendorCostValidationReportResponse fetchVendorPaymentAwaitingApproval(
			@RequestBody VendorCostValidationReportRequest params) {

		LOGGER.debug("Entering into OrderRestServiceImpl:fetchVendorPaymentAwaitingApproval()");
		VendorCostValidationReportResponse responseBean = null;
		try {
			LOGGER.debug("Instanttiating OrderBO");
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			LOGGER.debug("Calling objReportsBO.fetchVendorPaymentAwaitingApproval()");
			responseBean = objReportsBO
					.fetchVendorPaymentAwaitingApproval(params);
		} catch (Exception e) {
			LOGGER.error(
					" Exception occurred occoured in fetchVendorPaymentAwaitingApproval method of Order Rest Service - ",
					e);

		}
		LOGGER.debug("Exiting from OrderRestServiceImpl:fetchVendorPaymentAwaitingApproval()");
		return responseBean;
	}

	/**
	 * Pay on Fulfillment : - Vendor Cost validation Update Vendor payment
	 * approval
	 */
	@RequestMapping(value = "/updateVendorpaymentApproval", method = RequestMethod.POST)
	public @ResponseBody
	ApproveVCDataResponse updateVendorpaymentApproval(
			@RequestBody ApproveVCDataRequest params) {
		LOGGER.debug("Entering into OrderRestServiceImpl:updateVendorpaymentApproval()");
		ApproveVCDataResponse approveVCDataRes = new ApproveVCDataResponse();
		try {
			LOGGER.debug("Instantiating OrderRestServiceImpl:updateVendorpaymentApproval()");
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			LOGGER.debug("Calling OrderBO:updateVendorpaymentApproval()");
			POFVendorCostReportMessage responseMessage = objReportsBO
					.updateVendorpaymentApproval(params);
			approveVCDataRes.setVendorCostReportMsg(responseMessage);
			approveVCDataRes.setMessageHeader(params.getMessageHeader());

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at updateVendorpaymentApproval method of Order Rest Service - "
					+ e.getMessage());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at updateVendorpaymentApproval method of Order Rest Service - "
					+ e.getMessage());

		}
		LOGGER.debug("Exiting from OrderRestServiceImpl:updateVendorpaymentApproval()");
		return approveVCDataRes;
	}

	/**
	 * Pay on Fulfillment : - This method is used to get Vendor Name list for
	 * Pay on Fulfillment Vendor Cost validation Update Vendor payment approval
	 * 
	 * @param model
	 *            .
	 * @return lstVendorDetails.
	 */
	@RequestMapping(value = "/getPOFVendorNameList", method = RequestMethod.POST)
	public @ResponseBody
	VendorData getPOFVendorNameList(ModelMap model) {

		VendorData lstVendorDetails = null;
		try {
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			lstVendorDetails = objReportsBO.getPOFVendorNameList();
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getPOFVendorNameList method of Order Rest Service - "
					+ e.getMessage());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFVendorNameList method of Order Rest Service - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getPOFVendorNameList method of OrderRestServiceImpl ");
			}
		}

		return lstVendorDetails;
	}
	
	/**
	 * Pay on Fulfillment : - This method is used to get Vendor Name list for Approve Vendor List
	 * @param model
	 *            .
	 * @return lstVendorDetails.
	 */
	@RequestMapping(value = "/getPOFApproveVendorNameList", method = RequestMethod.POST)
	public @ResponseBody
	VendorData getPOFApproveVendorNameList(ModelMap model) {

		VendorData lstVendorDetails = null;
		try {
			OrderBO objReportsBO = omsBOFactory.getOrdersBO();
			lstVendorDetails = objReportsBO.getPOFApproveVendorNameList();
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getPOFApproveVendorNameList method of Order Rest Service - " + e.getMessage());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFApproveVendorNameList method of Order Rest Service - " + e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getPOFApproveVendorNameList method of OrderRestServiceImpl ");
			}
		}

		return lstVendorDetails;
	}

	/**
	 * Pay on Fulfillment :- This Method use to get the value for ORDER_VC_REP
	 * table under specific store number, start date & End date , EDI Upc
	 * 
	 * @param
	 */
	@RequestMapping(value = "/payOnFulfillmentReportRequest", method = RequestMethod.POST)
	public PayOnFulfillmentResponse payOnFulfillmentReportRequest(
			@RequestBody PayOnFulfillmentReqBean reqParam) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering payOnFulfillmentReportRequest method of OrderRestServiceImpl ");
		}
		PayOnFulfillmentResponse responseBean = null;

		try {

			OrderBO orderBO = omsBOFactory.getOrdersBO();
			responseBean = orderBO.payOnFulfillmentReportRequest(reqParam);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at payOnFulfillmentReportRequest method of OrderRestServiceImpl "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentReportRequest method of OrderRestServiceImpl ");
			}
		}
		return responseBean;

	}

	/**
	 * Pay on Fulfillment :- This Method use to get the value for Export to
	 * Excel for ORDER_VC_REP table under specific store number, start date &
	 * End date , EDI Upc
	 * 
	 * @param response
	 * @return
	 */
	@Override
	@RequestMapping(value = "/DownloadPOFCSV", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView submitCSVExportRequest(
			HttpServletResponse response,
			@RequestParam("pofDownloadCSVFilter") final String pofDownloadCSVFilter) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitCSVExportRequest method of OrderRestServiceImpl ");
		}
		ModelAndView model = new ModelAndView("DownloadPOFCSV");
		List<PayOnFulfillmentCSVRespData> csvResponseList = null;

		try {
			PayOnFulfillmentReqBean reqParam = new PayOnFulfillmentReqBean();
			PayOnFulfillmentResponse responseBean = new PayOnFulfillmentResponse();
			PayOnFulfillmentReq reqBean = new PayOnFulfillmentReq();

			final JSONParser parser = new JSONParser();
			final Object obj = parser.parse(pofDownloadCSVFilter);
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject = (JSONObject) jsonObject.get("filter");

			if (!CommonUtil.isNull(jsonObject.get("startDate"))
					&& !jsonObject.get("startDate").toString()
							.equalsIgnoreCase("")) {
				reqBean.setStartDate(jsonObject.get("startDate").toString());
			} else {
				reqBean.setStartDate(PhotoOmniConstants.BLANK);
			}
			if (!CommonUtil.isNull(jsonObject.get("endDate"))
					&& !jsonObject.get("endDate").toString()
							.equalsIgnoreCase("")) {
				reqBean.setEndDate(jsonObject.get("endDate").toString());
			} else {
				reqBean.setEndDate(PhotoOmniConstants.EMPTY_SPACE_CHAR);
			}
			if (!CommonUtil.isNull(jsonObject.get("filtertypePay"))
					&& !jsonObject.get("filtertypePay").toString()
							.equalsIgnoreCase("")) {
				reqBean.setFiltertypePay(jsonObject.get("filtertypePay")
						.toString());
			} else {
				reqBean.setFiltertypePay(PhotoOmniConstants.EMPTY_SPACE_CHAR);
			}

			if (!CommonUtil.isNull(jsonObject.get("storeNumber"))
					&& !jsonObject.get("storeNumber").toString()
							.equalsIgnoreCase("")) {
				reqBean.setStoreNumber(jsonObject.get("storeNumber").toString());
			} else {
				reqBean.setStoreNumber(PhotoOmniConstants.EMPTY_SPACE_CHAR);
			}
			if (!CommonUtil.isNull(jsonObject.get("vendor"))
					&& !jsonObject.get("vendor").toString()
							.equalsIgnoreCase("")) {
				reqBean.setVendorId(jsonObject.get("vendor").toString());
			} else {
				reqBean.setVendorId(PhotoOmniConstants.EMPTY_SPACE_CHAR);
			}
			reqParam.setFilter(reqBean);
			OrderBO orderBO = omsBOFactory.getOrdersBO();
			csvResponseList = orderBO
					.payOnFulfillmentCSVReportRequest(reqParam);
			responseBean.setCsvResponseList(csvResponseList);

			/*
			 * silverCanisterReportRepMsg.setMessageHeader(reqParam.getMessageHeader
			 * ()); ErrorDetails errorDetails = new ErrorDetails();
			 * errorDetails.
			 * setDescription(PhotoOmniConstants.ERROR_DESCRIPTION);
			 * errorDetails.setErrorCode(PhotoOmniConstants.ERROR_CODE);
			 * errorDetails.setErrorSource(PhotoOmniConstants.ERROR_SOURCE);
			 * errorDetails.setErrorString(PhotoOmniConstants.ERROR_STRING);
			 * silverCanisterReportRepMsg.setErrorDetails(errorDetails); s
			 */

			String[] header = { "serialNumber", "storeNumber", "reportingDate",
					"envelopeNumber", "eDIupc", "quantity", "cost", "doneDate" };
			model.addObject("csvHeader", header);
			model.addObject("csvData", csvResponseList);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(" Error occoured at submitCSVExportRequest method of OrderRestServiceImpl "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitCSVExportRequest method of OrderRestServiceImpl ");
			}
		}
		return model;

	}

	/**
	 * Method to get the PM Employee reports informations like EmployeeName ,
	 * DollersEarned, EarnedQty etc... for store level or employee level
	 * 
	 * @param objPMByEmployeeReqBean
	 *            contains MessageHeader and Employee informations like
	 *            EmployeeId, StoreNumber, filters selected etc... in JSON
	 * @return EmployeeDataResponse MessageHeader, ErrorDetails, List of PM
	 *         Employee reports object informations like EmployeeName ,
	 *         DollersEarned, EarnedQty etc....
	 * @since v1.0
	 */
	@Override
	@RequestMapping(value = "/pm_mbpm/employee", method = RequestMethod.POST)
	@ResponseBody
	public PMByEmployeeResBean getPMByEmployeeReportDetails(
			@RequestBody PMByEmployeeReqBean objPMByEmployeeReqBean)
					throws JsonParseException, JsonMappingException, IOException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderRestServiceImpl.getPMByEmployeeReportDetails()");
			LOGGER.debug("PMByEmployeeReqBean :" + objPMByEmployeeReqBean.toString());
		}
		PMByEmployeeResBean objPMByEmployeeResBean = null;
		boolean isFromPrintPage = false;
		try {
			OrderBO orderBO = omsBOFactory.getOrdersBO();
			objPMByEmployeeResBean = orderBO.getPmByEmployeeData(
					objPMByEmployeeReqBean, isFromPrintPage);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderRestServiceImpl.getPMByEmployeeReportDetails() - "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from OrderRestServiceImpl.getPMByEmployeeReportDetails()");
				LOGGER.debug("PMByEmployeeResBean : "
						+ objPMByEmployeeResBean.toString());
			}
		}
		return objPMByEmployeeResBean;
	}

	/**
	 * Method to get the PM Employee reports informations like EmployeeName ,
	 * DollersEarned, EarnedQty etc... for store level or employee level for
	 * print pAge.
	 * 
	 * @param request
	 *            contains MessageHeader and Employee informations like
	 *            EmployeeId, StoreNumber, filters selected etc... in JSON
	 * @return EmployeeDataResponse MessageHeader, ErrorDetails, List of PM
	 *         Employee reports object informations like EmployeeName ,
	 *         DollersEarned, EarnedQty etc....
	 * @since v1.0
	 */
	@RequestMapping(value = "/pm_mbpm/PMByEmpPrint", method = RequestMethod.POST)
	@ResponseBody
	public PMByEmployeeResBean printPMByEmployeeReport(
			HttpServletRequest request, HttpServletResponse response)
					throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderRestServiceImpl.printPMByEmployeeReport()");
		}
		OrderBO orderBO = null;
		PMByEmployeeResBean objPMByEmployeeResBean = null;
		boolean isFromPrintPage = true;
		String params = request.getParameter("param");
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("String PArams :" + params);
		}
		try {
			orderBO = omsBOFactory.getOrdersBO();
			ObjectMapper objectMapper = new ObjectMapper();
			PMByEmployeeReqBean objPMByEmployeeReqBean;
			objPMByEmployeeResBean = new PMByEmployeeResBean();
			objPMByEmployeeReqBean = objectMapper.readValue(new StringReader(
					params), PMByEmployeeReqBean.class);
			objPMByEmployeeResBean = orderBO.getPmByEmployeeData(
					objPMByEmployeeReqBean, isFromPrintPage);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderRestServiceImpl.printPMByEmployeeReport() "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("objPMByEmployeeResBean in printPMByEmployeeReport: "
						+ objPMByEmployeeResBean.toString());
				LOGGER.debug(" Exiting from OrderRestServiceImpl.printPMByEmployeeReport()");
			}
		}
		return objPMByEmployeeResBean;
	}

	/**
	 * Common Method to get the Employee Name or Store Location Address.
	 * 
	 * @param strEmpORLocID
	 *            contains EmployeeID if Employee NAme is required else Store
	 *            Number to get Store Address.
	 * @param isEmployeeName
	 *            contains true if employee name required else false for Store
	 *            address.
	 * @return strEmpNameOrStoreAdd contains Employee Name orStore Location
	 *         Address.
	 * @since v1.0
	 */
	@RequestMapping(value = "/pm_mbpm/getEmpNameOrStrAdd/{strEmpORLocID}/{isEmployeeName}", method = RequestMethod.POST)
	public @ResponseBody
	String getEmployeeNameStoreAdd(@PathVariable String strEmpORLocID,
			@PathVariable boolean isEmployeeName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderRestServiceImpl.getEmployeeNameStoreAdd()");
		}
		OrderBO orderBO = null;
		String strEmpNameORStrAdd = "";
		try {
			orderBO = omsBOFactory.getOrdersBO();
			strEmpNameORStrAdd = orderBO.getEmployeeNameStoreAdd(strEmpORLocID,
					isEmployeeName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderRestServiceImpl.getEmployeeNameStoreAdd() "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("strEmpNameORStrAdd: " + strEmpNameORStrAdd);
				LOGGER.debug(" Exiting from OrderRestServiceImpl.getEmployeeNameStoreAdd()");
			}
		}
		strEmpNameORStrAdd = "{\"strEmpNameORStrAdd\" : \""
				+ strEmpNameORStrAdd + "\"} ";
		return strEmpNameORStrAdd;
	}

	/**
	 * This method get all the print signs name searched by request param.
	 * @param reqParams contains searched.
	 * @return eventDataList.
	 */
	@RequestMapping(value = "/submit/signs_event_rpt_req", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody EventDataFilter getEventTypDetails(@RequestBody PrintSignEventReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getEventTypDetails method of PrintSignReportFilterServiceImpl ");
		}
		EventDataFilter eventDataList = null;
		PrintSignFilter filter = null;
		try {
			PrintSignReportFilterBO printSignReportBO = omsBOFactory.getpSReportBO();
			filter = reqParams.getFilter();
			eventDataList = printSignReportBO.getEventTypDetails(filter);
			eventDataList.setMessageHeader(reqParams.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterServiceImpl - ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getEventTypDetails method of PrintSignReportFilterServiceImpl ");
			}
		}
		return eventDataList;
	}

	/**
	 * This method save the Print Sign content filter conditions.
	 * @param params contains print Sign content filter data.
	 * @return respBean.
	 */
	@RequestMapping(value = "/submit/signs_rpt_req", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody PrintSignFilterRespBean submitPSReportFilterRequest(@RequestBody PrintSignReqBean params) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitPSReportFilterRequest method of PrintSignReportFilterServiceImpl ");
		}
		PrintSignFilterRespBean respBean = null;
		try {
			final PrintableSignFilter reqFilters = params.getFilter();
			final PrintSignReportFilterBO pSReportBO = omsBOFactory.getpSReportBO();
			respBean = pSReportBO.submitPSReportFilterRequest(reqFilters);
			respBean.setMessageHeader(params.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of OrderRestServiceImpl ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of PrintSignReportFilterServiceImpl ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitPSReportFilterRequest method of PrintSignReportFilterServiceImpl ");
			}
		}
		return respBean;
	}

	/**
	 * This method save the License content filter conditions.
	 * @param licenseReportReqBean contains license content filter data.
	 * @return respBean.
	 */
	@RequestMapping(value = "/submit/licensed_content_rpt_req", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody LicenseContentFilterRespBean submitLicenseReportFilterRequest(@RequestBody LicenseReportReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitLicenseReportFilterRequest method of LicenseReportFilterServiceImpl ");
		}
		LicenseReportFilterBO licenseReportFilterBO = null;
		LicenseContentFilterRespBean respBean = null;
		try {
			LicenseContentFilter filter = reqParams.getFilter();
			licenseReportFilterBO = omsBOFactory.getLicenseReportFilterBO();
			respBean = licenseReportFilterBO.submitLicenseReportFilterRequest(filter);
			respBean.setMessageHeader(reqParams.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrderRestServiceImpl ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LicenseReportServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitLicenseReportFilterRequest method of LicenseReportServiceImpl ");
			}
		}
		return respBean;
	}

	/*--------------------------------FOR Unclaimed task----------------------------------*/

	/**
	 * This method get all the unclaimed orders.
	 * @param reqBean contains front end parameters.
	 */
	@RequestMapping(value = "/unclaimed", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody UnclaimedResponse submitUnclaimedEnvRequest(@RequestBody UnclaimedEnvReqBean reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitUnclaimedEnvRequest method of OrderRestServiceImpl ");
		}
		UnclaimedResponse responseList = null;
		try {
			UnclaimedEnvFilter filter = reqBean.getFilter();
			OrdersUtilBO orderBO = omsBOFactory.getOrdersUtilBO();
			responseList = orderBO.submitUnclaimedEnvRequest(filter);
			responseList.setMessageHeader(reqBean.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrderRestServiceImpl ", e );
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrderRestServiceImpl ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitUnclaimedEnvRequest method of OrderRestServiceImpl ");
			}
		}
		return responseList;
	}

	/**
	 * This method get all order by the searched customer for last six month from today.
	 * @param reqBean contains front end parameters.
	 */
	@RequestMapping(value = "/unclaimedEnvCustOrderRequest", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody
	UnclaimedEnvCustomer unclaimedEnvCustOrderRequest(@RequestBody UnclaimedEnvCustorderReqBean reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering unclaimedEnvCustOrderRequest method of OrderRestServiceImpl ");
		}
		UnclaimedEnvCustomer unclaimedEnvCustomer = null;
		try {
			OrdersUtilBO orderBO = omsBOFactory.getOrdersUtilBO();
			unclaimedEnvCustomer = orderBO.unclaimedEnvCustOrderRequest(reqBean);
			unclaimedEnvCustomer.setMessageHeader(reqBean.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at unclaimedEnvCustOrderRequest method of OrderRestServiceImpl ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at unclaimedEnvCustOrderRequest method of OrderRestServiceImpl ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting unclaimedEnvCustOrderRequest method of OrderRestServiceImpl ");
			}
		}
		return unclaimedEnvCustomer;
	}

	/**
	 * This method get all the unclaimed orders and print them.
	 * 
	 * @param reqBean
	 *            contains front end parameters.
	 */
	@RequestMapping(value = "/unclaimed-store-print", produces = { "application/json" }, method = RequestMethod.POST)
	public @ResponseBody UnclaimedResponse submitUnclaimedEnvPrintRequest(@RequestBody UnclaimedEnvReqBean reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitUnclaimedEnvPrintRequest method of OrderRestServiceImpl ");
		}
		UnclaimedResponse responseList = null;
		try {
			UnclaimedEnvFilter filter = reqBean.getFilter();
			OrdersUtilBO orderBO = omsBOFactory.getOrdersUtilBO();
			responseList = orderBO.submitUnclaimedEnvRequest(filter);
			responseList.setMessageHeader(reqBean.getMessageHeader());
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvPrintRequest method of OrderRestServiceImpl ", e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvPrintRequest method of OrderRestServiceImpl ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitUnclaimedEnvPrintRequest method of OrderRestServiceImpl ");
			}
		}
		return responseList;
	}

	/*--------------------------------FOR Exception Report----------------------------------*/
	/*
	 * Exception Report by Envelopes
	 */
	@RequestMapping(value = "/submitEnvelopeReportRequest", method = RequestMethod.POST)
	public @ResponseBody
	ExceptionByEnvResponseBean submitEnvelopeReportRequest(
			@RequestBody ExceptionRequestBean reqParam) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitEnvelopeReportRequest method of ReportsRestServiceImpl ");
		}
		ExceptionByEnvResponseBean responseBean = null;
		OrderBO ordersBO = null;
		try {
			ordersBO = omsBOFactory.getOrdersBO();
			responseBean = ordersBO.submitEnvReportRequest(reqParam.getFilter());
			responseBean.setCurrentPageNo(reqParam.getFilter().getCurrentPageNo());
			responseBean.setMessageHeader(reqParam.getMessageHeader());
			/*
			ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			String strReportData = ow.writeValueAsString(responseBean);
			System.out.println(strReportData);*/
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitEnvelopeReportRequest method of ReportsRestServiceImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitEnvelopeReportRequest method of ReportsRestServiceImpl ");
			}
		}
		return responseBean;
	}

	/*
	 * Exception Report by Employees
	 */
	@RequestMapping(value = "/submitEmployeeReportRequest", method = RequestMethod.POST)
	public @ResponseBody
	ExceptionEmployeeRespBean submitEmployeeReportRequest(
			@RequestBody ExceptionEmployeeReqBean requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitEmployeeReportRequest method of ReportsRestServiceImpl ");
		}
		ExceptionEmployeeRespBean employeeRespBean = null;
		OrderBO ordersBO = null;

		try {
			ordersBO = omsBOFactory.getOrdersBO();
			employeeRespBean = ordersBO.submitEmployeeReportRequest(requestBean.getFilter());
			employeeRespBean.setCurrentPageNo(requestBean.getFilter().getCurrentPageNo());
			employeeRespBean.setMessageHeader(requestBean.getMessageHeader());
			/*ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			String strReportData = ow.writeValueAsString(employeeRespBean);
			System.out.println(strReportData);*/
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitEmployeeReportRequest method of ReportsRestServiceImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitEmployeeReportRequest method of ReportsRestServiceImpl ");
			}
		}

		return employeeRespBean;
	}


	/*
	 * Exception report Reason List
	 */
	@RequestMapping(value = "/exceptionReasonRequest", method = RequestMethod.GET)
	public @ResponseBody
	ExceptionReasonBean getReportExceptionReason() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportExceptionReason method of ReportsRestServiceImpl ");
		}
		ExceptionReasonBean responseBean = new ExceptionReasonBean();
		OrderBO ordersBO = null;
		try {
			
			ordersBO = omsBOFactory.getOrdersBO();
			responseBean = ordersBO.getReportExceptionReason();
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportExceptionReason method of ReportsRestServiceImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportExceptionReason method of ReportsRestServiceImpl ");
			}
		}
		return responseBean;

	}

	/**
	 * 
	 * @param LateEnvelopeReqRespBean
	 * @return
	 * @throws PhotoOmniException
	 */
	@RequestMapping(value = "/lateEnvlpRprtReq", method = RequestMethod.GET)
	public @ResponseBody
	LateEnvelopeReportRespBean fetchLateEnvelopeData(
			@RequestBody LateEnvelopeReportReqBean reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchLateEnvelopeDataRequest method of LateEnvelopeReportFilterServiceImpl ");
		}
		reqParams.getFilter().setReportName(
				ReportsConstant.LATE_ENVELOPE_REPORT);
		OrderBO ordersBO = null;
		LateEnvelopeReportRespBean respBean = null;
		try {

			ordersBO = omsBOFactory.getOrdersBO();
			respBean = ordersBO.fetchLateEnvelopeData(reqParams);
			respBean.setMessageHeader(reqParams.getMessageHeader());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LateEnvelopeReportServiceImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitLicenseReportFilterRequest method of LateEnvelopeReportServiceImpl ");
			}
		}
		return respBean;
	}

	
	/**
	 * This method generates Bean for Envelope Details ie Product details and Envelope History
	 * @param EnvelopeDtlsRequestBean reqParams
	 * @return EnvelopeDtlsDataRespBean
	 * @throws PhotoOmniException
	 */
	@RequestMapping(value = "/fetchEnvelopeDtlsRequest", method = RequestMethod.GET)
	public @ResponseBody
	EnvelopeDtlsDataRespBean fetchEnvelopeDtlsData(@RequestBody EnvelopeDtlsRequestBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchLateEnvelopeDetailsData method of LateEnvelopeReportFilterServiceImpl ");
		}
		OrderBO ordersBO = null;
		EnvelopeDtlsDataRespBean responseBean = null;
		try {

			ordersBO = omsBOFactory.getOrdersBO();
			responseBean = ordersBO.fetchEnvelopeDtlsData(reqParams.getFilter());
			responseBean.setMessageHeader(reqParams.getMessageHeader());
			responseBean.setEnvelopeNbr(reqParams.getFilter().getEnvelopeNbr());
			/*ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String strReportData = ow.writeValueAsString(responseBean);
			System.out.println(strReportData);*/

		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchLateEnvelopeDetailsData method of LateEnvelopeReportServiceImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchLateEnvelopeDetailsData method of LateEnvelopeReportServiceImpl ");
			}
		}
		return responseBean;
	}

	/*@Override
	public AsnOrderResponse updateASNDetails(AsnOrderRequest asnOrderJsonRequest) {
		// TODO Auto-generated method stub
		return null;
	}
*/
/*	@Override
	public AsnOrderResponse updateASNDetails(AsnOrderRequest asnOrderJsonRequest) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
	
     /**
 	 * 
 	 * @return
 	 */
 	/* @RequestMapping(value = "/fetchMBPMRequest",produces = { "application/json" } ,method = RequestMethod.POST)
      public @ResponseBody  boolean MBPromotionalMoney(@RequestBody TestBean testJson){
 	    if (LOGGER.isDebugEnabled()) {
 		LOGGER.debug("Entering into Calculate PromotionalMoney()");
 	       }
 	    TestBean testJson =new TestBean();
 	    testJson.setShoppingCartId(Long.parseLong(request.getParameter("shoppingCartId")));
 	    testJson.setOrderplaceddttm(request.getParameter("orderplaceddttm"));
 	    MBPromotionalMoneyBO mbpromotionalMoneyBO = null;
 	    boolean responseBean = false;
 		try {
             
 			mbpromotionalMoneyBO = omsBOFactory.getBasketPromotionalMoneyBO();
 			responseBean = mbpromotionalMoneyBO.calculateMBPromotionalMoney(testJson.getShoppingCartId(), testJson.getOrderplaceddttm());

 		} catch (Exception e) {
 			LOGGER.error(" Error occoured at  PromotionalMoney method of orderRestServiceImpl - "
 					+ e.getMessage());
 		} finally {
 			if (LOGGER.isDebugEnabled()) {
 				LOGGER.debug(" Exiting  PromotionalMoney method of orderRestServiceImpl ");
 			}
 		}
 		return responseBean;
 		
 	
      }*/
     }






