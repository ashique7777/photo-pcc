/* Copyright (c) 2015, Walgreens Co. */
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

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.bean.SalesReportByProductDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * This is a custom item writer class implementing spring itemWriter.
 * This class will read sales report data bean and save the 
 * same into repro csv file
 * </p>
 * {@link SalesReportByProductFileItemWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to  populate data to the csv file
 *  @author CTS
 * @since v1.0
 */
public class SalesReportByProductFileItemWriter implements
		ItemWriter<SalesReportByProductDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SalesReportByProductFileItemWriter.class);

	private SalesReportByProductBean salesReportByProductBean;
	
	private BufferedWriter bufferedWriter ;
	
	/**
	 * Method to retrieve the royalty bean from execution context
	 * @param stepExecution
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering SalesReportByProductFileItemWriter.retriveValue method -- >");
		}
		try {
			JobExecution jobExecution = stepExecution.getJobExecution();
			ExecutionContext executionContext = jobExecution.getExecutionContext();
			salesReportByProductBean = (SalesReportByProductBean) executionContext
					.get("refDataKey");
		}catch (Exception e) {
			LOGGER.error(" Exception occured at SalesReportByProductFileItemWriter.retriveValue method " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from SalesReportByProductFileItemWriter.retriveValue method ");
			}
		}
	}

	/**
	 * This method populate the data for the CSV report.
	 * 
	 * @param List<SalesReportByProductDataBean> 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@SuppressWarnings("unchecked")
	public void write(List<? extends SalesReportByProductDataBean> items)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductFileItemWriter.write method  -- >");
		}
		List<SalesReportByProductDataBean> salesReportByProductDataBeans =  (List<SalesReportByProductDataBean>) items;
		if(null != salesReportByProductBean){
			try {
				String fileLocation = salesReportByProductBean.getFileLocation();
				String fileName = salesReportByProductBean.getFileNameList().get(0);
				bufferedWriter = new BufferedWriter(new FileWriter(fileLocation.concat(fileName), true));			
				StringBuilder responseData = new StringBuilder(); 
				for (SalesReportByProductDataBean  SalesReportByProductDataBean : salesReportByProductDataBeans) {
					this.addingDataToCSVForSales(responseData, SalesReportByProductDataBean); 
				}
				bufferedWriter.append(responseData);	
			} catch (Exception e) {
				LOGGER.error("Exception occured at SalesReportByProductFileItemWriter.write method  -- >", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				try {
					bufferedWriter.flush();
				} catch (IOException e) {
					LOGGER.error(" Exception occured at SalesReportByProductFileItemWriter.write method -- >", e);
				}
			}
		}
	}
	
	/**
	 * This Method add data to Sales Report
	 * @param exceptionData
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private void addingDataToCSVForSales(StringBuilder responseData,  SalesReportByProductDataBean salesReportByProductDataBean) throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductFileItemWriter.addingDataToCSVForSales method  ");
		}	
		try {
			responseData.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);
			responseData.append(String.valueOf(salesReportByProductDataBean.getTemplateId()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(salesReportByProductDataBean.getCategory());
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(salesReportByProductDataBean.getDescription());
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(salesReportByProductDataBean.getOutputSize());
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(salesReportByProductDataBean.getVendor());
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean.getCount()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean.getQuantity()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean.getAmountPaid()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData
					.append(String.valueOf(salesReportByProductDataBean.getCalculatedRetail()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean.getOriginalRetail()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean.getOrderCost()));
			responseData
					.append(PhotoOmniBatchConstants.COMMA_DELIMITER);
			responseData.append(String.valueOf(salesReportByProductDataBean
					.getTotalDiscountAmount()));
		} catch (Exception e) {
			LOGGER.error(" Exception occured at SalesReportByProductFileItemWriter.addingDataToCSVForSales method   - " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.info(" Exiting from SalesReportByProductFileItemWriter.addingDataToCSVForSales method  ");
			}
		}
	}
	
	/**
	 * This method Close the file after flushing dates.
	 * 
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void closeFile() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVFileCustomWriter.closeFile() ");
		}
		try {
			if (!CommonUtil.isNull(bufferedWriter)) {
				bufferedWriter.close();
			}
		} catch (IOException e) {
			LOGGER.error(" Error occoured at CSVFileCustomWriter.closeFile() >---> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting From  CSVFileCustomWriter.closeFile() ");
			}
		}
	}
}
