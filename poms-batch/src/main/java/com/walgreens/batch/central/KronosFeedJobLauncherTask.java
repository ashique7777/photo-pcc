/**
 * KRONOSFeedJobLauncherTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 28 July 2015
 *  
 **/
package com.walgreens.batch.central;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is a luncher class Kronos scheduler
 * @author CTS
 * @version 1.1 July 28, 2015
 */
public class KronosFeedJobLauncherTask {
	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosFeedJobLauncherTask.class);
	/**
	 * jobReturnTyp
	 */
	private static String jobReturnTyp = "RERUNNO";
	/**
	 * beginDate
	 */
	private static String beginDate; 
	/**
	 * endDate
	 */
	private static String endDate; 
	/**
	 * dateFormat
	 */
	private static String dateFormat = PhotoOmniConstants.DATE_FORMAT_TWELFTH;
	/**
	 * isTest
	 */
	private static Boolean isTest = true;
	/**
	 * isValiadtionSuccess
	 */
	private static Boolean isValid = true;
	
	
	/**
	 * This method launch job for KronosFeedJobLauncherTask.
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of KronosFeedJobLauncherTask ");
		}
		BatchStatus kronosStatus = null;
		try {
			if (args.length == 0) {
				/*When no parameter entered by user, means daily batch*/
				String yesterday = DateTimeFormat.forPattern(dateFormat).print(new DateTime().plusDays(-1));
				beginDate = yesterday;
				endDate = yesterday;
				kronosStatus = loadJobLauncher();
			} else if (args.length == 4) {
				/*When 4  parameter are entered by user for Adhoc report*/
				validateUserInput(args);
				if (isValid) {
					kronosStatus = loadJobLauncher();
				}
			} else {
				System.out.println(" Please enter 4 params only - BeginDate(yyyymmdd), EndDate(yyyymmdd), JobRetrunType, IsTest(True/False) ");
				LOGGER.info(" Please enter 4 params only - BeginDate(yyyymmdd), EndDate(yyyymmdd), JobRetrunType, IsTest(True/False) ");
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at run method of LCDailyCustomReportTask - " + e.getMessage());
		} finally {
			LOGGER.info(" Kronos batch running status : " + kronosStatus + " with params begindate = " + beginDate + " enddate = " + endDate);
			System.out.println(" Kronos batch running status : " + kronosStatus + " with params begindate = " + beginDate + " enddate = " + endDate);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of LCDailyCustomReportTask ");
			}
			if (!CommonUtil.isNull(kronosStatus) && kronosStatus.name().equals("COMPLETED")) {
				System.exit(0);
			} else {
				System.exit(1);
			}
		}
	}

	/**
	 * This method add the params to the job params and start the batch.
	 * @return kronosStatus.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private static BatchStatus loadJobLauncher() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering loadJobLauncher method of KronosFeedJobLauncherTask ");
		}
		BatchStatus kronosStatus = null;
		try {
			String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_KronosFeed.xml" };
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
			final String fileCreationDate = DateTimeFormat.forPattern("yyyyMMddHHmmss").print(new DateTime());
			final JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString("BEGIN_DATE", beginDate);
			jobBuilder.addString("END_DATE", endDate);
			jobBuilder.addString("DECODE_VALUE", jobReturnTyp);
			jobBuilder.addString("IS_TEST", isTest.toString());
			jobBuilder.addString("FILE_CREATION_DATE", fileCreationDate);
			final JobParameters jobParameters = jobBuilder.toJobParameters();
			final JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			final Job job = (Job) context.getBean("kronosReportJob");
			JobExecution execution = jobLauncher.run(job, jobParameters);
			kronosStatus = execution.getStatus();
		} catch (BeansException e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (JobExecutionAlreadyRunningException e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (JobRestartException e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (JobInstanceAlreadyCompleteException e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (JobParametersInvalidException e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at loadJobLauncher method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting loadJobLauncher method of KronosFeedJobLauncherTask ");
			}
		}
		return kronosStatus;
	}

	/**
	 * This method validate the user inputs.
	 * @param args contains user inputs.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private static void validateUserInput(final String[] args) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering validateUserInput method of KronosFeedJobLauncherTask ");
		}
		try {
			for (int i = 0; i < args.length; i++) {
				if (i == 0) {
					String beginDateArg = args[i];
					if (CommonUtil.isNull(beginDateArg) || "".equals(beginDateArg) || !CommonUtil.isValidDate(beginDateArg, dateFormat)) {
						System.out.println(" Wrong Begin Date, Please enter Begin Date with " + dateFormat + " this format ");
						LOGGER.info(" Wrong Begin Date, Please enter Begin Date with " + dateFormat + " this format ");
						isValid = false;
						break;
					} else {
						beginDate = beginDateArg;
					}
				} else if (i == 1) {
					String endDateArg = args[i];
					if (CommonUtil.isNull(endDateArg) || "".equals(endDateArg) || !CommonUtil.isValidDate(endDateArg, dateFormat)) {
						System.out.println(" Wrong End Date, Please enter End Date with " + dateFormat + " this format ");
						LOGGER.info(" Wrong End Date, Please enter End Date with " + dateFormat + " this format ");
						isValid = false;
						break;
					} else {
						endDate = endDateArg;
					}
				} else if (i == 2) {
					String jobReturnTypArg = args[i];
					if (CommonUtil.isNull(jobReturnTypArg) || "".equals(jobReturnTypArg)) {
						System.out.println(" Please enter value for Job return type ");
						LOGGER.info(" Please enter value for Job return type ");
						isValid = false;
						break;
					} else {
						jobReturnTyp = jobReturnTypArg;
					}
				} else if (i == 3) {
					String isTestArg = args[i];
					if (CommonUtil.isNull(isTestArg) || "".equals(isTestArg) || (!isTestArg.equalsIgnoreCase("true") && !isTestArg.equalsIgnoreCase("false")) ) {
						System.out.println(" Please enter value for Is Test(TRUE/FALSE) ");
						LOGGER.info(" Please enter value for Is Test(TRUE/FALSE) ");
						isValid = false;
						break;
					} else {
						isTest = Boolean.getBoolean(isTestArg.toLowerCase());
					}
				}
			}
			
			if (isValid) {
				boolean isBefore = CommonUtil.isBeforeDate(beginDate, endDate, dateFormat);
				if (!isBefore) {
					isValid = false;
					System.out.println(" Date1 cannot be after Date2 ");
					LOGGER.info(" Date1 cannot be after Date2 ");
				} else {
					Date yesterday = new Date(new Date().getTime() - 24*3600*1000);
					boolean isFuture = CommonUtil.isFutureDate(endDate, yesterday, dateFormat);
					if (!isFuture) {
						isValid = false;
						System.out.println(" Date2 can be maximum yesterday ");
						LOGGER.info(" Date2 can be maximum yesterday ");
					} else {
						long daysGap = CommonUtil.findDaysGapBetweenDates(beginDate, endDate, dateFormat);
						if (daysGap > 195) {
							isValid = false;
							LOGGER.info(" Days gap between Date1 and Date2 can be Maximum 195 days ");
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at validateUserInput method of KronosFeedJobLauncherTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting validateUserInput method of KronosFeedJobLauncherTask ");
			}
		}
	}
	
}
