/**
 * 
 */
package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * This class contains queries required for MBPM report
 * 
 * @author Cognizant
 * 
 */
public class  MbpmQuery {

	/**
	 * This query populates Order Details Data
	 * @return
	 */
	public static StringBuilder populateMBPMData() {

		StringBuilder query = new StringBuilder();
		query.append("Select SHOPCART.SYS_SHOPPING_CART_ID AS SYS_SHOPPING_CART_ID,");
		query.append(" NVL(SHOPCART.PM_STATUS_CD, ' ')           AS PM_STATUS,");
		query.append("SHOPCART.SYS_LOCATION_ID           AS OWNING_LOC_ID,");
		query.append("OMORD.SYS_ORDER_ID                 AS SYS_ORDER_ID,");
		query.append("OMORD.STATUS                       AS STATUS,");
		query.append(" OMORDATTR.LABELED_EMPLOYEE_ID       AS LABELED_EMPLOYEE_ID,");
		query.append("OMORD.COUPON_CD                    AS COUPON_CD,");
		query.append("OMORD.DISCOUNT_CARD_USED_CD        AS DISCOUNT_CARD_USED_CD,");
        query.append(" TO_CHAR(OMORD.ORDER_PLACED_DTTM,'YYYY-MM-DD HH:MI:SS ') AS ORDER_PLACED_DTTM,");
		query.append(" OMORD.Final_Price                  As Final_Price,");
		query.append(" OMORD.Sold_Amount                  As Sold_Amount,");
		query.append(" OMORDATTR.EXPENSE_CD               AS EXPENSE_IND,");
		query.append(" OMUSERATTR.EMPLOYEE_ID             AS EMPLOYEE_ID,");
		query.append(" OMUSERATTR.SYS_USER_ID    As empId, ");
		query.append(" Omuserattr.Pm_Eligible_Cd          As Pm_Eligible_Cd "); 
		query.append(" FROM ").append(PhotoOmniDBConstants.OM_SHOPPING_CART).append(" SHOPCART,");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE ).append(" OMORDATTR ,").append(PhotoOmniDBConstants.OM_ORDER).append(" OMORD ,").append( PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" OMUSERATTR ");
		query.append(" WHERE SHOPCART.SYS_SHOPPING_CART_ID = OMORDATTR.SYS_SHOPPING_CART_ID ");
		query.append(" AND SHOPCART.ORDER_PLACED_DTTM      = OMORDATTR.ORDER_PLACED_DTTM  ");
		query.append(" AND OMORDATTR.SYS_ORDER_ID          = OMORD.SYS_ORDER_ID   ");
		query.append(" AND OMORDATTR.ORDER_PLACED_DTTM     = OMORD.ORDER_PLACED_DTTM ");
		query.append(" AND OMORDATTR.LABELED_EMPLOYEE_ID   = OMUSERATTR.SYS_USER_ID ");
		query.append(" AND SHOPCART.SYS_SHOPPING_CART_ID   = ?  ");
		query.append(" AND SHOPCART.ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
		return query;
	}

