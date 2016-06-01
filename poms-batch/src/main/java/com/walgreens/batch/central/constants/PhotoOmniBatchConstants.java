/**
 * 
 */
package com.walgreens.batch.central.constants;

/**
 * @author CTS
 *
 */
public interface PhotoOmniBatchConstants {
	
	public static final String CANCEL = "CANCEL";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String BLANK = " ";
	/*public static final String BY_QUANTITY = "By Quantity";
	public static final String BY_ORDER = "By Order";
	public static final String BY_SETS = "By Sets";
	public static final String GRADUATED = "Graduated";
	public static final String STRAIGHT = "Straight";
	public static final String ORDER_PROC= "PROC";
	public static final String ORDER_SOLD ="SOLD";
	public static final String ORDER_DONE = "DONE";
	public static final String ORDER_COMPLETE = "COMPLETE";
	public static final String ORDER_CANCEL = "CANCEL";*/ 
	/*public static final String PM_STATUS_P = "P"; 
	public static final String PM_STATUS_E = "E"; 
	public static final String PM_STATUS_T = "T"; 
	public static final String PM_STATUS_R = "R"; 
	public static final String ORDER_INTERNET = "INTERNET"; 
	public static final String ORDER_KIOSK = "KIOSK"; */
	//public static final String  ORDER_PM ="PM";
	public static final String PM_WIC_CSV_FILE_HEADER = "WIC,Product Description,Calculated Retail,Sales (Amount Paid),Amount of PM Paid,# of orders,Total Quantity,Item Cost,Gross Profit";
	public static final String PRINT_SIGNS_CSV_FILE_HEADER = "STORE#, EVENT NAME, QUANTITY";
	public static final String CSV_File_Path ="G://PictureCarePlus//lakshmipathi//csvFiles//PMByWICReport_";
	public static final String COMMA_DELIMITER = ","; 
	public static final String NEW_LINE_SEPARATOR = "\n"; 
	public static final String TARGET_RECIPENT_PERSONS = " Hi Business users,";
	public static final String EMAIL_MESSAGE_CONTENT = "  \n \t Please find the attached document for ";
	public static final String PRINT_SIGNS_FILE_NAME = "PrintSignsReport_";
	public static final String LC_DAILY_REPORT = "lcdailyreport";
	public static final String PRINT_SIGN_REPORT = "printsignreport";
	public static final String LC_ADHOC_EXCEP_REPORT = "lcadhocandexceptionreport";
	public static final String LC_ADHOC_REPORT_NAME = "licenseContentAdhocReport_";
	public static final String LC_EXCEPTION_REPORT_NAME = "licenseContentExceptionReport_";
	public static final String LC_DAILY_REPORT_NAME = "licenseContentDailyReport_";
	public static final String LC_DAILY_AND_ADHOC_HEADER = "Order Date, Quantity, Provider, Location Type, Location Number, Calculated Retail Price, Discount Applied, Employee Discount, Net Sale, Product Description, UPC, WIC, License Content ID/Template ID, Template ID, Envelope Number, Original Retail Price, Order Status";
	public static final String LC_EXCEPTION_HEADER = "Store #, Order ID, Product ID, Product Description, Exception Date, Exception Type, Exception Description, Remarks, Date time created";
	public static final String DATE_FORMAT_THREE = "yyyy-MM-dd hh:mm:ss";
	public static final String DATE_FORMAT_SIX = "yyyy-MM-dd";
	public static final String DATE_FORMAT_FORTH = "MM-dd-yyyy hh:mm:ss";
	public static final String DATE_FORMAT_FIFTH = "MM-dd-yyyy";
	public static final String DATE_FORMAT_TWO = "dd-MM-yyyy";
	public static final String CSV_FILE_EXTENSION = ".csv";
	public static final String DAT_FILE_EXTENSION = ".dat";
	public static final String IRS_FILE_EXTENSION = ".irs";
	public static final String ZIP_FILE_EXTENSION = ".zip";
	public static final String ADHOC_REPORT_COLUMN  = "AdhocAndExceptionReport";
	public static final String DAILY_REPORT_COLUMN = "DailyReport";
	public static final String PRINT_SIRN_REPORT_COLUMN = "PrintSignReport";
	public static final String CSV_FILE_SIZE_BYTE = "10";
	//public static final String  ORDER_MBPM ="MBPM";
	public static final String ROYALTY_REPORT_FILE_HEADER = "Template ID,Product Name, Template Category 1, Template Category 2, # of Prints, Sold Amount";
	public static final String CSV_TEMP_FILE_PATH="G://PictureCarePlus//lakshmipathi//csvFiles//";
	public static final String ROYALTY_REPORT_CUSTOM_FILE_NAME = "RoyaltyCustomReport_";
	public static final String ROYALTY_REPORT_MONTHLY_FILE_NAME = "RoyaltyMonthlyReport_";
	public static final String PM_BY_WIC_REPORT_CUSTOM_FILE_NAME = "PMByWICCustomReport_";
	public static final String PM_BY_WIC_REPOT_MONTHLY_NAME = "PMByWICMonthlyReport_";
	public static final String ROYALTY_CUSTOM = "royaltyCustomReport";
	public static final String ROYALTY_MONTHLY = "royaltyMonthlyReport";
	public static final String PMBYWIC_CUSTOM = "pmBYWICCustomReport";
	public static final String PMBYWIC_MONTHLY = "pmByWICMonthlyReport";
	public static final int ACTIVE_IND_PROC = 1;
	public static final int ACTIVE_IND_DONE = 0;
	public static final String POF_STATUS_IND_N = "N";
	public static final String POF_STATUS_IND_D = "D";
	public static final String POF_STATUS_IND_V = "V";
	public static final String POF_STATUS_IND_I = "I";
	public static final String POF_STATUS_IND_Y = "Y";
	public static final String POF_STORE_TYPE_S = "S";
	public static final String POF_EMAIL_SENT_IND ="N";
	public static final String CREATE_USER_ID = "system";
	public static final String CREATE_DTTM = "SYSDATE";
	public static final String UPDATE_USER_ID = "system";
	public static final String  UPDATE_DTTM = "SYSDATE";
	public static final String  POF ="POF";
	public static final String LC_AND_PS_FILE_LOCATION = "C:/Users/kumarxan/Test/";
	public static final String SALES_PRODUCT_RPT_CSVPATH = "G://PictureCarePlus//Deepak_Blr_backpup//csvFiles//SalesReportByProduct_";
	public static final String SALES_PRODUCT_RPT_HEADER= "TEMPLATE_ID,CATEGORY,DESCRIPTION,OUTPUT_SIZE,VENDOR,COUNT,QUANTITY,AMOUNT_PAID,CALCULATED_RETAIL,ORIGINAL_RETAIL,ORDER_COST,TOTAL_DISCOUNT_AMT";
	
