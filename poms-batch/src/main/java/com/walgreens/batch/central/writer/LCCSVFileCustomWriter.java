
/**
 * LCCSVFileCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 9 mar 2015
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
import com.walgreens.batch.central.bean.ExceptionCSVFileReportDataBean;
import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.LCCSVFileReportDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class getting the data from the reader and 
 * creating data structure for the CSV.
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 09, 2015
 */

public class LCCSVFileCustomWriter implements ItemWriter<LCCSVFileReportDataBean>{
	/**
     * LOGGER for Logging
     */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCCSVFileCustomWriter.class);
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * adhocBufferedWriter
	 */
	BufferedWriter adhocBufferedWriter = null;
	/**
	 * exceptionBufferedWriter
	 */
	BufferedWriter exceptionBufferedWriter = null;
	
	
	 /**
     * This method flushing the data to the CSV file.
     * @param items contains item data.
     * @throws PhotoOmniException - Custom Exception.
     */
	public void write(final List<? extends LCCSVFileReportDataBean > items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of LCAndPSReportPrefDataBean ");
		}
		try {
			for (LCCSVFileReportDataBean lCCSVFileReportDataBean : items) {
				AdhocAndDailyCSVFileReportDataBean adhocData = lCCSVFileReportDataBean.getAdhocReportResBean();
				ExceptionCSVFileReportDataBean exceptionData = lCCSVFileReportDataBean.getExceptionReportResBean();
					if (!CommonUtil.isNull(adhocData)) {
						/* For LC Adhoc Report */
						adhocBufferedWriter.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);
						this.addingDataToCSVForAdhoc(adhocData);
					} 
					if (!CommonUtil.isNull(exceptionData)) {
						/* For LC Exception Report */
						exceptionBufferedWriter.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);
						this.addingDataToCSVForException(exceptionData);
					}
			}
			if (!CommonUtil.isNull(adhocBufferedWriter)) {
				adhocBufferedWriter.flush();
			}
			if (!CommonUtil.isNull(exceptionBufferedWriter)) {
				exceptionBufferedWriter.flush();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of LCCSVFileCustomWriter ");
			}
		}
	}


	/**
	 * This Method add data to ExceptionCSVFileReportDataBean
	 * @param exceptionData
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private void addingDataToCSVForException(
			final ExceptionCSVFileReportDataBean exceptionData) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addingDataToCSVForException method of LCCSVFileCustomWriter ");
		}
		try {
			exceptionBufferedWriter.append(CommonUtil.isNull(exceptionData.getStore()) ? "" : exceptionData.getStore().toString() );
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(CommonUtil.isNull(exceptionData.getOrderID()) ? "" : exceptionData.getOrderID().toString());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(CommonUtil.isNull(exceptionData.getProductID()) ? "" : exceptionData.getProductID().toString());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(exceptionData.getProductDescription());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(CommonUtil.convertTimestampToString(exceptionData.getExceptionDate(),
					PhotoOmniConstants.DATE_FORMAT_ELEVEN));
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(exceptionData.getExceptionType());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(exceptionData.getExceptionDescription());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(exceptionData.getRemarks());
			exceptionBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			exceptionBufferedWriter.append(CommonUtil.convertTimestampToString(exceptionData.getDatetimeCreated(),
					PhotoOmniConstants.DATE_FORMAT_ELEVEN));
		} catch (IOException e) {
			LOGGER.error(" Error occoured at addingDataToCSVForException method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addingDataToCSVForException method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addingDataToCSVForException method of LCCSVFileCustomWriter ");
			}
	    }
	}


	/**
	 * This method add data to AdhocAndDailyCSVFileReportDataBean
	 * @param adhocDataBean
	 * @@throws BatchException - Custom Exception.
	 */
	private void addingDataToCSVForAdhoc(final AdhocAndDailyCSVFileReportDataBean adhocDataBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addingDataToCSVForAdhoc method of LCCSVFileCustomWriter ");
		}
		try {
			adhocBufferedWriter.append(CommonUtil.convertTimestampToString(adhocDataBean.getOrderDate(), 
					PhotoOmniConstants.DATE_FORMAT_ELEVEN)); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getQuantity()) ? "" : 
					adhocDataBean.getQuantity().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getProvider()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getLocationType()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getLocationNumb()) ? "" : 
				adhocDataBean.getLocationNumb().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getCalRetailPrice()) ? "" :
				adhocDataBean.getCalRetailPrice().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getDiscountApplied()) ? "" :
				adhocDataBean.getDiscountApplied().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getEmployeeDiscount()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getNetSale()) ? "" :
				adhocDataBean.getNetSale().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getProductDescription()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getuPC()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getwIC()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getLicenseContOrTempId()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getEnvelopeNumber()) ? "" :
				adhocDataBean.getEnvelopeNumber().toString()); 
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(CommonUtil.isNull(adhocDataBean.getOriginalRetailPrice()) ? "" :
				adhocDataBean.getOriginalRetailPrice().toString());
			adhocBufferedWriter.append(PhotoOmniConstants.COMMA_DELIMITER); 
			adhocBufferedWriter.append(adhocDataBean.getOrderStatus());
		} catch (IOException e) {
			LOGGER.error(" Error occoured at addingDataToCSVForAdhoc method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addingDataToCSVForAdhoc method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addingDataToCSVForAdhoc method of LCCSVFileCustomWriter ");
			}
		}
	}

	/**
	 * Retrieve value get the 'refDataKeyForAdhocAndException' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCCSVFileCustomWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForAdhocAndException");
			final String fileLocation = lCAndPSReportPrefDataBean.getFileLocation();
			/*As License content consist two reports*/
			final List<String> fileNames = lCAndPSReportPrefDataBean.getFileNameList();
			for (String fileName : fileNames) {
				if (fileName.contains(PhotoOmniConstants.LC_ADHOC_REPORT_NAME)) {
					/* For LC Adhoc Report */
					if (CommonUtil.isNull(adhocBufferedWriter)) {
						adhocBufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));
					}
				} else {
					/* For LC Exception Report */
					if (CommonUtil.isNull(exceptionBufferedWriter)) {
						exceptionBufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCCSVFileCustomWriter - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCCSVFileCustomWriter ");
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
			LOGGER.debug(" Entering closeFile method of LCCSVFileCustomWriter ");
		}
		try {
			if (!CommonUtil.isNull(adhocBufferedWriter)) {
				adhocBufferedWriter.close();
			}
			if (!CommonUtil.isNull(exceptionBufferedWriter)) {
				exceptionBufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error(" Error occoured at closeFile method of LCCSVFileCustomWriter ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting closeFile method of LCCSVFileCustomWriter ");
		}
	}
}
