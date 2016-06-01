/**
 * 
 */
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
import org.springframework.context.annotation.PropertySource;

import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

/**
 * @author CTS
 *
 */
@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
public class POFFileCopyToeXactFolderTasklet implements Tasklet {
	
	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(POFFileCopyToeXactFolderTasklet.class);
	
	
	/**
	 * workFolderPath.
	 */
	@Value("${pof.work.folder.path}")
	private String workFolderPath;
	
	
	/**
	 * eXact OutBound Feed Folder.
	 */
	@Value("${pof.exact.folder.path}")
	private String exactFolderPath;
	
	
	/**
     * This method will move all the files from the work folder to the exact folder.
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of POFFileCopyToeXactFolderTasklet ");
		}
		try {

			File exactFolder = new File(exactFolderPath);
			File workFolder = new File(workFolderPath);
			File[] files = workFolder.listFiles();
			
			if(!exactFolder.exists()){
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Exact Folder Does not Exists.");
				}
				throw new PhotoOmniException("Exact Folder Does not Exists.");
			}
			if(!workFolder.exists()){
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Work Folder Does not Exists.");
				}
				throw new PhotoOmniException("Work Folder Does not Exists.");
			}
			if (exactFolder.exists() && workFolder.exists()) {
				for(File file:files){
					PhotoOmniFileUtil.moveFilesWithName(file, new File(exactFolderPath+file.getName()));
				}
			} 	
					
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at execute method of POFFileCopyToeXactFolderTasklet - "+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of POFFileCopyToeXactFolderTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}

}
