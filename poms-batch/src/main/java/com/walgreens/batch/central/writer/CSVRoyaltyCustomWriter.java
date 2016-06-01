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
/**
 * <p>
 * 	Custom item writer implements Spring ItemWriter which will get royalty 
 * data bean to execution context 
 * </p>
 * 
 * {@link CSVRoyaltyCustomWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to set processed data to execution context
 * @since v1.0
 */
public class CSVRoyaltyCustomWriter implements ItemWriter<RoyaltySalesReportPrefDataBean>{

	private StepExecution stepExecution;
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVRoyaltyCustomWriter.class);


	/**
	 * method to get Execution context
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyCustomWriter.saveStepExecution() method  -- > ");
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
	@Override
	public void write(List<? extends RoyaltySalesReportPrefDataBean> items)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyCustomWriter.write()  method-- > ");
		}
		try{
			ExecutionContext executionContext = this.stepExecution.getExecutionContext();
			RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean = items.get(0);
			executionContext.put("refDataKey", royaltySalesReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at SVRoyaltyCustomWriter.write() ---- > " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from SVRoyaltyCustomWriter.write() method -- >");
			}
		}
	}

}
