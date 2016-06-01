/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import java.util.ArrayList;
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

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.bean.SalesReportByProductDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.SalesReportByProducDatatRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * This is a custom item reader class implementing spring itemReader.
 * This class will read adoc report data from database using filter criteria.
 * </p>
 * {@link SalesReportByProductFileItemReader} is a business implementation class for {@link ItemReader}
 * This class is used to read Sales report data from database
 *  @author CTS
 * @since v1.0
 */
public class SalesReportByProductFileItemReader implements
ItemReader<SalesReportByProductDataBean> {

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;

	private boolean queryFlag = false;
	/**
	 * counter
	 */
	private int counter = 0;
	/**
	 * pageBegin
	 */
	private int pageBegin = 1;
	/**
	 * paginationCounter
	 */
	private int paginationCounter = 20;

	private StepExecution stepExecution;


	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductFileItemReader.class);

	private SalesReportByProductBean salesReportByProductBean; 

	private List<SalesReportByProductDataBean>  salesReportByProductBeanList;
	/**
	 * Method to get sales report data for report generation
	 * 
	 * @return SalesReportByProductDataBean - Sales report data bean . 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@Override
	public SalesReportByProductDataBean read() throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductFileItemReader.read() method ");
		}
		SalesReportByProductDataBean salesReportByProductDataBean = null;
		try{
			JobExecution jobExecution = stepExecution.getJobExecution();
			ExecutionContext executionContext = jobExecution.getExecutionContext();
			salesReportByProductBean = (SalesReportByProductBean) executionContext.get("refDataKey");
			salesReportByProductDataBean = this.getSalesReportData();
		}catch(Exception e){
			LOGGER.error("Exception Occured in read method of SalesReportByProductFileItemReader  -- > "+ e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting read method of SalesReportByProductFileItemReader  -- >");
			}
		}
		return salesReportByProductDataBean;
	}
	/**
	 * Method to get stepExecutionContext
	 * this method will execute before step execution begins
	 * @param stepExecution
	 */
	@BeforeStep
	private void retriveValue(StepExecution stepExecution){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entered into SalesReportByProductFileItemReader.retriveValue() method");
		}
		this.stepExecution  = stepExecution;
	}
	/**
	 * Method to get Sales Report query 
	 * 
	 * @param filterMap --map of filter details
	 * @return report -- query
	 */
	@SuppressWarnings("unchecked")
	private String  getReportQuery(Map<String, Object> filterMap, List<Object> parameterList)
			throws PhotoOmniException{
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductFileItemReader.getReportQuery() method ");
		}
		String query = null;
		try {
			final String startDate = CommonUtil.stringDateFormatChange( (String) filterMap.get(PhotoOmniBatchConstants.START_DATE), 
					PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
			final String endDate = CommonUtil.stringDateFormatChange((String) filterMap.get(PhotoOmniBatchConstants.END_DATE), 
					PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
			final List<String> productTypeList = (List<String>) filterMap.get(PhotoOmniBatchConstants.PRODUCTTYPE);
			final List<String> printSizeList = (List<String>) filterMap.get(PhotoOmniBatchConstants.PRINTSIZE);

			for(String productType : productTypeList){
				parameterList.add(productType.trim()); 
			}
			for(String printSize : printSizeList){
				parameterList.add(printSize.trim());
			}
			parameterList.add("KIOSK_PROD");
			parameterList.add(startDate);
			parameterList.add(endDate);
			parameterList.add(startDate);
			parameterList.add(pageBegin);
			parameterList.add((pageBegin + paginationCounter - 1));
			query = ReportsQuery.getsalesDataQuery(productTypeList, printSizeList)
					.toString();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at SalesReportByProductFileItemReader.getReportQuery() method  - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting from SalesReportByProductFileItemReader.getReportQuery()  method ");
			}
		}
		return query;
	}


	/**
	 * This method get the data for Sales report.
	 * 
	 * @return SalesReportByProductDataBean - Report data bean
	 * @throws PhotoOmniException - Custom exception
	 */
	private SalesReportByProductDataBean getSalesReportData() throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductFileItemReader.getSalesReportData() method ");
		}
		SalesReportByProductDataBean salesReportByProductDataBean = null;
		try {
			if (!queryFlag) {
				List<Object> parameterList = new ArrayList<Object>();
				String salesReportQuery = getReportQuery(salesReportByProductBean.getFilterMap(), parameterList);
				salesReportByProductBeanList = jdbcTemplate.query(salesReportQuery, parameterList.toArray(), new SalesReportByProducDatatRowmapper());
				queryFlag = true;
				counter = 0;
			}
			if (salesReportByProductBeanList != null && counter < salesReportByProductBeanList.size()) {
				if (counter == (salesReportByProductBeanList.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				salesReportByProductDataBean = salesReportByProductBeanList.get(counter++);
			} else {
				salesReportByProductDataBean = null;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at SalesReportByProductFileItemReader.getSalesReportData() method  - " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting from SalesReportByProductFileItemReader.getSalesReportData() method ");
			}
		}
		return salesReportByProductDataBean;
	}

}

