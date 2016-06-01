/**
 * RestartFailedJobs.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		  16th September 2015
 *  
 **/
package com.walgreens.batch.central;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
/**
 * This class use to re Run the failed batch job.
 * @author CTS
 * @version 1.1 September 16, 2015
 */
public class RestartFailedJobs {
	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestartFailedJobs.class);
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
	private static String dateFormat = "yyyyMMdd";
	/**
	 * isValid
	 */
	private static Boolean isValid = true;
	/**
	 * jobName
	 */
	private static String jobName;
	
	/**
	 * This method is a entry point to call the failed batch.
	 * @param args contains data.
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of RestartFailedJobs ");
		}
		try {
			if (args.length == 3) {
				validateUserInput(args);
				if (isValid) {
					loadContext();
				}
			}  else {
				System.out.println(" Please enter 3 params only - BeginDate(yyyymmdd), EndDate(yyyymmdd), JobName ");
				LOGGER.info(" Please enter 3 params only - BeginDate(yyyymmdd), EndDate(yyyymmdd), JobName ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(" Error occoured at run method of RestartFailedJobs - " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of RestartFailedJobs ");
			}
			System.exit(0);
		}
	}


	/**
	 * This method use to load the context for the batch.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private static void loadContext() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering loadContext method of RestartFailedJobs ");
		}
		try {
			final Properties properties = new Properties();
			final InputStream input = new RestartFailedJobs().getClass().getResourceAsStream("/PhotoOmniBatch.properties");
			final InputStream sqlFile = new RestartFailedJobs().getClass().getResourceAsStream("/PhotoOmniBatchSQL.properties");
			properties.load(sqlFile);
			properties.load(input);
			input.close();
			sqlFile.close();
			String[] springConfig  = 
				{	"spring/batch/config/database.xml", 
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_KronosFeed.xml"
				};
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

			final StringBuffer queryRunAll = new StringBuffer(properties.getProperty("QUERY_RUN_ALL") );
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Query for queryRunAll is : " + queryRunAll);
			}
			final StringBuffer queryRunMostRecent = new StringBuffer(properties.getProperty("QUERY_RUN_MOST_RECENT") );
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Query for queryRunMostRecent is : " + queryRunMostRecent);
			}
			final StringBuffer queryJobReRunType = new StringBuffer(properties.getProperty("QUERY_JOB_RERUN_TYPE") );
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Query for queryJobReRunType is : " + queryJobReRunType);
			}
			reRunJob(context, queryJobReRunType.toString(), queryRunAll.toString(), queryRunMostRecent.toString());
			
		} catch (FileNotFoundException e) {
			LOGGER.error(" Error occoured at loadContext method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" Error occoured at loadContext method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (BeansException e) {
			LOGGER.error(" Error occoured at loadContext method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at loadContext method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting loadContext method of RestartFailedJobs ");
			}
		}
	}

	
	/**
	 * This method validate the user inputs.
	 * @param args contains user inputs.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private static void validateUserInput(final String[] args) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering validateUserInput method of RestartFailedJobs ");
		}
		try {
			for (int i = 0; i < args.length; i++) {
				if (i == 0) {
					final String beginDateArg = args[i];
					if (CommonUtil.isNull(beginDateArg) || "".equals(beginDateArg) || !CommonUtil.isValidDate(beginDateArg, dateFormat)) {
						System.out.println(" Wrong Begin Date, Please enter Begin Date with " + dateFormat + " this format ");
						LOGGER.info(" Wrong Begin Date, Please enter Begin Date with " + dateFormat + " this format ");
						isValid = false;
						break;
					} else {
						beginDate = beginDateArg;
					}
				} else if (i == 1) {
					final String endDateArg = args[i];
					if (CommonUtil.isNull(endDateArg) || "".equals(endDateArg) || !CommonUtil.isValidDate(endDateArg, dateFormat)) {
						System.out.println(" Wrong End Date, Please enter End Date with " + dateFormat + " this format ");
						LOGGER.info(" Wrong End Date, Please enter End Date with " + dateFormat + " this format ");
						isValid = false;
						break;
					} else {
						endDate = endDateArg;
					}
				} else if (i == 2) {
					final String jobNameArg = args[i];
					if (CommonUtil.isNull(jobNameArg) || "".equals(jobNameArg)) {
						System.out.println(" Please enter value for Job Name ");
						LOGGER.info(" Please enter value for Job Name ");
						isValid = false;
						break;
					} else {
						jobName = jobNameArg;
					}
				}
			}
			
			if (isValid) {
				final boolean isBefore = CommonUtil.isBeforeDate(beginDate, endDate, dateFormat);
				if (!isBefore) {
					isValid = false;
					System.out.println(" Date1 cannot be after Date2 ");
					LOGGER.info(" Date1 cannot be after Date2 ");
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at validateUserInput method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting validateUserInput method of RestartFailedJobs ");
			}
		}
	}
	
	
	/**
	 * This method use to run the failed batch job.
	 * @param context contains context data.
	 * @param queryJobReRunType contains SQL Query.
	 * @param queryRunAll contains SQL Query.
	 * @param queryRunMostRecent contains SQL Query
	 * @throws PhotoOmniException- Custom Exception. 
	 */
	private static void reRunJob(final ApplicationContext context, final String queryJobReRunType, 
			final String queryRunAll, final String queryRunMostRecent) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering reRunJob method of RestartFailedJobs ");
		}
		try {
			final JobOperator operator = (JobOperator) context.getBean("jobOperator");
			final DataSource dataSource = (DataSource) context.getBean("omniDataSource");
			final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			long a;
			List<String> restartJobList = null;
			String restartMostRecentExecution = "";
			String jobReRunType = "";
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Start Date for restart batch is : " + beginDate);
				LOGGER.debug(" End Date for restart batch is : " + endDate);
				LOGGER.debug(" Job name for restart batch is : " + jobName);
			}
			if (!CommonUtil.isNull(jobName) && !"".equals(jobName)) {
				final Object parameterJobReRunType[] = { jobName };
				jobReRunType = (String) jdbcTemplate.queryForObject(queryJobReRunType, parameterJobReRunType, String.class);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Job run type for job name " + jobName +" is "+ jobReRunType);
				}
				if (!jobReRunType.isEmpty() && jobReRunType.equalsIgnoreCase("RERUNALL")) {
					final Object parametersRunAll[] = { jobName, beginDate, endDate, jobName };
					restartJobList = (List<String>) jdbcTemplate.queryForList(queryRunAll, parametersRunAll, String.class);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Size of Restart job list for job name " + jobName +" is "+ restartJobList.size());
					}
					System.out.println(" Size of Restart job list for job name " + jobName +" is "+ restartJobList.size());
				if (!CommonUtil.isNull(restartJobList) && restartJobList.size() > 0) {
					for (String restartJobExecutionId : restartJobList) {
						System.out.println(" Start Running Job Execution id for failed job is " + restartJobExecutionId 
								+ " on Date between " + beginDate + " And " + endDate + " Job Name " + jobName);
						LOGGER.info(" Job Execution id for failed job is " + restartJobExecutionId 
								+ " on Date between " + beginDate + " And " + endDate + " Job Name " + jobName);;
						try {
							LOGGER.info(" Start Running for : " + jobName +" and jobExecutionId : "+ restartJobExecutionId);
							a = 0;
							a = operator.restart(Integer.parseInt(restartJobExecutionId));
							System.out.println(" End Running for : " + jobName +" and jobExecutionId : "+ restartJobExecutionId + " with new value :" + a);
							LOGGER.info(" End Running for : " + jobName +" and jobExecutionId : "+ restartJobExecutionId + " with new value :" + a);
						} catch (Exception e) {
							LOGGER.error(" Error occoured when Running for : " + jobName +" and jobExecutionId : " + restartJobExecutionId );
						}
					}
				}
			} else if (!jobReRunType.isEmpty() && jobReRunType.equalsIgnoreCase("RERUNMRJ")) {
				final Object parametersRunMostRecent[] = { jobName, beginDate, endDate };
				restartMostRecentExecution = (String) jdbcTemplate.queryForObject(queryRunMostRecent, parametersRunMostRecent, String.class);
				if (null != restartMostRecentExecution && !restartMostRecentExecution.isEmpty()) {
					a = 0;
					try {
						LOGGER.info(" Start Running for : " + jobName +" and jobExecutionId : "+ restartMostRecentExecution);
						a = operator.restart(Integer.parseInt(restartMostRecentExecution));
						LOGGER.info(" End Running for : " + jobName +" and jobExecutionId : "+ restartMostRecentExecution + " with new value :" + a);
					} catch (Exception e) {
						LOGGER.error(" Error occoured when Running for : " + jobName +" and jobExecutionId : " + restartMostRecentExecution );
					}
				}
			}
		}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at reRunJob method of RestartFailedJobs - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting reRunJob method of RestartFailedJobs ");
			}
		}
	}
}