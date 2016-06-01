
/**
 * DeploymentProcessWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 9 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;


import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import com.walgreens.batch.central.bean.DeploymentProcessDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * 
 * This is a writer class putting the data value to the execution context.
 * @author CTS
 * @version 1.1 March 09, 2015
 */

public class DeploymentProcessWriter implements ItemWriter<DeploymentProcessDataBean> {
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentProcessWriter.class);
	/**
     * stepExecution
     */
	private StepExecution stepExecution;
	
	
	@BeforeStep
	public void saveStepExecution(final StepExecution stepExecution) { 
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering saveStepExecution method of DeploymentProcessWriter ");
		}
		this.stepExecution = stepExecution;
	}	


	/**
	 * This method putting the 'LCAndPSReportPrefDataBean' value to the execution context.
	 * @param item contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends DeploymentProcessDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of DeploymentProcessWriter ");
		}
		try {
			 final ExecutionContext executionContext = this.stepExecution.getExecutionContext();
			 DeploymentProcessDataBean deploymentProcessDataBean = items.get(0);
			 String propertyValue = deploymentProcessDataBean.getPropertyValue();
			 Date chainWidePilotDate = CommonUtil.convertStringToDate(propertyValue, PhotoOmniConstants.DATE_FORMAT_ZERO); 
			 executionContext.put("deploymentProcessRef", chainWidePilotDate);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of DeploymentProcessWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of DeploymentProcessWriter ");
			}
		}
	}
	
}
