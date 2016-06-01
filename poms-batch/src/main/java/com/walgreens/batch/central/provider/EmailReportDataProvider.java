package com.walgreens.batch.central.provider;

import com.walgreens.batch.central.bean.EmailReportBean;

/**
 * <p>
 * Interface for reading and processing E-Mail report data
 * </p>
 * 
 * <p>
 * Read E-mail report data in the raw form based on report type and populate the same into report bean
 * process the raw data into required form based on the report type 
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public interface EmailReportDataProvider {

	/**
	 * To populate E-Mail report data into report bean based on report type
	 * 
	 * @param emailReportBean -- Contains Email report details
	 * @return EmailReportBean -- Contains Email report details with Raw data fetched
	 */
	public EmailReportBean populateData(EmailReportBean emailReportBean);
	
	/**
	 * To process and render the raw data fetched into required format based on report type
	 * 
	 * @param emailReportBean -- Contains Email report details
	 * @return String -- returns processed E-Mail report data
	 */
	public String processEmailBody(EmailReportBean emailReportBean);

}
