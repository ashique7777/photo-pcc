package com.walgreens.batch.central.dao;

import java.util.List;
import java.util.Map;

import com.walgreens.common.exception.PhotoOmniException;

public interface KPIDAO {

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
			throws PhotoOmniException;

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
	 * This method is used to update the data in KPI Transaction table once file
	 * is archived so that next time same data will not be picked
	 * 
	 * @param kpiTransmissionDate
	 * @throws PhotoOmniException
	 */
	public abstract void updateOnArchive(String kpiTransmissionDate)
			throws PhotoOmniException;

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
	 * This method will be used to get list of data from KPI Transaction table
	 * if KPI system not transmitted the file from EXACT location. and also this
	 * method is used while system will generate default zero value for stat
	 * 
	 * @param yesterdayDate
	 * @param currentDate
	 * 
	 * @return
	 * @throws PhotoOmniException
	 */
	public abstract List<Map<String, Object>> getLatestUntransmittedPmData(
			String yesterdayDate, String currentDate) throws PhotoOmniException;

	/**
	 * This method will be used to get list of active and send zero flag for all
	 * stat id's from the KPI stat table
	 * 
	 * @return list of all stat status
	 * @throws PhotoOmniException
	 */
	public abstract List<Map<String, Object>> getAllValidStat()
			throws PhotoOmniException;

	public abstract List<Map<String, Object>> getUntransmittedOrderData(
			String maxTransmissionDate, String yesterdayDate)
			throws PhotoOmniException;

	List<String> checkStoreClosed(String noOfDays) throws PhotoOmniException;

}