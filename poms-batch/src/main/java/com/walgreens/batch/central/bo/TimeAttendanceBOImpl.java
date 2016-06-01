package com.walgreens.batch.central.bo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.PhotoOmniFileUtil;

@Component("TimeAttendanceBO")
public class TimeAttendanceBOImpl implements TimeAttendanceBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TimeAttendanceBOImpl.class);

	public void transferExactFileToArchiveFolder(
			final String tandAExactFileLocation,
			final String tandAArchiveFileLocation) throws PhotoOmniException {

		File exactFolder = new File(tandAExactFileLocation);
		File archiveFolder = new File(tandAArchiveFileLocation);
		String fileNameWithExact = PhotoOmniConstants.EXACT_PROCESSED;
		File[] listOfFiles = exactFolder.listFiles();
		renameFiles(listOfFiles, tandAExactFileLocation);
		try {
			PhotoOmniFileUtil.moveFilesContainingText(exactFolder,
					archiveFolder, fileNameWithExact);
			LOGGER.debug("All the file names containing 'eXact_PROCESSED' has been moved to Archive folder");
		} catch (NullPointerException e) {
			LOGGER.error(" Error occoured at transferExactFileToArchiveFolder method of TimeAttendanceBOImpl - "
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} catch (IOException e) {
			LOGGER.error(" Error occoured at transferExactFileToArchiveFolder method of TimeAttendanceBOImpl -"
					+ e);
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting transferExactFileToArchiveFolder method of TimeAttendanceBOImpl ");
			}
		}
	}

	/**
	 * This method renames the file from Exact folder as
	 * "Archive_EXACT_PhotoPM_Date.csv" (append archive before and date at the
	 * end )
	 * 
	 * @param listOfFiles
	 * @param tandAExactFileLocation
	 * @throws PhotoOmniException
	 */
	private static void renameFiles(File[] listOfFiles,
			String tandAExactFileLocation) throws PhotoOmniException {
		try {
			for (File file : listOfFiles) {
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						PhotoOmniConstants.DATE_FORMAT_TWO);
				String modifiedFileName = null;
				String dateString = dateFormat.format(new Date());
				String originalFileName = file.getName();
				String base = FilenameUtils.removeExtension(originalFileName);
				String extension = FilenameUtils.getExtension(originalFileName);
				if (extension.contains(PhotoOmniConstants.EXACT_PROCESSED)) {
					String basecsv = FilenameUtils.removeExtension(base); 
					String extensioncsv = FilenameUtils.getExtension(base);
					modifiedFileName = PhotoOmniConstants.ARCHIVE_FILE_START_NAME
							+ basecsv
							+ PhotoOmniConstants.UNDERSCORE
							+ dateString + "." + extensioncsv;
				} else {
				 modifiedFileName = PhotoOmniConstants.ARCHIVE_FILE_START_NAME
						+ base
						+ PhotoOmniConstants.UNDERSCORE
						+ dateString + "." + extension;
				}
				file.renameTo(new File(tandAExactFileLocation 
						+ modifiedFileName));
			}
		} catch (Exception e) {
			LOGGER.error("Error while renaming files in Exact folder ---> ", e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public boolean checkDestinationFolderForExactFile(
			String tandAExactFileLocation) {
		boolean isPresent = false;
		File files = new File(tandAExactFileLocation);
		File[] listOfFiles = files.listFiles();
		for (File file : listOfFiles) {
			if (file.getName()
					.contains(PhotoOmniConstants.EXACT_PROCESSED)) {
				isPresent = true;
			} else {
				file.delete();
			}
		}
		return isPresent;
	}
}
