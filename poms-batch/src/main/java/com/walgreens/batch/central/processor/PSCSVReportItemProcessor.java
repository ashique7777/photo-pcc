/**
 * PSCSVReportItemProcessor.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 01 March 2015
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
 * @version 1.1 March 01, 2015
 */

public class PSCSVReportItemProcessor implements ItemProcessor<LCAndPSReportPrefDataBean, LCAndPSReportPrefDataBean> {
	
	/**
	 * oMSBOFactory
	 */
	@Autowired
	private OMSBOFactory oMSBOFactory;
	/**
	 * lCAndPSReportPrefDataBean
	 */
	LCAndPSReportPrefDataBean lCAndPSReportPrefDataBean = null;
	
	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PSCSVReportItemProcessor.class);
	
	/**
	 * This method use for calling the createCSVFile method of orderUtilBO 
	 * @param item contains item value.
	 * @return lCAndPSReportPrefDataBean
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public LCAndPSReportPrefDataBean process(final LCAndPSReportPrefDataBean item) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering LCAndPSReportPrefDataBean method of PSCSVReportItemProcessor ");
		}
		try {
			lCAndPSReportPrefDataBean = item;
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			lCAndPSReportPrefDataBean = (LCAndPSReportPrefDataBean) orderUtilBO.createCSVFile(lCAndPSReportPrefDataBean,
					PhotoOmniConstants.PRINT_SIGN_REPORT);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of PSCSVReportItemProcessor - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting LCAndPSReportPrefDataBean method of PSCSVReportItemProcessor ");
			}
		}
		
		return lCAndPSReportPrefDataBean;
	}

}
