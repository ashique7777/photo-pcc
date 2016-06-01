
/**
 * LCReportZipCustomWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 10 mar 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.utility.ZipCreatorUtil;
import com.walgreens.common.exception.PhotoOmniException;
/**
 * This writer class call the createZip method of ZipCreator. 
 * @author CTS
 * @version 1.1 March 10, 2015
 */

public class LCReportZipCustomWriter implements ItemWriter<LCAndPSReportPrefDataBean>{
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportZipCustomWriter.class);
	
	
	/**
	 * This method call the createZipFile method for zip file creation. 
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends LCAndPSReportPrefDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCReportZipCustomWriter ");
		}
		try {
			final LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean = items.get(0);
			ZipCreatorUtil.createZipFile(lCAndPSReportPrefDataBean);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCReportZipCustomWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCReportZipCustomWriter ");
			}
		}

	}
	
}
