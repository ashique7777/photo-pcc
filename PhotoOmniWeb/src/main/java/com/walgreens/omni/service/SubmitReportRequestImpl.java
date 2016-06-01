package com.walgreens.omni.service;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.walgreens.omni.bean.SimRetailBlockCSVBean;
import com.walgreens.omni.bo.ReportsBO;
import com.walgreens.omni.factory.OmniBOFactory;
import com.walgreens.omni.json.bean.SimRetailBlockOnloadResp;
import com.walgreens.omni.json.bean.SimRetailBlockReportRespMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateReqMsg;
import com.walgreens.omni.json.bean.SimRetailBlockUpdateRespMsg;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.ErrorDetails;

@Controller
@RequestMapping(value = "/submitReportRequest")
@Scope("request")
public class SubmitReportRequestImpl implements SubmitReportRequest {
	
	@Autowired
	private OmniBOFactory reportBOFactory;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SubmitReportRequestImpl.class);

	@RequestMapping(value = "/submitSimRetailBlockOnloadRequest", method = RequestMethod.POST)
	public @ResponseBody
	SimRetailBlockOnloadResp submitSimRetailBlockOnloadRequest() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering submitSimRetailBlockOnloadRequest method of SubmitReportRequestImpl ");
		}
		SimRetailBlockOnloadResp simRetailBlockOnloadResp = null;
		try {
		
			ReportsBO reportsBO = reportBOFactory.getExceptionReportBO();
			simRetailBlockOnloadResp = reportsBO.submitSimRetailBlockOnloadRequest();
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at submitSimRetailBlockOnloadRequest method of SubmitReportRequestImpl - "
					+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting submitSimRetailBlockOnloadRequest method of SubmitReportRequestImpl ");
			}
		}
		return simRetailBlockOnloadResp;
	}	

	
	/**
	 * 
	 * Method to upload Silver Cannister CSV File
	 */
	@Override
	@RequestMapping(value = "/genarateSimRetailBlockReport", method = RequestMethod.POST)
	public  @ResponseBody SimRetailBlockReportRespMsg genarateSimRetailBlockReport(
			@RequestParam(value="file", required=false) MultipartFile file,
			@RequestParam("retailBlock") String retailBlock,
			@RequestParam("locationType") String locationType,
			@RequestParam("number") String number,
			@RequestParam("pageNo") String pageNo,
			@RequestParam("sortColumnName") String sortColumnName,
			@RequestParam("sortOrder") String sortOrder,
			HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering genarateSimRetailBlockReport method of SubmitReportRequestImpl ");
		}
		
		SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
		List<String> locationNoList = new ArrayList<String>();
		
		/**Parameter for download request*/
		String downLoadCsv = "doNotDownLoadCsv"; 
		
		if(!number.equalsIgnoreCase("")){
			locationNoList.add(number);
		}	
		/**checks if the upload file is null*/	
		if(CommonUtil.isNull(file)){
			//locationNoList.add(number);
		}else {		
			if (file.isEmpty()) {		
				if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Empty *.xlsx file ... ");}
				ErrorDetails errorDetails = new ErrorDetails();
				errorDetails.setErrorString("Please Upload Data in the *.xlsx format");
				simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
				 //return simRetailBlockReportRespMsg;
			}else{

				File serverFile = null;	
				String fileName = file.getOriginalFilename();
				/** Validation for .xlsx file */
				if (!fileName.contains(".xlsx")) {
					ErrorDetails errorDetails = new ErrorDetails();
					errorDetails.setErrorString("Please Upload Data in the *.xlsx format");
					simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
					LOGGER.debug("Please Upload the Data in the *.xlsx format");
					return simRetailBlockReportRespMsg;		
				} else {
					try {
						@SuppressWarnings("unused")
						BufferedReader br = null;
						String rootPath = request.getSession().getServletContext().getRealPath("/");
						File dir = new File(rootPath + File.separator+ "uploadedfile");
						if (!dir.exists()) {
							dir.mkdirs();}
						serverFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
						try {
							InputStream is = file.getInputStream();
							BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
							int i = 0;
							/** write file to server */
							while ((i = is.read()) != -1) {
							stream.write(i);}
							stream.flush();
							stream.close();
						} catch (IOException e) {
							LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of SubmitReportRequestImpl - " + e.getMessage());
						}
						
						/**validation for  .xlsx data started */				
			            FileInputStream fis = new FileInputStream(serverFile);
			            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
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
			                    	/**Regular expression to match store no e.g 4,45,115,4855,85475 etc*/
									Pattern p1 = Pattern.compile("[0-9]{1,5}$");
									Matcher m1 = p1.matcher(temp);
									if (temp.isEmpty() || !m1.matches()) {
										throw new Exception(); }
									locationNoList.add(temp);		                        
			                        break;
			                    default :		                 
			                  }
			               }
			           }											
					} catch (Exception e) {
						LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of SubmitReportRequestImpl - "+ e.getMessage());
						ErrorDetails errorDetails = new ErrorDetails();
						errorDetails.setErrorString("Please upload vaild *.xlsx file");
						simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
						return simRetailBlockReportRespMsg;
					}
				}			
			}
		}
				
		/**Process to remove duplicates from list*/
		Set<String> tempSet = new HashSet<>();
		tempSet.addAll(locationNoList);
		locationNoList.clear();
		locationNoList.addAll(tempSet);	
		
		try{
			
			ReportsBO reportsBO = reportBOFactory.getExceptionReportBO();
			simRetailBlockReportRespMsg = reportsBO.genarateSimRetailBlockReport(retailBlock,locationType,locationNoList,pageNo,sortColumnName,sortOrder,downLoadCsv);
			
		} catch (Exception e) {
			if (LOGGER.isDebugEnabled()) {
			LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of SubmitReportRequestImpl - "+ e.getMessage());
			}
		}finally{
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting genarateSimRetailBlockReport method of SubmitReportRequestImpl ");
			}
		}

		return simRetailBlockReportRespMsg;
	}
	
	/**
	 * @param simRetailBlockUpdateReqMsg
	 * @return SimRetailBlockUpdateRespMsg
	 */
	@Override
	@RequestMapping(value = "/updateRetailBlockRequest", method = RequestMethod.POST)
	public @ResponseBody SimRetailBlockUpdateRespMsg updateRetailBlockRequest(@RequestBody SimRetailBlockUpdateReqMsg simRetailBlockUpdateReqMsg) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.error("Entering updateRetailBlockRequest method of SubmitReportRequestImpl ");
			}
		SimRetailBlockUpdateRespMsg simRetailBlockUpdateRespMsg = new SimRetailBlockUpdateRespMsg();
		try{
				
				ReportsBO reportsBO = reportBOFactory.getExceptionReportBO();
				if(!CommonUtil.isNull(simRetailBlockUpdateReqMsg.getSimRetailBlockUpdateReq().getRetailBlock())){
				
					simRetailBlockUpdateRespMsg = reportsBO.updateRetailBlockRequest(simRetailBlockUpdateReqMsg);
				}else{
					simRetailBlockUpdateRespMsg.setUpdateStatusMessage("Please select retail block for update");
				}
				
			} catch (Exception e) {
				if (LOGGER.isDebugEnabled()) {
				LOGGER.error(" Error occoured at updateRetailBlockRequest method of SubmitReportRequestImpl - "+ e.getMessage());
				}
			}finally{
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting updateRetailBlockRequest method of SubmitReportRequestImpl ");
				}
			}
		return simRetailBlockUpdateRespMsg;			
	}
	
	
	/**
	 * 
	 * Method to download SIM retail block report CSV
	 */
	@Override
	@RequestMapping(value = "/downloadSimRetailBlockCSV", method = RequestMethod.POST)
	public @ResponseBody
	ModelAndView downloadSimRetailBlockCSV(
			@RequestParam(value="file", required=false) MultipartFile file,
			@RequestParam("retailBlock") String retailBlock,
			@RequestParam("locationType") String locationType,
			@RequestParam("number") String number,
			@RequestParam("pageNo") String pageNo,
			@RequestParam("sortColumnName") String sortColumnName,
			@RequestParam("sortOrder") String sortOrder,
			HttpServletRequest request) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering downloadSimRetailBlockCSV method of SubmitReportRequestImpl ");
		}
		ModelAndView model = new ModelAndView("DownloadSimRetailBlockCSV");
		try {
			
			SimRetailBlockReportRespMsg simRetailBlockReportRespMsg = new SimRetailBlockReportRespMsg();
			List<String> locationNoList = new ArrayList<String>();
			
			/**Parameter for download request*/
			String downLoadCsv = "downLoadCsv"; 
			
			if(!number.equalsIgnoreCase("")){
				locationNoList.add(number);
			}
			
			/**checks if the upload file is null*/	
			if(CommonUtil.isNull(file)){
				//locationNoList.add(number);
			}else {		
				if (file.isEmpty()) {		
					if (LOGGER.isDebugEnabled()) {LOGGER.debug(" Empty *.xlsx file ... ");}
					ErrorDetails errorDetails = new ErrorDetails();
					errorDetails.setErrorString("Please Upload Data in the *.xlsx format");
					simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
					//return null;
				}else{

					File serverFile = null;	
					String fileName = file.getOriginalFilename();
					/** Validation for .xlsx file */
					if (!fileName.contains(".xlsx")) {
						ErrorDetails errorDetails = new ErrorDetails();
						errorDetails.setErrorString("Please Upload Data in the *.xlsx format");
						simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
						LOGGER.debug("Please Upload the Data in the *.xlsx format");
						return null;		
					} else {
						try {
							@SuppressWarnings("unused")
							BufferedReader br = null;
							String rootPath = request.getSession().getServletContext().getRealPath("/");
							File dir = new File(rootPath + File.separator+ "uploadedfile");
							if (!dir.exists()) {
								dir.mkdirs();}
							serverFile = new File(dir.getAbsolutePath()+ File.separator + file.getOriginalFilename());
							try {
								InputStream is = file.getInputStream();
								BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
								int i = 0;
								/** write file to server */
								while ((i = is.read()) != -1) {
								stream.write(i);}
								stream.flush();
								stream.close();
							} catch (IOException e) {
								LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of SubmitReportRequestImpl - " + e.getMessage());
							}
							
							/**validation for  .xlsx data started */				
				            FileInputStream fis = new FileInputStream(serverFile);
				            XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
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
				                    	/**Regular expression to match store no e.g 4,45,115,4855,85475 etc*/
										Pattern p1 = Pattern.compile("[0-9]{1,5}$");
										Matcher m1 = p1.matcher(temp);
										if (temp.isEmpty() || !m1.matches()) {
											throw new Exception(); }
										locationNoList.add(temp);		                        
				                        break;
				                    default :		                 
				                  }
				               }
				           }											
						} catch (Exception e) {
							LOGGER.error(" Error occoured at genarateSimRetailBlockReport method of SubmitReportRequestImpl - "+ e.getMessage());
							ErrorDetails errorDetails = new ErrorDetails();
							errorDetails.setErrorString("Please upload vaild *.xlsx file");
							simRetailBlockReportRespMsg.setErrorDetails(errorDetails);
							return null;
						}
					}			
				}
			}
					
			/**Process to remove duplicates from list*/
			Set<String> tempSet = new HashSet<>();
			tempSet.addAll(locationNoList);
			locationNoList.clear();
			locationNoList.addAll(tempSet);	

			String[] header = { "StoreNumber", "RetailBlock","Description"};
		
			ReportsBO reportsBO = reportBOFactory.getExceptionReportBO();
			simRetailBlockReportRespMsg = reportsBO.genarateSimRetailBlockReport(retailBlock,locationType,locationNoList,pageNo,sortColumnName,sortOrder,downLoadCsv);
			List<SimRetailBlockCSVBean> simRetailBlockCSVBeanList = new ArrayList<SimRetailBlockCSVBean>();

			for (int i = 0; i < simRetailBlockReportRespMsg.getSimRetailBlockReportResp().size(); i++) {

				SimRetailBlockCSVBean simRetailBlockCSVBean = new SimRetailBlockCSVBean();
				simRetailBlockCSVBean.setStoreNumber(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getStoreNumber());
				simRetailBlockCSVBean.setRetailBlock(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getRetailBlock());
				simRetailBlockCSVBean.setDescription(simRetailBlockReportRespMsg.getSimRetailBlockReportResp().get(i).getDescription());
				simRetailBlockCSVBeanList.add(simRetailBlockCSVBean);
			}
			
			model.addObject("retailBlock", retailBlock);
			model.addObject("locationType", locationType);
			model.addObject("number", number);
			model.addObject("csvHeader", header);
		    model.addObject("csvData", simRetailBlockCSVBeanList);
		  
		} catch (Exception e) {
			LOGGER.error(" Error occoured at downloadSilverCanisterReportCSV method of ReportsRestServiceImpl - "+ e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting downloadSilverCanisterReportCSV method of ReportsRestServiceImpl ");
			}
		}
		return model;
	}

	
}
