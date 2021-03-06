
/**
 * LCReportEmailItemProcessor.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 11 mar 2015
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

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class creates the send mail object. 
 * @author CTS
 *  @version 1.1 March 11, 2015
 */

public class LCReportEmailItemProcessor implements ItemProcessor<LCAndPSReportPrefDataBean, MimeMessage>{
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
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportEmailItemProcessor.class);
	
	/**
	 * This method call the getMimeMessage method for MimeMessage object creation. 
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public MimeMessage process(final LCAndPSReportPrefDataBean item) throws PhotoOmniException {
		MimeMessage mimeMessage = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering SendMailService method of LCReportEmailItemProcessor ");
		}
		try {
			lCAndPSReportPrefDataBean = item;
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			mimeMessage = orderUtilBO.getMimeMessage(mailSender, lCAndPSReportPrefDataBean, PhotoOmniBatchConstants.LC_ADHOC_EXCEP_REPORT);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of LCReportEmailItemProcessor - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting SendMailService method of LCReportEmailItemProcessor ");
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
			LOGGER.debug(" Entering deleteFile method of LCReportEmailItemProcessor ");
		}
		try {
			final OrderUtilBO orderUtilBO = oMSBOFactory.getOrderUtilBO();
			List<String> fileNames = lCAndPSReportPrefDataBean.getFileNameList();
			String fileLocation = lCAndPSReportPrefDataBean.getFileLocation();
			orderUtilBO.deleteFile(fileNames, fileLocation);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at deleteFile method of LCReportEmailItemProcessor ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting deleteFile method of LCReportEmailItemProcessor ");
			}
		}
	}
	
}
