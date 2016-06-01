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
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * <p>
 * 	Custom item reader implements Spring itemReader 
 * </p>
 * 
 * <p>
 * Class will get pmByWic report bean from Execution context
 * </p>
 * {@link PMByWICCustomReader} is a business implementation class for {@link ItemReader}
 * @author CTS
 * @since v1.0
 */
public class PMByWICCustomReader implements ItemReader<PMBYWICReportPrefDataBean>{

	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PMByWICCustomReader.class);
	
	int count = 0;
	
	/**
	 * Method will read pmBywic report bean from execution context
	 * 
	 * @return PMBYWICReportPrefDataBean -- Bean contains Adoc reportID details
	 * @throws Exception
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws NonTransientResourceException
	 */
	public PMBYWICReportPrefDataBean read() throws Exception,
	UnexpectedInputException, ParseException,
	NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICCustomReader.read() ");
		}
		if(count == 0){
			count++;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PMByWICCustomReader.read() ");
			}
			return objPMBYWICReportPrefDataBean;
		}else{
			return null;
		}
	}

	/**
	 * Method to retrieve pmBywic report bean and set the same into pmBywicReprotBean
	 * 
	 * @param stepExecution -- Execution Context holder
	 * @throws PhotoOmniException -- Custom Exception
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICCustomReader.retriveValue() ");
		}
		try {
			JobExecution objJobExecution = stepExecution.getJobExecution();
			ExecutionContext objExecutionContext = objJobExecution.getExecutionContext();
			objPMBYWICReportPrefDataBean = (PMBYWICReportPrefDataBean) objExecutionContext.get("refDataKey");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at PMByWICCustomReader.retriveValue() >----> " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PMByWICCustomReader.retriveValue() >----> ");
			}
		}
	}
}
