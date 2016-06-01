package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;

public class KPISameDayStatRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PHALPTOH", rs.getDouble("PHALPTOH"));
		map.put("PHALCTOH", rs.getDouble("PHALCTOH"));
		map.put("PHALPTOH_SIZE", rs.getDouble("PHALPTOH_SIZE"));
		map.put("STORE_NBR", rs.getString("STORE_NBR"));
		map.put("DATE_TIME", rs.getString("DATE_TIME"));
		return map;
	}
}