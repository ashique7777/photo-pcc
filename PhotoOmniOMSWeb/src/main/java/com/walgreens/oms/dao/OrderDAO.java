package com.walgreens.oms.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.CostCalculationBean;
import com.walgreens.oms.bean.CostCalculationTransferBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.EnvelopeOrderDtlsBean;
import com.walgreens.oms.bean.EnvelopeOrderLinePromotionBean;
import com.walgreens.oms.bean.EnvelopeOrderPromotionBean;
import com.walgreens.oms.bean.EnvelopePopupHeaderBean;
import com.walgreens.oms.bean.EnvelopeProductDtlsBean;
import com.walgreens.oms.bean.LateEnvelopeBean;
import com.walgreens.oms.bean.OmOrderAttributeBean;
import com.walgreens.oms.bean.OmOrderBean;
import com.walgreens.oms.bean.OrderInfo;
import com.walgreens.oms.bean.OrderItemLineBean;
import com.walgreens.oms.bean.OrderStatusBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeReqBean;
import com.walgreens.oms.bean.PMByEmployeeResBean;
import com.walgreens.oms.bean.PMByProductFilter;
import com.walgreens.oms.bean.PMReportResponseBean;
import com.walgreens.oms.bean.POFVendorCostReportDetail;
import com.walgreens.oms.bean.POFVendorCostValidationFilter;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;
import com.walgreens.oms.bean.PayOnFulfillmentReq;
import com.walgreens.oms.bean.RoyaltyReportResponseBean;
import com.walgreens.oms.bean.SalesByProductResponseBean;
import com.walgreens.oms.bean.VendorType;
import com.walgreens.oms.json.bean.EnvHistoryBean;
import com.walgreens.oms.json.bean.EnvelopeNumberFilter;
import com.walgreens.oms.json.bean.ExceptionByEmployeeBean;
import com.walgreens.oms.json.bean.ExceptionByEnvelopeFilter;
import com.walgreens.oms.json.bean.ExceptionEmployeeFilter;
import com.walgreens.oms.json.bean.ExceptionReason;
import com.walgreens.oms.json.bean.ExceptionRepEnv;
import com.walgreens.oms.json.bean.LCDataRequest;
import com.walgreens.oms.json.bean.LCDataResponse;
import com.walgreens.oms.json.bean.LabelDataRequest;
import com.walgreens.oms.json.bean.LabelDataResponse;
import com.walgreens.oms.json.bean.LateEnvelopeReportDetailsReqBean;
import com.walgreens.oms.json.bean.LateEnvelopeReportReqBean;
import com.walgreens.oms.json.bean.OrderItem;
import com.walgreens.oms.json.bean.OrderList;
import com.walgreens.oms.json.bean.PayOnFulfillmentResponse;
import com.walgreens.oms.json.bean.PayonFulfillmentData;
import com.walgreens.oms.json.bean.ProductDetails;
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;

public interface OrderDAO {
	
	public OrderStatusBean submitOrderDetails(OrderList orders, MessageHeader message) throws PhotoOmniException, RuntimeException, SQLException;
	
	public OrderStatusBean completeOrderDetails(OrderList orders, MessageHeader message) throws PhotoOmniException, ParseException, SQLException, DataAccessException, RuntimeException;

	public OrderStatusBean updateOrderExceptions(OrderList orders, MessageHeader message) throws PhotoOmniException, SQLException;
	
	public LCDataResponse updateLicenseContent(LCDataRequest jsonRequestBean) throws PhotoOmniException;
	
	public LabelDataResponse updateLabelPrntInfo(LabelDataRequest labelDataRequest) throws PhotoOmniException;

	public List<PMReportResponseBean> fetchPMEarnedByProduct(PMByProductFilter requestBean) throws PhotoOmniException;
	
	public PMBYWICResponseBean submitPMWICReportRequest(String filterState);

	public RoyaltyReportResponseBean submitRoyaltyReportRequest(String filterState) throws PhotoOmniException;

	public List<VendorType> getRoyaltyVendorNameList() throws PhotoOmniException;
	
	public List<Map<String, Object>> getCodeDecodeValue(String codeType);

	SalesByProductResponseBean saveSalesReportByProductData(String filters);
	
	public List<PayonFulfillmentData> getStoreDetails( String storeNumber) ;
		
	public List<ProductDetails>  getProductDetails(); 
	
	
	public PayOnFulfillmentResponse payOnFulfillmentReportRequest(PayOnFulfillmentReq  reqBean);
	
