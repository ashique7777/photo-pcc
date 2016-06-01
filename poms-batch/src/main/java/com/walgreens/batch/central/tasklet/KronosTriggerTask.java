/**
 * KronosTriggerTask.java 
 * Copyright (c) Walgreens Co. All Rights Reserved.
 *
 */

/*
 * Modification Log
 *-----------------------------------------------------------------------------------------------
 *   Ver             Date            Modified By         Description
 *-----------------------------------------------------------------------------------------------
 *  <1.1>     		  12th October 2015
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
 * This class is a Task class for Kronos scheduler used to delete/create/Exact the trigger file.
 * @author CTS
 * @version 1.1 October 12, 2015
 */
public class KronosTriggerTask implements Tasklet  {
	
	/**
	 * logger to log the details.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(KronosTriggerTask.class);

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
	 * This method delete/create/Exact the trigger file.
	 * @param contribution contains contribution value.
	 * @param chunkContext contains chunkContext value.
	 * @return RepeatStatus.
	 * @exception PhotoOmniException.
	 */
	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering execute method of KronosTriggerTask ");
		}
		try {
			if (!CommonUtil.isNull(this.workFolderPath) && !this.workFolderPath.equals("")) {
				if (!CommonUtil.isNull(this.fileCreationDate) && !"".equals(this.fileCreationDate)) {
					final File srcLoction = new File(this.workFolderPath);
					if (srcLoction.exists()) {
						/* Create a new trigger file in source folder */
						final StringBuilder fullFileName = new StringBuilder();
						fullFileName.append(PhotoOmniConstants.KRONOS_TRIGGER_FILE_NAME);
						fullFileName.append(fileCreationDate);
						fullFileName.append(PhotoOmniConstants.TRIGGER_FILE_EXTENSION);
						final File newTriggerFile = new File(this.workFolderPath.concat(fullFileName.toString()));
						final boolean isFileCreated = newTriggerFile.createNewFile();
						if (isFileCreated) {
							/* Exact the Trigger file */
							PhotoOmniFileUtil.moveFilesWithName(new File(this.workFolderPath + fullFileName), new File(
									this.exactLocation + fullFileName));
							LOGGER.info(" File with name " + fullFileName + " exacted to location :  : "
									+ this.exactLocation);
						}
					} else {
						LOGGER.error(" Could not Exact the file because source location is not valid ");
						throw new PhotoOmniException(" Failed to Exact the Kronos Trigger file ");
					}
				}
			} else {
				LOGGER.info(" Given File Location " + this.workFolderPath
						+ " not found, So could not Delete/Create/Exact the file ");
				throw new PhotoOmniException(" Failed to Delete/Create/Exact the Kronos Trigger file ");
			}
		} catch (PhotoOmniException e) {
			LOGGER.error(" Could not Exact the Kronos Trigger file ");
			LOGGER.error(" Error occoured at execute method of KronosTriggerTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Could not Exact the Kronos Trigger file ");
			LOGGER.error(" Error occoured at execute method of KronosTriggerTask - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of KronosTriggerTask ");
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
