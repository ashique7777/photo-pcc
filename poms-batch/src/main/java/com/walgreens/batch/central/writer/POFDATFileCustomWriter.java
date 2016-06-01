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

import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.batch.central.bean.POFOrderVCRepBean;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;


/**
 * @author CTS List<? extends POFOrderVCRepBean> items
 *
 */
public class POFDATFileCustomWriter implements ItemWriter<POFOrderVCRepBean> {
	
	POFDatFileDataBean pofDatFileDataBean ;
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(POFDATFileCustomWriter.class);
	
		
	BufferedWriter bufferWriter = null;
	
	public void write(List<? extends POFOrderVCRepBean> items) throws Exception {
		
		try{
			if(lOGGER.isInfoEnabled()){
				lOGGER.info(" Entering write method of POFDATFileCustomWriter .. ");
			}
			SimpleDateFormat datSdf = new SimpleDateFormat("yyyyMMdd");
			String loactionNumber ="";
			String vendorNumber ="";
			String ediUpc ="";
			Date datDate = new Date();
			String timeDoneSoldDTTM = "";
			String quantity ="";
			String filepath = pofDatFileDataBean.getFileLocation();
			
			File file = new File(filepath+pofDatFileDataBean.getFileNameList().get(0));
			
			bufferWriter = new BufferedWriter(new FileWriter((file),true));
			
			
			for(int i =0 ; i< items.size(); i++){
				POFOrderVCRepBean pofOrderVCRepBean = items.get(i);
							
				/**Padding by 0  Location Number with 5 **/
				loactionNumber = String.format("%05d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getLocationNumber())));
				
				/**Padding by 0  Vendor Number with 6 **/
				vendorNumber = String.format("%06d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getMarketVendorNumber())));
				ediUpc = pofOrderVCRepBean.getEdiUpc();
				datDate = pofOrderVCRepBean.getTimeDoneSoldDttm();
				if(null != datDate){
					timeDoneSoldDTTM = datSdf.format(datDate);
				}else timeDoneSoldDTTM = datSdf.format(new Date());
				
				
				quantity = String.format("%07d", Integer.parseInt(String.valueOf(pofOrderVCRepBean.getQuantity() * 100 )));
			
				/**Padding by 0 sold Amount with 12 **/
				String soldAmt = this.getSoldAmount(pofOrderVCRepBean.getSoldAmount());
				
				/**Padding by 0  Vendor Cost with 12 **/
				String vendorCost = this.getVendorCost(pofOrderVCRepBean.getVendPaymentAmt());
				
				
				String deptNo = pofOrderVCRepBean.getDeptNumber();
				lOGGER.info(" POFDATFileCustomWriter DAT File Content  "+loactionNumber+","+vendorNumber+","+ediUpc+","+timeDoneSoldDTTM+","+quantity+","+soldAmt+","+vendorCost+","+deptNo);
				
				bufferWriter.append(loactionNumber+","+vendorNumber+","+ediUpc+","+timeDoneSoldDTTM+","+quantity+","+soldAmt+","+vendorCost+","+deptNo);
				
			    /**  Added New Line ..**/
				bufferWriter.append(System.lineSeparator()); //'\n'
		        
		   
			}
			if (!CommonUtil.isNull(bufferWriter)) {
				bufferWriter.flush();
			}
	        
			
		}catch(Exception e ){
			lOGGER.error("Exception occure at POFDATFileCustomWriter  error.." + e);
		}finally{
		
			if(lOGGER.isInfoEnabled()){
				lOGGER.info(" Exiting write method of POFDATFileCustomWriter ");
				}
		}
	}
	
	/**
	 * Thumb Rule for sold amount is to add 4-digit after decimal point
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
	 * Thumb Rule for sold amount is to add 2-digit after decimal point
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
	 * This method Close the file after flushing dates.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void closeFile() throws PhotoOmniException {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering closeFile method of DAT File POF ");
		}
		try {
			if (!CommonUtil.isNull(bufferWriter)) {
				bufferWriter.close();
			}
		} catch (IOException e) {
			lOGGER.error(" Error occoured at closeFile method of DAT File POF " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			lOGGER.info(" Exiting closeFile method of DAT File POF ");
		}
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
			this.pofDatFileDataBean =  (POFDatFileDataBean) executionContext.get("refPofDATFileKey");
		} catch (Exception e) {
			lOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting retriveValue method of PSReportEmailItemWriter ");
			}
		}
	}
	
}