	/**
	 * This query fetches the promotional details
	 * @return
	 */
	public static StringBuilder populatePromotionalMoneyData() {
		StringBuilder query = new StringBuilder();

		query.append("SELECT OPM.SYS_PM_ID AS PM_ID,");
		query.append(" OPM.TIER_TYPE,");
		query.append(" OPM.DISCOUNT_APPLICABLE_CD,");
		query.append(" OPM_DTL.MINIMUM_TIER,");
		query.append(" OPM_DTL.MAXIMUM_TIER,");
		query.append(" OPM_DTL.PM_AMOUNT ");
	    query.append(" FROM ");
	    query.append("(SELECT SYS_PM_ID,");
	    query.append(" TIER_TYPE,");
	    query.append(" DISCOUNT_APPLICABLE_CD ");
	    query.append(" FROM OM_PROMOTIONAL_MONEY ");
	    query.append(" WHERE TYPE               = 'MBPM' ");
	    query.append(" AND PM_DISTRIBUTION_TYPE = 'Chain' ");
	    query.append(" AND ACTIVE_CD            = 1 ");
	    query.append(" AND START_DTTM          <= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
	    query.append(" AND END_DTTM            >= TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
	    query.append(" AND ROWNUM               = 1 ");
	    query.append(") OPM, ");
	    query.append(" OM_PROMOTIONAL_MONEY_DTL OPM_DTL ");
	    query.append("WHERE OPM.SYS_PM_ID   = OPM_DTL.SYS_PM_ID ");
	    query.append("AND OPM_DTL.ACTIVE_CD = 1 ");

		return query;
	}
	
	public static String getOrderPMQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID AS SYS_ORDER_ID FROM OM_ORDER_PM ");
		query.append(" WHERE SYS_ORDER_ID =? AND SYS_PRODUCT_ID = 0 ");
		query.append(" AND ORDER_PLACED_DTTM = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS') ");
		return query.toString();
	}
	
	/**
	 * This query inserts new order details into OM_ORDER_PM table
	 * 
	 * @return
	 */
	public static StringBuilder insertOrderPM() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_ORDER_PM (SYS_ORDER_PM_ID,SYS_ORDER_ID,SYS_PRODUCT_ID,SYS_PM_ID,SYS_EMPLOYEE_ID,EARNED_AMOUNT,");
		query.append("POTENTIAL_AMOUNT,PENDING_AMOUNT,ORDER_PLACED_DTTM,ACTIVE_CD,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) VALUES (OM_ORDER_PM_SEQ.NEXTVAL,?,?,?,?,?,?,?,TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF'),?,?,SYSDATE,?,SYSDATE)");
		return query;
	}

	/**
	 * This query updates OM_ORDER_PM table
	 * 
	 * @return
	 */
	public static StringBuilder updateOrderPM() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_PM SET SYS_PM_ID = ?,EARNED_AMOUNT = ?,POTENTIAL_AMOUNT =?,");
		query.append(" PENDING_AMOUNT =? , UPDATE_DTTM = SYSDATE, ACTIVE_CD = ? WHERE SYS_ORDER_ID = ? ");
		query.append(" AND ORDER_PLACED_DTTM = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF') ");
		return query;
	}
	
	/**
	 * This query updates PM_STATUS column of OM_SHOPPING_CART table for a
	 * particular SYS_SHOPPING_CART_ID
	 * 
	 * @return
	 */
	public static String getUpdatePmStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_SHOPPING_CART SET PM_STATUS_CD = ?,UPDATE_DTTM = SYSDATE WHERE SYS_SHOPPING_CART_ID =? AND ORDER_PLACED_DTTM = TO_TIMESTAMP(?,'YYYY-MM-DD HH24:MI:SS.FF')");
		return query.toString();
	}
	
	public static String getOrderPmUpQry(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_PM");
		query.append(" SET EARNED_QTY=?, POTENTIAL_QTY=?,PENDING_QTY=?,EARNED_AMOUNT=?, POTENTIAL_AMOUNT=?,PENDING_AMOUNT=?,SYS_PM_ID=?,");
		query.append(" ACTIVE_CD=?, UPDATE_USER_ID=").append(PhotoOmniConstants.UPDATE_USER_ID).append(",UPDATE_DTTM=SYSDATE WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID =?");
		query.append("AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getOrderPmIsrtQry(){
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_ORDER_PM(EARNED_QTY,POTENTIAL_QTY,PENDING_QTY,EARNED_AMOUNT,POTENTIAL_AMOUNT,PENDING_AMOUNT, SYS_PM_ID,ACTIVE_CD,");
		query.append(" SYS_ORDER_ID,SYS_PRODUCT_ID,ORDER_PLACED_DTTM,SYS_EMPLOYEE_ID,CREATE_USER_ID,CREATE_DTTM)");
		query.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,").append(PhotoOmniConstants.CREATE_USER_ID).append(", SYSDATE)");
		return query.toString();
	
   }
	
	public static String getPMStatusQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(PhotoOmniDBConstants.OM_SHOPPING_CART);
		query.append(" SET PM_STATUS_CD=? ");
		query.append(" WHERE SYS_SHOPPING_CART_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getOrderPmDefaultQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_PM");
		query.append(" SET EARNED_QTY=0, POTENTIAL_QTY=0,PENDING_QTY=0,EARNED_AMOUNT=0, POTENTIAL_AMOUNT=0,PENDING_AMOUNT=0,SYS_PM_ID=0,");
		query.append(" ACTIVE_CD=0, UPDATE_USER_ID='SYSTEM',UPDATE_DTTM=SYSDATE WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID =0");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
}
