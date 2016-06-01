package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;

public class POFDatFileItemReader implements ItemReader<POFDatFileDataBean>{

	private static final Logger logger = LoggerFactory.getLogger(POFDatFileItemReader.class);
	
	static int count = 0;
	
	POFDatFileDataBean pofDatFileDataBean = new POFDatFileDataBean();
	
	public POFDatFileDataBean read() throws PhotoOmniException{
		
				
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering read method of POFDatFileItemReader ");
		}
		try{
			if(count == 0){
				count++;
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting read method of PSReportEmailCustomReader ");
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(" Exiting read method of PSReportEmailCustomReader ");
				}
				return null;
			}
			
		} catch (Exception e) {
			logger.error(" Error occoured at read method of POFDatFileItemReader - " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting read method of POFDatFileItemReader ");
			}
		}
		return pofDatFileDataBean;
		
	}
	
}
