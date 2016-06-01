package com.walgreens.batch.central.bean;

import java.io.Serializable;


/**
 * @author CTS
 *
 */

public class POFEmailDataBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int sysReportEmailConfigId ;
	int sysReportId ;
	String configName;
	String emailId;
	String emailType;
	int activeCd ;
	
	
	
	/**
	 * @return the emailType
	 */
	public String getEmailType() {
		return emailType;
	}
	/**
	 * @param emailType the emailType to set
	 */
	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}
	public int getSysReportEmailConfigId() {
		return sysReportEmailConfigId;
	}
	public void setSysReportEmailConfigId(int sysReportEmailConfigId) {
		this.sysReportEmailConfigId = sysReportEmailConfigId;
	}
	public int getSysReportId() {
		return sysReportId;
	}
	public void setSysReportId(int sysReportId) {
		this.sysReportId = sysReportId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public int getActiveCd() {
		return activeCd;
	}
	public void setActiveCd(int activeCd) {
		this.activeCd = activeCd;
	}
	
	

}
