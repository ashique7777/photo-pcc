/**
 * DbPartitionPurgeTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 18 December 2015
 *  
 **/
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

/**
 * 
 * @author CTS
 * @version 1.1 December 18, 2015. This class is job luncher class for DbPartitionPurge
 *          scheduler.
 * 
 */
public class DbPartitionPurgeTask {


	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DbPartitionPurgeTask.class);
	
	/**
	 * This method launch job for DbPartitionPurge Task.
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of DbPartitionPurge Task ");
		}
		JobExecution jobExecution=null;
		JobParametersBuilder jobBuilder = new JobParametersBuilder();
		try {
			String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_DbPartitionPurge.xml" };
			
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
			
			jobBuilder.addLong("time",System.currentTimeMillis());
			
			JobParameters jobParameters = jobBuilder.toJobParameters();
			
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			Job job = (Job) context.getBean("dbPartitionPurge");
			jobExecution = jobLauncher.run(job, jobParameters);
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at run method of DbPartitionPurge Task - " + e);
			System.out.println("JOB FAILED. PLEASE CHECK THE LOG FILE.");
			System.exit(1);
		} finally {
			if(!CommonUtil.isNull(jobExecution)){
				LOGGER.debug("JOB EXECUTION STATUS :: " + jobExecution.getStatus());
				for(Throwable ex: jobExecution.getAllFailureExceptions()){
					LOGGER.error("Inside Finally Block " + ex.getMessage());
				}
			}
			if(!CommonUtil.isNull(jobExecution) && !"COMPLETED".equalsIgnoreCase(jobExecution.getStatus().toString())){
				LOGGER.error(" Exception occures from Job Step Finally Block .....");
				System.out.println("JOB FAILED. PLEASE CHECK THE LOG FILE.");
				System.exit(1);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of DbPartitionPurge Task from Finally Block");
			}			
		}
		System.out.println("JOB COMPLETED SUCCESSFULLY.");
		System.exit(0);
	}
}
