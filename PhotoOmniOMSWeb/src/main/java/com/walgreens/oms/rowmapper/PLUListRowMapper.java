package com.walgreens.oms.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class PLUListRowMapper implements RowMapper<String>{

	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		return rs.getString("PLU_NBR");
	}

}
