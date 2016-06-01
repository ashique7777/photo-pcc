
/**
 * PSCloseCSVCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 2 mar 2015
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

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class is used to call for machine down time report.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 02, 2015 
 */

public class PSCloseCSVCustomWriter implements ItemWriter<LCAndPSReportPrefDataBean>{
    /**
     * stepExecution
     */
	private StepExecution stepExecution;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSCloseCSVCustomWriter.class);
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	public BufferedReader fileReader = null;
	
	/**
	 * This method initialize the stepExecution object.
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(final StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}
	
	/**
     * This method closing the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends LCAndPSReportPrefDataBean> items) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of PSCloseCSVCustomWriter ");
		}
		try {
			lCAndPSReportPrefDataBean = items.get(0);
			final String fileLocation = lCAndPSReportPrefDataBean.getFileLocation();
			/*As Print signs Content's only one file so directly taken 0 index*/
			final String fileName = lCAndPSReportPrefDataBean.getFileNameList().get(0); 
			fileReader = new BufferedReader(new FileReader(fileLocation.concat(fileName)));
			fileReader.close();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of PSCloseCSVCustomWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PSCloseCSVCustomWriter ");
			}
		}
	}
}	
