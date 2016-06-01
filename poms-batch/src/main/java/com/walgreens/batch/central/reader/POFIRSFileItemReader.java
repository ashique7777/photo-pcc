package com.walgreens.batch.central.reader;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;

public class POFIRSFileItemReader implements ItemReader<POFIrsFileDataBean>{

	private static final Logger logger = LoggerFactory.getLogger(POFIRSFileItemReader.class);
	
	static int count = 0;
	
	POFIrsFileDataBean pofIrsFileDataBean = new POFIrsFileDataBean();
	
	public POFIrsFileDataBean read() throws PhotoOmniException{
		
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering read method of POFIRSFileItemReader ");
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
			logger.error(" Error occoured at read method of POFIRSFileItemReader - " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting read method of POFIRSFileItemReader ");
			}
		}
		return pofIrsFileDataBean;
		
	}
	
}
