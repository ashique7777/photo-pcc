package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * This class is a reader class for closing IRS file
 * @author CTS
 * @version
 */

public class POFIRSFileCloseItemReader implements ItemReader<POFIrsFileDataBean> {
	
	/**
     * pofIrsFileDataBean
     */
	POFIrsFileDataBean pofIrsFileDataBean = new POFIrsFileDataBean();
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFIRSFileCloseItemReader.class);
	static int count = 0;
	
	/**
	 * This method returns the 'pofIrsFileDataBean' value to the writer. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public POFIrsFileDataBean read() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of POFIRSFileCloseItemReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFIRSFileCloseItemReader ");
					}
					
				}else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFIRSFileCloseItemReader ");
					}
					pofIrsFileDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of POFIRSFileCloseItemReader - " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting retriveValue method of POFIRSFileCloseItemReader ");
				}
			}
			return pofIrsFileDataBean;
		} 
		
     
	
	
}