	public static final String SUMMARY = "Summary";
	public static final String XLS_FILE_EXTENSION = ".xlsx";
	public static final String ROYALTY_REPOR_SUMMARY_HEADER = "Template ID,Product Name, # of Prints, Sold Amount";
	public static final String XLS_TEMP_FILE_PATH="C://Users//alamna//Desktop//CSVFile//";
	public static final String PAY_ON_FULLFILLMENT = "payonfullfillment";
	public static final String DAT_FILE = "datfile";
	public static final String IRS_FILE = "irsfile";
	
	/**
	 * SFTP Credentials
	 */
	public static final String SFTP_USER = "";
	public static final String SFTP_PASSWORD = "";
	public static final String SFTP_HOST = "";
	public static final String SFTP_PORT = "";
	public static final String SFTP_TXT_FILE_REMOTE_LOCATION = "";
	public static final String SFTP_TXT_FILE_LOCATION = "C:\\Users\\mannasa\\Desktop\\San\\WORK";
	public static final String SFTP_TXT_FILE_NAME = "";
	public static final String ARCHIVE_FOLDER_PATH ="C:\\Users\\mannasa\\Desktop\\San\\ARCHIVE";
	public static final String  DAYS_BACK = "30"; 
	
	/**
	 * Filter constants
	 */
	public static final String EMAIL_IDS = "emailIds";
	public static final String START_DATE = "startDate";
	public static final String END_DATE = "endDate";
	public static final String FROM = "FROM:";
	public static final String TO = "TO:";
	public static final String SUMMARY_GET_METHODS = "getTemplateId,getProductName,getNumberOfPrints,getSoldAmount";
	public static final String ROYALTY_DATA_HEADER = "Store Number,TemplateId ,Product Name ,Template Name ,Vendor, # Of Orders , # Of Prints, AmountSold";
	public static final String VENDOR = "vendorName";
	public static final String PRODUCT_TYPE = "ProductTp";
	public static final String PRINT_SIZE = "PrintSize";
	public static final String ROYALTY_REPORT = "RoyaltyReport";
	public static final String SALES_REPORT_CUSTOM_FILE_NAME = "SalesCustomReportByProduct_";
	public static final String SALES_REPORT_MONTHLY_FILE_NAME = "SalesMonthlyReportByProduct_";
	public static final String SALES_REPORT_FILE_HEADER = "TEMPLATE_ID ,CATEGORY ,DESCRIPTION ,OUTPUT_SIZE ,VENDOR ,COUNT ,QUANTITY ,AMOUNT_PAID ,"
			+ "CALCULATED_RETAIL ,ORIGINAL_RETAIL ,ORDER_COST ,TOTAL_DISCOUNT_AMT ";
	public static final String SALES_CUSTOM_REPORT = "salesCustomReport";
	public static final String SALES_MONTHLY_REPORT = "salesMonthlyReport";
	public static final String WEEKLY = "WEEKLY";
	public static final String MONTHLY = "MONTHLY";
	public static final String QUATERLY = "QUARTERLY";
	public static final String YEARLY = "YEARLY";
	public static final String DAILY = "DAILY";
	public static final String ROYALTY_SALES_GEN_TYPE = "ROYALTY_SALES_GEN_TYPE";
	public static final String ROYALTY_REPORT_GENERATION_DAY = "ROYALTY_REPORT_GENERATION_DAY";
	public static final String ROYALTY_REPORT_GEN_QUARTER = "ROYALTY_REPORT_GEN_QUARTER";
	public static final String 	ROYALTY_REPORT_GEN_MONTH = "ROYALTY_REPORT_GEN_MONTH";
	public static final String EMPTY_SPACE_CHAR = "";
	public static final String ROYALTY_REPORT_FILE_NAME = "RoyaltyTemplateSalesReport_";
	public static final String SALES_REPORT_BY_PRODUCT = "SalesReport";
	public static final String SALES_REPORT_FILE_NAME = "SalesReportByProductReport_";
	public static final String PRODUCTTYPE = "ProductType";
	public static final String PRINTSIZE = "PrintSize";	
	
