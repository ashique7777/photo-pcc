package com.walgreens.batch.central.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.walgreens.common.exception.PhotoOmniException;

public interface KPIBO {

	/**
	 * This method is used to populate the data for KPI transaction table.KPI
	 * transaction table is used for getting data for KPI Feed File.
	 * 
	 * @param finalData
	 *            : finalData is a list which will hold the data which will be
	 *            inserted into KPI transaction tables
	 * @param tableName
	 *            : depending upon the Stat table name will passed through
	 *            caller method
	 * @param kpiTransmissionFlag
	 *            : for order related Stat we need to update the existing record
	 *            in transaction table if this parameter is coming true one more
	 *            condition will added in query to check KPI_TRANSMISSION_CD
	 * @throws PhotoOmniException
	 */
	public abstract void populateKPITranscations(
			List<Map<String, Object>> finalData, String tableName,
			boolean kpiTransmissionFlag) throws PhotoOmniException;

	/**
	 * This method will used to validate the given stat if stat is valid than it
	 * will generate the data in KPI transaction table if stat is not valid
	 * method will skip the stat and continue with other stat
	 * 
	 * @param stat
	 *            : Stat id from OM_KPI_STAT table which will get validated to
	 *            check for ACTIVE_CD and TRANSMIT_ZERO_CD
	 * @param items
	 *            : data which is coming from different classes for different
	 *            Stat
	 * @param finalData
	 *            : A list which hold newly prepared data from items and
	 *            finalData will get inserted or updated in KPI transaction
	 *            table
	 * @return
	 * @throws PhotoOmniException
	 */
	public abstract List<Map<String, Object>> validateStatAndPrepareTransactionData(
			String stat, List<? extends Map<String, Object>> items,
			List<Map<String, Object>> finalData) throws PhotoOmniException;

	/**
	 * This method will called before system will start stat calculation for KPI
	 * Feed File.
	 * 
	 * @param kpiCurrentDate
	 *            : date for which KPI feed file need the generate
	 * @param deployMentType
	 *            : help to decide to pick the store no based on chain wise or
	 *            pilot deployment
	 * @throws PhotoOmniException
	 */
	public abstract void updateKPIIndicator(String kpiCurrentDate,
			boolean deployMentType) throws PhotoOmniException;

	/**
	 * This method will used the update the POS Transaction table after
	 * PHALTOS,PHALCSDT and PHALCCDT stat calculation
	 * 
	 * @throws PhotoOmniException
	 */
	public abstract void updatePOSTransaction() throws PhotoOmniException;

	/**
	 * This method will be used to update the KPI_TRANSMIT_CD in KPI transaction
	 * table once file will moved to EXACT folder so that system will not pick
	 * same data again
	 * 
	 * @param jobExecutionId
	 *            : this parameter is used to get the batch start time which
	 *            require in update query
	 * @throws PhotoOmniException
	 */
	public abstract void updateKPITransactionAfterTransmit(long jobExecutionId)
			throws PhotoOmniException;

	/**
	 * This method is used to fetch the MaxTransmissionDate which will pass as a
	 * parameter in order related stat calculation.
	 * 
	 * @return date in string format
	 * @throws PhotoOmniException
	 */
	public abstract String getMaxTransmissionDate() throws PhotoOmniException;

	/**
	 * This method will used the update the POS Transaction table after PHALDPMP
	 * and PHALDPMS stat calculation
	 * 
	 * @param kpiCurrentDate
	 *            : date for which stat is calculated
	 * @throws PhotoOmniException
	 */
	public abstract void updateOrderPMTransaction(String kpiCurrentDate)
			throws PhotoOmniException;

	/**
	 * This method will be used for archive the KPI feed file.
	 * 
	 * @param kPIExactFileLocation
	 *            : exact folder location
	 * @param kPIArchiveFileLocation
	 *            : archive folder location
	 * @throws PhotoOmniException
	 */
	public abstract void getRemoteFile(final String kPIExactFileLocation,
			final String kPIArchiveFileLocation) throws PhotoOmniException;

	/**
	 * This method will check if given filePath is a directory or not. if it is
	 * not directory system will through exception.
	 * 
	 * @param filePathList
	 *            : List of path which need to get validate
	 * @throws PhotoOmniException
	 */
	public abstract void checkDirectoryExists(ArrayList<String> filePathList)
			throws PhotoOmniException;

	/**
	 * This method is used to get yesterday's date
	 * 
	 * @return : yesterday's date
	 */
	public abstract Date getYesterdayDate();

	/**
	 * This method is used to fetch the ACTIVE_CD and TRANSMIT_ZERO_CD value for
	 * given stat
	 * 
	 * @param stat
	 *            : stat id form OM_KPI_STAT
	 * @return : if for given stat data is present in data return a list else
	 *         return empty list
	 * @throws Exception
	 */
	public abstract List<Map<String, Object>> validateStat(String stat)
			throws Exception;

	/**
	 * This method is used to format the date in given pattern
	 * 
	 * @param date
	 *            : date which need to get formatted.
	 * @param formatPattern
	 *            : format patterns
	 * @return : formatted string
	 */
	public abstract String getFormattedDate(Date date, String formatPattern);

	/**
	 * This method will be used to generate zero value for given store numbers
	 * for all stats for which TRANSMIT_ZERO_CD is 1 and data not return by
	 * query.
	 * 
	 * @param storeNos
	 *            : list for store number for which default values need to
	 *            populate
	 * @param currentDate
	 * @throws PhotoOmniException
	 */
	public abstract void populateKPIPmStatZeroValue(List<String> storeNos,
			String currentDate) throws PhotoOmniException;

	/**
	 * This method will give store numbers if deployment type is chain wise
	 * 
	 * @return list of store numbers
	 * @throws PhotoOmniException
	 */
	public abstract List<String> getAllStoreNos() throws PhotoOmniException;

	/**
	 * This method will give store numbers if deployment type is pilot wise
	 * 
	 * @return list of store numbers
	 * @throws PhotoOmniException
	 */
	public abstract List<String> getStoreNos() throws PhotoOmniException;

	/**
	 * This method will give list distinct store number based on deployment type
	 * and KPI transaction table
	 * 
	 * @param deployMentType
	 *            :
	 * @return
	 * @throws PhotoOmniException
	 */
	public abstract List<String> getUniqueStoreNumber(String deployMentType)
			throws PhotoOmniException;

	public abstract void populateKPIOrderStatZeroValue(List<String> storeNo,
			String maxTransmissionDate) throws PhotoOmniException;

	List<String> checkStoreClosed(List<String> storeNo, String noOfDays)
			throws PhotoOmniException;

}