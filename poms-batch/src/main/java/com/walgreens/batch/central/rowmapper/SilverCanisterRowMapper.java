package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.walgreens.batch.central.bean.SilverCanisterDataBean;

/**
 * Rowmapper - generates SilverCanisterDataBean by executing SilverCanisterQuery.populateSilvCanDBQuery()
 * @author Cognizant
 *
 */
public class SilverCanisterRowMapper implements RowMapper<SilverCanisterDataBean>{

	private static final Logger log = LoggerFactory
			.getLogger(SilverCanisterRowMapper.class);
	
	/* 
	 * The below overridden method is used to fetch the values from the database and map into SilverCanisterDataBean respectively
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public SilverCanisterDataBean mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		log.debug("Entering into SilverCanisterRowMapper:mapRow()");
		SilverCanisterDataBean silverCanisterDB=new SilverCanisterDataBean();
		silverCanisterDB.setOrderNbr(rs.getString("ORDER_NBR"));
		silverCanisterDB.setServiceCatCode(rs.getString("SERVICE_CATEGORY_CODE"));
		silverCanisterDB.setSysOrderId(rs.getString("SYS_ORDER_ID"));
		silverCanisterDB.setSysProdId(rs.getString("SYS_PRODUCT_ID"));
		silverCanisterDB.setSysProdDimId(rs.getString("SYS_PRODUCT_DIMENSION_ID"));
		silverCanisterDB.setLength(rs.getString("LENGTH"));
		silverCanisterDB.setWidth(rs.getString("WIDTH"));
		silverCanisterDB.setSysMacInstId(rs.getInt("SYS_MACHINE_INSTANCE_ID"));
		silverCanisterDB.setSysMacTypeId(rs.getInt("SYS_MACHINE_TYPE_ID"));
		silverCanisterDB.setTotalPrintsFactor(rs.getString("TOTAL_PRINTS_FACTOR"));
		silverCanisterDB.setWastedQty(rs.getInt("WASTED_QTY"));
		silverCanisterDB.setRollFactor(rs.getFloat("ROLL_FACTOR"));
		silverCanisterDB.setRollCount(rs.getInt("ROLL_COUNT"));
		silverCanisterDB.setPrintCount(rs.getInt("PRINT_COUNT"));
		silverCanisterDB.setPrintInSqInchs(rs.getInt("PRINT_IN_SQ_INCHES"));
		silverCanisterDB.setSilverRecvRolls(rs.getInt("SILVER_RECV_ROLLS"));
		silverCanisterDB.setSilverRecvPrints(rs.getInt("SILVER_RECV_PRINTS"));
		silverCanisterDB.setCanisterStatus(rs.getString("CANISTER_STATUS"));
		silverCanisterDB.setLocationNo(rs.getInt("LOCATION_NBR"));
		silverCanisterDB.setLocationType(rs.getString("LOCATION_TYPE"));
		silverCanisterDB.setCanisterCalcDate(rs.getTimestamp("CANISTER_CALC_DATE"));	
		log.debug("Exiting from SilverCanisterRowMapper:mapRow()");
		return silverCanisterDB;
	}

}
