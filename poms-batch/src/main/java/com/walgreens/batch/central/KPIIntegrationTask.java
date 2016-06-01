package com.walgreens.batch.central;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

public class KPIIntegrationTask {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIIntegrationTask.class);

	private static ApplicationContext context;

	public static void main(String[] args) {
		boolean flag = LOGGER.isDebugEnabled();
		if (flag) {
			LOGGER.debug(" Entering main method of KPI Integration Batch Job ");
		}
		try {
			String appConfig[] = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PhotoKPIFeed.xml" };
			context = new ClassPathXmlApplicationContext(
					appConfig);
			JobLauncher jobLauncher = (JobLauncher) context
					.getBean(PhotoOmniConstants.JOB_LAUNCHER);
			Job job = (Job) context
					.getBean(PhotoOmniBatchConstants.KPI_INTEGRATION_JOB);
			String currentTimeStamp = CommonUtil.getCurrentTimeStamp();

			JobParameters jobParameters = new JobParametersBuilder()
					.addString(PhotoOmniConstants.JOB_SUBMIT_TIME,
							new Long(System.currentTimeMillis()).toString())
					.addString(
							"kpiFeedfileName",
							PhotoOmniBatchConstants.KPI_FEED_FILE_NAME_PATTERN
									+ CommonUtil
											.stringDateFormatChange(
													currentTimeStamp,
													PhotoOmniConstants.STORE_DATE_PATTERN,
													PhotoOmniConstants.DATE_FORMAT_TWELFTH))
					.addString(
							"currentDate",
							CommonUtil.stringDateFormatChange(currentTimeStamp,
									PhotoOmniConstants.STORE_DATE_PATTERN,
									PhotoOmniConstants.DATE_FORMAT_ZERO)
									+ PhotoOmniConstants.DAY_BEGIN_TIME)
					.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			LOGGER.debug("Exit Status : " + execution.getStatus());
			LOGGER.debug("Exit Status : " + execution.getExitStatus());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at run method of KPIIntegrationTask - "
					+ e.getMessage());
			System.exit(1);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of KPIIntegrationTask ");
			}
		}
		System.exit(0);
	}
}