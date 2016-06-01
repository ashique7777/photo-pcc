
/**
 * PSCSVFileCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 1 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.PrintSignsCSVFileReportDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class getting the data from the reader and 
 * creating data structure for the CSV.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 01, 2015
 */

public class PSCSVFileCustomWriter implements ItemWriter<PrintSignsCSVFileReportDataBean >{
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSCSVFileCustomWriter.class);
	/**
	 * printSignsCSVFileReportDataBean
	 */
	PrintSignsCSVFileReportDataBean printSignsCSVFileReportDataBean;
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * objBufferedWriter
	 */
	BufferedWriter objBufferedWriter = null;
	
    /**
     * This method flushing the data to the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends PrintSignsCSVFileReportDataBean > items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of PSCSVFileCustomWriter ");
		}
		try {
			for (PrintSignsCSVFileReportDataBean printSignData : items) {
				if (!CommonUtil.isNull(this.objBufferedWriter)) {
					this.objBufferedWriter.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR); 
					this.addingDataToCSVForPrintSign(printSignData); 
				}
			}
			if (!CommonUtil.isNull(this.objBufferedWriter)) {
				this.objBufferedWriter.flush();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of PSCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PSCSVFileCustomWriter ");
			}
		}
	}



	/**
	 * @param printSignsCSVFileReportDataBean
	 * @throws IOException
	 * @throws PhotoOmniException 
	 */
	private void addingDataToCSVForPrintSign(PrintSignsCSVFileReportDataBean printSignData) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addingDataToCSVForDaily method of LCDailyCSVFileCustomWriter ");
		}
		try {
			this.objBufferedWriter.append(printSignData.getStoreNo()); 
			this.objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
			this.objBufferedWriter.append(printSignData.getEventName()); 
			this.objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
			this.objBufferedWriter.append(printSignData.getQuantity().toString());
		} catch (IOException e) {
			LOGGER.error(" Error occoured at addingDataToCSVForPrintSign method of PSCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addingDataToCSVForPrintSign method of PSCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addingDataToCSVForPrintSign method of PSCSVFileCustomWriter ");
			}
		} 
	}

	
	
	/**
	 * Retrieve value get the 'refDataKeyForPrintSigns' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of PSCSVFileCustomWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			this.lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForPrintSigns");
			final String fileLocation = this.lCAndPSReportPrefDataBean.getFileLocation();
			/*As Print signs Content's only one file so directly taken 0 index*/
			final String fileName = this.lCAndPSReportPrefDataBean.getFileNameList().get(0); 
			if (CommonUtil.isNull(objBufferedWriter)) {
				this.objBufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Print Sign CSV file location is : " + fileLocation);
				LOGGER.debug(" Print Sign CSV file name is : " + fileName);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of PSCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of PSCSVFileCustomWriter ");
			}
		}
	}
	
	
	/**
	 * This method Close the file after flushing dates.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void closeFile() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering closeFile method of PSCSVFileCustomWriter ");
		}
		try {
			if (!CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error(" Error occoured at closeFile method of PSCSVFileCustomWriter ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting closeFile method of PSCSVFileCustomWriter ");
			}
		}
	}
	
	
}
