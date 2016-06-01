package com.walgreens.omni.web.constants;

public interface ReportsConstant {
	
	/**
	 * Pm Report Constatns
	 */

	String EMAIL_IDS = "emailIds";
	String FILTER = "filter";
	String RESPONSE_MESSAGE =  "responseMessage";
	String MESSAGE_HEADER = "messageHeader";
	String STATUS = "status";
	String STATUS_MESSAGE_SUCESS = "PM by WIC Custom report request submited sucessfully";
	String STATUS_MESSAGE_FAILURE = "PM by WIC Custom report request failed";
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
	String PMREPORT = "PMReport";
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
    
    /**Silver canister report constants*/
    public static final String DATE_FORMAT_SILVER_CANISTER_ONE = "MM/dd/yy";
    public static final String STORE_NO_PREFIX = "WL";
    public static final String BLANK = "";
    public static final String COMMA = ",";
    public static final String DATE_PATTERN_ONE = "^(1[0-2]|0?[1-9])/(3[01]|[12][0-9]|0?[1-9])/[0-9]{4}$";
    public static final String STORE_PATTERN = "[WL0-9]{3,7}$";
    public static final String ERROR_STRING_PATTERN_ONE = "Please Upload the Silver Recovery Data in the *.csv format";
    public static final String ERROR_STRING_PATTERN_TWO = "Please upload vaild *.csv file";
    public static final String SIGN_PATTERN_ONE = "|"; 
    public static final String SIGN_PATTERN_TWO = "-";
    public static final String NUMBER_ONE = "1";
    

}
