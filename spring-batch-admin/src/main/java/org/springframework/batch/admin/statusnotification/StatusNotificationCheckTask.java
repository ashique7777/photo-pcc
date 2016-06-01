package org.springframework.batch.admin.statusnotification;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.admin.bean.JobStatusNotificationDetailsBean;
import org.springframework.batch.admin.rowmapper.JobStatusNotificationDetailsRowmapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component(value = "StatusNotificationCheckTask")
public class StatusNotificationCheckTask {

	/**
	 * JDbc template
	 */
	@Autowired
	@Qualifier("jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	/**
	 * mailSender object
	 */
	@Autowired
	private JavaMailSender mailSender;

	@Value("${email.to}")
	private String toEmailIds;

	@Value("${email.from}")
	private String fromEmailIds;

	@Value("${email.subject}")
	private String emailSubject;

	@Value("${email.linkfor.moreinfo}")
	private String moreInfoLink;

	@Value("${email.data.query}")
	private String emailNotifyQuery;

	@Value("${email.data.jobstatus.query}")
	private String emailNotifyFromJobStatusTblQuery;
	
	@Value("${email.data.update.query}")
	private String emailNotifyIndUpdateQuery;
	


	/**
	 * Logger object
	 */
	protected final Log logger = LogFactory.getLog(StatusNotificationCheckTask.class);

	/**
	 * Public Method to send Job Status Notification mail to support team.
	 * 
	 * @throws Exception
	 * @version 1.0
	 */
	public void sendJobStatusNotification() {

		if (logger.isDebugEnabled()) {
			logger.debug(" Entering into StatusNotificationCheckTask.sendJobStatusNotification() ");
		}
		try {
			List<JobStatusNotificationDetailsBean> lstJobStatusNotificationDetails = null;

			lstJobStatusNotificationDetails = jdbcTemplate.query(this.emailNotifyQuery,
					new JobStatusNotificationDetailsRowmapper());

			List<JobStatusNotificationDetailsBean> lstOmTransferJobStatusDetails = null;

			lstOmTransferJobStatusDetails = jdbcTemplate.query(this.emailNotifyFromJobStatusTblQuery,
					new JobStatusNotificationDetailsRowmapper());

			// If there are any failure records
			if (lstJobStatusNotificationDetails != null && !lstJobStatusNotificationDetails.isEmpty() 
					|| lstOmTransferJobStatusDetails != null && !lstOmTransferJobStatusDetails.isEmpty()) {

				List<JobStatusNotificationDetailsBean> mergedlstJobStatusNotificationDetails = new ArrayList<JobStatusNotificationDetailsBean>();
				if(lstJobStatusNotificationDetails != null && !lstJobStatusNotificationDetails.isEmpty()){
					mergedlstJobStatusNotificationDetails.addAll(lstJobStatusNotificationDetails);
				}
				if(lstOmTransferJobStatusDetails != null && !lstOmTransferJobStatusDetails.isEmpty()){
					
					for(JobStatusNotificationDetailsBean objJobStatusNotificationDetails : lstOmTransferJobStatusDetails){
						jdbcTemplate.update(emailNotifyIndUpdateQuery, new Object[] {objJobStatusNotificationDetails.getJobInstanceId()});
					}
					mergedlstJobStatusNotificationDetails.addAll(lstOmTransferJobStatusDetails);
				}
				sendMail(getMailBody(mergedlstJobStatusNotificationDetails) );
			}

		} catch (Exception e) {
			logger.error(" Error occoured at StatusNotificationCheckTask.sendJobStatusNotification() " + e);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting From StatusNotificationCheckTask.sendJobStatusNotification()");
			}
		}
	}

