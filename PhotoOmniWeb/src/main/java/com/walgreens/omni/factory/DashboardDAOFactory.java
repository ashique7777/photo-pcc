package com.walgreens.omni.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.omni.dao.DashboardDAO;


@Component
@Scope("singleton")
public class DashboardDAOFactory {
	
	@Autowired
	@Qualifier("dashboardDAO")
	private DashboardDAO dashboardDAO;
	
	

	public DashboardDAO getDashboardDAO() {
		return dashboardDAO;
	}

	public void setDashboardDAO(DashboardDAO dashboardDAO) {
		this.dashboardDAO = dashboardDAO;
	}

	
	

}
