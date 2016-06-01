package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.SilverCanisterDetailsBean;

public class SilverRecoveryDetailBeanRowMapper implements RowMapper<SilverCanisterDetailsBean>{

	@Override
	public SilverCanisterDetailsBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		SilverCanisterDetailsBean silverCanisterDetailsBean = new SilverCanisterDetailsBean();
		silverCanisterDetailsBean.setPaperSquereInch(rs.getInt("PRINTS_IN_SQ_INCH"));
		silverCanisterDetailsBean.setPrintsCount(rs.getInt("PRINTS_COUNT"));
		silverCanisterDetailsBean.setRollsCount(rs.getInt("ROLLS_COUNT"));
		silverCanisterDetailsBean.setSilverContentPrints(rs.getDouble("SILVER_RECV_PRINTS"));
		silverCanisterDetailsBean.setSilverContentRolls(rs.getDouble("SILVER_RECV_ROLLS"));
		return silverCanisterDetailsBean;
	}

}
