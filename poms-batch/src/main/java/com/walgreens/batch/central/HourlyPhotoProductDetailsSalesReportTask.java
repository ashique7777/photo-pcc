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

/**
 * <p>
 * 	A Class which contains main method to launch hourly photo details report job. 
 * This class will set the required job parameters need to process Email report
 * </p>
 *  @author CTS
 * @since v1.0
 */
public class HourlyPhotoProductDetailsSalesReportTask {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HourlyPhotoProductDetailsSalesReportTask.class);

	public static void main(String[] args){
			LOGGER.info(" Entering HourlyPhotoProductDetailsSalesReportTask.main() method ");
		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_EmailReport.xml" };
		try {
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(
					springConfig);
			JobLauncher jobLauncher = (JobLauncher) context.getBean(PhotoOmniConstants.JOBLAUNCHER);
			Job job = (Job) context
					.getBean(PhotoOmniConstants.HOURLYPHOTOPRODUCTREPORTJOB);
			JobParameters jobParameters = new JobParametersBuilder()
					.addString(PhotoOmniConstants.REPORID,
							PhotoOmniConstants.HOURLYPHOTOPRODUCTREPORT)
					.addString(PhotoOmniConstants.JOBSUBMITTIME,
							new Long(System.currentTimeMillis()).toString())
					.toJobParameters();
			JobExecution execution = jobLauncher.run(job, jobParameters);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("EmailHourlyPhotoProductDetailsSalesReport Job status -- > "+ execution.getStatus());
			}
			System.exit(0);
		} catch (Exception e) {
				LOGGER.error(
						" Error at HourlyPhotoProductDetailsSalesReportTask.main() method ",
						e.getMessage());
				System.exit(1);
		}finally{
				LOGGER.info(" Exiting HourlyPhotoProductDetailsSalesReportTask.main() method ");
		}
	}

}
