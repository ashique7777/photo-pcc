
/**
 * PSReportEmailCustomReader.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 2 mar 2015
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

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;
/**
 * This is a reader class for email returns 'LCAndPSReportPrefDataBean' value to processor.
 * @author CTS
 * @version 1.1 March 02, 2015
 */

public class PSReportEmailCustomReader implements ItemReader<LCAndPSReportPrefDataBean>{
    /**
     * lCAndPSReportPrefDataBean
     */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSReportEmailCustomReader.class);
	/**
	 * count
	 */
	int count = 0;
	
	/**
	 * This method return's the 'lCAndPSReportPrefDataBean' to the processor.  
	 * @return lCAndPSReportPrefDataBean.
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	public LCAndPSReportPrefDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of PSReportEmailCustomReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of PSReportEmailCustomReader ");
					}
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of PSReportEmailCustomReader ");
					}
					lCAndPSReportPrefDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of PSCSVFileItemReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of PSReportEmailCustomReader ");
				}
			}
				
			
			return lCAndPSReportPrefDataBean;
	}
	
	
	/**
	 * Retrieve value get the 'refDataKeyForPrintSigns' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of PSReportEmailCustomReader ");
		}	
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForPrintSigns");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of PSCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of PSReportEmailCustomReader ");
			}
		}
	}
}
