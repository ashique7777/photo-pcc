/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.CSVFileReportDataBean;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.PMByWICReportRowmapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * 	Custom item reader implements Spring itemReader 
 * </p>
 * 
 * <p>
 * class will read report data from database using filter criteria
 * </p>
 * {@link CSVFileItemReader} is a business implementation class for {@link ItemReader}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class CSVFileItemReader implements ItemReader<CSVFileReportDataBean>{

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;
	
	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean;
	
	List<CSVFileReportDataBean>  lstCSVFileReportDataBean = null;
	
	CSVFileReportDataBean objCSVFileReportDataBean = null;
	
	int count = 0;
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
	private int paginationCounter = 2;


	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVFileItemReader.class);

	/**
	 * Reader method  which will read pmBywic data set the same to data bean
	 * 
	 * @param CSVFileReportDataBean -- pmByWic data bean to hold report data
	 * @throws NonTransientResourceException
	 * @throws UnexpectedInputException
	 * @throws ParseException
	 * @throws Exception
	 * 
	 */
	public CSVFileReportDataBean read() throws Exception,
	UnexpectedInputException, ParseException,
	NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVFileItemReader.read()");
		}
		try{
			objCSVFileReportDataBean = new CSVFileReportDataBean();
			String startDate = "", endDate = "";
			if(objPMBYWICReportPrefDataBean.getReportType().equals(PhotoOmniBatchConstants.PMBYWIC_CUSTOM)){
				final JSONObject objJson = new JSONObject(objPMBYWICReportPrefDataBean.getFilterState());
				startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
				endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
			}else{
				String strStartDateAndEndDateQuery = ReportsQuery.getStartDateAndEndDate().toString();
				List<Map<String, Object>> lstDates = jdbcTemplate.queryForList(strStartDateAndEndDateQuery);
				for (Map<String, Object> map : lstDates) {
					startDate = (String) map.get("STARTDATE");
					endDate = (String) map.get("ENDDATE"); 
				}
			}
			startDate = startDate+" 00:00:00";
			endDate = endDate+" 23:59:59";
			final Object[] objparam = {startDate, endDate, startDate, startDate, endDate, startDate, pageBegin, (pageBegin + paginationCounter - 1)};
			objCSVFileReportDataBean = this.getPMByCustomCSVData(objparam);
		}
		catch (Exception e) {
			LOGGER.error(" Error occoured at CSVFileItemReader.read() --> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From CSVFileItemReader.read()");
			}
		}
		return objCSVFileReportDataBean;
	}

	/**
	 * This method get the CSV Data for PMByWIC Custom Report.
	 * @param objparam contains query param vales.
	 * @return dailyData
	 * @throws PhotoOmniException 
	 */
	private CSVFileReportDataBean getPMByCustomCSVData(final Object[] objparam) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into CSVFileItemReader.getPMByCustomCSVData() ");
		}
		CSVFileReportDataBean objCSVFileReportDataBean = null;
		try {
			if (!queryFlag) {
				String strCSVDataSelectQuery = ReportsQuery.getPMBYWICCSVDataQuery().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" strCSVDataSelectQuery in CSVFileItemReader.getPMByCustomCSVData()" + strCSVDataSelectQuery);
				}
				lstCSVFileReportDataBean = jdbcTemplate.query(strCSVDataSelectQuery, objparam, new PMByWICReportRowmapper());
				queryFlag = true;
				counter = 0;
			}
			if (lstCSVFileReportDataBean != null && counter < lstCSVFileReportDataBean.size()) {
				if (counter == (lstCSVFileReportDataBean.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				objCSVFileReportDataBean = lstCSVFileReportDataBean.get(counter++);

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting From CSVFileItemReader.getPMByCustomCSVData() ");
				}
			} else {
				objCSVFileReportDataBean = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at CSVFileItemReader.getPMByCustomCSVData() >---> " + e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at CSVFileItemReader.getPMByCustomCSVData() >---> " + e);
			throw new PhotoOmniException(e.getMessage());
		}  finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From CSVFileItemReader.getPMByCustomCSVData() >--->  ");
			}
		}
		return objCSVFileReportDataBean;
	}

	/**
	 * Method to retrieve pmBywic report bean and set the same into pmBywicReprotBean
	 * 
	 * @param objStepExecution -- Execution Context holder
	 * @throws PhotoOmniException -- Custom Exception
	 */
	@BeforeStep
	private void retriveValue(StepExecution objStepExecution) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering CSVFileItemReader.retriveValue()");
		}
		try {

			JobExecution objJobExecution = objStepExecution.getJobExecution();
			ExecutionContext objExecutionContext = objJobExecution.getExecutionContext();
			objPMBYWICReportPrefDataBean = (PMBYWICReportPrefDataBean) objExecutionContext.get("refDataKey");

		}catch (Exception e) {
			LOGGER.error(" Error occoured at SVFileItemReader.retriveValue() ---> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting CSVFileItemReader.retriveValue() ");
			}
		}
	}
}
