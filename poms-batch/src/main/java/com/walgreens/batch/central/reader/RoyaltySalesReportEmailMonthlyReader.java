/* Copyright (c) 2015, Walgreens Co. */
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
 * 
 * {@link RoyaltySalesReportEmailMonthlyReader} is a business implementation class for {@link ItemReader}
 * This class is used to send reports through Email
 * from database
 * @author CTS
 * @since v1.0
 */
public class RoyaltySalesReportEmailMonthlyReader implements ItemReader<RoyaltySalesReportPrefDataBean>{

	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	int count = 0;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoyaltySalesReportEmailMonthlyReader.class);

	/**
	 * method to read royalty bean form Execution context
	 * 
	 * @return RoyaltySalesReportPrefDataBean -- Bean Contains filter state and reportId 
	 */
	@Override
	public RoyaltySalesReportPrefDataBean read() throws Exception,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering RoyaltySalesReportEmailMonthlyReader.read() method -- >");
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
	 * @param stepExecution -- Execution context holder
	 * @throws PhotoOmniException -- Custom Exception
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering RoyaltySalesReportEmailMonthlyReader.retriveValue() method --- >");
		}
		try {
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
		}catch (Exception e) {
			LOGGER.error(" Error occoured at RoyaltySalesReportEmailMonthlyReader.retrieveValue() method - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from RoyaltySalesReportEmailMonthlyReader.retrieveValue() method ");
			}
		}
	}

}

