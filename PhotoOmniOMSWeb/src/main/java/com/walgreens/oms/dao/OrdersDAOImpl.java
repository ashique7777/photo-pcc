package com.walgreens.oms.dao;

import java.sql.Timestamp;
import java.util.Date;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.OrderASNDetailsBean;
import com.walgreens.oms.bean.UnclaimedEnvCustOrderRespBean;
import com.walgreens.oms.bean.UnclaimedEnvCustorderReqBean;
import com.walgreens.oms.json.bean.UnclaimedEnvFilter;
import com.walgreens.oms.json.bean.UnclaimedEnvRespList;
import com.walgreens.oms.rowmapper.OrderASNDetailsBeanRowMapper;
import com.walgreens.oms.rowmapper.UnclaimedEnvCustOrderRowmapper;
import com.walgreens.oms.rowmapper.UnclaimedEnvRowmapper;
import com.walgreens.oms.service.OrderRestServiceImpl;
import com.walgreens.oms.utility.AsnOrderQuery;
import com.walgreens.oms.utility.ReportsQuery;
import com.walgreens.oms.utility.UnclaimedEnvQuery;

@Repository("OrdersDAO")
public class OrdersDAOImpl implements OrdersDAO, PhotoOmniConstants {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderRestServiceImpl.class);

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate dataGuardJdbcTemplate;	

	AsnOrderQuery asnOrderQuery = new AsnOrderQuery();

	@Override
	public boolean updateASNDetails(OrderASNDetailsBean orderASNDetailsBean) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	@Transactional
	public boolean getSysOrderId(long sysOrderId)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl getSysOrderId()");
		String sysOrderIdQuery = asnOrderQuery.getSysOrderIdQuery();
		boolean returnStatus = false;
		LOGGER.debug("OrdersDAOImpl checkItemExist() query: "
				+ sysOrderIdQuery);

		try {
			int checkSysOrderId = jdbcTemplate.queryForObject(
					sysOrderIdQuery, new Object[] { sysOrderId
							}, Integer.class);
			LOGGER.debug("OrdersDAOImpl checkItemExist() sysOrderLineId: "
					+ checkSysOrderId);

			if (checkSysOrderId > 0) {
				returnStatus = true;
			}

		} catch (Exception ex) {
			LOGGER.error("Exception occured in OrdersDAOImpl checkItemExist()",
					ex);
			throw new PhotoOmniException();
		}
		LOGGER.debug("Exiting OrdersDAOImpl checkItemExist()");
		return returnStatus;
	}
	
	@Override
	@Transactional
	public boolean checkUpdateOmShipment(OrderASNDetailsBean orderASNDetailsBean)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl checkupdateOmShipment()");
		String checkupdateOmShipmentQuery = asnOrderQuery.getCheckUpdateOmShipmentQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateOmShipment() query:"
				+ checkupdateOmShipmentQuery);
		try {
			LOGGER.debug("Entering into checkupdateOmShipmentQuery of AnsorderQuery");
			long sysOrderId = orderASNDetailsBean.getSysOrderId();
			LOGGER.debug("sysOrderId    " + sysOrderId);
			String status = orderASNDetailsBean.getStatus();
			LOGGER.debug("status    " + status);
			int locationNo = orderASNDetailsBean.getLocationNumber();
			LOGGER.debug("locationNo    " + locationNo);
			String locationType = orderASNDetailsBean.getLocationType();
			LOGGER.debug("locationType    " + locationType);
			int vendorId = orderASNDetailsBean.getVendorId();
			LOGGER.debug("vendorId    " + vendorId);
			Timestamp orderPlacedDttm = orderASNDetailsBean
					.getOrderPlacedDttm();
			LOGGER.debug("orderPlacedDttm    " + orderPlacedDttm);
			String carrierName = orderASNDetailsBean.getShipmentCompany();
			LOGGER.debug("carrierName    " + carrierName);
			String trackingNo = orderASNDetailsBean.getTrackingNo();
			LOGGER.debug("trackingNo    " + trackingNo);
			Timestamp actualShippedDTTM= orderASNDetailsBean.getShippedDTTM();
			LOGGER.debug("actualShippedDTTM    " + actualShippedDTTM);
			Timestamp estimatedShippedDttm= orderASNDetailsBean.getShippedDTTM();
			LOGGER.debug("estimatedShippedDttm    " + estimatedShippedDttm);
			Timestamp expectedArrivalDTTM=orderASNDetailsBean.getArrivalDTTM();
			LOGGER.debug("expectedArrivalDTTM    " + expectedArrivalDTTM);
			Timestamp actualArrivalDTTM=orderASNDetailsBean.getArrivalDTTM();
			LOGGER.debug("actualArrivalDTTM    " + actualArrivalDTTM);
			String shippedUrl = orderASNDetailsBean.getShipmentURL();
			LOGGER.debug("shippedUrl    " + shippedUrl);
			String companyPhone = orderASNDetailsBean.getShipmentCompanyPhone();
			LOGGER.debug("companyPhone    " + companyPhone);
			
			
			//Timestamp rollsPickUpDttm = new Timestamp(new Date().getTime());
			Timestamp createDttm = new Timestamp(new Date().getTime());
			Timestamp updateDttm = new Timestamp(new Date().getTime());
			int updStatusCount = jdbcTemplate.update(checkupdateOmShipmentQuery,
					new Object[] { orderPlacedDttm, trackingNo,
							carrierName, actualShippedDTTM, estimatedShippedDttm,
							status, expectedArrivalDTTM, actualArrivalDTTM,
							shippedUrl, companyPhone,CREATE_USER_ID, createDttm, UPDATE_USER_ID,
							updateDttm,sysOrderId });
			LOGGER.debug("OrdersDAOImpl updateOmShipment() updStatusCount:"
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl checkUpdateOmShipment()",daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl checkUpdateOM_SHIPMENT()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateOmShipment()");
		return updStatus;

	}


	@Override
	@Transactional
	public boolean updateOmShipment(OrderASNDetailsBean orderASNDetailsBean)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl updateOmShipment()");
		String updateOmShipmentQuery = asnOrderQuery.getUpdateOmShipmentQuery();
		
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateOmShipment() query:"
				+ updateOmShipmentQuery);
		try {
			LOGGER.debug("Entering into updateOmShipmentQuery of AnsorderQuery");
			long sysOrderId = orderASNDetailsBean.getSysOrderId();
			LOGGER.debug("sysOrderId    " + sysOrderId);
			String status = orderASNDetailsBean.getStatus();
			LOGGER.debug("status    " + status);
			int locationNo = orderASNDetailsBean.getLocationNumber();
			LOGGER.debug("locationNo    " + locationNo);
			String locationType = orderASNDetailsBean.getLocationType();
			LOGGER.debug("locationType    " + locationType);
			int vendorId = orderASNDetailsBean.getVendorId();
			LOGGER.debug("vendorId    " + vendorId);
			Timestamp orderPlacedDttm = orderASNDetailsBean
					.getOrderPlacedDttm();
			LOGGER.debug("orderPlacedDttm    " + orderPlacedDttm);
			String carrierName = orderASNDetailsBean.getShipmentCompany();
			LOGGER.debug("carrierName    " + carrierName);
			String trackingNo = orderASNDetailsBean.getTrackingNo();
			LOGGER.debug("trackingNo    " + trackingNo);
			Timestamp actualShippedDTTM= orderASNDetailsBean.getShippedDTTM();
			LOGGER.debug("actualShippedDTTM    " + actualShippedDTTM);
			Timestamp estimatedShippedDTTM= orderASNDetailsBean.getShippedDTTM();
			LOGGER.debug("actualShippedDTTM    " + estimatedShippedDTTM);
			Timestamp expectedArrivalDTTM=orderASNDetailsBean.getArrivalDTTM();
			LOGGER.debug("expectedArrivalDTTM    " + expectedArrivalDTTM);
			Timestamp actualArrivalDTTM=orderASNDetailsBean.getArrivalDTTM();
			LOGGER.debug("actualArrivalDTTM    " + actualArrivalDTTM);
			String shippedUrl = orderASNDetailsBean.getShipmentURL();
			LOGGER.debug("shippedUrl    " + shippedUrl);
			String companyPhone = orderASNDetailsBean.getShipmentCompanyPhone();
			LOGGER.debug("companyPhone    " + companyPhone);

			int updStatusCount = jdbcTemplate.update(updateOmShipmentQuery,
					new Object[] { orderPlacedDttm, sysOrderId, trackingNo,
							carrierName, actualShippedDTTM, estimatedShippedDTTM,
							status, expectedArrivalDTTM, actualArrivalDTTM,
							shippedUrl, companyPhone,CREATE_USER_ID, UPDATE_USER_ID});
			LOGGER.debug("OrdersDAOImpl updateOmShipment() updStatusCount:"
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl insertItemIntoOmOrderLine()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl insertItemIntoOmOrderLine()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateOmShipment()");
		return updStatus;

	}

	@Override
	@Transactional()
	public boolean updateOmOrderHistory(OrderASNDetailsBean orderHistoryBean)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl updateOmOrderHistory()");
		String updateOmOrdHistoryQuery = asnOrderQuery
				.getUpdateOmOrdHistoryQuery();
		boolean updStatus = false;

		try {
			int updStatusCount = jdbcTemplate.update(
					updateOmOrdHistoryQuery,
					new Object[] { orderHistoryBean.getSysOrderId(),
							orderHistoryBean.getActionCD(),
							orderHistoryBean.getStatus(),
							orderHistoryBean.getActionNotes(),
							orderHistoryBean.getOrderPlacedDttm(),
							CREATE_USER_ID, UPDATE_USER_ID });
			LOGGER.debug("OrdersDAOImpl updateOmOrderHistory() updStatusCount: "
					+ updStatusCount);
			updStatus = true;

		} catch (DataAccessException daex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderHistory()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderHistory()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}

		LOGGER.debug("Exiting OrdersDAOImpl updateOmOrderHistory()");
		return updStatus;
	}

	@Override
	public Map<String, Object> getAdditonalOrderDetails(String sysOrderId,
			int locationNumber) throws PhotoOmniException {
		Map<String, Object> additionalOrdrDtlM = null;
		try {
			String additonalOrderDetails = asnOrderQuery
					.getUpdateOmOrderQuery();
			additionalOrdrDtlM = jdbcTemplate.queryForMap(
					additonalOrderDetails, new Object[] { sysOrderId,
							locationNumber });
		} catch (DataAccessException ex) {
			LOGGER.error("Exception occured in OrdersDAOImpl getAdditonalOrderDetails()",
					ex);
			throw new PhotoOmniException();
		}
		return additionalOrdrDtlM;

	}

	@Override
	@Transactional
	public boolean checkItemExist(String pcpOrderId, String pcpProductId)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl checkItemExist()");
		String checkItemExistQuery = asnOrderQuery.getCheckItemExistQuery();
		boolean returnStatus = false;
		LOGGER.debug("OrdersDAOImpl checkItemExist() query: "
				+ checkItemExistQuery);

		try {
			int sysOrderLineId = jdbcTemplate.queryForObject(
					checkItemExistQuery, new Object[] { pcpOrderId,
							pcpProductId }, Integer.class);
			LOGGER.debug("OrdersDAOImpl checkItemExist() sysOrderLineId: "
					+ sysOrderLineId);

			if (sysOrderLineId != 0) {
				returnStatus = true;
			}

		} catch (Exception ex) {
			LOGGER.error("Exception occured in OrdersDAOImpl checkItemExist()",
					ex);
			throw new PhotoOmniException();
		}
		LOGGER.debug("Exiting OrdersDAOImpl checkItemExist()");
		return returnStatus;
	}

	@Override
	@Transactional
	public boolean removeItemFromOmOrderLine(long sysOrderId,
			String pcpProductId) throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl removeItemFromOmOrderLine()");
		String removeItemFromOmOrderLineQuery = asnOrderQuery
				.getRemoveItemFromOmOrderLineQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl removeItemFromOmOrderLine() query: "
				+ removeItemFromOmOrderLineQuery);

		try {
			int updStatusCount = jdbcTemplate.update(
					removeItemFromOmOrderLineQuery, new Object[] { sysOrderId,
							pcpProductId });
			LOGGER.debug("OrdersDAOImpl removeItemFromOmOrderLine() updStatusCount: "
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderHistory()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderHistory()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl removeItemFromOmOrderLine()");
		return updStatus;

	}

	@Override
	public boolean updateItemIntoOmOrderLine(int orderedQty,
			Double originalRetail, Double calculatedPrice,
			Double discountAmount, long sysOrderId, String pcpProductId)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl updateItemIntoOmOrderLine()");
		String updateItemIntoOmOrderLineQuery = asnOrderQuery
				.getupdateItemIntoOmOrderLineQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateItemIntoOmOrderLine() query: "
				+ updateItemIntoOmOrderLineQuery);
		try {
			int updStatusCount = jdbcTemplate.update(
					updateItemIntoOmOrderLineQuery, new Object[] { orderedQty,
							originalRetail, calculatedPrice, discountAmount,
							sysOrderId, pcpProductId });
			LOGGER.debug("OrdersDAOImpl updateItemIntoOmOrderLine() updStatusCount: "
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl updateOmOrderLine()",daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderLiney()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateItemIntoOmOrderLine()");
		return updStatus;

	}

	@Override
	@Transactional
	public OrderASNDetailsBean getOrderDetails(Long orderNumber,
			int locationNumber) throws PhotoOmniException {
		
		LOGGER.debug("Entering OrdersDAOImpl getOrderDetails()");
		OrderASNDetailsBean orderASNDetailsBean = new OrderASNDetailsBean();
		// List<OrderASNDetailsBean> orderASNDetailsList = new
		// ArrayList<OrderASNDetailsBean>();

		String asnOrderDetailsQuery = AsnOrderQuery.getAsnOrderDetailsQuery();
		LOGGER.debug("OrdersDAOImpl getOrderDetails() query: "
				+ asnOrderDetailsQuery);
		try {
			orderASNDetailsBean = (OrderASNDetailsBean) jdbcTemplate
					.queryForObject(asnOrderDetailsQuery,
							new OrderASNDetailsBeanRowMapper(), new Object[] {
						orderNumber.toString(), locationNumber});

		} catch (EmptyResultDataAccessException ex) {
			LOGGER.error("EmptyResultDataAccessException occured in OrdersDAOImpl getOrderDetails()",
					ex);
			throw new PhotoOmniException(RESPONSE_DB_ERROR_STRING);
		} catch (IncorrectResultSizeDataAccessException ex) {
			LOGGER.error(
					"IncorrectResultSizeDataAccessException occured in OrdersDAOImpl getOrderDetails()",
					ex);
			throw new PhotoOmniException(RESPONSE_DB_ERROR_STRING);
		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl updateOmOrderHistory()",
					daex);
			throw new PhotoOmniException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl getOrderDetails()", ex);
			throw new PhotoOmniException(ex.getMessage());
		}
		LOGGER.info("Exiting OrdersDAOImpl getOrderDetails()");
		return orderASNDetailsBean;
	}

	@Override
	@Transactional
	public int getvendorCostCalcStageInd(int sysSrcVendorId) {

		LOGGER.debug("Entering OrdersDAOImpl getvendorCostCalcStageInd()");
		int vendorCostCalcStageInd = 0;

		String vendorCostCalcStageIndQuery = asnOrderQuery
				.getVendorCostCalcStageIndQuery();
		LOGGER.debug("OrdersDAOImpl getvendorCostCalcStageInd() query: "
				+ vendorCostCalcStageIndQuery);

		try {
			String costCalcStageInd = jdbcTemplate.queryForObject(
					vendorCostCalcStageIndQuery,
					new Object[] { sysSrcVendorId }, String.class);
			vendorCostCalcStageInd = Integer.parseInt(costCalcStageInd);
			LOGGER.debug("OrdersDAOImpl getvendorCostCalcStageInd() vendorCostCalcStageInd: "
					+ vendorCostCalcStageInd);

		} catch (EmptyResultDataAccessException ex) {
			LOGGER.error("EmptyResultDataAccessException occured in OrdersDAOImpl getvendorCostCalcStageInd()",ex);
		} catch (IncorrectResultSizeDataAccessException ex) {
			LOGGER.error("IncorrectResultSizeDataAccessException occured in OrdersDAOImpl getvendorCostCalcStageInd()",ex);
		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl getvendorCostCalcStageInd()   ",daex);
		} catch (Exception ex) {
			LOGGER.error("Exception occured in OrdersDAOImpl getvendorCostCalcStageInd()",ex);			
		}
		LOGGER.debug("Exiting OrdersDAOImpl getvendorCostCalcStageInd()");

		return vendorCostCalcStageInd;
	}

	@Override
	@Transactional
	public boolean updateOrdAttrCostCalStatus(long sysOrderId)
			throws PhotoOmniException {
		LOGGER.debug("Entering OrdersDAOImpl updateOrdAttrCostCalStatus()");
		String updateOrdAttrCostCalStatusQuery = asnOrderQuery
				.getUpdateOrdAttrCostCalStatusQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateOrdAttrCostCalStatus() query: "
				+ updateOrdAttrCostCalStatusQuery);

		try {
			int updStatusCount = jdbcTemplate.update(
					updateOrdAttrCostCalStatusQuery,
					new Object[] { sysOrderId });
			LOGGER.debug("OrdersDAOImpl updateOrdAttrCostCalStatus() updStatusCount: "
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (IncorrectResultSizeDataAccessException ex) {
			LOGGER.error(
					"IncorrectResultSizeDataAccessException occured in OrdersDAOImpl updateOrdAttrCostCalStatus()",
					ex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (DataAccessException daex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOrdAttrCostCalStatus()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOrdAttrCostCalStatus()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateOrdAttrCostCalStatus()");

		return updStatus;

	}

	@Override
	public boolean insertItemIntoOmOrderLine(Timestamp orderPlacedDttm,
			long sysOrderId, String pcpProductId, int iMemQty, int OrderedQty,
			Double calculatedPrice, Double discountAmount)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl insertItemIntoOmOrderLine()");
	/*	String machineEquipQuery = asnOrderQuery.getMachineEquipDetails()
				.toString();*/
		String insertItemIntoOmOrderLineQuery = asnOrderQuery
				.getInsertItemIntoOmOrderLineQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl insertItemIntoOmOrderLine() query: "
				+ insertItemIntoOmOrderLineQuery);

		Double unitPrice = 0.00;
		unitPrice = calculatedPrice / iMemQty;

		try {

		/*	List machineDetails = jdbcTemplate.queryForList(machineEquipQuery,
					new Object[] { pcpProductId });*/
			/*int sysMachineInstanceId = (Integer) machineDetails.get(0);
			int sysEquipInstanceId = (Integer) machineDetails.get(1);*/
			// Changes done for testing purpose hardcoded
			int updStatusCount = jdbcTemplate.update(
					insertItemIntoOmOrderLineQuery, new Object[] {
							orderPlacedDttm, sysOrderId,
							pcpProductId,
							0,
							0,
							unitPrice, iMemQty, OrderedQty, calculatedPrice,
							calculatedPrice, discountAmount, calculatedPrice,
							0, 0, 0, 0, CREATE_USER_ID, 
							CREATE_USER_ID});
			LOGGER.debug("OrdersDAOImpl insertItemIntoOmOrderLine() updStatusCount: "
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl insertItemIntoOmOrderLine()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl insertItemIntoOmOrderLine()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		} finally {
			LOGGER.debug("Exiting insertItemIntoOmOrderLine method of OrdersDAOImpl");
		}
		LOGGER.debug("Exiting OrdersDAOImpl insertItemIntoOmOrderLine()");
		return updStatus;
	}

	@Override
	public boolean updateOmOrderDetails(long sysOrderId,
			Double originalOrderPrice, Double totalOrderDiscount,
			int couponInd, Double loyaltyPrice, Double loyaltyDiscountAmount)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl updateOmOrderDetails()");
		String updateOmOrderDetailsQuery = asnOrderQuery
				.getupdateOmOrderDetailsQuery();
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateOmOrderDetails() query: "
				+ updateOmOrderDetailsQuery);
		try {
			int updStatusCount = jdbcTemplate.update(updateOmOrderDetailsQuery,
					new Object[] { originalOrderPrice, totalOrderDiscount,
							ACTIVE_IND_Y, originalOrderPrice,
							totalOrderDiscount, sysOrderId });
			LOGGER.debug("OrdersDAOImpl updateOmOrderDetails() updStatusCount: "
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderDetails()",
					daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmOrderDetails()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		} finally {
			LOGGER.debug("Exiting insertItemIntoOmOrderLine method of OrdersDAOImpl");
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateOmOrderDetails()");
		return updStatus;
	}

	/**
	 * @param orderNo
	 * @return
	 */

	@Override
	public int getExceptionId(int orderNo) {

		LOGGER.debug("Entering OrdersDAOImpl getExceptionId");
		String getExceptionIdQuery = asnOrderQuery.getExceptionIdQuery();
		LOGGER.debug("OrdersDAOImpl getExceptionId query:"
				+ getExceptionIdQuery);
		int excpId = 0;
		try {

			excpId = jdbcTemplate.queryForObject(getExceptionIdQuery,
					new Object[] { orderNo }, Integer.class);
			LOGGER.debug("OrdersDAOImpl getExceptionId excpId:" + excpId);

		} catch (Exception ex) {
			LOGGER.error("Exception occured in PosOrdersDaoImpl getExceptionId()");
		} finally {

		}

		LOGGER.debug("Exiting PosOrdersDAOImpl getExceptionId");
		return excpId;
	}

	/*----------------------------Unclaimed Envelope-------------------------*/
	/**
	 * This method get all the unclaimed envelopes data.
	 * @param reqBean contains request parameters.
	 * @return responseList.
	 * @throws PhotoOmniException custom exception.
	 */
	public List<UnclaimedEnvRespList> submitUnclaimedEnvRequest(UnclaimedEnvFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitUnclaimedEnvRequest method of OrdersDAOImpl ");
		}
		List<UnclaimedEnvRespList> responseList = null;
		try {
			String currentPage = reqBean.getCurrentPageNo();
			String sqlQuery = UnclaimedEnvQuery.selectUnclaimedEnvdataQry(reqBean).toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" SQL Query for Unclaimed Order : " + sqlQuery);
			}
			if (!CommonUtil.isNull(currentPage) && !"".equals(currentPage)) {
				/*This works for Pagination*/
				Map<String, Long> pageLimit = CommonUtil.getPaginationLimit(reqBean.getCurrentPageNo(), PhotoOmniConstants.UNCLAIMED_ENVELOPE_PAGINATION_SIZE);
				String startLimit = !CommonUtil.isNull(pageLimit.get("START_LIMIT")) ? pageLimit.get("START_LIMIT").toString() : "";
				String endLimit = !CommonUtil.isNull(pageLimit.get("END_LIMIT")) ? pageLimit.get("END_LIMIT").toString() : "";
				Object[] params = { reqBean.getStoreNumber(), startLimit, endLimit };
				responseList = dataGuardJdbcTemplate.query(sqlQuery, params, new UnclaimedEnvRowmapper());
			} else {
				/*This works for print where all data need to show*/
				Object[] params = { reqBean.getStoreNumber()};
				responseList = dataGuardJdbcTemplate.query(sqlQuery, params, new UnclaimedEnvRowmapper());
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrdersDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrdersDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitUnclaimedEnvRequest method of OrdersDAOImpl ");
			}
		}
		return responseList;
	}

	/**
	 * This method get last six month orders of selected customer.
	 * @param reqBean contains requested filter value.
	 * @return responseList.
	 * @throws PhotoOmniException custom exception.
	 */
	public List<UnclaimedEnvCustOrderRespBean> unclaimedEnvCustOrderRequest(UnclaimedEnvCustorderReqBean reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering unclaimedEnvCustOrderRequest method of OrdersDAOImpl ");
		}
		List<UnclaimedEnvCustOrderRespBean> responseList = null;
		String customerId = reqBean.getCustomerId();
		try {
			String sqlQuery = UnclaimedEnvQuery.selectUnclaimedEnvcustOrderQry().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query for Unclaimed envelope last Six month order by customer is : " + sqlQuery);
			}
			Object[] param = { customerId };
			responseList = dataGuardJdbcTemplate.query(sqlQuery, param, new UnclaimedEnvCustOrderRowmapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitUnclaimedEnvRequest method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at unclaimedEnvCustOrderRequest method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting unclaimedEnvCustOrderRequest method of OrdersDAOImpl ");
			}
		}
		return responseList;
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
	 * This method returns the store address
	 * @param locId contains store number.
	 * @return StoreAddress.
	 * @throws PhotoOmniException 
	 */
	public String getLocationAddress(String locId) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into getLocationAddress method of OrderDAOImpl ");
		}
		String StrStoreAddress = null;
		try {
			String sqlQuery = ReportsQuery.getLocationAddresQuery().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query to find the address of a store " + sqlQuery);
			}
			Object param[] = {locId};
			List<String> StoreAddress = this.dataGuardJdbcTemplate.queryForList(sqlQuery, param, String.class);
			if (!CommonUtil.isNull(StoreAddress) && StoreAddress.size() > 0) {
				StrStoreAddress = StoreAddress.get(0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getLocationAddress method of OrderDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getLocationAddress method of OrderDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from getLocationAddress method of OrderDAOImpl ");
			}
		}
		return StrStoreAddress;
	}

	@Override
	public OrderASNDetailsBean getOrderDetails(String orderNumber,
			int locationNumber) throws PhotoOmniException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param dataGuardJdbcTemplate
	 */
	public void setDataGuardJdbcTemplate(JdbcTemplate dataGuardJdbcTemplate) {
		this.dataGuardJdbcTemplate = dataGuardJdbcTemplate;
	}


	// Added for Insert into OM_SHIPMENT - Starts
	/**
	 * This method returns the boolean value from OM_SHIPMENT table 
	 * after updating OM_SHIPMENT STATUS
	 * @param orderASNDetailsBean 
	 * @return boolean.
	 * @throws PhotoOmniException 
	 */
	@Override
	@Transactional
	public boolean updateOmShipmentStatus(OrderASNDetailsBean orderASNDetailsBean)
			throws PhotoOmniException {

		LOGGER.debug("Entering OrdersDAOImpl checkupdateOmShipment()");
		
		String checkupdateOmShipmentQuery = asnOrderQuery.updateOmShipmentStatusQuery();
		
		boolean updStatus = false;
		LOGGER.debug("OrdersDAOImpl updateOmShipmentStatus() query: "+ asnOrderQuery.updateOmShipmentStatusQuery());
		try {
			
			LOGGER.debug("Entering into updateOmShipmentStatusQuery of AnsorderQuery");
			long sysOrderId = orderASNDetailsBean.getSysOrderId();
			LOGGER.debug("sysOrderId    " + sysOrderId);
			String status = orderASNDetailsBean.getStatus();
			LOGGER.debug("status    " + status);
			Timestamp orderDtm = orderASNDetailsBean.getOrderPlacedDttm();
			LOGGER.debug("Order Placed  DTM " + orderDtm);
			Timestamp updateDttm = new Timestamp(new Date().getTime());
			
			int updStatusCount = jdbcTemplate.update(checkupdateOmShipmentQuery,
					new Object[] { status,  UPDATE_USER_ID,
									updateDttm,sysOrderId ,orderDtm});
			
			LOGGER.debug("OrdersDAOImpl updateOmShipmentStatus() updStatusCount:"
					+ updStatusCount);
			if (updStatusCount > 0) {
				updStatus = true;
			}

		} catch (DataAccessException daex) {
			LOGGER.error("Exception occured in OrdersDAOImpl updateOmShipmentStatus()",daex);
			throw new DataIntegrityViolationException(RESPONSE_DB_ERROR_STRING);
		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured in OrdersDAOImpl updateOmShipmentStatus()",
					ex);
			throw new DataIntegrityViolationException(ex.getMessage());
		}
		LOGGER.debug("Exiting OrdersDAOImpl updateOmShipmentStatus()");
		return updStatus;
	}
	
	/**
	 * This method returns the Processing type value from OM_SHIPMENT table
	 * @param sysOrderId ,orderDTM
	 * @return Processing type.
	 * @throws PhotoOmniException 
	 */
	public String getProcessingType(long sysOrderId , Timestamp orderDTM ) throws PhotoOmniException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into getProcessingType method of OrdersDAOImpl ");
		}
		String strProcessingType = null;
		try {
			String sqlQuery =  asnOrderQuery.getProcessingType();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query to find the processing type " + sqlQuery);
			}
			Object param[] = {sysOrderId, orderDTM};
			strProcessingType = this.dataGuardJdbcTemplate.queryForObject(sqlQuery, param, String.class);
			
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getProcessingType method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getProcessingType method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from getProcessingType method of OrdersDAOImpl ");
			}
		}
		return strProcessingType;
	}
	
	
	/**
	 * This method is to check a record is present already in OM_SHIPMENT for update label
	 * @param sysOrderId , orderDTM
	 * @return boolean
	 * @throws PhotoOmniException 
	 */
	public boolean checkRecordPresent(long sysOrderId , Timestamp orderDTM ) throws PhotoOmniException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into checkRecordPresent method of OrdersDAOImpl ");
		}
		boolean isRecordPresent = false;
		String count = "";
		
		try {
			String sqlQuery =  asnOrderQuery.checkRecordPresent();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query to find the record is present " + sqlQuery);
			}
			Object param[] = {sysOrderId, orderDTM};
			count = this.dataGuardJdbcTemplate.queryForObject(sqlQuery, param, String.class);
			int recordPresentCount = Integer.parseInt(count);
			if(recordPresentCount>0){
				isRecordPresent = true;
			}
			
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at checkRecordPresent method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at checkRecordPresent method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from checkRecordPresent method of OrdersDAOImpl ");
			}
		}
		return isRecordPresent;
	}
	
	
	
	/**
	 * This method returns the Processing type value from OM_SHIPMENT table
	 * @param sysOrderId ,orderDTM
	 * @return Processing type.
	 * @throws PhotoOmniException 
	 */
	public String getEstDTM(long sysOrderId , int locationNumber ) throws PhotoOmniException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into getEstDTM method of OrdersDAOImpl ");
		}
		String strEstDTM = null;
		try {
			String sqlQuery =  asnOrderQuery.getEstDTM();
			
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query to find the processing type " + sqlQuery);
			}
			strEstDTM= jdbcTemplate.queryForObject(
					sqlQuery,
					new Object[] {sysOrderId, locationNumber}, String.class);
			
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getEstDTM method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getEstDTM method of OrdersDAOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from getEstDTM method of OrdersDAOImpl ");
			}
		}
		return strEstDTM;
	}
	// Added for Insert into OM_SHIPMENT - Ends

}
