package com.walgreens.omni.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.omni.dao.OmniWebEnvConfigDAO;
import com.walgreens.omni.dao.ReportsDAO;

/**
* @author CTS
* @param <E>
* @version 1.1 Mar 17, 2015
*/

@Component
@Scope("singleton")
public class OmniDAOFactory {
	
	@Autowired
	@Qualifier("OmniWebEnvConfigDAO")
	private OmniWebEnvConfigDAO omniWebEnvConfigDAO;
	
	@Autowired
	@Qualifier("ReportDAO")
	private ReportsDAO exceptionReportDAO;
	@Autowired
	@Qualifier("ReportDAO")
	private ReportsDAO cannedReportDAO;

	public ReportsDAO getCannedReportDAO() {
		return cannedReportDAO;
	}

	public void setCannedReportDAO(ReportsDAO cannedReportDAO) {
		this.cannedReportDAO = cannedReportDAO;
	}

	/**
	 * @return the exceptionReportDAO
	 */
	public ReportsDAO getExceptionReportDAO() {
		return exceptionReportDAO;
	}

	/**
	 * @param exceptionReportDAO the exceptionReportDAO to set
	 */
	public void setExceptionReportDAO(ReportsDAO exceptionReportDAO) {
		this.exceptionReportDAO = exceptionReportDAO;
	}


	public OmniWebEnvConfigDAO getOmniWebEnvConfigDAO() {
		return omniWebEnvConfigDAO;
	}

	public void setOmniWebEnvConfigDAO(OmniWebEnvConfigDAO omniWebEnvConfigDAO) {
		this.omniWebEnvConfigDAO = omniWebEnvConfigDAO;
	}
	

	

}
