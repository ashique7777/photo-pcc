package com.walgreens.omni.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.common.security.oam.SAMLUserDetails;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.omni.bean.DashboardReportBean;

/**
 * 
 * @author CTS
 * 
 */
@Component("dashboardDAO")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class DashboardDAOImpl implements DashboardDAO {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DashboardDAOImpl.class);

	/**
	 * @param userId
	 * @return list of available report for the user in default report table and
	 *         user pref report table
	 */
	@SuppressWarnings("deprecation")
	@Override
	public List<DashboardReportBean> getActiveReportByUser(long userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Entered getActiveReportByUser method of DashboardDAOImpl for the userId --> {}",
					userId);
		}
		List<DashboardReportBean> list = new ArrayList<DashboardReportBean>();
		String sql = "SELECT RPT.SYS_REPORT_ID,RPT.REPORT_NAME,DEFRPT.CREATE_USER_ID,DEFRPT.PREFERENCE_TYPE, "
				+ "DEFRPT.FILTER_STATE,DEFRPT.SORT_STATE,DEFRPT.HIDDEN_COLUMNS,DEFRPT.AUTO_REFRESH_CD, "
				+ "DEFRPT.AUTO_EXECUTE_CD,DEFRPT.AUTO_REFRESH_INTERVAL,DEFRPT.UPDATE_USER_ID, "
				+ "DEFRPT.CREATE_DTTM,DEFRPT.UPDATE_DTTM,DEFRPT.PAGE_SIZE,DEFRPT.FILTER_ENABLED_CD FROM OM_REPORT RPT "
				+ "INNER JOIN OM_DEFAULT_REPORT_PREFS DEFRPT ON RPT.SYS_REPORT_ID = DEFRPT.SYS_REPORT_ID "
				+ "WHERE DEFRPT.PREFERENCE_TYPE = 'UI' AND DEFRPT.ACTIVE_CD=1 ";
		List<Map<String, Object>> dataMap = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> map : dataMap) {
			DashboardReportBean bean = new DashboardReportBean();
			bean.setReportId(CommonUtil.bigDecimalToLong(map
					.get("SYS_REPORT_ID")));
			bean.setUserId(0);
			bean.setReportName((String) map.get("REPORT_NAME"));
			bean.setCrateUserId((String) map.get("CREATE_USER_ID"));
			bean.setPreferenceType((String) map.get("PREFERENCE_TYPE"));
			bean.setFilterState((String) map.get("FILTER_STATE"));
			bean.setSortState((String) map.get("SORT_STATE"));
			bean.setHiddenColumn((String) map.get("HIDDEN_COLUMNS"));
			Long autoRefresh = CommonUtil.bigDecimalToLong(map
					.get("AUTO_REFRESH_CD"));
			bean.setAutoRefresh(autoRefresh.longValue() == 1 ? true : false);
			Long autoExecute = CommonUtil.bigDecimalToLong(map
					.get("AUTO_EXECUTE_CD"));
			bean.setAutoExecute(autoExecute.longValue() == 1 ? true : false);
			bean.setRefreshInterval(CommonUtil.bigDecimalToLong(map
					.get("AUTO_REFRESH_INTERVAL")));
			bean.setUpdateUserId((String) map.get("UPDATE_USER_ID"));
			bean.setCreatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
					.format(map.get("CREATE_DTTM"))));
			bean.setUpdatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
					.format(map.get("UPDATE_DTTM"))));
			bean.setPageSize(CommonUtil.bigDecimalToLong(map.get("PAGE_SIZE")));
			Long filterEnabled = CommonUtil.bigDecimalToLong(map
					.get("FILTER_ENABLED_CD"));
			bean.setFilterEnabled(filterEnabled.longValue() == 1 ? true : false);
			bean.setDefaultItem(true);
			list.add(bean);
		}
		sql = "SELECT RPT.SYS_REPORT_ID,RPT.REPORT_NAME,USRRPT.SYS_USER_ID,USRRPT.CREATE_USER_ID,USRRPT.PREFERENCE_TYPE, "
				+ "USRRPT.FILTER_STATE,USRRPT.SORT_STATE,USRRPT.HIDDEN_COLUMNS,USRRPT.AUTO_REFRESH_CD, "
				+ "USRRPT.AUTO_EXECUTE_CD,USRRPT.AUTO_REFRESH_INTERVAL,USRRPT.UPDATE_USER_ID, "
				+ "USRRPT.CREATE_DTTM,USRRPT.UPDATE_DTTM,USRRPT.PAGE_SIZE,USRRPT.FILTER_ENABLED_CD FROM OM_REPORT RPT "
				+ "INNER JOIN OM_USER_REPORT_PREFS USRRPT ON RPT.SYS_REPORT_ID = USRRPT.SYS_REPORT_ID "
				+ "WHERE USRRPT.PREFERENCE_TYPE = 'UI' AND USRRPT.ACTIVE_CD=1 AND USRRPT.SYS_USER_ID = ? ";
		dataMap = jdbcTemplate.queryForList(sql, userId);
		for (Map<String, Object> map : dataMap) {
			DashboardReportBean bean = new DashboardReportBean();
			bean.setReportId(CommonUtil.bigDecimalToLong(map
					.get("SYS_REPORT_ID")));
			bean.setUserId(CommonUtil.bigDecimalToLong(map.get("SYS_USER_ID")));
			bean.setReportName((String) map.get("REPORT_NAME"));
			bean.setCrateUserId((String) map.get("CREATE_USER_ID"));
			bean.setPreferenceType((String) map.get("PREFERENCE_TYPE"));
			bean.setFilterState((String) map.get("FILTER_STATE"));
			bean.setSortState((String) map.get("SORT_STATE"));
			bean.setHiddenColumn((String) map.get("HIDDEN_COLUMNS"));
			Long autoRefresh = CommonUtil.bigDecimalToLong(map
					.get("AUTO_REFRESH_CD"));
			bean.setAutoRefresh(autoRefresh.longValue() == 1 ? true : false);
			Long autoExecute = CommonUtil.bigDecimalToLong(map
					.get("AUTO_EXECUTE_CD"));
			bean.setAutoExecute(autoExecute.longValue() == 1 ? true : false);
			bean.setRefreshInterval(CommonUtil.bigDecimalToLong(map
					.get("AUTO_REFRESH_INTERVAL")));
			bean.setUpdateUserId((String) map.get("UPDATE_USER_ID"));
			bean.setCreatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
					.format(map.get("CREATE_DTTM"))));
			bean.setUpdatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
					.format(map.get("UPDATE_DTTM"))));
			bean.setPageSize(CommonUtil.bigDecimalToLong(map.get("PAGE_SIZE")));
			Long filterEnabled = CommonUtil.bigDecimalToLong(map
					.get("FILTER_ENABLED_CD"));
			bean.setFilterEnabled(filterEnabled.longValue() == 1 ? true : false);
			bean.setDefaultItem(false);
			list.add(bean);
		}
		return list;
	}

	/**
	 * @param key
	 *            : unique key reportName
	 * @param defaultFlag
	 *            : flag to check for which table we need to query
	 *            om_default_report_pref or om_user_report_pref
	 * @return DashboardReportBean
	 */
	@SuppressWarnings("deprecation")
	@Override
	public DashboardReportBean getDashboardReportBean(String key,
			Boolean defaultFlag, long userId) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Entered getDashboardReportBean method of DashboardDAOImpl for the report name and userId --> {}, {}",
					key, userId);
		}
		String tableName = defaultFlag ? "OM_DEFAULT_REPORT_PREFS"
				: "OM_USER_REPORT_PREFS";
		String sql = "SELECT RPT.SYS_REPORT_ID,RPT.REPORT_NAME,DEFRPT.CREATE_USER_ID,DEFRPT.PREFERENCE_TYPE, "
				+ "DEFRPT.FILTER_STATE,DEFRPT.SORT_STATE,DEFRPT.HIDDEN_COLUMNS,DEFRPT.AUTO_REFRESH_CD, "
				+ "DEFRPT.AUTO_EXECUTE_CD,DEFRPT.AUTO_REFRESH_INTERVAL,DEFRPT.UPDATE_USER_ID, "
				+ "DEFRPT.CREATE_DTTM,DEFRPT.UPDATE_DTTM,DEFRPT.PAGE_SIZE "
				+ " FROM OM_REPORT RPT INNER JOIN "
				+ tableName
				+ " DEFRPT ON RPT.SYS_REPORT_ID = DEFRPT.SYS_REPORT_ID "
				+ " WHERE DEFRPT.PREFERENCE_TYPE = 'UI' AND DEFRPT.ACTIVE_CD=1 AND RPT.REPORT_NAME=? ";
		if (!defaultFlag) {
			sql += "AND DEFRPT.SYS_USER_ID = " + userId;
		}
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, key);
		DashboardReportBean bean = new DashboardReportBean();
		bean.setReportId(CommonUtil.bigDecimalToLong(map.get("SYS_REPORT_ID")));
		bean.setUserId(userId);
		bean.setReportName((String) map.get("REPORT_NAME"));
		bean.setCrateUserId((String) map.get("CREATE_USER_ID"));
		bean.setPreferenceType((String) map.get("PREFERENCE_TYPE"));
		bean.setFilterState((String) map.get("FILTER_STATE"));
		bean.setSortState((String) map.get("SORT_STATE"));
		bean.setHiddenColumn((String) map.get("HIDDEN_COLUMNS"));
		bean.setAutoRefresh(Boolean.parseBoolean(bigDecimaltoBoolean(map
				.get("AUTO_REFRESH_CD"))));
		bean.setAutoExecute(Boolean.parseBoolean(bigDecimaltoBoolean(map
				.get("AUTO_EXECUTE_CD"))));
		bean.setRefreshInterval(CommonUtil.bigDecimalToLong(map
				.get("AUTO_REFRESH_INTERVAL")));
		bean.setUpdateUserId((String) map.get("UPDATE_USER_ID"));
		bean.setCreatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
				.format(map.get("CREATE_DTTM"))));
		bean.setUpdatedDate(new Date(new SimpleDateFormat("MM/dd/yyyy")
				.format(map.get("UPDATE_DTTM"))));
		bean.setPageSize(CommonUtil.bigDecimalToLong(map.get("PAGE_SIZE")));
		return bean;
	}

	private static String bigDecimaltoBoolean(Object objVal) {
		if (objVal == null) {
			return "false";
		} else {
			BigDecimal bigDecimalVal = new BigDecimal(objVal.toString());
			return (bigDecimalVal.intValue() == 1) ? "true" : "false";
		}
	}

	@Override
	public long getUserID() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered getUserId method of DashboardDAOImpl");
		}
		String empId = null;
		if (SecurityContextHolder.getContext().getAuthentication() instanceof ExpiringUsernameAuthenticationToken) {
			ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();
			empId = ((SAMLUserDetails) authentication.getDetails())
					.getEmployeenumber();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"Logged-in employeeId from getUserId method of DashboardDAOImpl ---> {}",
						empId);
			}
			String sql = " SELECT SYS_USER_ID FROM OM_USER_ATTRIBUTES WHERE EMPLOYEE_ID = ? ";
			return jdbcTemplate.queryForObject(sql, Long.class, empId);
		} else
			return 0;
	}
}