	/**Pos Batch constants starts*/
	public static final String DATE_FORMAT_POS_ONE = "dd-MMM-yy";
	public static final String POS_ORDER_STATUS_SOLD = "SOLD";
	public static final String POS_EXCEPTION_NOTES_ONE = "Exception created for : Priced for Free";
	public static final String POS_EXCEPTION_NOTES_TWO = "Exception created for : Sold Amount is less than Original Calculated Amount";
	public static final String POS_EXCEPTION_NOTES_THREE = "Exception created for : EmployeeDiscountFlag is set to true";
	public static final String POS_EXCEPTION_NOTES_FOUR = "Exception created for : prints returned > 0";
	public static final String POS_EXCEPTION_NOTES_FIVE = "Exception created for : Sold Free";
	public static final String POS_TRAN_TYPE_MS = "MS";
	public static final String POS_TRAN_TYPE_MV = "MV";
	public static final String POS_TRAN_TYPE_MR = "MR";
	public static final String POS_TRAN_TYPE_MA = "MA";
	public static final String POS_TRAN_TYPE_SR = "SR";
	public static final String POS_EXCEPTION_TYPE_SOLD_FOR_FREE = "SOLD FOR FREE";
	public static final String POS_EXCEPTION_TYPE_PRICE_MODIFY = "PRICE MODIFY";
	public static final String POS_EXCEPTION_TYPE_EMPLOYEE_DISCOUNT = "EMPLOYEE DISCOUNT";
	public static final String POS_EXCEPTION_TYPE_REFUSED = "REFUSED";
	public static final String POS_EXCEPTION_TYPE_REFUSED_REASON = "POS - Refused";
	/**Pos Batch constants ends*/
	
