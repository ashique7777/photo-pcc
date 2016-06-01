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

public class StoreCloningTask {
	
	/**
	 * @param args
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreCloningTask.class);
	
	
	public static void main(String[] args) {
		
		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_StoreCloning.xml" };
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("storeCloningJob");
		JobExecution jobExecution=null;
		try{
			JobParameters jobParameters = new JobParametersBuilder().addString("jobSubmitTime",new Long(System.currentTimeMillis()).toString()).toJobParameters();
			
			LOGGER.info("Entered into StoreCloningTask run method");
			jobExecution = jobLauncher.run(job, jobParameters);
			
			LOGGER.info("Exit from StoreCloningTask run method");
			LOGGER.info("Exit Status : " + jobExecution.getStatus());
			
			
		}catch (Exception e){
			LOGGER.error(" Error occoured while runing StoreCloning job in StoreCloningTask run method --->  " + e.getMessage());
			System.exit(1);
		}
		finally {
			
			if(!CommonUtil.isNull(jobExecution) && !jobExecution.getStatus().toString().equalsIgnoreCase("COMPLETED")){
				LOGGER.error(" Exception occures from Job Step Finally Block .....");
				System.exit(1);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of StoreCloningJobLauncher from Finally Block");
			}			
		}
		System.exit(0);
	}

}
