package com.walgreens.batch.central;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.bean.UserPrefOrDefaultRepDataBean;
import com.walgreens.batch.central.rowmapper.PSAndLcRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;

public class RoyaltyReportCustomTask {
	/**
	 * @param args
	 */
	private static JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RoyaltyReportCustomTask.class);

	public static void main(String[] args)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entered into RoyaltyReportCustomTask run method");
		}
		long prefId = 0;
		try {
			String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_RoyaltyReport.xml" };

			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
			String reportTyp = PhotoOmniConstants.ROYALTY_REPORT;
			String query = ReportsQuery.getActiveUserPrefId().toString();
			Object[] param = { reportTyp };
			jdbcTemplate = (JdbcTemplate) context.getBean("omniJdbcTemplate");
			List<UserPrefOrDefaultRepDataBean> reportIdList = jdbcTemplate
					.query(query, param, new PSAndLcRowmapper());
			for (UserPrefOrDefaultRepDataBean dataBean : reportIdList) {
				prefId = dataBean.getReportPrefId();
				JobParameters jobParameters = new JobParametersBuilder()
						.addLong(PhotoOmniConstants.REPORID, prefId)
						.addString(PhotoOmniConstants.JOBSUBMITTIME,
								new Long(System.currentTimeMillis()).toString())
						.toJobParameters();
				JobLauncher jobLauncher = (JobLauncher) context
						.getBean(PhotoOmniConstants.JOBLAUNCHER);
				Job job = (Job) context.getBean("RoyaltyCustomreportJob");
				if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Royalty report Batch for report id : " + prefId);
				}
				jobLauncher.run(job, jobParameters);
			}
			System.exit(0);
		} catch (Exception e) {
			LOGGER.error(" Error occoured while runing Royalty custom report job in RoyaltyReportCustomTask run method --->  "
					+ e);
			System.exit(1);
		}finally{
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting from RoyaltyReportCustomTask.run() method");
				}
		}
	}
}
