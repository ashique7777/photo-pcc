/**
 * 
 */
package com.walgreens.batch.central.tasklet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

/**
 * @author CTS
 *
 */

@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
public class POFBackUpToArchiveTasklet implements Tasklet{

	/**
	 * LOGGER
	 */
	final static Logger LOGGER = LoggerFactory.getLogger(POFBackUpToArchiveTasklet.class);
	
	/**
	 * eXact OutBound Feed Folder.
	 */
	@Value("${pof.exact.folder.path}")
	private String exactFolderPath;
	
	/**
	 * archive  Folder.
	 */
	@Value("${pof.archive.folder.path}")
	private String archiveFolderPath;
	
	
	/**
     * This method will check todays file exist in Outbound_feed , if exist then rename and move all todays files to archive folder.
     * 
     * @param contribution contains StepContribution values.
     * @param chunkContext contains chunkContext values.
     */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Enter execute method of POFBackUpToArchiveTasklet ");
		}
		try {

			File exactFolder = new File(exactFolderPath);
			File archiveFolder = new File(archiveFolderPath);
			File[] exactFiles = exactFolder.listFiles();
			
			if(!exactFolder.exists()){
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Exact Folder Does not Exists.");
				}
				throw new PhotoOmniException("Exact Folder Does not Exists.");
			}
			if(!archiveFolder.exists()){
				if (LOGGER.isInfoEnabled()) {
					LOGGER.error("Archive Folder Does not Exists.");
				}
				throw new PhotoOmniException("Archive Folder Does not Exists.");
			}
				
			SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmss");
		    String fileTime = dateFormat.format(new Date());
			String newName= "_backup_"+fileTime;
			
			if (exactFolder.exists() && archiveFolder.exists()) {
				for(File oldFile:exactFiles){
					if(this.isTodaysGeneratedFileExist(oldFile.getName())){
						File newFile = new File(exactFolderPath+oldFile.getName()+newName);
						if(oldFile.renameTo(newFile)){
							
							LOGGER.info("File rename from "+oldFile + " to "+ newFile);
						    PhotoOmniFileUtil.moveFile(newFile, new File(archiveFolderPath+newFile.getName()));
						}else{
							LOGGER.info("File does not remane.. ");
						}
					}
				}
			} 	
					
		} catch (NullPointerException e) {
			LOGGER.error(" Null Pointer Error occoured at execute method of POFBackUpToArchiveTasklet - "+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} catch (Exception e) {
			LOGGER.error(" Error occoured at execute method of POFBackUpToArchiveTasklet - "+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		}finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting execute method of POFBackUpToArchiveTasklet ");
			}
		}

		return RepeatStatus.FINISHED;
	}
	
	private boolean isTodaysGeneratedFileExist(String fileName){
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		Date date = new Date();
		String currentDate[] = sdf.format(date).split("/");
		String todaysDatFile = "EDI_VEND_PIC_"+currentDate[2]+currentDate[1]+currentDate[0]+PhotoOmniConstants.DAT_FILE_EXTENSION;
		String todaysIrsFile = "EDI_VEND_PIC_"+currentDate[2]+currentDate[1]+currentDate[0]+PhotoOmniConstants.IRS_FILE_EXTENSION;
		if(fileName.equals(todaysDatFile) || fileName.equals(todaysIrsFile))
			return true;
		else return false;
	}
	
}
