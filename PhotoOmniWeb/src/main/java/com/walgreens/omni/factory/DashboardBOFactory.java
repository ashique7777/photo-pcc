package com.walgreens.omni.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.omni.bo.DashboardBO;


@Component
@Scope("singleton")
public class DashboardBOFactory {

	@Autowired
	@Qualifier("dashboardBO")
	private DashboardBO dashboardBO;
	
	

	public DashboardBO getDashboardBO() {
		return dashboardBO;
	}

	public void setDashboardBO(DashboardBO dashboardBO) {
		this.dashboardBO = dashboardBO;
	}

}