	public List<PayOnFulfillmentCSVRespData> payOnFulfillmentCSVReportRequest(PayOnFulfillmentReq  reqBean);
	
	public PayOnFulfillmentResponse payOnFulfillmentStorePrintReport( PayOnFulfillmentReq reqBean);
	
	public List<VendorType> getPOFVendorNameList() throws PhotoOmniException;
	
	public VendorCostValidationReportResponse fetchVendorPaymentAwaitingApproval(VendorCostValidationReportRequest  reqBean) throws PhotoOmniException;
	
	
	public int getReportDataCount(POFVendorCostValidationFilter pofVendorCostFilter);
	
	public boolean updateVendorpaymentApproval(POFVendorCostReportDetail pofVendorCostReportDetail) throws PhotoOmniException;
	
	public DailyPLUResBean submitPLUReportRequest(String strFilterState)
			throws Exception;

	public List<String> getPLUListAll(int currentPage, int pluRecPerPage);

	public List<String> getPLUList(String pluKey, int currentPage, int pluRecPerPage);

	PMByEmployeeResBean getPmByEmployeeReportData(
			PMByEmployeeReqBean pmByEmployeeReqBean, boolean isFromPrintPage) throws Exception;
	
	public String getEmployeeNameStoreAdd(String strEmpORLocID, boolean isEmployeeName);
	
	
	public List<LateEnvelopeBean> fetchLateEnvelopeDataRequest(LateEnvelopeReportReqBean requestBean) throws PhotoOmniException, IOException;
    
	public List<OrderInfo> fetchLateEnvProdInfo(LateEnvelopeReportDetailsReqBean reqBean) throws PhotoOmniException;
	
	public int getPLUListCount(String pluKey);

	public int getPLUListAllCount();
	
	public int getPluReportRecordPerPageCount();

	public List<VendorType> getPOFApproveVendorNameList() throws PhotoOmniException;
	
	/**
	 * Method to get order cost
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @return
	 */
	//public List<OmOrderBean>  getOrderCost(long sysOrderId, String orderPlacedDttm);
	/**
	 *method to get instore cost  
	 * @param omOrderBean
	 * @return
	 */

	public List<CostCalculationBean> getInstoreCost(OmOrderBean omOrderBean);
	/**
	 * Method to get vendor Cost
	 * @param costCalculationBean
	 * @return
	 */

	public List<CostCalculationBean> getVendorCostComponent(CostCalculationBean costCalculationBean);

	
/**
 * Method to get default cost 
 * @param costCalculationTransferBean
 * @param calculatedProdList
 * @param orderType
 * @return
 */
	/*public List<CostCalculationBean> getCalculateDefaultCost(
			CostCalculationTransferBean costCalculationTransferBean,
			String calculatedProdList, String orderType);*/
	
	/**
	 * method to get vendor cost
	 * @param omOrderBean
	 * @return
	 */

	public List<CostCalculationBean> getVendorCostCalculation(
			OmOrderBean omOrderBean);
	/**
	 * Method to update OM_ORDER_ATTRIBUTE Table
	 * @param omOrderAttributeBean
	 * @param ordPlacedDttm
	 * @return
	 */

	public boolean updateOrderCostDtl(
			OmOrderAttributeBean omOrderAttributeBean, String ordPlacedDttm);
	/**
	 * Method to update OM_ORDER_LINE
	 * @param costCalculationTransferBeanItem
	 * @return
	 */

	public boolean updateOrderItemCostDtl(
			CostCalculationTransferBean costCalculationTransferBeanItem);

	/**
	 * Method to fetch details of Envelope History ie Envelope date/time, action, user,
	 * reason for transfer and other comments
	 * @param EnvelopeNumberFilter
	 * @return EnvHistoryBeanList
	 */
	public List<EnvHistoryBean> fetchEnvelopeHistoryDetails(EnvelopeNumberFilter reqParams);

	/**
	 * Method to fetch details of Envelope Header Popup ie WebId, KioskId, Vendor Details
	 * @param EnvelopeNumberFilter
	 * @return EnvelopePopupHeaderBean
	 */
	public EnvelopePopupHeaderBean fetchEnvelopePopupHeaderDtls(EnvelopeNumberFilter reqParams);

	/**
	 * Method to fetch details of Envelope Order Information ie Order Description and Order Final Price
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeOrderDtlsBean
	 */
	public EnvelopeOrderDtlsBean fetchEnvelopeOrderInfo(EnvelopeNumberFilter reqParams);

