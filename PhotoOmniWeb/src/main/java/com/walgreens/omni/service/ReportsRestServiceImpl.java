package com.walgreens.omni.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;
import com.walgreens.omni.bean.CannedReportDataCSVBean;
import com.walgreens.omni.bean.CsvSearchBean;
import com.walgreens.omni.bean.SilverCanisterCSVBean;
import com.walgreens.omni.bean.SilverCanisterFailureCSVBean;
import com.walgreens.omni.bo.ReportsBO;
import com.walgreens.omni.factory.OmniBOFactory;
import com.walgreens.omni.json.bean.CannedDropDownData;
import com.walgreens.omni.json.bean.CannedFilter;
import com.walgreens.omni.json.bean.CannedReportBean;
import com.walgreens.omni.json.bean.CannedReportGenericBean;
import com.walgreens.omni.json.bean.CannedReportReqBean;
import com.walgreens.omni.json.bean.SilverCanisterReportRepMsg;
import com.walgreens.omni.json.bean.SilverCanisterReportReq;
import com.walgreens.omni.json.bean.SilverCanisterReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStoreReportReqMsg;
import com.walgreens.omni.json.bean.SilverCanisterStroeReportRepMsg;
import com.walgreens.omni.json.bean.VendorUpdSilverCanisterDetailResMsg;
import com.walgreens.omni.json.bean.VendorUpdSilverCanisterDetails;
import com.walgreens.omni.json.bean.VendorUpdSilverCanisterDetailsList;
import com.walgreens.omni.web.constants.ReportsConstant;

@Controller
@RequestMapping(value = "/exceptionReport")
@Scope("request")
public class ReportsRestServiceImpl implements ReportsRestService {

