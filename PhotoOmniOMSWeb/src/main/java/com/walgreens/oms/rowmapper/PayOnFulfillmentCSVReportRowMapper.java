/**
 * 
 */
package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.common.utility.DateUtil;
import com.walgreens.oms.bean.PayOnFulfillmentCSVRespData;


/**
 * @author CTS
 * 
 *
 */
public class PayOnFulfillmentCSVReportRowMapper implements RowMapper<PayOnFulfillmentCSVRespData>{

	@Override
	public PayOnFulfillmentCSVRespData mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PayOnFulfillmentCSVRespData csvRespdata = new PayOnFulfillmentCSVRespData();
		csvRespdata.setTotalRecord(rs.getInt("TOTAL_RECORD"));
		csvRespdata.setSerialNumber(rs.getInt("Row_no"));
		csvRespdata.setStoreNumber(rs.getString("STORE_NUMBER"));
		csvRespdata.setEnvelopeNumber(rs.getString("ENVELOPE_NUMBER"));
		csvRespdata.setCost(rs.getDouble("VENDOR_TOTAL_COST"));
		csvRespdata.seteDIupc(rs.getString("EDI_UPC"));		
		csvRespdata.setQuantity(rs.getInt("QUANTITY"));
		csvRespdata.setDoneDate(!CommonUtil.isNull(rs.getDate("DONE_DATE")) ? DateUtil.getUSDateFormat(rs.getDate("DONE_DATE").getTime()): "");
		csvRespdata.setReportingDate(!CommonUtil.isNull(rs.getDate("REPORTING_DTTM")) ? DateUtil.getUSDateFormat(rs.getDate("REPORTING_DTTM").getTime()) : "");
		return csvRespdata;
	}

}
