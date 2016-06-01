/**
 * MachineReportDAOImpl.java 
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
package com.walgreens.admin.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.bean.MachineReportResBean;
import com.walgreens.admin.json.bean.MachineType;
import com.walgreens.admin.query.MachineReportQuery;
import com.walgreens.admin.rowmapper.MachineReportRowMapper;
import com.walgreens.admin.rowmapper.MachineTypeRowMapper;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to save/Get the  data to the underlying database.
 * @author CTS
 * @version 1.1 January 22, 2015
 *
 */

@Component("MachineReportDAO")
public class MachineReportDAOImpl  implements MachineReportDAO {
	
	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	/**
	 * dataGuardJdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcDatGuard")
	private JdbcTemplate dataGuardJdbcTemplate;	

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineReportDAOImpl.class);
	
	/**
	 * This method handles database transaction to get machine type list.
	 * @param contains store number.
	 * @return machineList
	 */
	@Override
	public List<MachineType> getReportMachineTypDetails(final String storeNumber)  throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportMachineTypDetails method of MachineReportDAOImpl ");
		}
		List<MachineType> machineList = null;
		String sqlQuery = null;
		try {
			if (!CommonUtil.isNull(storeNumber)) {
				Object[] param = {storeNumber};
				sqlQuery = MachineReportQuery.getActiveMachineTypeForSpecStore().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Machine DownTime Report SQL Query to get machine type for specfic store is : "+ sqlQuery);
				}
				machineList = dataGuardJdbcTemplate.query(sqlQuery, param, new MachineTypeRowMapper());
			} else {
				sqlQuery = MachineReportQuery.getActiveMachineType().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Machine DownTime Report SQL Query to get machine type is : "+ sqlQuery);
				}
				machineList = dataGuardJdbcTemplate.query(sqlQuery, new MachineTypeRowMapper());
			}
			
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportMachineTypDetails method of MachineReportDAOImpl ");
			}
		}
		return machineList;

	}
	

	/**
	 * This method formulate the query params.
	 * @param reqBean contains request parameters.
	 * @param stores contains store no.
	 * @return queryParam.
	 */
	private Map<String, Object> getQueryParams(final MachineFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getQueryParams method of MachineReportDAOImpl ");
		}
		Map<String, Object> queryParam = null;
		try {
			final Map<String, Long> pageLimit = CommonUtil.getPaginationLimit(reqBean.getCurrentPageNo(),
					PhotoOmniConstants.MACHINE_DOWNTIME_PAGINATION_SIZE);
			if (!CommonUtil.isNull(pageLimit)) {
				queryParam = new HashMap<String, Object>(pageLimit);
			} else {
				/* This for is used for export */
				queryParam = new HashMap<String, Object>();
			}
			queryParam.put("START_DATE", reqBean.getStartDate());
			queryParam.put("END_DATE", reqBean.getEndDate());
			queryParam.put("MACHINE_TYPE", reqBean.getMachineId());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportMachineTypDetails method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getQueryParams method of MachineReportDAOImpl ");
			}
		}
		return queryParam;
	}


	/**
	 * This methods finds the machine report data from database.
	 * @param reqBean contains request parameters.
	 * @return machineReportResBeanList.
	 */
	@Override
	public List<MachineReportResBean> submitReportRequest(final MachineFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of MachineReportDAOImpl ");
		}
		List<MachineReportResBean> machineReportResBeanList = null;
		try {
			final String currentPage = reqBean.getCurrentPageNo();
			final Map<String, Object> queryParam = this.getQueryParams(reqBean);
			final String sqlQuery = MachineReportQuery.getReportData(reqBean);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Machine DownTime Report SQL Query is : " + sqlQuery);
			}
			final String startDate = CommonUtil.stringDateFormatChange(queryParam.get("START_DATE").toString(),
					PhotoOmniConstants.DATE_FORMAT_SEVEN, PhotoOmniConstants.DATE_FORMAT_TWO);
			final String endDate = CommonUtil.stringDateFormatChange(queryParam.get("END_DATE").toString(),
					PhotoOmniConstants.DATE_FORMAT_SEVEN, PhotoOmniConstants.DATE_FORMAT_TWO);
			final String machineType = queryParam.get("MACHINE_TYPE").toString();

			if (!CommonUtil.isNull(currentPage) && !"".equals(currentPage)) {
				/* For Machine Down Time UI View */
				final String startLimit = !CommonUtil.isNull(queryParam.get("START_LIMIT")) ? queryParam.get(
						"START_LIMIT").toString() : "";
				final String endLimit = !CommonUtil.isNull(queryParam.get("END_LIMIT")) ? queryParam.get("END_LIMIT")
						.toString() : "";
				if (!CommonUtil.isNull(machineType) && !"".equals(machineType)) {
					/* When Machine Type is Selected */
					final Object[] params = { machineType, startDate, endDate, startLimit, endLimit };
					machineReportResBeanList = dataGuardJdbcTemplate.query(sqlQuery, params,
							new MachineReportRowMapper());
				} else {
					/* When Machine Type is not Selected. For all machine type */
					final Object[] params = { startDate, endDate, startLimit, endLimit };
					machineReportResBeanList = dataGuardJdbcTemplate.query(sqlQuery, params,
							new MachineReportRowMapper());
				}
			} else {
				/* For Machine Down Time Excel */
				if (!CommonUtil.isNull(machineType) && !"".equals(machineType)) {
					/* When Machine Type is Selected */
					final Object[] params = { machineType, startDate, endDate };
					machineReportResBeanList = dataGuardJdbcTemplate.query(sqlQuery, params,
							new MachineReportRowMapper());
				} else {
					/* When Machine Type is not Selected. For all machine type */
					final Object[] params = { startDate, endDate };
					machineReportResBeanList = dataGuardJdbcTemplate.query(sqlQuery, params,
							new MachineReportRowMapper());
				}
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitReportRequest method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of MachineReportDAOImpl ");
			}
		}

		return machineReportResBeanList;
	}
	
	
	/**
	 * This method returns the store address
	 * @param locId contains store number.
	 * @return StoreAddress.
	 * @throws PhotoOmniException 
	 */
	@Override
	public String getLocationAddress(final String locId) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into getLocationAddress method of MachineReportDAOImpl ");
		}
		String StrStoreAddress = null;
		try {
			final String sqlQuery = MachineReportQuery.getLocationAddresQuery().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Sql Query to find the address of a store " + sqlQuery);
			}
			final Object param[] = { locId };
			final List<String> StoreAddress = this.dataGuardJdbcTemplate.queryForList(sqlQuery, param, String.class);
			if (!CommonUtil.isNull(StoreAddress) && StoreAddress.size() > 0) {
				StrStoreAddress = StoreAddress.get(0);
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getLocationAddress method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getLocationAddress method of MachineReportDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting from getLocationAddress method of MachineReportDAOImpl ");
			}
		}
		return StrStoreAddress;
	}

	/**
	 * @return the jdbcTemplate
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	/**
	 * @param jdbcTemplate the jdbcTemplate to set
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * @param dataGuardJdbcTemplate
	 */
	public void setDataGuardJdbcTemplate(JdbcTemplate dataGuardJdbcTemplate) {
		this.dataGuardJdbcTemplate = dataGuardJdbcTemplate;
	}
	
	
}
