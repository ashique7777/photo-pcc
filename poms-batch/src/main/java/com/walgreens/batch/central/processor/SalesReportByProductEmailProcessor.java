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

import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.mail.SendMailService;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * <p>
 * This is a custom item processor class implementing spring itemProcessor.
 * This class will create mimeMessage object and delete csv file once it is mail to user
 * </p>
 * 
 * {@link SalesReportByProductEmailProcessor} is a business implementation class for {@link ItemProcessor}

 * @author CTS
 * @since v1.0
 */
public class SalesReportByProductEmailProcessor implements
		ItemProcessor<SalesReportByProductBean, MimeMessage> {

	@Autowired
	private ReportBOFactory reportBOFactory;
	
	@Autowired
	SendMailService sendMailService;
	
	private SalesReportByProductBean salesReportByProductBean;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesReportByProductEmailProcessor.class);
	
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	
	private MimeMessage mimeMessage;
	
	/**
	 * Method to create Mime message object 
	 * 
	 * @param SalesReportByProductBean -- Data bean which contains filter criteria and report type details
	 * @return MimeMessage -- Message object which contains from to and message details
	 * @throws PhotoOmniException -- Custom exception
	 */
	public MimeMessage process(SalesReportByProductBean item)
			throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Entering into SalesReportByProductEmailProcessor.process() method -- >");
		}
		try{
			salesReportByProductBean = item;
			if(null != salesReportByProductBean){
				ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
				mimeMessage = reportsUtilBO.getMimeMessage(mailSender, 
						salesReportByProductBean, salesReportByProductBean.getReportType());
			}
		}catch (Exception e) {
			LOGGER.error(" Error occoured at into SalesReportByProductEmailProcessor.process() method ---->  " + e);
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from SalesReportByProductEmailProcessor.process() method -- >");
			}
		}
		return mimeMessage;
	}
	
	/** 
	 * Method to delete CSV file after sending mail
	 */
	@AfterStep
	private void deleteFile(){
		if(LOGGER.isDebugEnabled()){
		LOGGER.debug(" Entering into SalesReportByProductEmailProcessor.deleteFile() method --- >");
		}
		try {
			if(null != salesReportByProductBean){
			ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
			List<String> fileNames = salesReportByProductBean.getFileNameList();
			String fileLocation = salesReportByProductBean.getFileLocation();
			reportsUtilBO.deleteFile(fileNames, fileLocation);
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at SalesReportByProductEmailProcessor.deleteFile() method ---->  " + e);
		}finally {
			if(LOGGER.isDebugEnabled()){
			LOGGER.debug(" Exiting from SalesReportByProductEmailProcessor.deleteFile() method ---> ");
			}
		}
	}

}
