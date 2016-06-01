package com.walgreens.omni.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.walgreens.omni.bean.SilverRecoveryHeaderDetails;

@SuppressWarnings("rawtypes")
public class SilverRecoveryHeaderDetailsRowMapper implements RowMapper {

	@Override
	public SilverRecoveryHeaderDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SilverRecoveryHeaderDetails silverRecoveryHeaderDetails = new SilverRecoveryHeaderDetails();
		
		silverRecoveryHeaderDetails.setCanisterStartDttm(rs.getTimestamp("CANISTER_START_DTTM"));
		silverRecoveryHeaderDetails.setCanisterEndDttm(rs.getTimestamp("CANISTER_END_DTTM"));
		silverRecoveryHeaderDetails.setSysLocationId(rs.getInt("SYS_LOCATION_ID"));
		silverRecoveryHeaderDetails.setSilverRecvPrints(rs.getDouble("SILVER_RECV_PRINTS"));
		silverRecoveryHeaderDetails.setSilverRecvRolls(rs.getDouble("SILVER_RECV_ROLLS"));
		silverRecoveryHeaderDetails.setCanisterStatus(rs.getString("CANISTER_STATUS"));
		silverRecoveryHeaderDetails.setPrintInSqInch(rs.getInt("PRINTS_IN_SQ_INCH"));
		silverRecoveryHeaderDetails.setPrintsCount(rs.getInt("PRINTS_COUNT"));
		silverRecoveryHeaderDetails.setRollsCount(rs.getInt("ROLLS_COUNT"));
		silverRecoveryHeaderDetails.setSilverCompany(rs.getString("SILVER_COMPANY"));
		silverRecoveryHeaderDetails.setCreateUserId(rs.getString("CREATE_USER_ID"));
		silverRecoveryHeaderDetails.setCreateDttm(rs.getTimestamp("CREATE_DTTM"));
		silverRecoveryHeaderDetails.setUpdateDttm(rs.getTimestamp("UPDATE_DTTM"));
		silverRecoveryHeaderDetails.setUpdateUserId(rs.getString("UPDATE_USER_ID"));
		
		return silverRecoveryHeaderDetails;
	}

}
