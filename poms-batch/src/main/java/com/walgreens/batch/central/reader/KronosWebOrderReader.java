
/**
 * KronosWebOrderReader.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 7 September 2015
 *  
 **/
package com.walgreens.batch.central.reader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;

import com.walgreens.batch.central.bean.KronosDataBean;
import com.walgreens.batch.central.bean.StoreOpenCloseDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This reader class is a dummy class later it will be. 
 * @author CTS
 * @version 1.1 September 07, 2015
 */

public class KronosWebOrderReader implements ItemReader<KronosDataBean>{
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosWebOrderReader.class);
	/**
	 * storeOpenCloseDataBeanList
	 */
	private Map<String, StoreOpenCloseDataBean> storeOpenCloseDataBeanList = null;
	/**
	 * beginDate;
	 */
	private String beginDate;
	/**
	 * endDate
	 */
	private String endDate;
	/**
	 * totalStore
	 */
	private int totalStore;
	/**
	 * storeNames
	 */
	private List<String> storeNames = null;
	/**
	 * dateFormat
	 */
	private String dateFormat = PhotoOmniConstants.DATE_FORMAT_TWELFTH;
	/**
	 * count
	 */
	private int count = 0;
	/**
	 * isBefore
	 */
	private boolean isBefore;
	/**
	 * noOfStore
	 */
	private int noOfStore;
	
	
	/**
	 * This method creating the Web Order for Kronos - Dummy class  
	 * @return kronosDataBean. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public KronosDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of KronosWebOrderReader ");
		}
		KronosDataBean kronosDataBean = null;
		try {
			if (count == 0) {
				isBefore = this.isBeforeOrEqualDate(this.beginDate, this.endDate, this.dateFormat);
			}
			 if (isBefore) {
				 for (;count < this.noOfStore; ) {
					 kronosDataBean = new KronosDataBean();
					 kronosDataBean.setUnits("UNITS");
					 kronosDataBean.setStoreNumber(storeNames.get(count)); 
					 kronosDataBean.setMatrixCode("PHOWEB");
					 kronosDataBean.setDate(CommonUtil.stringDateFormatChange(this.beginDate, this.dateFormat, "MM/dd/YYYY"));
					 kronosDataBean.setSlotAndCount(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
					 ++count;
					 break;
				}
				 if (count == noOfStore) {
					 this.addDays(this.beginDate, 1);
					 count = 0;
				}
				
			} 
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at read method of KronosWebOrderReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting read method of KronosWebOrderReader ");
			}
		}
		return kronosDataBean;
	}

	
	
	/**
	 * This method set the params values to execution context.
	 * @param stepExecution contains stepExecution value. 
	 * @throws PhotoOmniException - Custom Exception.
	 */
	@SuppressWarnings("unchecked")
	@BeforeStep
	protected void retriveValue(final StepExecution stepExecution) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering retriveValue method of KronosWebOrderReader ");
		}
		try {
			final JobExecution jobExecution = stepExecution.getJobExecution();
			final ExecutionContext executionContext = jobExecution.getExecutionContext();
			this.storeOpenCloseDataBeanList = (Map<String, StoreOpenCloseDataBean>) executionContext.get("storeOpenCloseSlot");
			this.totalStore = !CommonUtil.isNull(this.storeOpenCloseDataBeanList) ? storeOpenCloseDataBeanList.size() : 0;
			if (this.totalStore > 0) {
				this.storeNames  = new ArrayList<String>(storeOpenCloseDataBeanList.keySet());
				this.noOfStore = this.storeNames.size();
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at retriveValue method of KronosWebOrderReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting retriveValue method of KronosWebOrderReader ");
			}
	   }
	}


	/**
	 * This method checks if firstDate is before endDate or not.
	 * @param firstDate contains first date value.
	 * @param endDate contains last date value.
	 * @param format contains date format.
	 * @return boolean.
	 */
	public boolean isBeforeOrEqualDate(String firstDate, String endDate, String format) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into isBeforeOrEqualDate method of KronosWebOrderReader");
		}
		boolean isBefore = false;
		try {
			if (!CommonUtil.isNull(firstDate) && !CommonUtil.isNull(endDate) && !CommonUtil.isNull(format)) {
				final SimpleDateFormat sdf = new SimpleDateFormat(format);
				final Date date1 = sdf.parse(firstDate);
				final Date date2 = sdf.parse(endDate);

				final Calendar cal1 = Calendar.getInstance();
				final Calendar cal2 = Calendar.getInstance();
				cal1.setTime(date1);
				cal2.setTime(date2);
				if(cal1.equals(cal2)){
					isBefore = true;
	        	} else if(cal1.before(cal2)){
	        		isBefore = true;
	        	} 
			}
		} catch (ParseException e) {
			LOGGER.error(" Error occoured at isBeforeOrEqualDate method of KronosWebOrderReader " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at isBeforeOrEqualDate method of KronosWebOrderReader " + e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting isBeforeOrEqualDate method of KronosWebOrderReader ");
			}
		}
		return isBefore;
	}

	
	/**
	 * This method use to increment date by desire days.
	 * @param strDate contains date value string format.
	 * @param days contains no of days.
	 * @throws PhotoOmniException-Custom Exception.
	 */
	public void addDays(String strDate, int days) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entered into addDays method of KronosWebOrderReader");
		}
		try {
			if (!CommonUtil.isNull(strDate)) {
				final SimpleDateFormat sdf = new SimpleDateFormat(this.dateFormat);
				final Date date = sdf.parse(strDate);
				final Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DATE, days); 
				Date incrementDate = cal.getTime();
				this.beginDate = sdf.format(incrementDate);
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addDays method of KronosWebOrderReader - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addDays method of KronosWebOrderReader ");
			}
	   }
	}
	
	/**
	 * @return the beginDate
	 */
	public String getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}


	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}


	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
}
