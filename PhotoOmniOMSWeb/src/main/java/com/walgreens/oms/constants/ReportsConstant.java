package com.walgreens.oms.constants;

import java.util.Date;

public interface ReportsConstant {
	
	/**
	 * Pm Report Constatns
	 */

	String EMAIL_IDS = "emailIds";
	String FILTER = "filter";
	String RESPONSE_MESSAGE =  "responseMessage";
	String MESSAGE_HEADER = "messageHeader";
	String STATUS = "status";
	String DELIMITER = ",";
	String DOMAIN_NAME = "@walgreens.com";
	String START_DATE = "startDate";
	String END_DATE = "endDate";
	String USER_ID = "userId";
	String REPORT_ID = "reportId";
	String PREFERENCE_TYPE  = "preferenceType";
	String FILTER_STATE = "filterState";
	String AUTO_REFRESH_IND = "autoRefreshInd";
	String AUTO_EXECUTE_IND = "autoExecuteInd";
	String AUTO_EXECUTE_INT = "autoExecuteInt";
	String ACTIVE_CD = "activeCd";
	String CRATED_BY = "createdBy";
	String UPDATED_BY = "updatedBy";
	String FILTER_ENABLE_IND = "filterEnableInd";
	String BATCH = "BATCH";
	String Y_INDICATOR = "Y";
	String N_INDICATOR = "N";
	int INDICATOR = 1;
	String PMBYWIC = "pmbywic";
	String APP_ID = "appID";
	String COMMAND_NAME = "commandName";
	String COMMAND_VERSION = "commandVersion";
	String MESSAGE_TIME_STAMP = "msgSentTimestamp";
	String ORIGIN = "origin";
	String TRANSACTION_ID = "transactionID";
	String ERRORDETAILS = "errorDetails";
	String ERROR_CODE_STRING = "errorCode";
	String ERROR_SOURCE = "errorSource";
	String ERROR_STRING = "errorString";
    String DESCRIPTION = "description";
    String USER_NAME_PATTERN = "^[a-zA-Z]+[.*]+[a-zA-Z]+$";
	public static final int KIOSK_PAGE_SIZE = 10;
    public static final int MACHINE_DOWNTIME_STORE_PAGE_SIZE = 5;
	public static final String CHAIN_COLUMN = "NO COLUMN";
	public static final String REGION_COLUMN = "REGION_NUMBER";
	public static final String DISTRICT_COLUMN = "DISTRICT_NUMBER";
	public static final String STORE_COLUMN = "LOCATION_NUMBER";
	public static final String FIRST_PAGE_NO = "1";
	public static final Date CURRENT_DATE = new Date();
	public static final String FILTER_DATA_SAVE_SUCCESS_MSG = " Filter data saved successfully ";
	public static final String FILTER_DATA_SAVE_ERROR_MSG = " Filter data not saved sucessfully! Please try Again ";
	public static final String EMAIL_ID_PATTERN = "@walgreens.com";
	public static final int INDICATOR_YES = 1;
	public static final int INDICATOR_NO = 0; 
	public static final int ADHOC_EXCEPTION_REPORT_ID = 5;
	public static final int PRINT_SIGN_REPORT_ID = 7;
	public static final int DAILY_REPORT_ID = 6;
	public static final String USER_NAME = "userName";
	public static final String ROYALTY = "RoyaltyReport";
	public final static String splitExpression = ",";
	public static final String SALESREPORT = "SalesReport";
	public static final String PLUNUMBER = "pluNumber";
	public static final String SUCCESS_MESSAGE = "submitted successfully";
	public static final String FAILURE_MESSAGE = "failed";
	String PM_BY_EMP_REPORT_NAME = "pmbyemployee";
	public static final String PLU_REPORT = "PluReport";
	public static final String LATE_ENVELOPE_REPORT = "LateEnvelopeReport";
}
