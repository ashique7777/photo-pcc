package com.walgreens.batch.central.tasklet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.utility.SFTPUtil;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;

/**
 * This class implements the functionality required to transfer the PhotoPM.csv
 * file from local work directory to remote server folder
 * 
 * @author Cognizant
 * 
 */
//@Configuration
//@PropertySource("classpath:PhotoOmniBatch.properties")
public class TandASftpTasklet implements Tasklet,PhotoOmniConstants {

	@Value("${tanda.sftp.host.name}")
	private String hostName;
	@Value("${tanda.sftp.user.name}")
	private String userName;
	@Value("${tanda.sftp.password}")
	private String password;
	@Value("${tanda.sftp.port.number}")
	private int portNumber;
	
	/**
	 * logger to log the details.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(TandASftpTasklet.class);

	private String fileName;

	
	private JdbcTemplate jdbcTemplate;

	@Value("${tanda.archive.folder.path}")
	private String archiveFolderpath;

	@Value("${tanda.work.folder.path}")
	private String workFolderPath;

	@Value("${tanda.sftp.file.path}")
	private String remoteDirectory;

	/**
	 * This method returns the value of the jdbcTemplate attribute
	 * 
	 * @return JDBC template object
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * This method sets the value of the jdbcTemplate attribute
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * This method returns the value of the remoteDirectory attribute
	 * 
	 * @return String
	 */
	public String getRemoteDirectory() {
		return remoteDirectory;
	}

	/**
	 * This method sets the value of the remoteDirectory attribute
	 * 
	 * @param remoteDirectory
	 */
	public void setRemoteDirectory(String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}

