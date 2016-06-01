package com.walgreens.batch.central.tasklet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
public class DeleteAllFilesInDirectoryTasklet implements Tasklet {

	private static final Logger log = LoggerFactory
			.getLogger(DeleteAllFilesInDirectoryTasklet.class);
	
	@Value("${default.purge.days}")
	private String defaultPurgeDays ;
	
	private List<String> folders = new ArrayList<String>();
	
	private Properties properties = new Properties();

	private String propertyfilename ;
	

	public String getPropertyfilename() {
		return propertyfilename;
	}

	public void setPropertyfilename(String propertyfilename) {
		this.propertyfilename = propertyfilename;
	}


	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		try {
			
			final InputStream stream =
			           this.getClass().getResourceAsStream("/PhotoOmniBatch.properties");
			
			properties.load(stream);
			stream.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				
				if(key != null){
					if(key.endsWith(".archive.folder.path")) {
						folders.add(key);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new PhotoOmniException("Purging task failed");
		} 
		
		
		for (String folderNameKey : folders) {
			String folderPath = (String) properties.get(folderNameKey );
			String moduleName = folderNameKey.replaceAll(".archive.folder.path", PhotoOmniConstants.BLANK) ;
			String purgingDays = "0";
			if(moduleName!=null)
				purgingDays = (String) properties.get(moduleName.trim()+".purge.days" );
			if(log.isDebugEnabled())
			log.debug("Module : "+moduleName+". Number of days to purge: "+purgingDays);
			if(purgingDays!=null) ;
				deleteFiles(folderPath,convertToInteger(purgingDays.trim())) ;
		}
		return RepeatStatus.FINISHED;
	}
	
	
	
	
	/**checks If the File is Eligible For Deletion
	 * @param file
	 * @param numberOfDays
	 * @return
	 */
	private boolean checkIfFileEligibleForDeletion(File file,int numberOfDays){
		
		if(file == null)
			return false ;
		
		Long fileCreatedMillis = file.lastModified() ;
		
		Long currentTimeInMillis = System.currentTimeMillis() ;
		
		if(currentTimeInMillis-fileCreatedMillis >= TimeUnit.MILLISECONDS.convert(numberOfDays, TimeUnit.DAYS)) 
			return true ;
		
		return false ;
	}
	
	/**deletes the file olkder than the specified number of days
	 * @param directoryToClear
	 * @param numberOfDays
	 * @return
	 */
	private boolean deleteFiles(String directoryToClear, int numberOfDays){
		
			File directory = new File(directoryToClear);

			boolean directoryExists = directory.exists();
			boolean directoryDoesNotExists = !directoryExists;
			
			boolean directoryIsFile = directory.isFile();
			boolean directoryIsFolder  = !directoryIsFile;
			
			if (directoryDoesNotExists) {
				return false;
			}

			if (directoryIsFile) {
					if(checkIfFileEligibleForDeletion(directory,numberOfDays))
						directory.deleteOnExit();
			}

			if(directoryIsFolder) {

				for (File file : directory.listFiles()) {
						if(checkIfFileEligibleForDeletion(file,numberOfDays))
							while(file.exists()){
							file.delete();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								System.exit(1);
							}
							}
				}

			}
			return true ;
	}
	
	/**Convert String To Integer
	 * @param intString
	 * @return
	 */
	private int convertToInteger(String intString){
		int number=0;
		try{
		number = Integer.parseInt(intString);
		}catch(NumberFormatException nfe){
			if(log.isErrorEnabled())
			log.error("NumberFormatException in method convertToInteger(): Failed to convert String to integer.");
			return 0 ;
		}
		return number ;
	} 
	
}
