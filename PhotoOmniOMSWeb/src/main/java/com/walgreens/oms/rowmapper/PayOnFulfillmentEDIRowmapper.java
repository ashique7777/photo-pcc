package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.json.bean.PayOnFulfillmentRespData;

public class  PayOnFulfillmentEDIRowmapper implements RowMapper<PayOnFulfillmentRespData> {
	private boolean isNonEdi;
	
	public PayOnFulfillmentEDIRowmapper(boolean isNonEdi){
	  this.isNonEdi= isNonEdi;	
	}
	
	public PayOnFulfillmentRespData mapRow(ResultSet rs, int rowNum) throws SQLException {
		PayOnFulfillmentRespData payOnFulfil = new PayOnFulfillmentRespData();
		
		if(isNonEdi){
			payOnFulfil.setTotalRecord(rs.getInt("TOTAL_RECORD"));
			payOnFulfil.setSerialNumber(rs.getInt("Row_no"));
			payOnFulfil.setQuantity(rs.getInt("QUANTITY"));
		}else{
				
			//payOnFulfil.setAsnRecievedDate(rs.getString("ASN_RECEIVED_DTTM"));
			payOnFulfil.setAsnRecievedDate(!CommonUtil.isNull(rs.getString("ASN_RECEIVED_DTTM")) ? rs.getString("ASN_RECEIVED_DTTM") : "");
			payOnFulfil.setEdiTransferDate(!CommonUtil.isNull(rs.getString("EDI_TRANSFER_DATE")) ? rs.getString("EDI_TRANSFER_DATE") : "");
			//payOnFulfil.setEdiTransferDate(rs.getString("EDI_TRANSFER_DATE"));
			payOnFulfil.setVendorNbr(rs.getString("VENDOR_NAME"));
			payOnFulfil.setRetailPrice(rs.getDouble("RETAIL_PRICE"));
		}
		payOnFulfil.setStoreNumber(rs.getString("STORE_NUMBER"));
		payOnFulfil.setEnvelopeNumber(rs.getString("ENVELOPE_NUMBER"));
		payOnFulfil.setCost(rs.getDouble("VENDOR_TOTAL_COST"));
		payOnFulfil.seteDIupc(rs.getString("EDI_UPC"));	
		
	       
		payOnFulfil.setReportingDate(!CommonUtil.isNull(rs.getDate("REPORTING_DTTM")) ? rs.getString("REPORTING_DTTM") : "");
		payOnFulfil.setDoneDate(!CommonUtil.isNull(rs.getDate("DONE_DATE")) ? rs.getString("DONE_DATE"): "");
	//	
		
		return payOnFulfil;
	}
}