	/**
	 * Private method to send Job Status Notification mail to support team.
	 * 
	 * @param strMailBody
	 *            contains From ID, To ID, Subject, Email body etc.
	 * @throws MessagingException
	 *             , Exception
	 * @version 1.0
	 */
	private void sendMail(String strMailBody) {

		if (logger.isDebugEnabled()) {
			logger.debug(" Entering into StatusNotificationCheckTask.sendMail() " + strMailBody);
			logger.debug(" From Mail ID: " + this.fromEmailIds + "toEmailIds : " + this.toEmailIds + "emaiSubject : "
					+ this.emailSubject);
		}
		MimeMessage mimeMessage = null;

		try {
			mimeMessage = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			helper.setFrom(this.fromEmailIds);
			helper.setTo(this.toEmailIds.split(","));
			helper.setSubject(this.emailSubject);
			mimeMessage.setContent(strMailBody, "text/html; charset=utf-8");
			if (logger.isDebugEnabled()) {
				logger.debug(" Before sending mail in StatusNotificationCheckTask.sendMail() " + mimeMessage);
			}
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			logger.error(" Error occoured while creating MIME message in sendJobStatusNotification.sendMail() method "
					+ e);
		} catch (Exception e) {
			logger.error(" Error occoured at StatusNotificationCheckTask.sendMail() " + e);
		}
	}

	/**
	 * Private method to get html mail Body.
	 * 
	 * @param lstJobStatusNotificationDetails
	 *            contains list of Job Status Notification details like
	 *            JOB_INSTANCE_ID, JOB_NAME, CREATE_TIME etc.
	 * @return String contains html mail Body.
	 * @version 1.0
	 */
	private String getMailBody(List<JobStatusNotificationDetailsBean> lstJobStatusNotificationDetails) {

		if (logger.isDebugEnabled()) {
			logger.debug(" Entering into StatusNotificationCheckTask.getMailBody() ");
		}
		StringBuilder builder = new StringBuilder();

		builder.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;'>Below job(s) have been failed.</div>");
		builder.append("</br>");
		builder.append("</br>");
		builder.append("<table style='font-family:Courier New,Courier;font-size:11pt;border-collapse: collapse;' cellspacing='10' cellpadding='8'>");
		builder.append("<thead>");
		builder.append("<tr>");
		builder.append("<th align='left' style='border-bottom:1px solid black;' >JOB_INSTANCE_ID</th>");
		builder.append("<th align='center' style='border-bottom:1px solid black;'>JOB_NAME</th>");
		builder.append("<th align='center' style='border-bottom:1px solid black;'>JOB_EXECUTION_ID</th>");
		builder.append("<th align='center' style='border-bottom:1px solid black;'>START_TIME</th>");
		builder.append("<th align='center' style='border-bottom:1px solid black;'>END_TIME</th>");
		builder.append("</tr>");
		builder.append("</thead>");
		builder.append("<tbody>");

		try {

			for (JobStatusNotificationDetailsBean objJobStatusNotificationDetails : lstJobStatusNotificationDetails) {

				builder.append("<tr>");
				builder.append("<td align='left'>");
				builder.append(objJobStatusNotificationDetails.getJobInstanceId());
				builder.append("</td>");
				builder.append("<td align='center'>");
				builder.append(objJobStatusNotificationDetails.getJobName());
				builder.append("</td>");
				builder.append("<td align='center'> ");
				builder.append(objJobStatusNotificationDetails.getJobExecutionId());
				builder.append("</td>");
				builder.append("<td align='center'>");
				builder.append(objJobStatusNotificationDetails.getStartTime());
				builder.append("</td>");
				builder.append("<td align='center'>");
				builder.append(objJobStatusNotificationDetails.getEndTime());
				builder.append("</td>");
				builder.append("</tr>");
			}

			builder.append("</tbody>");
			builder.append("</table>");
			builder.append("</br>");
			builder.append("</br>");
			builder.append("</br>");
			builder.append("<div style='font-weight: bold;font-family:Courier New,Courier;font-size:11pt;color:#878787;'>");
			builder.append("<a href='" + this.moreInfoLink + "'>Please click here for more information.</a></div>");

		} catch (Exception e) {
			logger.error(" Error occoured at StatusNotificationCheckTask.getMailBody() " + e);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Mail Body : " + builder);
				logger.debug(" Exiting From StatusNotificationCheckTask.getMailBody()");
			}
		}
		return builder.toString();
	}
}