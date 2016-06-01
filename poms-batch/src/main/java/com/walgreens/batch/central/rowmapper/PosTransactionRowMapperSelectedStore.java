package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.PosTransactionDetails;

public class PosTransactionRowMapperSelectedStore implements RowMapper<PosTransactionDetails>{

	@Override
	public PosTransactionDetails mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		PosTransactionDetails posTransactionDetails = new PosTransactionDetails();
		
		posTransactionDetails.setBusinessDate(rs.getString("BUSINESS_DATE"));
		posTransactionDetails.setDiscountUsedInd(rs.getString("DISCOUNT_USED_CD"));
		posTransactionDetails.setEnvelopNo(rs.getInt("ENVELOPE_NBR"));
		posTransactionDetails.setSysLocationId(rs.getLong("SYS_LOCATION_ID"));
		posTransactionDetails.setSysOrderId(rs.getLong("SYS_ORDER_ID"));
		posTransactionDetails.setPosLedgerNo(rs.getInt("POS_LEDGER_NBR"));
		posTransactionDetails.setPosSequenceNo(rs.getString("POS_SEQUENCE_NBR"));
		posTransactionDetails.setProcessingInd(rs.getString("PROCESSING_CD"));
		posTransactionDetails.setRegisterNo(rs.getString("REGISTER_NBR"));
		posTransactionDetails.setReturnedQty(rs.getInt("RETURNED_QTY"));
		posTransactionDetails.setSoldAmt(rs.getDouble("SOLD_AMT"));
		posTransactionDetails.setSysStorePosId(rs.getLong("SYS_STORE_POS_ID"));
		posTransactionDetails.setTransactionTypeCd(rs.getString("TRANSACTION_TYPE_CD"));
		posTransactionDetails.setTransactionDttm(rs.getString("TRANSACTON_DTTM"));		
		posTransactionDetails.setEmployeeId(rs.getString("EMPLOYEE_ID"));
		posTransactionDetails.setLocationNbr(rs.getInt("LOCATION_NBR"));

		return posTransactionDetails;
	}

}
