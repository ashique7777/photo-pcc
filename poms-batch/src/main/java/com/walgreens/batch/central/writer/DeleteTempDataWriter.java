/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * <p>
 * A custom item writer  to delete 
 * the data from temporary tables for specified date
 * </p>
 * 
 * <p>
 * {@link DeleteTempDataWriter} is a business implementation class for {@link ItemWriter}
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public class DeleteTempDataWriter implements ItemWriter<List<String>>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTempDataWriter.class);

	/**
	 *  number specify temporary data older that these many 
	 *  days need to be deleted. 
	 */
	private Long noOfDaysOldData;

	/**
	 * set noOfDaysOldData  
	 * from jobParameter
	 * 
	 * @param noOfDaysOldData
	 */
	public void setNoOfDaysOldData(Long noOfDaysOldData) {
		this.noOfDaysOldData = noOfDaysOldData;
	}

	/**
	 * jdbctemplate to execute the query
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Value( "${promotion.inbound.feed.location}" )
	private String strInboundFeedLocation;

	@Value( "${promotion.archival.feed.location}" )
	private String strarchivalFeedLocation;
	/** 
	 * Variable which hold details of which
	 * report it triggered this job
	 */
	private String reportType;

	/**
	 * method to set the report type 
	 * from the jobParameter
	 * 
	 * @param reportType
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * method to delete the data from 
	 * temporary tables for the date specified
	 * 
	 * @param List<? extends List<String>> items
	 * @throws PhotoOmniException
	 */
	@Override
	public void write(List<? extends List<String>> items) throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into DeleteTempDataWritter.write() --- >");
		}
		try{
			// To move processed files to archival folder

			boolean status = CommonUtil.moveFilesBasedOnExtension(new File(strInboundFeedLocation), 
					new File(strarchivalFeedLocation), new String[] {PhotoOmniConstants.PROCESSED_FILE_EXT});
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Processed file move status" + status);
			}
			List<String> queryList = items.get(0);
			Object [] params =populateParams();
			for(String query : queryList)
			{
				jdbcTemplate.update(query,params);
			}
		}catch(Exception e){
			LOGGER.error(" Error occoured at DeleteTempDataWritter.write() ---->  " + e);
			throw new PhotoOmniException(e);
		}finally {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(" Exiting DeleteTempDataWritter.write() ---> ");
			}
		}

	}

	/**
	 * Method to populate date for query params
	 * 
	 * @return Object[]
	 */
	private Object [] populateParams(){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into DeleteTempDataWritter.populateParams() --- >");
		}
		if("PROMOTIONFEED".equals(reportType)){
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("NoOfDaysOlderFile" + noOfDaysOldData);
			}
			return  new Object[] {noOfDaysOldData};
		}else{
			return null;
		}
	}
}