	/**
	 * Method to fetch details of Envelope Order discount amount and description
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeOrderPromotionBeanList
	 */
	public List<EnvelopeOrderPromotionBean> fetchEnvelopeOrderPromotion(EnvelopeNumberFilter reqParams);

	/**
	 * Method to fetch details of Envelope Order Line Information ie Product details, its Per unit price, quantity and Final Price
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeProductDtlsBeanList
	 */
	public List<EnvelopeProductDtlsBean> fetchEnvelopeOrderLineInfo(EnvelopeNumberFilter reqParams);

	/**
	 * Method to fetch details of Envelope Order Line discount amount and description
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeOrderLinePromotionBeanList
	 */
	public List<EnvelopeOrderLinePromotionBean> fetchEnvelopeOrderLinePromotion(EnvelopeNumberFilter reqParams);

	/* 
	 * Exception Report by Envelopes
	 * */
	public List<ExceptionRepEnv> submitEnvReportRequest(ExceptionByEnvelopeFilter requestBean);
	/* 
	 * Exception Report by Employees
	 * */
	public List<ExceptionByEmployeeBean> submitEmployeeReportRequest(ExceptionEmployeeFilter requestBean);
	/**
	 * This method return Exception Reason from OM_EXCEPTION_TYPE table
	 */
	public List<ExceptionReason> getReportExceptionReason();
	
	/**
	 * Method to get order cost
	 * @param sysOrderId
	 * @param orderPlacedDttm
	 * @return
	 */
	public OmOrderAttributeBean  getOrderCost(long sysOrderId, String orderPlacedDttm);
	/**
	 *method to get instore cost  
	 * @param omOrderBean
	 * @return
	 */

	//public CostCalculationBean getInstoreCostEquipmentCost(OrderItemLineBean orderItemLineBean);
	/**
	 * Method to get vendor Cost
	 * @param costCalculationBean
	 * @return
	 */
	//public CostCalculationBean getInstoreDefaultMachineCost(OrderItemLineBean orderItemLineBean);

	//public List<CostCalculationBean> getVendorCostComponent(CostCalculationBean costCalculationBean);

	
/**
 * Method to get default cost 
 * @param costCalculationTransferBean
 * @param calculatedProdList
 * @param orderType
 * @return
 */
	public List<CostCalculationBean> getCalculateDefaultCost(
			CostCalculationTransferBean costCalculationTransferBean,
			String calculatedProdList, String orderType);
	
	
	/**
	 * Method to update OM_ORDER_ATTRIBUTE Table
	 * @param omOrderAttributeBean
	 * @param ordPlacedDttm
	 * @return
	 */

	public boolean updateOrderCostDtl(
			OmOrderAttributeBean omOrderAttributeBean);
	/**
	 * Method to update OM_ORDER_LINE
	 * @param costCalculationTransferBeanItem
	 * @return
	 */

	public boolean updateOrderItemCostDtl(OmOrderAttributeBean omOrderAttributeBean);
/**
 * Method to get vendor cost calculation
 * @param orderItemLineBean
 * @param omOrderAttributeBean
 * @return
 */
	public CostCalculationBean getVendorCostCalculation(
			OrderItemLineBean orderItemLineBean,OmOrderAttributeBean omOrderAttributeBean) throws PhotoOmniException;
	/**
	 * Method to getInstore Default Machine Cost 
	 * @param orderItemLineBean
	 * @return
	 */

	public CostCalculationBean getInstoreDefaultMachineCost(
			OrderItemLineBean orderItemLineBean)  throws PhotoOmniException;
	/**
	 * Method to get getInstoreCostEquipmentCost
	 * @param orderItemLineBean
	 * @return
	 */
	public CostCalculationBean getInstoreCostEquipmentCost(OrderItemLineBean orderItemLineBean) throws PhotoOmniException;
	/**
	 * Method to get getOrderReferenceID
	 * @param orderNo
	 * @param StoreNo
	 * @return
	 * @throws PhotoOmniException
	 */
	
	public long getOrderReferenceID(final String orderNo, final String StoreNo, final String orderPlacedDTTM) throws PhotoOmniException;
/**
 * To get the printed quantity .
 * @param orderItem
 * @return
 */
	public Integer getPrintQty(OrderItem orderItem);

 /**
  * Updating OM OrderLine Attribute Table 
  * @param sysOrderLineId
  * @param OrderPlacedDttm
  * @return
  */
	public boolean updateOmOrderLineAttribute(long sysOrderLineId, String OrderPlacedDttm,int printQty);
}
