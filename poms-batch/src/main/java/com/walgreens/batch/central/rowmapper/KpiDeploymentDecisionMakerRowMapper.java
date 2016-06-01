package com.walgreens.batch.central.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

public class KpiDeploymentDecisionMakerRowMapper implements
		RowMapper<Map<String, Object>> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(KpiDeploymentDecisionMakerRowMapper.class);

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map.put("PROPERTY_VALUE", rs.getString("PROPERTY_VALUE"));
		} catch (Exception e) {
			LOGGER.error("Error occured while setting value to map in the KpiDeploymentDecisionMakerRowMapper.class ---> "
					+ e);
		}

		return map;
	}

}