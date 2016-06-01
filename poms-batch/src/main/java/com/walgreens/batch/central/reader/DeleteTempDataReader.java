/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * A custom item reader which readers the query which is needed
 * to delete the data from the the specified date
 * </p>
 * 
 * <p>
 * {@link DeleteTempDataReader} is a business implementation class for {@link ItemReader}
 * </p>
 * 
 * @author CTS
 * @since v1.0
 */
public class DeleteTempDataReader implements ItemReader<List<String>>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTempDataReader.class);
	/** 
	 * Variable which hold details of which
	 * report it triggered this job
	 */
	private String reportType;

	int count = 0;

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
	 * Method to create delete query based on report type
	 * 
	 * @return List<String>
	 * @throws PhotoOmniException
	 */
	@Override
	public List<String> read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into DeleteTempDataReader.read() --- >");
		}
		List<String> queryList = null;
		if(count ==0){
			count++;
			try {
				if("PROMOTIONFEED".equals(reportType))
				{
					queryList = ReportsQuery.getDeleteTempTableQuery();
					queryList.add(ReportsQuery.getDeleteStoreAssociationQuery());
				}
				// get the query for other reports here 
			}catch(Exception e){
				LOGGER.error(" Error occoured at DeleteTempDataReader.read() ---->  " + e);
				throw new PhotoOmniException(e);
			}finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting DeleteTempDataReader.read() ---> ");
				}
			}
		}
		return queryList;
	}
}
