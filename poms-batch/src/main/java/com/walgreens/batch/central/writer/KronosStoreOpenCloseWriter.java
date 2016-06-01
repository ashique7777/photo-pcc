/**
 * KronosStoreOpenCloseWriter.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 2nd September 2015
 *  
 **/
package com.walgreens.batch.central.writer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;

import com.walgreens.batch.central.bean.LocationDataBean;
import com.walgreens.batch.central.bean.StoreOpenCloseDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This is a writer class Create the store open close slot and put them into execution context.
 * @author CTS
 * @version 1.1 September 02, 2015
 */

public class KronosStoreOpenCloseWriter implements ItemWriter<LocationDataBean> {
    /**
     * stepExecution
     */
	private StepExecution stepExecution;
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosStoreOpenCloseWriter.class);
	/**
	 * storeOpenCloseDataBeanList
	 */
	private Map<String, StoreOpenCloseDataBean> storeOpenCloseDataBeanList = new HashMap<String, StoreOpenCloseDataBean>();
	
	/**
	 * this method used to get stepExecution context.
	 * @param stepExecution
	 */
	@BeforeStep
	public void saveStepExecution(final StepExecution stepExecution) { 
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering saveStepExecution method of KronosStoreOpenCloseWriter ");
		}
		this.stepExecution = stepExecution;
	}


	/**
	 * This method putting the 'storeOpenCloseDataBeanList' value to the execution context.
	 * @param item contains item value.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	public void write(final List<? extends LocationDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of KronosStoreOpenCloseWriter ");
		}
		try {
			final ExecutionContext executionContext = this.stepExecution.getExecutionContext();
			final List<LocationDataBean> locationDataBeanList  = new ArrayList<LocationDataBean>(items); 
			this.getStoreopenCloseSlot(locationDataBeanList);
			executionContext.put("storeOpenCloseSlot", storeOpenCloseDataBeanList);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at write method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of KronosStoreOpenCloseWriter ");
			}
		}
	}

	
	/**
	 * This method use to get store open close slot.
	 * @param locationDataBeanList contains location data.
	 * @throws PhotoOmniException - Custom Exception. 
	 */
	private void getStoreopenCloseSlot(final List<LocationDataBean> locationDataBeanList) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getStoreopenCloseSlot method of KronosStoreOpenCloseWriter ");
		}
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" locationDataBeanList size is : " + locationDataBeanList.size());
			}
			for (LocationDataBean locationDataBean : locationDataBeanList) {
				StoreOpenCloseDataBean storeOpenCloseDataBean = new StoreOpenCloseDataBean();
				storeOpenCloseDataBeanList.put(locationDataBean.getLocationNumber(), storeOpenCloseDataBean);
				Long isTwentyFourHourstr = locationDataBean.getIsTwentyFourHourstr();
				if (isTwentyFourHourstr != 1) {
					storeOpenCloseDataBean.setIsTwentyFourHourstr(isTwentyFourHourstr);
					/*Sunday open close*/
					String storeOpenTimeSun = locationDataBean.getStoreOpenTimeSun();
					String storeCloseTimeSun = locationDataBean.getStoreCloseTimeSun();
					int openSlotSun = this.timeToSlotcalculator(storeOpenTimeSun);
					int closeSlotSun = PhotoOmniConstants.STORE_CLOSED; 
					if (openSlotSun != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotSun = this.timeToSlotcalculator(storeCloseTimeSun);
					}
					storeOpenCloseDataBean.setOpenSlotSun(openSlotSun);
					storeOpenCloseDataBean.setCloseSlotSun(closeSlotSun);
					
					/*Monday open close*/
					String storeOpenTimeMon = locationDataBean.getStoreOpenTimeMon();
					String storeCloseTimeMon = locationDataBean.getStoreCloseTimeMon();
					int openSlotMon = this.timeToSlotcalculator(storeOpenTimeMon);
					int closeSlotMon = PhotoOmniConstants.STORE_CLOSED; 
					if (openSlotMon != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotMon = this.timeToSlotcalculator(storeCloseTimeMon);
					}
					storeOpenCloseDataBean.setOpenSlotMon(openSlotMon);
					storeOpenCloseDataBean.setCloseSlotMon(closeSlotMon);
					
					/*Tuesday open close*/
					String storeOpenTimeTue = locationDataBean.getStoreOpenTimeTue();
					String storeCloseTimeTue = locationDataBean.getStoreCloseTimeTue();
					int openSlotTue = this.timeToSlotcalculator(storeOpenTimeTue);
					int closeSlotTue = PhotoOmniConstants.STORE_CLOSED;
					if (openSlotTue != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotTue = this.timeToSlotcalculator(storeCloseTimeTue);
					}
					storeOpenCloseDataBean.setOpenSlotTue(openSlotTue);
					storeOpenCloseDataBean.setCloseSlotTue(closeSlotTue);
					
					/*Wednesday open close*/
					String storeOpenTimeWed = locationDataBean.getStoreOpenTimeWed();
					String storeCloseTimeWed = locationDataBean.getStoreCloseTimeWed();
					int openSlotWed = this.timeToSlotcalculator(storeOpenTimeWed);
					int closeSlotWed = PhotoOmniConstants.STORE_CLOSED;
					if (openSlotWed != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotWed = this.timeToSlotcalculator(storeCloseTimeWed);
					}
					storeOpenCloseDataBean.setOpenSlotWed(openSlotWed);
					storeOpenCloseDataBean.setCloseSlotWed(closeSlotWed);
					
					/*Thursday open close*/
					String storeOpenTimeThus = locationDataBean.getStoreOpenTimeThus();
					String storeCloseTimeThus = locationDataBean.getStoreCloseTimeThus();
					int openSlotThus = this.timeToSlotcalculator(storeOpenTimeThus);
					int closeSlotThus = PhotoOmniConstants.STORE_CLOSED;
					if (openSlotThus != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotThus = this.timeToSlotcalculator(storeCloseTimeThus);
					}
					storeOpenCloseDataBean.setOpenSlotThus(openSlotThus);
					storeOpenCloseDataBean.setCloseSlotThus(closeSlotThus);
					
					/*Friday open close*/
					String storeOpenTimeFri = locationDataBean.getStoreOpenTimeFri();
					String storeCloseTimeFri = locationDataBean.getStoreCloseTimeFri();
					int openSlotFri = this.timeToSlotcalculator(storeOpenTimeFri);
					int closeSlotFri = PhotoOmniConstants.STORE_CLOSED;
					if (openSlotFri != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotFri = this.timeToSlotcalculator(storeCloseTimeFri);
					}
					storeOpenCloseDataBean.setOpenSlotFri(openSlotFri);
					storeOpenCloseDataBean.setCloseSlotFri(closeSlotFri);
					
					/*Saturday open close*/
					String storeOpenTimeSat = locationDataBean.getStoreOpenTimeSat();
					String storeCloseTimeSat = locationDataBean.getStoreCloseTimeSat();
					int openSlotSat = this.timeToSlotcalculator(storeOpenTimeSat);
					int closeSlotSat = PhotoOmniConstants.STORE_CLOSED;
					if (openSlotSat != PhotoOmniConstants.STORE_CLOSED) {
						closeSlotSat = this.timeToSlotcalculator(storeCloseTimeSat);
					}
					storeOpenCloseDataBean.setOpenSlotSat(openSlotSat);
					storeOpenCloseDataBean.setCloseSlotSat(closeSlotSat);
				} else {
					storeOpenCloseDataBean.setIsTwentyFourHourstr(isTwentyFourHourstr);
				}
			}
		} catch (NumberFormatException e) {
			LOGGER.error(" Error occoured at getStoreopenCloseSlot method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getStoreopenCloseSlot method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getStoreopenCloseSlot method of KronosStoreOpenCloseWriter ");
			}
		}
	}


	/**
	 * This method decide the open and close time slot in 15 min interval
	 * @param storeTime contains time.
	 * @return floor.
	 * @throws PhotoOmniException - Custom Exception.
	 */
	private int timeToSlotcalculator(final String storeTime) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering timeToSlotcalculator method of KronosStoreOpenCloseWriter ");
		}
		int floor = 0;
		try {
			if (!CommonUtil.isNull(storeTime) && !storeTime.isEmpty()) {
				String[] timeArray = null;
				if (storeTime.contains(".")) {
					timeArray = storeTime.split("\\.");
				} else if (storeTime.contains(":")) {
					timeArray = storeTime.split(":");
				}
				 if (!CommonUtil.isNull(timeArray) && timeArray.length > 1) {
					 int totalTimeValue = Integer.parseInt(timeArray[0])*60 + (Integer.parseInt(timeArray[1]));
					 floor = (totalTimeValue/15);
					 int ceil = (totalTimeValue%15);
					 if (ceil > 0) {
						 ++floor;
					 }
					 /*Below checking needed because from the system we populate Store 
					  open/close time in OM_LOCATION having some discrepancies from real 
					  time date it is having time like 24:00 also having 00:00*/ 
					if (floor < 1 || floor > 96) {
						floor = 96;
					}
				}
			} else {
				floor = PhotoOmniConstants.STORE_CLOSED;
			}
		} catch (NumberFormatException e) {
			LOGGER.error(" Error occoured at timeToSlotcalculator method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (ParseException e) {
			LOGGER.error(" Error occoured at timeToSlotcalculator method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at timeToSlotcalculator method of KronosStoreOpenCloseWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting timeToSlotcalculator method of KronosStoreOpenCloseWriter ");
			}
		}
		return  floor;
	}
}
