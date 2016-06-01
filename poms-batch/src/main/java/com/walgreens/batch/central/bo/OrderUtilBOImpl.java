/**
 * 
 */
package com.walgreens.batch.central.bo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.EmailReportBean;
import com.walgreens.batch.central.bean.KronosDataBean;
import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bean.PLUReportPrefDataBean;
import com.walgreens.batch.central.bean.PMBYWICReportPrefDataBean;
import com.walgreens.batch.central.bean.POFDatFileDataBean;
import com.walgreens.batch.central.bean.POFIrsFileDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bean.StoreOpenCloseDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.dao.OrdersUtilDAO;
import com.walgreens.batch.central.factory.BatchOmsDAOFactory;
import com.walgreens.batch.central.provider.EmailReportDataProvider;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * @author CTS
 * @version 1.1 March 04, 2015
 */
@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
@Component("OrderUtilBO")
public class OrderUtilBOImpl implements OrderUtilBO {

	/*public SendMailService sendMailService;*/

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private BatchOmsDAOFactory omsDAOFactory;

	@Value( "${pof.work.folder.path}" )
	private String pofFolderLocation;

	@Value( "${lc.work.folder.path}" )
	private String licenseContentFolderLocation;

	@Value( "${ps.work.folder.path}" )
	private String printSignFolderLocation;

