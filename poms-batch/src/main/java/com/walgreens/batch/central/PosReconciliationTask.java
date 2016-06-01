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

public class PosReconciliationTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PosReconciliationTask.class);

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_POS.xml" };

		ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("posReconciliationJob");

		JobExecution jobExecution = null;
		try {
			
			JobParameters jobParameters = new JobParametersBuilder()
					  .addLong("jobSubmitTime",new Long(System.currentTimeMillis())).toJobParameters();

			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into PosReconciliationTask run method");
			}
			jobExecution = jobLauncher.run(job, jobParameters);
			
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exit from PosReconciliationTask run method");
			LOGGER.debug("Exit Status : " + jobExecution.getStatus());
			}
			
		} catch (Exception e) {	
			if(LOGGER.isErrorEnabled()){
			LOGGER.error(" Error occoured while runing PosReconciliation job in PosReconciliationTask run method --->  " , e);
			}
			System.exit(1);
		}finally {
			if(!CommonUtil.isNull(jobExecution)){
				if(LOGGER.isDebugEnabled()){
				LOGGER.debug("JOB EXECUTION STATUS :: " + jobExecution.getStatus());
				}
				for(Throwable ex: jobExecution.getAllFailureExceptions()){
					if(LOGGER.isErrorEnabled()){
					LOGGER.error("Inside Finally Block " + ex.getMessage());
					}
				}
			}
			if(!CommonUtil.isNull(jobExecution) && !jobExecution.getStatus().toString().equalsIgnoreCase("COMPLETED")){
				if(LOGGER.isErrorEnabled()){
				LOGGER.error(" Exception occures from Job Step Finally Block .....");
				}
				System.exit(1);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of PosReconciliationTask from Finally Block");
			}			
		}
		System.exit(0);

	}

}
