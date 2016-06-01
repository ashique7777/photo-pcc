/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * This is a custom item writer class implementing spring itemWriter.
 * This class will read sales report data bean and save the 
 * same into execution context to be used in other steps
 * </p>
 * 
 * {@link SalesReportByProductItemWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to set processed data to execution context
 * @author CTS
 * @since v1.0
 */
public class SalesReportByProductItemWriter implements
ItemWriter<SalesReportByProductBean> {

	private StepExecution stepExecution;
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductItemWriter.class);

	/**
	 * method to get Execution context - This method will be executed before step execution begins
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		LOGGER.info(" Entering into SalesReportByProductItemWriter.saveStepExecution() method -- > ");
		this.stepExecution = stepExecution;
	}

	/**
	 * Method to set Bean to Execution Context
	 * @param List of SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@Override
	public void write(List<? extends SalesReportByProductBean> items)
			throws Exception {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into SalesReportByProductItemWriter.write() method -- > ");
		}
		try{
			if(null != items && !items.isEmpty()){
				ExecutionContext executionContext = this.stepExecution
						.getExecutionContext();
				SalesReportByProductBean bean = items.get(0);
				executionContext.put("refDataKey", bean);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at SalesReportByProductItemWriter.write() method ---- > " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting from SalesReportByProductItemWriter.write() method -- >");
			}
		}
	}

}
