
/**
 * LCDailyCSVFileItemReader.java 
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.AdhocAndDailyCSVFileReportDataBean;
import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.rowmapper.LCAdhocAndDailyReportRowmapper;
import com.walgreens.batch.query.LicenseContentReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This reader class getting the data for the CSV report
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyCSVFileItemReader implements ItemReader<AdhocAndDailyCSVFileReportDataBean>{
    /**
     * jdbcTemplate
     */
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyCSVFileItemReader.class);
	/**
	 * queryFlagAdhoc
	 */
	private boolean queryFlag = false;
	/**
	 * counterAdhoc
	 */
	private int counter = 0;
	/**
	 * pageBeginAdhoc
	 */
	private int pageBegin = 1;
	/**
	 * paginationCounterAdhoc
	 */
	private int paginationCounter = 500;
	/**
	 * lCDailyReportPrefDataBean
	 */
	LCDailyReportPrefDataBean lCDailyReportPrefDataBean;
	/**
	 * dailyReportDataList
	 */
	List<AdhocAndDailyCSVFileReportDataBean> dailyReportDataList = null;
	
	
	/**
	 * This method getting the data for the CSV report.
	 * @return dailyReportDataList. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public AdhocAndDailyCSVFileReportDataBean read() throws  PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCDailyCSVFileItemReader ");
		}
		AdhocAndDailyCSVFileReportDataBean dailyData = new AdhocAndDailyCSVFileReportDataBean();
			try {
				String startDate = null;
				String endDate = null;
				String sqlForDateRange = LicenseContentReportQuery.getCurrentAndPreviousDate().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" License Content Daily Report and Date Range SQL Query is : "+ sqlForDateRange);
				}
				List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sqlForDateRange);
				for (Map<String, Object> map : rows) {
					startDate = (String) map.get("DATERANGE");
					endDate = startDate; /*Start date end date same for daily report(Previous date)*/
				}
				lCDailyReportPrefDataBean.setStartDate(CommonUtil.stringDateFormatChange(startDate, PhotoOmniConstants.DATE_FORMAT_TWO, PhotoOmniConstants.DATE_FORMAT_FIFTH));
				lCDailyReportPrefDataBean.setEndDate(CommonUtil.stringDateFormatChange(endDate, PhotoOmniConstants.DATE_FORMAT_TWO, PhotoOmniConstants.DATE_FORMAT_FIFTH));
				LOGGER.info(" Date range for License Content Daily report is : " + startDate + " to" + endDate);
				final Object[] param = {startDate, endDate, pageBegin, (pageBegin + paginationCounter - 1)};
				dailyData = this.getDailyData(param);
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCDailyCSVFileItemReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCDailyCSVFileItemReader ");
				}
			}
			return dailyData;
		} 
	
	/**
	 * This method get the data for Daily report.
	 * @param paramA contains query param vales.
	 * @return dailyData
	 * @throws PhotoOmniException 
	 */
	private AdhocAndDailyCSVFileReportDataBean getDailyData(final Object[] param) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getDailyData method of LCDailyCSVFileItemReader ");
		}
		AdhocAndDailyCSVFileReportDataBean dailyData = null;
		try {
			if (!queryFlag) {
				final String sqlQuery = LicenseContentReportQuery.getAdhocAndDailyReportQuery().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" License Content Daily Report SQL Query is : "+ sqlQuery);
				}
				dailyReportDataList = getJdbcTemplate().query(sqlQuery, param, new LCAdhocAndDailyReportRowmapper());
				queryFlag = true;
				counter = 0;
			}
			if (dailyReportDataList != null && counter < dailyReportDataList.size()) {
				if (counter == (dailyReportDataList.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				dailyData = dailyReportDataList.get(counter++);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of MachineItemReader ");
				}
			} else {
				dailyData = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getDailyData method of LCDailyCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getDailyData method of LCDailyCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getDailyData method of LCDailyCSVFileItemReader ");
			}
		}
		return dailyData;
	}
	   
		/**
		 * Retrieve value get the 'refDataKeyForDaily' value from execution context.
		 * @param stepExecution contains stepExecution value. 
		 * @throws PhotoOmniException - Custom Exception.
		 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCDailyCSVFileItemReader ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCDailyReportPrefDataBean = (LCDailyReportPrefDataBean) executionContext.get("refDataKeyForDaily");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of LCDailyCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCDailyCSVFileItemReader ");
			}
		}
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
}
