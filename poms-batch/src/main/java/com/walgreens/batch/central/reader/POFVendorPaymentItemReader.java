/**
 * 
 */
package com.walgreens.batch.central.reader;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.BatchBO;
import com.walgreens.batch.central.factory.BatchBOFactory;
import com.walgreens.batch.central.rowmapper.POFOrderVCRepRowmapper;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;

/**
 * @author CTS
 * 
 */
public class POFVendorPaymentItemReader implements
		ItemReader<POFOrderVCRepBean> {

	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory
			.getLogger(POFVendorPaymentItemReader.class);

	// FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED
	private boolean queryFlag = false;

	// COUNTER USED TO READ & CALCULATE AN ORDER BEAN & SEND TO PROCESSOR
	private int counter = 0;

	private StepExecution stepExecution;
	
	//private int pofSysDateVal;

	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution) {
		this.stepExecution = stepExecution;
	}

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	private List<POFOrderVCRepBean> pofDataList = new ArrayList<POFOrderVCRepBean>();

	@Override
	public POFOrderVCRepBean read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		lOGGER.debug("Entering POF Reader step two ....");
		POFOrderVCRepBean orderVcRepBean = null;
		BatchBOFactory boFactory = new BatchBOFactory();
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			int pofSysDateVal = (int) executionContext.get("pofSysDateVal");
			String sql = PayOnFulfillmentQry.selectOderVCRepDetail().toString();
			lOGGER.debug("POF Reader step two selectOderVCRepDetail  query ...."
					+ sql);
			if (!queryFlag) {

				pofDataList = jdbcTemplate.query(sql,new Object[] {pofSysDateVal},
						new POFOrderVCRepRowmapper());
				lOGGER.debug("POF Reader step two pofDataList size  ...."
						+ pofDataList.size());
				if (pofDataList.size() == 0) {
					
					executionContext.put("refPofEmailKey", false);
					//executionContext.put("refPofEmailKey", true);
				}
				queryFlag = true;
				counter = 0;
			}
			if (pofDataList != null && pofDataList.size() >= 1
					&& counter < pofDataList.size()) {
				if (counter == pofDataList.size()) {
					pofDataList = null;

				}
				if(null != pofDataList){
					orderVcRepBean = pofDataList.get(counter++);
					BatchBO pofBO = boFactory.getBO(PhotoOmniBatchConstants.POF);
					pofBO.validateVendorPayment(orderVcRepBean);
				}
			} else
				orderVcRepBean = null;

		} catch (Exception e) {
			lOGGER.error("Error at Read method in POF Reader step two ...." + e);
		}
		return orderVcRepBean;
	}
	
	/**
	 * Retrieve value get the 'locationNumber' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 *//*
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering retriveValue method of PM Writer Location Number step Two  ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			pofSysDateVal = (int) executionContext.get("pofSysDateVal");
		} catch (Exception e) {
			lOGGER.error(" Error occoured at process write of PM Writer Location Number step Two  - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting retriveValue method of PM Writer Location Number step Two  ");
			}
		}
	}*/

}
