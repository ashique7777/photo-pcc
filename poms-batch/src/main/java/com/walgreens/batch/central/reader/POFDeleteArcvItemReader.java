/**
 * 
 */
package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.walgreens.common.exception.PhotoOmniException;


/**
 * @author CTS
 *
 */
public class POFDeleteArcvItemReader implements ItemReader<String>{
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFSftpItemReader.class);
	
	static int count = 0;

	@Override
	public String read() throws Exception, UnexpectedInputException,
			ParseException, NonTransientResourceException {
		try{
			if(count == 0){
				count++;
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of POFDeleteArcvItemReader ");
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of POFDeleteArcvItemReader ");
				}
				return null;
			}
			
		}catch (Exception e) {
			LOGGER.error(" Error occoured at read method of POFIRSFileItemReader - " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting read method of POFIRSFileItemReader ");
			}
		}
		return "";
	}

}
