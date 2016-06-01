package com.walgreens.batch.central.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.walgreens.batch.central.bean.CSVFileRoyaltyReportDataBean;
import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.mail.SendMailService;
import com.walgreens.common.exception.PhotoOmniException;

public interface ReportsUtilBO {
	
	/**
	 * This method creates the CSV file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createCSVFile(Object objData, String reportTyp) throws PhotoOmniException;
	
	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param objData contains objData value.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public SendMailService sendEmail(Object objData) throws PhotoOmniException;
	
	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * 
	 * @param mailSender contains mailSender value.
	 * @param simpleMailMessage contains simpleMailMessage value.
	 * @param dataBean contains dataBean value.
	 * @param reportType contains objData reportType.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public MimeMessage getMimeMessage(JavaMailSender mailSender,
			Object objData, String reportType) throws PhotoOmniException;
	/**
	 * Method to create XLSX file
	 * 
	 * @param fileLocation, fileName
	 * @throws IOException 
	 * @throws PhotoOmniException 
	 * 
	 */
	public Object createXlsxFile(Object objData , String reportTyp) throws IOException, PhotoOmniException;
	
	/**
	 * method to populate data to the report XLSX data sheet
	 * 
	 * @param royaltyDataBean - > CSVFileRoyaltyReportBean
	 * @param dataValueRow  -> excel sheet data row
	 * @throws PhotoOmniException
	 */
	public void populateRoyaltyReportData(CSVFileRoyaltyReportDataBean royaltyDataBean, XSSFRow dataValueRow) throws PhotoOmniException;
	
	/** Method to extract data and group by printSize
	 * 
	 * @param csvFileRoyaltyReportsDataBeans - > royalty data bean list
	 * @throws PhotoOmniException 
	 */
	public Map<String, List<CSVFileRoyaltyReportDataBean>> groupByPrintSize(List<CSVFileRoyaltyReportDataBean> csvFileRoyaltyReportDataBeans) 
			throws PhotoOmniException;
	
	/** Method to extract data and group by productName
	 * 
	 * @param csvFileRoyaltyReportsDataBeans - > royalty data bean list
	 * @throws PhotoOmniException 
	 */
	public Map<String, List<CSVFileRoyaltyReportDataBean>> groupByProductName(List<CSVFileRoyaltyReportDataBean> csvFileRoyaltyReportDataBeans) 
			throws PhotoOmniException;
	
	/**
	 * Method to get filterParams for RoyaltyReport 
	 * 
	 * @param FilterState -- Contains start date, end date, Email ID
	 * @param FilterMap --  Map containing start date end date and Email ID
	 * @throws PhotoOmniException
	 */
	public Map<String, Object> getfilterParamsForRoyalty(String filterState) throws PhotoOmniException;
	
	/**
	 * method to populate data into summary sheet
	 * 
	 * @param royaltyDataBean - > CSVFileRoyaltyReportBean
	 * @param dataValueRow  -> excel sheet data row
	 * @throws PhotoOmniException
	 */
	public void populateRoyaltyReportSummaryData(CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean,XSSFRow dataValueRow) throws PhotoOmniException;

	/**
	 * This method is used to delete the file.
	 * 
	 * @param fileNames contains file names.
	 * @param fileLocation contains file location.
	 * @throws PhotoOmniException 
	 */
	public void deleteFile(List<String> fileNames, String fileLocation) throws PhotoOmniException ;

	/**
	 * To create MIME Message object for sending E-Mail
	 * 
	 * @param mailSender -- Contains hostName, port number etc
	 * @param simpleMailMessage -- Contains E-mail details like to, From etc
	 * @param bean -- Email report bean 
	 * @return MimeMessage -- MIME message object with to from and Email report details
	 * @throws PhotoOmniException
	 */
	public MimeMessage getEmailReportMimeMessage(JavaMailSender mailSender,
			SimpleMailMessage simpleMailMessage, EmailReportBean bean) throws PhotoOmniException;
}
