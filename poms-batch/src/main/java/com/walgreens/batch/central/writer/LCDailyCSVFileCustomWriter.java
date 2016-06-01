

/**
 * LCDailyCSVFileCustomWriter.java 
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

import com.walgreens.batch.central.bean.AdhocAndDailyCSVFileReportDataBean;
import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bean.PrintSignsCSVFileReportDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class getting the data from the reader and 
 * creating data structure for the CSV.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyCSVFileCustomWriter implements ItemWriter<AdhocAndDailyCSVFileReportDataBean>{
	/**
	 * printSignsCSVFileReportDataBean
	 */
	PrintSignsCSVFileReportDataBean printSignsCSVFileReportDataBean;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyCSVFileCustomWriter.class);
	/**
	 * lCDailyReportPrefDataBean
	 */
	LCDailyReportPrefDataBean lCDailyReportPrefDataBean;
	/**
	 * objBufferedWriter
	 */
	BufferedWriter objBufferedWriter = null;
    
	
	 /**
     * This method flushing the data to the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends AdhocAndDailyCSVFileReportDataBean > items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCDailyCSVFileCustomWriter ");
		}
		try {
			for (AdhocAndDailyCSVFileReportDataBean dailyData : items) {
				if (!CommonUtil.isNull(objBufferedWriter)) {
					objBufferedWriter.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);
					this.addingDataToCSVForDaily(dailyData);
				} 
			}
			if (!CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter.flush();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCDailyCSVFileCustomWriter ");
			}
		
	      }
	}


	/**
	 * This Method add data to ExceptionCSVFileReportDataBean
	 * @param exceptionData
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private void addingDataToCSVForDaily(final AdhocAndDailyCSVFileReportDataBean adhocDataBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addingDataToCSVForDaily method of LCDailyCSVFileCustomWriter ");
		}
		try {
			objBufferedWriter.append(CommonUtil.convertTimestampToString(adhocDataBean.getOrderDate(), 
					PhotoOmniConstants.DATE_FORMAT_ELEVEN)); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getQuantity()) ? "" : 
					adhocDataBean.getQuantity().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getProvider()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getLocationType()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getLocationNumb()) ? "" : 
				adhocDataBean.getLocationNumb().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getCalRetailPrice()) ? "" :
				adhocDataBean.getCalRetailPrice().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getDiscountApplied()) ? "" :
				adhocDataBean.getDiscountApplied().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getEmployeeDiscount()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getNetSale()) ? "" :
				adhocDataBean.getNetSale().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getProductDescription()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getuPC()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getwIC()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getLicenseContOrTempId()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getEnvelopeNumber()) ? "" :
				adhocDataBean.getEnvelopeNumber().toString()); 
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getOriginalRetailPrice()) ? "" :
				adhocDataBean.getOriginalRetailPrice().toString());
			objBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			objBufferedWriter.append(adhocDataBean.getOrderStatus());
		} catch (IOException e) {
			LOGGER.error(" Error occoured at process method of LCDailyCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addingDataToCSVForDaily method of LCDailyCSVFileCustomWriter ");
			}
		}
	}

	
	
	/**
	 * Retrieve value get the 'refDataKeyForDaily' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCDailyCSVFileCustomWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCDailyReportPrefDataBean = (LCDailyReportPrefDataBean) executionContext.get("refDataKeyForDaily");
			final String fileLocation = lCDailyReportPrefDataBean.getFileLocation();
			/*As License content daily report consist one report*/
			final String fileName = lCDailyReportPrefDataBean.getFileNameList().get(0);
			if (CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCDailyCSVFileCustomWriter ");
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
			LOGGER.debug(" Entering closeFile method of LCDailyCSVFileCustomWriter ");
		}
		try {
			if (!CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error(" Error occoured at closeFile method of LCDailyCSVFileCustomWriter ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting closeFile method of LCDailyCSVFileCustomWriter ");
		}
	}
}
