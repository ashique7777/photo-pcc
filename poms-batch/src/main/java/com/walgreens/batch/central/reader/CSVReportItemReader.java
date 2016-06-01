package com.walgreens.batch.central.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.rowmapper.UserPrefRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item reader implements Spring itemReader 
 * </p>
 * 
 * <p>
 * Call to read filter criteria of the adoc report using adoc reportID
 * </p>
 * 
 * {@link CSVReportItemReader} is a business implementation class for {@link ItemReader}
 * @author CTS
 * @since v1.0
 */
public class CSVReportItemReader implements ItemReader<PMBYWICReportPrefDataBean>{

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	int count = 0;
	
	private UserPrefRowmapper objUserPrefRowmapper = null;
	
	private String strjobExecutionstepName;
	
	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;

	private Long reportId;
	@SuppressWarnings("unused")
	private long jobSubmitTime;

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVReportItemReader.class);

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}
	
	/**
	 * Method to read filter criteria for PmBYWicReport using reportId
	 * 
	 * @return PMBYWICReportPrefDataBean -- Bean contains filter criteria
	 * @throws PhotoOmniException -- Custom exception
	 */
	public PMBYWICReportPrefDataBean read() throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into CSVReportItemReader.read()");
		}

		//reportId = 2L;
		if(count == 0){
			count++;
			objUserPrefRowmapper = new UserPrefRowmapper();
			objPMBYWICReportPrefDataBean = new PMBYWICReportPrefDataBean();
			String strUserPrefDtlsSelectQuery = "";
			try{
				if("PMByWICMpnthlyreportJobstep1".equals(strjobExecutionstepName)){
					strUserPrefDtlsSelectQuery = ReportsQuery.getDefaultUserPrefQuery(reportId).toString();
				}else{
					strUserPrefDtlsSelectQuery = ReportsQuery.getUserPrefQuery(reportId).toString();
				}
				objPMBYWICReportPrefDataBean = jdbcTemplate.query(strUserPrefDtlsSelectQuery, objUserPrefRowmapper).get(0);
				objPMBYWICReportPrefDataBean.setSysUserReportPrefId(reportId);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Exiting From CSVReportItemReader.read()");
				}
			}catch (Exception e) {
				LOGGER.error(" Error occoured at CSVReportItemProcessor.read() ---> " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting From CSVReportItemProcessor.read() ");
				}
			}
			return objPMBYWICReportPrefDataBean;
		}else{
			return null;
		}
	}

	/**
	 * method to set execution context variable 
	 * 
	 * @param stepExecution -- Execution context holder
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering into CSVReportItemReader.beforeStep()");
		}
		strjobExecutionstepName = stepExecution.getStepName();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exiting From CSVReportItemReader.beforeStep()");
		}
	}
}