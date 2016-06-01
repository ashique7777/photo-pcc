/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.processor;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	Custom item Processor implements Spring itemProcessor 
 * </p>
 * 
 * <p>
 * Class to generate MIMEMESSAGE using pmByWic report bean
 * </p>
 * {@link PMByWICEmailItemProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to update report status as inactive
 * @author CTS
 * @since v1.0
 */
public class PMByWICEmailItemProcessor implements ItemProcessor<PMBYWICReportPrefDataBean, MimeMessage>{

	@Autowired
	private ReportBOFactory objReportBOFactory;//Use to call the BO factory class
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender objJavaMailSender;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PMByWICEmailItemProcessor.class);

	PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean = null;

	/**
	 * Method will create MimeMessage used to send E-Mail
	 * 
	 * @param PMBYWICReportPrefDataBean -- Bean contains EmailIDs and reprot file location 
	 * @throws  Exception
	 */
	public MimeMessage process(PMBYWICReportPrefDataBean item)
			throws Exception {

		MimeMessage objMimeMessage = null;
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICEmailItemProcessor.process() ");
		}
		try {
			objPMBYWICReportPrefDataBean = item;
			ReportsUtilBO objReportsUtilBO = objReportBOFactory.getReportsUtilBO();
			objMimeMessage = objReportsUtilBO.getMimeMessage(objJavaMailSender, 
					objPMBYWICReportPrefDataBean, objPMBYWICReportPrefDataBean.getReportType());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at PMByWICEmailItemProcessor.process() --> " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From PMByWICEmailItemProcessor.process() ");
			}
		}
		return objMimeMessage;
	}

	/**
	 * This method delete files.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void deleteFile() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into PMByWICEmailItemProcessor.deleteFile() ");
		}
		try {

			ReportsUtilBO objReportsUtilBO = objReportBOFactory.getReportsUtilBO();
			List<String> fileNames = objPMBYWICReportPrefDataBean.getFileNameList();
			String fileLocation = objPMBYWICReportPrefDataBean.getFileLocation();
			objReportsUtilBO.deleteFile(fileNames, fileLocation);

		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at PMByWICEmailItemProcessor.deleteFile() " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Exiting From PMByWICEmailItemProcessor.deleteFile() ");
			}
		}
	}
}
