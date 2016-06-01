/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * This is a custom item reader class implementing spring itemReader.
 * This call will read salesreportbean from the execution context
 * </p>
 * {@link SalesReportByProductEmailReader} is a business implementation class for {@link ItemReader}
 * @author CTS
 * @since v1.0
 */
public class SalesReportByProductEmailReader implements
		ItemReader<SalesReportByProductBean> {

	 int count = 0;
	 
	private StepExecution stepExecution;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductEmailReader.class);

	/**
	 * method to read sales bean form Execution context
	 * 
	 * @return SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @throws PhotoOmniException -- Custom exception
	 */
	@Override
	public SalesReportByProductBean read() throws  PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into SalesReportByProductEmailReader.read() method -- >");
		}
		SalesReportByProductBean salesReportByProductBean = null;
		if (count == 0) {
			count++;
			try {
				JobExecution jobExecution = stepExecution.getJobExecution();
				ExecutionContext executionContext = jobExecution.getExecutionContext();
				salesReportByProductBean = (SalesReportByProductBean) executionContext.get("refDataKey");
			} catch (Exception e) {
				LOGGER.error(" Error occoured at SalesReportByProductEmailReader.read() method ----> " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("Exiting from SalesReportByProductEmailReader.read() method --- >");
				}
			}
		}
		return salesReportByProductBean;
			
	}

	@BeforeStep
	private void retriveValue(StepExecution stepExecution) {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering SalesReportByProductEmailReader.retriveValue method -- >");
		}
		this.stepExecution = stepExecution;
		
	}

}
