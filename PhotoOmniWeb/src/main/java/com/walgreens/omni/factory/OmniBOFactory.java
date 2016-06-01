package com.walgreens.omni.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.walgreens.omni.bo.OmniWebEnvConfigBo;
import com.walgreens.omni.bo.ReportsBO;

/**
* @author CTS
* @param <E>
* @version 1.1 Mar 17, 2015
*/

@Component
@Scope("singleton")
public class OmniBOFactory {	
	
	
	@Autowired
	@Qualifier("OmniWebEnvConfigBo")
	private OmniWebEnvConfigBo omniWebEnvConfigBo;
	
	@Autowired
	@Qualifier("ReportsBO")
	private ReportsBO exceptionReportBO;

	/**
	 * @return the exceptionReportBO
	 */
	public ReportsBO getExceptionReportBO() {
		return exceptionReportBO;
	}

	/**
	 * @param exceptionReportBO the exceptionReportBO to set
	 */
	public void setExceptionReportBO(ReportsBO exceptionReportBO) {
		this.exceptionReportBO = exceptionReportBO;
	}

	public OmniWebEnvConfigBo getOmniWebEnvConfigBo() {
		return omniWebEnvConfigBo;
	}

	public void setOmniWebEnvConfigBo(OmniWebEnvConfigBo omniWebEnvConfigBo) {
		this.omniWebEnvConfigBo = omniWebEnvConfigBo;
	}
	
	/**canned report changes for Phase-2.0*/
	public ReportsBO getCannedReportBO() {
		return cannedReportBO;
	}

	public void setCannedReportBO(ReportsBO cannedReportBO) {
		this.cannedReportBO = cannedReportBO;
	}

	@Autowired
	@Qualifier("ReportsBO")
	private ReportsBO cannedReportBO;
	


	
}
