package com.walgreens.omni.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.omni.bean.DashboardReportBean;
import com.walgreens.omni.dao.DashboardDAO;
import com.walgreens.omni.factory.DashboardDAOFactory;

/**
 * 
 * @author CTS
 * 
 */
@Component("dashboardBO")
public class DashboardBOImpl implements DashboardBO {

	@Autowired
	private DashboardDAOFactory dashboardDAOFactory;

	/**
	 * @param userId
	 * @return list of available report for the user in default report table and
	 *         user pref report table
	 */
	@Override
	public List<DashboardReportBean> getActiveReportByUser(long userId) {
		DashboardDAO dashboardDAO = dashboardDAOFactory.getDashboardDAO();
		return dashboardDAO.getActiveReportByUser(userId);
	}

	/**
	 * @param key
	 *            : unique key reportName
	 * @param defaultFlag
	 *            : flag to check for which table we need to query
	 *            om_default_report_pref or om_user_report_pref
	 * @return DashboardReportBean
	 */
	@Override
	public DashboardReportBean getDashboardReportBean(String key,
			Boolean defaultFlag, long userId) {
		DashboardDAO dashboardDAO = dashboardDAOFactory.getDashboardDAO();
		return dashboardDAO.getDashboardReportBean(key, defaultFlag, userId);
	}

	@Override
	public long getUserID() {
		DashboardDAO dashboardDAO = dashboardDAOFactory.getDashboardDAO();
		return dashboardDAO.getUserID();
	}

}
