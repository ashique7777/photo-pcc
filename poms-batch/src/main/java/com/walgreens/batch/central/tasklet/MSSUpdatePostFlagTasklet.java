package com.walgreens.batch.central.tasklet;

import org.springframework.batch.core.StepContribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

public class MSSUpdatePostFlagTasklet implements Tasklet {
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(MSSUpdatePostFlagTasklet.class);
	
	/**
	 * query
	 */
	private String query ;
	
	/**
	 * omniJdbcTemplate
	 */
	private JdbcTemplate omniJdbcTemplate ;
	
	/**
	 * beginDate
	 */
	private String beginDate;
	
	/**
	 * endDate
	 */
	private String endDate;
	
	/**
	 * jobSubmitDate
	 */
	private String jobSubmitDate;
	
	/**
	 * fileSysTime
	 */
	private String fileSysTime;
	
	/**
	 * @return the jobSubmitDate
	 */
	public String getJobSubmitDate() {
		return jobSubmitDate;
	}
	/**
	 * @param jobSubmitDate the jobSubmitDate to set
	 */
	public void setJobSubmitDate(String jobSubmitDate) {
		this.jobSubmitDate = jobSubmitDate;
	}
	/**
	 * @return the fileSysTime
	 */
	public String getFileSysTime() {
		return fileSysTime;
	}
	/**
	 * @param fileSysTime the fileSysTime to set
	 */
	public void setFileSysTime(String fileSysTime) {
		this.fileSysTime = fileSysTime;
	}
	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}
	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the omniJdbcTemplate
	 */
	public JdbcTemplate getOmniJdbcTemplate() {
		return omniJdbcTemplate;
	}
	/**
	 * @param omniJdbcTemplate the omniJdbcTemplate to set
	 */
	public void setOmniJdbcTemplate(JdbcTemplate omniJdbcTemplate) {
		this.omniJdbcTemplate = omniJdbcTemplate;
	}
	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	private String fileSysDate;
	/**
	 * @return the fileSysDate
	 */
	public String getFileSysDate() {
		return fileSysDate;
	}

	/**
	 * @param fileSysDate the fileSysDate to set
	 */
	public void setFileSysDate(String fileSysDate) {
		this.fileSysDate = fileSysDate;
	}

	/**
     * This method will update the MSS_TRASFER_CD to "I".
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of MSSUpdatePreFlagTasklet ");
		}
		try {
			
			String mssTransmissionDate = jobSubmitDate + " " + fileSysTime;

			/**
			 * numberofRowsUpdated will get how many rows have been updated.
			 */
			if (!CommonUtil.isNull(beginDate)) {
				int rowsUpdated = omniJdbcTemplate.update(query, new Object[] { mssTransmissionDate,endDate, beginDate });
				LOGGER.debug("Number of rows updated with MSS_TRANSFER_CD = 'Y' :- " + rowsUpdated);
			} else {
				int rowsUpdated = omniJdbcTemplate.update(query, new Object[] { mssTransmissionDate,endDate });
				LOGGER.debug("Number of rows updated with MSS_TRANSFER_CD = 'Y' :- " + rowsUpdated);
			}

		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of MSSUpdatePostFlagTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at execute method of MSSUpdatePostFlagTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of MSSUpdatePostFlagTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}
	
}
