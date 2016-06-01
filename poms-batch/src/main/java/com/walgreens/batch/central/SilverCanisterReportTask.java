package com.walgreens.batch.central;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Cognizant
 * This class is responsible to execute SilverCanisterReport batch
 *
 */
public class SilverCanisterReportTask {
	
	private static final Logger log = LoggerFactory.getLogger(SilverCanisterReportTask.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		log.debug("Entering into SilverCanisterReportApp:main()");
		try{
			String appConfig[]={
					"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_SilverCanisterReport.xml" 
			};			
			@SuppressWarnings("resource")
			
			ApplicationContext context = new ClassPathXmlApplicationContext(appConfig);		
			
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addLong("CURRENT_TIME", new Date().getTime());
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			Job job = (Job) context.getBean("SilverCanisterJob");
			//Job job = (Job) context.getBean("sftpJob");
			JobExecution execution = jobLauncher.run(job, jobParameters);
			log.debug("Exit Status : " + execution.getStatus());
			System.exit(0);
		}
		catch(Exception ex){
			log.error("Exception occurred while processing SilverCanisterReport    ",ex);
			System.exit(1);
		}

	}

}
