package com.walgreens.batch.central.reader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import com.walgreens.batch.central.bean.CSVFilePLUReportDataBean;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.rowmapper.CSVFilePLUReportDataBeanRowMapper;
import com.walgreens.batch.central.utility.DailyPLUQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUCSVFileItemReader implements
		ItemReader<CSVFilePLUReportDataBean> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUCSVFileItemReader.class);

	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate jdbcTemplate;

	List<CSVFilePLUReportDataBean> listCsvFilePLUReportDataBeans = null;
	private PLUReportPrefDataBean pluReportPrefDataBean;
	private CSVFilePLUReportDataBean csvFilePLUReportDataBean;

	/*
	 * To fetch data in chunk
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
	private int paginationCounter = 10;

	/**
	 * This method getting the data for the CSV report.
	 * 
	 * @return List of PLU report bean.
	 * @throws PhotoOmniException
	 *             - Custom Exception.
	 */
	@Override
	public CSVFilePLUReportDataBean read() throws Exception,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into read method of PLUCSVFileItemReader ");
		}
		String sqlQuery = "";
		try {
			csvFilePLUReportDataBean = new CSVFilePLUReportDataBean();
			if (pluReportPrefDataBean.getReportType().equals(
					PhotoOmniConstants.PLU_DAILY)) {
				sqlQuery = DailyPLUQuery.getPLURrecord().toString();
				Object[] paramsDailyPlu = { pageBegin,
						(pageBegin + paginationCounter - 1) };
				csvFilePLUReportDataBean = this.getPluData(paramsDailyPlu,
						sqlQuery);
			} else {
				Map<String, String> map = getPluFilterParams();
				Object[] objParams = {
						map.get(PhotoOmniBatchConstants.START_DATE),
						map.get(PhotoOmniBatchConstants.END_DATE),
						map.get(PhotoOmniBatchConstants.START_DATE),
						map.get(PhotoOmniBatchConstants.END_DATE), pageBegin,
						(pageBegin + paginationCounter - 1) };
				sqlQuery = DailyPLUQuery.getPLURrecordAdhoc(
						map.get(PhotoOmniConstants.PLUNUMBER)).toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" SQL query in the read method of PLUCSVFileItemReader ----> "
							+ sqlQuery);
				}
				csvFilePLUReportDataBean = this.getPluData(objParams, sqlQuery);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of PLUCSVFileItemReader ---- > "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting read method of PLUCSVFileItemReader ");
			}
		}
		return csvFilePLUReportDataBean;
	}

	private CSVFilePLUReportDataBean getPluData(Object[] params, String pluSql)
			throws PhotoOmniException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PLUCSVFileItemReader.getPluData() "
					+ pluReportPrefDataBean.getReportType());
		}
		CSVFilePLUReportDataBean csvFilePLUReportDataBean = null;
		try {
			if (!queryFlag) {
				listCsvFilePLUReportDataBeans = jdbcTemplate.query(pluSql,
						params, new CSVFilePLUReportDataBeanRowMapper());
				queryFlag = true;
				counter = 0;
			}
			if (listCsvFilePLUReportDataBeans != null
					&& counter < listCsvFilePLUReportDataBeans.size()) {
				if (counter == (listCsvFilePLUReportDataBeans.size() - 1)) {
					queryFlag = false;
					pageBegin += paginationCounter;
				}
				csvFilePLUReportDataBean = listCsvFilePLUReportDataBeans
						.get(counter++);
			} else {
				csvFilePLUReportDataBean = null;
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at PLUCSVFileItemReader.getPluData() >---> "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at PLUCSVFileItemReader.getPluData() >---> "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PLUCSVFileItemReader.getPluData() >--->  ");
			}
		}
		return csvFilePLUReportDataBean;
	}

	/**
	 * Method to get stepExecution form Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	private void retrivevalue(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into retriveValue method of PLUCSVFileItemReader");
		}
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		pluReportPrefDataBean = (PLUReportPrefDataBean) executionContext
				.get("refDataKey");

	}

	/**
	 * Method to get filter parameters for the Ad-hoc PLU report
	 * 
	 * @return Map of filter params.
	 * @throws PhotoOmniException
	 *             - Custom Exception.
	 */
	private Map<String, String> getPluFilterParams() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PLUCSVFileItemReader.getPluFilterParams() ");
		}
		JSONParser parser = new JSONParser();
		StringBuilder sb = new StringBuilder();
		boolean allPluNumberflag = false;
		Map<String, String> pluFilterMap = new HashMap<String, String>();
		try {
			String filterState = pluReportPrefDataBean.getFilterState();
			JSONObject jsonObject = (JSONObject) parser.parse(filterState);
			pluFilterMap.put(PhotoOmniBatchConstants.START_DATE,
					(String) jsonObject.get(PhotoOmniBatchConstants.START_DATE)
							+ " 00:00:00");
			pluFilterMap.put(PhotoOmniBatchConstants.END_DATE,
					(String) jsonObject.get(PhotoOmniBatchConstants.END_DATE)
							+ " 23:59:59");
			if (jsonObject.get(PhotoOmniConstants.PLUNUMBER).equals(
					PhotoOmniConstants.ALLPLUNUMBER)) {
				allPluNumberflag = true;
				pluFilterMap.put(PhotoOmniConstants.PLUNUMBER,
						String.valueOf(allPluNumberflag));
			} else {
				List<String> pluNumberList = Arrays.asList(((String) jsonObject
						.get(PhotoOmniConstants.PLUNUMBER)).split(","));
				int k = 0;
				for (String str : pluNumberList) {
					if (k == 0) {
						sb.append("'");
						sb.append(str.trim());
						sb.append("'");
					} else {
						sb.append(",");
						sb.append("'");
						sb.append(str.trim());
						sb.append("'");
					}
					k++;
				}
				pluFilterMap.put(PhotoOmniConstants.PLUNUMBER, sb.toString());
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getPluFilterParams method of PLUCSVFileItemReader ---- > "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFilterparams method of PLUCSVFileItemReader ");
			}
		}
		return pluFilterMap;
	}
}