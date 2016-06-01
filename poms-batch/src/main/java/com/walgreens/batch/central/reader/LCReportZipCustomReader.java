
/**
 * LCReportZipCustomReader.java 
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
 * This class is a reader class for ZIP creation.
 * @author CTS
 * @version 1.1 March 10, 2015
 */

public class LCReportZipCustomReader implements ItemReader<LCAndPSReportPrefDataBean>{
    /**
     * lCAndPSReportPrefDataBean
     */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportZipCustomReader.class);
	static int count = 0;
	
	/**
	 * This method returns 'lCAndPSReportPrefDataBean' to  the Zip writer.
	 * @return lCAndPSReportPrefDataBean
	 * @throws PhotoOmniException - custom Exception
	 */
	public LCAndPSReportPrefDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCReportZipCustomReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCReportZipCustomReader ");
					}
				
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of LCReportZipCustomReader ");
					}
					lCAndPSReportPrefDataBean= null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCReportZipCustomReader - " + e.getMessage());
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCReportZipCustomReader ");
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
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCReportZipCustomReader ");
		}
		try {
			count = 0;
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForAdhocAndException");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of LCReportZipCustomReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCReportZipCustomReader ");
			}
		}
	}
}
