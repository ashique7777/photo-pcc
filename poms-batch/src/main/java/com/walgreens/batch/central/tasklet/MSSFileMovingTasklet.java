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
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

@Configuration
public class MSSFileMovingTasklet implements Tasklet {
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(MSSFileMovingTasklet.class);

	/**
	 * workFolderPath.
	 */
	@Value("${mss.src.folder.path}")
	private String workFolderPath;
	
	@Value("${mss.exact.folder.path}")
	private String exactFolderPath;
	
	/**
     * This method will move all the files from the source folder to the exact folder.
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of MSSFileMovingTasklet ");
		}
		try {

			File exactFolder = new File(exactFolderPath);
			File sourceFolder = new File(workFolderPath);
			File[] files = sourceFolder.listFiles();
			
			if (exactFolder.exists() && sourceFolder.exists()) {
				for(File file:files){
					PhotoOmniFileUtil.moveFilesWithName(file, new File(exactFolderPath+file.getName()));
				}
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Source Folder or Exact Folder Does not Exists.");
				}
				throw new PhotoOmniException("Source Folder or Exact Folder Does not Exists.");
			}		
					
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of MSSFileMovingTasklet - "+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of MSSFileMovingTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}
}
