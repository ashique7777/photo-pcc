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
import com.walgreens.batch.central.bean.CSVFileReportDataBean;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
* <p>
* 	Custom item writer implements Spring itemWriter
* </p>
* 
* <p>
* Class to populate report data into CSV file
* </p>
* {@link CSVFileCustomWriter} is a business implementation class for {@link ItemWriter}
* This class is used to update report status as inactive
* @author CTS
* @since v1.0
*/
public class CSVFileMonthlyWriter implements ItemWriter<CSVFileReportDataBean>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileMonthlyWriter.class);

	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;
	
	BufferedWriter objBufferedWriter = null;

	/**
	 * method to set report data into csv file
	 * 
	 * @param items -- List of pmBYwic data beans
	 * @throws Exception
	 */
	public void write(List<? extends CSVFileReportDataBean> items)
			throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVFileMonthlyWriter.write() ");
		}

		try {

			String fileLocation = objPMBYWICReportPrefDataBean.getFileLocation();
			String fileName = objPMBYWICReportPrefDataBean.getFileNameList().get(0);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" inside CSVFileMonthlyWriter.write() fileLocation : " + fileLocation +" fileName : "+ fileName);
			}
			objBufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));

			for (CSVFileReportDataBean objCsvFileReportDataBean : items) { 
				
				objBufferedWriter.append(objCsvFileReportDataBean.getWIC()); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append(objCsvFileReportDataBean.getProductDescription()); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append("$").append(String.valueOf(objCsvFileReportDataBean.getCalculatedRetail())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append("$").append(String.valueOf(objCsvFileReportDataBean.getAmountOfPMPaid())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append("$").append(String.valueOf(objCsvFileReportDataBean.getSales()));
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append(String.valueOf(objCsvFileReportDataBean.getNumberOfOrders())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append(String.valueOf(objCsvFileReportDataBean.getTotalQuantity())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER); 
				objBufferedWriter.append("$").append(String.valueOf(objCsvFileReportDataBean.getItemCost())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
				objBufferedWriter.append("$").append(String.valueOf(objCsvFileReportDataBean.getGrossProfit())); 
				objBufferedWriter.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR); 

			} 
			//Flush the file
			objBufferedWriter.flush();
		} catch (IOException e) {
			LOGGER.error(" Error occoured at CSVFileMonthlyWriter.write() --- >" + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at CSVFileMonthlyWriter.write() ----> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting Form CSVFileMonthlyWriter.write() ");
			}
		}
	}

	/**
	 * Method to retrieve pmBywic report bean and set the same into pmBywicReprotBean
	 * 
	 * @param objStepExecution -- Execution Context holder
	 * @throws PhotoOmniException -- Custom Exception
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVFileMonthlyWriter.retriveValue()");
		}
		try {

			JobExecution jobExecution = stepExecution.getJobExecution();
			ExecutionContext executionContext = jobExecution.getExecutionContext();
			objPMBYWICReportPrefDataBean = (PMBYWICReportPrefDataBean) executionContext.get("refDataKey");
		}
		catch (Exception e) {
			LOGGER.error(" Error occoured at CSVFileMonthlyWriter.retriveValue() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From  CSVFileMonthlyWriter.retriveValue() ");
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
			LOGGER.debug(" Entering into CSVFileMonthlyWriter.closeFile() ");
		}
		try {
			if (!CommonUtil.isNull(objBufferedWriter)) {
				objBufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error(" Error occoured at CSVFileMonthlyWriter.closeFile() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting From  CSVFileMonthlyWriter.closeFile() ");
		}
	}
}
