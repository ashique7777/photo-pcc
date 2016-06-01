package com.walgreens.admin.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.admin.bean.KioskDetailBean;
import com.walgreens.admin.bean.KioskReportRequestBean;
import com.walgreens.admin.bean.KioskReportResponseBean;
import com.walgreens.admin.bo.KioskReportsBO;
import com.walgreens.admin.factory.AdminBOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.MessageHeader;

/**
 * The Class KioskReportsRestServiceImpl.
 * 
 * @author CTS
 */

@Controller
@RequestMapping(value = "/report")
public class KioskReportsRestServiceImpl implements KioskReportsRestService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(KioskReportsRestServiceImpl.class);

	/** The admin bo factory. */
	@Autowired
	private AdminBOFactory adminBOFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.service.KioskReportsRestService#getKioskReportData
	 * (com.walgreens.admin.bean.KioskReportRequestBean)
	 */
	@Override
	@RequestMapping(value = "/kiosk-data", method = RequestMethod.POST)
	@ResponseBody
	public KioskReportResponseBean getKioskReportData(
			@RequestBody KioskReportRequestBean params) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entered in getKioskReportData method of KioskReportsRestServiceImpl");
		}
		KioskReportResponseBean model = new KioskReportResponseBean();
		KioskReportsBO koiskReportBO = adminBOFactory.getKioskReportsBO();
		try {
			int page = params.getCurrentPage();
			int rows = koiskReportBO
					.getKioskPageCount(PhotoOmniConstants.KIOSK);
			List<KioskDetailBean> list = koiskReportBO.getKioskReportData(
					params.getFilter(), page, rows);
			int totalRecord = koiskReportBO.getReportDataCount(params
					.getFilter());
			int totalPages = 0;
			if (CollectionUtils.isNotEmpty(list)) {
				int a = totalRecord + (rows - (totalRecord % rows));
				totalPages = a / rows;
				if (totalRecord % rows == 0) {
					totalPages = totalPages - 1;
				}
			}
			model.setCurrentPage(page);
			model.setTotalPage(totalPages);
			model.setTotalRecord(totalRecord);
			model.setData(list);
			model.setMessageHeader(params.getMessageHeader());
			model.setReadableString(koiskReportBO.getFilterString(params
					.getFilter()));
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getKioskReportData method of KioskReportsRestServiceImpl - "
					+ e);
			model.setErrorDetails(CommonUtil
					.createFailureMessageForDBException(e));
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getKioskReportData method of KioskReportsRestServiceImpl ");
			}
		}
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.service.KioskReportsRestService#getKioskFilterOptions
	 * (javax.servlet.http.HttpServletRequest)
	 */
	@Override
	@RequestMapping(value = "/koisk-filters", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getKioskFilterOptions(HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entered in getKioskFilterOptions method of KioskReportsRestServiceImpl");
		}
		Map<String, Object> model = new HashMap<>();
		KioskReportsBO koiskReportBO = adminBOFactory.getKioskReportsBO();
		String header = request.getParameter(PhotoOmniConstants.HEADER);
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<Map<String, Object>> list = koiskReportBO
					.getKioskFilterOptions();
			model.put(PhotoOmniConstants.DATA, list);
			model.put(PhotoOmniConstants.MESSAGE_HEADER, mapper.readValue(
					new StringReader(header), MessageHeader.class));
		} catch (JsonParseException e) {
			model.put("ErrorDetails",
					CommonUtil.createFailureMessageForSystemException(e));
			LOGGER.error(" Error occoured while parsing the json - " + e);
		} catch (IOException e) {
			LOGGER.error(" Error occoured while preparing message header - "
					+ e);
			model.put("ErrorDetails",
					CommonUtil.createFailureMessageForSystemException(e));
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at getKioskFilterOptions method of KioskReportsRestServiceImpl - "
					+ e);
			model.put("ErrorDetails",
					CommonUtil.createFailureMessageForDBException(e));
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getKioskFilterOptions method of KioskReportsRestServiceImpl ");
			}
		}
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.service.KioskReportsRestService#exportToExcel(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(value = "/kiosk-export")
	public void exportToExcel(HttpServletRequest request,
			HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entered in exportToExcel method of KioskReportsRestServiceImpl");
		}
		KioskReportsBO koiskReportBO = adminBOFactory.getKioskReportsBO();
		String params = request.getParameter(PhotoOmniConstants.PARAM);
		ObjectMapper objectMapper = new ObjectMapper();
		KioskReportRequestBean kioskReportRequestBean;
		try {
			kioskReportRequestBean = objectMapper.readValue(new StringReader(
					params), KioskReportRequestBean.class);
			koiskReportBO.exportToExcel(response,
					kioskReportRequestBean.getFilter());
		} catch (JsonParseException e) {
			LOGGER.error(" Error occoured while parsing the json - " + e);
		} catch (IOException e) {
			LOGGER.error(" Error occoured while converting string params to kioskReportRequestBean bean - "
					+ e);
		} catch (PhotoOmniException e) {
			LOGGER.error(" Error occoured at exportToExcel method of KioskReportsRestServiceImpl - "
					+ e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting exportToExcel method of KioskReportsRestServiceImpl ");
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.walgreens.admin.service.KioskReportsRestService#printKioskRpt(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@RequestMapping(value = "/kiosk-print", method = RequestMethod.POST)
	@ResponseBody
	public Object printKioskRpt(HttpServletRequest request,
			HttpServletResponse response) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entered in printKioskRpt method of KioskReportsRestServiceImpl");
		}
		KioskReportsBO koiskReportBO = adminBOFactory.getKioskReportsBO();
		KioskReportResponseBean model = new KioskReportResponseBean();
		ObjectMapper objectMapper = new ObjectMapper();
		KioskReportRequestBean kioskReportRequestBean;
		try {
			int rows = koiskReportBO
					.getKioskPageCount(PhotoOmniConstants.KIOSK);
			String params = request.getParameter(PhotoOmniConstants.PARAM);
			kioskReportRequestBean = objectMapper.readValue(new StringReader(
					params), KioskReportRequestBean.class);
			int page = kioskReportRequestBean.getCurrentPage();
			List<KioskDetailBean> list = koiskReportBO.getKioskReportData(
					kioskReportRequestBean.getFilter(), page, rows);
			model.setData(list);
			model.setMessageHeader(kioskReportRequestBean.getMessageHeader());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at printKioskRpt method of KioskReportsRestServiceImpl - "
					+ e);
			model.setErrorDetails(CommonUtil
					.createFailureMessageForDBException(e));
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting exportToExcel method of KioskReportsRestServiceImpl ");
			}
		}
		return model;
	}
}