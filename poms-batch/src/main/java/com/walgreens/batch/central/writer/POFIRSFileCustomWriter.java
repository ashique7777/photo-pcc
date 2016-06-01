/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;





/**
 * @author CTS
 *
 */
public class POFIRSFileCustomWriter implements ItemWriter<POFOrderVCRepBean> {
	
	POFIrsFileDataBean pofIrsFileDataBean;
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(POFIRSFileCustomWriter.class);
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	BufferedWriter bufferWriter = null;

	public void write(List<? extends POFOrderVCRepBean> items) throws Exception {
		if(lOGGER.isInfoEnabled()){
			lOGGER.info(" Entering write method of POFIRSFileCustomWriter ..s ");
			}	
		try{
			String loactionNumber ="";
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			
			Date date1 = null;
			
			String quantity="";
			String envelopQty= "0000000100";
			String filepath = pofIrsFileDataBean.getFileLocation();
			String timeDoneSoldDTTM ="";
			File file = new File(filepath+pofIrsFileDataBean.getFileNameList().get(0));
			bufferWriter = new BufferedWriter(new FileWriter((file),true));
			lOGGER.info(" Entering write method of POFIRSFileCustomWriter ..s ");
		
			
			for(int i =0 ; i< items.size(); i++){
				POFOrderVCRepBean pofOrderVCRepBean = items.get(i);
				long sysPayOnOrderVCId = pofOrderVCRepBean.getSysPayOnOrderVCId();
				
				/**Padding by 0  Location Number with 5 **/
				loactionNumber = String.format("%05d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getLocationNumber())));
				String locationType =pofOrderVCRepBean.getLocationType();
				
				/**Padding by 0  Order Number with 10 **/
				//String orderNumber = CommonUtil.paddingWithTen(pofOrderVCRepBean.getOrderNumber()); 
				String orderNumber = String.format("%010d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getOrderId())));
				
				/**Padding by 0  Product Number with 10 **/
			//	String prodNumber = CommonUtil.paddingWithTen(pofOrderVCRepBean.getProdNumber());  
				String prodNumber = String.format("%010d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getProdId())));
				
				/**Padding by 0  quantity with 10 **/
			//	quantity = CommonUtil.paddingWithTen(Integer.toString(pofOrderVCRepBean.getQuantity() * 100));
				quantity = String.format("%010d", Integer.valueOf(pofOrderVCRepBean.getQuantity() * 100));
				
				/**Padding by 0  Vendor Number with 10 **/
				//String vendorNumber = CommonUtil.paddingWithTen(pofOrderVCRepBean.getVendorNumber());
				String vendorNumber = String.format("%010d", Integer.parseInt(pofOrderVCRepBean.getVendorNumber()));
				
				if(null != pofOrderVCRepBean.getTimeDoneSoldDttm()){
					timeDoneSoldDTTM = sdf1.format(pofOrderVCRepBean.getTimeDoneSoldDttm());
				}else{
					timeDoneSoldDTTM = sdf1.format(new Date());
				}
				 
							
				/**Padding by 0  Vendor Cost with 12 **/
				String vendorCost = this.getVendorCost(pofOrderVCRepBean.getVendPaymentAmt());
							
				/**Padding by 0 sold Amount with 12 **/
				String soldAmt = this.getSoldAmount(pofOrderVCRepBean.getSoldAmount());
				//String soldAmt = String.format("%012d", Integer.parseInt(soldAmount.replace(".", "")));
				
				/**Get EDI UPC **/
				String ediUpc = pofOrderVCRepBean.getEdiUpc();
				String productDiscription = pofOrderVCRepBean.getProductDiscription();
				String productWIC = pofOrderVCRepBean.getProductWIC();
				String productUPC = pofOrderVCRepBean.getProductUPC();
				lOGGER.info(" POFIRSFileCustomWriter IRS File Content  "+loactionNumber+","+locationType+","+orderNumber+","+prodNumber+","+quantity+","+envelopQty+","+vendorNumber+","+timeDoneSoldDTTM+","+vendorCost);
				bufferWriter.append(loactionNumber+","+locationType+","+orderNumber+","+prodNumber+","+quantity+","+envelopQty+","+vendorNumber+","+timeDoneSoldDTTM+","+vendorCost
						+","+soldAmt+","+ediUpc+","+productDiscription+","+productWIC+","+productUPC);
			
				bufferWriter.append(System.lineSeparator());
			
				
			}
			
			if (!CommonUtil.isNull(bufferWriter)) {
				lOGGER.info(" Write method of POFIRSFileCustomWriter ..bufferWriter ");
				bufferWriter.flush();
			}
	        
			
		}catch(Exception e ){
			lOGGER.error("Exception occure at POFIRSFileCustomWriter  error.." + e);
		}finally{
			if(lOGGER.isInfoEnabled()){
				lOGGER.info(" Exiting write method of POFIRSFileCustomWriter ");
				}
		}
		
	}
	
	/**
	 * This method returns Item Vendor Cost.Thumb Rule for sold amount is to add 4-digit after decimal point
	 * 
	 * @param vendorCst
	 * @return
	 */
	private String getVendorCost(double vendorCst){
		String vendorCost2 = Double.toString(vendorCst);
		String vendorCostTemp="";
		if(vendorCost2.contains(".")){
			if(vendorCost2.substring(vendorCost2.indexOf(".")+1,vendorCost2.length()).length() < 4){
				String x = vendorCost2.substring(0,vendorCost2.indexOf(".")) ;
				String y = vendorCost2.substring(vendorCost2.indexOf(".")+1,vendorCost2.length());
				vendorCostTemp = x+String.format("%1$-" + 4 + "s", y).replace(' ', '0');
				
			}
			if(vendorCost2.substring(vendorCost2.indexOf(".")+1,vendorCost2.length()).length() > 4){
				String x = vendorCost2.substring(0,vendorCost2.indexOf(".")) ;
				String y = vendorCost2.substring(vendorCost2.indexOf(".")+1,vendorCost2.indexOf(".")+5);
				vendorCostTemp = x+y ;
			}
			
		}else{
			vendorCostTemp = vendorCost2 +"0000";
		}
		String vendorCost = String.format("%012d", Integer.parseInt(vendorCostTemp));
		return vendorCost;
		
	}
	

	/**
	 *This method return sold amount . Thumb Rule for sold amount is to add 2-digit after decimal point
	 * 
	 * @param soldAmnt
	 * @return
	 */
	
	private String getSoldAmount(double soldAmnt){
		String soldAmount= Double.toString(soldAmnt);
		
		if(soldAmount.contains(".")){
			if(soldAmount.substring(soldAmount.indexOf(".")+1,soldAmount.length()).length() == 1){
				soldAmount = soldAmount + "0";
			}
			if(soldAmount.substring(soldAmount.indexOf(".")+1,soldAmount.length()).length() > 2){
				String x = soldAmount.substring(0,soldAmount.indexOf(".")) ;
				String y = soldAmount.substring(soldAmount.indexOf(".")+1,soldAmount.indexOf(".")+3);
				soldAmount = x+y ;
			}
		}
		String soldAmt = String.format("%012d", Integer.parseInt(soldAmount.replace(".", "")));
		return soldAmt;
	
	}
	
	/**
	 * Retrieve value get the 'refPofDATFileKey' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering retriveValue method of PSReportEmailItemWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			this.pofIrsFileDataBean =  (POFIrsFileDataBean) executionContext.get("refPofIRSFileKey");
		} catch (Exception e) {
			lOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting retriveValue method of PSReportEmailItemWriter ");
			}
		}
	}
	
	
	
	
	
	/**
	 * This method Close the file after flushing dates.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void closeFile() throws PhotoOmniException {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering closeFile method of IRS File Close POF ");
		}
		try {
			if (!CommonUtil.isNull(bufferWriter)) {
				bufferWriter.close();
			}
		} catch (IOException e) {
			lOGGER.error(" Error occoured at closeFile method of IRS File Close POF " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			lOGGER.info(" Exiting closeFile method of IRS File Close POF ");
		}
	}
}