	public static final String ONE_HOUR_PRODUCT_IND = "H";
	public static final String SEND_OUT_PRODUCT_IND = "S";
	public static final String COST_CALC_PROC_IND = "P";
	public static final String QUANTITY = "Q";
	public static final String PMByWIC_REPORT = "pmbywic";
	
	public static final String PRECESSION = "999990.00";
	public static final String PRODUCT_CATEGORY_TYPE = "MASTERGRP";
	public static final String GIFTS = "Gifts";
	public static final String  HYPHEN = "-";
	public static final String TOTAL = "TOTAL";
	public static final String CATEGORY = "Category";
	
	/**
	 * Phase 2 Constants
	 */
	public static final String DATE_FORMAT_ONE = "yyyyMMdd";
	public static final String RENAMED_KPI_FEED_FILE_NAME_PATTERN = "eXact_PROCESSED_PCPPlus_Store_";		
	public static final String END_WITH_RENAMED_KPI_FEED_FILE_NAME_PATTERN = ".txt.eXact_PROCESSED";		
	public static final String KPI_FEED_FILE_NAME_PATTERN = "PCPPlus_Store_";		
	public static final String STORE_WISE = "storewise";		
	public static final String ALL_STORE = "allstore";		
			
	public static final String PHALCSDT = "PHALCSDT";		
	public static final String PHALTOS = "PHALTOS";		
	public static final String PHALCCDT = "PHALCCDT";		
	public static final String PHALDPMS = "PHALDPMS";		
	public static final String PHALDPMP = "PHALDPMP";		
	public static final String PHALPTOH = "PHALPTOH";		
	public static final String PHALCTOH = "PHALCTOH";		
	public static final String PHALPINT = "PHALPINT";		
	public static final String PHALCINT = "PHALCINT";		
	public static final String PHALPPTA = "PHALPPTA";		
	public static final String PHALPPTB = "PHALPPTB";		
	public static final String PHALPPTC = "PHALPPTC";		
	public static final String PHALCNOT = "PHALCNOT";		
	public static final String PHALPNOT = "PHALPNOT";		
			
	public static final String STAT_ID = "statId";		
	public static final String SAMPLE_VALUE = "sampleValue";		
	public static final String STORE_NO = "storeNo";		
	public static final String BUSSINESS_DATE = "bussinessDate";		
	public static final String SAMPLE_SIZE = "sampleSize";		
	public static final String IS_VALID_PAIR = "isValidPair";		
			
	public static final String OM_KPI_POS_PM_TRANSACTION = "OM_KPI_POS_PM_TRANSACTION";		
	public static final String OM_KPI_ORDER_TRANSACTION = "OM_KPI_ORDER_TRANSACTION";		
			
	public static final String KPI_INTEGRATION_JOB = "KPIIntegrationJob";
	
	public static final String RENAMED_TIME_ATTENDANCE_FEED_FILE_NAME_PATTERN = "EXACT";
}

