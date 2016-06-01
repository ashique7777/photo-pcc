package com.walgreens.batch.central.dao;

import java.sql.Timestamp;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.utility.MachineDowntimeQuery;


public class MachineDowntimeDAOImpl {

	private static DataSource dataSource;
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	/**
	 * @param dataSource
	 *            the dataSource to set
	 *//*
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}*/
	public int checkMachineHistryRecordExists(Timestamp start_date,
			String machine_downtime_id) {
		String checkMachineHistryRecordExists = MachineDowntimeQuery.queryToCheckMachineHistryData();
		//jdbcTemplate = new JdbcTemplate(dataSource);
		int count = 0;
		try {
			count = jdbcTemplate.queryForObject(checkMachineHistryRecordExists,
					new Object[] { machine_downtime_id,start_date }, Integer.class);
			//count = jdbcTemplate.queryForInt(checkMachineHistryRecordExists, new Object[]{machine_downtime_id,start_date });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
}
