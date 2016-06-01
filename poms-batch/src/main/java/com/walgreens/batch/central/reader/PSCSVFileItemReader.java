/**
 * PSCSVFileItemReader.java 
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
package com.walgreens.batch.central.reader;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;
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

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.PrintSignsCSVFileReportDataBean;
import com.walgreens.batch.central.rowmapper.PrintSignsReportDataRowmapper;
import com.walgreens.batch.query.PrintSignsReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This reader class getting the data for the CSV report
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 01, 2015
 */

public class PSCSVFileItemReader implements ItemReader<PrintSignsCSVFileReportDataBean>{
    /**
     * jdbcTemplate
     */
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSCSVFileItemReader.class);
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
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
	 * reportDataList
	 */
	List<PrintSignsCSVFileReportDataBean> reportDataList = null;
	
	/**
	 * This method getting the data for the CSV report.
	 * @return reportDataList. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public PrintSignsCSVFileReportDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of PSCSVFileItemReader ");
		}
		PrintSignsCSVFileReportDataBean printSignData = new PrintSignsCSVFileReportDataBean();
		try {
					final JSONObject objJson = new JSONObject(lCAndPSReportPrefDataBean.getFilterState());
					final String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
							PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
					final String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
							PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
					final String eventId = objJson.getString("eventId");
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Print Sign Report generator param values are : startDate-->"+startDate+" endDate-->"+endDate+" eventId-->"+eventId);
					}
					final Object[] param = {eventId, startDate, endDate, pageBegin, (pageBegin + paginationCounter - 1)};
					printSignData = this.getPrintSignData(param);
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of PSCSVFileItemReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of PSCSVFileItemReader ");
				}
			}
			return printSignData;
	}

	/**
	 * This method get the data for PrintSign report.
	 * @param paramA contains query param vales.
	 * @return printSignData
	 * @throws PhotoOmniException 
	 */
	private PrintSignsCSVFileReportDataBean getPrintSignData(final Object[] param) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getPrintSignData method of PSCSVFileItemReader ");
		}
		PrintSignsCSVFileReportDataBean printSignData = null;
		try {
			if (!queryFlag) {
				final String sqlQuery = PrintSignsReportQuery.getPrintSignsQuery().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Print Sign Report Query is  : " + sqlQuery);
				}
				reportDataList = getJdbcTemplate().query(sqlQuery,param, new PrintSignsReportDataRowmapper());
				queryFlag = true;
				counter = 0;
			}
			if (reportDataList != null && counter < reportDataList.size()) {
				if (counter == (reportDataList.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				printSignData = reportDataList.get(counter++);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of MachineItemReader ");
				}
			} else {
				printSignData = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getPrintSignData method of PSCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPrintSignData method of PSCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getPrintSignData method of PSCSVFileItemReader ");
			}
		}
		return printSignData;
	}
	
	/**
	 * Retrieve value get the 'refDataKeyForPrintSigns' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of retriveValue ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForPrintSigns");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of PSCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting read method of PSCSVFileItemReader ");
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
