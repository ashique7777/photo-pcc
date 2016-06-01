/**
 * DbPartitionPurgeBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 18 December 2015
 *  
 **/
package com.walgreens.batch.central.bo;

import java.util.Calendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * {@link DbPartitionPurgeBOImpl} is a business implementation class for {@link DbPartitionPurgeBO}
 * This class is used to process DbPartitionPurge related information.
 * This class using for the business logic.
 */
@Component("DbPartitionPurgeBO")
public class DbPartitionPurgeBOImpl implements DbPartitionPurgeBO{
	
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(DbPartitionPurgeBOImpl.class);
	
	@Override
	public String[] dbPartitionPurgeTableName(Properties prop) throws Exception{
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering dbPartitionPurgeTableName method of DbPartitionPurgeBOImpl");
		}
		Calendar cal = Calendar.getInstance();
		String[] tableNames = null;
		String tableName = null;
		String message;
		try {
			/*if(cal.get(Calendar.DAY_OF_MONTH) != 1) {
				
				LOGGER.error("Invalid day of month that this job is executed. Please run the job in first of the month");
				message = "Day of month that job is running is invalid, must be first of month";
				throw new PhotoOmniException(message);

			} else {*/
				
				if (CommonUtil.isNull(prop.get("tableName"))
						|| "".equals(prop.get("tableName"))) {
					
					LOGGER.error("No tables retrieved from property file. Please correct the property file.");
					message = "Invalid table values passed in from property file";
					throw new PhotoOmniException(message);

				} else {
					
					tableName = prop.get("tableName").toString();
					
					LOGGER.debug(" The table Names : " + tableName);
					
					tableNames = tableName.split(",");
					
					checkTableNamesIsAllCaps(tableNames);
					
				}
				
			//}
		} catch(NullPointerException ex){
			
			LOGGER.error("Error occoured at execute method of DbPartitionPurgeBOImpl. The error message :: " + ex);
			
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of DbPartitionPurgeBOImpl ");
			}
		}
		
		return tableNames;
	}
	
    /**
     * Utility method to validate if a tableNames is in all caps.
     * @param tableNames String[] value to perform validation on.
     * @return boolean Return true if string is in all caps.  Return false if string is not in all caps.
     * @throws PhotoOmniException 
     * 
     */
	private void checkTableNamesIsAllCaps(String[] tableNames) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering checkTableNamesIsAllCaps method of DbPartitionPurgeBOImpl");
		}
		String message ;
		if(tableNames.length>0){
			for(String tableName : tableNames){
				for (char c : tableName.toCharArray()) {
					if (Character.isLowerCase(c)) {
						message = "Invalid tableName input value";
						throw new PhotoOmniException(message);
					}
				}
			}
		} else {
			message = "TableName is not present in the Property File ";
			throw new PhotoOmniException(message);
		}
	}

}
