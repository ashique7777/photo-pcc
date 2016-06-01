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
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;

public class PLUEmailCustomReader implements ItemReader<PLUReportPrefDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUEmailCustomReader.class);

	int count = 0;
	private PLUReportPrefDataBean pluReportPrefDataBean;

	@Override
	public PLUReportPrefDataBean read() throws Exception,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into read method of the PLUEmailCustomReader ");
		}
		if (count == 0) {
			count++;
			return pluReportPrefDataBean;
		} else {
			return null;
		}
	}

	/**
	 * Method to get stepExecution form Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into retriveValue method of the PLUEmailCustomReader ");
		}
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		pluReportPrefDataBean = (PLUReportPrefDataBean) executionContext
				.get("refDataKey");
	}
}