/**
 * KronosFileMovingAndDeleteTasklet.java 
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
 * This class is a Task class for Kronos scheduler used for moving file source to archive folder.
 * @author CTS
 * @version 1.1 August 03, 2015
 */
public class KronosFileMovingAndDeleteTasklet implements Tasklet {
	/**
	 * LOGGER
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(KronosFileMovingAndDeleteTasklet.class);

	/**
	 * exactLocation
	 */
	@Value("${kronos.exact.folder.path}")
	private String exactLocation;
	/**
	 * archiveFolderpath.
	 */
	@Value("${kronos.archive.folder.path}")
	private String archiveFolderpath;

    /**
     * This method move file one location to another is successfully SFTPed.
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Enter execute method of KronosFileMovingAndDeleteTasklet ");
		}
		try {
			if (!CommonUtil.isNull(this.exactLocation) && !this.exactLocation.equals("")
					&& !CommonUtil.isNull(this.archiveFolderpath) && !this.archiveFolderpath.equals("")) {
				final File archivePath = new File(this.archiveFolderpath);
				if (!archivePath.exists()) {
					boolean pathCreated = archivePath.mkdirs();
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Path : " + archivePath + " was not, So created successfully : " + pathCreated);
					}
				}
				final File[] files = new File(this.exactLocation).listFiles();
				if (!CommonUtil.isNull(files)) {
					for (File file : files) {
						final String fileName = file.getName();
						if (!CommonUtil.isNull(fileName) && fileName.contains(PhotoOmniConstants.EXACT_PROCESSED)) {
							LOGGER.info(" File with name " + fileName + " found for moving archive folder ");
							PhotoOmniFileUtil.moveFile(file, new File(this.archiveFolderpath, fileName)); 
							LOGGER.info(" File with name " + fileName + " has been moved to location "
									+ archiveFolderpath + " successfully ");
						}
					}
				}
			} else {
				LOGGER.info(" source folder : " + exactLocation);
				LOGGER.info(" Destination folder : " + archiveFolderpath);
				LOGGER.info(" Source or destination folder not found or file creation date not found ");
			}
		} catch (Exception e) {
			LOGGER.error(" Error occoured at execute method of KronosFileMovingAndDeleteTasklet - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of KronosFileMovingAndDeleteTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}

	/**
	 * @return the archiveFolderpath
	 */
	public String getArchiveFolderpath() {
		return archiveFolderpath;
	}

	/**
	 * @param archiveFolderpath the archiveFolderpath to set
	 */
	public void setArchiveFolderpath(String archiveFolderpath) {
		this.archiveFolderpath = archiveFolderpath;
	}

}
