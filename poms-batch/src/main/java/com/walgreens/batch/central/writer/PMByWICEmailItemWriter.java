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
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.processor.LCReportEmailItemProcessor;
import com.walgreens.common.exception.PhotoOmniException;

public class PMByWICEmailItemWriter implements ItemWriter<MimeMessage>{

	@Autowired
	private ReportBOFactory reportBOFactory;//Use to call the BO factory class
	PMBYWICReportPrefDataBean objPmbywicReportPrefDataBean;
	/**
	 * mailSender
	 */
	@Autowired
	private JavaMailSender mailSender;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LCReportEmailItemProcessor.class);
	public void write(List<? extends MimeMessage> items)
			throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of LCReportEmailItemWriter ");
		}
		try {
			final ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
			List<String> fileNames = objPmbywicReportPrefDataBean.getFileNameList();
			String fileLocation = objPmbywicReportPrefDataBean.getFileLocation();
			MimeMessage mimeMessage = items.get(0);
			mailSender.send(mimeMessage);
			reportsUtilBO.deleteFile(fileNames, fileLocation);
		}catch (MailException e) {
			LOGGER.error(" Error occoured at write method of LCReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of LCReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of LCReportEmailItemWriter ");
			}
		}
		//objSendMailService.sendMail(objPmbywicReportPrefDataBean, PhotoOmniBatchConstants.PMBYWIC_CUSTOM);;
	}

	@BeforeStep
	private void retriveValue(StepExecution stepExecution){
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		objPmbywicReportPrefDataBean = (PMBYWICReportPrefDataBean) executionContext.get("refDataKey");
	}
}
