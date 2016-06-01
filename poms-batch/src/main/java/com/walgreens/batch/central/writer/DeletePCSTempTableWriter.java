package com.walgreens.batch.central.writer;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import com.walgreens.batch.central.utility.ReportsQuery;
import com.walgreens.common.exception.PhotoOmniException;


public class DeletePCSTempTableWriter implements ItemWriter<String>{

	/**
	 * LOGGER for Logging
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DeletePCSTempTableWriter.class);
	@Autowired
	@Qualifier("omniJdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public void write(List<? extends String> items) throws Exception {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" Entering into DeletePCSTempTableWriter.write() ");
		}
		try{
			String strDate = items.get(0),
					strdeleteHeaderQuery = ReportsQuery.deletPromoHederTempTableData().toString(),
					strdeleteCouponQuery = ReportsQuery.deletPromoCouponTempTableData().toString(),
					strdeleteStoreQuery = ReportsQuery.deletPromoStoreTempTableData().toString();

			jdbcTemplate.update(strdeleteHeaderQuery, new Object[] {strDate});
			jdbcTemplate.update(strdeleteCouponQuery, new Object[] {strDate});
			jdbcTemplate.update(strdeleteStoreQuery, new Object[] {strDate});

		}
		catch(DataAccessException e){
			LOGGER.error(" Error occoured at DeletePCSTempTableWriter.write() ----> " + e);
		}catch (Exception e) {
			LOGGER.error(" Error occoured at DeletePCSTempTableWriter.write() ----> " + e);
			throw new PhotoOmniException(e);
		} finally {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(" Exiting Form DeletePCSTempTableWriter.write() ");
			}
		}
	}
}