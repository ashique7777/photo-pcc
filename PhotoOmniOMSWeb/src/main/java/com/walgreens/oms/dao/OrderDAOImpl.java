/**

 * RealTimeOrderDAOImpl.java 
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
package com.walgreens.oms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.security.oam.SAMLUserDetails;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.common.utility.MessageHeader;
import com.walgreens.oms.bean.CostCalculationBean;
import com.walgreens.oms.bean.CostCalculationTransferBean;
import com.walgreens.oms.bean.DailyPLUResBean;
import com.walgreens.oms.bean.EnvelopeOrderDtlsBean;
import com.walgreens.oms.bean.EnvelopeOrderLinePromotionBean;
import com.walgreens.oms.bean.EnvelopeOrderPromotionBean;
import com.walgreens.oms.bean.EnvelopePopupHeaderBean;
import com.walgreens.oms.bean.EnvelopeProductDtlsBean;
import com.walgreens.oms.bean.LCDataBean;
import com.walgreens.oms.bean.LCResponseBean;
import com.walgreens.oms.bean.LCSelDataBean;
import com.walgreens.oms.bean.LateEnvelopeBean;
import com.walgreens.oms.bean.LicenseContent;
import com.walgreens.oms.bean.OmOrderAttributeBean;
import com.walgreens.oms.bean.OmOrderBean;
import com.walgreens.oms.bean.OmOrderLineBean;
import com.walgreens.oms.bean.OrderHistoryBean;
import com.walgreens.oms.bean.OrderInfo;
import com.walgreens.oms.bean.OrderItemLineBean;
import com.walgreens.oms.bean.OrderStatusBean;
import com.walgreens.oms.bean.OrderlinePluBean;
import com.walgreens.oms.bean.PMBYWICResponseBean;
import com.walgreens.oms.bean.PMByEmployeeDetailBean;
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
import com.walgreens.oms.bean.SelOrderPluDataBean;
import com.walgreens.oms.bean.ShopCartPluDetail;
import com.walgreens.oms.bean.ShoppingCartBean;
import com.walgreens.oms.bean.VendorType;
import com.walgreens.oms.constants.ReportsConstant;
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
import com.walgreens.oms.json.bean.LabelPrintDetails;
import com.walgreens.oms.json.bean.LateEnvelopeFilter;
import com.walgreens.oms.json.bean.LateEnvelopeReportDetailsReqBean;
import com.walgreens.oms.json.bean.LateEnvelopeReportReqBean;
import com.walgreens.oms.json.bean.LicenseContentDownloadReqList;
import com.walgreens.oms.json.bean.OrderException;
import com.walgreens.oms.json.bean.OrderItem;
import com.walgreens.oms.json.bean.OrderList;
import com.walgreens.oms.json.bean.PLUDetail;
import com.walgreens.oms.json.bean.PayOnFulfillmentRespData;
import com.walgreens.oms.json.bean.PayOnFulfillmentResponse;
import com.walgreens.oms.json.bean.PayonFulfillmentData;
import com.walgreens.oms.json.bean.PrintableSign;
import com.walgreens.oms.json.bean.ProductAttribute;
import com.walgreens.oms.json.bean.ProductDetails;
import com.walgreens.oms.json.bean.Template;
import com.walgreens.oms.json.bean.VendorCostValidationReportRequest;
import com.walgreens.oms.json.bean.VendorCostValidationReportResponse;
import com.walgreens.oms.rowmapper.CostCalculationBeanRowMapper;
import com.walgreens.oms.rowmapper.DefaultCostCalculationBeanRowMapper;
import com.walgreens.oms.rowmapper.EnvelopeHistryRowMapper;
import com.walgreens.oms.rowmapper.EnvelopeOrderInfoRowMapper;
import com.walgreens.oms.rowmapper.EnvelopeOrderLinePromotionRowMapper;
import com.walgreens.oms.rowmapper.EnvelopeOrderPromotionRowMapper;
import com.walgreens.oms.rowmapper.EnvelopePopupHeaderRowMapper;
import com.walgreens.oms.rowmapper.EnvelopeProductDtlsRowMapper;
import com.walgreens.oms.rowmapper.ExceptionReasonRowMapper;
import com.walgreens.oms.rowmapper.ExceptionReportEmployeeRowMapper;
import com.walgreens.oms.rowmapper.ExceptionReportEnvelopeRowMapper;
import com.walgreens.oms.rowmapper.LCselBeanRowMapper;
import com.walgreens.oms.rowmapper.LateEnvProdInfoRowmapper;
import com.walgreens.oms.rowmapper.LateEnvelopeRowmapper;
import com.walgreens.oms.rowmapper.OmOrderBeanRowMapper;
import com.walgreens.oms.rowmapper.OrderLinePluRowMapper;
import com.walgreens.oms.rowmapper.OrderPluDatarowMapper;
import com.walgreens.oms.rowmapper.PLUListRowMapper;
import com.walgreens.oms.rowmapper.PMByEmployeeDetailBeanRowMapper;
import com.walgreens.oms.rowmapper.PMReportByProductRowMapperTest;
import com.walgreens.oms.rowmapper.PayOnFulfillmentCSVReportRowMapper;
import com.walgreens.oms.rowmapper.PayOnFulfillmentEDIRowmapper;
import com.walgreens.oms.rowmapper.PayOnfullFillmentRowmapper;
import com.walgreens.oms.rowmapper.ProductdetailsRowmapper;
import com.walgreens.oms.rowmapper.RoyaltyVendorListRowMapper;
import com.walgreens.oms.rowmapper.ShoppingCartDetailRowMapper;
import com.walgreens.oms.rowmapper.VendorCostReportRowMapper;
import com.walgreens.oms.rowmapper.VendorListRowMapper;
import com.walgreens.oms.utility.ExceptionReportQuery;
import com.walgreens.oms.utility.PMReportByProductQuery;
import com.walgreens.oms.utility.PayOnFulfillmentQuery;
import com.walgreens.oms.utility.RealTimeOrderQuery;
import com.walgreens.oms.utility.ReportsQuery;
import com.walgreens.oms.utility.ServiceUtil;

/*s.utility.ServiceUtil;
 ng database.
 * 
 * @author CTS
 * @version 1.1 January 12, 2015
 * 
 */
