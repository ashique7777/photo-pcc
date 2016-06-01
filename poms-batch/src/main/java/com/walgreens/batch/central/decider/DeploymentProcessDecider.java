/**
 * DeploymentProcessDecider.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 10th September 2015
 *  
 **/
package com.walgreens.batch.central.decider;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

import com.walgreens.common.utility.CommonUtil;
/**
 * This is a decider class for deployment process.
 * @author CTS
 * @version 1.1 September 10, 2015
 */
public class DeploymentProcessDecider implements JobExecutionDecider{
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentProcessDecider.class);
	
	/**
	 * This method is a decider method for deployment process.
	 * @param jobExecution contains job execution value.
	 * @param stepExecution contains step execution value.
	 */
	@Override
	public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering decide method of DeploymentProcessDecider ");
		}
		final ExecutionContext executionContext = stepExecution.getExecutionContext();
		final Date currentDate = new Date();
		Date chainWidePilotDate = (Date) executionContext.get("deploymentProcessRef");
		if(CommonUtil.isNull(chainWidePilotDate)){
			chainWidePilotDate = (Date) jobExecution.getExecutionContext().get("deploymentProcessRef");
		}	else{
			jobExecution.getExecutionContext().put("deploymentProcessRef", chainWidePilotDate);
		}	
		if (!CommonUtil.isNull(chainWidePilotDate) && currentDate.before(chainWidePilotDate)  ) { //(current date < PROPERTY_VALUE)
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Deployment Process current date : " + currentDate + " and  Chain wise pilot date : " + chainWidePilotDate);
			}
			return  FlowExecutionStatus.COMPLETED;
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Deployment Process current date : " + currentDate + " and  Chain wise pilot date : " + chainWidePilotDate);
			}
			return  FlowExecutionStatus.FAILED;
		}

	}

}
