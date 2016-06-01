/**
 * 
 */
package com.walgreens.batch.central.reader;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.walgreens.batch.central.bean.PayOnFulfillmentLWU;
import com.walgreens.batch.central.rowmapper.PayOnFulfillmentDataRowmapper;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;

/**
 * @author CTS
 *
 */
public class PayOnFulfillmentItemReader implements ItemReader<PayOnFulfillmentLWU>{

	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory
			.getLogger(PayOnFulfillmentItemReader.class);
	
	// FLAG USED TO RESTRICT QUERY EXECUTION EVERY TIME READ() IS CALLED
	private boolean queryFlag = false;
	
	

	// COUNTER USED TO READ & CALCULATE AN ORDER BEAN & SEND TO PROCESSOR
	private int counter = 0;
	
	private StepExecution stepExecution;
    
	
	private int pofSysDateVal;
   
	@BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		this.stepExecution = stepExecution;
	}

		
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	private List<PayOnFulfillmentLWU> pofDataList = new ArrayList<PayOnFulfillmentLWU>();
	
	

	

	/**
	 * @return the pofSysDateVal
	 */
	public int getPofSysDateVal() {
		return pofSysDateVal;
	}

	/**
	 * @param pofSysDateVal the pofSysDateVal to set
	 */
	public void setPofSysDateVal(int pofSysDateVal) {
		this.pofSysDateVal = pofSysDateVal;
	}

	@Override
	public PayOnFulfillmentLWU read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		
		lOGGER.debug("Entering POF Reader Step one ....");
		PayOnFulfillmentLWU  pofLWUBean = null;
		
		try{
			ExecutionContext executionContext = this.stepExecution
					.getExecutionContext();
			executionContext.put("pofSysDateVal", pofSysDateVal);
			String sql =PayOnFulfillmentQry.selectPOFOrder().toString();
			lOGGER.debug("POF  step one POF Order Qry ...."+ sql);
			if (!queryFlag) {
				
				pofDataList = jdbcTemplate.query(sql, new Object[] {pofSysDateVal},new PayOnFulfillmentDataRowmapper());
				lOGGER.debug("Entering POF Reader step one pofDataList Size ...."+ pofDataList.size());
				if(null != pofDataList && pofDataList.size() >0){
				   vendorValidation(pofDataList);	
				}
				queryFlag = true;
				counter = 0;
			}
			if(pofDataList != null  && pofDataList.size() >=1 && counter < pofDataList.size()){
				if (counter == pofDataList.size()) {
					pofDataList = null;
				}
				if(null != pofDataList)
				  pofLWUBean =pofDataList.get(counter++);
								
			}else pofLWUBean = null;
			
		}catch(Exception e){
			lOGGER.error("Error at Read method in POF Reader step one ...."+e);
		}
		return pofLWUBean;
	}
	
	/**
	 * This method is used for validating Vendor : Vendor Type should be FULFILLMENT and Fulfillment Vendor Cost should be greater than Zero
	 * @param pofDataList
	 */
	private void vendorValidation(List<PayOnFulfillmentLWU> pofDataList){
		try{
			for (PayOnFulfillmentLWU  pofLWUBean : pofDataList) {
				if(null != pofLWUBean.getVendorType() && pofLWUBean.getVendorType().trim().equalsIgnoreCase("FULFILLMENT") && pofLWUBean.getVendPaymentAmt() >0){
					pofLWUBean.setVendorPaymentEligible(true);
					pofLWUBean.setEdiUPC(getEDIUPC(Integer.toString(pofLWUBean.getStoreNumber()),pofLWUBean.getEnvelopeNo())); 
				}else{
					pofLWUBean.setVendorPaymentEligible(false);
				
				}
			}
		}catch(Exception e){
			lOGGER.error("Error at find order line Detail method in POF calculation Step One...."+e);
		}
	}
	
	/**
	 * This method is used for formatting EDI UPC value
	 * @param storeNo
	 * @param envNo
	 * @return
	 */
	private String getEDIUPC(String storeNo, String envNo){
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date()); 
		String doy = String.format("%03d",cal.get(Calendar.DAY_OF_YEAR));
		storeNo = String.format("%05d",Integer.parseInt(storeNo));
		envNo = String.format("%06d",Integer.parseInt(envNo));
		String ediStr = doy + storeNo + envNo;
		lOGGER.debug("In POF step one getEDIUPC value  ...."+ ediStr);
		return ediStr;
	}

}
