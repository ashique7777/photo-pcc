/**
 * DailyMSSJobLauncher.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 17 August 2015
 *  
 **/
package com.walgreens.batch.central;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walgreens.batch.central.bean.DailyMSSDatabean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * 
 * @author CTS
 * @version 1.1 August 19, 2015. This class is job luncher class for MSS
 *          scheduler.
 * 
 */
public class DailyMSSJobLauncher {


	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyMSSJobLauncher.class);
	
	/**
	 * Properties for getting the properties params.
	 */
	private static Properties prop;
	
	/**
	 * To load the data in properties file.
	 */
	static {
		InputStream is = null;
		try {
			prop = new Properties();
			is = ClassLoader.class.getResourceAsStream("/PhotoOmniBatch.properties");
			prop.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method launch job for MSS Task.
	 */
	public static void main(String[] args) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering run method of DailyMSSJobLauncher ");
		}
		JobExecution jobExecution=null;
		try {
			/**
	         * Initializing the DailyMSSDatabean.
	         */
			DailyMSSDatabean mssDatabean = new DailyMSSDatabean();
	       
	      
	        
			String[] springConfig = { "spring/batch/config/database.xml",
					"spring/batch/config/context.xml",
					"spring/batch/jobs/job_MSSFeed.xml" };

			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
		
			SimpleDateFormat dateFormat1 = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_TWELFTH);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(PhotoOmniConstants.DATE_FORMAT_SIX);
			Calendar cal = Calendar.getInstance();
	        cal.add(Calendar.DATE, -1);	     
	       
			// If args.length==0 the default parameters will be set.
	        mssDatabean.setJobReturnTyp(PhotoOmniConstants.RERUNNO);
	        mssDatabean.setJobSubmitDate(dateFormat1.format(new Date()).toString());
	        mssDatabean.setDataEndDttm(dateFormat2.format(cal.getTime()) + PhotoOmniConstants.DAY_END_TIME);
			
			if(args.length!=0){
				// If args.length<=4 the parameters will be set as entered by the user. Parameter which is not entered it will take default value.
				parseParameters(args,mssDatabean);
			}
	        /**
	         * Setting parameters in the jobBuilder.
	         */
	        
			JobParametersBuilder jobBuilder = new JobParametersBuilder();
			jobBuilder.addString(prop.getProperty(PhotoOmniConstants.JOB_RERUN_TYPE), mssDatabean.getJobReturnTyp());
			jobBuilder.addString(prop.getProperty(PhotoOmniConstants.JOB_SUBMIT_DATE), mssDatabean.getJobSubmitDate());
			jobBuilder.addString(prop.getProperty(PhotoOmniConstants.DATA_BEGIN_DTTM), mssDatabean.getDataStartDttm());
			jobBuilder.addString(prop.getProperty(PhotoOmniConstants.DATA_END_DTTM), mssDatabean.getDataEndDttm());
			//jobBuilder.addString(prop.getProperty("is.test"), isTest);
			
			// Delete this line
			jobBuilder.addLong("time",System.currentTimeMillis());
			
			JobParameters jobParameters = jobBuilder.toJobParameters();
			
			
			JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
			Job job = (Job) context.getBean("dailyMSSJob");
			jobExecution = jobLauncher.run(job, jobParameters);
			
			
			LOGGER.debug("Job Execution Status ::   ::: "+jobExecution.getStatus());
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at run method of DailyMSSJobLauncher - " + e.getMessage());
			System.exit(1);
		} finally {
			if(!CommonUtil.isNull(jobExecution)){
				LOGGER.debug("JOB EXECUTION STATUS :: " + jobExecution.getStatus());
				for(Throwable ex: jobExecution.getAllFailureExceptions()){
					LOGGER.error("Inside Finally Block " + ex.getMessage());
				}
			}
			if(!CommonUtil.isNull(jobExecution) && !jobExecution.getStatus().toString().equalsIgnoreCase("COMPLETED")){
				LOGGER.error(" Exception occures from Job Step Finally Block .....");
				System.exit(1);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting run method of DailyMSSJobLauncher from Finally Block");
			}			
		}
		System.exit(0);

	}
	
	/**
	 * To Parse the arguments entered by the user and validate them.
	 * @param args
	 * @param jobSubmitDate
	 * @param dataStartDttm
	 * @param dataEndDttm
	 * @throws PhotoOmniException
	 */
	private static void parseParameters(String[] args, DailyMSSDatabean mssDatabean) throws PhotoOmniException {
		boolean errorFlag = true;
		String errorMessage = null;
		 if(args.length<=3 ){
	        	for(int i= 0;i<args.length;i++){
	        		errorFlag = true;
	        		
	        		if(!args[i].endsWith(PhotoOmniConstants.EQUALSIGN)){
		        		String[] params = args[i].split(PhotoOmniConstants.EQUALSIGN);
		        		if(params.length == 2){
			        		if(params[0].equalsIgnoreCase(prop.getProperty(PhotoOmniConstants.JOB_SUBMIT_DATE))){
			        			boolean paramValidate = CommonUtil.isValidDate(params[1],PhotoOmniConstants.DATE_FORMAT_TWELFTH);
			        			if(paramValidate){
			        				mssDatabean.setJobSubmitDate(params[1]);
			        				errorFlag = false;
			        			} 
			        		} else if(params[0].equalsIgnoreCase(prop.getProperty(PhotoOmniConstants.DATA_BEGIN_DTTM))){
			        			boolean paramValidate = CommonUtil.isValidDate(params[1],PhotoOmniConstants.DATE_FORMAT_SIX);
			        			if(paramValidate){
			        				mssDatabean.setDataStartDttm(params[1] + PhotoOmniConstants.DAY_BEGIN_TIME);
			        				errorFlag = false;
			        			}
			        		} else if(params[0].equalsIgnoreCase(prop.getProperty(PhotoOmniConstants.DATA_END_DTTM))){
			        			boolean paramValidate = CommonUtil.isValidDate(params[1],PhotoOmniConstants.DATE_FORMAT_SIX);
			        			if(paramValidate){
			        				mssDatabean.setDataEndDttm(params[1] + PhotoOmniConstants.DAY_END_TIME);
			        				errorFlag = false;
			        			}
			        		}
		        		}
	        		}
	        		if(errorFlag){
	        			errorMessage = "Parameter entered is not a valid parameter .";
	        			throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
	        		}
	        	}
	        	// To check whether The date entered by the user is future date or not.
	        	futureDateValidation(mssDatabean);
	        	// To check whether The begin date entered by the user is before the end date or not.
	        	beforeDateValidation(mssDatabean);
	        } else {
	        	errorMessage = "The Parameters entered exceeds the maximum limit.";
	        	throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
	        		        	
	        }
	}
	
	/**
	 * To check whether The date entered by the user is future date or not.
	 * @param mssDatabean
	 * @throws PhotoOmniException
	 */
	private static void futureDateValidation(DailyMSSDatabean mssDatabean) throws PhotoOmniException {
		boolean isFuture = true;
		String errorMessage = null;
		final Date currentDate = new Date();
		if(!CommonUtil.isNull(mssDatabean.getJobSubmitDate())){
			isFuture = CommonUtil.isFutureDate(mssDatabean.getJobSubmitDate(), currentDate, PhotoOmniConstants.DATE_FORMAT_TWELFTH);
			if (!isFuture) {
				errorMessage = "JOB_SUBMIT_DATE cannot be future Date .";
				throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
			}
		}
		if(!CommonUtil.isNull(mssDatabean.getDataStartDttm())){
	    	isFuture = CommonUtil.isFutureDate(mssDatabean.getDataStartDttm(), currentDate, PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
			if (!isFuture) {
				errorMessage = "DATA_BEGIN_DTTM cannot be future Date .";
				throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
			}
		}
		if(!CommonUtil.isNull(mssDatabean.getDataEndDttm())){
			isFuture = CommonUtil.isFutureDate(mssDatabean.getDataEndDttm(), currentDate, PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
			if (!isFuture) {
				errorMessage = "DATA_END_DTTM cannot be future Date .";
				throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
			}
		}
	}
	
	/**
	 * To check whether The begin date entered by the user is before the end date or not.
	 * @param mssDatabean
	 * @throws PhotoOmniException
	 */
	private static void beforeDateValidation(DailyMSSDatabean mssDatabean) throws PhotoOmniException {
		boolean isBefore = true;
		String errorMessage = null;
		if(!CommonUtil.isNull(mssDatabean.getDataStartDttm()) && !CommonUtil.isNull(mssDatabean.getDataEndDttm())){
			isBefore = CommonUtil.isBeforeDate(mssDatabean.getDataStartDttm(), mssDatabean.getDataEndDttm(), PhotoOmniConstants.DATE_FORMAT_THIRTEENTH);
		}
		if (!isBefore) {
			errorMessage = "DATA_BEGIN_DTTM cannot be after DATA_END_DTTM .";
			throw new PhotoOmniException(errorMessage + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_1 + System.lineSeparator() + PhotoOmniConstants.MSS_ERROR_MESSAGE_2);
		}
	}
}
