/**
 * 
 */
package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.PayOnFulfillmentLWU;



/**
 * @author CTS
 *
 */
public class PayOnFulfillmentDataRowmapper implements RowMapper<PayOnFulfillmentLWU>{

	@Override
	public PayOnFulfillmentLWU mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PayOnFulfillmentLWU pofLWUBean = new PayOnFulfillmentLWU();
		pofLWUBean.setOrderId(rs.getLong("ORDER_ID"));
		pofLWUBean.setProductId(rs.getInt("PRODUCT_ID"));
		pofLWUBean.setOrderPlacedDttm(rs.getString("ORDER_PLACED_DTTM"));
		pofLWUBean.setOriginalOrderPlacedDttm(rs.getTimestamp("ORDER_PLCD_DTTM"));
		
		pofLWUBean.setOrderNumber(rs.getString("ORDER_NUMBER"));
		pofLWUBean.setEnvelopeNo(rs.getString("ENVELOPE_NUMBER"));
		pofLWUBean.setVendorId(rs.getLong("VENDOR_ID"));
		pofLWUBean.setLocationId(rs.getLong("LOCATION_ID"));
		pofLWUBean.setCompletedDttm(rs.getDate("COMPLETED_DTTM"));
		pofLWUBean.setSoldDttm(rs.getDate("SOLD_DTTM"));
		pofLWUBean.setAsnRecieveDttm(rs.getDate("ASN_RECEIVED_DTTM"));
		pofLWUBean.setStoreNumber(rs.getInt("STORE_NUMBER"));
		
		
		pofLWUBean.setVendPaymentAmt(rs.getDouble("VENDOR_PAYMENT_AMOUNT"));
		pofLWUBean.setCalculatedPrice(rs.getDouble("CALCULATED_PRICE"));
		pofLWUBean.setQuantity(rs.getInt("QUANTITY"));
		pofLWUBean.setItemSoldAmount(rs.getDouble("ITEM_SOLD_AMOUNT"));
		pofLWUBean.setVendorType(rs.getString("VENDOR_TYPE"));
		
		return pofLWUBean;
	}

}
