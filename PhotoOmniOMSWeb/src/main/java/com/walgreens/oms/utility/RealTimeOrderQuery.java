/**
 * 
 */
package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniDBConstants;
import com.walgreens.oms.json.bean.LateEnvelopeReportReqBean;


/**
 * @author CTS
 *
 */
public class RealTimeOrderQuery {
	
	
	/**
	 * Prepare insert query for order table
	 * @return String 
	 * 	Order Insert Query 
	 */
	public static StringBuilder insertQueryOrderRef() {
		StringBuilder query = new StringBuilder();
		
			query.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER);
			query.append("(SYS_ORDER_ID, ORDER_NBR, SRC_VENDOR_ORDER_NBR, SRC_KIOSK_ORDER_NBR, ORDER_TYPE, SYS_CUSTOMER_ID, ORDER_ORIGIN_TYPE,");
			query.append( " SYS_SRC_VENDOR_ID, SYS_FULFILLMENT_VENDOR_ID, TOTAL_ORDER_DISCOUNT, FINAL_PRICE, ORIGINAL_ORDER_PRICE, STATUS, PROMISE_SHIP_DTTM, ORDER_PLACED_DTTM,");
			query.append( "PROMISE_DELIVERY_DTTM, COUPON_CD, LOYALTY_PRICE, LOYALTY_DISCOUNT_AMOUNT, DISCOUNT_CARD_USED_CD,SYS_OWNING_LOC_ID,SYS_FULFILLMENT_LOC_ID,");
			query.append(" SYS_CALENDAR_ID,  CURRENCY_CODE_ID, CUSTOMER_LAST_NAME, CUSTOMER_FIRST_NAME, UPPER_CUSTOMER_LAST_NAME, UPPER_CUSTOMER_FIRST_NAME, PHONE_NBR, FULL_PHONE_NBR, EMAIL_ADDR, ");
			query.append( "ORDER_DESCRIPTION, OWNING_LOC_TZ_OFFSET, SRC_VENDOR_TZ_OFFSET, AREA_CODE, ORDER_COMPLETED_DTTM, ORDER_TAX,SOLD_AMOUNT,LOYALTY_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM)");
			query.append( " VALUES(?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, ?,?, SYSDATE, ?, SYSDATE)");
			
			
		return query;
		 
	}
	
	/**
	 * Query to update order completion status & time from store to central database
	 * 
	 * @return
	 * 	Order UPDATE Query
	 */
	public static StringBuilder completeOrderQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER);
		stb.append(" SET TOTAL_ORDER_DISCOUNT=?, FINAL_PRICE=?, ORIGINAL_ORDER_PRICE=?, STATUS=?, ");
		stb.append( " COUPON_CD=?, LOYALTY_PRICE=?, LOYALTY_DISCOUNT_AMOUNT=?, DISCOUNT_CARD_USED_CD=?,SYS_FULFILLMENT_LOC_ID=?,");
		stb.append("UPDATE_USER_ID=?,");
		stb.append("UPDATE_DTTM= SYSDATE, ORDER_COMPLETED_DTTM=?, SRC_VENDOR_TZ_OFFSET=? WHERE SYS_ORDER_ID=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
	}
	
	/**
	 * Query to update order completion status & time from store to central database
	 * 
	 * @return
	 * 	Order UPDATE Query
	 */
	public static StringBuilder completeOrderQryWithOutStatus() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER);
		stb.append(" SET TOTAL_ORDER_DISCOUNT=?, FINAL_PRICE=?, ORIGINAL_ORDER_PRICE=?, ");
		stb.append( " COUPON_CD=?, LOYALTY_PRICE=?, LOYALTY_DISCOUNT_AMOUNT=?, DISCOUNT_CARD_USED_CD=?,SYS_FULFILLMENT_LOC_ID=?,");
		stb.append("UPDATE_USER_ID=?,");
		stb.append("UPDATE_DTTM= SYSDATE, ORDER_COMPLETED_DTTM=?, SRC_VENDOR_TZ_OFFSET=? WHERE SYS_ORDER_ID=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
	}
	
	/**
	 * Query to insert into order attribute table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryOrderAttrRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		stb.append(" (SYS_ORDER_ATTRIBUTE_ID, SYS_ORDER_ID, ENVELOPE_NUMBER,WAS_REF_ID,LABELED_EMPLOYEE_ID, SERVICE_CATEGORY_CODE, PROCESSING_TYPE_CD,");
		stb.append(" ORDER_PLACED_DTTM, SYS_SHOPPING_CART_ID,EXPENSE_CD,COST_CALCULATION_STATUS_CD, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM,");
		stb.append("CHANNEL_CD,CHANNEL_DETAIL_CD,EXCEPTION_CD,PRINTS_RETURNED_QTY,FUNCTIONALITY_CD,SHIP_TO_METHOD_CD, DEVELOPING_TYPE_CD, ");
		stb.append("CARRIER_CD, REPLENISHMENT_STATUS_CD,COST,FULFILLMENT_VENDOR_COST,SHR_ORDER_CD, NPT_CD,CALL_STATUS_CD, LATE_ENVELOPE_CD, SILVER_RECOVERY_CD)");
		stb.append("VALUES(OM_ORDER_ATTRIBUTE_SEQ.NEXTVAL, ?, ?, ?, ?, ?,  ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)") ;
		return stb;
		
	}

	/** Query to update order attribute table
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateOrderAttrQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" SET ENVELOPE_NUMBER=?, WAS_REF_ID=?, LABELED_EMPLOYEE_ID=?, SERVICE_CATEGORY_CODE=?, ");
		stb.append( " EXPENSE_CD=?, COST_CALCULATION_STATUS_CD=?, LATE_ENVELOPE_CD=?, PROCESSING_TYPE_CD=?, COMPLETED_EMPLOYEE_ID=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE, EXCEPTION_CD=? WHERE SYS_ORDER_ID =? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
		
	}
	
	/**
	 * 	Query to insert into Order line table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryOrderItemRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE);
		stb.append("(SYS_ORDER_LINE_ID, SYS_ORDER_ID, SYS_PRODUCT_ID, QUANTITY, ORIGINAL_QTY, ORIGINAL_LINE_PRICE,");
		stb.append(" DISCOUNT_AMT, FINAL_PRICE, LOYALTY_PRICE, LOYALTY_DISCOUNT_AMT, SYS_EQUIPMENT_INSTANCE_ID,");
		stb.append("SYS_MACHINE_INSTANCE_ID,ORDER_PLACED_DTTM, UNIT_PRICE, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM,");
		stb.append("COST,FULFILLMENT_VENDOR_COST,LINE_SOLD_AMOUNT) VALUES(?, ?, ?,?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE,?,?,?)") ;
		return stb;
		
	}
	
	/**
	 * 	Query to update Order line table
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQueryOrderItemRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE);
		stb.append(" SET QUANTITY=?, ORIGINAL_LINE_PRICE=?, DISCOUNT_AMT=?, FINAL_PRICE=?,LOYALTY_PRICE=?, ");
		stb.append(" LOYALTY_DISCOUNT_AMT=?, SYS_EQUIPMENT_INSTANCE_ID=?,SYS_MACHINE_INSTANCE_ID=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE ");
		stb.append(" WHERE SYS_ORDER_LINE_ID=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
		
	}
	
	
	/**
	 * 	Query to insert into Order line attribute table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQryOrderItemAttrRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE);
		stb.append("(SYS_OL_ATTRIBUTE_ID, SYS_ORDER_LINE_ID, ROLLS_SETS_QTY, PANOROMIC_PRINT_QTY,PRINTED_QTY, INDEX_SHEET_QTY, INPUT_IMAGE_QTY, WASTED_QTY, WASTE_PRINT_COST,");
		stb.append(" ORDER_PLACED_DTTM, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM,PROMOTIONAL_MONEY, SKIPPABLE_PRODUCT_CD)");
		stb.append( " VALUES(OM_ORDER_LINE_ATTRIBUTE_SEQ.NEXTVAL, ?, ?,?,?, ?, ?, ?, ?,?,?,SYSDATE,?,SYSDATE, ?,?)");
		return stb;
		
	}
	/**
	 * 	Query to Update Order line attribute table
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQryOrderItemAttrRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE).append(" SET ROLLS_SETS_QTY=?, PANOROMIC_PRINT_QTY=?,");
		stb.append("PRINTED_QTY=?, INDEX_SHEET_QTY=?, INPUT_IMAGE_QTY=?, WASTED_QTY=?, WASTE_PRINT_COST=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE ");
		stb.append (" WHERE SYS_ORDER_LINE_ID=? AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
		
	}
	/**
	 * 	Query to insert into order line template table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryTemplate() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE).append("(SYS_OL_TEMPLATE_ID, SYS_ORDER_LINE_ID,SYS_TEMPLATE_ID, TEMPLATE_QTY, ORDER_PLACED_DTTM, CREATE_USER_ID,");
		stb.append("CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM, TEMPLATE_SOLD_AMT) ");
		stb.append( "VALUES(OM_ORDER_LINE_TEMPLATE_SEQ.NEXTVAL,?,?,?, ?, ?, SYSDATE, ?, SYSDATE, ?)");
		return stb;
	}
	
	/**
	 * 	Query to Update order line template table
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQueryTemplate() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE_TEMPLATE).append(" SET TEMPLATE_QTY=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE  ");
		stb.append( " WHERE SYS_ORDER_LINE_ID=? AND SYS_TEMPLATE_ID=? AND ORDER_PLACED_DTTM=To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
	}
	/**
	 * 	Query to insert into Order line license content table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryLicenseContentRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT).append("(SYS_OL_LC_ID, SYS_ORDER_LINE_ID, VENDOR_LC_ID, VENDOR_LC_QTY, DOWNLOADED_DTTM, DOWNLOADED_CD, VENDOR_LC_DESC");
		stb.append( ", LC_AMOUNT_PAID, ORIGINAL_LC_PRICE, FINAL_LC_PRICE, ORDER_PLACED_DTTM, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM)");
		stb.append( " VALUES(OM_OL_LICENSE_CONTENT_SEQ.NEXTVAL, ?,?,?, ?, ?, ?, ?, ?, ?,?,?,SYSDATE,?,SYSDATE)") ;
		return stb;
		
	}
	/**
	 * 	Query to Update into Order line license content table
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQueryLicenseContentRef() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT).append(" SET VENDOR_LC_QTY=?, DOWNLOADED_DTTM=?, DOWNLOADED_CD=?, VENDOR_LC_DESC=?"
				+ ", FINAL_LC_PRICE=?, ORIGINAL_LC_PRICE=?, ORDER_PLACED_DTTM=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE "
				+ " WHERE SYS_ORDER_LINE_ID=? AND VENDOR_LC_ID=? AND ORDER_PLACED_DTTM=To_DATE(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
		
	}
	/**
	 * 	Query to insert into Order history table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryOrderHistoryTab() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_HISTORY).append("(SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_PLACED_DTTM,");
		stb.append("ORDER_STATUS, ORDER_ACTION_DTTM,ORDER_ACTION_NOTES, SYS_EXCEPTION_ID,  CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) ");
		stb.append("VALUES(OM_ORDER_HISTORY_SEQ.NEXTVAL, ?,?,?,?,?,?,?,?,SYSDATE,?, SYSDATE)") ;
		return stb;
		
	}
	/**
	 * 	Query to insert into Order history table
	 * @return
	 * 	String query
	 */
	public static StringBuilder completedOrderHistoryQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_HISTORY).append("(SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_PLACED_DTTM,ORDER_STATUS,ORDER_ACTION_DTTM, CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) "
				+ "VALUES(OM_ORDER_HISTORY_SEQ.NEXTVAL, ?,?,?,?,?,?,?,?,?)") ;
		return stb;
		
	}
	
	/**
	 * 	Query to insert into Order line exception table
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryException() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append("(SYS_ORD_EXCEPTION_ID, SYS_ORDER_ID, SYS_ORDER_LINE_ID,SYS_EXCEPTION_TYPE_ID, PREVIOUS_ORDER_STATUS, PREVIOUS_ENVELOPE_NO, WASTE_QTY,WASTE_COST,NOTES,"
				+ "STATUS,ORDER_PLACED_DTTM, CREATE_USER_ID,CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM,SYS_LOCATION_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?)") ;
		return stb;
	}
	
	
	/**
	 * 	Query to insert printable sign data of an order item
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryPrintableSignsRef(){
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE_SIGNS_DTL).append("(SYS_ORDER_LINE_SIGNS_ID,SYS_ORDER_LINE_ID,SIGNS_IMAGE_QTY, SYS_IMAGE_ID,ORDER_PLACED_DTTM, CREATE_USER_ID, "
				+ "CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) "
				+ "VALUES(OM_ORDER_LINE_SIGNS_DTL_SEQ.NEXTVAL,?, ?, ?,?, ?,SYSDATE,?,SYSDATE)") ;
		return stb;
	}
	/**
	 * 	Query to Update printable sign data of an order item
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQueryPrintableSignsRef(){
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE_SIGNS_DTL).append(" SET SIGNS_IMAGE_QTY=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE "
				+ " WHERE SYS_ORDER_LINE_ID=? AND SYS_IMAGE_ID=? AND ORDER_PLACED_DTTM=To_DATE(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
	}
	/**
	 * 	Query to insert ALU details data of an order item
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertQueryALUDetails() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU).append("(SYS_OL_PLU_ID, SYS_ORDER_LINE_ID, ACTIVE_CD, SYS_PRODUCT_ID,SYS_PLU_ID, PLU_DISCOUNT_AMOUNT, "
				+ "ORDER_PLACED_DTTM,CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM)"
				+ " VALUES(?, ?, ?,?, ?, ?, ?, ?, ?, ?,?)") ;
		return stb;
	}
	
	/**
	 * 	Query to insert ALU details data of an order item
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertExcepALUDetails() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU).append("(SYS_OL_PLU_ID, SYS_ORDER_LINE_ID, ACTIVE_CD, SYS_PRODUCT_ID,SYS_PLU_ID, PLU_DISCOUNT_AMOUNT, ORDER_PLACED_DTTM,CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM)"
				+ " VALUES(OM_ORDER_LINE_PLU_SEQ.NEXTVAL, ?, ?,?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE)") ;
		return stb;
	}
	// New add orderLinePLU query
	public static StringBuilder selorderLinePluQuery() {
		StringBuilder stb = new StringBuilder();
		stb.append("SELECT TO_CHAR(OPLU.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDERPLACEDDTTM,OPLU.SYS_OL_PLU_ID,OPLU.SYS_ORDER_LINE_ID,OPLU.SYS_PLU_ID,OPLU.PLU_DISCOUNT_AMOUNT,OPLU.ACTIVE_CD,OPM.PLU_NBR ");
		stb.append("FROM OM_ORDER_LINE_PLU OPLU,OM_PROMOTION OPM WHERE OPLU.SYS_PLU_ID = OPM.SYS_PLU_ID AND OPLU.ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS') AND OPLU.SYS_ORDER_LINE_ID =? ");
		return stb;
	}
	
	/**
	 * 	Query to Update Active IND  for Exception flow
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateQueryALUDetails() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU).append(" SET PLU_DISCOUNT_AMOUNT = ?,UPDATE_USER_ID=?,UPDATE_DTTM=? WHERE SYS_OL_PLU_ID=? AND ORDER_PLACED_DTTM=To_DATE(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
	}
	/**
	 * 	Fetch shopping cart id reference for market basket id of an order
	 * @return
	 * 	String query
	 */
	public static StringBuilder getShoppingCartIDQry() {
		StringBuilder stb = new StringBuilder();
		stb.append(" SELECT SYS_SHOPPING_CART_ID FROM ").append(PhotoOmniDBConstants.OM_SHOPPING_CART).append(" WHERE SHOPPING_CART_NBR=? AND SYS_LOCATION_ID=?") ;
		return stb;
	}
	/**
	 * 	Query to insert shopping cart id reference for market basket id of an order
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertShoppingCartQry() {
		StringBuilder stb = new StringBuilder();
		stb.append(" INSERT INTO ").append(PhotoOmniDBConstants.OM_SHOPPING_CART).append("(SYS_SHOPPING_CART_ID, SHOPPING_CART_NBR, SYS_LOCATION_ID, CART_TYPE_CD, PM_STATUS_CD, ORDER_PLACED_DTTM,CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) ");
				stb.append("VALUES(OM_SHOPPING_CART_SEQ.NEXTVAL, ?, ?,?, ?,?,?,SYSDATE,?,SYSDATE)") ;
		return stb;
	}
	/**
	 * 	Query to insert MBPLU data for an order
	 * @return
	 * 	String query
	 */
	
	public static StringBuilder insertShoppingCartPLUQry() { 
		StringBuilder stb = new StringBuilder();
		stb.append(" INSERT INTO ").append(PhotoOmniDBConstants.OM_SHOPPING_CART_PLU).append("(SYS_SC_PLU_ID,ACTIVE_CD,SYS_SHOPPING_CART_ID, SYS_PLU_ID,PLU_DISCOUNT_AMOUNT, ORDER_PLACED_DTTM,CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) ");
		stb.append("VALUES(?, ?,?, ?, ?, ?, ?,?, ?, ?)") ;
		return stb;
	}
	//Newly added update query
	public static StringBuilder updateShoppingCartPLUDataQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_SHOPPING_CART_PLU).append(" SET PLU_DISCOUNT_AMOUNT=?, UPDATE_USER_ID=?,UPDATE_DTTM= ? ");
		stb.append(" WHERE SYS_SHOPPING_CART_ID=? AND ORDER_PLACED_DTTM= To_DATE(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
	}
	/**
	 * Query to select data from OM_SHOPPING_CART_PLU
	 * @return
	 */
	public static StringBuilder selectShoppingCartPLUQry() { 
		StringBuilder stb = new StringBuilder();
		stb.append("SELECT TO_CHAR(SCPLU.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDERPLACEDDTTM,SCPLU.SYS_SC_PLU_ID,SCPLU.SYS_SHOPPING_CART_ID,SCPLU.SYS_PLU_ID,SCPLU.PLU_DISCOUNT_AMOUNT,SCPLU.ACTIVE_CD,");
		stb.append(" OMP.PLU_NBR FROM ").append( PhotoOmniDBConstants.OM_SHOPPING_CART_PLU).append(" SCPLU, ").append( PhotoOmniDBConstants.OM_PROMOTION ).append(" OMP  WHERE SCPLU.SYS_PLU_ID = OMP.SYS_PLU_ID  AND SCPLU.SYS_SHOPPING_CART_ID = ? ");
		stb.append("AND  SCPLU.ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
		return stb;
	}
	
	/**
	 * 	Query to UPDATE MBPLU data for an order
	 * @return
	 * 	String query
	 */
	
	public static StringBuilder updateShoppingCartPLUQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_SHOPPING_CART_PLU).append(" SET ACTIVE_CD=?, UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE ");
		stb.append(" WHERE SYS_SHOPPING_CART_ID=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
	}
	/**
	 * 	Query to insert ORDER PLU data for an order
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertOrderPLUQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_PLU).append("(SYS_ORDER_PLU_ID,SYS_ORDER_ID, ACTIVE_CD, SYS_PLU_ID,PLU_DISCOUNT_AMOUNT, ORDER_PLACED_DTTM,");
		stb.append("CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) ");
		stb.append("VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)") ;
		return stb;
	}
	
	/**
	 * 	Query to Update ORDER PLU data ACTIVE IND
	 * @return
	 * 	String query
	 */
	public static StringBuilder updateOrderPLUQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_PLU).append(" SET PLU_DISCOUNT_AMOUNT=?, UPDATE_USER_ID=?,UPDATE_DTTM= ? ");
		stb.append(" WHERE SYS_ORDER_PLU_ID=? AND ORDER_PLACED_DTTM=To_Date(?,'YYYY-MM-DD HH24:MI:SS')") ;
		return stb;
	}
	//New added
	
	public static StringBuilder selectOrderPLUQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("SELECT TO_CHAR(OPLU.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDERPLACEDDTTM,OPLU.SYS_ORDER_PLU_ID,OPLU.SYS_ORDER_ID,OPLU.SYS_PLU_ID,OPLU.PLU_DISCOUNT_AMOUNT,OPLU.ACTIVE_CD,OPM.PLU_NBR ");
		stb.append("FROM OM_ORDER_PLU OPLU,OM_PROMOTION OPM WHERE OPLU.SYS_PLU_ID = OPM.SYS_PLU_ID AND OPLU.ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') AND OPLU.SYS_ORDER_ID =? ");
		return stb;
	}
	/**
	 * 	Query to insert ORDER PLU data for an order
	 * @return
	 * 	String query
	 */
	public static StringBuilder insertOrderExceptionPLUQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_PLU).append("(SYS_ORDER_PLU_ID,SYS_ORDER_ID, ACTIVE_CD, SYS_PLU_ID,PLU_DISCOUNT_AMOUNT, ");
		stb.append("ORDER_PLACED_DTTM,CREATE_USER_ID, CREATE_DTTM, UPDATE_USER_ID,UPDATE_DTTM) ");
		stb.append("VALUES(OM_ORDER_PLU_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?,SYSDATE, ?, SYSDATE)") ;
		return stb;
	}
	/**
	 * @return
	 */
	public static StringBuilder getCalenderIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_CALENDAR_ID FROM ").append(PhotoOmniDBConstants.OM_CALENDER).append(" WHERE DAY_DATE = TO_DATE(? ,'YYYY-MM-DD') ");
		return query;
	}
	
	
	
	public static StringBuilder selLCOrderQry() {
		StringBuilder stb = new StringBuilder();
		stb.append("SELECT B.SYS_ORDER_LINE_ID, ");
		stb.append("A.ORDER_PLACED_DTTM ");
		stb.append(" FROM OM_ORDER A, ");
	    stb.append(" OM_ORDER_LINE B, ");
		stb.append("OM_PRODUCT C ");
		stb.append(" WHERE A.SYS_ORDER_ID    = B.SYS_ORDER_ID ");
		stb.append(" AND A.ORDER_PLACED_DTTM = B.ORDER_PLACED_DTTM ");
		stb.append(" AND B.SYS_PRODUCT_ID    = C.SYS_PRODUCT_ID ");
		stb.append(" AND A.ORDER_NBR          = ? ");
		stb.append("AND C.PRODUCT_NBR       = ? ");
		stb.append("AND A.ORDER_PLACED_DTTM > SYSDATE - 210 ");
	    return stb;
	
	}
	
	public static StringBuilder updateLCOrderQry() {
		StringBuilder stb = new StringBuilder();
		/*stb.append("UPDATE ").append(PhotoOmniDBConstants.OM_OL_LICENSE_CONTENT);
		stb.append(" SET DOWNLOADED_DTTM = ? ,DOWNLOADED_CD = ? ,UPDATE_DTTM= SYSDATE WHERE ");
		stb.append(" VENDOR_LC_ID = ? AND ");
		stb.append(" SYS_ORDER_LINE_ID = (SELECT SYS_ORDER_LINE_ID FROM ");
		stb.append( PhotoOmniDBConstants.OM_ORDER_LINE).append(" WHERE ");
		stb.append(" SYS_PRODUCT_ID = (SELECT SYS_PRODUCT_ID FROM ").append(PhotoOmniDBConstants.OM_PRODUCT);
		stb.append(" WHERE ").append(" PRODUCT_NBR = ? ) AND ");
		stb.append(" SYS_ORDER_ID = (SELECT SYS_ORDER_ID FROM ");
		stb.append(PhotoOmniDBConstants.OM_ORDER);
		stb.append(" WHERE ORDER_NBR = ?) )") ;*/
		
		stb.append("UPDATE OM_OL_LICENSE_CONTENT ");
		stb.append("SET DOWNLOADED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS'), ");
		stb.append(" DOWNLOADED_CD       = ? ,");
		stb.append(" UPDATE_DTTM         = SYSDATE ");
		stb.append("WHERE VENDOR_LC_ID    = ? ");
		stb.append("AND SYS_ORDER_LINE_ID = ? ");
		stb.append("AND ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
	}
	
	public static StringBuilder updateTookOrderQry() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" SET LABELED_EMPLOYEE_ID=?,UPDATE_USER_ID=?,  UPDATE_DTTM = SYSDATE  WHERE SYS_ORDER_ID=? AND ORDER_PLACED_DTTM=?");
		return query;
	}
	
	public static StringBuilder selectOrderLabelDataQry() {
		StringBuilder query = new StringBuilder();
		/*query.append("SELECT SYS_ORDER_ID, omorder.STATUS, omorder.ORDER_PLACED_DTTM,SYS_SHOPPING_CART_ID, TO_CHAR(omorder.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDER_SUBMITTED_TIME FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" omorder LEFT JOIN ").append(PhotoOmniDBConstants.OM_LOCATION);
		query.append(" ON SYS_OWNING_LOC_ID=SYS_LOCATION_ID LEFT JOIN ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" attr USING(SYS_ORDER_ID) WHERE ORDER_NBR=? AND LOCATION_NBR=?");*/
		
		query.append("SELECT Omorder.Sys_Order_Id,");
		query.append("Omorder.Status,");
		query.append("Omorder.Order_Placed_Dttm,");
		query.append(" Attr.SYS_SHOPPING_CART_ID,");
		query.append(" TO_CHAR(Omorder.Order_Placed_Dttm, 'YYYY-MM-DD HH24:MI:SS') AS Order_Submitted_Time ");
		query.append(" FROM Om_Order Omorder,");
		query.append(" Om_Order_Attribute Attr,");
		query.append(" Om_Location loc ");
		query.append(" WHERE Omorder.Sys_Order_Id    = Attr.Sys_Order_Id ");
		query.append(" AND Omorder.Order_Placed_Dttm = Attr.Order_Placed_Dttm ");
		query.append(" AND Omorder.Sys_Owning_Loc_Id =loc.Sys_Location_Id ");
		query.append(" AND Omorder.Order_Nbr         =? ");
		query.append(" AND loc.Location_Nbr          =? ");
		query.append("AND Omorder.Order_Placed_Dttm > sysdate - 210 ");
		return query;
	}
	
	
	public static StringBuilder selectLateEnvQry(LateEnvelopeReportReqBean reqBean){
		StringBuilder selectqry = new StringBuilder();
		String sortColumn = reqBean.getFilter().getSortColumnName();
		String sortOrder = reqBean.getFilter().getSortOrder();
		String msgTimestamp = ServiceUtil.dateformat24(reqBean.getMessageHeader().getMsgSentTimestamp());
		selectqry.append("SELECT * FROM ")
		//Take ALL the UNIONED Tables, get row numbers and total count
		.append("(SELECT A.*, ROW_NUMBER() OVER (ORDER BY "+sortColumn+" "+sortOrder+") AS RNK, COUNT(A.SYS_ORDER_ID) OVER() AS TOTALROWS FROM 	")
		.append("(SELECT * FROM (SELECT * FROM ( ")
		//Query where LATE_ENVELOPE_CD=1
		.append("SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
    	.append("orderattr.PROCESSING_TYPE_CD, ")
    	.append("omorder.STATUS, ")
    	.append("userattr.FIRST_NAME, ")
    	.append("userattr.LAST_NAME, ")
    	.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
    	.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
    	.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
    	.append("FROM OM_ORDER omorder ")
    	.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
    	.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
    	.append("AND omorder.ORDER_PLACED_DTTM =orderattr.ORDER_PLACED_DTTM ")
    	.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
    	.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
    	.append("INNER JOIN OM_LOCATION OL ")
    	.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
    	.append("WHERE OL.LOCATION_NBR = ? ")
    	.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND orderattr.LATE_ENVELOPE_CD = 1 ) ")

    	.append("UNION ALL ")
    	//Query where ORDER_STATUS='PROC' and PROMISED_DTTM<CURRENT_DTTM
		.append("SELECT * FROM (SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
		.append("orderattr.PROCESSING_TYPE_CD, ")
		.append("omorder.STATUS, ")
		.append("userattr.FIRST_NAME, ")
		.append("userattr.LAST_NAME, ")
		.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
		.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
		.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
		.append("FROM OM_ORDER omorder ")
		.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
		.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
		.append("AND omorder.ORDER_PLACED_DTTM =orderattr.ORDER_PLACED_DTTM ")
		.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
		.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
		.append("INNER JOIN OM_LOCATION OL ")
		.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
		.append("WHERE OL.LOCATION_NBR = ? ")
		.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND omorder.PROMISE_DELIVERY_DTTM < TO_DATE('"+msgTimestamp+"','YYYY-MM-DD HH24:MI:SS') ")
		.append("AND omorder.STATUS ='PROC' )")
	
		.append("UNION ALL ")
		//Query where ORDER_STATUS='SOLD' AND PROMISED_DTTM < CURRENT_DTTM AND PROMISED_DTTM < SOLD_DTTM AND (COMPLETED_DTTM = NULL OR COMPLETED_DTTM='0001-01-01')
		.append("SELECT * FROM (SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
		.append("orderattr.PROCESSING_TYPE_CD, ")
		.append("omorder.STATUS, ")
		.append("userattr.FIRST_NAME, ")
		.append("userattr.LAST_NAME, ")
		.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
		.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
		.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
		.append("FROM OM_ORDER omorder ")
		.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
		.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
		.append("AND omorder.ORDER_PLACED_DTTM = orderattr.ORDER_PLACED_DTTM ")
		.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
		.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
		.append("INNER JOIN OM_LOCATION OL ")
		.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
		.append("WHERE OL.LOCATION_NBR = ? ")
		.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND omorder.PROMISE_DELIVERY_DTTM < TO_DATE('"+msgTimestamp+"','YYYY-MM-DD HH24:MI:SS') ")
		.append("AND omorder.PROMISE_DELIVERY_DTTM < omorder.ORDER_SOLD_DTTM ")
		.append("AND omorder.STATUS ='SOLD' ")
		.append("AND (omorder.ORDER_COMPLETED_DTTM IS NULL ")
		.append(" OR TRUNC(omorder.ORDER_COMPLETED_DTTM) = CAST(TO_DATE('0001-01-01','YYYY-MM-DD') AS TIMESTAMP ) ) )")
		
		.append(" ) ORDER BY "+sortColumn+" "+sortOrder+ " ")
		.append(") A ")
		.append( ") ")
		.append("WHERE RNK BETWEEN ? AND ? ");
		return selectqry;
	}
	
	public static StringBuilder selectLateEnvPrintQry(LateEnvelopeReportReqBean reqBean){
		StringBuilder selectqry = new StringBuilder();
		String sortColumn = reqBean.getFilter().getSortColumnName();
		String sortOrder = reqBean.getFilter().getSortOrder();
		String msgTimestamp = ServiceUtil.dateformat24(reqBean.getMessageHeader().getMsgSentTimestamp());
		selectqry.append("SELECT * FROM ")
		//Take ALL the UNIONED Tables and total count
		.append("(SELECT A.*, COUNT(A.SYS_ORDER_ID) OVER() AS TOTALROWS FROM 	")
		.append("(SELECT * FROM (SELECT * FROM ( ")
		//Query where LATE_ENVELOPE_CD=1
		.append("SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
    	.append("orderattr.PROCESSING_TYPE_CD, ")
    	.append("omorder.STATUS, ")
    	.append("userattr.FIRST_NAME, ")
    	.append("userattr.LAST_NAME, ")
    	.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
    	.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
    	.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
    	.append("FROM OM_ORDER omorder ")
    	.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
    	.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
    	.append("AND omorder.ORDER_PLACED_DTTM =orderattr.ORDER_PLACED_DTTM ")
    	.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
    	.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
    	.append("INNER JOIN OM_LOCATION OL ")
    	.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
    	.append("WHERE OL.LOCATION_NBR = ? ")
    	.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND orderattr.LATE_ENVELOPE_CD = 1 ) ")

    	.append("UNION ALL ")
    	//Query where ORDER_STATUS='PROC' and PROMISED_DTTM<CURRENT_DTTM
		.append("SELECT * FROM (SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
		.append("orderattr.PROCESSING_TYPE_CD, ")
		.append("omorder.STATUS, ")
		.append("userattr.FIRST_NAME, ")
		.append("userattr.LAST_NAME, ")
		.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
		.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
		.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
		.append("FROM OM_ORDER omorder ")
		.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
		.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
		.append("AND omorder.ORDER_PLACED_DTTM =orderattr.ORDER_PLACED_DTTM ")
		.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
		.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
		.append("INNER JOIN OM_LOCATION OL ")
		.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
		.append("WHERE OL.LOCATION_NBR = ? ")
		.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND omorder.PROMISE_DELIVERY_DTTM < TO_DATE('"+msgTimestamp+"','YYYY-MM-DD HH24:MI:SS') ")
		.append("AND omorder.STATUS ='PROC' )")
	
		.append("UNION ALL ")
		//Query where ORDER_STATUS='SOLD' AND PROMISED_DTTM < CURRENT_DTTM AND PROMISED_DTTM < SOLD_DTTM AND (COMPLETED_DTTM = NULL OR COMPLETED_DTTM='0001-01-01')
		.append("SELECT * FROM (SELECT orderattr.SYS_ORDER_ID , ")
		.append("orderattr.ENVELOPE_NUMBER, ")
		.append("omorder.ORDER_ORIGIN_TYPE, ")
		.append("orderattr.PROCESSING_TYPE_CD, ")
		.append("omorder.STATUS, ")
		.append("userattr.FIRST_NAME, ")
		.append("userattr.LAST_NAME, ")
		.append("TO_CHAR(orderattr.ORDER_PLACED_DTTM , 'MM/dd/yyyy hh:mi AM')   AS ORDER_PLACED_DTTM, ")
		.append("TO_CHAR(omorder.PROMISE_DELIVERY_DTTM , 'MM/dd/yyyy hh:mi AM') AS PROMISE_DELIVERY_DTTM, ")
		.append("TO_CHAR(omorder.ORDER_COMPLETED_DTTM , 'MM/dd/yyyy hh:mi AM')  AS ORDER_COMPLETED_DTTM ")
		.append("FROM OM_ORDER omorder ")
		.append("INNER JOIN OM_ORDER_ATTRIBUTE orderattr ")
		.append("ON omorder.SYS_ORDER_ID = orderattr.SYS_ORDER_ID ")
		.append("AND omorder.ORDER_PLACED_DTTM = orderattr.ORDER_PLACED_DTTM ")
		.append("INNER JOIN OM_USER_ATTRIBUTES userattr ")
		.append("ON orderattr.LABELED_EMPLOYEE_ID = userattr.SYS_USER_ID ")
		.append("INNER JOIN OM_LOCATION OL ")
		.append("ON OL.SYS_LOCATION_ID = omorder. SYS_OWNING_LOC_ID ")
		.append("WHERE OL.LOCATION_NBR = ? ")
		.append("AND TRUNC(omorder.PROMISE_DELIVERY_DTTM) BETWEEN CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) AND CAST(to_date(?,'DD-MON-YYYY') AS TIMESTAMP) ")
    	.append("AND omorder.PROMISE_DELIVERY_DTTM < TO_DATE('"+msgTimestamp+"','YYYY-MM-DD HH24:MI:SS') ")
		.append("AND omorder.PROMISE_DELIVERY_DTTM < omorder.ORDER_SOLD_DTTM ")
		.append("AND omorder.STATUS ='SOLD' ")
		.append("AND (omorder.ORDER_COMPLETED_DTTM IS NULL ")
		.append(" OR TRUNC(omorder.ORDER_COMPLETED_DTTM) = CAST(TO_DATE('0001-01-01','YYYY-MM-DD') AS TIMESTAMP ) ) )")
		
		.append(" ) ORDER BY "+sortColumn+" "+sortOrder+ " ")
		.append(") A ")
		.append( ") ");
		
		return selectqry;
	}
	
	
	public static StringBuilder selectLateEnvOrderInfoQry(){
		StringBuilder selectqry = new StringBuilder();
		
		selectqry.append("SELECT OM_ORDER.ORDER_ORIGIN_TYPE "); 
		selectqry.append("||'('  "); 
		selectqry.append("||OM_ORDER.ORDER_DESCRIPTION  ");
		selectqry.append("||')' AS DESCRIPTION, ");
		selectqry.append("OM_ORDER.FINAL_PRICE,OM_ORDER_ATTRIBUTE.ENVELOPE_NUMBER, OM_ORDER.SRC_VENDOR_ORDER_NBR,OM_ORDER.SRC_KIOSK_ORDER_NBR "); 
	    selectqry.append("FROM ").append(PhotoOmniDBConstants.OM_ORDER).append("  OM_ORDER,").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE ).append(" OM_ORDER_ATTRIBUTE WHERE OM_ORDER.SYS_ORDER_ID = OM_ORDER_ATTRIBUTE.SYS_ORDER_ID ");
		selectqry.append(" AND  OM_ORDER.SYS_ORDER_ID  = ?  AND TRUNC(OM_ORDER.ORDER_PLACED_DTTM) =TO_DATE(?,'mm-dd-yyyy')");
		 
		return selectqry;
	}
	
	
	public static StringBuilder getMBPMPluIDQuery(){
		
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("select * from (SELECT SYS_PLU_ID FROM ").append(PhotoOmniDBConstants.OM_PROMOTION);
		sqlQuery.append(" WHERE PLU_NBR=? ORDER BY PROMOTION_CREATION_DATE DESC) M WHERE ROWNUM = 1");
		return sqlQuery;
	}
	
	public static StringBuilder getProductPackInfo(){
		
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT PRINTING_TYPE, PACK_SIZE FROM ").append(PhotoOmniDBConstants.OM_PRODUCT).append(" INNER JOIN ");
		sqlQuery.append(PhotoOmniDBConstants.OM_PRODUCT_ATTRIBUTE).append(" USING(SYS_PRODUCT_ID) WHERE PRODUCT_NBR = ?");
		return sqlQuery;
	}
	
	public static StringBuilder selectQueryOrderRef() {
		StringBuilder query = new StringBuilder();		
			query.append(" SELECT SYS_SRC_VENDOR_ID, SYS_FULFILLMENT_VENDOR_ID FROM ");
			query.append(PhotoOmniDBConstants.OM_ORDER).append( " WHERE SYS_ORDER_ID=? AND ORDER_PLACED_DTTM=To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");		
			
		return query;
		
	}
	
	public static StringBuilder getExceptionSeqId() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT EXCEPTION_TYPE FROM ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE);
		query.append(" WHERE SYS_EXCEPTION_TYPE_ID= ?");
		
		return query;
	}
	
	public static StringBuilder getShopCartPMStatus() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PM_STATUS_CD FROM ");
		query.append(PhotoOmniDBConstants.OM_SHOPPING_CART).append(" INNER JOIN ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" USING(SYS_SHOPPING_CART_ID) WHERE SYS_ORDER_ID = ?");
		
		return query;
	}
	
	
	public static StringBuilder getOldOrderStatus() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT STATUS FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER);
		query.append(" WHERE SYS_ORDER_ID = ?");
		
		return query;
	}
	
	public static StringBuilder getOldOrderExceptionStatus() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT PREVIOUS_ORDER_STATUS FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION);
		query.append(" INNER JOIN ");
		query.append( PhotoOmniDBConstants.OM_EXCEPTION_TYPE);
		query.append(" USING(SYS_EXCEPTION_TYPE_ID) WHERE SYS_ORDER_ID = ?");
		
		return query;
	}
	
	public static StringBuilder updateExeOrderQry() {
		StringBuilder stb = new StringBuilder();
		stb.append(" UPDATE ").append(PhotoOmniDBConstants.OM_ORDER).append(" SET ORDER_CANCELLED_DTTM = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = SYSDATE ");
		stb.append(" WHERE SYS_ORDER_ID = ? ") ;
		return stb;
	}
	
	/** llllllllll
	 * This method find if the vendor is a Electronics Film vendor.
	 * @return query.
	 */
	public static StringBuilder getElectronisFilmVendor() {
	StringBuilder query = new StringBuilder();
	query.append(" SELECT COUNT(1) AS CNT FROM OM_CODE_DECODE WHERE CODE_ID = ? AND UPPER(CODE_TYPE) = UPPER('Electronic_Film_Vendor') ");
	return query;
	}
	/**
	 * 
	 * @param orderId
	 * @return
	 */
	/*public static String getCompletedOrderQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT * ");
		sb.append(" FROM (SELECT ORD.SYS_ORDER_ID    AS SYS_ORDER_ID,"); 
		sb.append(" OMORDATTR.PROCESSING_TYPE_CD     AS PROCESSING_TYPE_CD,");
		sb.append(" OMORDATTR.COST_CALCULATION_STATUS_CD   AS COST_CALCULATION_STATUS_CD,");
		sb.append(" OMORDATTR.PAY_ON_FULFILLMENT_CD        AS PAY_ON_FULFILLMENT_IND,");
	    sb.append(" TO_CHAR(ORD.ORDER_PLACED_DTTM ,'yyyy-mm-dd hh:mm:ss') AS ORDER_PLACED_DTTM");
	    sb.append(" FROM OM_ORDER ORD,");
	    sb.append(" OM_ORDER_ATTRIBUTE OMORDATTR");
	    sb.append(" WHERE ORD.SYS_ORDER_ID                   = OMORDATTR.SYS_ORDER_ID");
	    sb.append(" AND ORD.ORDER_PLACED_DTTM                = OMORDATTR.ORDER_PLACED_DTTM");
	    sb.append(" AND OMORDATTR.COST_CALCULATION_STATUS_CD = 'P'");	   
	    sb.append(" AND  ORD.SYS_ORDER_ID = ? ");
	    sb.append(" AND   TO_CHAR(ORD.ORDER_PLACED_DTTM ,'yyyy-mm-dd hh:mm:ss') =?");
	    sb.append("	ORDER BY SYS_ORDER_ID");
	    sb.append(" )TEMPORD");



		return sb.toString();
	}*/
	public static String getOrderItemInstoreCostQuery() {
		StringBuilder query = new StringBuilder();
		query.append("  SELECT Omproequcost.Sys_Product_Id AS SYS_PRODUCT_ID, ");
		query.append("  OMPROEQUCOST.PRINT_COST  AS PRINT_COST, ");
		query.append("  OMPROEQUCOST.DEVELOPMENT_COST    AS DEVELOPMENT_COST, ");
		query.append("  OMPROEQUCOST.ADDITIONAL_COST     AS ADDITIONAL_COST, ");
		query.append("  Omproequcost.Binding_Cover_Cost  AS Binding_Cost_Instore ");
		query.append("  FROM OM_EQUIPMENT_INSTANCE OMEQIPINST, ");
		query.append("  OM_PRODUCT_EQUIP_TYPE_COST OMPROEQUCOST ");
		query.append("  WHERE OMEQIPINST.Sys_Equipment_Type_Id = OMPROEQUCOST.Sys_Equipment_Type_Id ");
		query.append("  AND OMEQIPINST.SYS_EQUIPMENT_INSTANCE_ID = ? ");
		query.append("  AND OMPROEQUCOST.Sys_Product_Id   = ? ");
		
		return query.toString();
	}
	
	public static String getDefaultCalculationProdList(String calculatedProdList){
		StringBuilder query = new StringBuilder();
		query.append("SELECT OMORDLINE.SYS_PRODUCT_ID PRODID, OMORDLINE.ORIGINAL_LINE_PRICE AS ORIGINAL_LINE_PRICE,"
				+ " OMORDLINE.SYS_ORDER_ID AS ORDER_ID FROM OM_ORDER_LINE OMORDLINE "
				+ "WHERE OMORDLINE.SYS_ORDER_ID    = ? AND TO_CHAR(OMORDLINE.ORDER_PLACED_DTTM ,'yyyy-mm-dd hh:mm:ss')=?");
		if(calculatedProdList != null && calculatedProdList.trim().length() != 0){
			query.append(" AND OMORDLINE.SYS_PRODUCT_ID NOT IN ("+calculatedProdList+ ")");
		}
		return query.toString();
	}
	



	
	/**
	 * This method returns query for getting envelope History details
	 */
	public static StringBuilder getEnvelopeHistryDtls() {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT * FROM (SELECT TO_CHAR(ORDER_HISTORY.ORDER_ACTION_DTTM, 'MM/dd/yyyy hh:mi:ss AM' ) AS ORDER_ACTION_DTTM, ORDER_HISTORY.ORDER_ACTION_CD AS ACTION, ");
		query.append(" USER_ATTRIBUTE.FIRST_NAME AS FIRST_NAME, ");
		query.append(" USER_ATTRIBUTE.LAST_NAME AS LAST_NAME, ");
		query.append(" EXCEPTION_TYPE.REASON AS REASON, ");
		query.append(" ORDER_HISTORY.ORDER_ACTION_NOTES AS COMMENTS ");
		query.append(" FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER_HISTORY).append(
				" ORDER_HISTORY ");
		query.append(" JOIN ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(
				" ORDER_TAB ");
		query.append(" ON ORDER_TAB.SYS_ORDER_ID = ORDER_HISTORY.SYS_ORDER_ID ");
		query.append(" AND ORDER_TAB.ORDER_PLACED_DTTM = ORDER_HISTORY.ORDER_PLACED_DTTM ");
		
		query.append(" LEFT JOIN ");
		 query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" USER_ATTRIBUTE ");
		 query.append("  ON ORDER_HISTORY.CREATE_USER_ID       = USER_ATTRIBUTE.employee_id ");
		 
		 /*query.append(" LEFT JOIN ");
		 query.append(PhotoOmniDBConstants.OM_ORDER_EXCEPTION).append(" ORDER_EXCEPTION ");
		 query.append(" ON ORDER_HISTORY.SYS_ORDER_ID       = ORDER_EXCEPTION.SYS_ORDER_ID ");
		 query.append(" AND ORDER_HISTORY.ORDER_PLACED_DTTM = ORDER_EXCEPTION.ORDER_PLACED_DTTM ");*/
		  
		query.append(" LEFT JOIN ");
		query.append(PhotoOmniDBConstants.OM_EXCEPTION_TYPE).append(
				" EXCEPTION_TYPE ");
		query.append(" ON ORDER_HISTORY.SYS_EXCEPTION_ID = EXCEPTION_TYPE.SYS_EXCEPTION_TYPE_ID ");
		
		query.append(" WHERE ORDER_TAB.SYS_ORDER_ID = ?) ");
		query.append(" ORDER BY ORDER_ACTION_DTTM ASC");
		return query;
	}
	
	/**
	 * This method returns query for getting envelope popup Header Details
	 */
	public static StringBuilder getEnvelopePopupHeaderQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT * FROM (SELECT VENDOR_TAB. DESCRIPTION AS VENDOR_NAME, ");
		query.append(" ORDER_TAB.SYS_SRC_VENDOR_ID, ");
		query.append(" VENDOR_TAB.SYS_VENDOR_ID, ");
		query.append(" VENDOR_TAB. AREA_CODE         AS AREA_CODE, ");
		query.append("	 '(' || VENDOR_TAB.AREA_CODE || ')'  ||  VENDOR_TAB.PHONE_NBR AS VENDOR_PHONE, ");
		query.append("  SHIPMENT_TAB. SHIPMENT_STATUS AS SHIPMENT_STATUS, ");
		query.append("	  SHIPMENT_TAB. SHIPMENT_URL    AS VENDOR_TRACKING_SITE,");
		query.append(" ORDER_TAB.SRC_VENDOR_ORDER_NBR as WEB_ID, ");
		query.append("  ORDER_TAB. SRC_KIOSK_ORDER_NBR AS KIOSK_ID ");
		query.append("	FROM ");
		query.append(PhotoOmniDBConstants.OM_ORDER).append(" ORDER_TAB ");
		query.append(" LEFT OUTER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_VENDOR).append(" VENDOR_TAB ");
		query.append(" ON ( ORDER_TAB.SYS_SRC_VENDOR_ID = VENDOR_TAB.SYS_VENDOR_ID ");
		query.append(" OR ORDER_TAB.SYS_FULFILLMENT_VENDOR_ID = VENDOR_TAB.SYS_VENDOR_ID ) ");
		query.append(" LEFT OUTER JOIN  ");
		query.append(PhotoOmniDBConstants.OM_SHIPMENT).append(" SHIPMENT_TAB ");
		query.append(" ON SHIPMENT_TAB.SYS_ORDER_ID              = ORDER_TAB.SYS_ORDER_ID ");
		query.append(" WHERE ORDER_TAB.SYS_ORDER_ID = ?)");
		return query;
	}

	/**
	 * This method returns query for getting envelope order Information
	 */
	public static StringBuilder getEnvelopeOrderDescriptionQry() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT * FROM ( SELECT ORDER_TAB.Order_Origin_Type ");
		query.append("  ||'(' ");
		query.append("	||ORDER_TAB.Order_Description");
		query.append("  ||')' AS ORDER_DESC, ");
		query.append(" ORDER_TAB.Final_Price  as FINAL_PRICE ");
		query.append("	FROM Om_Order ORDER_TAB ");
		query.append(" WHERE  ");
		query.append("	ORDER_TAB.Sys_Order_Id = ? ");
		query.append(" AND TRUNC(ORDER_TAB.order_placed_dttm) = TO_DATE(? ,'MM-dd-YYYY')) ");
		return query;
	}
	
	/**
	 * This method returns query for getting envelope order Discount details ie discount amount and discount description
	 */
	public static StringBuilder getEnvelopeOrderDiscountQry() {
		StringBuilder query = new StringBuilder();
		query.append("	SELECT DISTINCT * FROM ( ");
		query.append("	SELECT PROMOTION_TAB.Promotion_Desc as ORDER_PROMOTION_DESC, ");
		query.append(" ORDER_PLU_TAB.Plu_Discount_Amount AS ORDER_PROMOTION_AMT ");
		query.append("	FROM Om_Order ORDER_TAB, ");
		query.append(" Om_Order_Plu ORDER_PLU_TAB, ");
		query.append("	 Om_Promotion PROMOTION_TAB ");
		query.append("	WHERE ORDER_TAB.Sys_Order_Id    = ORDER_PLU_TAB.Sys_Order_Id ");
		query.append("	AND ORDER_TAB.Order_Placed_Dttm = ORDER_PLU_TAB.Order_Placed_Dttm ");
		query.append("	AND ORDER_PLU_TAB.sys_plu_id    = PROMOTION_TAB.sys_plu_id ");
		query.append("	AND ORDER_TAB.Sys_Order_Id      =? ");
		query.append("	AND TRUNC(ORDER_TAB.order_placed_dttm) = TO_DATE(? ,'MM-dd-YYYY')) ");
		return query;
	}

	/**
	 * This method returns query for getting envelope order Line details 
	 * ie product description, final price, quantity and per unit price
	 */
	public static StringBuilder getEnvelopeOrderLineDescQry() {
		StringBuilder query = new StringBuilder();

		query.append(" SELECT DISTINCT * FROM (	SELECT ORDER_LINE_TAB.sys_order_line_id as ORDER_LINE_ID, ");
		query.append("	ORDER_TAB.Order_Origin_Type ");
		query.append("	||'(' ");
		query.append("	||PRODUCT_TAB.DESCRIPTION ");
		query.append("	||')' AS PRODUCT_DESC ,  ORDER_LINE_TAB.QUANTITY  AS ORDER_LINE_QUANTITY, ");
		query.append("	 ORDER_LINE_TAB.FINAL_PRICE AS ORDER_LINE_PRICE ");
		query.append("	FROM Om_Order ORDER_TAB, ");
		query.append("	 Om_Order_Line ORDER_LINE_TAB, ");
		query.append("	  Om_Product PRODUCT_TAB ");
		query.append("	WHERE ORDER_TAB.Sys_Order_Id    = ORDER_LINE_TAB.Sys_Order_Id ");
		query.append("	AND ORDER_TAB.Order_Placed_Dttm = ORDER_LINE_TAB.Order_Placed_Dttm ");
		query.append("	AND ORDER_LINE_TAB.Sys_Product_Id    = PRODUCT_TAB.Sys_Product_Id ");
		query.append("	AND ORDER_TAB.Sys_Order_Id      = ? ");
		query.append("	AND TRUNC(ORDER_TAB.order_placed_dttm)  = TO_DATE(?,'MM-dd-YYYY') ");
		query.append("	ORDER BY ORDER_LINE_TAB.sys_order_line_id) ");
		return query;
	}

	/**
	 * This method returns query for getting envelope order Line Discount details 
	 * ie discount amount and discount description
	 */
	public static StringBuilder getEnvelopeOrderLinePromotionQry() {

		StringBuilder query = new StringBuilder();
		query.append("	SELECT DISTINCT * ");
		query.append("	FROM ");
		query.append("	  (SELECT ORDER_LINE_TAB.Sys_Order_Line_Id AS ORDER_LINE_ID, ");
		query.append("	  PROMOTION_TAB.Promotion_Desc AS ORDER_LINE_PROMOTION_DESC, ");
		query.append("	   ORDER_LINE_PLU_TAB.Plu_Discount_Amount AS ORDER_LINE_PROMOTION_AMT ");
		query.append("	 FROM Om_Order_Line ORDER_LINE_TAB ");
		query.append("	 INNER JOIN Om_Order_Line_Plu ORDER_LINE_PLU_TAB ");
		query.append("	 ON ORDER_LINE_TAB.Sys_Order_line_Id  = ORDER_LINE_PLU_TAB.Sys_Order_line_Id ");
		query.append("	 AND ORDER_LINE_TAB.Order_Placed_Dttm = ORDER_LINE_PLU_TAB.Order_Placed_Dttm ");
		query.append("	  INNER JOIN Om_Promotion PROMOTION_TAB ");
		query.append("	 ON ORDER_LINE_PLU_TAB.sys_plu_id            = PROMOTION_TAB.sys_plu_id ");
		query.append("	WHERE ORDER_LINE_TAB.Sys_Order_Id           = ? ");
		query.append("	 AND TRUNC(ORDER_LINE_TAB.order_placed_dttm) = TO_DATE(? ,'MM-dd-YYYY') ");
		query.append("	  ) ");
		query.append(" ORDER BY ORDER_LINE_ID ");
		return query;
	
	}
	
/*
 * CostCalculation changes
 */
	public static String updateCostCalOrdAttrQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_ATTRIBUTE SET COST = ?, COST_CALCULATION_STATUS_CD = ?, "
				+ "FULFILLMENT_VENDOR_COST = ?, PAY_ON_FULFILLMENT_CD = ?, UPDATE_USER_ID = 'SYSTEM', UPDATE_DTTM = SYSDATE WHERE SYS_ORDER_ID = ?  AND ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
		return query.toString();
	}

	public static String updateCostCalOrdLineQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_LINE SET COST = ?, FULFILLMENT_VENDOR_COST = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = SYSDATE "
				+ "WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID = ? AND ORDER_PLACED_DTTM = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
		return query.toString();
		
	}

	public static String getItemDetailsQuery() {
		
		StringBuilder query = new StringBuilder();
		
		query.append("  Select OMORDLINE.Sys_Order_Id AS SYS_ORDER_ID, TO_CHAR(OMORDLINE.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDER_PLACED_DTTM, OMORDLINE.Sys_Order_Line_Id    As Order_Line_Id, ");
		query.append("  OMORDLINE.Sys_Product_Id AS Sys_Product_Id, ");
		query.append("  OMORDLINE.QUANTITY AS QUANTITY,  OMORDLINEATTR.PRINTED_QTY AS PRINTED_QTY,  Omordlineattr.Wasted_Qty AS Wasted_Qty, ");
		query.append("  OMORDLINE.Original_Line_Price AS Original_Line_Price, Omordline.Sys_Machine_Instance_Id AS Sys_Machine_Instance_Id, ");
		query.append("  OMORDLINE.Sys_Equipment_Instance_Id AS Sys_Equipment_Instance_Id, ");	
		query.append("  OMORDLINEATTR.PANOROMIC_PRINT_QTY  AS PANOROMIC_PRINT_QTY,OMORDLINEATTR.INPUT_IMAGE_QTY AS INPUT_IMAGE_QTY ");
		query.append("  FROM OM_ORDER_LINE OMORDLINE,Om_Order_Line_Attribute Omordlineattr ");
		query.append("  WHERE OMORDLINEATTR.SYS_ORDER_LINE_ID = OMORDLINE.SYS_ORDER_LINE_ID ");
		query.append("  AND OMORDLINEATTR.ORDER_PLACED_DTTM   = OMORDLINE.ORDER_PLACED_DTTM ");
		query.append("  AND OMORDLINE.Sys_Order_Id =? AND OMORDLINE.ORDER_PLACED_DTTM = To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		
		
		
		return query.toString();
	}
	
	public static String getCompletedOrderQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ORD.SYS_ORDER_ID AS SYS_ORDER_ID , TO_CHAR(ORD.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDER_PLACED_DTTM , ORD.SYS_FULFILLMENT_VENDOR_ID as SYS_FULFILLMENT_VENDOR_ID,ORD_ATTR.PROCESSING_TYPE_CD AS PROCESSING_TYPE_CD, ");
		sb.append(" ORD_ATTR.PAY_ON_FULFILLMENT_CD AS PAY_ON_FULFIILLMENT_CD,ORD_ATTR.COST_CALCULATION_STATUS_CD AS COST_CALCULATION_STATUS_CD ");
		sb.append(" FROM OM_ORDER ORD,OM_ORDER_ATTRIBUTE ORD_ATTR ");
		sb.append(" WHERE ORD.SYS_ORDER_ID = ORD_ATTR.SYS_ORDER_ID ");
		sb.append(" AND ORD.SYS_ORDER_ID = ? AND ORD.ORDER_PLACED_DTTM = To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		



		return sb.toString();
	}
	public static String getInstoreMachineCostQuery(){
		StringBuilder query = new StringBuilder();
		
		query.append(" SELECT Dflt_Mac_Cost_Percnt ");
		query.append(" FROM Om_Machine_Instance A, ");
		query.append(" Om_Machine_Type B ");
		query.append(" Where A.Sys_Machine_Type_Id   = B.Sys_Machine_Type_Id ");
		query.append(" AND A.SYS_MACHINE_INSTANCE_ID = ? ");	
		return query.toString();
	}
	

	
	
	public static String getVendorIdQuery() {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  ");
		return query.toString();
		
	}
 	
	
	public static String getOrderItemVendorCostQuery() {
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT Omvenpro.Sys_Product_Id AS Sys_Product_Id, ");
		query.append(" OMVENPRO.COST AS COST,  OMVENPRO.COST_TYPE AS COST_TYPE, ");
		query.append(" OMVENPRO.SHIPPING_COST AS SHIPPING_COST, OMVENPRO.SHIPPING_COST_TYPE AS SHIPPING_COST_TYPE, ");
		query.append(" OMVENPRO.ADDITIONAL_COST  AS ADDITIONAL_COST, OMVENPRO.BINDING_COST AS BINDING_COST_VENDOR ");
		query.append(" From Om_Vendor_Product Omvenpro Where Omvenpro.Sys_Product_Id = ? AND Omvenpro.Sys_Vendor_Id = ?  ");
	
		
		return query.toString();
	}
	
	public static StringBuilder updateQryOrderItemAttributeRef() {
		StringBuilder stb = new StringBuilder();
		stb.append(" UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE).append(" SET PRINTED_QTY=?,  ");
		stb.append(" UPDATE_USER_ID=?,UPDATE_DTTM= SYSDATE ");
		stb.append (" WHERE SYS_ORDER_LINE_ID=? AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return stb;
		
	}
	
}
