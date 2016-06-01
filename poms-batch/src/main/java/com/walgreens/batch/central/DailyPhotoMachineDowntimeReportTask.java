package com.walgreens.batch.central;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DailyPhotoMachineDowntimeReportTask {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DailyPhotoMachineDowntimeReportTask.class);

	public static void main(String[] args) throws ParseException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering main method of EmailPhotoDailyMachineDowntimeReport Batch Job ");
		}
		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_EmailReport.xml" };

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context
				.getBean("EmailPhotoDailyMachineDowntimeReportJob");

		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("ReportId",
							"EmailPhotoDailyMachineDowntimeReport")
					.addString("jobSubmitTime",
							new Long(System.currentTimeMillis()).toString())
					.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Execution status -" + execution.getStatus());
			}
			System.exit(0);
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(
						" Error while execution EmailPhotoDailyMachineDowntimeReport job ",
						e.getMessage());
				System.exit(1);
			}
		}
	}

}
