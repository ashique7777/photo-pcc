package com.walgreens.batch.central.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import com.walgreens.batch.central.bean.DailyMSSDatabean;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

public class DailyMSSFileWriter implements ItemWriter<DailyMSSDatabean>{
	
	/**
	 * logger to log the details.
	 */
	private static final Logger lOGGER = LoggerFactory.getLogger(DailyMSSFileWriter.class);
	
	/**
	 * Filename.
	 */
	private String fileName ;
	
	/**
	 * Source Folder Path
	 */
	@Value("${mss.src.folder.path}")
	private String workFolderPath;
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	BufferedWriter bufferWriter = null;
	
	/**
	 * @author  List<? extends DailyMSSDatabean> items
	 *
	 */
	public void write(List<? extends DailyMSSDatabean> items) throws Exception {
		
		try{
			if (lOGGER.isInfoEnabled()) {
				lOGGER.debug(" Entering write method of DailyMSSFileWriter .. ");
			}
			File sourceFolder = new File(workFolderPath);
			DailyMSSDatabean dailyMSSDatabean = null;
			String mssFeed = null;
			if (sourceFolder.exists()) {
				bufferWriter = new BufferedWriter(new FileWriter((fileName),true));

				for (int i = 0; i < items.size(); i++) {
					dailyMSSDatabean = items.get(i);
					mssFeed = dailyMSSDatabean.getMssFeed();
					bufferWriter.append(mssFeed);
					lOGGER.debug(" DailyMSSFileWriter TXT File Content  "
							+ mssFeed);
					bufferWriter.append(System.lineSeparator());
				}
				if (!CommonUtil.isNull(bufferWriter)) {
					bufferWriter.flush();
				}
			} else {
				if (lOGGER.isInfoEnabled()) {
					lOGGER.error("Source Folder Does not Exists.");
				}
				throw new PhotoOmniException("Source Folder Does not Exists.");
			}
		} catch(NullPointerException e ){
			lOGGER.error("DailyMSSFileWriter  error.." + e.getMessage());
			throw new PhotoOmniException("Source Folder Does not Exists.");
		} finally{
			if(lOGGER.isInfoEnabled()){
				lOGGER.debug(" Exiting write method of DailyMSSFileWriter ");
			}
		}
	}
	
	/**
	 * This method Close the file after flushing dates.
	 * @throws PhotoOmniException custom Exception
	 */
	@AfterStep
	private void closeFile() throws PhotoOmniException {
		
		try {
			if (lOGGER.isDebugEnabled()) {
				lOGGER.debug(" Entering closeFile method of TXT File DailyMSS ");
			}
			if (!CommonUtil.isNull(bufferWriter)) {
				bufferWriter.close();
			}
		} catch (IOException e) {
			lOGGER.error(" Error occoured at closeFile method of TXT File DailyMSS " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			lOGGER.info(" Exiting closeFile method of TXT File DailyMSS ");
		}
	}

}
