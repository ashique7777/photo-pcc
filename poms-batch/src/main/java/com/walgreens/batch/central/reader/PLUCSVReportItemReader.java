package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.rowmapper.PLUReportPrefDataBeanRowMapper;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUCSVReportItemReader implements
		ItemReader<PLUReportPrefDataBean> {

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUCSVReportItemReader.class);

	int count = 0;
	PLUReportPrefDataBean pluReportPrefDataBean;
	private PLUReportPrefDataBeanRowMapper reportPrefDataBeanRowMapper;
	private String strjobExecutionstepName;
	private long reportId;
	@SuppressWarnings("unused")
	private long jobSubmitTime;

	/**
	 * Method to get reportId
	 * 
	 * @return reportId
	 */
	public Long getReportId() {
		return reportId;
	}

	/**
	 * Method to set reportId
	 * 
	 * @return reportId
	 */
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	/**
	 * Method to set jobSubmitTime
	 * 
	 * @return jobSubmitTime
	 */
	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	/**
	 * Method to get report Details based on Report ID
	 * 
	 * @return PLUReportPrefDataBean
	 * @throws PhotoOmniException
	 */
	@Override
	public PLUReportPrefDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of PLUCSVReportItemReader ");
		}
		reportPrefDataBeanRowMapper = new PLUReportPrefDataBeanRowMapper();
		String query = "";
		if (count == 0) {
			try {
				count++;
				if ("DailyPLUJobStep1"
						.equalsIgnoreCase(strjobExecutionstepName)) {
					query = ReportsQuery.getDefaultUserPrefQuery(reportId)
							.toString();
				} else {
					query = ReportsQuery.getUserPrefQuery(reportId).toString();
				}
				pluReportPrefDataBean = jdbcTemplate.query(query,
						reportPrefDataBeanRowMapper).get(0);
				pluReportPrefDataBean.setSysUserReportPrefId(reportId);
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of PLUCSVReportItemReader ----> "
						+ e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of PLUCSVReportItemReader ");
				}
			}
			return pluReportPrefDataBean;
		} else {
			return null;
		}
	}

	/**
	 * Method to get stepExecution from Execution context
	 * 
	 * @param stepExecution
	 */
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering beforeStep method of PLUCSVReportItemReader");
		}
		strjobExecutionstepName = stepExecution.getStepName();
	}
}