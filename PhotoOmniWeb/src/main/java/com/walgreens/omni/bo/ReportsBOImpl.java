package com.walgreens.omni.bo;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.bean.SilverRecoveryHeaderDetails;
import com.walgreens.omni.dao.ReportsDAO;
import com.walgreens.omni.exception.ReportException;
import com.walgreens.omni.factory.OmniDAOFactory;
import com.walgreens.omni.json.bean.CannedDropDownData;
import com.walgreens.omni.json.bean.CannedFilter;
import com.walgreens.omni.json.bean.CannedReportBean;
import com.walgreens.omni.json.bean.CannedReportDataBean;
import com.walgreens.omni.json.bean.CannedReportGenericBean;
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
import com.walgreens.omni.web.constants.ReportsConstant;

@Component("ReportsBO")
@Service
public class ReportsBOImpl implements ReportsBO {

	@Autowired
	private OmniDAOFactory reportDAOFactory;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportsBOImpl.class);

	/**
	 * 
	 * Method used to submit Silver Canister Report Request
	 */
	@Override
	public SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			SilverCanisterReportReqMsg reqParam, String reportReq)
			throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterReportRequest method of ReportsBO ");
		}
		SilverCanisterReportRepMsg silverCanisterReportRepMsg = new SilverCanisterReportRepMsg();

		try {

			ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
			silverCanisterReportRepMsg = reportsDAO
					.submitSilverCanisterReportRequest(reqParam, reportReq);

			silverCanisterReportRepMsg.setMessageHeader(reqParam
					.getMessageHeader());
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setDescription(PhotoOmniConstants.ERROR_DESCRIPTION);
			errorDetails.setErrorCode(PhotoOmniConstants.ERROR_CODE);
			errorDetails.setErrorSource(PhotoOmniConstants.ERROR_SOURCE);
			errorDetails.setErrorString(PhotoOmniConstants.ERROR_STRING);
			silverCanisterReportRepMsg.setErrorDetails(errorDetails);

		} catch (ReportException e) {
			LOGGER.error(
					" Error occoured at submitSilverCanisterReportRequest method of ReportsBO - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSilverCanisterReportRequest method of ReportsBO ");
			}
		}
		return silverCanisterReportRepMsg;

	}

	/**
	 * Method used to upload Silver Canister Details
	 */
	@Override
	public Map<Integer, ArrayList<String>> updateSilverCanisterDetails(
			Map<Integer, ArrayList<String>> dataMap, String vendorName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateSilverCanisterDetails method of ReportsBO ");
		}

		ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
		Map<Integer, ArrayList<String>> invalidRecdMap = new HashMap<Integer, ArrayList<String>>();

		try {

			for (Map.Entry<Integer, ArrayList<String>> entry : dataMap
					.entrySet()) {

				List<String> dataList = new ArrayList<String>();
				dataList = entry.getValue();
				String storeNo = dataList.get(0);
				String canisterchangeDate = dataList.get(1);
				String serviceDescription = dataList.get(2);

				/** split excel records for ServiceDescription */
				String tempServiceDescription = serviceDescription.replace(
						ReportsConstant.SIGN_PATTERN_ONE,
						ReportsConstant.SIGN_PATTERN_TWO);
				String[] serDescArr = tempServiceDescription
						.split(ReportsConstant.SIGN_PATTERN_TWO);

				/** get required format for canisterchangeDate */
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						ReportsConstant.DATE_FORMAT_SILVER_CANISTER_ONE);
				Date dateSelectedFrom = dateFormat.parse(canisterchangeDate);

				/** logic to find next date */
				int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
				String nextDate = dateFormat.format(dateSelectedFrom.getTime()
						+ MILLIS_IN_DAY);
				Date dateNextDate = dateFormat.parse(nextDate);

				/**
				 * get get the PROC status details for the corresponding store
				 * Number
				 */
				List<SilverRecoveryHeaderDetails> silverRecoveryHeaderDetailsList = new ArrayList<SilverRecoveryHeaderDetails>();
				silverRecoveryHeaderDetailsList = reportsDAO
						.getSilverRecoveryHeaderDetails(storeNo);

				if (silverRecoveryHeaderDetailsList.isEmpty()) {
					invalidRecdMap.put(entry.getKey(), entry.getValue());
				} else {
					boolean uploadStatus = false;
					for (int i = 0; i < silverRecoveryHeaderDetailsList.size(); i++) {
						/** process Canister Upload Request */
						uploadStatus = reportsDAO.processCanisterUploadRequest(
								silverRecoveryHeaderDetailsList.get(i),
								dateSelectedFrom, vendorName, serDescArr,
								dateNextDate);
						/** insert records in invalid map records */
						if (!uploadStatus) {
							invalidRecdMap
									.put(entry.getKey(), entry.getValue());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at updateSilverCanisterDetails method of ReportsBO - ",
					e);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting updateSilverCanisterDetails method of ReportsBO ");
		}
		return invalidRecdMap;
	}

	/**
	 * Method to submit Silver Canister Store Report Request
	 */
	@Override
	public SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			SilverCanisterStoreReportReqMsg reqParam) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterStoreReportRequest method of ReportsBO ");
		}
		SilverCanisterStroeReportRepMsg silverCanisterStroeReportRepMsg = new SilverCanisterStroeReportRepMsg();

		try {

			ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
			silverCanisterStroeReportRepMsg = reportsDAO
					.submitSilverCanisterStoreReportRequest(reqParam);

			silverCanisterStroeReportRepMsg.setMessageHeader(reqParam
					.getMessageHeader());
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setDescription(PhotoOmniConstants.ERROR_DESCRIPTION);
			errorDetails.setErrorCode(PhotoOmniConstants.ERROR_CODE);
			errorDetails.setErrorSource(PhotoOmniConstants.ERROR_SOURCE);
			errorDetails.setErrorString(PhotoOmniConstants.ERROR_STRING);
			silverCanisterStroeReportRepMsg.setErrorDetails(errorDetails);

		} catch (ReportException e) {
			LOGGER.error(
					" Error occoured at submitSilverCanisterStoreReportRequest method of ReportsBO - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSilverCanisterStoreReportRequest method of ReportsBO ");
			}
		}
		return silverCanisterStroeReportRepMsg;

	}

	@Override
	public SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSimRetailBlockOnloadRequest method of ReportsBOImpl ");
		}
		SimRetailBlockOnloadResp simRetailBlockOnloadResp = new SimRetailBlockOnloadResp();
		try {
			ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
			simRetailBlockOnloadResp = reportsDAO
					.submitSimRetailBlockOnloadRequest();

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at generateCannedReportRequest method of ReportsBO - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSimRetailBlockOnloadRequest method of ReportsBO ");
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
			LOGGER.debug(" Entering genarateSimRetailBlockReport method of ReportsBOImpl ");
		}
		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		/*
		 * String[] tempArr = retailBlock.replace("(", ",").split(","); String
		 * retailBlockNo = tempArr[0]; String tempRetailBlockNo =
		 * retailBlockNo.trim();
		 */

		try {
			ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
			simRetailBlockReportRespMsg = reportsDAO
					.genarateSimRetailBlockReport(retailBlock, locationType,
							number, pageNo, sortColumnName, sortOrder,
							downLoadCsv);

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of ReportsBOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting genarateSimRetailBlockReport method of ReportsBOImpl ");
			}
		}
		return simRetailBlockReportRespMsg;
	}

	@Override
	public SimRetailBlockUpdateRespMsg updateRetailBlockRequest(
			SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateRetailBlockRequest method of ReportsBOImpl ");
		}

		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsg = new SimRetailBlockUpdateRespMsg();
		try {
			ReportsDAO reportsDAO = reportDAOFactory.getExceptionReportDAO();
			simRetailBlockUpdateRespMsg = reportsDAO
					.updateRetailBlockRequest(simRetailBlockUpdateReqMsg);

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured updateRetailBlockRequest method of ReportsBOImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting updateRetailBlockRequest method of ReportsBOImpl ");
			}
		}
		return simRetailBlockUpdateRespMsg;
	}

	@Override
	public CannedDropDownData getCannedReportOrderTypeInputChannelRequest()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedReportOrderTypeInputChannelRequest method of MachineReportBOImpl ");
		}
		List<OrderType> cannedOrderTypeList = null;
		List<InputChannel> cannedInputChannelList = null;
		CannedDropDownData cannedDropDownData = new CannedDropDownData();
		try {
			ReportsDAO cannedDAO = reportDAOFactory.getCannedReportDAO();
			/* Fetching Order Type */
			LOGGER.debug("Entering into getCannedReportOrderType of ReportsDAO impl");
			cannedOrderTypeList = cannedDAO.getCannedReportOrderType();
			LOGGER.debug("Exiting from  getCannedReportOrderType of ReportsDAO impl");
			cannedDropDownData.setOrderType(cannedOrderTypeList);
			/* Fetching Input Channel */
			LOGGER.debug("Entering into getCannedInputType of ReportsDAO impl");
			cannedInputChannelList = cannedDAO.getCannedInputType();
			LOGGER.debug("Entering from getCannedInputType of ReportsDAO impl");
			cannedDropDownData.setInputChannel(cannedInputChannelList);

			/*
			 * ObjectWriter ow = new ObjectMapper().writer()
			 * .withDefaultPrettyPrinter();
			 */
			// System.out.println(ow.writeValueAsString(cannedDropDownData));
		} catch (PhotoOmniException e) {
			LOGGER.error(
					" Error occoured at getCannedReportOrderTypeInputChannelRequest method  of ReportsBOImpl ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at getCannedReportOrderTypeInputChannelRequest  of ReportsBOImpl ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderTypeInputChannelRequest method  of ReportsBOImpl");
			}
		}

		return cannedDropDownData;
	}

	@Override
	public CannedReportBean generateReportRequest(CannedFilter filter)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering generateReportRequest method of ReportsBOImpl ");
		}
		CannedReportBean cannedReportBean = new CannedReportBean();
		try {
			List<CannedReportResBean> cannedReportResBeanList = null;

			ReportsDAO cannedDAO = reportDAOFactory.getCannedReportDAO();
			String sortColumn = this.getSortColumnName(filter
					.getSortColumnName());
			filter.setSortColumnName(sortColumn);
			cannedReportResBeanList = cannedDAO.generateReportRequest(filter);
			this.createJsonStructureForCannedReport(filter, cannedReportBean,
					cannedReportResBeanList);
			ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			LOGGER.debug(ow.writeValueAsString(cannedReportBean));

		} catch (PhotoOmniException e) {
			e.printStackTrace();
			LOGGER.error(
					" Error occoured at generateReportRequest method ReportsBOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					" Error occoured at generateReportRequest method of ReportsBOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateReportRequest method of ReportsBOImpl ");
			}
		}
		return cannedReportBean;
	}

	@Override
	public List<CannedReportDataCSVBean> generateCannedReportRequest(
			CannedFilter reqParam, String req) throws PhotoOmniException {
		// TODO Auto-generated method stub
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering generateCannedReportRequest method of ReportsBO ");
		}
		// CannedReportBean cannedReportBean = new CannedReportBean();
		List<CannedReportDataCSVBean> cannedReportDataBeanList = new ArrayList<CannedReportDataCSVBean>();

		try {
			// List<CannedReportResBean> cannedReportResBeanList = null;

			ReportsDAO reportsDAO = reportDAOFactory.getCannedReportDAO();
			LOGGER.debug("Entering into generateCannedReportRequest of ReportsDAO");
			cannedReportDataBeanList = reportsDAO.generateCannedReportRequest(
					reqParam, req);
			LOGGER.debug("Exiting from generateCannedReportRequest of ReportsDAO");
			// this.createJsonStructureForCannedReport(reqParam,
			// cannedReportBean, cannedReportDataBeanList);
			ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			try {
				LOGGER.debug((ow.writeValueAsString(cannedReportDataBeanList)));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(
					" Error occoured at generateCannedReportRequest method of ReportsBO - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateCannedReportRequest method of ReportsBO ");
			}
		}
		return cannedReportDataBeanList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.omni.bo.ReportsBO#getGenericFields(com.walgreens.omni.json
	 * .bean.CannedFilter)
	 */
	@Override
	public CannedReportGenericBean getGenericFields(CannedFilter filter)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering generateReportRequest method of ReportsBOImpl ");
		}
		CannedReportGenericBean cannedReportGenericBean = new CannedReportGenericBean();
		try {
			List<CannedReportResGenericBean> cannedReportResGenericBeanList = null;
			ReportsDAO cannedDAO = reportDAOFactory.getCannedReportDAO();
			cannedReportResGenericBeanList = cannedDAO
					.getGenereicFields(filter);
			double sum = 0.0;
			double sumTotalOrderRevenueData = 0.0;
			double sumTotalDiscountAmountData = 0.0;
			double averagePlacedValueAmount = 0.0;
			double sumTotalRevenue = 0.0;
			double sumTotalDiscount = 0.0;
			double sumTotalOrdersData = 0.0;
			double sumTotalOrders = 0.0;
			double averagePlacedvalue = 0.0;
			double averagePlacedvalueData = 0.0;

			sumTotalOrderRevenueData = sumTotalOrderRevenueData
					+ cannedReportResGenericBeanList.get(0)
							.getTotalOrderRevenue();
			sumTotalRevenue = Math.round(sumTotalOrderRevenueData * 100.0) / 100.0;
			sumTotalDiscountAmountData = sumTotalDiscountAmountData
					+ cannedReportResGenericBeanList.get(0).getTotalDiscount();
			sumTotalDiscount = Math.round(sumTotalDiscountAmountData * 100.0) / 100.0;
			sumTotalOrdersData = sumTotalOrdersData
					+ cannedReportResGenericBeanList.get(0).getTotalOrder();

			cannedReportGenericBean.setSumTotalRevenue(sumTotalRevenue);
			cannedReportGenericBean.setSumTotalDiscount(sumTotalDiscount);
			cannedReportGenericBean.setSumTotalOrders(sumTotalOrdersData);
			averagePlacedvalueData = sumTotalRevenue / sumTotalOrdersData;
			averagePlacedvalue = Math.round(averagePlacedvalueData * 100.0) / 100.0;
			cannedReportGenericBean.setAveragePlacedvalue(averagePlacedvalue);
			/*
			 * this.createJsonStructureForCannedReport(reqBean,
			 * cannedReportBean, cannedReportResBeanList); ObjectWriter ow = new
			 * ObjectMapper().writer() .withDefaultPrettyPrinter();
			 * LOGGER.info(ow.writeValueAsString(cannedReportBean));
			 */

		} catch (PhotoOmniException e) {
			e.printStackTrace();
			LOGGER.error(
					" Error occoured at generateReportRequest method ReportsBOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(
					" Error occoured at generateReportRequest method of ReportsBOImpl - ",
					e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting generateReportRequest method of ReportsBOImpl ");
			}
		}
		return cannedReportGenericBean;
	}

	/**
	 * @param columnName
	 * @return
	 * @throws PhotoOmniException
	 *             Phase 2.0 Canned Report Start: Method for sorting Column
	 *             names Phase 2.0 Canned Report End
	 */
	private String getSortColumnName(String columnName)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of OrderBOImpl ");
		}
		String sortColumn = "sysProductId";
		try {
			Map<String, String> dbColumnName = new HashMap<String, String>();

			dbColumnName.put("productName", "PRODUCT_NAME");
			dbColumnName.put("totalProductQuantity", "TOTAL_PRODUCT_QUANTITY");
			dbColumnName.put("totalOrder", "TOTAL_ORDERS");
			dbColumnName.put("totalRevenue", "TOTAL_REVENUE");
			dbColumnName.put("amountPaid", "AMOUNT_PAID");
			dbColumnName.put("totalRevenueDiscount", "TOTAL_REVENUE_DISCOUNT");
			dbColumnName.put("unitPrice", "UNIT_PRICE");
			dbColumnName.put("profit", "PROFIT");
			sortColumn = dbColumnName.get(columnName);
		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at getSortColumnName method of OrderBOImpl - ",
					e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of OrderBOImpl ");
			}
		}
		return sortColumn;
	}

	/**
	 * @param reqBean
	 * @param cannedReportBean
	 * @param cannedReportResBeanList
	 *            Creating Json Structure
	 * @throws PhotoOmniException
	 */
	private void createJsonStructureForCannedReport(CannedFilter reqBean,
			CannedReportBean cannedReportBean,
			List<CannedReportResBean> cannedReportResBeanList)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into createJsonStructureForCannedReport");
		}
		if (!CommonUtil.isNull(cannedReportResBeanList)
				&& cannedReportResBeanList.size() > 0) {
			List<CannedReportDataBean> cannedReportDataBeanList = new ArrayList<CannedReportDataBean>();
			cannedReportBean.setCurrentPage(reqBean.getCurrrentPage());

			for (CannedReportResBean cannedReportResBean : cannedReportResBeanList) {
				cannedReportBean.setTotalRecord(cannedReportResBeanList.get(0)
						.getTotalRecord());
				cannedReportBean.setPageSize(cannedReportResBeanList.get(0)
						.getPageSize());
				/* Excnanging data to other bean for json format */
				CannedReportDataBean cannedReportDataBean = new CannedReportDataBean();
				LOGGER.debug("cannedReportResBean.getProductName()"
						+ cannedReportResBean.getProductName());
				cannedReportDataBean.setProductName(cannedReportResBean
						.getProductName());
				cannedReportDataBean.setProfit(cannedReportResBean.getProfit());
				cannedReportDataBean.setSerialNumber(cannedReportResBean
						.getSerialNumber());
				cannedReportDataBean.setTotalDiscount(cannedReportResBean
						.getTotalDiscount());
				cannedReportDataBean.setTotalOrder(cannedReportResBean
						.getTotalOrder());
				cannedReportDataBean.setTotalOrderRevenue(cannedReportResBean
						.getTotalOrderRevenue());
				/*
				 * cannedReportDataBean.setUnitPrice(cannedReportResBean
				 * .getUnitPrice());
				 */
				cannedReportDataBean
						.setTotalRevenueDiscount(cannedReportResBean
								.getTotalRevenueDiscount());
				cannedReportDataBean.setSysProductId(cannedReportResBean
						.getSysProductId());
				cannedReportDataBean
						.setTotalProductQuantity(cannedReportResBean
								.getTotalProductQuantity());
				cannedReportDataBean.setTotalRevenue(cannedReportResBean
						.getTotalRevenue());
				cannedReportDataBean.setAmountPaid(cannedReportResBean
						.getAmountPaid());
				/**
				 * Calculation for Unit Price CR
				 */
				Double price = 0.0;
				Double unitPrice = 0.0;
				if (("Placed Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))) {
					if (cannedReportResBean.getTotalRevenue() == 0.0
							|| cannedReportResBean.getTotalProductQuantity() == 0.0) {
						unitPrice = 0.0;
					} else {

						price = cannedReportResBean.getTotalRevenue()
								/ cannedReportResBean.getTotalProductQuantity();

						unitPrice = Math.round(price * 100.0) / 100.0;
					}
				} else if (("Sold Order By Product".equalsIgnoreCase(reqBean
						.getOrderTypeName()))) {
					if (cannedReportResBean.getAmountPaid() == 0.0
							|| cannedReportResBean.getTotalProductQuantity() == 0.0) {
						unitPrice = 0.0;
					}
					price = cannedReportResBean.getAmountPaid()
							/ cannedReportResBean.getTotalProductQuantity();
					unitPrice = Math.round(price * 100.0) / 100.0;
				}
				cannedReportDataBean.setUnitPrice(unitPrice);
				cannedReportDataBeanList.add(cannedReportDataBean);
			}
			cannedReportBean
					.setCannedReportDataBeanList(cannedReportDataBeanList);

		} else {
			/* When no data fetch from database */
			cannedReportBean.setCurrentPage(reqBean.getCurrrentPage());
			cannedReportBean.setTotalRecord(0);
			cannedReportBean.setPageSize(0);
			cannedReportBean
					.setCannedReportDataBeanList(new ArrayList<CannedReportDataBean>());
		}
		LOGGER.debug("Exiting from createJsonStructureForCannedReport");
	}
}
