/**
 * LCAdhocAndExceptionCustomReportTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 03 March 2015
 *  
 **/
package com.walgreens.batch.central;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * This class to launch job for Adhoc and Exception report.
 * 
 * @author CTS
 * @version 1.1 Mar 03, 2015
 * 
 */

public class MachineDownTimeReportTask {
	
	/**
     * LOGGER for logging
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MachineDownTimeReportTask.class);

	/**
	 * This method launch job for Adhoc and Exception report.
	 */
    public static void main(String[] args) {
	//public  void run() {
    	long st = System.currentTimeMillis();
		System.out.println("startTime:    "+st);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of MachineDownTimeReportTask ");
		}
		
		try {
			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_MachineDowntime.xml", 
				};
				@SuppressWarnings("resource")
				ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
				JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
				Job job = (Job) context.getBean("machineDownTimeJob");
				JobParameters jobParameters = new JobParametersBuilder().addLong("time",System.currentTimeMillis()).toJobParameters();
				System.out.println("Start Machine downtime running at : " + new Date());
				jobLauncher.run(job, jobParameters);
				System.out.println("End Machine downtime running at : " + new Date());
				
			} catch (Exception e) {
				LOGGER.error(" Error occoured at run method of MachineDownTimeReportTask - " + e.getMessage());
				System.exit(1);
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting run method of MachineDownTimeReportTask ");
				}
					long se = System.currentTimeMillis();
					System.out.println("endTime:    "+se);
					System.out.println("resultTime:    "+(se-st)/1000 +" seconds");
					System.exit(0);
			
			}

		}

}
