package com.walgreens.batch.central.processor;

import java.util.List;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.common.exception.PhotoOmniException;

public class PLUEmailItemProcessor implements
		ItemProcessor<PLUReportPrefDataBean, MimeMessage> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PLUEmailItemProcessor.class);
	@Autowired
	private OMSBOFactory omsboFactory;

	@Autowired
	private JavaMailSender mailSender;

	private PLUReportPrefDataBean pluReportPrefDataBean;

	@Override
	public MimeMessage process(PLUReportPrefDataBean item)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering process method of PLUEmailItemProcessor ");
		}
		MimeMessage mimeMessage = null;
		try {
			pluReportPrefDataBean = item;
			OrderUtilBO orderUtilBO = omsboFactory.getOrderUtilBO();
			mimeMessage = orderUtilBO.getMimeMessage(mailSender,
					pluReportPrefDataBean,
					pluReportPrefDataBean.getReportType());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process method of PLUEmailItemProcessor ---- > "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting process method of PLUEmailItemProcessor ");
			}
		}
		return mimeMessage;
	}

	@BeforeStep
	private void retriveValue(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		pluReportPrefDataBean = (PLUReportPrefDataBean) executionContext
				.get("refDataKey");
	}

	/**
	 * Method to delete CSV file after sending mail
	 */
	@AfterStep
	private void deleteFile() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering deleteFile method of PLUEmailItemProcessor --- >");
		}
		try {
			OrderUtilBO orderUtilBO = omsboFactory.getOrderUtilBO();
			List<String> fileNames = pluReportPrefDataBean.getFileNameList();
			String fileLocation = pluReportPrefDataBean.getFileLocation();
			orderUtilBO.deleteFile(fileNames, fileLocation);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at deleteFile method of PLUEmailItemProcessor ---->  "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting deleteFile method of PLUEmailItemProcessor ---> ");
			}
		}
	}
}