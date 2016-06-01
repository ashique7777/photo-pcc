package com.walgreens.admin.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.admin.bean.KioskFilter;
import com.walgreens.admin.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * DAO for the KIOSK excel report.
 * 
 * @author CTS
 */
@Repository("reportDAO")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class KioskReportsDAOImpl implements KioskReportsDAO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KioskReportsDAOImpl.class);

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate dataGuardJdbcTemplate;

	/**
	 * @param dataGuardJdbcTemplate
	 */
	public void setDataGuardJdbcTemplate(JdbcTemplate dataGuardJdbcTemplate) {
		this.dataGuardJdbcTemplate = dataGuardJdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.dao.KioskReportsDAO#getKioskReportData(java.util.
	 * List, int, int)
	 */
	@Override
	public List<Map<String, Object>> getKioskReportData(
			List<KioskFilter> filters, int page, int rows)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getKioskReportData method of KioskReportsDAOImpl");
		}
		List<Map<String, Object>> list = null;
		try {
			Object[] params = getFilterParams(filters, page, rows, true).split(
					",");
			String sql = ReportsQuery.getKioskData(
					getKioskReportConditons(filters)).toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Params for the sql query in the getKioskReportData method of KioskReportsDAOImpl - "
						+ params);
				LOGGER.debug("SQL query  for getKioskReportData method of KioskReportsDAOImpl "
						+ sql);
			}
			list = dataGuardJdbcTemplate.queryForList(sql, params);
		} catch (Exception e) {
			throw new PhotoOmniException();
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getKioskReportData method of KioskReportsDAOImpl");
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.admin.dao.KioskReportsDAO#getDataCount(java.util.List)
	 */
	@Override
	public int getDataCount(List<KioskFilter> filters)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getDataCount method of KioskReportsDAOImpl");
		}
		int totalCount = 0;
		try {
			String sql = ReportsQuery.getKioskReportDataCount().toString();
			sql += getKioskReportConditons(filters);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SQL query  for getDataCount method of KioskReportsDAOImpl"
						+ sql);
			}
			totalCount = dataGuardJdbcTemplate.queryForObject(sql,
					getFilterParams(filters, 0, 0, false).split(","),
					Integer.class);
		} catch (Exception e) {
			throw new PhotoOmniException();
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getDataCount method of KioskReportsDAOImpl");
			}
		}
		return totalCount;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getFilterParams(List<KioskFilter> filters, int page,
			int rows, boolean b) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getFilterParams method of KioskReportsDAOImpl");
		}
		StringBuilder builder = new StringBuilder();
		try {
			for (Map.Entry<String, Object> entry : getUniqueOriginType(filters)
					.entrySet()) {
				List<KioskFilter> listFilter = (List<KioskFilter>) entry
						.getValue();
				for (KioskFilter kioskFilter : listFilter) {
					builder.append("LIKE".equalsIgnoreCase(kioskFilter
							.getOperator()) ? "%" + kioskFilter.getValue()
							+ "%" : kioskFilter.getValue());
					builder.append(",");
				}

			}
		} catch (Exception e) {
			LOGGER.error(
					"Error occured in the getFilterParams method of KioskReportsDAOImpl - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getFilterParams method of KioskReportsDAOImpl");
			}
		}
		return true == b ? builder.append(page).append(",").append(rows)
				.append(",").append(page).append(",").append(rows).toString()
				: builder.substring(0, builder.length() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.dao.KioskReportsDAO#getKioskReportConditons(java.
	 * util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getKioskReportConditons(List<KioskFilter> filters)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getKioskReportConditons method of KioskReportsDAOImpl to get the WHERE clause for the SQL query");
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder strBuilder = new StringBuilder();
		List<String> ls = new ArrayList<>();
		List<String> orCondition = new ArrayList<>();
		List<String> andCondition = new ArrayList<>();
		List<String> objList = new ArrayList<>();
		try {
			Map<String, Object> kioskFilter = getUniqueOriginType(filters);
			for (Map.Entry<String, Object> entry : kioskFilter.entrySet()) {
				List<KioskFilter> list = (List<KioskFilter>) entry.getValue();
				if (list.size() > 1) {
					for (KioskFilter kioskFilter2 : list) {
						if (kioskFilter2.getOperator().equalsIgnoreCase("=")
								|| kioskFilter2.getOperator().equalsIgnoreCase(
										"LIKE")) {
							getCondition(orCondition, kioskFilter2);
						} else {
							getCondition(andCondition, kioskFilter2);
						}
					}
					if (CollectionUtils.isNotEmpty(orCondition)) {
						objList.add(convertListToStringSearchCriteria(
								orCondition, " OR "));
					}
					if (CollectionUtils.isNotEmpty(andCondition)) {
						objList.add(convertListToStringSearchCriteria(
								andCondition, " AND "));
					}
					int k = 0;
					for (String str : objList) {
						if (k == 0) {
							strBuilder.append("(");
							strBuilder.append(str);
							strBuilder.append(" OR ");
						} else {
							if (!str.isEmpty()) {
								strBuilder.append(str);
								strBuilder.append(" OR ");
							}
						}
						k++;
					}
					ls.add(strBuilder.substring(0, strBuilder.length() - 3)
							.concat(")"));
				} else {
					getCondition(ls, list.get(0));
				}
			}
			int count = 0;
			for (String str : ls) {
				if (count == 0) {
					sb.append(" WHERE ");
					sb.append(str);
					sb.append(" AND ");
				} else {
					sb.append(str);
					sb.append(" AND ");

				}
				count++;
			}
		} catch (Exception e) {
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getKioskReportConditons method of KioskReportsDAOImpl to get the WHERE clause for the SQL query");
			}
		}
		return sb.substring(0, sb.lastIndexOf(" AND "));
	}

	/**
	 * Method will prepare a readable string for UI and excel sheet.Since
	 * selected filter need to appear as readable string in both the place
	 * 
	 * @param condition
	 * @param operator
	 * @return string is a user readable string which is used in UI screen and
	 *         excel sheet as header
	 * 
	 */
	private static String convertListToStringSearchCriteria(
			List<String> condition, String operator) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in convertListToStringSearchCriteria method of KioskReportsDAOImpl ");
		}
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		for (String strObj : condition) {
			if (counter == 0) {
				builder.append("(");
				builder.append(strObj);
				builder.append(operator);
			} else {
				builder.append(strObj);
				builder.append(operator);
			}
			counter++;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from convertListToStringSearchCriteria method of KioskReportsDAOImpl ");
		}
		return builder.substring(0, builder.lastIndexOf(operator)).concat(")");
	}

	/**
	 * Method will prepare AND,OR condition based on
	 * 
	 * @param kioskFilter2
	 *            is list of criteria selected in UI for which data need to
	 *            fetched
	 * @param list
	 * 
	 * @return string which will be used in sql query as where clause
	 * 
	 */
	private static String getCondition(List<String> list,
			KioskFilter kioskFilter2) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getCondition method of KioskReportsDAOImpl ");
		}
		StringBuilder sb = new StringBuilder();
		if (PhotoOmniConstants.LAST_REBOOT.equals(kioskFilter2.getCriteria())
				|| PhotoOmniConstants.CREATED_DATE.equals(kioskFilter2
						.getCriteria())
				|| PhotoOmniConstants.UPDATED_DATE.equals(kioskFilter2
						.getCriteria())) {
			sb.append("TO_CHAR(CAST(OM_KIOSK_DEVICE_DETAIL.");
			sb.append(kioskFilter2.getCriteria());
			sb.append(" AS DATE) , 'MM-DD-YYYY')");
		} else {
			sb.append(kioskFilter2.getCriteria());
		}
		String value = " ? ";
		if ("LIKE".equals(kioskFilter2.getOperator())) {
			sb.append(" LIKE ");
			sb.append(value);
		} else {
			sb.append(kioskFilter2.getOperator());
			sb.append(value);
		}

		list.add(sb.toString());
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from getCondition method of KioskReportsDAOImpl ");
		}
		return sb.toString();
	}

	/**
	 * Method will prepare the proper data from list of filters which will
	 * further used to prepare query for KIOSK report
	 * 
	 * @param filters
	 *            is list of criteria selected in UI for which data need to
	 *            fetched
	 * 
	 * @return map of re arranged data for condition preparation
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getUniqueOriginType(
			List<KioskFilter> filters) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getUniqueOriginType method of KioskReportsDAOImpl ");
		}
		Map<String, Object> map = new LinkedHashMap<>();
		for (KioskFilter obj : filters) {
			String key = obj.getCriteria();
			if (!map.containsKey(key)) {
				List<KioskFilter> list = new ArrayList<>();
				list.add(obj);
				map.put(key, list);
			} else {
				List<KioskFilter> list = (List<KioskFilter>) map.get(key);
				list.add(obj);
				map.put(key, list);
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from getUniqueOriginType method of KioskReportsDAOImpl ");
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walgreens.admin.dao.KioskReportsDAO#getKioskFilterOptions()
	 */
	@Override
	public List<Map<String, Object>> getKioskFilterOptions()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getKioskFilterOptions method of KioskReportsDAOImpl to get filter search criteria");
		}
		List<Map<String, Object>> list = null;
		try {
			String query = ReportsQuery.getKioskFilter().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SQL query  for getKioskFilterOptions method of KioskReportsDAOImpl"
						+ query);
			}
			list = dataGuardJdbcTemplate.queryForList(query);
		} catch (Exception e) {
			throw new PhotoOmniException();
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getKioskFilterOptions method of KioskReportsDAOImpl to get filter search criteria");
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.dao.KioskReportsDAO#getExportToExcelData(java.util
	 * .List)
	 */
	@Override
	public List<Map<String, Object>> getExportToExcelData(
			List<KioskFilter> filters) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getExportToExcelData method of KioskReportsDAOImpl to get detailed data for the export to excel");
		}
		List<Map<String, Object>> list = null;
		try {
			String sql = ReportsQuery.getKioskReportExcelData().toString();
			sql += getKioskReportConditons(filters);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SQL query  for getExportToExcelData method of KioskReportsDAOImpl"
						+ sql);
			}
			list = dataGuardJdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			throw new PhotoOmniException();
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getExportToExcelData method of KioskReportsDAOImpl to get detailed data for the export to excel");
			}

		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.dao.KioskReportsDAO#getDecodedValue(java.lang.String)
	 */
	@Override
	public String getDecodedValue(String criteria) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getDecodedValue method of KioskReportsDAOImpl to get filter data for drop down");
		}
		String decodeVal;
		try {
			String sql = ReportsQuery.getCodeDecode().toString();
			Object[] objCriteria = { criteria };
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SQL query  for getDecodedValue method of KioskReportsDAOImpl"
						+ sql);
			}
			decodeVal = dataGuardJdbcTemplate.queryForObject(sql, objCriteria,
					String.class);
		} catch (Exception e) {
			throw new PhotoOmniException();
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Exiting from getDecodedValue method of KioskReportsDAOImpl to get filter data for drop down");
			}
		}
		return decodeVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.dao.KioskReportsDAO#getReportRecordPageCount(java
	 * .lang.String)
	 */
	@Override
	public int getReportRecordPageCount(String reportId)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered in getReportRecordPageCount method of KioskReportsDAOImpl to get page count");
		}
		int pageCount;
		try {
			String query = ReportsQuery.getPageCount().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("SQL query  for getReportRecordPageCount method of KioskReportsDAOImpl"
						+ query);
			}
			pageCount = dataGuardJdbcTemplate.queryForObject(query,
					new Object[] { reportId }, Integer.class);
		} catch (Exception e) {
			throw new PhotoOmniException();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting from getReportRecordPageCount method of KioskReportsDAOImpl to get page count");
		}
		return pageCount;
	}
}