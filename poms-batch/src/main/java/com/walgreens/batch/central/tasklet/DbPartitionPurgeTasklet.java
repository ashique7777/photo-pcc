/**
 * DbPartitionPurgeTasklet.java 
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
package com.walgreens.batch.central.tasklet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bo.DbPartitionPurgeBO;
import com.walgreens.batch.central.factory.DbPartitionPurgeBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * 
 * @author CTS
 * @version 1.1 December 18, 2015. This class is tasklet class for DbPartitionPurge.
 * 
 */

@Configuration
public class DbPartitionPurgeTasklet implements Tasklet {
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(DbPartitionPurgeTasklet.class);
	
	/**
	 * Properties for getting the properties params.
	 */
	private static Properties prop;
	
	private static String  partitionName = "";
	
	/**
	 * To load the data in properties file.
	 */
	static {
		InputStream is = null;
		try {
			prop = new Properties();
			is = ClassLoader.class.getResourceAsStream("/PhotoOmniBatch.properties");
			prop.load(is);
		} catch (FileNotFoundException e) {
			LOGGER.debug("PhotoOmniBatch.properties file not found "+ e);
		} catch (IOException e) {
			LOGGER.debug(" Exception occured in PhotoOmniBatch.properties "+ e);
		}
	}
	
	/** Call to load the BO Factory **/
	@Autowired
	private DbPartitionPurgeBOFactory dbPartitionPurgeBOFactory;
	
	/** jdbcTemplate source loaded from the job_DbPartitionPurge.xml **/
	private JdbcTemplate omniJdbcTemplate;
	
	/** dbPartitionQuery loaded from the job_DbPartitionPurge.xml **/
	private String dbPartitionQuery;

	/**
	 * @return the omniJdbcTemplate
	 */
	public JdbcTemplate getOmniJdbcTemplate() {
		return omniJdbcTemplate;
	}

	/**
	 * @param omniJdbcTemplate the omniJdbcTemplate to set
	 */
	public void setOmniJdbcTemplate(JdbcTemplate omniJdbcTemplate) {
		this.omniJdbcTemplate = omniJdbcTemplate;
	}

	/**
	 * @return the dbPartitionQuery
	 */
	public String getDbPartitionQuery() {
		return dbPartitionQuery;
	}

