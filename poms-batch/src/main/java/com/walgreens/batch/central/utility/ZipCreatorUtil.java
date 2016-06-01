/**
 * ZipCreator.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		 10 mar 2015
 *  
 **/
package com.walgreens.batch.central.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.batch.central.bean.LCAndPSReportPrefDataBean;
import com.walgreens.batch.central.bean.LCDailyReportPrefDataBean;
import com.walgreens.batch.central.bean.RoyaltySalesReportPrefDataBean;
import com.walgreens.batch.central.bean.SalesReportByProductBean;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This Class use to create zip file. 
 * @author CTS
 * @version 1.1 March 10, 2015
 */
public class ZipCreatorUtil {
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipCreatorUtil.class);
	
	
	/**
	 * This method is used to create Zip file.
	 * @param objVal contains objVal data.
	 * @throws PhotoOmniException custom exception.
	 */
	public static void createZipFile(final Object objVal) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering createZipFile method of ZipCreator ");
		}
    	final byte[] buffer = new byte[1024];
    	FileOutputStream fos = null;
    	List<String> fileNames= null;
    	FileInputStream inputStream = null;
    	ZipEntry ze = null;
    	String fileLocation = null;
    	boolean fileDeleteFlag = false;
    	try{
    		if (objVal instanceof LCAndPSReportPrefDataBean) {
    			fileNames = ((LCAndPSReportPrefDataBean) objVal).getFileNameList();
    			fileLocation = ((LCAndPSReportPrefDataBean) objVal).getFileLocation();
    			
			} else if (objVal instanceof LCDailyReportPrefDataBean) {
    			fileNames = ((LCDailyReportPrefDataBean) objVal).getFileNameList();
    			fileLocation = ((LCDailyReportPrefDataBean) objVal).getFileLocation();
    			
    		}  else if (objVal instanceof RoyaltySalesReportPrefDataBean) {
    			fileNames = ((RoyaltySalesReportPrefDataBean) objVal).getFileNameList();
    			fileLocation = ((RoyaltySalesReportPrefDataBean) objVal).getFileLocation();
    			
    		} else if (objVal instanceof SalesReportByProductBean) {
    			fileNames = ((SalesReportByProductBean) objVal).getFileNameList();
    			fileLocation = ((SalesReportByProductBean) objVal).getFileLocation();
    			
    		} 
    		for (int i = 0; i < fileNames.size(); i++) {
				final String fileName = fileNames.get(i);
				final File file = new File(fileLocation.concat(fileName));
    			if (isZipNeeded(file)) {
	    			final String withOutExtension = fileName.replace(PhotoOmniConstants.CSV_FILE_EXTENSION, "");
	    			fos = new FileOutputStream(fileLocation.concat(withOutExtension)
	    					.concat(PhotoOmniConstants.ZIP_FILE_EXTENSION));
	    			ze = new ZipEntry(fileName);
	    			inputStream = new FileInputStream(fileLocation.concat(fileName));
	    			final ZipOutputStream zos = new ZipOutputStream(fos);
	        		zos.putNextEntry(ze);
	        		int len;
	        		while ((len = inputStream.read(buffer)) > 0) {
	        			zos.write(buffer, 0, len);
	        		}
	        		fileNames.set(i, withOutExtension.concat(PhotoOmniConstants.ZIP_FILE_EXTENSION));
	        		inputStream.close();
	        		zos.closeEntry();
	        		zos.close();
	        		fileDeleteFlag = file.delete();
	        		if (LOGGER.isDebugEnabled()) {
	        			LOGGER.debug(file + " file delete successfully " + fileDeleteFlag);
	        		}
    			}
			}
    		
    	} catch (FileNotFoundException e) {
    		LOGGER.error(" Error occoured at createZipFile method of ZipCreator - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
    	} catch(IOException e) {
    		LOGGER.error(" Error occoured at createZipFile method of ZipCreator - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
    	} finally {
    		if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting createZipFile method of ZipCreator ");
			}
    	}
    }

	
	/**
	 * This method checks the file Size.
	 * @param file contains file obj.
	 * @return zipNeeded.
	 * @throws PhotoOmniException custom exception.
	 */
	private static boolean isZipNeeded(final File file) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering isZipNeeded method of ZipCreator ");
		}
		double fileSize = 0;
		double kilobytes = 0;
		double megabytes = 0;
		double gigabytes = 0;
		double definedFileSize = 0;
		boolean zipNeeded = false;
		try {
			if(file.exists()){
			   fileSize = file.length();
			   kilobytes = fileSize / 1024;
			   megabytes = kilobytes / 1024;
			   gigabytes = megabytes / 1024;
			   definedFileSize = new Double(PhotoOmniConstants.CSV_FILE_SIZE_BYTE);
			   if (fileSize > definedFileSize) {
				   zipNeeded = true;
			     }
			}
		} catch (NumberFormatException e) {
			LOGGER.error(" Error occoured at isZipNeeded method of ZipCreator - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at isZipNeeded method of ZipCreator - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting isZipNeeded method of ZipCreator ");
			}
		}
		return zipNeeded;
	}

}
