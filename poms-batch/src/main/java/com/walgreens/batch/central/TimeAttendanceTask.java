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

import com.walgreens.common.utility.CommonUtil;

public class TimeAttendanceTask {

	private static final Logger log = LoggerFactory
			.getLogger(TimeAttendanceTask.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.debug("Entering into TimeAttendanceTask:main()");
		JobExecution execution = null;
		try {
			String appConfig[] = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_TimeAndAttendanceFeed.xml" };
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(
					appConfig); 
			JobParameters jobParameters = new JobParametersBuilder().addLong(
					"time", System.currentTimeMillis()).toJobParameters();

			JobLauncher jobLauncher = (JobLauncher) context
					.getBean("jobLauncher");
			Job job = (Job) context.getBean("T_A_DAILY_JOB");
			execution = jobLauncher.run(job, jobParameters);			
			log.debug("Exit Status : " + execution.getStatus());
			
		} 
		catch (Exception ex) {
			log.error(
					"Exception occurred while processing TimeAttendanceTask    ",
					ex);
			System.exit(1);
		}
		finally {
			if(!CommonUtil.isNull(execution)){
				log.debug("JOB EXECUTION STATUS :: " + execution.getStatus());
				for(Throwable ex: execution.getAllFailureExceptions()){
					log.error("Inside Finally Block ", ex);
				}
			}
			if(!CommonUtil.isNull(execution) && !"COMPLETED".equalsIgnoreCase(execution.getStatus().toString())){
				log.error(" Exception occures from Job Step Finally Block .....");
				System.exit(1);
			}
		}
		System.exit(0);

	}

}
