package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.common.utility.CommonUtil;
import com.walgreens.oms.bean.LateEnvelopeBean;

public class LateEnvelopeRowmapper implements RowMapper<LateEnvelopeBean> {

	@Override
	public LateEnvelopeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		LateEnvelopeBean envelopeBean = new LateEnvelopeBean();
		envelopeBean.setOrderId(rs.getString("SYS_ORDER_ID"));
		envelopeBean.setEnvelopeNumber(CommonUtil.bigDecimalToLong(rs.getString("ENVELOPE_NUMBER")));
		envelopeBean.setOrderOriginType(rs.getString("ORDER_ORIGIN_TYPE"));
		envelopeBean.setProcessingTypeCD(rs.getString("PROCESSING_TYPE_CD")); 
		envelopeBean.setStatus(rs.getString("STATUS"));
		
		envelopeBean.setTimeSubmitted(rs.getString("ORDER_PLACED_DTTM"));
		envelopeBean.setTimePromised(rs.getString("PROMISE_DELIVERY_DTTM")); 
		envelopeBean.setTimeDone(rs.getString("ORDER_COMPLETED_DTTM"));
		envelopeBean.setEmployeeTookOrder(rs.getString("LAST_NAME")+ " , "+rs.getString("FIRST_NAME"));
		envelopeBean.setTotalRows(rs.getInt("TotalRows"));
		return envelopeBean;
	}

}
