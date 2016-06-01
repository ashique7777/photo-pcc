/**
 * LicenseReportFilterDAOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 01 May 2015
 *  
 **/
package com.walgreens.oms.dao;

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
import com.walgreens.oms.query.LicenseContentReportQuery;
/**
 * This class is used to save/Get the data to the underlying database.
 * @author CTS
 * @version 1.1 May 01, 2015
 *
 */

@Component("LicenseReportFilterDAO")
@SuppressWarnings("deprecation")
public class LicenseReportFilterDAOImpl  implements LicenseReportFilterDAO {
	
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LicenseReportFilterDAOImpl.class);
	
	/**
	 * This method save License content filter conditions.
	 * @param filterState contains filter state vale.
	 * @param sysReportId contains System Report Id.
	 * @return status.
	 * @throws PhotoOmniException custom exception.
	 */
	public int submitLicenseReportFilterRequest(final String filterState, final int sysReportId) throws PhotoOmniException {
		 if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Entering submitLicenseReportFilterRequest method of LicenseReportFilterDAOImpl ");
			}
		int status=0;
		try {
			final int sysUsrId = this.getSysUserId();
			final String sqlQuery = LicenseContentReportQuery.getLicenseReportInsertQuery().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" SYSUserId of the logged in user is : "+ sysUsrId);
				LOGGER.debug(" license Content Report Insert SQL Query : "+ sqlQuery);
			}
			final Object[] params = {sysUsrId, sysReportId, "BATCH", filterState, 
					PhotoOmniConstants.INDICATOR_YES, 1, 1, 0, 1, sysUsrId, sysUsrId};
			status = getJdbcTemplate().update(sqlQuery, params);
			
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitLicenseReportFilterRequest method of LicenseReportFilterDAOImpl ");
			}
		}
		return status;
	}

	/**
	 * This method get SYs report id for License Content Adhoc and Exception report.
	 * @return sysReportId.
	 * @exception PhotoOmniException.
	 */
	public Integer getReportIdForAdhocAndException() throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportIdForAdhocAndException method of LicenseReportFilterDAOImpl ");
		}
		Integer sysReportId = null;
		try {
			final String sqlQuery = LicenseContentReportQuery.getReportIdForAdhocAndExeception().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" license Content Report SQL Query to get sys report id "+ sqlQuery);
			}
			sysReportId = (Integer)getJdbcTemplate().queryForInt(sqlQuery, PhotoOmniConstants.ADHOC_REPORT_COLUMN);
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getReportIdForAdhocAndException method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportIdForAdhocAndException method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportIdForAdhocAndException method of LicenseReportFilterDAOImpl ");
			}
		}
		return sysReportId;
	}
	
	/**
	 * This method get last entered License Content SYS_USER_REPORT_PREF_ID.
	 * @param reportId contains print Sign report id. 
	 * @return lastUserRepPrefId.
	 */
	public Integer getLastEnteredDataForLicenseCont(final int reportId) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getLastEnteredDataForLicenseCont method of LicenseReportFilterDAOImpl ");
		}
		Integer lastUserRepPrefId = null;
		try {
			final String sqlQuery = LicenseContentReportQuery.getLastEnteredDataForLicenseCont().toString();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" license Content Report SQL Query for to get last inserted row "+ sqlQuery);
			}
			lastUserRepPrefId = (Integer)getJdbcTemplate().queryForInt(sqlQuery, reportId);
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getLastEnteredDataForLicenseCont method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getLastEnteredDataForLicenseCont method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getLastEnteredDataForLicenseCont method of LicenseReportFilterDAOImpl ");
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
			LOGGER.debug(" Entering getSysUserId method of LicenseReportFilterDAOImpl ");
		}
		int sysUserId = 0;
		try {
			final ExpiringUsernameAuthenticationToken authentication = (ExpiringUsernameAuthenticationToken)
					SecurityContextHolder.getContext().getAuthentication();
			if (!CommonUtil.isNull(authentication)) {
				final String empID = ((SAMLUserDetails)authentication.getDetails()).getEmployeenumber();
				final String sqlQuery = LicenseContentReportQuery.getSysUserIdByOamId().toString();
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Get SYS_USER_ID by OAM id : " + sqlQuery);
				}
				final Object[] params = {empID};
				sysUserId = getJdbcTemplate().queryForInt(sqlQuery, params);
			}
		} catch (DataAccessException e) {
			LOGGER.error(" Error occoured at getSysUserId method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getSysUserId method of LicenseReportFilterDAOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getSysUserId method of LicenseReportFilterDAOImpl ");
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
	
