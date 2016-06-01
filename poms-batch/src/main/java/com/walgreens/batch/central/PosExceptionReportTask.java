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

public class PosExceptionReportTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PosExceptionReportTask.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_POS.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(
				springConfig);
		

		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("posCreateExceptionJob");

		try {
			
			JobParameters jobParameters = new JobParametersBuilder()
			  .addLong("jobSubmitTime",new Long(System.currentTimeMillis())).toJobParameters();


			if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into PosExceptionReportTask run method");
			}
			JobExecution execution = jobLauncher.run(job, jobParameters);
			
			if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit from PosExceptionReportTask run method");
			}
			if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Exit Status : " + execution.getStatus());
			}
			
            System.exit(0);

		} catch (Exception e) {
			if (LOGGER.isErrorEnabled()) {
			 LOGGER.error(" Error occoured while runing PosException job in PosExceptionReportTask run method --->  " , e);
			 }
			 System.exit(1);
		}

	}

}
