package com.walgreens.batch.central;

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
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;

public final class AdhocPLUTask {

	/**
	 * Private constructor as this 
	 * class is final 
	 */
	private AdhocPLUTask() {

	}
	/**
	 * jdbcTemplate
	 */
	private static JdbcTemplate jdbcTemplate;
	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AdhocPLUTask.class);

	private static JobParameters jobParameters;

	public static void main(final String[] args) {
		LOGGER.info("Entered into AdhocPLUTask run method");
		try {
			long prefId;
			final String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PLUReport.xml" };
			@SuppressWarnings("resource")
			final ApplicationContext context = new ClassPathXmlApplicationContext(
					springConfig);
			final String reportTyp = PhotoOmniConstants.PLU_REPORT;
			final String query = ReportsQuery.getActiveUserPrefId().toString();
			final Object[] param = { reportTyp };
			jdbcTemplate = (JdbcTemplate) context.getBean("omniJdbcTemplate");
			final List<UserPrefOrDefaultRepDataBean> reportIdList = jdbcTemplate
					.query(query, param, new PSAndLcRowmapper());
			final JobLauncher jobLauncher = (JobLauncher) context
					.getBean("jobLauncher");
			final Job job = (Job) context.getBean("PLUJob");
			JobParametersBuilder jobParmBuilder = new JobParametersBuilder(); // NOPMD by kumarxan on 8/6/15 6:09 PM
			for (UserPrefOrDefaultRepDataBean dataBean : reportIdList) {
				prefId = dataBean.getReportPrefId();
				jobParameters = jobParmBuilder
						.addLong("ReportId", prefId)
						.addString("jobSubmitTime",
								Long.toString(System.currentTimeMillis()))
						.toJobParameters();
				LOGGER.info(" Adhoc PLU report Batch for report id : " + prefId);
				jobLauncher.run(job, jobParameters);
			}
			System.exit(0); // NOPMD by kumarxan on 8/6/15 5:19 PM
		} catch (Exception e) {
			LOGGER.error(" Error occoured while runing Adhoc PLU report job in AdhocPLUTask run method "
					+ e.getMessage());
			System.exit(1); // NOPMD by kumarxan on 8/6/15 5:19 PM
		}
	}
}
