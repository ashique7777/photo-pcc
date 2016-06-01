
/**
 * LCDailyCloseCSVCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 16 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is used to call for machine down time report.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyCloseCSVCustomWriter implements ItemWriter<LCDailyReportPrefDataBean>{
    /**
     * stepExecution
     */
	private StepExecution stepExecution;
	/**
	 * lCDailyReportPrefDataBean
	 */
	LCDailyReportPrefDataBean lCDailyReportPrefDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyCloseCSVCustomWriter.class);
	
	
	/**
	 * This method initialize the stepExecution object.
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(final StepExecution stepExecution)
	{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering saveStepExecution method of LCDailyCloseCSVCustomWriter ");
		}
		this.stepExecution = stepExecution;
	}
	
	
	/**
     * This method closing the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends LCDailyReportPrefDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCDailyCloseCSVCustomWriter ");
		}
		try {
			lCDailyReportPrefDataBean = items.get(0);
			final String fileLocation = lCDailyReportPrefDataBean.getFileLocation();
			/*As License content daily Report having 1 reports*/
			final String fileName = lCDailyReportPrefDataBean.getFileNameList().get(0); 
			BufferedReader fileReader = new BufferedReader(new FileReader(fileLocation.concat(fileName)));
			fileReader.close();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of LCDailyCloseCSVCustomWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCDailyCloseCSVCustomWriter ");
			}
		}

		
	}
}
