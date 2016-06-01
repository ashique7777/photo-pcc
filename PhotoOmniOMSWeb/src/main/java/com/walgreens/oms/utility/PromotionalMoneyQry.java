/**
 * 
 */
package com.walgreens.oms.utility;

import com.walgreens.common.constant.PhotoOmniConstants;
import com.walgreens.common.constant.PhotoOmniDBConstants;

/**
 * @author CTS
 *
 */
public class PromotionalMoneyQry {

	public static String getPMOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ODR.SYS_ORDER_ID     AS ORDER_ID,");
		query.append("ODR.STATUS                AS ORDER_STATUS,");
		query.append("NVL(ORDER_ATTR.PM_STATUS, ' ')      AS PM_STATUS,");
		query.append("ODR.FINAL_PRICE           AS FINAL_PRICE,");
		query.append("ODR.SOLD_AMOUNT           AS SOLD_AMOUNT,");
		query.append("ODR.COUPON_CD             AS COUPON_IND,");
		query.append("ODR.SYS_OWNING_LOC_ID	AS LOCATION_ID,");
		query.append("Odr.Discount_Card_Used_Cd AS Discount_Card_Ind,");
		query.append("Omuserattr.EMPLOYEE_ID    As employeeId,");
		query.append("Omuserattr.Sys_User_Id    As empId,");
		query.append("Omuserattr.Pm_Eligible_Cd AS Pm_Eligible_Cd,");
		query.append("ORDER_ATTR.EXPENSE_CD     AS EXPENSE_IND ");
		query.append("FROM ").append(PhotoOmniDBConstants.OM_ORDER).append(" ODR,");
		query.append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE).append(" Order_Attr,");
		query.append(PhotoOmniDBConstants.OM_USER_ATTRIBUTES).append(" Omuserattr");
		query.append(" WHERE ODR.SYS_ORDER_ID             = ORDER_ATTR.SYS_ORDER_ID");
		query.append(" AND Odr.ORDER_PLACED_DTTM          = Order_Attr.ORDER_PLACED_DTTM");
		query.append(" AND Order_Attr.Labeled_Employee_Id = Omuserattr.Sys_User_Id");
		query.append(" AND Odr.SYS_ORDER_ID               = ?");
		query.append(" AND ODR.ORDER_PLACED_DTTM          = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getPMOrderLineQuery() {
		StringBuilder query = new StringBuilder();

		query.append("SELECT ORDERLINE.SYS_ORDER_ID   AS ORDER_ID,");
		query.append("ORDERLINE.SYS_ORDER_LINE_ID      AS ORDER_LINE_ID,");
		query.append("ORDERLINE.SYS_PRODUCT_ID      AS PRODUCT_ID,");
		query.append("Orderline.Quantity            AS Quantity,");
		query.append("Orderline.Final_Price         AS Final_Price,");
		query.append("Ordlineattr.Rolls_Sets_Qty    AS Sets_Qty");
		query.append(" FROM ").append(PhotoOmniDBConstants.OM_ORDER_LINE).append(" Orderline, ");
		query.append(PhotoOmniDBConstants.OM_ORDER_LINE_ATTRIBUTE).append(" Ordlineattr, ");
		query.append(PhotoOmniDBConstants.OM_PRODUCT).append(" Product");
		query.append(" WHERE Orderline.Sys_Order_Line_Id = Ordlineattr.Sys_Order_Line_Id ");
		query.append("AND ORDERLINE.ORDER_PLACED_DTTM   = ORDLINEATTR.ORDER_PLACED_DTTM ");
		query.append("AND ORDERLINE.SYS_PRODUCT_ID      = PRODUCT.SYS_PRODUCT_ID ");
		query.append("AND Orderline.Sys_Order_Id              = ? ");
		query.append("AND Orderline.ORDER_PLACED_DTTM         = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getProdPMChainRuleQuery() {
		StringBuilder query = new StringBuilder();

		query.append("SELECT OPM.SYS_PM_ID AS PM_ID,");
		query.append("Opm.Pm_Rule_Type,");
		query.append("OPM.PM_PAYMENT_TYPE,");
		query.append("OPM.TIER_TYPE,");
		query.append("OPM.DISCOUNT_APPLICABLE_CD,");
		query.append("OPM_DTL.MINIMUM_TIER,");
		query.append("OPM_DTL.MAXIMUM_TIER,");
		query.append("Opm_Dtl.PM_AMOUNT ");
		query.append("From");
		query.append(" (SELECT PM_MONEY.SYS_PM_ID        AS SYS_PM_ID, ");
		query.append("PM_MONEY.PM_RULE_TYPE           AS PM_RULE_TYPE, ");
		query.append("PM_MONEY.PM_PAYMENT_TYPE        AS PM_PAYMENT_TYPE, ");
		query.append(" PM_MONEY.TIER_TYPE              AS TIER_TYPE, ");
		query.append("PM_MONEY.DISCOUNT_APPLICABLE_CD AS DISCOUNT_APPLICABLE_CD ");
		query.append(" FROM OM_PROMOTIONAL_MONEY PM_MONEY ");
		query.append("WHERE PM_MONEY.TYPE ='PM' ");
		query.append("AND PM_MONEY.ACTIVE_CD =1 ");
		query.append("AND PM_MONEY.PM_DISTRIBUTION_TYPE ='Chain' ");
		query.append("AND Pm_Money.Start_Dttm          <= To_Date(?,'YYYY-MM-DD HH24:MI:SS') ");
		query.append("AND Pm_Money.End_Dttm            >= To_Date(?,'YYYY-MM-DD HH24:MI:SS') ");
		query.append("AND Pm_Money.Sys_Product_Id       = ? ");
		query.append(" AND Rownum                        = 1 ");
		query.append(") OPM, ");
		query.append("Om_Promotional_Money_Dtl Opm_Dtl ");
		query.append("WHERE Opm.Sys_Pm_Id   = Opm_Dtl.Sys_Pm_Id ");
		query.append("AND Opm_Dtl.Active_Cd = 1");
		return query.toString();
	}
	
	public static String getProdPMStoreRuleQuery() {
		StringBuilder query = new StringBuilder();

		query.append("SELECT OPM.SYS_PM_ID AS PM_ID,");
		query.append("Opm.Pm_Rule_Type,");
		query.append("OPM.PM_PAYMENT_TYPE,");
		query.append("OPM.TIER_TYPE,");
		query.append("OPM.DISCOUNT_APPLICABLE_CD,");
		query.append("OPM_DTL.MINIMUM_TIER,");
		query.append("OPM_DTL.MAXIMUM_TIER,");
		query.append("Opm_Dtl.Pm_Amount ");
		query.append("FROM");
		query.append("(SELECT PM_MONEY.SYS_PM_ID        AS SYS_PM_ID,");
		query.append("PM_MONEY.PM_RULE_DESC           AS PM_RULE_DESC,");
		query.append("PM_MONEY.PM_RULE_TYPE           AS PM_RULE_TYPE,");
		query.append("PM_MONEY.PM_PAYMENT_TYPE        AS PM_PAYMENT_TYPE,");
		query.append("PM_MONEY.TIER_TYPE              AS TIER_TYPE,");
		query.append("PM_MONEY.DISCOUNT_APPLICABLE_CD AS DISCOUNT_APPLICABLE_CD ");
		query.append("FROM Om_Promotional_Money Pm_Money, ");
		query.append("Om_Pm_Store_Dist_List Pm_Store_List ");
		query.append("WHERE Pm_Money.Sys_Pm_Id          = Pm_Store_List.Sys_Pm_Id ");
		query.append("AND PM_STORE_LIST.SYS_LOCATION_ID = ? ");
		query.append("AND PM_MONEY.TYPE                 ='PM' ");
		query.append("AND PM_MONEY.ACTIVE_CD            =1 ");
		query.append("AND Pm_Money.Pm_Distribution_Type = 'Store' ");
		query.append("AND Pm_Money.Start_Dttm          <= To_Date(?,'YYYY-MM-DD HH24:MI:SS') ");
		query.append("AND Pm_Money.End_Dttm            >= To_Date(?,'YYYY-MM-DD HH24:MI:SS') ");
		query.append("AND PM_MONEY.SYS_PRODUCT_ID       =? ");
		query.append("AND Rownum                        = 1 ");
		query.append(") OPM, ");
		query.append("Om_Promotional_Money_Dtl Opm_Dtl ");
		query.append("WHERE Opm.Sys_Pm_Id   = Opm_Dtl.Sys_Pm_Id ");
		query.append("AND Opm_Dtl.Active_Cd = 1 ");
		return query.toString();
	}
	
	public static String getOrderPMQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_ID,SYS_PRODUCT_ID ");
		query.append(" FROM ").append(PhotoOmniDBConstants.OM_ORDER_PM);
		query.append(" WHERE SYS_ORDER_ID    = ? ");
		query.append(" AND SYS_PRODUCT_ID <> 0 ");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getPMStatusQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_ATTRIBUTE);
		query.append(" SET PM_STATUS=?");
		query.append(" WHERE SYS_ORDER_ID = ?");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getOrderPmDefaultQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_PM);
		query.append(" SET EARNED_QTY=0, POTENTIAL_QTY=0,PENDING_QTY=0,EARNED_AMOUNT=0, POTENTIAL_AMOUNT=0,PENDING_AMOUNT=0,SYS_PM_ID=0,");
		query.append(" ACTIVE_CD=0, UPDATE_USER_ID='").append(PhotoOmniConstants.UPDATE_USER_ID).append("',UPDATE_DTTM=SYSDATE WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID !=0");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getOrderPmUpQry(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE ").append(PhotoOmniDBConstants.OM_ORDER_PM);
		query.append(" SET EARNED_QTY=?, POTENTIAL_QTY=?,PENDING_QTY=?,EARNED_AMOUNT=?, POTENTIAL_AMOUNT=?,PENDING_AMOUNT=?,SYS_PM_ID=?,");
		query.append(" ACTIVE_CD=?, UPDATE_USER_ID=?,UPDATE_DTTM=? WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID =? ");
		query.append(" AND ORDER_PLACED_DTTM = To_DATE(?,'YYYY-MM-DD HH24:MI:SS')");
		return query.toString();
	}
	
	public static String getOrderPmIsrtQry(){
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO ").append(PhotoOmniDBConstants.OM_ORDER_PM).append(" (EARNED_QTY,POTENTIAL_QTY,PENDING_QTY,EARNED_AMOUNT,POTENTIAL_AMOUNT,PENDING_AMOUNT, SYS_PM_ID,ACTIVE_CD,");
		query.append(" SYS_ORDER_ID,SYS_PRODUCT_ID,ORDER_PLACED_DTTM,SYS_EMPLOYEE_ID,CREATE_USER_ID,CREATE_DTTM,SYS_ORDER_PM_ID,UPDATE_USER_ID,UPDATE_DTTM)");
		query.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		return query.toString();
	}
	
	public static String getOrderPLUCntQry(){
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(1) ORDER_PLU_COUNT FROM ").append(PhotoOmniDBConstants.OM_ORDER_PLU);
		query.append(" WHERE SYS_ORDER_ID  = ?");
		query.append(" AND ORDER_PLACED_DTTM = To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		query.append(" AND ACTIVE_CD         = 1");
		
		return query.toString();		
	}
	public static String getOrderlinePLUCntQry(){
		
		StringBuilder query = new StringBuilder();		
		query.append("SELECT COUNT(1) ORDER_PLU_COUNT FROM ").append(PhotoOmniDBConstants.OM_ORDER_LINE_PLU);
		query.append(" WHERE SYS_ORDER_LINE_ID  = ?");
		query.append(" AND ORDER_PLACED_DTTM = To_Date(?,'YYYY-MM-DD HH24:MI:SS')");
		query.append(" AND ACTIVE_CD         = 1");
		return query.toString();	
	}
}
