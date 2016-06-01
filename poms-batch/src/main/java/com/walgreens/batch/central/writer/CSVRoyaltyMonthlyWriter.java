/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * @author CTS
 * */
/**<p>
 * 	Custom item writer implements Spring ItemWriter which will get royalty 
 * data bean to execution context 
 * </p>
 * {@link CSVRoyaltyMonthlyWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to set processed data to execution context
 * @since v1.0
 */
public class CSVRoyaltyMonthlyWriter implements ItemWriter<RoyaltySalesReportPrefDataBean>{

	private StepExecution stepExecution;
	
	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVRoyaltyMonthlyWriter.class);

	/**
	 * method to get Execution context
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into CSVRoyaltyMonthlyWriter.saveStepExecution() method  -- > ");
			}
		this.stepExecution = stepExecution;
	}

	/**
	 * Method will get the royalty data bean from processor 
	 * and set the same into execution context 
	 * 
	 * @param List of RoyaltySalesReportPrefDataBean
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(List<? extends RoyaltySalesReportPrefDataBean> items)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyMonthlyWriter.write() method -- >");
		}
		try{
		ExecutionContext executionContext = this.stepExecution.getExecutionContext();
		 royaltySalesReportPrefDataBean = items.get(0);
		executionContext.put("refDataKey", royaltySalesReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at CSVRoyaltyMonthlyWriter.write() method ----> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from CSVRoyaltyMonthlyWriter.write() method-- >");
			}
		}
}
}
