
/**
 * LCDailyReportEmailCustomReader.java 
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

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This is a reader class for email returns 'LCDailyReportPrefDataBean' value to processor.
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyReportEmailCustomReader implements ItemReader<LCDailyReportPrefDataBean>{
    /**
     * lCDailyReportPrefDataBean
     */
	LCDailyReportPrefDataBean lCDailyReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyReportEmailCustomReader.class);
	/**
	 * count
	 */
	int count = 0;
	
	/**
	 * This method return's the 'lCDailyReportPrefDataBean' to the processor.  
	 * @return lCDailyReportPrefDataBean.
	 * @throws PhotoOmniException - custom Exception
	 */
	public LCDailyReportPrefDataBean read() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCDailyReportEmailCustomReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCDailyReportEmailCustomReader ");
					}				
				}else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCDailyReportEmailCustomReader ");
					}
					lCDailyReportPrefDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCDailyReportEmailCustomReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting retriveValue method of LCDailyReportEmailCustomReader ");
				}
			}
			return lCDailyReportPrefDataBean;
		} 
    
	
	/**
	 * Retrieve value get the 'refDataKeyForDaily' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCDailyReportEmailCustomReader ");
		}
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCDailyReportPrefDataBean = (LCDailyReportPrefDataBean) executionContext.get("refDataKeyForDaily");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of LCDailyReportEmailCustomReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCDailyReportEmailCustomReader ");
			}
		}

	}
}
