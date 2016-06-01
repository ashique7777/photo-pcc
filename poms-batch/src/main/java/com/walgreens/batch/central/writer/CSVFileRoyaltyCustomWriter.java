/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.CSVFileRoyaltyReportDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * <p>
 * 	Custom item writer implements Spring ItemWriter which will populate report data
 * into report excel file 
 * 
 * </p>
 * {@link CSVFileRoyaltyCustomWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to  populate data to the xls sheet
 * and to get the royalty data for the given report id
 *  @author CTS
 * @since v1.0
 */

public class CSVFileRoyaltyCustomWriter implements ItemWriter<CSVFileRoyaltyReportDataBean>{

	@Autowired
	private ReportBOFactory reportBOFactory;
	
	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	private  ReportsUtilBO reportsUtilBO ;
	
	private ExecutionContext executionContext;
	
	int summaryRowCount = 3;
	
	int dataSheetRowCount = 0;
	
	int headerCount = 0;
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileRoyaltyCustomWriter.class);
	
	FileInputStream fileInputStream ;

	/**
	 * This method populate royalty report data into report excel file
	 * 
	 *	@param Royalty report. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(List<? extends CSVFileRoyaltyReportDataBean > items)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVFileRoyaltyCustomWriter.write() method --- >");
		}
		try {
			reportsUtilBO = reportBOFactory.getReportsUtilBO();
			@SuppressWarnings("unchecked")
			List<CSVFileRoyaltyReportDataBean> csvFileRoyaltyReportDataBeanList = (List<CSVFileRoyaltyReportDataBean>) items;
			if(CollectionUtils.isNotEmpty(csvFileRoyaltyReportDataBeanList)){
				String fileLocation = royaltySalesReportPrefDataBean.getFileLocation();
				String fileName = royaltySalesReportPrefDataBean.getFileNameList().get(0);
				fileInputStream = new FileInputStream(new File(fileLocation.concat(fileName)));
			
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
				XSSFSheet summarySheet = workbook.getSheet(PhotoOmniBatchConstants.SUMMARY);
				/** To set the data for the summary sheet */
				for(CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean:csvFileRoyaltyReportDataBeanList){
					XSSFRow row = summarySheet.createRow(summaryRowCount);
					summaryRowCount++;
					reportsUtilBO.populateRoyaltyReportSummaryData(csvFileRoyaltyReportDataBean, row);
				}
				if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Completed reading excel file and populating data to the summary sheet");
				}
				/**
				 * To get the data grouping 
				 * to populate the grouped data to the XLSX
				 */
				Map<String, List<CSVFileRoyaltyReportDataBean>> royaltyDataMap = new HashMap<String, List<CSVFileRoyaltyReportDataBean>>();
				String groupBy = royaltySalesReportPrefDataBean.getGroupBy();
				if(PhotoOmniBatchConstants.PRODUCT_TYPE.equalsIgnoreCase(groupBy)){
						royaltyDataMap = reportsUtilBO.groupByProductName(csvFileRoyaltyReportDataBeanList);
				}else if (PhotoOmniBatchConstants.PRINT_SIZE.equalsIgnoreCase(groupBy)){
					royaltyDataMap = reportsUtilBO.groupByPrintSize(csvFileRoyaltyReportDataBeanList);
				}
				for(Entry<String, List<CSVFileRoyaltyReportDataBean>> entry :royaltyDataMap.entrySet()){
					List<CSVFileRoyaltyReportDataBean> royaltyDataBeanList = entry.getValue();
					
					XSSFSheet dataSheet = null;
					XSSFRow dataheaderRow = null;
					if(null != workbook.getSheet(entry.getKey())){
						dataSheet =workbook.getSheet(entry.getKey());
						dataSheetRowCount = dataSheet.getLastRowNum();
						dataheaderRow = dataSheet.createRow(dataSheetRowCount+1);
						 headerCount = 1;
					}else {
						 dataSheetRowCount = 0;
						dataSheet =workbook.createSheet(entry.getKey());
						 dataheaderRow=dataSheet.createRow(dataSheetRowCount);
						 headerCount = 0;
					}
					CellStyle style = workbook.createCellStyle();
					XSSFFont fontTop = workbook.createFont();
					fontTop.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
					style.setFont(fontTop);
					style.setAlignment(CellStyle.ALIGN_RIGHT);
					
					dataheaderRow.setRowStyle(style);
					List<String> dataHeaderList = Arrays.asList(PhotoOmniBatchConstants.ROYALTY_DATA_HEADER.split(PhotoOmniBatchConstants.COMMA_DELIMITER));
					int dataColumnIndex = 0;
					if(headerCount ==0 ){
					for(String headerValue:dataHeaderList){
						XSSFCell cell = dataheaderRow.createCell(dataColumnIndex);
						cell.setCellValue(headerValue);
						cell.setCellStyle(style);
						dataColumnIndex++;
					}
					}
					for(CSVFileRoyaltyReportDataBean royaltyDataBean :royaltyDataBeanList){
						dataSheetRowCount++;
						XSSFRow dataValueRow = dataSheet.createRow(dataSheetRowCount);
						reportsUtilBO.populateRoyaltyReportData(royaltyDataBean, dataValueRow);
					}
				}
				if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Completed populating header and data to the data sheet");
				}
				fileInputStream.close();
				FileOutputStream out = new FileOutputStream(new File(fileLocation.concat(fileName)));
				workbook.write(out);
		}else{
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("No report request to process in CSVFileRoyaltyCustomWriter.write() method -- >");
			}
		}
		} catch (Exception e) { 
			LOGGER.error("Error in CSVFileRoyaltyCustomWriter.write() method--- >"+e);
			throw new PhotoOmniException(e.getMessage());
			 
		} finally { 
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from CSVFileRoyaltyCustomWriter.write() method -- >");  
			}
		} 
	}

	
	/**
	 * Method to get royalty data bean from the execution context
	 * this method will execute before step execution
	 * 
	 * @param stepExecution
	 * @throws PhotoOmniException
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution) throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entered into  CSVFileRoyaltyCustomWriter.retriveValue() --- >");
		}
		try {
		JobExecution jobExecution = stepExecution.getJobExecution();
		 executionContext = jobExecution.getExecutionContext();
		royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
		} catch (Exception e) { 
			LOGGER.error("Error in CSVFileRoyaltyCustomWriter.retriveValue() method--- >"+e);
			throw new PhotoOmniException(e.getMessage()); 
		} finally { 
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting from CSVFileRoyaltyCustomWriter.retriveValue() method -- >");  
			}
		} 
		
	}
}
