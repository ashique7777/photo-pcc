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
public class MSSDeleteFeedFileTasklet implements Tasklet {
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(MSSDeleteFeedFileTasklet.class);

	/**
	 * workFolderPath.
	 */
	@Value("${mss.src.folder.path}")
	private String workFolderPath;

    /**
     * This method will delete all the file from the source folder
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of MSSDeleteFeedFileTasklet ");
		}
		try {
				
			File sourceFile = new File(workFolderPath);
			File[] files = new File(workFolderPath).listFiles();
			if (sourceFile.exists()) {
				for (File file : files) {
					if (file.isFile()) {
						PhotoOmniFileUtil.deleteFileByName(file);
						LOGGER.debug("File : '" + file.getName()+"' is deleted");
					} else {
						LOGGER.debug("File : '" + file.getName()+"' is not deleted");
					}
				}
			} else {
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Source Folder Does not Exists.");
				}
				throw new PhotoOmniException("Source Folder Does not Exists.");
			}
					
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of MSSDeleteFeedFileTasklet - ", e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of MSSDeleteFeedFileTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}
}
