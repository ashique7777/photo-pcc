package com.walgreens.omni.utility;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.common.exception.PhotoOmniException;


public class SilverCanisterReportsQuery {

	private static final Logger logger = LoggerFactory
			.getLogger(SilverCanisterReportsQuery.class);

	public static String getSilverCanisterReportQueryOne(
			List<Integer> storeDataList, String sortColoumnTwo, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		StringBuilder inQuery = getInQueryforOracleOne(storeDataList);
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,OML.LOCATION_NBR AS LOCATION_NBR,OMSRH.SYS_LOCATION_ID AS SYS_LOCATION_ID,OMSRH.ROLLS_COUNT AS ROLLS_COUNT,");
		query.append("OMSRH.PRINTS_COUNT AS PRINTS_COUNT,OMSRH.SILVER_RECV_ROLLS AS SILVER_RECV_ROLLS,TRUNC(OMSRH.CANISTER_END_DTTM) AS LAST_CANISTER_CHANGE_DTTM,");
		query.append("OMSRH.SILVER_RECV_PRINTS AS SILVER_RECV_PRINTS,OMSRH.PRINTS_IN_SQ_INCH AS PRINTS_IN_SQ_INCH,");
		query.append("ROWNUM AS ROWNUMBER FROM OM_SILVER_RECOVERY_HEADER OMSRH FULL OUTER JOIN OM_LOCATION OML ON OMSRH.SYS_LOCATION_ID = OML.SYS_LOCATION_ID");
		query.append(" WHERE TRUNC(CANISTER_START_DTTM) >= TO_DATE(? ,'DD-MM-YY') AND");
		query.append(" TRUNC(CANISTER_END_DTTM) <= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND (");
		query.append(inQuery);
		query.append(") AND CANISTER_STATUS = ? ORDER BY ");
		query.append(sortColoumnTwo +" "+sortOrder);
		return query.toString();
	}

