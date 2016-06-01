/**
 * KronosExactTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		  3rd August 2015
 *  
 **/
package com.walgreens.batch.central.tasklet;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.PhotoOmniFileUtil;


/**
 * This class is a Task class for Kronos scheduler used for Exact files.
 * @author CTS
 * @version 1.1 August 03, 2015
 */
public class KronosExactTask implements Tasklet  {
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosExactTask.class);
	
	/**
	 * workFolderPath
	 */
	@Value( "${kronos.src.folder.path}" )
	private String workFolderPath;
	/**
	 * exactLocation
	 */
	@Value("${kronos.exact.folder.path}")
	private String exactLocation;
	/**
	 * fileCreationDate
	 */
	private String fileCreationDate;

    
	/**
	 * This method find the correct file and Exact the file.
	 * @param contribution contains contribution value.
	 * @param chunkContext contains chunkContext value.
	 * @return RepeatStatus.
	 * @exception PhotoOmniException.
	 */
	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering execute method of KronosExactTask ");
		}
		try {
			if (!CommonUtil.isNull(this.workFolderPath) && !this.workFolderPath.equals("")) {
				if (!CommonUtil.isNull(this.fileCreationDate) && !"".equals(this.fileCreationDate)) {
					final File exactPath = new File(this.exactLocation);
					if (!CommonUtil.isNull(exactPath) && !exactPath.exists()) {
						boolean pathCreated = exactPath.mkdirs();
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(" Path : " + exactPath + " was not, So created successfully : " + pathCreated);
						}
					}
					final File[] files = new File(this.workFolderPath).listFiles();
					for (File file : files) {
						if (file.isFile()) {
							final String fileName = file.getName();
							if (!CommonUtil.isNull(fileName)
									&& fileName.contains(this.fileCreationDate)
									&& PhotoOmniConstants.TEXT_FILE_EXTENSION.equalsIgnoreCase(fileName.substring(
											fileName.lastIndexOf('.'), fileName.length()))) {
								PhotoOmniFileUtil.moveFilesWithName(file, new File(this.exactLocation + fileName));
								LOGGER.debug(" File with name : " + fileName + " exacted to location : " + exactPath);
								break;
							}
						}
					}
				}
			} else {
				LOGGER.info(" Given File Location " + this.workFolderPath + " not found, So could not Exact the file ");
				throw new PhotoOmniException(" Failed to Exact the kronos data file ");
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(" Could not Exact the kronos data file ");
			LOGGER.error(" Error occoured at execute method of KronosExactTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Could not Exact the kronos data file ");
			LOGGER.error(" Error occoured at execute method of KronosExactTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of KronosExactTask ");
			}
		}
		return RepeatStatus.FINISHED;
	}

	
	/**
	 * @return the workFolderPath
	 */
	public String getWorkFolderPath() {
		return workFolderPath;
	}


	/**
	 * @param workFolderPath the workFolderPath to set
	 */
	public void setWorkFolderPath(String workFolderPath) {
		this.workFolderPath = workFolderPath;
	}
	
	/**
	 * @return the fileCreationDate
	 */
	public String getFileCreationDate() {
		return fileCreationDate;
	}


	/**
	 * @param fileCreationDate the fileCreationDate to set
	 */
	public void setFileCreationDate(String fileCreationDate) {
		this.fileCreationDate = fileCreationDate;
	}
	
}
