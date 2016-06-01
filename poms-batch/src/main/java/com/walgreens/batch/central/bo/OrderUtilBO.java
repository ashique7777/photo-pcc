package com.walgreens.batch.central.bo;

import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.bean.KronosDataBean;
import com.walgreens.batch.central.bean.StoreOpenCloseDataBean;
import com.walgreens.common.exception.PhotoOmniException;

public interface OrderUtilBO {

	/**
	 * This method creates the CSV file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData.
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createCSVFile(Object objData, String lcAdhocExcepReport) throws PhotoOmniException ;

	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param mailSender contains mailSender value.
	 * @param dataBean contains dataBean value.
	 * @param reportType contains objData reportType.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public MimeMessage getMimeMessage(JavaMailSender mailSender, Object objData, String reportType) throws PhotoOmniException;
	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param mailSender contains mailSender value.
	 * @param simpleMailMessage contains simpleMailMessage value.
	 * @param dataBean contains dataBean value.
	 * @param reportType contains objData reportType.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public MimeMessage getMimeMessage(JavaMailSender mailSender, Object objData,String mailFrom, String reportType) throws PhotoOmniException;
	
	/**
	 * This method creates the Dat file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createDatOrIrsFile(Object objData,String datOrIrsFile) throws PhotoOmniException;
	
	/**
	 * Utility to delete list of files specified in the location.
	 * 
	 * @param fileNames -- List of files to be deleted
	 * @param fileLocation -- Location of the file list
	 * @throws PhotoOmniException - Custom exception defined in the application
	 */
	public void deleteFile(List<String> fileNames, String fileLocation) throws PhotoOmniException ;
	
	/**
	 * To get E-Mail details like to, from , bcc etc from the configuration 
	 * and report data using reportName. 
	 * Set the data fetched into EmailReportBean.
	 * 
	 * @param reportName -- Name of E-Mail report to be generated
	 * @return EmailReportBean -- Contains report details like to, from and report data etc
	 */
	public EmailReportBean getEmailReportDetails(String reportName) throws PhotoOmniException;
	
	/**
	 * This method create the Slot count logic for kronos.
	 * @param kronosDataBean contains data from database.
	 * @param storeOpenCloseDataBeanList contains store open close slot.
	 * @exception PhotoOmniException - custom exception.
	 */
	public void kronosDataFormatting(KronosDataBean item, Map<String, StoreOpenCloseDataBean> storeOpenCloseDataBeanList) throws PhotoOmniException;

}
