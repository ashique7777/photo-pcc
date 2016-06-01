package com.walgreens.omni.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.security.oam.SAMLUserDetails;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.DateUtil;
import com.walgreens.omni.bean.CanisterChangeBean;
import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.bean.OmPriceLevelBean;
import com.walgreens.omni.bean.OmSimRetailBlockReportBean;
import com.walgreens.omni.bean.SilverCanisterDetailsBean;
import com.walgreens.omni.bean.SilverRecoveryHeaderDetails;
import com.walgreens.omni.exception.ReportException;
import com.walgreens.omni.json.bean.CannedFilter;
import com.walgreens.omni.json.bean.CannedReportResBean;
import com.walgreens.omni.json.bean.CannedReportResGenericBean;
import com.walgreens.omni.json.bean.InputChannel;
import com.walgreens.omni.json.bean.OrderType;
import com.walgreens.omni.json.bean.SilverCanisterDetails;
import com.walgreens.omni.json.bean.SilverCanisterReportRepList;
import com.walgreens.omni.json.bean.SilverCanisterReportRepMsg;
import com.walgreens.omni.json.bean.SilverCanisterReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStoreDetails;
import com.walgreens.omni.json.bean.SilverCanisterStoreReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepList;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepMsg;
import com.walgreens.omni.json.bean.SimRetailBlocOnloadList;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;
import com.walgreens.omni.rowmapper.CannedReportRowMapper;
import com.walgreens.omni.rowmapper.GenerateCannedReportGenericRowMapper;
import com.walgreens.omni.rowmapper.GenerateCannedReportRowMapper;
import com.walgreens.omni.rowmapper.InputChannelRowmapper;
import com.walgreens.omni.rowmapper.OmPriceLevelRowMapper;
import com.walgreens.omni.rowmapper.OmSimRetailBlockReportBeanRowMapper;
import com.walgreens.omni.rowmapper.SilverCanisterDetailsBeanRowMapper;
import com.walgreens.omni.rowmapper.SilverCanisterStroeReportRowMapper;
import com.walgreens.omni.rowmapper.SilverRecoveryDetailBeanRowMapper;
import com.walgreens.omni.rowmapper.SilverRecoveryHeaderDetailsRowMapper;
import com.walgreens.omni.utility.ReportsQuery;
import com.walgreens.omni.utility.SilverCanisterReportsQuery;
import com.walgreens.omni.utility.SimRetailBlockReportQuery;
import com.walgreens.omni.web.constants.ReportsConstant;

