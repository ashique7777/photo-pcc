/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.writer;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * {@link RoyaltySalesReportEmailWriter} is a business implementation class for {@link ItemWriter}
 * This class is used to send reports through Email
 * from database
 * @author CTS
 * @since v1.0
 */
public class RoyaltySalesReportEmailWriter  implements ItemWriter<MimeMessage>{

	private RoyaltySalesReportPrefDataBean royaltySalesReportPrefDataBean;
	private static final Logger logger = LoggerFactory.getLogger(RoyaltySalesReportEmailWriter.class);
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ReportBOFactory reportBOFactory;
	
	/**
	 * Method to send report through Email using MIME object 
	 * @param MimeMessage
	 */
	public void write(List<? extends MimeMessage> items) throws Exception {
			logger.info(" Entering write method of RoyaltySalesReportEmailCustomWriter ");
		try {
			 ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
			List<String> fileNames = royaltySalesReportPrefDataBean.getFileNameList();
			String fileLocation = royaltySalesReportPrefDataBean.getFileLocation();
			MimeMessage mimeMessage = items.get(0);
			mailSender.send(mimeMessage);
			reportsUtilBO.deleteFile(fileNames, fileLocation);
		} catch (Exception e) {
			logger.error(" Error occoured at process write of RoyaltySalesReportEmailCustomWriter ----> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
				logger.info(" Exiting write method of RoyaltySalesReportEmailCustomWriter ");
		}
	}
	
	
	@BeforeStep
	private void retriveValue(StepExecution stepExecution){
		logger.info(" Entering retriveValue method of RoyaltySalesReportEmailCustomWriter ");
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		royaltySalesReportPrefDataBean = (RoyaltySalesReportPrefDataBean) executionContext.get("refDataKey");
	}

}
