package com.walgreens.batch.central.reader;

import java.util.List;

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

import com.walgreens.batch.central.bean.POFEmailDataBean;
import com.walgreens.batch.central.rowmapper.POFEmailRowmapper;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;
import com.walgreens.common.exception.PhotoOmniException;

public class POFEmailCustomReader implements ItemReader<List<POFEmailDataBean>>{
	
	 
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFEmailCustomReader.class);
	static int count = 0;
	private boolean isMail;
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	/**
	 * This method return's the 'pofOrderVCRepBean' to the processor.  
	 * @return pofOrderVCRepBean.
	 * @throws PhotoOmniException - custom Exception
	 */
	public List<POFEmailDataBean> read() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of POFEmailCustomReader ");
		}
		
		List<POFEmailDataBean> emailList = null;
			try {
				if(count == 0 && this.isMail){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFEmailCustomReader ");
					}
					String sql = PayOnFulfillmentQry.selectEmail().toString();
					emailList = this.getJdbcTemplate().query(sql, new POFEmailRowmapper());
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFEmailCustomReader ");
					}
					emailList = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of POFEmailCustomReader - " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting retriveValue method of POFEmailCustomReader ");
				}
			}
			return emailList;
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	/**
	 * Retrieve value get the 'refPofEmailKey' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of PSReportEmailItemWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			this.isMail = ((Boolean) executionContext.get("refPofEmailKey")).booleanValue();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of PSReportEmailItemWriter ");
			}
		}
	}

}
