package com.walgreens.oms.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * @author CTS Class used to hold all the Queries
 * 
 */

public class PosOrderQuery {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PosOrderQuery.class);
	/**
	 * @return
	 * 
	 */
	public static String getOrderNoQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID,ORDER_PLACED_DTTM,ORDER_NBR,STATUS FROM (SELECT T.*,ROWNUM AS RNK FROM(");
		query.append("(SELECT OM_ORDER.SYS_ORDER_ID,OM_ORDER.ORDER_PLACED_DTTM,OM_ORDER.ORDER_NBR,OM_ORDER.STATUS, ");
		query.append("CASE OM_ORDER.STATUS ");
		query.append("WHEN 'DONE' ");
		query.append("THEN 1 ");
		query.append("WHEN 'PROC' ");
		query.append("THEN 2 ");
		query.append("WHEN 'SOLD' ");
		query.append("THEN 3 ");
		query.append("WHEN 'CANCEL' ");
		query.append("THEN 4 ");
		query.append("WHEN 'COMPLETE' ");
		query.append("THEN 5 ");
		query.append("ELSE 6 ");
		query.append("END    AS SORTORDER, ");
		query.append("ROWNUM AS ROWNUMBER ");
		query.append("FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(",");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE OM_ORDER.SYS_ORDER_ID           =  OM_ORDER_ATTRIBUTE.SYS_ORDER_ID");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM = OM_ORDER_ATTRIBUTE.ORDER_PLACED_DTTM");
		query.append(" AND OM_ORDER.SYS_OWNING_LOC_ID = (SELECT SYS_LOCATION_ID FROM OM_LOCATION WHERE LOCATION_NBR = ? )");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM >  (SYSDATE-195)");
		query.append(" AND OM_ORDER_ATTRIBUTE.ENVELOPE_NUMBER = ?");
		query.append(" ORDER BY SORTORDER,OM_ORDER.ORDER_PLACED_DTTM DESC ))T ORDER BY SORTORDER) ");
		query.append(" WHERE RNK = 1 ");
		return query.toString();
	}
	/**
	 * @return
	 */
	public static String getInsertPosDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" (SYS_STORE_POS_ID,SYS_ORDER_ID,SYS_LOCATION_ID,TRANSACTON_DTTM,TRANSACTION_TYPE_CD,");
		query.append("SOLD_AMT,BUSINESS_DATE,POS_SEQUENCE_NBR,REGISTER_NBR,ENVELOPE_NBR,RETURNED_QTY,");
		query.append("POS_LEDGER_NBR,EMPLOYEE_ID,DISCOUNT_USED_CD,PROCESSING_CD,CREATE_USER_ID,");
		query.append("CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) VALUES (?,?,?,TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'),?,");
		query.append("?,TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,?,?,?,?, SYSDATE ");
		query.append(",?, SYSDATE)");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getInsertOrderHistoryQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_ORDER_HISTORY);
		query.append(" (ORDER_PLACED_DTTM,SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_ACTION_DTTM,ORDER_STATUS,");
		query.append("ORDER_ACTION_NOTES,SYS_EXCEPTION_ID,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM)");
		query.append(" VALUES (TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'),OM_ORDER_HISTORY_SEQ.NEXTVAL,?,?,");
		query.append("TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?, SYSDATE ,?,");
		query.append(" SYSDATE )");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getOmOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT OMOR.SYS_ORDER_ID,OMOR.SOLD_AMOUNT,OMOR.FINAL_PRICE,OMOR.STATUS,OMOR.ORDER_COMPLETED_DTTM,");
		query.append("OMOR.ORDER_PLACED_DTTM,OMOR.SYS_OWNING_LOC_ID,OMORA.PROCESSING_TYPE_CD,OMORA.COST_CALCULATION_STATUS_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" OMOR JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" OMORA ON OMOR.SYS_ORDER_ID= OMORA.SYS_ORDER_ID AND OMOR.ORDER_PLACED_DTTM = OMORA.ORDER_PLACED_DTTM");
		query.append(" WHERE OMOR.SYS_ORDER_ID = ? AND OMOR.ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
		}
	
	/**
	 * @return
	 */
	public static String getupdateSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET SOLD_AMOUNT = ? ,  UPDATE_DTTM= SYSDATE WHERE SYS_ORDER_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getOrderLineDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_LINE_ID,FINAL_PRICE,QUANTITY,SYS_PRODUCT_ID,COST FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getlicenceContentDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_OL_LC_ID,FINAL_LC_PRICE FROM ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT);
		query.append(" WHERE SYS_ORDER_LINE_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getorderLineTemplateBeansListQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_OL_TEMPLATE_ID,SYS_ORDER_LINE_ID,SYS_TEMPLATE_ID,TEMPLATE_QTY,TEMPLATE_SOLD_AMT FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getcAmtPaidToLicemceContentQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT);
		query.append(" SET LC_AMOUNT_PAID = ? ,  UPDATE_DTTM= SYSDATE WHERE SYS_OL_LC_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getupdatecTemplateSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE);
		query.append(" SET TEMPLATE_SOLD_AMT = ? ,  UPDATE_DTTM= SYSDATE  WHERE SYS_OL_TEMPLATE_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public static String getupdateLineSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" SET LINE_SOLD_AMOUNT = ? ,  UPDATE_DTTM= SYSDATE  WHERE SYS_ORDER_LINE_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getCalenderIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_CALENDAR_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_CALENDER);
		query.append(" WHERE DAY_DATE = TO_DATE(? ,'YYYY-MM-DD')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePosTranCalIdRtnQtyQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PRINTS_RETURNED_QTY = ?,LAST_POS_TRAN_CAL_ID = ?  WHERE SYS_ORDER_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePrintsReturnedQty() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PRINTS_RETURNED_QTY = ? ,  UPDATE_DTTM= SYSDATE  WHERE SYS_ORDER_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOrderStatusSoldDttm() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET STATUS = ? , ORDER_SOLD_DTTM = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOmOrderAttrPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PM_STATUS ='',UPDATE_DTTM= SYSDATE WHERE SYS_ORDER_ID = ?");
		query.append("  AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOmShoppingCartPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_SHOPPING_CART);
		query.append(" SET PM_STATUS_CD ='' ,  UPDATE_DTTM= SYSDATE WHERE SYS_LOCATION_ID = ?");
		query.append(" AND (SYS_SHOPPING_CART_ID = (SELECT SYS_SHOPPING_CART_ID FROM OM_ORDER_ATTRIBUTE WHERE SYS_ORDER_ID = ?))");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getDecodeQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DECODE FROM ");
		query.append(PhotoOmniDBConstants.OM_CODE_DECODE);
		query.append(" WHERE TRIM(CODE_ID) = ?");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getSysLocationIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_LOCATION_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" WHERE LOCATION_NBR = ?");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getSysExceptionIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_ORD_EXCEPTION_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
		query.append(" WHERE SYS_ORDER_ID = ?");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOrderSoldDttmQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET ORDER_SOLD_DTTM = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') ,  UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getOmOrderAttrPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PM_STATUS FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getOmShoppingCartPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PM_STATUS_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_SHOPPING_CART);
		query.append(" INNER JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" USING(SYS_SHOPPING_CART_ID) WHERE SYS_ORDER_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}


	/**
	 * @return
	 */
	public String getUpdatePosDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" SET SYS_ORDER_ID = ? , UPDATE_DTTM = SYSDATE WHERE ENVELOPE_NBR = ? ");
		query.append("AND TRANSACTON_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getSysStorePosIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_STORE_POS_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE SYS_ORDER_ID = ? AND ENVELOPE_NBR = ? ");
		query.append(" AND TRANSACTON_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getProcessingTypeCdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PROCESSING_TYPE_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ? AND ORDER_PLACED_DTTM   = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getSysProductId() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_PRODUCT_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM   = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/** Changes for JIRA#624 starts here **/
	// Modified this query (getProductDetailQuery) by adding PRODUCT_NBR
	/**
	 * @return
	 */
	public String getProductDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_PRODUCT_ID,UPC,WIC,PRODUCT_NBR FROM ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT);
		query.append(" WHERE SYS_PRODUCT_ID = ? ");
		LOGGER.debug(query.toString());return query.toString();
	}
	/** Changes for JIRA#624 ends here **/

	/**
	 * @return
	 */
	public String getCostCalStatusCdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COST_CALCULATION_STATUS_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ? AND ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getOmOrderLineCostQuantityQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COST,QUANTITY FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ? AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getItemMovementRepQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT MSS_REPORT_BY_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT_ATTRIBUTE);
		query.append(" WHERE SYS_PRODUCT_ID = ? ");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getSysPosTransByProductIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_POS_TRANS_BY_PRODUCT_ID FROM ");
		query.append("OM_POS_TRANS_BY_WIC_UPC");
		query.append(" WHERE SYS_STORE_POS_ID = ? AND MSS_TRANSFER_CD <> 'Y'");
		LOGGER.debug(query.toString());return query.toString();
	}

	/**
	 * @return
	 */
	public String getDeleteMssRecordQuery() {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM OM_POS_TRANS_BY_WIC_UPC WHERE SYS_POS_TRANS_BY_PRODUCT_ID = ? AND MSS_TRANSFER_CD <> 'Y'");
		LOGGER.debug(query.toString());return query.toString();
	}

	/** Changes for JIRA#624 starts here **/
	// Modified this query (getInserOmPosTransByWicUpc) by adding PRODUCT_NBR
    /**
	 * @return
	 */
	public String getInserOmPosTransByWicUpc() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(" OM_POS_TRANS_BY_WIC_UPC ");
		query.append("(SYS_POS_TRANS_BY_PRODUCT_ID,BUSINESS_DATE,COST_DOLLARS,DEFAULT_COST_CD,ENVELOPE_NBR,LOCATION_NBR,");
		query.append("MSS_TRANSFER_CD,MSS_TRANSMISSION_DATE,ORDER_NBR,SALES_DOLLARS,SALES_UNITS,SYS_STORE_POS_ID,");
		query.append("UPC_NBR,WIC_NBR,TRANSACTION_TYPE_CD,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM, PRODUCT_NBR) ");
		query.append("VALUES (OM_POS_TRANS_BY_WIC_UPC_SEQ.NEXTVAL,TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,?,TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS'),");
		query.append("?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE,?)");
		LOGGER.debug(query.toString());return query.toString();
	}
	/** Changes for JIRA#624 ends here **/
	
	

	public String getOmOrderAttrQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COST_CALCULATION_STATUS_CD,PROCESSING_TYPE_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ? AND ORDER_PLACED_DTTM = TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		LOGGER.debug(query.toString());return query.toString();
	}

	public String getSalesDollersFromMssQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SALES_DOLLARS FROM OM_POS_TRANS_BY_WIC_UPC ");
		query.append(" WHERE ORDER_NBR = ? AND ENVELOPE_NBR = ? AND LOCATION_NBR = ?");
		LOGGER.debug(query.toString());return query.toString();
	}
	/**
	 * @return
	 */
	public String getShoppingCartIDquery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_SHOPPING_CART_ID AS SHOPPINGCARTID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ? AND ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getQueryForSysPosTransByProductId() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_POS_TRANS_BY_PRODUCT_ID FROM OM_POS_TRANS_BY_WIC_UPC ");
		query.append("WHERE SYS_STORE_POS_ID = ? AND MSS_TRANSFER_CD <> 'Y'");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String checkSysStorePosIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_STORE_POS_ID,TRANSACTION_TYPE_CD FROM OM_POS_TRANSACTION WHERE ");
		query.append("(SYS_ORDER_ID = ? AND ENVELOPE_NBR = ? AND BUSINESS_DATE = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		query.append("AND SOLD_AMT = ? AND POS_SEQUENCE_NBR = ? AND REGISTER_NBR = ? AND RETURNED_QTY = ? AND POS_LEDGER_NBR = ? AND TRANSACTON_DTTM =TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') AND EMPLOYEE_ID = ? AND TRANSACTION_TYPE_CD = ?)");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String checkNullSysStorePosIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_STORE_POS_ID,TRANSACTION_TYPE_CD FROM OM_POS_TRANSACTION WHERE ");
		query.append("(SYS_ORDER_ID IS NULL AND ENVELOPE_NBR = ? AND BUSINESS_DATE = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		query.append("AND SOLD_AMT = ? AND POS_SEQUENCE_NBR = ? AND REGISTER_NBR = ? AND RETURNED_QTY = ? AND POS_LEDGER_NBR = ? AND TRANSACTON_DTTM =TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') AND EMPLOYEE_ID = ? AND TRANSACTION_TYPE_CD = ?)");
		return query.toString();
	}

	public String getUpdatePosDetailQueryOne() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" SET SYS_STORE_POS_ID = ?,SYS_ORDER_ID = ?,SYS_LOCATION_ID = ?,TRANSACTON_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'),TRANSACTION_TYPE_CD =?,");
		query.append("SOLD_AMT =?,BUSINESS_DATE = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'),POS_SEQUENCE_NBR =?,REGISTER_NBR =?,ENVELOPE_NBR =?,RETURNED_QTY =?,");
		query.append("POS_LEDGER_NBR =?,EMPLOYEE_ID = ?,DISCOUNT_USED_CD =?,PROCESSING_CD =?,");
		query.append("UPDATE_USER_ID = ?,UPDATE_DTTM = SYSDATE ");
		query.append(" WHERE (SYS_ORDER_ID = ? AND ENVELOPE_NBR = ? AND BUSINESS_DATE = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'))");
		LOGGER.debug(query.toString());return query.toString();
	}

	public String getSysStorePosIdTempQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT OM_POS_TRANSACTION_SEQ.NEXTVAL FROM DUAL");
		LOGGER.debug(query.toString());return query.toString();
	}

	public String getSysPosTransByProductIdListQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_POS_TRANS_BY_PRODUCT_ID FROM OM_POS_TRANS_BY_WIC_UPC WHERE SYS_STORE_POS_ID = ? AND ENVELOPE_NBR = ?");
		LOGGER.debug(query.toString());return query.toString();
	}

	public String getUpdateOmPosTransByWicUpcQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE");
		query.append(" OM_POS_TRANS_BY_WIC_UPC SET ");
		query.append("COST_DOLLARS = ?,DEFAULT_COST_CD = ?,ENVELOPE_NBR = ?,LOCATION_NBR = ?,");
		query.append("MSS_TRANSFER_CD = ?,ORDER_NBR = ?,SALES_DOLLARS = ?,SALES_UNITS = ?,SYS_STORE_POS_ID = ?,");
		query.append("UPC_NBR = ?,WIC_NBR = ?,TRANSACTION_TYPE_CD = ?,UPDATE_USER_ID = ?,UPDATE_DTTM = SYSDATE ");
		query.append("WHERE SYS_POS_TRANS_BY_PRODUCT_ID = ?");
		LOGGER.debug(query.toString());return query.toString();
	}

	public String getSysProductIdTemp() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_PRODUCT_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT_ATTRIBUTE);
		query.append(" WHERE SYS_PRODUCT_ID = ? ");
		LOGGER.debug(query.toString());return query.toString();
	}


}