@Repository("OrderDAO")
@Transactional(propagation = Propagation.REQUIRED)
public class OrderDAOImpl implements OrderDAO, PhotoOmniConstants {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate dataGuardJdbcTemplate;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderDAOImpl.class);

	/**
	 * This method id used for save order related data to database
	 * 
	 * @param orderRequestBean
	 * @return List of Order Response
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	@Override
	public OrderStatusBean submitOrderDetails(OrderList orders,
			MessageHeader message) throws RuntimeException, PhotoOmniException, SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering submitOrderDetails method of OrderDAOImpl ");
		}
		String orderQuery = RealTimeOrderQuery.insertQueryOrderRef().toString();
		String orderAttrQuery = RealTimeOrderQuery.insertQueryOrderAttrRef()
				.toString();
		OrderStatusBean orderStatus = new OrderStatusBean();
		orderStatus.setStatus(false);
		orderStatus.setOrderPlacedDTTM(orders.getOrder().getSubmittedTime());
		String orderCategory = orders.getOrder().getOrderCategory();
		Integer expenseInd = (ZERO == orderCategory.compareToIgnoreCase("Y")) ? 1
				: 0;

		List<OrderItem> orderItemList = orders.getOrderItem();

		/*
		 * 
		 * Need to check whether this order is already transfered or not
		 */
		long duplicateOrderID = this.getOrderReferenceID(orders.getOrder()
				.getPcpOrderId(), orders.getOrder().getLocationNumber(), orders.getOrder().getSubmittedTime());

		if (duplicateOrderID == 0) {
			// fetch location reference id
			long locationRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_LOCATION, "SYS_LOCATION_ID",
					"LOCATION_NBR", orders.getOrder().getLocationNumber(),
					"ACTIVE_CD", 1);
			Long vendorId;
			Integer owiningLOCTZOffSet = this.getTimezoneOffset(locationRefId);
			Integer vendorLOCTZOffSet = null;
			if (orders.getOrder().getVendorId().compareTo("0") == 0) {
				// Overriding vendor for kiosk
				vendorId = new Long(0);
			} else {
				vendorId = this.getSystemReferenceID(
						PhotoOmniDBConstants.OM_VENDOR, "SYS_VENDOR_ID",
						"VENDOR_NBR", orders.getOrder().getVendorId(),
						"ACTIVE_CD", 1);
				vendorLOCTZOffSet = this.getTimezoneOffset(vendorId);
			}

			Long sysSRCVendorID = null, sysFullfillmentID = null;
			if (orders.getOrder().getProcessingType().compareToIgnoreCase("H") == PhotoOmniConstants.ZERO) {
				String vendorNbr = orders.getOrder().getVendorId();
				boolean isElectronicsFilmVendor = this
						.isElectronicsFilmVendor(vendorNbr);
				if (isElectronicsFilmVendor) {
					sysFullfillmentID = vendorId;
				} else {
					sysSRCVendorID = vendorId;
				}
			} else if (orders.getOrder().getProcessingType()
					.compareToIgnoreCase("S") == PhotoOmniConstants.ZERO) {
				sysFullfillmentID = vendorId;
			}

			int couponCD = (orders.getOrder().getCouponInd()
					.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			int employeeDiscountCD = (orders.getOrder()
					.getEmployeeDiscountInd().compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			int orderSequenceNum = this
					.getTableSequenceId(PhotoOmniDBConstants.OM_ORDER);
			String originOrder = orders.getOrder().getOriginOrderId();
			String vendorOrderNBR = this.getVendorNBR(orders.getOrder()
					.getOrderSource(), originOrder);
			String kioskOrderNBR = this.getKisoskNBR(orders.getOrder()
					.getOrderSource(), originOrder);
			
			//PROD Fix for expense order customer issue - starts
			long customerId = 0;
			try{
				customerId = Long.parseLong(orders.getOrder().getCustomerId());
			}catch (Throwable th){
				LOGGER.info("ALPHANUMERIC customer ID Receivied --> "+ orders.getOrder().getCustomerId());
			}
			//PROD Fix for expense order customer issue - ends
			
			/*Submit Order, Complete Order, Order Exception flows need 
			 * to be modified to update OM_ORDER.STATUS based on the below logic:*/
			String odrStatus = orders.getOrder().getOrderStatus();
			final String odrSource  = orders.getOrder().getOrderSource();
			if (!CommonUtil.isNull(odrSource) && "INST".equalsIgnoreCase(odrSource.trim())) {
				odrStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
			} else {
				odrStatus = PhotoOmniConstants.ORDER_STATUS_PROC;
			}

			Object[] objOrderParms = new Object[] {
					orderSequenceNum,
					orders.getOrder().getPcpOrderId(),
					vendorOrderNBR,
					kioskOrderNBR,
					PhotoOmniConstants.ORDER_TYPE, // Using PHOTO ONLy
					customerId,
					this.getOrderOrigin(orders.getOrder().getOrderSource(),
							vendorId),
					sysSRCVendorID,
					sysFullfillmentID,
					orders.getOrder().getTotalDiscountAmount(),
					orders.getOrder().getCalculatedRetail(),
					orders.getOrder().getOriginalRetail(),
					odrStatus, 
					ServiceUtil.dateFormatter(orders.getOrder()
							.getInStorePromisedTime()),
					ServiceUtil.dateFormatter(orders.getOrder()
							.getSubmittedTime()),
					ServiceUtil.dateFormatter(orders.getOrder()
							.getCalculatedPromisedTime()),
					couponCD,
					orders.getOrder().getLoyaltyPrice(),
					orders.getOrder().getLoyaltyDiscountAmt(),
					employeeDiscountCD,
					locationRefId,
					locationRefId,
					this.getCalenderId(orders.getOrder().getSubmittedTime()),
					PhotoOmniConstants.CURRENCY_CODE_ID,
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					orders.getOrder().getOrderDesc(), owiningLOCTZOffSet,
					vendorLOCTZOffSet, null,
					null, PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO, PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID
			/* PhotoOmniConstants.UPDATE_DTTM */};
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OM_ORDER SQL --> Data");
				LOGGER.debug(orderQuery, objOrderParms);
			}
			// Inserting into Order table
			jdbcTemplate.update(orderQuery, objOrderParms);

			/*
			 * Process market basket data starts
			 */
			long shoppingCartID = 0;
			// get shopping cart id of this market basket
			if (String.valueOf(orders.getOrder().getMarketBasketId()) != null) {
				shoppingCartID = this.getShoppingCartID(locationRefId, orders
						.getOrder().getMarketBasketId(), orders.getOrder()
						.getSubmittedTime());
				List<PLUDetail> mbPLUDetails = orders.getOrder()
						.getMbPLUDetails();
				// process market basket PLU
				this.mbPLUProcess(mbPLUDetails, shoppingCartID, orders
						.getOrder().getSubmittedTime());
			}
			/*
			 * Process market basket data End
			 */
			orderStatus.setSysShoppingCartId(shoppingCartID);
			// Update shopping cart reference into attr table
			long emplTookOrder = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_USER_ATTRIBUTES, "SYS_USER_ID",
					"EMPLOYEE_ID", orders.getOrder().getEmplTookOrder());

			Object[] objOrderAttrParms = new Object[] {
					orderSequenceNum,
					orders.getOrder().getEnvelopeNumber(),

					orders.getOrder().getReferenceId(),
					emplTookOrder,
					orders.getOrder().getServiceCategoryCode(),
					orders.getOrder().getProcessingType(),
					ServiceUtil.dateFormatter(orders.getOrder()
							.getSubmittedTime()), 
							shoppingCartID,
							expenseInd,
							/*PhotoOmniConstants.BLANK,*/
							PhotoOmniConstants.ZERO,
							PhotoOmniConstants.CREATE_USER_ID,
							/*PhotoOmniConstants.CREATE_DTTM,*/
							PhotoOmniConstants.UPDATE_USER_ID,
							/*PhotoOmniConstants.UPDATE_DTTM,*/
							PhotoOmniConstants.ZERO,
							PhotoOmniConstants.ZERO,
							PhotoOmniConstants.ZERO,
							PhotoOmniConstants.ZERO,
							PhotoOmniConstants.ZERO,

					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO,

					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO

			};
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OM_ORDER_ATTRIBUTE SQL --> Data");
				LOGGER.debug(orderAttrQuery, objOrderAttrParms);
			}
			// Inserting into Order attributes table
			jdbcTemplate.update(orderAttrQuery, objOrderAttrParms);

			// Inserting into Order history table
			OrderHistoryBean historyBean = new OrderHistoryBean();
			historyBean.setAction(PhotoOmniConstants.ORDER_SUBMIT_ACTION_CODE);
			historyBean.setActionDttm(ServiceUtil.dateFormatter(orders
					.getOrder().getSubmittedTime()));
			historyBean.setActionNotes(this.getDecodeVal(PhotoOmniConstants.ORDER_SUBMIT_ACTION_CODE, "OrderActions"));
			historyBean.setOrderPlacedDttm(ServiceUtil.dateFormatter(orders
					.getOrder().getSubmittedTime()));
			historyBean.setOrderStatus(orders.getOrder().getOrderStatus());

			historyBean.setCreateUserId(orders.getOrder()
					.getSubmittedEmployeeId());
			historyBean.setExceptionId(0);
			historyBean.setOrderId(orderSequenceNum);

			this.insertOrderHistoryDetails(historyBean);

			// Process order PLU Details
			List<PLUDetail> orderPLUDetails = orders.getOrder()
					.getOrderPLUDetails();
			this.orderPLUProcess(orderPLUDetails, orders.getOrder()
					.getSubmittedTime(), orderSequenceNum);

			long machineId = this.getMachineInstanceId(orders.getOrder()
					.getMachineId(), orders.getOrder().getLocationNumber());
			// Process order item
			this.orderItemProcess(orderItemList, orders.getOrder()
					.getSubmittedTime(), machineId, orderSequenceNum);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Records Inserted for Order --> "
						+ orders.getOrder().getOriginOrderId());
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Records Inserted for Order --> "
						+ orders.getOrder().getOriginOrderId());
			}
			orderStatus.setSysOrderId(orderSequenceNum);
			orderStatus.setStatus(true);

		} else {
			orderStatus.setSysOrderId(duplicateOrderID);
		}

		return orderStatus;

	}

	/**
	 * Stores Order level promotion data
	 * 
	 * @param orderPLUDetails
	 *            List of PLUDetail
	 * @param orderPlacedDttm
	 *            Order placed time
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void orderPLUProcess(List<PLUDetail> orderPLUDetails,
			String orderPlacedDttm, int omOrderCuRRSEQ)
			throws PhotoOmniException, SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering orderPLUProcess method of OrderDAOImpl ");
		}
		String orderPLUQry = RealTimeOrderQuery.insertOrderPLUQry().toString();
		List<SelOrderPluDataBean> insertOrderPluList = new ArrayList<SelOrderPluDataBean>();
		List<SelOrderPluDataBean> updateOrderPluList = new ArrayList<SelOrderPluDataBean>();
		long orderid = (long)omOrderCuRRSEQ;
		for (int j = 0; j < orderPLUDetails.size(); j++) {
			PLUDetail orderPLU = orderPLUDetails.get(j);
			if (orderPLU.getDiscountAmt().isEmpty()
					|| orderPLU.getPluId().isEmpty()) {
				throw new DataIntegrityViolationException(
						"Empty Order PLU id or amount provided");
			}
			/*
			 * long promotionRefId =
			 * this.getSystemReferenceID(PhotoOmniDBConstants.OM_PROMOTION,
			 * "SYS_PLU_ID", "PLU_NBR", orderPLU.getPluId(), "ACTIVE_CD", 1);
			 */
			long promotionRefId = this.getSysPluID(orderPLU.getPluId());
			SelOrderPluDataBean pluBean = new SelOrderPluDataBean();
			pluBean.setInserInd(true);
			pluBean.setActiveCd(1);
			pluBean.setDiscountAmount(orderPLU.getDiscountAmt());
			pluBean.setOrderPlaceddttm(orderPlacedDttm);
			pluBean.setSysOrderid(orderid);
			pluBean.setSysPluId(promotionRefId);
			insertOrderPluList.add(pluBean);
		}
		this.updateOrderPlu(insertOrderPluList, updateOrderPluList);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit orderPLUProcess method of OrderDAOImpl ");
		}
	}

	/**
	 * Stores Order level promotion data for Exception
	 * 
	 * @param orderPLUDetails
	 *            List of PLUDetail
	 * @param orderPlacedDttm
	 *            Order placed time
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void orderPLUExceptionProcess(List<PLUDetail> orderPLUDetails,
			String orderPlacedDttm, long orderReferenceId)
			throws PhotoOmniException, SQLException {
		
		List<SelOrderPluDataBean> insertOrderPluList = new ArrayList<SelOrderPluDataBean>();
		List<SelOrderPluDataBean> updateOrderPluList = new ArrayList<SelOrderPluDataBean>();
		List<SelOrderPluDataBean> orderPluList = this.getOrderPluData(orderPlacedDttm, orderReferenceId);
		for (int j = 0; j < orderPLUDetails.size(); j++) {
			PLUDetail orderPLU = orderPLUDetails.get(j);
			SelOrderPluDataBean orderPLUBean = this.getOrderPLUBean(orderPluList, orderPLU.getPluId());
			if(orderPLUBean.isInserInd()){
				long promotionRefId = this.getSysPluID(orderPLU.getPluId());
				orderPLUBean.setActiveCd(1);
				orderPLUBean.setDiscountAmount(orderPLU.getDiscountAmt());
				orderPLUBean.setOrderPlaceddttm(orderPlacedDttm);
				orderPLUBean.setSysOrderid(orderReferenceId);
				orderPLUBean.setSysPluId(promotionRefId);
				insertOrderPluList.add(orderPLUBean);
			} else {
				orderPLUBean.setDiscountAmount(orderPLU.getDiscountAmt());
			//	orderPLUBean.setOrderPlaceddttm(orderPlacedDttm);
				updateOrderPluList.add(orderPLUBean);
			}
			
		}
		this.updateOrderPlu(insertOrderPluList, updateOrderPluList);
	}
	//New Insert/update method
		public SelOrderPluDataBean getOrderPLUBean(
				List<SelOrderPluDataBean> orderPLUList, String pluNBR) {
			SelOrderPluDataBean dataBean = null;
			for (SelOrderPluDataBean pluBean : orderPLUList) {
				if (pluBean.getPluNumber().equalsIgnoreCase(pluNBR)) {
					dataBean = pluBean;
					dataBean.setUpdateInd(true);
					dataBean.setInserInd(false);
					break;
				}
			}
			if (null == dataBean) {
				dataBean = new SelOrderPluDataBean();
				dataBean.setUpdateInd(false);
				dataBean.setInserInd(true);
				
			}

			return dataBean;
		}
		//Update/Insert method JDBC
		
		public void updateOrderPlu(final List<SelOrderPluDataBean> insertOrderPluList,final List<SelOrderPluDataBean> updateOrderPluList) throws SQLException {
			
			String insertQry = RealTimeOrderQuery.insertOrderPLUQry().toString();
			String updateQry = RealTimeOrderQuery.updateShoppingCartPLUDataQry().toString();
			// insert block
			if (!insertOrderPluList.isEmpty()) {
				jdbcTemplate.batchUpdate(insertQry,
						new BatchPreparedStatementSetter() {

							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								SelOrderPluDataBean pluBean = insertOrderPluList.get(i);
								ps.setLong(1, this.getTableSequenceId("OM_ORDER_PLU"));
								ps.setDouble(2, pluBean.getSysOrderid());
								ps.setLong(3, pluBean.getActiveCd());
								ps.setDouble(4,pluBean.getSysPluId());
								ps.setString(5, pluBean.getDiscountAmount());
								ps.setTimestamp(6, ServiceUtil.dateFormatter(pluBean.getOrderPlaceddttm()));
								ps.setString(7, PhotoOmniConstants.CREATE_USER_ID);
								ps.setDate(8,  getCurrentDate());
								ps.setString(9, PhotoOmniConstants.UPDATE_USER_ID);
								ps.setDate(10,  getCurrentDate());

							}
							
							private java.sql.Date  getCurrentDate() {
								java.util.Date today = new java.util.Date();
								// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
								return new java.sql.Date(today.getTime());
							}
							public long getTableSequenceId(String sequenceName) {
								long sequence = 0;
								StringBuilder seqSql = new StringBuilder();
								seqSql.append("SELECT ").append(sequenceName)
										.append("_SEQ.NEXTVAL AS id FROM DUAL");
								sequence = jdbcTemplate.queryForLong(seqSql
										.toString());
								if (LOGGER.isDebugEnabled()) {
									LOGGER.debug(" OM_SHOPPING_CART_PLU next sequence val"
											+ sequence);
								}
								return sequence;
							}

							public int getBatchSize() {
								return insertOrderPluList.size();
							}
						});
			}
			// Update block
			if (!updateOrderPluList.isEmpty()) {
				jdbcTemplate.batchUpdate(updateQry,
						new BatchPreparedStatementSetter() {

							public void setValues(PreparedStatement ps, int i)
									throws SQLException {
								SelOrderPluDataBean pluBean = updateOrderPluList.get(i);
								ps.setString(1, pluBean.getDiscountAmount());
								ps.setString(2, PhotoOmniConstants.UPDATE_USER_ID);
								ps.setDate(3, getCurrentDate());
								ps.setLong(4, pluBean.getSysOrderPluId());
								ps.setString(5,pluBean.getOrderPlaceddttm());
							}

							private java.sql.Date  getCurrentDate() {
								java.util.Date today = new java.util.Date();
								// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
								return new java.sql.Date(today.getTime());
							}

							public int getBatchSize() {
								return updateOrderPluList.size();
							}
						});
			}

		}

	//Select OM_order_PLU
	private List<SelOrderPluDataBean> getOrderPluData( String orderPlacedDttm , long refrenceId) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getOrderPluData method of OrderDAOImpl ");
		}
		List<SelOrderPluDataBean> orderPluBeanList = null;
		try {
			String orderPluDataQry = RealTimeOrderQuery
					.selectOrderPLUQry().toString();
			Object[] params = new Object[] {ServiceUtil.dateformat24(orderPlacedDttm), refrenceId};
			orderPluBeanList = dataGuardJdbcTemplate.query(orderPluDataQry, params,
				new OrderPluDatarowMapper());

		} catch (Exception e) {
			LOGGER.error(" Error occurred getOrderPluData method of OrderDAOImpl  - "
					,e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getOrderPluData method of OrderDAOImpl  ");
			}

		}
		return orderPluBeanList;
		
	}

	/**
	 * Stores Market basket promotion data
	 * 
	 * @param mbPLUDetails
	 *            List of PLUDetails data
	 * @param shoppingCartID
	 *            Sys reference of shopping cart table
	 * @param orderPlacedDttm
	 *            Order placed time
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void mbPLUProcess(List<PLUDetail> mbPLUDetails,
			long shoppingCartID, String orderPlacedDttm)
			throws PhotoOmniException, SQLException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering mbPLUDetails method of OrderDAOImpl");
			LOGGER.debug(mbPLUDetails.toString(), shoppingCartID,
					orderPlacedDttm);
		}
		String shoppingCartPLUQry = RealTimeOrderQuery
				.insertShoppingCartPLUQry().toString();
		List<ShopCartPluDetail> insertShopCartList = new ArrayList<ShopCartPluDetail>();
		List<ShopCartPluDetail> updateShopCartList = new ArrayList<ShopCartPluDetail>();
		for (int j = 0; j < mbPLUDetails.size(); j++) {
			PLUDetail pluDetail = mbPLUDetails.get(j);
			if (pluDetail.getDiscountAmt().isEmpty()
					|| pluDetail.getPluId().isEmpty()) {
				throw new DataIntegrityViolationException(
						"Empty Market basket PLU id or amount provided");
			}
			if (pluDetail.getPluId().compareToIgnoreCase(
					PhotoOmniConstants.BLANK) != 0) {
				// Check whether this combination is already present or not
				int duplicateMbPlu = this.getMBPLUCount(pluDetail.getPluId(),
						shoppingCartID);
				if (duplicateMbPlu == 0) {
					Long promotionRefId = this
							.getSysPluID(pluDetail.getPluId());
					if (!CommonUtil.isNull(promotionRefId)) {
						ShopCartPluDetail cartPlu = new ShopCartPluDetail();
						cartPlu.setActiveCd(1);
						cartPlu.setInserInd(true);
						cartPlu.setOrderPlacedDttm(orderPlacedDttm);
						cartPlu.setPluDiscAmount(pluDetail.getDiscountAmt());
						cartPlu.setShoppingCartId(shoppingCartID);
						cartPlu.setSysPluId(promotionRefId);
						insertShopCartList.add(cartPlu);
						/*if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(" shoppingCartPLUQry SQl --> DATA ");
							LOGGER.debug(shoppingCartPLUQry, objCartAluParms);
						}
						jdbcTemplate
								.update(shoppingCartPLUQry, objCartAluParms);*/
					}
				}
			}
		}
		
		this.updateShoppingCartPlu(insertShopCartList, updateShopCartList);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit mbPLUDetails method of OrderDAOImpl ");
		}
	}

	/**
	 * Count plu for a particular market basket
	 * 
	 * @param pluCode
	 * @param shoppingCartID
	 * @return
	 */
	private int getMBPLUCount(String pluCode, long shoppingCartID) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getMBPLUCount method of OrderDAOImpl ");
		}
		int id = 0;
		StringBuilder SqlQuery = new StringBuilder();
		SqlQuery.append("SELECT COUNT(SYS_SC_PLU_ID) AS num FROM ");
		SqlQuery.append(PhotoOmniDBConstants.OM_SHOPPING_CART_PLU);
		SqlQuery.append(" OSCL LEFT JOIN ").append(
				PhotoOmniDBConstants.OM_PROMOTION);
		SqlQuery.append(" USING(SYS_PLU_ID) WHERE SYS_SHOPPING_CART_ID=? AND PLU_NBR=? AND OSCL.ACTIVE_CD=1");
		// System.out.println(SqlQuery.toString());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Sql query in getMBPLUCount : " + SqlQuery.toString(),
					pluCode, shoppingCartID);
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				SqlQuery.toString(), new Object[] { shoppingCartID, pluCode });
		for (Map<String, Object> row : rows) {
			id = ServiceUtil.bigDecimalToInt((row.get("num")));
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("EXIT getMBPLUCount method of OrderDAOImpl ");
		}
		return id;

	}

	/**
	 * Stores Market basket promotion data for Exception
	 * 
	 * @param mbPLUDetails
	 *            List of PLUDetails data
	 * @param shoppingCartID
	 *            Sys reference of shopping cart table
	 * @param orderPlacedDttm
	 *            Order placed time
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void mbPLUExceptionProcess(List<PLUDetail> mbPLUDetails,
			long shoppingCartID, String orderPlacedDttm)
			throws PhotoOmniException, SQLException {
		String shoppingCartPLUQry = RealTimeOrderQuery
				.insertShoppingCartPLUQry().toString();
		List<ShopCartPluDetail> shopCartList = new ArrayList<ShopCartPluDetail>();
		List<ShopCartPluDetail> insertShopCartList = new ArrayList<ShopCartPluDetail>();
		List<ShopCartPluDetail> updateShopCartList = new ArrayList<ShopCartPluDetail>();
		
		String tempDttm = ServiceUtil.dateformat24(orderPlacedDttm);
		shopCartList = this.getShoppingCartData(shoppingCartID, tempDttm);
		for (int j = 0; j < mbPLUDetails.size(); j++) {
			PLUDetail pluDetail = mbPLUDetails.get(j);
			ShopCartPluDetail shopCartData = this.getShopCartDataBean(shopCartList, pluDetail.getPluId());
			if(shopCartData.isInserInd()){
				Long promotionRefId = this.getSysPluID(pluDetail.getPluId());
				shopCartData.setSysPluId(promotionRefId);
				shopCartData.setActiveCd(1);
				shopCartData.setOrderPlacedDttm(orderPlacedDttm);
				shopCartData.setShoppingCartId(shoppingCartID);
				shopCartData.setPluDiscAmount(pluDetail.getDiscountAmt());
				insertShopCartList.add(shopCartData);
				
			}else{
				shopCartData.setPluDiscAmount(pluDetail.getDiscountAmt());
				//shopCartData.setOrderPlacedDttm(orderPlaceDDttm);
				updateShopCartList.add(shopCartData);
			}
			
		}
		this.updateShoppingCartPlu(insertShopCartList, updateShopCartList);
		
	}
	
	//New update method added for shoppingCartPlu
	private List<ShopCartPluDetail> getShoppingCartData(long shoppingCartID, String orderPlacedDttm) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering shoppingcartplu method of OrderDAOImpl ");
		}
		List<ShopCartPluDetail> shopCartBeanList = null;
		try {
			String shoppingCartDataQry = RealTimeOrderQuery
					.selectShoppingCartPLUQry().toString();
			Object[] params = new Object[] {shoppingCartID, orderPlacedDttm};
			shopCartBeanList = dataGuardJdbcTemplate.query(shoppingCartDataQry, params,
				new ShoppingCartDetailRowMapper());

		} catch (Exception e) {
			LOGGER.error(" Error occurred shoppingcartplu method of OrderDAOImpl  - "
					,e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting shoppingcartplu method of OrderDAOImpl  ");
			}

		}
		return shopCartBeanList;
		
	}
	
	//New Insert/update method
	public ShopCartPluDetail getShopCartDataBean(
			List<ShopCartPluDetail> shopBeanList, String sysPluId) {
		ShopCartPluDetail dataBean = null;
		for (ShopCartPluDetail shopBean : shopBeanList) {
			if (shopBean.getPluNumber().equalsIgnoreCase(sysPluId)) {
				dataBean = shopBean;
				dataBean.setUpdateInd(true);
				dataBean.setInserInd(false);
				break;
			}
		}
		if (null == dataBean) {
			dataBean = new ShopCartPluDetail();
			dataBean.setUpdateInd(false);
			dataBean.setInserInd(true);
			
		}

		return dataBean;
	}

	//Update/Insert method JDBC
	
	public void updateShoppingCartPlu(final List<ShopCartPluDetail> insertShopCartList,final List<ShopCartPluDetail> updateShopCartList) throws SQLException {
		
		String insertQry = RealTimeOrderQuery.insertShoppingCartPLUQry().toString();
		String updateQry = RealTimeOrderQuery.updateShoppingCartPLUDataQry().toString();
		// insert block
		if (!insertShopCartList.isEmpty()) {
			jdbcTemplate.batchUpdate(insertQry,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							ShopCartPluDetail shopBean = insertShopCartList.get(i);
							ps.setLong(1, this.getTableSequenceId("OM_SHOPPING_CART_PLU"));
							ps.setDouble(2, shopBean.getActiveCd());
							ps.setLong(3, shopBean.getShoppingCartId());
							ps.setDouble(4,shopBean.getSysPluId());
							ps.setString(5, shopBean.getPluDiscAmount());
							ps.setTimestamp(6, ServiceUtil.dateFormatter(shopBean.getOrderPlacedDttm()));
							ps.setString(7, PhotoOmniConstants.CREATE_USER_ID);
							ps.setDate(8,  getCurrentDate());
							ps.setString(9, PhotoOmniConstants.UPDATE_USER_ID);
							ps.setDate(10,  getCurrentDate());
							

						}
						
						private java.sql.Date  getCurrentDate() {
							java.util.Date today = new java.util.Date();
							// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
							return new java.sql.Date(today.getTime());
						}
						public long getTableSequenceId(String sequenceName) {
							long sequence = 0;
							StringBuilder seqSql = new StringBuilder();
							seqSql.append("SELECT ").append(sequenceName)
									.append("_SEQ.NEXTVAL AS id FROM DUAL");
							sequence = jdbcTemplate.queryForLong(seqSql
									.toString());
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(" OM_SHOPPING_CART_PLU next sequence val"
										+ sequence);
							}
							return sequence;
						}

						public int getBatchSize() {
							return insertShopCartList.size();
						}
					});
		}
		// Update block
		if (!updateShopCartList.isEmpty()) {
			jdbcTemplate.batchUpdate(updateQry,
					new BatchPreparedStatementSetter() {

						public void setValues(PreparedStatement ps, int i)
								throws SQLException {
							ShopCartPluDetail shopBean = updateShopCartList.get(i);
							ps.setString(1, shopBean.getPluDiscAmount());
							ps.setString(2, PhotoOmniConstants.UPDATE_USER_ID);
							ps.setDate(3, getCurrentDate());
							ps.setLong(4, shopBean.getShoppingCartId());
							ps.setString(5,shopBean.getOrderPlacedDttm());
						}

						private java.sql.Date  getCurrentDate() {
							java.util.Date today = new java.util.Date();
							// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
							return new java.sql.Date(today.getTime());
						}

						public int getBatchSize() {
							return updateShopCartList.size();
						}
					});
		}

	}

	

	private long getShoppingCartID(long locationRefId, String marketBasketId,
			String orderPlacedTime) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getShoppingCartID method of OrderDAOImpl ");
		}

		long shoppingCartId = 0;
		// check whether market basket id exists in shopping cart table

		shoppingCartId = this.getSystemReferenceID(
				PhotoOmniDBConstants.OM_SHOPPING_CART, "SYS_SHOPPING_CART_ID",
				"SHOPPING_CART_NBR", marketBasketId);
		if (shoppingCartId <= 0) {

			ShoppingCartBean shopingcartBean = new ShoppingCartBean();
			shopingcartBean.setShopingCartNBR(marketBasketId);
			shopingcartBean.setSysLocationID(locationRefId);
			shopingcartBean.setCartTypeCD("MarketBasket");
			shopingcartBean.setPmStatusCD(PhotoOmniConstants.BLANK);
			shopingcartBean.setOrderPlacedDTTM(ServiceUtil
					.dateFormatter(orderPlacedTime));
			shopingcartBean.setCreateUserID(PhotoOmniConstants.CREATE_USER_ID);
			/* shopingcartBean.setCreateDTTM(PhotoOmniConstants.CREATE_DTTM); */
			shopingcartBean.setUpdateUserID(PhotoOmniConstants.UPDATE_USER_ID);
			/* shopingcartBean.setUpdateDTTM(PhotoOmniConstants.UPDATE_DTTM ); */
			this.insertShoppingCart(shopingcartBean);

			shoppingCartId = this
					.getSystemReferenceID(
							PhotoOmniDBConstants.OM_SHOPPING_CART,
							"SYS_SHOPPING_CART_ID", "SHOPPING_CART_NBR",
							marketBasketId);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit getShoppingCartID method of OrderDAOImpl ");
		}
		return shoppingCartId;
	}

	private long getProductReferenceID(String productCode)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getProductReferenceID method of RealTimeOrderDAOImpl ");
		}
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		try {
			sqlQuery.append("SELECT SYS_PRODUCT_ID AS id FROM ").append(
					PhotoOmniDBConstants.OM_PRODUCT);
			sqlQuery.append(" WHERE PRODUCT_NBR=?");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" SQl " + sqlQuery.toString() + " productCode : "
						+ productCode);
			}
			id = jdbcTemplate.queryForObject(sqlQuery.toString(),
					new Object[] { productCode }, Long.class);
		} catch (Exception ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getProductReferenceID method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.info(" SQL-" + sqlQuery.toString());
				LOGGER.info(" productCode- :" + productCode);
			}
			throw new DataIntegrityViolationException(
					"Invalid pcpProductId found.");

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getProductReferenceID method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * This method is used for insert Order Item data into database
	 * 
	 * @param orderRequestBean
	 * @param orderResponseBean
	 * @param templtQry
	 * @param licncQry
	 * @param orderItemList
	 * @return
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 * @throws DataAccessException 
	 * @throws NumberFormatException 
	 */
	private void orderItemProcess(List<OrderItem> orderItemList,
			String orderPlacedTime, long machineId, int orderSequenceNum)
			throws PhotoOmniException, SQLException, NumberFormatException, DataAccessException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering orderItemProcess method of OrderDaOImpl ");
		}

		String orderItemQry = RealTimeOrderQuery.insertQueryOrderItemRef()
				.toString();
		String orderItemAttrQry = RealTimeOrderQuery
				.insertQryOrderItemAttrRef().toString();
		Long sysMachineOrderId = new Long(machineId);
		if (sysMachineOrderId == 0) {
			sysMachineOrderId = null;
		}
		for (int j = 0; j < orderItemList.size(); j++) {

			OrderItem orderItem = orderItemList.get(j);
			Object[] objItemParms;
			long productRefId = this.getProductReferenceID(orderItem
					.getpCPProductId());

			ProductAttribute productAttribute = orderItem.getProductAttribute();

			int omOrderLineSeq = this
					.getTableSequenceId(PhotoOmniDBConstants.OM_ORDER_LINE);
			objItemParms = new Object[] {
					omOrderLineSeq,
					orderSequenceNum,
					productRefId,
					orderItem.getQuantity(),
					orderItem.getQuantity(),
					orderItem.getOriginalRetail(),
					orderItem.getDiscountAmt(),
					orderItem.getCalculatedRetail(),
					orderItem.getLoyaltyPrice(),
					orderItem.getLoyaltyDiscountAmt(),
					this.getEquipInstanceId(sysMachineOrderId,
							Integer.parseInt(orderItem.getEquipmentId())),
					sysMachineOrderId,
					ServiceUtil.dateFormatter(orderPlacedTime),
					PhotoOmniConstants.ZERO, // UNIT_PRICE hardcoded for now
					PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO,
					PhotoOmniConstants.ZERO };

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OM_ORDER_LINE inser Qry :" + orderItemQry
						+ objItemParms);
			}
			jdbcTemplate.update(orderItemQry, objItemParms);

			// Inserting Order Item attributes
			Object[] objItemAttrParms = new Object[] { omOrderLineSeq,
					productAttribute.getNbrSets(),
					productAttribute.getPanaromicPrints(),
					productAttribute.getNbrSheetsPrinted(),
					productAttribute.getNbrIndexSheets(),
					productAttribute.getNoOfInputs(), orderItem.getWasteQty(),
					orderItem.getWastePrintCost(),
					ServiceUtil.dateFormatter(orderPlacedTime),
					PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					PhotoOmniConstants.ZERO, PhotoOmniConstants.ZERO };
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OM_ORDER_LINE_ATTRIBUTE inser Qry :"
						+ orderItemAttrQry + objItemAttrParms);
			}
			jdbcTemplate.update(orderItemAttrQry, objItemAttrParms);

			// Process template info for this item
			List<Template> templtList = orderItem.getTemplate();
			this.templeteProcess(orderPlacedTime, productRefId, templtList,
					omOrderLineSeq);

			// Process license content info
			List<LicenseContent> licncList = orderItem.getLicenseContent();
			this.licenseContentProcess(orderPlacedTime, productRefId,
					licncList, omOrderLineSeq);

			// process printable sign report
			List<PrintableSign> printableSign = orderItem.getPrintableSigns();
			this.printableSignsProcess(orderPlacedTime, printableSign,
					omOrderLineSeq);

			// Process PLUDetails
			List<PLUDetail> pluDetail = orderItem.getPluDetails();
			this.pluDetailProcess(orderPlacedTime, pluDetail, productRefId,
					omOrderLineSeq);

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit orderitemprocess method of OrderDAOImpl ");
		}
	}

	/**
	 * This method is used for insert License Content data into database
	 * 
	 * @param licncQry
	 * @param licncList
	 */
	private void licenseContentProcess(String orderPlacedTime,
			long productRefId, List<LicenseContent> licncList,
			int omOrderLineSeq) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering licenseContentProcess method of OrderDAOImpl ");
		}
		String licncQry = RealTimeOrderQuery.insertQueryLicenseContentRef()
				.toString();

		for (int lnc = 0; lnc < licncList.size(); lnc++) {
			LicenseContent lncContent = licncList.get(lnc);
			int getDownloadedInd = (lncContent.getDownloadedInd()
					.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			Timestamp downloadDTTM = (lncContent.getDownloadDttm().isEmpty()) ? null
					: ServiceUtil.dateFormatter(lncContent.getDownloadDttm());
			Object[] objOrderLCParms = new Object[] { omOrderLineSeq,
					lncContent.getLicenseContentId(), lncContent.getQuantity(),
					downloadDTTM, getDownloadedInd,
					lncContent.getAddonLicenseContentDesc(),
					PhotoOmniConstants.ZERO, lncContent.getOriginalRetail(),
					lncContent.getCalculatedRetail(),
					ServiceUtil.dateFormatter(orderPlacedTime),
					PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID
			/* PhotoOmniConstants.UPDATE_DTTM */};
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sql & Data licenseContentProcess method of OrderDAOImpl "
						+ licncQry + objOrderLCParms.toString());
			}
			jdbcTemplate.update(licncQry, objOrderLCParms);

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit licenseContentProcess method of OrderDAOImpl ");
		}
	}

	/**
	 * This method is used for insert License Content data into database
	 * 
	 * @param licncQry
	 * @param licncList
	 */
	private void licenseContentUpdateProcess(String orderPlacedTime,
			long productRefId, List<LicenseContent> licncList,
			long orderLineReferenceID) {

		String licncQry = RealTimeOrderQuery.updateQueryLicenseContentRef()
				.toString();

		for (int lnc = 0; lnc < licncList.size(); lnc++) {
			LicenseContent lncContent = licncList.get(lnc);
			int getDownloadedInd = (lncContent.getDownloadedInd()
					.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			Object[] objOrderLCParms = new Object[] { lncContent.getQuantity(),
					ServiceUtil.dateFormatter(lncContent.getDownloadDttm()),
					getDownloadedInd, lncContent.getAddonLicenseContentDesc(),
					lncContent.getCalculatedRetail(),
					lncContent.getOriginalRetail(),
					ServiceUtil.dateFormatter(orderPlacedTime),
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					orderLineReferenceID, lncContent.getLicenseContentId(),
					ServiceUtil.dateformat24(orderPlacedTime) };
			jdbcTemplate.update(licncQry, objOrderLCParms);

		}
	}

	/**
	 * Stores PrintableSign information of an order item
	 * 
	 * @param orderPlacedTime
	 * @param printableSigns
	 * @throws PhotoOmniException
	 * @throws SQLException
	 */
	private void printableSignsProcess(String orderPlacedTime,
			List<PrintableSign> printableSigns, int omOrderLineSeq)
			throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering printableSignsProcess method of OrderDAOImpl ");
		}
		String printableQry = RealTimeOrderQuery.insertQueryPrintableSignsRef()
				.toString();

		for (int prs = 0; prs < printableSigns.size(); prs++) {
			PrintableSign printSignContent = printableSigns.get(prs);
			Object[] objOrderPSParms = new Object[] { omOrderLineSeq,
					printSignContent.getImageQuantity(),
					printSignContent.getImageId(),
					ServiceUtil.dateFormatter(orderPlacedTime),
					PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID
			/* PhotoOmniConstants.UPDATE_DTTM */};
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Sql & data  printableSignsProcess method of OrderDAOImpl "
						+ printableQry + objOrderPSParms.toString());
			}
			jdbcTemplate.update(printableQry, objOrderPSParms);

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exits printableSignsProcess method of OrderDAOImpl ");
		}
	}

	/**
	 * Stores PrintableSign information of an order item
	 * 
	 * @param orderPlacedTime
	 * @param printableSigns
	 * @param orderPlacedTime
	 * @throws PhotoOmniException
	 * @throws SQLException
	 */
	private void printableSignsUpdateProcess(
			List<PrintableSign> printableSigns, long orderLineReferenceID,
			String orderPlacedTime) throws PhotoOmniException {

		String printableQry = RealTimeOrderQuery.updateQueryPrintableSignsRef()
				.toString();

		for (int prs = 0; prs < printableSigns.size(); prs++) {
			PrintableSign printSignContent = printableSigns.get(prs);
			Object[] objOrderPSParms = new Object[] {
					printSignContent.getImageQuantity(),
					PhotoOmniConstants.UPDATE_USER_ID,
					orderLineReferenceID, 
					printSignContent.getImageId(),
					ServiceUtil.dateformat24(orderPlacedTime) };

			jdbcTemplate.update(printableQry, objOrderPSParms);

		}
	}

	/**
	 * Stores PLU information of an order item.
	 * 
	 * @param orderPlacedTime
	 * @param pluDetail
	 * @param productRefId
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void pluDetailProcess(String orderPlacedTime,
			List<PLUDetail> pluDetail, long productRefId, int omOrderLineSeq)
			throws PhotoOmniException, SQLException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering pluDetailProcess method of OrderDAOImpl ");
		}
		String pluQry = RealTimeOrderQuery.insertQueryALUDetails().toString();
		
		List<OrderlinePluBean> insertOrderLinePluList = new ArrayList<OrderlinePluBean>();
		List<OrderlinePluBean> updateOrderLinePluList = new ArrayList<OrderlinePluBean>();
		
		for (int plu = 0; plu < pluDetail.size(); plu++) {
			PLUDetail pluDetailContent = pluDetail.get(plu);
			if (pluDetailContent.getDiscountAmt().isEmpty()
					|| pluDetailContent.getPluId().isEmpty()) {
				throw new DataIntegrityViolationException(
						"Empty Order item PLU id or amount provided");
			}
			/*
			 * long promotionRefId =
			 * this.getSystemReferenceID(PhotoOmniDBConstants.OM_PROMOTION,
			 * "SYS_PLU_ID", "PLU_NBR", pluDetailContent.getPluId(),"ACTIVE_CD",
			 * 1);
			 */
			long promotionRefId = this.getSysPluID(pluDetailContent.getPluId());
			
			OrderlinePluBean pluBean = new OrderlinePluBean();
			pluBean.setInserInd(true);
			pluBean.setActiveCd(1);
			pluBean.setDiscountAmount(pluDetailContent.getDiscountAmt());
			pluBean.setOrderPlaceddttm(orderPlacedTime);
			pluBean.setSysOrderLineid(omOrderLineSeq);
			pluBean.setSysPluId(promotionRefId);
			pluBean.setSysProductId(productRefId);			
			insertOrderLinePluList.add(pluBean);
		}
		this.updateOrderLinePlu(insertOrderLinePluList, updateOrderLinePluList);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit pluDetailProcess method of OrderDAOImpl ");
		}
		
	}

	/**
	 * Update PLU information of an order item in Exception.
	 * 
	 * @param orderPlacedTime
	 * @param pluDetail
	 * @param produtID
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 */
	private void pluDetailExceptionProcess(String orderPlacedTime,
			OrderItem orderItem, long orderLineReferenceID, long productRefId)
			throws PhotoOmniException, SQLException {

		String pluQry = RealTimeOrderQuery.insertExcepALUDetails().toString();
		
		List<OrderlinePluBean> orderLinepluList = this.getOrderLinePluData(orderPlacedTime, orderLineReferenceID);
		List<PLUDetail> pluDetail = orderItem.getPluDetails();
		List<OrderlinePluBean> insertOrderLinePluList = new ArrayList<OrderlinePluBean>();
		List<OrderlinePluBean> updateOrderLinePluList = new ArrayList<OrderlinePluBean>();
		for (int plu = 0; plu < pluDetail.size(); plu++) {
			PLUDetail pluDetailContent = pluDetail.get(plu);
			OrderlinePluBean pluBean = this.getOrderLinePLUBean(orderLinepluList, pluDetailContent.getPluId());
			if(pluBean.isInserInd()){
				long promotionRefId = this.getSysPluID(pluDetailContent.getPluId());
				pluBean.setActiveCd(1);
				pluBean.setDiscountAmount(pluDetailContent.getDiscountAmt());
				pluBean.setOrderPlaceddttm(orderPlacedTime);
				pluBean.setSysOrderLineid(orderLineReferenceID);
				pluBean.setSysPluId(promotionRefId);
				pluBean.setSysProductId(productRefId);
				insertOrderLinePluList.add(pluBean);
			} else{
				pluBean.setDiscountAmount(pluDetailContent.getDiscountAmt());
			//	pluBean.setOrderPlaceddttm(orderPlacedTime);
				updateOrderLinePluList.add(pluBean);
			}			

		}
		this.updateOrderLinePlu(insertOrderLinePluList, updateOrderLinePluList);
	}
	//New Insert/update method
			public OrderlinePluBean getOrderLinePLUBean(
					List<OrderlinePluBean> orderLinePLUList, String pluNBR) {
				OrderlinePluBean dataBean = null;
				for (OrderlinePluBean pluBean : orderLinePLUList) {
					if (pluBean.getPluNumber().equalsIgnoreCase(pluNBR)) {
						dataBean = pluBean;
						dataBean.setUpdateInd(true);
						dataBean.setInserInd(false);
						break;
					}
				}
				if (null == dataBean) {
					dataBean = new OrderlinePluBean();
					dataBean.setUpdateInd(false);
					dataBean.setInserInd(true);
					
				}

				return dataBean;
			}
			//Update/Insert method JDBC
			
			public void updateOrderLinePlu(final List<OrderlinePluBean> insertOrderPluList,final List<OrderlinePluBean> updateOrderPluList) throws SQLException {
				
				String insertQry = RealTimeOrderQuery.insertQueryALUDetails().toString();
				String updateQry = RealTimeOrderQuery.updateQueryALUDetails().toString();
				// insert block
				if (!insertOrderPluList.isEmpty()) {
					jdbcTemplate.batchUpdate(insertQry,
							new BatchPreparedStatementSetter() {

								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									OrderlinePluBean pluBean = insertOrderPluList.get(i);
									ps.setLong(1, this.getTableSequenceId("OM_ORDER_LINE_PLU"));
									ps.setLong(2, pluBean.getSysOrderLineid());
									ps.setLong(3, pluBean.getActiveCd());
									ps.setLong(4,pluBean.getSysProductId());
									ps.setLong(5, pluBean.getSysPluId());
									ps.setString(6, pluBean.getDiscountAmount());
									ps.setTimestamp(7, ServiceUtil.dateFormatter(pluBean.getOrderPlaceddttm()));
									ps.setString(8, PhotoOmniConstants.CREATE_USER_ID);
									ps.setDate(9, getCurrentDate());
									ps.setString(10, PhotoOmniConstants.UPDATE_USER_ID);
									ps.setDate(11, getCurrentDate());

								}
								
								private java.sql.Date  getCurrentDate() {
									java.util.Date today = new java.util.Date();
									// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
									return new java.sql.Date(today.getTime());
								}
								public long getTableSequenceId(String sequenceName) {
									long sequence = 0;
									StringBuilder seqSql = new StringBuilder();
									seqSql.append("SELECT ").append(sequenceName)
											.append("_SEQ.NEXTVAL AS id FROM DUAL");
									sequence = jdbcTemplate.queryForLong(seqSql
											.toString());
									if (LOGGER.isDebugEnabled()) {
										LOGGER.debug(" OM_SHOPPING_CART_PLU next sequence val"
												+ sequence);
									}
									return sequence;
								}

								public int getBatchSize() {
									return insertOrderPluList.size();
								}
							});
				}
				// Update block
				if (!updateOrderPluList.isEmpty()) {
					jdbcTemplate.batchUpdate(updateQry,
							new BatchPreparedStatementSetter() {

								public void setValues(PreparedStatement ps, int i)
										throws SQLException {
									OrderlinePluBean pluBean = updateOrderPluList.get(i);
									ps.setString(1, pluBean.getDiscountAmount());
									ps.setString(2, PhotoOmniConstants.UPDATE_USER_ID);
									ps.setDate(3, getCurrentDate());
									ps.setLong(4, pluBean.getSysOrderLinePluId());
									ps.setString(5,pluBean.getOrderPlaceddttm());
								}

								private java.sql.Date  getCurrentDate() {
									java.util.Date today = new java.util.Date();
									// java.sql.Date sqlDate = new java.sql.Date(today.getTime());
									return new java.sql.Date(today.getTime());
								}

								public int getBatchSize() {
									return updateOrderPluList.size();
								}
							});
				}

			}
	private List<OrderlinePluBean> getOrderLinePluData( String orderPlacedDttm , long refrenceId) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getOrderLinePluData method of OrderDAOImpl ");
		}
		List<OrderlinePluBean> orderLinePluBeanList = null;
		try {
			String orderLinePluDataQry = RealTimeOrderQuery.selorderLinePluQuery().toString();
			Object[] params = new Object[] {ServiceUtil.dateformat24(orderPlacedDttm), refrenceId};
			orderLinePluBeanList = dataGuardJdbcTemplate.query(orderLinePluDataQry, params,
				new OrderLinePluRowMapper());

		} catch (Exception e) {
			LOGGER.error(" Error occurred getOrderLinePluData method of OrderDAOImpl  - "
					,e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getOrderLinePluData method of OrderDAOImpl  ");
			}

		}
		return orderLinePluBeanList;
		
	}

	/**
	 * This method is used for insert Template data into database
	 * 
	 * @param templtQry
	 * @param templtList
	 * @throws PhotoOmniException
	 */
	private void templeteProcess(String orderPlacedTime, long productRefId,
			List<Template> templtList, int omOrderLineSeq)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering templeteProcess method of OrderDAOImpl ");
		}
		String templtQry = RealTimeOrderQuery.insertQueryTemplate().toString();
		for (int tmp = 0; tmp < templtList.size(); tmp++) {

			Template template = templtList.get(tmp);
			long templateRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_TEMPLATE, "SYS_TEMPLATE_ID",
					"TEMPLATE_NBR", template.getTemplateId(), "ACTIVE_CD", 1);
			Timestamp ordrPlaceddatetime = ServiceUtil
					.dateFormatter(orderPlacedTime);
			Object[] objTemplateParms = new Object[] { omOrderLineSeq,
					templateRefId, template.getQuantity(), ordrPlaceddatetime,
					PhotoOmniConstants.CREATE_USER_ID,
					/* PhotoOmniConstants.CREATE_DTTM, */
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					PhotoOmniConstants.ZERO };
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("OM_ORDER_LINE_TEMPLATE inser Qry :" + templtQry
						+ objTemplateParms);
			}
			jdbcTemplate.update(templtQry, objTemplateParms);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit templeteProcess method of OrderDAOImpl ");
		}
	}

	/**
	 * This method is used for insert Template data into database
	 * 
	 * @param templtQry
	 * @param templtList
	 * @throws PhotoOmniException
	 */
	private void templeteUpdateProcess(long orderLineReferenceID,
			List<Template> templtList, String orderPlacedDTTM)
			throws PhotoOmniException {

		String templtQry = RealTimeOrderQuery.updateQueryTemplate().toString();
		for (int tmp = 0; tmp < templtList.size(); tmp++) {

			Template template = templtList.get(tmp);
			long templateRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_TEMPLATE, "SYS_TEMPLATE_ID",
					"TEMPLATE_NBR", template.getTemplateId(), "ACTIVE_CD", 1);
			Object[] objTemplateParms = new Object[] { template.getQuantity(),
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					orderLineReferenceID, templateRefId,
					ServiceUtil.dateformat24(orderPlacedDTTM) };
			jdbcTemplate.update(templtQry, objTemplateParms);
		}
	}

	/**
	 * This method id used for order completion related data to database
	 * 
	 * @param orderRequestBean
	 * @return List of Order Response
	 * @throws PhotoOmniException
	 * @throws ParseException
	 * @throws SQLException 
	 * @throws RuntimeException 
	 * @throws DataAccessException 
	 */
	@Override
	public OrderStatusBean completeOrderDetails(OrderList orders,
			MessageHeader message) throws PhotoOmniException, ParseException, SQLException, DataAccessException, RuntimeException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("DaoImpl:: Complete order starts ");
		}
		OrderStatusBean orderStatus = new OrderStatusBean();
		orderStatus.setStatus(false);
		orderStatus.setOrderPlacedDTTM(orders.getOrder().getSubmittedTime());
		String orderQuery = RealTimeOrderQuery.completeOrderQry().toString();
		String orderAttrQuery = RealTimeOrderQuery.updateOrderAttrQry()
				.toString();

		List<OrderItem> orderItemList = orders.getOrderItem();

		long orderReferenceId = this.getOrderReferenceID(orders.getOrder()
				.getPcpOrderId(), orders.getOrder().getLocationNumber(),orders.getOrder().getSubmittedTime());
		orderStatus.setSysOrderId(orderReferenceId);
		if (orderReferenceId > 0) {

			long locationRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_LOCATION, "SYS_LOCATION_ID",
					"LOCATION_NBR", orders.getOrder().getLocationNumber(),
					"ACTIVE_CD", 1);

			Integer vendorLOCTZOffSet = null;

			Long vendorId;
			if (orders.getOrder().getVendorId().compareTo("0") == 0) {
				// Overriding vendor for kiosk
				vendorId = new Long(0);
			} else {
				vendorId = this.getSystemReferenceID(
						PhotoOmniDBConstants.OM_VENDOR, "SYS_VENDOR_ID",
						"VENDOR_NBR", orders.getOrder().getVendorId(),
						"ACTIVE_CD", 1);
				vendorLOCTZOffSet = this.getTimezoneOffset(vendorId);
			}
			Map<String, Long> vendorDetails = this.getVendorDetails(
					orderReferenceId, ServiceUtil.dateformat24(orders
							.getOrder().getSubmittedTime()));
			Long sysSRCVendorID = null;
			Long sysFullfillmentID = null;
			if (orders.getOrder().getProcessingType().compareToIgnoreCase("H") == PhotoOmniConstants.ZERO) {
				String vendorNbr = orders.getOrder().getVendorId();
				boolean isElectronicsFilmVendor = this
						.isElectronicsFilmVendor(vendorNbr);
				if (isElectronicsFilmVendor) {
					sysFullfillmentID = vendorId;
				} else {
					if (vendorDetails.get("SYS_SRC_VENDOR_ID") == PhotoOmniConstants.ZERO) {
						sysSRCVendorID = vendorId;
					} else {
						sysSRCVendorID = vendorDetails.get("SYS_SRC_VENDOR_ID");
					}
				}

			} else if (orders.getOrder().getProcessingType()
					.compareToIgnoreCase("S") == PhotoOmniConstants.ZERO) {
				if (vendorDetails.get("SYS_FULFILLMENT_VENDOR_ID") == PhotoOmniConstants.ZERO) {
					sysFullfillmentID = vendorId;
				} else {
					sysFullfillmentID = vendorDetails
							.get("SYS_FULFILLMENT_VENDOR_ID");
				}
			}

			int couponCD = (orders.getOrder().getCouponInd()
					.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			int employeeDiscountCD = (orders.getOrder()
					.getEmployeeDiscountInd().compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			/*Submit Order, Complete Order, Order Exception flows 
			 * need to be modified to update OM_ORDER.STATUS based on the below logic:*/
			final String orderCategory = orders.getOrder().getOrderCategory();
			String ordStatus = this.getOldOrderStatus(orderReferenceId);
			final int expenseInd = (orderCategory.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1 : 0;
			if(!ordStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD) && !ordStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_CANCEL)) {
				if (expenseInd == 1) {
					ordStatus = PhotoOmniConstants.ORDER_STATUS_COMPLETE;
				} else {
					ordStatus = PhotoOmniConstants.ORDER_STATUS_DONE;
				}
			}
			
			Object[] objOrderParms = new Object[] {

					orders.getOrder().getTotalDiscountAmount(),
					orders.getOrder().getCalculatedRetail(),
					orders.getOrder().getOriginalRetail(),
					ordStatus,
					couponCD,
					orders.getOrder().getLoyaltyPrice(),
					orders.getOrder().getLoyaltyDiscountAmt(),
					employeeDiscountCD,
					locationRefId,
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					ServiceUtil.dateFormatter(orders.getOrder()
							.getCompletionTime()),
					vendorLOCTZOffSet,
					orderReferenceId,
					ServiceUtil.dateformat24(orders.getOrder()
							.getSubmittedTime()) };

			// Updating Order table

			jdbcTemplate.update(orderQuery, objOrderParms);

			/*String orderCategory = orders.getOrder().getOrderCategory();*/
			/*int expenseInd = (orderCategory.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;*/
			int lateEnvCD = 0;
			String costCalCD = "";
			Long completedBy = null;
			//PROD FIX - CENTRPCA-615 [Completed order information not getting updated]
			//if (orders.getOrder().getOrderStatus()
			//		.compareToIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE) == 0) {
				// Only completed orders will be calculated for lateCD
				SimpleDateFormat sdf = new SimpleDateFormat(
						PhotoOmniConstants.STORE_DATE_PATTERN);
				Date promisedDTTM = sdf.parse(orders.getOrder()
						.getCalculatedPromisedTime());
				Date completedDTTM = sdf.parse(orders.getOrder()
						.getCompletionTime());

				if (completedDTTM.after(promisedDTTM)) {
					lateEnvCD = 1;
				}
				costCalCD = PhotoOmniConstants.COST_CALCULATION_IND; // Updating
																		// with
																		// "P"
																		// for
																		// completed
																		// orders
				if (!orders.getOrder().getCompletedEmployeeId().isEmpty()) {
					completedBy = this.getSystemReferenceID(
							PhotoOmniDBConstants.OM_USER_ATTRIBUTES,
							"SYS_USER_ID", "EMPLOYEE_ID", orders.getOrder()
									.getCompletedEmployeeId());
				}
			//}
			long emplTookOrder = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_USER_ATTRIBUTES, "SYS_USER_ID",
					"EMPLOYEE_ID", orders.getOrder().getEmplTookOrder());
			// Update shopping cart reference into attr table
			/* Modification */			
			Object[] objOrderAttrParms = new Object[] {
					orders.getOrder().getEnvelopeNumber(),

					orders.getOrder().getReferenceId(),
					emplTookOrder,
					orders.getOrder().getServiceCategoryCode(),
					expenseInd,
					costCalCD,
					lateEnvCD,
					orders.getOrder().getProcessingType(),
					completedBy,
					PhotoOmniConstants.UPDATE_USER_ID,
					PhotoOmniConstants.ACTIVE_IND_N,
					orderReferenceId,
					ServiceUtil.dateformat24(orders.getOrder()
							.getSubmittedTime()) };
			// Update Order attributes table
			jdbcTemplate.update(orderAttrQuery, objOrderAttrParms);

			String createUserID = "";
			if (orders.getOrder().getOrderStatus()
					.compareToIgnoreCase(PhotoOmniConstants.ORDER_STATUS_DONE) == 0) {
				createUserID = orders.getOrder().getCompletedEmployeeId();

			} else {
				createUserID = orders.getOrder().getSubmittedEmployeeId();
			}
			// Inserting into Order history table				
			
			OrderHistoryBean historyBean = new OrderHistoryBean();
			historyBean.setAction(orders.getOrder().getOrderStatus());
			// This method is executed for both 'PROC' & DONE or COMPLETE status
			if(orders.getOrder().getOrderStatus().equalsIgnoreCase(PhotoOmniConstants.ORDER_PROC)){
				historyBean.setActionDttm(ServiceUtil.dateFormatter(orders
						.getOrder().getSubmittedTime()));
			}else{
				historyBean.setActionDttm(ServiceUtil.dateFormatter(orders
						.getOrder().getCompletionTime()));
			}
			

			historyBean.setActionNotes(this.getDecodeVal(orders.getOrder()
					.getOrderStatus(), "OrderActions"));
			historyBean.setOrderPlacedDttm(ServiceUtil.dateFormatter(orders
					.getOrder().getSubmittedTime()));
			historyBean.setOrderStatus(orders.getOrder().getOrderStatus());
			historyBean.setCreateUserId(createUserID);
			historyBean.setExceptionId(0);
			historyBean.setOrderId(orderReferenceId);
			this.insertOrderHistoryDetails(historyBean);

			// get shopping cart id of this market basket

			long shoppingCartID = this.getShoppingCartID(locationRefId, orders
					.getOrder().getMarketBasketId(), orders.getOrder()
					.getSubmittedTime());
			List<PLUDetail> mbPLUDetails = orders.getOrder().getMbPLUDetails();
			// process market basket PLU
			this.mbPLUExceptionProcess(mbPLUDetails, shoppingCartID, orders
					.getOrder().getSubmittedTime());
			/*
			 * Process market basket data End
			 */		
			orderStatus.setSysShoppingCartId(shoppingCartID);
			/*// Update shopping c
			String updateCartQry = RealTimeOrderQuery.updateCartPMStatusQry()
					.toString();
			String shopCartPMStatus = this
					.getShopCartPMStatus(orderReferenceId);
			if (!CommonUtil.isNull(shopCartPMStatus)
					&& !"T".equals(shopCartPMStatus)) {
				shopCartPMStatus = PhotoOmniConstants.BLANK;
			}*/
			/*
			 * Commenting as it will be done from MBPM job
			 * // Update Shopping cart pm_status
			Object[] objCartParms = new Object[] {
					shopCartPMStatus,
					PhotoOmniConstants.UPDATE_USER_ID,
					 PhotoOmniConstants.UPDATE_DTTM, 
					shoppingCartID,
					ServiceUtil.dateFormatter(orders.getOrder()
							.getSubmittedTime()) };
			jdbcTemplate.update(updateCartQry, objCartParms);*/
			long machineId = this.getMachineInstanceId(orders.getOrder()
					.getMachineId(), orders.getOrder().getLocationNumber());

			// Process order PLU Details
			List<PLUDetail> orderPLUDetails = orders.getOrder()
					.getOrderPLUDetails();
			this.orderPLUExceptionProcess(orderPLUDetails, orders.getOrder()
					.getSubmittedTime(), orderReferenceId);

			// Process order item
			this.orderItemUpdateProcess(orders, machineId, orderReferenceId,
					orders.getOrder().getSubmittedTime());
			// Update other order Item attributes
			this.updateOrderItemChldAttr(orderItemList, orders.getOrder()
					.getSubmittedTime(), orderReferenceId);
			orderStatus.setStatus(true);
		} else {
			orderStatus.setStatus(false);
		}

		return orderStatus;

	}

	/**
	 * This method handles all type of database related logic for exception .
	 * 
	 * @param orderRequestBean
	 *            holds all the front end parameter.
	 * @return RealTimeOrderResponse.
	 * @throws SQLException 
	 */
	@Override
	public OrderStatusBean updateOrderExceptions(
			OrderList orders, MessageHeader message) throws PhotoOmniException, SQLException {
		LOGGER.info(" Entering OrderException method of RealTimeOrderDAOImpl ");

		String orderQueryWithStatus = RealTimeOrderQuery.completeOrderQry().toString();
		String orderQueryWithOutStatus = RealTimeOrderQuery.completeOrderQryWithOutStatus().toString();
		String orderAttrQuery = RealTimeOrderQuery.updateOrderAttrQry()
				.toString();
		boolean status;

		List<OrderItem> orderItemList = orders.getOrderItem();

		status = false;
		OrderStatusBean orderStatus = new OrderStatusBean();
		orderStatus.setStatus(false);
		orderStatus.setOrderPlacedDTTM(orders.getOrder().getSubmittedTime());
		
		long orderReferenceId = this.getOrderReferenceID(orders.getOrder()
				.getPcpOrderId(), orders.getOrder().getLocationNumber(),orders.getOrder().getSubmittedTime());
		orderStatus.setSysOrderId(orderReferenceId);
		if (orderReferenceId > 0) {
			
			long locationRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_LOCATION, "SYS_LOCATION_ID",
					"LOCATION_NBR", orders.getOrder().getLocationNumber(),
					"ACTIVE_CD", 1);
			Integer vendorLOCTZOffSet = null;
			Long vendorId;
			if (orders.getOrder().getVendorId().compareTo("0") == 0) {
				// Overriding vendor for kiosk
				vendorId = new Long(0);
			} else {
				vendorId = this.getSystemReferenceID(
						PhotoOmniDBConstants.OM_VENDOR, "SYS_VENDOR_ID",
						"VENDOR_NBR", orders.getOrder().getVendorId(),
						"ACTIVE_CD", 1);
				vendorLOCTZOffSet = this.getTimezoneOffset(vendorId);
			}

			Map<String, Long> vendorDetails = this.getVendorDetails(
					orderReferenceId, ServiceUtil.dateformat24(orders
							.getOrder().getSubmittedTime()));
			Long sysSRCVendorID = null;
			Long sysFullfillmentID = null;
			if (orders.getOrder().getProcessingType().compareToIgnoreCase("H") == PhotoOmniConstants.ZERO) {
				String vendorNbr = orders.getOrder().getVendorId();
				boolean isElectronicsFilmVendor = this
						.isElectronicsFilmVendor(vendorNbr);
				if (isElectronicsFilmVendor) {
					sysFullfillmentID = vendorId;
				} else {
					if (vendorDetails.get("SYS_SRC_VENDOR_ID") == PhotoOmniConstants.ZERO) {
						sysSRCVendorID = vendorId;
					} else {
						sysSRCVendorID = vendorDetails.get("SYS_SRC_VENDOR_ID");
					}
				}
			} else if (orders.getOrder().getProcessingType()
					.compareToIgnoreCase("S") == PhotoOmniConstants.ZERO) {
				if (vendorDetails.get("SYS_FULFILLMENT_VENDOR_ID") == PhotoOmniConstants.ZERO) {
					sysFullfillmentID = vendorId;
				} else {
					sysFullfillmentID = vendorDetails
							.get("SYS_FULFILLMENT_VENDOR_ID");
				}
			}

			int couponCD = (orders.getOrder().getCouponInd()
					.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			int employeeDiscountCD = (orders.getOrder()
					.getEmployeeDiscountInd().compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			/*Submit Order, Complete Order, Order Exception flows need 
			 * to be modified to update OM_ORDER.STATUS based on the below logic*/
			final String odrStatus = orders.getOrder().getOrderStatus();
			final String oldOdrStatus = this.getOldOrderStatus(orderReferenceId);
			if(!CommonUtil.isNull(oldOdrStatus) && !oldOdrStatus.equalsIgnoreCase(PhotoOmniConstants.ORDER_STATUS_SOLD)){
				final Object[] objOrderParms = new Object[] { 
						orders.getOrder().getTotalDiscountAmount(),
						orders.getOrder().getCalculatedRetail(), orders.getOrder().getOriginalRetail(), odrStatus,
						couponCD, orders.getOrder().getLoyaltyPrice(), orders.getOrder().getLoyaltyDiscountAmt(),
						employeeDiscountCD, locationRefId, PhotoOmniConstants.UPDATE_USER_ID,
						ServiceUtil.dateFormatter(orders.getOrder().getCompletionTime()), vendorLOCTZOffSet,
						orderReferenceId, ServiceUtil.dateformat24(orders.getOrder().getSubmittedTime()) };
				this.jdbcTemplate.update(orderQueryWithStatus, objOrderParms);
			} else {
				final Object[] objOrderParms = new Object[] {
						orders.getOrder().getTotalDiscountAmount(), orders.getOrder().getCalculatedRetail(),
						orders.getOrder().getOriginalRetail(), couponCD, orders.getOrder().getLoyaltyPrice(),
						orders.getOrder().getLoyaltyDiscountAmt(), employeeDiscountCD, locationRefId,
						PhotoOmniConstants.UPDATE_USER_ID,
						ServiceUtil.dateFormatter(orders.getOrder().getCompletionTime()), vendorLOCTZOffSet,
						orderReferenceId, ServiceUtil.dateformat24(orders.getOrder().getSubmittedTime()) };
				this.jdbcTemplate.update(orderQueryWithOutStatus, objOrderParms);
			}
			

			// Process order PLU Details
			List<PLUDetail> orderPLUDetails = orders.getOrder()
					.getOrderPLUDetails();
			this.orderPLUExceptionProcess(orderPLUDetails, orders.getOrder()
					.getSubmittedTime(), orderReferenceId);

			/*
			 * Process market basket data starts
			 */
			// get shopping cart id of this market basket

			long shoppingCartID = this.getShoppingCartID(locationRefId, orders
					.getOrder().getMarketBasketId(), orders.getOrder()
					.getSubmittedTime());
			List<PLUDetail> mbPLUDetails = orders.getOrder().getMbPLUDetails();
			// process market basket PLU
			this.mbPLUExceptionProcess(mbPLUDetails, shoppingCartID, orders
					.getOrder().getSubmittedTime());
			/*
			 * Process market basket data End
			 */
			orderStatus.setSysShoppingCartId(shoppingCartID);

			// Update Order attributes table
			String orderCategory = orders.getOrder().getOrderCategory();
			int expenseInd = (orderCategory.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1
					: 0;
			int lateEnvCD = PhotoOmniConstants.ZERO;
			String costCalIND = PhotoOmniConstants.COST_CALCULATION_IND;
			Long completedBy = null;
			if (orders.getOrder().getOrderStatus()
					.compareToIgnoreCase(PhotoOmniConstants.ORDER_PROC) == 0) {
				costCalIND = ""; // if it's PROC we don't need to update it with
									// "P"
			}
			long emplTookOrder = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_USER_ATTRIBUTES, "SYS_USER_ID",
					"EMPLOYEE_ID", orders.getOrder().getEmplTookOrder());			
			Object[] objOrderAttrParms = new Object[] {
					orders.getOrder().getEnvelopeNumber(),
					orders.getOrder().getReferenceId(),
					emplTookOrder,
					orders.getOrder().getServiceCategoryCode(),
					expenseInd,
					/*pmStatus,*/
					costCalIND,
					lateEnvCD,
					orders.getOrder().getProcessingType(),
					completedBy,
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					PhotoOmniConstants.ACTIVE_IND_Y,
					orderReferenceId,
					ServiceUtil.dateformat24(orders.getOrder()
							.getSubmittedTime()) };

			jdbcTemplate.update(orderAttrQuery, objOrderAttrParms);

			long machineId = this.getMachineInstanceId(orders.getOrder()
					.getMachineId(), orders.getOrder().getLocationNumber());
			// Process order item
			this.orderItemUpdateProcess(orders, machineId, orderReferenceId,
					orders.getOrder().getSubmittedTime());

			// Update other order Item attributes
			this.updateOrderItemChldAttr(orderItemList, orders.getOrder()
					.getSubmittedTime(), orderReferenceId);

			/* processing Exception Bean */

			this.exceptionProcess(orderReferenceId, orders,locationRefId);

			// Changed for new completion date
			List<OrderException> exceptions = orders.getException();
			if (!CommonUtil.isNull(status) && exceptions.size() > 0) {
				for (OrderException orderException : exceptions) {
					this.updateOrderCompletionDateTime(orderReferenceId,
							orderException);
				}
			}
			// Changed for new completion date

			// Update shopping c
			/*String updateCartQry = RealTimeOrderQuery.updateCartPMStatusQry()
					.toString();
			String shopCartPMStatus = this
					.getShopCartPMStatus(orderReferenceId);
			if (!"T".equals(shopCartPMStatus)) {
				shopCartPMStatus = PhotoOmniConstants.BLANK;
			}*/
			// Update Shopping cart pm_status
			/*Object[] objCartParms = new Object[] {
					shopCartPMStatus,
					PhotoOmniConstants.UPDATE_USER_ID,
					PhotoOmniConstants.UPDATE_DTTM,
					/* PhotoOmniConstants.UPDATE_DTTM, 
					shoppingCartID,
					ServiceUtil.dateFormatter( orders.getOrder().getSubmittedTime())};
			jdbcTemplate.update(updateCartQry, objCartParms);*/
		//	status = true;
			orderStatus.setStatus(true);
		} else {
		//	status = false;
			orderStatus.setStatus(false);
		}

		return orderStatus;
	}

	/**
	 * This method is used for insert exception data into database
	 * 
	 * @param orderReferenceId
	 * @param orders
	 * @param orderPlacedTime
	 * @throws PhotoOmniException
	 * @throws SQLException
	 */
	private void exceptionProcess(long orderReferenceId, OrderList orders,long locationRefrId)
			throws PhotoOmniException {
		String orderExceptionQry = RealTimeOrderQuery.insertQueryException()
				.toString();

		List<OrderException> exceptions = orders.getException();

		for (int i = 0; i < exceptions.size(); i++) {

			OrderException exceptionBean = exceptions.get(i);
			long orderLineID = 0;
			if (Integer.parseInt(exceptionBean.getProductId()) != 0) {
				orderLineID = this.getOrderLineReferenceID(orderReferenceId,
						exceptionBean.getProductId(), orders.getOrder()
						.getSubmittedTime());
			}

			// int duplicateExpId = getDuplicateExceptionId(orderId,
			// orderLineID);
			// if (orderLineID > 0) {
			// INSERT only when a match found
			long sysExceptionId = this
					.getTableSequenceId(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
			Object[] objOrderExcpParms = new Object[] {
					sysExceptionId,
					orderReferenceId,
					orderLineID,
					exceptionBean.getExceptionIdSeq(),
					exceptionBean.getOldOrderStatus(),
					exceptionBean.getOldEnvelopeNbr(),
					exceptionBean.getWasteQty(),
					exceptionBean.getWasteRetailAmount(),
					exceptionBean.getOrderNotes(),
					exceptionBean.getProcessingType(),
					ServiceUtil.dateFormatter(orders.getOrder()
							.getSubmittedTime()),
					exceptionBean.getExceptionEmployeeId().toUpperCase(),
					ServiceUtil.dateFormatter(exceptionBean
							.getOrderCancelDateTime()),
					PhotoOmniConstants.UPDATE_USER_ID,
					locationRefrId
			/* PhotoOmniConstants.UPDATE_DTTM */};

			jdbcTemplate.update(orderExceptionQry, objOrderExcpParms);

			OrderHistoryBean historyBean = new OrderHistoryBean();
			Map<String, Object> exceptionType = this
					.getExceptionType(exceptionBean.getExceptionIdSeq());
			historyBean.setAction(exceptionType.get("EXCEPTION_TYPE")
					.toString());
			historyBean.setActionDttm(ServiceUtil.dateFormatter(exceptionBean
					.getOrderCancelDateTime()));
			historyBean.setActionNotes(exceptionType.get("REASON").toString());
			historyBean.setOrderPlacedDttm(ServiceUtil.dateFormatter(orders
					.getOrder().getSubmittedTime()));
			historyBean.setOrderStatus(orders.getOrder().getOrderStatus());
			historyBean.setCreateUserId(exceptionBean.getExceptionEmployeeId());
			historyBean.setExceptionId(Long.parseLong(exceptionBean.getExceptionIdSeq()));
			historyBean.setOrderId(orderReferenceId);
			this.insertOrderHistoryDetails(historyBean);
			// }
		}
	}

	/**
	 * @param orderReferenceId
	 * @param exceptionBean
	 * @throws DataAccessException
	 */
	private void updateOrderCompletionDateTime(long orderReferenceId,
			OrderException exceptionBean) throws DataAccessException {
		String oldExceptionOrder = this
				.getOldOrderExceptionStatus(orderReferenceId);
		String orderStatus = this.getOldOrderStatus(orderReferenceId);

		if (!CommonUtil.isNull(orderStatus)
				&& orderStatus.equalsIgnoreCase("CANCEL")
				&& !CommonUtil.isNull(oldExceptionOrder)
				&& !oldExceptionOrder.equalsIgnoreCase("CANCEL")) {
			Timestamp orderCancelDateTime = ServiceUtil
					.dateFormatter(exceptionBean.getOrderCancelDateTime());
			String cancelOrderUpdateQuery = RealTimeOrderQuery
					.updateExeOrderQry().toString();
			jdbcTemplate.update(cancelOrderUpdateQuery, new Object[] {
					orderCancelDateTime, PhotoOmniConstants.UPDATE_USER_ID,
					orderReferenceId });

		}
		/* Added CANCEL ORDER IMPLEMENTIOn */
	}

	/**
	 * Return system reference id of supplied table
	 * 
	 * @param tableName
	 *            Table name
	 * @param columnName
	 *            Col name used in Select
	 * @param paramField
	 *            Col name used in Where clause
	 * @param paramVal
	 *            Value of where col field
	 * @return id columnName value or NULL
	 * @throws PhotoOmniException
	 */
	public long getSystemReferenceID(String tableName, String columnName,
			String paramField, String paramVal) throws PhotoOmniException {
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering getSystemReferenceID method of RealTimeOrderDAOImpl ");
		}
		try {

			sqlQuery.append("SELECT ").append(columnName)
					.append(" AS id FROM ").append(tableName);
			sqlQuery.append(" WHERE ").append(paramField).append("=?");

			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { paramVal });
			for (Map<String, Object> row : rows) {
				id = Long.parseLong(row.get("id").toString());
			}
		} catch (NullPointerException ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getSystemReferenceID method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.info(" SQL-" + sqlQuery.toString());
				LOGGER.info(" tableName- :" + tableName + "columnName : "
						+ columnName + "paramField :" + paramField
						+ "paramVal :" + paramVal);
			}
			throw new PhotoOmniException(ne.getMessage());

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getSystemReferenceID method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * Return system reference id of supplied table
	 * 
	 * @param tableName
	 *            Table name
	 * @param columnName
	 *            Col name used in Select
	 * @param paramField
	 *            Col name used in Where clause
	 * @param paramVal
	 *            Value of where col field
	 * @param paramField1
	 *            Col name used in Where clause
	 * @param paramVal1
	 *            Value of where col field
	 * @return id columnName value or NULL
	 * @throws PhotoOmniException
	 */
	public long getSystemReferenceID(String tableName, String columnName,
			String paramField, String paramVal, String paramField1,
			int paramVal1) throws PhotoOmniException {
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering getSystemReferenceID method of RealTimeOrderDAOImpl ");
		}
		try {

			sqlQuery.append("SELECT ").append(columnName)
					.append(" AS id FROM ").append(tableName);
			sqlQuery.append(" WHERE ").append(paramField).append("=?");
			sqlQuery.append(" AND ").append(paramField1).append("=?");
			// System.out.println(SqlQuery.toString());
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { paramVal, paramVal1 });
			for (Map<String, Object> row : rows) {
				id = Long.parseLong(row.get("id").toString());
			}

		} catch (NullPointerException ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getSystemReferenceID method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.info(" SQL-" + sqlQuery.toString());
				LOGGER.info(" tableName- :" + tableName + "columnName : "
						+ columnName + "paramField :" + paramField
						+ "paramVal :" + paramVal);
			}
			throw new PhotoOmniException(ne.getMessage());

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getSystemReferenceID method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * Fetch system reference of Order table by filtering with Order no &
	 * StoreNo
	 * 
	 * @param orderNo
	 *            Order reference from Store
	 * @param StoreNo
	 *            Store no of Location master
	 * @param  OrderPlacedDTTM      
	 * 			  Order placed time	    
	 * @return system reference of Order table or NULL
	 * @throws PhotoOmniException
	 */
	public long getOrderReferenceID(final String orderNo, final String StoreNo, final String orderPlacedDTTM)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering getOrderReferenceID method of RealTimeOrderDAOImpl ");
		}
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		try {
			sqlQuery.append("SELECT SYS_ORDER_ID AS id FROM ").append(
					PhotoOmniDBConstants.OM_ORDER);
			sqlQuery.append(" LEFT JOIN ").append(
					PhotoOmniDBConstants.OM_LOCATION);
			sqlQuery.append(" ON SYS_OWNING_LOC_ID = SYS_LOCATION_ID WHERE ORDER_NBR = ? AND LOCATION_NBR=? AND ACTIVE_CD = 1 AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')");

			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(" getOrderReferenceID method of RealTimeOrderDAOImpl SQL-"
						+ sqlQuery.toString());
				LOGGER.info(" getOrderReferenceID method of RealTimeOrderDAOImpl orderNo- :"
						+ orderNo + "StoreNo : " + StoreNo);
			}
			/*
			 * id = jdbcTemplate.queryForObject( sqlQuery.toString(), new
			 * Object[] { orderNo, StoreNo }, Long.class);
			 */
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { orderNo, StoreNo, ServiceUtil.dateformat24(orderPlacedDTTM) });
			for (Map<String, Object> row : rows) {
				id = Long.parseLong(row.get("id").toString());
			}

		} catch (NullPointerException ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getOrderReferenceID method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.info(" SQL-" + sqlQuery.toString());
				LOGGER.info(" orderNo- :" + orderNo + "StoreNo : " + StoreNo);
			}
			throw new PhotoOmniException(ne.getMessage());

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getOrderReferenceID method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * Fetch system reference of Order line table by filtering with product code
	 * & order system id
	 * 
	 * @param orderReferenceId
	 *            order table System reference of
	 * @param productCode
	 *            Product code of Product master table
	 * @return System reference of Order line table if found else NULL
	 * @throws PhotoOmniException
	 */
	public long getOrderLineReferenceID(long orderReferenceId,
			String productCode, String orderPlacedDTTM) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering getOrderLineReferenceID method of RealTimeOrderDAOImpl ");
		}
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		try {
			sqlQuery.append("SELECT SYS_ORDER_LINE_ID AS id FROM ").append(
					PhotoOmniDBConstants.OM_ORDER_LINE);
			sqlQuery.append(" LEFT JOIN ").append(
					PhotoOmniDBConstants.OM_PRODUCT);
			sqlQuery.append(" USING(SYS_PRODUCT_ID) WHERE SYS_ORDER_ID = ? AND PRODUCT_NBR=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" getOrderLineReferenceID method of RealTimeOrderDAOImpl SQL-"
						+ sqlQuery.toString());
				LOGGER.debug(" getOrderLineReferenceID method of RealTimeOrderDAOImpl orderNo- :"
						+ orderReferenceId + "productCode : " + productCode);
			}

			/*
			 * id = jdbcTemplate.queryForObject( sqlQuery.toString(), new
			 * Object[] { orderReferenceId, productCode}, Long.class);
			 */

			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { orderReferenceId,
							productCode, ServiceUtil.dateformat24(orderPlacedDTTM) });
			for (Map<String, Object> row : rows) {
				id = Long.parseLong(row.get("id").toString());
			}
		} catch (NullPointerException ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getOrderLineReferenceID method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.error(" SQL-" + sqlQuery.toString());
				LOGGER.error(" orderNo- :" + orderReferenceId
						+ " productCode : " + productCode);
			}
			//throw new PhotoOmniException(ne.getMessage());

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getOrderLineReferenceID method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * Update order items data in ORDER COMPLETION PROCESS
	 * 
	 * @param orderItemList
	 *            List of OrderItem Bean
	 * @param orderPlacedTime
	 *            Order submitted time
	 * @param orderReferenceId
	 *            System reference ID of ORDER table
	 * @param orderPlacedDTTM
	 * @throws PhotoOmniException
	 * @throws SQLException 
	 * @throws DataAccessException 
	 * @throws NumberFormatException 
	 */
	private void orderItemUpdateProcess(OrderList order, long machineId,
			long orderReferenceId, String orderPlacedDTTM)
			throws PhotoOmniException, NumberFormatException, DataAccessException, SQLException {
		List<OrderItem> orderItemList = order.getOrderItem();
		String orderItemQry = RealTimeOrderQuery.updateQueryOrderItemRef()
				.toString();
		String orderItemAttrQry = RealTimeOrderQuery
				.updateQryOrderItemAttrRef().toString();
		Long sysMachineOrderId = new Long(machineId);
		if (sysMachineOrderId == 0) {
			sysMachineOrderId = null;
		}
		for (int j = 0; j < orderItemList.size(); j++) {

			OrderItem orderItem = orderItemList.get(j);

			// int productRefId = this.getSystemReferenceID("OM_PRODUCT",
			// "SYS_PRODUCT_ID", "PRODUCT_CODE",
			// Integer.parseInt(orderItem.getpCPProductId()));
			long orderLineReferenceID = this.getOrderLineReferenceID(
					orderReferenceId, orderItem.getpCPProductId(), orderPlacedDTTM);
			
			if(orderLineReferenceID > 0){
				// updating existing orderitems
				ProductAttribute productAttribute = orderItem.getProductAttribute();
	
				Object[] objItemParms = new Object[] {
						orderItem.getQuantity(),
						orderItem.getOriginalRetail(),
						orderItem.getDiscountAmt(),
						orderItem.getCalculatedRetail(),
						orderItem.getLoyaltyPrice(),
						orderItem.getLoyaltyDiscountAmt(),
						this.getEquipInstanceId(sysMachineOrderId,
								Integer.parseInt(orderItem.getEquipmentId())),
						sysMachineOrderId, PhotoOmniConstants.UPDATE_USER_ID,
						/* PhotoOmniConstants.UPDATE_DTTM, */
						orderLineReferenceID,
						ServiceUtil.dateformat24(orderPlacedDTTM) };
	
				jdbcTemplate.update(orderItemQry, objItemParms);
				Integer printQty = Integer.parseInt(productAttribute
						.getNbrSheetsPrinted());
				if ((order.getOrder().getProcessingType().compareToIgnoreCase("H") == PhotoOmniConstants.ZERO)
						&& printQty == PhotoOmniConstants.ZERO) {
					printQty = this.getPrintQty(orderItem);
	
				}
				// Inserting Order Item attributes
				Object[] objItemAttrParms = new Object[] {
						productAttribute.getNbrSets(),
						productAttribute.getPanaromicPrints(), printQty,
						productAttribute.getNbrIndexSheets(),
						productAttribute.getNoOfInputs(), orderItem.getWasteQty(),
						orderItem.getWastePrintCost(),
						PhotoOmniConstants.UPDATE_USER_ID,
						/* PhotoOmniConstants.UPDATE_DTTM, */
						orderLineReferenceID,
						ServiceUtil.dateformat24(orderPlacedDTTM) };
	
				jdbcTemplate.update(orderItemAttrQry, objItemAttrParms);

			} else  {
				
				List<OrderItem> newOrderItemList =  new ArrayList();
				newOrderItemList.add(orderItem);
				this.orderItemProcess(newOrderItemList, orderPlacedDTTM, machineId, (int) orderReferenceId);
			}

			/*
			 * Don't need to update other information. If any changes take
			 * place, then it will be taken care by Exception process
			 */

		}

	}

	private void updateOrderItemChldAttr(List<OrderItem> orderItemList,
			String orderPlacedTime, long orderReferenceId)
			throws PhotoOmniException, SQLException {
		for (int j = 0; j < orderItemList.size(); j++) {
			OrderItem orderItem = orderItemList.get(j);
			long productRefId = this.getSystemReferenceID(
					PhotoOmniDBConstants.OM_PRODUCT, "SYS_PRODUCT_ID",
					"PRODUCT_NBR", orderItem.getpCPProductId());
			long orderLineReferenceID = this.getOrderLineReferenceID(
					orderReferenceId, orderItem.getpCPProductId(), orderPlacedTime);
			if (orderLineReferenceID > 0) {

				// Process template info for this item
				List<Template> templtList = orderItem.getTemplate();
				this.templeteUpdateProcess(orderLineReferenceID, templtList,
						orderPlacedTime);

				// Process license content info
				List<LicenseContent> licncList = orderItem.getLicenseContent();
				this.licenseContentUpdateProcess(orderPlacedTime, productRefId,
						licncList, orderLineReferenceID);

				// process printable sign report
				List<PrintableSign> printableSign = orderItem
						.getPrintableSigns();
				this.printableSignsUpdateProcess(printableSign,
						orderLineReferenceID, orderPlacedTime);

				// Process PLUDetails
				this.pluDetailExceptionProcess(orderPlacedTime, orderItem,
						orderLineReferenceID, productRefId);
			}
		}

	}

	public LCDataResponse updateLicenseContent(LCDataRequest jsonRequest)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering updateLicenseContent method of OrderDAOImpl ");
		}
		LCDataResponse lcResponse = new LCDataResponse();
		List<LCResponseBean> lcList = new ArrayList<LCResponseBean>();
		LCResponseBean lcResponseBean = new LCResponseBean();
		LCSelDataBean  lcSelDataBean = new LCSelDataBean();
		boolean status, validate = false;
		try {
			MessageHeader message = jsonRequest.getMessageHeader();
			List<LicenseContentDownloadReqList> lcReqList = jsonRequest
					.getLcDetailList();
			lcResponse.setMessageHeader(message);

			for (int i = 0; i < lcReqList.size(); i++) {
				LicenseContentDownloadReqList reqList = lcReqList.get(i);
				ErrorDetails errorDetails = new ErrorDetails();
				//String lcSelQuery = RealTimeOrderQuery.selLCOrderQry().toString();
				validate = false;
				int reqStatus = 0;
				status = false;
				try {
					LCDataBean downloadDetail = reqList
							.getLicenseContentDownloadDetail();
					String orderNbr = reqList.getLicenseContentDownloadDetail().getOriginOrderId();
					String productId = reqList.getLicenseContentDownloadDetail().getPcpProductId();
					String lcSelQuery = RealTimeOrderQuery.selLCOrderQry().toString();
					lcSelDataBean =  jdbcTemplate.queryForObject(lcSelQuery, new Object[] { orderNbr,productId },new LCselBeanRowMapper());
							
					
					reqStatus = ServiceUtil
							.validateLicenceCntReq(downloadDetail);
					if (reqStatus == 0) {
						int count = this.updateLCDownloadDetail(downloadDetail,lcSelDataBean);
						if (count == 0) {
							status = false;
							errorDetails = CommonUtil
									.createFailureMessageForValidationException("Invalid order or product provided");
						} else {
							status = true;
						}
					} else {
						validate = true;
					}

				} catch (Exception e) {
					status = false;
					errorDetails = CommonUtil
							.createFailureMessageForDBException(e);
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error("Exception occured in updateLicenseContent Dao Detail Insertion of OrderDAOImpl",e);
					}
					throw new PhotoOmniException(e.getMessage());
				} finally {
					if (status) {
						lcResponseBean.setStatus(status);
						lcResponseBean.setErrorDetails(null);
					} else {
						if (validate) {
							errorDetails = CommonUtil
									.createFailureMessageForValidationException(ServiceUtil
											.orderValidatemsg(reqStatus));
						}
						lcResponseBean.setStatus(status);
						lcResponseBean.setErrorDetails(errorDetails);
					}
					lcList.add(lcResponseBean);
				}
			}
			lcResponse.setLicenseContentDownloadDetailList(lcList);
		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Exception occurred in LicenseContentDAOImpl.setLcDetail",e);
			}
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting updateLicenseContent method of OrderDAOImpl ");
			}
		}
		return lcResponse;
	}

	public int updateLCDownloadDetail(LCDataBean lcDownloadDetail, LCSelDataBean lcSelDataDetailBean)
			throws DataAccessException, SQLException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateLCDownloadDetail method of OrderDAOImpl ");
		}
		int count = 0;
		String downloadDttm = ServiceUtil.dateformat24(lcDownloadDetail.getDownloadDttm());
		SimpleDateFormat targetFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String orderPlacedDTTM = targetFormatter.format(lcSelDataDetailBean.getOrderPlacedDttm());
		
		int getDownloadedInd = (lcDownloadDetail.getDownloadedInd()
				.compareToIgnoreCase("Y") == PhotoOmniConstants.ZERO) ? 1 : 0;
		try {
			String lcQuery = RealTimeOrderQuery.updateLCOrderQry().toString();
			count = jdbcTemplate.update(lcQuery,
					new Object[] { downloadDttm, getDownloadedInd,
							lcDownloadDetail.getLicenseContentId(),
							lcSelDataDetailBean.getOrderlineId(),
							orderPlacedDTTM });

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("error occurred in updateLCDownloadDetail method of OrderDAOImpl",e);
			}
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.debug(" Exiting updateLCDownloadDetail method of OrderDAOImpl ");
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#updateLabelPrntInfo(com.walgreens.common
	 * .json.bean.LabelDataRequest)
	 */
	public LabelDataResponse updateLabelPrntInfo(
			LabelDataRequest labelDataRequest) throws PhotoOmniException {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Entering updateLabelPrntInfo method of OrderDAOImpl ");
		}
		LabelDataResponse labelDataResponse = new LabelDataResponse();
		MessageHeader message = labelDataRequest.getMessageHeader();
		List<LabelPrintDetails> labelPrintDetailList = new ArrayList<LabelPrintDetails>();
		labelDataResponse.setMessageHeader(message);
		try {

			CommonUtil commonUtil = new CommonUtil();
			List<LabelPrintDetails> labelPrintDetailsList = labelDataRequest
					.getLabelPrintDetailsList();

			String updateEmplTkQuery = RealTimeOrderQuery.updateTookOrderQry()
					.toString();
			

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Inside updateLabelPrntInfo of  OrderDAOImpl :: updateEmplTkQuery sql :: "
						+ updateEmplTkQuery);
				LOGGER.debug(" JSON labelDataRequest data :: "
						+ labelDataRequest.toString());
			}
			for (int i = 0; i < labelPrintDetailsList.size(); i++) {
				Boolean status = false;
				LabelPrintDetails labelPrintDetail = labelPrintDetailsList
						.get(i);
				ErrorDetails errorDetails = new ErrorDetails();
				long emplTookOrder = 0;
				Map<String, Object> orderRefID = new HashMap<String, Object>();
				try {
					orderRefID = this.getOrderRef(
							labelPrintDetail.getOrderId(),
							labelPrintDetail.getLocationNumber());


					emplTookOrder = this.getSystemReferenceID(PhotoOmniDBConstants.OM_USER_ATTRIBUTES, "SYS_USER_ID", "EMPLOYEE_ID", labelPrintDetail.getEmployeeId());
					if (!CommonUtil.isNull(orderRefID.get("SYS_ORDER_ID")) && commonUtil.bigDecimalToInt(orderRefID
							.get("SYS_ORDER_ID")) > 0 && emplTookOrder > 0 ) {

						labelPrintDetail.setSysOrderId(Long.parseLong(orderRefID.get("SYS_ORDER_ID").toString()));
						labelPrintDetail.setOrderPlacedDTTM((String) orderRefID.get("ORDER_SUBMITTED_TIME"));
						labelPrintDetail.setSysShoppingCartId(Long.parseLong(orderRefID.get("SYS_SHOPPING_CART_ID").toString()));
						// Update empl_took_order, PM_STATUS
						Object[] objTookOrdParms = new Object[] {
								emplTookOrder,
								PhotoOmniConstants.UPDATE_USER_ID,
								// PhotoOmniConstants.UPDATE_DTTM,
								orderRefID.get("SYS_ORDER_ID"),
								orderRefID.get("ORDER_PLACED_DTTM") };
						jdbcTemplate.update(updateEmplTkQuery, objTookOrdParms);

						// insert into history table
						Timestamp ref = (Timestamp) (orderRefID
								.get("ORDER_PLACED_DTTM"));
						OrderHistoryBean orderHistoryBean = new OrderHistoryBean();
						orderHistoryBean.setOrderId(Long.parseLong(orderRefID
								.get("SYS_ORDER_ID").toString()));
						orderHistoryBean
								.setActionNotes(PhotoOmniConstants.ORDER_LABEL_PRNT_ACTION_CODE);
						orderHistoryBean.setOrderPlacedDttm(ref);
						orderHistoryBean.setOrderStatus(orderRefID
								.get("STATUS").toString());
						orderHistoryBean.setActionDttm(ServiceUtil
								.dateFormatter(labelPrintDetail
										.getLabelPrintDTTM()));
						orderHistoryBean
								.setAction(this
										.getDecodeVal(
												PhotoOmniConstants.ORDER_LABEL_PRNT_ACTION_CODE,
												"OrderActions"));
						orderHistoryBean
								.setExceptionId(PhotoOmniConstants.ZERO);
						orderHistoryBean.setCreateUserId(labelPrintDetail
								.getEmployeeId());
						orderHistoryBean
								.setUpdateUserId(PhotoOmniConstants.UPDATE_USER_ID);
						this.insertOrderHistoryDetails(orderHistoryBean);
						
						status = true;

					} else {

						status = false;
						if (orderRefID.isEmpty()) {
							errorDetails = CommonUtil
									.createFailureMessageForValidationException("Invalid Order no");
						} else if (emplTookOrder == 0) {
							errorDetails = CommonUtil
									.createFailureMessageForValidationException("Invalid Employee ID");
						}
					}
				} catch (Exception e) {
					status = false;

					if (orderRefID.isEmpty()) {
						errorDetails = CommonUtil
								.createFailureMessageForValidationException("Invalid Order no");
					} else if (emplTookOrder == 0) {
						errorDetails = CommonUtil
								.createFailureMessageForValidationException("Invalid Employee ID");
					} else {
						errorDetails = CommonUtil
								.createFailureMessageForDBException(e);
					}

					if (LOGGER.isErrorEnabled()) {
						LOGGER.error(" Exception occoured at updateLabelPrntInfo method of OrderDAOImpl - "
								+ e.getMessage());
						LOGGER.error(" Exception occoured at updateLabelPrntInfo method of OrderDAOImpl - "
								+ e.getStackTrace().toString());
					}
					throw new PhotoOmniException(e.getMessage());
				} finally {
					if (status) {
						labelPrintDetail.setStatus(true);
						labelPrintDetail.setErrorDetails(null);
					} else {
						labelPrintDetail.setStatus(false);
						labelPrintDetail.setErrorDetails(errorDetails);
					}
					labelPrintDetailList.add(labelPrintDetail);
				}

			}

		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" DataAccessException occoured at updateLabelPrntInfo method of OrderDAOImpl - "
						+ e.getMessage());
			}

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Exception occoured at updateLabelPrntInfo method of OrderDAOImpl - "
						+ e.getMessage());
			}

		}

		labelDataResponse.setLabelPrintDetails(labelPrintDetailList);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exit from updateLabelPrntInfo method of OrderDAOImpl ");
		}
		return labelDataResponse;
	}

	/**
	 * Get order details by location & orderNo
	 * 
	 * @param orderNo
	 * @param locationNo
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> getOrderRef(String orderNo, long locationNo)
			throws SQLException {
		Map<String, Object> orderData = new HashMap<String, Object>();
		String selectQuery = RealTimeOrderQuery.selectOrderLabelDataQry()
				.toString();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				selectQuery.toString(), new Object[] { orderNo, locationNo });
		for (Map<String, Object> row : rows) {
			orderData.put("SYS_ORDER_ID", row.get("SYS_ORDER_ID"));
			orderData.put("STATUS", row.get("STATUS"));
			orderData.put("ORDER_PLACED_DTTM", row.get("ORDER_PLACED_DTTM"));
			orderData.put("ORDER_SUBMITTED_TIME", row.get("ORDER_SUBMITTED_TIME"));
			orderData.put("SYS_SHOPPING_CART_ID",
					row.get("SYS_SHOPPING_CART_ID"));
		}
		return orderData;
	}

	@Override
	/**
	 * Get PM Report by Product details filter by employee Id, startDate, endDate, storeNumber
	 * 
	 * @param PMByProductFilter
	 * @return List of PMReportResponseBean
	 */
	public List<PMReportResponseBean> fetchPMEarnedByProduct(
			PMByProductFilter requestBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering fetchPMEarnedByProduct method of OrderDAOImpl ");
		}
		List<PMReportResponseBean> responseBeanList = null;
		try {
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("DAO Impl Start....");
			}
			String selectQuery = PMReportByProductQuery
					.selectPMByProductDetails(requestBean);
			String empId = requestBean.getEmployeeId();
			String locationNumber = requestBean.getStoreNumber();
			// Map<String, Long> pageLimit =
			// CommonUtil.getPaginationLimit(requestBean.getCurrentPageNo(),PhotoOmniConstants.EXCEPTION_REPORT_PAGINATION_SIZE);
			String startDate = CommonUtil.stringDateFormatChange(
					requestBean.getStartDate(),
					PhotoOmniConstants.DATE_FORMAT_SEVEN,
					PhotoOmniConstants.DATE_FORMAT_TWO);
			startDate = startDate + " 00:00:00";
			String endDate = CommonUtil.stringDateFormatChange(
					requestBean.getEndDate(),
					PhotoOmniConstants.DATE_FORMAT_SEVEN,
					PhotoOmniConstants.DATE_FORMAT_TWO);
			endDate = endDate + " 23:59:59";
			/*
			 * String startLimit =
			 * !CommonUtil.isNull(pageLimit.get("START_LIMIT")) ?
			 * pageLimit.get("START_LIMIT").toString() : ""; String endLimit =
			 * !CommonUtil.isNull(pageLimit.get("END_LIMIT")) ?
			 * pageLimit.get("END_LIMIT").toString() : "";
			 */
			Object[] params = {locationNumber, empId, startDate, endDate, startDate,locationNumber, empId,
					startDate, endDate, startDate };
			responseBeanList = dataGuardJdbcTemplate.query(selectQuery, params,
					new PMReportByProductRowMapperTest());
		} catch (DataAccessException e) {
			LOGGER.error(" DataAccessException occoured at fetchPMEarnedByProduct method of OrderDAOImpl - "
					+ e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Exception occoured at fetchPMEarnedByProduct method of OrderDAOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting fetchPMEarnedByProduct method of OrderDAOImpl ");
			}
		}
		return responseBeanList;
	}

	/**
	 * @param orderPlacedDTTM
	 * @return
	 */
	public int getCalenderId(String orderPlacedDTTM) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering OrderDAOImpl getCalenderId()");
		}
		String getCalenderIdQuery = RealTimeOrderQuery.getCalenderIdQuery()
				.toString();
		int calenderId = 0;
		try {
			Date placedDate = new SimpleDateFormat(
					PhotoOmniConstants.STORE_DATE_PATTERN)
					.parse(orderPlacedDTTM);
			String date = new SimpleDateFormat(
					PhotoOmniConstants.CALENDER_DATE_PATTERN)
					.format(placedDate);
			calenderId = jdbcTemplate.queryForObject(getCalenderIdQuery,
					new Object[] { date }, Integer.class);

		} catch (Exception ex) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("Exception occured in OrderDAOImpl getCalenderId()");
			}
		} finally {

		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting OrderDAOImpl getCalenderId()");
		}
		return calenderId;
	}

	public int getCodeDecodeVal(String codeID) {
		int decode = 0;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT SYS_CODE_DECODE_ID AS decode FROM ").append(
				PhotoOmniDBConstants.OM_CODE_DECODE);
		sqlQuery.append(" WHERE CODE_ID=?");
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { codeID });
		for (Map<String, Object> row : rows) {
			decode = ServiceUtil.bigDecimalToInt(row.get("decode"));
		}
		return decode;
	}

	private long getMachineInstanceId(String machineId, String locationNo) {
		long id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT SYS_MACHINE_INSTANCE_ID AS id FROM ");
		sqlQuery.append(PhotoOmniDBConstants.OM_MACHINE_INSTANCE).append(
				" OMI INNER JOIN ");
		sqlQuery.append(PhotoOmniDBConstants.OM_LOCATION);
		// sqlQuery.append(" OL USING (SYS_LOCATION_ID) WHERE MACHINE_NAME =? AND LOCATION_NBR=? AND OL.ACTIVE_CD = 1 AND OMI.ACTIVE_CD = 1");
		sqlQuery.append(" OL USING (SYS_LOCATION_ID) WHERE MACHINE_NAME =? AND LOCATION_NBR=? AND OL.ACTIVE_CD = 1 ");
		// System.out.println(SqlQuery.toString());
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { machineId, locationNo });
		for (Map<String, Object> row : rows) {
			id = Long.parseLong(row.get("id").toString());
		}
		return id;
	}

	/**
	 * Method to persist filter details to Om_user_report_prefs table
	 * 
	 * @param filterState
	 * @return PMBYWICResponseBean
	 * @throws DataAccessException
	 */
	public PMBYWICResponseBean submitPMWICReportRequest(String filterState) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderDAOImplsubmitPMWICReportRequest method");
		}
		final String sql = ReportsQuery.getCustomreportInsertQuery().toString();
		PMBYWICResponseBean pmbywicResponseBean = new PMBYWICResponseBean();
		boolean bStatus = false;
		ErrorDetails errorDetails = null;
		KeyHolder holder = new GeneratedKeyHolder();
		long id = 0;
		try {
			final Map<String, Object> paramMap = populateParams(filterState, ReportsConstant.PMBYWIC);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql.toString(),
							new String[] { "SYS_USER_REPORT_PREF_ID" });
					ps.setLong(1, (Long) paramMap.get(ReportsConstant.USER_ID));
					ps.setLong(2, (Long) paramMap.get(ReportsConstant.REPORT_ID));
					ps.setString(3, (String) paramMap.get(ReportsConstant.PREFERENCE_TYPE));
					ps.setString(4, (String) paramMap.get(ReportsConstant.FILTER_STATE));
					ps.setInt(5, (Integer) paramMap.get(ReportsConstant.AUTO_REFRESH_IND));
					ps.setInt(6, (Integer) paramMap.get(ReportsConstant.AUTO_EXECUTE_IND));
					ps.setInt(7, (Integer) paramMap.get(ReportsConstant.AUTO_EXECUTE_INT));
					ps.setInt(8, (Integer) paramMap.get(ReportsConstant.ACTIVE_CD));
					ps.setString(9, (String) paramMap.get(ReportsConstant.CRATED_BY));
					ps.setString(10, (String) paramMap.get(ReportsConstant.UPDATED_BY));
					ps.setInt(11, (Integer) paramMap.get(ReportsConstant.FILTER_ENABLE_IND));
					return ps;
				}
			}, holder);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
			id = holder.getKey().longValue();
			bStatus = true;
		} catch (DataAccessException e) {
			LOGGER.error("Error occoured at OrderDAOImpl.submitPMWICReportRequest() method",e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForDBException(e);
		} catch (Exception e) {
			LOGGER.error("Error occoured at OrderDAOImpl.submitPMWICReportRequest() method",e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForSystemException(e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.submitPMWICReportRequest() method");
			}
		}
		pmbywicResponseBean.setStatus(bStatus);
		if (null != errorDetails) {
			pmbywicResponseBean.setErrorDetails(errorDetails);
			pmbywicResponseBean.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} else {
			pmbywicResponseBean.setResponseMessage(ReportsConstant.SUCCESS_MESSAGE);
			pmbywicResponseBean.setReportId(id);
		}
		return pmbywicResponseBean;
	}

	/**
	 * Method to populate parameters for Om_user_report_prefs table
	 * 
	 * @param filterState
	 * @param reportName
	 * @return MAp<String, Object> -- parameters as key value pair
	 * @throws DataAccessException
	 */
	private Map<String, Object> populateParams(String filterState,
			String reportName) throws DataAccessException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered OrderDAOImpl.populateParams() method");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(ReportsConstant.USER_ID, getUserId());
		paramMap.put(ReportsConstant.REPORT_ID, getReportId(reportName));
		paramMap.put(ReportsConstant.PREFERENCE_TYPE, ReportsConstant.BATCH);
		paramMap.put(ReportsConstant.FILTER_STATE, filterState);
		paramMap.put(ReportsConstant.AUTO_REFRESH_IND,
				ReportsConstant.INDICATOR);
		paramMap.put(ReportsConstant.AUTO_EXECUTE_IND,
				ReportsConstant.INDICATOR);
		paramMap.put(ReportsConstant.AUTO_EXECUTE_INT, 0);
		paramMap.put(ReportsConstant.ACTIVE_CD, ReportsConstant.INDICATOR);
		paramMap.put(ReportsConstant.CRATED_BY,
				PhotoOmniConstants.CREATE_USER_ID);
		paramMap.put(ReportsConstant.UPDATED_BY,
				PhotoOmniConstants.UPDATE_USER_ID);
		paramMap.put(ReportsConstant.FILTER_ENABLE_IND,
				ReportsConstant.INDICATOR);
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from OrderDAOImpl.populateParams() method");
		}
		return paramMap;
	}

	/**
	 * Method to get report id based on reportName 
	 * 
	 * @param reportname
	 * @return Long -- ReportID 
	 * @throws DataAccessException
	 */
	private Long getReportId(String reportname) throws DataAccessException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into OrderDAOImpl.getReportId() method");
		}
		try {
		return jdbcTemplate.queryForObject(
				ReportsQuery.getPMReportIdQuery().toString(), Long.class, reportname);
		}catch(DataAccessException e){
			LOGGER.error("Error Occured at  OrderDAOImpl.getReportId() method" + e);
			throw e;
		}finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.getReportId() method");
			}
		}
	}

	/**
	 * Method to get userId details from database using logged in user details
	 * 
	 * @return Long -- userId of logged in user 
	 * @throws DataAccessException 
	 */
	private Long getUserId() throws DataAccessException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into OrderDAOImpl.getUserId() method");
		}
		Long userId = null;
		try {
			ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();
			String empId = ((SAMLUserDetails) authentication.getDetails()).getEmployeenumber();
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Logged in user name " + empId);
			}
			userId  = jdbcTemplate.queryForObject(
					ReportsQuery.getUserIdQuery().toString(), Long.class, empId);
		}catch(DataAccessException e){
			LOGGER.error("Error Occured at  OrderDAOImpl.getUserId() method" + e);
			throw e;
		}finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.getUserId() method");
			}
		}
		return userId;
	}

	/**
	 * Method to persist royalty report request
	 * 
	 * @param filterState
	 * @return RoyaltyReportResponseBean
	 * @throws PhotoOmniException
	 */
	public RoyaltyReportResponseBean submitRoyaltyReportRequest(
			String filterState) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering OrderDAOImpl.submitRoyaltyReportRequest() method");
		}
		final String strInsertQuery = ReportsQuery.getCustomreportInsertQuery().toString();
		RoyaltyReportResponseBean royaltyReportResponseBean = new RoyaltyReportResponseBean();
		boolean bStatus = false;
		ErrorDetails errorDetails = null;
		KeyHolder holder = new GeneratedKeyHolder();
		long id = 0;
		try {
			final Map<String, Object> paramMap = populateParams(filterState,
					ReportsConstant.ROYALTY);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							strInsertQuery.toString(),
							new String[] { "SYS_USER_REPORT_PREF_ID" });
					ps.setLong(1, (Long) paramMap.get(ReportsConstant.USER_ID));
					ps.setLong(2,	(Long) paramMap.get(ReportsConstant.REPORT_ID));
					ps.setString(3, (String) paramMap	.get(ReportsConstant.PREFERENCE_TYPE));
					ps.setString(4,	(String) paramMap.get(ReportsConstant.FILTER_STATE));
					ps.setInt(5, (Integer) paramMap	.get(ReportsConstant.AUTO_REFRESH_IND));
					ps.setInt(6, (Integer) paramMap	.get(ReportsConstant.AUTO_EXECUTE_IND));
					ps.setInt(7, (Integer) paramMap	.get(ReportsConstant.AUTO_EXECUTE_INT));
					ps.setInt(8,	(Integer) paramMap.get(ReportsConstant.ACTIVE_CD));
					ps.setString(9,	(String) paramMap.get(ReportsConstant.CRATED_BY));
					ps.setString(10,	(String) paramMap.get(ReportsConstant.UPDATED_BY));
					ps.setInt(11, (Integer) paramMap	.get(ReportsConstant.FILTER_ENABLE_IND));
					return ps;
				}
			}, holder);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
			id = holder.getKey().longValue();
			bStatus = true;
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.submitRoyaltyReportRequest() method "+ e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForDBException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.submitRoyaltyReportRequest() method "+ e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForSystemException(e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.submitRoyaltyReportRequest() method ");
			}
		}
		royaltyReportResponseBean.setStatus(bStatus);
		if (null != errorDetails) {
			royaltyReportResponseBean.setErrorDetails(errorDetails);
			royaltyReportResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} else {
			royaltyReportResponseBean
			.setResponseMessage(ReportsConstant.SUCCESS_MESSAGE);
			royaltyReportResponseBean.setReportId(id);
		}
		return royaltyReportResponseBean;
	}

	/**
	 * Method to get Royalty Vendor List Details.
	 * 
	 * @return List<VendorType> -- List of Royalty vendor details
	 * @throws PhotoOmniException -- Custom Exception
	 */
	public List<VendorType> getRoyaltyVendorNameList()
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into OrderDAOImpl.getRoyaltyVendorNameList() method");
		}
		List<VendorType> lstVendorDetails = null;
		try {
			lstVendorDetails = dataGuardJdbcTemplate.query(ReportsQuery
					.getVendorListQueryForRoyaltyReport().toString(),
					new RoyaltyVendorListRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.getRoyaltyVendorNameList() method - "+ e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.getRoyaltyVendorNameList() method - "+ e);
			throw new PhotoOmniException(e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.getRoyaltyVendorNameList() method");
			}
		}
		return lstVendorDetails;
	}

	/**
	 * Method to get Product filter list
	 * 
	 * @param codeType
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> getCodeDecodeValue(String codeType) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into OrderDAOImpl.getCodeDecodeValue() method ");
		}
		List<Map<String, Object>> resultList = null;
		try {
			String sql = ReportsQuery.getProductFilterQuery().toString();
			resultList = dataGuardJdbcTemplate.queryForList(sql,
					new Object[] { codeType });
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.getCodeDecodeValue() method" + e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at OrderDAOImpl.getCodeDecodeValue() method "+ e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from OrderDAOImpl.getCodeDecodeValue() method");
			}
		}
		return resultList;
	}

	/**
	 * Method to persist Sales report by product request
	 * 
	 * @param filterState
	 * @return SalesByProductResponseBean
	 */
	@Override
	public SalesByProductResponseBean saveSalesReportByProductData(
			String filterState) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering OrderDAOImpl.saveSalesReportByProductData() method");
		}
		final String strInsertQuery = ReportsQuery.getCustomreportInsertQuery().toString();
		SalesByProductResponseBean salesByProductResponseBean = new SalesByProductResponseBean();
		boolean bStatus = false;
		ErrorDetails errorDetails = null;
		KeyHolder holder = new GeneratedKeyHolder();
		long id = 0;
		try {
			final Map<String, Object> paramMap = populateParams(filterState,
					ReportsConstant.SALESREPORT);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(
							strInsertQuery.toString(),
							new String[] { "SYS_USER_REPORT_PREF_ID" });
					ps.setLong(1, (Long) paramMap.get(ReportsConstant.USER_ID));
					ps.setLong(2, (Long) paramMap.get(ReportsConstant.REPORT_ID));
					ps.setString(3, (String) paramMap.get(ReportsConstant.PREFERENCE_TYPE));
					ps.setString(4,	(String) paramMap.get(ReportsConstant.FILTER_STATE));
					ps.setInt(5, (Integer) paramMap.get(ReportsConstant.AUTO_REFRESH_IND));
					ps.setInt(6, (Integer) paramMap.get(ReportsConstant.AUTO_EXECUTE_IND));
					ps.setInt(7, (Integer) paramMap.get(ReportsConstant.AUTO_EXECUTE_INT));
					ps.setInt(8,(Integer) paramMap.get(ReportsConstant.ACTIVE_CD));
					ps.setString(9,(String) paramMap.get(ReportsConstant.CRATED_BY));
					ps.setString(10,(String) paramMap.get(ReportsConstant.UPDATED_BY));
					ps.setInt(11, (Integer) paramMap.get(ReportsConstant.FILTER_ENABLE_IND));
					return ps;
				}
			}, holder);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
			id = holder.getKey().longValue();
			bStatus = true;
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at  OrderDAOImpl.saveSalesReportByProductData() method "
					+ e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForDBException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at  OrderDAOImpl.saveSalesReportByProductData() method"
					+ e);
			bStatus = false;
			errorDetails = CommonUtil.createFailureMessageForSystemException(e);
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from  OrderDAOImpl.saveSalesReportByProductData() method");
			}
		}
		salesByProductResponseBean.setStatus(bStatus);
		if (null != errorDetails) {
			salesByProductResponseBean.setErrorDetails(errorDetails);
			salesByProductResponseBean
			.setResponseMessage(ReportsConstant.FAILURE_MESSAGE);
		} else {
			salesByProductResponseBean
			.setResponseMessage(ReportsConstant.SUCCESS_MESSAGE);
			salesByProductResponseBean.setReportId(id);
		}
		return salesByProductResponseBean;
	}

	@Override
	public DailyPLUResBean submitPLUReportRequest(String strFilterState)
			throws Exception {

		LOGGER.info("Entering into OrderDAOImpl.submitPLUReportRequest()");
		DailyPLUResBean objDailyPLUResBean = null;
		final String strInsertQuery = ReportsQuery.getCustomreportInsertQuery()
				.toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.submitPLUReportRequest()");
			LOGGER.debug("Insert SQL for sumitting PLU request "
					+ strInsertQuery.toString());
		}
		final Map<String, Object> mpParams = populateParams(strFilterState,
				ReportsConstant.PLU_REPORT);
		ErrorDetails errorDetails = null;
		boolean pluStatus = false;
		KeyHolder holder = new GeneratedKeyHolder();
		long id = 0;
		try {
			objDailyPLUResBean = new DailyPLUResBean();
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(false);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(
							strInsertQuery.toString(),
							new String[] { "SYS_USER_REPORT_PREF_ID" });
					ps.setLong(1, (Long) mpParams.get(ReportsConstant.USER_ID));
					ps.setLong(2,
							(Long) mpParams.get(ReportsConstant.REPORT_ID));
					ps.setString(3, (String) mpParams
							.get(ReportsConstant.PREFERENCE_TYPE));
					ps.setString(4,
							(String) mpParams.get(ReportsConstant.FILTER_STATE));
					ps.setInt(5, (Integer) mpParams
							.get(ReportsConstant.AUTO_REFRESH_IND));
					ps.setInt(6, (Integer) mpParams
							.get(ReportsConstant.AUTO_EXECUTE_IND));
					ps.setInt(7, (Integer) mpParams
							.get(ReportsConstant.AUTO_EXECUTE_INT));
					ps.setInt(8,
							(Integer) mpParams.get(ReportsConstant.ACTIVE_CD));
					ps.setString(9,
							(String) mpParams.get(ReportsConstant.CRATED_BY));
					ps.setString(10,
							(String) mpParams.get(ReportsConstant.UPDATED_BY));
					ps.setInt(11, (Integer) mpParams
							.get(ReportsConstant.FILTER_ENABLE_IND));
					return ps;
				}
			}, holder);
			jdbcTemplate.getDataSource().getConnection().setAutoCommit(true);
			id = holder.getKey().longValue();
			pluStatus = true;
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitPLUReportRequest method of OrderDAOImpl - "
					+ e.getMessage());
			pluStatus = false;
			errorDetails = CommonUtil.createFailureMessageForDBException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitPLUReportRequest method of ReportsDAOImpl - "
					+ e.getMessage());
			pluStatus = false;
			errorDetails = CommonUtil.createFailureMessageForSystemException(e);
		}
		objDailyPLUResBean.setPluStatus(pluStatus);
		if (null != errorDetails) {
			objDailyPLUResBean.setErrorDetails(errorDetails);
			objDailyPLUResBean.setResponse(ReportsConstant.FAILURE_MESSAGE);
		} else {
			objDailyPLUResBean.setResponse(ReportsConstant.SUCCESS_MESSAGE);
			objDailyPLUResBean.setReportId(id);
		}
		return objDailyPLUResBean;
	}

	@Override
	public List<String> getPLUListAll(int currentPage, int pluRecPerPage) {

		LOGGER.info("Entering into OrderDAOImpl.getPLUListAll()");
		List<String> lstPlUDetails = new ArrayList<String>();
		String strPLUQueryAll = ReportsQuery.getPluListQueryAll().toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.getPLUListAll()");
			LOGGER.debug(" SQL query to fetch PLU numbers includes pagination "
					+ strPLUQueryAll.toString());
		}
		Object[] objParam = { currentPage, pluRecPerPage, currentPage,
				pluRecPerPage };
		try {
			lstPlUDetails = dataGuardJdbcTemplate.query(strPLUQueryAll, objParam,
					new PLUListRowMapper());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPLUList method of ReportsDAOImpl - "
					+ e.getMessage());
		}
		return lstPlUDetails;
	}

	@Override
	public List<String> getPLUList(String pluKey, int currentPage,
			int pluRecPerPage) {

		LOGGER.info("Entering into OrderDAOImpl.getPLUList()");
		List<String> lstPlUDetails = new ArrayList<String>();
		String strPLUQuery = ReportsQuery.getPluListQuery(pluKey).toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.getPLUList()");
			LOGGER.debug(" SQL query for PLU number " + strPLUQuery.toString());
		}
		Object[] objParam = { currentPage, pluRecPerPage, currentPage,
				pluRecPerPage };
		try {
			lstPlUDetails = dataGuardJdbcTemplate.query(strPLUQuery, objParam,
					new PLUListRowMapper());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPLUList method of ReportsDAOImpl - "
					+ e.getMessage());
		}
		return lstPlUDetails;
	}

	public List<PayonFulfillmentData> getStoreDetails(String storeNumber) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getStoreDetails method of OrderDAOImpl ");
		}
		List<PayonFulfillmentData> dataList = null;
		try {
			String sqlQuery = PayOnFulfillmentQuery
					.selectPayOnfullfillmentQry().toString();
			Object[] param = new Object[] { storeNumber };
			dataList = (List<PayonFulfillmentData>) dataGuardJdbcTemplate.query(
					sqlQuery, param, new PayOnfullFillmentRowmapper());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getStoreDetails method of OrderDAOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getStoreDetails method of OrderDAOImpl ");
			}
		}
		return dataList;

	}

	/**
	 * This method handles database transaction to get Vendor Name.
	 * 
	 * @return machineList
	 */
	public List<VendorType> getPOFVendorNameList() throws PhotoOmniException {

		List<VendorType> lstVendorDetails = new ArrayList<VendorType>();
		try {
			String strVendorLstQuery = PayOnFulfillmentQuery
					.getVendorListQueryForPOF().toString();
			lstVendorDetails = jdbcTemplate.query(strVendorLstQuery,
					new VendorListRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.error(
					" Exception occoured at getPOFVendorNameList method of ReportsDAOImpl - ",
					ex);
			throw new PhotoOmniException(ex.getMessage());
		} catch (IncorrectResultSizeDataAccessException ex) {
			LOGGER.error(
					" Exception occoured at getPOFVendorNameList method of ReportsDAOImpl - ",
					ex);
			throw new PhotoOmniException(ex.getMessage());
		} catch (DataAccessException e) {
			LOGGER.error(
					" Exception occoured at getPOFVendorNameList method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFVendorNameList method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e);
		}
		return lstVendorDetails;
	}

	/**
	 * This method handles database transaction to get Vendor Name.
	 * 
	 * @return lstVendorDetails
	 */
	public List<VendorType> getPOFApproveVendorNameList()
			throws PhotoOmniException {

		List<VendorType> lstVendorDetails = new ArrayList<VendorType>();
		try {
			String strVendorLstQuery = PayOnFulfillmentQuery
					.getVendorListQueryForApproveVendor().toString();
			lstVendorDetails = jdbcTemplate.query(strVendorLstQuery,
					new VendorListRowMapper());
		} catch (EmptyResultDataAccessException ex) {
			LOGGER.error(
					" Exception occoured at getPOFApproveVendorNameList method of ReportsDAOImpl - ",
					ex);
			throw new PhotoOmniException(ex.getMessage());
		} catch (IncorrectResultSizeDataAccessException ex) {
			LOGGER.error(
					" Exception occoured at getPOFApproveVendorNameList method of ReportsDAOImpl - ",
					ex);
			throw new PhotoOmniException(ex.getMessage());
		} catch (DataAccessException e) {
			LOGGER.error(
					" Exception occoured at getPOFApproveVendorNameList method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPOFApproveVendorNameList method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e);
		}
		return lstVendorDetails;
	}

	public VendorCostValidationReportResponse fetchVendorPaymentAwaitingApproval(
			VendorCostValidationReportRequest reqBean)
			throws PhotoOmniException {

		LOGGER.debug("Entering into OrderBOImpl:fetchVendorPaymentAwaitingApproval()");

		List<POFVendorCostReportDetail> vendorCostLst = new ArrayList<POFVendorCostReportDetail>();
		POFVendorCostValidationFilter filter = reqBean.getFilter();
		VendorCostValidationReportResponse responseBean = new VendorCostValidationReportResponse();
		String currentPage = filter.getCurrrentPage();
		int pageSize = 5;
		Map<String, Long> pageMap = CommonUtil.getPaginationLimit(currentPage,
				pageSize);
		long upperLimit = pageMap.get("END_LIMIT");
		long lowerLimit = pageMap.get("START_LIMIT");
		String strVendorCostQuery = null;
		boolean isNonEmptyStore = (null != filter.getStoreNumber() && !EMPTY_SPACE_CHAR
				.equals(filter.getStoreNumber().trim()));
		boolean isEmptyStore = (null == filter.getStoreNumber() || EMPTY_SPACE_CHAR
				.equals(filter.getStoreNumber().trim()));
		boolean isNonEmptyVendor = (null != filter.getVendorName() && !EMPTY_SPACE_CHAR
				.equals(filter.getVendorName().trim()));
		boolean isEmptyVendor = (null == filter.getVendorName() || EMPTY_SPACE_CHAR
				.equals(filter.getVendorName().trim()));
		try {

			if (isNonEmptyStore && isNonEmptyVendor) {
				LOGGER.debug("fetchVendorPaymentAwaitingApproval():Store number    "
						+ filter.getStoreNumber());
				LOGGER.debug("fetchVendorPaymentAwaitingApproval():Vendor name    "
						+ filter.getVendorName());

				Object[] param = {
						Integer.parseInt(filter.getStoreNumber().trim()),
						filter.getVendorName(), lowerLimit, upperLimit };
				strVendorCostQuery = PayOnFulfillmentQuery
						.getVendorPaymentCostApprovalQry(POF_STORE_TYPE_SV)
						.toString();
				LOGGER.debug("fetchVendorPaymentAwaitingApproval() for Non Empty Store and Non Empty Vendor.... "
						+ strVendorCostQuery);
				vendorCostLst = jdbcTemplate.query(strVendorCostQuery, param,
						new VendorCostReportRowMapper());

			} else if (isEmptyStore && isNonEmptyVendor) {
				LOGGER.debug("fetchVendorPaymentAwaitingApproval():Vendor name    "
						+ filter.getVendorName());
				Object[] param = { filter.getVendorName(), lowerLimit,
						upperLimit };
				strVendorCostQuery = PayOnFulfillmentQuery
						.getVendorPaymentCostApprovalQry(POF_STATUS_IND_V)
						.toString();
				LOGGER.debug("fetchVendorPaymentAwaitingApproval() for Empty Store and Non Empty Vendor.... "
						+ strVendorCostQuery);
				vendorCostLst = jdbcTemplate.query(strVendorCostQuery, param,
						new VendorCostReportRowMapper());

			} else if (isNonEmptyStore && isEmptyVendor) {
				LOGGER.debug("fetchVendorPaymentAwaitingApproval():Store number    "
						+ filter.getStoreNumber());
				Object[] param = { filter.getStoreNumber(), lowerLimit,
						upperLimit };
				strVendorCostQuery = PayOnFulfillmentQuery
						.getVendorPaymentCostApprovalQry(POF_STORE_TYPE_S)
						.toString();
				LOGGER.debug("fetchVendorPaymentAwaitingApproval() for Non Empty Store and Empty Vendor.... "
						+ strVendorCostQuery);
				vendorCostLst = jdbcTemplate.query(strVendorCostQuery, param,
						new VendorCostReportRowMapper());

			} else if (isEmptyStore && isEmptyVendor) {
				Object[] param = { lowerLimit, upperLimit };
				strVendorCostQuery = PayOnFulfillmentQuery
						.getVendorPaymentCostApprovalQry(POF_STORE_TYPE_BLANK)
						.toString();
				LOGGER.debug("fetchVendorPaymentAwaitingApproval() for Empty Store and Empty Vendor.... "
						+ strVendorCostQuery);
				vendorCostLst = jdbcTemplate.query(strVendorCostQuery, param,
						new VendorCostReportRowMapper());
			}

			responseBean.setData(vendorCostLst);
			responseBean.setCurrrentPage(currentPage);
			if (vendorCostLst.size() > 0)
				responseBean.setTotalRecord(vendorCostLst.get(0)
						.getTotalRecord());
			else {
				responseBean.setTotalRecord(0);
			}

		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchVendorPaymentAwaitingApproval method of ReportsDAOImpl - "
					+ e.getMessage());
			e.printStackTrace();
			// throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchVendorPaymentAwaitingApproval method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e);
		}
		LOGGER.debug("Exiting from OrderRestBOImpl:fetchVendorPaymentAwaitingApproval()");
		return responseBean;
	}

	@Override
	public int getReportDataCount(
			POFVendorCostValidationFilter pofVendorCostFilter) {
		String getReportCountSql = PayOnFulfillmentQuery
				.getVendorCostvalidationCount().toString();
		@SuppressWarnings("deprecation")
		int count = jdbcTemplate.queryForInt(getReportCountSql,
				new VendorCostReportRowMapper());
		return count;
	}

	public boolean updateVendorpaymentApproval(
			POFVendorCostReportDetail pofVendorCostReportDetail) {
		LOGGER.debug("Entering into OrderDAOImpl:updateVendorpaymentApproval()");
		boolean status = false;
		try {
			String statusCd = POF_STATUS_CD;
			LOGGER.debug("statusCd    " + statusCd);
			String updateVendorPaymentAppvl = PayOnFulfillmentQuery
					.updateVendorpaymentApproval().toString();
			LOGGER.debug("updateVendorPaymentAppvl    "
					+ updateVendorPaymentAppvl);
			Timestamp timeStamp = new Timestamp(new Date().getTime());
			LOGGER.debug("timeStamp    " + timeStamp);
			Object[] objPluParms = new Object[] {
					pofVendorCostReportDetail.getApprovedCost(),
					pofVendorCostReportDetail.getCentralCalculatedCost(),
					UPDATE_USER_ID, statusCd,
					pofVendorCostReportDetail.getEnvNumber(),
					pofVendorCostReportDetail.getVcId() };
			int updated = jdbcTemplate.update(updateVendorPaymentAppvl,
					objPluParms);
			status = true;

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at fetchVendorPaymentAwaitingApproval method of ReportsDAOImpl - ",
					e);
			status = false;
		}
		LOGGER.debug("Exiting from OrderDAOImpl:updateVendorpaymentApproval()");
		return status;

	}

	/**
	 * 
	 */

	public List<ProductDetails> getProductDetails() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getProductDetails method of OrderDAOImpl ");
		}
		List<ProductDetails> productList = null;
		try {
			String sqlQuery = PayOnFulfillmentQuery.selectProductDetailsQry()
					.toString();
			productList = (List<ProductDetails>) dataGuardJdbcTemplate.query(
					sqlQuery, new ProductdetailsRowmapper());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getProductDetails method of OrderDAOImpl - "
					+ e.getMessage());

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getProductDetails method of OrderDAOImpl ");
			}
		}
		return productList;

	}

	/**
	 * This method fetch records from Pay On fulfillment Report with EDI UPC and
	 * Non EDI values for store and Vendor
	 */
	public PayOnFulfillmentResponse payOnFulfillmentReportRequest(
			PayOnFulfillmentReq reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering payOnFulfillmentReportRequest method of OrderDAOImpl ");
		}
		List<PayOnFulfillmentRespData> responseList = new ArrayList<PayOnFulfillmentRespData>();
		List<PayOnFulfillmentRespData> ediResponseList = new ArrayList<PayOnFulfillmentRespData>();
		List<ProductDetails> productList = new ArrayList<ProductDetails>();;
		PayOnFulfillmentResponse responseBean = new PayOnFulfillmentResponse();

		String sqlQuery = "";
		String currentPage = reqBean.getCurrrentPage();
		int pageSize = 5;
		Map<String, Long> pageMap = CommonUtil.getPaginationLimit(currentPage,
				pageSize);
		String startDate = CommonUtil.stringDateFormatChange(
				reqBean.getStartDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		String endDate = CommonUtil.stringDateFormatChange(
				reqBean.getEndDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		long upperLimit = pageMap.get("END_LIMIT");
		long lowerLimit = pageMap.get("START_LIMIT");
		boolean isNonEmptyStore = (null != reqBean.getStoreNumber() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isEmptyStore = (null == reqBean.getStoreNumber() || EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isNonEmptyVendor = (null != reqBean.getVendorId() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		boolean isEmptyVendor = (null == reqBean.getVendorId() || EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		boolean isStoreEDIRpt = isNonEmptyStore && reqBean.getFiltertypePay().equalsIgnoreCase("ediupc");
		
		try {
			if (reqBean.getFiltertypePay().equalsIgnoreCase("ediupc")) { // EDI
				if(isStoreEDIRpt){
					sqlQuery = PayOnFulfillmentQuery.selectEDIDetailsQry(true)
							.toString();
					Object[]  param = { reqBean.getEdiupcValue(), reqBean.getStoreNumber()};
					ediResponseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(false));
					if(null != ediResponseList && ediResponseList.size() >0){
						sqlQuery = PayOnFulfillmentQuery.selectProductDetailsQry()
								.toString();
						LOGGER.debug("payOnFulfillmentReportRequest() EDI Product Report sqlQuery    "
								+ sqlQuery);
						Object[] param1 = { reqBean.getEdiupcValue(), lowerLimit,
								upperLimit };
						productList = (List<ProductDetails>) getJdbcTemplate().query(
								sqlQuery, param1, new ProductdetailsRowmapper());
					}
				}else{
					sqlQuery = PayOnFulfillmentQuery.selectEDIDetailsQry(false)
						.toString();
					Object[]  param = { reqBean.getEdiupcValue() };
					ediResponseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(false));
					sqlQuery = PayOnFulfillmentQuery.selectProductDetailsQry()
							.toString();
					LOGGER.debug("payOnFulfillmentReportRequest() EDI Product Report sqlQuery    "
							+ sqlQuery);
					Object[] param1 = { reqBean.getEdiupcValue(), lowerLimit,
							upperLimit };
					productList = (List<ProductDetails>) getJdbcTemplate().query(
							sqlQuery, param1, new ProductdetailsRowmapper());
				}
				
				/*LOGGER.debug("payOnFulfillmentReportRequest() EDI Header Report sqlQuery    "
						+ sqlQuery);
				ediResponseList = jdbcTemplate.query(sqlQuery, param,
						new PayOnFulfillmentEDIRowmapper(false));
				sqlQuery = PayOnFulfillmentQuery.selectProductDetailsQry()
						.toString();
				LOGGER.debug("payOnFulfillmentReportRequest() EDI Product Report sqlQuery    "
						+ sqlQuery);
				Object[] param1 = { reqBean.getEdiupcValue(), lowerLimit,
						upperLimit };
				productList = (List<ProductDetails>) dataGuardJdbcTemplate.query(
						sqlQuery, param1, new ProductdetailsRowmapper());*/
				if (null!= ediResponseList && ediResponseList.size() > 0) {
					PayOnFulfillmentRespData respData = calculateVendPaymentNRetailPrice(ediResponseList);
					responseBean.setRespData(respData);
					responseBean.setTotalRecord(String.valueOf(ediResponseList
							.get(0).getTotalRecord()));
				} else {
					responseBean.setRespData(null);
				}
				responseBean.setProductList(productList);
				if (null!= productList && productList.size() > 0) {
					responseBean.setTotalRecord(String.valueOf(productList.get(
							0).getTotalRecord()));
				} else {
					responseBean.setTotalRecord("0");
				}

			} else if (null != reqBean.getStartDate()
					&& null != reqBean.getEndDate()
					&& reqBean.getFiltertypePay().equalsIgnoreCase("default")) // NON
																				// EDI
																				// Report
			{

				if (isNonEmptyStore && isNonEmptyVendor) {
					String vendorNbr = reqBean.getVendorId();
					String storerNumber = reqBean.getStoreNumber();
					LOGGER.debug("payOnFulfillmentReportRequest() for Non Empty Store and Non Empty Vendor : vendorId    "
							+ reqBean.getVendorId());
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							vendorNbr, true, false).toString();
					LOGGER.debug("payOnFulfillmentReportRequest() for Non Empty Store and Non Empty Vendor sqlQuery    "
							+ sqlQuery);
					Object[] param = { startDate, endDate, storerNumber,
							lowerLimit, upperLimit };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));

				} else if (isNonEmptyStore && isEmptyVendor) {
					String storerNumber = reqBean.getStoreNumber();
					sqlQuery = PayOnFulfillmentQuery
							.selectNonEDINonVendStoreQry(false).toString();
					LOGGER.debug("payOnFulfillmentReportRequest() for Non Empty Store and  Empty Vendor sqlQuery    "
							+ sqlQuery);
					Object[] param = { storerNumber, startDate, endDate,
							lowerLimit, upperLimit };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));
				} else if (isNonEmptyVendor && isEmptyStore) {
					String vendorNbr = reqBean.getVendorId();
					sqlQuery = PayOnFulfillmentQuery
							.selectEDIVendorDetailsForVendorQry(startDate,
									endDate, vendorNbr, false).toString();
					LOGGER.debug("payOnFulfillmentReportRequest() for Empty Store and Non Empty Vendor sqlQuery    "
							+ sqlQuery);
					Object[] param = { lowerLimit, upperLimit };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));
				} else if (isEmptyVendor && isEmptyStore) {
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							"", false, false).toString();
					LOGGER.debug("payOnFulfillmentReportRequest() for Empty Store and Empty Vendor sqlQuery    "
							+ sqlQuery);
					Object[] param = { startDate, endDate, lowerLimit,
							upperLimit };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));
				}
				responseBean.setRepByPayOnFulfillmentList(responseList);
				if (responseList.size() > 0) {
					responseBean.setTotalRecord(String.valueOf(responseList
							.get(0).getTotalRecord()));
				} else {
					responseBean.setTotalRecord("0");
				}

			}

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at payOnFulfillmentReportRequest method of OrderDAOImpl - ",
					e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentReportRequest method of OrderDAOImpl ");
			}
		}
		return responseBean;
	}

	private PayOnFulfillmentRespData calculateVendPaymentNRetailPrice(
			List<PayOnFulfillmentRespData> ediResponseList) {
		PayOnFulfillmentRespData respData = new PayOnFulfillmentRespData();
		double vendpaymntAmnt = 0.0;
		double retailPrice = 0.0;
		for (int i = 0; i < ediResponseList.size(); i++) {
			PayOnFulfillmentRespData temp = ediResponseList.get(i);
			respData.setStoreNumber(temp.getStoreNumber());
			respData.setEnvelopeNumber(temp.getEnvelopeNumber());
			respData.setAsnRecievedDate(temp.getAsnRecievedDate());
			respData.setEdiTransferDate(temp.getEdiTransferDate());
			respData.seteDIupc(temp.geteDIupc());
			respData.setReportingDate(temp.getReportingDate());
			respData.setDoneDate(temp.getDoneDate());
			respData.setVendorNbr(temp.getVendorNbr());
			vendpaymntAmnt += temp.getCost();
			respData.setCost(Double.parseDouble(String.format("%.2f",
					vendpaymntAmnt)));
			retailPrice += temp.getRetailPrice();
			respData.setRetailPrice(Double.parseDouble(String.format("%.2f",
					retailPrice)));
		}
		return respData;

	}

	/**
	 * Description : This method is used for get data to download csv
	 */
	public List<PayOnFulfillmentCSVRespData> payOnFulfillmentCSVReportRequest(
			PayOnFulfillmentReq reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering payOnFulfillmentReportRequest method of OrderDAOImpl ");
		}
		List<PayOnFulfillmentCSVRespData> responseList = new ArrayList<PayOnFulfillmentCSVRespData>();

		String sqlQuery = "";

		String startDate = CommonUtil.stringDateFormatChange(
				reqBean.getStartDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		String endDate = CommonUtil.stringDateFormatChange(
				reqBean.getEndDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		boolean isNonEmptyStore = (null != reqBean.getStoreNumber() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isEmptyStore = (null == reqBean.getStoreNumber() || EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isNonEmptyVendor = (null != reqBean.getVendorId() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		boolean isEmptyVendor = (null == reqBean.getVendorId() || EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		try {
			if (null != reqBean.getStartDate() && null != reqBean.getEndDate()
					&& reqBean.getFiltertypePay().equalsIgnoreCase("default")) // NON
																				// EDI
																				// Report
			{

				if (isNonEmptyStore && isNonEmptyVendor) {
					String vendorNbr = reqBean.getVendorId();
					String storerNumber = reqBean.getStoreNumber();
					LOGGER.debug("vendorId    " + reqBean.getVendorId());
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							vendorNbr, true, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { startDate, endDate, storerNumber };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentCSVReportRowMapper());

				} else if (isNonEmptyStore && isEmptyVendor) {
					String storerNumber = reqBean.getStoreNumber();
					sqlQuery = PayOnFulfillmentQuery
							.selectNonEDINonVendStoreQry(true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { reqBean.getStoreNumber(), startDate,
							endDate };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentCSVReportRowMapper());
				} else if (isNonEmptyVendor && isEmptyStore) {
					String vendorNbr = reqBean.getVendorId();
					sqlQuery = PayOnFulfillmentQuery
							.selectEDIVendorDetailsForVendorQry(startDate,
									endDate, vendorNbr, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					responseList = jdbcTemplate.query(sqlQuery,
							new PayOnFulfillmentCSVReportRowMapper());
				} else if (isEmptyVendor && isEmptyStore) {
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							"", false, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { startDate, endDate };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentCSVReportRowMapper());
				}

				/*
				 * if(null!= reqBean.getStoreNumber() &&
				 * null!=reqBean.getVendorId() && !
				 * EMPTY_SPACE_CHAR.equals(reqBean.getVendorId())){ // WITH
				 * VENDOR iD int vendorId =
				 * Integer.parseInt(reqBean.getVendorId()); sqlQuery =
				 * PayOnFulfillmentQuery
				 * .selectEDIVendorDetailsForVendorQry(startDate,
				 * endDate,reqBean.getVendorId(),true).toString(); Object[]
				 * param = {reqBean.getStoreNumber()}; responseList =
				 * jdbcTemplate.query(sqlQuery, param, new
				 * PayOnFulfillmentCSVReportRowMapper()); }else{ sqlQuery =
				 * PayOnFulfillmentQuery
				 * .selectEDIVendorDetailsQry(true).toString(); // WITHOUT
				 * VENDOR iD Object[] param =
				 * {reqBean.getStoreNumber(),startDate, endDate}; responseList =
				 * jdbcTemplate.query(sqlQuery, param, new
				 * PayOnFulfillmentCSVReportRowMapper()); }
				 */

			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at payOnFulfillmentReportRequest method of OrderDAOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentReportRequest method of OrderDAOImpl ");
			}
		}
		return responseList;
	}

	/**
	 * Description : This method gets data for PayOnFulfillment Report Store
	 * Print
	 */
	public PayOnFulfillmentResponse payOnFulfillmentStorePrintReport(
			PayOnFulfillmentReq reqBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering payOnFulfillmentStrorePrintReport method of OrderDAOImpl ");
		}
		List<PayOnFulfillmentRespData> responseList = new ArrayList<PayOnFulfillmentRespData>();
		PayOnFulfillmentResponse responseBean = new PayOnFulfillmentResponse();

		String sqlQuery = "";
		String startDate = CommonUtil.stringDateFormatChange(
				reqBean.getStartDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		LOGGER.debug("startDate    " + startDate);
		String endDate = CommonUtil.stringDateFormatChange(
				reqBean.getEndDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		LOGGER.debug("endDate    " + endDate);

		boolean isNonEmptyStore = (null != reqBean.getStoreNumber() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isEmptyStore = (null == reqBean.getStoreNumber() || EMPTY_SPACE_CHAR
				.equals(reqBean.getStoreNumber()));
		boolean isNonEmptyVendor = (null != reqBean.getVendorId() && !EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		boolean isEmptyVendor = (null == reqBean.getVendorId() || EMPTY_SPACE_CHAR
				.equals(reqBean.getVendorId()));
		try {
			if (null != reqBean.getStartDate() && null != reqBean.getEndDate()
					&& reqBean.getFiltertypePay().equalsIgnoreCase("default")) // NON
																				// EDI
																				// Report
			{
				if (isNonEmptyStore && isNonEmptyVendor) {
					String vendorNbr = reqBean.getVendorId();
					String storerNumber = reqBean.getStoreNumber();
					LOGGER.debug("vendorId    " + reqBean.getVendorId());
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							vendorNbr, true, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { startDate, endDate, storerNumber };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));

				} else if (isNonEmptyStore && isEmptyVendor) {
					String storerNumber = reqBean.getStoreNumber();
					sqlQuery = PayOnFulfillmentQuery
							.selectNonEDINonVendStoreQry(true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { storerNumber, startDate, endDate };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));
				} else if (isNonEmptyVendor && isEmptyStore) {
					String vendorNbr = reqBean.getVendorId();
					sqlQuery = PayOnFulfillmentQuery
							.selectEDIVendorDetailsForVendorQry(startDate,
									endDate, vendorNbr, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					responseList = jdbcTemplate.query(sqlQuery,
							new PayOnFulfillmentEDIRowmapper(true));
				} else if (isEmptyVendor && isEmptyStore) {
					sqlQuery = PayOnFulfillmentQuery.selectEDIVendorDetailsQry(
							"", false, true).toString();
					LOGGER.debug("sqlQuery    " + sqlQuery);
					Object[] param = { startDate, endDate };
					responseList = jdbcTemplate.query(sqlQuery, param,
							new PayOnFulfillmentEDIRowmapper(true));
				}
				responseBean.setRepByPayOnFulfillmentList(responseList);
				if (responseList.size() > 0) {
					responseBean.setTotalRecord(String.valueOf(responseList
							.get(0).getTotalRecord()));
				} else {
					responseBean.setTotalRecord("0");
				}

			}

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at payOnFulfillmentStrorePrintReport method of OrderDAOImpl - ",
					e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting payOnFulfillmentStrorePrintReport method of OrderDAOImpl ");
			}
		}
		return responseBean;
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
	public PMByEmployeeResBean getPmByEmployeeReportData(
			PMByEmployeeReqBean objPMByEmployeeReqBean, boolean isFromPrintPage)
					throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.getPmByEmployeeReportData().");
		}
		PMByEmployeeResBean pmByEmployeeResBean = null;
		String startDate = null, endDate = null;
		int storeNumber = 0, curpage = 0, pagesize = 0;
		List<PMByEmployeeDetailBean> listPMByEmployeeDetailBean = new ArrayList<PMByEmployeeDetailBean>();

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		startDate = sdf.format(sf.parse(objPMByEmployeeReqBean.getFilter()
				.getStartDate()));
		startDate = startDate + " 00:00:00";
		endDate = sdf.format(sf.parse(objPMByEmployeeReqBean.getFilter()
				.getEndDate()));
		endDate = endDate + " 23:59:59";
		String empId = (null == objPMByEmployeeReqBean.getFilter()
				.getEmployeeId()) ? "" : objPMByEmployeeReqBean.getFilter()
						.getEmployeeId();
		storeNumber = objPMByEmployeeReqBean.getFilter().getStoreNumber();
		pmByEmployeeResBean = new PMByEmployeeResBean();
		boolean isAutharised = false;

		try {

			ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();

			if (authentication != null) {
				String StrEmplyee_ID = ((SAMLUserDetails) authentication
						.getDetails()).getEmployeenumber();
				Collection<GrantedAuthority> authorities = authentication
						.getAuthorities();

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Inside OrderDAOImpl.getPmByEmployeeReportData() :: StrEmplyee_ID ::"
							+ StrEmplyee_ID);
					LOGGER.debug("Inside OrderDAOImpl.getPmByEmployeeReportData()   ::Role "
							+ authorities);
					LOGGER.debug("ROle HeadPhotoSpecialist ::  "
							+ isRolePresent(
									authorities,
									PhotoOmniConstants.ROLE_HEADPHOTOSPECIALIST)
									+ ">>>> ROle PhotoOmni_Support::"
									+ isRolePresent(
											authorities,
											PhotoOmniConstants.ROLE_PHOTOOMNI_SUPPORT)
											+ ">>>>>>--> ROle StoreManager::"
											+ isRolePresent(authorities,
													PhotoOmniConstants.ROLE_STOREMANAGER));
				}

				if (isRolePresent(authorities,
						PhotoOmniConstants.ROLE_HEADPHOTOSPECIALIST)
						|| isRolePresent(authorities,
								PhotoOmniConstants.ROLE_PHOTOOMNI_SUPPORT)
								|| isRolePresent(authorities,
										PhotoOmniConstants.ROLE_STOREMANAGER)) {
					isAutharised = true;
				} else if (empId.equals("")) {
					empId = StrEmplyee_ID;
					isAutharised = true;
				} else if (!empId.equals(StrEmplyee_ID)) {
					isAutharised = false;
				}
			}

			if (!isFromPrintPage) {
				if (isAutharised) {
					curpage = objPMByEmployeeReqBean.getCurrentPage();
					String strEmpDetalsQuery = ReportsQuery
							.pmByEmployeeReportQuery(objPMByEmployeeReqBean, empId)
							.toString();

					String strQueryForPageSize = ReportsQuery.getPageCount()
							.toString();

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("checking storeNumber:" + storeNumber
								+ ">> empId : " + empId + ">>>> curpage :"
								+ curpage + ">>>> startDate : " + startDate
								+ ">>>>>> endDate : " + endDate);
						LOGGER.debug("select Query to strEmpDetalsQuery>>>>->."
								+ strEmpDetalsQuery);
						LOGGER.debug("strQueryForPageSize Query >>>>->."
								+ strQueryForPageSize);
					}
					Object[] objpagesizeparam = { ReportsConstant.PM_BY_EMP_REPORT_NAME };
					pagesize = dataGuardJdbcTemplate.queryForObject(strQueryForPageSize,
							objpagesizeparam, Integer.class);

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("pagesize per page to display >>>>->."
								+ pagesize);
					}

					Object[] param = { storeNumber, startDate, endDate,
							startDate, curpage, pagesize, curpage, pagesize };

					listPMByEmployeeDetailBean = dataGuardJdbcTemplate.query(
							strEmpDetalsQuery, param,
							new PMByEmployeeDetailBeanRowMapper());

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("list Size afterselecting Employee report Query >>>>->."
								+ listPMByEmployeeDetailBean.size());
					}


				}
				if (listPMByEmployeeDetailBean != null && !listPMByEmployeeDetailBean.isEmpty())
				{pmByEmployeeResBean.setTotalPage(listPMByEmployeeDetailBean.get(0).getTotalRows());}
				pmByEmployeeResBean.setAuthorised(isAutharised);
				pmByEmployeeResBean.setTotalRows(pagesize);
				pmByEmployeeResBean.setCurrentPage(curpage);
			} else {
				String strEmpDetalsQuery = ReportsQuery
						.pmByEmployeeReportQueryForPrint(objPMByEmployeeReqBean, empId)
						.toString();
				Object[] param = { storeNumber, startDate, endDate, startDate };

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("checking storeNumber:" + storeNumber
							+ ">> empId : " + empId + ">>>> startDate : "
							+ startDate + ">>>>>> endDate : " + endDate);
					LOGGER.debug("select Query to strEmpDetalsQuery>>>>->."
							+ strEmpDetalsQuery);
				}
				listPMByEmployeeDetailBean = dataGuardJdbcTemplate.query(
						strEmpDetalsQuery, param,
						new PMByEmployeeDetailBeanRowMapper());
			}

			pmByEmployeeResBean
			.setPmByEmployeeDetailBeans(listPMByEmployeeDetailBean);
			pmByEmployeeResBean.setMessage(objPMByEmployeeReqBean
					.getMessageHeader());
		} catch (Exception e) {
			LOGGER.error("Error occured in OrderDAOImpl.getPmByEmployeeReportData()."
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from OrderDAOImpl.getPmByEmployeeReportData().");
			}
		}

		return pmByEmployeeResBean;
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
	public String getEmployeeNameStoreAdd(String strEmpORLocID,
			boolean isEmployeeName) {

		String strEmpNameOrStoreAdd = "";

		strEmpNameOrStoreAdd = jdbcTemplate.queryForObject(
				(isEmployeeName ? ReportsQuery.getEmployeeNameQuery(
						strEmpORLocID).toString() : ReportsQuery
						.getLocationAddresQuery(strEmpORLocID).toString()),
				String.class);

		return strEmpNameOrStoreAdd;
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param jdbcTemplate
	 *            the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Fetch system reference of Order table by filtering with Order no &
	 * StoreNo
	 * 
	 * @param orderNo
	 *            Order reference from Store
	 * @param StoreNo
	 *            Store no of Location master
	 * @return system reference of Order table or NULL
	 * @throws PhotoOmniException
	 */
	public int getDuplicateExceptionId(int orderId, int orderLineID)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Entering getDuplicateExceptionId method of RealTimeOrderDAOImpl ");
		}
		Integer id = 0;
		StringBuilder sqlQuery = new StringBuilder();
		try {
			sqlQuery.append("SELECT COUNT(SYS_ORD_EXCEPTION_ID) AS id FROM ")
					.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
			sqlQuery.append(" WHERE SYS_ORDER_ID = ? AND SYS_ORDER_LINE_ID=? ");

			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(" getDuplicateExceptionId method of RealTimeOrderDAOImpl SQL-"
						+ sqlQuery.toString());
				LOGGER.info(" getDuplicateExceptionId method of RealTimeOrderDAOImpl orderId- :"
						+ orderId + "orderLineID : " + orderLineID);
			}
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { orderId, orderLineID });
			for (Map<String, Object> row : rows) {
				id = ServiceUtil.bigDecimalToInt((row.get("id")));
			}
		} catch (NullPointerException ne) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Inside getDuplicateExceptionId method of RealTimeOrderDAOImpl : got NullPointerException Start  "
						+ ne.getStackTrace());
				LOGGER.info(" SQL-" + sqlQuery.toString());
				LOGGER.info(" orderId- :" + orderId + "orderLineID : "
						+ orderLineID);
			}
			throw new PhotoOmniException(ne.getMessage());

		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Exiting getDuplicateExceptionId method of RealTimeOrderDAOImpl ");
		}
		return id;
	}

	/**
	 * This method is used to save user entered data to the database
	 * 
	 * @param filterState
	 *            .
	 * @return status.
	 */
	public List<LateEnvelopeBean> fetchLateEnvelopeDataRequest(
			LateEnvelopeReportReqBean requestBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchLateEnvelopeDataRequest method of LateEnvelopeReportDAOImpl ");
		}
		LateEnvelopeFilter reqBean = requestBean.getFilter();
		int page = this.getPageSize(reqBean.getReportName());
		reqBean.setPageSizeResponse(page);
		List<LateEnvelopeBean> envelopeBeanList = null;
		try {
			Map<String, Long> pageLimit = CommonUtil.getPaginationLimit(
					reqBean.getCurrentPageNo(), page);
			String startDate = CommonUtil.stringDateFormatChange(
					reqBean.getStartDate(),
					PhotoOmniConstants.DATE_FORMAT_SEVEN,
					PhotoOmniConstants.DATE_FORMAT_TWO);
			LOGGER.debug("startDate    " + startDate);
			String endDate = CommonUtil.stringDateFormatChange(
					reqBean.getEndDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
					PhotoOmniConstants.DATE_FORMAT_TWO);
			LOGGER.debug("endDate    " + endDate);
			String startLimit = !CommonUtil
					.isNull(pageLimit.get("START_LIMIT")) ? pageLimit.get(
					"START_LIMIT").toString() : "";
			String endLimit = !CommonUtil.isNull(pageLimit.get("END_LIMIT")) ? pageLimit
					.get("END_LIMIT").toString() : "";

			boolean printReport = reqBean.isFlag();
			if (printReport == false) {
				Object[] params = new Object[] { reqBean.getStoreNumber(),
						startDate, endDate,reqBean.getStoreNumber(),startDate, endDate,reqBean.getStoreNumber(),startDate, endDate,startLimit, endLimit };
				String selectQry = RealTimeOrderQuery.selectLateEnvQry(requestBean)
					.toString();
				envelopeBeanList = dataGuardJdbcTemplate.query(selectQry, params,
						new LateEnvelopeRowmapper());

			} else {
				Object[] params = new Object[] { reqBean.getStoreNumber(),
						startDate, endDate,reqBean.getStoreNumber(),startDate, endDate,reqBean.getStoreNumber(),startDate, endDate };
				String selectQry = RealTimeOrderQuery.selectLateEnvPrintQry(
						requestBean).toString();
				envelopeBeanList = dataGuardJdbcTemplate.query(selectQry, params,
						new LateEnvelopeRowmapper());

			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchLateEnvelopeDataRequest method of LateEnvelopeReportDAOImpl - "
					,e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchLateEnvelopeDataRequest method of LateEnvelopeReportDAOImpl ");
			}
		}
		return envelopeBeanList;

	}

	/**
	 * This method is used to save user entered data to the database
	 * 
	 * @param reqBean
	 *            .
	 * @return envelopeBeanList.
	 */
	public List<OrderInfo> fetchLateEnvProdInfo(
			LateEnvelopeReportDetailsReqBean reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchLateEnvProdInfo method of LateEnvelopeReportDAOImpl ");
		}
		List<OrderInfo> envelopeBeanList = null;
		try {
			String Timesubmitted = reqBean.getFilter().getTimeSubmitted();
			String selectQuery = RealTimeOrderQuery.selectLateEnvOrderInfoQry()
					.toString();
			Object[] params = new Object[] { reqBean.getFilter().getOrderid(),
					Timesubmitted };
			envelopeBeanList = dataGuardJdbcTemplate.query(selectQuery, params,
					new LateEnvProdInfoRowmapper());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at fetchLateEnvProdInfo method of LateEnvelopeReportDAOImpl - "
					,e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchLateEnvProdInfo method of LateEnvelopeReportDAOImpl ");
			}

		}
		return envelopeBeanList;

	}






	@Override
	public int getPLUListCount(String pluKey) {

		LOGGER.info("Entering into OrderDAOImpl.getPLUListCount()");
		String strPLUQuery = ReportsQuery.getPluListCountQuery(pluKey)
				.toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.getPLUListCount()");
			LOGGER.debug("SQL query to get PLU count " + strPLUQuery.toString());
		}
		int count = 0;
		try {
			count = dataGuardJdbcTemplate.queryForObject(strPLUQuery, Integer.class);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPLUListCount method of ReportsDAOImpl - "
					+ e.getMessage());
		}
		return count;
	}

	@Override
	public int getPLUListAllCount() {

		LOGGER.info("Entering into OrderDAOImpl.getPLUListAllCount()");
		String strPLUQuery = ReportsQuery.getPluTotalCount().toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into OrderDAOImpl.getPLUListAllCount()");
			LOGGER.debug("SQL query to fetch total count of PLU numbes "
					+ strPLUQuery.toString());
		}
		int count = 0;
		try {
			count = dataGuardJdbcTemplate.queryForObject(strPLUQuery, Integer.class);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPLUListAllCount method of ReportsDAOImpl - "
					+ e.getMessage());
		}
		return count;
	}

	/**
	 * @param orderHistoryBean
	 * @return boolean
	 */

	private boolean insertOrderHistoryDetails(OrderHistoryBean orderHistoryBean)
			throws RuntimeException, PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl insertOrderHistoryDetails() ");
		String orderHistoryQry = RealTimeOrderQuery
				.insertQueryOrderHistoryTab().toString();
		boolean updStatus = false;
		String userId = !CommonUtil.isNull(orderHistoryBean.getCreateUserId())
				&& !"".equals(orderHistoryBean.getCreateUserId()) ? orderHistoryBean
				.getCreateUserId() : PhotoOmniConstants.CREATE_USER_ID;
		int updateCount = jdbcTemplate.update(
				orderHistoryQry,
				new Object[] { orderHistoryBean.getOrderId(),
						orderHistoryBean.getAction(),
						orderHistoryBean.getOrderPlacedDttm(),
						orderHistoryBean.getOrderStatus(),
						orderHistoryBean.getActionDttm(),
						orderHistoryBean.getActionNotes(),
						orderHistoryBean.getExceptionId(), userId.toUpperCase(),
						PhotoOmniConstants.UPDATE_USER_ID });

		if (updateCount > 0) {
			updStatus = true;
		}

		LOGGER.debug("Exiting OrdersDAOImpl insertOrderHistoryDetails");
		return updStatus;
	}

	/**
	 * @param sequenceName
	 * @return Current Table Sequence number
	 * @throws PhotoOmniException
	 */
	public int getTableSequenceId(String sequenceName)
			throws PhotoOmniException {
		LOGGER.debug("Entering OrdersDAOImpl getTableSequenceId() ");
		int sequence = 0;
		try {
			StringBuilder seqSql = new StringBuilder();
			seqSql.append("SELECT ").append(sequenceName)
					.append("_SEQ.NEXTVAL AS id FROM DUAL");
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(seqSql
					.toString());
			for (Map<String, Object> row : rows) {
				sequence = ServiceUtil.bigDecimalToInt((row.get("id")));
			}
		} catch (DataAccessException e) {

			LOGGER.error(" Error occoured at getPLUListAllCount method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException();
		}
		LOGGER.debug("Exit OrdersDAOImpl getTableSequenceId() ");
		return sequence;
	}

	/**
	 * @param sysExceptionId
	 * @return
	 * @throws PhotoOmniException
	 */
	private Map<String, Object> getExceptionType(String sysExceptionId)
			throws PhotoOmniException {
		LOGGER.debug("Entering OrdersDAOImpl getTableSequenceId() ");
		Map<String, Object> exceptionType = new HashMap<String, Object>();
		try {
			StringBuilder seqSql = new StringBuilder();
			seqSql.append("SELECT EXCEPTION_TYPE, REASON FROM ")
					.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE)
					.append(" WHERE SYS_EXCEPTION_TYPE_ID=?");
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					seqSql.toString(), new Object[] { sysExceptionId });
			for (Map<String, Object> row : rows) {
				exceptionType.put("EXCEPTION_TYPE", row.get("EXCEPTION_TYPE")
						.toString());
				exceptionType.put("REASON", row.get("REASON").toString());
			}
		} catch (Exception e) {

			LOGGER.error(" Error occoured at getPLUListAllCount method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException();
		}
		LOGGER.debug("Exit OrdersDAOImpl getTableSequenceId() ");
		return exceptionType;

	}

	/**
	 * @param shopingcartBean
	 * @throws PhotoOmniException
	 */
	private void insertShoppingCart(ShoppingCartBean shopingcartBean)
			throws PhotoOmniException {

		try {
			String insertMktBsktQry = RealTimeOrderQuery
					.insertShoppingCartQry().toString();

			Object[] objCartParms = new Object[] {
					shopingcartBean.getShopingCartNBR(),
					shopingcartBean.getSysLocationID(),
					shopingcartBean.getCartTypeCD(),
					shopingcartBean.getPmStatusCD(),
					shopingcartBean.getOrderPlacedDTTM(),
					shopingcartBean.getCreateUserID(),
					/* shopingcartBean.getCreateDTTM(), */
					shopingcartBean.getUpdateUserID()
			/* shopingcartBean.getUpdateDTTM() */
			};
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("insertMktBsktQry " + insertMktBsktQry
						+ objCartParms.toString());
			}

			jdbcTemplate.update(insertMktBsktQry, objCartParms);
		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" Error : insertShoppingCartmethod "
						+ e.getMessage());
			}
			throw new PhotoOmniException(e.getMessage());
		}
	}

	@Override
	public int getPluReportRecordPerPageCount() {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into getPluReportPageCount method of OrderDAOImpl");
		}
		LOGGER.info("Entering into getPluReportPageCount method of OrderDAOImpl");
		int recCount = 0;
		try {
			String queryPluRecPerPageCount = ReportsQuery.getPageCount()
					.toString();
			Object[] pluRecPerPageCountParams = { ReportsConstant.PLU_REPORT };
			recCount = jdbcTemplate.queryForObject(queryPluRecPerPageCount,
					pluRecPerPageCountParams, Integer.class);
		} catch (Exception e) {
			LOGGER.error("Error Occured in getPluReportPageCount method of OrderDAOImpl "
					+ e.getMessage());
		}
		return recCount;
	}

	public String getDecodeVal(String codeID, String orderAction) {
		String decode = "";
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT DECODE AS decode FROM ").append(
				PhotoOmniDBConstants.OM_CODE_DECODE);
		sqlQuery.append(" WHERE CODE_ID=? AND CODE_TYPE=? ");
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { codeID, orderAction });
		for (Map<String, Object> row : rows) {
			decode = row.get("decode").toString();
		}
		return decode;
	}

	public Long getEquipInstanceId(Long machineId, int equpmentTypeID) {
		Long sysID = null;
		if (machineId == null)
			return sysID;
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT SYS_EQUIPMENT_INSTANCE_ID AS ID FROM ").append(
				PhotoOmniDBConstants.OM_EQUIPMENT_INSTANCE);
		sqlQuery.append(" INNER JOIN ")
				.append(PhotoOmniDBConstants.OM_EQUIPMENT_TYPE)
				.append(" USING(SYS_EQUIPMENT_TYPE_ID) ");
		sqlQuery.append(" WHERE SYS_MACHINE_INSTANCE_ID=? AND EQUIPMENT_TYPE_NBR=?");
		List<Map<String, Object>> rows = jdbcTemplate
				.queryForList(sqlQuery.toString(), new Object[] { machineId,
						equpmentTypeID });
		for (Map<String, Object> row : rows) {
			sysID = Long.parseLong(row.get("ID").toString());
		}
		return sysID;
	}

	/**
	 * @param originOrder
	 * @param orderSource
	 * @return
	 */
	private String getVendorNBR(String orderSource, String originOrder) {

		String vendorOrderNBR = null;

		if ((orderSource.compareToIgnoreCase("Internet") == PhotoOmniConstants.ZERO)
				|| (orderSource.compareToIgnoreCase("Mobile") == PhotoOmniConstants.ZERO)) {

			vendorOrderNBR = originOrder;
		}
		return vendorOrderNBR;
	}

	/**
	 * @param originOrder
	 * @param orderSource
	 * @return
	 */
	private String getKisoskNBR(String orderSource, String originOrder) {

		String kioskOrderNBR = null;

		if ((orderSource.compareToIgnoreCase("Kiosk") == PhotoOmniConstants.ZERO)
				|| (orderSource.compareToIgnoreCase("iMemories") == PhotoOmniConstants.ZERO)
				|| (orderSource.compareToIgnoreCase("In-Store") == PhotoOmniConstants.ZERO)) {

			kioskOrderNBR = originOrder;
		}
		return kioskOrderNBR;
	}

	/**
	 * @param sysLocationID
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getTimeZone(Long sysLocationID) {
		String timeZone = "";

		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT TIME_ZONE FROM ").append(
				PhotoOmniDBConstants.OM_LOCATION);
		sqlQuery.append(" WHERE SYS_LOCATION_ID=?");
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { sysLocationID });
		for (Map<String, Object> row : rows) {
			timeZone = row.get("TIME_ZONE").toString();
		}

		return timeZone;
	}

	/**
	 * @param sysLocationID
	 * @return Timezone OffSet
	 */
	private Integer getTimezoneOffset(Long sysLocationID) {
		Integer timezoneOffset = null;

		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT DECODE FROM ").append(
				PhotoOmniDBConstants.OM_CODE_DECODE);
		sqlQuery.append(" INNER JOIN ")
				.append(PhotoOmniDBConstants.OM_LOCATION)
				.append(" ON CODE_ID = TIME_ZONE WHERE ACTIVE_CD=1 AND CODE_TYPE='UTC_Timezone_Offset' AND SYS_LOCATION_ID=?")
				.append(" AND CODE_SUB_TYPE = (SELECT CASE WHEN DAYLIGHT_SAVINGS_CD=1 THEN 'DST_Active' ELSE 'DST_In_Active' END FROM ")
				.append(PhotoOmniDBConstants.OM_LOCATION).append(" WHERE SYS_LOCATION_ID=?)") ;
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { sysLocationID,sysLocationID });
		for (Map<String, Object> row : rows) {
			timezoneOffset = Integer.parseInt(row.get("DECODE").toString());
		}
		return timezoneOffset;
	}

	/**
	 * @param storeOrderOrigin
	 * @param vendorID
	 * @return
	 */
	private String getOrderOrigin(String storeOrderOrigin, Long vendorID) {
		String orderOrigin = " ";
		if ((storeOrderOrigin.toUpperCase().compareTo("INTERNET") == PhotoOmniConstants.ZERO)
				&& vendorID != null) {
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append("SELECT SUB_TYPE_CD FROM ").append(
					PhotoOmniDBConstants.OM_VENDOR);
			sqlQuery.append(" WHERE SYS_VENDOR_ID=?");
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					sqlQuery.toString(), new Object[] { vendorID });
			for (Map<String, Object> row : rows) {
				storeOrderOrigin = row.get("SUB_TYPE_CD").toString();
			}
		}

		StringBuilder codeDecQuery = new StringBuilder();
		codeDecQuery.append("SELECT DECODE FROM ").append(
				PhotoOmniDBConstants.OM_CODE_DECODE);
		codeDecQuery
				.append(" WHERE UPPER(CODE_ID)=? AND CODE_TYPE='Order_Origin_Type'");
		List<Map<String, Object>> orderOriginList = jdbcTemplate.queryForList(
				codeDecQuery.toString(),
				new Object[] { storeOrderOrigin.toUpperCase() });
		for (Map<String, Object> map : orderOriginList) {
			orderOrigin = map.get("DECODE") != null ? map.get("DECODE")
					.toString() : " ";
		}

		//PROD Fix for electronic film orders
		if(!StringUtils.hasText(orderOrigin)){
			orderOrigin = "In-Store";
		}
		
		return orderOrigin;
	}

	private Long getSysPluID(String pluNBR) {
		Long pluID = null;
		List<Map<String, Object>> pluIDList = null;
		StringBuilder sqlQuery = RealTimeOrderQuery.getMBPMPluIDQuery();
		pluIDList = jdbcTemplate.queryForList(sqlQuery.toString(),
				new Object[] { pluNBR });
		for (Map<String, Object> map : pluIDList) {
			if (!CommonUtil.isNull(map.get("SYS_PLU_ID"))) {
				pluID = Long.parseLong(map.get("SYS_PLU_ID").toString());
			}
		}
		return pluID;
	}

	public int getPageSize(String reportName) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into getPageSize method of OrderDAOImpl");
		}
		LOGGER.info("Entering into getPageSize method of OrderDAOImpl");
		int Count = 0;
		try {
			String queryPageSize = ReportsQuery.getPageCount().toString();
			Object[] PageCountParams = { ReportsConstant.LATE_ENVELOPE_REPORT };
			Count = jdbcTemplate.queryForObject(queryPageSize, PageCountParams,
					Integer.class);
		} catch (Exception e) {
			LOGGER.error("Error Occured in getPageSize method of OrderDAOImpl "
					+ e.getMessage());
		}
		return Count;
	}
