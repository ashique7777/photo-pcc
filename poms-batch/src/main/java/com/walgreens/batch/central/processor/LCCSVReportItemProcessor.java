
/**
 * LCCSVReportItemProcessor.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 9 mar 2015
 *  
 **/
package com.walgreens.batch.central.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This processor class call the createCSVFile method to create the CSV file.  
 * 
 * @author CTS
 * @version 1.1 March 09, 2015
 */
public class LCCSVReportItemProcessor implements ItemProcessor<LCAndPSReportPrefDataBean, LCAndPSReportPrefDataBean> {
    /**
     * oMSBOFactory
     */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCCSVReportItemProcessor.class);
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean = null;
	
	/**
	 * This method use for calling the createCSVFile method of orderUtilBO 
	 * @param item
	 * @return lCAndPSReportPrefDataBean
	 * @throws Exception
	 */
	public LCAndPSReportPrefDataBean process(final LCAndPSReportPrefDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of LCCSVReportItemProcessor ");
		}
		try {
			OrderUtilBO orderUtilBO = null;
			lCAndPSReportPrefDataBean = item;
			orderUtilBO = oMSBOFactory.getOrderUtilBO();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) orderUtilBO.createCSVFile(lCAndPSReportPrefDataBean,
					PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCCSVReportItemProcessor - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of LCCSVReportItemProcessor ");
			}
		}
		return lCAndPSReportPrefDataBean;
	}

}
