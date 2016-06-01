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

public class PMByWICEmailMonthlyReader implements ItemReader<PMBYWICReportPrefDataBean>{

	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;
	static int count = 0;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PMByWICEmailMonthlyReader.class);

	public PMBYWICReportPrefDataBean read() throws Exception,
	UnexpectedInputException, ParseException,
	NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICEmailMonthlyReader.read() ");
		}
		if(count == 0){
			count++;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from PMByWICEmailMonthlyReader.read() ");
			}
			return objPMBYWICReportPrefDataBean;
		}else{
			return null;
		}
	}

	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICEmailMonthlyReader.retriveValue() ");
		}
		try {
			JobExecution objJobExecution = stepExecution.getJobExecution();
			ExecutionContext objExecutionContext = objJobExecution.getExecutionContext();
			objPMBYWICReportPrefDataBean = (PMBYWICReportPrefDataBean) objExecutionContext.get("refDataKey");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at PMByWICEmailMonthlyReader.retriveValue() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from PMByWICEmailMonthlyReader.retriveValue() ");
			}
		}
	}
}
