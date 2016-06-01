package com.walgreens.batch.central.writer;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.walgreens.batch.central.bean.SFTPDataBean;
import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;


public class POFSftpItemWriter implements ItemWriter<SFTPDataBean>{

	/**
	 * LOGGER for logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(POFSftpItemWriter.class);
	
	@Override
	public void write(List<? extends SFTPDataBean> items) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering write method of POFSftpItemWriter ");
		}
		try {
			boolean sftpSuccess = false; 
			final SFTPDataBean sftpDataBean = items.get(0);
			String localFilePath = PhotoOmniBatchConstants.SFTP_TXT_FILE_LOCATION; // WORK Folder Location
			String remoteDirPath = PhotoOmniBatchConstants.SFTP_TXT_FILE_REMOTE_LOCATION;
			String remoteFileName = PhotoOmniBatchConstants.SFTP_TXT_FILE_NAME;
			String archiveFolderpath = PhotoOmniBatchConstants.ARCHIVE_FOLDER_PATH;
			
			File source = new File(localFilePath); 
	        File dest = new File(archiveFolderpath);
			
			LOGGER.info("SFTP Local File Path "+ localFilePath);
			LOGGER.info("SFTP Remote Dir path "+ remoteDirPath);
			LOGGER.info("SFTP Remote File name "+ remoteFileName);
			
			sftpSuccess = sftpFileToDir(localFilePath, remoteDirPath,
					remoteFileName,sftpDataBean);
			if (sftpSuccess) {
				if(!source.exists()){
					LOGGER.info("Directory does not exist.");
		         }else{
		            CommonUtil.copyFolder(source,dest);
		            LOGGER.info("File Copy Successfully After SFTP....");
		         }
			}
			
		} catch (Exception e) {
			LOGGER.error(" Error occoured at process write of PSReportEmailItemWriter - " + e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting write method of PSReportEmailItemWriter ");
			}
		}
		
	}
	/**
	 * This method makes connection with all credentials with the remote server
	 * and puts the file in that location through SFTP.
	 * 
	 * @param localFilePath
	 * @param remoteDirPath
	 * @param remoteFileName
	 * @return
	 */

	public static boolean sftpFileToDir(String localFilePath, String remoteDirPath,
			String remoteFileName,SFTPDataBean sftpBean) {
		
		LOGGER.info(" Entering sftpFileToDir method of CommonUtil ");
		
		boolean sftpSuccess = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		String sftpUser = sftpBean.getSftpUser();
		String sftpPassword = sftpBean.getSftpPassword();
		int sftpPort = sftpBean.getSftpPort();
		String sftpDir = sftpBean.getSftpDir();
		String sftpHost = sftpBean.getSftpHost();
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			session.setPassword(sftpPassword);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			if (null != remoteDirPath)
				channelSftp.cd(remoteDirPath);
			else
				channelSftp.cd(sftpDir);

			File f = new File(localFilePath + "/" + remoteFileName);
			String fileName = f.getName();
			if (null != remoteFileName && remoteFileName.length() > 0)
				fileName = remoteFileName;

			channelSftp.put(new FileInputStream(f), fileName);

			sftpSuccess = true;
			// Disconnecting the channel
			channel.disconnect();
			// Disconnecting the session
			session.disconnect();

		} catch (Exception ex) {
			LOGGER.error(" Error occoured at sftpFileToDir method of CommonUtil - " + ex.getMessage());
		}

		return sftpSuccess;
	}

}

