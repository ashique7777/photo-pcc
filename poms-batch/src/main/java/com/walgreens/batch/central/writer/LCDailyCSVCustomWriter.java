
/**
 * LCDailyCSVCustomWriter.java 
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
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * 
 * This is a writer class putting the data value to the execution context.
 * @author CTS
 * @version 1.1 March 16, 2015
 */


public class LCDailyCSVCustomWriter implements ItemWriter<LCDailyReportPrefDataBean> {
    /**
     * stepExecution
     */
	private StepExecution stepExecution;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyCSVCustomWriter.class);
	
	/**
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(final StepExecution stepExecution)
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering saveStepExecution method of LCDailyCSVCustomWriter ");
		}
		this.stepExecution = stepExecution;
	}



	/**
	 * This method putting the 'lCDailyReportPrefDataBean' value to the execution context.
	 * @param item contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends LCDailyReportPrefDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCDailyCSVCustomWriter ");
		}
		try {
			final ExecutionContext executionContext = this.stepExecution.getExecutionContext();
			final LCDailyReportPrefDataBean lCDailyReportPrefDataBean = items.get(0);
			executionContext.put("refDataKeyForDaily", lCDailyReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of LCDailyCSVCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCDailyCSVCustomWriter ");
			}
		}
	}
}
