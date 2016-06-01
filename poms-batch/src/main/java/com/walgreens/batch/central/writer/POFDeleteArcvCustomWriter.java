/**
 * 
 */
package com.walgreens.batch.central.writer;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * @author CTS
 *
 */
@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
public class POFDeleteArcvCustomWriter implements ItemWriter<String> {

	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFDeleteArcvCustomWriter.class);
	
	@Value( "${pof.archive.folder.path}" )
	private String archiveFolderpath;
	
	
	@Override
	public void write(List<? extends String> items) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of POFSftpItemWriter ");
		}
		try {
					
			LOGGER.info("ARCHIVE folder Path ...  "+ archiveFolderpath); 
			File dest = new File(archiveFolderpath);
	        long daysBack = Long.valueOf(PhotoOmniConstants.DAYS_BACK);
	        
	        this.deleteFilesOlderThanNdays(daysBack,dest);
	       
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PSReportEmailItemWriter ");
			}
		}
		
	}
	
	private static void deleteFilesOlderThanNdays(long daysBack, File directory)
	  {
	    if (directory.exists()) {
	      File[] listFiles = directory.listFiles();
	      long time = (daysBack + 1) * 24 * 60 * 60 * 1000;
	      long purgeTime = System.currentTimeMillis() - time;

	      for (File listFile : listFiles)
	        if (listFile.lastModified() < purgeTime)
	          if (!(listFile.delete())) {
	            LOGGER.debug(new StringBuilder().append("Unable to delete file: ").append(listFile).toString());
	          } else {
	            LOGGER.debug(new StringBuilder().append("delete file: ").append(listFile).toString());
	            listFile.delete();
	          }
	    }
	    else
	    {
	      LOGGER.debug("Folder not exist for deletion ...");
	    }
	  }
	
	/*@BeforeJob
	private void retriveValue(StepExecution stepExecution){
		final JobExecution jobExecution = stepExecution.getJobExecution();
		final ExecutionContext executionContext = jobExecution.getExecutionContext();
		archiveFolderpath = (String) executionContext.get("refPofSFTPFile");

	}*/

}
