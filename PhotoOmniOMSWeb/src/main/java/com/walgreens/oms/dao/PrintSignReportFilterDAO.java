package com.walgreens.oms.dao;

import java.util.List;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.oms.json.bean.EventData;

/**
 * @author CTS
 * @version 1.1 Mar 18, 2015
 * 
 */
public interface PrintSignReportFilterDAO {
	
	public  List<EventData> getEventTypDetails(final String params)  throws PhotoOmniException;
	
	public int submitPSReportFilterRequest(final String strReportData, final int sysReportId) throws PhotoOmniException;
	
	public int getSysUserId() throws PhotoOmniException;
	
	public Integer getLastEnteredDataForPrintSign(final int reportId) throws PhotoOmniException;
	
	public Integer getReportIdForPrintSignReport() throws PhotoOmniException;

}
