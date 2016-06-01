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

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p> Custom item processor class which  implements spring itemProcessor 
 * which will get the mimemessage bean to send mail
 * 
 * {@link RoyaltySalesReportEmailItemProcessor} is a business implementation class for {@link ItemProcessor}
 * This class is used to send reports through Email
 * from database
 * 
 * @author CTS
 * @since v1.0
 */
public class RoyaltySalesReportEmailItemProcessor implements ItemProcessor<RoyaltySalesReportPrefDataBean, MimeMessage>{


	@Autowired
	private ReportBOFactory reportBOFactory;
	
	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	
	@Autowired
	private JavaMailSender mailSender;

	private MimeMessage mimeMessage ;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RoyaltySalesReportEmailItemProcessor.class);

	/**
	 * Method to create Mime message object 
	 * @param RoyaltySalesReportPrefDataBean
	 * @return MimeMessage
	 */
	public MimeMessage process(RoyaltySalesReportPrefDataBean item)
			throws Exception {
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering process method of RoyaltySalesReportEmailItemProcessor --- >");
		}
		try{
			royaltySalesReportPrefDataBean = item;
			ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
			mimeMessage = reportsUtilBO.getMimeMessage(mailSender, 
					royaltySalesReportPrefDataBean, royaltySalesReportPrefDataBean.getReportType());
		}catch (Exception e) {
			LOGGER.error(" Error occoured at process method of RoyaltySalesReportEmailItemProcessor ---->  " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting process method of RoyaltySalesReportEmailItemProcessor ---> ");
			}
		}
		return mimeMessage;
	}
	
	/** 
	 * Method to delete XLSX file after sending mail
	 */
	@AfterStep
	private void deleteFile(){
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering deleteFile method of RoyaltySalesReportEmailMonthlyReader --- >");
		}
		try {
		ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
		List<String> fileNames = royaltySalesReportPrefDataBean.getFileNameList();
		String fileLocation = royaltySalesReportPrefDataBean.getFileLocation();
			reportsUtilBO.deleteFile(fileNames, fileLocation);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at deleteFile method of RoyaltySalesReportEmailItemProcessor ---->  " + e);
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting deleteFile method of RoyaltySalesReportEmailItemProcessor ---> ");
			}
	}
	}

}
