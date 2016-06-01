

/**
 * LCDailyReportZipCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 16 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.utility.ZipCreatorUtil;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This writer class call the createZip method of ZipCreator. 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyReportZipCustomWriter implements ItemWriter<LCDailyReportPrefDataBean>{
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyReportZipCustomWriter.class);
	
	/**
	 * This method call the createZipFile method for zip file creation. 
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends LCDailyReportPrefDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCDailyReportZipCustomWriter ");
		}
		try {
			final LCDailyReportPrefDataBean lCDailyReportPrefDataBean = items.get(0);
			ZipCreatorUtil.createZipFile(lCDailyReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyReportZipCustomWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCDailyReportZipCustomWriter ");
			}
		}
	}
	
}