@Component("ReportDAO")
@Repository
@Transactional
public class ReportsDAOImpl implements ReportsDAO {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

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

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate dataGuardJdbcTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportsDAOImpl.class);

	/**
	 * 
	 */
	public final String BLANK = "";

	/**
	 * Method used to submit Silver Canister Report Request
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			SilverCanisterReportReqMsg reqParam, String reportReq)
			throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterReportRequest method of ReportsDAOImpl ");
		}

		SilverCanisterReportRepMsg silverCanisterReportRepMsg = new SilverCanisterReportRepMsg();
		List<SilverCanisterDetailsBean> silverCanisterDetailsList = new ArrayList<SilverCanisterDetailsBean>();
		@SuppressWarnings("rawtypes")
		List silverCanisterRepDetailsList = new ArrayList();
		Map<String, Long> pageNoMap = new HashMap<String, Long>();

		String locationNoForDistrictQuery = SilverCanisterReportsQuery
				.getLocationNoForDistrictQuery();
		String locationNoForRegionQuery = SilverCanisterReportsQuery
				.getLocationNoForRegionQuery();
		String locationNoForChainQuery = SilverCanisterReportsQuery
				.getLocationNoForChainQuery();
		String locationNoForStoreQuery = SilverCanisterReportsQuery
				.getLocationNoForStoreQuery();

		/** Build map for pagination */
		String pageNo = reqParam.getFilter().getPageNo();
		if (!CommonUtil.isNull(pageNo) && !"".equals(pageNo)) {
			CommonUtil commonUtil = new CommonUtil();
			pageNoMap = commonUtil.getPaginationLimit(reqParam.getFilter()
					.getPageNo().toString(),
					PhotoOmniConstants.SILVER_CANISTER_PAGINATION_SIZE);
		} else {
			pageNoMap = null;
		}

		try {
			/** Null checking for startdate and enddate ,number field */
			String startDate = null;
			String endDate = null;
			String number = null;

			try {
				if (CommonUtil.isNull(reqParam.getFilter().getStartDate())
						|| reqParam.getFilter().getStartDate()
								.equalsIgnoreCase("")) {
					startDate = PhotoOmniConstants.BLANK;
				} else {
					startDate = reqParam.getFilter().getStartDate();
				}
			} catch (Exception e) {
				startDate = PhotoOmniConstants.BLANK;
			}

			try {
				if (CommonUtil.isNull(reqParam.getFilter().getEndDate())
						|| reqParam.getFilter().getEndDate()
								.equalsIgnoreCase("")) {
					endDate = PhotoOmniConstants.BLANK;
				} else {
					endDate = reqParam.getFilter().getEndDate();
				}
			} catch (Exception e) {
				endDate = PhotoOmniConstants.BLANK;
			}

			try {
				if (CommonUtil.isNull(reqParam.getFilter().getNumber())
						|| reqParam.getFilter().getNumber()
								.equalsIgnoreCase("")) {
					number = PhotoOmniConstants.BLANK;
				} else {
					number = reqParam.getFilter().getNumber();
				}
			} catch (Exception e) {
				number = PhotoOmniConstants.BLANK;
			}

			List<Integer> locationIdList = new ArrayList<Integer>();

			/** check for blank request parameters */
			if (!startDate.equalsIgnoreCase(PhotoOmniConstants.BLANK)
					&& !endDate.equalsIgnoreCase(PhotoOmniConstants.BLANK)
					&& !reqParam.getFilter().getLocation()
							.equalsIgnoreCase(PhotoOmniConstants.BLANK)
					&& !reqParam.getFilter().getStatus()
							.equalsIgnoreCase(PhotoOmniConstants.BLANK)) {

				/** Assign respected order status */
				String status = PhotoOmniConstants.BLANK;
				if (reqParam.getFilter().getStatus()
						.equalsIgnoreCase("in progress")) {
					status = PhotoOmniConstants.ORDER_PROC;
				} else if (reqParam.getFilter().getStatus()
						.equalsIgnoreCase("completed")) {
					status = PhotoOmniConstants.ORDER_DONE;
				}

				/** Assign respected sort column */
				String sortColoumnTwo = PhotoOmniConstants.BLANK;
				String sortColumn = reqParam.getFilter().getSortColumnName();
				if (!CommonUtil.isNull(sortColumn)
						&& "LastCanisterChangeDate"
								.equalsIgnoreCase(sortColumn)) {
					sortColoumnTwo = "CANISTER_END_DTTM";
				} else if (!CommonUtil.isNull(sortColumn)
						&& "Store".equalsIgnoreCase(sortColumn)) {
					sortColoumnTwo = "LOCATION_NBR";
				} else if (!CommonUtil.isNull(sortColumn)
						&& "ID".equalsIgnoreCase(sortColumn)) {
					sortColoumnTwo = "LOCATION_NBR";
				} else {
					sortColoumnTwo = "LOCATION_NBR";
				}

				/** Null check for sortorder */
				String sortOrder = "ASC";
				if (!CommonUtil.isNull(reqParam.getFilter().getSortOrder())
						&& reqParam.getFilter().getSortOrder()
								.equalsIgnoreCase("ASC")) {
					sortOrder = "ASC";
				} else if (!CommonUtil.isNull(reqParam.getFilter()
						.getSortOrder())
						&& reqParam.getFilter().getSortOrder()
								.equalsIgnoreCase("DESC")) {
					sortOrder = "DESC";
				}

				/** check for location type */
				if (!number.equalsIgnoreCase(PhotoOmniConstants.BLANK)) {

					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("District")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForDistrictQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}
					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Region")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForRegionQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}
					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Chain")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForChainQuery, Integer.class);
					}
					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Store")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForStoreQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}
				} else {

					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Chain")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForChainQuery, Integer.class);
					}
				}

				/** Preparing report queries */
				String silverCanisterReportQueryTwo = SilverCanisterReportsQuery
						.getSilverCanisterReportQueryTwo(locationIdList,
								sortColoumnTwo, sortOrder);
				String silverCanisterReportQueryOne = SilverCanisterReportsQuery
						.getSilverCanisterReportQueryOne(locationIdList,
								sortColoumnTwo, sortOrder);

				/**
				 * check if the request is for Report generation or report
				 * download
				 */
				if (reportReq.equalsIgnoreCase("ReportReq")) {

					/** if pagination map is not null */
					if (pageNoMap != null) {
						silverCanisterDetailsList = dataGuardJdbcTemplate.query(
								silverCanisterReportQueryTwo,
								new SilverCanisterDetailsBeanRowMapper(),
								new Object[] { startDate, endDate, status,
										pageNoMap.get("START_LIMIT"),
										pageNoMap.get("END_LIMIT") });

						silverCanisterRepDetailsList
								.add(silverCanisterDetailsList);

					} else {
						silverCanisterDetailsList = dataGuardJdbcTemplate.query(
								silverCanisterReportQueryOne,
								new SilverCanisterDetailsBeanRowMapper(),
								new Object[] { startDate, endDate, status });

						silverCanisterRepDetailsList
								.add(silverCanisterDetailsList);
					}

				} else {

					silverCanisterDetailsList = dataGuardJdbcTemplate.query(
							silverCanisterReportQueryOne,
							new SilverCanisterDetailsBeanRowMapper(),
							new Object[] { startDate, endDate, status });

					silverCanisterRepDetailsList.add(silverCanisterDetailsList);

				}

			}
			/**
			 * chech for blank Start date and End date OR check for blank
			 * request parameters
			 */
			else if (startDate.equalsIgnoreCase(PhotoOmniConstants.BLANK)
					&& endDate.equalsIgnoreCase(PhotoOmniConstants.BLANK)
					&& !reqParam.getFilter().getLocation()
							.equalsIgnoreCase(PhotoOmniConstants.BLANK)) {

				/** Assign respected order status */
				String status = PhotoOmniConstants.BLANK;
				if (reqParam.getFilter().getStatus()
						.equalsIgnoreCase("in progress")) {
					status = PhotoOmniConstants.ORDER_PROC;
				} else if (reqParam.getFilter().getStatus()
						.equalsIgnoreCase("completed")) {
					status = PhotoOmniConstants.ORDER_DONE;
				}

				/** Assign respected sort column */
				String sortColoumnTwo = PhotoOmniConstants.BLANK;
				if (!CommonUtil
						.isNull(reqParam.getFilter().getSortColumnName())
						&& reqParam.getFilter().getSortColumnName()
								.equalsIgnoreCase("LastCanisterChangeDate")) {
					sortColoumnTwo = "CANISTER_END_DTTM";
				} else if (!CommonUtil.isNull(reqParam.getFilter()
						.getSortColumnName())
						&& reqParam.getFilter().getSortColumnName()
								.equalsIgnoreCase("Store")) {
					sortColoumnTwo = "LOCATION_NBR";
				} else if (!CommonUtil.isNull(reqParam.getFilter()
						.getSortColumnName())
						&& reqParam.getFilter().getSortColumnName()
								.equalsIgnoreCase("ID")) {
					sortColoumnTwo = "LOCATION_NBR";
				} else {
					sortColoumnTwo = "LOCATION_NBR";
				}

				/** Null check for sortorder */
				String sortOrder = "ASC";
				if (!CommonUtil.isNull(reqParam.getFilter().getSortOrder())
						&& reqParam.getFilter().getSortOrder()
								.equalsIgnoreCase("ASC")) {
					sortOrder = "ASC";
				} else if (!CommonUtil.isNull(reqParam.getFilter()
						.getSortOrder())
						&& reqParam.getFilter().getSortOrder()
								.equalsIgnoreCase("DESC")) {
					sortOrder = "DESC";
				}

				/** check for location type */
				if (!number.equalsIgnoreCase(PhotoOmniConstants.BLANK)) {

					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("District")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForDistrictQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}
					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Store")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForStoreQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}

					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Region")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForRegionQuery,
								new Object[] { Integer.parseInt(number) },
								Integer.class);
					}
					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Chain")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForChainQuery, Integer.class);
					}

				} else {

					if (reqParam.getFilter().getLocation()
							.equalsIgnoreCase("Chain")) {
						locationIdList = dataGuardJdbcTemplate.queryForList(
								locationNoForChainQuery, Integer.class);
					}
				}

				/** Preparing report queries */
				String silverCanisterReportQueryThree = SilverCanisterReportsQuery
						.getSilverCanisterReportQueryThree(locationIdList,
								sortColoumnTwo, sortOrder, status);
				String silverCanisterReportQueryFour = SilverCanisterReportsQuery
						.getSilverCanisterReportsQueryFour(locationIdList,
								sortColoumnTwo, sortOrder, status);

				/**
				 * check if the request is for Report generation or report
				 * download
				 */
				if (reportReq.equalsIgnoreCase("ReportReq")) {

					/** if pagination map is not null */
					if (pageNoMap != null) {
						silverCanisterDetailsList = dataGuardJdbcTemplate.query(
								silverCanisterReportQueryThree,
								new SilverCanisterDetailsBeanRowMapper(),
								new Object[] { status,
										pageNoMap.get("START_LIMIT"),
										pageNoMap.get("END_LIMIT") });

						silverCanisterRepDetailsList
								.add(silverCanisterDetailsList);

					} else {
						silverCanisterDetailsList = dataGuardJdbcTemplate.query(
								silverCanisterReportQueryFour,
								new SilverCanisterDetailsBeanRowMapper(),
								new Object[] { status });

						silverCanisterRepDetailsList
								.add(silverCanisterDetailsList);
					}

				} else {

					silverCanisterDetailsList = dataGuardJdbcTemplate.query(
							silverCanisterReportQueryFour,
							new SilverCanisterDetailsBeanRowMapper(),
							new Object[] { status });

					silverCanisterRepDetailsList.add(silverCanisterDetailsList);
				}

			}

			/** holds total number of record fetched */
			String totalRow = PhotoOmniConstants.BLANK;

			List<SilverCanisterReportRepList> silverCanisterReportRepList = new ArrayList<SilverCanisterReportRepList>();

			/** Iterates and populates silverCanisterReportRepList */
			for (int i = 0; i < silverCanisterRepDetailsList.size(); i++) {

				List<SilverCanisterDetailsBean> silverCanisterDetailsListOne = new ArrayList<SilverCanisterDetailsBean>();
				silverCanisterDetailsListOne = (List<SilverCanisterDetailsBean>) silverCanisterRepDetailsList
						.get(i);
				totalRow = String.valueOf(silverCanisterDetailsListOne.get(i)
						.getTotalRowCnt());

				for (int j = 0; j < silverCanisterDetailsListOne.size(); j++) {

					SilverCanisterReportRepList silverCanisterReportRepList1 = new SilverCanisterReportRepList();
					SilverCanisterDetails silverCanisterDetails = new SilverCanisterDetails();

					silverCanisterDetails
							.setLastCanisterChangeDate(silverCanisterDetailsListOne
									.get(j).getLastCanisterChangeDate()
									.toString());
					silverCanisterDetails.setPaperSquereInch(String
							.valueOf(silverCanisterDetailsListOne.get(j)
									.getPaperSquereInch()));
					silverCanisterDetails.setPrintsCount(String
							.valueOf(silverCanisterDetailsListOne.get(j)
									.getPrintsCount()));
					silverCanisterDetails.setRollsCount(String
							.valueOf(silverCanisterDetailsListOne.get(j)
									.getRollsCount()));
					silverCanisterDetails.setSilverContentPrints(CommonUtil
							.formatDoubleToString(silverCanisterDetailsListOne
									.get(j).getSilverContentPrints()));
					silverCanisterDetails.setSilverContentRolls(CommonUtil
							.formatDoubleToString(silverCanisterDetailsListOne
									.get(j).getSilverContentRolls()));
					silverCanisterDetails.setStore(String
							.valueOf(silverCanisterDetailsListOne.get(j)
									.getStore()));
					silverCanisterDetails.setTotalSilver(String
							.valueOf(silverCanisterDetailsListOne.get(j)
									.getSilverContentPrints()));

					silverCanisterReportRepList1
							.setSilverCanisterDetails(silverCanisterDetails);
					silverCanisterReportRepList
							.add(silverCanisterReportRepList1);

				}

			}

			/** Set response message parameters */
			silverCanisterReportRepMsg
					.setSilverCanisterReportRepList(silverCanisterReportRepList);
			silverCanisterReportRepMsg.setCurrentPage(reqParam.getFilter()
					.getPageNo());
			silverCanisterReportRepMsg.setTotalRecords(totalRow);

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at submitSilverCanisterReportRequest method of ReportsDAOImpl - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSilverCanisterReportRequest method of ReportsDAOImpl ");
			}
		}
		return silverCanisterReportRepMsg;
	}

	/**
	 * Method to get Silver Recovery Header Details
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SilverRecoveryHeaderDetails> getSilverRecoveryHeaderDetails(
			String storeNum) throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterReportRequest method of ReportsDAOImpl ");
		}

		/** set order status to PROC */
		String status = PhotoOmniConstants.ORDER_PROC;

		String silverRecoveryHeaderDetailsQuery = SilverCanisterReportsQuery
				.getSilverRecoveryHeaderDetailsQuery();
		List<SilverRecoveryHeaderDetails> silverRecoveryHeaderDetailsList = new ArrayList<SilverRecoveryHeaderDetails>();

		silverRecoveryHeaderDetailsList = jdbcTemplate.query(
				silverRecoveryHeaderDetailsQuery,
				new Object[] { Integer.parseInt(storeNum), status },
				new SilverRecoveryHeaderDetailsRowMapper());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting submitSilverCanisterReportRequest method of ReportsDAOImpl ");
		}

		return silverRecoveryHeaderDetailsList;
	}

	/**
	 * Method used to update Silver Recovery Header table
	 */
	@Override
	@Transactional
	public boolean updateSilverRecHead(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempOne)
			throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateSilverRecHead method of ReportsDAOImpl ");
		}
		boolean updFlag = false;
		String updateSilverRecHeadQuery = SilverCanisterReportsQuery
				.getUpdateSilverRecHeadQuery();

		int updCnt = jdbcTemplate
				.update(updateSilverRecHeadQuery,
						new Object[] {
								silverRecoveryHeaderDetailsTempOne
										.getCanisterEndDttm(),
								silverRecoveryHeaderDetailsTempOne
										.getCanisterStatus(),
								silverRecoveryHeaderDetailsTempOne
										.getSilverRecvRolls(),
								silverRecoveryHeaderDetailsTempOne
										.getSilverRecvPrints(),
								silverRecoveryHeaderDetailsTempOne
										.getUpdateUserId(),
								silverRecoveryHeaderDetailsTempOne
										.getRollsCount(),
								silverRecoveryHeaderDetailsTempOne
										.getPrintsCount(),
								silverRecoveryHeaderDetailsTempOne
										.getPrintInSqInch(),
								silverRecoveryHeaderDetailsTempOne
										.getSilverCompany(),

								silverRecoveryHeaderDetailsTempOne
										.getSysLocationId(),
								silverRecoveryHeaderDetailsTempOne
										.getCanisterPrevStatus() });

		if (updCnt > 0) {
			updFlag = true;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting updateSilverRecHead method of ReportsDAOImpl ");
		}
		return updFlag;
	}

	/**
	 * Method used to insert record in Silver Recovery Header table
	 */
	@Override
	@Transactional
	public boolean insertSilverRecHead(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempTwo)
			throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering insertSilverRecHead method of ReportsDAOImpl ");
		}

		boolean updFlag = false;
		String insertSilverRecHeadQuery = SilverCanisterReportsQuery
				.getInsertSilverRecHeadQuery();

		int updCnt = jdbcTemplate
				.update(insertSilverRecHeadQuery,
						new Object[] {
								silverRecoveryHeaderDetailsTempTwo
										.getSysLocationId(),
								silverRecoveryHeaderDetailsTempTwo
										.getCanisterStartDttm(),
								silverRecoveryHeaderDetailsTempTwo
										.getCanisterEndDttm(),
								silverRecoveryHeaderDetailsTempTwo
										.getCanisterStatus(),
								silverRecoveryHeaderDetailsTempTwo
										.getSilverCompany(),
								silverRecoveryHeaderDetailsTempTwo
										.getRollsCount(),
								silverRecoveryHeaderDetailsTempTwo
										.getPrintsCount(),
								silverRecoveryHeaderDetailsTempTwo
										.getPrintInSqInch(),
								silverRecoveryHeaderDetailsTempTwo
										.getSilverRecvRolls(),
								silverRecoveryHeaderDetailsTempTwo
										.getSilverRecvPrints(),
								silverRecoveryHeaderDetailsTempTwo
										.getCreateUserId(),
								silverRecoveryHeaderDetailsTempTwo
										.getUpdateUserId() });
		if (updCnt > 0) {
			updFlag = true;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting insertSilverRecHead method of ReportsDAOImpl ");
		}
		return updFlag;
	}

	/**
	 * Method used to insert Canister Change Details
	 * 
	 */
	@Override
	@Transactional
	public boolean insertCanisterChange(CanisterChangeBean canisterChangeBean)
			throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering insertCanisterChange method of ReportsDAOImpl ");
		}
		boolean insrtFlag = false;
		String insertCanisterChangeQuery = SilverCanisterReportsQuery
				.getCanisterChangeQuery();

		int instCnt = jdbcTemplate.update(
				insertCanisterChangeQuery,
				new Object[] { canisterChangeBean.getSysLocationId(),
						canisterChangeBean.getCanisterStartDttm(),
						canisterChangeBean.getCanisterEndDttm(),
						canisterChangeBean.getCanisterCd(),
						canisterChangeBean.getServiceDescription(),
						canisterChangeBean.getCreateUserId(),
						canisterChangeBean.getUpdateUserId(), });
		if (instCnt > 0) {
			insrtFlag = true;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting insertCanisterChange method of ReportsDAOImpl ");
		}
		return insrtFlag;
	}

	/**
	 * Method used to get SilverRecvRolls From Canister Detail
	 */
	@Override
	public double getSilverRecvRollsFromCanisterDetail(String startDate,
			String endDate, int sysLocationId) throws ReportException,
			PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSilverRecvRollsFromCanisterDetail method of ReportsDAOImpl ");
		}
		String silverRecvRollsOmSilverRecvDetailQuery = SilverCanisterReportsQuery
				.getSilverRecvRollsOmSilverRecvDetailQuery();
		double silverRecvRolls = 0.0000;
		silverRecvRolls = jdbcTemplate.queryForObject(
				silverRecvRollsOmSilverRecvDetailQuery, new Object[] {
						sysLocationId, startDate, endDate }, Double.class);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting getSilverRecvRollsFromCanisterDetail method of ReportsDAOImpl ");
		}
		return silverRecvRolls;
	}

	/**
	 * Method used to get SilverRecvPrints From Canister Detail
	 * 
	 */
	@Override
	public double getSilverRecvPrintsFromCanisterDetail(String startDate,
			String endDate, int sysLocationId) throws ReportException,
			PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSilverRecvPrintsFromCanisterDetail method of ReportsDAOImpl ");
		}
		String silverRecvPrintsOmSilverRecvDetailQuery = SilverCanisterReportsQuery
				.getSilverRecvPrintsOmSilverRecvDetailQuery();
		double silverRecvPrints = 0.0000;
		silverRecvPrints = jdbcTemplate.queryForObject(
				silverRecvPrintsOmSilverRecvDetailQuery, new Object[] {
						sysLocationId, startDate, endDate }, Double.class);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting getSilverRecvPrintsFromCanisterDetail method of ReportsDAOImpl ");
		}
		return silverRecvPrints;
	}

	/**
	 * Method used to update SilverRecoveryCd
	 * 
	 * @param statusInd
	 * 
	 */
	@Override
	public void updateSilverRecoveryCd(String canisterChandeDate,
			String statusInd) throws ReportException, PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateSilverRecoveryCd method of ReportsDAOImpl ");
		}
		String updateSilverRecoveryCdQuery = SilverCanisterReportsQuery
				.getupdateSilverRecoveryCdQuery();
		jdbcTemplate.update(updateSilverRecoveryCdQuery, new Object[] {
				statusInd, canisterChandeDate });

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting updateSilverRecoveryCd method of ReportsDAOImpl ");
		}
	}

	/**
	 * Method used to submit Silver Canister Store Report Request
	 * 
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			SilverCanisterStoreReportReqMsg reqParam) throws ReportException,
			PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterStoreReportRequest method of ReportsDAOImpl ");
		}

		SilverCanisterStroeReportRepMsg silverCanisterStroeReportRepMsg = new SilverCanisterStroeReportRepMsg();
		Map<String, Long> pageNoMap = new HashMap<String, Long>();
		List<SilverCanisterStoreDetails> silverCanisterStoreDetailsList = new ArrayList<SilverCanisterStoreDetails>();

		/** Build map for pagination */
		if (!CommonUtil.isNull(reqParam.getFilter().toString())
				&& reqParam.getFilter().toString() != "") {
			pageNoMap = CommonUtil.getPaginationLimit(reqParam.getFilter()
					.getCurrentPage().toString(),
					PhotoOmniConstants.SILVER_CANISTER_PAGINATION_SIZE);
		} else {
			pageNoMap = null;
		}

		/** Assign respected sort column */
		String sortColoumnOne = PhotoOmniConstants.BLANK;
		if (!CommonUtil.isNull(reqParam.getFilter().getSortColumnName())
				&& reqParam.getFilter().getSortColumnName()
						.equalsIgnoreCase("lastCanisterChangeDate")) {
			sortColoumnOne = "CANISTER_END_DTTM";
		} else {
			sortColoumnOne = "SERVICE_DESCRIPTION";
		}

		/** Null check for sortorder */
		String sortOrder = "ASC";
		if (!CommonUtil.isNull(reqParam.getFilter().getSortOrder())
				&& reqParam.getFilter().getSortOrder().equalsIgnoreCase("ASC")) {
			sortOrder = "ASC";
		} else if (!CommonUtil.isNull(reqParam.getFilter().getSortOrder())
				&& reqParam.getFilter().getSortOrder().equalsIgnoreCase("DESC")) {
			sortOrder = "DESC";
		}

		String silverCanisterStoreReportQuery = SilverCanisterReportsQuery
				.getSilverCanisterStoreReport(sortColoumnOne, sortOrder);

		/**
		 * Status marked as DONE as Store reports will contain only DONE status
		 * records
		 */
		String status = PhotoOmniConstants.ORDER_DONE;

		String totalRowCount = PhotoOmniConstants.BLANK;

		silverCanisterStoreDetailsList = jdbcTemplate.query(
				silverCanisterStoreReportQuery,
				new Object[] {
						Integer.parseInt(reqParam.getFilter().getLocation()),
						Integer.parseInt(reqParam.getFilter().getLocation()),
						reqParam.getFilter().getStartDate(),
						reqParam.getFilter().getEndDate(),
						reqParam.getFilter().getStartDate(),
						reqParam.getFilter().getEndDate(), status,
						pageNoMap.get("START_LIMIT"),
						pageNoMap.get("END_LIMIT") },
				new SilverCanisterStroeReportRowMapper());

		List<SilverCanisterStroeReportRepList> silverCanisterStroeReportRepList = new ArrayList<SilverCanisterStroeReportRepList>();

		/** Iterates and populates silverCanisterStroeReportRepList */
		for (int i = 0; i < silverCanisterStoreDetailsList.size(); i++) {

			totalRowCount = silverCanisterStoreDetailsList.get(i)
					.getTotalRowCount();

			SilverCanisterStoreDetails silverCanisterStoreDetails = new SilverCanisterStoreDetails();
			SilverCanisterStroeReportRepList silverCanisterStroeReportRepListObj = new SilverCanisterStroeReportRepList();
			silverCanisterStoreDetails
					.setLastCanisterChangeDate(silverCanisterStoreDetailsList
							.get(i).getLastCanisterChangeDate());
			silverCanisterStoreDetails
					.setServiceDescription(silverCanisterStoreDetailsList
							.get(i).getServiceDescription());
			silverCanisterStoreDetails
					.setSilverCompany(silverCanisterStoreDetailsList.get(i)
							.getSilverCompany());
			silverCanisterStoreDetails
					.setStoreAddress(silverCanisterStoreDetailsList.get(i)
							.getStoreAddress());
			silverCanisterStroeReportRepListObj
					.setSilverCanisterStoreDetails(silverCanisterStoreDetails);

			silverCanisterStroeReportRepList
					.add(silverCanisterStroeReportRepListObj);
		}

		/** Set response message parameters */
		silverCanisterStroeReportRepMsg
				.setSilverCanisterStroeReportRepList(silverCanisterStroeReportRepList);
		silverCanisterStroeReportRepMsg.setCurrentPage(reqParam.getFilter()
				.getCurrentPage());
		silverCanisterStroeReportRepMsg.setTotalRecords(totalRowCount);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting submitSilverCanisterStoreReportRequest method of ReportsDAOImpl ");
		}

		return silverCanisterStroeReportRepMsg;
	}

	/** Process process Cannister Upload Request */

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	@SuppressWarnings({ "deprecation" })
	public boolean processCanisterUploadRequest(
			SilverRecoveryHeaderDetails silverRecoveryHeaderDetails,
			Date dateSelectedFrom, String vendorName, String[] serDescArr,
			Date dateNextDate) throws ReportException, PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering processCanisterUploadRequest method of ReportsDAOImpl ");
		}

		boolean updateCanisterFlag = false;
		boolean insertCanisterFlag = false;
		boolean insertCanisterServiceFlag = false;
		boolean uploadStatus = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				ReportsConstant.DATE_FORMAT_SILVER_CANISTER_ONE);
		String tempCanisterStartDttm = DateUtil
				.getUSDateFormat(silverRecoveryHeaderDetails
						.getCanisterStartDttm().getTime());
		String tempCanisterEndDttm = DateUtil
				.getUSDateFormat(silverRecoveryHeaderDetails
						.getCanisterEndDttm().getTime());
		Date canisterStartDttm = new Date(tempCanisterStartDttm);
		Date canisterEndDttm = new Date(tempCanisterEndDttm);

		String omSilverRecvDetailQuery = SilverCanisterReportsQuery
				.getOmSilverRecvDetailQuery();

		/**
		 * compare if the canister change date is before CanisterEndDttm and
		 * after CanisterStartDttm
		 */
		if (dateSelectedFrom.before(canisterEndDttm)
				|| dateSelectedFrom.compareTo(canisterEndDttm) == 0
				&& dateSelectedFrom.after(canisterStartDttm)) {

			SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempOne = new SilverRecoveryHeaderDetails();
			silverRecoveryHeaderDetailsTempOne
					.setCanisterStartDttm(silverRecoveryHeaderDetails
							.getCanisterStartDttm());
			silverRecoveryHeaderDetailsTempOne
					.setCanisterEndDttm(new Timestamp(dateSelectedFrom
							.getTime()));
			silverRecoveryHeaderDetailsTempOne
					.setCanisterStatus(PhotoOmniConstants.DONE);
			silverRecoveryHeaderDetailsTempOne
					.setSysLocationId(silverRecoveryHeaderDetails
							.getSysLocationId());
			/**
			 * get details from OM_SILVER_RECOVERY_DETAIL with silver_calc_date
			 * and store no
			 */
			Date canisterStartDate = new Date(silverRecoveryHeaderDetails
					.getCanisterStartDttm().getTime());
			List<SilverCanisterDetailsBean> silverCanisterDetailsBeanDoneList = new ArrayList<SilverCanisterDetailsBean>();
			silverCanisterDetailsBeanDoneList = jdbcTemplate.query(
					omSilverRecvDetailQuery,
					new Object[] { dateFormat.format(canisterStartDate),
							dateFormat.format(dateSelectedFrom),
							silverRecoveryHeaderDetails.getSysLocationId() },
					new SilverRecoveryDetailBeanRowMapper());

			/**
			 * get SilverRecvRolls from OM_SILVER_RECOVERY_DETAIL with
			 * silver_calc_date and store no
			 */
			// Date canisterStartDate = new
			// Date(silverRecoveryHeaderDetails.getCanisterStartDttm().getTime());
			// double tempSilverRecvRolls =
			// getSilverRecvRollsFromCanisterDetail(dateFormat.format(canisterStartDate),
			// dateFormat.format(dateSelectedFrom),silverRecoveryHeaderDetails.getSysLocationId());
			/***
			 * get SilverRecvPrints from OM_SILVER_RECOVERY_DETAIL with
			 * silver_calc_date and store no
			 */
			// double tempSilverRecvPrints =
			// getSilverRecvPrintsFromCanisterDetail(dateFormat.format(canisterStartDate),
			// dateFormat.format(dateSelectedFrom),silverRecoveryHeaderDetails.getSysLocationId());
			if (silverCanisterDetailsBeanDoneList.size() > 0) {
				silverRecoveryHeaderDetailsTempOne
						.setSilverRecvPrints(silverCanisterDetailsBeanDoneList
								.get(0).getSilverContentPrints());
				silverRecoveryHeaderDetailsTempOne
						.setSilverRecvRolls(silverCanisterDetailsBeanDoneList
								.get(0).getSilverContentRolls());
				silverRecoveryHeaderDetailsTempOne
						.setPrintsCount(silverCanisterDetailsBeanDoneList
								.get(0).getPrintsCount());
				silverRecoveryHeaderDetailsTempOne
						.setRollsCount(silverCanisterDetailsBeanDoneList.get(0)
								.getRollsCount());
				silverRecoveryHeaderDetailsTempOne
						.setPrintInSqInch(silverCanisterDetailsBeanDoneList
								.get(0).getPaperSquereInch());
			}
			silverRecoveryHeaderDetailsTempOne.setUpdateUserId(String
					.valueOf(this.getSysUserId()));
			silverRecoveryHeaderDetailsTempOne.setSilverCompany(vendorName);
			silverRecoveryHeaderDetailsTempOne
					.setCanisterPrevStatus(silverRecoveryHeaderDetails
							.getCanisterStatus());
			/** update records in silverRecoveryHeader table */
			updateCanisterFlag = updateSilverRecHead(silverRecoveryHeaderDetailsTempOne);

			if (!(dateSelectedFrom.compareTo(canisterEndDttm) == 0)) {
				SilverRecoveryHeaderDetails silverRecoveryHeaderDetailsTempTwo = new SilverRecoveryHeaderDetails();
				silverRecoveryHeaderDetailsTempTwo
						.setCanisterStartDttm(new Timestamp(dateNextDate
								.getTime()));
				silverRecoveryHeaderDetailsTempTwo
						.setCanisterEndDttm(silverRecoveryHeaderDetails
								.getCanisterEndDttm());
				silverRecoveryHeaderDetailsTempTwo
						.setCanisterStatus(PhotoOmniConstants.PROC);
				silverRecoveryHeaderDetailsTempTwo
						.setSysLocationId(silverRecoveryHeaderDetails
								.getSysLocationId());
				/**
				 * get details from OM_SILVER_RECOVERY_DETAIL with
				 * silver_calc_date and store no
				 */
				Date canisterEndDate = new Date(silverRecoveryHeaderDetails
						.getCanisterEndDttm().getTime());
				List<SilverCanisterDetailsBean> silverCanisterDetailsBeanProcList = new ArrayList<SilverCanisterDetailsBean>();
				silverCanisterDetailsBeanProcList = jdbcTemplate
						.query(omSilverRecvDetailQuery,
								new Object[] {
										dateFormat.format(dateNextDate),
										dateFormat.format(canisterEndDate),
										silverRecoveryHeaderDetails
												.getSysLocationId() },
								new SilverRecoveryDetailBeanRowMapper());
				/** calculate and insert SilverRecvPrints for PROC status */
				// Date canisterEndDate = new
				// Date(silverRecoveryHeaderDetails.getCanisterEndDttm().getTime());
				// double tempSilverRecvPrintsOne =
				// (silverRecoveryHeaderDetails.getSilverRecvPrints() -
				// tempSilverRecvPrints);
				// double tempSilverRecvPrintsOne =
				// getSilverRecvPrintsFromCanisterDetail(dateFormat.format(dateNextDate),
				// dateFormat.format(canisterEndDate),silverRecoveryHeaderDetails.getSysLocationId());
				/** calculate and insert SilverRecvRolls for PROC status */
				// double tempSilverRecvRollsOne =
				// (silverRecoveryHeaderDetails.getSilverRecvRolls() -
				// tempSilverRecvRolls);
				// double tempSilverRecvRollsOne =
				// getSilverRecvRollsFromCanisterDetail(dateFormat.format(dateNextDate),
				// dateFormat.format(canisterEndDate),silverRecoveryHeaderDetails.getSysLocationId());
				if (silverCanisterDetailsBeanProcList.size() > 0) {
					silverRecoveryHeaderDetailsTempTwo
							.setSilverRecvPrints(silverCanisterDetailsBeanProcList
									.get(0).getSilverContentPrints());
					silverRecoveryHeaderDetailsTempTwo
							.setSilverRecvRolls(silverCanisterDetailsBeanProcList
									.get(0).getSilverContentRolls());
					silverRecoveryHeaderDetailsTempTwo
							.setPrintsCount(silverCanisterDetailsBeanProcList
									.get(0).getPrintsCount());
					silverRecoveryHeaderDetailsTempTwo
							.setRollsCount(silverCanisterDetailsBeanProcList
									.get(0).getRollsCount());
					silverRecoveryHeaderDetailsTempTwo
							.setPrintInSqInch(silverCanisterDetailsBeanProcList
									.get(0).getPaperSquereInch());
				}
				silverRecoveryHeaderDetailsTempTwo
						.setSilverCompany(silverRecoveryHeaderDetails
								.getSilverCompany());
				silverRecoveryHeaderDetailsTempTwo.setCreateUserId(String
						.valueOf(this.getSysUserId()));
				silverRecoveryHeaderDetailsTempTwo.setUpdateUserId(String
						.valueOf(this.getSysUserId()));
				/** insert records in silver recovery header table */
				insertCanisterFlag = insertSilverRecHead(silverRecoveryHeaderDetailsTempTwo);
			}

			/** Update SilverRecoveryCd in OM_ORDER_ATTRIBUTE */
			updateSilverRecoveryCd(dateFormat.format(dateSelectedFrom),
					ReportsConstant.NUMBER_ONE);

			for (int j = 0; j < serDescArr.length; j++) {

				CanisterChangeBean canisterChangeBean = new CanisterChangeBean();
				canisterChangeBean.setCanisterCd(j + 1);
				canisterChangeBean
						.setCanisterStartDttm(silverRecoveryHeaderDetails
								.getCanisterStartDttm());
				canisterChangeBean.setCanisterEndDttm(new Timestamp(
						dateSelectedFrom.getTime()));
				canisterChangeBean.setServiceDescription(serDescArr[j]);
				canisterChangeBean.setSysLocationId(silverRecoveryHeaderDetails
						.getSysLocationId());
				canisterChangeBean.setCreateUserId(String.valueOf(this
						.getSysUserId()));
				canisterChangeBean.setUpdateUserId(String.valueOf(this
						.getSysUserId()));
				/** insert records in cannister service table */
				insertCanisterServiceFlag = insertCanisterChange(canisterChangeBean);
			}
		}
		/***
		 * if any of the insert or update fails add records in invalid record
		 * map
		 */
		if (updateCanisterFlag && insertCanisterFlag
				&& insertCanisterServiceFlag) {
			uploadStatus = true;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting processCanisterUploadRequest method of ReportsDAOImpl ");
		}
		return uploadStatus;
	}

	/**
	 * This method find the sys_user_id by oamid.
	 * 
	 * @return sysUserId.
	 * @throws PhotoOmniException
	 *             custom exception.
	 */
	@SuppressWarnings("deprecation")
	public int getSysUserId() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSysUserId method of ReportsDAOImpl ");
		}
		int sysUserId = 0;
		try {
			ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();
			if (!CommonUtil.isNull(authentication)) {
				String empId = ((SAMLUserDetails) authentication.getDetails())
						.getEmployeenumber();
				String sqlQuery = SilverCanisterReportsQuery
						.getSysUserIdByOamId().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Get SYS_USER_ID by OAM id : " + sqlQuery);
				}
				Object[] params = { empId };
				sysUserId = jdbcTemplate.queryForInt(sqlQuery, params);
			}
		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at getSysUserId method of PrintSignReportFilterDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at getSysUserId method of PrintSignReportFilterDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSysUserId method of ReportsDAOImpl ");
			}
		}
		return sysUserId;
	}

	@Override
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSimRetailBlockOnloadRequest method of ReportsDAOImpl ");
		}

		String simRetailBlockOnloadRespQuery = SimRetailBlockReportQuery
				.getSimRetailBlockOnloadRespQuery();
		SimRetailBlockOnloadResp simRetailBlockOnloadResp = new SimRetailBlockOnloadResp();
		List<OmPriceLevelBean> omPriceLevelBeanList = new ArrayList<OmPriceLevelBean>();
		List<SimRetailBlocOnloadList> simRetailBlocOnloadList = new ArrayList<SimRetailBlocOnloadList>();
		try {

			omPriceLevelBeanList = jdbcTemplate.query(
					simRetailBlockOnloadRespQuery, new OmPriceLevelRowMapper());
			for (int i = 0; i < omPriceLevelBeanList.size(); i++) {
				SimRetailBlocOnloadList simRetailBlocOnloadBeanList = new SimRetailBlocOnloadList();
				simRetailBlocOnloadBeanList.setRetailBlock(omPriceLevelBeanList
						.get(i).getDescription());
				simRetailBlocOnloadBeanList.setPriceLevel(omPriceLevelBeanList
						.get(i).getPriceLevel());
				simRetailBlocOnloadList.add(simRetailBlocOnloadBeanList);
			}
			simRetailBlockOnloadResp
					.setSimRetailBlocOnloadList(simRetailBlocOnloadList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitSimRetailBlockOnloadRequest method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSimRetailBlockOnloadRequest method of ReportsDAOImpl ");
			}
		}
		return simRetailBlockOnloadResp;
	}

	@Override
	public SimRetailBlockReportRespMsg genarateSimRetailBlockReport(
			String retailBlock, String locationType, List<String> number,
			String pageNo, String sortColumnName, String sortOrder,
			String downLoadCsv) throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering genarateSimRetailBlockReport method of ReportsDAOImpl ");
		}

		String locationNoQuery = SimRetailBlockReportQuery.getLocationNoList(
				locationType, number);
		List<String> locNumList = new ArrayList<String>();
		locNumList = jdbcTemplate.queryForList(locationNoQuery, String.class);

		/** Build map for pagination */
		Map<String, Long> pageNoMap = new HashMap<String, Long>();
		if (!CommonUtil.isNull(pageNo) && pageNo != "") {
			pageNoMap = CommonUtil.getPaginationLimit(pageNo,
					PhotoOmniConstants.SILVER_CANISTER_PAGINATION_SIZE);
		} else {
			pageNoMap = null;
		}

		/** Assign respected sort column */
		String sortColoumnOne = PhotoOmniConstants.BLANK;
		if (!CommonUtil.isNull(sortColumnName)
				&& sortColumnName.equalsIgnoreCase("storeNumber")) {
			sortColoumnOne = "LOCATION_NBR";
		} else if (!CommonUtil.isNull(sortColumnName)
				&& sortColumnName.equalsIgnoreCase("retailBlock")) {
			sortColoumnOne = "SYS_PRICE_LEVEL_ID";
		} else {
			sortColoumnOne = "LOCATION_NBR";
		}

		String genarateSimRetailBlockReportQuery = SimRetailBlockReportQuery
				.getGenarateSimRetailBlockReportQuery(locationType, locNumList,
						sortColoumnOne, sortOrder);
		String genarateSimRetailBlockReportCSVQuery = SimRetailBlockReportQuery
				.getGenarateSimRetailBlockReportCSVQuery(locationType,
						locNumList, sortColoumnOne, sortOrder);
		String simRetailBlockOnloadRespQuery = SimRetailBlockReportQuery
				.getSimRetailBlockPriceLevelQuery();

		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		List<SimRetailBlockReportResp> SimRetailBlockReportRespList = new ArrayList<SimRetailBlockReportResp>();
		List<OmSimRetailBlockReportBean> omSimRetailBlockReportBeanList = new ArrayList<OmSimRetailBlockReportBean>();

		List<OmPriceLevelBean> omPriceLevelBeanList = new ArrayList<OmPriceLevelBean>();
		omPriceLevelBeanList = jdbcTemplate.query(
				simRetailBlockOnloadRespQuery, new Object[] { retailBlock },
				new OmPriceLevelRowMapper());

		try {

			if (downLoadCsv.equalsIgnoreCase("downLoadCsv")) {
				omSimRetailBlockReportBeanList = jdbcTemplate.query(
						genarateSimRetailBlockReportCSVQuery, new Object[] {
								omPriceLevelBeanList.get(0).getPriceLevel(),
								omPriceLevelBeanList.get(0)
										.getSysPriceLevelId() },
						new OmSimRetailBlockReportBeanRowMapper());
			} else {
				omSimRetailBlockReportBeanList = jdbcTemplate.query(
						genarateSimRetailBlockReportQuery,
						new Object[] {
								omPriceLevelBeanList.get(0).getPriceLevel(),
								omPriceLevelBeanList.get(0)
										.getSysPriceLevelId(),
								pageNoMap.get("START_LIMIT"),
								pageNoMap.get("END_LIMIT") },
						new OmSimRetailBlockReportBeanRowMapper());
			}

			for (int i = 0; i < omSimRetailBlockReportBeanList.size(); i++) {

				SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
				simRetailBlockReportResp.setStoreNumber(String
						.valueOf(omSimRetailBlockReportBeanList.get(i)
								.getLocationNbr()));
				simRetailBlockReportResp.setRetailBlock(String
						.valueOf(omSimRetailBlockReportBeanList.get(i)
								.getRetailBlock()));
				simRetailBlockReportResp
						.setDescription(omSimRetailBlockReportBeanList.get(i)
								.getDescription());
				simRetailBlockReportResp.setTotalrecords(String
						.valueOf(omSimRetailBlockReportBeanList.get(i)
								.getTotalRows()));

				SimRetailBlockReportRespList.add(simRetailBlockReportResp);
			}
			simRetailBlockReportRespMsg
					.setSimRetailBlockReportResp(SimRetailBlockReportRespList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting genarateSimRetailBlockReport method of ReportsDAOImpl ");
			}
		}
		return simRetailBlockReportRespMsg;
	}

	@Override
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest(
			SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg)
			throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateRetailBlockRequest method of ReportsDAOImpl ");
		}

		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsg = new SimRetailBlockUpdateRespMsg();
		List<String> storeNoList = simRetailBlockUpdateReqMsg
				.getSimRetailBlockUpdateReq().getStoreNumber();
		String retailBlock = simRetailBlockUpdateReqMsg
				.getSimRetailBlockUpdateReq().getRetailBlock();
		;
		/*
		 * String[] tempArr = retailBlock .replace("(", ",").split(",");
		 * 
		 * @SuppressWarnings("unused") String retailBlockNo = tempArr[0]; String
		 * retailBlockDescription = tempArr[1]; String
		 * tempRetailBlockDescription = retailBlockDescription.replace(")",
		 * "").trim();
		 */

		String updateSimRetailBlockQuery = SimRetailBlockReportQuery
				.getUpdateSimRetailBlockQuery(storeNoList, retailBlock);
		try {

			int updateCnt = jdbcTemplate.update(updateSimRetailBlockQuery);
			if (updateCnt < 0) {
				simRetailBlockUpdateRespMsg
						.setUpdateStatusMessage("Failed to update records");
			} else {
				if (updateCnt > 1) {
					simRetailBlockUpdateRespMsg
							.setUpdateStatusMessage(updateCnt
									+ " records were updated");
				} else {
					simRetailBlockUpdateRespMsg
							.setUpdateStatusMessage(updateCnt
									+ " record was updated");
				}
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at updateRetailBlockRequest method of ReportsDAOImpl - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting updateRetailBlockRequest method of ReportsDAOImpl ");
			}
		}
		return simRetailBlockUpdateRespMsg;
	}

	@Override
	public List<OrderType> getCannedReportOrderType() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedReportOrderType method of MachineReportDAOImpl ");
		}
		List<OrderType> cannedOrderTypeList = null;
		String sqlQuery = null;
		try {
			LOGGER.debug("Entering into ReportsQuery");
			sqlQuery = ReportsQuery.getOrderTypeQuery().toString();
			cannedOrderTypeList = dataGuardJdbcTemplate.query(sqlQuery,
					new com.walgreens.omni.rowmapper.OrderTypeRowmapper());
		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(
					" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at getCannedReportOrderType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderType method of ReportsDAOImpl ");
			}
		}
		return cannedOrderTypeList;
	}

	@Override
	public List<InputChannel> getCannedInputType() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedInputType method of CannedReport DAOImpl ");
		}
		List<InputChannel> cannedInputChannelList = null;
		String sqlQuery = null;
		try {
			LOGGER.debug("Entering into ReportsQuery");
			sqlQuery = ReportsQuery.getInputChannelQuery().toString();
			cannedInputChannelList = dataGuardJdbcTemplate.query(sqlQuery,
					new InputChannelRowmapper());
		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at getCannedInputType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (NullPointerException e) {
			LOGGER.error(
					" Error occoured at getCannedInputType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at getCannedInputType method of ReportsDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedInputType method of ReportsDAOImpl ");
			}
		}
		return cannedInputChannelList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.omni.dao.ReportsDAO#generateReportRequest(com.walgreens
	 * .omni.json.bean.CannedFilter)
	 */
	@Override
	public List<CannedReportResBean> generateReportRequest(CannedFilter reqBean)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering geneerateReportRequest method of CannedReportDAOImpl ");
		}
		List<CannedReportResBean> cannedReportResBeanList = null;
		String sqlQuery = null;
		try {

			if (!CommonUtil.isNull(reqBean)) {
				String currentPage = reqBean.getCurrrentPage();
				LOGGER.debug("currentPage" + currentPage);
				int pageSize = 5;
				LOGGER.debug("Invoking the method from CommonUtil file to get the upper limit and lower limit ");
				Map<String, Long> pageMap = CommonUtil.getPaginationLimit(
						currentPage, pageSize);
				long upperLimit = pageMap.get("END_LIMIT");
				LOGGER.debug("upperLimit " + upperLimit);
				long lowerLimit = pageMap.get("START_LIMIT");
				LOGGER.debug("lowerLimit " + lowerLimit);
				// filter.setOrderTypeName("placedOrderByProduct");//filter.setOrderTypeName("soldOrderByProduct")
				LOGGER.debug("Entering into placedOrderByProduct of orderType");
				if (("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_KIOSK
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataPlaced(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, false, false,
							true).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(true, false,
									pageSize));
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_KIOSK
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataSold(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, false, false,
							true).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(false, true,
									pageSize));
					LOGGER.debug("Exiting from GenerateCannedReportRowMapper");
				} else if (("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_INTERNET
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataPlaced(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, false, true,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(true, false,
									pageSize));
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");

				} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_INTERNET
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataSold(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, false, true,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(false, true,
									pageSize));
					LOGGER.debug("Exiting from GenerateCannedReportRowMapper");
				}

				else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())) && ("ALL"
						.equalsIgnoreCase(reqBean.getInputChannelName())))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataPlaced(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, false,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(true, false,
									pageSize));
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else if ("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())
						&& "ALL".equalsIgnoreCase(reqBean.getInputChannelName())) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType for ALL");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataSold(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, false,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(false, true,
									pageSize));
					LOGGER.debug("Exiting from GenerateCannedReportRowMapper");
				}

				else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())) && ("Mobile"
						.equalsIgnoreCase(reqBean.getInputChannelName())))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataPlaced(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, false,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(true, false,
									pageSize));
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGeneratedCannedReportDataSold(
							reqBean.getSortColumnName(),
							reqBean.getSortOrder(), reqBean.getStartDate(),
							reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, false,
							false).toString();
					Object[] params = { lowerLimit, upperLimit };
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResBeanList = dataGuardJdbcTemplate.query(
							sqlQuery, params,
							new GenerateCannedReportRowMapper(false, true,
									pageSize));
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				}

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Canned  Report SQL Query is : " + sqlQuery);
				}
			}

		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (NullPointerException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (ClassCastException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateReportRequest method of CannedReportDAOImpl ");
			}
		}

		return cannedReportResBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.omni.dao.ReportsDAO#generateCannedReportRequest(com.walgreens
	 * .omni.json.bean.CannedFilter, java.lang.String)
	 */
	@Override
	public List<CannedReportDataCSVBean> generateCannedReportRequest(
			CannedFilter reqBean, String req) throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering generateCannedReportRequest method of CannedReportDAOImpl ");
		}
		// List<CannedReportResBean> cannedReportResBeanList = null;
		List<CannedReportDataCSVBean> cannedReportDataBeanList = new ArrayList<CannedReportDataCSVBean>();
		String sqlQuery = null;
		try {

			LOGGER.info("Entering into placedOrderByProduct of orderType");
			if (("Placed Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName()))
					&& (PhotoOmniConstants.ORDER_KIOSK.equalsIgnoreCase(reqBean
							.getInputChannelName()))) {
				LOGGER.info("Entering into Reports query");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvPlaced(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, false, false,
						true).toString();
				LOGGER.info("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(true, false));
				LOGGER.info("Exiting from  CannedReportRowMapper");
			} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName()))
					&& (PhotoOmniConstants.ORDER_KIOSK.equalsIgnoreCase(reqBean
							.getInputChannelName()))) {
				LOGGER.debug("Entering into soldOrderByProduct of orderType");
				LOGGER.debug("Entering into ReportsQuery ");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvSold(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, false, false,
						true).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(false, true));
				LOGGER.debug("Exiting from CannedReportRowMapper");
			} else if (("Placed Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName()))
					&& (PhotoOmniConstants.ORDER_INTERNET
							.equalsIgnoreCase(reqBean.getInputChannelName()))) {
				LOGGER.debug("Entering into Reports query");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvPlaced(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, false, true,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(true, false));
				LOGGER.debug("Exiting from  CannedReportRowMapper");
			}

			else if (("Sold Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName()))
					&& (PhotoOmniConstants.ORDER_INTERNET
							.equalsIgnoreCase(reqBean.getInputChannelName()))) {
				LOGGER.debug("Entering into soldOrderByProduct of orderType");
				LOGGER.debug("Entering into ReportsQuery ");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvSold(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, false, true,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(false, true));
				LOGGER.debug("Exiting from CannedReportRowMapper");
			}

			else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName())) && ("ALL".equalsIgnoreCase(reqBean
					.getInputChannelName())))) {
				LOGGER.debug("Entering into Reports query");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvPlaced(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), true, false, false,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(true, false));
				LOGGER.debug("Exiting from  CannedReportRowMapper");
			} else if ((("Sold Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName())) && ("ALL".equalsIgnoreCase(reqBean
					.getInputChannelName())))) {
				LOGGER.debug("Entering into soldOrderByProduct of orderType for Mobile");
				LOGGER.debug("Entering into ReportsQuery ");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvSold(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), true, false, false,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(false, true));
				LOGGER.debug("Exiting from CannedReportRowMapper");

			} else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
					.getOrderTypeName())) && ("Mobile".equalsIgnoreCase(reqBean
					.getInputChannelName())))) {
				LOGGER.debug("Entering into Reports query");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvPlaced(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, true, false,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(true, false));
				LOGGER.debug("Exiting from  CannedReportRowMapper");
			} else {
				LOGGER.debug("Entering into Reports query");
				sqlQuery = ReportsQuery.getGeneratedCannedReportDataCsvSold(
						reqBean.getStartDate(), reqBean.getEndDate(),
						reqBean.getInputChannelName(), false, true, false,
						false).toString();
				LOGGER.debug("Entering into CannedReportRowMapper");
				cannedReportDataBeanList = dataGuardJdbcTemplate.query(
						sqlQuery, new CannedReportRowMapper(false, true));
				LOGGER.debug("Exiting from  CannedReportRowMapper");
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Canned  Report SQL Query is : " + sqlQuery);
			}

		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at generateCannedReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(
					" Error occoured at generateCannedReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (ClassCastException e) {
			LOGGER.error(
					" Error occoured at generateCannedReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateCannedReportRequest method of CannedReportDAOImpl ");
			}
		}

		return cannedReportDataBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.omni.dao.ReportsDAO#getGenereicFields(com.walgreens.omni
	 * .json.bean.CannedFilter)
	 */
	@Override
	public List<CannedReportResGenericBean> getGenereicFields(
			CannedFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering geneerateReportRequest method of CannedReportDAOImpl ");
		}
		List<CannedReportResGenericBean> cannedReportResGenericBeanList = null;
		String sqlQuery = null;
		try {

			if (!CommonUtil.isNull(reqBean)) {
				String currentPage = reqBean.getCurrrentPage();
				LOGGER.debug("currentPage" + currentPage);
				int pageSize = 5;
				LOGGER.debug("Invoking the method from CommonUtil file to get the upper limit and lower limit ");
				Map<String, Long> pageMap = CommonUtil.getPaginationLimit(
						currentPage, pageSize);
				long upperLimit = pageMap.get("END_LIMIT");
				LOGGER.debug("upperLimit " + upperLimit);
				long lowerLimit = pageMap.get("START_LIMIT");
				LOGGER.debug("lowerLimit " + lowerLimit);
				// filter.setOrderTypeName("placedOrderByProduct");//filter.setOrderTypeName("soldOrderByProduct")
				LOGGER.debug("Entering into placedOrderByProduct of orderType");
				if (("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_KIOSK
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, false,
							false, false, true).toString();

					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");

				} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_KIOSK
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, false,
							false, false, true).toString();
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else if (("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_INTERNET
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, false,
							false, true, false).toString();
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))
						&& (PhotoOmniConstants.ORDER_INTERNET
								.equalsIgnoreCase(reqBean.getInputChannelName()))) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, false,
							false, true, false).toString();
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				}

				else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())) && ("ALL"
						.equalsIgnoreCase(reqBean.getInputChannelName())))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, true,
							false, false, false).toString();
					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else if ("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())
						&& "ALL".equalsIgnoreCase(reqBean.getInputChannelName())) {
					LOGGER.debug("Entering into soldOrderByProduct of orderType for ALL");
					LOGGER.debug("Entering into ReportsQuery ");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, true,
							false, false, false).toString();

					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from GenerateCannedReportRowMapper");
				}

				else if ((("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName())) && ("Mobile"
						.equalsIgnoreCase(reqBean.getInputChannelName())))) {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), true, false, false,
							true, false, false).toString();

					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				} else {
					LOGGER.debug("Entering into Reports query");
					sqlQuery = ReportsQuery.getGenericFields(
							reqBean.getStartDate(), reqBean.getEndDate(),
							reqBean.getInputChannelName(), false, true, false,
							true, false, false).toString();

					LOGGER.debug("Entering into GenerateCannedReportRowMapper");
					cannedReportResGenericBeanList = dataGuardJdbcTemplate
							.query(sqlQuery,
									new GenerateCannedReportGenericRowMapper());
					LOGGER.debug("Exiting from  GenerateCannedReportRowMapper");
				}

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Canned  Report SQL Query is : " + sqlQuery);
				}
			}

		} catch (DataAccessException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (NullPointerException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (ClassCastException e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at generateReportRequest method of CannedReportDAOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateReportRequest method of CannedReportDAOImpl ");
			}
		}

		return cannedReportResGenericBeanList;
	}

}
