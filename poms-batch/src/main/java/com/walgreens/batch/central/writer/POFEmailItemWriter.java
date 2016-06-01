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
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.POFEmailDataBean;
import com.walgreens.batch.central.processor.POFEmailItemProcessor;
import com.walgreens.common.exception.PhotoOmniException;

public class POFEmailItemWriter implements ItemWriter<MimeMessage>{

	POFEmailDataBean pofEmailDataBean = null;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFEmailItemProcessor.class);
	/**
	 * mailSender
	 */
	private JavaMailSender mailSender;
	
	boolean isMail;
	/**
	 * This write method call the sendEmail method for sending email. 
	 * @param items contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends MimeMessage> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of POFEmailItemWriter ");
		}
		try {
			
			if(isMail){
  			   MimeMessage mimeMessage = items.get(0);
			   mailSender.send(mimeMessage);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of POFEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of POFEmailItemWriter ");
			}
		}
	}
	/**
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}
	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	
	
	/**
	 * Retrieve value get the 'refPofEmailKey' value from execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@BeforeStep
	private void retriveValue(final StepExecution stepExecution) throws PhotoOmniException{
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of PSReportEmailItemWriter ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			isMail = ((Boolean) executionContext.get("refPofEmailKey")).booleanValue();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of PSReportEmailItemWriter ");
			}
		}
	}
	
}