	@Value( "${plu.work.folder.path}" )
	private String pluFolderLocation;


	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderUtilBOImpl.class);

	/**
	 * This method creates the CSV file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData.
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createCSVFile(Object objData, String reportTyp) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering createCSVFile method of OrderUtilBOImpl ");
		}
		Date today = new Date();
		FileWriter fileWriter = null;
		String strAddedPath = null;
		String abspathWithFileNm = null;
		String abspathWithOutFileNm = null;
		File objDirectory = null;
		Long repPrefId = null ;
		String fileLocation = "";
		StringBuffer fileHeader = null;
		StringBuffer fileFilterCriteria = null;
		List<String> fileNameList = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		StringBuilder fullFileName = null;
		try {
			fileHeader = this.getFileNmAndHeader(reportTyp, fileHeader, fileNames);
			fileFilterCriteria = this.getFilterToShowOnReport(objData, fileFilterCriteria, reportTyp);
			fileLocation = this.getFileSaveLocation(reportTyp);
			
			if (objData instanceof LCAndPSReportPrefDataBean) {
				repPrefId = ((LCAndPSReportPrefDataBean) objData).getReportPrefId();
			} else if (objData instanceof LCDailyReportPrefDataBean) {
				repPrefId = ((LCDailyReportPrefDataBean) objData).getReportPrefId();
			} else if (objData instanceof PLUReportPrefDataBean) {
				repPrefId = ((PLUReportPrefDataBean) objData).getReportId();
			}
			String currentDate = CommonUtil.convertDateToString(today, PhotoOmniConstants.DATE_FORMAT_FIFTH);
			for (String fileName : fileNames) {
				fullFileName = new StringBuilder(fileName);
				fullFileName.append(repPrefId);
				fullFileName.append("_");
				fullFileName.append(currentDate);
				fullFileName.append(PhotoOmniConstants.CSV_FILE_EXTENSION);
				strAddedPath =  fileLocation.concat(fullFileName.toString());
				objDirectory = new File(strAddedPath);
				if (objDirectory.exists()) {
					objDirectory.delete();
				}

				if(!objDirectory.isDirectory()) {
					try {
						fileWriter = new FileWriter(strAddedPath);
						if (PhotoOmniConstants.LC_EXCEPTION_REPORT_NAME.equals(fileName)) {
							fileHeader = new StringBuffer();
							fileHeader.append(PhotoOmniConstants.LC_EXCEPTION_HEADER);
						} else if (PhotoOmniConstants.LC_ADHOC_REPORT_NAME.equals(fileName)) {
							fileHeader = new StringBuffer();
							fileHeader.append(PhotoOmniConstants.LC_DAILY_AND_ADHOC_HEADER);
						} else if (PhotoOmniConstants.DAILY_PLU_REPORT.equals(fileName)) {
							fileHeader = new StringBuffer();
							fileHeader.append(PhotoOmniConstants.DAILY_PLU_RPT_HEADER);
							fileHeader.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
						} else if (PhotoOmniConstants.AD_HOC_PLU_REPORT.equals(fileName)) {
							fileHeader = new StringBuffer();
							PLUReportPrefDataBean pluReportPrefDataBean  = (PLUReportPrefDataBean) objData;
							final JSONObject objJson = new JSONObject(pluReportPrefDataBean.getFilterState());
							final String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							final String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
									PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
							String strObjStartDate = "Start Date " +","+ startDate;
							String strObjEndDate = "End Date" +","+ endDate;
							fileHeader.append(strObjStartDate);
							fileHeader.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
							fileHeader.append(strObjEndDate);
							fileHeader.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
							fileHeader.append(PhotoOmniConstants.DAILY_PLU_RPT_HEADER);
							fileHeader.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
						}
						if (!CommonUtil.isNull(fileFilterCriteria) && !fileFilterCriteria.equals("")) {
							fileWriter.append(fileFilterCriteria); 
						}
						fileWriter.append(fileHeader); 
					} catch (IOException e) {
						LOGGER.error(" Error occoured at createCSVFile method of OrderUtilBOImpl - ", e);
						throw new PhotoOmniException(e.getMessage());
					} finally { 
						try { 
							fileWriter.flush(); 
							fileWriter.close(); 
						} catch (IOException e) { 
							LOGGER.error(" Error occoured at createCSVFile method of OrderUtilBOImpl - ", e);
							throw new PhotoOmniException(e.getMessage());
						} 
					} 
				}
				fileNameList.add(objDirectory.getName());

				if (objData instanceof LCAndPSReportPrefDataBean) {
					((LCAndPSReportPrefDataBean) objData).setFileNameList(fileNameList);
				} else if (objData instanceof LCDailyReportPrefDataBean) {
					((LCDailyReportPrefDataBean) objData).setFileNameList(fileNameList);
				} else if (objData instanceof PLUReportPrefDataBean) {
					((PLUReportPrefDataBean) objData).setFileNameList(fileNameList);
				}
			}
			abspathWithFileNm = objDirectory.getAbsolutePath();
			int indexVal = abspathWithFileNm.lastIndexOf("/");
			abspathWithOutFileNm = abspathWithFileNm.substring(0, (indexVal+1));

			if (objData instanceof LCAndPSReportPrefDataBean) {
				((LCAndPSReportPrefDataBean) objData).setFileLocation(abspathWithOutFileNm);
			} else if (objData instanceof LCDailyReportPrefDataBean) {
				((LCDailyReportPrefDataBean) objData).setFileLocation(abspathWithOutFileNm);
			} else if (objData instanceof PLUReportPrefDataBean) {
				((PLUReportPrefDataBean) objData).setFileLocation(abspathWithOutFileNm);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at createCSVFile method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting createCSVFile method of OrderUtilBOImpl ");
			}
		}

		return  objData;
	}

	/**
	 * This method used to create the filter condition on report
	 * @param objData contains data.
	 * @param reportTyp contains report type.
	 * @return fileFilterCriteria.
	 * @throws PhotoOmniException custom exception. 
	 */
	private StringBuffer getFilterToShowOnReport(Object objData, StringBuffer fileFilterCriteria, String reportTyp) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFilterToShowOnReport method of OrderUtilBOImpl for Pay On Fulfillement step 5");
		}
		try {
			if (PhotoOmniConstants.PRINT_SIGN_REPORT.equals(reportTyp) && objData instanceof LCAndPSReportPrefDataBean) {
				JSONObject objJson = new JSONObject(((LCAndPSReportPrefDataBean) objData).getFilterState());
				String startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
				String endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
						PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
				fileFilterCriteria = new StringBuffer();
				fileFilterCriteria.append(" Filters : , ");
				fileFilterCriteria.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR); 
				fileFilterCriteria.append(" From date : , ");
				fileFilterCriteria.append(startDate);
				fileFilterCriteria.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
				fileFilterCriteria.append(" To date : , ");
				fileFilterCriteria.append(endDate);
				fileFilterCriteria.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
				fileFilterCriteria.append(PhotoOmniBatchConstants.NEW_LINE_SEPARATOR);
			}
		} catch (JSONException e) {
			LOGGER.error(" Error occoured at getFilterToShowOnReport method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getFilterToShowOnReport method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFilterToShowOnReport method of OrderUtilBOImpl ");
			}
		}
		return fileFilterCriteria;
	}

	/**
	 * This method initialize the file location value.
	 * @param reportTyp contains report type value.
	 * @return filePath contains file path.
	 * @throws PhotoOmniException custom exception. 
	 */
	private String getFileSaveLocation(String reportTyp) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFileSaveLocation method of OrderUtilBOImpl for Pay On Fulfillement step 5");
		}
		String filePath = null;
		try {
			if (reportTyp.equals(PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT) 
					|| reportTyp.equals(PhotoOmniConstants.LC_DAILY_REPORT)) {
				filePath = licenseContentFolderLocation;
			} else if (reportTyp.equals(PhotoOmniConstants.PRINT_SIGN_REPORT)) {
				filePath = printSignFolderLocation;
			} else if(PhotoOmniConstants.PLU_DAILY.equals(reportTyp) || PhotoOmniConstants.PLU_ADHOC.equals(reportTyp)){
				filePath = pluFolderLocation;
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getFileSaveLocation method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFileSaveLocation method of OrderUtilBOImpl ");
			}
		}
		return filePath;
	}

	/**
	 * This method creates the Dat file.
	 * @param objData contains object data value.
	 * @param  reportTyp contains report type value.
	 * @return objData
	 * @exception PhotoOmniException custom exception.
	 */
	public Object createDatOrIrsFile(Object objData, String reportTyp) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering createDatOrIrsFile method of OrderUtilBOImpl for Pay On Fulfillement step 5");
		}

		String strAddedPath = null;
		String abspathWithFileNm = null;
		String abspathWithOutFileNm = null;
		File objDirectory = null;

		StringBuffer fileHeader = new StringBuffer();
		List<String> fileNameList = new ArrayList<String>();
		List<String> fileNames = new ArrayList<String>();
		try{
			this.getFileNmAndHeader(reportTyp, fileHeader, fileNames);
			for (String fileName : fileNames) {
				if (PhotoOmniConstants.DAT_FILE.equals(reportTyp)) {
					strAddedPath =  pofFolderLocation.concat(fileName).concat(PhotoOmniConstants.DAT_FILE_EXTENSION);
				} else if (PhotoOmniConstants.IRS_FILE.equals(reportTyp)) {
					strAddedPath =  pofFolderLocation.concat(fileName).concat(PhotoOmniConstants.IRS_FILE_EXTENSION);
				}

				objDirectory = new File(strAddedPath);
				/*if (objDirectory.exists()) {
					 objDirectory.delete();
				 } */
				fileNameList.add(objDirectory.getName());
				if (PhotoOmniConstants.DAT_FILE.equals(reportTyp)) {
					if (objData instanceof POFDatFileDataBean) {
						((POFDatFileDataBean) objData).setFileNameList(fileNameList);

					}
				} else if (PhotoOmniConstants.IRS_FILE.equals(reportTyp)) {
					if (objData instanceof POFIrsFileDataBean) {
						((POFIrsFileDataBean) objData).setFileNameList(fileNameList);

					}
				}
			}
			abspathWithFileNm = objDirectory.getAbsolutePath();
			int indexVal = abspathWithFileNm.lastIndexOf('/');
			//int indexVal = abspathWithFileNm.lastIndexOf('\\');
			abspathWithOutFileNm = abspathWithFileNm.substring(0, (indexVal+1));
			if (PhotoOmniConstants.DAT_FILE.equals(reportTyp)) {
				if (objData instanceof POFDatFileDataBean) {
					((POFDatFileDataBean) objData).setFileLocation(abspathWithOutFileNm);
				} 
			} else if (PhotoOmniConstants.IRS_FILE.equals(reportTyp)) {
				if (objData instanceof POFIrsFileDataBean) {
					((POFIrsFileDataBean) objData).setFileLocation(abspathWithOutFileNm);
				} 
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at createCSVFile method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting createCSVFile method of OrderUtilBOImpl ");
			}
		}

		return  objData;

	}


	/**
	 * This method checks what type of report is selected and 
	 * generate file name and header according to the file type.
	 * @param reportTyp contains report type.
	 * @param fileHeader contains file header.
	 * @param fileNames contains file names.
	 * @return fileHeader.
	 * @exception PhotoOmniException custom exception.
	 */
	private StringBuffer getFileNmAndHeader(String reportTyp, StringBuffer fileHeader, List<String> fileNames) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFileNmAndHeader method of OrderUtilBOImpl ");
		}
		try {
			fileHeader = new StringBuffer();
			if (PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT.equals(reportTyp)) {
				fileNames.add(PhotoOmniConstants.LC_ADHOC_REPORT_NAME);
				fileNames.add(PhotoOmniConstants.LC_EXCEPTION_REPORT_NAME);

			} else if (PhotoOmniConstants.LC_DAILY_REPORT.equals(reportTyp)) {
				fileNames.add(PhotoOmniConstants.LC_DAILY_REPORT_NAME);
				fileHeader.append(PhotoOmniConstants.LC_DAILY_AND_ADHOC_HEADER);

			} else if (PhotoOmniConstants.PRINT_SIGN_REPORT.equals(reportTyp)) {
				fileNames.add(PhotoOmniConstants.PRINT_SIGNS_FILE_NAME);
				fileHeader.append(PhotoOmniConstants.PRINT_SIGNS_CSV_FILE_HEADER);

			} else if (PhotoOmniConstants.PLU_DAILY.equals(reportTyp)) {
				fileNames.add(PhotoOmniConstants.DAILY_PLU_REPORT);

			} else if (PhotoOmniConstants.PLU_ADHOC.equals(reportTyp)) {
				fileNames.add(PhotoOmniConstants.AD_HOC_PLU_REPORT);

			} else if (PhotoOmniConstants.DAT_FILE.equals(reportTyp)) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
				Date date = new Date();
				String currentDate[] = sdf.format(date).split("/");
				fileNames.add("EDI_VEND_PIC_"+currentDate[2]+currentDate[1]+currentDate[0]);
				fileHeader.append(PhotoOmniConstants.POF_DAT_FILE_HEADER);
			} else if (PhotoOmniConstants.IRS_FILE.equals(reportTyp)) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
				Date date = new Date();
				String currentDate[] = sdf.format(date).split("/");
				fileNames.add("EDI_VEND_PIC_"+currentDate[2]+currentDate[1]+currentDate[0]);
				fileHeader.append(PhotoOmniConstants.POF_IRS_FILE_HEADER);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getFileNmAndHeader method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFileNmAndHeader method of OrderUtilBOImpl ");
			}
		}
		return fileHeader;
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
	@Override
	public MimeMessage getMimeMessage(JavaMailSender mailSender,Object dataBean, String mailFrom, String reportType) 	throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getMimeMessage method of OrderUtilBOImpl ");
		}
		String addedDateMessage = null;
		String strEmailIDsTo = "";
		MimeMessage message = null;
		try {
			
			message = mailSender.createMimeMessage();

			if (dataBean instanceof StringBuffer) {  // Pay on Fulfillment
				strEmailIDsTo = dataBean.toString();
			}

			String[] emailArrayTo = strEmailIDsTo.split(",");
			addedDateMessage = this.getSubjectorMessageForEmail(dataBean, reportType);
			
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailFrom);
			helper.setTo(emailArrayTo);
			helper.setSubject(addedDateMessage);

			if (PhotoOmniConstants.PAY_ON_FULLFILLMENT.equals(reportType)) {
				helper.setText(String.format((this.getTextMessageForPOF())));
			} 
			

		} catch (MessagingException e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl PayOnFulfillment Email sending- ", e);
			throw new PhotoOmniException(e.getMessage());
		}  catch (ClassCastException e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl PayOnFulfillment Email sending- ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl    PayOnFulfillment Email sending - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getMimeMessage method of OrderUtilBOImpl  PayOnFulfillment Email sending");
			}
		}
		return message;
	}


	/**
	 * This method creates the sendMailService object and find the to mail ids.
	 * @param mailSender contains mailSender value.
	 * @param dataBean contains dataBean value.
	 * @param reportType contains objData reportType.
	 * @return sendMailService.
	 * @throws PhotoOmniException custom exception.
	 */
	public MimeMessage getMimeMessage(JavaMailSender mailSender, Object dataBean, String reportType) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getMimeMessage method of OrderUtilBOImpl ");
		}
		String addedDateMessage = null;
		JSONObject objJson = null;
		String strEmailIDs = "";
		String fromEmailID = "";
		MimeMessage message = null;
		List<String> fileNames = null;
		String fileLocation = null;
		try {
			FileSystemResource file = null;
			String users = PhotoOmniConstants.TARGET_RECIPENT_PERSONS;
			String content = PhotoOmniConstants.EMAIL_MESSAGE_CONTENT;
			message = mailSender.createMimeMessage();

			if (dataBean instanceof LCAndPSReportPrefDataBean) {
				objJson = new JSONObject(((LCAndPSReportPrefDataBean) dataBean).getFilterState());
				fileLocation = ((LCAndPSReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((LCAndPSReportPrefDataBean) dataBean).getFileNameList();
			} else if (dataBean instanceof LCDailyReportPrefDataBean) {
				objJson = new JSONObject(((LCDailyReportPrefDataBean) dataBean).getFilterState());
				fileLocation = ((LCDailyReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((LCDailyReportPrefDataBean) dataBean).getFileNameList();
			} else if (dataBean instanceof PLUReportPrefDataBean) {
				objJson =  new JSONObject(((PLUReportPrefDataBean) dataBean).getFilterState());
				fileLocation = ((PLUReportPrefDataBean) dataBean).getFileLocation();
				fileNames = ((PLUReportPrefDataBean) dataBean).getFileNameList();
			} else if (dataBean instanceof StringBuffer) {  // Pay on Fulfillment
				strEmailIDs = dataBean.toString();
			}
			
			fromEmailID = this.getFromEmailId(reportType);
			
			if (!CommonUtil.isNull(objJson)) {
				strEmailIDs = objJson.getString("emailIds");
			}
			String[] emailArray = strEmailIDs.split(",");

			if(reportType.equals(PhotoOmniConstants.PLU_DAILY)) {
				addedDateMessage = "PLU report is generated for the Date " + 
						new SimpleDateFormat("MM-dd-yyyy").format(new Date(System.currentTimeMillis()-24*60*60*1000)).toString();
			} else {
				addedDateMessage = this.getSubjectorMessageForEmail(dataBean, reportType);
			}
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(fromEmailID);
			helper.setTo(emailArray);
			helper.setSubject(addedDateMessage);

			if (PhotoOmniConstants.PAY_ON_FULLFILLMENT.equals(reportType)) {
				helper.setText(String.format((this.getTextMessageForPOF())));
			} else {
				helper.setText(String.format(users.concat(content).concat(addedDateMessage)));
			}
			if(null!= fileNames){
				for (String fileName : fileNames) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Attaching File path is : "+ fileLocation.concat(fileName));
					}
					file = new FileSystemResource(fileLocation.concat(fileName));
					helper.addAttachment(file.getFilename(), file);
				}
			}

		} catch (MessagingException e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		}  catch (ClassCastException e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (JSONException e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getMimeMessage method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getMimeMessage method of OrderUtilBOImpl ");
			}
		}
		return message;
	}

	/**
	 * This method fetch the from email id for specific report id.
	 * @param reportType contains report type.
	 * @return fromEmailId.
	 * @throws PhotoOmniException custom exception.
	 */
	private String getFromEmailId(String reportType) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFromEmailId method of OrderUtilBOImpl ");
		}
		String fromEmailId = null;
		try {
			String reportConfigName = null;
			OrdersUtilDAO ordersUtilDAO = omsDAOFactory.getOrdersUtilDAO();
			if (PhotoOmniConstants.PRINT_SIGN_REPORT.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIL_PRINTSIGN_REPORT.trim().toUpperCase();
			} else if (PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIL_ADHOC_EXCEPTON_REPORT.trim().toUpperCase();
			} else if (PhotoOmniConstants.LC_DAILY_REPORT.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIL_DAILY_REPORT.trim().toUpperCase();
			} else if (PhotoOmniConstants.PLU_DAILY.equals(reportType) || PhotoOmniConstants.PLU_ADHOC.equals(reportType)) {
				reportConfigName = PhotoOmniConstants.EMAIL_PLU_REPORT.trim().toUpperCase();
			} else {
				//For others(Bangalore team)
			}
			/*From email id should be only one still anyone by mistake enter 2 email id 
			 * for from below code will send the mail only from the first mail*/
			String emails = ordersUtilDAO.getFromEmailId(reportConfigName).toString();
			String[] email = emails.split(",");
			fromEmailId = email[0];
		} catch (Exception e) {
			LOGGER.error(" Error occoured at deleteFile method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFromEmailId method of OrderUtilBOImpl ");
			}
		}
		return fromEmailId;
	}

	/**
	 * This method is used to delete the file.
	 * @param fileNames contains file names.
	 * @param fileLocation contains file location.
	 * @throws PhotoOmniException Custom exception.
	 */
	public void deleteFile(List<String> fileNames, String fileLocation) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering deleteFile method of OrderUtilBOImpl ");
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
			LOGGER.error(" Error occoured at deleteFile method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting deleteFile method of OrderUtilBOImpl ");
			}
		}
	}

	/**
	 * This method add the date rang to E-Mail subject and E-mail Message
	 * @param objVal contains object value.
	 * @return reportType contains report type value.
	 * @exception PhotoOmniException custom exception.
	 */
	private String getSubjectorMessageForEmail(Object objVal, String reportType) throws PhotoOmniException /*throws ReportException*/{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSubjectorMessageForEmail method of OrderUtilBOImpl ");
		}
		StringBuffer addedDateMessage = new StringBuffer();
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
				objJson = new JSONObject(((RoyaltySalesReportPrefDataBean) objVal).getFilterState());
			} else if (objVal instanceof PLUReportPrefDataBean) {
				objJson = new JSONObject(((PLUReportPrefDataBean) objVal).getFilterState());
			} 

			if (PhotoOmniConstants.LC_ADHOC_EXCEP_REPORT.equals(reportType)) {
				mainSub = " License Content Adhoc report and Exceptin report between ";
			} else if (PhotoOmniConstants.LC_DAILY_REPORT.equals(reportType)) {
				mainSub = " License Content Daily report between ";
			} else if (PhotoOmniConstants.PRINT_SIGN_REPORT.equals(reportType)) {
				mainSub = " Printable Signs report between ";
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
				String startDate = "";
				String endDate = "";
				if (PhotoOmniConstants.LC_DAILY_REPORT.equals(reportType)) {
					if (objVal instanceof LCDailyReportPrefDataBean) {
						startDate = ((LCDailyReportPrefDataBean) objVal).getStartDate();
						endDate = ((LCDailyReportPrefDataBean) objVal).getEndDate();
					}
					
				} else {
					startDate = CommonUtil.stringDateFormatChange(objJson.getString("startDate"), 
							PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
					endDate = CommonUtil.stringDateFormatChange(objJson.getString("endDate"), 
							PhotoOmniConstants.DATE_FORMAT_SIX, PhotoOmniConstants.DATE_FORMAT_FIFTH);
				}
				addedDateMessage.append(mainSub);
				addedDateMessage.append(startDate);
				addedDateMessage.append(" to ");
				addedDateMessage.append(endDate);
			} else {
				addedDateMessage.append(mainSub);
			}

		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSubjectorMessageForEmail method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSubjectorMessageForEmail method of OrderUtilBOImpl ");
			}
		}

		return addedDateMessage.toString();
	}



	/**
	 * This method add the Text message for pay on fullfillment E-mail Message
	 * @return txtMessage contains txtMessage value.
	 * @exception PhotoOmniException custom exception.
	 */
	private String getTextMessageForPOF() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getTextMessageForPOF method of OrderUtilBOImpl ");
		}
		String txtMessage = null;
		try{
			txtMessage = "HI , \nTHERE ARE FEW VENDOR PAYMENTS AWAITING YOUR APPROVAL. PLEASE LOGIN TO PHOTOOMNI APPLICATION AND APPROVE THE PAYMENTS. \n"
					+ "\nTHANKS.";
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getTextMessageForPOF method of OrderUtilBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getTextMessageForPOF method of OrderUtilBOImpl ");
			}
		}
		return txtMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.batch.central.bo.OrderUtilBO#getEmailReportDetails(String)
	 */
	@Override
	public EmailReportBean getEmailReportDetails(String reportName) throws PhotoOmniException {
		LOGGER.info(" Entered into OrderUtilBOImpl.getEmailReportDetails() Method",reportName);

		EmailReportBean emailReportBean = new EmailReportBean();
		OrdersUtilDAO ordersUtilDAO = omsDAOFactory.getOrdersUtilDAO();
		String emailType = null, email = null;
		try{
			List<Map<String, Object>> data = ordersUtilDAO.getReportData(reportName);
			for (Map<String, Object> map : data) {
				emailReportBean.setSysReportId(CommonUtil.bigDecimalToLong(map
						.get(PhotoOmniConstants.SYSREPORTID)));
				emailReportBean.setReportName((String) map.get(PhotoOmniConstants.REPORTNAME));
				if(null != map.get(PhotoOmniConstants.EMAIlTYPE) && null !=  map.get(PhotoOmniConstants.EMAILID)){
					emailType = (String) map.get(PhotoOmniConstants.EMAIlTYPE);
					email = (String) map.get(PhotoOmniConstants.EMAILID);
					if (PhotoOmniConstants.FROMID.equalsIgnoreCase(emailType)) {
						if (email != null) {
							emailReportBean.setFrom(email);
						} else {
							LOGGER.info("value is null for {} ", emailType);
						}
					}
					if (PhotoOmniConstants.TOID.equalsIgnoreCase(emailType)) {
						if (email != null) {
							if (null == emailReportBean.getTo()) {
								emailReportBean.setTo(email.split("[\\s,]"));
							} else {
								List<String> arrayList = new ArrayList<String>();
								for (String toEmailId : emailReportBean.getTo()) {
									arrayList.add(toEmailId);
								}
								for (String toEmailId : email.split("[\\s,]")) {
									arrayList.add(toEmailId);
								}
								String[] destArray = new String[arrayList.size()];
								emailReportBean.setTo(arrayList.toArray(destArray));
							}
						} else {
							LOGGER.info("value is null for {} ", emailType);
						}
					}
					if (PhotoOmniConstants.CCID.equalsIgnoreCase(emailType)) {
						if (email != null) {
							if (null == emailReportBean.getCc()) {
								emailReportBean.setCc(email.split("[\\s,]"));
							} else {
								List<String> arrayList = new ArrayList<String>();
								for (String toEmailId : emailReportBean.getCc()) {
									arrayList.add(toEmailId);
								}
								for (String toEmailId : email.split("[\\s,]")) {
									arrayList.add(toEmailId);
								}
								String[] destArray = new String[arrayList.size()];
								emailReportBean.setCc(arrayList.toArray(destArray));
							}

						} else {
							LOGGER.info("value is null for {} ", emailType);
						}
					}
					if (PhotoOmniConstants.BCCID.equalsIgnoreCase(emailType)) {
						if (email != null) {
							if (null == emailReportBean.getBcc()) {
								emailReportBean.setBcc(email.split("[\\s,]"));
							} else {
								List<String> arrayList = new ArrayList<String>();
								for (String toEmailId : emailReportBean.getBcc()) {
									arrayList.add(toEmailId);
								}
								for (String toEmailId : email.split("[\\s,]")) {
									arrayList.add(toEmailId);
								}
								String[] destArray = new String[arrayList.size()];
								emailReportBean.setBcc(arrayList.toArray(destArray));
							}
						} else {
							LOGGER.info("value is null for {} ", emailType);
						}
					}
				}
			}
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Start Loading provider bean for {}", reportName);
			}
			EmailReportDataProvider dataProvider = applicationContext.getBean(
					emailReportBean.getReportName(), EmailReportDataProvider.class);
			if (dataProvider != null) {
				emailReportBean = dataProvider.populateData(emailReportBean);
			} else {
				LOGGER.error("Error while loading provider bean for {}", reportName);
			}
		}catch(PhotoOmniException e){
			LOGGER.error("Exception occured while fetching data at OrderUtilBOImpl.getEmailReportDetails() method", reportName);
			throw e;
		}catch(Exception e){
			LOGGER.error("Exception occured at OrderUtilBOImpl.getEmailReportDetails() method", reportName);
			throw new PhotoOmniException(e.getMessage());
		}finally{
			LOGGER.info("Exiting from OrderUtilBOImpl.getEmailReportDetails() Method",reportName);
		}
		return emailReportBean;
	}
	
	
	/**
	 * This method create the Slot count logic for kronos.
	 * @param kronosDataBean contains data from database.
	 * @param storeOpenCloseDataBeanList contains store open close slot.
	 * @exception PhotoOmniException - custom exception.
	 */
	public void kronosDataFormatting(KronosDataBean kronosDataBean, Map<String, 
			StoreOpenCloseDataBean> storeOpenCloseDataBeanList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering kronosDataFormatting method of OrderUtilBOImpl ");
		}
		try {
			if (!CommonUtil.isNull(kronosDataBean)) { 
				String[] timeGapArray = new String[96];
				int arrayLength = timeGapArray.length;
				Arrays.fill(timeGapArray, 0, arrayLength, "");
				String storeNbr = kronosDataBean.getStoreNumber();
				String matrixValue = kronosDataBean.getMatrixCode();
				String orderCompletedDateTime = kronosDataBean.getDate();
				String dayName = kronosDataBean.getDayName();
				String allSlotsAndCountForStr = kronosDataBean.getSlotAndCount();
				/*Below line plot the store open close slot*/
				Map<String, Integer> openCloseForStore  = this.addOpenCloseTimeToPlot(timeGapArray, storeNbr, dayName, storeOpenCloseDataBeanList);
				if (!CommonUtil.isNull(allSlotsAndCountForStr) && !"".equals(allSlotsAndCountForStr)) {
					String[] allSlotsAndCountForStrArray = allSlotsAndCountForStr.split(",");
					for (String slotCount : allSlotsAndCountForStrArray) {
						if (!CommonUtil.isNull(slotCount) && !"".equals(slotCount)) {
							String[] slotCountArray = slotCount.split("\\$");
							String slot = !CommonUtil.isNull(slotCountArray[0]) && slotCountArray[0].equals("0") ? "96" : slotCountArray[0]; 
							/*Above line For 00:00 time it comes at 0 (Zero) slot, so moved it to last slot which is 96*/
							String count = slotCountArray[1];
							int storeOpenSlot = openCloseForStore.get("OPEN_SLOT");
							int storeCloseSlot = openCloseForStore.get("CLOSE_SLOT");
							if (!CommonUtil.isNull(slot) && !"".equals(slot) && !CommonUtil.isNull(count) && !"".equals(count)
									&& (storeOpenSlot != PhotoOmniConstants.STORE_CLOSED || storeCloseSlot != PhotoOmniConstants.STORE_CLOSED)) {
								/*If order slot comes under Store open close slot, then only order will be placed */
								if (storeCloseSlot >= storeOpenSlot && (Integer.parseInt(slot) >= storeOpenSlot && Integer.parseInt(slot) <= storeCloseSlot)) {
									/*Store open and close same day*/
									timeGapArray[Integer.parseInt(slot)-1] = count;
								} else if (storeCloseSlot < storeOpenSlot && (Integer.parseInt(slot) >= storeOpenSlot && Integer.parseInt(slot) <= arrayLength)
										&& (Integer.parseInt(slot) >= 0 && Integer.parseInt(slot) <= storeCloseSlot)) {
									/*For those store which closed next day-Like open time 07.00 and close time 01.00*/
									timeGapArray[Integer.parseInt(slot)-1] = count;
								}
							}
						}
					}
				}
				String modifiedSlotCnt = Arrays.toString(timeGapArray);
				modifiedSlotCnt = modifiedSlotCnt.replaceAll("[\\[\\]\\s]", "");
				kronosDataBean.setSlotAndCount(modifiedSlotCnt);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" store number : " + storeNbr + " and matrix : " + matrixValue + " Completion date : " + orderCompletedDateTime + " slotCount : " + modifiedSlotCnt );
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at kronosDataFormatting method of OrderUtilBOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting kronosDataFormatting method of OrderUtilBOImpl ");
			}
		}
	}
	
	/**
	 * This method plot the Store open close hour. 
	 * @param timeGapArray contains slots
	 * @param storeNbr contains store no.
	 * @param dayName contains name of the day.
	 * @param storeOpenCloseDataBeanList contains store open close data.
	 * @return openCloseForStore.
	 * @throws PhotoOmniException 
	 */
	private Map<String, Integer> addOpenCloseTimeToPlot(String[] timeGapArray, String storeNbr, String dayName,
			Map<String, StoreOpenCloseDataBean> storeOpenCloseDataBeanList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addOpenCloseTimeToPlot method of OrderUtilBOImpl ");
		}
		Map<String, Integer> openCloseForStore = new HashMap<String, Integer>();
		try {
			int openSlot = 0;
			int closeSlot = 0;
			if (!CommonUtil.isNull(storeOpenCloseDataBeanList) && storeOpenCloseDataBeanList.size() > 0) {
				StoreOpenCloseDataBean storeOpenCloseDataBean = storeOpenCloseDataBeanList.get(storeNbr);
				if (storeOpenCloseDataBean.getIsTwentyFourHourstr() !=1 ) {
					dayName = dayName.trim();
					if ("Sunday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotSun();
						closeSlot = storeOpenCloseDataBean.getCloseSlotSun();

					} else if ("Monday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotMon();
						closeSlot = storeOpenCloseDataBean.getCloseSlotMon();

					} else if ("Tuesday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotTue();
						closeSlot = storeOpenCloseDataBean.getCloseSlotTue();

					} else if ("Wednesday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotWed();
						closeSlot = storeOpenCloseDataBean.getCloseSlotWed();

					} else if ("Thursday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotThus();
						closeSlot = storeOpenCloseDataBean.getCloseSlotThus();

					} else if ("Friday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotFri();
						closeSlot = storeOpenCloseDataBean.getCloseSlotFri();

					} else if ("Saturday".equalsIgnoreCase(dayName)) {
						openSlot = storeOpenCloseDataBean.getOpenSlotSat();
						closeSlot = storeOpenCloseDataBean.getCloseSlotSat();
					}
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Open Close slot for Store number : " + storeNbr + " is " + openSlot + " and " + closeSlot + " on " + dayName);
					}
					//Fix Given jeera - 594
					if (openSlot > 0) {
						openSlot++;
					}
					if (openSlot != PhotoOmniConstants.STORE_CLOSED || closeSlot != PhotoOmniConstants.STORE_CLOSED) {
						if (closeSlot < openSlot-1) {
							/*For those store which closed next day-Like open time 07.00 and close time 01.00*/
							Arrays.fill(timeGapArray, openSlot-1, timeGapArray.length, "0");
							Arrays.fill(timeGapArray, 0, closeSlot, "0");
						} else {
							Arrays.fill(timeGapArray, openSlot-1, closeSlot, "0");
						}
						openCloseForStore.put("OPEN_SLOT", openSlot);
						openCloseForStore.put("CLOSE_SLOT", closeSlot);
					}
				} else {
					/*For 24 hours open stores*/
					openSlot = 0;
					closeSlot = timeGapArray.length;
					Arrays.fill(timeGapArray, openSlot, closeSlot, "0");
					openCloseForStore.put("OPEN_SLOT", openSlot);
					openCloseForStore.put("CLOSE_SLOT", closeSlot);
				}
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Store number : " + storeNbr + " is a 24 hours store. ");
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addOpenCloseTimeToPlot method of OrderUtilBOImpl - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addOpenCloseTimeToPlot method of OrderUtilBOImpl ");
			}
		}
		return openCloseForStore;
	}
	
}
