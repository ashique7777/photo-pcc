
/**
 * LCReportCustomReader.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 12 mar 2015
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
import com.walgreens.batch.central.processor.LCReportEmailItemProcessor;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This reader class returns 'lCAndPSReportPrefDataBean' value to the writer. 
 * @author CTS
 * @version 1.1 March 12, 2015
 */

public class LCReportCustomReader implements ItemReader<LCAndPSReportPrefDataBean>{
    /**
     * lCAndPSReportPrefDataBean
     */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportEmailItemProcessor.class);
	/**
	 * count
	 */
	int count = 0;
	
	/**
	 * This reader use for OM_USER_REPORT_PREF table update. 
	 * @return lCAndPSReportPrefDataBean
	 * @throws PhotoOmniException - custom Exception
	 */
	public LCAndPSReportPrefDataBean read() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCReportCustomReader ");
		}
		try {
			if(count == 0){
				count++;
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCReportCustomReader ");
				}
				
			} else{
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCReportCustomReader ");
				}
				lCAndPSReportPrefDataBean = null;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of LCReportCustomReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting read method of LCReportCustomReader ");
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
			LOGGER.debug(" Entering retriveValue method of LCReportCustomReader ");
		}
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForAdhocAndException");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of LCReportCustomReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCReportCustomReader ");
			}
		}
	}
}
