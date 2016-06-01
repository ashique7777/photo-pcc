/* Copyright (c) 2015, Walgreens Co. */
package com.walgreens.batch.central.reader;

import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.bo.OrderUtilBO;
import com.walgreens.batch.central.bo.ReportsUtilBO;
import com.walgreens.batch.central.factory.OMSBOFactory;
import com.walgreens.batch.central.factory.ReportBOFactory;
import com.walgreens.batch.central.provider.EmailRenderer;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * <p>
 * 	A Custom item reader class implements Spring itemReader.
 * 	Used to fetch E-Mail report data for the specified report using reportName
 * </p>
 * 
 * {@link EmailReportingCustomReader} is a business implementation class for {@link ItemReader}
 *  @author CTS
 * @since v1.0
 */
public class EmailReportingCustomReader implements ItemReader<MimeMessage> {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailReportingCustomReader.class);

	/**
	 * BO Factory to get orders utility objects
	 */
	@Autowired
	private OMSBOFactory omsboFactory;

	/**
	 * Email Renderer to get Email provider
	 */
	@Autowired
	private EmailRenderer renderer;

	/**
	 * Contains details about type of email report
	 */
	private String reportName;

	/**
	 * Contains Email details used to create MIME object
	 */
	@Autowired
	private SimpleMailMessage simpleMailMessage;

	/**
	 * MailSender object contains details like host,
	 * post etc for sending mail.
	 */
	@Autowired
	private JavaMailSender mailSender;

	/**
	 * MIME object used for sending mails
	 */
	private MimeMessage mimeMessage;

	/**
	 * BO Factory to get reports utility objects
	 */
	@Autowired
	private ReportBOFactory reportBOFactory;

	@SuppressWarnings("unused")
	private long jobSubmitTime;

	/**
	 * Method to set jobSubmitTime from jobParameter
	 * 
	 * @return jobSubmitTime
	 */
	public void setJobSubmitTime(long jobSubmitTime) {
		this.jobSubmitTime = jobSubmitTime;
	}

	/**
	 * Method set the email reportName from jobParameter
	 * 
	 * @param reportName
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	static int count = 0;

	/**
	 * Reads E-mail report data based on reportName. It will render the data
	 * using E-mail render suitable for E-Mail and sets the data to MIME object
	 * 
	 * @return MimeMessage -- Contains E-mail reports details like from, to message body etc
	 * @throws PhotoOmniException -- Custom Exception defined for application
	 */
	@Override
	public MimeMessage read() throws PhotoOmniException {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering into EmailReportingCustomReader.read() method" );
		}
		if (count == 0) {
			try{
				OrderUtilBO orderUtilBO = omsboFactory.getOrderUtilBO();
				ReportsUtilBO reportsUtilBO = reportBOFactory.getReportsUtilBO();
				EmailReportBean bean = orderUtilBO
						.getEmailReportDetails(reportName);
				mimeMessage = reportsUtilBO.getEmailReportMimeMessage(mailSender,
						simpleMailMessage, bean);
				mimeMessage.setContent(renderer.render(bean).getProcessedFragment(), "text/html; charset=utf-8");
				count++;
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("Finish execution read method in EmailReportingCustomReader for ",reportName);
				}
				return mimeMessage;
			}catch (Exception e) {
				LOGGER.error(" Error occoured at EmailReportingCustomReader.read() Method ----> " + e);
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug(" Exiting from EmailReportingCustomReader.read() Method ---> ");
				}
			}
		} else {
			return null;
		}
	}
}
