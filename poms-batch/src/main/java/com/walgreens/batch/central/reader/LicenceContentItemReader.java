/**
 * 
 */
package com.walgreens.batch.central.reader;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.LicenceContentDetails;
import com.walgreens.batch.central.dao.LicenceContentDAO;

/**
 * @author mahakudajy
 * @param <T>
 *
 */
public class LicenceContentItemReader implements ItemReader<LicenceContentDetails> {
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public LicenceContentDetails read() throws Exception, UnexpectedInputException, ParseException,
			NonTransientResourceException {
		LicenceContentDetails licContentDetails = new LicenceContentDetails();		

		// Calling a DAO object to get records
		LicenceContentDAO   licContentDao = new LicenceContentDAO();
		licContentDetails = licContentDao.getRecords();
		
		return licContentDetails;
	}

}
