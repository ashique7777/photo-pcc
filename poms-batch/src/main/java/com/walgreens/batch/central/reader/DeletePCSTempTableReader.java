package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;



public class DeletePCSTempTableReader implements ItemReader<String>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeletePCSTempTableReader.class); 
	int count = 0;
	private String strRecvDTTM;

	public void setStrRecvDTTM(String strRecvDTTM) {
		this.strRecvDTTM = strRecvDTTM;
	}

	public String read() throws Exception, UnexpectedInputException,
	ParseException, NonTransientResourceException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into DeletePCSTempTableReader.read()");
		}
		if(count == 0){
			count++;
			return strRecvDTTM;
		}else{
			return null;
		}
	}
}
