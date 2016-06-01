/**
 * MachineReportBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     		22 Jan 2015
*  
**/
package com.walgreens.admin.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.bean.MachineReportResBean;
import com.walgreens.admin.dao.MachineReportDAO;
import com.walgreens.admin.factory.AdminDAOFactory;
import com.walgreens.admin.json.bean.MachineData;
import com.walgreens.admin.json.bean.MachineReportBean;
import com.walgreens.admin.json.bean.MachineType;
import com.walgreens.admin.json.bean.ResponseMachineData;
import com.walgreens.admin.json.bean.Store;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
/**
 * This class is used to implement Business validation as per action 
 * @author CTS
 * @version 1.1 January 22, 2015
 *
 */
@Component("MachineReportBO")
public class MachineReportBOImpl implements MachineReportBO {
	
	/**
	 * adminDAOFactory
	 */
	@Autowired
	private AdminDAOFactory adminDAOFactory; 

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineReportBOImpl.class);
	
	/**
	 * This method call the Dao layer and gets the Machine list. 
	 * @param contains store number.
	 * @return machineData
	 * @throws PhotoOmniException.
	 */
	@Override
	public MachineData  getReportMachineTypDetails(final String storeNumber)  throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportMachineTypDetails method of MachineReportBOImpl ");
		}
		List<MachineType> machineList = null;
		final MachineData machineData = new MachineData();
		try {
			final MachineReportDAO machineReportDAO = adminDAOFactory.getMachineReportDAO();
			machineList = machineReportDAO.getReportMachineTypDetails(storeNumber);
			machineData.setMachine(machineList);
			
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportMachineTypDetails method of RealTimeOrderBOImpl ");
			}
		}
		
		return machineData;
	}
	
	
	/**
	 * This method is used to get machine down time report data.
	 * @param reqBean contains request parameters.
	 * @return machineReportBean.
	 * @throws PhotoOmniException.
	 */
	@Override
	public MachineReportBean submitReportRequest(final MachineFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of MachineReportBOImpl ");
		}
		MachineReportBean machineReportBean = null;
		try {
			List<MachineReportResBean> machineReportResBeanList = null;
			MachineReportDAO machineReportDAO = adminDAOFactory.getMachineReportDAO();
			String shortColumn = this.getSortColumnName(reqBean.getSortColumnName());
			reqBean.setSortColumnName(shortColumn);
			machineReportResBeanList = machineReportDAO.submitReportRequest(reqBean);
			machineReportBean = this.getRespJsonDataStructure(machineReportResBeanList, reqBean);
			/*
			 * Store no should show the address of the store for the machine
			 * down time store report
			 */
			if (PhotoOmniConstants.LOCATION_TYPE_STORE.equals(reqBean.getLocationId())
					&& machineReportBean.getStore().size() > 0) {
				String storeNbrAddress = machineReportDAO.getLocationAddress(reqBean.getStoreId());
				machineReportBean.getStore().get(0).setStoreNbrAddress(storeNbrAddress);
			}
			if (LOGGER.isDebugEnabled()) {
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String strReportData = ow.writeValueAsString(machineReportBean);
				LOGGER.debug(" Machine downtime responce json -- > " + strReportData);
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitReportRequest method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of RealTimeOrderBOImpl ");
			}
		}
		return machineReportBean;
	}


	/**
	 * This method use to find the database shot column name.
	 * @param columnName contains front end parameter.
	 * @return shortColumn
	 * @throws PhotoOmniException.
	 */
	private String getSortColumnName(final String columnName) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSortColumnName method of MachineReportBOImpl ");
		}
		String shortColumn = "ID";
		try {
			Map <String, String> dbColumnName = new HashMap<String, String>();
			dbColumnName.put("machineName", "MACHINE_NAME");
			dbColumnName.put("equipmentName", "EQUIPMENT_NAME");
			dbColumnName.put("componentName", "MEDIA_NAME");
			dbColumnName.put("startTime", "START_TIME");
			dbColumnName.put("duration", "DURATION");
			dbColumnName.put("reason", "OM_DOWNTIME_REASON.DOWNTIME_REASON");
			dbColumnName.put("ID", "ID");
			shortColumn = dbColumnName.get(columnName);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSortColumnName method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSortColumnName method of RealTimeOrderBOImpl ");
			}
		}
		return shortColumn;
	}
	
	
	/**
	 * This method is used to get machine down time report data.
	 * @param reqBean contains request parameters.
	 * @return strReportData
	 * @exception PhotoOmniException custom exception.
	 */
	@Override
	 public MachineReportBean  submitExportRequest(final MachineFilter reqBean) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of MachineReportBOImpl ");
		}
		MachineReportBean machineReportBean = null;
		try {
			List<MachineReportResBean> machineReportResBeanList = null;
			MachineReportDAO machineReportDAO = adminDAOFactory.getMachineReportDAO();
			String shortColumn = this.getSortColumnName(reqBean.getSortColumnName());
			reqBean.setSortColumnName(shortColumn);
			machineReportResBeanList = machineReportDAO.submitReportRequest(reqBean);
			machineReportBean = this.getRespJsonDataStructure(machineReportResBeanList, reqBean);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitReportRequest method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of RealTimeOrderBOImpl ");
			}
		}
		return machineReportBean;
	}
	 
	 
	 
	/**
	 * This method creates the JSON for machine down time report
	 * @param machineReportResBeanList result set from database.
	 * @param reqBean contains front end parameter.
	 * @return machineReportBean.
	 * @throws PhotoOmniException custom exception. 
	 */
	public MachineReportBean getRespJsonDataStructure (final List<MachineReportResBean> machineReportResBeanList, 
			final MachineFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getRespJsonDataStructure method of MachineReportBOImpl ");
		}
		MachineReportBean machineReportBean = new MachineReportBean();
		try {
			this.getHeaderContentOfJson(reqBean, machineReportBean);
			List<ResponseMachineData> dataList = null;
			Store storeBean = null;
			for (int i = 0;  machineReportResBeanList.size() > i  ; i++) {
				MachineReportResBean machineReportResBean = machineReportResBeanList.get(i);
				if ( i == 0 || (i != 0 && (!machineReportResBean.getRegionNumber().equals(machineReportResBeanList.get(i - 1).getRegionNumber())
						|| !machineReportResBean.getDistrictNumber().equals(machineReportResBeanList.get(i - 1).getDistrictNumber())
						|| !machineReportResBean.getStoreNumber().equals(machineReportResBeanList.get(i - 1).getStoreNumber())))) {
					/*For a new region new district and new store*/
					storeBean = new Store();
					dataList = new ArrayList<ResponseMachineData>();
					if ( i == 0 || i !=0 && !machineReportResBean.getRegionNumber().equals(machineReportResBeanList.get(i - 1).getRegionNumber())) {
						storeBean.setRegionNumber(machineReportResBean.getRegionNumber().toString());
					} 
					if ( i == 0 || i !=0 && !machineReportResBean.getDistrictNumber().equals(machineReportResBeanList.get(i - 1).getDistrictNumber())) {
						storeBean.setDistrictNumber(machineReportResBean.getDistrictNumber().toString());
					} 
					storeBean.setTotalRecord(machineReportResBean.getTotalRecord().toString()); /*Setting total page*/
					if (!CommonUtil.isNull(reqBean.getCurrentPageNo())) {
						storeBean.setCurrrentPage(reqBean.getCurrentPageNo()); /*Setting current page */
					}
					storeBean.setStoreNumber(machineReportResBean.getStoreNumber().toString());
					ResponseMachineData responseMachineData = this.getResponseMachineData(machineReportResBean);
					dataList.add(responseMachineData);
					storeBean.setData(dataList);
					machineReportBean.getStore().add(storeBean);
					
				} else if (i != 0 && machineReportResBean.getRegionNumber().equals(machineReportResBeanList.get(i - 1).getRegionNumber())
						&& machineReportResBean.getDistrictNumber().equals(machineReportResBeanList.get(i - 1).getDistrictNumber())
						&& machineReportResBean.getStoreNumber().equals(machineReportResBeanList.get(i - 1).getStoreNumber())) {
					/*For same region, district and store */
					ResponseMachineData responseMachineData = this.getResponseMachineData(machineReportResBean);
					dataList.add(responseMachineData);
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getRespJsonDataStructure method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getRespJsonDataStructure method of RealTimeOrderBOImpl ");
			}
		}
		
		return machineReportBean;
	}
	
	
	/**
	 * This method set the json message header for MachinedownTime report 
	 * @param reqBean contains front end parameter.
	 * @param machineReportBean where value are getting set.
	 * @throws PhotoOmniException custom exception.
	 */
	private void getHeaderContentOfJson(final MachineFilter reqBean, final MachineReportBean machineReportBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getHeaderContentOfJson method of MachineReportBOImpl ");
		}
		try {
			machineReportBean.setStartDate(reqBean.getStartDate());
			machineReportBean.setEndDate(reqBean.getEndDate());
			machineReportBean.setLocation(reqBean.getLocation());
			machineReportBean.setLocationNumber(reqBean.getLocationId());
			machineReportBean.setMachine(reqBean.getMachineId());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getHeaderContentOfJson method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getHeaderContentOfJson method of RealTimeOrderBOImpl ");
			}
		}
	}
	
	
	/**
	 * This method creates the store data information.
	 * @param machineReportResBean contains response bean data.
	 * @return responseMachineData.
	 * @throws PhotoOmniException custom exception.
	 */
	private ResponseMachineData getResponseMachineData(MachineReportResBean machineReportResBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getResponseMachineData method of MachineReportBOImpl ");
		}
		ResponseMachineData responseMachineData = new ResponseMachineData();
		try {
			if (!CommonUtil.isNull(machineReportResBean.getMachineName())) {
				responseMachineData.setMachineName(machineReportResBean.getMachineName());
			}
			if (!CommonUtil.isNull(machineReportResBean.getEquipmentName())) {
				responseMachineData.setEquipmentName(machineReportResBean.getEquipmentName());
			}
			if (!CommonUtil.isNull(machineReportResBean.getComponentName())) {
				responseMachineData.setComponentName(machineReportResBean.getComponentName());
			}
			if (!CommonUtil.isNull(machineReportResBean.getEnteredBy())) {
				responseMachineData.setEnteredBy(machineReportResBean.getEnteredBy());
			}
			if (!CommonUtil.isNull(machineReportResBean.getDuration())) {
				responseMachineData.setDuration(machineReportResBean.getDuration().toString());
			}
			if (!CommonUtil.isNull(machineReportResBean.getReason())) {
				responseMachineData.setReason(machineReportResBean.getReason());
			}
			responseMachineData.setStartTime(CommonUtil.convertTimestampToString(machineReportResBean.getStartTime(),
					PhotoOmniConstants.DATE_FORMAT_TEN));
			responseMachineData.setEndTime(CommonUtil.convertTimestampToString(machineReportResBean.getEndTime(),
					PhotoOmniConstants.DATE_FORMAT_TEN));
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getResponseMachineData method of RealTimeOrderBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getResponseMachineData method of RealTimeOrderBOImpl ");
			}
		}
		
		return responseMachineData;
	}
		
	
}
