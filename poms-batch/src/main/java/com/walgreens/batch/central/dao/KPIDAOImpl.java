package com.walgreens.batch.central.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.walgreens.batch.central.constants.PhotoOmniBatchConstants;
import com.walgreens.batch.query.KPIReportQuery;
import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.exception.PhotoOmniException;
import com.walgreens.common.utility.CommonUtil;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class KPIDAOImpl implements KPIDAO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIDAOImpl.class);

	@Autowired
	@Qualifier(PhotoOmniConstants.OMNI_JDBC_TEMPLATE)
	private JdbcTemplate jdbcTemplate;

	@Override
	public void populateKPITranscations(
			final List<Map<String, Object>> finalData, String tableName,
			boolean kpiTransmissionFlag) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getKpiTransactionRec method of KPIDAOImpl.Class ");
		}
		final String sql = KPIReportQuery.getKpiTransactionSelectQuery(
				tableName, kpiTransmissionFlag);
		try {
			for (Map<String, Object> map : finalData) {
				List<Map<String, Object>> list = jdbcTemplate
						.queryForList(
								sql,
								(new Object[] {
										map.get(PhotoOmniBatchConstants.STAT_ID),
										map.get(PhotoOmniBatchConstants.BUSSINESS_DATE),
										map.get(PhotoOmniBatchConstants.STORE_NO) }));
				if (list.size() > 0) {
					map.put("transSysId",
							((kpiTransmissionFlag) ? list.get(0).get(
									"SYS_KPI_ORDER_TRANS_ID") : list.get(0)
									.get("SYS_KPI_POS_PM_TRANS_ID")));
					map.put(PhotoOmniBatchConstants.SAMPLE_VALUE,
							(CommonUtil.bigDecimalToDouble(map
									.get(PhotoOmniBatchConstants.SAMPLE_VALUE))));
					updateKPITransactionTable(map, tableName);
				} else {
					insertKPITransactionTable(map, tableName);
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Error while checking the same records in KPI order transaction table ---> ",
					e);
			throw new PhotoOmniException(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void insertKPITransactionTable(final Map<String, Object> map,
			String tableName) {
		final String sql = KPIReportQuery
				.getKpiTransactionInsertQuery(tableName);
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(
					final PreparedStatement pSstatement) throws SQLException,
					DataAccessException {
				pSstatement.setObject(1,
						map.get(PhotoOmniBatchConstants.STORE_NO));
				pSstatement.setString(2,
						(String) map.get(PhotoOmniBatchConstants.STAT_ID));
				pSstatement.setObject(3,
						map.get(PhotoOmniBatchConstants.SAMPLE_VALUE));
				pSstatement.setObject(4,
						map.get(PhotoOmniBatchConstants.SAMPLE_SIZE));
				pSstatement.setObject(5,
						map.get(PhotoOmniBatchConstants.BUSSINESS_DATE));
				pSstatement.execute();
				return null;
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateKPITransactionTable(final Map<String, Object> map,
			String tableName) {
		final String sql = KPIReportQuery
				.getKpiTransactionUpdateQuery(tableName);
		jdbcTemplate.execute(sql, new PreparedStatementCallback() {
			public Object doInPreparedStatement(
					final PreparedStatement pSstatement) throws SQLException,
					DataAccessException {
				pSstatement.setObject(1,
						map.get(PhotoOmniBatchConstants.SAMPLE_VALUE));
				pSstatement.setObject(2,
						map.get(PhotoOmniBatchConstants.SAMPLE_SIZE));
				pSstatement.setObject(3, map.get("transSysId"));
				pSstatement.executeUpdate();
				return null;
			}
		});
	}

	@Override
	public List<Map<String, Object>> validateStat(String stat)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getKpiStatStatus method of KPIDAOImpl.Class ");
		}
		List<Map<String, Object>> list;
		try {
			list = jdbcTemplate.queryForList(KPIReportQuery.getStatStatus(),
					new Object[] { stat });
		} catch (Exception e) {
			LOGGER.error("Error while updating KPI flag indicator ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public void updateKPIIndicator(String kpiCurrentDate, boolean deployMentType)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					" Entering updateKPIIndicator method of KPIDAOImpl.Class and the kpiCurrentDate is where deployMentType is - {},{}",
					kpiCurrentDate, deployMentType);
		}
		try {
			final Object[] currentDate = { kpiCurrentDate };
			if (deployMentType) {
				final String posTransactionSql = KPIReportQuery
						.updatePosTransactionStoreWise();
				jdbcTemplate.update(posTransactionSql, currentDate);
				final String orderPmSql = KPIReportQuery
						.updateOrderPmStoreWise();
				jdbcTemplate.update(orderPmSql, currentDate);
			} else {
				final String posTransactionSql = KPIReportQuery
						.updatePosTransaction();
				jdbcTemplate.update(posTransactionSql, currentDate);
				final String orderPmSql = KPIReportQuery.updateOrderPm();
				jdbcTemplate.update(orderPmSql);
			}
		} catch (Exception e) {
			LOGGER.error("Error while updating KPI flag indicator ---> ", e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public void updatePOSTransaction() throws PhotoOmniException {
		try {
			jdbcTemplate.update(KPIReportQuery.updatePosTansactionAfterStep());
		} catch (Exception e) {
			LOGGER.error("Error while updating POSTransaction ---> ", e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public void updateKPITransactionAfterTransmit(long jobExecutionId)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					" Entering updateKPITransactionAfterTransmit method of KPIDAOImpl.Class and the jobExecutionId is - {}",
					jobExecutionId);
		}
		try {
			final Object[] executionId = { jobExecutionId };
			final String orderTransactionSql = KPIReportQuery
					.updateKpiOrderTransaction();
			jdbcTemplate.update(orderTransactionSql, executionId);
			final String posPmTransactionSql = KPIReportQuery
					.updateKpiPosPmTransaction();
			jdbcTemplate.update(posPmTransactionSql, executionId);
		} catch (Exception e) {
			LOGGER.error(
					"Error while updating updateKPITransactionAfterTransmit ---> ",
					e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public String getMaxTransmissionDate() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering getMaxTransmissionDate method of KPIDAOImpl.Class");
		}
		String string;
		try {
			final String sql = KPIReportQuery.getMaxTransmissionDate();
			string = jdbcTemplate.queryForObject(sql, String.class);
		} catch (Exception e) {
			LOGGER.error("Error while getting getMaxTransmissionDate ---> ", e);
			throw new PhotoOmniException(e);
		}
		return string;
	}

	@Override
	public void updateOrderPMTransaction(String kpiCurrentDate)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering updateOrderPMTransaction method of KPIDAOImpl.Class");
		}
		try {
			jdbcTemplate.update(
					KPIReportQuery.updateOrderPMTansactionAfterStep(),
					new Object[] { kpiCurrentDate });
		} catch (Exception e) {
			LOGGER.error("Error while updating updateOrderPMTransaction ---> ",
					e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public void updateOnArchive(String kpiTransmissionDate)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(
					" Entering updateOnArchive method of KPIDAOImpl.Class and kpiTransmissionDate is {}",
					kpiTransmissionDate);
		}
		try {
			final Object[] transmissionDate = { kpiTransmissionDate };
			final String kpiOrderTransactionSql = KPIReportQuery
					.updateKpiOrderTransactionOnArchive();
			jdbcTemplate.update(kpiOrderTransactionSql, transmissionDate);
			final String kpiPosPmTransactionSql = KPIReportQuery
					.updateKpiPosPmTransactionOnArchive();
			jdbcTemplate.update(kpiPosPmTransactionSql, transmissionDate);
		} catch (Exception e) {
			LOGGER.error("Error while updating updateOnArchive ---> ", e);
			throw new PhotoOmniException(e);
		}
	}

	@Override
	public List<String> getAllStoreNos() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getAllStoreNos method of KPIDAOImpl.Class");
		}
		List<String> list;
		try {
			final String sql = KPIReportQuery.getAllStoreNosSql();
			list = jdbcTemplate.queryForList(sql, String.class);
		} catch (Exception e) {
			LOGGER.error("Error while getting getAllStoreNos ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public List<String> getStoreNos() throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getStoreNos method of KPIDAOImpl.Class");
		}
		List<String> list;
		try {
			final String sql = KPIReportQuery.getStoreNosSql();
			list = jdbcTemplate.queryForList(sql, String.class);
		} catch (Exception e) {
			LOGGER.error("Error while getting getStoreNos ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getLatestUntransmittedPmData(
			String yesterdayDate, String currentDate) throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getLatestUntransmittedData method of KPIDAOImpl.Class");
		}
		List<Map<String, Object>> list;
		try {
			final String sql = KPIReportQuery.getLatestUntransmittedPmDataSql();
			list = jdbcTemplate.queryForList(sql, new Object[] { yesterdayDate,
					currentDate });
		} catch (Exception e) {
			LOGGER.error(
					"Error while getting getLatestUntransmittedData ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getAllValidStat()
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getAllValidStat method of KPIDAOImpl.Class");
		}
		List<Map<String, Object>> list;
		try {
			final String sql = KPIReportQuery.getAllStat();
			list = jdbcTemplate.queryForList(sql);
		} catch (Exception e) {
			LOGGER.error("Error while getting getAllValidStat ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getUntransmittedOrderData(
			String maxTransmissionDate, String yesterdayDate)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering getUntransmittedOrderData method of KPIDAOImpl.Class");
		}
		List<Map<String, Object>> list;
		try {
			final String sql = KPIReportQuery.getUntransmittedOrderDataSql();
			list = jdbcTemplate.queryForList(sql, new Object[] {
					maxTransmissionDate, yesterdayDate });
		} catch (Exception e) {
			LOGGER.error("Error while getting getUntransmittedOrderData ---> ",
					e);
			throw new PhotoOmniException(e);
		}
		return list;
	}

	@Override
	public List<String> checkStoreClosed(String noOfDays)
			throws PhotoOmniException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering checkStoreClosed method of KPIDAOImpl.Class");
		}
		List<String> list = null;
		try {
			final String sql = KPIReportQuery.checkStoreClosedSql();
			list = jdbcTemplate.queryForList(sql, String.class,
					new Object[] { noOfDays });
		} catch (Exception e) {
			LOGGER.error("Error while checking checkStoreClosed ---> ", e);
			throw new PhotoOmniException(e);
		}
		return list;
	}
}