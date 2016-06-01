/**
 * LCAdhocAndExceptionCustomReportTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 03 March 2015
 *  
 **/
package com.walgreens.batch.central;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.UserPrefOrDefaultRepDataBean;
import com.walgreens.batch.central.rowmapper.PSAndLcRowmapper;
import com.walgreens.batch.query.LicenseContentReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
/**
 * This class to launch job for Adhoc and Exception report.
 * 
 * @author CTS
 * @version 1.1 Mar 03, 2015
 * 
 */

public class LCAdhocAndExceptionCustomReportTask {
	/**
     * jdbcTemplate
     */
	private static JdbcTemplate jdbcTemplate;
	
	/**
     * LOGGER for logging
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LCAdhocAndExceptionCustomReportTask.class);

	/**
	 * This method launch job for Adhoc and Exception report.
	 */
    public static void main(String[] args) {
	/*public  void run() {*/
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of LCAdhocAndExceptionCustomReportTask ");
		}
		long prefId = 0;
		try {
			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_LicenseContentReport.xml"
				};
		
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		jdbcTemplate = (JdbcTemplate)context.getBean("omniJdbcTemplate");
		final String reportTyp = PhotoOmniConstants.ADHOC_REPORT_COLUMN.trim().toUpperCase();
		final String query = LicenseContentReportQuery.getActiveUserPrefId().toString();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" License Content SQL Query is : "+ query);
		}
		final Object[] param = {reportTyp};
		final List<UserPrefOrDefaultRepDataBean> reportIdList = (List<UserPrefOrDefaultRepDataBean>)
				jdbcTemplate.query(query, param, new PSAndLcRowmapper());
		LOGGER.info(" ADHOC and Exception report batch running time "+ new Date() + " for " + reportIdList.size() + " report Ids" );
		/*Remove later below line*/
		System.out.println(" ADHOC and Exception report Running time "+ new Date() + " for " + reportIdList.size() + " reportIds");
		for(UserPrefOrDefaultRepDataBean dataBean: reportIdList) {
			prefId = dataBean.getReportPrefId();
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addLong("REPORT_ID", prefId);
			jobBuilder.addLong("CURRENT_TIME", new Date().getTime());
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			final Job job = (Job) context.getBean("LicenseContentJob");
			LOGGER.info(" ADHOC and Exception report Batch for report id : " + prefId );
			/*Remove later below line*/
			System.out.println("ADHOC and Exception report Batch for report id : " + prefId);
			jobLauncher.run(job, jobParameters);
			}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at run method of LCAdhocAndExceptionCustomReportTask - for report id : " + prefId);
				LOGGER.error(" Error occoured at run method of LCAdhocAndExceptionCustomReportTask - ", e);
				
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting run method of LCAdhocAndExceptionCustomReportTask ");
				}
				System.exit(0);
			}

		}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
}
