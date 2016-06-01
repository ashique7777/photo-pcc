package com.walgreens.batch.central.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.walgreens.common.exception.PhotoOmniException;

public class SFTPUtil {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SFTPUtil.class);

	public static boolean sendFile(String SFTPHOST, int SFTPPORT,
			String SFTPUSER, String SFTPPASS, String FILEPATH,
			String SFTPWORKINGDIR) throws JSchException {
		ChannelSftp channelSftp = null;
		try {
			channelSftp = getChannelSftp(SFTPUSER, SFTPHOST, SFTPPORT,
					SFTPPASS, SFTPWORKINGDIR);
			File f = new File(FILEPATH);
			channelSftp.put(new FileInputStream(f), f.getName());

		} catch (Exception ex) {
			LOGGER.error(
					"Exception occured while sending file thru channelSftp in the sendFile method of SFTPUtil where hostname and username is {}, {} --> ",
					channelSftp.getSession().getHost().toString(), channelSftp
							.getSession().getUserName().toString());
			return false;
		}
		return true;
	}

	/*
	 * @params sftpUser user name
	 * 
	 * @params sftpHost host name
	 * 
	 * @params sftpPort port number
	 * 
	 * @params sftpPassword login password
	 * 
	 * @params remoteDirPath remote server working directory
	 */
	public static ChannelSftp getChannelSftp(String sftpUser, String sftpHost,
			int sftpPort, String sftpPassword, String remoteDirPath)
			throws PhotoOmniException {

		ChannelSftp channelSftp = null;
		Session session = null;
		Channel channel = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(sftpUser, sftpHost, sftpPort);
			session.setPassword(sftpPassword);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("PreferredAuthentications",
					"publickey,keyboard-interactive,password");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(remoteDirPath);
		} catch (Exception e) {
			LOGGER.error("Exception occured while getting channelSftp in the getChannelSftp method of SFTPUtil ----> "
					+ e.getMessage());
			throw new PhotoOmniException(
					"Remote directory does not exist and the remote directory path is -- "
							+ remoteDirPath);
		}
		return channelSftp;
	}

	/*
	 * To move the files from remote server.
	 * 
	 * @params moveFileFlag set flag true to move file only from the remote
	 * server
	 * 
	 * @params channelSftp the underlying channel
	 * 
	 * @params remoteDirPath path of the remote server (Source)
	 * 
	 * @params entry LsEntry to get the file name on the remote server
	 * 
	 * @params destination path where file should be copied (Destination)
	 */
	public static void moveFiles(boolean moveFileFlag, ChannelSftp channelSftp,
			String remoteDirPath, LsEntry entry, String destination)
			throws JSchException {

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		File fileObj = null;
		OutputStream outputStream = null;
		BufferedOutputStream bos = null;
		if (moveFileFlag) {
			try {
				channelSftp.cd(remoteDirPath);

				bis = new BufferedInputStream(channelSftp.get(entry
						.getFilename()));
				fileObj = new File(destination + entry.getFilename());
				outputStream = new FileOutputStream(fileObj);
				bos = new BufferedOutputStream(outputStream);
				int readCount;
				while ((readCount = bis.read(buffer)) > 0) {
					bos.write(buffer, 0, readCount);
				}
				bis.close();
				bos.close();
				/*
				 * channelSftp.get(entry.getFilename(), fILEPATH +
				 * entry.getFilename());
				 */
			} catch (Exception e) {
				LOGGER.error(
						"Exception occured while moving file form sftp server in moveFiles method of SFTPUtil where hostname and username is {}, {} --> ",
						channelSftp.getSession().getHost().toString(),
						channelSftp.getSession().getUserName().toString()
								+ e.getMessage());
			}
		}
	}

	/*
	 * To delete the list of files on the remote server.
	 * 
	 * @params fileList list of files to be deleted
	 * 
	 * @params deleteFileFlag set flag true to delete
	 * 
	 * @params channelSftp the underlying channel
	 * 
	 * @params remoteDirPath path of the remote server
	 */
	public static void deleteFiles(List<String> fileList,
			boolean deleteFileFlag, ChannelSftp channelSftp,
			String remoteDirPath) throws JSchException {
		if (deleteFileFlag && fileList.size() != 0) {
			try {
				for (String str : fileList) {
					channelSftp.rm(remoteDirPath + str);
				}
			} catch (Exception e) {
				LOGGER.error(
						"Exception occured while deleting file form sftp server in deleteFiles method of SFTPUtil where hostname and username is {}, {} --> ",
						channelSftp.getSession().getHost().toString(),
						channelSftp.getSession().getUserName().toString()
								+ e.getMessage());
			}
		}
	}
}