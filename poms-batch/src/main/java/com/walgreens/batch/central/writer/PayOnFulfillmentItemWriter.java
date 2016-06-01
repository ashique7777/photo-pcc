/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.bean.PayOnFulfillmentLWU;
import com.walgreens.batch.central.utility.PayOnFulfillmentQry;
import com.walgreens.common.constant.PhotoOmniConstants;



/**
 * @author CTS
 *
 */
public class PayOnFulfillmentItemWriter implements ItemWriter<PayOnFulfillmentLWU>  {
	
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(PayOnFulfillmentItemWriter.class);
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends PayOnFulfillmentLWU> items)
			throws Exception {
			if(lOGGER.isInfoEnabled()){
				lOGGER.debug(" Entering write method of PayonFulfillment Step One ");
				}
		   try{
			   Date reportingDttm = null;
			   String insrtQry = PayOnFulfillmentQry.insertOrderVCRep().toString();
			   String updateOrderQry =PayOnFulfillmentQry.updateOrderAttribute().toString(); 
				
			for(int i =0 ; i< items.size(); i++){
				PayOnFulfillmentLWU orderDataBean = items.get(i);
				long orderId = orderDataBean.getOrderId();
				String orderPlcDttm =  orderDataBean.getOrderPlacedDttm();
						/**Insert Order VC REP table**/
						if(orderDataBean.isVendorPaymentEligible()){
							/**Calculate Reporting Date Time**/
							reportingDttm = calculateReportingDttm(orderDataBean);
							jdbcTemplate.update(insrtQry, new Object[]{orderDataBean.getLocationId(),
									orderDataBean.getOrderNumber(),
									Integer.parseInt(orderDataBean.getEnvelopeNo()),
										orderDataBean.getProductId(),
									orderDataBean.getEdiUPC(), 
									
									orderDataBean.getQuantity(),
									orderDataBean.getItemSoldAmount(),
									orderDataBean.getVendorId(),
									orderDataBean.getAsnRecieveDttm(),
									orderDataBean.getCompletedDttm(),
									orderDataBean.getSoldDttm(),
									reportingDttm,// reporting dttm
									orderDataBean.getVendPaymentAmt(), // vendor payment amount
									orderDataBean.getCalculatedPrice(),
									0, // CALCULATED_VENDOR_PAYMENT
								    0, // PREV_VENDOR_PAYMENT_AMOUNT
									PhotoOmniConstants.POF_STATUS_IND_N,// status_ind
									PhotoOmniConstants.POF_EMAIL_SENT_IND_N,  //EMAIL_SENT_IND
									PhotoOmniConstants.CREATE_USER_ID,
									PhotoOmniConstants.UPDATE_USER_ID,
									orderDataBean.getOriginalOrderPlacedDttm()});  
							lOGGER.debug("Pay On Fulfillment at step one update Order VC Rep table with Statsu CD N for Order .." + orderId + " Product Id : "+orderDataBean.getProductId());
						}
				/**Update Order Attribute Table**/
				jdbcTemplate.update(updateOrderQry, new Object[]{orderId,orderPlcDttm});
				lOGGER.debug("Pay On Fulfillment at step one update Order Attribute table with Fulfillment Ind with 0 for Order .." + orderId);
			}
		}catch(Exception e ){
			lOGGER.error("Pay On Fulfillment Error occure at step one .." + e.getMessage());
		}finally{
			if(lOGGER.isInfoEnabled()){
				lOGGER.info(" Exiting writer method of PayonFulFillment Step One");
				}
		}
		
	}



	/**
	 * Calculate Reporting Date Time
	 * @param orderDataBean
	 * @return
	 */
	private Date  calculateReportingDttm(PayOnFulfillmentLWU orderDataBean){
		Date reportingDttm = null;
		if (null != orderDataBean.getSoldDttm()) {
			reportingDttm = orderDataBean.getSoldDttm();
		} else {
			if (null != orderDataBean.getCompletedDttm()) {
				reportingDttm = orderDataBean.getCompletedDttm();
			} else {
				if (null != orderDataBean.getAsnRecieveDttm()) {
					reportingDttm = orderDataBean
							.getAsnRecieveDttm();
				} else {
					reportingDttm = new Date();
				}
			}
		}
		return reportingDttm;
	}

}
