package com.walgreens.batch.central.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walgreens.batch.central.StoreCloningTask;

public class StoreCloningTaskTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreCloningTaskTest.class);

	@Test
	public void testMain() {
		
		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job-report.xml" };
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("storeCloningJob");
		
		try{
			JobParameters jobParameters = new JobParametersBuilder().addString("jobSubmitTime",new Long(System.currentTimeMillis()).toString()).toJobParameters();
			
			LOGGER.info("Entered into StoreCloningTask run method");
			JobExecution execution = jobLauncher.run(job, jobParameters);
			
			assertEquals("COMPLETED", execution.getStatus());
			
			LOGGER.info("Exit from StoreCloningTask run method");
			LOGGER.info("Exit Status : " + execution.getStatus());
			System.exit(0);
			
		}catch (Exception e){
			LOGGER.error(" Error occoured while runing StoreCloning job in StoreCloningTask run method --->  " + e.getMessage());
			System.exit(1);
		}
	}

}