//changed from Private to public so that it can be accessed from BO -CostCalculation
	@Override
	public  Integer getPrintQty(OrderItem orderItem) {
		Integer printQty = 0;
		String printType = "";
		Integer packSize = 0;
		String prodPackQry = RealTimeOrderQuery.getProductPackInfo().toString();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(prodPackQry,
				new Object[] { orderItem.getpCPProductId() });
		for (Map<String, Object> row : rows) {
			// Changed As issue found
			if (!CommonUtil.isNull(row.get("PRINTING_TYPE"))) {
				printType = row.get("PRINTING_TYPE").toString();
			}
			packSize = Integer.parseInt(row.get("PACK_SIZE").toString());
		}

		if (printType.compareToIgnoreCase("PACKS") == PhotoOmniConstants.ZERO) {

			printQty = (Integer.parseInt(orderItem.getQuantity()) + Integer
					.parseInt(orderItem.getWasteQty())) * packSize;

		} else if (printType.compareToIgnoreCase("ALBUMS CALENDAR") == PhotoOmniConstants.ZERO) {
			printQty = (Integer.parseInt(orderItem.getQuantity()) + Integer
					.parseInt(orderItem.getWasteQty()))
					* Integer.parseInt(orderItem.getProductAttribute()
							.getNoOfInputs());
		} else {
			printQty = Integer.parseInt(orderItem.getQuantity())
					+ Integer.parseInt(orderItem.getWasteQty())
					+ Integer.parseInt(orderItem.getProductAttribute()
							.getPanaromicPrints());
		}
		return printQty;
	}

	private Map<String, Long> getVendorDetails(Long sysOrderID,
			String orderPlacedDTTM) {
		Map<String, Long> vendorDetails = new HashMap<String, Long>();
		String selectQry = RealTimeOrderQuery.selectQueryOrderRef().toString();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(selectQry,
				new Object[] { sysOrderID, orderPlacedDTTM });
		for (Map<String, Object> row : rows) {
			if (!CommonUtil.isNull(row.get("SYS_SRC_VENDOR_ID"))) {
				vendorDetails
						.put("SYS_SRC_VENDOR_ID", Long.parseLong(row.get(
								"SYS_SRC_VENDOR_ID").toString()));
			} else {
				vendorDetails.put("SYS_SRC_VENDOR_ID", new Long(0));
			}
			if (!CommonUtil.isNull(row.get("SYS_FULFILLMENT_VENDOR_ID"))) {
				vendorDetails.put("SYS_FULFILLMENT_VENDOR_ID", Long
						.parseLong(row.get("SYS_FULFILLMENT_VENDOR_ID")
								.toString()));
			} else {
				vendorDetails.put("SYS_FULFILLMENT_VENDOR_ID", new Long(0));
			}
		}
		return vendorDetails;
	}

	@SuppressWarnings("unused")
	private String getExceptionSeqID(String exceptionIdSequence) {
		String exceptionType = null;

		StringBuilder sqlQuery = RealTimeOrderQuery.getExceptionSeqId();
		exceptionType = jdbcTemplate.queryForObject(sqlQuery.toString(),
				new Object[] { exceptionIdSequence }, String.class);

		return exceptionType;
	}

	/**
	 * 
	 * @param orderReferenceId
	 * @return
	 */
	private String getOldOrderStatus(long orderReferenceId) {
		String oldOrderStatus = null;
		StringBuilder sqlQuery = RealTimeOrderQuery.getOldOrderStatus();
		List<Map<String, Object>> orderStatusList = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { orderReferenceId });
		for (Map<String, Object> map : orderStatusList) {
			oldOrderStatus = !CommonUtil.isNull(map.get("STATUS")) ? map.get(
					"STATUS").toString() : null;
		}
		return oldOrderStatus;
	}

	/**
	 * 
	 * @param orderReferenceId
	 * @return
	 */
	private String getOldOrderExceptionStatus(Long orderReferenceId) {
		String orderExceptionStatus = null;
		StringBuilder sqlQuery = RealTimeOrderQuery
				.getOldOrderExceptionStatus();
		List<Map<String, Object>> orderStatusList = jdbcTemplate.queryForList(
				sqlQuery.toString(), new Object[] { orderReferenceId });
		for (Map<String, Object> map : orderStatusList) {
			orderExceptionStatus = !CommonUtil.isNull(map
					.get("PREVIOUS_ORDER_STATUS")) ? map.get(
					"PREVIOUS_ORDER_STATUS").toString() : null;
		}
		return orderExceptionStatus;
	}

	/**
	 * This method check if a vendor is a Electronics film vendor.
	 * 
	 * @param vendorNbr
	 *            contains sysVendorId.
	 * @return isElectronicsFilmVendor.
	 */
	private boolean isElectronicsFilmVendor(String vendorNbr) {
		boolean isElectronicsFilmVendor = false;
		String sql = RealTimeOrderQuery.getElectronisFilmVendor().toString();
		LOGGER.debug(" Sql Query to check if vendor is a electronis film vendor "
				+ sql);
		int countVal = jdbcTemplate.queryForObject(sql,
				new Object[] { vendorNbr }, Integer.class);
		if (countVal > 0) {
			isElectronicsFilmVendor = true;
		}
		return isElectronicsFilmVendor;
	}

	/**
	 * Check if a role is present in the authorities of current user
	 * 
	 * @param authorities
	 *            all authorities assigned to current user
	 * @param role
	 *            required authority
	 * @return true if role is present in list of authorities assigned to
	 *         current user, false otherwise
	 */
	private boolean isRolePresent(Collection<GrantedAuthority> authorities,
			String role) {
		boolean isRolePresent = false;
		for (GrantedAuthority grantedAuthority : authorities) {
			isRolePresent = grantedAuthority.getAuthority().equals(role);
			if (isRolePresent)
				break;
		}
		return isRolePresent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.common.dao.OrderDAO#getOrderCost(long,
	 * java.lang.String)
	 */
	/*
	 * @Override public List<OmOrderBean> getOrderCost( long sysOrderId, String
	 * orderPlacedDttm) { //boolean getOrderCost = false;
	 * 
	 * List<OmOrderBean> omOrderBeanList = new ArrayList<OmOrderBean>(); try {
	 * String completedOrderQuery =
	 * RealTimeOrderQuery.getCompletedOrderQuery().toString(); omOrderBeanList =
	 * jdbcTemplate.query(completedOrderQuery, new Object[]
	 * {sysOrderId,orderPlacedDttm}, new OmOrderBeanRowMapper());
	 * 
	 * } catch (Exception e) { LOGGER.error("Error Occured" + e.getMessage()); }
	 * 
	 * 
	 * return omOrderBeanList;
	 * 
	 * }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getInstoreCost(com.walgreens.common
	 * .bean.OmOrderBean)
	 */
	public List<CostCalculationBean> getInstoreCost(OmOrderBean omOrderBean) {
		List<CostCalculationBean> costCalculationBeanList = new ArrayList<CostCalculationBean>();
		try {
			String orderItemVendorCostQuery = RealTimeOrderQuery
					.getOrderItemInstoreCostQuery();
			LOGGER.debug("orderItemInstoreCostQuery : "
					+ orderItemVendorCostQuery);
			costCalculationBeanList = jdbcTemplate.query(
					orderItemVendorCostQuery,
					new Object[] { omOrderBean.getSysOrderId(),
							omOrderBean.getOrderPlacedDttm() },
					new CostCalculationBeanRowMapper());
		} catch (Exception e) {

			LOGGER.error("Error" + e.getMessage());
		}
		return costCalculationBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getVendorCostComponent(com.walgreens
	 * .common.bean.CostCalculationBean)
	 */
	@Override
	public List<CostCalculationBean> getVendorCostComponent(
			CostCalculationBean costCalculationBean) {
		List<CostCalculationBean> costCalculationBeanList = new ArrayList<CostCalculationBean>();
		try {

			String orderItemVendorCostQuery = new StringBuilder(
					RealTimeOrderQuery.getOrderItemVendorCostQuery()).append(
					" AND OMORDLINE.SYS_PRODUCT_ID = ?").toString();
			LOGGER.debug("EorderItemVendorCostQuery : "
					+ orderItemVendorCostQuery);

			costCalculationBeanList = jdbcTemplate.query(
					orderItemVendorCostQuery, new Object[] {
							costCalculationBean.getSysOrderId(),
							costCalculationBean.getOrdPlcedDttm(),
							costCalculationBean.getSysProductId() },
					new CostCalculationBeanRowMapper());
		} catch (Exception e) {
			LOGGER.error("Error" + e.getMessage());
		}
		return costCalculationBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getCalculateDefaultCost(com.walgreens
	 * .common.bean.CostCalculationTransferBean, java.lang.String,
	 * java.lang.String)
	 */

	/*
	 * public List<CostCalculationBean>
	 * getCalculateDefaultCost(CostCalculationTransferBean
	 * costCalculationTransferBean, String calculatedProdList, String orderType)
	 * { List<CostCalculationBean> costCalculationBeanList = new
	 * ArrayList<CostCalculationBean>(); try{ String calculateDefaultCostQuery =
	 * RealTimeOrderQuery .getDefaultCalculationProdList(calculatedProdList);
	 * 
	 * LOGGER.debug("calculateDefaultCostQuery : " + calculateDefaultCostQuery);
	 * costCalculationBeanList = jdbcTemplate.query( calculateDefaultCostQuery,
	 * new Object[] { costCalculationTransferBean
	 * .getOmOrderAttributeBean().getOrderId(),
	 * costCalculationTransferBean.getOrdPlacedDttm() }, new
	 * DefaultCostCalculationBeanRowMapper()); } catch(Exception e) {
	 * LOGGER.error("Error"+e.getMessage()); }
	 * 
	 * return costCalculationBeanList; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getVendorCostCalculation(com.walgreens
	 * .common.bean.OmOrderBean)
	 */
	@Override
	public List<CostCalculationBean> getVendorCostCalculation(
			OmOrderBean omOrderBean) {
		List<CostCalculationBean> costCalculationBeanList = new ArrayList<CostCalculationBean>();
		try {
			String orderItemVendorCostQuery = RealTimeOrderQuery
					.getOrderItemVendorCostQuery();

			costCalculationBeanList = jdbcTemplate.query(
					orderItemVendorCostQuery,
					new Object[] { omOrderBean.getSysOrderId(),
							omOrderBean.getOrderPlacedDttm() },
					new CostCalculationBeanRowMapper());
			LOGGER.debug("orderItemVendorCostQuery: "
					+ orderItemVendorCostQuery);
		} catch (Exception e) {

		}
		return costCalculationBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#updateOrderCostDtl(com.walgreens.common
	 * .bean.OmOrderAttributeBean, java.lang.String)
	 */
	public boolean updateOrderCostDtl(
			OmOrderAttributeBean omOrderAttributeBean, String time) {

		LOGGER.debug("Entering CostCalculationItemWriter.updateOrderCostDtl.");
		boolean status = false;
		try {
			String updateCostCalOrdAttrQuery = RealTimeOrderQuery
					.updateCostCalOrdAttrQuery();
			int updateCount = jdbcTemplate.update(updateCostCalOrdAttrQuery,
					new Object[] { omOrderAttributeBean.getCost(),
							omOrderAttributeBean.getCostCalculatiomStatusCd(),
							omOrderAttributeBean.getFulfillmentVendorCost(),
							omOrderAttributeBean.getPayOnFulfillmentInd(),
							"system", omOrderAttributeBean.getSysorderId(),
							time });
			if (updateCount > 0) {
				status = true;
				LOGGER.debug("Cost updated in order. OrderId: "
						+ omOrderAttributeBean.getSysorderId()
						+ " updateCount = " + updateCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in updateOrderCostDtl.", e);
			status = false;
		}
		LOGGER.debug("Exiting CostCalculationItemWriter.updateOrderCostDtl.");
		return status;
	}

	/**
	 * Update cost details at order item level
	 * 
	 * @param costCalculationTransferBeanItem
	 * @return status
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#updateOrderItemCostDtl(com.walgreens
	 * .common.bean.CostCalculationTransferBean)
	 */
	public boolean updateOrderItemCostDtl(
			CostCalculationTransferBean costCalculationTransferBeanItem) {
		LOGGER.debug("Entering CostCalculationItemWriter.updateOrderItemCostDtl.");
		boolean status = false;
		String updateCostCalOrdLineQuery = RealTimeOrderQuery
				.updateCostCalOrdLineQuery();
		int updateCount = 0;
		try {
			for (OmOrderLineBean omOrderLineBean : costCalculationTransferBeanItem
					.getOmOrderLineBeanList()) {
				updateCount += jdbcTemplate.update(
						updateCostCalOrdLineQuery,
						new Object[] {
								omOrderLineBean.getCost(),
								omOrderLineBean.getFulfillmentVendorCost(),
								"system",
								omOrderLineBean.getOrderId(),
								omOrderLineBean.getProductId(),
								costCalculationTransferBeanItem
										.getOrdPlacedDttm() });
			}
			if (updateCount > 0) {
				status = true;
				LOGGER.debug("Cost updated in order Line. OrderId:"
						+ costCalculationTransferBeanItem
								.getOmOrderAttributeBean().getSysorderId()
						+ " updateCount = " + updateCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in updateOrderItemCostDtl.", e);
			status = false;
		}
		LOGGER.debug("Exiting CostCalculationItemWriter.updateOrderItemCostDtl.");
		return status;

	}

	/**
	 * Method to fetch details of Envelope History
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvHistoryBeanList
	 */
	public List<EnvHistoryBean> fetchEnvelopeHistoryDetails(
			EnvelopeNumberFilter reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeHistoryDetails method of OrderDAOImpl ");
		}
		List<EnvHistoryBean> envelopeHisBeanList = null;
		try {
			String selectQry = RealTimeOrderQuery.getEnvelopeHistryDtls()
					.toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			envelopeHisBeanList = dataGuardJdbcTemplate.query(selectQry,
					new Object[] { reqParams.getOrderId() },
					new EnvelopeHistryRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopeHistoryDetails method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeHistoryDetails method of OrderDAOImpl ");
			}
		}
		return envelopeHisBeanList;
	}

	/**
	 * Method to fetch details of Envelope Header Popup
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvelopePopupHeaderBean
	 */
	public EnvelopePopupHeaderBean fetchEnvelopePopupHeaderDtls(
			EnvelopeNumberFilter reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopePopupHeaderDtls method of OrderDAOImpl ");
		}
		EnvelopePopupHeaderBean popupHeaderBean = new EnvelopePopupHeaderBean();
		try {
			String sqlQuery = RealTimeOrderQuery.getEnvelopePopupHeaderQuery()
					.toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			popupHeaderBean = dataGuardJdbcTemplate.queryForObject(sqlQuery,
					new Object[] { reqParams.getOrderId() },
					new EnvelopePopupHeaderRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopePopupHeaderDtls method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopePopupHeaderDtls method of OrderDAOImpl ");
			}
		}
		return popupHeaderBean;

	}

	/**
	 * Method to fetch details of Envelope Order Information ie Order
	 * Description and Order Final Price
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvelopePopupHeaderBean
	 */
	public EnvelopeOrderDtlsBean fetchEnvelopeOrderInfo(
			EnvelopeNumberFilter reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeOrderInfo method of OrderDAOImpl ");
		}
		EnvelopeOrderDtlsBean orderInfo = new EnvelopeOrderDtlsBean();
		try {
			String orderDescQuery = RealTimeOrderQuery
					.getEnvelopeOrderDescriptionQry().toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			Object[] orderParam = new Object[] { reqParams.getOrderId(),
					reqParams.getStartDate() };
			orderInfo = dataGuardJdbcTemplate.queryForObject(orderDescQuery, orderParam,
					new EnvelopeOrderInfoRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopeOrderInfo method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeOrderInfo method of OrderDAOImpl ");
			}
		}
		return orderInfo;
	}

	/**
	 * Method to fetch details of Envelope Order discount amount and description
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeOrderPromotionBeanList
	 */
	public List<EnvelopeOrderPromotionBean> fetchEnvelopeOrderPromotion(
			EnvelopeNumberFilter reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeOrderPromotion method of OrderDAOImpl ");
		}
		List<EnvelopeOrderPromotionBean> orderPromotionDtlsList = new ArrayList<EnvelopeOrderPromotionBean>();
		try {
			String orderDiscountQuery = RealTimeOrderQuery
					.getEnvelopeOrderDiscountQry().toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			Object[] orderParam = new Object[] { reqParams.getOrderId(),
					reqParams.getStartDate() };
			orderPromotionDtlsList = dataGuardJdbcTemplate.query(orderDiscountQuery,
					orderParam, new EnvelopeOrderPromotionRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopeOrderPromotion method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeOrderPromotion method of OrderDAOImpl ");
			}
		}
		return orderPromotionDtlsList;
	}

	/**
	 * Method to fetch details of Envelope Order Line Information ie Product
	 * details, its Per unit price, quantity and Final Price
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeProductDtlsBeanList
	 */
	public List<EnvelopeProductDtlsBean> fetchEnvelopeOrderLineInfo(
			EnvelopeNumberFilter reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeOrderLineInfo method of OrderDAOImpl ");
		}
		List<EnvelopeProductDtlsBean> orderLineDtlsList = new ArrayList<EnvelopeProductDtlsBean>();
		try {
			String orderLineDescQuery = RealTimeOrderQuery
					.getEnvelopeOrderLineDescQry().toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			Object[] orderParam = new Object[] { reqParams.getOrderId(),
					reqParams.getStartDate() };
			orderLineDtlsList = dataGuardJdbcTemplate.query(orderLineDescQuery,
					orderParam, new EnvelopeProductDtlsRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopeOrderLineInfo method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeOrderLineInfo method of OrderDAOImpl ");
			}
		}
		return orderLineDtlsList;

	}

	/**
	 * Method to fetch details of Envelope Order Line discount amount and
	 * description
	 * 
	 * @param EnvelopeNumberFilter
	 * @return EnvelopeOrderLinePromotionBeanList
	 */
	public List<EnvelopeOrderLinePromotionBean> fetchEnvelopeOrderLinePromotion(
			EnvelopeNumberFilter reqParams) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering fetchEnvelopeOrderLineInfo method of OrderDAOImpl ");
		}
		List<EnvelopeOrderLinePromotionBean> orderLinePromotionList = new ArrayList<EnvelopeOrderLinePromotionBean>();
		try {
			String orderLineDiscountQuery = RealTimeOrderQuery
					.getEnvelopeOrderLinePromotionQry().toString();
			/*
			 * String startDate =
			 * CommonUtil.stringDateFormatChange(reqParams.getStartDate(),
			 * PhotoOmniConstants.DATE_FORMAT_FORTH,
			 * PhotoOmniConstants.DATE_FORMAT_TWO);
			 */
			Object[] orderParam = new Object[] { reqParams.getOrderId(),
					reqParams.getStartDate() };
			orderLinePromotionList = dataGuardJdbcTemplate.query(orderLineDiscountQuery,
					orderParam, new EnvelopeOrderLinePromotionRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at fetchEnvelopeOrderLineInfo method of OrderDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting fetchEnvelopeOrderLineInfo method of OrderDAOImpl ");
			}
		}
		return orderLinePromotionList;

	}

	/*--------------------------------FOR Exception Report Start----------------------------------*/
	/*
	 * Exception Report by Envelopes
	 */
	public List<ExceptionRepEnv> submitEnvReportRequest(
			ExceptionByEnvelopeFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of ExceptionReportDAOImpl ");
		}
		List<ExceptionRepEnv> repByEnvList = null;
		try {
			StringBuilder sqlQuery = ExceptionReportQuery
					.getExceptionByEnvMain(requestBean);
			List<Object> listParam = addParams(requestBean, sqlQuery);
			Object[] params = listParam.toArray();
			repByEnvList = dataGuardJdbcTemplate.query(sqlQuery.toString(), params,
					new ExceptionReportEnvelopeRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitReportRequest method of ExceptionReportDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of ExceptionReportDAOImpl ");
			}
		}
		return repByEnvList;
	}

	/**
	 * @param requestBean
	 * @param sqlQuery
	 * @return listParam
	 */
	private List<Object> addParams(ExceptionByEnvelopeFilter requestBean,
			StringBuilder sqlQuery) {
		String checkQuery;
		List<Object> listParam = new ArrayList<Object>();
		String locationNumber = requestBean.getStoreNumber();
		String startDate = CommonUtil.stringDateFormatChange(
				requestBean.getStartDate(),
				PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		String endDate = CommonUtil.stringDateFormatChange(
				requestBean.getEndDate(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		listParam.add(locationNumber);
		listParam.add(startDate);
		listParam.add(endDate);
		listParam.add(startDate);

		try {
			if (!(CommonUtil.isNull(requestBean.getCustomerLastName()) || requestBean
					.getCustomerLastName().isEmpty())) {
				checkQuery = ExceptionReportQuery.getCustomerNameCheck()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(requestBean.getCustomerLastName());
			}
			if (!(CommonUtil.isNull(requestBean.getEmployeeLastName()) || requestBean
					.getEmployeeLastName().isEmpty())) {
				checkQuery = ExceptionReportQuery.getEmployeeNameCheck()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(requestBean.getEmployeeLastName());
			}

			if (!(CommonUtil.isNull(requestBean.getEnvNumber()) || requestBean
					.getEnvNumber().isEmpty())) {
				checkQuery = ExceptionReportQuery.getEnvNumberCheck()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(requestBean.getEnvNumber());
			}

			if (!(CommonUtil.isNull(requestBean.getEnvelopeEnteredDate()) || requestBean
					.getEnvelopeEnteredDate().isEmpty())) {
				checkQuery = ExceptionReportQuery.getEnvEnteredDttmCheck()
						.toString();
				sqlQuery.append(checkQuery);
				String envEnteredDate = CommonUtil.stringDateFormatChange(
						requestBean.getEnvelopeEnteredDate(),
						PhotoOmniConstants.DATE_FORMAT_SEVEN,
						PhotoOmniConstants.DATE_FORMAT_TWO);
				listParam.add(envEnteredDate);
			}
			if (!((CommonUtil.isNull(requestBean.getExceptionStatus())) || requestBean
					.getExceptionStatus().equalsIgnoreCase("ALL"))) {
				checkQuery = ExceptionReportQuery.getExceptionStatusCheck()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(requestBean.getExceptionStatus());
			}
			if (!((CommonUtil.isNull(requestBean.getReasonType())) || requestBean
					.getReasonType().equalsIgnoreCase("ALL"))) {
				checkQuery = ExceptionReportQuery.getReasonTypeCheck()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(requestBean.getReasonType());
			}
			if (requestBean.isPrint()) {
				checkQuery = ExceptionReportQuery.getorderByQuery(requestBean)
						.toString();
				sqlQuery.append(checkQuery);
			} else {
				Map<String, Object> queryParam = getQueryParam(requestBean
						.getCurrentPageNo());
				String startLimit = !CommonUtil.isNull(queryParam
						.get("START_LIMIT")) ? queryParam.get("START_LIMIT")
						.toString() : "";
				String endLimit = !CommonUtil.isNull(queryParam
						.get("END_LIMIT")) ? queryParam.get("END_LIMIT")
						.toString() : "";
				checkQuery = ExceptionReportQuery.getPaginationQuery()
						.toString();
				sqlQuery.append(checkQuery);
				listParam.add(startLimit);
				listParam.add(endLimit);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportExceptionReason method of ExceptionReportDAOImpl - "
					, e);
		} finally {

		}
		return listParam;
	}

	/**
	 * @param reqBean
	 * @return
	 */
	private Map<String, Object> getQueryParam(String currentPageNo) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getQueryParam method of ExceptionReportDAOImpl ");
		}
		Map<String, Object> queryParam = null;
		try {
			Map<String, Long> pageLimit = CommonUtil.getPaginationLimit(
					currentPageNo,
					PhotoOmniConstants.EXCEPTION_REPORT_PAGINATION_SIZE);
			if (!CommonUtil.isNull(pageLimit)) {
				queryParam = new HashMap<String, Object>(pageLimit);
			} else {
				queryParam = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getQueryParam method of ExceptionReportDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getQueryParam method of ExceptionReportDAOImpl ");
			}
		}
		return queryParam;
	}

	/**
	 * 
	 */
	public List<ExceptionByEmployeeBean> submitEmployeeReportRequest(
			ExceptionEmployeeFilter requestBean) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitEmployeeReportRequest method of ExceptionReportDAOImpl ");
		}
		List<ExceptionByEmployeeBean> exceptionByEmployeeBeanList = new ArrayList<ExceptionByEmployeeBean>();
		try {
			String sqlQuery = ExceptionReportQuery
					.getExceptionReportByEmployeeQuery(requestBean).toString();
			Object[] param = paramForExceptionByEmployee(requestBean);
			exceptionByEmployeeBeanList = dataGuardJdbcTemplate.query(sqlQuery,
					param, new ExceptionReportEmployeeRowMapper());

		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitEmployeeReportRequest method of ExceptionReportDAOImpl - "
					, e);

		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitEmployeeReportRequest method of ExceptionReportDAOImpl ");
			}
		}
		return exceptionByEmployeeBeanList;
	}

	/**
	 * This method prepares params for Exception Report by employees
	 * 
	 * @param requestBean
	 * @return param
	 */
	private Object[] paramForExceptionByEmployee(
			ExceptionEmployeeFilter requestBean) {
		String startDate = CommonUtil.stringDateFormatChange(requestBean
				.getStartDate().toString(),
				PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		String endDate = CommonUtil.stringDateFormatChange(requestBean
				.getEndDate().toString(), PhotoOmniConstants.DATE_FORMAT_SEVEN,
				PhotoOmniConstants.DATE_FORMAT_TWO);
		/*
		 * Map<String, Object> queryParam =
		 * getQueryParam(requestBean.getCurrentPageNo()); String startLimit =
		 * !CommonUtil.isNull(queryParam.get("START_LIMIT")) ?
		 * queryParam.get("START_LIMIT").toString() : ""; String endLimit =
		 * !CommonUtil.isNull(queryParam.get("END_LIMIT")) ?
		 * queryParam.get("END_LIMIT").toString() : "";
		 */
		// Object[] param = {startDate,endDate,requestBean.getStoreNumber(),
		// startLimit, endLimit};
		Object[] param = { requestBean.getStoreNumber(), startDate, endDate, startDate  };
		return param;
	}

	/**
	 * This method return Exception Reason from OM_EXCEPTION_TYPE table
	 */
	public List<ExceptionReason> getReportExceptionReason() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportExceptionReason method of ReportsDAOImpl ");
		}
		List<ExceptionReason> reasonList = null;
		String reasonQuery = ExceptionReportQuery.getExceptionReason()
				.toString();
		try {
			reasonList = jdbcTemplate.query(reasonQuery,
					new ExceptionReasonRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getReportExceptionReason method of ExceptionReportDAOImpl - "
					, e);
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at getReportExceptionReason method of ExceptionReportDAOImpl - "
					, e);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportExceptionReason method of ExceptionReportDAOImpl - "
					, e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportExceptionReason method of ExceptionReportDAOImpl ");
			}
		}
		return reasonList;
	}

	/*
	 * Below changes are made for CostCalculationChanges
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.common.dao.OrderDAO#getOrderCost(long,
	 * java.lang.String)
	 */
	@Override
	public OmOrderAttributeBean getOrderCost(long sysOrderId,
			String orderPlacedDttm) {

		List<OrderItemLineBean> orderItemLineBeanList = new ArrayList<OrderItemLineBean>();
		OmOrderAttributeBean omOrderAttributeBean = null;
		try {
			String completedOrderQuery = RealTimeOrderQuery
					.getCompletedOrderQuery().toString();
			String itemDetailsQuery = RealTimeOrderQuery.getItemDetailsQuery();

			omOrderAttributeBean = (OmOrderAttributeBean) jdbcTemplate
					.queryForObject(completedOrderQuery, new Object[] {
							sysOrderId, orderPlacedDttm },
							new OmOrderBeanRowMapper());

			orderItemLineBeanList = jdbcTemplate.query(itemDetailsQuery,
					new Object[] { sysOrderId, orderPlacedDttm },
					new OrderLineDetailsRowMapper());

			omOrderAttributeBean
					.setOrderItemLineBeanList(orderItemLineBeanList);

		} catch (Exception e) {
			LOGGER.error("Error Occured", e);

		}

		return omOrderAttributeBean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getInstoreCost(com.walgreens.common
	 * .bean.OmOrderBean)
	 */

	public CostCalculationBean getInstoreCostEquipmentCost(
			OrderItemLineBean orderItemLineBean) throws PhotoOmniException {

		CostCalculationBean calculationBean = null;
		try {
			String inStoreEquipmentQuery = RealTimeOrderQuery
					.getOrderItemInstoreCostQuery();

			LOGGER.debug("orderItemInstoreCostQuery : " + inStoreEquipmentQuery);
			List<CostCalculationBean> calculationBeanList = (List<CostCalculationBean>) jdbcTemplate
					.query(inStoreEquipmentQuery, new Object[] {
							orderItemLineBean.getSysEquipmentInstanceId(),
							orderItemLineBean.getSysProductId() },
							new CostCalculationBeanRowMapper());
			if (!CommonUtil.isNull(calculationBeanList)
					&& calculationBeanList.size() > 0) {
				calculationBean = calculationBeanList.get(0); // As only one row
																// will be
																// fetched from
																// query side.
			}

		} catch (Exception e) {
			LOGGER.error("Error", e.getMessage());
			throw new PhotoOmniException();
		}
		return calculationBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getCalculateDefaultCost(com.walgreens
	 * .common.bean.CostCalculationTransferBean, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<CostCalculationBean> getCalculateDefaultCost(
			CostCalculationTransferBean costCalculationTransferBean,
			String calculatedProdList, String orderType) {
		List<CostCalculationBean> costCalculationBeanList = new ArrayList<CostCalculationBean>();
		try {
			String calculateDefaultCostQuery = RealTimeOrderQuery
					.getDefaultCalculationProdList(calculatedProdList);

			LOGGER.debug("calculateDefaultCostQuery : "
					+ calculateDefaultCostQuery);
			costCalculationBeanList = jdbcTemplate.query(
					calculateDefaultCostQuery, new Object[] {
							costCalculationTransferBean
									.getOmOrderAttributeBean().getSysorderId(),
							costCalculationTransferBean.getOrdPlacedDttm() },
					new DefaultCostCalculationBeanRowMapper());
		} catch (Exception e) {
			LOGGER.error("Error" + e.getMessage());
		}

		return costCalculationBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getVendorCostCalculation(com.walgreens
	 * .common.bean.OmOrderBean)
	 */
	public CostCalculationBean getVendorCostCalculation(
			OrderItemLineBean orderItemLineBean,
			OmOrderAttributeBean omOrderAttributeBean)
			throws PhotoOmniException {

		CostCalculationBean calculationBean = null;
		try {

			String orderItemVendorCostQuery = RealTimeOrderQuery
					.getOrderItemVendorCostQuery();

			List<CostCalculationBean> calculationBeanList = (List<CostCalculationBean>) jdbcTemplate
					.query(orderItemVendorCostQuery, new Object[] {
							orderItemLineBean.getSysProductId(),
							omOrderAttributeBean.getSysFulfilmentVendorId() },
							new CostCalculationBeanRowMapperForVendor());
			if (!CommonUtil.isNull(calculationBeanList)) {
				calculationBean = calculationBeanList.get(0);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occurred in getVendorCostCalculation:", e);
			throw new PhotoOmniException();
		}
		return calculationBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#updateOrderCostDtl(com.walgreens.common
	 * .bean.OmOrderAttributeBean, java.lang.String)
	 */
	public boolean updateOrderCostDtl(OmOrderAttributeBean omOrderAttributeBean) {

		LOGGER.debug("Entering CostCalculationItemWriter.updateOrderCostDtl.");
		boolean status = false;
		try {
			String updateCostCalOrdAttrQuery = RealTimeOrderQuery
					.updateCostCalOrdAttrQuery();
			int updateCount = jdbcTemplate.update(updateCostCalOrdAttrQuery,
					new Object[] { omOrderAttributeBean.getCost(),
							omOrderAttributeBean.getCostCalculatiomStatusCd(),
							omOrderAttributeBean.getFulfillmentVendorCost(),
							omOrderAttributeBean.getPayOnFulfillmentInd(),
							omOrderAttributeBean.getSysorderId(),
							omOrderAttributeBean.getOrderPlacedDttm() });
			if (updateCount > 0) {
				status = true;
				LOGGER.debug("Cost updated in order. OrderId: "
						+ omOrderAttributeBean.getSysorderId()
						+ " updateCount = " + updateCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in updateOrderCostDtl.", e);
			status = false;
		}
		LOGGER.debug("Exiting CostCalculationItemWriter.updateOrderCostDtl.");
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#updateOrderItemCostDtl(com.walgreens
	 * .common.bean.CostCalculationTransferBean)
	 */
	public boolean updateOrderItemCostDtl(
			OmOrderAttributeBean omOrderAttributeBean) {
		LOGGER.debug("Entering CostCalculationItemWriter.updateOrderItemCostDtl.");
		boolean status = false;
		String updateCostCalOrdLineQuery = RealTimeOrderQuery
				.updateCostCalOrdLineQuery();
		int updateCount = 0;
		try {
			for (OrderItemLineBean omOrderLineBean : omOrderAttributeBean
					.getOrderItemLineBeanList()) {
				updateCount += jdbcTemplate.update(updateCostCalOrdLineQuery,
						new Object[] { omOrderLineBean.getCost(),
								omOrderLineBean.getFulfillmentVendorCost(),
								"system", omOrderLineBean.getSysOrderId(),
								omOrderLineBean.getSysProductId(),
								omOrderAttributeBean.getOrderPlacedDttm() });
			}
			if (updateCount > 0) {
				status = true;
				LOGGER.debug("Cost updated in order Line. OrderId:"
						+ omOrderAttributeBean.getSysorderId()
						+ " updateCount = " + updateCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occur in updateOrderItemCostDtl.", e);
			status = false;
		}
		LOGGER.debug("Exiting CostCalculationItemWriter.updateOrderItemCostDtl.");
		return status;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.common.dao.OrderDAO#getInstoreDefaultMachineCost(com.walgreens
	 * .common.bean.OrderItemLineBean)
	 */
	@Override
	public CostCalculationBean getInstoreDefaultMachineCost(
			OrderItemLineBean orderItemLineBean) throws PhotoOmniException {

		CostCalculationBean calculationBean = null;
		try {

			String inStoreMachineCostQuery = RealTimeOrderQuery
					.getInstoreMachineCostQuery();
			LOGGER.debug("orderItemInstoreCostQuery : "
					+ inStoreMachineCostQuery);
			List<CostCalculationBean> calculationBeanList = (List<CostCalculationBean>) jdbcTemplate
					.query(inStoreMachineCostQuery,
							new Object[] { orderItemLineBean
									.getSysMachineInstanceId() },
							new MachineCostCalculationBeanRowMapper());
			if (!CommonUtil.isNull(calculationBeanList)
					&& calculationBeanList.size() > 0) {
				calculationBean = calculationBeanList.get(0); // As only one row
																// will be
																// fetched from
																// query side.
			}
		} catch (Exception e) {
			LOGGER.error("Error", e);
			throw new PhotoOmniException();

		}
		return calculationBean;

	}
	@Override
	public boolean updateOmOrderLineAttribute(long sysOrderLineId,String orderPlacedDttm, int printQty){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into the method" );
		}
		SimpleDateFormat sdf= new SimpleDateFormat("YYYY-MM-DD HH24:mm:ss");
		boolean status = true ; 
		String orderItemAttributeQry = RealTimeOrderQuery
				.updateQryOrderItemAttributeRef().toString();
		try{
			 Object[] objItemAttrParms = new Object[] {
					 printQty,
					PhotoOmniConstants.UPDATE_USER_ID,
					/* PhotoOmniConstants.UPDATE_DTTM, */
					sysOrderLineId,
					orderPlacedDttm};
			jdbcTemplate.update(orderItemAttributeQry, objItemAttrParms);
		}catch(Exception e){
			status =false;
			LOGGER.debug("Exception occured during update in updateOmOrderLineAttribute" );
		}
		
		return status;
	}
	

	/**
	 * @param dataGuardJdbcTemplate
	 */
	public void setDataGuardJdbcTemplate(JdbcTemplate dataGuardJdbcTemplate) {
		this.dataGuardJdbcTemplate = dataGuardJdbcTemplate;
	}	
	
}
