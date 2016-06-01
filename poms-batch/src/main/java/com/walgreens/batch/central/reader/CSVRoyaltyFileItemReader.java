/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.CSVFileRoyaltyReportDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.RoyaltyReportRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * 	Custom item reader implements Spring itemReader to to read report data from
 * database using filter criteria
 * </p>
 * 
 * <p>
 * This will read the filter criteria from the royalty report bean and using filter criteria 
 * it will fetch the report data from the database
 * </p>
 * 
 * {@link CSVRoyaltyFileItemReader} is a business implementation class for {@link ItemReader}
 * This class is used to read royalty bean from execution context 
 * and to get the royalty data for the given report id
 *  @author CTS
 * @since v1.0
 */
public class CSVRoyaltyFileItemReader implements ItemReader<CSVFileRoyaltyReportDataBean>{


	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;

	private boolean queryFlag = false;

	private int counter = 0;

	private int pageBegin = 1;

	private int paginationCounter = 50;

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVRoyaltyFileItemReader.class);

	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;

	List<CSVFileRoyaltyReportDataBean>  csvFileRoyaltyReportDataBeanList = null;
	/**
	 * Method will read report data from the database and set the same 
	 * into database for further processing
	 * 
	 * @return Royalty report. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public CSVFileRoyaltyReportDataBean read() throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyFileItemReader.read() method-- >");
		}
		CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean = null;
		try {
			if(null != royaltySalesReportPrefDataBean){
				csvFileRoyaltyReportDataBean = new CSVFileRoyaltyReportDataBean();
			final Map<String, Object> filterMap =royaltySalesReportPrefDataBean.getFilterMap();
			final String startDate = CommonUtil.stringDateFormatChange( filterMap.get(PhotoOmniBatchConstants.START_DATE).toString(), 
					PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
			final String endDate = CommonUtil.stringDateFormatChange( filterMap.get(PhotoOmniBatchConstants.END_DATE).toString(), 
					PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
			Object [] params = {(String) filterMap.get(PhotoOmniBatchConstants.VENDOR), "KIOSK_PROD",  startDate, endDate,
					startDate , pageBegin, (pageBegin + paginationCounter - 1)};				
			csvFileRoyaltyReportDataBean = this.getRoyaltyData(params);
		}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at CSVRoyaltyFileItemReader.read() method ---- > " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {	
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from CSVRoyaltyFileItemReader.read() method --- >");
			}
		}
		return csvFileRoyaltyReportDataBean;
	}

	/**
	 * Method to retrieve the royalty bean from execution context
	 * this method will be executed before the step execution starts
	 * 
	 * @param stepExecution
	 * @throws PhotoOmniException
	 */
	@BeforeStep
	private void retrieveValue(StepExecution stepExecution) throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug("Entered into  CSVFileRoyaltyCustomWriter.retrieveValue()  --- >");
		}
		try{
			JobExecution jobExecution = stepExecution.getJobExecution();
			ExecutionContext executionContext = jobExecution.getExecutionContext();
			royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
		}catch (Exception e) {
			LOGGER.error(" Error occoured at gCSVRoyaltyFileItemReader.retrieveValue() method - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from CSVRoyaltyFileItemReader.retrieveValue() method ");
			}
		}
	}

	/**
	 * This method get the data for Royalty report.
	 * @param paramA contains query param vales.
	 * @return Royalty data bean
	 * @throws PhotoOmniException 
	 */
	private CSVFileRoyaltyReportDataBean getRoyaltyData(final Object[] param) throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into CSVRoyaltyFileItemReader.getRoyaltyData() method ");
		}

		CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean = null;
		try {
			if (!queryFlag) {
				String royaltyReportQuery = ReportsQuery.getRoyaltyReportQuery().toString();
				LOGGER.info(" Royalty Sales Adoch Report SQL Query is : "+ royaltyReportQuery);
				csvFileRoyaltyReportDataBeanList = jdbcTemplate.query(royaltyReportQuery, param, new RoyaltyReportRowmapper());
				queryFlag = true;
				counter = 0;
			}
			if (csvFileRoyaltyReportDataBeanList != null && counter < csvFileRoyaltyReportDataBeanList.size()) {
				if (counter == (csvFileRoyaltyReportDataBeanList.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				csvFileRoyaltyReportDataBean = csvFileRoyaltyReportDataBeanList.get(counter++);
			} else {
				csvFileRoyaltyReportDataBean = null;
			}
		}catch (Exception e) {
			LOGGER.error(" Error occoured at gCSVRoyaltyFileItemReader.getRoyaltyData() method - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from CSVRoyaltyFileItemReader.getRoyaltyData() method ");
			}
		}
		return csvFileRoyaltyReportDataBean;
	}	
}

