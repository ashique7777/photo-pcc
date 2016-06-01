/**
 * LicenseReportFilterBOImpl.java 
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
package com.walgreens.oms.bo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.LicenseContentFilter;
import com.walgreens.oms.bean.LicenseContentFilterRespBean;
import com.walgreens.oms.dao.LicenseReportFilterDAO;
import com.walgreens.oms.factory.OmsDAOFactory;

/**
 * This class is used to implement Business validation as per action 
 * @author CTS
 * @version 1.1 May 01, 2015
 *
 */


@Component("LicenseReportFilterBO")
public class LicenseReportFilterBOImpl implements LicenseReportFilterBO {
	
	/**
	 * omsDAOFactory
	 */
	@Autowired
	private OmsDAOFactory omsDAOFactory; 
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LicenseReportFilterBOImpl.class);
	
	
	/**
	 * This method call the Dao layer to save License content filter conditions.
	 * @param licenseReportReqBean contains license content filter data.
	 * @return respBean.
	 * @throws PhotoOmniException custom exception.
	 */
	public LicenseContentFilterRespBean submitLicenseReportFilterRequest(final LicenseContentFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitLicenseReportFilterRequest method of LicenseReportFilterBOImpl ");
		}
		LicenseReportFilterDAO licenseReportDAO = null;
		final StringBuilder message = new StringBuilder();
		LicenseContentFilterRespBean respBean = null;
		int updateStatus = 0;
		String filterState = null;
		try {
			 if (!CommonUtil.isNull(reqBean)) {
				 final String emailId = CommonUtil.extensionAppenderForEmail(reqBean.getEmailIds());
				 reqBean.setEmailIds(emailId);
				 filterState = this.getJsonStringFormatForLCFilter(reqBean);
				 licenseReportDAO = omsDAOFactory.getLicenseReportFilterDAO();
				 final Integer sysReportId = licenseReportDAO.getReportIdForAdhocAndException();
				 updateStatus  = licenseReportDAO.submitLicenseReportFilterRequest(filterState, sysReportId);
				 respBean = new LicenseContentFilterRespBean();
				 if (updateStatus == 1) {
					 final Integer lastUserRepPrefId = licenseReportDAO.getLastEnteredDataForLicenseCont(sysReportId);
					 if (!CommonUtil.isNull(lastUserRepPrefId)) {
						 respBean.setResult("true");
						 message.append(PhotoOmniConstants.FILTER_DATA_SAVE_SUCCESS_MSG_PREF);
						 message.append(lastUserRepPrefId);
						 message.append(PhotoOmniConstants.FILTER_DATA_SAVE_SUCCESS_MSG_SUFIX);
						 respBean.setMessage(message.toString());
					    }
				 } else {
					 respBean.setResult("false");
					 respBean.setMessage(PhotoOmniConstants.FILTER_DATA_SAVE_ERROR_MSG);
				 }
			 }
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LicenseReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitLicenseReportFilterRequest method of LicenseReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitLicenseReportFilterRequest method of LicenseReportFilterBOImpl ");
			}
		}
		return respBean;	
	}

	/**
	 * this method create the json format for filter state.
	 * @param reqBean contains requested parameter.
	 * @return strLcFilter.
	 * @throws PhotoOmniException custom exception.
	 */
	public String getJsonStringFormatForLCFilter(final LicenseContentFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getJsonStringFormatForLCFilter method of LicenseReportFilterBOImpl ");
		}
		ObjectWriter ow = null;
		String strLcFilter = null;
		try {
			ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			strLcFilter = ow.writeValueAsString(reqBean);
		} catch (JsonProcessingException e) {
			LOGGER.error(" Error occoured at getJsonStringFormatForLCFilter method of LicenseReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) { 
			LOGGER.error(" Error occoured at getJsonStringFormatForLCFilter method of LicenseReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getJsonStringFormatForLCFilter method of LicenseReportFilterBOImpl ");
			}
		}
		return strLcFilter;
	}


}