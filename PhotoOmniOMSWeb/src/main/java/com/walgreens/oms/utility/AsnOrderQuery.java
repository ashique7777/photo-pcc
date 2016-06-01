package com.walgreens.oms.utility;

public class AsnOrderQuery {
	

	public static String getAsnOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT LOC.LOCATION_TYPE,TO_CHAR(ORD.ORDER_PLACED_DTTM,'YYYY-MM-DD HH24:MI:SS') AS ORDER_PLACED_DTTM, NVL(ORD.SYS_SRC_VENDOR_ID,0) AS VENDOR_ID,ORD.STATUS,ORD.SYS_ORDER_ID,LOC.LOCATION_NBR, ORD.SYS_FULFILLMENT_VENDOR_ID ");
		query.append(" FROM OM_ORDER ORD JOIN OM_LOCATION LOC ON ORD.SYS_OWNING_LOC_ID=LOC.SYS_LOCATION_ID WHERE ORD.ORDER_NBR= ? AND LOC.LOCATION_NBR =? ");
		return query.toString();
	} 

	public String getUpdateOmShipmentQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_SHIPMENT (ORDER_PLACED_DTTM,SYS_SHIPMENT_ID,SYS_ORDER_ID,SHIPMENT_TRACKING_NBR,CARRIER_NAME,ACTUAL_SHIPPED_DTTM,");
				query.append(" ESTIMATED_SHIP_DTTM,SHIPMENT_STATUS,EXPECTED_ARRIVAL_DTTM,ACTUAL_ARRIVAL_DTTM,SHIPMENT_URL,CARRIER_PHONE,");
				query.append(" CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) VALUES (?,OM_SHIPMENT_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}
	
	public String getSysOrderIdQuery(){
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(*) FROM OM_SHIPMENT WHERE  SYS_ORDER_ID=? ");
		return query.toString();
		
	}
	
	public String getCheckUpdateOmShipmentQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_SHIPMENT SET ORDER_PLACED_DTTM = ?,SHIPMENT_TRACKING_NBR = ?,");
		query.append("CARRIER_NAME = ?, ACTUAL_SHIPPED_DTTM = ?, ESTIMATED_SHIP_DTTM = ?, SHIPMENT_STATUS = ?, EXPECTED_ARRIVAL_DTTM = ?, ACTUAL_ARRIVAL_DTTM = ?,");
		query.append("SHIPMENT_URL = ?, CARRIER_PHONE = ?, CREATE_USER_ID = ?, CREATE_DTTM = ?, UPDATE_USER_ID = ?, UPDATE_DTTM = ?  WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}
	

	public String getUpdateOmOrdHistoryQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_ORDER_HISTORY (SYS_ORDER_HISTORY_ID,SYS_ORDER_ID,ORDER_ACTION_CD,ORDER_ACTION_DTTM,ORDER_STATUS,ORDER_ACTION_NOTES,ORDER_PLACED_DTTM,");
		query.append("CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) VALUES (OM_ORDER_HISTORY_SEQ.NEXTVAL,?,?,SYSDATE,?,?,?,?,SYSDATE,?,SYSDATE)");
		return query.toString();
	}
	public String getUpdateOmOrderQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ORD.STATUS,ORD.ORDER_PLACED_DTTM FROM OM_ORDER ORD JOIN OM_LOCATION LOC ON ORD.SYS_OWNING_LOC_ID=LOC.SYS_LOCATION_ID WHERE ORD.ORDER_NBR= ? AND LOC.LOCATION_NBR = ? AND ORD.ORDER_PLACED_DTTM = ? ");
		
		return query.toString();
	}

	public String getVendorCostCalcStageIndQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT NVL(VENDOR_COST_CALC_STAGE_CD,0) AS VENDOR_COST_CALC_STAGE_CD FROM OM_VENDOR_ATTRIBUTE OVA JOIN OM_VENDOR OV ON OVA.SYS_VENDOR_ID = OV.SYS_VENDOR_ID WHERE OV.SYS_VENDOR_ID = ?");
		return query.toString();
	}

	public String getCheckItemExistQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORDER_LINE_ID FROM OM_ORDER_LINE WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID = ? AND ORDER_PLACED_DTTM = ?");
		return query.toString();
	}

	public String getRemoveItemFromOmOrderLineQuery() {
		StringBuilder query = new StringBuilder();
		query.append("DELETE FROM OM_ORDER_LINE WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID = ?");
		return query.toString();
	}

	public String getupdateItemIntoOmOrderLineQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_LINE SET QUANTITY = ?, ORIGINAL_LINE_PRICE = ?, FINAL_PRICE = ?, DISCOUNT_AMT = ?, UPDATE_DTTM=SYSDATE  WHERE SYS_ORDER_ID = ? AND SYS_PRODUCT_ID = ? ");
		return query.toString();
	}

	public String getUpdateOmOrderDetailsQuery() {
		// TODO Auto-generated method stub
		return null;
	}
	

	public String getInsertItemIntoOmOrderLineQuery() {
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO OM_ORDER_LINE (ORDER_PLACED_DTTM,SYS_ORDER_LINE_ID,SYS_ORDER_ID,SYS_PRODUCT_ID,SYS_MACHINE_INSTANCE_ID,SYS_EQUIPMENT_INSTANCE_ID,UNIT_PRICE,");
		query.append(" QUANTITY,ORIGINAL_QTY,ORIGINAL_LINE_PRICE,FINAL_PRICE,DISCOUNT_AMT,LOYALTY_PRICE,LOYALTY_DISCOUNT_AMT,COST,FULFILLMENT_VENDOR_COST,LINE_SOLD_AMOUNT,CREATE_USER_ID,CREATE_DTTM,UPDATE_USER_ID,UPDATE_DTTM) ");
		query.append(" VALUES (?,OM_ORDER_LINE_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    		   return query.toString();
	}
//FINAL_PRICE,DISCOUNT_AMT
	public String getupdateOmOrderDetailsQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER SET FINAL_PRICE = ?,TOTAL_ORDER_DISCOUNT = ?,COUPON_CD = ?,");
		query.append(" LOYALTY_PRICE = ?,LOYALTY_DISCOUNT_AMOUNT = ?,UPDATE_DTTM=SYSDATE WHERE SYS_ORDER_ID = ?");
		return query.toString();
	}

	public String getUpdateOrdAttrCostCalStatusQuery() {
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_ORDER_ATTRIBUTE SET COST_CALCULATION_STATUS_CD = 'P' WHERE SYS_ORDER_ID = ?");
		return query.toString(); 
	}

	/**
	 * @return
	 */
	public String getExceptionIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_ORD_EXCEPTION_ID FROM OM_ORDER_EXCEPTION WHERE SYS_ORDER_ID = ? ");
		return query.toString();
	}
	
	public String getomOrderLineQuery()
	{
		StringBuilder query = new StringBuilder();
		query.append("SELECT SYS_PRODUCT_ID FROM OM_PRODUCT WHERE PRODUCT_NBR=?");
		return query.toString();
		
	}	
	
	public StringBuilder getMachineEquipDetails(){
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT MAC_INST.SYS_MACHINE_INSTANCE_ID,EQUIP_INST.SYS_EQUIPMENT_INSTANCE_ID FROM OM_MACHINE_INSTANCE MAC_INST JOIN OM_EQUIPMENT_INSTANCE EQUIP_INST ");
		query.append(" ON MAC_INST.SYS_MACHINE_INSTANCE_ID = EQUIP_INST.SYS_MACHINE_INSTANCE_ID JOIN OM_PRODUCT_MC_TYPE_EQUIP_TYPE PROD_MAC_EQUIP ");
		query.append(" ON MAC_INST.SYS_MACHINE_TYPE_ID = PROD_MAC_EQUIP.SYS_MACHINE_TYPE_ID WHERE PROD_MAC_EQUIP.SYS_PRODUCT_ID = ? AND MAC_INST.ACTIVE_CD=1 AND ROWNUM=1");
		return query;
	}
	
	// Added for Insert into OM_SHIPMENT - Starts
	
	public String updateOmShipmentStatusQuery(){
		StringBuilder query = new StringBuilder();
		query.append("UPDATE OM_SHIPMENT SET SHIPMENT_STATUS = ?, ");
		query.append(" UPDATE_USER_ID = ?, UPDATE_DTTM = ?  WHERE SYS_ORDER_ID = ? AND  ORDER_PLACED_DTTM = ?");
		return query.toString();
	}
	
	public String getProcessingType(){
		StringBuilder query = new StringBuilder();
		query.append(" SELECT PROCESSING_TYPE_CD FROM OM_ORDER_ATTRIBUTE WHERE SYS_ORDER_ID = ? AND  ORDER_PLACED_DTTM = ?");
		return query.toString();
	}
	
	public String checkRecordPresent(){
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(*) FROM  OM_SHIPMENT WHERE SYS_ORDER_ID = ? AND  ORDER_PLACED_DTTM = ?");
		return query.toString();
	}
	
	
	public String getEstDTM(){
		StringBuilder query = new StringBuilder();
		query.append(" SELECT ORD.PROMISE_DELIVERY_DTTM FROM OM_ORDER ORD JOIN OM_LOCATION LOC ON ORD.SYS_OWNING_LOC_ID=LOC.SYS_LOCATION_ID WHERE ORD.ORDER_NBR= ? AND LOC.LOCATION_NBR = ?");
		return query.toString();
	}
	// Added for Insert into OM_SHIPMENT - Ends
	
}
