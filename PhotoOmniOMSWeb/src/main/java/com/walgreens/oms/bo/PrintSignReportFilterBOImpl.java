/**
 * PrintSignReportFilterBOImpl.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 23 Apr 2015
 *  
 **/
package com.walgreens.oms.bo;

import java.util.List;

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
import com.walgreens.oms.bean.PrintSignFilter;
import com.walgreens.oms.bean.PrintSignFilterRespBean;
import com.walgreens.oms.dao.PrintSignReportFilterDAO;
import com.walgreens.oms.factory.OmsDAOFactory;
import com.walgreens.oms.json.bean.EventData;
import com.walgreens.oms.json.bean.EventDataFilter;
import com.walgreens.oms.json.bean.PrintableSignFilter;

/**
 * This class is used to implement Business validation as per action 
 * @author CTS
 * @version 1.1 Apr 23, 2015
 *
 */


@Component("PrintSignReportFilterBO")
public class PrintSignReportFilterBOImpl implements PrintSignReportFilterBO {
	
	/**
	 * omsDAOFactory
	 */
	@Autowired
	private OmsDAOFactory omsDAOFactory; 

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PrintSignReportFilterBOImpl.class);
	
	
	/**
	 * This method call the Dao layer and gets the Event list to get all the searched eventlist.
	 * @param filter contains event data value. 
	 * @return eventData contains searched event data.
	 */
	public EventDataFilter  getEventTypDetails(final PrintSignFilter filter) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getEventTypDetails method of PrintSignReportFilterBOImpl ");
		}
		List<EventData> eventList = null;
		final EventDataFilter eventData = new EventDataFilter();
		String eventName = null;
		try {
			eventName = filter.getEventName();
			if (!CommonUtil.isNull(eventName)) {
				eventName = eventName.trim();
				eventName = eventName.toUpperCase();
			}
			final PrintSignReportFilterDAO printSignReportDAO = omsDAOFactory.getpSReportDAO();
			eventList = printSignReportDAO.getEventTypDetails(eventName);
			if (eventList != null && eventList.size() > 0) {
				eventData.setEvent(eventList);
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getEventTypDetails method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getEventTypDetails method of PrintSignReportFilterBOImpl ");
			}
		}
		
		return eventData;
	}
	
	
	/**
	 * This method submit the data for Print signs report.
	 * @param reqBean contains front end data.
	 * @return status boolean value.
	 * Throws PhotoOmniException custom exception.
	 */
	public PrintSignFilterRespBean submitPSReportFilterRequest(final PrintableSignFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitPSReportFilterRequest method of PrintSignReportFilterBOImpl ");
		}
		final PrintSignFilterRespBean respBean = new PrintSignFilterRespBean();
		PrintSignReportFilterDAO pSReportDAO = null;
		final StringBuilder message = new StringBuilder();
		String filterState = null;
		int updateStatus = 0;
		try {
			 if (!CommonUtil.isNull(reqBean)) {
				 final String emailId = CommonUtil.extensionAppenderForEmail(reqBean.getEmailIds());
				 reqBean.setEmailIds(emailId);
				 filterState = this.getJsonStringFormatForPSFilter(reqBean);
				 pSReportDAO = omsDAOFactory.getpSReportDAO();
				 final Integer sysReportId = pSReportDAO.getReportIdForPrintSignReport();
				 updateStatus  = pSReportDAO.submitPSReportFilterRequest(filterState, sysReportId);
				 if (updateStatus == 1) {
					 final Integer lastUserRepPrefId = pSReportDAO.getLastEnteredDataForPrintSign(sysReportId);
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
		}  catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitPSReportFilterRequest method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitPSReportFilterRequest method of PrintSignReportFilterBOImpl ");
			}
		}
		return respBean;
	}
	
	/**
	 * This method creates convert the filter state data to json format.
	 * @param PSReportBean contains front end data.
	 * @return strLcFilter filter state data(Json String).
	 * @throws PhotoOmniException custom exception.
	 */
	public String getJsonStringFormatForPSFilter(final PrintableSignFilter reqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getJsonStringFormatForPSFilter method of PrintSignReportFilterBOImpl ");
		}
		ObjectWriter ow = null;
		String strLcFilter = null;
		try {
			ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			strLcFilter = ow.writeValueAsString(reqBean);
		} catch (JsonProcessingException e) {
			LOGGER.error(" Error occoured at getJsonStringFormatForPSFilter method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getJsonStringFormatForPSFilter method of PrintSignReportFilterBOImpl - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getJsonStringFormatForPSFilter method of PrintSignReportFilterBOImpl ");
			}
		}
		return strLcFilter;
	}

}