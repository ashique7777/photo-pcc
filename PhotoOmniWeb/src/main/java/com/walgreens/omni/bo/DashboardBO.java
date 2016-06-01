package com.walgreens.omni.bo;

import java.util.List;

import com.walgreens.omni.bean.DashboardReportBean;

public interface DashboardBO {

	List<DashboardReportBean> getActiveReportByUser(long userId);

	DashboardReportBean getDashboardReportBean(String key, Boolean defaultFlag,
			long userId);
	
	long getUserID();  

}
