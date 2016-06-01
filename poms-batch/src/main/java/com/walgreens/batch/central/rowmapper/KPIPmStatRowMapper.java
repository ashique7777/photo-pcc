package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class KPIPmStatRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("FINAL_PRICE", rs.getDouble("FINAL_PRICE"));
		map.put("EARNED_AMOUNT", rs.getDouble("EARNED_AMOUNT"));
		map.put("LOCATION_NBR", rs.getString("LOCATION_NBR"));
		map.put("SOLD_DTTM", rs.getString("SOLD_DTTM"));
		return map;
	}

}
