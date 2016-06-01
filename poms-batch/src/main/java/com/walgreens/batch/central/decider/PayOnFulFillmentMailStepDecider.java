/**
 * 
 */
package com.walgreens.batch.central.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;


/**
 * @author CTS
 *
 */
public class PayOnFulFillmentMailStepDecider  implements JobExecutionDecider{
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PayOnFulFillmentMailStepDecider.class);

	
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution,StepExecution stepExecution) {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering PayOnFulFillmentMailStepDecider Decider class ");
		}
		
		final ExecutionContext executionContext = jobExecution.getExecutionContext();
		boolean isMail = ((Boolean) executionContext.get("refPofEmailKey")).booleanValue();
		
		if(isMail)
	    	return  FlowExecutionStatus.COMPLETED;
		else return  FlowExecutionStatus.FAILED;
	}
	
	
}
