package com.walgreens.batch.central.bo;

import com.walgreens.common.exception.PhotoOmniException;

public interface TimeAttendanceBO {

	/**
	 * This method will be used for archive the time and attendance feed file.
	 * 
	 * @param tandAExactFileLocation
	 *            : exact folder location
	 * @param tandAArchiveFileLocation
	 *            : archive folder location
	 * @throws PhotoOmniException
	 */
	public void transferExactFileToArchiveFolder(
			final String tandAExactFileLocation,
			final String tandAArchiveFileLocation) throws PhotoOmniException;

	/**
	 * This method check the destination folder for EXACT renamed file
	 * 
	 * @param tandAExactFileLocation
	 * @return status true if EXACT renamed file is present in destination
	 *         folder else false
	 */
	public boolean checkDestinationFolderForExactFile(
			String tandAExactFileLocation);
}
