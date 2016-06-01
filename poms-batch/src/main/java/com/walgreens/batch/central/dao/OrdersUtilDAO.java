package com.walgreens.batch.central.dao;

import java.util.List;
import java.util.Map;

import com.walgreens.common.exception.PhotoOmniException;
/**
 * <p>
 * A utility Interface for DAO transactions
 * </p>
 *
 * @author CTS
 * @since v1.0
 */
public interface OrdersUtilDAO {

	/**
	 * This method handles database transaction to get EMAIL id from OM_REPORT_EMAIL_CONFIG table.
	 * 
	 * @param reportConfigName contains reports config name.
	 * @return machineList
	 */
	List<Map<String, Object>> getReportData(String reportName) throws PhotoOmniException;
	
	/**
	 * To Fetch from Email id for reports from the configuration using the reportName configured 
	 * for the report 
	 * 
	 * @param reportConfigName -- Specifies the type of report
	 * @return StringBuilder -- String of reportId, Email details
	 * @throws PhotoOmniException -- Custom exception defined in the application
	 */
	public StringBuilder getFromEmailId(String reportConfigName)  throws PhotoOmniException;

}
