/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.POFOrderVCRepBean;

/**
 * @author CTS
 *
 */
public class POFOrderVCRepRowmapper implements RowMapper<POFOrderVCRepBean>{
//done
	@Override
	public POFOrderVCRepBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		POFOrderVCRepBean  pofVCRepBean = new POFOrderVCRepBean();
		pofVCRepBean.setOriginalOrderPlcDttm(rs.getTimestamp("Order_Plcd_Dttm"));
		
		pofVCRepBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		pofVCRepBean.setSysPayOnOrderVCId(rs.getLong("SYS_PAY_ON_ORDER_VC_ID"));
		/*pofVCRepBean.setLocId(rs.getLong("LOCATION_ID"));
		pofVCRepBean.setOrderNo(rs.getString("ORDER_NUMBER"));
		pofVCRepBean.setEnvNo(rs.getInt("ENVELOPE_NUMBER"));
		pofVCRepBean.setProdId(rs.getLong("PRODUCT_ID"));
		pofVCRepBean.setEdiUpc(rs.getString("EDI_UPC"));*/
		pofVCRepBean.setQuantity(rs.getInt("QUANTITY"));
	//	pofVCRepBean.setItemCost(rs.getDouble("ITEM_COST"));
		pofVCRepBean.setSoldAmount(rs.getDouble("ITEM_SOLD_AMOUNT"));
		/*pofVCRepBean.setVendorId(rs.getLong("VENDOR_ID"));
		pofVCRepBean.setAsnRcvDate(rs.getDate("ASN_RECEIVED_DTTM"));
		pofVCRepBean.setCompletedDttm(rs.getDate("COMPLETED_DTTM"));
		pofVCRepBean.setSoldDttm(rs.getDate("SOLD_DTTM"));
		pofVCRepBean.setReportingDttm(rs.getDate("REPORTING_DTTM"));*/
		pofVCRepBean.setVendPaymentAmt(rs.getDouble("VENDOR_PAYMENT_AMOUNT"));
		pofVCRepBean.setCalculatedPrice(rs.getDouble("CALCULATED_PRICE_RETAIL"));
		pofVCRepBean.setCalcVendPayment(rs.getDouble("CENTRAL_CALCULATED_VEND_PMT"));
	//	pofVCRepBean.setPrevVendPaymentAmt(rs.getDouble("OLD_VENDOR_ITEM_PAYMENT_AMT"));
		/*pofVCRepBean.setStatusInd(rs.getString("STATUS_CD"));
		
		pofVCRepBean.setApproveBy(rs.getString("APPROVED_BY"));
		pofVCRepBean.setApproveDttm(rs.getDate("APPROVED_DTTM"));
		pofVCRepBean.setEdiTransferDt(rs.getDate("EDI_TRANSFER_DATE"));
		pofVCRepBean.setEmailSendInd(rs.getString("EMAIL_SENT_CD"));
		pofVCRepBean.setUpdatedUserId(rs.getString("CREATE_USER_ID"));
		pofVCRepBean.setCreateDttm(rs.getDate("CREATE_DTTM"));
		pofVCRepBean.setUpdatedUserId(rs.getString("UPDATE_USER_ID"));
		pofVCRepBean.setUpdatedDttm(rs.getDate("UPDATE_DTTM"));*/
		
		pofVCRepBean.setCostThresoldCap(rs.getInt("COST_THRESOLD_CAP"));
		pofVCRepBean.setRecalculatedCostCD(rs.getInt("RECALCULATE_COST_CD"));
		pofVCRepBean.setVendCost(rs.getDouble("VEND_COST"));
		pofVCRepBean.setVendCostType(rs.getString("VEND_COST_TYPE"));
		pofVCRepBean.setVendShippingCost(rs.getDouble("VEND_SHIPPING_COST"));
		pofVCRepBean.setVendShippingCostType(rs.getString("SHIPPING_COST_TYPE"));
		
		return pofVCRepBean;
	}

}
