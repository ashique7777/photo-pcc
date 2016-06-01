/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;
import com.walgreens.common.constant.PhotoOmniConstants;

/**
 * @author CTS
 *
 */
public class POFVendorPaymentCustomWriter implements ItemWriter<POFOrderVCRepBean> {

	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(POFVendorPaymentCustomWriter.class);
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private StepExecution stepExecution;
	    
    @BeforeStep
	public void saveStepExecution(StepExecution stepExecution)
	{
		this.stepExecution = stepExecution;
	}
		
	
	@Override
	public void write(List<? extends POFOrderVCRepBean> items) throws Exception {
		if(lOGGER.isInfoEnabled()){
			lOGGER.debug(" Entering write method of POFVendorPaymentCustomWriter Step Two .............");
			}	
		try{
			boolean isEmail = false;
			String updateOrderVCRepQry =PayOnFulfillmentQry.updateOrderVCRepForVendorPayment().toString();
			String insrtOrderVCAuditQry = PayOnFulfillmentQry.insertOrderVCAudit().toString(); 
			ExecutionContext executionContext = this.stepExecution.getExecutionContext();
			for(int i =0 ; i< items.size(); i++){
				POFOrderVCRepBean orderVCRepDataBean = items.get(i);
				long sysId = orderVCRepDataBean.getSysPayOnOrderVCId();
				String orderPlcDttm =  orderVCRepDataBean.getOrderPlacedDttm();
				String statusInd = orderVCRepDataBean.getStatusInd();
				double centVendPayment = orderVCRepDataBean.getCalcVendPayment();
				if(statusInd.equalsIgnoreCase(PhotoOmniConstants.POF_STATUS_IND_V)){
					jdbcTemplate.update(insrtOrderVCAuditQry, new Object[]{sysId,
																		  PhotoOmniConstants.CREATE_USER_ID,
																		  PhotoOmniConstants.UPDATE_USER_ID,
																		  orderVCRepDataBean.getOriginalOrderPlcDttm()});	
					jdbcTemplate.update(updateOrderVCRepQry, new Object[]{statusInd,centVendPayment,PhotoOmniConstants.POF_EMAIL_SENT_IND_Y,sysId,orderPlcDttm});
					isEmail = true;
				}else{
					jdbcTemplate.update(updateOrderVCRepQry, new Object[]{statusInd,centVendPayment,PhotoOmniConstants.POF_EMAIL_SENT_IND_N,sysId,orderPlcDttm});
				}
			 lOGGER.debug("Pay On Fulfillment at step two update Order VC Rep table with Statsu CD for  Product Id : "+orderVCRepDataBean.getProdId() +" with status CD :  "+orderVCRepDataBean.getStatusInd() + "  For Order Number : "+orderVCRepDataBean.getOrderNo());
			}
			executionContext.put("refPofEmailKey", isEmail);
			lOGGER.debug("............. POF write method of  Step Two .Email sending status ............" + isEmail);
		}catch(Exception e ){
			lOGGER.error("POFVendorPaymentCustomWriter Step Two  error.." + e);
		}finally{
			if(lOGGER.isInfoEnabled()){
				lOGGER.debug(" Exiting write method of POFVendorPaymentCustomWriter ");
				}
		}
	}

}
