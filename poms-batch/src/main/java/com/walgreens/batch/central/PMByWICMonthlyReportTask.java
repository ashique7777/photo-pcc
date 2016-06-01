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
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.DefaultReportRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;

public class PMByWICMonthlyReportTask {
	/**
	 * @param args
	 */
	private static JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory.getLogger(PMByWICMonthlyReportTask.class);
	public static void main(String[] args)  throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {

		LOGGER.info("Entering into PMByWICMonthlyReportTask.run()");
		long prefId = 0;
		try {
			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PMByWIC.xml" 
				};

			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
			String reportTyp = PhotoOmniBatchConstants.PMByWIC_REPORT;
			String query = ReportsQuery.getActiveDefaultPrefId().toString();
			Object[] param = {reportTyp};
			jdbcTemplate = (JdbcTemplate) context.getBean("omniJdbcTemplate");
			List<UserPrefOrDefaultRepDataBean> reportIdList =jdbcTemplate.query(query, param, new  DefaultReportRowmapper());
			for (UserPrefOrDefaultRepDataBean dataBean : reportIdList) {
				prefId = dataBean.getReportPrefId();
				JobParameters jobParameters = new JobParametersBuilder()
						.addLong("ReportId", prefId)
						.addString("jobSubmitTime",
								new Long(System.currentTimeMillis()).toString())
						.toJobParameters();
				JobLauncher jobLauncher = (JobLauncher) context
						.getBean("jobLauncher");
				Job job = (Job) context.getBean("PMByWICMonthlyreportJob");
				LOGGER.info(" PMByWIC Monthly report Batch for report id : "
						+ prefId);
				jobLauncher.run(job, jobParameters);
			}
			System.exit(0);
		}catch (Exception e) {
			LOGGER.error(" Error occoured at PMByWICMonthlyReportTask.run()--->  " + e.getMessage());
			System.exit(1);
		}
	}
}
