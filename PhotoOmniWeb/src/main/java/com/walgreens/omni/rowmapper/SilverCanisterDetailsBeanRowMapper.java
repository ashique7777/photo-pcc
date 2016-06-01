package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.SilverCanisterDetailsBean;

@SuppressWarnings("rawtypes")
public class SilverCanisterDetailsBeanRowMapper implements RowMapper {

	@Override
	public SilverCanisterDetailsBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SilverCanisterDetailsBean silverCanisterDetailsBean = new SilverCanisterDetailsBean();
		
		silverCanisterDetailsBean.setLastCanisterChangeDate(rs.getDate("LAST_CANISTER_CHANGE_DTTM"));
		silverCanisterDetailsBean.setPaperSquereInch(rs.getInt("PRINTS_IN_SQ_INCH"));
		silverCanisterDetailsBean.setPrintsCount(rs.getInt("PRINTS_COUNT"));
		silverCanisterDetailsBean.setRollsCount(rs.getInt("ROLLS_COUNT"));
		silverCanisterDetailsBean.setSilverContentPrints(rs.getDouble("SILVER_RECV_PRINTS"));
		silverCanisterDetailsBean.setSilverContentRolls(rs.getDouble("SILVER_RECV_ROLLS"));
		silverCanisterDetailsBean.setStore(rs.getInt("LOCATION_NBR"));
		silverCanisterDetailsBean.setTotalSilver(rs.getDouble("SILVER_RECV_PRINTS"));
		silverCanisterDetailsBean.setTotalRowCnt(rs.getInt("TOTAL_ROWS"));
		
		return silverCanisterDetailsBean;
	}

}
