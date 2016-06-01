package com.walgreens.batch.central.bo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.central.dao.KPIDAO;
import com.walgreens.batch.central.factory.KPIDAOFactory;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.PhotoOmniFileUtil;

@Component
public class KPIBoImpl implements KPIBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIBoImpl.class);

	@Autowired
	private KPIDAOFactory factory;

	private List<Map<String, Object>> prepareData(String stat, long transmitCD,
			List<? extends Map<String, Object>> items,
			List<Map<String, Object>> finalData) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering prepareData method of KPIBoImpl.Class ");
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALCSDT)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("SOLD_AMT"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("LOCATION_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("BUSINESS_DATE"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALTOS)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("ENVELOPE_NBR"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("LOCATION_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("BUSINESS_DATE"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALCCDT)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("COST"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("LOCATION_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("BUSINESS_DATE"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALDPMS)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("EARNED_AMOUNT"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("LOCATION_NBR"));
					if (item.get("SOLD_DTTM") == null) {
						map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
								getFormattedDate(getYesterdayDate(),
										PhotoOmniConstants.DATE_FORMAT_EIGHT));
					} else {
						map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
								item.get("SOLD_DTTM"));
					}
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALDPMP)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("FINAL_PRICE"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("LOCATION_NBR"));
					if (item.get("SOLD_DTTM") == null) {
						map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
								getFormattedDate(getYesterdayDate(),
										PhotoOmniConstants.DATE_FORMAT_EIGHT));
					} else {
						map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
								item.get("SOLD_DTTM"));
					}
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALPTOH)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("PHALPTOH"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("STORE_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("DATE_TIME"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE,
							item.get("PHALPTOH_SIZE"));
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALCTOH)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("PHALCTOH"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("STORE_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("DATE_TIME"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALPINT)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("PHALPINT"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("STORE_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("DATE_TIME"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE,
							item.get("PHALPINT_SIZE"));
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALCINT)) {
			for (Map<String, Object> item : items) {
				if ((boolean) item.get(PhotoOmniBatchConstants.IS_VALID_PAIR)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(PhotoOmniBatchConstants.STAT_ID, stat);
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							item.get("PHALCINT"));
					map.put(PhotoOmniBatchConstants.STORE_NO,
							item.get("STORE_NBR"));
					map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
							item.get("DATE_TIME"));
					map.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
					finalData.add(map);
				}
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALPPTA)) {
			for (Map<String, Object> item : items) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(PhotoOmniBatchConstants.STAT_ID, stat);
				map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
						item.get("PHALPPTA"));
				map.put(PhotoOmniBatchConstants.STORE_NO, item.get("STORE_NBR"));
				map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
						item.get("DATE_TIME"));
				map.put(PhotoOmniBatchConstants.SAMPLE_SIZE,
						item.get("PHALPPTA_SIZE"));
				if (checkTransmitZero(transmitCD, map))
					finalData.add(map);
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALPPTB)) {
			for (Map<String, Object> item : items) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(PhotoOmniBatchConstants.STAT_ID, stat);
				map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
						item.get("PHALPPTB"));
				map.put(PhotoOmniBatchConstants.STORE_NO, item.get("STORE_NBR"));
				map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
						item.get("DATE_TIME"));
				map.put(PhotoOmniBatchConstants.SAMPLE_SIZE,
						item.get("PHALPPTB_SIZE"));
				if (checkTransmitZero(transmitCD, map))
					finalData.add(map);
			}
		}
		if (stat.equalsIgnoreCase(PhotoOmniBatchConstants.PHALPPTC)) {
			for (Map<String, Object> item : items) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(PhotoOmniBatchConstants.STAT_ID, stat);
				map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
						item.get("PHALPPTC"));
				map.put(PhotoOmniBatchConstants.STORE_NO, item.get("STORE_NBR"));
				map.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
						item.get("DATE_TIME"));
				map.put(PhotoOmniBatchConstants.SAMPLE_SIZE,
						item.get("PHALPPTC_SIZE"));
				if (checkTransmitZero(transmitCD, map))
					finalData.add(map);
			}
		}
		return finalData;
	}

	private boolean checkTransmitZero(long transmitCD, Map<String, Object> map) {
		return (transmitCD == 1 ? true : ((Double.valueOf(map.get(
				PhotoOmniBatchConstants.SAMPLE_VALUE).toString()) > 0) ? true
				: false));
	}

	@Override
	public void populateKPITranscations(List<Map<String, Object>> finalData,
			String tableName, boolean kpiTransmissionFlag)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getKpiTransactionRec method KPIBoImpl.Class ");
		}
		KPIDAO kpidaoImpl = factory.getKpidao();
		kpidaoImpl.populateKPITranscations(finalData, tableName,
				kpiTransmissionFlag);
	}

	@Override
	public List<Map<String, Object>> validateStatAndPrepareTransactionData(
			String stat, List<? extends Map<String, Object>> items,
			List<Map<String, Object>> finalData) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getKpiStatStatus method KPIBoImpl.Class ");
		}
		KPIDAO kpidaoImpl = factory.getKpidao();
		List<Map<String, Object>> list = kpidaoImpl.validateStat(stat);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			long active = CommonUtil.bigDecimalToLong(map.get("ACTIVE_CD"));
			long transmitCD = CommonUtil.bigDecimalToLong(map
					.get("TRANSMIT_ZERO_CD"));
			if (active > 0) {
				validateAndPreparePairedStats(stat, items, transmitCD);
				finalData = prepareData(stat, transmitCD, items, finalData);
			}
		}
		return finalData;
	}

	private void validateAndPreparePairedStats(String stat,
			List<? extends Map<String, Object>> items, long transmitCD) {
		if (PhotoOmniBatchConstants.PHALDPMS.equals(stat)
				|| PhotoOmniBatchConstants.PHALTOS.equals(stat)
				|| PhotoOmniBatchConstants.PHALPTOH.equals(stat)
				|| PhotoOmniBatchConstants.PHALPINT.equals(stat)) {
			for (Map<String, Object> map : items) {
				map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
				if (PhotoOmniBatchConstants.PHALDPMS.equals(stat)
						&& transmitCD == 1) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
					if (Double.valueOf(map.get("EARNED_AMOUNT").toString()) <= 0) {
						map.put("FINAL_PRICE", 0.0);
					}
				}
				if (PhotoOmniBatchConstants.PHALDPMS.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("EARNED_AMOUNT").toString()) <= 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, false);
				}
				if (PhotoOmniBatchConstants.PHALDPMS.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("EARNED_AMOUNT").toString()) > 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
				}
				if (PhotoOmniBatchConstants.PHALTOS.equals(stat)
						&& transmitCD == 1) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
					if (Integer.valueOf(map.get("ENVELOPE_NBR").toString()) <= 0) {
						map.put("COST", 0.0);
						map.put("SOLD_AMT", 0.0);
					}
				}
				if (PhotoOmniBatchConstants.PHALTOS.equals(stat)
						&& transmitCD == 0
						&& Integer.valueOf(map.get("ENVELOPE_NBR").toString()) <= 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, false);
				}
				if (PhotoOmniBatchConstants.PHALTOS.equals(stat)
						&& transmitCD == 0
						&& Integer.valueOf(map.get("ENVELOPE_NBR").toString()) > 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
				}
				if (PhotoOmniBatchConstants.PHALPTOH.equals(stat)
						&& transmitCD == 1) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
					if (Double.valueOf(map.get("PHALPTOH_SIZE").toString()) <= 0) {
						map.put("PHALCTOH", 0.0);
					}
				}
				if (PhotoOmniBatchConstants.PHALPTOH.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("PHALPTOH_SIZE").toString()) <= 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, false);
				}
				if (PhotoOmniBatchConstants.PHALPTOH.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("PHALPTOH_SIZE").toString()) > 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
				}
				if (PhotoOmniBatchConstants.PHALPINT.equals(stat)
						&& transmitCD == 1) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, true);
					if (Double.valueOf(map.get("PHALPINT_SIZE").toString()) <= 0) {
						map.put("PHALCINT", 0.0);
					}
				}
				if (PhotoOmniBatchConstants.PHALPINT.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("PHALPINT_SIZE").toString()) <= 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, false);
				}
				if (PhotoOmniBatchConstants.PHALPINT.equals(stat)
						&& transmitCD == 0
						&& Double.valueOf(map.get("PHALPINT_SIZE").toString()) > 0) {
					map.put(PhotoOmniBatchConstants.IS_VALID_PAIR, false);
				}
			}
		}
	}

	@Override
	public void updateKPIIndicator(String kpiCurrentDate, boolean deployMentType)
			throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		kpidaoImpl.updateKPIIndicator(kpiCurrentDate, deployMentType);

	}

	@Override
	public void updatePOSTransaction() throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		kpidaoImpl.updatePOSTransaction();

	}

	@Override
	public void updateKPITransactionAfterTransmit(long jobExecutionId)
			throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		kpidaoImpl.updateKPITransactionAfterTransmit(jobExecutionId);

	}

	@Override
	public String getMaxTransmissionDate() throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		return kpidaoImpl.getMaxTransmissionDate();
	}

	@Override
	public void updateOrderPMTransaction(String kpiCurrentDate)
			throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		kpidaoImpl.updateOrderPMTransaction(kpiCurrentDate);

	}

	@Override
	public void getRemoteFile(final String kPIExactFileLocation,
			final String kPIArchiveFileLocation) throws PhotoOmniException {
		try {
			File files = new File(kPIExactFileLocation);
			File[] listOfFiles = files.listFiles();
			moveFiles(listOfFiles, kPIExactFileLocation, kPIArchiveFileLocation);
		} catch (Exception e) {
			LOGGER.error(
					"Error while getting the list of file which needs to be archived ---> ",
					e);
			throw new PhotoOmniException(e);

		}
	}

	private void moveFiles(File[] listOfFiles,
			final String kPIExactFileLocation,
			final String kPIArchiveFileLocation) throws PhotoOmniException {
		try {
			KPIDAO kpidaoImpl = factory.getKpidao();
			for (File file : listOfFiles) {
				File destDir = null;
				if (file.getName()
						.startsWith(
								PhotoOmniBatchConstants.RENAMED_KPI_FEED_FILE_NAME_PATTERN)
						&& file.getName()
								.endsWith(
										PhotoOmniBatchConstants.END_WITH_RENAMED_KPI_FEED_FILE_NAME_PATTERN)) {
					destDir = new File(kPIArchiveFileLocation);
					PhotoOmniFileUtil.moveFileToDirectory(file, destDir, false);
					kpidaoImpl.updateOnArchive(CommonUtil
							.stringDateFormatChange(
									file.getName().replaceAll("[^0-9]", "")
											.toString(),
									PhotoOmniConstants.DATE_FORMAT_TWELFTH,
									PhotoOmniConstants.DATEFORMATONE));
				} else {
					file.delete();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while moving files to the destination ---> ", e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public void checkDirectoryExists(ArrayList<String> filePathList)
			throws PhotoOmniException {

		for (String str : filePathList) {
			if (!PhotoOmniFileUtil.isDirectoryOrFileExists(true, str, false)) {
				throw new PhotoOmniException(
						"Directory does not exists and the directory path is -- "
								+ str);
			}
		}
	}

	@Override
	public Date getYesterdayDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	@Override
	public List<Map<String, Object>> validateStat(String stat) throws Exception {
		KPIDAO kpidaoImpl = factory.getKpidao();
		return kpidaoImpl.validateStat(stat);
	}

	@Override
	public String getFormattedDate(Date date, String formatPattern) {
		String formattedDate = null;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					formatPattern);
			formattedDate = simpleDateFormat.format(date);
		} catch (Exception e) {
			LOGGER.error(
					"Error while formatting date in the getFormattedDate method of KPIBoImpl.Class ---> ",
					e);
		}
		return formattedDate;
	}

	@Override
	public void populateKPIPmStatZeroValue(List<String> storeNos,
			String currentDate) throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		try {
			List<Map<String, Object>> existigTransactionPmDataList = kpidaoImpl
					.getLatestUntransmittedPmData(
							getFormattedDate(getYesterdayDate(),
									PhotoOmniConstants.DATE_FORMAT_TWO),
							currentDate);
			if (existigTransactionPmDataList.size() == 0) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (String storeNo : storeNos) {
					generateDefaultData(list, storeNo, getYesterdayDate(), true);
				}
				populateKPITranscations(list,
						PhotoOmniBatchConstants.OM_KPI_POS_PM_TRANSACTION,
						false);
			}
		} catch (Exception e) {
			LOGGER.error("Error while populating zero value data for stats", e);
		}
	}

	private Map<String, Map<String, Object>> getValidPairdStat()
			throws PhotoOmniException {
		Map<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
		KPIDAO kpidaoImpl = factory.getKpidao();
		List<Map<String, Object>> ls = kpidaoImpl.getAllValidStat();
		for (Map<String, Object> map : ls) {
			data.put((String) map.get("KPI_STAT_ID"), map);
		}
		if (ls.size() > 0) {
			if (data.get(PhotoOmniBatchConstants.PHALCCDT) != null) {
				data.get(PhotoOmniBatchConstants.PHALCCDT).put(
						"TRANSMIT_ZERO_CD",
						data.get(PhotoOmniBatchConstants.PHALTOS).get(
								"TRANSMIT_ZERO_CD"));
			}
			if (data.get(PhotoOmniBatchConstants.PHALCSDT) != null) {
				data.get(PhotoOmniBatchConstants.PHALCSDT).put(
						"TRANSMIT_ZERO_CD",
						data.get(PhotoOmniBatchConstants.PHALTOS).get(
								"TRANSMIT_ZERO_CD"));
			}
			if (data.get(PhotoOmniBatchConstants.PHALDPMP) != null) {
				data.get(PhotoOmniBatchConstants.PHALDPMP).put(
						"TRANSMIT_ZERO_CD",
						data.get(PhotoOmniBatchConstants.PHALDPMS).get(
								"TRANSMIT_ZERO_CD"));
			}
			if (data.get(PhotoOmniBatchConstants.PHALCTOH) != null) {
				data.get(PhotoOmniBatchConstants.PHALCTOH).put(
						"TRANSMIT_ZERO_CD",
						data.get(PhotoOmniBatchConstants.PHALPTOH).get(
								"TRANSMIT_ZERO_CD"));
			}
			if (data.get(PhotoOmniBatchConstants.PHALCINT) != null) {
				data.get(PhotoOmniBatchConstants.PHALCINT).put(
						"TRANSMIT_ZERO_CD",
						data.get(PhotoOmniBatchConstants.PHALPINT).get(
								"TRANSMIT_ZERO_CD"));
			}
		}
		return data;
	}

	@Override
	public List<String> getAllStoreNos() throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		return kpidaoImpl.getAllStoreNos();
	}

	@Override
	public List<String> getStoreNos() throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		return kpidaoImpl.getStoreNos();
	}

	@Override
	public List<String> getUniqueStoreNumber(String deployMentType)
			throws PhotoOmniException {
		KPIDAO kpidaoImpl = factory.getKpidao();
		if (PhotoOmniBatchConstants.ALL_STORE.equals(deployMentType)) {
			return kpidaoImpl.getAllStoreNos();
		} else {
			return kpidaoImpl.getStoreNos();
		}
	}

	@Override
	public void populateKPIOrderStatZeroValue(List<String> storeNo,
			String maxTransmissionDate) throws PhotoOmniException {
		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();
		KPIDAO kpidaoImpl = factory.getKpidao();
		List<Date> dateRange = getDaterange(maxTransmissionDate);
		List<Map<String, Object>> existingOrderData = kpidaoImpl
				.getUntransmittedOrderData(
						maxTransmissionDate,
						getFormattedDate(getYesterdayDate(),
								PhotoOmniConstants.DATE_FORMAT_TWO));
		Map<String, Map<String, List<String>>> modifiedExistingOrderData = modifiedExistingOrderData(existingOrderData);
		for (String store : storeNo) {
			for (Date date : dateRange) {
				if (!modifiedExistingOrderData.containsKey(store)) {
					try {
						generateDefaultData(finalData, store, date, false);
					} catch (PhotoOmniException e) {
						LOGGER.error(
								"Erroe while prepare defualt data for order stats",
								e);
					}
				} else {
					if (modifiedExistingOrderData.containsKey(store)) {
						Map<String, List<String>> val = modifiedExistingOrderData
								.get(store);
						if (!val.containsKey(getFormattedDate(date,
								PhotoOmniConstants.DATE_FORMAT_TWO))) {
							generateDefaultData(finalData, store, date, false);
						} else {
							List<String> availableStats = val
									.get(getFormattedDate(date,
											PhotoOmniConstants.DATE_FORMAT_TWO));
							for (Map.Entry<String, Map<String, Object>> stat : getValidPairdStat()
									.entrySet()) {
								if (!availableStats.contains(stat.getKey())
										&& "ORDER".equals(stat.getValue().get(
												"ORDER_TYPE"))) {
									Map<String, Object> newMap = new HashMap<>();
									newMap.put(
											PhotoOmniBatchConstants.STORE_NO,
											store);
									newMap.put(PhotoOmniBatchConstants.STAT_ID,
											stat.getKey());
									newMap.put(
											PhotoOmniBatchConstants.SAMPLE_VALUE,
											0.00);
									newMap.put(
											PhotoOmniBatchConstants.SAMPLE_SIZE,
											0.00);
									newMap.put(
											PhotoOmniBatchConstants.BUSSINESS_DATE,
											getFormattedDate(
													date,
													PhotoOmniConstants.DATE_FORMAT_EIGHT));
									finalData.add(newMap);
								}

							}
						}
					}
				}
			}
		}
		try {
			populateKPITranscations(finalData,
					PhotoOmniBatchConstants.OM_KPI_ORDER_TRANSACTION, true);
		} catch (PhotoOmniException e) {
			LOGGER.error("Erroe while prepare defualt data for order stats", e);
		}
	}

	private void generateDefaultData(List<Map<String, Object>> finalData,
			String store, Date date, boolean orderTypeFlag)
			throws PhotoOmniException {
		for (Map.Entry<String, Map<String, Object>> val : getValidPairdStat()
				.entrySet()) {
			long transmitCD = CommonUtil.bigDecimalToLong(val.getValue().get(
					"TRANSMIT_ZERO_CD"));
			if ((transmitCD == 1 && "ORDER".equals(val.getValue().get(
					"ORDER_TYPE")))
					&& (!orderTypeFlag)) {
				generateZeroValueForStats(finalData, store, val.getKey(), date);
			}
			if (transmitCD == 1
					&& "PM".equals(val.getValue().get("ORDER_TYPE"))
					&& orderTypeFlag) {
				generateZeroValueForStats(finalData, store, val.getKey(), date);
			}

		}
	}

	private void generateZeroValueForStats(List<Map<String, Object>> finalData,
			String store, String statId, Date date) {
		Map<String, Object> newMap = new HashMap<>();
		newMap.put(PhotoOmniBatchConstants.STORE_NO, store);
		newMap.put(PhotoOmniBatchConstants.STAT_ID, statId);
		newMap.put(PhotoOmniBatchConstants.SAMPLE_VALUE, 0.00);
		newMap.put(PhotoOmniBatchConstants.SAMPLE_SIZE, 0.00);
		newMap.put(PhotoOmniBatchConstants.BUSSINESS_DATE,
				getFormattedDate(date, PhotoOmniConstants.DATE_FORMAT_EIGHT));
		finalData.add(newMap);
	}

	private List<Date> getDaterange(String maxTransmissionDate) {
		List<Date> dates = new ArrayList<Date>();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		Calendar past = Calendar.getInstance();
		past.setTime(CommonUtil.convertStringToDate(maxTransmissionDate,
				PhotoOmniConstants.DATE_FORMAT_TWO));
		while (past.getTime().before(today.getTime())) {
			dates.add(past.getTime());
			past.add(Calendar.DATE, 1);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					"Preparing list of date range using maxTransmissionDate - {}",
					dates);
		}

		return dates;
	}

	private Map<String, Map<String, List<String>>> modifiedExistingOrderData(
			List<Map<String, Object>> existingOrderData) {
		Map<String, Map<String, List<String>>> data = new HashMap<String, Map<String, List<String>>>();
		for (Map<String, Object> map : existingOrderData) {
			String storeNo = CommonUtil.bigDecimalToLong(
					map.get("LOCATION_NBR")).toString();
			String date = CommonUtil.stringDateFormatChange(map.get("KPI_DATE")
					.toString(), PhotoOmniConstants.DATE_FORMAT_ELEVEN,
					PhotoOmniConstants.DATE_FORMAT_TWO);
			String statId = (String) map.get("KPI_STAT_ID");
			if (!data.containsKey(storeNo)) {
				data.put(storeNo, new HashMap<String, List<String>>());
				Map<String, List<String>> inside = data.get(storeNo);
				if (!inside.containsKey(date)) {
					inside.put(date, new ArrayList<String>());
					inside.get(date).add(statId);
				}
			} else {
				Map<String, List<String>> inside = data.get(storeNo);
				if (!inside.containsKey(date)) {
					inside.put(date, new ArrayList<String>());
					inside.get(date).add(statId);
				} else {
					inside.get(date).add(statId);
				}
			}
		}
		return data;
	}

	@Override
	public List<String> checkStoreClosed(List<String> storeNo, String noOfDays)
			throws PhotoOmniException {
		KPIDAO kpidao = factory.getKpidao();
		List<String> availableStoreList = new ArrayList<>();
		List<String> list = kpidao.checkStoreClosed(noOfDays);
		for (String string : storeNo) {
			for (String str : list) {
				if (string.equalsIgnoreCase(str)) {
					availableStoreList.add(string);
				}
			}
		}
		return availableStoreList;
	}
}