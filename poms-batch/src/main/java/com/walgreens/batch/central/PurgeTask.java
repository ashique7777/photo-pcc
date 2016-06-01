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

public class PurgeTask {

	private static final Logger log = LoggerFactory
			.getLogger(TimeAttendanceTask.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("Entering into SilverCanisterReportApp:main()");
		try {
			String appConfig[] = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_PurgeProcess.xml" };
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(
					appConfig); 
			JobParameters jobParameters = new JobParametersBuilder().addLong(
					"time", System.currentTimeMillis()).toJobParameters();

			JobLauncher jobLauncher = (JobLauncher) context
					.getBean("jobLauncher");
			Job job = (Job) context.getBean("deleteFilesInArchiveFolders");
			JobExecution execution = jobLauncher.run(job, jobParameters);
			System.out.println(execution.getStatus());
			log.debug("Exit Status : " + execution.getStatus());
			System.exit(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error(
					"Exception occurred while processing SilverCanisterReport    ",
					ex);
			System.exit(1);
		}

	}
	
}
