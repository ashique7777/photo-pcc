/**
 * MachineReportExcel.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
* Modification Log
*-----------------------------------------------------------------------------------------------
*   Ver             Date            Modified By         Description
*-----------------------------------------------------------------------------------------------
*  <1.1>     	 19 Mar 2015
*  
**/
package com.walgreens.admin.excel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.admin.bean.MachineFilter;
import com.walgreens.admin.json.bean.MachineReportBean;
import com.walgreens.admin.json.bean.ResponseMachineData;
import com.walgreens.admin.json.bean.Store;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

/**
 * This class is used to generate the Excel for Machine down time reports 
 * @author CTS
 * @version 1.1 Mar 19, 2015
 */


public class MachineReportExcel {
	
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MachineReportExcel.class);

	/**
	 * This method builds the content for the excel.
	 * @param request contains request value.
	 * @param response contains response value.
	 * @param machineReportData contains machineReportData data.
	 * @param machineReportReqBean contains machineReportReqBean (Filter data).
	 * @throws PhotoOmniException 
	 */
	public void buildExcelDocument(final HttpServletRequest request, final HttpServletResponse response, 
			final MachineReportBean machineReportData, final MachineFilter machineReportReqBean) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering buildExcelDocument method of MachineReportExcel ");
		}
		try {
			List<Store> storeList = null;
			Store storeData = null;
			HSSFWorkbook workbook = new HSSFWorkbook();
			final HSSFSheet sheet = workbook.createSheet("Machine Downtime Report");
			sheet.setDefaultColumnWidth(23);
			final CellStyle style = workbook.createCellStyle();
			final Font font = workbook.createFont();
			font.setFontName("Arial");
			style.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			this.getBorder(style);
			font.setColor(HSSFColor.WHITE.index);
			style.setFont(font);
			/* Creates Top header content*/
			this.getReportNameHeader(sheet, workbook);
			/* creates filter content*/
			this.getFilterContent(machineReportReqBean, sheet, workbook);
			
			if (!CommonUtil.isNull(machineReportData)) {
				storeList = machineReportData.getStore();
				if (!CommonUtil.isNull(storeList) && storeList.size() > 0 ) {
					storeData = storeList.get(0);
				}
			}
			this.getExcelContent(storeData, sheet, style, workbook);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at buildExcelDocument method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting buildExcelDocument method of MachineReportExcel ");
			}
		}
    }
	
	/**
	 * This method creates the Top header content for the excel.
	 * @param sheet contains sheet value.
	 * @param style contains style value.
	 * @throws PhotoOmniException 
	 */
	private void getReportNameHeader(final HSSFSheet sheet, final HSSFWorkbook workbook) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getReportNameHeader method of MachineReportExcel ");
		}
        try {
        	final CellStyle styleTopHead = workbook.createCellStyle();
        	final Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setColor(HSSFColor.WHITE.index);
            styleTopHead.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        	this.getBorder(styleTopHead);
        	styleTopHead.setFont(font);
        	
        	final HSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Machine DownTime Report");
			header.getCell(0).setCellStyle(styleTopHead);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getReportNameHeader method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getReportNameHeader method of MachineReportExcel ");
			}
		}
	}
	
	/**
	 * This method creates the filer content for the excel(Searched filter )
	 * @param machineReportReqBean contains machineReportReqBean data.
	 * @param sheet contains sheet value.
	 * @param workbook contains workbook value.
	 * @throws PhotoOmniException. 
	 */
	private void getFilterContent(final MachineFilter machineReportReqBean, final HSSFSheet sheet, final HSSFWorkbook workbook)  throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getFilterContent method of MachineReportExcel ");
		}
		int rowCountFilter = 2;
        try {
        	final CellStyle styleFilter = workbook.createCellStyle();
        	final Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setColor(HSSFColor.WHITE.index);
            styleFilter.setFillForegroundColor(HSSFColor.BROWN.index);
        	this.getBorder(styleFilter);
        	styleFilter.setFont(font);
        	final CellStyle styleFilterData = workbook.createCellStyle();
        	this.getBorder(styleFilterData);
        	styleFilterData.setFillForegroundColor(HSSFColor.WHITE.index);
        	final Font fontForFilter = workbook.createFont();
        	fontForFilter.setFontName("Arial");
        	fontForFilter.setColor(HSSFColor.BLACK.index);
        	styleFilterData.setFont(fontForFilter);
        	
			if (!CommonUtil.isNull(machineReportReqBean)) {
				for (int i = 0; i < 5; i++) {
					final HSSFRow aRowFilter = sheet.createRow(rowCountFilter++);
					if (i == 1) {
						aRowFilter.createCell(0).setCellValue(" Start Date : ");
						aRowFilter.createCell(1).setCellValue(
								CommonUtil.stringDateFormatChange(machineReportReqBean.getStartDate(), 
										PhotoOmniConstants.DATE_FORMAT_THREE, PhotoOmniConstants.DATE_FORMAT_FIFTH));
						this.addStyleToFilterData(styleFilterData, aRowFilter);
					} else if (i == 2) {
						aRowFilter.createCell(0).setCellValue(" End Date : ");
						aRowFilter.createCell(1).setCellValue(
								CommonUtil.stringDateFormatChange(machineReportReqBean.getEndDate(), 
										PhotoOmniConstants.DATE_FORMAT_THREE, PhotoOmniConstants.DATE_FORMAT_FIFTH));
						this.addStyleToFilterData(styleFilterData, aRowFilter);
					} else if (i == 3) {
						aRowFilter.createCell(0).setCellValue(" Machine Type : ");
						aRowFilter.createCell(1).setCellValue(machineReportReqBean.getMachineName());
						this.addStyleToFilterData(styleFilterData, aRowFilter);
					} else if (i == 4) {
						aRowFilter.createCell(0).setCellValue(" Store No : ");
						aRowFilter.createCell(1).setCellValue(machineReportReqBean.getStoreId());
						this.addStyleToFilterData(styleFilterData, aRowFilter);
					} else if (i == 0) {
						aRowFilter.createCell(0).setCellValue(" FILTERS ");
						aRowFilter.getCell(0).setCellStyle(styleFilter);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getFilterContent method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getFilterContent method of MachineReportExcel ");
			}
		}
	}

	/**
	 * The method used to add style to filter data.
	 * @param styleFilterData contains styleFilterData value.
	 * @param aRowFilter contains aRowFilter value.
	 * @throws PhotoOmniException. 
	 */
	private void addStyleToFilterData(final CellStyle styleFilterData, final HSSFRow aRowFilter) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addStyleToFilterData method of MachineReportExcel ");
		}
		try {
			aRowFilter.getCell(0).setCellStyle(styleFilterData);
			aRowFilter.getCell(1).setCellStyle(styleFilterData);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addStyleToFilterData method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addStyleToFilterData method of MachineReportExcel ");
			}
		}
	}

	/**
	 * This method used to get the border.
	 * @param styleFilter contains styleFilter value.
	 * @throws PhotoOmniException. 
	 */
	@SuppressWarnings("static-access")
	private void getBorder(final CellStyle styleFilter) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getBorder method of MachineReportExcel ");
		}
		try {
			styleFilter.setFillPattern(CellStyle.SOLID_FOREGROUND);
			styleFilter.setBorderBottom(styleFilter.BORDER_MEDIUM);
			styleFilter.setBorderTop(styleFilter.BORDER_MEDIUM);
			styleFilter.setBorderRight(styleFilter.BORDER_MEDIUM);
			styleFilter.setBorderLeft(styleFilter.BORDER_MEDIUM);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getBorder method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getBorder method of MachineReportExcel ");
			}
		}
	}

	/**
	 * This method creates header content and row data for excel.
	 * @param storeData contains store date. 
	 * @param sheet contains sheet value.
	 * @param style contains sheet value.
	 * @throws PhotoOmniException. 
	 */
	private void getExcelContent(final Store storeData, final HSSFSheet sheet, final CellStyle style,
			final HSSFWorkbook workbook) throws PhotoOmniException{
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getExcelContent method of MachineReportExcel ");
		}
		try {
			final HSSFRow header = sheet.createRow(10);
			 
			header.createCell(0).setCellValue("Machine Name");
			header.getCell(0).setCellStyle(style);
			 
			header.createCell(1).setCellValue("Equipment Name");
			header.getCell(1).setCellStyle(style);
			 
			header.createCell(2).setCellValue("Component Name");
			header.getCell(2).setCellStyle(style);
			 
			header.createCell(3).setCellValue("Entered By");
			header.getCell(3).setCellStyle(style);
			 
			header.createCell(4).setCellValue("Start Time");
			header.getCell(4).setCellStyle(style);
			
			header.createCell(5).setCellValue("End Time");
			header.getCell(5).setCellStyle(style);
			
			header.createCell(6).setCellValue("Duration");
			header.getCell(6).setCellStyle(style);
			
			header.createCell(7).setCellValue("Reason");
			header.getCell(7).setCellStyle(style);
			
			final CellStyle styleData = workbook.createCellStyle();
        	this.getBorder(styleData);
        	styleData.setFillForegroundColor(HSSFColor.WHITE.index);
        	final Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setColor(HSSFColor.BLACK.index);
        	styleData.setFont(font);
        	
			int rowCount = 11;
			
			if (!CommonUtil.isNull(storeData.getData()) ) {
				for (ResponseMachineData responseMachineDataItr : storeData.getData()) {
					final HSSFRow aRow = sheet.createRow(rowCount++);
			        aRow.createCell(0).setCellValue(responseMachineDataItr.getMachineName());
			        aRow.createCell(1).setCellValue(responseMachineDataItr.getEquipmentName());
			        aRow.createCell(2).setCellValue(responseMachineDataItr.getComponentName());
			        aRow.createCell(3).setCellValue(responseMachineDataItr.getEnteredBy());
			        aRow.createCell(4).setCellValue(responseMachineDataItr.getStartTime());
			        aRow.createCell(5).setCellValue(responseMachineDataItr.getEndTime());
			        aRow.createCell(6).setCellValue(responseMachineDataItr.getDuration());
			        aRow.createCell(7).setCellValue(responseMachineDataItr.getReason());
			        
			        this.addStyleToDataColumn(styleData, aRow);
				}
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getExcelContent method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getExcelContent method of MachineReportExcel ");
			}
		}
	}

	/**
	 * This method use to add style to data column.
	 * @param styleData contains styleData value.
	 * @param aRow contains aRow value.
	 * @throws PhotoOmniException. 
	 */
	private void addStyleToDataColumn(CellStyle styleData, HSSFRow aRow) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering addStyleToDataColumn method of MachineReportExcel ");
		}
		try {
			aRow.getCell(0).setCellStyle(styleData);
			aRow.getCell(1).setCellStyle(styleData);
			aRow.getCell(2).setCellStyle(styleData);
			aRow.getCell(3).setCellStyle(styleData);
			aRow.getCell(4).setCellStyle(styleData);
			aRow.getCell(5).setCellStyle(styleData);
			aRow.getCell(6).setCellStyle(styleData);
			aRow.getCell(7).setCellStyle(styleData);
		} catch (Exception e) {
			LOGGER.error(" Error occoured at addStyleToDataColumn method of MachineReportExcel - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting addStyleToDataColumn method of MachineReportExcel ");
			}
		}
	}

}