	/**
	 * @param dbPartitionQuery the dbPartitionQuery to set
	 */
	public void setDbPartitionQuery(String dbPartitionQuery) {
		this.dbPartitionQuery = dbPartitionQuery;
	}
	

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of DbPartitionPurgeTasklet ");
		}
		DbPartitionPurgeBO dbPartitionPurgeBO = null;
		List<Map<String, Object>> results ;
		List<String> highValuesLst;
		String[] tableNames;
		
		try {
				
			dbPartitionPurgeBO = dbPartitionPurgeBOFactory.getDbPartitionPurgeBO();
 			tableNames = dbPartitionPurgeBO.dbPartitionPurgeTableName(prop);
			int count = 0;
			for(String tableName : tableNames){
				
				final  Object[] params = new Object[]{
						tableName,
						"PHOTOOMNI_OWNER"
				};
				LOGGER.debug(" The Query to Fetch DB Partition Value :: " + dbPartitionQuery);
				results = omniJdbcTemplate.queryForList(dbPartitionQuery,params);
				
				System.out.println(" The Count of rows Fetch DB Partition Value :: " + results.size() +" For the table : "+tableName);
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(" The Count of rows Fetch DB Partition Value :: " + results.size() +" For the table : "+tableName);
				}
				if(results.size()!= PhotoOmniConstants.ZERO){
					highValuesLst = getHighValueList(results);
					getPartitionName(results, tableName,highValuesLst,count);

				} else {
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug(" ZERO rows fetched while executing the dbPartitionPurge Query For the table : "+tableName);
					}
				}
				count++;
				
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			LOGGER.error(" Error occoured at execute method of DbPartitionPurgeTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of DbPartitionPurgeTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at execute method of DbPartitionPurgeTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of DbPartitionPurgeTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}
	
	
	/**
	 * Description : This method is used for get partition Name for the given table
	 * 
	 * @param results
	 * @param tableName
	 * @throws PhotoOmniException
	 * @throws SQLException
	 */
	private void getPartitionName(List<Map<String, Object>> results, String tableName,List<String> highValuesLst, int count) throws PhotoOmniException, SQLException{
		
		String dropPartitionDDL;
		String exitCodeDDL;
		try{		
		Collections.sort(highValuesLst);
		
		String maxPartition = (String)highValuesLst.get(highValuesLst.size() -1 );
		String minPartition =  (String)highValuesLst.get(0);
		
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Max Partition and Min Partition for the table : "+tableName +" are: "+maxPartition + " And " + minPartition);
		}
		
		/** This If block is executed for checking partition of the purge date mentioned in properties file .**/
		if (partitionFound(results, retrievePurgeDate(count))) { 

			SimpleDateFormat dateFormat = new SimpleDateFormat(PhotoOmniConstants.CALENDER_DATE_PATTERN);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH,1);
			cal.add(Calendar.MONTH,2);
			String currentDatePlus = dateFormat.format(cal.getTime());
			
			/**  This If block is executed to check partition exist for current month + 2, for example if 
			 * current month is January then partition will be found for March to check future Partition **/
			if(!partitionFound(results,currentDatePlus)){
				
				System.out.println("Purge process is executing ... ");
				dropPartitionDDL = "{call wag_batch_ddl.rotate_table( ? )}";
	
				LOGGER.debug("The Stored Procedure DDL statement :: "
						+ dropPartitionDDL);
	
				/** Execute wag_batch_ddl.rotate_table store procedure **/
				omniJdbcTemplate.update(dropPartitionDDL, tableName);
				
				exitCodeDDL = "select wag_batch_ddl.get_exit_code( ?, ? ) from dual";
				
				/** Execute wag_batch_ddl.get_exit_code store procedure **/
				omniJdbcTemplate.update(exitCodeDDL, tableName,
						"ROTATE_TABLE");
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(" Purge is done for the table : "+tableName);
				}
			}else{
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(" Purge is Already done in DB for date: "+currentDatePlus +" of the table : "+ tableName);
				}
			}
			
		 } else {
			if(LOGGER.isDebugEnabled()){
			   LOGGER.debug(" Past partition not found for specified date of purge date for the table : "+tableName);
		}
				
		}
	  }catch(PhotoOmniException ex){
		LOGGER.error("Exception Occure at getPartitionName method");
	  }
	}

	/**
     * Utility method to convert the results of the partition select query and extract out the 
     * high_value column within the results and store into a list.
     * 
     * @param pResults  List of HashMap objects that contain the results of the patition select query
     *                  passed into method.

     * @return List  Return a List object of high_value based on results of select query passed into method.
     * 
     */
	public List<String> getHighValueList(List<Map<String, Object>> pResults) {
		ArrayList<String> aList = new ArrayList<String>();
		for (int i = 0; i < pResults.size(); i++) {
			HashMap<String, Object> partDtls = (HashMap<String, Object>) pResults.get(i);
			if (!CommonUtil.isNull(partDtls.get("HIGH_VALUE"))) {
				aList.add((String) partDtls.get("HIGH_VALUE"));
			}
		}
		return aList;
	}
	
	/**
     * Retrieve purge date based on purge rule configured within property file. Will look up the purge
     * rule (in months) from the property file based on table name passed into method. Key/Value pair
     * within the property file defining the purge criteria.  Value is in months as a negative value.
     * For example, purge after 18 months would be defined at -18.
     * 
     * @param tableName  Table name
     * @param statusBean  Bean that is used to store various status attributes for table that will get
     *                     included in final status email that job sends out.
     * @return String  Return the calculated partition to be purged in YYYY-MM-DD string format.
     * @throws PhotoOmniException  ApplicationException thrown under following scenarios:
     *                               - unable to retrieve purge date value from property file
     *                               - invalid purge value
     *                               
     */
	public String retrievePurgeDate(int count) throws PhotoOmniException {
		int purgeDate;
		String strDate="";
		String purgeDateStr = prop.get("purgeDate").toString();
		String[] purgeDtArry = purgeDateStr.split(",");
		purgeDate = Integer.parseInt(purgeDtArry[count].toString());
		 
		if (purgeDate >= 0) {
			LOGGER.error("Invalid purge months. Must be a negative integer value defined in property file.");
			throw new PhotoOmniException("Invalid purge months. Must be a negative integer value defined in property file."); 
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat(PhotoOmniConstants.CALENDER_DATE_PATTERN);
			Calendar cal = Calendar.getInstance();
			
			/** Delete the below line **/
			cal.set(Calendar.DAY_OF_MONTH,1);
			cal.add(Calendar.MONTH, purgeDate);
			strDate = dateFormat.format(cal.getTime());
			LOGGER.debug("Purge Date := " + strDate);
		}
		
		return strDate;
	}
	
	/**
     * Loop through the partitions found for a table to see if there is a partition based on the
     * purgeDate parameter that is passed into method.
     * 
     * @param sqlRows  Partitions selected from the all_tab_partitions table.
     * @param purgeDate Partition that needs to be found
     * @param pLogger	Logger object to write logs to log file.
     * @param pStatusBean  Bean that is used to store various status attributes for table that will get
     *                     included in final status email that job sends out.
     * @return boolean  Return true if partition found. Return false if partition not found.
     * @throws ApplicationException  ApplicationException thrown if unable to retrieve the high_value
     * 	                             column from the all_tab_partitions table.
     */
	private static boolean partitionFound(List<Map<String, Object>> sqlRows, String purgeDate)
			throws PhotoOmniException {
		LOGGER.debug("Inside partitionFound()");
		boolean found = false;
		
		for (int i = 0; i < sqlRows.size(); i++) {
			HashMap<String, Object> partDtls = (HashMap<String, Object>) sqlRows.get(i);
			if (!CommonUtil.isNull(partDtls.get("HIGH_VALUE"))) {
				LOGGER.debug("HIGH_VALUE" + (String) partDtls.get("HIGH_VALUE"));
				CharSequence cs = purgeDate;
				found = ((String) partDtls.get("HIGH_VALUE")).contains(cs);
				if (found) {
					if (!CommonUtil.isNull(partDtls.get("PARTITION_NAME"))) {
						
						System.out.println("Partition name found based on the Purge Date :: " + (String) partDtls.get("PARTITION_NAME"));
						
						partitionName = (String) partDtls.get("PARTITION_NAME");
					}
					break;
				}else{
					partitionName = "";
				}
			} else {
				LOGGER.error("Error at partitionFound method trying to retrieve high_value column from table");
				throw new PhotoOmniException("Error at partitionFound method  trying to retrieve high_value column from table"); 
				
			}

		}
		return found;
	}
}
