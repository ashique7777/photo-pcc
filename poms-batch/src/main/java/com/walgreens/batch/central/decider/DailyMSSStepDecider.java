package com.walgreens.batch.central.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import com.walgreens.common.utility.CommonUtil;

public class DailyMSSStepDecider implements JobExecutionDecider{
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyMSSStepDecider.class);
	
	/**
	 * This method is a decider method for Daily MSS.
	 * @param jobExecution contains job execution value.
	 * @param stepExecution contains step execution value.
	 */
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering DailyMSSStepDecider Decider class ");
		}
		String beginDate = null;
		beginDate = jobExecution.getJobParameters().getString("DATA_BEGIN_DTTM");
		if(!CommonUtil.isNull(beginDate)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("DailyMSSStepDecider - COMPLETED");
			}
	    	return  FlowExecutionStatus.COMPLETED;
		}
		else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("DailyMSSStepDecider- FAILED");
			}
			return  FlowExecutionStatus.FAILED;
		}
	}

}
