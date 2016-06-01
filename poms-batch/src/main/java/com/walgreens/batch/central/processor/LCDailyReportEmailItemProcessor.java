
/**
 * LCDailyReportEmailItemProcessor.java 
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

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class creates the send mail object. 
 * @author CTS
 * @version 1.1 March 16, 2015
 */

public class LCDailyReportEmailItemProcessor implements ItemProcessor<LCDailyReportPrefDataBean, MimeMessage>{
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
	private static final Logger LOGGER = LoggerFactory.getLogger(LCDailyReportEmailItemProcessor.class);
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	/**
	 * This method call the getMimeMessage method for MimeMessage object creation.  
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public MimeMessage process(final LCDailyReportPrefDataBean item) throws PhotoOmniException {
		MimeMessage mimeMessage = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of LCDailyReportEmailItemProcessor ");
		}
		try {
			lCDailyReportPrefDataBean = item;
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			mimeMessage = orderUtilBO.getMimeMessage(mailSender, lCDailyReportPrefDataBean, PhotoOmniConstants.LC_DAILY_REPORT);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCDailyReportEmailItemProcessor - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of LCDailyReportEmailItemProcessor ");
			}
		}
	    return mimeMessage;
	}
	
	/**
	 * This method delete files.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void deleteFile() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering deleteFile method of LCDailyReportEmailItemWriter ");
		}
		try {
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			List<String> fileNames = lCDailyReportPrefDataBean.getFileNameList();
			String fileLocation = lCDailyReportPrefDataBean.getFileLocation();
			orderUtilBO.deleteFile(fileNames, fileLocation);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at deleteFile method of LCDailyReportEmailItemWriter ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting deleteFile method of LCDailyReportEmailItemWriter ");
		}
	}
}
