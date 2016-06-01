package com.walgreens.batch.central.tasklet;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

@Configuration
public class MSSArchiveFolderTasklet implements Tasklet {
	
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(MSSFileMovingTasklet.class);

	/**
	 * workFolderPath.
	 */
	@Value("${mss.exact.folder.path}")
	private String exactFolderPath;
	
	@Value("${mss.archive.folder.path}")
	private String archiveFolderPath;
	
	/**
     * This method will move all the files from the source folder to the exact folder.
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.info(" Enter execute method of MSSArchiveFileMovingTasklet ");
		}
		try {

			File exactFolder = new File(exactFolderPath);
			File destinationFolder = new File(archiveFolderPath);
			
			String fileNameWithExact = PhotoOmniConstants.EXACT_PROCESSED;
			
			if (exactFolder.exists() && destinationFolder.exists()) {
				
						PhotoOmniFileUtil.moveFilesContainingText(exactFolder, destinationFolder, fileNameWithExact);
						LOGGER.debug("All the file names starting with 'eXact_PROCESSED' has been moved to Archive folder");
					
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Exact Folder or Archive Folder Does not Exists.");
				}
				throw new PhotoOmniException("Exact Folder or Archive Folder Does not Exists.");
			}		
					
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of MSSArchiveFileMovingTasklet - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of MSSArchiveFileMovingTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}

}
