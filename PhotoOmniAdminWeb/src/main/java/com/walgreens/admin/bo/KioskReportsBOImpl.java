package com.walgreens.admin.bo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bean.KioskDetailBean;
import com.walgreens.admin.bean.KioskFilter;
import com.walgreens.admin.dao.KioskReportsDAO;
import com.walgreens.admin.factory.AdminDAOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * The Class KioskReportsBOImpl.
 */
@Component("reportBO")
public class KioskReportsBOImpl implements KioskReportsBO {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KioskReportsBOImpl.class);

	/** The admin dao factory. */
	@Autowired
	private AdminDAOFactory adminDAOFactory;

	/** The export to excel. */
	@Autowired
	private com.walgreens.admin.utility.ExportToExcel exportToExcel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.bo.KioskReportsBO#getKioskReportData(java.util.List,
	 * int, int)
	 */
	@Override
	public List<KioskDetailBean> getKioskReportData(List<KioskFilter> filters,
			int page, int rows) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execution getKioskReportData in KioskReportsBOImpl");
		}
		List<KioskDetailBean> beans = new ArrayList<>();
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		List<Map<String, Object>> list = kioskReportDAO.getKioskReportData(
				filters, page, rows);
		if (!CollectionUtils.isEmpty(list)) {
			for (Map<String, Object> map : list) {
				KioskDetailBean kioskDetailBean = new KioskDetailBean();
				kioskDetailBean.setStoreNumber(CommonUtil
						.bigDecimalToDouble(map
								.get(PhotoOmniConstants.LOCATIONNBR)));
				kioskDetailBean.setDeviceIp((String) map
						.get(PhotoOmniConstants.KIOSKIP));
				kioskDetailBean.setSoftwareVersion((String) map
						.get(PhotoOmniConstants.SOFTWAREVER));
				kioskDetailBean.setTemplateVersion((String) map
						.get(PhotoOmniConstants.TEMPLATEVER));
				kioskDetailBean.setImemoriesVersion((String) map
						.get(PhotoOmniConstants.IMEMORIESVER));
				kioskDetailBean.setTomoVersion((String) map
						.get(PhotoOmniConstants.TOMOVER));
				kioskDetailBean.setLastReboot((Date) map
						.get(PhotoOmniConstants.LASTREBOOT));
				kioskDetailBean
						.setcDriveSpaceMb(CommonUtil.bigDecimalToDouble(map
								.get(PhotoOmniConstants.CSPACE)));
				kioskDetailBean.setdDriveSpaceMb((String) map
						.get(PhotoOmniConstants.DSPACE));
				beans.add(kioskDetailBean);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting execution getKioskReportData in KioskReportsBOImpl");
		}
		return beans;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.bo.KioskReportsBO#getReportDataCount(java.util.List)
	 */
	@Override
	public int getReportDataCount(List<KioskFilter> filters)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("To get data count for pagination uisng getReportDataCount in KioskReportsBOImpl");
		}
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		return kioskReportDAO.getDataCount(filters);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.admin.bo.KioskReportsBO#getKioskFilterOptions()
	 */
	@Override
	public List<Map<String, Object>> getKioskFilterOptions()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execution getKioskFilterOptions in KioskReportsBOImpl");
		}
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		List<Map<String, Object>> list = kioskReportDAO.getKioskFilterOptions();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting execution getKioskFilterOptions in KioskReportsBOImpl");
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.bo.KioskReportsBO#exportToExcel(javax.servlet.http
	 * .HttpServletResponse, java.util.List)
	 */
	@Override
	public void exportToExcel(HttpServletResponse response,
			List<KioskFilter> filters) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execution exportToExcel in KioskReportsBOImpl");
		}
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		Map<String, String> columnNames = new HashMap<>();
		List<Map<String, Object>> kioskFilterOptions = kioskReportDAO
				.getKioskFilterOptions();
		int numberOfColumns = kioskFilterOptions.size();
		for (int j = 0; j < numberOfColumns; j++) {
			String code = (String) kioskFilterOptions.get(j).get(
					PhotoOmniConstants.CODE_ID);
			String decode = (String) kioskFilterOptions.get(j).get(
					PhotoOmniConstants.DECODE);
			columnNames.put(code, decode);
		}

		String sql = com.walgreens.admin.utility.ReportsQuery
				.getKioskReportExcelData().toString();
		try {
			sql += kioskReportDAO.getKioskReportConditons(filters);
			Object[] params = kioskReportDAO.getFilterParams(filters, 0, 0,
					false).split(",");
			String readableString = getFilterString(filters);
			exportToExcel.export(response, sql, Arrays.asList(params),
					columnNames, readableString,
					PhotoOmniConstants.KIOSK_REPORT);
		} catch (Exception e) {
			LOGGER.error(
					"Error while performing export to excel in KIOSK report ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting execution exportToExcel in KioskReportsBOImpl");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.bo.KioskReportsBO#getFilterString(java.util.List)
	 */
	@Override
	public String getFilterString(List<KioskFilter> filters)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execution getFilterString in KioskReportsBOImpl");
		}
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		StringBuilder builder = new StringBuilder();
		builder.append(PhotoOmniConstants.SEARCH_CRITERIA);
		for (KioskFilter kioskFilter : filters) {
			builder.append(kioskReportDAO.getDecodedValue(kioskFilter
					.getCriteria()));
			builder.append(PhotoOmniConstants.BLANK);
			builder.append(kioskFilter.getOperator());
			builder.append(PhotoOmniConstants.BLANK);
			builder.append(kioskFilter.getValue());
			builder.append(PhotoOmniConstants.COMMA_DELIMITER);
		}
		String data = builder.toString().substring(0,
				builder.toString().length() - 1);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting execution getFilterString in KioskReportsBOImpl");
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.bo.KioskReportsBO#getKioskPageCount(java.lang.String)
	 */
	@Override
	public int getKioskPageCount(String reportId) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Start execution getKioskPageCount in KioskReportsBOImpl");
		}
		KioskReportsDAO kioskReportDAO = adminDAOFactory.getKioskReportsDAO();
		int perPageDataCount = kioskReportDAO
				.getReportRecordPageCount(reportId);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting execution getKioskPageCount in KioskReportsBOImpl");
		}
		return perPageDataCount;
	}
}