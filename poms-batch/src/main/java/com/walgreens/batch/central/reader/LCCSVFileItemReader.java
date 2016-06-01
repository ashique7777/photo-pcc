
/**
 * LCCSVFileItemReader.java 
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
package com.walgreens.batch.central.reader;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
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

import com.walgreens.batch.central.bean.AdhocAndDailyCSVFileReportDataBean;
import com.walgreens.batch.central.bean.ExceptionCSVFileReportDataBean;
import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.LCCSVFileReportDataBean;
import com.walgreens.batch.central.rowmapper.LCAdhocAndDailyReportRowmapper;
import com.walgreens.batch.central.rowmapper.LCExceptionReportDataRowmapper;
import com.walgreens.batch.query.LicenseContentReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This reader class getting the data for the CSV report
 * Report process in central system as per action.
 * 
 * @author CTS
 * @version 1.1 March 09, 2015
 */

public class LCCSVFileItemReader implements ItemReader<LCCSVFileReportDataBean>{
    /**
     * jdbcTemplate
     */
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCCSVFileItemReader.class);
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean;
	/**
	 * queryFlagAdhoc
	 */
	private boolean queryFlagAdhoc = false;
	/**
	 * counterAdhoc
	 */
	private int counterAdhoc = 0;
	/**
	 * pageBeginAdhoc
	 */
	private int pageBeginAdhoc = 1;
	/**
	 * paginationCounterAdhoc
	 */
	private int paginationCounterAdhoc = 500;
	/**
	 * queryFlagException
	 */
	private boolean queryFlagException = false;
	/**
	 * counterException
	 */
	private int counterException = 0;
	/**
	 * pageBeginException
	 */
	private int pageBeginException = 1;
	/**
	 * paginationCounterException
	 */
	private int paginationCounterException = 500;
	/**
	 * adhocReportDataList
	 */
	private List<AdhocAndDailyCSVFileReportDataBean> adhocReportDataList = null;
	/**
	 * exceptionReportDataList
	 */
	private List<ExceptionCSVFileReportDataBean> exceptionReportDataList = null;
	
	/**
	 * This method getting the data for the CSV report.
	 * @return lCCSVFileReportDataBean. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public LCCSVFileReportDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of LCCSVFileItemReader ");
		}
		AdhocAndDailyCSVFileReportDataBean adhocData = new AdhocAndDailyCSVFileReportDataBean();
		ExceptionCSVFileReportDataBean exceptionData = new ExceptionCSVFileReportDataBean(); 
		LCCSVFileReportDataBean lCCSVFileReportDataBean = null;
			try {
						final JSONObject objJson = new JSONObject(lCAndPSReportPrefDataBean.getFilterState());
						final String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
								PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
						final String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
								PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
						final Object[] paramAdhoc = {startDate, endDate, pageBeginAdhoc, (pageBeginAdhoc + paginationCounterAdhoc - 1)};
						final Object[] paramException = {startDate, endDate, pageBeginException, (pageBeginException+ paginationCounterException - 1)};
						/*Below Block works for Adhoc report*/
						adhocData = this.getAdhocData(paramAdhoc);
						/*Below Block works for Exception report*/
						exceptionData = this.getExceptionData(paramException);
						if (adhocData != null || exceptionData != null ) {
							lCCSVFileReportDataBean = new LCCSVFileReportDataBean();
							lCCSVFileReportDataBean.setAdhocReportResBean(adhocData);
							lCCSVFileReportDataBean.setExceptionReportResBean(exceptionData);
						} 
						
			} catch (JSONException e) {
				LOGGER.error(" Error occoured at read method of LCCSVFileItemReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of LCCSVFileItemReader - ", e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of LCCSVFileItemReader ");
				}
			}
			return lCCSVFileReportDataBean;
	}

	/**
	 * This method get the data for exception report.
	 * @param paramException contains query param values.
	 * @throws PhotoOmniException custome exception.
	 */
	private ExceptionCSVFileReportDataBean getExceptionData(final Object[] paramException) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getExceptionData method of LCCSVFileItemReader ");
		}
		ExceptionCSVFileReportDataBean exceptionData = null;
		try {
			if (!queryFlagException) {
					final String sqlQueryException = LicenseContentReportQuery.getExceptionReportQuery().toString();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" License Content Exception Report SQL Query is : "+ sqlQueryException);
					}
					exceptionReportDataList = getJdbcTemplate().query(sqlQueryException,paramException,
							new LCExceptionReportDataRowmapper());
				queryFlagException = true;
				counterException = 0;
			}
			if (exceptionReportDataList != null && counterException < exceptionReportDataList.size()) {
				if (counterException == (adhocReportDataList.size() - 1)) {
					queryFlagException = false;
					pageBeginException += paginationCounterException;
				}
				exceptionData = exceptionReportDataList.get(counterException++);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of MachineItemReader ");
				}
			} else {
				exceptionData = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getExceptionData method of LCCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getExceptionData method of LCCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getExceptionData method of LCCSVFileItemReader ");
			}
		}
		return exceptionData;
	}

	/**
	 * This method get the data for Adhoc report.
	 * @param paramAdhoc contains query param vales.
	 * @return adhocData
	 * @throws PhotoOmniException 
	 */
	private AdhocAndDailyCSVFileReportDataBean getAdhocData(final Object[] paramAdhoc) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getAdhocData method of LCCSVFileItemReader ");
		}
		AdhocAndDailyCSVFileReportDataBean adhocData = null;
		try {
			if (!queryFlagAdhoc) {
					final String sqlQueryAdhoc = LicenseContentReportQuery.getAdhocAndDailyReportQuery().toString();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" License Content Adhoc Report and Daily Report SQL Query is : "+ sqlQueryAdhoc);
					}
					adhocReportDataList = getJdbcTemplate().query(sqlQueryAdhoc, 
							paramAdhoc, new LCAdhocAndDailyReportRowmapper());
				queryFlagAdhoc = true;
				counterAdhoc = 0;
			}
			if (adhocReportDataList != null && counterAdhoc < adhocReportDataList.size()) {
				if (counterAdhoc == (adhocReportDataList.size() - 1)) {
					queryFlagAdhoc = false;
					pageBeginAdhoc += paginationCounterAdhoc;
				}
				adhocData = adhocReportDataList.get(counterAdhoc++);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of MachineItemReader ");
				}
			} else {
				adhocData = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getAdhocData method of LCCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getAdhocData method of LCCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getAdhocData method of LCCSVFileItemReader ");
			}
		}
		return adhocData;
	}
      
	/**
	 * Retrieve value get the 'refDataKeyForAdhocAndException' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of LCCSVFileItemReader ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) executionContext.get("refDataKeyForAdhocAndException");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of PSCSVFileItemReader - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of LCCSVFileItemReader ");
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

