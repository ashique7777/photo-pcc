/**
 * PrintSignReportFilterDAOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 22 Apr 2015
 *  
 **/
package com.walgreens.oms.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.stereotype.Component;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.security.oam.SAMLUserDetails;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.EventData;
import com.walgreens.oms.query.PrintableSignReportQuery;
import com.walgreens.oms.rowmapper.EventTypeRowMapper;

/**
 * This class is used to save/Get the data to the underlying database.
 * @author CTS
 * @version 1.1 Apr 22, 2015
 *
 */

@Component("PrintSignReportFilterDAO")
@SuppressWarnings("deprecation")
public class PrintSignReportFilterDAOImpl  implements PrintSignReportFilterDAO {
	
	/**
	 * jdbcTemplate
	 */
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintSignReportFilterDAOImpl.class);
	
	ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
	
	/**
	 * This method handles database transaction to get event type list.
	 * @param reqEventName contains event type value which will be placed at like query. 
	 * @return eventList contains searched event list data.
	 */
	public List<EventData> getEventTypDetails(final String reqEventName)  throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getEventTypDetails method of PrintSignReportFilterDAOImpl ");
		}
		List<EventData> eventList = null;
		try {
			final String sqlQuery = PrintableSignReportQuery.getActiveEventType(reqEventName).toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Print Sign Report SQL Query for OM_SIGNS_HEADER table is : "+ sqlQuery);
			}
			eventList = getJdbcTemplate().query(sqlQuery, new EventTypeRowMapper());
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getEventTypDetails method of PrintSignReportFilterDAOImpl ");
			}
		}
		return eventList;
	}
	
	
	 /**
     * This method submit the data for Print signs report.
     * @param filterState contains filter state data.
     * @param sysReportId contains System report id.
     * @return status contains boolean status.
     * @throws PhotoOmniException custom exception.
     */
	public int submitPSReportFilterRequest(final String filterState, final int sysReportId) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitPSReportFilterRequest method of PrintSignReportFilterDAOImpl ");
		}
		int status=0;
		try {
			final int sysUsrId = this.getSysUserId();
			final String sqlQuery = PrintableSignReportQuery.getPrintableSignReportInsertQuery().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Print Sign Report SQL Insert Query : " + sqlQuery);
			}
			final Object[] params = { sysUsrId, sysReportId , "BATCH", filterState,
					PhotoOmniConstants.INDICATOR_YES, 1, 1, 0, 1, sysUsrId, sysUsrId};
			status = getJdbcTemplate().update(sqlQuery, params);
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitPSReportFilterRequest method of PrintSignReportFilterDAOImpl ");
			}
		}
		return status;
	}
	
	/**
	 * This method get SYS report id for Print Sign report.
	 * @return sysReportId.
	 * @exception PhotoOmniException.
	 */
	public Integer getReportIdForPrintSignReport() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportIdForPrintSignReport method of PrintSignReportFilterDAOImpl ");
		}
		Integer sysReportId = null;
		try {
			final String sqlQuery = PrintableSignReportQuery.getReportIdForPrintSignReport().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Print Sign Report SQL Query to get sys report id "+ sqlQuery);
			}
			sysReportId = (Integer)getJdbcTemplate().queryForInt(sqlQuery, PhotoOmniConstants.PRINT_SIRN_REPORT_COLUMN);
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getReportIdForPrintSignReport method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportIdForPrintSignReport method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportIdForPrintSignReport method of PrintSignReportFilterDAOImpl ");
			}
		}
		return sysReportId;
	}
	
	/**
	 * This method get last entered print sign SYS_USER_REPORT_PREF_ID.
	 * @param reportId contains print Sign report id. 
	 * @return lastUserRepPrefId.
	 */
	public Integer getLastEnteredDataForPrintSign(final int reportId) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getLastEnteredDataForPrintSign method of PrintSignReportFilterDAOImpl ");
		}
		Integer lastUserRepPrefId = null;
		try {
			final String sqlQuery = PrintableSignReportQuery.getLastEnteredDataForPrintSign().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Print Sign Report SQL Query for to get last inserted row "+ sqlQuery);
			}
			lastUserRepPrefId = (Integer)getJdbcTemplate().queryForInt(sqlQuery, reportId);
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getLastEnteredDataForPrintSign method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getLastEnteredDataForPrintSign method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getLastEnteredDataForPrintSign method of PrintSignReportFilterDAOImpl ");
			}
		}
		return lastUserRepPrefId;
	}
	
	
	/**
	 * This method find the sys_user_id by oamid.
	 * @return sysUserId.
	 * @throws PhotoOmniException custom exception.
	 */
	public int getSysUserId() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getSysUserId method of PrintSignReportFilterDAOImpl ");
		}
		int sysUserId = 0;
		try {
			final ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken) SecurityContextHolder
					.getContext().getAuthentication();
			if (!CommonUtil.isNull(authentication)) {
				final String empId = ((SAMLUserDetails)authentication.getDetails()).getEmployeenumber();;
				final String sqlQuery = PrintableSignReportQuery.getSysUserIdByOamId().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Get SYS_USER_ID by OAM id : " + sqlQuery);
				}
				final Object[] params = {empId};
				sysUserId = getJdbcTemplate().queryForInt(sqlQuery, params);
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getSysUserId method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSysUserId method of PrintSignReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSysUserId method of PrintSignReportFilterDAOImpl ");
			}
		}
		return sysUserId;
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

}
	
