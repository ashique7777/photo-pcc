/**
 * MachineDAOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		12 Jan 2015
 *  
 **/
package com.walgreens.admin.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.admin.bean.MachineDowntimeBean;
import com.walgreens.admin.utility.MachineDowntimeDetailQuery;
import com.walgreens.admin.utility.ServiceUtil;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to save the json data to the underlyling database.
 * 
 * @author CTS
 * 
 */
@Repository("MachineDowntimeDAO")
@Transactional(propagation = Propagation.REQUIRED)
public class MachineDAOImpl implements MachineDAO, PhotoOmniConstants {

	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MachineDAOImpl.class);

	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * This method id used for save machine downtime related data to database
	 * 
	 * @param machineReqList
	 * @return machineRespList
	 * @throws SQLException 
	 */
	@Override
	public boolean updateMachineDowntimeDtls(List<MachineDowntimeBean> machineReqList)
			throws PhotoOmniException, SQLException {

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering updateMachineDwntmDetails method of MachineDAOImpl ");
			boolean status = false;
			for (MachineDowntimeBean machineDowntimeDetailsReqList : machineReqList) {
				status = updateData(status, machineDowntimeDetailsReqList);
			}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting updateMachineDwntmDetails method of MachineDAOImpl ");
			return status;
	}

	/**
	 * @param status
	 * @param machineReqBean
	 * @return
	 * @throws PhotoOmniException 
	 */
	private boolean updateData(boolean status,
			MachineDowntimeBean machineReqBean) throws PhotoOmniException {
		int flag;
		if (null != machineReqBean) {
			String machineInstanceId = getMachineInstanceId(machineReqBean);
			if (CommonUtil.isNull(machineInstanceId) || "".equals(machineInstanceId)) {
				throw new PhotoOmniException(PhotoOmniConstants.MACHINE_ID_NOT_FOUND);
				} else {
					String downtimeReasonId = getMachineDowntimeReasonId(machineReqBean);
					machineReqBean.setDowntimeReasonId(downtimeReasonId);
					if (CommonUtil.isNull(downtimeReasonId) || "".equals(downtimeReasonId)) {
						throw new PhotoOmniException(PhotoOmniConstants.DOWNTIME_REASON_NOT_MATCHED);
						} else {
								/*
								 *boolean checkDowntimeId = checkDowntimeReasonId(machineReqBean); 
								 */
						machineReqBean.setMachineInstanceId(machineInstanceId);
						if (ServiceUtil.isMachineDowntime(machineReqBean)) {
							flag = checkIfMachineDataAlreadyExists(machineReqBean);
							if (flag == 0) { 
								status = insertMachineDwntmDtls(machineReqBean);
							} else {
								status = updateMachineDwntmDtls(machineReqBean);
							}
						} else if (ServiceUtil.isEquipmentDowntime(machineReqBean)) {
							String equipmentInstanceId = getEquipmentInstanceId(machineReqBean);
							if (CommonUtil.isNull(equipmentInstanceId) || "".equals(equipmentInstanceId)) {
								throw new PhotoOmniException(PhotoOmniConstants.EQUIPMENT_ID_NOT_FOUND);
								} else {
								machineReqBean.setEquipmentInstanceId(equipmentInstanceId);
								flag = checkIfEquipmentDataAlreadyExists(machineReqBean);
								if (flag == 0) {
									status = insertEquipmentDwntmDtls(machineReqBean);
								} else {
									status = updateEquipmentDwntmDtls(machineReqBean);
								}
							}
						}else if (ServiceUtil.isComponentDowntime(machineReqBean)) {
							String componentDowntimeId = getProductComponentId(machineReqBean);
							if (CommonUtil.isNull(componentDowntimeId) || "".equals(componentDowntimeId)) {
								throw new PhotoOmniException(PhotoOmniConstants.COMPONENT_ID_NOT_FOUND);
							} else {
							machineReqBean.setComponentDowntimeId(componentDowntimeId);
							String equipmentInstanceId = getEquipmentInstanceId(machineReqBean);
							machineReqBean.setEquipmentInstanceId(equipmentInstanceId);
							flag = checkIfMediaDataAlreadyExists(machineReqBean);
							if (flag == 0) {
								status = insertMediaDwntmDtls(machineReqBean);
							} else {
								status = updateMediaDwntmDtls(machineReqBean);
							}
						}
					}
				}
			}
		}
			return status;
	}

	/**
	 * @param machineReqBean
	 * @return
	 *//*
	private String getComponentDowntimeId(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering getComponentDowntimeId method of MachineDAOImpl ");
		
		String selectQuery = MachineDowntimeDetailQuery
				.selectComponentDowntimeId().toString();
		Object[] param = new Object[]{ machineReqBean.getMediaId() };
		String componentDowntimeId = null;
		try {
			componentDowntimeId = jdbcTemplate.queryForObject(selectQuery, param, 
					String.class);
		} catch (DataAccessException e) {
				LOGGER.error(" DataAccessException occoured at getComponentDowntimeId method of MachineDAOImpl - "
						, e);
		} finally {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting getComponentDowntimeId method of MachineDAOImpl ");
			}
		}
		return componentDowntimeId;
	
	}*/

	private String getMachineDowntimeReasonId(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering getMachineDowntimeReasonId method of MachineDAOImpl ");
		}
		String selectQuery = MachineDowntimeDetailQuery
				.selectDowntimeReasonId(machineReqBean.getDowntimeReason()).toString();
		String downtimeReasonId = null;
		try {
			downtimeReasonId = jdbcTemplate.queryForObject(selectQuery, 
					String.class);
		} catch (DataAccessException e) {
				LOGGER.error(" DataAccessException occoured at getMachineDowntimeReasonId method of MachineDAOImpl - "
						, e);
		} finally {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting getMachineDowntimeReasonId method of MachineDAOImpl ");
			}
		}
		return downtimeReasonId;
	}

	/**
	 * This method updates existing data with data from JSON in
	 * OM_COMPONENT_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 */
	private boolean updateMediaDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering updateMediaDwntmDtls method of MachineDAOImpl ");
		boolean status = false;

		String workQueuestatus = machineReqBean.getWorkqueueStatus();
		final int activeInd = getActiveIndicator(machineReqBean);
		String updateQuery = null;
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		int processedInd = PhotoOmniConstants.PROCESSED_IND;
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			updateQuery = MachineDowntimeDetailQuery.editQueryMediaDwntm()
					.toString();
			Object params[] = new Object[] { machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK, startDttm,
					completedDttm,
					machineReqBean.getEmployeeId().toUpperCase(), activeInd,
					machineReqBean.getDowntimeReason(),
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate, processedInd,
					machineReqBean.getComponentDowntimeId(), machineReqBean.getEquipmentInstanceId() };
				jdbcTemplate.update(updateQuery, params);

				status = true;
		} else if (workQueuestatus
				.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			updateQuery = MachineDowntimeDetailQuery
					.completedStatusQueryMediaDwntm().toString();
			Object params[] = new Object[] { machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK, startDttm,
					completedDttm,
					machineReqBean.getEmployeeId().toUpperCase(), activeInd,
					machineReqBean.getDowntimeReason(),
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate, processedInd,
					machineReqBean.getComponentDowntimeId(), machineReqBean.getEquipmentInstanceId() };
				jdbcTemplate.update(updateQuery, params);

			status = true;
		}
		
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting updateMediaDwntmDtls method of MachineDAOImpl ");
		return status;
	}

	/**
	 * This method checks in OM_COMPONENT_DOWNTIME table whether the MEDIA data
	 * exists or not
	 * 
	 * @param machineReqBean
	 * @return flag(0 or 1)
	 */
	private int checkIfMediaDataAlreadyExists(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering checkIfMediaDataAlreadyExists method of MachineDAOImpl ");
		int flag;
		String checkQuery = MachineDowntimeDetailQuery.checkQueryMediaDataExists().toString();
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(checkQuery, new Object[] { machineReqBean.getComponentDowntimeId(), machineReqBean.getEquipmentInstanceId() }, Integer.class);
		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" DataAccessException occoured at checkIfMediaDataAlreadyExists method of MachineDAOImpl - ", e);
			}
		}
		if (count > 0)
			flag = 1; // Data already exists
		else
			flag = 0; // Data doesnot exists
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting checkIfMediaDataAlreadyExists method of MachineDAOImpl ");
		return flag;
	}

	/**
	 * This method updates existing data with data from JSON in
	 * OM_EQUIPMENT_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 */
	private boolean updateEquipmentDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering updateEquipmentDwntmDtls method of MachineDAOImpl ");
		boolean status = false;
		final String equipmentInstanceId = machineReqBean.getEquipmentInstanceId();
		final int activeInd = getActiveIndicator(machineReqBean);
		final String workQueuestatus = machineReqBean.getWorkqueueStatus();
		String updateQuery = null;
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		int processedInd = PhotoOmniConstants.PROCESSED_IND;
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			updateQuery = MachineDowntimeDetailQuery.editQueryEquipmentDwntm().toString();
			jdbcTemplate.update( updateQuery,
					new Object[] {
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, 
							startDttm, completedDttm,
							machineReqBean.getEmployeeId().toUpperCase(), activeInd,
							machineReqBean.getDowntimeReason(),
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate, processedInd,
							equipmentInstanceId });

			status = true;
		} else if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			updateQuery = MachineDowntimeDetailQuery.completedStatusQueryEquipmentDwntm().toString();
			jdbcTemplate.update( updateQuery,
					new Object[] {
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, 
							startDttm, completedDttm,
							machineReqBean.getEmployeeId().toUpperCase(), activeInd,
							machineReqBean.getDowntimeReason(),
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate, processedInd,
							equipmentInstanceId });

			status = true;
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting updateEquipmentDwntmDtls method of MachineDAOImpl ");
		return status;
	}

	/**
	 * This method checks in OM_EQUIPMENT_DOWNTIME table whether the EQUIPMENT
	 * data exists or not
	 * 
	 * @param machineReqBean
	 * @return flag(0 or 1)
	 */
	private int checkIfEquipmentDataAlreadyExists(
			MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering checkIfEquipmentDataAlreadyExists method of MachineDAOImpl ");
		}
		int flag = 0;
		String checkQuery = MachineDowntimeDetailQuery
				.checkQueryEquipmentDataExists().toString();
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(checkQuery,
					new Object[] { machineReqBean.getEquipmentInstanceId() }, Integer.class);
		} catch (DataAccessException e) {
				LOGGER.error(" DataAccessException occoured at checkIfEquipmentDataAlreadyExists method of MachineDAOImpl - "
						, e);
		}
		if (count == 0) {
			flag = 0;
		} else {
			flag = 1;
		}
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting checkIfEquipmentDataAlreadyExists method of MachineDAOImpl ");
		}
		return flag;
	}

	/**
	 * This method updates the existing data with new data in
	 * OM_MACHINE_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 */
	private boolean updateMachineDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering updateMachineDwntmDtls method of MachineDAOImpl ");
		boolean status = false;
		String machineInstanceId = machineReqBean.getMachineInstanceId();
		int activeInd = getActiveIndicator(machineReqBean);
		String workQueuestatus = machineReqBean.getWorkqueueStatus();
		String updateQuery = null;
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			updateQuery = MachineDowntimeDetailQuery.editQueryMachineDwntm().toString();
			Object params[] = new Object[] { machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK, startDttm,
					completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
					activeInd,
					machineReqBean.getDowntimeReason(),
					PhotoOmniConstants.DEFUALT_USER_ID, 
					currentSysDate,
					PhotoOmniConstants.PROCESSED_IND,
					machineInstanceId };
					jdbcTemplate.update(updateQuery, params);
			status = true;
		} else if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			updateQuery = MachineDowntimeDetailQuery.completedStatusQueryMachineDwntm().toString();
			Object params[] = new Object[] { machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK, startDttm,
					completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
					activeInd,
					machineReqBean.getDowntimeReason(),
					PhotoOmniConstants.DEFUALT_USER_ID, 
					currentSysDate,
					PhotoOmniConstants.PROCESSED_IND,
					machineInstanceId };
					jdbcTemplate.update(updateQuery, params);
					
			status = true;
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting updateMachineDwntmDtls method of MachineDAOImpl ");
		return status;
	}

	/**
	 * This method checks in OM_MACHINE_DOWNTIME table whether the machine data
	 * exists or not
	 * 
	 * @param machineReqBean
	 * @return flag(0 or 1)
	 */
	private int checkIfMachineDataAlreadyExists(
			MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering checkIfMachineDataAlreadyExists method of MachineDAOImpl ");
		String machineInstanceId = machineReqBean.getMachineInstanceId();
		int flag = 0;
		String checkQuery = MachineDowntimeDetailQuery
				.checkQueryMachineDataExists().toString();

		int count = 0;

		try {
			count = jdbcTemplate.queryForObject(checkQuery,
					new Object[] { machineInstanceId }, Integer.class);

		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" DataAccessException occoured at checkIfMachineDataAlreadyExists method of MachineDAOImpl - "
						, e);
			}
		}

		if (count == 0) {
			flag = 0;
		} else {
			flag = 1;
		}
		/*
		 * If the status is PROC and the data exists then data should be updated
		 * with the new entry, but if the status is DONE and the data exists
		 * then the new row should be inserted with the entry
		 */
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting checkIfMachineDataAlreadyExists method of MachineDAOImpl ");
		return flag;
	}

	/**
	 * This method inserts data from JSON in OM_COMPONENT_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 */
	private boolean insertMediaDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering insertMediaDwntmDtls method of MachineDAOImpl ");
		boolean status = false;
		String equipmentInstanceId = machineReqBean.getEquipmentInstanceId();
		int activeInd = getActiveIndicator(machineReqBean);
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		int processedInd = PhotoOmniConstants.PROCESSED_IND;
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		String insertQuery = null;
		String workQueuestatus = machineReqBean.getWorkqueueStatus();
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			insertQuery = MachineDowntimeDetailQuery
					.insertPROCQueryMediaDwntm().toString();
			jdbcTemplate.update(insertQuery, new Object[] {
					equipmentInstanceId,
					machineReqBean.getComponentDowntimeId(),
					machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK,
					startDttm, 
					completedDttm,
					machineReqBean.getEmployeeId().toUpperCase(), activeInd,
					machineReqBean.getDowntimeReason(), processedInd,
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });

			status = true;
		} else if (workQueuestatus
				.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			insertQuery = MachineDowntimeDetailQuery.inserDONEQueryMediaDwntm()
					.toString();
			jdbcTemplate.update(insertQuery, new Object[] {
					equipmentInstanceId,
					machineReqBean.getComponentDowntimeId(),
					machineReqBean.getDowntimeReasonId(),
					PhotoOmniConstants.BLANK,
					startDttm, 
					completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
					machineReqBean.getEmployeeId().toUpperCase(), activeInd,
					machineReqBean.getDowntimeReason(), processedInd,
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
					PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });

			status = true;
		}
		
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting insertMediaDwntmDtls method of MachineDAOImpl ");
		return status;
	}

	private String getProductComponentId(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering getActiveIndicator method of MachineDAOImpl ");
		String mediaId = null;
		String selectQuery = MachineDowntimeDetailQuery.getProductComponentIdQuery().toString();
		Object[] param = new Object[]{ machineReqBean.getMediaId() };
		try {
			mediaId = jdbcTemplate.queryForObject(selectQuery, param, 
					String.class);
		} catch (DataAccessException e) {
				LOGGER.error(" DataAccessException occoured at getMachineInstanceId method of MachineDAOImpl - "
						, e);
		} finally {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting getMachineInstanceId method of MachineDAOImpl ");
			}
		}
		return mediaId;
	
	
	}

	/**
	 * This method inserts data from JSON into OM_EQUIPMENT_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 */
	private boolean insertEquipmentDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering insertEquipmentDwntmDtls method of MachineDAOImpl ");
		boolean status = false;
		int activeInd = getActiveIndicator(machineReqBean);
		String insertQuery = null;
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		int processedInd = PhotoOmniConstants.PROCESSED_IND;
		String workQueuestatus = machineReqBean.getWorkqueueStatus();
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			insertQuery = MachineDowntimeDetailQuery.insertPROCQueryEquipmentDwntm().toString();
			jdbcTemplate.update( insertQuery,
					new Object[] { machineReqBean.getEquipmentInstanceId(),
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, startDttm,
							completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
							activeInd,
							machineReqBean.getDowntimeReason(), processedInd,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });

		status = true;
		} else if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			insertQuery = MachineDowntimeDetailQuery.insertDONEQueryEquipmentDwntm().toString();
			jdbcTemplate.update( insertQuery,
					new Object[] { machineReqBean.getEquipmentInstanceId(),
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, startDttm,
							completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
							machineReqBean.getEmployeeId().toUpperCase(), activeInd,
							machineReqBean.getDowntimeReason(), processedInd,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });

		status = true;
		}
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting insertEquipmentDwntmDtls method of MachineDAOImpl ");
		return status;
	}

	/**
	 * This method insert data in OM_MACHINE_DOWNTIME table
	 * 
	 * @param machineReqBean
	 * @return status
	 * @throws SQLException 
	 */
	private boolean insertMachineDwntmDtls(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering insertMachineDwntmDtls method of MachineDAOImpl ");
		}
		boolean status = false;
		int activeInd = getActiveIndicator(machineReqBean);
		String insertQuery = null;
		String workQueuestatus = machineReqBean.getWorkqueueStatus();
		Timestamp startDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getStartDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		Timestamp completedDttm = CommonUtil.convertStringToTimestamp(machineReqBean.getCompletedDttm(), PhotoOmniConstants.DATE_FORMAT_NINE);
		int processedInd = PhotoOmniConstants.PROCESSED_IND;
		Timestamp currentSysDate = new Timestamp(new Date().getTime());
		if (workQueuestatus.equalsIgnoreCase(PhotoOmniConstants.PROC)) {
			insertQuery = MachineDowntimeDetailQuery
					.insertPROCQueryMachineDwntm().toString();
			jdbcTemplate.update(
					insertQuery,
					new Object[] { machineReqBean.getMachineInstanceId(),
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, startDttm,
							completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
							activeInd,
							machineReqBean.getDowntimeReason(), processedInd,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });
		status = true;
		} else if (workQueuestatus
				.equalsIgnoreCase(PhotoOmniConstants.DONE)) {
			insertQuery = MachineDowntimeDetailQuery
					.insertDONEQueryMachineDwntm().toString();
			jdbcTemplate.update(
					insertQuery,
					new Object[] { machineReqBean.getMachineInstanceId(),
							machineReqBean.getDowntimeReasonId(),
							PhotoOmniConstants.BLANK, startDttm,
							completedDttm, machineReqBean.getEmployeeId().toUpperCase(),
							machineReqBean.getEmployeeId().toUpperCase(),
							activeInd,
							machineReqBean.getDowntimeReason(), processedInd,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate,
							PhotoOmniConstants.DEFUALT_USER_ID, currentSysDate });
		status = true;
		}
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Exiting insertMachineDwntmDtls method of MachineDAOImpl ");
		}
	 	return status;
	}

	/**
	 * This method checks WorkQueueStatus and return Active Indicator
	 * accordingly
	 * 
	 * @param machineReqBean
	 * @return active_Ind
	 */
	private int getActiveIndicator(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering getActiveIndicator method of MachineDAOImpl ");
		int activeInd;
		if (machineReqBean.getWorkqueueStatus().equalsIgnoreCase(
				PhotoOmniConstants.PROC)
				|| machineReqBean.getWorkqueueStatus().equalsIgnoreCase(
						PhotoOmniConstants.NEW))
			activeInd = PhotoOmniConstants.MACHINE_ACTIVE_CD_PROC;
		else
			activeInd = PhotoOmniConstants.MACHINE_ACTIVE_CD_DONE;
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Exiting getActiveIndicator method of MachineDAOImpl ");
		return activeInd;
	}

	/**
	 * This method return machine_instance_id after mapping machine_id with
	 * Machine_Number in OM_MACHINE_INSTANCE table
	 * 
	 * @param machineReqBean
	 * @return machine_instance_id
	 */
	private String getMachineInstanceId(MachineDowntimeBean machineReqBean) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Entering getMachineInstanceId method of MachineDAOImpl ");
		}
		String selectQuery = MachineDowntimeDetailQuery
				.selectMachineInstanceId().toString();
		Object[] param = new Object[]{ machineReqBean.getMachineId(),machineReqBean.getLocationNbr() };
		String machineInstanceId = null;
		try {
			machineInstanceId = jdbcTemplate.queryForObject(selectQuery, param, 
					String.class);
		} catch (DataAccessException e) {
				LOGGER.error(" DataAccessException occoured at getMachineInstanceId method of MachineDAOImpl - "
						, e);
		} finally {
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("Exiting getMachineInstanceId method of MachineDAOImpl ");
			}
		}
		return machineInstanceId;
	}

	/**
	 * This method return equipmentInstanceId after mapping equipmentId with
	 * Equipment_Number in OM_EQUIPMENT_INSTANCE table
	 * 
	 * @param machineReqBean
	 * @return equipmentInstanceId
	 */
	private String getEquipmentInstanceId(MachineDowntimeBean machineReqBean) {

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Entering getEquipmentInstanceId method of MachineDAOImpl ");
		String selectQuery = MachineDowntimeDetailQuery
				.selectEquipmentInstanceId().toString();
		String equipmentInstanceId = null;
		try {
			equipmentInstanceId = jdbcTemplate.queryForObject(selectQuery,
					String.class,  machineReqBean.getMachineInstanceId(),machineReqBean.getEquipmentId());
		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" DataAccessException occoured at getEquipmentInstanceId method of MachineDAOImpl - "
						, e);
			}
		} finally {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Exiting getEquipmentInstanceId method of MachineDAOImpl ");
		}
		return equipmentInstanceId;
	}

	/*private boolean checkDowntimeReasonId (MachineDowntimeBean machineReqBean){
		boolean checkId = false;
		String downtimeReasonId = machineReqBean.getDowntimeReasonId();
		String checkQuery = MachineDowntimeDetailQuery.checkDowntimeReasonId().toString();
		int count = 0;

		try {
			count = jdbcTemplate.queryForObject(checkQuery,
					new Object[] { downtimeReasonId }, Integer.class);
		} catch (DataAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error(" DataAccessException occoured at checkDowntimeReasonId method of MachineDAOImpl - "
						, e);
			}
		}
		if (count == 0) {
			checkId = false;
		} else {
			checkId = true;
		}
		return checkId;
	}*/

}
