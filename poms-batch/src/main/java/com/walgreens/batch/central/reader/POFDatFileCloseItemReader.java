package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * This class is a reader class for closing DAT file
 * @author CTS
 * @version
 */
public class POFDatFileCloseItemReader implements ItemReader<POFDatFileDataBean>{
	
	/**
     * pofDatFileDataBean
     */
	POFDatFileDataBean pofDatFileDataBean = new POFDatFileDataBean();
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFDatFileCloseItemReader.class);
	static int count = 0;
	
	/**
	 * This method returns the 'pofDatFileDataBean' value to the writer. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public POFDatFileDataBean read() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of POFDatFileCloseReader ");
		}
			try {
				if(count == 0){
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFDatFileCloseReader ");
					}
					
				}else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFDatFileCloseReader ");
					}
					pofDatFileDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of POFDatFileCloseReader - " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting retriveValue method of POFDatFileCloseReader ");
				}
			}
			return pofDatFileDataBean;
		} 
		
     
	
}
