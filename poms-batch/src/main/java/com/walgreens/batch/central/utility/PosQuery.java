package com.walgreens.batch.central.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * @author CTS
 * 
 */
public class PosQuery {

	/**
	 * @return
	 */
	public String getPosTranDatilsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_STORE_POS_ID,LOCATION_ID,ORDER_ID,TRANSACTON_DTTM,SYS_TRANSACTION_TYPE_ID,");
		query.append("SOLD_AMT,BUSINESS_DATE,POS_SEQUENCE_NBR,REGISTER_NUMBER,ENVELOPE_NUMBER,PROCESSING_STATUS,");
		query.append("RETURNED_QTY,POS_LEDGER_NUMBER,EMPLOYEE_ID,DISCOUNT_USED_IND,TO_CHAR(ORDER_PLACED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_PLACED_DTTM,");
		query.append("CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE OM_POS_TRANSACTION_FLAG = 'Y' AND ROWNUMBER >= ? AND ROWNUMBER <= ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String posOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID,SOLD_AMOUNT,FINAL_PRICE,STATUS,TO_CHAR(ORDER_COMPLETED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_COMPLETED_DTTM ,ORIGINAL_ORDER_PRICE,DISCOUNT_CARD_USED_CD,");
		query.append(" TO_CHAR(ORDER_PLACED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_PLACED_DTTM,SYS_OWNING_LOC_ID,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getBlankOrderIdPosDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM");
		query.append("(SELECT SYS_STORE_POS_ID AS SYS_STORE_POS_ID,");
		query.append("SYS_LOCATION_ID AS SYS_LOCATION_ID,");
		query.append("SYS_ORDER_ID AS SYS_ORDER_ID,");
		query.append("TO_CHAR(TRANSACTON_DTTM, 'YYYY-MM-DD HH24:MI:SS') AS TRANSACTON_DTTM,");
		query.append("TRANSACTION_TYPE_CD AS TRANSACTION_TYPE_CD,");
		query.append("SOLD_AMT AS SOLD_AMT,");
		query.append("TO_CHAR(BUSINESS_DATE, 'YYYY-MM-DD HH24:MI:SS') AS BUSINESS_DATE,");
		query.append("POS_SEQUENCE_NBR AS POS_SEQUENCE_NBR,");
		query.append("REGISTER_NBR AS REGISTER_NBR,");
		query.append("ENVELOPE_NBR AS ENVELOPE_NBR,");
		query.append("PROCESSING_CD AS PROCESSING_CD,");
		query.append("RETURNED_QTY AS RETURNED_QTY,");
		query.append("POS_LEDGER_NBR AS POS_LEDGER_NBR,");
		query.append("EMPLOYEE_ID AS EMPLOYEE_ID,");
		query.append("DISCOUNT_USED_CD AS DISCOUNT_USED_CD,");
		query.append("CREATE_USER_ID AS CREATE_USER_ID,");
		query.append("CREATE_DTTM AS CREATE_DTTM,");
		query.append("UPDATE_USER_ID AS UPDATE_USER_ID,");
		query.append("UPDATE_DTTM AS UPDATE_DTTM,");
		query.append("ROWNUM AS ROWNUMBER ");
		query.append("FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE SYS_ORDER_ID IS null)TEMPPOSTRAN WHERE TEMPPOSTRAN.ROWNUMBER >= ? AND TEMPPOSTRAN.ROWNUMBER <= ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ORDER_NO FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePosDetailQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" SET SYS_ORDER_ID = ? , UPDATE_DTTM = SYSDATE WHERE ENVELOPE_NBR = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePosExceptionQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
		query.append(" (SYS_ORD_EXCEPTION_ID, ORDER_PLACED_DTTM,");
		query.append("SYS_ORDER_ID,CREATE_DTTM,UPDATE_DTTM,SYS_EXCEPTION_TYPE_ID,CREATE_USER_ID,UPDATE_USER_ID,");
		query.append("NOTES,SYS_ORDER_LINE_ID,PREVIOUS_ENVELOPE_NO,PREVIOUS_ORDER_STATUS,STATUS,WASTE_CALC_CD,WASTE_COST,WASTE_QTY) ");
		query.append("VALUES (OM_ORDER_EXCEPTION_SEQ.NEXTVAL,TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),?,SYSDATE,SYSDATE,?,?,?,?,?,?,?,?,?,?,?) ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getOmOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID,SOLD_AMOUNT,FINAL_PRICE,STATUS, TO_CHAR(ORDER_COMPLETED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_COMPLETED_DTTM ,ORIGINAL_ORDER_PRICE,DISCOUNT_CARD_USED_CD,");
		query.append(" TO_CHAR(ORDER_PLACED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_PLACED_DTTM,SYS_OWNING_LOC_ID,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	
	/**
	 * @return
	 */
	public String getupdateSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET SOLD_AMOUNT = ? , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getOrderLineDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_LINE_ID,QUANTITY,FINAL_PRICE,TO_CHAR(ORDER_PLACED_DTTM, 'YYYY-MM-DD HH24:MI:SS') ORDER_PLACED_DTTM,SYS_ORDER_ID,SYS_PRODUCT_ID,SYS_MACHINE_INSTANCE_ID,");
		query.append("SYS_EQUIPMENT_INSTANCE_ID,UNIT_PRICE,ORIGINAL_QTY,ORIGINAL_LINE_PRICE,DISCOUNT_AMT,LOYALTY_PRICE,LOYALTY_DISCOUNT_AMT,");
		query.append("COST,CREATE_DTTM,UPDATE_DTTM,UPDATE_USER_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getlicenceContentDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_OL_LC_ID,FINAL_LC_PRICE,SYS_ORDER_LINE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT);
		query.append(" WHERE SYS_ORDER_LINE_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getorderLineTemplateBeansListQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_OL_TEMPLATE_ID,SYS_ORDER_LINE_ID,SYS_TEMPLATE_ID,TEMPLATE_QTY,TEMPLATE_SOLD_AMT FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getcAmtPaidToLicemceContentQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT);
		query.append(" SET LC_AMOUNT_PAID = ? , UPDATE_DTTM = SYSDATE WHERE SYS_OL_LC_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getupdatecTemplateSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE);
		query.append(" SET TEMPLATE_SOLD_AMT = ? , UPDATE_DTTM = SYSDATE WHERE SYS_OL_TEMPLATE_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getupdateLineSoldAmtQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" SET LINE_SOLD_AMOUNT = ? , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_LINE_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */

	public String getInsertOrderHistoryQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_ORDER_HISTORY);
		query.append(" (ORDER_PLACED_DTTM,SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_ACTION_DTTM,ORDER_STATUS,");
		query.append("ORDER_ACTION_NOTES,SYS_EXCEPTION_ID,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM)");
		query.append(" VALUES (TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),OM_ORDER_HISTORY_SEQ.NEXTVAL,?,?,");
		query.append("TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}
	
	public String getInsertOrderHistoryQueryOne() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ");
		query.append(PhotoOmniDBConstants.OM_ORDER_HISTORY);
		query.append(" (ORDER_PLACED_DTTM,SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_ACTION_DTTM,ORDER_STATUS,");
		query.append("ORDER_ACTION_NOTES,SYS_EXCEPTION_ID,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM)");
		query.append(" VALUES (TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),OM_ORDER_HISTORY_SEQ.NEXTVAL,?,?,");
		query.append("TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getOrderIdFromOrderAttribute() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID FROM ");
		query.append("(SELECT OM_ORDER.SYS_ORDER_ID, ");
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
		//Prod - bug fix starts
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM        =  OM_ORDER_ATTRIBUTE.ORDER_PLACED_DTTM");
		query.append(" AND OM_ORDER.SYS_OWNING_LOC_ID = ?");
		query.append(" AND OM_ORDER.ORDER_PLACED_DTTM >  TO_DATE(? ,'YYYY-MM-DD HH24:MI:SS')");
		//Prod - bug fix ends
		query.append(" AND OM_ORDER_ATTRIBUTE.ENVELOPE_NUMBER = ?");
		query.append(" ORDER BY SORTORDER,OM_ORDER.ORDER_PLACED_DTTM DESC )");
		query.append(" WHERE ROWNUMBER = 1 ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getPosTransactionTypeQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT TRANSACTION_TYPE  FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION_TYPE);
		query.append(" WHERE SYS_TRANSACTION_TYPE_ID = ? ");
		return query.toString();
	}

	public String getExceptionIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORD_EXCEPTION_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getorderLineIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_ORDER_LINE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getExcepTypeIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_EXCEPTION_TYPE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE);
		query.append(" WHERE EXCEPTION_TYPE =?");
		return query.toString();
	}
	

	/**
	 * @return
	 */
	public String getOrderLineCostQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT COST FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getCalenderIdWithEarlierDayQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_CALENDAR_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_CALENDER);
		query.append(" WHERE trunc(DAY_DATE) = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getOrderDetailsWithCalenderIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query.append("(SELECT SYS_ORDER_ID AS SYS_ORDER_ID,");
		query.append("TO_CHAR(ORDER_PLACED_DTTM, 'YYYY-MM-DD HH24:MI:SS') AS ORDER_PLACED_DTTM,");
		query.append("ORIGINAL_ORDER_PRICE AS ORIGINAL_ORDER_PRICE,");
		query.append("TO_CHAR(ORDER_COMPLETED_DTTM, 'YYYY-MM-DD HH24:MI:SS') AS ORDER_COMPLETED_DTTM,");
		query.append("STATUS AS STATUS,");
		query.append("CREATE_USER_ID AS CREATE_USER_ID,");
		query.append("CREATE_DTTM AS CREATE_DTTM,");
		query.append("UPDATE_USER_ID AS UPDATE_USER_ID,");
		query.append("UPDATE_DTTM AS UPDATE_DTTM,");
		query.append("SOLD_AMOUNT AS SOLD_AMOUNT,");
		query.append("SYS_OWNING_LOC_ID,");
		query.append("FINAL_PRICE AS FINAL_PRICE,");
		query.append("DISCOUNT_CARD_USED_CD AS DISCOUNT_CARD_USED_CD,");
		query.append("ROWNUM AS ROWNUMBER");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE SYS_CALENDAR_ID = ? ORDER BY SYS_ORDER_ID)TEMPORD ");
		query.append("WHERE TEMPORD.ROWNUMBER >= ? AND TEMPORD.ROWNUMBER <= ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getPrintsReturnedQty() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT PRINTS_RETURNED_QTY FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getWasteQtyQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT WASTED_QTY FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_LINE_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdWasteQtyQuery() {
		StringBuffer query = new StringBuffer();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE);
		query.append(" SET WASTED_QTY = ? , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_LINE_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getPosTranDttmQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT TO_CHAR(TRANSACTON_DTTM, 'YYYY-MM-DD HH24:MI:SS') AS TRANSACTON_DTTM FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE SYS_ORDER_ID =?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getPosEmployeeIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT EMPLOYEE_ID AS EMPLOYEE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE SYS_ORDER_ID =?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOrderStatus() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET STATUS = ? , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePosTranCalIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET LAST_POS_TRAN_CAL_ID = ?  , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdatePrintsReturnedQty() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PRINTS_RETURNED_QTY = ?  , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getCalenderIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_CALENDAR_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_CALENDER);
		query.append(" WHERE TO_CHAR(DAY_DATE, 'mm/dd/yyyy') = TO_CHAR(TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), 'mm/dd/yyyy') ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getUpdateOmOrderAttrPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PM_STATUS =' ' , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	
	/***
	 * @return
	 */
	public String getUpdateOmShoppingCartPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_SHOPPING_CART);
		query.append(" SET PM_STATUS_CD =' ' , UPDATE_DTTM = SYSDATE WHERE SYS_LOCATION_ID = ?");
		query.append(" AND (SYS_SHOPPING_CART_ID = (SELECT SYS_SHOPPING_CART_ID FROM OM_ORDER_ATTRIBUTE WHERE SYS_ORDER_ID = ?))");
		query.append(" AND ORDER_PLACED_DTTM = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') ");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getPosTranTypeQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT TRANSACTION_TYPE_CD AS TRANSACTION_TYPE_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_POS_TRANSACTION);
		query.append(" WHERE SYS_ORDER_ID =?");
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getDecodeQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT DECODE FROM ");
		query.append(PhotoOmniDBConstants.OM_CODE_DECODE);
		query.append(" WHERE TRIM(CODE_ID) = ? AND CODE_TYPE='OrderActions'");
		return query.toString();
	}
	
	/**
	 * @return
	 */
	public String getSysExceptionIdQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_ORD_EXCEPTION_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}
	
	/**
	 * @return
	 */
	public String getUpdateOrderSoldDttmQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" SET ORDER_SOLD_DTTM = TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') , UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}
	
	/**
	 * @return
	 */
	public String getOmOrderAttrPmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PM_STATUS FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" WHERE SYS_ORDER_ID = ?");
		return query.toString();
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
		return query.toString();
	}

	/**
	 * @return
	 */
	public String getExcepTypeIdForRefusedQuery() {
		StringBuffer query = new StringBuffer();
		query.append("SELECT SYS_EXCEPTION_TYPE_ID FROM ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE);
		query.append(" WHERE EXCEPTION_TYPE =? AND REASON =?");
		return query.toString();
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
}
