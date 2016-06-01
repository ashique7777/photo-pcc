/**
 * 
 */
package com.walgreens.batch.central.tasklet;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;


/**
 * @author CTS
 *
 */
@Configuration
@PropertySource("classpath:PhotoOmniBatch.properties")
public class POFSftpTasklet implements Tasklet  {
	
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(POFSftpTasklet.class);
	
	private String fileName;
	
	@Value( "${pof.archive.folder.path}" )
	private String archiveFolderpath;
	
	@Value( "${pof.work.folder.path}" )
	private String workFolderPath;
	
	@Value( "${pof.sftp.file.path}" )
	private String sftpPath;
	
	private AtomicReference<String> targetDirVar  ;
	
	
	private MessageChannel sftpChannel;

	public AtomicReference<String> getTargetDirVar() {
		return targetDirVar;
	}

	public void setTargetDirVar(AtomicReference<String> targetDirVar) {
		this.targetDirVar = targetDirVar;
	}
	
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if(lOGGER.isInfoEnabled()){
			lOGGER.info("Enter in Sftp Tasklet....");
		}
		targetDirVar.set(sftpPath);
		File sourceFile = new File(workFolderPath);
		File[] files = new File(workFolderPath).listFiles();
		File srcFile = null;
		File destFile =null;
		Message<File> message= null;
		
		boolean isSftpSuccess= false;
        if (sourceFile.exists()) {
             
            try {
            	
            	for (File file : files) {
            	    if (file.isFile()) {
            	    	srcFile = new File(workFolderPath +file.getName());
            	    	destFile = new File(archiveFolderpath +file.getName());
            	    	 message = MessageBuilder.withPayload(srcFile).build();
            	    	 isSftpSuccess = sftpChannel.send(message);
            	    	if(!isSftpSuccess){
            	    		if(lOGGER.isInfoEnabled()){
            	    			lOGGER.info("SFTP fail for file ...."+ file.getName());
            	    		}
            	    	}else{
            	    		if(lOGGER.isInfoEnabled()){
            	    			lOGGER.info("SFTP success for file ...."+ file.getName());
            	    		}
            	    		copyNDeleteFile(srcFile,destFile); 
            	    	}
            	    }
               	}
              
              
            } catch (Exception e) {
            	e.printStackTrace();
            	if(lOGGER.isInfoEnabled()){
            		lOGGER.error("Could not send file per SFTP: " + e);
            	}
            }
        } else {
        	if(lOGGER.isInfoEnabled()){
        		lOGGER.info("File does not exist.");
        	}
        }
		
    return RepeatStatus.FINISHED;

	}
	
	public String getFileName() {
	    return fileName;
	  }
	
	public void setFileName(String fileName) {
	    this.fileName = fileName;
	  }
	 
	public MessageChannel getSftpChannel() {
	    return sftpChannel;
	}
	
	public void setSftpChannel(MessageChannel sftpChannel) {
	    this.sftpChannel = sftpChannel;
	 }
	
	private static void copyNDeleteFile(File src, File dest)
	    	throws IOException{
		
 	
			if(lOGGER.isInfoEnabled()){
				lOGGER.info("File copied from " + src + " to " + dest);
			}
			/** Move File from WORK to ARCHIVE folder**/
			FileUtils.moveFile(src, dest);
	    	
	    	if(lOGGER.isInfoEnabled()){
	    		lOGGER.info(src + "File delete ");
	        }
	    		
	}
	 
	
	
}
