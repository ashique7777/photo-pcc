package com.walgreens.batch.central.decider;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

/**
 * The below defined decider decides which module to execute. 
 * Whether it is for all stores (For FAILED cased) or for specific stores (COMPLETED CASE)
 * @author Cognizant
 *
 */
public class TAStepDecider implements JobExecutionDecider{
	
	final private static Logger log = LoggerFactory
			.getLogger(TAStepDecider.class);
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution,
			StepExecution stepExecution) {
		if (log.isDebugEnabled()) {
			log.debug("Entering into TAStepDecider:decide()");
		}
		final Date currentDate = new Date();	
		Date chainWideDate = null;
		try{
			ExecutionContext executionContext = jobExecution.getExecutionContext();			
			chainWideDate = (Date) executionContext.get("propValue");	
			log.info("currentDate    "+currentDate);
			log.info("chainWidePilotDate    "+chainWideDate);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		if(null!=chainWideDate && currentDate.before(chainWideDate)){ 
			if (log.isDebugEnabled()) {
				log.debug("Entering into PropertyValueWriter:write() - COMPLETED");
			}
			return  FlowExecutionStatus.COMPLETED;			
		}
		else{ 
			if (log.isDebugEnabled()) {
				log.debug("Exiting from TAStepDecider:decide() - FAILED");
			}
			return  FlowExecutionStatus.FAILED;
		}
	}

}
