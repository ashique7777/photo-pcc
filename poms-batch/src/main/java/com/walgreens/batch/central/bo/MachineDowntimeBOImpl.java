/**
 * MachineDowntimeBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		
 *  
 **/
package com.walgreens.batch.central.bo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.bean.MachineDowntimeDataBean;
import com.walgreens.batch.central.bean.MachineDwntmSplitDataBean;
import com.walgreens.common.constant.PhotoOmniConstants;

@Component("MachineDowntimeBO")
public class MachineDowntimeBOImpl implements MachineDowntimeBO {

	/**
	 * LOGGER for Logging
	 */
	private static final Logger lOGGER = LoggerFactory
			.getLogger(MachineDowntimeBOImpl.class); 
	
	/**
	 * This method process the downtime records and split them according to
	 *  downtime start date and  downtime end date
	 * 
	 * @param machineDataBean
	 * @param machineHistrySplitList
	 */
	@Override
	public List<MachineDwntmSplitDataBean> processDowntimeRecords(
			MachineDowntimeDataBean machineDataBean) throws ParseException {
		
		List<MachineDwntmSplitDataBean> machineHistrySplitList = new ArrayList<MachineDwntmSplitDataBean>();
		int activeInd = machineDataBean.getWorkqueueStatus();
		try {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Starting splitting for ----> " + machineDataBean);
			}
			if (activeInd == PhotoOmniConstants.MACHINE_ACTIVE_CD_PROC) {
				Timestamp startDate = machineDataBean.getStartDttm();
				Timestamp currentSysDate = new Timestamp(new Date().getTime());
				if (machineDataBean.getEstimatedEndDttm().getTime() < currentSysDate
						.getTime()) {
					splitRecordsForDoneStatus(machineHistrySplitList,
							machineDataBean,
							machineDataBean.getEstimatedEndDttm());
				} else {
					long daysDiff = calculateNumberOfDays(startDate,
							currentSysDate);
					for (int procCount = 0; procCount < daysDiff; procCount++) {
						this.populateMachineHisDataForProc(
								machineHistrySplitList, machineDataBean,
								startDate);
						String time = this.getDayOfWeekOpenMac(machineDataBean,
								startDate);
						startDate = this.incrementDate(startDate, 1, time);
					}
				}
			} else if (activeInd == PhotoOmniConstants.MACHINE_ACTIVE_CD_DONE) {
				splitRecordsForDoneStatus(machineHistrySplitList,
						machineDataBean, machineDataBean.getActualEndDttm());
			}
		} catch (Exception e) {
			lOGGER.error(
					"Exception occured in processMachineRecords method of MachineDwntmBO",
					e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Completing splitting for ----> "
						+ machineDataBean);
			}
		}
	
		return machineHistrySplitList;
	}
	/**
	 * @param machineHistrySplitList
	 * @param machineDataBean
	 */
	private void splitRecordsForDoneStatus(
			List<MachineDwntmSplitDataBean> machineHistrySplitList,
			MachineDowntimeDataBean machineDataBean, Timestamp machineEndTime) {

		long numOfDays = calculateNumberOfDays(machineDataBean.getStartDttm(),
				machineEndTime);
		if (numOfDays <= 1) {
			MachineDwntmSplitDataBean dataBean = new MachineDwntmSplitDataBean();
			long dailyDuration = 0;
			try {
				dataBean.setDowntimeId(machineDataBean.getDowntimeId());
				dataBean.setInstanceId(machineDataBean.getInstanceId());
				dataBean.setComponentId(machineDataBean.getComponentId());
				dataBean.setEquipmentInstanceId(machineDataBean.getInstanceId());
				dataBean.setDowntimeReasonId(machineDataBean
						.getDowntimeReasonId());
				dataBean.setDowntimeEventName(machineDataBean
						.getDowntimeEventName());
				dataBean.setStartDttm(machineDataBean.getStartDttm());
				dataBean.setActualEndDttm(machineDataBean.getActualEndDttm());
				dataBean.setEstimatedEndDttm(machineDataBean
						.getEstimatedEndDttm());
				dataBean.setBeginEmployeeId(machineDataBean
						.getBeginEmployeeId());
				dataBean.setEndEmployeeId(machineDataBean.getEndEmployeeId());
				dataBean.setWorkqueueStatus(PhotoOmniConstants.MACHINE_ACTIVE_CD_DONE);

				String storeOpenTime = this.getDayOfWeekOpenMac(
						machineDataBean, machineDataBean.getStartDttm());
				Timestamp openDate = getStartDttm(
						machineDataBean.getStartDttm(), storeOpenTime);

				String storeCloseTime = this.getDayOfWeekCloseMac(
						machineDataBean, machineEndTime);
				Timestamp closeDate = getEndDttm(machineEndTime, storeCloseTime);
				dailyDuration = calculateDifferenceInDays(openDate, closeDate);
				if (dailyDuration < 0) {
					dailyDuration = 0;
				}
				dataBean.setDailyDwntmStart(openDate);
				dataBean.setDailyDwntmEnd(closeDate);

				dataBean.setDuration(dailyDuration);
				dataBean.setDowntimeReason(machineDataBean.getDowntimeReason());
				dataBean.setIdCreated(PhotoOmniConstants.DEFUALT_USER_ID);
				dataBean.setIdModified(PhotoOmniConstants.DEFUALT_USER_ID);
				dataBean.setDateTimeCreated(new Timestamp(new Date().getTime()));
				dataBean.setDateTimeModified(new Timestamp(new Date().getTime()));
			} catch (Exception e) {
				lOGGER.error(
						"Exception occured in splitRecordsForDoneStatus method of MachineDwntmBO",
						e);
			} finally {
				if (lOGGER.isDebugEnabled()) {
					lOGGER.debug(" Exiting splitRecordsForDoneStatus method of MachineDwntmBO ");
				}
			}
			if (dailyDuration != 0 && dailyDuration > 0) {
				machineHistrySplitList.add(dataBean);
			}
		} else {
			for (int doneCount = 0; doneCount < numOfDays; doneCount++) {
				MachineDwntmSplitDataBean dataBean = new MachineDwntmSplitDataBean();
				Timestamp downtimeDate = this.incrementDate(
						machineDataBean.getStartDttm(), doneCount, null);
				Timestamp endDateDone = null;
				Timestamp startDateDone = null;
				if (doneCount == 0) {
					String openTime = this.getDayOfWeekOpenMac(machineDataBean,
							downtimeDate);
					Timestamp dailyDowntimeStart = getStartDttm(
							machineDataBean.getStartDttm(), openTime);
					dataBean.setDailyDwntmStart(dailyDowntimeStart);
				} else {
					String openTime = this.getDayOfWeekOpenMac(machineDataBean,
							downtimeDate);
					Timestamp incrementedDate = incrementDate(
							machineDataBean.getStartDttm(), doneCount, openTime);
					Timestamp dailyDowntimeStart = getStartDttm(
							incrementedDate, openTime);
					dataBean.setDailyDwntmStart(dailyDowntimeStart);
				}
				if (doneCount == (numOfDays - 1)) {
					String closeTime = this.getDayOfWeekCloseMac(
							machineDataBean, machineEndTime);
					Timestamp closeDate = getEndDttm(machineEndTime, closeTime);
					dataBean.setDailyDwntmEnd(closeDate);
				} else {
					String closeTime = this.getDayOfWeekCloseMac(
							machineDataBean, downtimeDate);
					Timestamp incrementedDate = incrementDate(
							machineDataBean.getStartDttm(), doneCount,
							closeTime);
					Timestamp closeDate = getEndDttm(incrementedDate, closeTime);
					dataBean.setDailyDwntmEnd(closeDate);
				}
				startDateDone = dataBean.getDailyDwntmStart();
				endDateDone = dataBean.getDailyDwntmEnd();
				// machineEndTime = endDateDone;
				long dailyDuration = calculateDifferenceInDays(startDateDone,
						endDateDone);

				this.populateMachineHisDataForDone(machineHistrySplitList,
						machineDataBean, dataBean, dailyDuration);
			}
		}
	}
	/**
	 * @param machineHistrySplitList
	 * @param machineDataBean
	 * @param dataBean
	 * @param hourDiff
	 */
	private void populateMachineHisDataForDone(List<MachineDwntmSplitDataBean> machineHistrySplitList,
			MachineDowntimeDataBean machineDataBean, MachineDwntmSplitDataBean dataBean, long dailyDuration) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering populateMachineHisDataForDone method of MachineDwntmBO ");
		}
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		try {
			dataBean.setDuration(dailyDuration);
			dataBean.setStartDttm(machineDataBean.getStartDttm());
			dataBean.setActualEndDttm(machineDataBean.getActualEndDttm());
			dataBean.setEstimatedEndDttm(machineDataBean.getEstimatedEndDttm());
			dataBean.setWorkqueueStatus(PhotoOmniConstants.MACHINE_ACTIVE_CD_DONE);
			dataBean.setDowntimeId(machineDataBean.getDowntimeId());
			dataBean.setInstanceId(machineDataBean.getInstanceId());
			dataBean.setComponentId(machineDataBean.getComponentId());
			dataBean.setEquipmentInstanceId(machineDataBean.getInstanceId());
			dataBean.setDowntimeReason(machineDataBean.getDowntimeReason());
			dataBean.setDowntimeReasonId(machineDataBean.getDowntimeReasonId());
			dataBean.setDowntimeEventName(machineDataBean.getDowntimeEventName());
			dataBean.setIdCreated(PhotoOmniConstants.DEFUALT_USER_ID);
			dataBean.setBeginEmployeeId(machineDataBean.getBeginEmployeeId());
			dataBean.setEndEmployeeId(machineDataBean.getEndEmployeeId());
			dataBean.setIdModified(PhotoOmniConstants.DEFUALT_USER_ID);
			dataBean.setDateTimeCreated(currentSysDate);
			dataBean.setDateTimeModified(currentSysDate);
			if (dailyDuration != 0 && dailyDuration > 0) {
				machineHistrySplitList.add(dataBean);
			}
		} catch (Exception e) {
			lOGGER.error("Exception occured in populateMachineHisDataForDone method of MachineDwntmBO" , e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting populateMachineHisDataForDone method of MachineDwntmBO ");
			}
		}
	}
	
	/**
	 * This method calculates the days difference between start_date and
	 * end_date
	 * 
	 * @param start_date
	 * @param end_date
	 * @return days
	 */
	public static long calculateNumberOfDays(Timestamp start_date,
			Timestamp end_date) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering calculateNumberOfDays method of MachineDwntmBO ");
		}
		int days = 0;
		try {

			Calendar cal1 = Calendar.getInstance(); // locale-specific
			cal1.setTimeInMillis(start_date.getTime());
			cal1.set(Calendar.HOUR_OF_DAY, 0);
			cal1.set(Calendar.MINUTE, 0);
			cal1.set(Calendar.SECOND, 0);
			cal1.set(Calendar.MILLISECOND, 0);
			long time1 = cal1.getTimeInMillis();
			Calendar cal2 = Calendar.getInstance(); // locale-specific
			cal2.setTimeInMillis(end_date.getTime());
			cal2.set(Calendar.HOUR_OF_DAY, 0);
			cal2.set(Calendar.MINUTE, 0);
			cal2.set(Calendar.SECOND, 0);
			cal2.set(Calendar.MILLISECOND, 0);
			long time2 = cal2.getTimeInMillis();

			float diff = (float) time2 - time1;
			// int tempDays = (int) Math.ceil((diff / (24 * 60 * 60 * 1000))) +
			// 1;
			float diff24 = (diff / (24 * 60 * 60 * 1000));
			int ceil = (int) Math.round(diff24);
			if (!(DateUtils.isSameDay(end_date, start_date))) {
				days = ceil + 1;
			} else {
				days = 1;
			}
			/*System.out.println(start_date + " ; " + end_date + " ; diff="
					+ (diff / (60 * 60 * 1000)) + " ; diff24 = " + diff24
					+ " ; days = " + days);*/
		} catch (Exception e) {
			if (lOGGER.isErrorEnabled()) {
				lOGGER.error(
						"Exception occured in calculateNumberOfDays method of MachineDwntmBO",
						e);
			}
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting calculateNumberOfDays method of MachineDwntmBO ");
			}
		}
		return days;
	}

	/**
	 * @param machineHistrySplitList
	 * @param machineDataBean
	 * @param startDate
	 */
	private void populateMachineHisDataForProc(
			List<MachineDwntmSplitDataBean> machineHistrySplitList,
			MachineDowntimeDataBean machineDataBean, Timestamp startDate) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering populateMachineHisDataForProc method of MachineDwntmBO ");
		}
		try {
			String time = this.getDayOfWeekCloseMac(machineDataBean, startDate);
			Timestamp endDate = this.incrementDate(startDate, 0, time);
			this.setRecordsInMachineHistory(machineHistrySplitList,
					machineDataBean, startDate, endDate);
		} catch (Exception e) {
			lOGGER.error(
					"Exception occured in populateMachineHisDataForProc method of MachineDwntmBO",
					e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting populateMachineHisDataForProc method of MachineDwntmBO ");
			}
		}
	}
	/**
	 * This method sets records in split data bean for history table
	 * 
	 * @param machineDataBean
	 * @param start_date
	 * @return MachineDwntmSplitDataBean
	 */
	private MachineDwntmSplitDataBean setRecordsInMachineHistory(List<MachineDwntmSplitDataBean> machineHistrySplitList, MachineDowntimeDataBean machineDataBean, Timestamp startDate, Timestamp endDate) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering setRecordsInMachineHistory method of MachineDwntmBO ");
		}
		MachineDwntmSplitDataBean dataBean = new MachineDwntmSplitDataBean();
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		try {
			dataBean.setDowntimeId(machineDataBean.getDowntimeId());
			dataBean.setInstanceId(machineDataBean.getInstanceId());
			dataBean.setComponentId(machineDataBean.getComponentId());
			dataBean.setEquipmentInstanceId(machineDataBean.getInstanceId());
			dataBean.setStartDttm(machineDataBean.getStartDttm());
			dataBean.setActualEndDttm(machineDataBean.getActualEndDttm());
			dataBean.setEstimatedEndDttm(machineDataBean.getEstimatedEndDttm());
			dataBean.setWorkqueueStatus(PhotoOmniConstants.MACHINE_ACTIVE_CD_DONE);
			dataBean.setDowntimeReason(machineDataBean.getDowntimeReason());
			dataBean.setDowntimeReasonId(machineDataBean.getDowntimeReasonId());
			dataBean.setDowntimeEventName(machineDataBean.getDowntimeEventName());
			dataBean.setBeginEmployeeId(machineDataBean.getBeginEmployeeId());
			dataBean.setEndEmployeeId(machineDataBean.getEndEmployeeId());
			//CHNAGES TO BE DONE
			dataBean.setIdCreated(PhotoOmniConstants.DEFUALT_USER_ID);
			dataBean.setIdModified(PhotoOmniConstants.DEFUALT_USER_ID);
			dataBean.setDateTimeCreated(currentSysDate);
			dataBean.setDateTimeModified(currentSysDate);
			//dataBean.setDailyDwntmStart(startDate);
			
			String openTime = this.getDayOfWeekOpenMac(machineDataBean, startDate);
			Timestamp openDate = getStartDttm(startDate,openTime);
			
			String closeTime = this.getDayOfWeekCloseMac(machineDataBean, endDate);
			Timestamp closeDate = getEndDttm(endDate,closeTime);
			
			dataBean.setDailyDwntmStart(openDate);
			long dailyDuration = 0 ;
			if (closeDate.getTime() <= machineDataBean.getEstimatedEndDttm().getTime()) {
				dataBean.setDailyDwntmEnd(closeDate);
				dailyDuration = this.calculateDifferenceInDays(dataBean.getDailyDwntmStart(), closeDate);
				dataBean.setDuration(dailyDuration);
			} else {
				dataBean.setDailyDwntmEnd(machineDataBean.getEstimatedEndDttm());
				dailyDuration = calculateDifferenceInDays(dataBean.getDailyDwntmStart(), machineDataBean.getEstimatedEndDttm());
				dataBean.setDuration(dailyDuration);
			}
			if (dailyDuration != 0 && dailyDuration > 0) {
				machineHistrySplitList.add(dataBean);
			}
		} catch (Exception e) {
			lOGGER.error("Exception occured in setRecordsInMachineHistory method of MachineDwntmBO", e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting setRecordsInMachineHistory method of MachineDwntmBO ");
			}
		}
		return dataBean;
	}
	private String getDayOfWeekOpenMac(MachineDowntimeDataBean machineDataBean,
			Timestamp startDate) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering getDayOfWeekOpenMac method of MachineDwntmBO ");
		}
		String time = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case 1:
				time = machineDataBean.getBusinessTimeOpenSun();
				break;
			case 2:
				time = machineDataBean.getBusinessTimeOpenMon();
				break;
			case 3:
				time = machineDataBean.getBusinessTimeOpenTue();
				break;
			case 4:
				time = machineDataBean.getBusinessTimeOpenWed();
				break;
			case 5:
				time = machineDataBean.getBusinessTimeOpenThs();
				break;
			case 6:
				time = machineDataBean.getBusinessTimeOpenFri();
				break;
			case 7:
				time = machineDataBean.getBusinessTimeOpenSat();
				break;
			}
		} catch (Exception e) {
			lOGGER.error(
					"Exception occured in getDayOfWeekOpenMac method of MachineDwntmBO",
					e);
		}
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Exiting getDayOfWeekOpenMac method of MachineDwntmBO ");
		}
		return time;
	}

	/**
	 * This method increments the date by 'k' days
	 * 
	 * @param d
	 * @param k
	 * @param time
	 * @return incrementedDate
	 * @throws ParseException
	 */
	@SuppressWarnings("deprecation")
	private Timestamp incrementDate(Timestamp d, int k, String time) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering incrementDate method of MachineDwntmBO ");
		}
		Timestamp incrementedDate = null;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_WEEK, k);
			incrementedDate = new Timestamp(cal.getTime().getTime());
			if (time != null) {
				String hours = time.substring(0, 2);
				String mins = time.substring(3, 5);
				incrementedDate.setHours(Integer.parseInt(hours));
				incrementedDate.setMinutes(Integer.parseInt(mins));
				incrementedDate.setSeconds(0);
			}
		} catch (NumberFormatException e) {
			lOGGER.error(
					"Exception occured in incrementDate method of MachineDwntmBO",
					e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting incrementDate method of MachineDwntmBO ");
			}
		}
		return incrementedDate;
	}

	// Jira 540 fix
	private Timestamp getStartDttm(Timestamp date, String openTime) {
		Timestamp startDttm;

		Calendar actualStartTime = Calendar.getInstance();
		actualStartTime.setTime(date);
		/*System.out.println(actualStartTime.getTime());*/
		Calendar storeOpenTime = Calendar.getInstance();
		storeOpenTime.setTime(date);
		int hours = Integer.parseInt(openTime.substring(0, 2));
		int mins = Integer.parseInt(openTime.substring(3, 5));
		storeOpenTime.set(Calendar.HOUR_OF_DAY, hours);
		storeOpenTime.set(Calendar.MINUTE, mins);
		storeOpenTime.set(Calendar.SECOND, 0);
		/*System.out.println(storeOpenTime.getTime());*/
		if (actualStartTime.getTimeInMillis() < storeOpenTime.getTimeInMillis()) {
			startDttm = new Timestamp(storeOpenTime.getTime().getTime());
		} else {
			startDttm = new Timestamp(actualStartTime.getTime().getTime());
		}
		return (Timestamp) startDttm;

	}

	// Jira 540 fix
	private Timestamp getEndDttm(Timestamp downDate, String storeCloseTime) {
		Timestamp startDttm;

		Calendar actualEndTime = Calendar.getInstance();
		actualEndTime.setTime(downDate);
		/*System.out.println(actualEndTime.getTime());*/
		Calendar storeEndTime = Calendar.getInstance();
		storeEndTime.setTime(downDate);
		int hours = Integer.parseInt(storeCloseTime.substring(0, 2));
		int mins = Integer.parseInt(storeCloseTime.substring(3, 5));
		storeEndTime.set(Calendar.HOUR_OF_DAY, hours);
		storeEndTime.set(Calendar.MINUTE, mins);
		storeEndTime.set(Calendar.SECOND, 0);
		/*System.out.println(storeEndTime.getTime());*/
		if (actualEndTime.getTimeInMillis() > storeEndTime.getTimeInMillis()) {
			startDttm = new Timestamp(storeEndTime.getTime().getTime());
		} else {
			startDttm = new Timestamp(actualEndTime.getTime().getTime());
		}
		return (Timestamp) startDttm;

	}

	/**
	 * This method calculate the difference between two date in terms of hours
	 * 
	 * @param startDttm
	 * @param actualEndDttm
	 * @return diffInMinutes
	 */
	private long calculateDifferenceInDays(Timestamp startDttm,
			Timestamp actualEndDttm) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering calculateDifferenceInDays method of MachineDwntmBO ");
		}
		long diffInMinutes = 0;
		try {
			long miliSecondForStartDate = startDttm.getTime();
			long miliSecondForEndDate = actualEndDttm.getTime();
			long diffInMilis = miliSecondForEndDate - miliSecondForStartDate;
			diffInMinutes = diffInMilis / (60 * 1000);
		} catch (Exception e) {
			lOGGER.error(
					"Exception occured in calculateDifferenceInDays method of MachineDwntmBO",
					e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting calculateDifferenceInDays method of MachineDwntmBO ");
			}
		}
		return diffInMinutes;
	}
	/** This method return store closing hour
	 * @param machineDataBean
	 * @param startDate
	 * @return time
	 */
	private String getDayOfWeekCloseMac(MachineDowntimeDataBean machineDataBean,
			Timestamp startDate) {
		if (lOGGER.isDebugEnabled()) {
			lOGGER.debug(" Entering getDayOfWeekCloseMac method of MachineDwntmBO ");
		}
		String time = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case 1:
				time = machineDataBean.getBusinessTimeCloseSun();
				break;
			case 2:
				time = machineDataBean.getBusinessTimeCloseMon();
				break;
			case 3:
				time = machineDataBean.getBusinessTimeCloseTue();
				break;
			case 4:
				time = machineDataBean.getBusinessTimeCloseWed();
				break;
			case 5:
				time = machineDataBean.getBusinessTimeCloseThs();
				break;
			case 6:
				time = machineDataBean.getBusinessTimeCloseFri();
				break;
			case 7:
				time = machineDataBean.getBusinessTimeCloseSat();
				break;
			}
		} catch (Exception e) {
			lOGGER.error("Exception occured in getDayOfWeekCloseMac method of MachineDwntmBO", e);
		} finally {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Exiting getDayOfWeekCloseMac method of MachineDwntmBO ");
			}
		}
		return time;
	}

}
