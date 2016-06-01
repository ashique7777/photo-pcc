
/**
 * LCDailyReportZipCustomReader.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 16 mar 2015
 *  
 **/
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is a reader class for ZIP creation.
 * @author CTS
 *  @version 1.1 March 16, 2015
 */

public class LCDailyReportZipCustomReader implements ItemReader<LCDailyReportPrefDataBean>{
    /**
     * lCAndPSReportPrefDataBean
     */
	LCDailyReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyReportZipCustomReader.class);
	static int count = 0;
	
	
	/**
	 * This method returns 'lCAndPSReportPrefDataBean' to  the Zip writer.
	 * @return lCAndPSReportPrefDataBean
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	public LCDailyReportPrefDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCDailyReportZipCustomReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCDailyReportZipCustomReader ");
					}
					
				}else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCDailyReportZipCustomReader ");
					}
					lCAndPSReportPrefDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCDailyReportZipCustomReader - " + e.getMessage());
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting retriveValue method of LCDailyReportZipCustomReader ");
				}
			}
			return lCAndPSReportPrefDataBean;
		}
    
	
	/**
	 * Retrieve value get the 'refDataKeyForDaily' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCDailyReportZipCustomReader ");
		}
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCDailyReportPrefDataBean) executionContext.get("refDataKeyForDaily");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of LCDailyReportZipCustomReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCDailyReportZipCustomReader ");
			}
		}
	}
}
