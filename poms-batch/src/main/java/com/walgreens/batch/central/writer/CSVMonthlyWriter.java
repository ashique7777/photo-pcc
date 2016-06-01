package com.walgreens.batch.central.writer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item writer implements Spring itemWriter
 * </p>
 * 
 * <p>
 * Call will set the pmbywic bean into execution context 
 * </p>
 * {@link CSVMonthlyWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class CSVMonthlyWriter implements ItemWriter<PMBYWICReportPrefDataBean> {

	private StepExecution objStepExecution;

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVMonthlyWriter.class);

	/**
	 * Method to set execution context variable
	 * 
	 * @param objStepExecution -- Execution context holder
	 */
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVMonthlyWriter.saveStepExecution() ");
		}
		this.objStepExecution = stepExecution;
	}

	/**
	 * Method to set pmByWic bean to execution context
	 * 
	 * @param items -- List of pmBywic report bean
	 * @throws Exception 
	 */
	public void write(List<? extends PMBYWICReportPrefDataBean> items)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVMonthlyWriter.write() ");
		}
		try {
			ExecutionContext objExecutionContext = this.objStepExecution.getExecutionContext();
			PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean = items.get(0);
			objExecutionContext.put("refDataKey", objPMBYWICReportPrefDataBean);
		}
		catch (Exception e) {
			LOGGER.error(" Error occoured at CSVMonthlyWriter.write() ---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From CSVMonthlyWriter.write()");
			}
		}
	}
}
