package com.walgreens.omni.dao;

import java.util.List;

import com.walgreens.omni.bean.DashboardReportBean;

public interface DashboardDAO {

	List<DashboardReportBean> getActiveReportByUser(long userId);

	DashboardReportBean getDashboardReportBean(String key, Boolean defaultFlag,
			long userId);
	
	long getUserID();
}
