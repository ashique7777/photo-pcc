package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class KPIOrderStatRowMapper implements RowMapper<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KPIOrderStatRowMapper.class);

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("BUSINESS_DATE", rs.getString("BUSINESS_DATE"));
			map.put("LOCATION_NBR", rs.getString("LOCATION_NBR"));
			map.put("COST", rs.getDouble("COST"));
			map.put("SOLD_AMT", rs.getDouble("SOLD_AMT"));
			map.put("ENVELOPE_NBR", rs.getInt("ENVELOPE_NBR"));
		} catch (Exception e) {
			LOGGER.error("Error occured while setting value to map in the KPIOrderStatRowMapper.class ---> "
					+ e);
		}
		return map;
	}
}