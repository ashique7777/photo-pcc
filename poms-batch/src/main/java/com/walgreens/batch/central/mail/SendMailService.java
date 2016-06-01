/**
 * 
 */
package com.walgreens.batch.central.mail;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.batch.central.bean.SalesReportByProductBean;

/**
 * @author CTS
 *
 */
public class SendMailService {
	
	private JavaMailSender mailSender;
	private SimpleMailMessage simpleMailMessage;
	private static final Logger logger = LoggerFactory.getLogger(SendMailService.class);
 
	/**
	 * This method is involved to send Email with attached document 
	 * @param dataBean contains databean value.
	 * @param reportType contains report type.
	 * @exception PhotoOmniException custom exception.
	 */
	public void sendMail(Object dataBean, String reportType) throws PhotoOmniException {
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering sendMail method of SendMailService ");
		}
		String fileLocation = null;
		List<String> fileNames = null;
		String users = PhotoOmniConstants.TARGET_RECIPENT_PERSONS;
		String content = PhotoOmniConstants.EMAIL_MESSAGE_CONTENT;
		MimeMessage message = mailSender.createMimeMessage();
		FileSystemResource file = null;
		File fileToDelete = null;
		try {
			if (dataBean instanceof LCAndPSReportPrefDataBean) {
				fileLocation = ((LCAndPSReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((LCAndPSReportPrefDataBean) dataBean).getFileNameList();
				
			} else if (dataBean instanceof LCDailyReportPrefDataBean) {
				fileLocation = ((LCDailyReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((LCDailyReportPrefDataBean) dataBean).getFileNameList();
			
			} else if (dataBean instanceof PMBYWICReportPrefDataBean) {
				fileLocation = ((PMBYWICReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((PMBYWICReportPrefDataBean) dataBean).getFileNameList();
			
			} else if (dataBean instanceof RoyaltySalesReportPrefDataBean) {
				fileLocation = ((RoyaltySalesReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((RoyaltySalesReportPrefDataBean) dataBean).getFileNameList();
			
			} else if (dataBean instanceof PLUReportPrefDataBean) {
				fileLocation = ((PLUReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((PLUReportPrefDataBean) dataBean).getFileNameList();
			}else if (dataBean instanceof SalesReportByProductBean) {
				fileLocation = ((SalesReportByProductBean) dataBean).getFileLocation();
				fileNames = ((SalesReportByProductBean) dataBean).getFileNameList();
			}
			
			String addedDateMessage = this.getSubjectorMessageForEmail(dataBean, reportType);
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(simpleMailMessage.getFrom());
			helper.setTo(simpleMailMessage.getTo());
			helper.setSubject(simpleMailMessage.getSubject().concat(addedDateMessage));
			
		    if (PhotoOmniConstants.PAY_ON_FULLFILLMENT.equals(reportType)) {
		    	helper.setText(String.format(users.concat(content).concat(this.getTextMessageForPOF())));
			} else {
				helper.setText(String.format(users.concat(content).concat(addedDateMessage)));
			}
		    
            for (String fileName : fileNames) {
            	 file = new FileSystemResource(fileLocation.concat(fileName));
            	 helper.addAttachment(file.getFilename(), file);
			}
            mailSender.send(message);
		} catch (MailException e) {
			logger.error(" Error occoured at sendMail method of SendMailService - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (MessagingException e) {
			logger.error(" Error occoured at sendMail method of SendMailService - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			logger.error(" Error occoured at sendMail method of SendMailService - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			/*As per business logic if the mail is not sent still the file should be deleted*/
			boolean deleteFlag = false;
			for (String fileName : fileNames) {
				fileToDelete = new File(fileLocation.concat(fileName));
				deleteFlag = fileToDelete.delete();
				if (logger.isDebugEnabled()) {
					logger.debug(" Deleted file " + fileName +" is " + deleteFlag);
				}
			}
			logger.info(" Exiting sendMail method of SendMailService ");
		}
		
	}

	
	/**
	 * This method add the Text message for pay on fullfillment E-mail Message
	 * @return txtMessage contains txtMessage value.
	 * @exception PhotoOmniException custom exception.
	 */
	private String getTextMessageForPOF() throws PhotoOmniException{
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering getTextMessageForPOF method of SendMailServices ");
		}
		String txtMessage = null;
		try{
		txtMessage = "There are few Vendor Payments awating your approval. Please login to PhotoOmni Application and approve the payments";
		} catch (Exception e) {
			logger.error(" Error occoured at getTextMessageForPOF method of SendMailService - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting getTextMessageForPOF method of SendMailService ");
			}
		}
		return txtMessage;
	}



	/**
	 * This method add the date rang to E-Mail subject and E-mail Message
	 * @param objVal contains object value.
	 * @return reportType contains report type value.
	 * @exception PhotoOmniException custom exception.
	 */
	 private String getSubjectorMessageForEmail(Object objVal, String reportType) throws PhotoOmniException /*throws ReportException*/{
		 if (logger.isDebugEnabled()) {
				logger.debug(" Entering createCSVFile method of OrderUtilBOImpl ");
			}
		StringBuffer addedDateMessage = null;
		JSONObject objJson = null;
		String mainSub = null;
		try {
			if (objVal instanceof LCAndPSReportPrefDataBean) {
				 objJson = new JSONObject(((LCAndPSReportPrefDataBean) objVal).getFilterState());
			} else if (objVal instanceof LCDailyReportPrefDataBean) {
				 objJson = new JSONObject(((LCDailyReportPrefDataBean) objVal).getFilterState());
			} else if (objVal instanceof PMBYWICReportPrefDataBean) {
				 objJson = new JSONObject(((PMBYWICReportPrefDataBean) objVal).getFilterState());
			} else if (objVal instanceof RoyaltySalesReportPrefDataBean) {
				 objJson = new JSONObject(((RoyaltySalesReportPrefDataBean) objVal).getFilterMap());
			} else if (objVal instanceof PLUReportPrefDataBean) {
				objJson = new JSONObject(((PLUReportPrefDataBean) objVal).getFilterState());
			} else if (objVal instanceof SalesReportByProductBean) {
				objJson = new JSONObject(((SalesReportByProductBean) objVal).getFilterState());
			} 
			
			if (PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT.equals(reportType)) {
				mainSub = " License Content Adhoc report and Exceptin report between ";
			} else if (PhotoOmniConstants.LC_DAILY_REPORT.equals(reportType)) {
				mainSub = " License Content Daily report between ";
			} else if (PhotoOmniConstants.PRINT_SIGN_REPORT.equals(reportType)) {
				mainSub = " Print Signs report between ";
			} if(PhotoOmniConstants.ROYALTY_CUSTOM.equals(reportType) ||PhotoOmniConstants.ROYALTY_MONTHLY.equals(reportType) ){
				mainSub = " Royalty Report between ";
			} else if(PhotoOmniConstants.PMBYWIC_CUSTOM.equals(reportType)||PhotoOmniConstants.PMBYWIC_MONTHLY.equals(reportType)){
				mainSub = " PM by WIC Report between ";
			} else if(PhotoOmniConstants.PLU_DAILY.equals(reportType)) {
				mainSub = " Daily PLU Report Between ";
			} else if (PhotoOmniConstants.PLU_ADHOC.equals(reportType)) {
				mainSub = " Ad-hoc PLU Report Between ";
			} else if (PhotoOmniConstants.PAY_ON_FULLFILLMENT.equals(reportType)) {
				mainSub = " Pay On Fullfillment - Approval Required ";
			}
			if (!CommonUtil.isNull(objJson)) {
				String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
				String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_TWO);
				addedDateMessage = new StringBuffer();
				addedDateMessage.append(mainSub);
				addedDateMessage.append(startDate);
				addedDateMessage.append(" to ");
				addedDateMessage.append(endDate);
			} else {
				addedDateMessage.append(mainSub);
			}
			
		} catch (Exception e) {
			logger.error(" Error occoured at sendMail method of SendMailService - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting sendMail method of SendMailService ");
			}
		}
		
		return addedDateMessage.toString();
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
	 * @return the simpleMailMessage
	 */
	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

	/**
	 * @param simpleMailMessage the simpleMailMessage to set
	 */
	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
}