	public static String getSilverCanisterReportQueryTwo(
			List<Integer> storeDataList, String sortColoumnTwo, String sortOrder) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		StringBuilder inQuery = getInQueryforOracleOne(storeDataList);
		query.append("SELECT * FROM (SELECT T.*,ROWNUM AS RNK FROM(");
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,OML.LOCATION_NBR AS LOCATION_NBR,OMSRH.SYS_LOCATION_ID AS SYS_LOCATION_ID,OMSRH.ROLLS_COUNT AS ROLLS_COUNT,");
		query.append("OMSRH.PRINTS_COUNT AS PRINTS_COUNT,OMSRH.SILVER_RECV_ROLLS AS SILVER_RECV_ROLLS,TRUNC(OMSRH.CANISTER_END_DTTM) AS LAST_CANISTER_CHANGE_DTTM,");
		query.append("OMSRH.SILVER_RECV_PRINTS AS SILVER_RECV_PRINTS,OMSRH.PRINTS_IN_SQ_INCH AS PRINTS_IN_SQ_INCH,");
		query.append("ROWNUM AS ROWNUMBER FROM OM_SILVER_RECOVERY_HEADER OMSRH FULL OUTER JOIN OM_LOCATION OML ON OMSRH.SYS_LOCATION_ID = OML.SYS_LOCATION_ID");
		query.append(" WHERE TRUNC(CANISTER_START_DTTM) >= TO_DATE(? ,'DD-MM-YY') AND");
		query.append(" TRUNC(CANISTER_END_DTTM) <= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND (");
		query.append(inQuery);
		query.append(") AND CANISTER_STATUS = ? ORDER BY ");
		query.append(sortColoumnTwo +" "+sortOrder);
		query.append(")T )OMSILVERRECHEAD WHERE RNK BETWEEN ? AND ? ");
		return query.toString();
	}

	public static String getSilverCanisterReportQueryThree(
			List<Integer> storeDataList, String sortColoumnTwo, String sortOrder,String status) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		StringBuilder inQuery = getInQueryforOracleOne(storeDataList);
		query.append("SELECT * FROM (SELECT T.*,ROWNUM AS RNK FROM(");
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,OML.LOCATION_NBR AS LOCATION_NBR,OMSRH.SYS_LOCATION_ID AS SYS_LOCATION_ID,OMSRH.ROLLS_COUNT AS ROLLS_COUNT,");
		query.append("OMSRH.PRINTS_COUNT AS PRINTS_COUNT,OMSRH.SILVER_RECV_ROLLS AS SILVER_RECV_ROLLS,");
		if(status.equalsIgnoreCase(PhotoOmniConstants.PROC))
		 {
		  query.append("TRUNC(OMSRH.CANISTER_START_DTTM)-1 AS LAST_CANISTER_CHANGE_DTTM,");
		   }else {
		  query.append("TRUNC(OMSRH.CANISTER_END_DTTM) AS LAST_CANISTER_CHANGE_DTTM,");
		 }
		query.append("OMSRH.SILVER_RECV_PRINTS AS SILVER_RECV_PRINTS,OMSRH.PRINTS_IN_SQ_INCH AS PRINTS_IN_SQ_INCH,");
		query.append("ROWNUM AS ROWNUMBER FROM OM_SILVER_RECOVERY_HEADER OMSRH FULL OUTER JOIN OM_LOCATION OML ON OMSRH.SYS_LOCATION_ID = OML.SYS_LOCATION_ID");
		query.append(" WHERE (");
		query.append(inQuery);
		query.append(") AND CANISTER_STATUS = ? ORDER BY ");
		query.append(sortColoumnTwo +" "+sortOrder);
		query.append(")T )OMSILVERRECHEAD WHERE RNK BETWEEN ? AND ? ");
		return query.toString();
	}
	
	public static String getSilverCanisterReportsQueryFour(
			List<Integer> storeDataList, String sortColoumnTwo, String sortOrder,String status) throws PhotoOmniException {
		StringBuilder query = new StringBuilder();
		StringBuilder inQuery = getInQueryforOracleOne(storeDataList);
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,OML.LOCATION_NBR AS LOCATION_NBR,OMSRH.SYS_LOCATION_ID AS SYS_LOCATION_ID,OMSRH.ROLLS_COUNT AS ROLLS_COUNT,");
		query.append("OMSRH.PRINTS_COUNT AS PRINTS_COUNT,OMSRH.SILVER_RECV_ROLLS AS SILVER_RECV_ROLLS,");
		if(status.equalsIgnoreCase(PhotoOmniConstants.PROC))
		 {
		  query.append("TRUNC(OMSRH.CANISTER_START_DTTM)-1 AS LAST_CANISTER_CHANGE_DTTM,");
		   }else {
		  query.append("TRUNC(OMSRH.CANISTER_END_DTTM) AS LAST_CANISTER_CHANGE_DTTM,");
		 }
		query.append("OMSRH.SILVER_RECV_PRINTS AS SILVER_RECV_PRINTS,OMSRH.PRINTS_IN_SQ_INCH AS PRINTS_IN_SQ_INCH,");
		query.append("ROWNUM AS ROWNUMBER FROM OM_SILVER_RECOVERY_HEADER OMSRH FULL OUTER JOIN OM_LOCATION OML ON OMSRH.SYS_LOCATION_ID = OML.SYS_LOCATION_ID");
		query.append(" WHERE (");
		query.append(inQuery);
		query.append(") AND CANISTER_STATUS = ? ORDER BY ");
		query.append(sortColoumnTwo +" "+sortOrder);
		return query.toString();
	}

	public static String getLocationNoForDistrictQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE DISTRICT_NBR = ? AND ACTIVE_CD = 1 ");
		return query.toString();
	}

	public static String getLocationNoForRegionQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE REGION_NBR = ? AND ACTIVE_CD = 1 ");
		return query.toString();
	}

	public static String getLocationNoForDivisionQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE DIVISION_NBR = ? AND ACTIVE_CD = 1");
		return query.toString();
	}

	public static String getLocationNoForMarketQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE MARKET_NBR = ? AND ACTIVE_CD = 1");
		return query.toString();
	}

	public static String getSilverRecoveryHeaderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT TRUNC(CANISTER_START_DTTM) AS CANISTER_START_DTTM,");
		query.append("TRUNC(CANISTER_END_DTTM) AS CANISTER_END_DTTM,");
		query.append("CANISTER_STATUS AS CANISTER_STATUS,");
		query.append("SILVER_RECV_PRINTS AS SILVER_RECV_PRINTS,");
		query.append("SILVER_RECV_ROLLS AS SILVER_RECV_ROLLS,");
		query.append("SYS_LOCATION_ID AS SYS_LOCATION_ID,");
		query.append("PRINTS_IN_SQ_INCH AS PRINTS_IN_SQ_INCH,");
		query.append("ROLLS_COUNT AS ROLLS_COUNT,");
		query.append("PRINTS_COUNT AS PRINTS_COUNT,");
		query.append("SILVER_COMPANY AS SILVER_COMPANY,");
		query.append("CREATE_USER_ID AS CREATE_USER_ID,");
		query.append("CREATE_DTTM AS CREATE_DTTM,");
		query.append("UPDATE_USER_ID AS UPDATE_USER_ID,");
		query.append("UPDATE_DTTM AS UPDATE_DTTM");
		query.append(" FROM OM_SILVER_RECOVERY_HEADER");
		query.append(" WHERE SYS_LOCATION_ID = (SELECT OMLOC.SYS_LOCATION_ID FROM OM_LOCATION OMLOC WHERE OMLOC.LOCATION_NBR = ?) AND CANISTER_STATUS = ?");
		return query.toString();
	}

	public static String getUpdateSilverRecHeadQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_SILVER_RECOVERY_HEADER SET CANISTER_END_DTTM = ?,");
		query.append("CANISTER_STATUS = ? ,SILVER_RECV_ROLLS = ? ,SILVER_RECV_PRINTS = ? ,UPDATE_USER_ID = ? ,UPDATE_DTTM = SYSDATE,");
		query.append("ROLLS_COUNT = ? ,PRINTS_COUNT = ? ,PRINTS_IN_SQ_INCH = ? ,SILVER_COMPANY = ?");
		query.append(" WHERE SYS_LOCATION_ID = ? AND CANISTER_STATUS = ?");
		return query.toString();
	}

	public static String getInsertSilverRecHeadQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_SILVER_RECOVERY_HEADER (SYS_SILVER_RECOVERY_HEADER_ID,");
		query.append("SYS_LOCATION_ID,CANISTER_START_DTTM,CANISTER_END_DTTM,CANISTER_STATUS,");
		query.append("SILVER_COMPANY,ROLLS_COUNT,PRINTS_COUNT,PRINTS_IN_SQ_INCH,SILVER_RECV_ROLLS,");
		query.append("SILVER_RECV_PRINTS,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) VALUES");
		query.append(" (OM_SILVER_RECOVERY_HEADER_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}

	public static String getCanisterChangeQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_CANISTER_SERVICE (SYS_CANISTER_SERVICE_ID,SYS_LOCATION_ID,");
		query.append("CANISTER_START_DTTM,CANISTER_END_DTTM,SERVICE_CD,SERVICE_DESCRIPTION,");
		query.append("CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM)");
		query.append(" VALUES (OM_CANISTER_SERVICE_SEQ.NEXTVAL,?,?,?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}
	
	public static String getOmSilverRecvDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT NVL(SUM(SILVER_RECV_ROLLS), 0) AS SILVER_RECV_ROLLS,");
		query.append("NVL(SUM(SILVER_RECV_PRINTS),0) AS SILVER_RECV_PRINTS,");
		query.append("NVL(SUM(ROLLS_COUNT),0) AS ROLLS_COUNT,");
		query.append("NVL(SUM(PRINTS_COUNT),0) AS PRINTS_COUNT,");
		query.append("NVL(SUM(PRINTS_IN_SQ_INCH),0) AS PRINTS_IN_SQ_INCH");
		query.append(" FROM OM_SILVER_RECOVERY_DETAIL WHERE");
		query.append(" (TRUNC(SILVER_CALC_DTTM) BETWEEN TO_DATE(? ,'MM/DD/YY') AND TO_DATE(? ,'MM/DD/YY')) AND SYS_LOCATION_ID = ? ");
		return query.toString();
	}

	public static String getSilverRecvRollsOmSilverRecvDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT NVL(SUM(SILVER_RECV_ROLLS), 0) AS SILVER_RECV_ROLLS");
		query.append(" FROM OM_SILVER_RECOVERY_DETAIL WHERE");
		query.append(" SYS_LOCATION_ID = ? AND TRUNC(SILVER_CALC_DTTM) BETWEEN TO_DATE(? ,'MM/DD/YY') AND TO_DATE(? ,'MM/DD/YY')");
		return query.toString();
	}

	public static String getSilverRecvPrintsOmSilverRecvDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT NVL(SUM(SILVER_RECV_PRINTS),0) AS SILVER_RECV_PRINTS");
		query.append(" FROM OM_SILVER_RECOVERY_DETAIL WHERE");
		query.append(" SYS_LOCATION_ID = ? AND TRUNC(SILVER_CALC_DTTM) BETWEEN TO_DATE(? ,'MM/DD/YY') AND TO_DATE(? ,'MM/DD/YY')");
		return query.toString();
	}

	public static String getupdateSilverRecoveryCdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_ATTRIBUTE SET SILVER_RECOVERY_CD = ?");
		query.append(" WHERE TRUNC(ORDER_PLACED_DTTM) = TO_DATE(?,'MM/DD/YY')");
		return query.toString();
	}

	/**
	 * This method creates multiple in block for SqlQuery if storeDataList size
	 * is more than 1000.
	 * 
	 * @param storeDataList
	 *            contains store.
	 * @return inQuery.
	 * @throws PhotoOmniException
	 *             custom exception.
	 */

	private static StringBuilder getInQueryforOracleOne(
			List<Integer> storeDataList) throws PhotoOmniException {
		if (logger.isDebugEnabled()) {
			logger.debug(" Entering getInQueryforOracleOne method of SilverCanisterReportsQuery ");
		}
		StringBuilder inQuery = new StringBuilder();
		int listSize = storeDataList.size();
		int IN_QUERY_VALUE_SIZE = 1000;
		try {
			if (listSize > IN_QUERY_VALUE_SIZE) {
				int loop = (listSize) / IN_QUERY_VALUE_SIZE;
				int remainder = (listSize) % IN_QUERY_VALUE_SIZE;
				if (remainder > 0) {
					loop++;
				}
				int start = 0;
				int end = IN_QUERY_VALUE_SIZE;
				for (int i = 0; i < loop; i++) {
					inQuery.append(" OMSRH.SYS_LOCATION_ID IN (");
					for (int j = start; j < end; j++) {
						if (j == (listSize - 1)) {
							inQuery.append(storeDataList.get(listSize - 1)
									+ ",");
							break;
						} else {
							inQuery.append(storeDataList.get(j) + ",");
						}
					}
					int lastIndex = inQuery.lastIndexOf(",");
					inQuery.deleteCharAt(lastIndex);
					inQuery.append(" ) ");
					if (loop != (i + 1)) {
						inQuery.append(" OR ");
					}
					start = end;
					end = end + IN_QUERY_VALUE_SIZE;
				}

			} else {
				inQuery.append(" OMSRH.SYS_LOCATION_ID IN (");
				for (int i = 0; i < listSize; i++) {
					inQuery.append(storeDataList.get(i) + ",");
				}
				int lastIndex = inQuery.lastIndexOf(",");
				inQuery.deleteCharAt(lastIndex);
				inQuery.append(" ) ");

			}
		} catch (Exception e) {
			logger.error(" Error occoured at getInQueryforOracleOne method of SilverCanisterReportsQuery - "
					+ e.getMessage());
			throw new PhotoOmniException(e.getMessage());
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug(" Exiting getInQueryforOracleOne method of SilverCanisterReportsQuery ");
			}
		}
		return inQuery;
	}

	public static String getSilverCanisterStoreReport(String sortColoumnOne, String sortOrder) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM  (SELECT T.*,ROWNUM AS RNK FROM(");
		query.append("SELECT COUNT(*) OVER () AS TOTAL_ROWS,OSRH.SILVER_COMPANY AS SILVER_COMPANY,");
		query.append("OCS.SERVICE_DESCRIPTION AS SERVICE_DESCRIPTION,TRUNC(OSRH.CANISTER_END_DTTM)   AS CANISTER_END_DTTM,");
		query.append(" (SELECT ADDRESS_LINE_1 FROM OM_LOCATION OMLOC WHERE OMLOC.LOCATION_NBR = ?) AS LOCATION_ADDRESS,");
		query.append(" ROWNUM AS ROWNUMBER FROM OM_SILVER_RECOVERY_HEADER OSRH");
		query.append(" JOIN OM_CANISTER_SERVICE OCS ON OSRH.SYS_LOCATION_ID = OCS.SYS_LOCATION_ID");
		query.append(" WHERE OSRH.SYS_LOCATION_ID = (SELECT OMLOC.SYS_LOCATION_ID FROM OM_LOCATION OMLOC WHERE OMLOC.LOCATION_NBR = ?)");
		query.append(" AND TRUNC(OSRH.CANISTER_START_DTTM) >= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND TRUNC(OSRH.CANISTER_END_DTTM)  <= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND TRUNC(OCS.CANISTER_START_DTTM) >= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND TRUNC(OCS.CANISTER_END_DTTM) <= TO_DATE(? ,'DD-MM-YY')");
		query.append(" AND OSRH.CANISTER_STATUS = ?");
		query.append(" ORDER BY ");
		query.append(sortColoumnOne+" "+sortOrder);
		query.append(")T )TEMPSILVERTABLE WHERE RNK BETWEEN ? AND ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public static String getLocationNoForChainQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE ACTIVE_CD = 1");
		return query.toString();
	}
	
	/**
	 * This method find the SYS_USER_ID from OAM_ID.
	 * @return query
	 */
	public static StringBuilder getSysUserIdByOamId() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT SYS_USER_ID FROM ").append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES);
		query.append(" WHERE EMPLOYEE_ID = ? ");
		return query;
	}

	public static String getLocationNoForStoreQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_LOCATION_ID AS SYS_LOCATION_ID FROM OM_LOCATION WHERE LOCATION_NBR = ? AND ACTIVE_CD = 1");
		return query.toString();
	}

	
}