	@Autowired
	private OmniBOFactory reportBOFactory;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ReportsRestServiceImpl.class);

	

	/**
	 * Method used to Generate silver canister report
	 * 
	 */
	@Override
	@RequestMapping(value = "/silverCanisterReportRequest", method = RequestMethod.POST)
	public @ResponseBody
	SilverCanisterReportRepMsg submitSilverCanisterReportRequest(
			@RequestBody SilverCanisterReportReqMsg reqParam) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterReportRequest method of ReportsRestServiceImpl ");
		}
		SilverCanisterReportRepMsg silverCanisterReportRepMsg = new SilverCanisterReportRepMsg();

		try {

			ReportsBO exceptionBO = reportBOFactory.getExceptionReportBO();
			silverCanisterReportRepMsg = exceptionBO
					.submitSilverCanisterReportRequest(reqParam, "ReportReq");

		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitSilverCanisterReportRequest method of ReportsRestServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSilverCanisterReportRequest method of ReportsRestServiceImpl ");
			}
		}
		return silverCanisterReportRepMsg;

	}

	/**
	 * 
	 * Method to download Silver Canister CSV
	 */
	@Override
	@RequestMapping(value = "/downloadSilverCanisterCSV", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView downloadSilverCanisterReportCSV(HttpServletResponse response,
			@RequestParam("silverCanisterFilter") final String silverCanisterFilter) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering downloadSilverCanisterReportCSV method of ReportsRestServiceImpl ");
		}
		ModelAndView model = new ModelAndView("DownloadSilverCanisterCSV");
		try {
			final JSONParser parser = new JSONParser();
			final Object obj = parser.parse(silverCanisterFilter);
			JSONObject jsonObject = (JSONObject) obj;
			jsonObject = (JSONObject)jsonObject.get("filter");
			
			SilverCanisterReportReqMsg reqParam = new SilverCanisterReportReqMsg();
			SilverCanisterReportReq silverCanisterReportReq = new SilverCanisterReportReq();
			
			if (!CommonUtil.isNull(jsonObject.get("startDate")) 
					&& !jsonObject.get("startDate").toString().equalsIgnoreCase("")) {
				silverCanisterReportReq.setStartDate(jsonObject.get("startDate").toString());
			}else {
				silverCanisterReportReq.setStartDate(PhotoOmniConstants.BLANK);
			}
			
			if (!CommonUtil.isNull(jsonObject.get("endDate")) 
					&& !jsonObject.get("endDate").toString().equalsIgnoreCase("")) {
				silverCanisterReportReq.setEndDate(jsonObject.get("endDate").toString());
			}else {
				silverCanisterReportReq.setEndDate(PhotoOmniConstants.BLANK);
			}
			if (!CommonUtil.isNull(jsonObject.get("location")) 
					&& !jsonObject.get("location").toString().equalsIgnoreCase("")) {
				silverCanisterReportReq.setLocation(jsonObject.get("location").toString());
			}else {
				silverCanisterReportReq.setLocation(PhotoOmniConstants.BLANK);
			}
			if (!CommonUtil.isNull(jsonObject.get("status")) 
					&& !jsonObject.get("status").toString().equalsIgnoreCase("")) {
				silverCanisterReportReq.setStatus(jsonObject.get("status").toString());
			}else {
				silverCanisterReportReq.setStatus(PhotoOmniConstants.BLANK);
			}
			if (!CommonUtil.isNull(jsonObject.get("number")) 
					&& !jsonObject.get("number").toString().equalsIgnoreCase("")) {
				silverCanisterReportReq.setNumber(jsonObject.get("number").toString());
			}else {
				silverCanisterReportReq.setNumber(PhotoOmniConstants.BLANK);
			}
			
			reqParam.setFilter(silverCanisterReportReq);
			List<SilverCanisterCSVBean> silverCanisterDetailsBeanList = new ArrayList<SilverCanisterCSVBean>();

			String[] header = { "Store", "LastCanisterChangeDate",
					"RollsCount", "PrintsCount", "PaperSquareInch",
					"SilverContentRolls", "SilverContentPrints", "TotalSilver" };

			SilverCanisterReportRepMsg silverCanisterReportRepMsg = new SilverCanisterReportRepMsg();
			ReportsBO exceptionBO = reportBOFactory.getExceptionReportBO();
			silverCanisterReportRepMsg = exceptionBO
					.submitSilverCanisterReportRequest(reqParam, "downloadReq");

			for (int i = 0; i < silverCanisterReportRepMsg
					.getSilverCanisterReportRepList().size(); i++) {

				SilverCanisterCSVBean silverCanisterCSVBean = new SilverCanisterCSVBean();
				silverCanisterCSVBean
						.setLastCanisterChangeDate(silverCanisterReportRepMsg
								.getSilverCanisterReportRepList().get(i)
								.getSilverCanisterDetails()
								.getLastCanisterChangeDate());
				silverCanisterCSVBean
						.setPaperSquareInch(silverCanisterReportRepMsg
								.getSilverCanisterReportRepList().get(i)
								.getSilverCanisterDetails()
								.getPaperSquereInch());
				silverCanisterCSVBean.setPrintsCount(silverCanisterReportRepMsg
						.getSilverCanisterReportRepList().get(i)
						.getSilverCanisterDetails().getPrintsCount());
				silverCanisterCSVBean.setRollsCount(silverCanisterReportRepMsg
						.getSilverCanisterReportRepList().get(i)
						.getSilverCanisterDetails().getRollsCount());
				silverCanisterCSVBean
						.setSilverContentPrints(silverCanisterReportRepMsg
								.getSilverCanisterReportRepList().get(i)
								.getSilverCanisterDetails()
								.getSilverContentPrints());
				silverCanisterCSVBean
						.setSilverContentRolls(silverCanisterReportRepMsg
								.getSilverCanisterReportRepList().get(i)
								.getSilverCanisterDetails()
								.getSilverContentRolls());
				silverCanisterCSVBean.setStore(silverCanisterReportRepMsg
						.getSilverCanisterReportRepList().get(i)
						.getSilverCanisterDetails().getStore());
				silverCanisterCSVBean.setTotalSilver(silverCanisterReportRepMsg
						.getSilverCanisterReportRepList().get(i)
						.getSilverCanisterDetails().getSilverContentPrints());

				silverCanisterDetailsBeanList.add(silverCanisterCSVBean);
			}
			
			model.addObject("csvHeader", header);
		    model.addObject("csvData", silverCanisterDetailsBeanList);
		  
		} catch (Exception e) {
			LOGGER.error(" Error occoured at downloadSilverCanisterReportCSV method of ReportsRestServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting downloadSilverCanisterReportCSV method of ReportsRestServiceImpl ");
			}
		}
		return model;
	}

	/**
	 * 
	 * Method to upload Silver Canister CSV File
	 */
	@SuppressWarnings("resource")
	@Override
	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	public  @ResponseBody VendorUpdSilverCanisterDetailResMsg uploadSilverCanisterCSVFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam("vendorName") String vendorName,
			HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Entering uploadSilverCanisterCSVFile method of ReportsRestServiceImpl ");}
		
		VendorUpdSilverCanisterDetailResMsg vendorUpdSilverCanisterDetailResMsg = new VendorUpdSilverCanisterDetailResMsg();
		if (file.isEmpty()) {		
			if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Empty *.csv file ... ");}
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setErrorString(ReportsConstant.ERROR_STRING_PATTERN_ONE);
			vendorUpdSilverCanisterDetailResMsg.setErrorDetails(errorDetails);
			return vendorUpdSilverCanisterDetailResMsg;
		}
		
		File serverFile = null;		
		if (!file.isEmpty()) {

			String fileName = file.getOriginalFilename();
			/** Validation for .CSV file */
			if (!fileName.contains(".csv")) {
				ErrorDetails errorDetails = new ErrorDetails();
				errorDetails.setErrorString(ReportsConstant.ERROR_STRING_PATTERN_ONE);
				vendorUpdSilverCanisterDetailResMsg.setErrorDetails(errorDetails);
				LOGGER.debug("Please Upload the Silver Recovery Data in the *.csv format");
				return vendorUpdSilverCanisterDetailResMsg;
		    /** Validation for .CSV file content */
			} else {
				try {
					String nextLine = ReportsConstant.BLANK;
					String cvsSplitBy = ReportsConstant.COMMA;
					BufferedReader br = null;

					String rootPath = request.getSession().getServletContext().getRealPath("/");
					File dir = new File(rootPath + File.separator+ "uploadedfile");
					if (!dir.exists()) {dir.mkdirs();}

					serverFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
					try {
						InputStream is = file.getInputStream();
						BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						int i = 0;
						/** write file to server */
						while ((i = is.read()) != -1) {stream.write(i);}
						stream.flush();
						stream.close();
					} catch (IOException e) {
						LOGGER.error("Error occoured at uploadSilverCanisterCSVFile method of ReportsRestServiceImpl - ", e);
					}					
					/**validation for  .csv data started */
					FileReader fileReader = new FileReader(serverFile);
					br = new BufferedReader(fileReader);
					{
						while ((nextLine = br.readLine()) != null) {
							@SuppressWarnings("unused")
							int length = 0;
							String[] csvData = nextLine.split(cvsSplitBy);
							if(csvData.length>=4){
								throw new Exception();
							}							
							length = csvData[0].length();
							/**Regular expression to match store no e.g WL4,WL45,WL115,WL4855,WL85475 etc*/
							Pattern p1 = Pattern.compile(ReportsConstant.STORE_PATTERN);
							Matcher m1 = p1.matcher(csvData[0].toString());
							if (csvData[0].isEmpty() || !m1.matches()) {
								throw new Exception();
							}							
							length = csvData[1].length();
							/**Regular expression to match mm/dd/yyyy format only*/
							Pattern p2 = Pattern.compile(ReportsConstant.DATE_PATTERN_ONE);
							Matcher m2 = p2.matcher(csvData[1].toString());
							if(csvData[1].isEmpty() || !m2.matches() ) 
							 {
								throw new Exception();
							 }				
							length = csvData[2].length();
							if (csvData[2].isEmpty()) {
								throw new Exception();
							}
						}
					}
					br.close();
				} catch (Exception e) {
					LOGGER.error(" Error occoured at uploadSilverCanisterCSVFile method of ReportsRestServiceImpl - ", e);
					ErrorDetails errorDetails = new ErrorDetails();
					errorDetails.setErrorString(ReportsConstant.ERROR_STRING_PATTERN_TWO);
					vendorUpdSilverCanisterDetailResMsg.setErrorDetails(errorDetails);
					return vendorUpdSilverCanisterDetailResMsg;
				}
			}
		}

		String nextLine = ReportsConstant.BLANK;String cvsSplitBy = ReportsConstant.COMMA;
		BufferedReader br = null;int count = 0;

		try {			
			/** read file after saving file in server */
			Map<Integer, ArrayList<String>> maps = new HashMap<Integer, ArrayList<String>>();
			FileReader fileReader = new FileReader(serverFile);
			br = new BufferedReader(fileReader);
			{				
				while ((nextLine = br.readLine()) != null) {

					String[] csvData = nextLine.split(cvsSplitBy);
					ArrayList<String> dataList = new ArrayList<String>();
					String tempStoreNo = ReportsConstant.BLANK;
					String tempOneStoreNo = ReportsConstant.BLANK;
					String tempTwoStoreNo = ReportsConstant.BLANK;
					
					/**formating storeno before process*/
					tempStoreNo = csvData[0];
					tempOneStoreNo = tempStoreNo.replace(ReportsConstant.STORE_NO_PREFIX, ReportsConstant.BLANK);
										
						if(tempOneStoreNo.length() == 1){
							tempTwoStoreNo = "0000"+tempOneStoreNo;
							dataList.add(tempTwoStoreNo);
						}
						else if(tempOneStoreNo.length() == 2){
							tempTwoStoreNo = "000"+tempOneStoreNo;
							dataList.add(tempTwoStoreNo);
						}
						else if(tempOneStoreNo.length() == 3){
							tempTwoStoreNo = "00"+tempOneStoreNo;
							dataList.add(tempTwoStoreNo);
						}
						else if(tempOneStoreNo.length() == 4){
							tempTwoStoreNo = "0"+tempOneStoreNo;
							dataList.add(tempTwoStoreNo);
						} 
						else if(tempOneStoreNo.length() == 5){
							dataList.add(tempOneStoreNo);
						}
						
					dataList.add(csvData[1]);
					dataList.add(csvData[2]);

					maps.put(count++, dataList);
				}
			}
			br.close();
			
			int totalRecs = count;			
			ReportsBO exceptionBO = reportBOFactory.getExceptionReportBO();
			Map<Integer, ArrayList<String>> invalidRecMaps = new HashMap<Integer, ArrayList<String>>();
			
			/** Update or insert records in respected tables */
			invalidRecMaps = exceptionBO.updateSilverCanisterDetails(maps,vendorName);
			
			int totalRecsUpdated = (count - invalidRecMaps.size());
			int totalInvalidRecord = invalidRecMaps.size();
			List<VendorUpdSilverCanisterDetailsList> vendorUpdSilverCanisterDetailsLists = new ArrayList<VendorUpdSilverCanisterDetailsList>();

			/** populate response message with invalid records */
			for (Map.Entry<Integer, ArrayList<String>> entry : invalidRecMaps.entrySet()) {

				List<String> dataList = new ArrayList<String>();
				dataList = entry.getValue();
				String storeNo = dataList.get(0);
				String canisterchangeDate = dataList.get(1);
				String serviceDescription = dataList.get(2);

				SimpleDateFormat dateFormat = new SimpleDateFormat(ReportsConstant.DATE_FORMAT_SILVER_CANISTER_ONE);
				Date dateSelectedFrom = dateFormat.parse(canisterchangeDate);

				VendorUpdSilverCanisterDetailsList vendorUpdSilverCanisterDetailsList = new VendorUpdSilverCanisterDetailsList();
				VendorUpdSilverCanisterDetails vendorUpdSilverCanisterDetails = new VendorUpdSilverCanisterDetails();
				vendorUpdSilverCanisterDetails.setStoreNumber(storeNo);
				vendorUpdSilverCanisterDetails.setCanisterChangeDate(dateFormat.format(dateSelectedFrom));
				vendorUpdSilverCanisterDetails.setServiceDescription(serviceDescription);
				vendorUpdSilverCanisterDetailsList.setVendorUpdSilverCanisterDetails(vendorUpdSilverCanisterDetails);

				vendorUpdSilverCanisterDetailsLists.add(vendorUpdSilverCanisterDetailsList);
			}
			vendorUpdSilverCanisterDetailResMsg.setFilter(vendorUpdSilverCanisterDetailsLists);
			ErrorDetails errorDetails = new ErrorDetails();
			errorDetails.setErrorString("Total Records Processed"+" "+totalRecs+" ,"
			+"Total Records Updated"+" "+totalRecsUpdated+" ,"+"Total Invalid Records"+" "+totalInvalidRecord);
			vendorUpdSilverCanisterDetailResMsg.setErrorDetails(errorDetails);

		} catch (Exception e) {
			LOGGER.error(" Error occoured at uploadSilverCanisterCSVFile method of ReportsRestServiceImpl - ", e);
		}
		return vendorUpdSilverCanisterDetailResMsg;
	}

	/**
	 * 
	 * Method to download Failure SilverCanister CSV file
	 */
	@Override
	@RequestMapping(value = "/downloadFailureSilverCanisterCSV", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView downloadFailureSilverCanisterCSV(HttpServletResponse response,
			@RequestBody VendorUpdSilverCanisterDetailResMsg reqParam) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering downloadFailureSilverCanisterCSV method of ReportsRestServiceImpl ");
		}
		
		ModelAndView model = new ModelAndView("DownloadFailureSilverCanisterCSV");
		try {

			String storeNoPrefix = "WL";
			String[] header = { "StoreNumber", "LastCanisterChangeDate","ServiceDescription" };
			List<SilverCanisterFailureCSVBean> silverCanisterFailureCSVBeanList = new ArrayList<SilverCanisterFailureCSVBean>();

			for (int i = 0; i < reqParam.getFilter().size(); i++) {

				SilverCanisterFailureCSVBean silverCanisterFailureCSVBean = new SilverCanisterFailureCSVBean();
				silverCanisterFailureCSVBean.setStoreNumber(storeNoPrefix + reqParam
						.getFilter().get(i).getVendorUpdSilverCanisterDetails()
						.getStoreNumber());
				silverCanisterFailureCSVBean.setLastCanisterChangeDate(reqParam
						.getFilter().get(i).getVendorUpdSilverCanisterDetails()
						.getCanisterChangeDate());
				silverCanisterFailureCSVBean.setServiceDescription(reqParam
						.getFilter().get(i).getVendorUpdSilverCanisterDetails()
						.getServiceDescription());

				silverCanisterFailureCSVBeanList
						.add(silverCanisterFailureCSVBean);
			}
			
			model.addObject("csvHeader", header);
		    model.addObject("csvData", silverCanisterFailureCSVBeanList);
		    
		} catch (Exception e) {
			LOGGER.error(" Error occoured at downloadFailureSilverCanisterCSV method of ReportsRestServiceImpl - ", e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting downloadFailureSilverCanisterCSV method of ReportsRestServiceImpl ");
			}

		}
		return model;
	}

	/**
	 * Method to submit Silver Canister Store Report Request
	 */
	@Override
	@RequestMapping(value = "/submitSilverCanisterStoreReportRequest", method = RequestMethod.POST)
	public @ResponseBody
	SilverCanisterStroeReportRepMsg submitSilverCanisterStoreReportRequest(
			@RequestBody SilverCanisterStoreReportReqMsg reqParam) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSilverCanisterStoreReportRequest method of ReportsRestServiceImpl ");
		}
		SilverCanisterStroeReportRepMsg silverCanisterStoreReportRepMsg = new SilverCanisterStroeReportRepMsg();

		try {

			ReportsBO exceptionBO = reportBOFactory.getExceptionReportBO();
			silverCanisterStoreReportRepMsg = exceptionBO
					.submitSilverCanisterStoreReportRequest(reqParam);

		} catch (Exception e) {
			LOGGER.error(
					" Error occoured at submitSilverCanisterStoreReportRequest method of ReportsRestServiceImpl - ",e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSilverCanisterStoreReportRequest method of ReportsRestServiceImpl ");
			}
		}
		return silverCanisterStoreReportRepMsg;
	}

     /**
	 *   Phase 2.0 CannedReport Start :Method to get the drop down list populated for order type and input channel in UI  
	 *    Phase 2.0 CannedReport End
	 *   
	 */
	
	@RequestMapping(value = "/getCannedReportOrderTypeInputChannelRequest", method = RequestMethod.GET)
	public @ResponseBody 	CannedDropDownData getCannedReportOrderTypeInputChannelRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getCannedReportOrderTypeRequest method of ReportsRestServiceImpl ");
		}	
		CannedDropDownData cannedDataList = null;
		try {
		  
			ReportsBO cannedBO = reportBOFactory.getCannedReportBO();
		    LOGGER.debug(" Entering into getCannedReportOrderTypeInputChannelRequest method of ReportsBO");
			cannedDataList = cannedBO.getCannedReportOrderTypeInputChannelRequest();
			LOGGER.debug(" Exiting from getCannedReportOrderTypeInputChannelRequest method");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at getCannedReportOrderTypeInputChannelRequest method of ReportsRestServiceImpl - ",
					e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting getCannedReportOrderTypeInputChannelRequest method of ReportsRestServiceImpl ");
			}
		}
		return cannedDataList;
	}
	/**
	 * 
	 * Phase 2.0 CannedReport Start: Method to generate report for orderType - placedOrderByProduct & soldOrderByProduct where input channel type can be Kiosk,Internet,Mobile or ALL
	 * Phase 2.0 CannedReport End
	 */
	/**
	 * @param reqParams
	 * @return
	 */
	@RequestMapping(value = "/generateOrderPlacedCannedReport", method = RequestMethod.POST)
	public @ResponseBody CannedReportBean submitReportRequest(@RequestBody CannedReportReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of CannedReportServiceImpl ");
		}
		//CannedReportReqBean reqParams = new CannedReportReqBean();
		CannedReportBean cannedReportBean=null;
		try {
			final CannedFilter filter = reqParams.getFilter();
			ReportsBO cannedBO = reportBOFactory.getCannedReportBO();
			cannedReportBean=cannedBO.generateReportRequest(filter);
			LOGGER.debug("Exiting from  submitReportRequest method of ReportsBO" );
		} catch (PhotoOmniException e) {
			LOGGER.debug(" Error occoured at submitReportRequest method of CannedReportServiceImpl ");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of CannedReportServiceImpl - " ,e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of CannedReportServiceImpl ");
			}
		}		
		
		return cannedReportBean;
	}
	/**
	 * 
	 * Phase 2.0 CannedReport Start: Method to get the generic fields 
	 * Phase 2.0 CannedReport End
	 */
	/**
	 * @param reqParams
	 * @return
	 */
	
	@RequestMapping(value = "/generateOrderPlacedCannedReportGenerricFields", method = RequestMethod.POST)
	public @ResponseBody CannedReportGenericBean getGenericFields(@RequestBody CannedReportReqBean reqParams) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitReportRequest method of CannedReportServiceImpl ");
		}
		//CannedReportReqBean reqParams = new CannedReportReqBean();
		CannedReportGenericBean cannedReportGenericBean =null;
		try {
			final CannedFilter filter = reqParams.getFilter();
			ReportsBO cannedBO = reportBOFactory.getCannedReportBO();
			cannedReportGenericBean=cannedBO.getGenericFields(filter);
			LOGGER.debug("Exiting from  submitReportRequest method of ReportsBO" );
		} catch (PhotoOmniException e) {
			LOGGER.debug(" Error occoured at submitReportRequest method of CannedReportServiceImpl ");
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitReportRequest method of CannedReportServiceImpl - ",e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitReportRequest method of CannedReportServiceImpl ");
			}
		}		
		
		return cannedReportGenericBean;
	}
	
	/* 
	 *  Phase 2.0 CannedReport Start : This method executes export to csv functionality. 
	 *  Phase 2.0 CannedReport En
	 */

	/* (non-Javadoc)
	 * @see com.walgreens.common.service.ReportsRestService#downloadCannedReportCSV(javax.servlet.http.HttpServletResponse, java.lang.String)
	 */
	@Override
	@RequestMapping(value = "/downloadCannedReportCSV", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView downloadCannedReportCSV(HttpServletResponse response,@RequestParam("cannedFilter") final String filter) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering downloadCannedReportCSV method of ReportsRestServiceImpl ");
		}
		ModelAndView model =null;
			
			try {
				
				final JSONParser parser = new JSONParser();
				final Object obj = parser.parse(filter);
				JSONObject jsonObject = (JSONObject) obj;
				jsonObject = (JSONObject)jsonObject.get("filter");
				
				CannedReportReqBean reqParam = new CannedReportReqBean();
				CannedFilter cannedFilter = new CannedFilter();		
				
				if (!CommonUtil.isNull(jsonObject.get("startDate")) 
						&& !jsonObject.get("startDate").toString().equalsIgnoreCase("")) {
					cannedFilter.setStartDate(jsonObject.get("startDate").toString());
				}else {
					cannedFilter.setStartDate(PhotoOmniConstants.BLANK);
				}
				if (!CommonUtil.isNull(jsonObject.get("endDate")) 
						&& !jsonObject.get("endDate").toString().equalsIgnoreCase("")) {
					cannedFilter.setEndDate(jsonObject.get("endDate").toString());
				}else {
					cannedFilter.setEndDate(PhotoOmniConstants.BLANK);
				}
				if (!CommonUtil.isNull(jsonObject.get("orderTypeName")) 
						&& !jsonObject.get("orderTypeName").toString().equalsIgnoreCase("")) {
					cannedFilter.setOrderTypeName(jsonObject.get("orderTypeName").toString());
				}else {
					cannedFilter.setEndDate(PhotoOmniConstants.BLANK);
				}
				if (!CommonUtil.isNull(jsonObject.get("inputChannelName")) 
						&& !jsonObject.get("inputChannelName").toString().equalsIgnoreCase("")) {
					cannedFilter.setInputChannelName(jsonObject.get("inputChannelName").toString());
				}else {
					cannedFilter.setEndDate(PhotoOmniConstants.BLANK);
				}
				reqParam.setFilter(cannedFilter);
				if(("Placed Order By Product".equalsIgnoreCase(cannedFilter.getOrderTypeName()))){
				
					model =	new ModelAndView("downloadCannedReportCSVPlacedOrder");
					
				List<CannedReportDataCSVBean> cannedReportDataBeanList = new ArrayList<CannedReportDataCSVBean>();
			    	CsvSearchBean searchBean=new CsvSearchBean();
			    	
			    	String[] searchCriteria ={"Start_Date","End_Date","Order_Type_Name","Input_Channel_Name"};				
				    String[] header = { "Product_Name", "Total_Product_Quantity",
						"Total_Order", "Total_Revenue", "Total_Revenue_Discount",
						"Unit_Revenue" };
				CannedReportBean cannedReportReqBean = new CannedReportBean();
				ReportsBO cannedBO = reportBOFactory.getCannedReportBO();
				LOGGER.info("Entering into generateCannedReportRequest method of ReportsBO" );
				cannedReportDataBeanList = (List<CannedReportDataCSVBean>) cannedBO.generateCannedReportRequest(cannedFilter, "downloadReq");
				LOGGER.info("Exiting from generateCannedReportRequest method of ReportsBO" );
				searchBean.setStart_Date(cannedFilter.getStartDate());
				searchBean.setEnd_Date(cannedFilter.getEndDate());
				searchBean.setInput_Channel_Name(cannedFilter.getInputChannelName());
				searchBean.setOrder_Type_Name(cannedFilter.getOrderTypeName());
				
				
				//ModelAndView model = new ModelAndView("placedordertypecsv");
				
				model.addObject("csvHeader", header);
				LOGGER.info("Adding cannedReportDataBeanList into model ");
			    model.addObject("csvData", cannedReportDataBeanList);
			    model.addObject("csvSearchCriteria",searchCriteria);
			    model.addObject("cvsSearch",searchBean);
				}
				else
				{
					model =	new ModelAndView("downloadCannedReportCSVSoldOrder");
					List<CannedReportDataCSVBean> cannedReportDataBeanList = new ArrayList<CannedReportDataCSVBean>();
			    	CsvSearchBean searchBean=new CsvSearchBean();
			    	String[] searchCriteria ={"Start_Date","End_Date","Order_Type_Name","Input_Channel_Name"};				
				    String[] header = { "Product_Name", "Total_Product_Quantity",
						"Total_Order", "Amount_Paid", "Total_Revenue_Discount",
						"Unit_Revenue","Profit" };
				CannedReportBean cannedReportReqBean = new CannedReportBean();
				ReportsBO cannedBO = reportBOFactory.getCannedReportBO();
				LOGGER.debug("Entering into generateCannedReportRequest method of ReportsBO" );
				cannedReportDataBeanList = (List<CannedReportDataCSVBean>) cannedBO.generateCannedReportRequest(cannedFilter, "downloadReq");
				LOGGER.debug("Exiting from generateCannedReportRequest method of ReportsBO" );
				searchBean.setStart_Date(cannedFilter.getStartDate());
				searchBean.setEnd_Date(cannedFilter.getEndDate());
				searchBean.setInput_Channel_Name(cannedFilter.getInputChannelName());
				searchBean.setOrder_Type_Name(cannedFilter.getOrderTypeName());				
				//ModelAndView model = new ModelAndView("soldordertypecsv");
				model.addObject("csvHeader", header);
				LOGGER.debug("Adding cannedReportDataBeanList into model ");
			    model.addObject("csvData", cannedReportDataBeanList);
			    model.addObject("csvSearchCriteria",searchCriteria);
			    model.addObject("cvsSearch",searchBean);
				  
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at downloadCannedReportCSV method of ReportsRestServiceImpl - "
						+ e);
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting downloadCannedReportCSV method of ReportsRestServiceImpl ");
				}
			}				
		
	
		return model;
		
	}

}


