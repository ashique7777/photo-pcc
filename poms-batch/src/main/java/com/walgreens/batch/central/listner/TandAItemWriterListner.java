package com.walgreens.batch.central.listner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.walgreens.batch.central.bean.TADataBean;

/**
 * The overridden afterWrite method of this class is updating time attendance
 * indicator of om_order_pm to I for each order id
 * 
 * @author Cognizant
 * 
 */
public class TandAItemWriterListner implements ItemWriteListener<TADataBean> {

	private static final Logger log = LoggerFactory
			.getLogger(TandAItemWriterListner.class);

	private JdbcTemplate jdbcTemplate;

	/**
	 * This method returns the value of the jdbcTemplate attribute
	 * 
	 * @return JDBC template object
	 */
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	/**
	 * This method sets the value of the jdbcTemplate attribute
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.core.ItemWriteListener#beforeWrite(java.util
	 * .List)
	 */
	@Override
	public void beforeWrite(List<? extends TADataBean> items) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.core.ItemWriteListener#afterWrite(java.util
	 * .List)
	 */
	@Override
	public void afterWrite(List<? extends TADataBean> items) {
		if (log.isDebugEnabled())
			log.debug("Entering into TandAItemWriterListner:afterWrite()");
		try {
			for (TADataBean taDataBean : items) {
				int updated = jdbcTemplate
						.update("update om_order_pm set time_attendance_cd = 'I' , REPORT_SENT_DTTM = SYSDATE where sys_order_id = "
								+ taDataBean.getOrderId());
				log.info("updated    " + updated);
			}
		} 
		catch (EmptyResultDataAccessException ex) {
			log.error(
					"Exception occurred in TandAItemWriterListner:afterWrite() while updating om_order_pm    ",
					ex);
		}
		catch (InvalidResultSetAccessException ex) {
			log.error(
					"Exception occurred in TandAItemWriterListner:afterWrite() while updating om_order_pm    ",
					ex);
		}
		catch (DataAccessException ex) {
			log.error(
					"Exception occurred in TandAItemWriterListner:afterWrite() while updating om_order_pm    ",
					ex);
		}		
		if (log.isDebugEnabled())
			log.debug("Exiting from TandAItemWriterListner:afterWrite()");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.batch.core.ItemWriteListener#onWriteError(java.lang
	 * .Exception, java.util.List)
	 */
	@Override
	public void onWriteError(Exception exception,
			List<? extends TADataBean> items) {
	}

}
