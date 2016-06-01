package com.walgreens.batch.central;

import java.text.ParseException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DailyPhotoTotalSalesReportTask {
	public static void main(String[] args) throws ParseException {
		String[] springConfig = { "spring/batch/config/database.xml",
				"spring/batch/config/context.xml",
				"spring/batch/jobs/job_EmailReport.xml"};

		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				springConfig);
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("EmailPhotoTotalSalesReportJob");

		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addString("ReportId", "EmailPhotoTotalSalesReport")
					.addString("jobSubmitTime",
							new Long(System.currentTimeMillis()).toString())
					.toJobParameters();
			 jobLauncher.run(job, jobParameters);		
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
