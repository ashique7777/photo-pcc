
/**
 * LCCloseCSVCustomReader.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 10 mar 2015
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

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is a reader class for closing CSV file
 * @author CTS
 * @version 1.1 March 10, 2015
 */

public class LCCloseCSVCustomReader implements ItemReader<LCAndPSReportPrefDataBean>{
    /**
     * lCAndPSReportPrefDataBean
     */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCCloseCSVCustomReader.class);
	static int count = 0;
	
	/**
	 * This method returns the 'lCAndPSReportPrefDataBean' value to the writer. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public LCAndPSReportPrefDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCCloseCSVCustomReader ");
		}
			try {
				if(count == 0){
					count++;
					
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCCloseCSVCustomReader ");
					}
					
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCCloseCSVCustomReader ");
					}
					lCAndPSReportPrefDataBean= null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCCloseCSVCustomReader - " + e.getMessage());
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCCloseCSVCustomReader ");
				}
			}
			return lCAndPSReportPrefDataBean;
			
	}
  

	/**
	 * Retrieve value get the 'refDataKeyForAdhocAndException' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCCloseCSVCustomReader ");
		}
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForAdhocAndException");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at retriveValue method of LCCloseCSVCustomReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCCloseCSVCustomReader ");
			}
	}
}
}
