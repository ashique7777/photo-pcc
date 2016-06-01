/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item reader implements Spring itemReader 
 * </p>
 * 
 * <p>
 * This will read the royalty data bean from execution context 
 * </p>
 * {@link RoyaltySalesCustomReader} is a business implementation class for {@link ItemReader}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class RoyaltySalesCustomReader implements ItemReader<RoyaltySalesReportPrefDataBean>{

	RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	int count = 0;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoyaltySalesCustomReader.class);
	
	/**
	 * This method fetches the royaltySales bean form Execution context
	 * @return RoyaltySalesReportPrefDataBean
	 */
	public RoyaltySalesReportPrefDataBean read() {
	 if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering RoyaltySalesCustomReader.read() method --- >");
	}
		if(count == 0){
			count++;
			return royaltySalesReportPrefDataBean;
		}else{
			return null;
		}
	}

	/**
	 * Method will fetch the royalty sales bean from execution context 
	 * this method will execute before step execution begins
	 * 
	 * @param stepExecution
	 * @throws PhotoOmniException
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering RoyaltySalesCustomReader.retrieveValue() method  --- >");
		}
		try {
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
		}catch (Exception e) {
			LOGGER.error(" Error occoured at RoyaltySalesCustomReader.retrieveValue() method - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from RoyaltySalesCustomReader.retrieveValue() method ");
			}
		}
	}
}
