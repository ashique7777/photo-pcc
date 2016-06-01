package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.json.bean.PayonFulfillmentData;

public class PayOnfullFillmentRowmapper implements RowMapper<PayonFulfillmentData> {
 
	
	public PayonFulfillmentData mapRow(ResultSet  rs, int rowNum) throws SQLException {
		PayonFulfillmentData payOnFulfill = new PayonFulfillmentData();
		payOnFulfill.setTransmittedDate(rs.getString("EDI_TRANSFER_DATE"));
		payOnFulfill.setVendorCost(rs.getDouble("VENDOR_PAYMENT_AMOUNT"));
		payOnFulfill.setVendorName(rs.getString("VENDOR_NAME"));
		payOnFulfill.setReportingDate(rs.getString("REPORTING_DTTM"));
		payOnFulfill.setEnvelopeNumber(rs.getLong("ENVELOPE_NUMBER"));
		payOnFulfill.setDateDone(rs.getString("COMPLETED_DTTM"));
		payOnFulfill.setCalculateRetailPrice(rs.getDouble("CALCULATED_PRICE"));
		return payOnFulfill;
	}
}
