package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.oms.bean.PosTransactionDetailBean;

public class PosTransactionDetailBeanRowMapper implements RowMapper<PosTransactionDetailBean> {

	@Override
	public PosTransactionDetailBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		
		PosTransactionDetailBean posTransactionDetailBean = new PosTransactionDetailBean();
		posTransactionDetailBean.setSysStorePosId(rs.getLong("SYS_STORE_POS_ID"));
		posTransactionDetailBean.setPosTransactionType(rs.getString("TRANSACTION_TYPE_CD"));
		
		return posTransactionDetailBean;
	}

}
