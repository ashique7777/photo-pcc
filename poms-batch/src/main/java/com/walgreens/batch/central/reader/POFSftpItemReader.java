package com.walgreens.batch.central.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import com.walgreens.batch.central.bean.SFTPDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.exception.PhotoOmniException;


/**
 * @author CTS
 * 
 */

public class POFSftpItemReader implements ItemReader<SFTPDataBean>{
	
	SFTPDataBean  sftpDataBean = null; 
	
	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFSftpItemReader.class);
	
	static int count = 0;
	
	@Override
	public SFTPDataBean read() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering read method of POFSftpItemReader ");
		}
			try {
				if(count == 0){
					sftpDataBean = new SFTPDataBean();
					sftpDataBean.setSftpHost(PhotoOmniBatchConstants.SFTP_HOST);
					sftpDataBean.setSftpPort(Integer.parseInt(PhotoOmniBatchConstants.SFTP_PORT));
					sftpDataBean.setSftpUser(PhotoOmniBatchConstants.SFTP_USER);
					sftpDataBean.setSftpPassword(PhotoOmniBatchConstants.SFTP_PASSWORD);
					sftpDataBean.setSftpDir(PhotoOmniBatchConstants.SFTP_TXT_FILE_REMOTE_LOCATION);
					count++;
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFSftpItemReader ");
					}
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(" Exiting read method of POFSftpItemReader ");
					}
					sftpDataBean = null;
				}
			} catch (Exception e) {
				LOGGER.error(" Error occoured at read method of POFSftpItemReader - " + e.getMessage());
				throw new PhotoOmniException(e.getMessage());
			} finally {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(" Exiting read method of v ");
				}
			}
				
			
			return sftpDataBean;
	}
	
	
}
