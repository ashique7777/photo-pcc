package com.walgreens.batch.central.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.walgreens.batch.central.bo.ReportsUtilBO;


@Component
@Scope("singleton")
public class ReportBOFactory {

	@Autowired
	@Qualifier("ReportsUtilBO")
	private ReportsUtilBO reportsUtilBO;

	/*
	 * Return reports util bo 
	 * 
	 * @return ReportsUtilBO
	 */
	public ReportsUtilBO getReportsUtilBO() {
		return reportsUtilBO;
	}

	public void setReportsUtilBO(ReportsUtilBO reportsUtilBO) {
		this.reportsUtilBO = reportsUtilBO;
	}
}
