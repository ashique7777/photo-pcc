package com.walgreens.omni.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import com.walgreens.omni.bean.SimRetailBlockCSVBean;
import com.walgreens.omni.bo.ReportsBOImplTest;
import com.walgreens.omni.json.bean.SimRetailBlocOnloadList;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReq;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;

public class SubmitReportRequestImplTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubmitReportRequestImplTest.class);
	
	private ReportsBOImplTest reportsBOImplTest;

	private static final SimRetailBlockOnloadResp simRetailBlockOnloadRespExpected = new SimRetailBlockOnloadResp();
	private static final SimRetailBlocOnloadList simRetailBlocOnloadList = new SimRetailBlocOnloadList();
	private static final SimRetailBlockReportRespMsg simRetailBlockReportRespMsgExpected = new SimRetailBlockReportRespMsg();
	private static final SimRetailBlockReportResp simRetailBlockReportResp = new SimRetailBlockReportResp();
	private static final SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg = new SimRetailBlockUpdateReqMsg();
	private static final SimRetailBlockUpdateReq simRetailBlockUpdateReq = new SimRetailBlockUpdateReq();
	private static final SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgExpected = new SimRetailBlockUpdateRespMsg();
	private static final ModelAndView modelExpected = new ModelAndView("DownloadSimRetailBlockCSV");
	private static final SimRetailBlockCSVBean simRetailBlockCSVBean = new SimRetailBlockCSVBean();

	/**
	 * Method to test setUp Mock up Test Data
	 */
	@Before
	public void setUp() {
		
		reportsBOImplTest = createMock(ReportsBOImplTest.class);

		simRetailBlocOnloadList.setRetailBlock("30");
		List<SimRetailBlocOnloadList> SimRetailBlocOnloadListTest = new ArrayList<SimRetailBlocOnloadList>();
		SimRetailBlocOnloadListTest.add(simRetailBlocOnloadList);
		simRetailBlockOnloadRespExpected.setSimRetailBlocOnloadList(SimRetailBlocOnloadListTest);

		simRetailBlockReportResp.setDescription("US");
		simRetailBlockReportResp.setRetailBlock("30");
		simRetailBlockReportResp.setStoreNumber("59156");
		simRetailBlockReportResp.setTotalrecords("1");
		List<SimRetailBlockReportResp> simRetailBlockReportRespList = new ArrayList<SimRetailBlockReportResp>();
		simRetailBlockReportRespList.add(simRetailBlockReportResp);
		simRetailBlockReportRespMsgExpected.setSimRetailBlockReportResp(simRetailBlockReportRespList);

		simRetailBlockUpdateReq.setRetailBlock("30");
		List<String> storeNumber = new ArrayList<String>();
		storeNumber.add("59156");
		simRetailBlockUpdateReq.setStoreNumber(storeNumber);
		simRetailBlockUpdateReqMsg.setSimRetailBlockUpdateReq(simRetailBlockUpdateReq);
		simRetailBlockUpdateRespMsgExpected.setUpdateStatusMessage("Update sucessfull");
		
		String[] header = { "StoreNumber", "RetailBlock", "Description" };
		simRetailBlockCSVBean.setDescription("US");
		simRetailBlockCSVBean.setRetailBlock("30");
		simRetailBlockCSVBean.setStoreNumber("59156");
		List<SimRetailBlockCSVBean> simRetailBlockCSVBeanList = new ArrayList<SimRetailBlockCSVBean>();
		simRetailBlockCSVBeanList.add(simRetailBlockCSVBean);
		modelExpected.addObject("csvHeader", header);
		modelExpected.addObject("csvData", simRetailBlockCSVBeanList);

		expect(reportsBOImplTest.updateRetailBlockRequest()).andReturn(simRetailBlockUpdateRespMsgExpected);
		expect(reportsBOImplTest.genarateSimRetailBlockReport()).andReturn(simRetailBlockReportRespMsgExpected);
		expect(reportsBOImplTest.submitSimRetailBlockOnloadRequest()).andReturn(simRetailBlockOnloadRespExpected);
		
		replay(reportsBOImplTest);
	}

	/**
	 * Method to test testSubmitSimRetailBlockOnloadRequest()
	 */
	@Test()
	public void testSubmitSimRetailBlockOnloadRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering testSubmitSimRetailBlockOnloadRequest method of SubmitReportRequestImplTest ");
		}
		SimRetailBlockOnloadResp simRetailBlockOnloadRespActual = new SimRetailBlockOnloadResp();
		try {
			simRetailBlockOnloadRespActual = reportsBOImplTest.submitSimRetailBlockOnloadRequest();
		} catch (Exception e) {
			LOGGER.error(" Error occoured at testSubmitSimRetailBlockOnloadRequest method of SubmitReportRequestImplTest - "+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testSubmitSimRetailBlockOnloadRequest method of SubmitReportRequestImplTest ");
			}
		}
		assertEquals(simRetailBlockOnloadRespExpected,simRetailBlockOnloadRespActual);
	}

	
	/**
	 * Method to test testGenarateSimRetailBlockReport()
	 */
	@Test
	public void testGenarateSimRetailBlockReport() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering testGenarateSimRetailBlockReport method of SubmitReportRequestImpl ");
		}

		File file = new File("V:\\Users\\dassuv\\Desktop\\storeNumbers.xlsx");
		String number = "59156";

		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		List<String> locationNoList = new ArrayList<String>();

		/** checks if the upload file is null */
		if (CommonUtil.isNull(file)) {
			locationNoList.add(number);
		} else {
			try {

				/** validation for .xlsx data started */
				FileInputStream fis = new FileInputStream(file);
				XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);
				Iterator<Row> rowIterator = mySheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							locationNoList.add(cell.toString());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							String temp = cell.toString().replace(".0", "");
							/**
							 * Regular expression to match store no e.g
							 * 4,45,115,4855,85475 etc
							 */
							Pattern p1 = Pattern.compile("[0-9]{1,5}$");
							Matcher m1 = p1.matcher(temp);
							if (temp.isEmpty() || !m1.matches()) {
								throw new Exception();
							}
							locationNoList.add(temp);
							break;
						default:
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at testGenarateSimRetailBlockReport method of SubmitReportRequestImplTest - "+ e.getMessage());
				ErrorDetails errorDetails = new ErrorDetails();
				errorDetails.setErrorString("Please upload vaild *.xlsx file");
				simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
				
				assertEquals(simRetailBlockReportRespMsg.getErrorDetails().getErrorString().toString(),"Please upload vaild *.xlsx file");
			}
		}

		/** Process to remove duplicates from list */
		Set<String> tempSet = new HashSet<>();
		tempSet.addAll(locationNoList);
		locationNoList.clear();
		locationNoList.addAll(tempSet);

		assertEquals("59156", locationNoList.get(0).toString());

		try {
			simRetailBlockReportRespMsg = reportsBOImplTest.genarateSimRetailBlockReport();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(" Error occoured at testGenarateSimRetailBlockReport method of SubmitReportRequestImpl - "+ e.getMessage());
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testGenarateSimRetailBlockReport method of SubmitReportRequestImpl ");
			}
		}
		assertEquals(simRetailBlockReportRespMsgExpected,simRetailBlockReportRespMsg);
	}

	
	/**
	 * Method to test testUpdateRetailBlockRequest()
	 */
	@Test
	public void testUpdateRetailBlockRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.error("Entering testUpdateRetailBlockRequest method of SubmitReportRequestImpl ");
		}
		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsgActual = new SimRetailBlockUpdateRespMsg();
		try {
			simRetailBlockUpdateRespMsgActual = reportsBOImplTest.updateRetailBlockRequest();
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.error(" Error occoured at testUpdateRetailBlockRequest method of SubmitReportRequestImpl - "+ e.getMessage());
			}
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testUpdateRetailBlockRequest method of SubmitReportRequestImpl ");
			}
		}
		assertEquals(simRetailBlockUpdateRespMsgExpected,simRetailBlockUpdateRespMsgActual);
	}

	/**
	 * Method to test testDownloadSimRetailBlockCSV()
	 */
	@Test
	public void testDownloadSimRetailBlockCSV() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.error("Entering testDownloadSimRetailBlockCSV method of SubmitReportRequestImpl ");
		}
		ModelAndView modelActual = new ModelAndView("DownloadSimRetailBlockCSV");

		File file = new File("V:\\Users\\dassuv\\Desktop\\storeNumbers.xlsx");
		String number = "59156";

		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		List<String> locationNoList = new ArrayList<String>();

		/** checks if the upload file is null */
		if (CommonUtil.isNull(file)) {
			locationNoList.add(number);
		} else {
			try {

				/** validation for .xlsx data started */
				FileInputStream fis = new FileInputStream(file);
				XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
				XSSFSheet mySheet = myWorkBook.getSheetAt(0);
				Iterator<Row> rowIterator = mySheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					Iterator<Cell> cellIterator = row.cellIterator();
					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							locationNoList.add(cell.toString());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							String temp = cell.toString().replace(".0", "");
							/**
							 * Regular expression to match store no e.g
							 * 4,45,115,4855,85475 etc
							 */
							Pattern p1 = Pattern.compile("[0-9]{1,5}$");
							Matcher m1 = p1.matcher(temp);
							if (temp.isEmpty() || !m1.matches()) {
								throw new Exception();
							}
							locationNoList.add(temp);
							break;
						default:
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at testDownloadSimRetailBlockCSV method of SubmitReportRequestImplTest - "+ e.getMessage());
				ErrorDetails errorDetails = new ErrorDetails();
				errorDetails.setErrorString("Please upload vaild *.xlsx file");
				simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
				assertEquals(simRetailBlockReportRespMsg.getErrorDetails().getErrorString().toString(),"Please upload vaild *.xlsx file");
			}
		}

		/** Process to remove duplicates from list */
		Set<String> tempSet = new HashSet<>();
		tempSet.addAll(locationNoList);
		locationNoList.clear();
		locationNoList.addAll(tempSet);

		assertEquals("59156", locationNoList.get(0).toString());

		try {
			String[] header = { "StoreNumber", "RetailBlock", "Description" };

			simRetailBlockReportRespMsg = reportsBOImplTest.genarateSimRetailBlockReport();
			List<SimRetailBlockCSVBean> simRetailBlockCSVBeanList = new ArrayList<SimRetailBlockCSVBean>();

			for (int i = 0; i < simRetailBlockReportRespMsg.getSimRetailBlockReportResp().size(); i++) {

				SimRetailBlockCSVBean simRetailBlockCSVBean = new SimRetailBlockCSVBean();
				simRetailBlockCSVBean.setStoreNumber(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getStoreNumber());
				simRetailBlockCSVBean.setRetailBlock(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getRetailBlock());
				simRetailBlockCSVBean.setDescription(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getDescription());
				simRetailBlockCSVBeanList.add(simRetailBlockCSVBean);
			}

			modelActual.addObject("csvHeader", header);
			modelActual.addObject("csvData", simRetailBlockCSVBeanList);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at testDownloadSimRetailBlockCSV method of ReportsRestServiceImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting testDownloadSimRetailBlockCSV method of ReportsRestServiceImpl ");
			}
			assertEquals(modelExpected.getViewName().toString(), 
					     modelActual.getViewName().toString());
		}
	}

}
