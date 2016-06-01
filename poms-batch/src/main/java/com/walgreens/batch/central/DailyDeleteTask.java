/* Copyright (c) 2015, Walgreens Co. */
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
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * 	class to launch the daily delete job
 * 	to deleted archived and temporary data
 * </p>
 * 
 * <p>
 * This call will clean up the archived files which are three days older 
 * and the temporary data which are 7 days old in the database
 * </p>
 * 
 * <p>
 * {@link DailyDeleteTask}
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public class DailyDeleteTask {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DailyDeleteTask.class);

	/**
	 * Context file loader
	 */
	private static ApplicationContext context;

	/**
	 * Main method to launch dailyDeleteJob to move processed files to archival folder
	 * temporary data which are 7 days older form the database
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into DailyDeleteTask.main() method ");
		}
		JobExecution execution = null;
		try {
			String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
			"spring/batch/jobs/job_PromotionFeedtocentral.xml" };
			context = new ClassPathXmlApplicationContext(springConfig);
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addLong(PhotoOmniConstants.JOBSUBMITTIME, new Long(System.currentTimeMillis()));
			jobBuilder.addLong("NoOfDaysOldData", 7L);
			jobBuilder.addString(PhotoOmniConstants.REPORTNAME, "PROMOTIONFEED");
			JobParameters jobParameters = jobBuilder.toJobParameters();
			JobLauncher jobLauncher = (JobLauncher) context
					.getBean(PhotoOmniConstants.JOBLAUNCHER);
			Job job = (Job) context.getBean("DailyDeleteJob");
			execution = jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at DailyDeleteTask.Main()--->  "
					+ e);
			System.exit(1);
		}finally{
			if(!CommonUtil.isNull(execution) && !"COMPLETED".equalsIgnoreCase(execution.getStatus().toString())){
				LOGGER.error("Job execution failed for DailyDeleteJob.");
				System.exit(1);
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.info("Exiting from  DailyDeleteTask.main() method");
			}
		}
		System.exit(0);
	}
}
