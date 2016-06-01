package com.walgreens.batch.central.bo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.CSVFileRoyaltyReportDataBean;
import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.dao.OrdersUtilDAO;
import com.walgreens.batch.central.factory.BatchOmsDAOFactory;
import com.walgreens.batch.central.mail.SendMailService;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

@Component("ReportsUtilBO")
public class ReportsUtilBOImpl implements ReportsUtilBO {

	public SendMailService objSendMailService;

	@Value( "${pmw.work.folder.path}" )
	private String strPmByWicFilePath;

	@Value( "${ry.work.folder.path}" )
	private String strRoyaltyFilePath;

	@Value( "${sbp.work.folder.path}" )
	private String strSalesFilePath;
	
	@Autowired
	private BatchOmsDAOFactory omsDAOFactory;
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportsUtilBOImpl.class);

	/**
	 * This method creates the CSV file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createCSVFile(Object objData, String reportTyp) throws PhotoOmniException {
		LOGGER.info(" Entering into ReportsUtilBOImpl.createCSVFile() ");

		FileWriter fileWriter = null;
		String strAddedPath = null;
		String abspathWithFileNm = null;
		String abspathWithOutFileNm = null;
		File objDirectory = null;
		String fileHeader = null;
		Long repPrefId = null ;
		List<String> fileNameList = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		try {
			this.getFileNmAndHeader(reportTyp, fileHeader, fileNames);
			String fileLocation = this.getFileSaveLocation(reportTyp);

			if (objData instanceof PMBYWICReportPrefDataBean) {
				repPrefId = ((PMBYWICReportPrefDataBean) objData).getSysUserReportPrefId();
			} else if(objData instanceof SalesReportByProductBean) {
				repPrefId = ((SalesReportByProductBean) objData).getSysUserReportPrefId();
			}

			for (String fileName : fileNames) {
				strAddedPath =  fileLocation.concat(fileName).concat(repPrefId.toString()).concat(PhotoOmniConstants.CSV_FILE_EXTENSION);
				objDirectory = new File(strAddedPath);
				if (objDirectory.exists()) {
					objDirectory.delete();
				}

				if(!objDirectory.isDirectory()) {
					try {
						fileWriter = new FileWriter(strAddedPath);
						if (PhotoOmniBatchConstants.PM_BY_WIC_REPORT_CUSTOM_FILE_NAME
								.equals(fileName)
								|| PhotoOmniBatchConstants.PM_BY_WIC_REPOT_MONTHLY_NAME
								.equals(fileName)) {
							String startDate = "", endDate = "";
							if(PhotoOmniBatchConstants.PM_BY_WIC_REPORT_CUSTOM_FILE_NAME.equals(fileName)){
							PMBYWICReportPrefDataBean objPMBYWICReportPrefDataBean  = (PMBYWICReportPrefDataBean) objData;
							final JSONObject objJson = new JSONObject(objPMBYWICReportPrefDataBean.getFilterState());
							 startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							 endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							}else{
								String strStartDateAndEndDateQuery = ReportsQuery.getStartDateAndEndDate().toString();
								List<Map<String, Object>> lstDates = jdbcTemplate.queryForList(strStartDateAndEndDateQuery);
								for (Map<String, Object> map : lstDates) {
									startDate = (String) map.get("STARTDATE");
									endDate = (String) map.get("ENDDATE"); 
								}
							}
							String  strSearchHeader =  "From  "+ startDate + " To " + endDate  + PhotoOmniBatchConstants.NEW_LINE_SEPARATOR +
									PhotoOmniBatchConstants.NEW_LINE_SEPARATOR;
							fileHeader = strSearchHeader + PhotoOmniBatchConstants.PM_WIC_CSV_FILE_HEADER+ PhotoOmniBatchConstants.NEW_LINE_SEPARATOR ;
						}else if (PhotoOmniBatchConstants.SALES_REPORT_FILE_NAME
								.equals(fileName)) {
							SalesReportByProductBean salesReportByProductBean  = (SalesReportByProductBean) objData;
							final JSONObject objJson = new JSONObject(salesReportByProductBean.getFilterState());
							final String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							final String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							String  strSearchHeader =  "From:, "+ startDate + ",To:, " + endDate  + PhotoOmniBatchConstants.NEW_LINE_SEPARATOR +
									PhotoOmniBatchConstants.NEW_LINE_SEPARATOR;
							fileHeader = strSearchHeader + PhotoOmniBatchConstants.SALES_REPORT_FILE_HEADER;
							getMetaData(((SalesReportByProductBean) objData), fileWriter);
						}
						fileWriter.append(fileHeader); 
					} catch (IOException e) {
						LOGGER.error(" Error occoured at ReportsUtilBOImpl.createCSVFile() --- > " + e.getMessage());
						throw new PhotoOmniException(e.getMessage());
					} finally { 
						try { 
							fileWriter.flush(); 
						} catch (IOException e) { 
							LOGGER.error(" Error occoured at ReportsUtilBOImpl.createCSVFile() --- > " + e.getMessage());
							throw new PhotoOmniException(e.getMessage());
						} 
					} 
				}
				fileNameList.add(objDirectory.getName());

				if (objData instanceof PMBYWICReportPrefDataBean) {
					((PMBYWICReportPrefDataBean) objData).setFileNameList(fileNameList);
				}else if (objData instanceof SalesReportByProductBean) {
					((SalesReportByProductBean) objData).setFileNameList(fileNameList);
				}
			}
			abspathWithFileNm = objDirectory.getAbsolutePath();
			int indexVal = abspathWithFileNm.lastIndexOf('/');
			abspathWithOutFileNm = abspathWithFileNm.substring(0, (indexVal+1));

			if (objData instanceof PMBYWICReportPrefDataBean) {
				((PMBYWICReportPrefDataBean) objData).setFileLocation(abspathWithOutFileNm);
			} else if (objData instanceof SalesReportByProductBean) {
				((SalesReportByProductBean) objData).setFileLocation(abspathWithOutFileNm);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.createCSVFile() --- > " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.debug(" Exiting From ReportsUtilBOImpl.createCSVFile() -- >");
		}
		return  objData;
	}

	/**
	 * This method checks what type of report is selected and 
	 * generate file name and header according to the file type.
	 * @param reportTyp contains report type.
	 * @param fileHeader contains file header.
	 * @param fileNames contains file names.
	 * @exception PhotoOmniException custom exception.
	 */
	private void getFileNmAndHeader(String reportTyp, String fileHeader, List<String> fileNames) throws PhotoOmniException {
		LOGGER.debug(" Entering into ReportsUtilBOImpl.getFileNmAndHeader() ");
		try {

			if(PhotoOmniBatchConstants.PMBYWIC_CUSTOM.equals(reportTyp)){
				fileNames.add(PhotoOmniBatchConstants.PM_BY_WIC_REPORT_CUSTOM_FILE_NAME);
			}else if(PhotoOmniBatchConstants.PMBYWIC_MONTHLY.equals(reportTyp)){
				fileNames.add(PhotoOmniBatchConstants.PM_BY_WIC_REPOT_MONTHLY_NAME);
			}else if(PhotoOmniBatchConstants.SALES_CUSTOM_REPORT.equals(reportTyp)){
				fileNames.add(PhotoOmniBatchConstants.SALES_REPORT_FILE_NAME);
			}else if(PhotoOmniBatchConstants.ROYALTY_CUSTOM.equals(reportTyp) 
					|| PhotoOmniBatchConstants.ROYALTY_MONTHLY.equals(reportTyp)){
				fileNames.add(PhotoOmniBatchConstants.ROYALTY_REPORT_FILE_NAME);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getFileNmAndHeader() >---> " + e.getMessage());
			throw new PhotoOmniException(e);
		} finally {
			LOGGER.info(" Exiting From ReportsUtilBOImpl.getFileNmAndHeader() ");
		}
	}

	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param objData contains objData value.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public SendMailService sendEmail(Object objData) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into ReportsUtilBOImpl.sendEmail() ");
		}
		JSONObject objJson = null;
		String strEmailIDs = "";
		try {
			if (objData instanceof PMBYWICReportPrefDataBean) {
				objJson = new JSONObject(((PMBYWICReportPrefDataBean) objData).getFilterState());
			} else if (objData instanceof RoyaltySalesReportPrefDataBean) {
				objJson = new JSONObject(((RoyaltySalesReportPrefDataBean) objData).getFilterState());
			}else if (objData instanceof SalesReportByProductBean) {
				objJson = new JSONObject(((SalesReportByProductBean) objData).getFilterState());
			}
			strEmailIDs = objJson.getString("emailIds");
			String[] emailArray = strEmailIDs.split(",");

			if (null != objSendMailService && null != objSendMailService.getSimpleMailMessage()) {
				objSendMailService.getSimpleMailMessage().setTo(emailArray);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at sendEmail method of ReportsUtilBOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From ReportsUtilBOImpl.sendEmail() ");
			}
		}
		return objSendMailService;
	}

	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param mailSender contains mailSender value.
	 * @param simpleMailMessage contains simpleMailMessage value.
	 * @param dataBean contains dataBean value.
	 * @param reportType contains objData reportType.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public MimeMessage getMimeMessage(JavaMailSender mailSender,
			Object dataBean, String reportType) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into ReportsUtilBOImpl.getMimeMessage() ");
		}
		JSONObject objJson = null;
		String strEmailIDs = "";
		MimeMessage message = null;
		String fromEmailID = "";
		List<String> fileNames = null;
		String fileLocation = null;
		try {
			FileSystemResource file = null;
			String users = PhotoOmniConstants.TARGET_RECIPENT_PERSONS;
			String content = PhotoOmniConstants.EMAIL_MESSAGE_CONTENT;
			message = mailSender.createMimeMessage();


			if (dataBean instanceof PMBYWICReportPrefDataBean) {
				objJson = new JSONObject(((PMBYWICReportPrefDataBean) dataBean).getFilterState());
				fileLocation = ((PMBYWICReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((PMBYWICReportPrefDataBean) dataBean).getFileNameList();
			} else if (dataBean instanceof RoyaltySalesReportPrefDataBean) {
				objJson = new JSONObject(((RoyaltySalesReportPrefDataBean) dataBean).getFilterMap());
				fileLocation = ((RoyaltySalesReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((RoyaltySalesReportPrefDataBean) dataBean).getFileNameList();
			} else if (dataBean instanceof SalesReportByProductBean) {
				objJson = new JSONObject(((SalesReportByProductBean) dataBean).getFilterState());
				fileLocation = ((SalesReportByProductBean) dataBean).getFileLocation();
				fileNames = ((SalesReportByProductBean) dataBean).getFileNameList();
			}else if (dataBean instanceof StringBuffer) {
				strEmailIDs = dataBean.toString();
			}

			fromEmailID = this.getFromEmailId(reportType);
			
			if (!CommonUtil.isNull(objJson)) {
				strEmailIDs = objJson.getString("emailIds");
			}
			String[] emailArray = strEmailIDs.split(",");

			String addedDateMessage = this.getSubjectorMessageForEmail(dataBean, reportType);
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(fromEmailID);
			helper.setTo(emailArray);
			helper.setSubject(addedDateMessage);			
			helper.setText(String.format(users.concat(content).concat(addedDateMessage)));


			for (String fileName : fileNames) {
				file = new FileSystemResource(fileLocation.concat(fileName));
				helper.addAttachment(file.getFilename(), file);
			}

		} catch (MessagingException e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getMimeMessage() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}  catch (ClassCastException e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getMimeMessage() >--->  " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (JSONException e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getMimeMessage() >--->  " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getMimeMessage() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
				LOGGER.debug(" Exiting From ReportsUtilBOImpl.getMimeMessage() >--->  ");
		}
		return message;
	}

	/**
	 * This method add the date rang to E-Mail subject and E-mail Message
	 * @param objVal contains object value.
	 * @return reportType contains report type value.
	 * @exception PhotoOmniException custom exception.
	 */
	private String getSubjectorMessageForEmail(Object objVal, String reportType) throws PhotoOmniException /*throws ReportException*/{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into ReportsUtilBOImpl.getSubjectorMessageForEmail() >--->  ");
		}
		StringBuffer addedDateMessage = null;
		JSONObject objJson = null;
		String mainSub = null;
		try {
			if (objVal instanceof PMBYWICReportPrefDataBean) {
				objJson = new JSONObject(((PMBYWICReportPrefDataBean) objVal).getFilterState());
			}else if (objVal instanceof RoyaltySalesReportPrefDataBean) {
				objJson = new JSONObject(((RoyaltySalesReportPrefDataBean) objVal).getFilterMap());
			}else if (objVal instanceof SalesReportByProductBean) {
				objJson = new JSONObject(((SalesReportByProductBean) objVal).getFilterState());
			}

			if (PhotoOmniBatchConstants.PMBYWIC_CUSTOM.equals(reportType)||PhotoOmniBatchConstants.PMBYWIC_MONTHLY.equals(reportType)) {
				mainSub = "PMs earned by WIC report between ";
			} else if(PhotoOmniBatchConstants.ROYALTY_CUSTOM.equals(reportType) ||PhotoOmniBatchConstants.ROYALTY_MONTHLY.equals(reportType) ){
				mainSub = " Royalty Report between ";
			} else if(PhotoOmniBatchConstants.SALES_CUSTOM_REPORT.equals(reportType) ||PhotoOmniBatchConstants.SALES_MONTHLY_REPORT.equals(reportType) ){
				mainSub = " Sales Report between ";
			}   

			if (!CommonUtil.isNull(objJson)) {
				String startDate =  "", endDate = "";
				if(PhotoOmniBatchConstants.PMBYWIC_MONTHLY.equals(reportType)){
					String strStartDateAndEndDateQuery = ReportsQuery.getStartDateAndEndDate().toString();
					List<Map<String, Object>> lstDates = jdbcTemplate.queryForList(strStartDateAndEndDateQuery);
					for (Map<String, Object> map : lstDates) {
						startDate = (String) map.get("STARTDATE");
						endDate = (String) map.get("ENDDATE"); 
					}
				}else{
				startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
						PhotoOmniBatchConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
				endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
						PhotoOmniBatchConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
				}
				addedDateMessage = new StringBuffer();
				addedDateMessage.append(mainSub);
				addedDateMessage.append(startDate);
				addedDateMessage.append(" to ");
				addedDateMessage.append(endDate);
			} else {
				addedDateMessage = new StringBuffer();
				addedDateMessage.append(mainSub);
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getSubjectorMessageForEmail() >---> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From ReportsUtilBOImpl.getSubjectorMessageForEmail() >---> ");
			}
		}

		return addedDateMessage.toString();
	}

	/**
	 * Method to create XLSX file
	 * @param fileLocation, fileName
	 * @throws IOException 
	 * @throws PhotoOmniException 
	 * 
	 */
	public Object createXlsxFile(Object objData , String reportTyp) throws IOException, PhotoOmniException{
		LOGGER.info(" Entering into ReportsUtilBOImpl.createXlsxFile() -- > ");

		String strAddedPath = null;
		String abspathWithFileNm = null;
		String abspathWithOutFileNm = null;
		File excelFile = null;
		String fileHeader = null;
		Long repPrefId = null ;
		List<String> fileNameList = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		Map<String,Object> filterMap = null;
		int rowCount = 0;

		try {
			this.getFileNmAndHeader(reportTyp, fileHeader, fileNames);
			String fileLocation = this.getFileSaveLocation(reportTyp);
			String sheetName = PhotoOmniBatchConstants.SUMMARY;
			if (objData instanceof RoyaltySalesReportPrefDataBean) {
				repPrefId = ((RoyaltySalesReportPrefDataBean) objData).getSysUserReportPrefId();
			}
			for (String fileName : fileNames) {
				strAddedPath =  fileLocation.concat(fileName).concat(repPrefId.toString())
						.concat(PhotoOmniBatchConstants.XLS_FILE_EXTENSION);
				excelFile = new File(strAddedPath);
				if (excelFile.exists()) {
					excelFile.delete();
				}
				if(!excelFile.isDirectory()) {
					try {
						XSSFWorkbook workbook = new XSSFWorkbook();
						XSSFSheet sheet = workbook.createSheet(sheetName);
						//Set Style information
						CellStyle style = workbook.createCellStyle();
						XSSFFont fontTop = workbook.createFont();
						fontTop.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
						style.setFont(fontTop);
						style.setAlignment(CellStyle.ALIGN_LEFT);
						if (PhotoOmniBatchConstants.ROYALTY_REPORT_FILE_NAME
								.equals(fileName))
						{
							fileHeader = PhotoOmniBatchConstants.ROYALTY_REPOR_SUMMARY_HEADER;
							//To set From the to Date
							filterMap =((RoyaltySalesReportPrefDataBean) objData).getFilterMap();
							XSSFRow filterRow = sheet.createRow(rowCount);
							XSSFCell fromCell = filterRow.createCell(0);
							fromCell.setCellValue(PhotoOmniBatchConstants.FROM);
							fromCell.setCellStyle(style);
							XSSFCell fromValue = filterRow.createCell(1);
							String startDate  = CommonUtil
									.stringDateFormatChange(filterMap.get(PhotoOmniBatchConstants.START_DATE).toString(), 
											PhotoOmniConstants.DATE_FORMAT_SIX , PhotoOmniConstants.DATE_FORMAT_EIGHT);
							fromValue.setCellValue(startDate);
							XSSFCell toCell = filterRow.createCell(2);
							toCell.setCellValue(PhotoOmniBatchConstants.TO);
							toCell.setCellStyle(style);
							XSSFCell toValue = filterRow.createCell(3);
							String endDate  = CommonUtil
									.stringDateFormatChange(filterMap.get(PhotoOmniBatchConstants.END_DATE).toString(), 
											PhotoOmniConstants.DATE_FORMAT_SIX , PhotoOmniConstants.DATE_FORMAT_EIGHT);
							toValue.setCellValue(endDate);
							rowCount = rowCount+2;
						}
						// To set File header
						XSSFRow row = sheet.createRow(rowCount);
						List<String> headerList = Arrays.asList(fileHeader.split(PhotoOmniBatchConstants.COMMA_DELIMITER));
						CellStyle dataStyle = workbook.createCellStyle();
						dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
						dataStyle.setFont(fontTop);
						// To set yellow color
						dataStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
						for(int i = 0; i< headerList.size(); i++){
							XSSFCell cell = row.createCell(i);
							cell.setCellValue(headerList.get(i));
							cell.setCellStyle(dataStyle);
						}
						FileOutputStream out = new FileOutputStream(new File(excelFile.toString()));
						workbook.write(out);
					}catch (IOException e) {
						LOGGER.error(" Error occoured at ReportsUtilBOImpl.createXlsxFile() >---> " + e.getMessage());
						throw new PhotoOmniException(e);
					}
				}
				fileNameList.add(excelFile.getName());
				if (objData instanceof RoyaltySalesReportPrefDataBean) {
					((RoyaltySalesReportPrefDataBean) objData).setFileNameList(fileNameList);
				}
			}
			abspathWithFileNm = excelFile.getAbsolutePath();
			int indexVal = abspathWithFileNm.lastIndexOf('/');
			abspathWithOutFileNm = abspathWithFileNm.substring(0, (indexVal+1));
			if (objData instanceof RoyaltySalesReportPrefDataBean) {
				((RoyaltySalesReportPrefDataBean) objData).setFileLocation(abspathWithOutFileNm);
			}
		}catch (Exception e) {
			LOGGER.error(" ReportsUtilBOImpl.createXlsxFile() >--> " + e.getMessage());
			throw new PhotoOmniException(e);
		} finally {
			LOGGER.info(" Exiting From ReportsUtilBOImpl.createXlsxFile()  >-- > ");
		}
		return  objData;
	}

	/** Method to extract data and group by productName
	 * @param csvFileRoyaltyReportsDataBeans - > royalty data bean list
	 * @throws PhotoOmniException 
	 */
	public Map<String, List<CSVFileRoyaltyReportDataBean>> groupByProductName(List<CSVFileRoyaltyReportDataBean> csvFileRoyaltyReportDataBeans) 
			throws PhotoOmniException{

		LOGGER.info("Entered into groupByProductName method of ReportsUtilBOImpl  --- >");
		Map<String, List<CSVFileRoyaltyReportDataBean>> dataMap = new HashMap<String, List<CSVFileRoyaltyReportDataBean>>();
		try{
			for(CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean: csvFileRoyaltyReportDataBeans){
				String productName = csvFileRoyaltyReportDataBean.getProduct();
				if(dataMap.containsKey(productName)){
					List<CSVFileRoyaltyReportDataBean> value = dataMap.get(productName);
					value.add(csvFileRoyaltyReportDataBean);
					dataMap.put(productName, value);
				}else{
					List<CSVFileRoyaltyReportDataBean> value = new ArrayList<CSVFileRoyaltyReportDataBean>();
					value.add(csvFileRoyaltyReportDataBean);
					dataMap.put(productName, value);
				}

			}
		} catch (Exception e) { 
			LOGGER.error("Error in groupByProductName method of ReportsUtilBOImpl ----->"+e.getMessage());
			throw new PhotoOmniException(e.getMessage());

		} finally { 
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exiting groupByProductName method of ReportsUtilBOImpl -- >");  
		} 
		return dataMap;
	} 

	/** Method to extract data and group by printSize
	 * @param csvFileRoyaltyReportsDataBeans - > royalty data bean list
	 * @throws PhotoOmniException 
	 */
	public Map<String, List<CSVFileRoyaltyReportDataBean>> groupByPrintSize(List<CSVFileRoyaltyReportDataBean> csvFileRoyaltyReportDataBeans) 
			throws PhotoOmniException{

		LOGGER.info("Entered into groupByPrintSize method of ReportsUtilBOImpl -- > ");
		Map<String, List<CSVFileRoyaltyReportDataBean>> dataMap = new HashMap<String, List<CSVFileRoyaltyReportDataBean>>();
		try{
			for(CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean: csvFileRoyaltyReportDataBeans){
				String printSize = csvFileRoyaltyReportDataBean.getOutPutSize();
				if(dataMap.containsKey(printSize)){
					List<CSVFileRoyaltyReportDataBean> value = dataMap.get(printSize);
					value.add(csvFileRoyaltyReportDataBean);
					dataMap.put(printSize, value);
				}else{
					List<CSVFileRoyaltyReportDataBean> value = new ArrayList<CSVFileRoyaltyReportDataBean>();
					value.add(csvFileRoyaltyReportDataBean);
					dataMap.put(printSize, value);
				}

			}
		} catch (Exception e) { 
			LOGGER.error("Error in groupByPrintSize method of ReportsUtilBOImpl ----->"+e.getMessage());
			throw new PhotoOmniException(e.getMessage());

		} finally { 
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exiting groupByPrintSize method of ReportsUtilBOImpl --- >");  
		} 
		return dataMap;
	} 

	/**
	 * method to populate data to the report XLSX dats sheet
	 * @param royaltyDataBean - > CSVFileRoyaltyReportBean
	 * @param dataValueRow  -> excel sheet data row
	 * @throws PhotoOmniException
	 */
	public void populateRoyaltyReportData(CSVFileRoyaltyReportDataBean royaltyDataBean, XSSFRow dataValueRow) throws PhotoOmniException{
		LOGGER.info("Entered into populateRoyaltyReportData method of ReportsUtilBOImpl");
		try{
			XSSFCell storeNumberCell = dataValueRow.createCell(0);
			storeNumberCell.setCellValue(royaltyDataBean.getStoreNumber());
			XSSFCell templateIdell = dataValueRow.createCell(1);
			templateIdell.setCellValue(royaltyDataBean.getTemplateId());
			XSSFCell productNameCell = dataValueRow.createCell(2);
			productNameCell.setCellValue(royaltyDataBean.getProductName());
			XSSFCell templateNameCell = dataValueRow.createCell(3);
			templateNameCell.setCellValue(royaltyDataBean.getTemplateName());
			XSSFCell vendorCell = dataValueRow.createCell(4);
			vendorCell.setCellValue(royaltyDataBean.getVendor());
			XSSFCell noOfOrdersCell = dataValueRow.createCell(5);
			noOfOrdersCell.setCellValue(royaltyDataBean.getNumberOfOrders());
			XSSFCell noOfPrintsCell = dataValueRow.createCell(6);
			noOfPrintsCell.setCellValue(royaltyDataBean.getNumberOfPrints());
			XSSFCell soldAmountCell = dataValueRow.createCell(7);
			soldAmountCell.setCellValue(royaltyDataBean.getSoldAmount());
		} catch (Exception e) { 
			LOGGER.error("Error in populateRoyaltyReportData method of ReportsUtilBOImpl ----->"+e.getMessage());
			throw new PhotoOmniException(e.getMessage());

		} finally { 
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exiting populateRoyaltyReportData method of ReportsUtilBOImpl");  
		} 
	}

	/**
	 * method to populate data into summary sheet
	 * @param royaltyDataBean - > CSVFileRoyaltyReportBean
	 * @param dataValueRow  -> excel sheet data row
	 * @throws PhotoOmniException
	 */
	public void populateRoyaltyReportSummaryData(CSVFileRoyaltyReportDataBean csvFileRoyaltyReportDataBean,XSSFRow dataValueRow) throws PhotoOmniException{

		LOGGER.info("Entered into populateRoyaltyReportSummaryData method of ReportsUtilBOImpl");
		// To set the Cell details for the summary sheet using reflection
		try{
			XSSFCell storeNumberCell = dataValueRow.createCell(0);
			storeNumberCell.setCellValue(csvFileRoyaltyReportDataBean.getTemplateId());
			XSSFCell templateIdell = dataValueRow.createCell(1);
			templateIdell.setCellValue(csvFileRoyaltyReportDataBean.getProductName());
			XSSFCell productNameCell = dataValueRow.createCell(2);
			productNameCell.setCellValue(csvFileRoyaltyReportDataBean.getNumberOfPrints());
			XSSFCell soldAmountCell = dataValueRow.createCell(3);
			soldAmountCell.setCellValue(csvFileRoyaltyReportDataBean.getSoldAmount());
		} catch (Exception e) { 
			LOGGER.error("Error in populateRoyaltyReportSummaryData method of ReportsUtilBOImpl ----->"+e.getMessage());
			throw new PhotoOmniException(e.getMessage());

		} finally { 
			if (LOGGER.isDebugEnabled()) 
				LOGGER.debug("Exiting populateRoyaltyReportSummaryData method of ReportsUtilBOImpl");  
		} 
	}

	/**
	 * Method to get filterParams for RoyaltyReport 
	 * @param FilterState -- Contains start date, end date, Email ID
	 * @param FilterMap --  Map containing start date end date and Email ID
	 * @throws PhotoOmniException
	 */
	public Map<String, Object>  getfilterParamsForRoyalty(String filterState) throws PhotoOmniException{
		LOGGER.info("Entered into getFilterparams method of ReportsUtilBO");

		JSONParser parser = new JSONParser();
		Map<String, Object> filterMap = new HashMap<String, Object>();
		try {
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) parser
					.parse(filterState);
			filterMap.put(PhotoOmniBatchConstants.START_DATE,(String)jsonObject.get(PhotoOmniBatchConstants.START_DATE));
			filterMap.put(PhotoOmniBatchConstants.END_DATE,(String)jsonObject.get(PhotoOmniBatchConstants.END_DATE));
			filterMap.put(PhotoOmniBatchConstants.VENDOR,(String)jsonObject.get(PhotoOmniBatchConstants.VENDOR));
			String strEmailIDs = (String)jsonObject.get(PhotoOmniBatchConstants.EMAIL_IDS);
			filterMap.put(PhotoOmniBatchConstants.EMAIL_IDS,strEmailIDs);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getfilterParamsForRoyalty method of ReportsUtilBO ---- > " + e.getMessage());
			throw new PhotoOmniException(e);
		} finally {
			LOGGER.info(" Exiting getfilterParamsForRoyalty method of ReportsUtilBO ");
		}
		return filterMap;
	}

	/**
	 * This method is used to delete the file.
	 * @param fileNames contains file names.
	 * @param fileLocation contains file location.
	 * @throws PhotoOmniException 
	 */
	public void deleteFile(List<String> fileNames, String fileLocation) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into ReportsUtilBOImpl.deleteFile() >--> ");
		}
		try {
			File fileToDelete;
			boolean deleteFlag = false;
			for (String fileName : fileNames) {
				fileToDelete = new File(fileLocation.concat(fileName));
				deleteFlag = fileToDelete.delete();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Deleted file " + fileName +" is " + deleteFlag);
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.deleteFile() >--> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From ReportsUtilBOImpl.deleteFile() >--> ");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.batch.central.bo.ReportsUtilBO#getEmailReportMimeMessage(JavaMailSender,
	 * SimpleMailMessage, EmailReportBean )
	 */
	@Override
	public MimeMessage getEmailReportMimeMessage(JavaMailSender mailSender,
			SimpleMailMessage simpleMailMessage, EmailReportBean bean)
					throws PhotoOmniException {
		LOGGER.info(" Entering into ReportsUtilBOImpl.getEmailReportMimeMessage() method");
		MimeMessage message = null;
		try {
			message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(bean.getFrom());
			helper.setTo(bean.getTo());
			if (bean.getBcc() != null) {
				helper.setBcc(bean.getBcc());
			}
			if (bean.getCc() != null) {
				helper.setCc(bean.getCc());
			}
			helper.setSubject(bean.getSubject());

		}catch (MessagingException e) {
			LOGGER.error(" Error occoured while creating MIME message in ReportsUtilBOImpl.getEmailReportMimeMessage() method "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getEmailReportMimeMessage() method"
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			LOGGER.info(" Exiting from ReportsUtilBOImpl.getEmailReportMimeMessage() method");
		}
		return message;
	}


	/**
	 * Method to format PrintSize and Product type String
	 * @param salesReportByProductBean
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	private void getMetaData( SalesReportByProductBean salesReportByProductBean, FileWriter fileWriter) throws IOException{

		List<String> list = (List<String>) salesReportByProductBean.getFilterMap().get(PhotoOmniBatchConstants.PRINTSIZE);
		StringBuilder printSizeString = new StringBuilder();
		StringBuilder productTypeString = new StringBuilder();
		String delimiter = "";
		for (String productType : list) {
			printSizeString.append(delimiter).append(productType);
			delimiter = PhotoOmniConstants.COMMA_DELIMITER;
		}
		list = (List<String>) salesReportByProductBean.getFilterMap().get(PhotoOmniBatchConstants.PRODUCTTYPE);
		delimiter = "";
		for (String printSize : list) {
			productTypeString.append(delimiter).append(printSize);
			delimiter = PhotoOmniConstants.COMMA_DELIMITER;
		}
		fileWriter.append(PhotoOmniBatchConstants.PRODUCTTYPE+ PhotoOmniConstants.COMMA_DELIMITER+ "\"" +productTypeString.toString()+ "\"");
		fileWriter.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);
		fileWriter.append(PhotoOmniBatchConstants.PRINTSIZE +PhotoOmniConstants.COMMA_DELIMITER+"\""+printSizeString.toString()+"\"");
		fileWriter.append(PhotoOmniConstants.NEW_LINE_SEPARATOR);

	}

	/**
	 * This method initialize the file location value.
	 * @param reportTyp contains report type value.
	 * @return filePath contains file path.
	 * @throws PhotoOmniException custom exception. 
	 */
	private String getFileSaveLocation(String reportTyp) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into ReportsUtilBOImpl.getFileSaveLocation() >-->");
		}
		String filePath = null;
		try {

			if(PhotoOmniBatchConstants.PMBYWIC_CUSTOM.equals(reportTyp) || PhotoOmniBatchConstants.PMBYWIC_MONTHLY.equals(reportTyp)){
				filePath = strPmByWicFilePath;
			}else if(PhotoOmniBatchConstants.SALES_CUSTOM_REPORT.equals(reportTyp)){
				filePath = strSalesFilePath;
			}else if(PhotoOmniBatchConstants.ROYALTY_CUSTOM.equals(reportTyp) 
					|| PhotoOmniBatchConstants.ROYALTY_MONTHLY.equals(reportTyp)){
				filePath = strRoyaltyFilePath;
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at ReportsUtilBOImpl.getFileSaveLocation() >--> " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting From ReportsUtilBOImpl.getFileSaveLocation() >--> ");
			}
		}
		return filePath;
	}
	
	/**
	 * This method fetch the from email id for specific report id.
	 * @param reportType contains report type.
	 * @return fromEmailId.
	 * @throws PhotoOmniException custom exception.
	 */
	private String getFromEmailId(String reportType) throws PhotoOmniException {
			LOGGER.debug(" Entering getFromEmailId method of ReportsUtilBOImpl ");
			
		String fromEmailId = null;
		try {
			String reportConfigName = null;
			OrdersUtilDAO ordersUtilDAO = omsDAOFactory.getOrdersUtilDAO();
			if (PhotoOmniConstants.PMBYWIC_CUSTOM.equals(reportType) || PhotoOmniConstants.PMBYWIC_MONTHLY.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIl_PM_BY_WIC_REPORT.trim().toUpperCase();
			} else if (PhotoOmniConstants.ROYALTY_CUSTOM.equals(reportType)|| PhotoOmniConstants.ROYALTY_MONTHLY.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIL_ROYALTY_REPORT.trim().toUpperCase();
			} else if (PhotoOmniConstants.SALES_CUSTOM_REPORT.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIl_SALES_REPORT.trim().toUpperCase();
			}
			/*From email id should be only one still anyone by mistake enter 2 email id 
			 * for from below code will send the mail only from the first mail*/
			String emails = ordersUtilDAO.getFromEmailId(reportConfigName).toString();
			String[] email = emails.split(",");
			fromEmailId = email[0];
		} catch (Exception e) {
			LOGGER.error(" Error occoured at deleteFile method of ReportsUtilBOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
				LOGGER.info(" Exiting getFromEmailId method of ReportsUtilBOImpl ");
		}
		return fromEmailId;
	}

}
