/**
 * 
 */
package com.walgreens.oms.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.bean.DailyPLUReqBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeReqBean;
import com.walgreens.oms.bean.PMByEmployeeResBean;
import com.walgreens.oms.bean.PMByProductFilter;
import com.walgreens.oms.bean.PMReportResponseBeanList;
import com.walgreens.oms.bean.POFVendorCostReportMessage;
import com.walgreens.oms.bean.POFVendorCostValidationFilter;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;
import com.walgreens.oms.bean.RoyaltyReportResponseBean;
import com.walgreens.oms.bean.SalesByProductResponseBean;
import com.walgreens.oms.bean.VendorData;
import com.walgreens.oms.json.bean.ApproveVCDataRequest;
import com.walgreens.oms.json.bean.EnvelopeDtlsDataRespBean;
import com.walgreens.oms.json.bean.EnvelopeNumberFilter;
import com.walgreens.oms.json.bean.ExceptionByEnvResponseBean;
import com.walgreens.oms.json.bean.ExceptionByEnvelopeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeRespBean;
import com.walgreens.oms.json.bean.ExceptionReasonBean;
import com.walgreens.oms.json.bean.LCDataRequest;
import com.walgreens.oms.json.bean.LCDataResponse;
import com.walgreens.oms.json.bean.LabelDataRequest;
import com.walgreens.oms.json.bean.LabelDataResponse;
import com.walgreens.oms.json.bean.LateEnvelopeReportDetailsReqBean;
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
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;

/**
 * @author CTS
 *
 */
public interface OrderBO {
	
	public OrderDataResponse submitOrderDetails(OrderDataRequest jsonRequest) throws PhotoOmniException ;
	
	public OrderDataResponse completeOrderDetails(OrderDataRequest request) throws PhotoOmniException ;
	
	public OrderDataResponse updateOrderExceptions(OrderDataRequest strJson) throws PhotoOmniException ;
	
	public LCDataResponse updateLicenseContent(LCDataRequest strJson) throws PhotoOmniException ;
	
	public LabelDataResponse updateLabelPrntInfo(LabelDataRequest labelDataRequest)throws PhotoOmniException;

	public PMReportResponseBeanList fetchPMEarnedByProduct(
			PMByProductFilter requestBean);
	 
	public PMBYWICResponseBean submitPMWICReportRequest(PMBYWICRequestBean pmbywicRequestBean);

	public 	RoyaltyReportResponseBean  submitRoyaltyReportRequest(RoyaltyRequestBean royaltyRequestBean);

	public VendorData getRoyaltyVendorNameList() throws PhotoOmniException;
	
	public List<Map<String, Object>> getCodeDecodeValue(String codeType);

	SalesByProductResponseBean saveSalesReportByProductData(SalesByProductRequestBean saveSalesReportByProductData);	
	
	public DailyPLUResBean submitPLUReportRequest(DailyPLUReqBean dailyPLUReqBean)	throws Exception;

	public DailyPLUResBean getPLUList(DailyPLUReqBean dailyPLUReqBean);
	
	/************************Pay On Fulfillment: Start *********************/
	
	
    public List<PayonFulfillmentData> getStoreDetails( String storeNumber) ;
	
	public List<ProductDetails>  getProductDetails();
	public PayOnFulfillmentResponse payOnFulfillmentReportRequest(PayOnFulfillmentReqBean  reqBean);
	
	public List<PayOnFulfillmentCSVRespData>  payOnFulfillmentCSVReportRequest(PayOnFulfillmentReqBean reqBean) ;
	
	public PMByEmployeeResBean getPmByEmployeeData(
			PMByEmployeeReqBean pmByEmployeeReqBean, boolean isFromPrintPage) throws Exception;
	
	public String getEmployeeNameStoreAdd(String strEmpORLocID, boolean isEmployeeName);
	
	public VendorData getPOFVendorNameList() throws PhotoOmniException;
	
	public VendorCostValidationReportResponse fetchVendorPaymentAwaitingApproval(VendorCostValidationReportRequest  reqBean) throws PhotoOmniException;
	
	public int getReportDataCount(POFVendorCostValidationFilter pofVendorCostFilter) ;
	
	public POFVendorCostReportMessage updateVendorpaymentApproval(ApproveVCDataRequest reqBean) throws PhotoOmniException;
	
	/************************Pay On Fulfillment: End *********************/
	public LateEnvelopeReportRespBean fetchLateEnvelopeData(LateEnvelopeReportReqBean reqBean) throws PhotoOmniException, IOException;
	
	public VendorData getPOFApproveVendorNameList()throws PhotoOmniException;
	
	/**
	 * Method to implement cost calculation 
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @return
	 * @throws PhotoOmniException
	 */
	public boolean calculateOrderCost(long sysOrderId,String orderPlacedDttm) throws PhotoOmniException;
	
	/**
	 * Method to calculate cost calculation for sendout orders
	 * @param costCalculationBean
	 * @return
	 */
	//public double  getVendorCostComponentCalculation(CostCalculationBean costCalculationBean);
	/**
	 * * Method will calculate vendor component of the order cost
	 * @param costCalculationBean
	 * @return
	 */
	
	
	/**
	 * Method to calculate cost calculation for instore orders
	 * @param costCalculationBean
	 * @return
	 */
	
	
	

	/**
	 * This method generates Bean for Envelope Details ie Product details and Envelope History
	 * @param EnvelopeDtlsRequestBean reqParams
	 * @return EnvelopeDtlsDataRespBean
	 */
	public EnvelopeDtlsDataRespBean fetchEnvelopeDtlsData(EnvelopeNumberFilter reqParams);
	
	/* 
	 * Exception Report by Envelopes
	 * */
	public ExceptionByEnvResponseBean submitEnvReportRequest(ExceptionByEnvelopeFilter requestBean);
	/* 
	 * Exception Report by Employees
	 * */
	public ExceptionEmployeeRespBean submitEmployeeReportRequest(ExceptionEmployeeFilter requestBean);
	/**
	 * This method return Exception Reason from OM_EXCEPTION_TYPE table
	 */
	public ExceptionReasonBean getReportExceptionReason();
	
	/**
	 * Method to implement cost calculation 
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @return
	 * @throws PhotoOmniException
	 */

	
	/**
	 * Method to calculate cost calculation for sendout orders
	 * @param costCalculationBean
	 * @return
	 */
	//public double  getVendorCostComponentCalculation(OrderItemLineBean orderItemLineBean);
	/**
	 * * Method will calculate vendor component of the order cost
	 * @param costCalculationBean
	 * @return
	 */
	//public double vendorCostComponentCalculation(CostCalculationBean costCalculationBean);
	
	/**
	 * Method to calculate cost calculation for instore orders
	 * @param costCalculationBean
	 * @return
	 */
	//public double instoreCostComponentCalculation(CostCalculationBean costCalculationBean);
	
	//public CostCalculationTransferBean instoreCostCalculation(OmOrderBean omOrderBean);
}
