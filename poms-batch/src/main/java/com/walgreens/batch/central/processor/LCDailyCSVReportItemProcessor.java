
/**
 * LCDailyCSVReportItemProcessor.java 
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
package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This processor class call the createCSVFile method to create the CSV file.  
 * 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyCSVReportItemProcessor implements ItemProcessor<LCDailyReportPrefDataBean, LCDailyReportPrefDataBean> {
    /**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * lCDailyReportPrefDataBean
	 */
	LCDailyReportPrefDataBean lCDailyReportPrefDataBean = null;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyCSVReportItemProcessor.class);
	
	
	/**
	 * This method use for calling the createCSVFile method of orderUtilBO 
	 * @param item
	 * @return lCDailyReportPrefDataBean
	 * @throws PhotoOmniException - Custom Exception
	 */
	public LCDailyReportPrefDataBean process(final LCDailyReportPrefDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of LCDailyCSVReportItemProcessor ");
		}
		try {
			OrderUtilBO orderUtilBO = null;
			lCDailyReportPrefDataBean = item;
			orderUtilBO = oMSBOFactory.getOrderUtilBO();
			lCDailyReportPrefDataBean = (LCDailyReportPrefDataBean) orderUtilBO.createCSVFile(lCDailyReportPrefDataBean,
					PhotoOmniConstants.LC_DAILY_REPORT);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyCSVReportItemProcessor - ", e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of LCDailyCSVReportItemProcessor ");
			}
		}
		
		return lCDailyReportPrefDataBean;
	}

}
