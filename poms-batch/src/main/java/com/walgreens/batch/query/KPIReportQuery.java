package com.walgreens.batch.query;

public class KPIReportQuery {

	/*
	 * Select SQL to get status and send zero status for all the stats from the
	 * OM_KPI_STAT table.
	 */
	public static String getStatStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ACTIVE_CD,TRANSMIT_ZERO_CD FROM OM_KPI_STAT WHERE KPI_STAT_ID = ? ");
		return sb.toString();
	}

	/*
	 * Update SQL to set KPI_INDICATOR_FLAG to 'I' in the OM_POS_TRANSACTION
	 * table.
	 */
	public static String updatePosTransaction() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_POS_TRANSACTION SET OM_POS_TRANSACTION.KPI_INDICATOR_FLAG='I' ");
		sb.append(" WHERE NVL(OM_POS_TRANSACTION.KPI_INDICATOR_FLAG ,' ') = ' ' AND ");
		sb.append(" OM_POS_TRANSACTION.BUSINESS_DATE < TO_DATE(?, 'MM-dd-yyyy hh24:mi:ss') ");
		return sb.toString();
	}

	/*
	 * Update SQL to set KPI_INDICATOR_FLAG to 'I' in the OM_ORDER_PM table.
	 */
	public static String updateOrderPm() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE OM_ORDER_PM \n");
		sb.append("SET OM_ORDER_PM.KPI_INDICATOR_FLAG = 'I' \n");
		sb.append("WHERE ORDER_PLACED_DTTM            > SYSDATE-195 \n");
		sb.append("AND (KPI_INDICATOR_FLAG           IS NULL \n");
		sb.append("OR KPI_INDICATOR_FLAG              = ' ') \n");
		sb.append("AND ACTIVE_CD                      = 1 \n");
		sb.append("AND EARNED_AMOUNT                  > 0");
		return sb.toString();
	}

	/*
	 * This update SQL should be executed once SALES STATS calculation
	 * completes. Update SQL to set KPI_INDICATOR_FLAG to 'S' in the
	 * OM_POS_TRANSACTION table.
	 */
	public static String updatePosTansactionAfterStep() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Update OM_POS_TRANSACTION Set KPI_INDICATOR_FLAG = 'S' WHERE KPI_INDICATOR_FLAG = 'I' ");
		return sb.toString();
	}

	/*
	 * Select SQL to get the max transmission date for order's from
	 * OM_KPI_ORDER_TRANSACTION transaction table.
	 */
	public static String getMaxTransmissionDate() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT MAX(KPI_TRANSMISSION_DTTM) FROM OM_KPI_ORDER_TRANSACTION ");
		sb.append(" WHERE KPI_TRANSMISSION_CD = 'S' ");
		return sb.toString();
	}

	/*
	 * Select SQL for OM_KPI_ORDER_TRANSACTION and OM_KPI_POS_PM_TRANSACTION
	 * transaction table to check the records exists or not.
	 */
	public static String getKpiTransactionSelectQuery(String tableName,
			boolean kpiTransmissionFlag) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ");
		sb.append(tableName.equalsIgnoreCase("OM_KPI_POS_PM_TRANSACTION") ? "SYS_KPI_POS_PM_TRANS_ID"
				: "SYS_KPI_ORDER_TRANS_ID");
		sb.append(", KPI_SAMPLE_VALUE ");
		sb.append(" FROM ");
		sb.append(tableName);
		sb.append(" WHERE KPI_STAT_ID = ? AND KPI_DATE = TO_DATE(?,'MM-dd-yyyy') AND LOCATION_NBR = ? ");
		if (kpiTransmissionFlag) {
			sb.append(" AND KPI_TRANSMISSION_CD IN ('Y','R','T') ");
		}
		return sb.toString();
	}

	/*
	 * Update SQL for OM_KPI_ORDER_TRANSACTION and OM_KPI_POS_PM_TRANSACTION
	 * transaction table to update the KPI_SAMPLE_VALUE value.
	 */
	public static String getKpiTransactionUpdateQuery(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE ");
		sb.append(tableName);
		sb.append(" SET KPI_SAMPLE_VALUE = ? ,  KPI_TRANSMISSION_CD = 'Y' , KPI_SAMPLE_SIZE = ?, KPI_TRANSMISSION_DTTM = null WHERE ");
		sb.append(tableName.equalsIgnoreCase("OM_KPI_POS_PM_TRANSACTION") ? "SYS_KPI_POS_PM_TRANS_ID"
				: "SYS_KPI_ORDER_TRANS_ID");
		sb.append(" = ? ");
		return sb.toString();
	}

	/*
	 * Insert SQL for OM_KPI_ORDER_TRANSACTION and OM_KPI_POS_PM_TRANSACTION
	 * transaction table to insert records.
	 */
	public static String getKpiTransactionInsertQuery(String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append(" INSERT INTO ");
		sb.append(tableName);
		sb.append(tableName.equalsIgnoreCase("OM_KPI_POS_PM_TRANSACTION") ? " (SYS_KPI_POS_PM_TRANS_ID"
				: "(SYS_KPI_ORDER_TRANS_ID");
		sb.append(",LOCATION_NBR,KPI_STAT_ID,KPI_SAMPLE_VALUE,KPI_SAMPLE_SIZE,KPI_DATE, ");
		sb.append(" KPI_TRANSMISSION_DTTM,KPI_TRANSMISSION_CD,CREATE_USER_ID,CREATE_DTTM) ");
		sb.append(" VALUES (");
		sb.append(tableName.equalsIgnoreCase("OM_KPI_POS_PM_TRANSACTION") ? "OM_KPI_POS_PM_TRANSACTION_SEQ.NEXTVAL"
				: "OM_KPI_ORDER_TRANSACTION_SEQ.NEXTVAL");
		sb.append(",?, ?, ?, ?, TO_DATE(?,'MM-dd-yyyy'),null,'Y','System',SYSDATE) ");
		return sb.toString();
	}

	/*
	 * Update SQL for OM_KPI_ORDER_TRANSACTION table once the batch job is
	 * completed and the feed file is transmitted.
	 */
	public static String updateKpiOrderTransaction() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_KPI_ORDER_TRANSACTION SET KPI_TRANSMISSION_CD = 'T', ");
		sb.append(" KPI_TRANSMISSION_DTTM = (SELECT START_TIME ");
		sb.append(" FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = ?) ");
		sb.append(" WHERE KPI_TRANSMISSION_CD IN ('T','Y','R') ");
		return sb.toString();
	}

	/*
	 * Update SQL for OM_KPI_POS_PM_TRANSACTION table once the batch job is
	 * completed and the feed file is transmitted.
	 */
	public static String updateKpiPosPmTransaction() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_KPI_POS_PM_TRANSACTION SET KPI_TRANSMISSION_CD = 'T', ");
		sb.append(" KPI_TRANSMISSION_DTTM = (SELECT START_TIME ");
		sb.append(" FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = ?) ");
		sb.append(" WHERE KPI_TRANSMISSION_CD IN ('T','Y','R') ");
		return sb.toString();
	}

	/*
	 * This update SQL should be execute once PM STATS calculation completes.
	 * Update SQL to set KPI_INDICATOR_FLAG to 'S' in the OM_ORDER_PM table.
	 */
	public static String updateOrderPMTansactionAfterStep() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_ORDER_PM SET KPI_INDICATOR_FLAG = 'S' WHERE KPI_INDICATOR_FLAG = 'I' ");
		sb.append(" AND ORDER_PLACED_DTTM > (TO_DATE(?, 'MM-dd-yyyy hh24:mi:ss') -195) ");
		return sb.toString();
	}

	/*
	 * This update SQL should be execute once the archive process completes.
	 * Update SQL to set KPI_TRANSMISSION_CD to 'S' in the
	 * OM_KPI_ORDER_TRANSACTION table.
	 */
	public static String updateKpiOrderTransactionOnArchive() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_KPI_ORDER_TRANSACTION SET KPI_TRANSMISSION_CD = 'S' ");
		sb.append(" WHERE KPI_TRANSMISSION_CD= 'T' AND TO_CHAR(KPI_TRANSMISSION_DTTM, 'dd-Mon-yyyy')  = ? ");
		return sb.toString();
	}

	/*
	 * This update SQL should be execute once the archive process completes.
	 * Update SQL to set KPI_TRANSMISSION_CD to 'S' in the
	 * OM_KPI_POS_PM_TRANSACTION table.
	 */
	public static String updateKpiPosPmTransactionOnArchive() {
		StringBuilder sb = new StringBuilder();
		sb.append(" UPDATE OM_KPI_POS_PM_TRANSACTION SET KPI_TRANSMISSION_CD = 'S' ");
		sb.append(" WHERE KPI_TRANSMISSION_CD= 'T' AND TO_CHAR(KPI_TRANSMISSION_DTTM, 'dd-Mon-yyyy')  = ? ");
		return sb.toString();
	}

	/*
	 * Update SQL to set KPI_INDICATOR_FLAG to 'I' in the OM_POS_TRANSACTION
	 * table. Based upon deployment type for specific stores.
	 */
	public static String updatePosTransactionStoreWise() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE OM_POS_TRANSACTION \n");
		sb.append("SET OM_POS_TRANSACTION.KPI_INDICATOR_FLAG ='I' \n");
		sb.append("WHERE EXISTS \n");
		sb.append("  (SELECT 1 \n");
		sb.append("  FROM OM_LOCATION, \n");
		sb.append("    OM_STORE_PARAMETER, \n");
		sb.append("    OM_PARAMETER_MASTER \n");
		sb.append("  WHERE OM_POS_TRANSACTION.SYS_LOCATION_ID                       = OM_LOCATION.SYS_LOCATION_ID \n");
		sb.append("  AND OM_STORE_PARAMETER.SYS_PARAMETER_ID                        = OM_PARAMETER_MASTER.SYS_PARAMETER_ID \n");
		sb.append("  AND OM_LOCATION.LOCATION_NBR                                   = OM_STORE_PARAMETER.LOCATION_NBR \n");
		sb.append("  AND TO_DATE(OM_STORE_PARAMETER.PARAMETER_VALUE, 'YYYYMMDD') <= sysdate \n");
		sb.append("  AND OM_PARAMETER_MASTER.PARAMETER_NAME                         = 'KPI' \n");
		sb.append("  ) \n");
		sb.append("AND NVL(OM_POS_TRANSACTION.KPI_INDICATOR_FLAG ,' ') = ' ' \n");
		sb.append("AND OM_POS_TRANSACTION.BUSINESS_DATE                < TO_DATE(?, 'MM-dd-yyyy hh24:mi:ss')");
		return sb.toString();
	}

	/*
	 * Update SQL to set KPI_INDICATOR_FLAG to 'I' in the OM_ORDER_PM table.
	 * Based upon deployment type for specific stores.
	 */
	public static String updateOrderPmStoreWise() {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE OM_ORDER_PM \n");
		sb.append("SET OM_ORDER_PM.KPI_INDICATOR_FLAG = 'I' \n");
		sb.append("WHERE EXISTS \n");
		sb.append("  (SELECT 1 \n");
		sb.append("  FROM OM_ORDER, \n");
		sb.append("    OM_LOCATION, \n");
		sb.append("    OM_STORE_PARAMETER, \n");
		sb.append("    OM_PARAMETER_MASTER \n");
		sb.append("  WHERE OM_ORDER.SYS_ORDER_ID                                  = OM_ORDER_PM.SYS_ORDER_ID \n");
		sb.append("  AND OM_ORDER.SYS_OWNING_LOC_ID                               = OM_LOCATION.SYS_LOCATION_ID \n");
		sb.append("  AND OM_LOCATION.LOCATION_NBR                                 = OM_STORE_PARAMETER.LOCATION_NBR \n");
		sb.append("  AND OM_STORE_PARAMETER.SYS_PARAMETER_ID                      = OM_PARAMETER_MASTER.SYS_PARAMETER_ID \n");
		sb.append("  AND TO_DATE(OM_STORE_PARAMETER.PARAMETER_VALUE, 'YYYYMMDD') <= sysdate \n");
		sb.append("  AND OM_PARAMETER_MASTER.PARAMETER_NAME                       = 'KPI' \n");
		sb.append("  AND OM_ORDER.ORDER_PLACED_DTTM                               > (TO_DATE(?, 'MM-dd-yyyy hh24:mi:ss') -195) \n");
		sb.append("  ) \n");
		sb.append("AND order_placed_dttm         > sysdate-195 \n");
		sb.append("AND (KPI_INDICATOR_FLAG      IS NULL \n");
		sb.append("OR KPI_INDICATOR_FLAG         = ' ') \n");
		sb.append("AND OM_ORDER_PM.ACTIVE_CD     = 1 \n");
		sb.append("AND OM_ORDER_PM.EARNED_AMOUNT > 0");
		return sb.toString();
	}

	/*
	 * Select SQL query to get all the unique store number from table
	 * OM_LOCATION.
	 */
	public static String getAllStoreNosSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT DISTINCT(LOCATION_NBR) FROM OM_LOCATION ");
		return sb.toString();
	}

	/*
	 * Select SQL query to get all the unique store number from table
	 * OM_STORE_PARAMETER based upon deployment type.
	 */
	public static String getStoreNosSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT(OM_STORE_PARAMETER.LOCATION_NBR) \n");
		sb.append("FROM OM_STORE_PARAMETER \n");
		sb.append("INNER JOIN OM_PARAMETER_MASTER \n");
		sb.append("ON OM_STORE_PARAMETER.SYS_PARAMETER_ID = OM_PARAMETER_MASTER.SYS_PARAMETER_ID \n");
		sb.append("WHERE TO_DATE(OM_STORE_PARAMETER.PARAMETER_VALUE, 'YYYYMMDD') <= sysdate \n");
		sb.append("AND OM_PARAMETER_MASTER.PARAMETER_NAME = 'KPI'");
		return sb.toString();
	}

	public static String getLatestUntransmittedPmDataSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT LOCATION_NBR, \n");
		sb.append("  KPI_STAT_ID, \n");
		sb.append("  KPI_DATE \n");
		sb.append("FROM OM_KPI_POS_PM_TRANSACTION \n");
		sb.append("WHERE KPI_TRANSMISSION_CD != 'S' \n");
		sb.append("AND KPI_DATE              >= To_DATE(?,'dd-MM-yyyy') \n");
		sb.append("AND KPI_DATE               < To_DATE(?,'MMddyyyy hh24:mi:ss')");
		return sb.toString();
	}

	public static String getAllStat() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT KPI_STAT_ID, \n");
		sb.append("  ACTIVE_CD, \n");
		sb.append("  TRANSMIT_ZERO_CD, \n");
		sb.append("  CASE \n");
		sb.append("    WHEN KPI_STAT_ID IN ('PHALTOS', 'PHALCSDT', 'PHALCCDT', 'PHALDPMS', 'PHALDPMP') \n");
		sb.append("    THEN 'PM' \n");
		sb.append("    WHEN KPI_STAT_ID IN ('PHALPTOH','PHALCTOH','PHALPNOT','PHALCNOT','PHALPINT','PHALCINT','PHALPPTA','PHALPPTB','PHALPPTC') \n");
		sb.append("    THEN 'ORDER' \n");
		sb.append("  END \"ORDER_TYPE\" \n");
		sb.append("FROM OM_KPI_STAT WHERE ACTIVE_CD = 1 ");
		return sb.toString();
	}

	public static String getUntransmittedOrderDataSql() {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT LOCATION_NBR,KPI_STAT_ID,KPI_DATE FROM OM_KPI_ORDER_TRANSACTION ");
		sb.append(" WHERE KPI_TRANSMISSION_CD != 'S' AND KPI_DATE >= To_DATE(?,'dd-MM-yyyy') ");
		sb.append(" AND KPI_DATE <= To_DATE(?,'dd-MM-yyyy') ");
		return sb.toString();
	}
	
	public static String checkStoreClosedSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT(LOCATION_NBR) \n");
		sb.append("FROM OM_LOCATION \n");
		sb.append("WHERE STORE_TYPE        = 'Regular' \n");
		sb.append("AND (VALID_MWB_STORE_CD = 1 \n");
		sb.append("OR (VALID_MWB_STORE_CD  = 0 \n");
		sb.append("AND (DATE_CLOSED        > SYSDATE -? \n");
		sb.append("OR DATE_CLOSED         IS NULL)))");
		return sb.toString();
	}
}