	/**
	 * This method returns the value of the hostName attribute
	 * 
	 * @return String
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * This method sets the value of the hostName attribute
	 * 
	 * @param hostName
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * This method returns the value of the userName attribute
	 * 
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method sets the value of the userName attribute
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * This method returns the value of the password attribute
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the value of the password attribute
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * This method returns the value of the fileName attribute
	 * 
	 * @return String
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * This method sets the value of the fileName attribute
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.batch.core.step.tasklet.Tasklet#execute(org.
	 * springframework.batch.core.StepContribution,
	 * org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		if (log.isInfoEnabled()) {
			log.info("Entering in Time and Attendance Sftp Tasklet....");
		}

		ResourceBundle propFile = null;
		File sourceFile = new File(workFolderPath);
		File[] files = new File(workFolderPath).listFiles();

		boolean isSftpSuccess = false;
		if (sourceFile.exists()) {
			log.info("Source file is present in Time and Attendance local work folder....");

			try {
				for (File file : files) {

					if (!TIME_ATTENDANCE_CSV_FILE_NAME.equals(file.getName()))
						continue;

					if (file.isFile()) {

						isSftpSuccess = SFTPUtil.sendFile(hostName, portNumber,
								userName, password, file.getAbsolutePath(),
								remoteDirectory);
						if (!isSftpSuccess) {
							if (log.isInfoEnabled()) {
								log.info("SFTP fail for file ...."
										+ file.getName());
								throw new PhotoOmniException("SFTP failed");
							}
						} else {
							if (log.isInfoEnabled()) {
								log.info("SFTP success for file ...."
										+ file.getName());
							}
						}
					}

					Date date = new Date();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							DATE_FORMAT_TWO);
					String dateString = dateFormat.format(date);
					File newFile = new File(workFolderPath + "Archive_"
							+ file.getName().replace(CSV_FILE_EXTENSION, EMPTY_SPACE_CHAR) + UNDERSCORE
							+ dateString + CSV_FILE_EXTENSION);
					copyFile(file, newFile);

					if (isSftpSuccess) {

						propFile = ResourceBundle
								.getBundle("PhotoOmniBatchSQL");
						String updateShoppingCart = propFile
								.getString("UPDATE_OM_SHOPPING_CART");
						String updateOrderAttribute = propFile
								.getString("UPDATE_OM_ORDER_ATTRIBUTE");
						String updateOrderPm = propFile
								.getString("UPDATE_OM_ORDER_PM");

						log.info("About to zip the flat file");
						zipIt(file.getAbsolutePath(),
								archiveFolderpath + ARCHIVE_FILE_START_NAME
										+ file.getName().replace(CSV_FILE_EXTENSION, EMPTY_SPACE_CHAR)
										+ "_" + dateString + ZIP_FILE_EXTENSION);
						// Updating Time_Attendance_Status to Y after successful
						// SFTP
						log.info("Updating Time_Attendance_Status to Y after successful SFTP");
						
						jdbcTemplate.execute(updateOrderPm);

						// Updating PM_STATUS to T after successful
						// SFTP
						log.info("Updating PM_STATUS to T after successful SFTP");
						jdbcTemplate.execute(updateOrderAttribute);

						// Updating PM_STATUS_CD to T after successful
						// SFTP
						log.info("Updating PM_STATUS_CD to T after successful SFTP");
						jdbcTemplate.execute(updateShoppingCart);
						log.info("All the values are uopdated");

						if (log.isInfoEnabled()) {
							log.info("time_attendance_cd and REPORT_SENT_DTTM updated in Time and Attendance batch process....");
						}
						log.info("Deleting renamed flat file");
						newFile.deleteOnExit();
						log.info("Deleting flat file");
						file.deleteOnExit();
						
						System.gc();
					}

				}

				if (log.isInfoEnabled()) {
					log.info("Sftp Successfully Completed....");
				}
			} catch (Exception e) {
				log.error("Could not send file per SFTP: ", e);
				throw new PhotoOmniException(e.getMessage());
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("File does not exist.");
			}
		}

		return RepeatStatus.FINISHED;

	}

	/**
	 * This private method zips a source file to an archive file at a specified
	 * directory
	 * 
	 * @param srcFilename
	 * @param zipFile
	 */
	public static void zipIt(String srcFilename, String zipFile) {

		if (log.isInfoEnabled()) {
			log.info("Entering into TandASftpTasklet:zip()");
		}
		try {

			// create byte buffer
			byte[] buffer = new byte[1024];

			FileOutputStream fos = new FileOutputStream(zipFile);

			ZipOutputStream zos = new ZipOutputStream(fos);

			File srcFile = new File(srcFilename);

			FileInputStream fis = new FileInputStream(srcFile);

			// begin writing a new ZIP entry, positions the stream to the start
			// of the entry data
			zos.putNextEntry(new ZipEntry(srcFile.getName()));

			int length;

			while ((length = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, length);
			}

			zos.closeEntry();

			// close the InputStream
			fis.close();

			// close the ZipOutputStream
			zos.close();

			if (log.isInfoEnabled()) {
				log.info("In Time and Attendance batch process : "
						+ srcFilename + " zipped to " + zipFile);
			}

		} catch (IOException ioe) {
			log.error("Error creating zip file", ioe);
		}
		if (log.isInfoEnabled()) {
			log.info("Exiting from TandASftpTasklet:zip()");
		}
	}

	/**
	 * This private method copies a source file to a new file at a specified
	 * directory
	 * 
	 * @param sourceFile
	 * @param destFile
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (log.isInfoEnabled()) {
			log.info("Entering into TandASftpTasklet:copyFile()");
		}
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();

			// previous code: destination.transferFrom(source, 0,
			// source.size());
			// to avoid infinite loops, should be:
			long count = 0;
			long size = source.size();
			while ((count += destination.transferFrom(source, count, size
					- count)) < size)
				;

			if (log.isInfoEnabled()) {
				log.info("In Time and Attendance batch process : "
						+ (sourceFile != null ? sourceFile.getAbsolutePath()
								: "no file is")
						+ " copied to "
						+ (destFile != null ? destFile.getAbsolutePath()
								: "nowhere"));
			}

		} finally {
			if (source != null) {
				log.info("Closing source");
				source.close();
			}
			if (destination != null) {
				log.info("Closing destinition");
				destination.close();
			}
			if (log.isInfoEnabled()) {
				log.info("Exiting from TandASftpTasklet:copyFile()");
			}
		}
	}

}
